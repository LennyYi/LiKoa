package com.aiait.eflow.report.vo;

import java.sql.Date;

import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;

public class LeaveDetailVO {

    protected String typeCode;
    protected String typeName;
    protected double applyWorkingDays;
    protected double applyCalendarDays;
    protected Date fromDate;
    protected String fromTimeCode;
    protected String fromTimeName;
    protected Date toDate;
    protected String toTimeCode;
    protected String toTimeName;
    protected String remark;
    protected String staffType;

    protected WorkFlowProcessVO formBasicData = new WorkFlowProcessVO();

    /**
     * @return the typeCode
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode
     *            the typeCode to set
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the applyWorkingDays
     */
    public double getApplyWorkingDays() {
        return applyWorkingDays;
    }

    /**
     * @param applyWorkingDays
     *            the applyWorkingDays to set
     */
    public void setApplyWorkingDays(double applyWorkingDays) {
        this.applyWorkingDays = applyWorkingDays;
    }
    
    /**
     * @return the applyCalendarDays
     */
    public double getApplyCalendarDays() {
        return applyCalendarDays;
    }

    /**
     * @param applyCalendarDays
     *            the applyCalendarDays to set
     */
    public void setApplyCalendarDays(double applyCalendarDays) {
        this.applyCalendarDays = applyCalendarDays;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the fromTimeCode
     */
    public String getFromTimeCode() {
        return fromTimeCode;
    }

    /**
     * @param fromTimeCode
     *            the fromTimeCode to set
     */
    public void setFromTimeCode(String fromTimeCode) {
        this.fromTimeCode = fromTimeCode;
    }

    /**
     * @return the fromTimeName
     */
    public String getFromTimeName() {
        return fromTimeName;
    }

    /**
     * @param fromTimeName
     *            the fromTimeName to set
     */
    public void setFromTimeName(String fromTimeName) {
        this.fromTimeName = fromTimeName;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate
     *            the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the toTimeCode
     */
    public String getToTimeCode() {
        return toTimeCode;
    }

    /**
     * @param toTimeCode
     *            the toTimeCode to set
     */
    public void setToTimeCode(String toTimeCode) {
        this.toTimeCode = toTimeCode;
    }

    /**
     * @return the toTimeName
     */
    public String getToTimeName() {
        return toTimeName;
    }

    /**
     * @param toTimeName
     *            the toTimeName to set
     */
    public void setToTimeName(String toTimeName) {
        this.toTimeName = toTimeName;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the formBasicData
     */
    public WorkFlowProcessVO getFormBasicData() {
        return formBasicData;
    }

    /**
     * @param formBasicData
     *            the formBasicData to set
     */
    public void setFormBasicData(WorkFlowProcessVO formBasicData) {
        this.formBasicData = formBasicData;
    }

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

}
