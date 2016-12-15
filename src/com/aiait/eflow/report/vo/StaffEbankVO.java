package com.aiait.eflow.report.vo;

import com.aiait.framework.vo.BaseVO;

public class StaffEbankVO extends BaseVO {

    private String requestNo;

    private String formSystemId;

    private String requestStaffCode;

    private String requestStaffCNName;

    private String orgId;

    private String orgName;

    private String payeeBank;

    private String payeeAccount;

    private double amount;

    private String beginDateStr;

    private String endDateStr;

    private String remark;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRequestStaffCode(String requestStaffCode) {
        this.requestStaffCode = requestStaffCode;
    }

    public String getRequestStaffCode() {
        return requestStaffCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRequestStaffCNName(String requestStaffCNName) {
        this.requestStaffCNName = requestStaffCNName;
    }

    public String getRequestStaffCNName() {
        return requestStaffCNName;
    }

    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId
     *            the orgId to set
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName
     *            the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

}
