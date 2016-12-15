package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.DBOwnerVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class DBOwnerDAO extends BaseDAOImpl {

	public DBOwnerDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getDBOwnerList()throws DAOException{
		String sql = "select * from teflow_db_owner order by db_id";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			DBOwnerVO vo = new DBOwnerVO();
			HashMap map = (HashMap)it.next();
			vo.setDBId(FieldUtil.convertSafeString(map,"DB_ID"));
			vo.setDBName(FieldUtil.convertSafeString(map,"DB_NAME"));
			vo.setDBStaffCode(FieldUtil.convertSafeString(map,"DB_OWNER_CODE"));
			result.add(vo);
		}
		return result;
	}
	
	public DBOwnerVO getDBOwnerVO(String DBId)throws DAOException{
		String sql = "select * from teflow_db_owner where db_id='" + DBId + "'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		DBOwnerVO vo = new DBOwnerVO();
		vo.setDBId(FieldUtil.convertSafeString(map,"DB_ID"));
		vo.setDBName(FieldUtil.convertSafeString(map,"DB_NAME"));
		vo.setDBStaffCode(FieldUtil.convertSafeString(map,"DB_OWNER_CODE"));
		return vo;
	}
	
	public int delete(BaseVO vo) throws DAOException {
		DBOwnerVO DBOwner = (DBOwnerVO)vo;
		String delSQL = "delete from teflow_db_owner where db_id='"+DBOwner.getDBId()+"'";
		dbManager.executeUpdate(delSQL);
		return 0;
	}

	public int save(BaseVO vo) throws DAOException {
		DBOwnerVO DBOwner = (DBOwnerVO)vo;
		String selSQL = "select * from teflow_db_owner where db_id='"+DBOwner.getDBId()+"'";
		Collection list = dbManager.query(selSQL);
		if(list!=null && list.size()>0) return 1;
		String sql = "insert into teflow_db_owner(db_id, db_name, db_owner_code) values('"+DBOwner.getDBId()+"','"+DBOwner.getDBName()
		              +"','"+DBOwner.getDBStaffCode()+"')";
		dbManager.executeUpdate(sql);
		return 0;
	}

	public int update(BaseVO vo) throws DAOException {
		   DBOwnerVO DBOwner = (DBOwnerVO)vo;
		   String updateSQL = "update teflow_db_owner set db_name='" + DBOwner.getDBName() + "',db_owner_code='"+
		                        DBOwner.getDBStaffCode()+"' where db_id='"+DBOwner.getDBId()+"'";
		   dbManager.executeUpdate(updateSQL);
		   return 0;
		}

}
