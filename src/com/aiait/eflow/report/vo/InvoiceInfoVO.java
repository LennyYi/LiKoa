package com.aiait.eflow.report.vo;

import com.aiait.framework.vo.BaseVO;

public class InvoiceInfoVO extends BaseVO {
	
    private String requestNo;

    private String requestStaffCode;
    
    private String teamCode;
    
    private String remark;
    
    private String checkNo;
    
    private String payee;
    
	private String formSystemId;

	private String beginDateStr;

	private String endDateStr;
	
	private String sbmDateStr;
	
	private String payDateStr;
	
    private String formType;
    
    private String formName;
    
    private double amount;

	public String getBeginDateStr() {
		return beginDateStr;
	}

	public void setBeginDateStr(String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getFormSystemId() {
		return formSystemId;
	}

	public void setFormSystemId(String formSystemId) {
		this.formSystemId = formSystemId;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public String getRequestStaffCode() {
		return requestStaffCode;
	}

	public void setRequestStaffCode(String requestStaffCode) {
		this.requestStaffCode = requestStaffCode;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getSbmDateStr() {
		return sbmDateStr;
	}

	public void setSbmDateStr(String sbmDateStr) {
		this.sbmDateStr = sbmDateStr;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getPayDateStr() {
		return payDateStr;
	}

	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
