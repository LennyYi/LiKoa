package com.aiait.eflow.wkf.dao;

import java.util.Collection;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class PersonalWorkDAO extends BaseDAOImpl {
	
	public PersonalWorkDAO(IDBManager dbManager){
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
    
	/**
	 * to get count of form which status='0'
	 * @return
	 * @throws DAOException
	 */
	public int getPublishedFormCount(String orgIds)throws DAOException{
		String sql = "select count(a.form_system_id) from teflow_wkf_define a,teflow_form b where a.form_system_id=b.form_system_id and a.form_system_id>0 and b.status='0' ";
		if(orgIds!=null && !"".equals(orgIds)){
			sql = sql + " and b.org_id in (" + orgIds +") ";
		}
        int count = dbManager.getRecordCount(sql);
        return count;
	}
	/**
	 * get the count of form which need handle by staffCode
	 * @param staffCode
	 * @return
	 * @throws DAOException
	 */
    public int getNeedHandleFormCount(String staffCode)throws DAOException{
    	String sql = "select count(*) from teflow_wkf_process where ','+rtrim(current_processor)+',' like '%,"+staffCode+",%' and status<>'00' and status<>'03'";
        int count = dbManager.getRecordCount(sql);
        return count;
    }
    
    public int getCompletedFormCount()throws DAOException{
    	String sql = "select count(*) from teflow_wkf_process where  status='04'";
        int count = dbManager.getRecordCount(sql);
        return count;
    }
    
    public int getCanQueryFormCount(String formTypes, String orgIds) throws DAOException {
        String sql = "select count(a.request_no) from teflow_wkf_process a, teflow_wkf_define b, teflow_form c "
                + "where a.flow_id = b.flow_id and b.form_system_id = c.form_system_id " + "and c.form_type in("
                + formTypes + ")";
        if (orgIds != null && !"".equals(orgIds)) {
            sql = sql + " and c.org_id in (" + orgIds + ")";
        }
        long timeStart = System.currentTimeMillis();
        int count = dbManager.getRecordCount(sql);
        boolean logTime = "yes".equalsIgnoreCase(ParamConfigHelper.getInstance().getParamValue(
                CommonName.PARAM_LOG_TIME));
        if (logTime) {
            System.out.println("personalWorkIndex - CanQueryFormCount SQL: " + sql);
            System.out.println("personalWorkIndex - CanQueryFormCount Time: "
                    + (System.currentTimeMillis() - timeStart));
        }
        return count;
    }
    
    public int getOvertimeFormCount()throws DAOException{
    	StringBuffer sql = new StringBuffer("SELECT count(a.request_no) ");
		sql.append("  from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN " +
				" teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id AND a.node_id = d.node_id " +
				" WHERE Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0),2) < 0 and d.limited_hours>0");
        int count = dbManager.getRecordCount(sql.toString());
        return count;
    }
    
    public int getOvertimeFormCount(WorkFlowProcessVO queryVo) throws DAOException {
        StringBuffer sql = new StringBuffer(
                "SELECT count(a.request_no) from teflow_wkf_process a INNER JOIN teflow_wkf_define b ON a.flow_id = b.flow_id INNER JOIN "
                        + "teflow_form c ON b.form_system_id = c.form_system_id INNER JOIN teflow_wkf_detail d ON a.flow_id = d.flow_id AND a.node_id = d.node_id "
                        + "WHERE a.status <> '00' and a.status <> '03' and Round(((d.limited_hours * 60 - DATEDIFF(minute, a.receiving_date, GETDATE()))/60.0), 2) < 0 and d.limited_hours > 0");

        if (queryVo.getFormType() != null && !"".equals(queryVo.getFormType())) {
            sql.append(" and c.form_type in (" + queryVo.getFormType() + ")");
        }
        // System.out.println("sql:" + sql.toString());
        int count = dbManager.getRecordCount(sql.toString());
        return count;
    }

}
