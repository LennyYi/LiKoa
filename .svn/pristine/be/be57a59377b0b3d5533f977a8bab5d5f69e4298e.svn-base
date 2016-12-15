package com.aiait.eflow.report.dao;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.aiait.eflow.common.helper.LeaveEntitleHelper;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.report.vo.LeaveDetailVO;
import com.aiait.eflow.report.vo.LeaveSummaryVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class LeaveReportDAO extends BaseDAOImpl {

    public LeaveReportDAO(IDBManager dbManager) {
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
    
    public Collection getLeaveDetailList(String formSystemId,String leaveDateBegin, String leaveDateEnd, String teamList, String currentStaffCode, String selectType) throws DAOException {
        String SQL = "select a.request_no,a.request_staff_code, f.team_name, b2.field_02_1 as type_code, g.option_label as type_name, "
				+ "b2.field_02_2 as apply_working_days,b2.field_02_9 as apply_calendar_days, b2.field_02_3 as from_date, b2.field_02_5 as to_date, b2.field_02_7 as remark, " 
				+ "h.option_label as from_time_name, i.option_label as to_time_name, a.status, a.current_processor, we.Staff_type as staff_type "
				+ "from teflow_wkf_process a, teflow_user_work_experience we, teflow_"+formSystemId+"_01 b left join tpma_team f on (b.team_code = f.team_code), teflow_"+formSystemId+"_02 b2, " 
				+ "(select * from teflow_base_data_detail where master_id = ("
				+ "select master_id from teflow_field_basedata where (form_system_id = "+formSystemId+") and (section_id = '02') and (field_id = 'field_02_1'))) g, " 
				+ "(select * from teflow_base_data_detail where master_id = ("
				+ "select master_id from teflow_field_basedata where (form_system_id = "+formSystemId+") and (section_id = '02') and (field_id = 'field_02_4'))) h, " 
				+ "(select * from teflow_base_data_detail where master_id = ("
				+ "select master_id from teflow_field_basedata where (form_system_id = "+formSystemId+") and (section_id = '02') and (field_id = 'field_02_6'))) i "
				+ "where (a.request_no = b.request_no) and (b.request_no = b2.request_no) and (a.status <>'00') and (a.status <>'03') and (a.request_staff_code=we.Staff_code)"
				+ "and (b2.field_02_1 = g.option_value) and (b2.field_02_4 = h.option_value) and (b2.field_02_6 = i.option_value) " 
				+ "and (( (b2.field_02_3 >= ?) and (b2.field_02_3 <= ?) ) or ( (b2.field_02_3 < ?) and  (b2.field_02_5 >= ?) ) ) ";
       	
        if(teamList !=null && !("").equals(teamList)){
          SQL += "and (a.request_staff_code in (select staff_code from tpma_staffbasic where team_code in ("+teamList+"))) "; 
        }else if(currentStaffCode !=null && !("").equals(currentStaffCode)){
          SQL += "and (a.request_staff_code = '"+currentStaffCode+"' ) "; 
        }

        
        if(selectType != null && !("").equals(selectType)){
        	SQL += "and (b2.field_02_1 = '"+selectType+"' ) "; 
        }
        
        SQL += "order by f.team_name, a.request_staff_code, b2.field_02_3";
        
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection detailList = new Vector();
       
        try {
        	stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString (i++, leaveDateBegin);
            stm.setString (i++, leaveDateEnd);
            stm.setString (i++, leaveDateBegin);
            stm.setString (i++, leaveDateBegin);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	LeaveDetailVO leaveDetail = new LeaveDetailVO();
            	leaveDetail.getFormBasicData().setRequestNo(rs.getString("request_no"));
            	leaveDetail.getFormBasicData().setRequestStaffCode(rs.getString("request_staff_code"));
            	leaveDetail.getFormBasicData().setTeamName(rs.getString("team_name"));
            	leaveDetail.setTypeName(rs.getString("type_name"));	
            	leaveDetail.setTypeCode(rs.getString("type_code"));
            	leaveDetail.setApplyWorkingDays(rs.getDouble("apply_working_days"));           
            	leaveDetail.setApplyCalendarDays(rs.getDouble("apply_calendar_days"));          	
            	leaveDetail.setFromDate(rs.getDate("from_date"));
            	leaveDetail.setFromTimeName(rs.getString("from_time_name"));
            	leaveDetail.setToDate(rs.getDate("to_date"));
                leaveDetail.setToTimeName(rs.getString("to_time_name"));                
                leaveDetail.setRemark(rs.getString("remark"));               
                leaveDetail.getFormBasicData().setStatus(rs.getString("status"));
                leaveDetail.getFormBasicData().setCurrentProcessor(rs.getString("current_processor"));   
                leaveDetail.setStaffType(rs.getString("staff_type"));
                
                leaveDetail.getFormBasicData().setFormSystemId(Integer.valueOf(formSystemId));
                detailList.add(leaveDetail);
            }
            return detailList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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
    
    public ArrayList getLeaveSummaryList(int year, String teamList, String currentStaffCode,HashMap leaveSummaryMap) throws DAOException {
        String SQL = "select sb.staff_code,  t.team_name, we.Onboard_date, we.Staff_type, lb.*" +
        		" from tpma_staffbasic sb left join  tpma_team t on (sb.team_code = t.team_code)" +
        		" left join  teflow_user_work_experience we on (sb.staff_code  = we.Staff_code)" +
        		" left join  teflow_leave_balance lb on (sb.staff_code = lb.staff_code and lb.year = ? )" +
        		" where (year(sb.to_date) >= ? )";

  	
        if (teamList != null && !("").equals(teamList)) {
			SQL += " and (sb. team_code in (" + teamList + "))";
		} else if (currentStaffCode != null && !("").equals(currentStaffCode)) {
			SQL += " and (sb.staff_code = '" + currentStaffCode + "' ) ";
		}

        
        SQL += " order by sb.team_code, sb.staff_code";
        
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        ArrayList leaveSummaryList = new ArrayList();  
        if(leaveSummaryMap == null){
        	leaveSummaryMap = new HashMap();
        }
        
        try {
        	stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, year);
            stm.setInt(i++, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

            	LeaveSummaryVO leaveSummary = new LeaveSummaryVO();
            	String staffCode = rs.getString("staff_code").trim();
            	leaveSummary.getLeaveBasicData().setStaffCode(staffCode);
            	leaveSummary.setDepartment(rs.getString("team_name"));
            	leaveSummary.setEffectiveDate(rs.getDate("Onboard_date"));
            	leaveSummary.setStaffType(rs.getString("Staff_type"));
            	
            	leaveSummary.getLeaveBasicData().setAnnualTotalEntitleDays(rs.getDouble("annual_total_entitle_days"));
            	leaveSummary.getLeaveBasicData().setAnnualCarryForwardDays(rs.getDouble("annual_carry_forward_days"));
            	leaveSummary.getLeaveBasicData().setAnnualAppliedDays(rs.getDouble("annual_applied_days"));                
            	leaveSummary.getLeaveBasicData().setAnnualStatutoryEntitleDays(rs.getDouble("annual_statutory_entitle_days"));
            	leaveSummary.getLeaveBasicData().setAnnualCompanyEntitleDays(rs.getDouble("annual_company_entitle_days"));
            	leaveSummary.getLeaveBasicData().setAnnualForfeitDays(rs.getDouble("annual_forfeit_days"));            
            	leaveSummary.getLeaveBasicData().setSickTotalEntitleDays(rs.getDouble("sick_total_entitle_days"));
            	leaveSummary.getLeaveBasicData().setSickAppliedDays(rs.getDouble("sick_applied_days"));
            	
            	leaveSummary.setAnnualEnquiryMonth(0.0);
            	leaveSummary.setAnnualYTD(0.0);
            	leaveSummary.setSickEnquiryMonth(0.0);
            	leaveSummary.setSickYTD(0.0);
            	leaveSummary.setOtherEnquiryMonth(0.0);
            	leaveSummary.setOtherYTD(0.0);
            	leaveSummary.setCommonEnquiryMonth(0.0);
            	leaveSummary.setCommonYTD(0.0);
            	leaveSummary.setRemark("");
            	     	
            	leaveSummaryList.add(leaveSummary);
            	leaveSummaryMap.put(staffCode,leaveSummaryList.size()-1);
            	//leaveSummaryMap.put(staffCode, leaveSummary);  	
            }            
            return leaveSummaryList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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
    
    /**
     * @param CalYearMonthTaken
     * 
     * <li> YTD: the actual leave days that were taken on this year
     * <li> EnquiryMonth: the actual leave days that were taken on this month
     *  
     */
    public void CalYearMonthTaken(ArrayList leaveSummaryList, HashMap leaveSummaryMap, String formSystemId, String yearBegin, String monthBegin, String monthEnd, String teamList,String currentStaffCode,String selectType)throws DAOException {
    	
    	String SQL = "select wfp.request_staff_code, leave_type = b2.field_02_1, working_days = b2.field_02_2, from_date = b2.field_02_3, from_time= b2.field_02_4, to_date = b2.field_02_5, to_time= b2.field_02_6, remark = b2.field_02_7" 
                   +" from teflow_wkf_process wfp,teflow_"+formSystemId+"_01 b1,teflow_"+formSystemId+"_02 b2 "
                   +" where (wfp.status <> '00') and (wfp.status <> '03') and (b2.field_02_10 <> '2') " 
                   +" and (wfp.request_no not in (select cancel_leave_form from teflow_"+formSystemId+"_02 a, teflow_wkf_process b where (a.field_02_10 ='2') and (a.request_no =b.request_no) and ( (b.status = '01') or (b.status = '02') or (b.status = '04') ) ) ) "
                   +" and (wfp.request_no = b1.request_no) and (b1.request_no = b2.request_no) "
                   +" and (( (b2.field_02_3 >= ? ) and (b2.field_02_3 <= ? ) ) or ( (b2.field_02_3 < ? ) and  (b2.field_02_5 >= ? ) ) ) ";
    	
        if (teamList != null && !("").equals(teamList)) {
        	SQL += " and (wfp.request_staff_code in (select staff_code from tpma_staffbasic where team_code in (" + teamList + ")))";
		} else if (currentStaffCode != null && !("").equals(currentStaffCode)) {
			SQL += " and (b1.request_staff_code = '" + currentStaffCode + "' ) ";
		}
        
        if(selectType != null && !("").equals(selectType)){
        	SQL += " and (b2.field_02_1 = '"+selectType+"' ) "; 
        }
        
        SQL += " order by wfp.request_staff_code ";
    	
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection staffLeaveRecord = new Vector();

        try {
        	stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setString (i++, yearBegin);
            stm.setString (i++, monthEnd);
            stm.setString (i++, yearBegin);
            stm.setString (i++, yearBegin);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	String staffCode = rs.getString("request_staff_code").trim();
            	Integer index = (Integer)leaveSummaryMap.get(staffCode);
            	if(index !=null){
            		
	            	LeaveSummaryVO leaveSummary = (LeaveSummaryVO)leaveSummaryList.get(index);
	                //LeaveSummaryVO leaveSummary = (LeaveSummaryVO)leaveSummaryMap.get(staffCode);
	            	
	            	String remark = (rs.getString("remark"));
	            	remark = remark == null ? "" : remark;
	            	
	            	String leaveType = rs.getString("leave_type");
	            	double workingDays = rs.getDouble("working_days");
	            	
	            	String dateFormat = "MM/dd/yyyy";
	            	Date fromDate = rs.getDate("from_date");
	            	String fromDateStr = StringUtil.getDateStr(fromDate, dateFormat);
	            	Date toDate = rs.getDate("to_date");
	            	String toDateStr = StringUtil.getDateStr(toDate, dateFormat);
	            	
	            	String fromTime = rs.getString("from_time").trim();
	            	String toTime = rs.getString("to_time").trim();
	            	
	            	/**** Leave in this month of the year ****/
					if ((LeaveEntitleHelper.compareDateStr(fromDateStr, monthBegin, dateFormat) != 1 && LeaveEntitleHelper.compareDateStr(fromDateStr, monthEnd, dateFormat) != 2)
							|| (LeaveEntitleHelper.compareDateStr(fromDateStr, monthBegin, dateFormat) == 1 && LeaveEntitleHelper.compareDateStr(toDateStr, monthBegin, dateFormat) != 1)) {
	
						// All applied dates are in the range between monthBegin and monthEnd,
						// then the leave taken must be equal to the applied working days.
						if ((LeaveEntitleHelper.compareDateStr(fromDateStr, monthBegin, dateFormat) != 1) && (LeaveEntitleHelper.compareDateStr(toDateStr, monthEnd, dateFormat) != 2)) {
							this.setEnquiryMonthOnType(selectType,leaveSummary, leaveType, remark,workingDays);
							this.setYTDOnType(selectType,leaveSummary, leaveType, workingDays);
						}
						// Applied dates are over the range between monthBegin and monthEnd,
						// Find out the actual taken days of this month
						else {
							String aFromDateStr = fromDateStr;
							String aToDateStr = toDateStr;
							String aFromTime = fromTime;
							String aToTime = toTime;
	
							// if from date before this month, only care the days in
							// this month
							if (LeaveEntitleHelper.compareDateStr(fromDateStr, monthBegin, dateFormat) == 1) {
								aFromDateStr = monthBegin;
								aFromTime = "3";
							}
	
							// if to date after this month, only care the days in
							// this month
							if (LeaveEntitleHelper.compareDateStr(toDateStr, monthEnd, dateFormat) == 2) {
								aToDateStr = monthEnd;
								aToTime = "3";
							}
	
							double aWorkingDays = LeaveEntitleHelper.daysBetweenDateStr(aFromDateStr, aToDateStr, dateFormat, aFromTime, aToTime);
							double holidaysBetween = this.getHolidays(aFromDateStr, aToDateStr, aFromTime, aToTime);
							aWorkingDays -= holidaysBetween;
							this.setEnquiryMonthOnType(selectType,leaveSummary, leaveType, remark,aWorkingDays);	
							
							
							String yaFromDateStr = fromDateStr;
							String yaFromTime = fromTime;
							// if from date before this month, only care the days in
							// this year
							if (LeaveEntitleHelper.compareDateStr(fromDateStr, yearBegin, dateFormat) == 1) {
								yaFromDateStr = yearBegin;
								yaFromTime = "3";
							}
							
							double yaWorkingDays = LeaveEntitleHelper.daysBetweenDateStr(yaFromDateStr, aToDateStr, dateFormat, yaFromTime, aToTime);
							double yaHolidaysBetween = this.getHolidays(yaFromDateStr, aToDateStr, yaFromTime, aToTime);
							yaWorkingDays -= yaHolidaysBetween;
							this.setYTDOnType(selectType,leaveSummary, leaveType, yaWorkingDays);							
						}
						
	            	/**** Leave not in this month of the year ****/
	            	}else{            		
	            		// All applied dates are in the range between yearBegin and  monthEnd, 
	                	// then the leave taken must be equal to the applied working days. 
	            		if ((LeaveEntitleHelper.compareDateStr(fromDateStr, yearBegin,dateFormat) != 1) && (LeaveEntitleHelper.compareDateStr(toDateStr, monthEnd, dateFormat) != 2)){
	                		this.setYTDOnType(selectType,leaveSummary, leaveType, workingDays);
	                	}      		
	                	// Applied dates are over the range between yearBegin and  monthEnd,
	                	// Find out the actual taken days of this year
	                	else{
	                		String aFromDateStr = fromDateStr;
	                		String aToDateStr = toDateStr;
	                		String aFromTime = fromTime;
	                		String aToTime = toTime;
	                		
	                		//if from date before this year, only care the days in this year
	                		if(LeaveEntitleHelper.compareDateStr(fromDateStr, yearBegin, dateFormat) ==1){
	                			aFromDateStr = yearBegin;
	                			aFromTime ="3";
	                		}
	                		
	                		//if to date after this month, only care the days in this month
	                		if(LeaveEntitleHelper.compareDateStr(toDateStr, monthEnd, dateFormat) == 2){
	                			aToDateStr = monthEnd;
	                			aToTime ="3";
	                		}
	                		
	                		double aWorkingDays = LeaveEntitleHelper.daysBetweenDateStr(aFromDateStr, aToDateStr, dateFormat, aFromTime, aToTime);
	                		double holidaysBetween = this.getHolidays(aFromDateStr, aToDateStr, aFromTime, aToTime);
	                		aWorkingDays -= holidaysBetween;
	                		
	                		this.setYTDOnType(selectType,leaveSummary, leaveType, aWorkingDays);	
	                	}           		
	            	}
					leaveSummaryList.set(index, leaveSummary);
	            	//leaveSummaryMap.put(staffCode, leaveSummary);
            	}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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
    
    public double getHolidays(String beginDateStr, String endDateStr,String beginTime, String endTime)throws DAOException {  
    	
    	String fromDateTime = beginDateStr.substring(6,10) +"-"+beginDateStr.substring(0, 2)+"-"+beginDateStr.substring(3, 5);
    	String toDateTime = endDateStr.substring(6,10) +"-"+endDateStr.substring(0, 2)+"-"+endDateStr.substring(3, 5);
    	
    	fromDateTime = ("2").equals(beginTime) ? fromDateTime + " 12:00" : fromDateTime + " 00:00";
    	toDateTime = ("1").equals(endTime) ? toDateTime + " 00:00" : toDateTime + " 12:00";
    	
        String SQL = "select sum (days) as total_holidays from(select from_date, afrom_date, to_date, ato_date, "
            +"days = datediff(hour, afrom_date, dateadd(hour, 12, ato_date)) from "
            +"(select from_date, afrom_date = case when '"+fromDateTime+"' > from_date then '"+fromDateTime+"' else from_date end, to_date, " 
            +"ato_date = case when '"+toDateTime+"' < to_date then '"+toDateTime+"' else to_date end, status from " 
            +"(select from_date = case status when 2 then dateadd(hour, 12, from_date) else from_date end," 
            +"to_date = case status when 3 then to_date else  dateadd(hour, 12, to_date) end, status from teflow_holiday_define " 
            +"where set_year >= year('"+fromDateTime+"') and set_year <=  year('"+toDateTime+"')) a "
            +"where  (from_date >= '"+fromDateTime+"' and from_date <= '"+toDateTime+"') or (from_date <= '"+fromDateTime+"' and  to_date >= '"+fromDateTime+"')) b )c";
        
        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        double result = 0.0;

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	result = rs.getDouble("total_holidays");
            	result = result / 24.0;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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
    
    private void setEnquiryMonthOnType(String conditionType, LeaveSummaryVO leaveSummary, String leaveType, String remark,double days){
    	
    	// If get all type records
		if (conditionType == null || "".equals(conditionType)) {

			// Annual leave
			if ("01".equals(leaveType)) {
				leaveSummary.setAnnualEnquiryMonth(leaveSummary.getAnnualEnquiryMonth() + days);
			}
			// Sick leave
			else if (("11").equals(leaveType)) {
				leaveSummary.setSickEnquiryMonth(leaveSummary.getSickEnquiryMonth() + days);
			}
			// Other leave
			else {
				leaveSummary.setOtherEnquiryMonth(leaveSummary.getOtherEnquiryMonth() + days);
			}

			// If get specific type records
		} else {
			leaveSummary.setCommonEnquiryMonth(leaveSummary.getCommonEnquiryMonth() + days);
			
			if (remark != null && !("").equals(remark)) {
				
				if (leaveSummary.getRemark() == "") {
					leaveSummary.setRemark(remark);
				} else {
					leaveSummary.setRemark(leaveSummary.getRemark() + "; " + remark);
				}
			}
		}
    	
    }
    
    private void setYTDOnType(String conditionType,LeaveSummaryVO leaveSummary, String leaveType, double days){
    	
    	// If get all type records
		if (conditionType == null || "".equals(conditionType)) {
			// Annual leave
			if ("01".equals(leaveType)) {
				leaveSummary.setAnnualYTD(leaveSummary.getAnnualYTD() + days);
			}
			// Sick leave
			else if (("11").equals(leaveType)) {
				leaveSummary.setSickYTD(leaveSummary.getSickYTD() + days);
			}
			// Other leave
			else {
				leaveSummary.setOtherYTD(leaveSummary.getOtherYTD() + days);
			}
			// If get specific type records
		} else {
			leaveSummary.setCommonYTD(leaveSummary.getCommonYTD() + days);
		}
    }   
    
    

    public Collection getMonthlyList() throws DAOException {
        String SQL = "select year(submission_date) as year, month(submission_date) as month, count(*) as total "
                + "from teflow_wkf_process group by year(submission_date), month(submission_date) order by year desc, month desc";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection monthlyList = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveMonthlyVO leaveMonthly = new LeaveMonthlyVO();
                leaveMonthly.setYear(rs.getString("year"));
                leaveMonthly.setMonth(rs.getString("month"));
                leaveMonthly.setTotal(rs.getInt("total"));
                monthlyList.add(leaveMonthly);
            }
            return monthlyList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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

    public Collection getMonthlyRecords(String year, String month) throws DAOException {
        String SQL = "select a.request_no, a.submission_date, a.submit_staff_code, c.staff_name as submit_staff_name, "
                + "a.request_staff_code, d.staff_name as request_staff_name, b.company_id, e.org_name as company_name, "
                + "b.team_code, f.team_name, b2.field_02_1 as type_code, g.option_label as type_name, "
                + "b2.field_02_2 as apply_days, b2.field_02_3 as from_date, b2.field_02_5 as to_date, b2.field_02_7 as remark, "
                + "b2.field_02_4 as from_time_code, h.option_label as from_time_name, "
                + "b2.field_02_6 as to_time_code, i.option_label as to_time_name "
                + "from teflow_wkf_process a left join tpma_staffbasic c on (a.submit_staff_code = c.staff_code) "
                + "left join tpma_staffbasic d on (a.request_staff_code = d.staff_code), "
                + "teflow_110_01 b left join teflow_company e on (b.company_id = e.org_id) "
                + "left join tpma_team f on (b.team_code = f.team_code), teflow_110_02 b2, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_1'))) g, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_4'))) h, "
                + "(select * from teflow_base_data_detail where master_id in ("
                + "select master_id from teflow_field_basedata where (form_system_id = 110) and (section_id = '02') and (field_id = 'field_02_6'))) i "
                + "where (a.request_no = b.request_no) and (b.request_no = b2.request_no) "
                + "and (b2.field_02_1 = g.option_value) and (b2.field_02_4 = h.option_value) "
                + "and (b2.field_02_6 = i.option_value) and (year(a.submission_date) = ?) and (month(a.submission_date) = ?) "
                + "order by submission_date";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection monthlyRecords = new Vector();

        try {
            stm = conn.prepareStatement(SQL);
            int i = 1;
            stm.setInt(i++, Integer.parseInt(year));
            stm.setInt(i++, Integer.parseInt(month));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveVO leave = new LeaveVO();
                leave.getFormBasicData().setRequestNo(rs.getString("request_no"));
                leave.getFormBasicData().setRequestDate(rs.getTimestamp("submission_date"));
                leave.getFormBasicData().setSubmiterCode(rs.getString("submit_staff_code"));
                leave.getFormBasicData().setSubmiterName(rs.getString("submit_staff_name"));
                leave.getFormBasicData().setRequesterCode(rs.getString("request_staff_code"));
                leave.getFormBasicData().setRequesterName(rs.getString("request_staff_name"));
                leave.getFormBasicData().setCompanyCode(rs.getString("company_id"));
                leave.getFormBasicData().setCompanyName(rs.getString("company_name"));
                leave.getFormBasicData().setTeamCode(rs.getString("team_code"));
                leave.getFormBasicData().setTeamName(rs.getString("team_name"));

                leave.setTypeCode(rs.getString("type_code"));
                leave.setTypeName(rs.getString("type_name"));
                leave.setApplyDays(rs.getDouble("apply_days"));
                leave.setFromDate(rs.getDate("from_date"));
                leave.setToDate(rs.getDate("to_date"));
                leave.setFromTimeCode(rs.getString("from_time_code"));
                leave.setFromTimeName(rs.getString("from_time_name"));
                leave.setToTimeCode(rs.getString("to_time_code"));
                leave.setToTimeName(rs.getString("to_time_name"));
                leave.setRemark(rs.getString("remark"));
                monthlyRecords.add(leave);
            }
            return monthlyRecords;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
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
