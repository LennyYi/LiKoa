package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class BankVO extends BaseVO {

    protected String orgId;
    protected String city;
    protected String sunCode;
	protected String bankCode;
    protected String bankName;
    protected String accountCode;
    protected String accountName;
    protected String type;
    protected int isDefault;

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSunCode() {
		return sunCode;
	}
	public void setSunCode(String sunCode) {
		this.sunCode = sunCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}
