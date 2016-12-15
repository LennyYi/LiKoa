package com.aiait.eflow.housekeeping.dao;

/**
 * IT1002 DS-001 从check in/ou系统中获取OT Record的表中
 * @author asnpgj3 Robin Hou
 * @date  03/10/2008
 */
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT1002	Robin	04/15/2008	DS-002 从check in/ou系统中获取OT Record的表中*/
/*IT1002	Young	04/16/2008	DS-002 reject form*/
/*IT1002	Young	04/17/2008	DS-003 personal ot sum
 *IT1092	Queenie	10/19/2009	Change OT exception case*/
/******************************************************************/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.CheckInoutVO;
import com.aiait.eflow.housekeeping.vo.OTRecordVO;
import com.aiait.eflow.housekeeping.vo.OTSummaryCheckVO;
import com.aiait.eflow.housekeeping.vo.OTSummaryVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class OTRecordDAO extends BaseDAOImpl {
	
	public OTRecordDAO(IDBManager dbManager){
		super(dbManager);
	}
	

	/**
	 * 获取指定员工指定日期的打卡记录
	 * @param otPlanDate
	 * @param requestStaffName
	 * @return
	 * @throws DAOException
	 */
	public HashMap getCheckInoutRecords(String otPlanDate,String requestStaffCode)throws DAOException{
		if(requestStaffCode==null) return null;
		String sql = "select * from tpma_checkinout where workdate_c in ("+otPlanDate+") and staffcode = '"+
		requestStaffCode.trim()+"' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap resultMap = new HashMap();
		Iterator it = list.iterator();
		while(it.hasNext()){
			CheckInoutVO vo = new CheckInoutVO();
			HashMap map = (HashMap)it.next();
			vo.setOtDate(FieldUtil.convertSafeString(map,"WORKDATE_C"));
			vo.setArrivalTime(FieldUtil.convertSafeString(map,"ARRIVAL"));
			vo.setLeaveTime(FieldUtil.convertSafeString(map,"LEAVE"));
			resultMap.put(FieldUtil.convertSafeString(map,"WORKDATE_C"),vo);
		}
		return resultMap;
	}
	
	/**
	 * 获取指定员工的还未报销的滞后记录数
	 * @param requestStaffCode
	 * @return
	 * @throws DAOException
	 */
	public int getDelayingNum(String requestStaffCode)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "select count(a.ot_plan_date) from "+ saveOtRecordTable + " a,teflow_wkf_process b where a.request_no=b.request_no and a.ot_plan_date<b.submission_date "
		            +" and b.request_staff_code='" + requestStaffCode+"' and b.request_no not in (select request_no from teflow_ot_apply_monthly_his) ";
		  int count = dbManager.getRecordCount(sql);
		  return count;
	}
	
	public int getOTRecord(String otPlanDate,String requestStaffCode,String requestNo,String exceptionalCase)throws DAOException{
	  String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
	  String sql = "select count(ot_plan_date) from " + saveOtRecordTable + " where request_no in("
      +" select request_no from teflow_wkf_process where request_staff_code ='" + requestStaffCode +"' "
      + " and (status<>'00' or status<>'04'))"
      +" and ot_plan_date = '" + otPlanDate + "' and request_no<>'"+requestNo+"' ";
	  sql = sql + " and isnull(is_exceptional_case,'')='" + exceptionalCase +"'";
	//  if(isExceptionalCase){
	//	  sql = sql + " and isnull(is_exceptional_case,'')='01' ";
	//  }else{
	//	  sql = sql + " and isnull(is_exceptional_case,'')<>'01' ";
	//  }
	 
	  int count = dbManager.getRecordCount(sql);
	  return count;
	}
	
	public int getOTRecord(String otPlanDate,String requestStaffCode,String requestNo)throws DAOException{
		  String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		  String sql = "select count(ot_plan_date) from " + saveOtRecordTable + " where request_no in("
	      +" select request_no from teflow_wkf_process where request_staff_code ='" + requestStaffCode +"' "
	      + " and (status<>'00' or status<>'04'))"
	      +" and ot_plan_date = '" + otPlanDate + "' and request_no<>'"+requestNo+"' ";
		 
		  int count = dbManager.getRecordCount(sql);
		  return count;
		}
	
	/**
	 * 检查某个员工是否已经申请过otPlanDate这天的加班
	 * @param otPlanDate
	 * @param requestStaffCode
	 * @return true ---- not existed; false ---- existed;
	 * @throws DAOException
	 */
	public int checkOTRecord(String otPlanDate,String requestStaffCode,String requestNo)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "select count(ot_plan_date) from " + saveOtRecordTable + " where request_no in("
		                +" select request_no from teflow_wkf_process where request_staff_code ='" + requestStaffCode +"' "
		                + " and (status<>'00' or status<>'04'))"
		                +" and ot_plan_date = '" + otPlanDate + "' "
		                ;
	     if(requestNo!=null && !"".equals(requestNo)){
	    	 sql = sql + " and request_no<>'"+requestNo+"' ";
	     }
		//System.out.println(sql);
		int count = dbManager.getRecordCount(sql);
		
		return count;
	}
	
	public void checkMonthlySummaryOT(OTSummaryCheckVO vo)throws DAOException{
		String sql = "insert into teflow_ot_summary_check_trace(year_month,team_code,staff_code,action_code,remark,action_date) values('"
			      +vo.getYearMonth()+"',"+vo.getTeamCode()+",'"+vo.getStaffCode()+"','"+vo.getActionCode()+"','"
			      +vo.getRemark()+"','"+vo.getActionDate()+"')";
		//System.out.println(sql);
		dbManager.executeUpdate(sql);
	}
	
	public Collection getCheckTraceByMonthTeam(String yearMonth,int teamCode)throws DAOException{
		Collection resultList = new ArrayList();
		Collection list = dbManager.query("select * from teflow_ot_summary_check_trace where year_month='" + yearMonth + "' and "
				               +" team_code=" + teamCode);
		if(list==null || list.size()==0) return resultList;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			OTSummaryCheckVO vo = new OTSummaryCheckVO();
			vo.setYearMonth(FieldUtil.convertSafeString(map,"YEAR_MONTH"));
			vo.setTeamCode(FieldUtil.convertSafeInt(map,"TEAM_CODE",0));
			vo.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			//vo.setActionCode(FieldUtil.convertSafeString(map,"ACTION_CODE"));
			if("01".equals(FieldUtil.convertSafeString(map,"ACTION_CODE"))){
			  vo.setActionCode("Confirmed");
			}else if("02".equals(FieldUtil.convertSafeString(map,"ACTION_CODE"))){
			  vo.setActionCode("Canceled");
			}else if("03".equals(FieldUtil.convertSafeString(map,"ACTION_CODE"))){
			  vo.setActionCode("Final Completed");
			}
			vo.setRemark(FieldUtil.convertSafeString(map,"REMARK"));
			vo.setActionDate(FieldUtil.convertSafeString(map,"ACTION_DATE"));
			resultList.add(vo);
		}
		return resultList;
	}
	
	public boolean isCloseYearMonthSummary(String yearMonth)throws DAOException{
		String sql = "select count(*) from teflow_ot_summary_monthly where year_month='" + yearMonth + "' and status='02' ";
		int result = dbManager.getRecordCount(sql);
		//System.out.println("rows="+result);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 删除team leader 确认操作的记录
	 * @param requestNo
	 * @throws DAOException
	 */
	public void cancelTeamLeaderConfirm(String staffCode,String yearMonth)throws DAOException{
		String sql = "delete from teflow_ot_confirm where request_no in(select request_no from teflow_wkf_process where request_staff_code='" + staffCode+"'  and node_id='-1') and status='01'" +
						" and request_no in (select request_no from teflow_ot_apply_monthly_his where staff_code='" + staffCode + "' and year_month='" + yearMonth + "')";
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 删除指定员工的指定年月的ot summary record
	 * @param staffCode
	 * @param yearMonth
	 * @throws DAOException
	 */
	public void cancelStaffOTSummary(String staffCode,String yearMonth)throws DAOException{
		String sql = "delete from teflow_ot_summary_monthly where year_month='" + yearMonth + "' and staff_code='"
		            + staffCode + "'";
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 删除指定员工的指定年月的ot apply his record
	 * @param staffCode
	 * @param yearMonth
	 * @throws DAOException
	 */
	public void cancelStaffApplyOT(String staffCode,String yearMonth)throws DAOException{
		String sql = "delete from teflow_ot_apply_monthly_his where staff_code='" + staffCode + "' and year_month='" + yearMonth + "' ";
		dbManager.executeUpdate(sql);
	}
	
	public Collection getRequestNoById(String ids)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "select distinct request_no from " + saveOtRecordTable + " where id in (" + ids+ ")";
		return dbManager.query(sql);
	}
	
	/**
	 * TL reject 某条OT记录，那么该记录所在的OT form就会退回到指定的节点
	 * @param vo
	 * @throws DAOException
	 */
	public void rejectOTRecord(WorkFlowProcessVO vo)throws DAOException{
		
		//修改该OT记录所在form的当前节点为rejectToNodeId
		String sql = "update teflow_wkf_process set node_id ='" + vo.getNodeId() + "',status='03',previous_processor='"+vo.getPreviousProcessor()+"',"
		            +"current_processor='"+vo.getCurrentProcessor()+"' where request_no='" + vo.getRequestNo() + "' ";
		//System.out.println(sql);
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 将进行reject操作的日志写入到trace表中
	 * @param operateStaffCode
	 * @param requestNo
	 * @param comments
	 * @throws DAOException
	 */
	public void logRejectOTRecord(String operateStaffCode,String requestNo,String comments)throws DAOException{
		String sql = "INSERT INTO teflow_wkf_process_trace(request_no,flow_id,node_id,current_processor,handle_date,handle_type,handle_comments,is_deputy,origin_processor,overdue_hours,attach_file) "
			    + " SELECT ?,flow_id,-1,?,GETDATE() ,'04',?,'0','',0,'' "
			    + " from teflow_wkf_process where request_no=? ";
		
		Object[] parameters = new Object[4];
		
		parameters[0] = requestNo;
		parameters[1] = operateStaffCode;
		parameters[2] = comments;
		parameters[3] = requestNo;
		
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		
		dbManager.executeUpdate(sql,parameters,dataType);
		
		//dbManager.executeUpdate(sql);
		
	}
	/**
	 * 更新comment内容
	 * @author Nicolas Chen
	 * @param comment
	 * @param processId
	 * @param requestNo
	 * @throws DAOException
	 */
	public void editProcessComment(String comment, Integer processId, String requestNo) throws DAOException{
		String sql = "update teflow_wkf_process_trace set handle_comments = ?,handle_date = GETDATE() where process_id = ? and request_no = ?";
		
		Object[] parameters = new Object[3];
		
		parameters[0] = comment;
		parameters[1] = processId.toString();
		parameters[2] = requestNo;
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,parameters,dataType);
	}
	
	public void completedOTRecord(String confirmStaffCode,String yearMonth,String requestStaffCode)throws DAOException{
		String sql = "update teflow_ot_confirm set status =?, complete_staff_code=?,complete_date=GETDATE() where request_no in "
			          + "(select request_no from teflow_ot_apply_monthly_his where year_month=? and staff_code=?)";
		Object[] obj = new Object[4];
		obj[0] = CommonName.OT_FORM_STATUS_COMPLETE;
		obj[1] = confirmStaffCode;
		obj[2] = yearMonth;
		obj[3] = requestStaffCode;
		
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType);
	}
	
    public void completeOTSummary(String yearMonth) throws DAOException {
        String sql = "update teflow_ot_summary_monthly set status = '02' where year_month = '" + yearMonth + "'";
        dbManager.executeUpdate(sql);
    }

    public void completeOTSummary(String yearMonth, String location) throws DAOException {
        String sql = "update teflow_ot_summary_monthly set status = '02' where year_month = '" + yearMonth + "'";
        if (location != null && !location.equals("")) {
            if (location.equals("BJ")) {
                sql += " and staff_code in (select staff_code from tpma_staffbasic where logon_id like 'BS%')";
            } else {
                sql += " and staff_code in (select staff_code from tpma_staffbasic where logon_id not like 'BS%')";
            }
        }
        dbManager.executeUpdate(sql);
    }
	
    public Collection getOTSummaryStaffList(String yearMonth) throws DAOException {
        String sql = "select staff_code, total_tax_amount, taxi_fee_amount from teflow_ot_summary_monthly where year_month = '"
                + yearMonth + "'";
        return dbManager.query(sql);
    }

    public Collection getOTSummaryStaffList(String yearMonth, String location) throws DAOException {
        String sql = "select staff_code, total_tax_amount, taxi_fee_amount from teflow_ot_summary_monthly where year_month = '"
                + yearMonth + "'";
        if (location != null && !location.equals("")) {
            if (location.equals("BJ")) {
                sql += " and staff_code in (select staff_code from tpma_staffbasic where logon_id like 'BS%')";
            } else {
                sql += " and staff_code in (select staff_code from tpma_staffbasic where logon_id not like 'BS%')";
            }
        }
        return dbManager.query(sql);
    }
	
	public void teamLeaderConfirmOTRecord(String tlStaffCode,String requestNo)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "insert into teflow_ot_confirm(request_no,status,confirm_staff_code,confirm_date,confirm_description) select distinct request_no,?,?,GETDATE(),?"
			+" from "+saveOtRecordTable+" where request_no= ?";
		Object[] obj = new Object[4];
		obj[0] = CommonName.OT_FORM_STATUS_CONFIRM;
		obj[1] = tlStaffCode;
		obj[2] = StaffTeamHelper.getInstance().getStaffNameByCode(tlStaffCode)+"("+StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss")+")";
		obj[3] = requestNo;
		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType);
	}
	
	/**
	 * 检查某张form是否已经经过TeamLeader的confirm
	 * @param requestNo
	 * @return true --- 已经confirm过；false --- 未经confirm
	 * @throws DAOException
	 */
	public boolean checkConfirmOTForm(String requestNo)throws DAOException{
		String sql = "select * from teflow_ot_confirm where request_no='"+requestNo+"' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return false;
		return true;
	}
	
	public void updateOTForNoExceptional(String requestNo,int num, String id)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "update " + saveOtRecordTable + " set weeky_num='"+num+"' where (is_exceptional_case='02' or is_exceptional_case='' or is_exceptional_case is null) and request_no='"+ requestNo + "' and id = " + id;
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 
	 * @param id
	 * @throws DAOException
	 */
	public void updateOTForNormalExceptional(String requestNo,int num, String id)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "update " + saveOtRecordTable + " set weeky_num='"+num+"' where is_exceptional_case='01' and request_no='"+ requestNo + "' and id = " + id;
		dbManager.executeUpdate(sql);
	}
	
	public void updateOTForMidNightExceptional (String requestNo, int num, String id) throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "update " + saveOtRecordTable + " set weeky_num = '"+num+"', pub_holiday_num = '0' where is_exceptional_case = '03' and request_no = '"+ requestNo + "' and id = " + id;
		dbManager.executeUpdate(sql);
	}
	
	/**获取该teamLeader下各个员工的OT记录总数（还没有CONFIRM的，即等待CONFIRM的,并且流程已经结束了的form）
	 * @param staffCode  TL's logonId
	 * @return
	 * @throws DAOException
	 */
	public Collection getWaitingConfirmBySum(String currentStaffCode,String requestStaffCode,String lowerTeam)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String subSql = "";

		StringBuffer sql = new StringBuffer("select b.request_staff_code,sum(c.ot_records) total_ot_num from teflow_wkf_process b,(select  a.request_no request_no, count(a.request_no) ot_records from "+saveOtRecordTable+" a ");
		sql.append(" where a.request_no not in (select request_no from teflow_ot_confirm) group by request_no) c ") 
            .append(" where b.request_no = c.request_no ");
		if(requestStaffCode!=null && !"".equals(requestStaffCode)){
			subSql = " and b.request_staff_code='" + requestStaffCode + "' ";
			sql.append(subSql);
		}else{
           sql.append(" and b.request_staff_code in (select staff_code from tpma_staffbasic where status='A' ")
            .append(" and (team_code in (select team_code from tpma_team where status='A' and tl_id in ") 
            .append(" (select logon_id from tpma_staffbasic where status='A' and staff_code in ")
            .append(" (select authority_approver from teflow_deputy_manage where status='01' and authority_deputy='"+currentStaffCode+"'))")
            .append(") or team_code in (select team_code from tpma_team where tl_id in (select logon_id from tpma_staffbasic where staff_code='"+currentStaffCode+"') and status='A')");
            if(!"".equals(lowerTeam)){
              sql.append(" or team_code in ("+lowerTeam+") ");
            }
            sql.append("))");
		}
        sql.append(" and b.node_id = '-1' group by b.request_staff_code order by total_ot_num desc");
		// System.out.println(sql);
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		Iterator it = list.iterator();
		Collection resultList = new ArrayList();
		while(it.hasNext()){
			OTRecordVO vo = new OTRecordVO();
			HashMap map = (HashMap)it.next();
			vo.setStaffCode(FieldUtil.convertSafeString(map,"REQUEST_STAFF_CODE"));
			vo.setOtRecordNum(FieldUtil.convertSafeInt(map,"TOTAL_OT_NUM",0));
			resultList.add(vo);
		}
		return resultList;
	}
	
	/**
	 * 获取某员工在某个月份报销时，还没有申请报销的并且可以申请报销的加班记录集合
	 * @param yearMonth
	 * @param staffCode
	 * @return
	 * @throws DAOException
	 */
	public Collection getAdditionalOTRecord(String yearMonth,String staffCode)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "select * from " + saveOtRecordTable + " where request_no in (select request_no from teflow_ot_confirm) "
        + " and request_no not in (select request_no from teflow_ot_apply_monthly_his where staff_code='" + staffCode+"' and "
        +" year_month='" + yearMonth+"' ) and request_no in (select request_no from teflow_wkf_process where request_staff_code='"+staffCode+"') order by request_no";
        
		return dbManager.query(sql);
	}
	
	/**
	 * 获取该员工所有还没有得到confirm的ot记录集合(并且流程已经结束了的）
	 * @param  conditionVo
	 * @return 还没有得到confirm的ot记录集合
	 * @throws DAOException
	 */
	public Collection getWaitingConfirmByStaff(OTRecordVO conditionVo)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "select * from " + saveOtRecordTable + " where request_no not in (select request_no from teflow_ot_confirm) "
                     + " and request_no in (select request_no from teflow_wkf_process where request_staff_code='"
                     + conditionVo.getStaffCode() + "' and node_id = '-1') ";
        if(conditionVo.getOtPlanDateBegin()!=null && !"".equals(conditionVo.getOtPlanDateBegin())){
        	if(conditionVo.getOtPlanDateEnd()!=null && !"".equals(conditionVo.getOtPlanDateEnd())){
        		sql = sql + " and ot_plan_date between '" + conditionVo.getOtPlanDateBegin() + "'  and '"
        		          + conditionVo.getOtPlanDateEnd() + "'  ";
        	}else{
        		sql = sql + " and ot_plan_date>='"+ conditionVo.getOtPlanDateBegin() + "'  ";
        	}
        }else if(conditionVo.getOtPlanDateEnd()!=null && !"".equals(conditionVo.getOtPlanDateEnd())){
        	sql = sql + " and ot_plan_date<='"+ conditionVo.getOtPlanDateEnd() + "' ";
        }
        if(conditionVo.getIsExceptionalCase()!=null && !"".equals(conditionVo.getIsExceptionalCase())){
        	sql = sql + " and is_exceptional_case='" + conditionVo.getIsExceptionalCase() + "' ";
        }
        sql = sql + " order by ot_plan_date";
        //System.out.println(sql);
	    return dbManager.query(sql);	
	}
	
    public Collection getWaitingConfirmByStaffs(String[] staffCodes) throws DAOException {
        String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "select distinct request_no from "
                + saveOtRecordTable
                + " where request_no not in (select request_no from teflow_ot_confirm) "
                + "and request_no in (select request_no from teflow_wkf_process where request_staff_code in (<staffCodes>) and node_id = '-1') "
                + "order by request_no";
        String _staffCodes = "";
        for (String staffCode : staffCodes) {
            _staffCodes += ",'" + staffCode + "'";
        }
        _staffCodes = _staffCodes.substring(1);
        sql = sql.replaceAll("<staffCodes>", _staffCodes);
        System.out.println(sql);
        return dbManager.query(sql);
    }
	
	public Collection getOTFormTableFields()throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		//分解table名字，得到sectionId,因为表名的组成形式是：teflow_formsystemid_sectionId
		String[] str = StringUtil.split(saveOtRecordTable,"_"); //三个成员的数组，第三个是sectionId
		String sql = "select * from teflow_form_section_field where form_system_id in "
			         + "(select form_system_id from teflow_form_section where table_name = '" + saveOtRecordTable + "') "
			         + " and section_id = '" + str[2] + "' order by order_id ";
		Collection sectionFieldList = new ArrayList();
		//System.out.println(sql);
		Collection list = dbManager.query(sql);
		if(list!=null && list.size()>0){
		    Iterator it = list.iterator();
			//add ID field
			FormSectionFieldVO idField = new FormSectionFieldVO();
			idField.setFieldId("ID");
			//add Request No field
			FormSectionFieldVO requestNoField = new FormSectionFieldVO();
			requestNoField.setFieldId("REQUEST_NO");
			requestNoField.setFieldLabel("Request No.");
			sectionFieldList.add(idField);
			sectionFieldList.add(requestNoField);
		    while(it.hasNext()){
			    FormSectionFieldVO field = new FormSectionFieldVO();
			    HashMap map = (HashMap)it.next();
			    field.setFieldId(FieldUtil.convertSafeString(map,"FIELD_ID"));
			    field.setFieldLabel(FieldUtil.convertSafeString(map,"FIELD_LABEL"));
			    field.setFieldType(FieldUtil.convertSafeInt(map,"FIELD_TYPE",1));
			    sectionFieldList.add(field);
		    }
		}
		return sectionFieldList;
	}
	
	
	/**
	 * 获取某人指定年月申请报销的历史纪录
	 * @param staffCode
	 * @param yearMonth
	 * @param status
	 * @return
	 * @throws DAOException
	 */
    public Collection getStaffApplyOTMonthHis(String staffCode, String yearMonth, String status) throws DAOException {
        String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "select c.*,d.confirm_description tl_confirm from " + saveOtRecordTable
                + " c,teflow_ot_confirm d where c.request_no = d.request_no and c.request_no "
                + "in (select request_no from teflow_ot_apply_monthly_his where staff_code='" + staffCode
                + "' and year_month='" + yearMonth + "')"
                // For both new and old forms
                + " and (c.weeky_num > '0.1' or c.pub_holiday_num > '0.1' or c.ot_meal_allowance_num > 0)"
                + " order by c.ot_plan_date ";
        return dbManager.query(sql);
    }
	
	public void deleteStaffSummary(String yearMonth,String staffCode)throws DAOException{
		String sql = "delete from teflow_ot_summary_monthly where year_month='" + yearMonth+"' and staff_code='" + staffCode+"' and status='01' ";
		dbManager.executeUpdate(sql);
	}
	
	public void deleteApplyOtMontly(String yearMonth,String staffCode)throws DAOException{
		String sql = "delete from teflow_ot_apply_monthly_his where year_month='" + yearMonth+"' and staff_code='" + staffCode+"' ";
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 获取最新的某人所有可以申请报销的ot记录（在流转中已经结束（status='04'）。还没有申请报销的：包括已经confirm过和还有confirm过的）（有效的OT记录：Weeeky_num,public_num,statutory_hours三个字段中必须有一个的值大于0）
	 * @param conditionVo
	 * @return
	 * @throws DAOException
	 */
	public Collection getValidOTRecordForApplyByStaff(OTRecordVO conditionVo)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		//String sql = "select c.*,d.confirm_description tl_confirm from " + saveOtRecordTable + " c,teflow_ot_confirm d where c.request_no = d.request_no and "
		//             +" c.request_no in (select b.request_no from teflow_wkf_process b where b.request_staff_code='"+conditionVo.getStaffCode()+"') and d.status = '01' ";
		String sql = "select c.*,d.confirm_description tl_confirm from " + saveOtRecordTable + " c left join teflow_ot_confirm d on c.request_no = d.request_no where "
        +" c.request_no not in (select b.request_no from teflow_ot_apply_monthly_his b where b.staff_code='"+conditionVo.getStaffCode()+"') "
        +" and c.request_no in (select b.request_no from teflow_wkf_process b where b.request_staff_code='"+conditionVo.getStaffCode()+"' and status='04') ";
		
		if(conditionVo.getOtPlanDateBegin()!=null && !"".equals(conditionVo.getOtPlanDateBegin())){
        	if(conditionVo.getOtPlanDateEnd()!=null && !"".equals(conditionVo.getOtPlanDateEnd())){
        		sql = sql + " and c.request_no in (select request_no from teflow_wkf_process where Submission_date between '" + conditionVo.getOtPlanDateBegin() + " 00:00:00'  and '"
        		          + conditionVo.getOtPlanDateEnd() + " 23:59:59' )";
        	}else{
        		sql = sql + " and c.request_no in (select request_no from teflow_wkf_process where Submission_date>='"+ conditionVo.getOtPlanDateBegin() + " 00:00:00')  ";
        	}
			//if(conditionVo.getOtPlanDateEnd()!=null && !"".equals(conditionVo.getOtPlanDateEnd())){
        	//	sql = sql + " and c.request_no in (select request_no from "+saveOtRecordTable+" where ot_plan_date between '" + conditionVo.getOtPlanDateBegin() + " 00:00:00'  and '"
        	//	          + conditionVo.getOtPlanDateEnd() + " 23:59:59' )";
        	//}else{
        	//	sql = sql + " and c.request_no in (select request_no from "+saveOtRecordTable+" where ot_plan_date>='"+ conditionVo.getOtPlanDateBegin() + " 00:00:00')  ";
        	//}
        }else if(conditionVo.getOtPlanDateEnd()!=null && !"".equals(conditionVo.getOtPlanDateEnd())){
        	sql = sql + " and c.request_no in (select request_no from teflow_wkf_process where Submission_date<='"+ conditionVo.getOtPlanDateEnd() + " 23:59:59') ";
        	//sql = sql + " and c.request_no in (select request_no from "+saveOtRecordTable+ " where ot_plan_date<='"+ conditionVo.getOtPlanDateEnd() + " 23:59:59') ";
        }
		sql = sql + " and (c.weeky_num > '0.1' or c.pub_holiday_num > '0.1' or c.ot_meal_allowance_num > 0)"; // For both new and old forms
        sql = sql + " order by c.ot_plan_date";
        //System.out.println(sql);
		return dbManager.query(sql);
	}
	
	public String getStaffChineseName(String staffCode)throws DAOException{
		String sql = "select staff_name_cn from teflow_ot_summary_monthly where staff_code='" + staffCode + "'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return "";
		HashMap map = (HashMap)list.iterator().next();
		return (String)map.get("STAFF_NAME_CN");
	}
	
	/**
	 * 更新某个员工的加班记录
	 * @param staffCode
	 * @throws DAOException
	 */
	public void updateOTRecordFromCheckSys(String staffCode) throws DAOException {
        String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);

        String sql = "select a.is_exceptional_case,a.id,a.request_no,CONVERT(nvarchar(10),a.ot_plan_date,101) as ot_plan_date,b.request_staff_code,b.node_id from "
                + saveOtRecordTable
                + " as a,teflow_wkf_process b "
                + " where a.request_no = b.request_no and b.request_staff_code='"
                + staffCode
                + "' and b.request_no not in (select request_no from teflow_ot_confirm) "
                + "and (((a.weeky_num is null or a.weeky_num='0.0' or a.weeky_num='0') and (a.pub_holiday_num is null or a.pub_holiday_num='0.0' or a.pub_holiday_num='0')) "
                + "or (a.is_exceptional_case in ('02','03','04'))) order by a.request_no ";
        Collection otRecordList = dbManager.query(sql);
        if (otRecordList == null || otRecordList.size() == 0)
            return;
        Iterator it = otRecordList.iterator();
        String tempStaffCode = "";
        String otPlanDate = "";
        String id = "-1";
        String isExceptionalCase = "";
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            tempStaffCode = (String) map.get("REQUEST_STAFF_CODE");
            otPlanDate = (String) map.get("OT_PLAN_DATE"); // 得到日期格式是"yyyy-mmm-dd",
            id = (String) map.get("ID");
            isExceptionalCase = (String) map.get("IS_EXCEPTIONAL_CASE");
			//2011-5-24 IT1234 根据最新的policy不再需要自动计算 
            if (isExceptionalCase != null && "01".equals(isExceptionalCase)) {
                //updateOTForNormalExceptional((String) map.get("REQUEST_NO"), 1, id);
            } else if (isExceptionalCase != null && "03".equals(isExceptionalCase)) {
                //updateOTForMidNightExceptional((String) map.get("REQUEST_NO"), 2, id);
            } else if (isExceptionalCase != null && "04".equals(isExceptionalCase)) {
            	
            } else {
                updateOTForNoExceptional((String) map.get("REQUEST_NO"), 0, id);
                // 获取该员工该OT记录的check in/out记录
                HashMap checkMap = getCheckRecord(tempStaffCode, otPlanDate);
                if (checkMap != null) {
                    // 更新OT表中该记录的相关考勤字段的值
                    updateOTRecord(checkMap, saveOtRecordTable, id);
                }
            }
        }
    }
	
	public void updateOTRecordFromCheckSys()throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		//String otFlowId = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_FLOW_ID_CODE);
		//首先获取当前还没有结束的form中的所有加班记录
		Collection otRecordList = getAllOTRecord();
		if(otRecordList==null || otRecordList.size()==0) return;
		Iterator it = otRecordList.iterator();
		String staffCode = "";
		String otPlanDate = "";
		String id = "-1";
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			staffCode = (String)map.get("REQUEST_STAFF_CODE");
			otPlanDate = (String)map.get("OT_PLAN_DATE"); //得到日期格式是"yyyy-mmm-dd",
			id = (String)map.get("ID");
			//如果该记录标识为"is_exceptional_case='01'(Yes)",则该为其weeky_num算一次
			//2011-5-24 IT1234 根据最新的policy不再需要自动计算 
			if((String)map.get("IS_EXCEPTIONAL_CASE")!=null && "01".equals(map.get("IS_EXCEPTIONAL_CASE"))){
				//dbManager.executeUpdate("update "+saveOtRecordTable+" set weeky_num=1.0 where id=" + id);
				continue;
			}
			if((String)map.get("IS_EXCEPTIONAL_CASE")!=null && "03".equals(map.get("IS_EXCEPTIONAL_CASE"))){
				//dbManager.executeUpdate("update "+saveOtRecordTable+" set weeky_num=2.0 where id=" + id);
				continue;
			}
			if((String)map.get("IS_EXCEPTIONAL_CASE")!=null && "04".equals(map.get("IS_EXCEPTIONAL_CASE"))){
				continue;
			}
			//获取该员工该OT记录的check in/out记录
			HashMap checkMap = getCheckRecord(staffCode,otPlanDate);
			if(checkMap!=null){
				//更新OT表中该记录的相关考勤字段的值
				updateOTRecord(checkMap,saveOtRecordTable,id);
			}
		}
	}
	
	/**
	 * 获取所有申请过加班，(该form已经submit) 最近3月以内的所有的已经申请并且还没有统计是否加班的记录
	 * @return
	 * @throws DAOException
	 * 2013-9-3 优化：只取考勤系统中有加班数的记录，以减少多余的数据库查询
	 */
	private Collection getAllOTRecord()throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		
		/*String sql = "select a.id,a.request_no,CONVERT(nvarchar(10),a.ot_plan_date,101) as ot_plan_date,b.request_staff_code,b.node_id,a.is_exceptional_case from " + saveOtRecordTable +" as a,teflow_wkf_process b "
		              + " where a.request_no = b.request_no and a.ot_plan_date>=DateAdd(MM,-3,'"+StringUtil.getCurrentDateStr("MM/dd/yyyy")+"') and b.node_id<>'0' and (a.weeky_num is null or a.weeky_num='0' or a.weeky_num='0.0') "
		              +" and (a.pub_holiday_num is null or a.pub_holiday_num='0' or a.pub_holiday_num='0.0') ";*/
		String sql = "select a.id,a.request_no,CONVERT(nvarchar(10),a.ot_plan_date,101) as ot_plan_date,b.request_staff_code,b.node_id,a.is_exceptional_case from " + saveOtRecordTable +" as a,teflow_wkf_process b "
		+ ", tpma_checkinout c "
        + " where a.request_no = b.request_no and a.ot_plan_date>=DateAdd(MM,-3,'"+StringUtil.getCurrentDateStr("MM/dd/yyyy")+"') and b.node_id<>'0' and (a.weeky_num is null or a.weeky_num='0' or a.weeky_num='0.0') "
        + " and (a.pub_holiday_num is null or a.pub_holiday_num='0' or a.pub_holiday_num='0.0') "
        + " and c.workdate_c =a.ot_plan_date and c.staffcode=b.request_staff_code and (c.workdayOT >0 or c.statuaryOT>0 or c.holidayOT>0)";
		
		return dbManager.query(sql);
	}

	private HashMap getCheckRecord(String staffCode,String otDate)throws DAOException{
		HashMap map = null;
		String sql = "select arrival as check_in_time,leave as check_out_time,workdayot as workday_ot_num, holidayot as holiday_ot_num,statuaryot as statuary_ot_hours "
			      + "from tpma_checkinout where workdate_c ='" + otDate+"' and staffcode='"
			      + staffCode + "' ";
        Collection list = dbManager.query(sql);
        if(list==null || list.size()==0) return map;
        map = (HashMap)list.iterator().next();
		return map;
	}
	
	private void updateOTRecord(HashMap checkMap,String tableName,String id)throws DAOException{
		if(checkMap==null) return;
		String updateSQL = "update " + tableName + " set weeky_num='" + FieldUtil.convertSafeFloat(checkMap,"WORKDAY_OT_NUM",0)+"',"
        +"pub_holiday_num='"+FieldUtil.convertSafeFloat(checkMap,"HOLIDAY_OT_NUM",0)+"' where id=" + id;
		dbManager.executeUpdate(updateSQL);
	}
	
	/**
	 * 检查所指定的加班记录是否都已经confirm过了
	 * @param ids
	 * @return true ---都confirm过了；false---存在记录还没有得到TL的confirm
	 * @throws DAOException
	 */
	public boolean checkConfirmedOTRecord(String ids)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "select request_no from " + saveOtRecordTable+" where id in (" + ids + ") and request_no not in("
		            +"select request_no from teflow_ot_confirm where status='01')";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return true;
		return false;
	}
	
	
	/**
	 * 
	 * @param staffCode
	 * @param yearMonth
	 * @param ids
	 * @throws DAOException
	 */
    public void saveOTApplyMonthHis(String staffCode,String yearMonth,String ids)throws DAOException{
		String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
		String sql = "insert into teflow_ot_apply_monthly_his(request_no,year_month,staff_code) select distinct request_no,'"
			       +yearMonth+"','" + staffCode+"' from "+saveOtRecordTable+" where id in ("  + ids + ") ";
		dbManager.executeUpdate(sql);
    }
	
    public void saveOTApplyMonthHisByRequestNo(String staffCode,String yearMonth,String requestNo)throws DAOException{
    	String sql = "insert into teflow_ot_apply_monthly_his(request_no,year_month,staff_code) "
    		         +" values('"+requestNo+"','"+yearMonth+"','"+staffCode+"') ";
    	dbManager.executeUpdate(sql);
    }
    
    // Discarded
    public void saveOTApplyMonthHis(String staffCode, String yearMonth) throws DAOException {
        String saveOtRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "insert into teflow_ot_apply_monthly_his(request_no,year_month,staff_code) "
                + "select distinct c.request_no,'" + yearMonth + "','" + staffCode + "' from " + saveOtRecordTable
                + " c,teflow_ot_confirm d where c.request_no = d.request_no and "
                + "c.request_no in (select b.request_no from teflow_wkf_process b where b.request_staff_code='"
                + staffCode
                + "' and d.status = '01' "
                // For both new and old forms
                + " and (c.weeky_num > '0.1' or c.pub_holiday_num > '0.1' or c.ot_meal_allowance_num > 0))"
                + " and c.request_no not in (select request_no from teflow_ot_apply_monthly_his where staff_code = '"
                + staffCode + "')";

        dbManager.executeUpdate(sql);
    }
	
	public void deleteOTMonthlySummary(String staffCode,String yearMonth)throws DAOException{
		String sql = "delete from teflow_ot_summary_monthly where year_month='" + yearMonth + "' and staff_code='" + staffCode + "'";
		dbManager.executeUpdate(sql);
	}
	
    public void saveOTMonthlySummary(OTSummaryVO vo) throws DAOException {
        String sql = "insert into teflow_ot_summary_monthly(year_month,staff_code,team_code,staff_name_en,staff_name_cn,"
                + "weeky_num,public_num,statutory_hours,mid_night_num,day_time_num,meal_allowance_num,"
                + "total_tax_amount,pre_tax_amount,taxi_fee_amount,status) " + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = new Object[15];
        obj[0] = vo.getYearMonth();
        obj[1] = vo.getStaffCode();
        obj[2] = "" + vo.getTeamCode();
        obj[3] = vo.getStaffNameEn();
        obj[4] = vo.getStaffNameCn();
        obj[5] = "" + vo.getWeekyNum();
        obj[6] = "" + vo.getPublicNum();
        obj[7] = "" + vo.getStatutoryHours();
        obj[8] = vo.getMidNightNum().toPlainString();
        obj[9] = vo.getDayTimeNum().toPlainString();
        obj[10] = vo.getMealAllowanceNum().toPlainString();
        obj[11] = "" + vo.getTotalTaxAmount();
        obj[12] = "" + vo.getPreTaxAmount();
        obj[13] = "" + vo.getTaxiFeeAmount();
        obj[14] = "01";

        int[] dataType = {DataType.VARCHAR, DataType.VARCHAR, DataType.INT, DataType.VARCHAR, DataType.VARCHAR,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.VARCHAR};

        dbManager.executeUpdate(sql, obj, dataType);
    }
	
	public OTSummaryVO getOTSummaryVOByStaff(String staffCode,String yearMonth)throws DAOException{
		String sql = "select * from teflow_ot_summary_monthly where staff_code='"+staffCode+"' and year_month='"
		              +yearMonth+"'";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		return convertMapToOTSummaryVO(map);
	}
	
	public Collection getOTSummaryHistory(String staffCode)throws DAOException{
		String sql = "select  a.year_month,a.staff_code,a.status from teflow_ot_summary_monthly a where 1=1 ";
		if(staffCode!=null && !"".equals(staffCode)){
			sql = sql + " and a.staff_code='" + staffCode + "'";
		}
		sql = sql + " order by a.status,a.year_month desc";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Iterator it = list.iterator();
		Collection result = new ArrayList();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			OTSummaryVO vo = new OTSummaryVO();
			vo.setYearMonth(FieldUtil.convertSafeString(map,"YEAR_MONTH"));
			vo.setStaffCode(FieldUtil.convertSafeString(map,"STAFF_CODE"));
			vo.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
			result.add(vo);
		}
		return result;
	}
	
    public Collection getOTSummaryByTeam(String yearMonth, String teamCode, String location) throws DAOException {
        String otRecordTable = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_OT_RECORD_TABLE_CODE);
        String sql = "select a.*, b.is_exceptional_case, c.logon_id from teflow_ot_summary_monthly a left join "
                + "(select distinct a.year_month, a.staff_code, 'hasexception' as is_exceptional_case "
                + "from teflow_ot_apply_monthly_his a, "
                + otRecordTable
                + " b, teflow_ot_confirm c "
                + "where (a.request_no = b.request_no) and (b.request_no = c.request_no)"
                // For both new and old forms
                + " and (b.weeky_num > '0.1' or b.pub_holiday_num > '0.1' or b.ot_meal_allowance_num > 0)"
                + " and (b.is_exceptional_case = '01' or b.is_exceptional_case = '03' or b.is_exceptional_case = '04' ) "
                + ") b on (a.year_month = b.year_month) and (a.staff_code = b.staff_code), tpma_staffbasic c "
                + "where a.year_month = '" + yearMonth + "' ";

        if (teamCode != null && !"".equals(teamCode)) {
            sql = sql + " and a.team_code in (" + teamCode + ") ";
        }

        sql += " and (a.staff_code = c.staff_code) ";
        if (location != null && !location.equals("")) {
            if (location.equals("BJ")) {
                sql += " and (c.logon_id like 'BS%') ";
            } else {
                sql += " and (c.logon_id not like 'BS%') ";
            }
        }

        sql = sql + " order by a.team_code, a.total_tax_amount";
        Collection list = dbManager.query(sql);
        if (list == null || list.size() == 0)
            return null;
        Collection result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            OTSummaryVO vo = convertMapToOTSummaryVO(map);
            result.add(vo);
        }
        return result;
    }
	
    public OTSummaryVO convertMapToOTSummaryVO(HashMap map) {
        OTSummaryVO vo = new OTSummaryVO();
        vo.setStaffCode((String) map.get("STAFF_CODE"));
        vo.setYearMonth((String) map.get("YEAR_MONTH"));
        vo.setStaffNameCn(FieldUtil.convertSafeString(map, "STAFF_NAME_CN"));
        vo.setStaffNameEn(FieldUtil.convertSafeString(map, "STAFF_NAME_EN"));
        vo.setTeamCode(FieldUtil.convertSafeInt(map, "TEAM_CODE", 0));
        vo.setPreTaxAmount(FieldUtil.convertSafeDouble(map, "PRE_TAX_AMOUNT", 0));
        vo.setWeekyNum(FieldUtil.convertSafeDouble(map, "WEEKY_NUM", 0));
        vo.setPublicNum(FieldUtil.convertSafeDouble(map, "PUBLIC_NUM", 0));
        vo.setStatutoryHours(FieldUtil.convertSafeDouble(map, "STATUTORY_HOURS", 0));
        vo.setMidNightNum(FieldUtil.convertSafeDecimal(map, "MID_NIGHT_NUM", BigDecimal.ZERO));
        vo.setDayTimeNum(FieldUtil.convertSafeDecimal(map, "DAY_TIME_NUM", BigDecimal.ZERO));
        vo.setMealAllowanceNum(FieldUtil.convertSafeDecimal(map, "MEAL_ALLOWANCE_NUM", BigDecimal.ZERO));
        vo.setTotalTaxAmount(FieldUtil.convertSafeDouble(map, "TOTAL_TAX_AMOUNT", 0));
        vo.setTaxiFeeAmount(FieldUtil.convertSafeDouble(map, "TAXI_FEE_AMOUNT", 0));
        if (vo.getPreTaxAmount() > 0) {
            vo.setAfterTaxAmount(vo.getTotalTaxAmount() - vo.getPreTaxAmount());
        } else {
            vo.setAfterTaxAmount(vo.getTotalTaxAmount());
        }
        vo.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        vo.setExceptionalCase("hasexception".equals(FieldUtil.convertSafeString(map, "IS_EXCEPTIONAL_CASE")));

        return vo;
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
		double f=102345.8d;
		double d=0.0d;
		d=f;
		System.out.println(d);
	}

}
