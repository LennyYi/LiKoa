package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.LeaveRecordVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class LeaveDAO extends BaseDAOImpl {

	public LeaveDAO(IDBManager dbManager){
		super(dbManager);
	}

	/**
	 * �¶��ݼٻ���
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws DAOException
	 */
	public Collection getMonthlyLeaveReport(String beginDate,String endDate)throws DAOException{
		
		return null;
	}
	
	/**
	 * �ݼ���ϸ���� �� ��ѯ���˵���ټ�¼
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws DAOException
	 */
	public Collection getMonthlyLeaveDetailReport(LeaveRecordVO conditionVo)throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		//1.��ȡ�����룬����û�����ģ����ڴ���...��,δ���,��ɵ�,��ȡ�ѹ��ʻ��Ѵ浵�ļ�¼
        //status˵���� '00'---δ�ύ; '01'---submitted,'02'---approved,������'������'��
		//            '03'---�Ѿܾ���'04'---���ڼ�¼������'δ���','�����'��;05---�ѹ��ʣ�δ�浵����'06'---�Ѵ浵��
     	StringBuffer str = new StringBuffer("select c.* from (");
     	str.append("select a.leave_from_date leave_from_date,a.leave_to_date leave_to_date,b.request_staff_code request_staff_code,");
        str.append("a.leave_from_type leave_from_type,a.leave_to_type leave_to_type,a.leave_type leave_type,a.leave_days leave_days,")
           .append(" b.status status ")
           .append("from "+leaveRecordTableName+" a,teflow_wkf_process b where a.request_no = b.request_no ")
           .append("and (b.status<>'00')");

        str.append(" and a.request_no not in ( select request_no from teflow_leave_posting) ")
           .append(" UNION ALL ");
        str.append("(select a.leave_from_date leave_from_date,a.leave_to_date leave_to_date,a.staff_code  request_staff_code,")
           .append("a.leave_from_type leave_from_type,a.leave_to_type leave_to_type,a.leave_type leave_type,a.leave_days leave_days,")
           .append("a.status status  from teflow_leave_posting a ) ");
          
        str.append(") c where 1=1 ");
        
        if(conditionVo.getRequestStaffCode()!=null && !"".equals(conditionVo.getRequestStaffCode())){
			str.append(" and c.request_staff_code='" + conditionVo.getRequestStaffCode()+"'");
		}
		if(conditionVo.getLeaveFromDate()!=null && !"".equals(conditionVo.getLeaveFromDate())){
			str.append(" and c.leave_from_date>='" + conditionVo.getLeaveFromDate()+"' ");
		}
		if(conditionVo.getLeaveToDate()!=null && !"".equals(conditionVo.getLeaveToDate())){
			str.append( " and c.leave_to_date>='" + conditionVo.getLeaveToDate()+"' ");
		}
        str .append(" order by c.request_staff_code,c.leave_from_date desc");
        
        Collection list = dbManager.query(str.toString());
        if(list==null || list.size()==0) return null;
        Collection result = new ArrayList();
        
        Iterator it = list.iterator();
        while(it.hasNext()){
        	HashMap map = (HashMap)it.next();
        	LeaveRecordVO vo = convertMapToLeaveVO(map);
        	vo.setProcessStatus(FieldUtil.convertSafeString(map,"STATUS"));
        	result.add(vo);
        }
		return result;
	}
	
	/**
	public Collection getOthersLeaveDetail(LeaveRecordVO conditionVo)throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		String sql = "select a.*,b.status,b.request_staff_code from " + leaveRecordTableName + " a,teflow_wkf_process b where a.request_no=b.request_no "
		              +" and b.status<>'00' ";
		if(conditionVo.getRequestStaffCode()!=null && !"".equals(conditionVo.getRequestStaffCode())){
			sql = sql + " and b.request_staff_code='" + conditionVo.getRequestStaffCode()+"'";
		}
		if(conditionVo.getLeaveFromDate()!=null && !"".equals(conditionVo.getLeaveFromDate())){
			sql = sql + " and a.leave_from_date>='" + conditionVo.getLeaveFromDate()+"' ";
		}
		if(conditionVo.getLeaveToDate()!=null && !"".equals(conditionVo.getLeaveToDate())){
			sql = sql + " and a.leave_to_date>='" + conditionVo.getLeaveToDate()+"' ";
		}
		sql = sql + " order by leave_from_date desc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			LeaveRecordVO vo = convertMapToLeaveVO(map);
			vo.setProcessStatus(FieldUtil.convertSafeString(map,"STATUS"));
			resultList.add(vo);
		}
		return resultList;
	}
	**/
	
	/**
	 * ��ȡ�ȴ����ʵ���ټ�¼�����������Ѿ�����������from���ں��ڵ�ǰ���ڣ�
	 * @return
	 * @throws DAOException
	 */
	public Collection getWaitForPostingList()throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		String sql = "select a.*,b.request_staff_code from " + leaveRecordTableName + " a,teflow_wkf_process b where a.request_no = b.request_no and "
		             +" b.status='04' and a.leave_from_date<='" + StringUtil.getCurrentDateStr("MM/dd/yyyy")+"' and a.request_no not in ( "
		             +" select request_no from teflow_leave_posting) ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			LeaveRecordVO vo = convertMapToLeaveVO(map);
			resultList.add(vo);
		}
		return resultList;
	}
	
	/**
	 *  ��ȡ�ȴ��������ټ�¼���Ѿ����ʣ���teflow_leave_posting�д��ڣ�����״̬Ϊ'01')
	 * @return
	 * @throws DAOException
	 */
	public Collection getWaitForBalanceList(LeaveRecordVO conditionVo)throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		if(leaveRecordTableName==null || "".equals(leaveRecordTableName)) return null;
		String sql = "select a.* from teflow_leave_posting a where 1=1 ";
		if(conditionVo.getProcessStatus()!=null && !"".equals(conditionVo.getProcessStatus())){
			sql = sql + " and status='" + conditionVo.getProcessStatus()+"' ";
		}
		if(conditionVo.getLeaveFromDate()!=null && !"".equals(conditionVo.getLeaveFromDate())){
			sql = sql + " and leave_from_date>=convert(datetime,'"+conditionVo.getLeaveFromDate()+"') ";
		}
		if(conditionVo.getLeaveToDate()!=null && !"".equals(conditionVo.getLeaveToDate())){
			sql = sql + " and leave_to_date<=convert(datetime,'"+conditionVo.getLeaveToDate()+"') ";
		}		
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			LeaveRecordVO vo = convertMapToLeaveVO(map);
			vo.setRequestStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			vo.setProcessStatus(FieldUtil.convertSafeString(map,"STATUS"));
			resultList.add(vo);
		}
		return resultList;
	}
	
	/**
	 * ������ټ�¼,status='05'�ѹ��ʣ�δ�浵��
	 * @param vo
	 * @throws DAOException
	 */
	public void postingLeave(LeaveRecordVO vo)throws DAOException{
		String sql = "insert into teflow_leave_posting(request_no,staff_code,leave_from_date,leave_to_date,leave_days,status) "
			+" values('" + vo.getRequestNo()+"','"+vo.getRequestStaffCode()+"','"+vo.getLeaveFromDate()+"','"
			+vo.getLeaveToDate()+"',"+vo.getLeaveDays()+",'05')";
		dbManager.executeUpdate(sql);
	}
	
	public void postingLeave(String requestNo)throws DAOException{
		String leaveRecordTableName = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_RECORD_TABLE_CODE);
		String sql = "insert into teflow_leave_posting(request_no,staff_code,leave_from_date,leave_to_date,leave_days,status,leave_from_type,leave_to_type,leave_type)"
			         +" select '"+requestNo+"',b.request_staff_code,a.leave_from_date,a.leave_to_date,a.leave_days,'05',a.leave_from_type,a.leave_to_type,a.leave_type from "
			         +leaveRecordTableName+" a,teflow_wkf_process b where a.request_no=b.request_no and a.request_no='"
			         +requestNo+"' ";
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * ��ټ�¼�浵��status='06'�Ѵ浵
	 * @param vo
	 * @throws DAOException
	 */
	public void archiveLeave(int id)throws DAOException{
		String sql = "update teflow_leave_posting set status='06' where id= " + id;
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
		vo.setId(FieldUtil.convertSafeInt(map,"ID",0));
		vo.setLeaveFromDate(FieldUtil.convertSafeString(map,"LEAVE_FROM_DATE"));
		vo.setLeaveFromType(FieldUtil.convertSafeString(map,"LEAVE_FROM_TYPE"));
		vo.setLeaveToDate(FieldUtil.convertSafeString(map,"LEAVE_TO_DATE"));
		vo.setLeaveToType(FieldUtil.convertSafeString(map,"LEAVE_TO_TYPE"));
		vo.setLeaveType(FieldUtil.convertSafeString(map,"LEAVE_TYPE"));
		vo.setLeaveDays(FieldUtil.convertSafeFloat(map,"LEAVE_DAYS",0));
		vo.setRequestStaffCode(FieldUtil.convertSafeString(map,"REQUEST_STAFF_CODE"));
		vo.setRequestNo(FieldUtil.convertSafeString(map,"REQUEST_NO"));
		return vo;
	}
}
