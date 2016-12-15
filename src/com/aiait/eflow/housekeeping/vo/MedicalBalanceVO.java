package com.aiait.eflow.housekeeping.vo;

public class MedicalBalanceVO {
	
	//C: stands for Clinic
	//H: stands for Hospitalization
	
    protected String staffCode;
    protected int year;
    protected double staffCEntitlement=0.0;
    protected double staffCApplied=0.0;
    protected double staffCBalance=0.0;
    protected double staffHEntitlement=0.0;
    protected double staffHApplied=0.0;
    protected double staffHBalance=0.0; 
    
    protected String connubialName="";
    protected double connubialCEntitlement=0.0;
    protected double connubialCApplied=0.0;
    protected double connubialCBalance=0.0;
    protected double connubialHEntitlement=0.0;
    protected double connubialHApplied=0.0; 
    protected double connubialHBalance=0.0;
    
    protected String childName="";
    protected double childCEntitlement=0.0;
    protected double childCApplied=0.0; 
    protected double childCBalance=0.0;
    protected double childHEntitlement=0.0; 
    protected double childHApplied=0.0;
    protected double childHBalance=0.0;

    public void clone(MedicalBalanceVO vo){
    	this.setStaffCode(vo.getStaffCode());
    	this.setYear(vo.getYear());
    	this.setConnubialName(vo.getConnubialName());
    	this.setChildName(vo.getChildName());
    	this.setAllApplied(vo);
    	this.setAllEntitlement(vo);
    }
    
    /**
     * @return the staffCode
     */
    public String getStaffCode() {
        return staffCode;
    }

    /**
     * @param staffCode
     *            the staffCode to set
     */
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

	public double getStaffCEntitlement() {
		return staffCEntitlement;
	}

	public void setStaffCEntitlement(double staffCEntitlement) {
		this.staffCEntitlement = staffCEntitlement;
	}

	public double getStaffCApplied() {
		return staffCApplied;
	}

	public void setStaffCApplied(double staffCApplied) {
		this.staffCApplied = staffCApplied;
	}

	public double getStaffCBalance() {
		return this.staffCEntitlement - this.staffCApplied;
	}

	public void setStaffCBalance(double staffCBalance) {
		this.staffCBalance = staffCBalance;
	}

	public double getStaffHEntitlement() {
		return staffHEntitlement;
	}

	public void setStaffHEntitlement(double staffHEntitlement) {
		this.staffHEntitlement = staffHEntitlement;
	}

	public double getStaffHApplied() {
		return staffHApplied;
	}

	public void setStaffHApplied(double staffHApplied) {
		this.staffHApplied = staffHApplied;
	}

	public double getStaffHBalance() {
		return this.staffHEntitlement - this.staffHApplied;
	}

	public void setStaffHBalance(double staffHBalance) {
		this.staffHBalance = staffHBalance;
	}

	public String getConnubialName() {
		return connubialName;
	}

	public void setConnubialName(String connubialName) {
		this.connubialName = connubialName;
	}

	public double getConnubialCEntitlement() {
		return connubialCEntitlement;
	}

	public void setConnubialCEntitlement(double connubialCEntitlement) {
		this.connubialCEntitlement = connubialCEntitlement;
	}

	public double getConnubialCApplied() {
		return connubialCApplied;
	}

	public void setConnubialCApplied(double connubialCApplied) {
		this.connubialCApplied = connubialCApplied;
	}

	public double getConnubialCBalance() {
		return this.connubialCEntitlement - this.connubialCApplied;
	}

	public void setConnubialCBalance(double connubialCBalance) {
		this.connubialCBalance = connubialCBalance;
	}

	public double getConnubialHEntitlement() {
		return connubialHEntitlement;
	}

	public void setConnubialHEntitlement(double connubialHEntitlement) {
		this.connubialHEntitlement = connubialHEntitlement;
	}

	public double getConnubialHApplied() {
		return connubialHApplied;
	}

	public void setConnubialHApplied(double connubialHApplied) {
		this.connubialHApplied = connubialHApplied;
	}

	public double getConnubialHBalance() {
		return this.connubialHEntitlement - this.connubialHApplied;
	}

	public void setConnubialHBalance(double connubialHBalance) {
		this.connubialHBalance = connubialHBalance;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public double getChildCEntitlement() {
		return childCEntitlement;
	}

	public void setChildCEntitlement(double childCEntitlement) {
		this.childCEntitlement = childCEntitlement;
	}

	public double getChildCApplied() {
		return childCApplied;
	}

	public void setChildCApplied(double childCApplied) {
		this.childCApplied = childCApplied;
	}

	public double getChildCBalance() {
		return this.childCEntitlement - this.childCApplied;
	}

	public void setChildCBalance(double childCBalance) {
		this.childCBalance = childCBalance;
	}

	public double getChildHEntitlement() {
		return childHEntitlement;
	}

	public void setChildHEntitlement(double childHEntitlement) {
		this.childHEntitlement = childHEntitlement;
	}

	public double getChildHApplied() {
		return childHApplied;
	}

	public void setChildHApplied(double childHApplied) {
		this.childHApplied = childHApplied;
	}

	public double getChildHBalance() {
		return this.childHEntitlement - this.childHApplied;
	}

	public void setChildHBalance(double childHBalance) {
		this.childHBalance = childHBalance;
	}
	
	public void setAllEntitlement(double sc, double sh, double conc, double conh, double chlc, double chlh){
		this.setStaffCEntitlement(sc);
		this.setConnubialCEntitlement(conc);
		this.setChildCEntitlement(chlc);
		
		this.setStaffHEntitlement(sh);
		this.setConnubialHEntitlement(conh);
		this.setChildHEntitlement(chlh);	
	}
	
	public void setAllEntitlement(MedicalBalanceVO vo){
		this.setStaffCEntitlement(vo.getStaffCEntitlement());
		this.setConnubialCEntitlement(vo.getConnubialCEntitlement());
		this.setChildCEntitlement(vo.getChildCEntitlement());
		
		this.setStaffHEntitlement(vo.getStaffHEntitlement());
		this.setConnubialHEntitlement(vo.getConnubialHEntitlement());
		this.setChildHEntitlement(vo.getChildHEntitlement());	
	}
	
	public void setAllApplied(double sca, double sha, double conca, double conha, double chlca, double chlha){
		this.setStaffCApplied(sca);
		this.setStaffHApplied(sha);
		this.setConnubialCApplied(conca);
		this.setConnubialHApplied(conha);
		this.setChildCApplied(chlca);
		this.setChildHApplied(chlha);
	}
	
	public void setAllApplied(MedicalBalanceVO vo){
		this.setStaffCApplied(vo.getStaffCApplied());
		this.setStaffHApplied(vo.getStaffHApplied());
		this.setConnubialCApplied(vo.getConnubialCApplied());
		this.setConnubialHApplied(vo.getConnubialHApplied());
		this.setChildCApplied(vo.getChildCApplied());
		this.setChildHApplied(vo.getChildHApplied());
	}

}
