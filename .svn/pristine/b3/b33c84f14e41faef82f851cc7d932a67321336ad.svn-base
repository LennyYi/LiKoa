package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.TitleVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class TitleDAO extends BaseDAOImpl {
	
	public TitleDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getActiveTitleList()throws DAOException{
		return getTitleList("Y");
	}
	
	public Collection getTitleList(String status)throws DAOException{
		Collection resultList = new ArrayList();
		String sql = "select * from tpma_title where 1=1 ";
		if(status!=null && !"".equals(status)){
			sql = sql + " and active='" + status + "'";
		}
		sql = sql + " order by abrev";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return resultList;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			TitleVO vo = new TitleVO();
			vo.setTitleId(FieldUtil.convertSafeInt(map,"TITLE_ID",0));
			vo.setAbrev(FieldUtil.convertSafeString(map,"ABREV"));
			vo.setDescription(FieldUtil.convertSafeString(map,"DESCRIPTION"));
			vo.setTitleLevel(FieldUtil.convertSafeString(map,"TITLE_LEVEL"));
			vo.setActive(FieldUtil.convertSafeString(map,"ACTIVE"));
			resultList.add(vo);
		}
		return resultList;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
