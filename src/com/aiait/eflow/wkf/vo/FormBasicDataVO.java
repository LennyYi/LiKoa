package com.aiait.eflow.wkf.vo;

import java.util.Date;

import com.aiait.framework.vo.BaseVO;

public class FormBasicDataVO extends BaseVO {

    protected String requestNo;
    protected String submiterCode;
    protected String submiterName;
    protected String companyCode;
    protected String companyName;
    protected String teamCode;
    protected String teamName;
    protected String requesterCode;
    protected String requesterName;
    protected Date requestDate;
    protected Date completeDate;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getSubmiterCode() {
        return submiterCode;
    }

    public void setSubmiterCode(String submiterCode) {
        this.submiterCode = submiterCode;
    }

    public String getSubmiterName() {
        return submiterName;
    }

    public void setSubmiterName(String submiterName) {
        this.submiterName = submiterName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRequesterCode() {
        return requesterCode;
    }

    public void setRequesterCode(String requesterCode) {
        this.requesterCode = requesterCode;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the completeDate
     */
    public Date getCompleteDate() {
        return completeDate;
    }

    /**
     * @param completeDate
     *            the completeDate to set
     */
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

}
