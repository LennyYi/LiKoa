package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.housekeeping.dao.ModuleDAO;
import com.aiait.eflow.housekeeping.dao.RoleDAO;
import com.aiait.eflow.housekeeping.vo.RoleVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;


public class RoleAction extends DispatchAction {
	
	/**
	 * to show the edit member of role
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation editRoleMember(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		String roleId = (String)request.getParameter("roleId");
		try {
			RoleDAO dao = new RoleDAO(dbManager);
            Collection sourceStaffList = dao.getSourceStaff();
            Collection roleStaffList = dao.getMemberByRole(roleId);
            request.setAttribute("sourceStaffList",sourceStaffList);
            request.setAttribute("roleStaffList",roleStaffList);
		} catch (DAOException e) {
			e.printStackTrace();
			return mapping.findActionLocation("fail");
		} finally {
			dbManager.freeConnection();
		}
	
		return mapping.findActionLocation("editRoleMemberPage");
	}
	
	/**
	 * to save the members of role
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveRoleMember(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		String staffList = (String)request.getParameter("staffList");
		String roleId = (String)request.getParameter("roleId");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		try {
		    dbManager = DBManagerFactory.getDBManager();
			RoleDAO dao = new RoleDAO(dbManager);
			dbManager.startTransaction();
			
			long oldHistoryId = dao.getRoleMemberMaxHistoryId();
		  	int result = dao.saveRoleMemberOldHistory(oldHistoryId,roleId);
		  	long newHistoryId = oldHistoryId+1;
			
			dao.deleteRoleMember(roleId);
            if(staffList!=null && !"".equals(staffList)){
            	String[] staffCode = split(staffList,",");
            	for(int i=0;i<staffCode.length;i++){
            		dao.saveRoleMember(roleId,staffCode[i]);
            		dao.saveRoleMemberNewHistory(newHistoryId,roleId,staffCode[i]);
            	}
            }
            if(result<1){
            	oldHistoryId = -1;
            }
            dao.saveRoleMemberLog(staff.getStaffCode(),roleId,oldHistoryId,newHistoryId);
            dbManager.commit();
            out.print("success");
		} catch (DAOException e) {
			out.print("fail");
			dbManager.rollback();
			e.printStackTrace();
			return mapping.findActionLocation("fail");
		} finally {
			if(out!=null) out.close();
			if (dbManager != null) {
			    dbManager.freeConnection();
			}
		}
		return null;
	}
	
	/**
	 * to show the edit role page
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation editRole(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		String saveType = (String)request.getParameter("saveType");
		if(!"new".equals(saveType)){
		  String roleId = (String)request.getParameter("roleId");
		  try {
			RoleDAO dao = new RoleDAO(dbManager);
			RoleVO role = dao.getRole(roleId);
			request.setAttribute("role", role);
		  } catch (DAOException e) {
			e.printStackTrace();
			return mapping.findActionLocation("fail");
		  } finally {
			dbManager.freeConnection();
		  }
		}
		return mapping.findActionLocation("editRolePage");
	}
	
	/**
	 * save Role
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveRole(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		String saveType = (String)request.getParameter("saveType");
		RoleVO role = new RoleVO();
		role.setRoleId((String)request.getParameter("roleId"));
		role.setRoleName((String)request.getParameter("roleName"));
		role.setRemark((String)request.getParameter("remark"));
		PrintWriter out = response.getWriter();
		try {
			RoleDAO dao = new RoleDAO(dbManager);
			if(!"new".equals(saveType)){
			  dao.update(role);
			}else{
			  RoleVO temp = dao.getRole(role.getRoleId());
			  if(temp!=null){
				out.print("Role Id ("+role.getRoleId()+") already exists!");
				return null;
			 }
			  dao.save(role);	
			}
			out.print("success");
		  } catch (DAOException e) {
			e.printStackTrace();
			out.print(e.getMessage());
			//return mapping.findActionLocation("fail");
		  } finally {
			if(out!=null) out.close();  
			if(dbManager!=null) dbManager.freeConnection();
		  }
		return null;
		//return mapping.findActionLocation("toShowRolePurview");
	}	
	
	/**
	 * delete Role
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteRole(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		String[] roleIds = request.getParameterValues("roleId");

		try {
			RoleDAO dao = new RoleDAO(dbManager);
			dbManager.startTransaction();
			for(int i=0;i<roleIds.length;i++){
				RoleVO role = new RoleVO();
				role.setRoleId(roleIds[i]);
				dao.delete(role);
				dao.deleteRolePurview(roleIds[i]);
				dao.deleteRoleMember(roleIds[i]);
			}
			dbManager.commit();
		  } catch (DAOException e) {
			dbManager.rollback();
			e.printStackTrace();
			return mapping.findActionLocation("fail");
		  } finally {
			dbManager.freeConnection();
		  }
		return mapping.findActionLocation("toShowRolePurview");
	}
	
	/**
	 * search the role list to show
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listRole(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		try {
			RoleDAO dao = new RoleDAO(dbManager);
			Collection list = dao.getAllRole();
			request.setAttribute("roleList", list);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute(
				"fatalerror",
				"During search Role list happen error ,please contact Admin!");
			return mapping.findActionLocation("fail");
		} finally {
			dbManager.freeConnection();
		}
		return mapping.findActionLocation("listRole");
	}

	public ActionLocation preModifyRolePurview(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		String roleId = (String)request.getParameter("roleId");
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		try {
			RoleDAO dao = new RoleDAO(dbManager);
			ModuleDAO moduleDao = new ModuleDAO(dbManager);
			Collection rolePurviewList = dao.getFunctionsByRole(roleId);
			Collection moduleList = moduleDao.getModuleAndOperateList();
			request.setAttribute("rolePurviewList",rolePurviewList);
			request.setAttribute("moduleList",moduleList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute(
				"fatalerror",
				"During to show Role Purview happen error ,please contact Admin!");
			return mapping.findActionLocation("fail");
		}finally{
			dbManager.freeConnection();
		}
		return mapping.findActionLocation("rolePurview");
	}
	
	public ActionLocation saveRolePurview(
		ModuleMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
	  String roleId = (String)request.getParameter("roleId");
	  String[] temp = request.getParameterValues("checkbox");
	  if (temp == null){
		request.setAttribute("fatalerror",
						"You have not select any module's operation!");
					return mapping.findActionLocation("fail");
	  }
	  StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	  IDBManager dbManager = null;
	  
	  try{
	      dbManager = DBManagerFactory.getDBManager();
	      RoleDAO dao = new RoleDAO(dbManager);
	      dbManager.startTransaction();
	  	//write history and log
	  	long oldHistoryId = dao.getRolePurviewMaxHistoryId();
	  	int result = dao.saveRolePurviewOldHistory(oldHistoryId,roleId);
	  	long newHistoryId = oldHistoryId+1;
	  	
	  	dao.deleteRolePurview(roleId);
	    for (int i = 0; i < temp.length; i++) {
		  String[] split = split(temp[i],CommonName.MODULE_ROLE_SPLIT_SIGN);
		  dao.saveRolePurview(roleId,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
		  dao.saveRolePurviewNewHistory(newHistoryId,roleId,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
	    }
        if(result<1){
        	oldHistoryId = -1;
        }
	    dao.saveRolePurivewLog(staff.getStaffCode(),roleId,oldHistoryId,newHistoryId);
	    dbManager.commit();
	  }catch(DAOException e){
	  	e.printStackTrace();
		request.setAttribute("fatalerror",
								"During save the role purview happen error,please contact Admin!");
	  	dbManager.rollback();
		return mapping.findActionLocation("fail");
	  }finally{
	  	dbManager.freeConnection();
	  }
	  
	  String currentRole = staff.getCurrentRoleId();
	  AuthorityHelper author = AuthorityHelper.getInstance();
	  author.refresh();
	  return mapping.findActionLocation("toShowRolePurview");
	}
	
	private static  String[] split(String string, String delim) {
		StringTokenizer token = new StringTokenizer(string, delim);
		String[] result = new String[token.countTokens()];
		List tmp = new ArrayList();
		while(token.hasMoreTokens()) {
			tmp.add(token.nextToken());
		}
		tmp.toArray(result);
		return result;
	}
}
