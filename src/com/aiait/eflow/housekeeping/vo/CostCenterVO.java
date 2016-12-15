package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class CostCenterVO extends BaseVO {

    protected String cc_code;
    protected String cc_name;
    protected String exco;
    protected String t_code;
    
	public String getCc_code() {
		return cc_code;
	}
	public void setCc_code(String ccCode) {
		cc_code = ccCode;
	}
	public String getCc_name() {
		return cc_name;
	}
	public void setCc_name(String ccName) {
		cc_name = ccName;
	}
	public String getExco() {
		return exco;
	}
	public void setExco(String exco) {
		this.exco = exco;
	}
	public String getT_code() {
		return t_code;
	}
	public void setT_code(String tCode) {
		t_code = tCode;
	}
}
