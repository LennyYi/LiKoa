package com.aiait.eflow.housekeeping.vo;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.aiait.framework.vo.BaseVO;

public class ExchangeRateVO2 extends BaseVO {

    public static final NumberFormat numberFormat = NumberFormat.getInstance();
    static {
        numberFormat.setMaximumFractionDigits(6);
    }

    protected int effyear;
    protected int effmonth;
    protected String currCode;
    protected String currName;
    protected BigDecimal rate;

    public String getId() {
        return this.getEffyear() + "_" + this.getEffmonth() + "_" + this.getCurrCode();
    }

    public void setId(String id) throws Exception {
        String[] _id = id.split("_");
        this.setEffyear(Integer.parseInt(_id[0]));
        this.setEffmonth(Integer.parseInt(_id[1]));
        if (_id.length > 3) {
            this.setCurrCode(_id[2]);
        }
    }

    /**
     * @return the effyear
     */
    public int getEffyear() {
        return effyear;
    }

    /**
     * @param effyear
     *            the effyear to set
     */
    public void setEffyear(int effyear) {
        this.effyear = effyear;
    }

    /**
     * @return the effmonth
     */
    public int getEffmonth() {
        return effmonth;
    }

    /**
     * @param effmonth
     *            the effmonth to set
     */
    public void setEffmonth(int effmonth) {
        this.effmonth = effmonth;
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

}
