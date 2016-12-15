package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.LeaveRecordVO;
import com.aiait.eflow.housekeeping.vo.StaffHolidayVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class StaffHolidayDAO extends BaseDAOImpl {
	
	public StaffHolidayDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	/**
	 * 休假结余报表
	 * @return
	 * @throws DAOException
	 */
	public Collection getStaffBalanceHoliday(String teamCode,String staffCode,String leaveType)throws DAOException{
		String sql = "select * from teflow_staff_holiday where 1=1 ";
		if(staffCode!=null && !"".equals(staffCode)){
			sql = sql + " and staff_code='"+staffCode+"' ";
		}else if(teamCode!=null && !"".equals(teamCode)){
			sql = sql + " and staff_code in ( select staff_code from tpma_staffbasic where status='A' and team_code in("
			+teamCode+") )";
		}
		if(leaveType!=null && !"".equals(leaveType)){
			sql = sql + " and leave_type='" + leaveType+"' ";
		}
		sql = sql + " order by staff_code,leave_type ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			StaffHolidayVO vo = convertMapToVO(map);
			result.add(vo);
		}
		return result;
	}
	

	
	
	
	public LeaveRecordVO getLeaveRecord(String requestNo)throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		String sql = "select a.*,b.request_staff_code from " + leaveRecordTableName + " a,teflow_wkf_process b where a.request_no=b.request_no and a.request_no='" + requestNo + "' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		LeaveRecordVO vo = new LeaveRecordVO();
		vo.setRequestStaffCode(FieldUtil.convertSafeString(map,"REQUEST_STAFF_CODE"));
		vo.setRequestNo(requestNo);
		vo.setLeaveType(FieldUtil.convertSafeString(map,"LEAVE_TYPE"));
		vo.setLeaveDays(FieldUtil.convertSafeFloat(map,"LEAVE_DAYS",0));
		return vo;
	}
	
	public StaffHolidayVO getStaffHoliday(String staffCode,String leaveType)throws DAOException{
		String sql = "select * from teflow_staff_holiday where staff_code = '" + staffCode + "' and leave_type='"
		             +leaveType+"' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new StaffHolidayVO();
		HashMap map  = (HashMap)list.iterator().next();
		return convertMapToVO(map);
	}
	
	/**
	 * 获取指定员工的请假记录
	 * @param staffCode
	 * @return
	 * @throws DAOException
	 */
	public Collection getStaffLeaveDetail(String staffCode)throws DAOException{
		//结果是个人请假记录表与批量请假记录表的综合
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		String sql = "select a.*,b.status from " + leaveRecordTableName + " a,teflow_wkf_process b where a.request_no=b.request_no "
		              +" and b.request_staff_code='" + staffCode+"' and b.status<>'00' ";
		sql = sql + " order by leave_from_date desc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			LeaveRecordVO vo = convertMapToLeaveVO(map);
			vo.setRequestStaffCode(staffCode);
			vo.setProcessStatus(FieldUtil.convertSafeString(map,"STATUS"));
			resultList.add(vo);
		}
		return resultList;
	}


	
	/**
	 * 
	 * @param staffCode
	 * @param leaveType:  01 ---  annual days; 02 ---- current year sick days
	 * @param days
	 * @throws DAOException
	 */
	public void balanceHolidays(String staffCode,String leaveType,double days)throws DAOException{
		String sql = "update teflow_staff_holiday set used_days="+days+" where staff_code='" + staffCode + "' and leave_type='" + leaveType+"' ";
		dbManager.executeUpdate(sql);
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	private LeaveRecordVO convertMapToLeaveVO(HashMap map){
		LeaveRecordVO vo = new LeaveRecordVO();
		vo.setLeaveFromDate(FieldUtil.convertSafeString(map,"LEAVE_FROM_DATE"));
		vo.setLeaveFromType(FieldUtil.convertSafeString(map,"LEAVE_FROM_TYPE"));
		vo.setLeaveToDate(FieldUtil.convertSafeString(map,"LEAVE_TO_DATE"));
		vo.setLeaveToType(FieldUtil.convertSafeString(map,"LEAVE_TO_TYPE"));
		vo.setLeaveType(FieldUtil.convertSafeString(map,"LEAVE_TYPE"));
		vo.setLeaveDays(FieldUtil.convertSafeFloat(map,"LEAVE_DAYS",0));
		return vo;
	}
	
	private StaffHolidayVO convertMapToVO(HashMap map){
		StaffHolidayVO vo = new StaffHolidayVO();
		vo.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
		vo.setLeaveType(FieldUtil.convertSafeString(map,"LEAVE_TYPE"));
		vo.setLastYearDays(FieldUtil.convertSafeFloat(map,"LAST_YEAR_DAYS",0));
		vo.setLastAbandonDays(FieldUtil.convertSafeFloat(map,"LAST_ABANDON_DAYS",0));
		vo.setThisYearDays(FieldUtil.convertSafeFloat(map,"THIS_YEAR_DAYS",0));
		vo.setAdjustDays(FieldUtil.convertSafeFloat(map,"ADJUST_DAYS",0));
		vo.setUsedDays(FieldUtil.convertSafeFloat(map,"USED_DAYS",0));
		return vo;
	}
}
