package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class StaffHolidayVO extends BaseVO {
	private String staffCode;
    private String leaveType;          //�������
    private double lastYearDays;       //�����������
    private double lastAbandonDays;    //�����������
    private double thisYearDays;       //��������е�����
    private double adjustDays;         //����������
    private double usedDays;           //������Ѿ�ʹ�õ�����
    
	public double getAdjustDays() {
		return adjustDays;
	}
	public void setAdjustDays(double adjustDays) {
		this.adjustDays = adjustDays;
	}
	public double getLastAbandonDays() {
		return lastAbandonDays;
	}
	public void setLastAbandonDays(double lastAbandonDays) {
		this.lastAbandonDays = lastAbandonDays;
	}
	public double getLastYearDays() {
		return lastYearDays;
	}
	public void setLastYearDays(double lastYearDays) {
		this.lastYearDays = lastYearDays;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getStaffCode() {
		return staffCode;
	}
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
	public double getThisYearDays() {
		return thisYearDays;
	}
	public void setThisYearDays(double thisYearDays) {
		this.thisYearDays = thisYearDays;
	}
	public double getUsedDays() {
		return usedDays;
	}
	public void setUsedDays(double usedDays) {
		this.usedDays = usedDays;
	}
    
    

}
