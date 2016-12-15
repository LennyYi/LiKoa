package com.aiait.eflow.housekeeping.action;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT1002	Robin	04/16/2008	Base Data Manage			      */
/******************************************************************/
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.basedata.dao.BaseDataDAO;
import com.aiait.eflow.basedata.vo.BaseDataVO;
import com.aiait.eflow.common.helper.BaseDataHelper;
import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.housekeeping.vo.FormTypeVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.vo.BaseVO;

public class BaseDataManageAction extends DispatchAction {
	
	/**
	 * 进入base data 显示页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listMasterData(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPage";
		//
		Collection formTypeList = FormTypeHelper.getInstance().getFormTypeList();
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		  
		  if(formTypeList!=null && formTypeList.size()>0){
			Iterator it = formTypeList.iterator();
			while(it.hasNext()){
				FormTypeVO vo = (FormTypeVO)it.next();
				Collection masterDataList = baseDao.getMasterDataByType(vo.getFormTypeId());
				request.setAttribute(vo.getFormTypeName(),masterDataList);
			}
		  }
		  //获取公共的field集合
		  Collection list = baseDao.getMasterDataByType("00");
		  request.setAttribute("Public Fields",list);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error",e.getMessage());
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation getBaseDataList(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
		
		String masterId = (String)request.getParameter("masterId");
		if(masterId==null){
			out.print("fail");
			return null;
		}
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   Collection detailDataList = baseDao.getDetailData(Long.parseLong(masterId));
		   out.print("<table width='100%'  border='0' cellpadding='0' cellspacing='1' class=sortable id=mytable style='border:1px #8899cc solid;'>");
		   out.print("<tr class=\"liebiao_tou\">");
		   out.print("<td align='center' ><input type='checkbox' name='allBtn' onclick=\"selectAll(this,'fieldId')\"></td>");
		   out.print("<td align='center' >Option Value</td><td align='center' >Option Label</td>");
		   out.print("</tr>");
		   if(detailDataList!=null && detailDataList.size()>0){
			   Iterator it = detailDataList.iterator();
			   while(it.hasNext()){
				 BaseDataVO vo = (BaseDataVO)it.next();
				 String obsoleteTag = ""; //IT1321
				 if("T".equalsIgnoreCase(vo.getStatus())) obsoleteTag = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Obsolete)"; 
				 out.print("<tr class=\"tr_change\">");
			     out.print("<td align='center' ><input type='checkbox' name='optionValue' value='"+vo.getOptionValue()+"'></td>");
			     out.print("<td>"+vo.getOptionValue()+"&nbsp;</td>");
			     out.print("<td><a href='javascript:openCenterWindow(\""+request.getContextPath()+"/baseDataManageAction.it?method=enterUpdateOptionLabel"
			    		 +"&masterId="+vo.getMasterId()+"&optionValue="+vo.getOptionValue()+"&type=update\",350,150)'>"+vo.getOptionLabel()+obsoleteTag+"</a>&nbsp;</td>");
			     out.print("</tr>");
			   }
		   }
		   out.print("</table>");
		}catch(Exception e){
			e.printStackTrace();
			out.print("fail");
			return null;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation enterUpdateOptionLabel(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "optionLabelPage"; 
		String masterId = (String)request.getParameter("masterId");
		String optionValue = (String)request.getParameter("optionValue");
		String type = (String)request.getParameter("type");
		if(type==null){
			type = "new";
		}
		if("new".equals(type)){
			return mapping.findActionLocation(returnLabel);
		}
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   BaseDataVO vo = baseDao.getBaseDataVO(Long.parseLong(masterId),optionValue);
		   request.setAttribute("baseVo",vo);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error",e.getMessage());
			returnLabel="fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation updateOptionLabel(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String masterId = (String)request.getParameter("masterId");
		String optionValue = (String)request.getParameter("optionValue");
		String optionLabel = (String)request.getParameter("optionLabel");
		optionLabel = java.net.URLDecoder.decode(optionLabel,"UTF-8");  
		String type = (String)request.getParameter("type");
		BaseDataVO vo = new BaseDataVO();
		
		vo.setMasterId(Long.parseLong(masterId));
		vo.setOptionLabel(URLDecoder.decode(optionLabel));
		vo.setOptionValue(URLDecoder.decode(optionValue));
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   if(!"new".equals(type)){
		     baseDao.updateOptionLabel(vo);
		   }else{
			 //检查该value值不能重复
			 if(baseDao.checkOptionValue(vo)==false){
				 out.print("The Option Value(" + vo.getOptionValue() + ") has already existed in this master data!");
				 return null;
			 }
			 baseDao.saveDetailData(vo);
		   }
		   BaseDataHelper.getInstance().refresh();
		   out.print("success");
		}catch(Exception e){
			e.printStackTrace();
			out.print("fail");
			return null;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation deleteOptions(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String masterId = (String)request.getParameter("masterId");
		String[] optionValues = request.getParameterValues("optionValue");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   //首先检查该masterId是否已经关联form在使用，如果存在，则不允许进行删除操作
		   if(baseDao.checkMasterIdInForm(Long.parseLong(masterId))){
			   baseDao.setOptionsToObsolete(Long.parseLong(masterId), optionValues);
			   out.print("The master data had been used in some forms, so can't delete its options!\n Now disable the option(s) for you.");
			   BaseDataHelper.getInstance().refresh();
			   return null;
		   }
		   dbManager.startTransaction();
		   for(int i=0;i<optionValues.length;i++){
			   baseDao.deleteDetailData(Long.parseLong(masterId),optionValues[i]);
		   }
		   dbManager.commit();
		   BaseDataHelper.getInstance().refresh();
		   out.print("success");
		}catch(Exception e){
			dbManager.rollback();
		    e.printStackTrace();
		    out.print("fail");
			return null;
		}finally{
				if(dbManager!=null) dbManager.freeConnection();
		} 
		return null;
	}
	
	public ActionLocation updateMasterData(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String masterId = (String)request.getParameter("masterId");
		String masterName = (String)request.getParameter("masterName");
		String fieldType = (String)request.getParameter("fieldType");
		String type = (String)request.getParameter("type");
		BaseDataVO baseVo = new BaseDataVO();
		if(type==null){
			type = "new";
		}
		baseVo.setFieldName(masterName);
		baseVo.setFieldType(fieldType);
		if(!"new".equals(type)){
           baseVo.setMasterId(Long.parseLong(masterId));
		}
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   if("new".equals(type)){
			   baseDao.saveMaster(baseVo);
			   long currentMasterId = baseDao.getNewMasterId();
			   if(currentMasterId>-1){
				   out.print(""+currentMasterId);
			   }else{
				   out.print("fail");
			   }
		   }else{
			   baseDao.updateMaster(baseVo.getMasterId(),baseVo.getFieldName(),baseVo.getFieldType());
			   out.print("success");
		   }
		   BaseDataHelper.getInstance().refresh();
		}catch(Exception e){
		    e.printStackTrace();
		    out.print("fail");
			return null;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		} 
		return null;
	}
	
	/**
	 * IT1321 复原被标注无效的选项 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation reviveOptions(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String masterId = (String)request.getParameter("masterId");
		String[] optionValues = request.getParameterValues("optionValue");
		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   BaseDataDAO baseDao = new BaseDataDAO(dbManager);
		   baseDao.reviveObsoleteOptions(Long.parseLong(masterId), optionValues);
		   out.print("Revived the option(s) successfully.");
		   BaseDataHelper.getInstance().refresh();
		}catch(Exception e){
			dbManager.rollback();
		    e.printStackTrace();
		    out.print("fail");
			return null;
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		} 
		return null;
	}	
}
