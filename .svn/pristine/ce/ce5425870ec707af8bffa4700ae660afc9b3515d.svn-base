package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class StaffAction extends DispatchAction {

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation getStaffList(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String teamCode = (String)request.getParameter("teamCode");
		String createNullFlag = (String)request.getParameter("createNullFlag"); //是否生成一个空白选项的标识。如果该参数存在，则需要
		if(createNullFlag==null){
			createNullFlag = "";
		}
	    IDBManager dbManager = null;
	    response.setContentType("text/xml;charset=GBK");
	    PrintWriter out = response.getWriter();
	    
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	   try{
		  StringBuffer responseXML = new StringBuffer("");
		  responseXML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		  responseXML.append("<staffs>");
		  if(createNullFlag!=null && !"".equals(createNullFlag)){
			  responseXML.append("<staff code=''>All</staff>");
		  }
		  dbManager =  DBManagerFactory.getDBManager();
		  StaffDAO dao = new StaffDAO(dbManager);
		  Collection staffList = null;
		  staffList = dao.getStaffListByTeam(teamCode);
		  if(staffList!=null && staffList.size()>0){
			   Iterator it = staffList.iterator();
			   while(it.hasNext()){
				   StaffVO staff = (StaffVO)it.next();
				   responseXML.append("<staff code='" + staff.getStaffCode() + "'>")
		             .append(staff.getStaffName())
		             .append("</staff>");
			   }
		  }
		  responseXML.append("</staffs>");
		  out.write(responseXML.toString());
		}catch(Exception e){
		  e.printStackTrace();	
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		if(out!=null) out.close();
		return null;
	}
}
