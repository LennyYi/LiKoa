package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.OptionVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class OptionDAO extends BaseDAOImpl {
	
	public OptionDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getOptionList()throws DAOException{
		String sql = "select * from teflow_option_value order by field_id ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
           HashMap map = (HashMap)it.next();
           OptionVO vo = new OptionVO();
           vo.setFieldId(FieldUtil.convertSafeString(map,"FIELD_ID"));
           vo.setOptionLabel(FieldUtil.convertSafeString(map,"OPTION_LABEL"));
           vo.setOptionValue(FieldUtil.convertSafeString(map,"OPTION_VALUE"));
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

}
