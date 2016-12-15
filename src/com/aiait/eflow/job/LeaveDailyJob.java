package com.aiait.eflow.job;

import java.text.SimpleDateFormat;
import java.util.*;


import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.action.LeaveBalanceAction;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.dao.LeaveBalanceCommonInforDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.util.OverDueUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.action.WorkFlowProcessAction;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.db.*;




/**
 * Waiting Node Job
 * 
 * @version 2010-11-19
 */
public class LeaveDailyJob extends BaseJob {

    public static final String JOB_ID = "leave_daily_job";
    protected String defaultYearlyJobDate ="01/01";

    public LeaveDailyJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
            	
            	String todayStr = StringUtil.getDateStr(new Date(), "MM/dd/yyyy");
            	
            	updateStaffLeaveEntitlementDaily(todayStr);
            	
            	String paramtYearlyJobDate = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LEAVE_YEARLY_JOB_DAY);
            	 if (paramtYearlyJobDate == null || ("").equals(paramtYearlyJobDate.trim())){
            		 paramtYearlyJobDate = defaultYearlyJobDate;
            	 }
            	
            	String monthDayStr = todayStr.substring(0,5);
            	
            	if(monthDayStr.equals(paramtYearlyJobDate)){
            		generateComingYearBalance(todayStr);
            	}            	
            }
        };
    }
    
    
    public void updateStaffLeaveEntitlementDaily(String todayStr) {
    	
    	IDBManager dbManager = null;	
    	Collection gradeList = null;
    	Collection gradeEmailList = null;    	
		Collection experienceList = null;
		Collection experienceEmailList = null;		
		
		try {
			System.out.println("Begin - Processing Leave Daily Job of update balance ... (" + Thread.currentThread().getId() + ")");
			long startTime = System.currentTimeMillis();

			// Processing ...			
			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
			gradeList = leaveBalanceCommonInforDAO.getListByGradeEffDate(todayStr,"leave");
			experienceList = leaveBalanceCommonInforDAO.getListByExperienceEffDate(todayStr);
			
			if (gradeList.size() > 0) {		
				gradeEmailList = LeaveBalanceAction.UpdateStaffAnnualEntitleForGradeChange(gradeList, false);
				try {
					LeaveBalanceAction.emailSuccessUpdate(gradeEmailList, "1");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (experienceList.size() > 0) {
				experienceEmailList = LeaveBalanceAction.UpdateStaffAnnualEntitleForExperienceChange(experienceList);
				try {
					LeaveBalanceAction.emailSuccessUpdate(experienceEmailList, "2");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			long costTime = System.currentTimeMillis() - startTime;
			int recordCount = gradeList.size()+experienceList.size();
			System.out.println("End - Processing Leave Daily Job of update balance(" + recordCount + " records / " + costTime + " ms)");	
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			//Send email to notify the error
			try {
				LeaveBalanceAction.emailFaildUpdate(gradeList, "1", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}

		

			try {
				LeaveBalanceAction.emailFaildUpdate(experienceList, "2", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
	}
    
    public void generateComingYearBalance(String todayStr) {
    	
    	IDBManager dbManager = null;	
    	Collection staffList = null;
    	Collection staffEmailList = null;
		try {
			System.out.println("Begin - Processing Leave Yearly Job of generate new year balance... (" + Thread.currentThread().getId() + ")");
			long startTime = System.currentTimeMillis();
			// Processing ...		
			String currentYear = todayStr.substring(6, 10);
			String yearBeginStr = "01/01/" + currentYear;

			dbManager = DBManagerFactory.getDBManager();
			LeaveBalanceCommonInforDAO leaveBalanceCommonInforDAO = new LeaveBalanceCommonInforDAO(dbManager);
			staffList = leaveBalanceCommonInforDAO.getAllStaffBalanceCommonInfor(yearBeginStr);				
			
			if (staffList.size() > 0) {				
				//Update the new year balance first.
				LeaveBalanceAction.generateNewYearBalance(staffList,yearBeginStr);
				//Then update the statutory
				staffEmailList = LeaveBalanceAction.UpdateStaffAnnualStatutory(staffList, yearBeginStr);	
				try {
					LeaveBalanceAction.emailSuccessUpdate(staffEmailList, "3");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			long costTime = System.currentTimeMillis() - startTime;
			System.out.println("End - Processing Leave Yearly Job of generate new year balance (" + staffList.size()+ " records / " + costTime + " ms)");
		} catch (Exception ex) {
			
			ex.printStackTrace();
			//Send email to notify the error
			try {
				LeaveBalanceAction.emailFaildUpdate(staffList, "0", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				LeaveBalanceAction.emailFaildUpdate(staffEmailList, "3", ex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
	}
}
