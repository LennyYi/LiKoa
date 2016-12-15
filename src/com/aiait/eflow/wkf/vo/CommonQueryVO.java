package com.aiait.eflow.wkf.vo;

import com.aiait.framework.vo.BaseVO;

public class CommonQueryVO extends BaseVO {
	private String sectionId;

	private String fieldId;

	private String operateType;

	private String fieldValue;

	private String logicType;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getLogicType() {
		return logicType;
	}

	public void setLogicType(String logicType) {
		this.logicType = logicType;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

}
