package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class DBOwnerVO extends BaseVO {
	private String dbId;

	private String dbName;

	private String dbStaffCode;

	public String getDBId() {
		return dbId;
	}

	public void setDBId(String dbId) {
		this.dbId = dbId;
	}

	public String getDBName() {
		return dbName;
	}

	public void setDBName(String dbName) {
		this.dbName = dbName;
	}

	public String getDBStaffCode() {
		return dbStaffCode;
	}

	public void setDBStaffCode(String dbStaffCode) {
		this.dbStaffCode = dbStaffCode;
	}
}
