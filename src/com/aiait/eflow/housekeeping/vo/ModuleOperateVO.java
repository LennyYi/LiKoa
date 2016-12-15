package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class ModuleOperateVO extends BaseVO {
	private int operateId;

	private int moduleId;

	private String operateName;

	private String remark;
	
	private String formTypeId;

	public String getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getOperateId() {
		return operateId;
	}

	public void setOperateId(int operateId) {
		this.operateId = operateId;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
