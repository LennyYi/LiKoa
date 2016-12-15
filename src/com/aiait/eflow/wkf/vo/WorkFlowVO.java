package com.aiait.eflow.wkf.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.aiait.framework.vo.BaseVO;

public class WorkFlowVO extends BaseVO {
	private int flowBaseId;

	private String flowName;

	private int formSystemId;

	private String formName;
	
	private String formId;

	private String description;
	
	private String orgId;
	
	private String afterHandleUrl; //每个操作处理完毕后，需要额外的处理

	public Collection itemList = new ArrayList();
	
	public String currentNodeId;
	
	public HashMap conditionMap = null;

	public int getFlowBaseId() {
		return flowBaseId;
	}

	public void setFlowBaseId(int flowBaseId) {
		this.flowBaseId = flowBaseId;
	}

	public Collection getItemList() {
		return itemList;
	}

	public void setItemList(Collection itemList) {
		this.itemList = itemList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public int getFormSystemId() {
		return formSystemId;
	}

	public void setFormSystemId(int formSystemId) {
		this.formSystemId = formSystemId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public HashMap getConditionMap() {
		return conditionMap;
	}

	public void setConditionMap(HashMap conditionMap) {
		this.conditionMap = conditionMap;
	}

	public String getCurrentNodeId() {
		return currentNodeId;
	}

	public void setCurrentNodeId(String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAfterHandleUrl() {
		return afterHandleUrl;
	}

	public void setAfterHandleUrl(String afterHandleUrl) {
		this.afterHandleUrl = afterHandleUrl;
	}

}