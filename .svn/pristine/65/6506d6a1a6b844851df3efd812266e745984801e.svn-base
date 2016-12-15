package com.aiait.eflow.housekeeping.action;

/**
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*          Robin   2008-05-21   年假标准管理类
/******************************************************************/

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.AnnualLeaveStDAO;
import com.aiait.eflow.housekeeping.vo.AnnualLeaveStVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class AnnualLeaveStandardAction extends DispatchAction {
   
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listAnnualLeaveStandard(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "listAnnualLeaveStandard";
		String orgId = (String)request.getParameter("orgId");
		String serviceYearLevel = (String)request.getParameter("serviceYearLevel");
		AnnualLeaveStVO vo = new AnnualLeaveStVO();
		if(serviceYearLevel!=null && !"".equals(serviceYearLevel)){
			vo.setServiceYearLevel(Integer.parseInt(serviceYearLevel));
		}
		if(orgId!=null && !"".equals(orgId)){
			vo.setOrgId(orgId);
		}		
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  AnnualLeaveStDAO dao = new AnnualLeaveStDAO(dbManager);
		  Collection list = dao.getList(vo);
		  request.setAttribute("list",list);
		}catch(Exception e){
			e.printStackTrace();
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
		String id=(String)request.getParameter("id");
		if(id!=null && !"".equals(id)){
			IDBManager dbManager = null;
			try{
				dbManager =  DBManagerFactory.getDBManager();
				AnnualLeaveStDAO dao = new AnnualLeaveStDAO(dbManager);
				AnnualLeaveStVO vo = dao.getAnnualLeaveStVO(Integer.parseInt(id));
				String gradeIds = dao.getGradeIdsByOrgServiceYear(vo.getOrgId(),vo.getServiceYearLevel()); //已经设定过得职级集合
				request.setAttribute("vo",vo);
				request.setAttribute("existedGradeIds",gradeIds);
			}catch(Exception e){
				e.printStackTrace();
				returnLabel = "fail";
			}finally{
				if(dbManager!=null) dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation save(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "toList";
		String orgId = (String)request.getParameter("orgId");
		String[] gradeIds = request.getParameterValues("gradeId");
		String saveType = (String)request.getParameter("saveType");
		String id = (String)request.getParameter("id");
		if(saveType==null){
			saveType = "new";
		}
		String gradeId = ""; //用来保存多个gradeId,每个之间以分割符“,”分割
		if(gradeIds==null){
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,"You haven't selected any grades.");
			mapping.findActionLocation("fail");
		}
		for(int i=0;i<gradeIds.length;i++){
			gradeId = gradeId + gradeIds[i] + ",";
		}
		//去掉最后的","
		if(gradeId.indexOf(",")>-1){
			gradeId = gradeId.substring(0,gradeId.length()-1);
		}
		String serviceYearLevel = (String)request.getParameter("serviceYearLevel");
		String annualLeaveDays = (String)request.getParameter("annualLeaveDays");
		String limitTransferDays = (String)request.getParameter("limitTransferDays");
		
		String remark = (String)request.getParameter("remark");
		if(remark==null){
			remark = "";
		}
		if(serviceYearLevel==null){
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,"You haven't selected service year level.");
			mapping.findActionLocation("fail");
		}
		if(annualLeaveDays==null || "".equals(annualLeaveDays)){
			annualLeaveDays = "0";
		}
		if(limitTransferDays==null || "".equals(limitTransferDays)){
			limitTransferDays = "0";
		}
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			AnnualLeaveStDAO dao = new AnnualLeaveStDAO(dbManager);
			AnnualLeaveStVO vo = new AnnualLeaveStVO();
			//dbManager.startTransaction();
			vo.setOrgId(orgId);
			vo.setServiceYearLevel(Integer.parseInt(serviceYearLevel));
			vo.setAnnualLeaveDays(Double.parseDouble(annualLeaveDays));
			vo.setRemark(remark);
			vo.setGradeId(gradeId);
			vo.setLimitTransferDays(Double.parseDouble(limitTransferDays));
			if("new".equals(saveType)){
			    dao.save(vo);
			}else{
				vo.setId(Integer.parseInt(id));
				dao.update(vo);
			}
	 	    //dbManager.commit();
		}catch(Exception e){
			//dbManager.rollback();
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation delete(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "toList";
		String[] ids = request.getParameterValues("id");
		IDBManager dbManager = null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			AnnualLeaveStDAO dao = new AnnualLeaveStDAO(dbManager);
			AnnualLeaveStVO vo = new AnnualLeaveStVO();
			dbManager.startTransaction();
	        for(int i=0;i<ids.length;i++){
			  vo.setId(Integer.parseInt(ids[i]));
	          dao.delete(vo);
	        }
	 	    dbManager.commit();
		}catch(Exception e){
			dbManager.rollback();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,"There is one error during deleted the selected records:"
					+e.getMessage());
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	/**
	 * 动态生成可以使用的grade集合
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation getDynamicGrade(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String orgId = (String)request.getParameter("orgId");
		String serviceYearLevel = (String)request.getParameter("serviceYearLevel");
		if(serviceYearLevel==null || "".equals(serviceYearLevel)){
			serviceYearLevel = "0";
		}
		//获取某个公司某段服务年限已经设好的grade集合，然后将其设定为不可选定状态
		IDBManager dbManager = null;
		response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		try{
			dbManager =  DBManagerFactory.getDBManager();
			AnnualLeaveStDAO dao = new AnnualLeaveStDAO(dbManager);
			String gradeIds = dao.getGradeIdsByOrgServiceYear(orgId,Integer.parseInt(serviceYearLevel));
			String[] grade = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"};
			//if(gradeIds!=null && !"".equals(gradeIds)){
			  for(int i=0;i<15;i++){
			     out.print("<input type='checkbox' name='gradeId' value='"+grade[i]+"' ");	
			     if(gradeIds!=null && !"".equals(gradeIds) && gradeIds.indexOf(grade[i])>-1){
			    	 out.print(" disabled ");
			     }
			     out.print("title='职级' required='true'>"+(i+1)+"级&nbsp;");
			     if(i==3){
			    	 out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			     }
			     if(i==7){
			    	 out.print("<br><br>");
			     }
			  }
			//}else{
			//  out.print("success");
			//}
		}catch(Exception e){
			out.print("fail");
			e.printStackTrace();
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}	
		return null;
	}
}
