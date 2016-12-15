package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.OperateLogVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class OperateLogDAO extends BaseDAOImpl {
	
	public OperateLogDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection getAllLog()throws DAOException{
		return getLogByType(null);
	}
	
	public Collection getLogByType(String operateType)throws DAOException{
		String sql = "select * from teflow_operate_log where 1=1 ";
		if(operateType!=null && !"".equals(operateType)){
			sql = sql + " and operate_type='" + operateType + "'";
		}
		sql = sql + " order by operate_date desc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		ArrayList resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			OperateLogVO vo = new OperateLogVO();
			vo.setId(FieldUtil.convertSafeInt(map,"ID",0));
			vo.setOperateStaffCode(FieldUtil.convertSafeString(map,"OPERATE_STAFF_CODE"));
			vo.setOperateDateStr(FieldUtil.convertSafeString(map,"OPERATE_DATE"));
		    vo.setOperateDescription(FieldUtil.convertSafeString(map,"OPERATE_DESCRIPTION"));
		    vo.setRemark(FieldUtil.convertSafeString(map,"REMARK"));
		    resultList.add(vo);
		}
		return resultList;
	}

	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO obj) throws DAOException {
		OperateLogVO vo = (OperateLogVO)obj;
		StringBuffer sql = new StringBuffer("insert into teflow_operate_log(operate_staff_code,operate_date,operate_type,operate_description,remark) values(?,?,?,?,?)");
		
		int[] dataType = new int[5];
		Object[] parameters = new Object[5];
		
		parameters[0] = vo.getOperateStaffCode();
		dataType[0] = DataType.VARCHAR;
		try{
		 parameters[1] = StringUtil.stringToSqlDate(vo.getOperateDateStr(),"MM/dd/yyyy HH:mm:ss");
		}catch(Exception e){}
		dataType[1] = DataType.DATE;		
		parameters[2] = vo.getOperateType();
		dataType[2] = DataType.VARCHAR;
		parameters[3] = vo.getOperateDescription();
		dataType[3] = DataType.VARCHAR;
		parameters[4] = vo.getRemark();
		dataType[4] = DataType.VARCHAR;
		
		dbManager.executeUpdate(sql.toString(),parameters,dataType);
		
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}


}
