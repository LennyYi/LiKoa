package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.SystemOwnerDAO;
import com.aiait.eflow.housekeeping.vo.SystemOwnerVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class SystemOwnerAction extends DispatchAction {
  
	 /**
	  * entern list system owner page
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation listSystemOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listResult";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  SystemOwnerDAO dao = new SystemOwnerDAO(dbManager);
		  Collection list = dao.getSystemOwnerList();
		  request.setAttribute("resultList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * delete the selected records
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteSystemOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String[] systemId = request.getParameterValues("systemId");
		IDBManager dbManager=null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  dbManager.startTransaction();
		  SystemOwnerDAO dao = new SystemOwnerDAO(dbManager);
		  SystemOwnerVO vo = new SystemOwnerVO();
		  for(int i=0;i<systemId.length;i++){
			vo.setSystemId(systemId[i]);
			dao.delete(vo);
		  }
		  dbManager.commit();
		}catch(Exception e){
			dbManager.rollback();
			request.setAttribute("error",e.getMessage());
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	 /**
	  * enter system owner edit page 
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation editSystemOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}

		if(!"".equals(saveType)){
			String systemId = (String)request.getParameter("systemId");
			IDBManager dbManager = null;
			try{
			  dbManager =  DBManagerFactory.getDBManager();
			  SystemOwnerDAO dao = new SystemOwnerDAO(dbManager);
			  SystemOwnerVO vo = dao.getSystemOwnerVO(systemId);
			  request.setAttribute("vo",vo);
			}catch(Exception e){
				e.printStackTrace();
				returnLabel = "fail";
			}finally{
				if(dbManager!=null) dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	 /**
	  * save system owner 
	  * @param mapping
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public ActionLocation saveSystemOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		//String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
		SystemOwnerVO vo = new SystemOwnerVO();
		vo.setSystemId((String)request.getParameter("systemId"));
		vo.setSystemName((String)request.getParameter("systemName"));
		vo.setSystemStaffCode((String)request.getParameter("staffCode"));
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  SystemOwnerDAO dao = new SystemOwnerDAO(dbManager);
		  if("new".equals(saveType)){
			  int result = dao.save(vo);
			  if(result==1){
				  //request.setAttribute("error","System Id (" + vo.getSystemId() + ") had aready exist!");
				  //returnLabel = "fail";
				  out.print("System Id (" + vo.getSystemId() + ") already exists!");
				  return null;
			  }
		  }else{
			  dao.update(vo);
		  }
		  out.print("success");
		}catch(Exception e){
			e.printStackTrace();
			//returnLabel = "fail";
			out.print(e.getMessage());
		}finally{
			if(out!=null) out.close();  
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
		//return mapping.findActionLocation(returnLabel);
	}
}
