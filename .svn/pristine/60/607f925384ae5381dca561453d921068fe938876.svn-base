package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.housekeeping.dao.FormTypeDAO;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class FormTypeAction extends DispatchAction {
   
	public ActionLocation listFormType(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listResult";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  FormTypeDAO dao = new FormTypeDAO(dbManager);
		  Collection list = dao.getFormTypeList();
		  request.setAttribute("formTypeList",list);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error",e.getMessage());
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation preEdit(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		String typeCode = (String)request.getParameter("typeCode");
		if(saveType!=null && "new".equals(saveType)){
			return mapping.findActionLocation(returnLabel);
		}
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  FormTypeDAO dao = new FormTypeDAO(dbManager);
		  FormTypeVO vo = dao.getFormType(typeCode);
		  request.setAttribute("vo",vo);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error",e.getMessage());
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation saveFormType(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
	   IDBManager dbManager = null;
	   PrintWriter out = response.getWriter();
	   FormTypeVO vo = new FormTypeVO();
	   //if(!"new".equals(saveType)){
	   vo.setFormTypeId(URLDecoder.decode(request.getParameter("formTypeCode"),"UTF-8"));
	   //}
	   vo.setFormTypeName(URLDecoder.decode(request.getParameter("formTypeName"),"UTF-8"));
	   vo.setDescription(URLDecoder.decode(request.getParameter("description"),"UTF-8"));
	   try{
		 dbManager =  DBManagerFactory.getDBManager();
		 FormTypeDAO dao = new FormTypeDAO(dbManager);
		 if(!"new".equals(saveType)){
			 dao.update(vo);
		 }else{
			 dao.save(vo);
		 }
		 FormTypeHelper.getInstance().refresh();
		 out.print("success");
	   }catch(Exception e){
			e.printStackTrace();
			out.print("Fail to save form type : " + e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
	  return null;
	}
}
