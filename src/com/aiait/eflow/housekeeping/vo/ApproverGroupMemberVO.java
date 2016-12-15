package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class ApproverGroupMemberVO extends BaseVO {
	private String groupId;

	private String staffCode;
	
	private String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

}
