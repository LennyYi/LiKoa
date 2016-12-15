package com.aiait.eflow.housekeeping.dao;

/**
 * 该类用来实现与PMA系统的数据同步
 */

import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class SynchronizePMADAO extends BaseDAOImpl {
	
	public SynchronizePMADAO(IDBManager dbManager){
		super(dbManager);
	}
	
	//IT1321
	public void synchTeamLeader()throws DAOException{
	/*	String sql = "insert into teflow_approver_group_member(approver_group_id,staff_code) select '02',staff_code from  tpma_StaffBasic "
			        +" where status = 'A' and staff_code NOT IN (select  staff_code from teflow_approver_group_member where  approver_group_id ='02') "
			        +" and logon_id IN (select tl_id from tpma_team where status = 'A') ";*/

		String sql1 = "delete teflow_approver_group_member where approver_group_id in('02','15','34')";//TL1 and Resource Owner
		String sql2 = "insert into teflow_approver_group_member(approver_group_id,staff_code) select distinct '02',s.staff_code "
					+" from tpma_StaffBasic s, tpma_team a, tpma_team b "
					+" where s.status='A' and s.logon_id=a.tl_id  and a.status='A' and (a.subordinate=b.team_code and b.org_chart='Y' "
					+" or a.team_code=0) ";
					//Sync TL1
		String sql3 = "insert into teflow_approver_group_member(approver_group_id,staff_code) select distinct '15',s.staff_code "
					+" from tpma_StaffBasic s, tpma_team a, tpma_team b "
					+" where s.status='A' and s.logon_id=a.tl_id  and a.status='A' and (a.subordinate=b.team_code and b.org_chart='Y' "
					+" or a.team_code=0) ";
					//Sync Resource Owner
		String sql4 = "insert into teflow_approver_group_member(approver_group_id,staff_code) select '34',staff_code from tpma_StaffBasic "
					+" where status = 'A' and logon_id IN (select a.tl_id from tpma_team a " 
					+" where status = 'A' and  org_chart='Y')";
					//Sync TL (34)
		String sql5 = "delete teflow_role_member where role_id='02'";
		String sql6 = "insert into teflow_role_member(role_id,staff_code) select '02',staff_code from tpma_StaffBasic "
					+" where status = 'A' and logon_id IN (select a.tl_id from tpma_team a " 
					+" where status = 'A' and  org_chart='Y')";
		try{
			dbManager.startTransaction();
			dbManager.executeUpdate(sql1);
			dbManager.executeUpdate(sql2);
			dbManager.executeUpdate(sql3);
			dbManager.executeUpdate(sql4);
			dbManager.executeUpdate(sql5);
			dbManager.executeUpdate(sql6);
			dbManager.commit();
		}catch(DAOException e){
			dbManager.rollback();
			throw e;
		}
	}
	
	/**
	 * 对于新来的员工，给予一个系统默认的角色'00'/default
	 * @throws DAOException
	 */
	public void sychNewStaff()throws DAOException{
		String sql = "insert into teflow_role_member(role_id,staff_code) select '00',staff_code from tpma_StaffBasic where status='A' and staff_code not in (select staff_code from teflow_role_member)";
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

}
