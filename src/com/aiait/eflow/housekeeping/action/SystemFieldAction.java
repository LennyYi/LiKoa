package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.housekeeping.dao.SystemFieldDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class SystemFieldAction extends DispatchAction {
	String returnLabel = "success";
	
	/**
	 * 进入systemField列表显示页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listSystemField(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		returnLabel = "listField";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  SystemFieldDAO dao = new SystemFieldDAO(dbManager);		  
		  //StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		  Collection sysFieldList = dao.getAllField(); 
	      request.setAttribute("formList",sysFieldList);
		}catch(DAOException e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 进入编辑页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation preEdit(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		if(!"create".equals(saveType)){
			String fieldId = (String)request.getParameter("fieldId");
			IDBManager dbManager = null;
			try{
			  dbManager =  DBManagerFactory.getDBManager();
			  SystemFieldDAO dao = new SystemFieldDAO(dbManager);
			  //获取指定的system Field
			  //StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
			  SystemFieldVO field = dao.getField(fieldId);
		      request.setAttribute("systemFieldVo",field);
			}catch(DAOException e){
				e.printStackTrace();
				returnLabel = "fail";
			}finally{
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 保存systemField
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveSystemField(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		returnLabel = "saveSuccess";
		IDBManager dbManager = null;
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "create"; // default is new field
		}
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  SystemFieldDAO dao = new SystemFieldDAO(dbManager);
		  SystemFieldVO vo = new SystemFieldVO();
		  vo.setFieldId(FieldUtil.convertSafeString((String)request.getParameter("fieldId"),""));
		  vo.setFieldLabel(FieldUtil.convertSafeString((String)request.getParameter("fieldLabel"),""));
		  vo.setFieldType(FieldUtil.convertSafeString((String)request.getParameter("fieldType"),"01"));
		  
		  vo.setColumnType(FieldUtil.convertSafeInt((String)request.getParameter("columnType"),1));
		  vo.setColumnLength(FieldUtil.convertSafeInt((String)request.getParameter("columnLength"),20));
		  dao.saveOrUpate(vo,saveType);
		  
		}catch(DAOException e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
}
