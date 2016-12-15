package com.aiait.eflow.job;

import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.action.MedicalBalanceAction;
import com.aiait.eflow.util.StringUtil;

/**
 * Waiting Node Job
 * 
 * @version 2010-11-19
 */
public class MedicalDailyJob extends BaseJob {

    public static final String JOB_ID = "medical_daily_job";
    protected String defaultYearlyJobDate ="12/01";

    public MedicalDailyJob(int hour, int minute, int second) {
        super(hour, minute, second);
    }

    public String getJobId() {
        return JOB_ID;
    }

    public SchedulerCtrlTask createTask() {
        return new SchedulerCtrlTask(JOB_ID) {

            protected void process() {
            	
            	String todayStr = StringUtil.getDateStr(new Date(), "MM/dd/yyyy");
            	
            	updateStaffMedicalEntitlementDaily(todayStr);
            	
            	String paramtYearlyJobDate = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_MEDICAL_YEARLY_JOB_DAY);
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
    
    
    public void updateStaffMedicalEntitlementDaily(String todayStr) {
    			
		try {
			System.out.println("Begin - Processing Medical Daily Job of update balance ... (" + Thread.currentThread().getId() + ")");
			long startTime = System.currentTimeMillis();

			// Processing ...			
			int recordCount = MedicalBalanceAction.updateStaffMedicalEntitlementDaily(todayStr);	
			long costTime = System.currentTimeMillis() - startTime;
			System.out.println("End - Processing Medical Daily Job of update balance(" + recordCount + " records / " + costTime + " ms)");	
			
		} catch (Exception ex) {
			System.out.println ("Faild to process Medical Daily Job!");
			ex.printStackTrace();						
		} finally {
			//
		}
	}
    
    public void generateComingYearBalance(String todayStr) {
    	
		try {
			System.out.println("Begin - Processing Medical Yearly Job of generate new year balance... (" + Thread.currentThread().getId() + ")");
			long startTime = System.currentTimeMillis();
			
			// Processing ...		
			int recordCount = MedicalBalanceAction.generateComingYearBalance(todayStr);
			long costTime = System.currentTimeMillis() - startTime;
			System.out.println("End - Processing Medical Yearly Job of generate new year balance (" + recordCount+ " records / " + costTime + " ms)");
			
		} catch (Exception ex) {
			System.out.println ("Faild to process Medical Yearly Job!");
			ex.printStackTrace();			
		} finally {
			//
		}
	}
}
