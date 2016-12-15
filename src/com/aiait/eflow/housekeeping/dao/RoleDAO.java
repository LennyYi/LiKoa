/*IT1280	Mario	02/10/2011	Multiple roles for a user*/
package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.vo.RoleVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;


public class RoleDAO extends BaseDAOImpl {
	
	public Collection getSourceStaff()throws DAOException{
		//String sql = "select staff_code,staff_name from tpma_staffbasic where status='A' and staff_code not in (select staff_code from teflow_role_member) order by staff_name asc";
		String sql = "select staff_code,staff_name from tpma_staffbasic where status='A' order by staff_name asc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			//RoleMemberVO vo = new RoleMemberVO();
			StaffVO staff = new StaffVO();
			staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(staff);
		}
		return result;
	}
	
	public Collection getMemberByRole(String roleId)throws DAOException{
		String sql = "select staff_code,staff_name from tpma_staffbasic where status='A' and staff_code in (select staff_code from teflow_role_member where role_id='"+roleId+"') order by staff_name asc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			StaffVO staff = new StaffVO();
			staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			result.add(staff);
		}
		return result;
	}
	
	public void saveRoleMember(String roleId,String staffCode)throws DAOException{
		String sql = "insert into teflow_role_member(role_id,staff_code) values('"+roleId+"','"+staffCode+"')";
		dbManager.executeUpdate(sql);
	}
	
	public void deleteRoleMember(String roleId)throws DAOException{
		String sql = "delete from teflow_role_member where role_id='"+roleId+"'";
		dbManager.executeUpdate(sql);
	}

	public void deleteRolePurview(String roleId)throws DAOException{
		String delSQL = "delete from teflow_role_module_operate";
		if((roleId!=null) &&  !"".equals(roleId)){
			delSQL = delSQL + " where role_id='" + roleId + "'";
		}
		dbManager.executeUpdate(delSQL);
	}
	
	
	public void saveRolePurview(String roleId,int moduleId,int operateId)throws DAOException{
		String sql = "insert into teflow_role_module_operate(role_id,module_id,operate_id) values('"
			         +roleId+"',"+moduleId+","+operateId+")";
		dbManager.executeUpdate(sql);
	}
	
	public Collection getFunctionsByRole(String roleId)throws DAOException{
		roleId = "'"+roleId.replaceAll(CommonName.MODULE_ROLE_SPLIT_SIGN, "','")+"'";
		//String sql = "select * from teflow_role_module_operate where role_id='"+roleId+"'";
		String sql = "select * from teflow_role_module_operate where role_id in ("+roleId+")";
		Collection result = new ArrayList();
		Collection list = dbManager.query(sql);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			HashMap map = (HashMap)iterator.next();
			result.add(map.get("MODULE_ID")+CommonName.MODULE_ROLE_SPLIT_SIGN+map.get("OPERATE_ID"));
		}
		return result;
	}
	
	public Collection getFormTypeFunctionsByRole(String roleId)throws DAOException{
		String sql = "select a.*,b.form_type_id from teflow_role_module_operate a,teflow_module_operate b where a.role_id='"+roleId+"' "
		             +" and a.module_id = b.module_id and a.operate_id = b.operate_id and b.form_type_id is not null ";
		
		Collection result = new ArrayList();
		Collection list = dbManager.query(sql);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			HashMap map = (HashMap)iterator.next();
			result.add(map.get("MODULE_ID")+CommonName.MODULE_ROLE_SPLIT_SIGN+map.get("FORM_TYPE_ID"));
		}
		return result;
	}
	
	public Collection getNoRoleByStaff(String staffCode)throws DAOException{
		String sql = "select * from teflow_role where 1=1 ";
		if(staffCode!=null && !"".equals(staffCode)){
			sql = sql + " and role_id not in (select role_id from teflow_role_member where staff_code='" + staffCode + "') ";
		}
		sql = sql + " order by role_name";
		Collection result = new ArrayList();
		Collection list = dbManager.query(sql);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			RoleVO role = new RoleVO();
		  HashMap map = (HashMap)iterator.next();
		  role.setRoleId((String)map.get("ROLE_ID"));
		  role.setRoleName(DataConvertUtil.convertISOToGBK((String)map.get("ROLE_NAME")));
		  role.setRemark(DataConvertUtil.convertISOToGBK((String)map.get("REMARK")));
		  result.add(role);
		}
		return result;
	}
	
	public Collection getRoleByStaff(String staffCode)throws DAOException{
		return search("",staffCode);
	}
	
	public RoleVO getRole(String roleId) throws DAOException{
		Collection list = search(roleId);
		if(list.size()!=0){
			return (RoleVO)list.iterator().next();
		}
		return null;
	}
	
	public Collection getAllRole()throws DAOException{
		return search("");
	}
	
	public Collection search(String roleId)throws DAOException{
		return search(roleId,"");
	}
	
	public Collection search(String roleId,String staffCode)throws DAOException{
		String sql = "select * from teflow_role where 1=1 ";
		if((roleId!=null) &&  !"".equals(roleId)){
			sql = sql + " and role_id='" + roleId + "'";
		}
		if(staffCode!=null && !"".equals(staffCode)){
			sql = sql + " and role_id in (select role_id from teflow_role_member where staff_code='" + staffCode + "') ";
		}
		sql = sql + " order by role_name";
		Collection result = new ArrayList();
		Collection list = dbManager.query(sql);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			RoleVO role = new RoleVO();
		  HashMap map = (HashMap)iterator.next();
		  role.setRoleId((String)map.get("ROLE_ID"));
		  role.setRoleName(DataConvertUtil.convertISOToGBK((String)map.get("ROLE_NAME")));
		  role.setRemark(DataConvertUtil.convertISOToGBK((String)map.get("REMARK")));
		  result.add(role);
		}
		return result;
	}
	
	
	public RoleDAO(IDBManager dbManager){
		super(dbManager);
	}
   
	public void deleteRoleByStaff(String staffCode)throws DAOException {
		String sql = "delete from teflow_role_member where staff_code='" + staffCode + "'";
		dbManager.executeUpdate(sql);
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		RoleVO role = (RoleVO)arg0;
		String sql = "delete from teflow_role where role_id='"+role.getRoleId()+"'";
		dbManager.executeUpdate(sql);
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		RoleVO role = (RoleVO)arg0;
		String sql = "insert into teflow_role(role_id,role_name,remark) values('" + role.getRoleId()
		             +"','"+role.getRoleName()+"','"+role.getRemark()+"')";
		dbManager.executeUpdate(sql);
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		RoleVO role = (RoleVO)arg0;
		String sql = "update teflow_role set role_name='" + role.getRoleName()+"',remark='"+role.getRemark()+"' "
		             +" where role_id='" + role.getRoleId()+"' ";
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	
	public long getRoleMemberMaxHistoryId()throws DAOException{
		String sql = "select max(history_id) history_id from teflow_role_member_history";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return 0;
		HashMap map = (HashMap)list.iterator().next();
		if(map.get("HISTORY_ID")!=null){
		  return Long.parseLong((String)map.get("HISTORY_ID"))+1;
		}
		return 0;
	}
	
	public int saveRoleMemberOldHistory(long historyId,String roleId)throws DAOException{
		String sql = "INSERT INTO teflow_role_member_history(history_id,role_id,staff_code) select "+historyId+",role_id,staff_code from teflow_role_member where role_id='"+roleId+"'";
		return dbManager.executeUpdate(sql);
	}
	
	public void saveRoleMemberNewHistory(long historyId,String roleId,String staffCode)throws DAOException{
		String sql = "INSERT INTO teflow_role_member_history(history_id,role_id,staff_code)  VALUES("+historyId+",'"+roleId+"','"+staffCode+"')";
		dbManager.executeUpdate(sql);
	}
	
	public void saveRoleMemberLog(String staffCode,String roleId,long oldHistoryId,long newHistoryId)throws DAOException{
		String sql = "INSERT INTO teflow_role_member_log(staff_code,operate_time,role_id,old_history_id,new_history_id) "
			         + " VALUES('"+staffCode+"',getdate(),'"+roleId+"',"+oldHistoryId+","+newHistoryId+") ";
		
		dbManager.executeUpdate(sql);
	}
	
	public long getRolePurviewMaxHistoryId()throws DAOException{
		String sql = "select max(history_id) history_id from teflow_role_module_operate_history";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return 0;
		HashMap map = (HashMap)list.iterator().next();
		if(map.get("HISTORY_ID")!=null){
		  return Long.parseLong((String)map.get("HISTORY_ID"))+1;
		}
		return 0;
	}
	
	public int saveRolePurviewOldHistory(long historyId,String roleId)throws DAOException{
		String sql = "INSERT INTO teflow_role_module_operate_history(history_id,role_id,module_id,operate_id) select "+historyId+",role_id,module_id,operate_id from teflow_role_module_operate where role_id='"+roleId+"'";
		return dbManager.executeUpdate(sql);
	}
	
	public void saveRolePurviewNewHistory(long historyId,String roleId,int moduleId,int operateId)throws DAOException{
		String sql = "INSERT INTO teflow_role_module_operate_history(history_id,role_id,module_id,operate_id) VALUES("
			         +historyId+",'"+roleId+"',"+moduleId+","+operateId+")";
		dbManager.executeUpdate(sql);
	}
	
	public void saveRolePurivewLog(String staffCode,String roleId,long oldHistoryId,long newHistoryId)throws DAOException{
		String sql = "INSERT INTO teflow_role_module_operate_log(staff_code,operate_time,role_id,old_history_id,new_history_id) "
			         + " VALUES('"+staffCode+"',getdate(),'"+roleId+"',"+oldHistoryId+","+newHistoryId+") ";
		
		dbManager.executeUpdate(sql);
	}

}
