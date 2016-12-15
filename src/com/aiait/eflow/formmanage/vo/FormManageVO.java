package com.aiait.eflow.formmanage.vo;

import java.util.ArrayList;
import java.util.Collection;

public class FormManageVO {
	public int formSystemId;

	public String formId;

	public String formName;

	public String formType;
	
	public String formSystemImg;

	private String description;

	private String status;

	private String createDateStr;
	
	private String actionType;
	
	private String actionMessage;
	
	private String orgId;

	private String pre_validation_url; //保存之前需要调用url所指定的资源进行校验
	
	private String afterSaveUrl; //保存之后需要调用url所指定的资源进行额外处理
	
	private String submitAlias="";
	private String saveAlias="";
	public String getSubmitAlias() {
		return submitAlias;
	}

	public void setSubmitAlias(String submitAlias) {
		this.submitAlias = submitAlias;
	}

	public String getSaveAlias() {
		return saveAlias;
	}

	public void setSaveAlias(String saveAlias) {
		this.saveAlias = saveAlias;
	}

	private Collection sectionList = new ArrayList();

	// public Collection fieldList;

	// public Collection getFieldList() {
	// return fieldList;
	// }

	// public void setFieldList(Collection fieldList) {
	// this.fieldList = fieldList;
	// }

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection getSectionList() {
		return sectionList;
	}

	public void setSectionList(Collection sectionList) {
		this.sectionList = sectionList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFormSystemId() {
		return formSystemId;
	}

	public void setFormSystemId(int formSystemId) {
		this.formSystemId = formSystemId;
	}
	
	public String getFormSystemImg(){
		return formSystemImg;
	}
	
	public void setFormSystemImg(String formSystemImg) {
		this.formSystemImg = formSystemImg;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDataStr) {
		this.createDateStr = createDataStr;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
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

	public void setPre_validation_url(String pre_validation_url) {
		this.pre_validation_url = pre_validation_url;
	}

	public String getAfterSaveUrl() {
		return afterSaveUrl;
	}

	public void setAfterSaveUrl(String afterSaveUrl) {
		this.afterSaveUrl = afterSaveUrl;
	}

}
