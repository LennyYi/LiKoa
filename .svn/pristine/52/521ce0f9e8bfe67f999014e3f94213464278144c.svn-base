package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.HolidayDAO;
import com.aiait.eflow.housekeeping.vo.HolidayVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class HolidayAction extends DispatchAction{
	
	public ActionLocation listHolidayYear(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listYear";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  HolidayDAO dao = new HolidayDAO(dbManager);
		  Collection list = dao.getHolidayYear();
		  request.setAttribute("yearList",list);
		}catch(Exception e){
		  e.printStackTrace();
		  returnLabel = "fail";
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	public ActionLocation listHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listResult";
		IDBManager dbManager = null;
		String setYear = (String)request.getParameter("setYear");
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  HolidayDAO dao = new HolidayDAO(dbManager);
		  Collection list = dao.getHolidayList(setYear);
		  request.setAttribute("resultList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation deleteHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String[] HolidayId = request.getParameterValues("holidayId");
		IDBManager dbManager=null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  dbManager.startTransaction();
		  HolidayDAO dao = new HolidayDAO(dbManager);
		  HolidayVO vo = new HolidayVO();
		  for(int i=0; i<HolidayId.length; i++){
			vo.setHolidayId(Integer.parseInt(HolidayId[i]));
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
	
	public ActionLocation batchHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "batchPage";
		String[] HolidayId = request.getParameterValues("holidayId");
		IDBManager dbManager=null;
		try{
			dbManager =  DBManagerFactory.getDBManager();
			HolidayDAO dao = new HolidayDAO(dbManager);			
			HolidayVO vo = dao.getHolidayVO(Integer.parseInt(HolidayId[0]));
			request.setAttribute("vo",vo);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation editHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null){
			saveType = "new";
		}
		if(!"new".equals(saveType)){
			int HolidayId = Integer.parseInt((String)request.getParameter("holidayId"));
			IDBManager dbManager = null;
			try{
			  dbManager =  DBManagerFactory.getDBManager();
			  HolidayDAO dao = new HolidayDAO(dbManager);
			  HolidayVO vo = dao.getHolidayVO(HolidayId);
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
	
	public ActionLocation saveHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("saveType");
		//String holidayYear = (String)request.getParameter("setYear");
		String submitType = (String)request.getParameter("submitType");
		if(saveType==null){
			saveType = "new";
		}
		HolidayVO vo = new HolidayVO();
		vo.setHolidayYear((String)request.getParameter("holidayYear"));
		vo.setHolidayFromDate((String)request.getParameter("holidayFromDate"));
		vo.setHolidayToDate((String)request.getParameter("holidayToDate"));
		vo.setHolidayDescription((String)request.getParameter("holidayDescription"));
		vo.setHolidayStatus(Integer.parseInt((String)request.getParameter("holidayStatus")));
		vo.setHolidayBatchFromYear((String)request.getParameter("holidayBatchFromYear"));
		vo.setHolidayBatchToYear((String)request.getParameter("holidayBatchToYear"));
		vo.setHolidayType((String)request.getParameter("holidaykind"));
		IDBManager dbManager = null;
		response.setCharacterEncoding("GB2312");
		PrintWriter out = response.getWriter();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  HolidayDAO dao = new HolidayDAO(dbManager);
		  if("new".equals(saveType)){
			  dao.save(vo);
		  }
		  else if("batch".equals(saveType)){
			  dao.saveBatch(vo);
		  }
		  else{
			  int HolidayId = Integer.parseInt((String)request.getParameter("holidayId"));
			  vo.setHolidayId(HolidayId);
			  dao.update(vo);
		  }
		  out.print("saveSuccess");
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
			out.print(e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		if ("0".equals(submitType)){
			return mapping.findActionLocation(returnLabel);
		}else{
			return null;
		}
		
	}
	public ActionLocation listCalendar(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel="listCalendar";
				
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation setpublicHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		//String returnLabel="listCalendar";
		String strYear = (String)request.getParameter("strYear");
		String strMonth = (String)request.getParameter("strMonth");
		String seldate = (String)request.getParameter("seldate");
		PrintWriter out = response.getWriter();
		
		HolidayVO vo = new HolidayVO();		
		vo.setHolidayYear(strYear);
		vo.setHolidayStatus(5);
		vo.setHolidayType("1");
		IDBManager dbManager = null;
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  HolidayDAO dao = new HolidayDAO(dbManager);
			  String[] arrseldate = null;			  
			  arrseldate = seperatedate(seldate);			  
			  dbManager.startTransaction();
			  if (arrseldate!=null && arrseldate.length>0){
				  for (int i=0;i<arrseldate.length;i++){			
					  String tmpdate = (String)arrseldate[i];		
					  String strdate = strYear+"-"+strMonth+"-"+tmpdate;
					  vo.setHolidayFromDate(strdate);
					  vo.setHolidayToDate(strdate);
					  String datedes = dao.getWeekdayofDate(strdate);
					  vo.setHolidayDescription(datedes);
					  //判断是否已经存在；存在则update
					  String HolidayId=null;
					  HolidayId = dao.getHolidayIdByDate(strdate);
					  if (!"".equals(HolidayId)){
						  vo.setHolidayId(Integer.parseInt(HolidayId));
						  dao.update(vo);
					  }else{
						  dao.save(vo);  
					  }					  
				  }
			  }
			  dbManager.commit();
			  out.print("success");
		}catch(Exception e){
		    dbManager.rollback();
			e.printStackTrace();
			out.print(e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	private String[] seperatedate(String seldate){
			String[] result = StringUtil.split(seldate,",");		
			return result;			
	}
	public ActionLocation setLegalHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		//String returnLabel="listCalendar";
		String strYear = (String)request.getParameter("strYear");
		String strMonth = (String)request.getParameter("strMonth");
		String seldate = (String)request.getParameter("seldate");
		PrintWriter out = response.getWriter();
		
		HolidayVO vo = new HolidayVO();		
		vo.setHolidayYear(strYear);
		vo.setHolidayStatus(5);
		vo.setHolidayType("2");
		IDBManager dbManager = null;
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  HolidayDAO dao = new HolidayDAO(dbManager);
			  String[] arrseldate = null;			  
			  arrseldate = seperatedate(seldate);			  
			  dbManager.startTransaction();
			  if (arrseldate!=null && arrseldate.length>0){
				  for (int i=0;i<arrseldate.length;i++){			
					  String tmpdate = (String)arrseldate[i];		
					  String strdate = strYear+"-"+strMonth+"-"+tmpdate;
					  vo.setHolidayFromDate(strdate);
					  vo.setHolidayToDate(strdate);
					  String datedes = dao.getWeekdayofDate(strdate);
					  vo.setHolidayDescription(datedes);
					  //判断是否已经存在；存在则update
					  String HolidayId=null;
					  HolidayId = dao.getHolidayIdByDate(strdate);
					  if (!"".equals(HolidayId)){
						  vo.setHolidayId(Integer.parseInt(HolidayId));
						  dao.update(vo);
					  }else{
						  dao.save(vo);  
					  }					  
				  }
			  }
			  dbManager.commit();
			  out.print("success");
		}catch(Exception e){
		    dbManager.rollback();
			e.printStackTrace();
			out.print(e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation importPMA(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

		String strYear = (String)request.getParameter("setYear");
		PrintWriter out = response.getWriter();
		
		HolidayVO vo = new HolidayVO();		
		vo.setHolidayYear(strYear);
		IDBManager dbManager = null;
		try{
			  dbManager =  DBManagerFactory.getDBManager();
			  HolidayDAO dao = new HolidayDAO(dbManager);
			  dbManager.startTransaction();
			  int[] RecCnt = dao.importPMAOffDays(vo);
			  dbManager.commit();
			  out.print("Synchronize complete, "+RecCnt[0]+" imported and "+RecCnt[1]+" removed");
			  
		}catch(Exception e){
		    dbManager.rollback();
			e.printStackTrace();
			out.print(e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
}
