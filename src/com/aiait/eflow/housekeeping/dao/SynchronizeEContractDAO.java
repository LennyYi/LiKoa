package com.aiait.eflow.housekeeping.dao;

/**
 * 该类用来实现与eContract系统的数据同步 (1.读eFlow内存，写eContract DB；2.读eContract DB，写入eFlow页面)
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class SynchronizeEContractDAO extends BaseDAOImpl {
	
	public SynchronizeEContractDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	// 新供应商
	public void newContract(String requestNo)throws DAOException{
		
		String econtractDBName = ParamConfigHelper.getInstance().getParamValue("ECONTRACT_DB");
		String econtractFormSystemID = ParamConfigHelper.getInstance().getParamValue("ECONTRACT_FORM_ID");
		String econtractSectionID = ParamConfigHelper.getInstance().getParamValue("ECONTRACT_SECTION_ID");
		
//		String sql0 = "select gen_value +1 as newId from "+ econtractDBName
//				+ ".dbo.tctr_id_generator where gen_name='vender_id'";

 		String sql1 = "select field_comments as col_name, field_id from teflow_form_section_field where " 
 				+ " form_system_id='"+ econtractFormSystemID +"' and section_id='"+ econtractSectionID +"'";

		String sqlN = "update "+ econtractDBName + ".dbo.tctr_id_generator set  gen_value = gen_valeu +1  where gen_name='vender_id'";
		
		Collection<HashMap> list = dbManager.query(sql1);
		Map<String, String> fieldMap = new HashMap<String, String>();
		for(HashMap map:list){
			String colName = (String)map.get("COL_NAME");
			String fieldId = (String)map.get("FIELD_ID");
			fieldMap.put(colName, fieldId);
		}
		String sql2a = "insert into "+econtractDBName+".dbo.tctr_vender([id] ";
		String sql2b = " select (select gen_value +1 as newId from "+ econtractDBName + ".dbo.tctr_id_generator where gen_name='vendor_id') ";
		Iterator<Map.Entry<String, String>> it = fieldMap.entrySet().iterator();
		while(it.hasNext()){
			String colName = (String)((Map.Entry)it.next()).getKey();
			String fieldId = (String)((Map.Entry)it.next()).getValue();
			sql2a = sql2a + "," + colName;
			sql2b = sql2b + "," + fieldId;
		}
		sql2a = sql2a + ") ";
		sql2b = sql2b + " from teflow_82_3 where request_no='"+requestNo+"'";
		String sql2 = sql2a + sql2b;

		try{
			dbManager.startTransaction();
			dbManager.executeUpdate(sql2);
			dbManager.executeUpdate(sqlN);
			dbManager.commit();
		}catch(DAOException e){
			dbManager.rollback();
			throw e;
		}
	}
	// 修改供应商信息
	public void updateContract(String requestNo)throws DAOException{
		
		String sql = "select team_code from "+ParamConfigHelper.getInstance().getParamValue("PMA_DB")
		+".dbo.tpma_team where is_dept='Y' and status='A'";
	/*	String sql = "insert into teflow_approver_group_member(approver_group_id,staff_code) select '02',staff_code from  tpma_StaffBasic "
			        +" where status = 'A' and staff_code NOT IN (select  staff_code from teflow_approver_group_member where  approver_group_id ='02') "
			        +" and logon_id IN (select tl_id from tpma_team where status = 'A') ";*/

		String sql1 = "delete teflow_approver_group_member where approver_group_id in('02','15','34')";//TL1 and Resource Owner

		try{
			dbManager.startTransaction();
			dbManager.executeUpdate(sql1);
			dbManager.commit();
		}catch(DAOException e){
			dbManager.rollback();
			throw e;
		}
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
