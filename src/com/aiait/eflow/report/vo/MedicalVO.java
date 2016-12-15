package com.aiait.eflow.report.vo;

import java.math.BigDecimal;

public class MedicalVO {

    protected String teamCode;
    protected String staffCode;
    protected double grade=0.0;
    
    protected double staffCMonthBegin=0.0;
    protected double staffCMonthSpent=0.0;
    protected double staffCMonthEnd=0.0;
    protected double staffHMonthBegin=0.0;
    protected double staffHMonthSpent=0.0;
    protected double staffHMonthEnd=0.0;
    
    protected String connubialName="";
    protected double connubialCMonthBegin=0.0;
    protected double connubialCMonthSpent=0.0;
    protected double connubialCMonthEnd=0.0;
    protected double connubialHMonthBegin=0.0;
    protected double connubialHMonthSpent=0.0; 
    protected double connubialHMonthEnd=0.0;
    
    protected String childName="";
    protected double childCMonthBegin=0.0;
    protected double childCMonthSpent=0.0; 
    protected double childCMonthEnd=0.0;
    protected double childHMonthBegin=0.0; 
    protected double childHMonthSpent=0.0;
    protected double childHMonthEnd=0.0;
    
    protected double totalMonthBegin=0.0;
    protected double totalMonthSpent =0.0;
    protected double totalMonthEnd =0.0;
    protected double totalMonthBeforeTax=0.0;
    protected double totalMonthAfterTax=0.0;
    
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public String getStaffCode() {
		return staffCode;
	}
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public double getStaffCMonthBegin() {
		return staffCMonthBegin;
	}
	public void setStaffCMonthBegin(double staffCMonthBegin) {
		this.staffCMonthBegin = staffCMonthBegin;
	}
	public double getStaffCMonthSpent() {
		return staffCMonthSpent;
	}
	public void setStaffCMonthSpent(double staffCMonthSpent) {
		this.staffCMonthSpent = staffCMonthSpent;
	}
	public double getStaffCMonthEnd() {
		return staffCMonthEnd;
	}
	public double getStaffHMonthBegin() {
		return staffHMonthBegin;
	}
	public void setStaffHMonthBegin(double staffHMonthBegin) {
		this.staffHMonthBegin = staffHMonthBegin;
	}
	public double getStaffHMonthSpent() {
		return staffHMonthSpent;
	}
	public void setStaffHMonthSpent(double staffHMonthSpent) {
		this.staffHMonthSpent = staffHMonthSpent;
	}
	public double getStaffHMonthEnd() {
		return staffHMonthEnd;
	}
	public String getConnubialName() {
		return connubialName;
	}
	public void setConnubialName(String connubialName) {
		this.connubialName = connubialName;
	}
	public double getConnubialCMonthBegin() {
		return connubialCMonthBegin;
	}
	public void setConnubialCMonthBegin(double connubialCMonthBegin) {
		this.connubialCMonthBegin = connubialCMonthBegin;
	}
	public double getConnubialCMonthSpent() {
		return connubialCMonthSpent;
	}
	public void setConnubialCMonthSpent(double connubialCMonthSpent) {
		this.connubialCMonthSpent = connubialCMonthSpent;
	}
	public double getConnubialCMonthEnd() {
		return connubialCMonthEnd;
	}
	public double getConnubialHMonthBegin() {
		return connubialHMonthBegin;
	}
	public void setConnubialHMonthBegin(double connubialHMonthBegin) {
		this.connubialHMonthBegin = connubialHMonthBegin;
	}
	public double getConnubialHMonthSpent() {
		return connubialHMonthSpent;
	}
	public void setConnubialHMonthSpent(double connubialHMonthSpent) {
		this.connubialHMonthSpent = connubialHMonthSpent;
	}
	public double getConnubialHMonthEnd() {
		return connubialHMonthEnd;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public double getChildCMonthBegin() {
		return childCMonthBegin;
	}
	public void setChildCMonthBegin(double childCMonthBegin) {
		this.childCMonthBegin = childCMonthBegin;
	}
	public double getChildCMonthSpent() {
		return childCMonthSpent;
	}
	public void setChildCMonthSpent(double childCMonthSpent) {
		this.childCMonthSpent = childCMonthSpent;
	}
	public double getChildCMonthEnd() {
		return childCMonthEnd;
	}
	public double getChildHMonthBegin() {
		return childHMonthBegin;
	}
	public void setChildHMonthBegin(double childHMonthBegin) {
		this.childHMonthBegin = childHMonthBegin;
	}
	public double getChildHMonthSpent() {
		return childHMonthSpent;
	}
	public void setChildHMonthSpent(double childHMonthSpent) {
		this.childHMonthSpent = childHMonthSpent;
	}
	public double getChildHMonthEnd() {
		return childHMonthEnd;
	}
	
	public void calTotal(){
		BigDecimal result = BigDecimal.ZERO;	
		
		result = BigDecimal.valueOf(this.staffCMonthBegin).subtract(BigDecimal.valueOf(this.staffCMonthSpent));
		this.staffCMonthEnd=result.doubleValue();
		result = BigDecimal.valueOf(this.staffHMonthBegin).subtract(BigDecimal.valueOf(this.staffHMonthSpent));
		this.staffHMonthEnd=result.doubleValue();
		result = BigDecimal.valueOf(this.connubialCMonthBegin).subtract(BigDecimal.valueOf(this.connubialCMonthSpent));
		this.connubialCMonthEnd=result.doubleValue();
		result = BigDecimal.valueOf(this.connubialHMonthBegin).subtract(BigDecimal.valueOf(this.connubialHMonthSpent));
		this.connubialHMonthEnd=result.doubleValue();
		result = BigDecimal.valueOf(this.childCMonthBegin).subtract(BigDecimal.valueOf(this.childCMonthSpent));
		this.childCMonthEnd=result.doubleValue();
		result = BigDecimal.valueOf(this.childHMonthBegin).subtract(BigDecimal.valueOf(this.childHMonthSpent));
		this.childHMonthEnd=result.doubleValue();
		
		result = BigDecimal.valueOf(this.staffCMonthBegin).add(BigDecimal.valueOf(this.staffHMonthBegin ))
			.add(BigDecimal.valueOf(this.connubialCMonthBegin)).add(BigDecimal.valueOf(this.connubialHMonthBegin))
			.add(BigDecimal.valueOf(this.childCMonthBegin)).add(BigDecimal.valueOf(this.childHMonthBegin));	
		this.totalMonthBegin = result.doubleValue();
		
		result = BigDecimal.valueOf(this.staffCMonthSpent).add(BigDecimal.valueOf(this.staffHMonthSpent ))
		.add(BigDecimal.valueOf(this.connubialCMonthSpent)).add(BigDecimal.valueOf(this.connubialHMonthSpent))
		.add(BigDecimal.valueOf(this.childCMonthSpent)).add(BigDecimal.valueOf(this.childHMonthSpent));
		this.totalMonthSpent = result.doubleValue();
		
		result = BigDecimal.valueOf(this.staffCMonthEnd).add(BigDecimal.valueOf(this.staffHMonthEnd ))
		.add(BigDecimal.valueOf(this.connubialCMonthEnd)).add(BigDecimal.valueOf(this.connubialHMonthEnd))
		.add(BigDecimal.valueOf(this.childCMonthEnd)).add(BigDecimal.valueOf(this.childHMonthEnd));
		this.totalMonthEnd  = result.doubleValue();
		
		result = BigDecimal.valueOf(this.totalMonthSpent).subtract(BigDecimal.valueOf(this.totalMonthBeforeTax ));
		this.totalMonthAfterTax = result.doubleValue();
		
//		this.totalMonthBegin = this.staffCMonthBegin + this.staffHMonthBegin 
//							 + this.connubialCMonthBegin + this.connubialHMonthBegin
//		                     + this.childCMonthBegin + this.childHMonthBegin;
		
//		this.totalMonthSpent = this.staffCMonthSpent + this.staffHMonthSpent
//							 + this.connubialCMonthSpent + this.connubialHMonthSpent
//							 + this.childCMonthSpent + this.childHMonthSpent;
		
//		this.totalMonthEnd = this.getStaffCMonthEnd() + this.getStaffHMonthEnd()
//						   + this.getConnubialCMonthEnd() + this.getConnubialHMonthEnd()
//						   + this.getChildCMonthEnd() + this.getChildHMonthEnd();
	}
	
	public double getTotalMonthBegin() {
		return this.totalMonthBegin;
	}
	public double getTotalMonthSpent() {
		return this.totalMonthSpent;
	}

	public double getTotalMonthEnd() {
		return this.totalMonthEnd;
	}
	public double getTotalMonthBeforeTax() {
		return totalMonthBeforeTax;
	}
	public void setTotalMonthBeforeTax(double totalMonthBeforeTax) {
		this.totalMonthBeforeTax = totalMonthBeforeTax;
	}
	public double getTotalMonthAfterTax() {
		return this.totalMonthAfterTax;
	}
}
