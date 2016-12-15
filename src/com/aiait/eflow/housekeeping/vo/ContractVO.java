package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class ContractVO extends BaseVO {
	private int contractNo;
	private String city;
	private String receiveDate;
	private String respStaff;
	private String respDept;
	private String contactTel;
	private String contractName;
	private String sign1;
	private String sign2;
	private String sign3;
	private String content;
	private double amount;
	private String signDate;
	private String effPeriod;
	private String issueDate;
	private String signDoc;
	private String orgName;
	private String remark;
	
	public int getContractNo() {
		return contractNo;
	}
	public void setContractNo(int contractNo) {
		this.contractNo = contractNo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getSign1() {
		return sign1;
	}
	public void setSign1(String sign1) {
		this.sign1 = sign1;
	}
	public String getSign2() {
		return sign2;
	}
	public void setSign2(String sign2) {
		this.sign2 = sign2;
	}
	public String getSign3() {
		return sign3;
	}
	public void setSign3(String sign3) {
		this.sign3 = sign3;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getSignDoc() {
		return signDoc;
	}
	public void setSignDoc(String signDoc) {
		this.signDoc = signDoc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRespStaff() {
		return respStaff;
	}
	public void setRespStaff(String respStaff) {
		this.respStaff = respStaff;
	}
	public String getRespDept() {
		return respDept;
	}
	public void setRespDept(String respDept) {
		this.respDept = respDept;
	}
	public String getEffPeriod() {
		return effPeriod;
	}
	public void setEffPeriod(String effPeriod) {
		this.effPeriod = effPeriod;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
