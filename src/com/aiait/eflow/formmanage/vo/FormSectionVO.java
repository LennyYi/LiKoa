package com.aiait.eflow.formmanage.vo;

import java.util.ArrayList;
import java.util.Collection;

public class FormSectionVO 
{
	private int formSystemId;

	private String sectionId;

	private String sectionType;

	private String sectionRemark;

	private String tableName;
	
	private String orderId;

	private String sectionUrl;
	
	private String export;
	
	
	private Collection fieldList = new ArrayList();

	
	
	public String getExport() {
		return export;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public String getSectionUrl() {
		return sectionUrl;
	}

	public void setSectionUrl(String sectionUrl) {
		this.sectionUrl = sectionUrl;
	}

	public Collection getFieldList() {
		return fieldList;
	}

	public void setFieldList(Collection fieldList) {
		this.fieldList = fieldList;
	}

	public int getFormSystemId() {
		return formSystemId;
	}

	public void setFormSystemId(int formSystemId) {
		this.formSystemId = formSystemId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionRemark() {
		return sectionRemark;
	}

	public void setSectionRemark(String sectionRemark) {
		this.sectionRemark = sectionRemark;
	}

	public String getSectionType() {
		return sectionType;
	}

	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
