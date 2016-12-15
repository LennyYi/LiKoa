package com.aiait.eflow.report.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.report.vo.OverdueSummaryVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class OverdueSummaryDAO extends BaseDAOImpl{
	
	public OverdueSummaryDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public Collection overdueSummary(OverdueSummaryVO vo, int opt1, int opt2)throws DAOException
	{	
		StringBuffer sql = new StringBuffer("");
		Collection result = new ArrayList();
		
		//sql for overdue_forms -- table
		if(opt1 == 1 && opt2 == 1)
		{
			sql.append("select count(overdue_hours) as overdue_forms,current_processor as handle_by from teflow_wkf_process_trace ");
			sql.append("where flow_id in (select flow_id from teflow_wkf_define where form_system_id=").append(vo.getFormSystemId())
			   .append(") and node_id='").append(vo.getNodeId()).append("' and overdue_hours < 0 ");			
			String endDate = ""; 
			if(vo.getEndDate()!=null && !"".equals(vo.getEndDate())){
				endDate = vo.getEndDate() + " 23:59:59";
			}		
			if(vo.getBeginDate()!=null && !"".equals(vo.getBeginDate())){
				if(!"".equals(endDate)){
				  sql.append(" and handle_date between '").append(vo.getBeginDate()+" 00:00:00' and '").append(endDate).append("'");
				}else{
				  sql.append(" and handle_date >='").append(vo.getBeginDate()+" 00:00:00'");	
				}
			}else{
				if(!"".equals(endDate)){
					sql.append(" and handle_date<='").append(endDate).append("'");
				}
			}
			sql.append("group by current_processor");
			System.out.println("sql:"+sql.toString());
			
			Collection list = dbManager.query(sql.toString());
			//Collection result = new ArrayList();
			if(list==null || list.size()==0) return result;
			Iterator it = list.iterator();
			while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				OverdueSummaryVO summaryVo = new OverdueSummaryVO();
				summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map,"HANDLE_BY"));
				summaryVo.setOverdueForms(FieldUtil.convertSafeInt(map,"OVERDUE_FORMS",0));
				result.add(summaryVo);
			}
		}
		//sql for overdue hours -- table
		else if(opt1 == 2 && opt2 == 1)
		{
			sql.append("select sum(overdue_hours) as overdue_hours,current_processor as handle_by from teflow_wkf_process_trace ");
			sql.append("where flow_id in (select flow_id from teflow_wkf_define where form_system_id=").append(vo.getFormSystemId())
			   .append(") and node_id='").append(vo.getNodeId()).append("' and overdue_hours < 0 ");			
			String endDate = ""; 
			if(vo.getEndDate()!=null && !"".equals(vo.getEndDate())){
				endDate = vo.getEndDate() + " 23:59:59";
			}		
			if(vo.getBeginDate()!=null && !"".equals(vo.getBeginDate())){
				if(!"".equals(endDate)){
				  sql.append(" and handle_date between '").append(vo.getBeginDate()+" 00:00:00' and '").append(endDate).append("'");
				}else{
				  sql.append(" and handle_date >='").append(vo.getBeginDate()+" 00:00:00'");	
				}
			}else{
				if(!"".equals(endDate)){
					sql.append(" and handle_date<='").append(endDate).append("'");
				}
			}
			sql.append("group by current_processor");
			Collection list = dbManager.query(sql.toString());
			//Collection result = new ArrayList();
			if(list==null || list.size()==0) return result;
			Iterator it = list.iterator();
			while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				OverdueSummaryVO summaryVo = new OverdueSummaryVO();
				summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map,"HANDLE_BY"));
				summaryVo.setOverdueHours(FieldUtil.convertSafeFloat(map,"OVERDUE_HOURS",0));
				result.add(summaryVo);
			}
		}
		//sql for overdue forms -- graphic
		else if(opt1 == 1 && opt2 == 2)
		{
			sql.append("select count(overdue_hours) as overdue_forms,current_processor as handle_by from teflow_wkf_process_trace ");
			sql.append("where flow_id in (select flow_id from teflow_wkf_define where form_system_id=").append(vo.getFormSystemId())
			   .append(") and node_id='").append(vo.getNodeId()).append("' and overdue_hours < 0 ");			
			String endDate = ""; 
			if(vo.getEndDate()!=null && !"".equals(vo.getEndDate())){
				endDate = vo.getEndDate() + " 23:59:59";
			}		
			if(vo.getBeginDate()!=null && !"".equals(vo.getBeginDate())){
				if(!"".equals(endDate)){
				  sql.append(" and handle_date between '").append(vo.getBeginDate()+" 00:00:00' and '").append(endDate).append("'");
				}else{
				  sql.append(" and handle_date >='").append(vo.getBeginDate()+" 00:00:00'");	
				}
			}else{
				if(!"".equals(endDate)){
					sql.append(" and handle_date<='").append(endDate).append("'");
				}
			}
			sql.append("group by current_processor");
			
			Collection list = dbManager.query(sql.toString());
			//Collection result = new ArrayList();
			if(list==null || list.size()==0) return result;
			Iterator it = list.iterator();
			while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				OverdueSummaryVO summaryVo = new OverdueSummaryVO();
				summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map,"HANDLE_BY"));
				summaryVo.setOverdueForms(FieldUtil.convertSafeInt(map,"OVERDUE_FORMS",0));
				result.add(summaryVo);
			}
		}
		//sql for overdue hours -- graphic
		else if(opt1 == 2 && opt2 == 2)
		{
			sql.append("select sum(overdue_hours) as overdue_hours,current_processor as handle_by from teflow_wkf_process_trace ");
			sql.append("where flow_id in (select flow_id from teflow_wkf_define where form_system_id=").append(vo.getFormSystemId())
			   .append(") and node_id='").append(vo.getNodeId()).append("' and overdue_hours < 0 ");			
			String endDate = ""; 
			if(vo.getEndDate()!=null && !"".equals(vo.getEndDate())){
				endDate = vo.getEndDate() + " 23:59:59";
			}		
			if(vo.getBeginDate()!=null && !"".equals(vo.getBeginDate())){
				if(!"".equals(endDate)){
				  sql.append(" and handle_date between '").append(vo.getBeginDate()+" 00:00:00' and '").append(endDate).append("'");
				}else{
				  sql.append(" and handle_date >='").append(vo.getBeginDate()+" 00:00:00'");	
				}
			}else{
				if(!"".equals(endDate)){
					sql.append(" and handle_date<='").append(endDate).append("'");
				}
			}
			sql.append("group by current_processor");
			Collection list = dbManager.query(sql.toString());
			//Collection result = new ArrayList();
			if(list==null || list.size()==0) return result;
			Iterator it = list.iterator();
			while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				OverdueSummaryVO summaryVo = new OverdueSummaryVO();
				summaryVo.setHandleStaffCode(FieldUtil.convertSafeString(map,"HANDLE_BY"));
				summaryVo.setOverdueHours(FieldUtil.convertSafeFloat(map,"OVERDUE_HOURS",0));
				result.add(summaryVo);
			}
		}
		
		return result;
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
