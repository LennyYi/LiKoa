package com.aiait.eflow.housekeeping.vo;

public class LeaveBalanceVO {

    protected String staffCode;
    protected int year;
    protected double annualTotalEntitleDays;
    protected double annualCarryForwardDays;
    protected double annualAppliedDays;
    protected double annualBalanceDays;
    
    protected double annualStatutoryEntitleDays;
    protected double annualCompanyEntitleDays;
    protected double annualForfeitDays;
    
    protected double sickTotalEntitleDays;
    protected double sickAppliedDays;
    protected double sickBalanceDays;
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

    /**
     * @return the annualTotalEntitleDays
     */
    public double getAnnualTotalEntitleDays() {
        return annualTotalEntitleDays;
    }

    /**
     * @param annualTotalEntitleDays
     *            the annualTotalEntitleDays to set
     */
    public void setAnnualTotalEntitleDays(double annualTotalEntitleDays) {
        this.annualTotalEntitleDays = annualTotalEntitleDays;
    }

    /**
     * @return the annualCarryForwardDays
     */
    public double getAnnualCarryForwardDays() {
        return annualCarryForwardDays;
    }

    /**
     * @param annualCarryForwardDays
     *            the annualCarryForwardDays to set
     */
    public void setAnnualCarryForwardDays(double annualCarryForwardDays) {
        this.annualCarryForwardDays = annualCarryForwardDays;
    }

    /**
     * @return the annualAppliedDays
     */
    public double getAnnualAppliedDays() {
        return annualAppliedDays;
    }

    /**
     * @param annualAppliedDays
     *            the annualAppliedDays to set
     */
    public void setAnnualAppliedDays(double annualAppliedDays) {
        this.annualAppliedDays = annualAppliedDays;
    }

    /**
     * @return the annualBalanceDays
     */
    public double getAnnualBalanceDays() {
        return this.annualTotalEntitleDays + this.annualCarryForwardDays - this.annualAppliedDays- this.annualForfeitDays;
    }
    
    /**
     * @return the annualStatutoryEntitleDays
     */
    public double getAnnualStatutoryEntitleDays() {
        return annualStatutoryEntitleDays;
    }

    /**
     * @param annualStatutoryEntitleDays
     *            the annualStatutoryEntitleDays to set
     */
    public void setAnnualStatutoryEntitleDays(double annualStatutoryEntitleDays) {
        this.annualStatutoryEntitleDays = annualStatutoryEntitleDays;
    }
    
    /**
     * @return the annualCompanyEntitleDays
     */
    public double getAnnualCompanyEntitleDays() {
        return annualCompanyEntitleDays;
    }

    /**
     * @param annualCompanyEntitleDays
     *            the annualCompanyEntitleDays to set
     */
    public void setAnnualCompanyEntitleDays(double annualCompanyEntitleDays) {
        this.annualCompanyEntitleDays = annualCompanyEntitleDays;
    }
    
    /**
     * @return the annualForfeitDays
     */
    public double getAnnualForfeitDays() {
        return annualForfeitDays;
    }

    /**
     * @param annualForfeitDays
     *            the annualForfeitDays to set
     */
    public void setAnnualForfeitDays(double annualForfeitDays) {
        this.annualForfeitDays = annualForfeitDays;
    }
    
    /**
     * @return the SickTotalEntitleDays
     */
    public double getSickTotalEntitleDays() {
        return sickTotalEntitleDays;
    }

    /**
     * @param SickTotalEntitleDays
     *            the SickTotalEntitleDays to set
     */
    public void setSickTotalEntitleDays(double sickTotalEntitleDays) {
        this.sickTotalEntitleDays = sickTotalEntitleDays;
    }
    
    /**
     * @return the sickAppliedDays
     */
    public double getSickAppliedDays() {
        return sickAppliedDays;
    }

    /**
     * @param sickAppliedDays
     *            the sickAppliedDays to set
     */
    public void setSickAppliedDays(double sickAppliedDays) {
        this.sickAppliedDays = sickAppliedDays;
    }
    
    /**
     * @return the sickBalanceDays
     */
    public double getSickBalanceDays() {
        return this.sickTotalEntitleDays - this.sickAppliedDays;
    }

}
