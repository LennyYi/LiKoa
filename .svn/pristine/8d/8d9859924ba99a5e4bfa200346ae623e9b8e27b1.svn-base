package com.aiait.eflow.housekeeping.action;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	Project Management Action			  */
/******************************************************************/
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.vo.EflowStaffVO;
import com.aiait.eflow.housekeeping.vo.ProjectVO;
import com.aiait.eflow.housekeeping.dao.ProjectDAO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class ProjectManageAction extends DispatchAction{
		
	/**
	  * Searching Project List with query condition
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation searchProject(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listProject";
		
		String projectCode = (String)request.getParameter("projectCode");
		String projectName = (String)request.getParameter("projectName");
		String projectDesc = (String)request.getParameter("projectDesc");
		String projLdId = (String)request.getParameter("projLdId");
		String projLdName = (String)request.getParameter("projLdName");
		String projStartDate = (String)request.getParameter("projStartDate");
		String status = (String)request.getParameter("status");
		
		IDBManager dbManager = null;
		try{
			
			dbManager =  DBManagerFactory.getDBManager();
			ProjectDAO pDao = new ProjectDAO(dbManager);
			ProjectVO queryVo = new ProjectVO();
			
			if (projectCode!=null){
				queryVo.setPrj_code(projectCode);
			}
			if (projectName!=null){
				queryVo.setPrj_name(projectName);
			}
			if (projectDesc!=null){
				queryVo.setPrj_desc(projectDesc);
			}
			if (projLdId!=null){
				queryVo.setPrj_ld_id(projLdId);
			}
			if (projLdName!=null){
				queryVo.setPrj_ld_name(projLdName);
			}
			if (projStartDate!=null){
				queryVo.setPrj_start_date(projStartDate);
			}		
			if (status!=null){				
				queryVo.setStatus(status);
			}
			
			//set query condition end
			Collection projectList = pDao.getSearchProject(queryVo);
			request.setAttribute("eflowprojectList",projectList);
			
			//获取所有的Team列表
			//Collection TeamList = null;
			//TeamDAO tdao = new TeamDAO(dbManager);
			//TeamList = tdao.getMergedTeamList();
			//request.setAttribute("teamList",TeamList);
			
		}catch(DAOException e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	/**
	  * Enter edit page for creating or editing team
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation enterEditProject(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "newProjectPage";
		String type = (String)request.getParameter("type");
		if(type==null){type = "new";}		
		IDBManager dbManager = null;
		try{			
			dbManager =  DBManagerFactory.getDBManager();
			ProjectDAO dao = new ProjectDAO(dbManager);
			if (!"new".equals(type)){//编辑type="edit"		
				String projectCode = request.getParameter("projectCode");
				ProjectVO pvo = dao.getProjectByProjectCode(projectCode);
				request.setAttribute("eflowproject",pvo);
			}
			//获取所有的user列表
			Collection userList = null;
			StaffDAO sdao = new StaffDAO(dbManager);
			userList = sdao.getAllStaff();
			request.setAttribute("userList",userList);	
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
				if(dbManager!=null) dbManager.freeConnection();
		}
		
		return mapping.findActionLocation(returnLabel);
	}
	/**
	  * Save Team Information
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation saveProject(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("type");
		if(saveType==null || "".equals(saveType)){
			saveType = "new";
		}
		String projectCode = (String)request.getParameter("projectcode");
		String projectName = (String)request.getParameter("projectName");
		String projectdesc = (String)request.getParameter("projectdesc");
		String projectleader = (String)request.getParameter("projectleader");
		String beginDate = (String)request.getParameter("beginDate");
		String status = (String)request.getParameter("status");
			
		ProjectVO	pvo = new ProjectVO();
		pvo.setPrj_code(projectCode);
		pvo.setPrj_name(projectName);
		pvo.setPrj_desc(projectdesc);
		pvo.setPrj_ld_id(projectleader);
		pvo.setPrj_start_date(beginDate);
		pvo.setStatus(status);
		
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		
		try{
			dbManager =  DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			ProjectDAO dao = new ProjectDAO(dbManager);
			if("new".equals(saveType)){
				ProjectVO temp =  dao.getProjectByProjectCode(pvo.getPrj_code());
				if(temp!=null){
					out.print("Staff Code ("+pvo.getPrj_code()+") already exists!");					
				}else{
					dao.save(pvo,request);
				}
			}else{				
				dao.update(pvo,request);
				dao.saveHistory(pvo, request);
			}
			dbManager.commit();
			out.print("success");			
		}catch(Exception e){
		    dbManager.rollback();
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(out!=null) out.close(); 
			if(dbManager!=null) dbManager.freeConnection();
		}			
		return null;
	}
	/**
	 * 将指定的Project删除(实质为改变其状态为"T")
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteProjects(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "enterListProject";
		String[] projectCode = (String[])request.getParameterValues("projectcodeId");
		System.out.println("length of project------------>"+projectCode.length);
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			ProjectDAO pdao = new ProjectDAO(dbManager);			
			dbManager.startTransaction();
            for(int i=0;i<projectCode.length;i++){       	            	
            	//写历史纪录
            	ProjectVO pvo = pdao.getProjectByProjectCode(projectCode[i]);
            	pdao.saveHistory(pvo, request);
            	//删除
            	pdao.deleteProject(projectCode[i]);
            }			
			dbManager.commit();			
		}catch(Exception e){
			dbManager.rollback();
    		request.setAttribute("error",e.getMessage());
    		return mapping.findActionLocation("fail");			
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
}
