package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO;
import com.aiait.eflow.housekeeping.vo.DBOwnerVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.SystemOwnerVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ApproverGroupMemberDAO extends BaseDAOImpl {

	public ApproverGroupMemberDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getSystemOwnerList()throws DAOException{
		String sql = "select a.system_name,b.staff_name from teflow_system_owner a,tpma_staffbasic b where a.SYSTEM_OWNER_CODE = b.staff_code and b.status='A' order by system_id";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			ApproverGroupMemberVO vo = new ApproverGroupMemberVO();
			HashMap map = (HashMap)it.next();
			vo.setStaffCode(FieldUtil.convertSafeString(map,"SYSTEM_NAME"));
			vo.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(vo);
		}
		return result;
	}
	
	public Collection getDBOwnerList()throws DAOException{
		String sql = "select a.DB_NAME,b.staff_name from teflow_db_owner a,tpma_staffbasic b where a.DB_OWNER_CODE = b.staff_code and b.status='A' order by db_id";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			ApproverGroupMemberVO vo = new ApproverGroupMemberVO();
			HashMap map = (HashMap)it.next();
			vo.setStaffCode(FieldUtil.convertSafeString(map,"DB_NAME"));
			vo.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(vo);
		}
		return result;
	}
	
	public Collection getProjectLeaderList()throws DAOException{
		String sql = "select distinct b.staff_code,b.staff_name from tpma_project a,tpma_staffbasic b where a.status='10' and a.prj_ld_id = b.logon_id and b.status='A' order by b.staff_name";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			ApproverGroupMemberVO vo = new ApproverGroupMemberVO();
			HashMap map = (HashMap)it.next();
			vo.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			vo.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(vo);
		}
		return result;
	}
	
	public void deleteMemberByGroup(String groupId)throws DAOException{
		String sql = "delete from teflow_approver_group_member where approver_group_id='" + groupId +"'";
		dbManager.executeUpdate(sql);
	}
	
	public void deleteStaffApproverGroup(String staffCode)throws DAOException{
		String sql = "delete from teflow_approver_group_member where staff_code='" + staffCode + "'";
		dbManager.executeUpdate(sql);
	}
	
	public int delete(BaseVO vo) throws DAOException {
		if(vo == null) return 0;
		ApproverGroupMemberVO member = (ApproverGroupMemberVO)vo;
		String sql = "delete from teflow_approver_group_member where approver_group_id='" + member.getGroupId() +"' and staff_code='" + member.getStaffCode()+"'";
		return dbManager.executeUpdate(sql);
	}

	public int save(BaseVO vo) throws DAOException {
		if(vo == null) return 0;
		ApproverGroupMemberVO member = (ApproverGroupMemberVO)vo;
		
		String csql = "select count(*) from teflow_approver_group_member where approver_group_id='"+member.getGroupId()+"' and staff_code='"+member.getStaffCode()+"' ";
		int count = dbManager.getRecordCount(csql);
		
		if(count < 1){
			String sql = "insert into teflow_approver_group_member(approver_group_id,staff_code) values('"+member.getGroupId()+"','"+member.getStaffCode()+"')";
			return dbManager.executeUpdate(sql);
		}
		return 0;
	}
	
	/**
	 * get all members by groupId
	 * @param groupId
	 * @return
	 * @throws DAOException
	 */
	public Collection getMemberList(String groupId)throws DAOException{
		String sql = "select a.approver_group_id,a.staff_code,b.staff_name from teflow_approver_group_member a,tpma_staffbasic b where a.staff_code=b.staff_code and  b.status='A' and a.approver_group_id='" + groupId+"' order by b.staff_name";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			ApproverGroupMemberVO vo = new ApproverGroupMemberVO();
			HashMap map = (HashMap)it.next();
			vo.setGroupId(FieldUtil.convertSafeString(map,"APPROVER_GROUP_ID"));
			vo.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			vo.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(vo);
		}
		return result;
	}
	
	public Collection getAvailableStaffList(String groupId,String groupType)throws DAOException{
		String sql = "SELECT staff_code, staff_name from tpma_StaffBasic where staff_code NOT IN (select  staff_code from teflow_approver_group_member where  approver_group_id ='" +groupId + "')";
		sql = sql + " and status = 'A' ";
		//if(CommonName.APPROVER_GROUP_TYPE_TL.equals(groupType)){
		//	sql = sql + " and logon_id IN (select tl_id from tpma_team where status = 'A')";
		//}
		sql = sql + " order by staff_name";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			HashMap  map = (HashMap)it.next();
			StaffVO staff = new StaffVO();
			staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(staff);
		}
		return result;
	}

	public String[] getApproverGroupStaffList(String groupIds)throws DAOException{
		String sql = "SELECT distinct staff_code from tpma_StaffBasic where staff_code in (select staff_code from teflow_approver_group_member where approver_group_id in ("+groupIds+")) ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return  null;
		Iterator it = list.iterator();
		String[] result = new String[list.size()];
		int i =0;
		while(it.hasNext()){
			HashMap  map = (HashMap)it.next();
			result[i] = FieldUtil.convertSafeString(map,"STAFF_CODE");
			i++;
		}
		return result;
	}

	
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
