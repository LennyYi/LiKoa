package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.LeaveDAO;
import com.aiait.eflow.housekeeping.dao.StaffHolidayDAO;
import com.aiait.eflow.housekeeping.vo.LeaveRecordVO;
import com.aiait.eflow.housekeeping.vo.StaffHolidayVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class StaffHolidayAction extends DispatchAction {
    
	/**
	 * 进入个人假期情况汇总信息页面
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation queryPersonalHoliday(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPersonalHoliday";
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  StaffHolidayDAO holidayDao = new StaffHolidayDAO(dbManager);
		  //年假
		  StaffHolidayVO holidayAnnualVo = holidayDao.getStaffHoliday(staff.getStaffCode(),CommonName.LEAVE_TYPE_ANNUAL_DAY);
		  //病假
		  StaffHolidayVO holidaySickVo = holidayDao.getStaffHoliday(staff.getStaffCode(),CommonName.LEAVE_TYPE_SICK_LEAVE);
		  request.setAttribute("holidayAnnualVo",holidayAnnualVo);
		  request.setAttribute("holidaySickVo",holidaySickVo);
		}catch(DAOException e){
			returnLabel = "fail";
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 显示个人的请假明细
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listPersonalLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPersonalLeave";
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  StaffHolidayDAO holidayDao = new StaffHolidayDAO(dbManager);
		  Collection leaveList = holidayDao.getStaffLeaveDetail(staff.getStaffCode());
		  request.setAttribute("leaveList",leaveList);
		}catch(DAOException e){
			returnLabel = "fail";
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel); 
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterQueryOthersLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "queryOtherLeave";
		//根据当前操作者的查询权限来显示可以查询的人员列表
		
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * 显示其他人的请假明细
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listOthersLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listOthersLeave";
		//StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String staffCode = (String)request.getParameter("staffCode");
		String staffName = (String)request.getParameter("staffName");
		String beginDate = (String)request.getParameter("beginDate");
		String endDate = (String)request.getParameter("endDate");
		LeaveRecordVO conditionVo = new LeaveRecordVO();
		conditionVo.setRequestStaffCode(staffCode);
		conditionVo.setLeaveFromDate(beginDate);
		conditionVo.setLeaveToDate(endDate);
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  //StaffHolidayDAO holidayDao = new StaffHolidayDAO(dbManager);
		  LeaveDAO dao = new LeaveDAO(dbManager);
		  Collection leaveList = dao.getMonthlyLeaveDetailReport(conditionVo);
		  request.setAttribute("leaveList",leaveList);
		}catch(DAOException e){
			returnLabel = "fail";
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR,e.getMessage());
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
}
