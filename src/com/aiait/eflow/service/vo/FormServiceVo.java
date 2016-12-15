package com.aiait.eflow.service.vo;

import java.io.Serializable;
import java.util.*;

/**
 * Form Service Object
 * 
 * @version 2010-11-15
 */
public class FormServiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int formSystemId;
    protected String formCode;
    protected String requester;
    protected String submitBy;
    protected boolean isNTID;
    protected String callback;
    protected String serviceId;
    protected String requestNo;

    protected List initFields = new ArrayList();

    /**
     * @return the formSystemId
     */
    public int getFormSystemId() {
        return formSystemId;
    }

    /**
     * @param formSystemId
     *            the formSystemId to set
     */
    public void setFormSystemId(int formSystemId) {
        this.formSystemId = formSystemId;
    }

    /**
     * @return the formCode
     */
    public String getFormCode() {
        return formCode;
    }

    /**
     * @param formCode
     *            the formCode to set
     */
    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    /**
     * @return the requester
     */
    public String getRequester() {
        return requester;
    }

    /**
     * @param requester
     *            the requester to set
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * @return the submitBy
     */
    public String getSubmitBy() {
        return submitBy;
    }

    /**
     * @param submitBy
     *            the submitBy to set
     */
    public void setSubmitBy(String submitBy) {
        this.submitBy = submitBy;
    }

    /**
     * @return the isNTID
     */
    public boolean isNTID() {
        return isNTID;
    }

    /**
     * @param isNTID
     *            the isNTID to set
     */
    public void setNTID(boolean isNTID) {
        this.isNTID = isNTID;
    }

    /**
     * @return the callback
     */
    public String getCallback() {
        return callback;
    }

    /**
     * @param callback
     *            the callback to set
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(String serviceId) {
        if (serviceId == null || serviceId.trim().equals("")) {
            return;
        }
        this.serviceId = serviceId.trim();
    }

    /**
     * @return the requestNo
     */
    public String getRequestNo() {
        return requestNo;
    }

    /**
     * @param requestNo
     *            the requestNo to set
     */
    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    /**
     * @return the initFields
     */
    public List getInitFields() {
        return initFields;
    }

    /**
     * @param initFields
     *            the initFields to set
     */
    public void setInitFields(List initFields) {
        this.initFields = initFields;
    }

}
