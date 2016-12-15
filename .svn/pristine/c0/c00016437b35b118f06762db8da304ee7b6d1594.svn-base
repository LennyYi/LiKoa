package com.aiait.eflow.common.helper;

import java.text.ParseException;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.LeaveBalanceCommonInforVO;
import com.aiait.eflow.housekeeping.vo.MedicalBalanceVO;
import com.aiait.eflow.util.StringUtil;

public class MedicalEntitleHelper {
	
	public static int C_ENTITLEMENT1 = 1200;
	public static int C_ENTITLEMENT2 = 1800;
	public static int H_ENTITLEMENT1 = 38000;
	public static int H_ENTITLEMENT2 = 57000;


	/**
	 * Get the days between two date string
	 * @throws ParseException 
	 * 
	 */
	public static int daysBetweenDateStr (String beginDateStr, String endDateStr, String dateFormat)
	  throws ParseException{
		
		Date beginDate = StringUtil.stringToDate(beginDateStr,  dateFormat);
		Date endDate = StringUtil.stringToDate(endDateStr, dateFormat);
		
		int result = (int) StringUtil.getWorkingDays(beginDate, "", endDate, "");
		result -=1;
		return result;
	}
	
	/**
	 * get the basic entitlement
	 * @throws ParseException 
	 * 
	 *<li>newGrade: 
	 *<li>type: C-Clinic; H-Hospitalization
	 * 
	 */
	public static double getBasicEntitlement(double newGrade,String type){
		double result = type == "C" ? (newGrade > 3 ? C_ENTITLEMENT2 : C_ENTITLEMENT1) 
                : (newGrade > 3 ? H_ENTITLEMENT2 : H_ENTITLEMENT1);
		
		return result;
	}
	
	/**
	 * Calculate the actual entitlement
	 * @throws ParseException 
	 * 
	 *<li> Actual =ENTITLEMENT / 365 * (11/30 - gradeEffectDate)
	 * 
	 */
	public static MedicalBalanceVO calNewGradeEntitlement (LeaveBalanceCommonInforVO leaveBalanceCommonInforVO, MedicalBalanceVO medicalBalanceVO)
	  throws ParseException{
			
		//Basic entitlement
		double CEntitlement = MedicalEntitleHelper.getBasicEntitlement(leaveBalanceCommonInforVO.getNewGrade(), "C");
		double HEntitlement = MedicalEntitleHelper.getBasicEntitlement(leaveBalanceCommonInforVO.getNewGrade(), "H");
		String gradeEffectDate = leaveBalanceCommonInforVO.getGradeEffectDate();
		double oldGrade = leaveBalanceCommonInforVO.getOldGrade();
		int AIAITWorkDays = leaveBalanceCommonInforVO.getCompWorkDays();
		
		int gradeEffectMonth = Integer.parseInt(gradeEffectDate.substring(0,2));
		int gradeEffectYear = Integer.parseInt(gradeEffectDate.substring(6, 10));
		gradeEffectYear = gradeEffectMonth == 12 ? gradeEffectYear + 1  : gradeEffectYear;
		int currentYear = medicalBalanceVO.getYear();
		
		//if grade changed was happened in this year
		if(currentYear == gradeEffectYear 
		&& ( (oldGrade <=3 && leaveBalanceCommonInforVO.getNewGrade() >3) ||  oldGrade ==0) ){
			//New staff
			if(oldGrade == 0){
				if(AIAITWorkDays < 365){
					String yearEndDateStr ="11/30/";
					String yearStr=Integer.toString(medicalBalanceVO.getYear());
					
					// Use end date of this year if before May.
					//if(onBoardMonth < 5){
						yearEndDateStr += yearStr;
					//}else{
					//	yearEndDateStr += Integer.toString(Integer.parseInt(onBoardYear) + 1 );
					//}
					
					int daysBetween = daysBetweenDateStr (gradeEffectDate, yearEndDateStr, "MM/dd/yyyy") + 1;
					daysBetween = daysBetween > 365 ? 365 : daysBetween;
					
					CEntitlement = Math.round((double)CEntitlement/ 365 * daysBetween);
					HEntitlement = Math.round((double)HEntitlement/ 365 * daysBetween);
					//Entitlement = Math.round((double)Entitlement/ 365 * AIAITWorkDays);
				}
			}
			
			//Old staff
			else{			
				int oldGradeMonth = Integer.parseInt(gradeEffectDate.substring(0,2));
				oldGradeMonth = oldGradeMonth == 12 ? 0 : (oldGradeMonth +1);
				int newGradeMonth = 12 - oldGradeMonth;
				
				double oldGradeEntitlement = Math.round((double)C_ENTITLEMENT1 / 12 * oldGradeMonth);
				double newGradeEntitlement = Math.round((double)C_ENTITLEMENT2 / 12 * newGradeMonth);
				CEntitlement = oldGradeEntitlement + newGradeEntitlement;
				
				oldGradeEntitlement = Math.round((double)H_ENTITLEMENT1 / 12 * oldGradeMonth);
				newGradeEntitlement = Math.round((double)H_ENTITLEMENT2 / 12 * newGradeMonth);
				HEntitlement = oldGradeEntitlement + newGradeEntitlement;
			}									
		}

		MedicalBalanceVO newRecord = new MedicalBalanceVO();
		newRecord.clone(medicalBalanceVO);
		newRecord.setAllEntitlement(CEntitlement, HEntitlement, CEntitlement, HEntitlement, CEntitlement, HEntitlement);
		return newRecord;
	}
		
	/**
	 * Return the end date string of the month from the given date string 
	 * 
	 * <li> dateStr:  Format must be MM/dd/yyyy
	 * 
	 */
	
	public static String monthEndDate(String dateStr){
		
		Calendar calendar = Calendar.getInstance(); 
		String yearStr = dateStr.substring(6, 10);
		String monthStr = dateStr.substring(0, 2);
		
		Integer year = Integer.parseInt(yearStr);
		Integer month = Integer.parseInt(monthStr)-1;
		
		calendar.set(year, month, 1);
		Integer  maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	  
		String result = monthStr+"/"+maxDay+"/"+year; 
		return result; 
	}
	
	/**
	 * Return the last month of current month
	 * 
	 * <li> yearMonth:  Format must be yyyy/MM
	 * 
	 */
	public static String getPreviousYearMonth(String currentYearMonth){
		String lastYearMonth = StringUtil.getPreviousYearMonth(currentYearMonth);
		return lastYearMonth;
	}
	
	/**
	 * Return the last day of current date
	 * 
	 * <li> currentDate:  Format must be MM/dd/yyyy
	 * @throws Exception 
	 * 
	 */
	public static String getPreviousDay(String currentDate) throws Exception{
		String lastDate = StringUtil.afterNDay(-1, "MM/dd/yyyy", currentDate);
		return lastDate;
	}
	
	public static void main(String[] args){
	
	
	//---
	System.out.println("cal func: ");
		
	try {

		LeaveBalanceCommonInforVO leaveBalanceCommonInforVO = new LeaveBalanceCommonInforVO();
		leaveBalanceCommonInforVO.setCompWorkDays(677);
		leaveBalanceCommonInforVO.setOldGrade(3);
		leaveBalanceCommonInforVO.setNewGrade(5);
		leaveBalanceCommonInforVO.setGradeEffectDate("12/01/2012");
		
		MedicalBalanceVO medicalBalanceVO = new MedicalBalanceVO ();
		medicalBalanceVO.setYear(2013);
		medicalBalanceVO.setStaffCode("");
		medicalBalanceVO = MedicalEntitleHelper.calNewGradeEntitlement(leaveBalanceCommonInforVO, medicalBalanceVO);
		
		System.out.println("New staff within year: " +medicalBalanceVO.getYear()+" "+ medicalBalanceVO.getStaffCEntitlement() +" " + medicalBalanceVO.getStaffHEntitlement());
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
