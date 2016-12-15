package com.aiait.eflow.wkf.dao;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0958	Young	11/01/2007	Add workflow query condition      */
/******************************************************************/
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.wkf.vo.WorkFlowItemVO;
import com.aiait.eflow.wkf.vo.WorkFlowVO;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class WorkFlowDefineDAO extends BaseDAOImpl {
	
	public WorkFlowDefineDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	public void copyFlow(int flowId)throws DAOException{
		Collection paramList = new ArrayList();
		paramList.add(""+flowId);
		dbManager.prepareCall("poef_wkf_copy_flow",paramList);
	}
	
	public int getLinkedFormNum(int formSystemId,int flowId)throws DAOException{
		String sql = "select count(*) from teflow_wkf_define where form_system_id="+formSystemId;
		if(flowId>0){
			sql = sql + " and flow_id<>"+flowId;
		}
		int num = dbManager.getRecordCount(sql);
		return num;
	}
	

	
	/**
	 * get the form's all node except begin and end node
	 * @param formSystemId
	 * @return
	 * @throws DAOException
	 */
	public Collection getNodeListByForm(int formSystemId)throws DAOException{
		StringBuffer sql = new StringBuffer("SELECT node_id, node_name FROM teflow_wkf_detail WHERE (node_id <> '0') AND (node_id <> '-1') ");
		sql.append(" and flow_id = (SELECT flow_id  FROM teflow_wkf_define where form_system_id=").append(formSystemId).append(")");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			WorkFlowItemVO node = new WorkFlowItemVO();
			HashMap map = (HashMap)it.next();
			node.setItemId(FieldUtil.convertSafeString(map,"NODE_ID"));
			node.setName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"NODE_NAME")));
			result.add(node);
		}
		return result;
	}
	
	public WorkFlowVO getWorkFlow(int flowId)throws DAOException{
		String sql = "select * from teflow_wkf_define where flow_id="+flowId;
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		WorkFlowVO flow = convertMapToVo(map);
		return flow;
	}
	
	public WorkFlowVO getWorkFlowByForm(int formSystemId)throws DAOException{
		String sql = "select * from teflow_wkf_define where form_system_id="+formSystemId;
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		WorkFlowVO flow = convertMapToVo(map);
		return flow;
	}
	//IT0958 new method begin
	public Collection getFlowByFormType(FormManageVO form, WorkFlowVO flw,String frmtp)throws DAOException{
		StringBuffer sql = new StringBuffer("select b.*,a.form_id from ");		
		if(form.getFormType()!=null && !"".equals(form.getFormType())){
			sql.append("(select form_system_id,form_id from teflow_form where form_type in("+form.getFormType()+") ");
		}
		if((frmtp==null || "".equals(frmtp)) && (form.getFormType()!=null && !"".equals(form.getFormType()))){
			sql.append(" UNION ALL select 0 as form_system_id,'' as form_id ");
		}else if(form.getFormType()==null || "".equals(form.getFormType())){
			sql.append(" (select 0 as form_system_id,'' as form_id ");
		}
		sql.append(") a left join teflow_wkf_define b on a.form_system_id = b.form_system_id ");
		sql.append(" where flow_id is not null ");
		if (flw.getFlowName()  != null && !"".equals(flw.getFlowName())){
			sql.append(" and b.flow_name like '%"+flw.getFlowName()+"%'");
		}		
		if(form.getOrgId()!=null && !"".equals(form.getOrgId())){
			sql.append(" and b.company_id in (").append(form.getOrgId()).append(") ");
		}
		sql.append("order by b.flow_name");			
		//System.out.println(sql.toString());
		
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			WorkFlowVO flow = convertMapToVo(map);
			resultList.add(flow);
		}
		return resultList;
	}
	//IT0958 end
	public Collection getAllFlow()throws DAOException{
		String sql = "select a.flow_id,a.flow_name,a.form_system_id,a.description,b.form_name,b.form_id,a.after_handle_url from teflow_wkf_define a LEFT OUTER JOIN teflow_form b ON a.form_system_id = b.form_system_id order by a.flow_name asc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			WorkFlowVO flow = convertMapToVo(map);
			resultList.add(flow);
		}
		return resultList;
	}
	
	private WorkFlowVO convertMapToVo(HashMap map){
		WorkFlowVO flow = new WorkFlowVO();
		flow.setFlowBaseId(Integer.parseInt((String)map.get("FLOW_ID")));
		flow.setFlowName(DataConvertUtil.convertISOToGBK((String)map.get("FLOW_NAME")));
		flow.setDescription(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"DESCRIPTION")));
		flow.setFormSystemId(FieldUtil.convertSafeInt(map,"FORM_SYSTEM_ID",0));
		flow.setFormId(FieldUtil.convertSafeString(map,"FORM_ID"));
		flow.setOrgId(FieldUtil.convertSafeString(map,"COMPANY_ID"));
		flow.setFormName(DataConvertUtil.convertISOToGBK(FieldUtil.convertSafeString(map,"FORM_NAME")));
		flow.setAfterHandleUrl(FieldUtil.convertSafeString(map,"AFTER_HANDLE_URL"));
		return flow;
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void deleteFlowBaseInfo(int flowId)throws DAOException{
		String delSQL = "delete from teflow_wkf_define where flow_id = " + flowId;
		dbManager.executeUpdate(delSQL);
	}

	public int save(BaseVO vo) throws DAOException {
		WorkFlowVO flow = (WorkFlowVO)vo;
		StringBuffer insertSQL = new StringBuffer("insert into teflow_wkf_define(flow_name,description,form_system_id,company_id,after_handle_url) ");
		insertSQL.append(" values(?,?,?,?,?)");
		Object[] obj = new Object[5];
		obj[0] = flow.getFlowName();
		obj[1] = flow.getDescription();
		obj[2] = ""+flow.getFormSystemId();
		obj[3] = flow.getOrgId();
		obj[4] = flow.getAfterHandleUrl();
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.INT,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(insertSQL.toString(),obj,dataType);	
		return 0;
	}

	public int update(BaseVO vo) throws DAOException {
		WorkFlowVO flow = (WorkFlowVO)vo;
		StringBuffer updateSQL = new StringBuffer("update teflow_wkf_define set flow_name=?,description=?,form_system_id=?,after_handle_url=?,company_id=? ");
		updateSQL.append(" where flow_id=?");
		Object[] obj = new Object[6];
		obj[0] = flow.getFlowName();
		obj[1] = flow.getDescription();
		obj[2] = ""+flow.getFormSystemId();
		obj[3] = flow.getAfterHandleUrl();
		obj[4] = flow.getOrgId();
		obj[5] = ""+flow.getFlowBaseId();
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.INT,DataType.VARCHAR,DataType.VARCHAR,DataType.INT};
		dbManager.executeUpdate(updateSQL.toString(),obj,dataType);	
		return 0;
	}

}
