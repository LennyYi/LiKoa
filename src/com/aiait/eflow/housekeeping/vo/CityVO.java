package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class CityVO extends BaseVO {

    protected String code;
    protected String name;
    protected String regionCode;
    protected String regionName;
    protected int tier;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the regionCode
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * @param regionCode
     *            the regionCode to set
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * @return the regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName
     *            the regionName to set
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return the tier
     */
    public int getTier() {
        return tier;
    }

    /**
     * @param tier
     *            the tier to set
     */
    public void setTier(int tier) {
        this.tier = tier;
    }

}
