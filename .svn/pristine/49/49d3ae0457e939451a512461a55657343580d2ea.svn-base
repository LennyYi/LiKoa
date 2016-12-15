package com.aiait.eflow.formmanage.dao;

import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class DAOTest extends BaseDAOImpl {
	
	public DAOTest(IDBManager dbManager){
		super(dbManager);
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
	
	public void creatTable(String sql,String tableName)throws DAOException{
		dbManager.executeUpdate("if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].["+tableName+"]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)  drop table [dbo].["+tableName+"]");
		dbManager.executeUpdate(sql);
		
	}

}
