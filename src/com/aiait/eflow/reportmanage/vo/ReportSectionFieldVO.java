package com.aiait.eflow.reportmanage.vo;

import java.util.Collection;

public class ReportSectionFieldVO {
	public int reportSystemId;

	public String sectionId;
	
	public String fieldId;
	
	public String sectionType;

	public String fieldLabel;

	public int fieldType = 1; // �ؼ����ͣ����磺1���ı���2���ı���3����ѡ���4����ѡ���5��ѡһѡ���6��ѡ��ѡ����; 8Comments(ע��)

	public boolean isRequired; // �Ƿ�����Ϊ�գ�true--����Ϊ�գ�false--������Ϊ�գ�Ϊ������

	public int dataType = 1; // �������ͣ����磺1�ַ�����2���ڣ�3����

	public int fieldLength; // �ֶ����ݳ���

	public int fieldDataSourceType; // ����ؼ�����Ϊ��4����ѡ��򡰻�5��ѡһѡ�����6��ѡ��ѡ�����ָ��ѡ������ݶ�����Դ���ͣ�
                                    // 1������ϵͳ��������2���������ݿ��ֵ��
	
	public String fieldDataSrSQL; // ��ȡ���ɸ�ѡ����ѡ���SQL;
	
	public int orderId;
	
	public int decimalDigits = 0;    //���dataType=3�����֣�����Ҫָ��С��λ����Ĭ��Ϊ0
	
	private int highLevel = -1; //�Ƿ��������ʾ��-1��ʾ����Ҫ��1��ʾ��Ҫ
	
	
	private int issinglerow =-1;  //�Ƿ�����ʾ
	
	private int controlsWidth;  //�ؼ��Ŀ��ȣ�ֻ���ؼ�����Ϊ��1���ı��򡱣���2���ı���ʱ��Ч
	
	private int controlsHeight; //�ؼ��ĸ߶ȣ�ֻ���ؼ�����Ϊ��2���ı���ʱ��Ч
	
	private String fieldComments; //���ֶε�ע��˵��
	
	private String commentContent;//�������ֶλ�ǰSection�Ľ���˵��;�����������fieldComments
	
	public Collection optionList;
	
	private String systemFieldType; //��������System Field�ֶ��е�fieldType
	
	private String systemFieldParams; //��������System Field�ֶ��е�paramList
	
	private	int columnType;
	
	private String defaultValue;   //���ֶ��趨��Ĭ��ֵ
	
	private int columnWidth;       //����ǡ�table��section�е�field,���ֶ������趨��field�����е���ʾ���ȵİٷֱ�ֵ
	
	private int isSingleLabel = -1;
	
	private int isReadonly = -1;   //�Ƿ���ֻ����-1��ʾ��ֻ����1��ʾ��ֻ��
	
	
	private int border = 0;
	
	private String cssStr;
	
	private int aligned;
	
	
	
	
	public int getAligned() {
		return aligned;
	}
	public void setAligned(int aligned) {
		this.aligned = aligned;
	}
	public int getBorder() {
		return border;
	}
	public void setBorder(int border) {
		this.border = border;
	}
	
	public int getIsReadonly() {
		return isReadonly;
	}
	public void setIsReadonly(int isReadonly) {
		this.isReadonly = isReadonly;
	}
			
	public int getIsSingleLabel() {
		return isSingleLabel;
	}
	public void setIsSingleLabel(int isSingleLabel) {
		this.isSingleLabel = isSingleLabel;
	}
	public int getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public int getColumnType(){
		return this.columnType;
	}
	public void setColumnType(int columnType){
		this.columnType = columnType;
	}
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getFieldDataSourceType() {
		return fieldDataSourceType;
	}

	public void setFieldDataSourceType(int fieldDataSourceType) {
		this.fieldDataSourceType = fieldDataSourceType;
	}

	public String getFieldDataSrSQL() {
		return fieldDataSrSQL;
	}

	public void setFieldDataSrSQL(String fieldDataSrSQL) {
		this.fieldDataSrSQL = fieldDataSrSQL;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getSectionType() {
		return sectionType;
	}

	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	public int getReportSystemId() {
		return reportSystemId;
	}

	public void setReportSystemId(int reportSystemId) {
		this.reportSystemId = reportSystemId;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Collection getOptionList() {
		return optionList;
	}

	public void setOptionList(Collection optionList) {
		this.optionList = optionList;
	}

	public int getHighLevel() {
		return highLevel;
	}

	public void setHighLevel(int highLevel) {
		this.highLevel = highLevel;
	}
	
	
	public int getIsSingleRow() {
		return issinglerow;
	}

	public void setIsSingleRow(int IsSingleRow) {
		this.issinglerow = IsSingleRow;
	}

	public int getControlsHeight() {
		return controlsHeight;
	}

	public void setControlsHeight(int controlsHeight) {
		this.controlsHeight = controlsHeight;
	}

	public int getControlsWidth() {
		return controlsWidth;
	}

	public void setControlsWidth(int controlsWidth) {
		this.controlsWidth = controlsWidth;
	}

	public String getFieldComments() {
		return fieldComments;
	}

	public void setFieldComments(String fieldComments) {
		this.fieldComments = fieldComments;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getSystemFieldType() {
		return systemFieldType;
	}
	public void setSystemFieldType(String systemFieldType) {
		this.systemFieldType = systemFieldType;
	}
	public String getSystemFieldParams() {
		return systemFieldParams;
	}
	public void setSystemFieldParams(String systemFieldParams) {
		this.systemFieldParams = systemFieldParams;
	}
	public String getCssStr() {
		return cssStr;
	}
	public void setCssStr(String cssStr) {
		this.cssStr = cssStr;
	}
	
	
}