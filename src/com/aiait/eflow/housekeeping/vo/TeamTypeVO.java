package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class TeamTypeVO extends BaseVO {
	String teamTypeId;
	String teamTypeName;
	String teamTypeDes;
	
	public String getTeamTypeDes() {
		return teamTypeDes;
	}
	public void setTeamTypeDes(String teamTypeDes) {
		this.teamTypeDes = teamTypeDes;
	}
	public String getTeamTypeId() {
		return teamTypeId;
	}
	public void setTeamTypeId(String teamTypeId) {
		this.teamTypeId = teamTypeId;
	}
	public String getTeamTypeName() {
		return teamTypeName;
	}
	public void setTeamTypeName(String teamTypeName) {
		this.teamTypeName = teamTypeName;
	}
}
