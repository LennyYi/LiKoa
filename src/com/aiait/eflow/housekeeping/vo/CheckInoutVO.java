package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class CheckInoutVO extends BaseVO {
    private String otDate;
    private String staffCode;
    private String arrivalTime;
    private String leaveTime;
    
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	public String getOtDate() {
		return otDate;
	}
	public void setOtDate(String otDate) {
		this.otDate = otDate;
	}
	public String getStaffCode() {
		return staffCode;
	}
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
    
}
