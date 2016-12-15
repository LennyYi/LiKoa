package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class HolidayVO extends BaseVO{
	private int holidayId;
	
	private String holidayYear;

	private String holidayFromDate;
	
	private String holidayToDate;

	private String holidayDescription;
	
	private int holidayStatus;
	
	private String holidayBatchFromYear;
	
	private String holidayBatchToYear;
	
	private String holidayType;//1为公众假期，2为法定假期

	public String getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}

	public int getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}
	
	public String getHolidayYear() {
		return holidayYear;
	}

	public void setHolidayYear(String holidayYear) {
		this.holidayYear = holidayYear;
	}
	
	public String getHolidayFromDate() {
		return holidayFromDate;
	}

	public void setHolidayFromDate(String holidayDate) {
		this.holidayFromDate = holidayDate;
	}
	
	public String getHolidayToDate() {
		return holidayToDate;
	}

	public void setHolidayToDate(String holidayDate) {
		this.holidayToDate = holidayDate;
	}

	public String getHolidayDescription() {
		return holidayDescription;
	}

	public void setHolidayDescription(String holidayDescription) {
		this.holidayDescription = holidayDescription;
	}

	public int getHolidayStatus() {
		return holidayStatus;
	}

	public void setHolidayStatus(int holidayStatus) {
		this.holidayStatus = holidayStatus;
	}

	public String getHolidayBatchFromYear() {
		return holidayBatchFromYear;
	}

	public void setHolidayBatchFromYear(String holidayBatchFromYear) {
		this.holidayBatchFromYear = holidayBatchFromYear;
	}

	public String getHolidayBatchToYear() {
		return holidayBatchToYear;
	}

	public void setHolidayBatchToYear(String holidayBatchToYear) {
		this.holidayBatchToYear = holidayBatchToYear;
	}

}
