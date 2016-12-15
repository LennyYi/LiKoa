package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.DBOwnerDAO;
import com.aiait.eflow.housekeeping.vo.DBOwnerVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class DBOwnerAction extends DispatchAction {
	
	public ActionLocation listDBOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listResult";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DBOwnerDAO dao = new DBOwnerDAO(dbManager);
		  Collection list = dao.getDBOwnerList();
		  request.setAttribute("resultList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation deleteDBOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String[] DBId = request.getParameterValues("dbId");
		IDBManager dbManager=null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  dbManager.startTransaction();
		  DBOwnerDAO dao = new DBOwnerDAO(dbManager);
		  DBOwnerVO vo = new DBOwnerVO();
		  for(int i=0; i<DBId.length; i++){
			vo.setDBId(DBId[i]);
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
	
	public ActionLocation editDBOwner(
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
			String DBId = (String)request.getParameter("dbId");
			IDBManager dbManager = null;
			try{
			  dbManager =  DBManagerFactory.getDBManager();
			  DBOwnerDAO dao = new DBOwnerDAO(dbManager);
			  DBOwnerVO vo = dao.getDBOwnerVO(DBId);
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
	
	public ActionLocation saveDBOwner(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		//String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
		DBOwnerVO vo = new DBOwnerVO();
		vo.setDBId((String)request.getParameter("dbId"));
		vo.setDBName((String)request.getParameter("dbName"));
		vo.setDBStaffCode((String)request.getParameter("dbStaffCode"));
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  DBOwnerDAO dao = new DBOwnerDAO(dbManager);
		  if("new".equals(saveType)){
			  int result = dao.save(vo);
			  if(result==1){
				  //request.setAttribute("error","DB Id (" + vo.getDBId() + ") had aready exist!");
				  //returnLabel = "fail";
				  out.print("DB Id (" + vo.getDBId() + ") already exists!");
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
