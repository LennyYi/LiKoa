package com.aiait.eflow.tag;

public class ColumnModelVO {
	private String name;
	private String index;
	private String formatter;
	private int width;
	private String sorttype;
	private boolean resizable;
	private boolean hidden;
	private String searchoptions;
	
	
	
	public String getSearchoptions() {
		return searchoptions;
	}
	public void setSearchoptions(String searchoptions) {
		this.searchoptions = searchoptions;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getSorttype() {
		return sorttype;
	}
	public void setSorttype(String sorttype) {
		this.sorttype = sorttype;
	}
	public boolean isResizable() {
		return resizable;
	}
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
	
	
}
