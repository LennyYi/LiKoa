package com.aiait.eflow.housekeeping.vo;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.*;

import com.aiait.framework.vo.BaseVO;

public class PerDiemRateVO2 extends BaseVO {

    public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    public static final DateFormat _dateFormat = new SimpleDateFormat("yyyyMMdd");
    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    protected Date effDate;
    protected String cityCode;
    protected String cityName;
    protected int grade;
    protected BigDecimal rate;
    protected String currCode;
    protected String currName;

    public String getId() {
        return _dateFormat.format(this.getEffDate()) + "_" + this.getCityCode() + "_" + this.getGrade();
    }

    public void setId(String id) throws Exception {
        String[] _id = id.split("_");
        this.setEffDate(new java.sql.Date(_dateFormat.parse(_id[0]).getTime()));
        this.setCityCode(_id[1]);
        this.setGrade(Integer.parseInt(_id[2]));
    }

    /**
     * @return the effDate
     */
    public Date getEffDate() {
        return effDate;
    }

    public String getEffDate2() {
        return dateFormat.format(this.getEffDate());
    }

    /**
     * @param effDate
     *            the effDate to set
     */
    public void setEffDate(Date effDate) {
        this.effDate = effDate;
    }

    /**
     * @return the cityCode
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * @param cityCode
     *            the cityCode to set
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * @return the cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName
     *            the cityName to set
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * @param grade
     *            the grade to set
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    public String getRate2() {
        return numberFormat.format(this.getRate());
    }

    /**
     * @param rate
     *            the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the currCode
     */
    public String getCurrCode() {
        return currCode;
    }

    /**
     * @param currCode
     *            the currCode to set
     */
    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    /**
     * @return the currName
     */
    public String getCurrName() {
        return currName;
    }

    /**
     * @param currName
     *            the currName to set
     */
    public void setCurrName(String currName) {
        this.currName = currName;
    }

}
