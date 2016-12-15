package com.aiait.eflow.common.helper;

import java.text.ParseException;
import java.util.*;

import com.aiait.eflow.housekeeping.vo.LeaveBalanceVO;
import com.aiait.eflow.util.StringUtil;



public class LeaveEntitleHelper {
	
	public static int LEAVE_BALANCE_1YEAR = 365;
	public static int LEAVE_BALANCE_5YEARS = 1825;
	public static int LEAVE_BALANCE_10YEARS = 3650;
	public static int LEAVE_BALANCE_15YEARS = 5475;
	public static int LEAVE_BALANCE_20YEARS = 7300;
	
	public static int SICK_ENTITLEMENT = 24;
	
	private final static int[][] entitleDay =  {{10, 12, 17},
                                                {12, 15, 19},
                                                {15, 18, 22},
                                                {18, 22, 24}};
	
	/**
	*<li>                         年休假最高天数 
	*<li>
	*<li>AIA / AIAIT 服务年资 	 Band 1-3 	Band 4-5	Band 6 and above 
	*<li> 
	*<li> 0  - <5年    	         10个工作日 	12个工作日 	17个工作日 
	*<li> 5 - <10年 	             12个工作日 	15个工作日 	19个工作日 
	*<li> 10 -<15年  	         	 15个工作日    18个工作日 	22个工作日 
	*<li> 15年及以上 	         	 18个工作日 	22个工作日 	24个工作日
	*<li> <1 年                 			    参照“1 - <5年”的年休假,并按照在公司服务的日历天数折算 
	* 
	*/
	public static int getBasicTotalAnnualEntitle (int totalWorkDays, double grade){
		
		if(grade ==0){
			return 0;
		}
				
		int row = totalWorkDays / LEAVE_BALANCE_5YEARS;
				
		if(row >3 ){
			row = 3;
		}
		
		int column = grade < 4.0 ? 0 : (grade < 6.0 ? 1:2);
		
		return entitleDay [row][column];
	}
	
	
	private final static int[] statutoryDay ={5, 10, 15};
	/**
	*<li>             法定年休假天数
	*<li> 
	*<li>累计工作时间  	        法定年休假天数(所有员工) 
	*<li>
	*<li>1 年 - <10年 	          5个工作日  
	*<li>10年 - <20年    	         10个工作日  
	*<li>20年及以上  	         15个工作日  
	* 
	*/

	public static int getStatutoryAnnualEntitle (int totalWorkDays){
		
		if(totalWorkDays < LEAVE_BALANCE_1YEAR){
		//如累计工作时间未满一年的，不享有法定年休假	
			return 0;
		}
		
		int column = totalWorkDays / LEAVE_BALANCE_10YEARS;
				
		if(column >2 ){
			column = 2;
		}
				
		return statutoryDay [column];
	}	
	
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
	 * Get the days between two date string include half day
	 * 
	 * <li>Type = 1, AM
	 * <li>Type = 2, PM
	 * <li>Type = 3, Full Day
	 * 
	 * @throws ParseException 
	 * 
	 */
	
	public static double daysBetweenDateStr (String beginDateStr, String endDateStr, String dateFormat, String beginType,String endType)
	  throws ParseException{
		
		Date beginDate = StringUtil.stringToDate(beginDateStr,  dateFormat);
		Date endDate = StringUtil.stringToDate(endDateStr, dateFormat);
		
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(beginDate);
		calEnd.setTime(endDate);
		
		int days = StringUtil.getDaysBetween(calStart, calEnd);
		double result = days;
		
		if("1".equals(endType)){
			result += 0.5;			
		}else{
			result += 1.0;
		}
		
		if("2".equals(beginType)){
			result -=0.5; 
		}
		
		return result;
	}
	
	/**
	 * Compare two date string
	 * @throws ParseException 
	 * 
	 * <li>If beginDateStr  before endDateStr return 1; 
	 * <li>If beginDateStr  after endDateStr return 2; 
	 * <li>If beginDateStr  equal endDateStr return 0; 
	 * 
	 */
	
	public static int compareDateStr (String beginDateStr, String endDateStr, String dateFormat)
	  throws ParseException{
		
		Date beginDate = StringUtil.stringToDate(beginDateStr,  dateFormat);
		Date endDate = StringUtil.stringToDate(endDateStr, dateFormat);
		
		int result =0;
		
		if(beginDate.before(endDate)){
			result =1;
		}else if (beginDate.after(endDate)){
			result =2;
		}
		
		return result;
	}
	
	/**
	 * Calculate the actual entitlement base on the working days and basic entitlement
	 * @throws ParseException 
	 * 
	 * <li> Actual = (endDate - beginDate) / 365 * basic entitlement
	 * 
	 */
	
	public static double calActualAnnualLeaveDays(String beginDateStr, String endDateStr, String onBoardDateStr, String dateFormat, double grade)
	  throws ParseException{
		
		
		//Working days of beginDate and endDate
		int daysBetween = daysBetweenDateStr (beginDateStr, endDateStr, dateFormat);
		
		//Total working days till endDate
		int totalWorkDays =daysBetweenDateStr(onBoardDateStr, endDateStr, dateFormat);

		//Basic entitlement
		int basicEntitlement = getBasicTotalAnnualEntitle (totalWorkDays, grade);
		
		
		//Actual entitlement
		double actualEntitlement = (double)daysBetween / 365 * basicEntitlement;
		
		return actualEntitlement;
	}
	
	/**
	 * Calculate the statutory entitlement base on the working days
	 * @throws ParseException 
	 * 
	 *
	 * 
	 */
	
	public static double calStatutoryLeaveDays(String beginDateStr, String endDateStr, String onBoardDateStr, String dateFormat, int preWorkDays)
	  throws ParseException{
		
		
		//Working days of beginDate and endDate
		int daysBetween = daysBetweenDateStr (beginDateStr, endDateStr, dateFormat);
		
		//Total working days till endDate
		int totalWorkDays =preWorkDays + daysBetweenDateStr(onBoardDateStr, endDateStr, dateFormat);

		//Basic entitlement
		int basicEntitlement = getStatutoryAnnualEntitle (totalWorkDays);
				
		//Actual entitlement
		double actualEntitlement = (double)daysBetween / 365 * basicEntitlement;
		
		return actualEntitlement;
	}
	
	/**
	 * Calculate the statutory entitlement base on the working days
	 * @throws ParseException 
	 * 
	 *
	 * 
	 */
	
	public static String findStatutoryChangeDate(String onBoardDateStr, String endDateStr, String dateFormat, int preWorkDays)
	  throws Exception{
		
		
		//Working days of beginDate and endDate
		int daysBetween = daysBetweenDateStr (onBoardDateStr, endDateStr, dateFormat);
		
		//Total working days till endDate
		int totalWorkDays = preWorkDays + daysBetween;
		
		if(totalWorkDays < LEAVE_BALANCE_1YEAR){
			return null;
		}
		
		int finddays =0;
		if(totalWorkDays >= LEAVE_BALANCE_20YEARS){
			finddays = LEAVE_BALANCE_20YEARS;
		}else if (totalWorkDays >= LEAVE_BALANCE_10YEARS){
			finddays = LEAVE_BALANCE_10YEARS;
		}else if (totalWorkDays >= LEAVE_BALANCE_1YEAR){
			finddays = LEAVE_BALANCE_1YEAR;
		}
		
		String result = StringUtil.afterNDay(finddays -preWorkDays , dateFormat, onBoardDateStr);		
        return result;
	}
	
	/**
	 * Calculate the actual sick entitlement days base on the working days and basic entitlement
	 * @throws ParseException 
	 * 
	 * <li> Actual = (endDate - onBoardDate) / 365 * basic entitlement
	 * 
	 */
	
	public static double calActualSickLeaveDays(String todayStr,String onBoardDateStr, String dateFormat)
	  throws ParseException{
		
		String begin = todayStr;
		
		String currentYearStr = begin.substring(6,10);
		
		String endDateStr = "12/31/"+currentYearStr;
		
		int onBoardYear = Integer.parseInt(onBoardDateStr.substring(6,10));
		int currentYear = Integer.parseInt(currentYearStr);
		
		double actualEntitlement=0.0;
		
		//Joined before this year, have 24 days
		if(onBoardYear < currentYear){
			actualEntitlement = (double) SICK_ENTITLEMENT;
		}else{

		//Working days of beginDate and endDate
		int daysBetween = daysBetweenDateStr (onBoardDateStr, endDateStr, dateFormat);
		daysBetween +=1;
		
		//Actual sick entitlement
            actualEntitlement = (double)daysBetween / 365 * SICK_ENTITLEMENT;
		}
		
		return actualEntitlement;
	}
	
	/**
	 * Round down the days base on the decimal part 
	 * 
	 * <li> 0—0.24=0
	 * <li> 0.25—0.74=0.5
	 * <li> 0.75—0.99=1
	 * 
	 */
	
	public static double RoundDownDays(double days){
			    	 		
		int intPart = (int) days;
		double decimalPart = days - intPart;
		
		double decimalPartRD = Math.round(decimalPart*100.0)/100.0;
		
		if (decimalPartRD < 0.25){
			decimalPartRD = 0.0;
		}else if (decimalPartRD < 0.75){
			decimalPartRD = 0.5;
		}else {
			decimalPartRD = 1.0;
		}
		
		double result = (double)intPart + decimalPartRD;
		
		return result; 

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
	 * Return the total holidays of the given date range 
	 * 
	 * <li> dateStr:  Format must be MM/dd/yyyy
	 */	
	public static double getHolidays(String beginDateStr, String endDateStr, HashMap holidayMap) {
        double holidays = 0;
        int count = 0;
        String endDate = endDateStr;
        String beginDate = beginDateStr;
        while (!endDate.equals(beginDate)) { 
            try {
                endDate = StringUtil.afterNDay(-1, "MM/dd/yyyy", endDate);
                if (holidayMap.containsKey(endDate)) {
                    String type = (String) holidayMap.get(endDate);
                    if ("5".equals(type)) {
                        holidays++;
                    } else {
                        holidays = holidays + 0.5;
                    }
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return holidays;
    }
	
	public static LeaveBalanceVO generateLeaveBalanceRecord(String staffCode, String onBoardDate, String toDateStr, String dateFormat, double totalEntitleDays, double annualStatutoryEntitle) {

		LeaveBalanceVO newRecord = new LeaveBalanceVO();

		try {			
				String year = toDateStr.substring(6, 10);				
				newRecord.setStaffCode(staffCode);
			    newRecord.setYear(Integer.parseInt(year));
				
				newRecord.setAnnualTotalEntitleDays(totalEntitleDays);				
				newRecord.setAnnualStatutoryEntitleDays(annualStatutoryEntitle);
				newRecord.setAnnualCompanyEntitleDays(totalEntitleDays- annualStatutoryEntitle);

				newRecord.setAnnualAppliedDays(0);
				newRecord.setAnnualCarryForwardDays(0);
				newRecord.setAnnualForfeitDays(0);

				double sickTotalEntitleDays = calActualSickLeaveDays(toDateStr, onBoardDate, dateFormat);
				sickTotalEntitleDays = RoundDownDays (sickTotalEntitleDays);
				newRecord.setSickTotalEntitleDays(sickTotalEntitleDays);
				newRecord.setSickAppliedDays(0);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newRecord;
	}
	
	public static LeaveBalanceVO updateLeaveBalanceRecord(LeaveBalanceVO currentRecord, double totalEntitleDays) {

		LeaveBalanceVO newRecord = new LeaveBalanceVO();

		try {
				newRecord.setStaffCode(currentRecord.getStaffCode());
			    newRecord.setYear(currentRecord.getYear());
				
				newRecord.setAnnualTotalEntitleDays(totalEntitleDays);			
				newRecord.setAnnualStatutoryEntitleDays(currentRecord.getAnnualStatutoryEntitleDays());
				newRecord.setAnnualCompanyEntitleDays(totalEntitleDays- newRecord.getAnnualStatutoryEntitleDays());

				newRecord.setAnnualAppliedDays(currentRecord.getAnnualAppliedDays());
				newRecord.setAnnualCarryForwardDays(currentRecord.getAnnualCarryForwardDays());
				newRecord.setAnnualForfeitDays(currentRecord.getAnnualForfeitDays());

				newRecord.setSickTotalEntitleDays(currentRecord.getSickTotalEntitleDays());
				newRecord.setSickAppliedDays(currentRecord.getSickAppliedDays());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newRecord;
	}
			
	public static void main(String[] args){
		
		String dateFormat = "MM/dd/yyyy";	
			
		System.out.println("Total Annual Entitle: ");
		
		System.out.println(LeaveEntitleHelper.getBasicTotalAnnualEntitle(122, 1));
		
		System.out.println(LeaveEntitleHelper.getBasicTotalAnnualEntitle(LEAVE_BALANCE_1YEAR, 4));
		
		System.out.println(LeaveEntitleHelper.getBasicTotalAnnualEntitle(LEAVE_BALANCE_5YEARS, 6.0));
		
		System.out.println(LeaveEntitleHelper.getBasicTotalAnnualEntitle(LEAVE_BALANCE_10YEARS, 9.0));
		
		System.out.println(LeaveEntitleHelper.getBasicTotalAnnualEntitle(LEAVE_BALANCE_15YEARS, 23.0));
		
		//---
		System.out.println("Statutory Annual Entitle: ");
		
		System.out.println("1 年 - <10年: "+LeaveEntitleHelper.getStatutoryAnnualEntitle(3649));
		
		System.out.println("10 年 - <20年: "+LeaveEntitleHelper.getStatutoryAnnualEntitle(6000));
		System.out.println("10 年 - <20年: "+LeaveEntitleHelper.getStatutoryAnnualEntitle(7299));
		
		System.out.println("20年: "+LeaveEntitleHelper.getStatutoryAnnualEntitle(7300));
		System.out.println("20年: "+LeaveEntitleHelper.getStatutoryAnnualEntitle(17300));
		
		//---
		System.out.println("cal func: ");
		

		
//		try {
//			int preWorkDays = (int)(0.2*365);
//			double grade = 11.0;
//			String beginDateStr = "11/11/2011";
//			String endDateStr = "12/31/2011";
//			String onBoardDateStr = "11/11/2011";			
//		
//			System.out.println("1 Total:"+ RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("1 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//
//			preWorkDays = 7*365;
//			grade = 8.0;
//			System.out.println("2 Total:"+RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("2 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//			
//			preWorkDays = (int) (365 * 10.3);
//			grade = 7.0;
//			System.out.println("3 Total:"+RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("3 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//			
//			preWorkDays = (int) (365 * 10.3);
//			grade = 8.0;
//			System.out.println("4 Total:"+RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("4 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//
//			preWorkDays = (int) (365 * 20.2);
//			grade = 8.0;			
//			System.out.println("5 Total:"+RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("5 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//
//			preWorkDays = (int) (365 * 20.2);
//			grade = 11.0;
//			System.out.println("6 Total:"+RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) ));
//			//System.out.println("6 法定:"+RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) ));
//
//			
//			double totalTotal =0;
//			double totalStautory=0;
//			onBoardDateStr = "06/25/2008";
//			beginDateStr = "12/31/2010";
//			endDateStr = "02/28/2011";
//			grade = 7.0;
//			preWorkDays = (int) (365 * 1.67);
//			totalTotal = RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			beginDateStr = "03/01/2011";
//			endDateStr = "12/31/2011";
//			grade = 8.0;
//			totalTotal += RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			//totalStautory = RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) );
//			System.out.println("Cherry Chen 1: ");
//			System.out.println("Total:"+totalTotal);
//			System.out.println("法定 Total:"+totalStautory);
//			
//			beginDateStr = "12/31/2010";
//			endDateStr = "02/28/2011";
//			grade = 7.0;
//			preWorkDays = (int) (365 * 8.7);
//			totalTotal = RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			beginDateStr = "03/01/2011";
//			endDateStr = "12/31/2011";
//			grade = 8.0;
//			totalTotal += RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			//totalStautory = RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) );
//			System.out.println("Cherry Chen 2: ");
//			System.out.println("Total:"+totalTotal);
//			System.out.println("法定 Total:"+totalStautory);
//			
//			beginDateStr = "12/31/2010";
//			endDateStr = "02/28/2011";
//			grade = 10.0;
//			preWorkDays = (int) (365 * 20.2);
//			totalTotal = RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			beginDateStr = "03/01/2011";
//			endDateStr = "12/31/2011";
//			grade = 11.0;
//			totalTotal += RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			//totalStautory = RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) );
//			System.out.println("Cherry Chen 3: ");
//			System.out.println("Total:"+totalTotal);
//			System.out.println("法定 Total:"+totalStautory);
//			
//			beginDateStr = "12/31/2010";
//			endDateStr = "02/28/2011";
//			grade = 7.0;
//			preWorkDays = (int) (365 * 20.2);
//			totalTotal = RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			beginDateStr = "03/01/2011";
//			endDateStr = "12/31/2011";
//			grade = 8.0;
//			totalTotal += RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			//totalStautory = RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) );
//			System.out.println("Cherry Chen 4: ");
//			System.out.println("Total:"+totalTotal);
//			System.out.println("法定 Total:"+totalStautory);
//			
//			onBoardDateStr = "04/07/2006";
//			beginDateStr = "12/31/2010";
//			endDateStr = "08/31/2011";
//			grade = 7.0;
//			preWorkDays = (int) (365 * 5);
//			totalTotal = RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			beginDateStr = "09/01/2011";
//			endDateStr = "12/31/2011";
//			grade = 8.0;
//			totalTotal += RoundDownDays(LeaveEntitleHelper.calActualAnnualLeaveDays(beginDateStr, endDateStr, onBoardDateStr,dateFormat, grade) );
//			//totalStautory = RoundDownDays(LeaveEntitleHelper.calStatutoryLeaveDays(onBoardDateStr, endDateStr, dateFormat,  preWorkDays) );
//			System.out.println("Riva Li : ");
//			System.out.println("Total:"+totalTotal);
//			System.out.println("法定 Total:"+totalStautory);
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
