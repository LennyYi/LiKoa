package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class OTRecordVO extends BaseVO {
    private String staffCode;
    private int otRecordNum;
    
    private String otPlanDateBegin;
    private String otPlanDateEnd;
    private String isExceptionalCase;
    
	public String getIsExceptionalCase() {
		return isExceptionalCase;
	}
	public void setIsExceptionalCase(String isExceptionalCase) {
		this.isExceptionalCase = isExceptionalCase;
	}
	public String getOtPlanDateBegin() {
		return otPlanDateBegin;
	}
	public void setOtPlanDateBegin(String otPlanDateBegin) {
		this.otPlanDateBegin = otPlanDateBegin;
	}
	public String getOtPlanDateEnd() {
		return otPlanDateEnd;
	}
	public void setOtPlanDateEnd(String otPlanDateEnd) {
		this.otPlanDateEnd = otPlanDateEnd;
	}
	public int getOtRecordNum() {
		return otRecordNum;
	}
	public void setOtRecordNum(int otRecordNum) {
		this.otRecordNum = otRecordNum;
	}
	public String getStaffCode() {
		return staffCode;
	}
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

}
