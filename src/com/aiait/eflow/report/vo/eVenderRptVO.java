package com.aiait.eflow.report.vo;

import com.aiait.framework.vo.BaseVO;

public class eVenderRptVO extends BaseVO {

	private String requestNo;
	private String orgId;
	private int formType;
	private String winnerVender;
	private double budget;
	private double contractSum;
	private double savingBudget;
	private double savingContract;
	
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public int getFormType() {
		return formType;
	}
	public void setFormType(int formType) {
		this.formType = formType;
	}
	public String getWinnerVender() {
		return winnerVender;
	}
	public void setWinnerVender(String winnerVender) {
		this.winnerVender = winnerVender;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	public double getContractSum() {
		return contractSum;
	}
	public void setContractSum(double contractSum) {
		this.contractSum = contractSum;
	}
	public double getSavingBudget() {
		return savingBudget;
	}
	public void setSavingBudget(double savingBudget) {
		this.savingBudget = savingBudget;
	}
	public double getSavingContract() {
		return savingContract;
	}
	public void setSavingContract(double savingContract) {
		this.savingContract = savingContract;
	}
	
	
}
