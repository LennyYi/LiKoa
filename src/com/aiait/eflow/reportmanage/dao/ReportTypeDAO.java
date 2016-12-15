package com.aiait.eflow.reportmanage.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.reportmanage.vo.ReportTypeVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class ReportTypeDAO extends BaseDAOImpl {
	
	public ReportTypeDAO(IDBManager dbManager){
		super(dbManager);
	}

	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		ReportTypeVO vo = (ReportTypeVO)arg0;
		String sql1 = "insert into teflow_report_type(report_type_id,report_type_name,description) values('"
			        +vo.getReportTypeId()+"','"+vo.getReportTypeName()+"','"+vo.getDescription()+"') ";
		dbManager.executeUpdate(sql1);
		
		/******************** IT1177 New form type automation enhancement 2010/09 Begin ***********************/
		ParamConfigHelper param = ParamConfigHelper.getInstance();
		String[] reportTypeModule = param.getParamValue("report_type_related_modules").split(";");
		
		for(int i=0;i<reportTypeModule.length;i++){
			
			String sql2 = "select max(operate_id) V1 from teflow_module_operate where module_id = "+reportTypeModule[i];
			Collection tempList = dbManager.query(sql2);
			
			Iterator it = tempList.iterator();
			int maxOprID = 0;
			if(it.hasNext()){
				HashMap map = (HashMap)it.next();
				maxOprID = Integer.parseInt((String) map.get("V1")) + 1;
			} else {
				continue;
			}
			
			String sql3 = "insert into teflow_module_operate(operate_id, module_id, operate_name, remark, report_type_id) " +
				"values("+maxOprID+", "+reportTypeModule[i]+", '"+vo.getReportTypeName()+"', null, '"+vo.getReportTypeId()+"')";
			dbManager.executeUpdate(sql3);

			String sql4 = "insert into teflow_role_module_operate(role_id,module_id,operate_id) values(" +
					"'01',"+reportTypeModule[i]+","+maxOprID+")";//grant authority to Admin first
			dbManager.executeUpdate(sql4);			
		}
		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		ReportTypeVO vo = (ReportTypeVO)arg0;
		String sql = "update teflow_report_type set report_type_name='" + vo.getReportTypeName()+"',description='"
		             +vo.getDescription()+"' where report_type_id='" + vo.getReportTypeId()+"' ";
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	public ReportTypeVO getReportType(String typeCode)throws DAOException{
		String sql = "select * from teflow_report_type where report_type_id = '" + typeCode + "' ";
		Collection tempList = dbManager.query(sql);
		if(tempList==null || tempList.size()==0) return null;
		HashMap map = (HashMap)tempList.iterator().next();
		return convertMapToVO(map);
	}
	
	public Collection getReportTypeList()throws DAOException{
		Collection list = new ArrayList();
		String sql = "select * from teflow_report_type where 1=1 order by report_type_id";
		Collection tempList = dbManager.query(sql);
		if(tempList==null || tempList.size()==0) return list;
		Iterator it = tempList.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			ReportTypeVO vo = convertMapToVO(map);
			list.add(vo);
		}
		return list;
	}
	
	private ReportTypeVO convertMapToVO(HashMap map){
		ReportTypeVO vo = new ReportTypeVO();
		vo.setReportTypeId(FieldUtil.convertSafeString(map,"FORM_TYPE_ID"));
		vo.setReportTypeName(FieldUtil.convertSafeString(map,"FORM_TYPE_NAME"));
		vo.setDescription(FieldUtil.convertSafeString(map,"DESCRIPTION"));
		return vo;
	}

}
