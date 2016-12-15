package com.aiait.eflow.wkf.vo;

import java.util.Date;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.framework.vo.BaseVO;

public class WorkFlowProcessTraceVO extends BaseVO {
    private String requestNo;

    private int flowId;

    private String currentNodeId;

    private String handleStaffCode;

    private String handleComments;

    private Date handleDate;
    private String handleDateStr;

    protected double handleHours;

    private String handleType;

    private String approveAlias;

    private String rejectAlias;

    private double delayTime;

    private int processId;

    private int formSystemId;

    private String deputyFlag;

    private String originProcessor;

    private String filePathName;

    private String isUrgent;

    private String rejectToNodeId;

    private String nodeName;

    private String nextApprover;

    private String ccStaffs;

    private String orgId;

    private String teamCode;

    public String getCcStaffs() {
        return ccStaffs;
    }

    public void setCcStaffs(String ccStaffs) {
        this.ccStaffs = ccStaffs;
    }

    public String getNextApprover() {
        return nextApprover;
    }

    public void setNextApprover(String nextApprover) {
        this.nextApprover = nextApprover;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRejectToNodeId() {
        return rejectToNodeId;
    }

    public void setRejectToNodeId(String rejectToNodeId) {
        this.rejectToNodeId = rejectToNodeId;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public double getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(double delayTime) {
        this.delayTime = delayTime;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public String getHandleComments() {
        return handleComments;
    }

    public void setHandleComments(String handleComments) {
        this.handleComments = handleComments;
    }

    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getHandleDateStr() {
        return handleDateStr;
    }

    public void setHandleDateStr(String handleDateStr) {
        this.handleDateStr = handleDateStr;
    }

    /**
     * @return the handleHours
     */
    public double getHandleHours() {
        return handleHours;
    }

    /**
     * @param handleHours
     *            the handleHours to set
     */
    public void setHandleHours(double handleHours) {
        this.handleHours = handleHours;
    }

    public String getHandleStaffCode() {
        return handleStaffCode;
    }

    public void setHandleStaffCode(String handleStaffCode) {
        this.handleStaffCode = handleStaffCode;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getHandleTypeName() {
        if ((CommonName.HANDLE_TYPE_APPROVE.equals(this.handleType) || CommonName.HANDLE_TYPE_COMPLETE
                .equals(this.handleType)) && this.getApproveAlias() != null && !this.getApproveAlias().equals("")) {
            return this.getApproveAlias();
        } else if (CommonName.HANDLE_TYPE_REJECT.equals(this.handleType) && this.getRejectAlias() != null
                && !this.getRejectAlias().equals("")) {
            return this.getRejectAlias();
        } else {
            return DataMapUtil.convertHandleType(this.handleType);
        }
    }

    /**
     * @return the approveAlias
     */
    public String getApproveAlias() {
        return approveAlias;
    }

    /**
     * @param approveAlias
     *            the approveAlias to set
     */
    public void setApproveAlias(String approveAlias) {
        this.approveAlias = approveAlias;
    }

    /**
     * @return the rejectAlias
     */
    public String getRejectAlias() {
        return rejectAlias;
    }

    /**
     * @param rejectAlias
     *            the rejectAlias to set
     */
    public void setRejectAlias(String rejectAlias) {
        this.rejectAlias = rejectAlias;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public int getFormSystemId() {
        return formSystemId;
    }

    public void setFormSystemId(int formSystemId) {
        this.formSystemId = formSystemId;
    }

    public String getDeputyFlag() {
        return deputyFlag;
    }

    public void setDeputyFlag(String deputyFlag) {
        this.deputyFlag = deputyFlag;
    }

    public String getOriginProcessor() {
        return originProcessor;
    }

    public void setOriginProcessor(String originProcessor) {
        this.originProcessor = originProcessor;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }
}
