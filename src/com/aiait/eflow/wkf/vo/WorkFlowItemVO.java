package com.aiait.eflow.wkf.vo;

import com.aiait.framework.vo.BaseVO;

public class WorkFlowItemVO extends BaseVO {

    private String flowId = "";

    private String itemId = ""; // nodeId;

    private String itemType = ""; // 0---开始节点;1---结束节点;2---Approval;3---Process

    private String name = ""; // nodeName

    private String priDepId = ""; // preNodeId

    private String limiteDate = ""; // limitedHours

    private String posX = "";

    private String posY = "";

    private String hasRule = "0"; // 是否定义了规则：0---没有规则;1---有规则

    private int ruleId = 0;

    private String approveGroupId = "";

    private String approvestaffCode = "";

    private String masterId = "";

    private String secondId = "";

    private String updateSections = ""; // 在流转过程可以update的field，形式“SectionId.Field_id,SectionId.Field_id”

    private String fillSectionFields = ""; // 只能在流转过程中才可以填写的field,形式“SectionId.Field_id,SectionId.Field_id”
    
    private String hiddenSectionFields = "";

    private String nodeAlias = ""; // 节点别名

    private String processorField = "";

    private String companyField = "";

    private String delaytimeField = "";

    private String approveHandle = "";

    private String rejectHandle = "";

    private String approveAlias = "";

    private String rejectAlias = "";

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLimiteDate() {
        return limiteDate;
    }

    public void setLimiteDate(String limiteDate) {
        this.limiteDate = limiteDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getPriDepId() {
        return priDepId;
    }

    public void setPriDepId(String priDepId) {
        this.priDepId = priDepId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getApproveGroupId() {
        return approveGroupId;
    }

    public void setApproveGroupId(String approveGroupId) {
        this.approveGroupId = approveGroupId;
    }

    public String getApprovestaffCode() {
        return approvestaffCode;
    }

    public void setApprovestaffCode(String approvestaffCode) {
        this.approvestaffCode = approvestaffCode;
    }

    public String getHasRule() {
        return hasRule;
    }

    public void setHasRule(String hasRule) {
        this.hasRule = hasRule;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getUpdateSections() {
        return updateSections;
    }

    public void setUpdateSections(String updateSections) {
        this.updateSections = updateSections;
    }

    public String getNodeAlias() {
        return nodeAlias;
    }

    public void setNodeAlias(String nodeAlias) {
        this.nodeAlias = nodeAlias;
    }

    public String getFillSectionFields() {
        return fillSectionFields;
    }

    public void setFillSectionFields(String fillSectionFields) {
        this.fillSectionFields = fillSectionFields;
    }

    /**
     * @return the processorField
     */
    public String getProcessorField() {
        return processorField;
    }

    /**
     * @param processorField
     *            the processorField to set
     */
    public void setProcessorField(String processorField) {
        this.processorField = processorField;
    }

    /**
     * @return the companyField
     */
    public String getCompanyField() {
        return companyField;
    }

    /**
     * @param companyField
     *            the companyField to set
     */
    public void setCompanyField(String companyField) {
        this.companyField = companyField;
    }

    /**
     * @return the delaytimeField
     */
    public String getDelaytimeField() {
        return delaytimeField;
    }

    /**
     * @param delaytimeField
     *            the delaytimeField to set
     */
    public void setDelaytimeField(String delaytimeField) {
        this.delaytimeField = delaytimeField;
    }

    /**
     * @return the approveHandle
     */
    public String getApproveHandle() {
        return approveHandle;
    }

    /**
     * @param approveHandle
     *            the approveHandle to set
     */
    public void setApproveHandle(String approveHandle) {
        this.approveHandle = approveHandle;
    }

    /**
     * @return the rejectHandle
     */
    public String getRejectHandle() {
        return rejectHandle;
    }

    /**
     * @param rejectHandle
     *            the rejectHandle to set
     */
    public void setRejectHandle(String rejectHandle) {
        this.rejectHandle = rejectHandle;
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

	public String getHiddenSectionFields() {
		return hiddenSectionFields;
	}

	public void setHiddenSectionFields(String hiddenSectionFields) {
		this.hiddenSectionFields = hiddenSectionFields;
	}
    

}
