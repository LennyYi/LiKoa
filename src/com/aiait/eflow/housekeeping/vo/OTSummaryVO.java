package com.aiait.eflow.housekeeping.vo;

import java.math.BigDecimal;

import com.aiait.framework.vo.BaseVO;

public class OTSummaryVO extends BaseVO {

    private String yearMonth;

    private String staffCode;

    private String staffNameEn;

    private String staffNameCn;

    private int teamCode;

    private double weekyNum;

    private double publicNum;

    private double statutoryHours;

    private BigDecimal midNightNum = BigDecimal.ZERO;

    private BigDecimal dayTimeNum = BigDecimal.ZERO;

    private BigDecimal mealAllowanceNum = BigDecimal.ZERO;

    private double totalTaxAmount;

    private double preTaxAmount;

    private double afterTaxAmount;

    private double taxiFeeAmount;

    private String status;

    private String accountNo;

    private String hrConfirm;

    private String accountConfirm;

    private boolean hasExceptionalCase = false;

    // private String exceptionalCase;

    public String getAccountConfirm() {
        return accountConfirm;
    }

    public void setAccountConfirm(String accountConfirm) {
        this.accountConfirm = accountConfirm;
    }

    public String getHrConfirm() {
        return hrConfirm;
    }

    public void setHrConfirm(String hrConfirm) {
        this.hrConfirm = hrConfirm;
    }

    public double getAfterTaxAmount() {
        return afterTaxAmount;
    }

    public void setAfterTaxAmount(double afterTaxAmount) {
        this.afterTaxAmount = afterTaxAmount;
    }

    public double getPreTaxAmount() {
        return preTaxAmount;
    }

    public void setPreTaxAmount(double preTaxAmount) {
        this.preTaxAmount = preTaxAmount;
    }

    public double getPublicNum() {
        return publicNum;
    }

    public void setPublicNum(double publicNum) {
        this.publicNum = publicNum;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffNameCn() {
        return staffNameCn;
    }

    public void setStaffNameCn(String staffNameCn) {
        this.staffNameCn = staffNameCn;
    }

    public String getStaffNameEn() {
        return staffNameEn;
    }

    public void setStaffNameEn(String staffNameEn) {
        this.staffNameEn = staffNameEn;
    }

    public double getStatutoryHours() {
        return statutoryHours;
    }

    public void setStatutoryHours(double statutoryHours) {
        this.statutoryHours = statutoryHours;
    }

    public double getTaxiFeeAmount() {
        return taxiFeeAmount;
    }

    public void setTaxiFeeAmount(double taxiFeeAmount) {
        this.taxiFeeAmount = taxiFeeAmount;
    }

    public int getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(int teamCode) {
        this.teamCode = teamCode;
    }

    public double getWeekyNum() {
        return weekyNum;
    }

    public void setWeekyNum(double weekyNum) {
        this.weekyNum = weekyNum;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * @return the hasExceptionalCase
     */
    public boolean getExceptionalCase() {
        return hasExceptionalCase;
    }

    /**
     * @param hasExceptionalCase
     *            the hasExceptionalCase to set
     */
    public void setExceptionalCase(boolean hasExceptionalCase) {
        this.hasExceptionalCase = hasExceptionalCase;
    }

    /**
     * @return the midNightNum
     */
    public BigDecimal getMidNightNum() {
        return midNightNum;
    }

    /**
     * @param midNightNum
     *            the midNightNum to set
     */
    public void setMidNightNum(BigDecimal midNightNum) {
        this.midNightNum = midNightNum;
    }

    /**
     * @return the dayTimeNum
     */
    public BigDecimal getDayTimeNum() {
        return dayTimeNum;
    }

    /**
     * @param dayTimeNum
     *            the dayTimeNum to set
     */
    public void setDayTimeNum(BigDecimal dayTimeNum) {
        this.dayTimeNum = dayTimeNum;
    }

    /**
     * @return the mealAllowanceNum
     */
    public BigDecimal getMealAllowanceNum() {
        return mealAllowanceNum;
    }

    /**
     * @param mealAllowanceNum
     *            the mealAllowanceNum to set
     */
    public void setMealAllowanceNum(BigDecimal mealAllowanceNum) {
        this.mealAllowanceNum = mealAllowanceNum;
    }

}
