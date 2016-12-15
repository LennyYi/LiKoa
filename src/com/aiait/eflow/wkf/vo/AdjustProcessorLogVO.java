package com.aiait.eflow.wkf.vo;

import com.aiait.framework.vo.BaseVO;

public class AdjustProcessorLogVO extends BaseVO {
	private String requestNo;

	private String nodeId;

	private String originalProcessor;

	private String adjustToProcessor;

	private String operateStaffCode;

	private String operateDateStr;

	public String getAdjustToProcessor() {
		return adjustToProcessor;
	}

	public void setAdjustToProcessor(String adjustToProcessor) {
		this.adjustToProcessor = adjustToProcessor;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getOperateDateStr() {
		return operateDateStr;
	}

	public void setOperateDateStr(String operateDateStr) {
		this.operateDateStr = operateDateStr;
	}

	public String getOperateStaffCode() {
		return operateStaffCode;
	}

	public void setOperateStaffCode(String operateStaffCode) {
		this.operateStaffCode = operateStaffCode;
	}

	public String getOriginalProcessor() {
		return originalProcessor;
	}

	public void setOriginalProcessor(String originalProcessor) {
		this.originalProcessor = originalProcessor;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

}
