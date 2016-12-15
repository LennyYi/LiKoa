package com.aiait.eflow.housekeeping.vo;

import java.util.HashMap;

import com.aiait.framework.vo.BaseVO;

public class HotelRateVO extends BaseVO {

    protected String city;
    protected String titleGroup;
    protected int rate;

    protected static HashMap titleGroupMap = new HashMap();
    static {
        titleGroupMap.put("1", "Manager and below");
        titleGroupMap.put("2", "Senior Manager to AVP");
        titleGroupMap.put("3", "VP");
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the titleGroup
     */
    public String getTitleGroup() {
        return titleGroup;
    }

    /**
     * @param titleGroup
     *            the titleGroup to set
     */
    public void setTitleGroup(String titleGroup) {
        this.titleGroup = titleGroup;
    }

    /**
     * @return the rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * @param rate
     *            the rate to set
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    public static HashMap getTitleGroupMap() {
        return titleGroupMap;
    }

    public String getTitleGroupName() {
        return (String) titleGroupMap.get(this.titleGroup);
    }

}
