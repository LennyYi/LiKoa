package com.aiait.eflow.wkf.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;

/*
 *NO. Task_ID  Author   Modify_Date   Description
 *1		N/A		asnpg9a	 11/16/2009	  added search criteria "completed date".
 *2		N/A		asnpg9a	 11/16/2009	  Fixed: there are duplicate records when export ACE Interface.
 *									  cause that there are multiple records with hanle_type 'completed:05' for one request_no in table process_trace.
 *
 */
public class exportAceDAO extends WorkFlowProcessDAO {
   
  public exportAceDAO(IDBManager dbManager){
	   super(dbManager);
  }
  
  /**IT0958 : add filter : requested by
	 * 获取指定用户所申请的记录
	 * @param staffCode
	 * @return
	 * @throws DAOException
	 */
	public Collection searchInquiryForm(WorkFlowProcessVO queryVo, PageVO page, String supperOrgs)throws DAOException{
		StringBuffer sql = new StringBuffer("select distinct a.request_no,a.flow_id,a.submission_date,a.request_staff_code,a.receiving_date,a.previous_processor,a.current_processor,a.status,a.inprocess,c.form_name,c.form_system_id,a.node_id,a.is_deputy,a.origin_processor ");
		sql.append(" from teflow_wkf_process a,teflow_wkf_define b,teflow_form c, tpma_staffbasic d, teflow_wkf_process_trace ptrace where 1=1 ");
		
		HashMap resultMap = makeCondition(queryVo, supperOrgs);
		
		sql.append(resultMap.get("conditionSQL"));
		
		sql.append(" and a.flow_id = ptrace.flow_id and a.request_no = ptrace.request_no");
		sql.append(" and a.flow_id=b.flow_id and b.form_system_id = c.form_system_id and a.request_staff_code = d.staff_code order by submission_date desc");

		//Collection list = dbManager.query(sql.toString());
		//Collection list = dbManager.query(sql.toString(),(Object[])resultMap.get("parameters"),(int[])resultMap.get("dataType"));
		//System.out.println("-----sql_2: " + sql);
		
		Collection list = dbManager.query(sql.toString(),(Object[])resultMap.get("parameters"),(int[])resultMap.get("dataType"),page.getPageSize(),page.getCurrentPage());
		
		
		if(list==null && list.size()==0)
		  return null;
	    Collection result = new ArrayList();
	    Iterator it = list.iterator();
	    while(it.hasNext()){
	    	HashMap map = (HashMap)it.next();
	    	WorkFlowProcessVO vo = convertMapToVo(map);
	    	
	    	// Set tip field
	    	// vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1));
	    	vo.setTipField(this.getSpecialField(vo.getRequestNo(), 1, vo.getFormSystemId()));
	    	// Set highlight field
            // vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2));
            vo.setHighlightField(this.getSpecialField(vo.getRequestNo(), 2, vo.getFormSystemId()));
	    	
	    	result.add(vo);
	    }
	    return result;
	}
	
	/**
     * Get export form data with basic information, such as requester, company, etc.
     */
    public Collection exportAce(WorkFlowProcessVO queryVo, PageVO page, String supperOrgs) throws DAOException {
        Collection list = this.searchInquiryForm(queryVo, page, supperOrgs);
        Iterator it = list.iterator();
        String requestNos = "";
        int i= 0;
        while (it.hasNext()) {
            WorkFlowProcessVO vo = (WorkFlowProcessVO) it.next();
            if(i == 0){
            	requestNos = "'"+vo.getRequestNo()+"'";
            }else{
            	requestNos = requestNos +", '" + vo.getRequestNo()+"'";
            }
            i++;
        }
        
        Collection listOut = new ArrayList();
        
        String sql = " select distinct * from teflow_ace_interface where epay_id in ("+requestNos+") ";

        System.out.println("ExportAceDAO:ExportAce: "+sql);
        
    	Connection conn = dbManager.getJDBCConnection();
    	PreparedStatement stmt = null; 
        try {
        	stmt = conn.prepareStatement(sql);
        	ResultSet rs = stmt.executeQuery();
        
	        while (rs.next()) {
	        	
	            AceVO tempvo = new AceVO();
	            tempvo.setCOMPANY_CODE(rs.getString("company_code"));
	            tempvo.setCITY_CODE(rs.getString("city_code"));
	            tempvo.setACCNT_CODE(rs.getString("accnt_code"));
	            tempvo.setPERIOD(rs.getString("period"));
	            tempvo.setTRANS_DATE(rs.getString("trans_date"));
	            tempvo.setAMOUNT(rs.getDouble("amount"));
	            tempvo.setD_C(rs.getString("d_c"));      
	            tempvo.setALLOCATION(rs.getString("allocation"));
	            tempvo.setBATCH_NO(rs.getString("batch_no"));  
	            tempvo.setJRNAL_TYPE(rs.getString("jrnal_type"));
	            tempvo.setJRNAL_SRCE(rs.getString("jrnal_srce"));  
	            tempvo.setTREFERENCE(rs.getString("treference"));  
	            tempvo.setDESCRIPTION(rs.getString("description"));  
	            tempvo.setCONV_CODE(rs.getString("conv_code")); 
	            tempvo.setCONV_RATE(rs.getDouble("conv_rate"));   
	            tempvo.setOTH_AMT(rs.getDouble("oth_amt"));   
	            tempvo.setASSET_CODE(rs.getString("asset_code"));
	            tempvo.setASSET_SUB(rs.getString("asset_sub"));  
	            tempvo.setASSET_IND(rs.getString("asset_ind"));   
	            tempvo.setDUE_DATE(rs.getString("due_date"));   
	            tempvo.setANAL_T0(rs.getString("anal_t0"));    
	            tempvo.setANAL_T1(rs.getString("anal_t1"));      
	            tempvo.setANAL_T2(rs.getString("anal_t2"));      
	            tempvo.setANAL_T3(rs.getString("anal_t3"));     
	            tempvo.setANAL_T4(rs.getString("anal_t4"));      
	            tempvo.setANAL_T5(rs.getString("anal_t5"));      
	            tempvo.setANAL_T6(rs.getString("anal_t6"));      
	            tempvo.setANAL_T7(rs.getString("anal_t7"));      
	            tempvo.setANAL_T8(rs.getString("anal_t8"));      
	            tempvo.setANAL_T9(rs.getString("anal_t9"));      
	            tempvo.setWB_FLAG(rs.getString("wb_flag"));      
	            tempvo.setWB_ERR_DESC(rs.getString("wb_err_desc"));
	            tempvo.setWB_JRNAL_NO(rs.getString("wb_jrnal_no")); 
	            tempvo.setWB_TREFERENCE(rs.getString("wb_treference"));
	            tempvo.setDB_CODE(rs.getString("db_code"));
	            tempvo.setPOLICY_CODE(rs.getString("policy_code"));
	            tempvo.setBANK_NO(rs.getString("bank_no")); 
	            tempvo.setEpay_id(rs.getString("epay_id"));     
	            tempvo.setDc_id(rs.getString("dc_id"));
	            listOut.add(tempvo);
	        }
	        rs.close();

            return listOut;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
	
	protected HashMap makeCondition(WorkFlowProcessVO queryVo, String supperOrgs){
		HashMap map = new HashMap();
		StringBuffer sql = new StringBuffer("");
		int index = 0;
		int parameterNum = 0;
		if(queryVo.getRequestNo()!=null && !"".equals(queryVo.getRequestNo())){
			parameterNum++;
		}
		if(queryVo.getStatus()!=null && !"".equals(queryVo.getStatus())){
			parameterNum++;
		}
		if (queryVo.getOrgId() != null && !queryVo.getOrgId().equals("")) {
			parameterNum++;
		}
		if(queryVo.getRequestStaffCode()!=null && !"".equals(queryVo.getRequestStaffCode())){
			parameterNum++;
		}
		if(queryVo.getBeginSubmissionDate()!=null && !"".equals(queryVo.getBeginSubmissionDate())){
			parameterNum++;
		}
		if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){
			parameterNum++;
		}
		if(queryVo.getFormSystemId()>0){
			parameterNum++;
		}
		if(queryVo.getInProcess()!=null && !"".equals(queryVo.getInProcess())){
			parameterNum++;
		}
		if (!(StringUtil.isEmptyString(queryVo.getBeginCompleteDate()))){
			parameterNum++;
		}
		if (!(StringUtil.isEmptyString(queryVo.getEndCompleteDate()))){
			parameterNum++;
		} 

		int[] dataType = new int[parameterNum];
		Object[] parameters = new Object[parameterNum];
		
		if(queryVo.getRequestNo()!=null && !"".equals(queryVo.getRequestNo())){
			sql.append(" and a.request_no=? ");
			dataType[index] = DataType.VARCHAR;
			parameters[index] = queryVo.getRequestNo();
			index++;
		}
		if(queryVo.getFormType()!=null && !"".equals(queryVo.getFormType())){
			sql.append(" and c.form_type in (").append(queryVo.getFormType()).append(") ");
		}
		if(queryVo.getStatus()!=null && !"".equals(queryVo.getStatus())){
			sql.append(" and a.status=? ");
			dataType[index] = DataType.VARCHAR;
			parameters[index] = queryVo.getStatus();
			index++;
		}
		
		if (queryVo.getOrgId() != null && !queryVo.getOrgId().equals("")) {
			sql.append(" and d.org_id = ? ");
			dataType[index] = DataType.VARCHAR;
			parameters[index] = queryVo.getOrgId();
			index++;
		}
		
		//IT0958 DS-007 Begin
		if(queryVo.getRequestStaffCode()!=null && !"".equals(queryVo.getRequestStaffCode())){
			sql.append(" and a.request_staff_code=? ");
			dataType[index] = DataType.VARCHAR;
			parameters[index] = queryVo.getRequestStaffCode();
			index++;
		}
		//IT0958 DS-007 End
		if(queryVo.getBeginSubmissionDate()!=null && !"".equals(queryVo.getBeginSubmissionDate())){
			  if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){
				  sql.append(" and Convert(varchar(10),a.submission_date,101)>=? and Convert(varchar(10),a.submission_date,101)<=?");
			      dataType[index] = DataType.DATE;
				  try{
			        parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(queryVo.getBeginSubmissionDate()+" 00:00:00","MM/dd/yyyy HH:mm:ss").getTime());
				  }catch(Exception e){}
				  index++;
				  
				  dataType[index] = DataType.DATE;
				  try{
				        parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate()+" 23:59:59","MM/dd/yyyy HH:mm:ss").getTime());
					  }catch(Exception e){}
				 index++;
			  }else{
			      sql.append( " and a.submission_date>=? ");
			      dataType[index] = DataType.DATE;
				  try{
			        parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(queryVo.getBeginSubmissionDate()+" 00:00:00","MM/dd/yyyy HH:mm:ss").getTime());
				  }catch(Exception e){}
				  index++;
			  }
		  }else{
			  if(queryVo.getEndSubmissionDate()!=null && !"".equals(queryVo.getEndSubmissionDate())){
				  sql.append( " and a.submission_date<=? ");
				  dataType[index] = DataType.DATE;
				  try{
				        parameters[index] = new java.sql.Date(StringUtil.stringToSqlDate(queryVo.getEndSubmissionDate()+" 23:59:59","MM/dd/yyyy HH:mm:ss").getTime());
					  }catch(Exception e){}
				 index++;
			  }
		}
		
		if (!(StringUtil.isEmptyString(queryVo.getBeginCompleteDate()))){
			sql.append(" and ptrace.handle_date >= ? ");
			dataType[index] = DataType.DATE;
			try{
			     parameters[index] = StringUtil.stringToSqlDate(queryVo.getBeginCompleteDate()+" 00:00:00","MM/dd/yyyy HH:mm:ss");
			}catch(Exception e){}
			index++;			
		}
		if (!(StringUtil.isEmptyString(queryVo.getEndCompleteDate()))){
			//sql.append(" and ptrace.handle_date <= ? ");
			sql.append(" and Convert(varchar(10),ptrace.handle_date,101) <= ? ");
			dataType[index] = DataType.DATE;
			try{
			     parameters[index] = StringUtil.stringToSqlDate(queryVo.getEndCompleteDate()+" 23:59:59","MM/dd/yyyy HH:mm:ss");
			}catch(Exception e){}
			index++;				
		} 
		if (!StringUtil.isEmptyString(queryVo.getEndCompleteDate()) || !StringUtil.isEmptyString(queryVo.getBeginCompleteDate())){
			sql.append(" and a.status = '04' ");	//initial 00 submitted 01 Approved    02 Rejected 03 completed   04
			sql.append(" and ptrace.handle_type = '05' ");	//Initial 00  Submit 01  WithDraw 02   Approve  03   Reject  04   Completed 05Invite Expert 06Expert advise 07
		}
		
		if(queryVo.getFormSystemId()>0){
			sql.append(" and b.form_system_id=?");
			dataType[index] = DataType.INT;
			parameters[index] = ""+queryVo.getFormSystemId();
			index++;
		}
      
		//IT0973 BEGIN
		if(queryVo.getInProcess()!=null && !"".equals(queryVo.getInProcess())){
			sql.append(" and a.inprocess=? ");
			dataType[index] = DataType.CHAR;
			parameters[index] = queryVo.getInProcess();
			index++;
		}
		//IT0973 END
		
		if(supperOrgs != null && !"".equals(supperOrgs)){
			sql.append(" and c.org_id in (" + supperOrgs + ") ");
		}
		//System.out.println(sql.toString());
		map.put("conditionSQL",sql);
		map.put("parameters",parameters);
		map.put("dataType",dataType);
		return map;
	}
}
