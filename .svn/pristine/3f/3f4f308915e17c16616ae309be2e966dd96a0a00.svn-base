package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.LeaveDAO;
import com.aiait.eflow.housekeeping.dao.StaffHolidayDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.vo.LeaveRecordVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class LeaveAction extends DispatchAction {
	
	
	/**
	 * 进入休假结余报表页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterLeaveBalance(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "leaveBalanceSearch";
		//to get team list
		
		//to get staff list
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 进入休假结余报表结果显示页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listLeaveBalance(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "listLeaveBalanceReport";
		String teamCode = (String)request.getParameter("teamCode");
		String staffCode = (String)request.getParameter("request_staff_code");
		String leaveType = (String)request.getParameter("leaveType");
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  StaffHolidayDAO staffHolidayDao = new StaffHolidayDAO(dbManager);
		  String subTeams = "";
		  if(teamCode!=null && !"".equals(teamCode)){
			  TeamDAO teamDao = new TeamDAO(dbManager);
			  subTeams = teamDao.groupSubTeam(teamCode);
			  if(subTeams!=null && !"".equals(subTeams)){
				  //判断最后一位是否是","
				  if(subTeams.substring(subTeams.length()-1).equals(",")){
					  teamCode = subTeams +teamCode;
				  }else{
					  teamCode = subTeams + ","+teamCode;
				  }
			  }
		  }
		  System.out.println("teamCode="+teamCode);
		  Collection staffHolidayList = staffHolidayDao.getStaffBalanceHoliday(teamCode,staffCode,leaveType);
		  request.setAttribute("staffHolidayList",staffHolidayList);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}	

	/**
	 * 进入休假明细报表统计页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterMonthlyLeaveDetail(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception {
		String returnLabel = "leaveDetailPage";
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 休假明细报表
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listMonthlyLeaveDetail(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listLeaveDetailReport";
		String beginDate = (String)request.getParameter("beginDate");
		String endDate = (String)request.getParameter("endDate");
		LeaveRecordVO conditionVo = new LeaveRecordVO();
		//conditionVo.setRequestStaffCode(staffCode);
		conditionVo.setLeaveFromDate(beginDate);
		conditionVo.setLeaveToDate(endDate);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  Collection list = dao.getMonthlyLeaveDetailReport(conditionVo);
		  request.setAttribute("leaveDetailList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
   
	public ActionLocation listLeaveForPosting(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listForPosting";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  Collection list = dao.getWaitForPostingList();
		  request.setAttribute("postingList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	public ActionLocation postingLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String[] requestNos = request.getParameterValues("requestNo");
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  dbManager.startTransaction();
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  for(int i=0;i<requestNos.length;i++){
			  dao.postingLeave(requestNos[i]);
		  }
		  dbManager.commit();
		  out.print("success");
		}catch(Exception e){
		  dbManager.rollback();
		  e.printStackTrace();
		  out.print("fail");
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
	
	public ActionLocation listLeaveForArchiving(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listForArchiving";
		String status = (String)request.getParameter("status");
		String leaveFromDate = (String)request.getParameter("leaveFromDate");
		String leaveToDate = (String)request.getParameter("leaveToDate");
		LeaveRecordVO vo = new LeaveRecordVO();
		vo.setProcessStatus(status);
		vo.setLeaveFromDate(leaveFromDate);
		vo.setLeaveToDate(leaveToDate);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  Collection list = dao.getWaitForBalanceList(vo);
		  request.setAttribute("balanceList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	
	public ActionLocation archiveLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String[] ids = request.getParameterValues("id");
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  dbManager.startTransaction();
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  for(int i=0;i<ids.length;i++){
			 dao.archiveLeave(Integer.parseInt(ids[i]));
		  }
		  dbManager.commit();
		  out.print("success");
		}catch(Exception e){
		  dbManager.rollback();
		  e.printStackTrace();
		  out.print("fail");
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
	}
}
