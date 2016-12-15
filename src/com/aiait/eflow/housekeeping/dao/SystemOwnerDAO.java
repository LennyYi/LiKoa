package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.SystemOwnerVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class SystemOwnerDAO extends BaseDAOImpl {
	
	public SystemOwnerDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getSystemOwnerList()throws DAOException{
		String sql = "select * from teflow_system_owner order by system_id";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			SystemOwnerVO vo = new SystemOwnerVO();
			HashMap map = (HashMap)it.next();
			vo.setSystemId(FieldUtil.convertSafeString(map,"SYSTEM_ID"));
			vo.setSystemName(FieldUtil.convertSafeString(map,"SYSTEM_NAME"));
			vo.setSystemStaffCode(FieldUtil.convertSafeString(map,"SYSTEM_OWNER_CODE"));
			result.add(vo);
		}
		return result;
	}
	
	public SystemOwnerVO getSystemOwnerVO(String systemId)throws DAOException{
		String sql = "select * from teflow_system_owner where system_id='" + systemId + "'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		SystemOwnerVO vo = new SystemOwnerVO();
		vo.setSystemId(FieldUtil.convertSafeString(map,"SYSTEM_ID"));
		vo.setSystemName(FieldUtil.convertSafeString(map,"SYSTEM_NAME"));
		vo.setSystemStaffCode(FieldUtil.convertSafeString(map,"SYSTEM_OWNER_CODE"));
		return vo;
	}

	public int delete(BaseVO vo) throws DAOException {
		SystemOwnerVO systemOwner = (SystemOwnerVO)vo;
		String delSQL = "delete from teflow_system_owner where system_id='"+systemOwner.getSystemId()+"'";
		dbManager.executeUpdate(delSQL);
		return 0;
	}
    
    /**
     * return 1 : primary key conflict
     * return 0 : save sucessfully
     */
	public int save(BaseVO vo) throws DAOException {
		SystemOwnerVO systemOwner = (SystemOwnerVO)vo;
		String selSQL = "select * from teflow_system_owner where system_id='"+systemOwner.getSystemId()+"'";
		Collection list = dbManager.query(selSQL);
		if(list!=null && list.size()>0) return 1;
		String sql = "insert into teflow_system_owner(system_id,system_name,system_owner_code) values('"+systemOwner.getSystemId()+"','"+systemOwner.getSystemName()
		              +"','"+systemOwner.getSystemStaffCode()+"')";
		dbManager.executeUpdate(sql);
		return 0;
	}

	public int update(BaseVO vo) throws DAOException {
	   SystemOwnerVO systemOwner = (SystemOwnerVO)vo;
	   String updateSQL = "update teflow_system_owner set system_name='" + systemOwner.getSystemName() + "',system_owner_code='"+
	                        systemOwner.getSystemStaffCode()+"' where system_id='"+systemOwner.getSystemId()+"'";
	   dbManager.executeUpdate(updateSQL);
	   return 0;
	}

}
