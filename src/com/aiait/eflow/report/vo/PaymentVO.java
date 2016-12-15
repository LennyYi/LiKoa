package com.aiait.eflow.report.vo;

import com.aiait.framework.vo.BaseVO;

public class PaymentVO extends BaseVO {
	
    private String requestNo;
    
    private String remark;
    
    private String checkNo;
    
    private String payee;
    
	private String formSystemId;
	
	private String payBank;
	
	private String payAccount;

	private String payName;
	
	private String payeeBank;
	
	private String payeeAccount;
	
	private String payeeName;
	
	private String payeeProvince;
	
	private String payeeCity;
	
	private String purpose;

	private String beginDateStr;

	private String endDateStr;
	
	private String payDateStr;
    
    private String formName;
    
    private double amount;

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

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
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

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayeeBank() {
		return payeeBank;
	}

	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}

	public String getPayeeAccount() {
		return payeeAccount;
	}

	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeProvince() {
		return payeeProvince;
	}

	public void setPayeeProvince(String payeeProvince) {
		this.payeeProvince = payeeProvince;
	}

	public String getPayeeCity() {
		return payeeCity;
	}

	public void setPayeeCity(String payeeCity) {
		this.payeeCity = payeeCity;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

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

}
