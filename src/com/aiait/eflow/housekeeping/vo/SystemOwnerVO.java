package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class SystemOwnerVO extends BaseVO {
	private String systemId;

	private String systemName;

	private String systemStaffCode;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemStaffCode() {
		return systemStaffCode;
	}

	public void setSystemStaffCode(String systemStaffCode) {
		this.systemStaffCode = systemStaffCode;
	}

}
