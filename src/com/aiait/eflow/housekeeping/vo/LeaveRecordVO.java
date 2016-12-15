package com.aiait.eflow.housekeeping.vo;

import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.vo.BaseVO;

public class LeaveRecordVO extends BaseVO {
	private int id;
	
	private String requestNo;

	private String leaveType;

	private double leaveDays;
	
	private String leaveFromDate;
	
	private String leaveFromType;
	
	private String leaveToDate;
	
	private String leaveToType;
	
	private String requestStaffCode;
	
	private String processStatus;
	
	private String processStatusLabel;

	public String getProcessStatusLabel() {
		if(this.getProcessStatus()!=null && "03".equals(this.getProcessStatus())){
			processStatusLabel = "Rejected（已拒绝）";
		}else if(this.getProcessStatus()!=null && "04".equals(this.getProcessStatus())){
			String currentDate = StringUtil.getCurrentDateStr("yyyy-MM-dd");
			
			processStatusLabel = "Approved";
			if(this.getLeaveFromDate()!=null && this.getLeaveFromDate().compareTo(currentDate)<0){
				processStatusLabel = processStatusLabel + "（已完成）";
			}else{
				processStatusLabel = processStatusLabel + "（未完成）";
			}
			
		}else if(this.getProcessStatus()!=null && ("01".equals(this.getProcessStatus()) || "02".equals(this.getProcessStatus()))){
			processStatusLabel = "Processing（审批中）";
		}else if(this.getProcessStatus()!=null && "05".equals(this.getProcessStatus())){
			processStatusLabel = "未存档";
		}else if(this.getProcessStatus()!=null && "06".equals(this.getProcessStatus())){
			processStatusLabel = "已存档";
		}else if(this.getProcessStatus()!=null && ("00".equals(this.getProcessStatus()))){
			processStatusLabel = "Processing（未审批）";
		}else{
			processStatusLabel = "Unkown";
		}
		return processStatusLabel;
	}

	public String getLeaveFromDate() {
		return leaveFromDate;
	}

	public void setLeaveFromDate(String leaveFromDate) {
		this.leaveFromDate = leaveFromDate;
	}

	public String getLeaveFromType() {
		return leaveFromType;
	}

	public void setLeaveFromType(String leaveFromType) {
		this.leaveFromType = leaveFromType;
	}

	public String getLeaveToDate() {
		return leaveToDate;
	}

	public void setLeaveToDate(String leaveToDate) {
		this.leaveToDate = leaveToDate;
	}

	public String getLeaveToType() {
		return leaveToType;
	}

	public void setLeaveToType(String leaveToType) {
		this.leaveToType = leaveToType;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getRequestStaffCode() {
		return requestStaffCode;
	}

	public void setRequestStaffCode(String requestStaffCode) {
		this.requestStaffCode = requestStaffCode;
	}

	public double getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(double leaveDays) {
		this.leaveDays = leaveDays;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
