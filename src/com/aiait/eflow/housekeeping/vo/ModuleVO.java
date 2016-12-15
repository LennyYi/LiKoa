package com.aiait.eflow.housekeeping.vo;

import java.util.Collection;

import com.aiait.framework.vo.BaseVO;

public class ModuleVO extends BaseVO {
	private int moduleId;

	private String moduleName;

	private int parentId;

	private String targetUrl;

	private int orderId;

	private String remark;

	private String imageFileName;

	private Collection operateList;
	

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Collection getOperateList() {
		return operateList;
	}

	public void setOperateList(Collection operateList) {
		this.operateList = operateList;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
