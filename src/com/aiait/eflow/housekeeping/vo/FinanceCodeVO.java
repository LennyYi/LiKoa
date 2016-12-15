package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class FinanceCodeVO extends BaseVO {

    protected String orgId;
    protected String code;
    protected String name;
    /* below is for sunac code only */
    protected int t0;
    protected int t1;
    protected int t2;
    protected int t3;
    protected int t4;
    protected int t5;
    protected int t6;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getT0() {
        return t0;
    }

    public void setT0(int t0) {
        this.t0 = t0;
    }

    public int getT2() {
        return t2;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getT4() {
        return t4;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getT6() {
        return t6;
    }

    public void setT6(int t6) {
        this.t6 = t6;
    }

    /**
     * @return the t1
     */
    public int getT1() {
        return t1;
    }

    /**
     * @param t1
     *            the t1 to set
     */
    public void setT1(int t1) {
        this.t1 = t1;
    }

    /**
     * @return the t5
     */
    public int getT5() {
        return t5;
    }

    /**
     * @param t5
     *            the t5 to set
     */
    public void setT5(int t5) {
        this.t5 = t5;
    }

}
