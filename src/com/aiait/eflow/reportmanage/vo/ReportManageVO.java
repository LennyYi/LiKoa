package com.aiait.eflow.reportmanage.vo;

import java.util.ArrayList;
import java.util.Collection;

public class ReportManageVO {
	
	public int reportSystemId;

	public String reportId;

	public String reportName;

	public String reportType;
	
	private String displayType;
	
	public String reportSystemImg;

	private String description;

	private String status;

	private String createDateStr;
	
	private String actionType;
	
	private String actionMessage;
	
	private String orgId;

	private String pre_validation_url; //保存之前需要调用url所指定的资源进行校验
	
	private String afterSaveUrl; //保存之后需要调用url所指定的资源进行额外处理
	
	private Collection sectionList = new ArrayList();

	public int getReportSystemId() {
		return reportSystemId;
	}

	public void setReportSystemId(int reportSystemId) {
		this.reportSystemId = reportSystemId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportSystemImg() {
		return reportSystemImg;
	}

	public void setReportSystemImg(String reportSystemImg) {
		this.reportSystemImg = reportSystemImg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPre_validation_url() {
		return pre_validation_url;
	}

	public void setPre_validation_url(String preValidationUrl) {
		pre_validation_url = preValidationUrl;
	}

	public String getAfterSaveUrl() {
		return afterSaveUrl;
	}

	public void setAfterSaveUrl(String afterSaveUrl) {
		this.afterSaveUrl = afterSaveUrl;
	}

	public Collection getSectionList() {
		return sectionList;
	}

	public void setSectionList(Collection sectionList) {
		this.sectionList = sectionList;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	
	
	

}
