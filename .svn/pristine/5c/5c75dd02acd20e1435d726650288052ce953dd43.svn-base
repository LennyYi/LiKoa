package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.ApproverGroupVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ApproverGroupDAO extends BaseDAOImpl {

	public ApproverGroupDAO(IDBManager dbManager) {
		super(dbManager);
	}

	/**
	 * 获取某个人拥有的所有approver group
	 * 
	 * @param staffCode
	 * @throws DAOException
	 */
	public Collection getApproverGroupByStaff(String staffCode)
			throws DAOException {
		String sql = "select * from teflow_approver_group where approver_group_id in (select approver_group_id from teflow_approver_group_member where staff_code='"
				+ staffCode + "') order by approver_group_name";
		Collection list = dbManager.query(sql);
		if (list == null || list.size() == 0)
			return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ApproverGroupVO vo = convertMap(map);
			result.add(vo);
		}
		return result;
	}

	/**
	 * 获取某个人所没有拥有的approver group
	 * 
	 * @param staffCode
	 * @return
	 * @throws DAOException
	 */
	public Collection getNotApproverGroupByStaff(String staffCode)
			throws DAOException {
		String sql = "select * from teflow_approver_group where approver_group_id not in (select approver_group_id from teflow_approver_group_member where staff_code='"
				+ staffCode + "') order by approver_group_name";
		Collection list = dbManager.query(sql);
		if (list == null || list.size() == 0)
			return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ApproverGroupVO vo = convertMap(map);
			result.add(vo);
		}
		return result;
	}
    

	
	/**
	 * 
	 * @param groupId
	 * @return true --- 已经存在引用； false --- 没有存在引用
	 * @throws DAOException
	 */
	public boolean checkGroupUsed(String groupId) throws DAOException {
		String sql = "select count(*) from teflow_wkf_detail where approver_group_id like'%"
				+ groupId + "%'";
		int count = dbManager.getRecordCount(sql);
		if (count > 0)
			return true;
		return false;
	}

	public int delete(BaseVO vo) throws DAOException {
		ApproverGroupVO group = (ApproverGroupVO) vo;
		String delSql = "delete from teflow_approver_group where approver_group_id='"
				+ group.getGroupId() + "'";
		dbManager.executeUpdate(delSql);
		return 0;
	}

	/**
	 * return 0:save sucessfully return 1:primary key is exist
	 */
	public int save(BaseVO vo) throws DAOException {
		ApproverGroupVO group = (ApproverGroupVO) vo;
		String sql = "select count(*) from teflow_approver_group where approver_group_id='"
				+ group.getGroupId() + "'";
		int num = dbManager.getRecordCount(sql);
		if (num > 0)
			return 1;

		StringBuffer insertSQL = new StringBuffer(
				"insert into teflow_approver_group(approver_group_id,approver_group_name,group_type,description) ");
		insertSQL.append(" values('").append(group.getGroupId()).append("','")
				.append(group.getGroupName()).append("','").append(
						group.getGroupType()).append("','").append(
						group.getDescription()).append("')");
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}

	public int update(BaseVO vo) throws DAOException {
		ApproverGroupVO group = (ApproverGroupVO) vo;
		StringBuffer updateSQL = new StringBuffer(
				" update teflow_approver_group ");
		updateSQL.append(" set approver_group_name='").append(
				group.getGroupName()).append("',").append("group_type='")
				.append(group.getGroupType()).append("',").append(
						"description='").append(group.getDescription()).append(
						"'");
		updateSQL.append(" where approver_group_id='").append(
				group.getGroupId()).append("'");
		dbManager.executeUpdate(updateSQL.toString());
		return 0;
	}

	public ApproverGroupVO getById(String groupId) throws DAOException {
		String sql = "select * from teflow_approver_group where approver_group_id='"
				+ groupId + "'";
		Collection list = dbManager.query(sql);
		if (list == null || list.size() == 0)
			return null;
		HashMap map = (HashMap) list.iterator().next();
		ApproverGroupVO vo = convertMap(map);
		return vo;
	}

	public Collection getApproverGroupList() throws DAOException {
		String sql = "select * from teflow_approver_group order by approver_group_id";
		Collection list = dbManager.query(sql);
		if (list == null || list.size() == 0)
			return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			ApproverGroupVO vo = convertMap(map);
			result.add(vo);
		}
		return result;
	}

	public int getResultCount() throws DAOException {
		return dbManager.getRecordCount(getSQL());
	}

	private String getSQL() {
		String sql = "select * from teflow_approver_group order by approver_group_id";
		return sql;
	}

	private ApproverGroupVO convertMap(HashMap map) {
		ApproverGroupVO vo = new ApproverGroupVO();
		vo.setGroupId((String) map.get("APPROVER_GROUP_ID"));
		vo.setGroupName(DataConvertUtil.convertISOToGBK((String) map
				.get("APPROVER_GROUP_NAME")));
		vo.setGroupType((String) map.get("GROUP_TYPE"));
		vo.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil
				.convertSafeString(map, "DESCRIPTION")));
		return vo;
	}

}
