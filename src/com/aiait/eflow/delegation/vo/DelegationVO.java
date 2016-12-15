package com.aiait.eflow.delegation.vo;

import com.aiait.framework.vo.BaseVO;

public class DelegationVO extends BaseVO {
	private int id;
	
	private String authorityApprover;

	private String authorityDeputy;

	private String delegateFromStr;

	private String delegateToStr;

	private String approverTeamCode;

	private String deputyTeamCode;

	private String status;

	private String activedBy;

	private String activedDateStr;
	
	private String handledBeginDate;
	private String handledEndDate;

	private String orgId;
	
	private String delegateLevel;
	private String formTypeId;
	private String formSystemId;
	private String scopeStr;
	private String applyOrgId;
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHandledBeginDate() {
		return handledBeginDate;
	}

	public void setHandledBeginDate(String handledBeginDate) {
		this.handledBeginDate = handledBeginDate;
	}

	public String getHandledEndDate() {
		return handledEndDate;
	}

	public void setHandledEndDate(String handledEndDate) {
		this.handledEndDate = handledEndDate;
	}

	public String getActivedBy() {
		return activedBy;
	}

	public void setActivedBy(String activedBy) {
		this.activedBy = activedBy;
	}

	public String getActivedDateStr() {
		return activedDateStr;
	}

	public void setActivedDateStr(String activedDateStr) {
		this.activedDateStr = activedDateStr;
	}

	public String getApproverTeamCode() {
		return approverTeamCode;
	}

	public void setApproverTeamCode(String approverTeamCode) {
		this.approverTeamCode = approverTeamCode;
	}

	public String getAuthorityApprover() {
		return authorityApprover;
	}

	public void setAuthorityApprover(String authorityApprover) {
		this.authorityApprover = authorityApprover;
	}

	public String getAuthorityDeputy() {
		return authorityDeputy;
	}

	public void setAuthorityDeputy(String authorityDeputy) {
		this.authorityDeputy = authorityDeputy;
	}

	public String getDelegateFromStr() {
		return delegateFromStr;
	}

	public void setDelegateFromStr(String delegateFromStr) {
		this.delegateFromStr = delegateFromStr;
	}

	public String getDelegateToStr() {
		return delegateToStr;
	}

	public void setDelegateToStr(String delegateToStr) {
		this.delegateToStr = delegateToStr;
	}

	public String getDeputyTeamCode() {
		return deputyTeamCode;
	}

	public void setDeputyTeamCode(String deputyTeamCode) {
		this.deputyTeamCode = deputyTeamCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDelegateLevel() {
		return delegateLevel;
	}

	public void setDelegateLevel(String delegateLevel) {
		this.delegateLevel = delegateLevel;
	}

	public String getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getFormSystemId() {
		return formSystemId;
	}

	public void setFormSystemId(String formSystemId) {
		this.formSystemId = formSystemId;
	}

	public String getScopeStr() {
		return scopeStr;
	}

	public void setScopeStr(String scopeStr) {
		this.scopeStr = scopeStr;
	}

	public String getApplyOrgId() {
		return applyOrgId;
	}

	public void setApplyOrgId(String applyOrgId) {
		this.applyOrgId = applyOrgId;
	}
	
}
