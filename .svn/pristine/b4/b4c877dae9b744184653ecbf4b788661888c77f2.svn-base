package com.aiait.eflow.housekeeping.vo;

import java.util.HashMap;

import com.aiait.framework.vo.BaseVO;

public class PerdiemRateVO extends BaseVO {

    protected String region;
    protected String titleGroup;
    protected int rate;

    protected static HashMap titleGroupMap = new HashMap();
    static {
        titleGroupMap.put("1", "Manager and below");
        titleGroupMap.put("2", "Senior Manager to AVP");
        titleGroupMap.put("3", "VP");
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
        this.region = region;
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
