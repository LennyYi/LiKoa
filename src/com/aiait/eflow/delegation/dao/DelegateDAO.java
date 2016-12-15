package com.aiait.eflow.delegation.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.FormTypeHelper;
import com.aiait.eflow.delegation.vo.DelegationVO;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class DelegateDAO extends BaseDAOImpl {
	
	public DelegateDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	/**
	 * 取消当天已经到期的deputy设置记录，主要是提供给定时任务调用
	 * @throws DAOException
	 */
	public void revokeDeputyByTask()throws DAOException{
		String sql = "update teflow_deputy_manage set status='02' where status='01' and delegate_to<getdate() ";
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public Collection getAllDeputyHandle(DelegationVO queryVo, String orgid)throws DAOException{
	  String sql = "select a.request_no,a.current_processor,a.origin_processor,a.handle_date,b.node_name,c.form_system_id,d.org_id ";
	  sql = sql + " from teflow_wkf_process_trace a,teflow_wkf_detail b,teflow_wkf_define c, tpma_staffbasic d where is_deputy='1' and a.node_id = b.node_id and a.flow_id = b.flow_id"
	            + " and a.flow_id=c.flow_id and a.origin_processor=d.staff_code ";
	  
	  if(queryVo.getAuthorityApprover()!=null && !"".equals(queryVo.getAuthorityApprover())){
		  sql = sql + " and a.origin_processor like '%" + queryVo.getAuthorityApprover()+"%' ";
	  }
	  if(queryVo.getAuthorityDeputy()!=null && !"".equals(queryVo.getAuthorityDeputy())){
		  sql = sql + " and a.current_processor='" + queryVo.getAuthorityDeputy()+"' ";
	  }
	  if(queryVo.getHandledBeginDate()!=null && !"".equals(queryVo.getHandledBeginDate())){
		  if(queryVo.getHandledEndDate()!=null && !"".equals(queryVo.getHandledEndDate())){
			  sql = sql + " and a.handle_date between '"+queryVo.getHandledBeginDate()+" 00:00:00' and '" +queryVo.getHandledEndDate()+" 23:59:59' ";
		  }else{
		      sql = sql + " and a.handle_date>='" + queryVo.getHandledBeginDate()+" 00:00:00' ";
		  }
	  }else{
		  if(queryVo.getHandledEndDate()!=null && !"".equals(queryVo.getHandledEndDate())){
			  sql = sql + " and a.handle_date<='" + queryVo.getHandledEndDate()+" 23:59:59' ";
		  }
	  }
	  if(orgid!=null && !"".equals(orgid)){
		  sql = sql + " and d.org_id='" + orgid+"' ";
	  }
	  sql = sql + " order by a.handle_date desc";
	  Collection list = dbManager.query(sql);
	  if(list==null && list.size()==0) return new ArrayList();
	  Iterator it = list.iterator();
	  SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Collection resulList = new ArrayList();
	  while(it.hasNext()){
		  HashMap map = (HashMap)it.next();
		  WorkFlowProcessTraceVO trace = new WorkFlowProcessTraceVO();
		  trace.setRequestNo(FieldUtil.convertSafeString(map,"REQUEST_NO"));
		  trace.setHandleStaffCode(FieldUtil.convertSafeString(map,"CURRENT_PROCESSOR"));
		  trace.setOriginProcessor(FieldUtil.convertSafeString(map,"ORIGIN_PROCESSOR"));
		  try{
			  Date cDate = df.parse((String)map.get("HANDLE_DATE"));
			  trace.setHandleDateStr(StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"));
		  }catch(Exception e){
			  trace.setHandleDateStr((String)map.get("HANDLE_DATE"));
		  }
		  trace.setFormSystemId(FieldUtil.convertSafeInt(map,"FORM_SYSTEM_ID",0));
		  trace.setCurrentNodeId(FieldUtil.convertSafeString(map,"NODE_NAME"));
		  trace.setFilePathName(FieldUtil.convertSafeString(map,"ORG_ID"));
		  resulList.add(trace);
	  }
	  return resulList;	
	}
	
	public Collection getToStaffList(String teamCode)throws DAOException{
		String sql = "select staff_code,staff_name from tpma_staffbasic where status='A' ";
		if(teamCode!=null && !"".equals(teamCode)){
			sql = sql + " and team_code=" + teamCode;
		}
		sql = sql + " order by staff_name";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		return covert(list);
	}
	
	public Collection getFromStaffList(String teamCode)throws DAOException{
		//String sql = "select staff_code,staff_name from tpma_staffbasic where status='A' and staff_code not in (select authority_approver from "
		//	        +" teflow_deputy_manage where status='01') ";
		String sql = "select staff_code,staff_name from tpma_staffbasic where status='A'  ";
		if(teamCode!=null && !"".equals(teamCode)){
			sql = sql + " and team_code=" + teamCode;
		}
		sql = sql + "  order by staff_name ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return new ArrayList();
		return covert(list);
	}
	
	private Collection covert(Collection list){
		Collection resulList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			StaffVO staff = new StaffVO();
			staff.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			staff.setStaffName(FieldUtil.convertSafeString(map,"STAFF_NAME"));
			resulList.add(staff);
		}
		return resulList;
	}
	
	public Collection getList(DelegationVO queryVo, String orgid)throws DAOException{
		String selSql = "select * from teflow_deputy_manage a, tpma_team b where a.approver_team=b.team_code ";
		if(queryVo.getStatus()!=null && !"".equals(queryVo.getStatus())){
			selSql = selSql + " and a.status='" + queryVo.getStatus() +"'";
		}
		if(orgid!=null && !"".equals(orgid)){
			selSql = selSql + " and b.org_id='" + orgid +"'";
		}
		if(queryVo.getHandledBeginDate()!=null && !"".equals(queryVo.getHandledBeginDate())){
			if(queryVo.getHandledEndDate()!=null && !"".equals(queryVo.getHandledEndDate())){
			  selSql = selSql + " and a.delegate_from between '"+queryVo.getHandledBeginDate()+" 00:00:00' and '" +queryVo.getHandledEndDate()+" 23:59:59' ";
			}else{
		      selSql = selSql + " and a.delegate_from>='" + queryVo.getHandledBeginDate()+" 00:00:00' ";
			}
		}else{
			if(queryVo.getHandledEndDate()!=null && !"".equals(queryVo.getHandledEndDate())){
				selSql = selSql + " and a.delegate_from<='" + queryVo.getHandledEndDate()+" 23:59:59' ";
			}
		}
		selSql = selSql + " order by a.actived_date";
		Collection list = dbManager.query(selSql);
		if(list==null || list.size()==0) return new ArrayList();
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while(it.hasNext()){
			DelegationVO vo = new DelegationVO();
			HashMap map = (HashMap)it.next();
			FormManageDAO formDAO = new FormManageDAO(dbManager);
			vo.setId(FieldUtil.convertSafeInt(map,"ID",0));
			vo.setAuthorityApprover(FieldUtil.convertSafeString(map,"AUTHORITY_APPROVER"));
			vo.setAuthorityDeputy(FieldUtil.convertSafeString(map,"AUTHORITY_DEPUTY"));
			try{
			  Date cDate = df.parse((String)map.get("DELEGATE_FROM"));
			  vo.setDelegateFromStr(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
			}catch(Exception e){
			  vo.setDelegateFromStr((String)map.get("DELEGATE_FROM"));
			}
			try{
			   Date cDate = df.parse((String)map.get("DELEGATE_TO"));
			   vo.setDelegateToStr(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
			}catch(Exception e){
				  vo.setDelegateToStr((String)map.get("DELEGATE_TO"));
			}
			vo.setApproverTeamCode(FieldUtil.convertSafeString(map,"APPROVER_TEAM"));
			vo.setDeputyTeamCode(FieldUtil.convertSafeString(map,"DEPUTY_TEAM"));
			vo.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
			vo.setActivedBy(FieldUtil.convertSafeString(map,"ACTIVED_BY"));
			try{
				   Date cDate = df.parse((String)map.get("ACTIVED_DATE"));
				   vo.setActivedDateStr(StringUtil.getDateStr(cDate,"MM/dd/yyyy"));
		    }catch(Exception e){
					vo.setActivedDateStr((String)map.get("ACTIVED_DATE"));
		    }
		    vo.setOrgId((String)map.get("ORG_ID"));
		    vo.setDelegateLevel((String)map.get("DELEGATE_LEVEL"));
		    vo.setFormTypeId((String)map.get("FORM_TYPE_ID"));
		    vo.setFormSystemId((String)map.get("FORM_SYSTEM_ID"));
		    vo.setApplyOrgId((String)map.get("APPLY_ORG_ID"));
		    StringBuffer tmp = new StringBuffer("");
		    if(vo.getDelegateLevel().indexOf("1")>=0){
		    	tmp.append("All Forms");
		    }else{
			    if(vo.getDelegateLevel().indexOf("2")>=0){
			    	String[] formTypeIds = vo.getFormTypeId().split(",");
				    for(int i=0; i<formTypeIds.length;i++){
				    	if("".equals(formTypeIds[i].trim()))continue;
				    	tmp.append("All "+FormTypeHelper.getInstance().getFormTypeName(formTypeIds[i])+" <br>");
				    }
			    }
			    if(vo.getDelegateLevel().indexOf("3")>=0){
			    	String[] formSystemIds = vo.getFormSystemId().split(",");
				    for(int i=0; i<formSystemIds.length;i++){			    	
				    	try{
				    		tmp.append(""+((FormManageVO)formDAO.getFormBaseInforBySystemId(Integer.parseInt(formSystemIds[i]))).getFormId()+" <br>");
				    	}catch(NumberFormatException ex){}
				    }
			    }
		    }
			if(vo.getApplyOrgId()!=null && !"".equals(vo.getApplyOrgId().trim())){
				tmp.append(" @");
			    String[] orgs = vo.getApplyOrgId().split(",");
			    for(String org:orgs){
			    	tmp.append(" ["+CompanyHelper.getInstance().getOrgName(org)+"] ");
			    }
			}
		    vo.setScopeStr(tmp.toString());
		    result.add(vo);
		}
		return result;
	}

	public int delete(BaseVO arg0) throws DAOException {
		DelegationVO vo = (DelegationVO)arg0;
		String sql = "delete from teflow_deputy_manage where id=" + vo.getId();
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	public int save(BaseVO arg0) throws DAOException {
		DelegationVO vo = (DelegationVO)arg0;
		StringBuffer sql = new StringBuffer("insert into teflow_deputy_manage(authority_approver,authority_deputy,delegate_from,delegate_to,approver_team,deputy_team,status,actived_by,actived_date," +
				"delegate_level, form_type_id, form_system_id, apply_org_id) ");
		sql.append(" values('").append(vo.getAuthorityApprover()).append("','").append(vo.getAuthorityDeputy()).append("','")
		   .append(vo.getDelegateFromStr()).append("','").append(vo.getDelegateToStr()).append("','")
		   .append(vo.getApproverTeamCode()).append("','").append(vo.getDeputyTeamCode()).append("','")
		   .append(vo.getStatus()).append("','").append(vo.getActivedBy()).append("',getdate(),'")
		   .append(vo.getDelegateLevel()).append("','").append(vo.getFormTypeId()).append("','").append(vo.getFormSystemId()).append("','")
		   .append(vo.getApplyOrgId()).append("')");
		dbManager.executeUpdate(sql.toString());
		return 0;
	}
	
	/**
	 * @param fromDate
	 * @param staffCode
	 * @param level	：1=全部，2=表单类型，3=单个表单；混合level时用逗号分隔
	 * @return
	 * @throws DAOException
	 */
	public int checkBeginDate(String fromDate,String staffCode,String level, String formType, String formSystemId, String orgId)throws DAOException{
		String sql = "select count(*) from teflow_deputy_manage where authority_approver='"+staffCode+"' and status='01' and '"+fromDate+"' between delegate_from and delegate_to "
					+ " and delegate_level like '%"+level+"%' ";	//注意delegate_level必须是个位数
		if(formType!=null)
					sql += " and form_type_id like '%,"+formType+",%'";
		if(formSystemId!=null)
					sql += " and form_system_id like '%,"+formSystemId+",%'";
		if(orgId!=null)
					sql += " and apply_org_id like '%"+orgId+"%'";
		int num = dbManager.getRecordCount(sql);
		return num;
	}
	/**
	 * update status
	 */
	public int update(BaseVO arg0) throws DAOException {
		DelegationVO vo = (DelegationVO)arg0;
		String sql = "update teflow_deputy_manage set status='"+vo.getStatus()+"',actived_by='"+vo.getActivedBy()+"',actived_date=getdate()"
		            +" where id="+vo.getId();
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	public String getStaffsByDeputy(String staffs, String processor) {
        String inStaffs = "'" + staffs.replaceAll(",", "','") + "'";
        String SQL = "select authority_approver as approver, authority_deputy as deputy from teflow_deputy_manage "
                + "where (convert(varchar(10), Getdate(), 111) between delegate_from and delegate_to) "
                + "and (status = '01') and (authority_approver in (" + inStaffs + "))";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String approver = rs.getString("approver");
                String deputy = rs.getString("deputy");
                if (deputy.equalsIgnoreCase(processor)) {
                    // ignore if the processor is deputy
                    continue;
                }
                staffs = staffs.replaceFirst(approver, approver + "/" + deputy);
            }
            return staffs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
	//overload, to enable different BU deputys for same original approver
	public String getStaffsByDeputy(String staffs, String processor, String orgId, int formSystemId) {
        String inStaffs = "'" + staffs.replaceAll(",", "','") + "'";
        String SQL = "select authority_approver as approver, authority_deputy as deputy from teflow_deputy_manage "
                + "where (convert(varchar(10), Getdate(), 111) between delegate_from and delegate_to) "
                + " and (status = '01') and (authority_approver in (" + inStaffs + ")) and (isnull(apply_org_id,'')='' or apply_org_id like '%"+orgId+"%') " 
                + " and (delegate_level='1' or form_system_id like '%,"+formSystemId+",%')";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String approver = rs.getString("approver");
                String deputy = rs.getString("deputy");
                if (deputy.equalsIgnoreCase(processor)) {
                    // ignore if the processor is deputy
                    continue;
                }
                staffs = staffs.replaceFirst(approver, approver + "/" + deputy);
            }
            return staffs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
	public String getDeputyStaffCode(String staff) {
		
        String SQL = "select authority_approver as approver, authority_deputy as deputy from teflow_deputy_manage "
                + "where (convert(varchar(10), Getdate(), 111) between delegate_from and delegate_to) "
                + "and (status = '01') and (authority_approver ='" + staff + "')";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String deputy = rs.getString("deputy");
                return deputy;
            }
            return staff;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
