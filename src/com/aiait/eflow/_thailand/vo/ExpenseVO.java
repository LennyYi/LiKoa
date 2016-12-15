package com.aiait.eflow._thailand.vo;

import java.io.Serializable;

import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.framework.vo.BaseVO;

public class ExpenseVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_GOE = "GOE";
    public static final String TYPE_VEA = "VEA";

    public static final String YES = "Yes";
    public static final String NO = "No";

    protected String acCode;
    protected String acSubCode;
    protected String acDesc;
    protected String type;
    protected String relateHR;
    protected String relateRE;
    protected String relateIT;
    protected String spExpense;
    protected String finance;
    protected String fsi;
    protected String capex;

    public String getId() {
        return this.getAcCode() + "_" + this.getAcSubCode();
    }

    /**
     * @return the acCode
     */
    public String getAcCode() {
        return acCode;
    }

    /**
     * @param acCode
     *            the acCode to set
     */
    public void setAcCode(String acCode) {
        this.acCode = acCode == null ? "" : acCode.trim();
    }

    /**
     * @return the acSubCode
     */
    public String getAcSubCode() {
        return acSubCode;
    }

    /**
     * @param acSubCode
     *            the acSubCode to set
     */
    public void setAcSubCode(String acSubCode) {
        this.acSubCode = acSubCode == null ? "" : acSubCode.trim();
    }

    /**
     * @return the acDesc
     */
    public String getAcDesc() {
        return acDesc;
    }

    public String getAcDescHashCode() {
        int hashCode = this.acDesc.toLowerCase().hashCode();
        return Integer.toString(hashCode >= 0 ? hashCode : -1 * hashCode);
    }

    /**
     * @param acDesc
     *            the acDesc to set
     */
    public void setAcDesc(String acDesc) {
        this.acDesc = acDesc == null ? "" : acDesc.trim();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the relateHR
     */
    public String getRelateHR() {
        return relateHR;
    }

    /**
     * @param relateHR
     *            the relateHR to set
     */
    public void setRelateHR(String relateHR) {
        this.relateHR = relateHR;
    }

    /**
     * @return the relateRE
     */
    public String getRelateRE() {
        return relateRE;
    }

    /**
     * @param relateRE
     *            the relateRE to set
     */
    public void setRelateRE(String relateRE) {
        this.relateRE = relateRE;
    }

    /**
     * @return the relateIT
     */
    public String getRelateIT() {
        return relateIT;
    }

    /**
     * @param relateIT
     *            the relateIT to set
     */
    public void setRelateIT(String relateIT) {
        this.relateIT = relateIT;
    }

    /**
     * @return the spExpense
     */
    public String getSpExpense() {
        return spExpense;
    }

    /**
     * @param spExpense
     *            the spExpense to set
     */
    public void setSpExpense(String spExpense) {
        this.spExpense = spExpense;
    }

    public static String[] getSpExpenses() {
        String list = ParamConfigHelper.getInstance().getParamValue("thailand_specific_expense");
        list = list == null ? "" : list;
        return list.split(",");
    }

    /**
     * @return the finance
     */
    public String getFinance() {
        return finance;
    }

    /**
     * @param finance
     *            the finance to set
     */
    public void setFinance(String finance) {
        this.finance = finance;
    }

    /**
     * @return the fsi
     */
    public String getFsi() {
        return fsi;
    }

    /**
     * @param fsi
     *            the fsi to set
     */
    public void setFsi(String fsi) {
        this.fsi = fsi;
    }

    /**
     * @return the capex
     */
    public String getCapex() {
        return capex;
    }

    /**
     * @param capex
     *            the capex to set
     */
    public void setCapex(String capex) {
        this.capex = capex;
    }

}
