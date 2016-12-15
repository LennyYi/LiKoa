package com.aiait.eflow.wkf.vo;

import java.util.Date;

import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.vo.BaseVO;

public class WorkFlowProcessVO extends BaseVO {

    protected String requestNo;

    protected String requestStaffCode;

    protected int flowId;

    protected Date receivingDate;
    protected String receivingDateStr;

    protected String previousProcessor;

    protected String currentProcessor;

    protected String nextProcessor;

    protected String nodeId;

    protected String nodeType;

    protected String approveAlias;

    protected String rejectAlias;

    protected String status;

    protected String formType;

    protected int formSystemId;
    protected String formId;
    protected String formName;

    protected Date submissionDate;
    protected String submissionDateStr;

    protected String beginSubmissionDate;
    protected String endSubmissionDate;

    protected String beginHandleDate;
    protected String endHandleDate;

    protected String beginCompleteDate;
    protected String endCompleteDate;

    protected boolean expertMultiReply = false;

    protected boolean expertReplied = false;

    protected String remainTime;

    protected String inProcess;
    protected String inProcessStaffCode;

    protected double limitedHours;

    protected String nodeName;

    protected Date handleDate;
    protected String handleDateStr;

    protected double handleHours;

    protected String openFlag;

    protected String hasNextNode;

    protected String isUrgent;

    protected String invitedExpert;

    protected String submittedBy;

    protected boolean expertAdviceFlag;

    protected String ccStaffs;

    protected String isDeputy;

    protected String originProcessor;

    protected String orgId;
    protected String orgName;

    protected String teamCode;
    protected String teamName;

    protected Date waitNodeProcessDate;

    protected String waitNodeProcessType;
    
    //indicator to show is deal form or personal form
    protected boolean IsDealForm;

    // For tip field
    protected String[] tipField;

    // For highlight fields
    protected String[] highlightField;
    protected String[] highlightField2;
    protected String[] highlightField3;

    protected FormBasicDataVO formBasicData;

    protected Date updateDate;
    protected int remaineApproversNum;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName
     *            the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getIsDeputy() {
        return isDeputy;
    }

    public void setIsDeputy(String isDeputy) {
        this.isDeputy = isDeputy;
    }

    public String getOriginProcessor() {
        return originProcessor;
    }

    public void setOriginProcessor(String originProcessor) {
        this.originProcessor = originProcessor;
    }

    public String getCcStaffs() {
        return ccStaffs;
    }

    public void setCcStaffs(String ccStaffs) {
        this.ccStaffs = ccStaffs;
    }

    public boolean getExpertAdviceFlag() {
        return expertAdviceFlag;
    }

    public void setExpertAdviceFlag(boolean expertAdviceFlag) {
        this.expertAdviceFlag = expertAdviceFlag;
    }

    public String getInvitedExpert() {
        return invitedExpert;
    }

    public void setInvitedExpert(String invitedExpert) {
        this.invitedExpert = invitedExpert;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getHasNextNode() {
        return hasNextNode;
    }

    public void setHasNextNode(String hasNextNode) {
        this.hasNextNode = hasNextNode;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    /**
     * @return the handleDate
     */
    public Date getHandleDate() {
        return handleDate;
    }

    /**
     * @param handleDate
     *            the handleDate to set
     */
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public double getLimitedHours() {
        return limitedHours;
    }

    public void setLimitedHours(double limitedHours) {
        this.limitedHours = limitedHours;
    }

    public String getInProcessStaffCode() {
        return inProcessStaffCode;
    }

    public void setInProcessStaffCode(String inProcessStaffCode) {
        this.inProcessStaffCode = inProcessStaffCode;
    }

    public String getInProcess() {
        return inProcess;
    }

    public void setInProcess(String inProcess) {
        this.inProcess = inProcess;
    }

    public String getCurrentInProcessor() {
        if ("1".equals(this.getInProcess())) {
            return this.getInProcessStaffCode();
        }
        return this.getCurrentProcessor();
    }

    /**
     * @return the formId
     */
    public String getFormId() {
        return formId;
    }

    /**
     * @param formId
     *            the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    public int getFormSystemId() {
        return formSystemId;
    }

    public void setFormSystemId(int formSystemId) {
        this.formSystemId = formSystemId;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCurrentProcessor() {
        return currentProcessor;
    }

    public void setCurrentProcessor(String currentProcessor) {
        this.currentProcessor = currentProcessor;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public String getNextProcessor() {
        return nextProcessor;
    }

    public void setNextProcessor(String nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getPreviousProcessor() {
        return previousProcessor;
    }

    public void setPreviousProcessor(String previousProcessor) {
        this.previousProcessor = previousProcessor;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Date receivingDate) {
        this.receivingDate = receivingDate;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRequestStaffCode() {
        return requestStaffCode;
    }

    public void setRequestStaffCode(String requestStaffCode) {
        this.requestStaffCode = requestStaffCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivingDateStr() {
        return receivingDateStr;
    }

    public void setReceivingDateStr(String receivingDateStr) {
        this.receivingDateStr = receivingDateStr;
    }

    public String getSubmissionDateStr() {
        return submissionDateStr;
    }

    public void setSubmissionDateStr(String submissionDateStr) {
        this.submissionDateStr = submissionDateStr;
    }

    public String getBeginSubmissionDate() {
        return beginSubmissionDate;
    }

    public void setBeginSubmissionDate(String beginSubmissionDate) {
        this.beginSubmissionDate = beginSubmissionDate;
    }

    public String getEndSubmissionDate() {
        return endSubmissionDate;
    }

    public void setEndSubmissionDate(String endSubmissionDate) {
        this.endSubmissionDate = endSubmissionDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getBeginHandleDate() {
        return beginHandleDate;
    }

    public void setBeginHandleDate(String beginHandleDate) {
        this.beginHandleDate = beginHandleDate;
    }

    public String getEndHandleDate() {
        return endHandleDate;
    }

    public void setEndHandleDate(String endHandleDate) {
        this.endHandleDate = endHandleDate;
    }

    public boolean isDealByDeputy() {
        if (!"-1".equals(this.getNodeId()) && this.getIsDeputy() != null && "1".equals(this.getIsDeputy())
                && this.getOriginProcessor() != null && this.getOriginProcessor().indexOf(",") == -1) {
            return true;
        }
        return false;
    }

    public String[] getTipField() {
        return tipField;
    }

    public void setTipField(String[] tipField) {
        this.tipField = tipField;
    }

    // For tip field
    public String getHtmlTitleAttr() {
        if (this.tipField == null) {
            return "";
        }
        String titleAttr = "title='" + StringUtil.htmlEncoder(this.tipField[1] + ": " + this.tipField[2]) + "'";
        return titleAttr;
    }

    public String getTipContent() {
        if (this.tipField == null) {
            return "";
        }
        String tip = this.tipField[1] + ": " + this.tipField[2];
        return tip;
    }

    // For highlight field
    public String[] getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(String[] highlightField) {
        this.highlightField = highlightField;
    }

    public String getHighlightContent() {
        return this.getHighlightContent(false);
    }

    public String getHighlightContent(boolean onlyContent) {
        if (this.highlightField == null) {
            return "";
        }
        String remark = this.highlightField[3];
        String content;
        if (onlyContent || (remark != null && !"".equals(remark))) {
            content = StringUtil.htmlEncoder(this.highlightField[2]);
        } else {
            content = StringUtil.htmlEncoder(this.highlightField[1]) + ": <b>"
                    + StringUtil.htmlEncoder(this.highlightField[2]) + "</b>";
        }
        return content;
    }

    // For highlight field 2
    public String[] getHighlightField2() {
        return highlightField2;
    }

    public void setHighlightField2(String[] highlightField2) {
        this.highlightField2 = highlightField2;
    }

    public String getHighlightContent2() {
        return this.getHighlightContent2(false);
    }

    public String getHighlightContent2(boolean onlyContent) {
        if (this.highlightField2 == null) {
            return "";
        }
        String remark = this.highlightField2[3];
        String content;
        if (onlyContent || (remark != null && !"".equals(remark))) {
            content = StringUtil.htmlEncoder(this.highlightField2[2]);
        } else {
            content = StringUtil.htmlEncoder(this.highlightField2[1]) + ": <b>"
                    + StringUtil.htmlEncoder(this.highlightField2[2]) + "</b>";
        }
        return content;
    }

    // For highlight field 3
    public String[] getHighlightField3() {
        return highlightField3;
    }

    public void setHighlightField3(String[] highlightField3) {
        this.highlightField3 = highlightField3;
    }

    public String getHighlightContent3() {
        return this.getHighlightContent3(false);
    }

    public String getHighlightContent3(boolean onlyContent) {
        if (this.highlightField3 == null) {
            return "";
        }
        String remark = this.highlightField3[3];
        String content;
        if (onlyContent || (remark != null && !"".equals(remark))) {
            content = StringUtil.htmlEncoder(this.highlightField3[2]);
        } else {
            content = StringUtil.htmlEncoder(this.highlightField3[1]) + ": <b>"
                    + StringUtil.htmlEncoder(this.highlightField3[2]) + "</b>";
        }
        return content;
    }

    public FormBasicDataVO getFormBasicData() {
        return formBasicData;
    }

    public void setFormBasicData(FormBasicDataVO formBasicData) {
        this.formBasicData = formBasicData;
    }

    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType
     *            the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
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

    public void setBeginCompleteDate(String beginCompleteDate) {
        this.beginCompleteDate = beginCompleteDate;
    }

    public String getBeginCompleteDate() {
        return beginCompleteDate;
    }

    public void setEndCompleteDate(String endCompleteDate) {
        this.endCompleteDate = endCompleteDate;
    }

    public String getEndCompleteDate() {
        return endCompleteDate;
    }

    public void setExpertMultiReply(boolean expertMultiReply) {
        this.expertMultiReply = expertMultiReply;
    }

    public boolean isExpertMultiReply() {
        return expertMultiReply;
    }

    public void setExpertReplied(boolean expertReplied) {
        this.expertReplied = expertReplied;
    }

    public boolean isExpertReplied() {
        return expertReplied;
    }

    /**
     * @return the waitNodeProcessDate
     */
    public Date getWaitNodeProcessDate() {
        return waitNodeProcessDate;
    }

    /**
     * @param waitNodeProcessDate
     *            the waitNodeProcessDate to set
     */
    public void setWaitNodeProcessDate(Date waitNodeProcessDate) {
        this.waitNodeProcessDate = waitNodeProcessDate;
    }

    /**
     * @return the waitNodeProcessType
     */
    public String getWaitNodeProcessType() {
        return waitNodeProcessType;
    }

    /**
     * @param waitNodeProcessType
     *            the waitNodeProcessType to set
     */
    public void setWaitNodeProcessType(String waitNodeProcessType) {
        this.waitNodeProcessType = waitNodeProcessType;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getRemaineApproversNum() {
        return remaineApproversNum;
    }

    public void setRemaineApproversNum(int remaineApproversNum) {
        this.remaineApproversNum = remaineApproversNum;
    }

}
