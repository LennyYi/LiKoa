package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class AnnualLeaveStVO extends BaseVO {
	private int id;
    private String orgId;
    private int serviceYearLevel;
    private String gradeId;
    private double annualLeaveDays;   //���е��������
    private double limitTransferDays; //�ܹ�ת�Ƶ���һ����������
    private String remark;
    
    private String gradeLabel;
    
    private static String[] labels = {"1��","2��","3��","4��","5��","6��","7��","8��","9��","10��","11��","12��","13��","14��","15��"};
    private static String[] grade = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"};
    
	public String getGradeLabel() {
		String gradeIds = this.getGradeId();
		if(gradeIds==null || "".equals(gradeIds)) return "";
		
		return gradeLabel;
	}
	public void setGradeLabel(String gradeLabel) {
		this.gradeLabel = gradeLabel;
	}
	public double getAnnualLeaveDays() {
		return annualLeaveDays;
	}
	public void setAnnualLeaveDays(double annualLeaveDays) {
		this.annualLeaveDays = annualLeaveDays;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getServiceYearLevel() {
		return serviceYearLevel;
	}
	public void setServiceYearLevel(int serviceYearLevel) {
		this.serviceYearLevel = serviceYearLevel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLimitTransferDays() {
		return limitTransferDays;
	}
	public void setLimitTransferDays(double limitTransferDays) {
		this.limitTransferDays = limitTransferDays;
	}
    
    
}
