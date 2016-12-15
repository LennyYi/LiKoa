package com.aiait.eflow.housekeeping.vo;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	For Eflow Project Management      */
/******************************************************************/
import com.aiait.framework.vo.BaseVO;

public class ProjectVO extends BaseVO {
	protected String Prj_code;	
	protected String Prj_type;	
	protected String Prj_name;	
	protected String Prj_desc;	
	protected String Prj_ld_id;
	protected String Prj_ld_name;
	protected String Prj_start_date;	
	protected String Prj_update_date;	
	protected String Prj_update_user;
	protected String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrj_code() {
		return Prj_code;
	}
	public void setPrj_code(String prj_code) {
		Prj_code = prj_code;
	}
	public String getPrj_desc() {
		return Prj_desc;
	}
	public void setPrj_desc(String prj_desc) {
		Prj_desc = prj_desc;
	}
	public String getPrj_ld_id() {
		return Prj_ld_id;
	}
	public void setPrj_ld_id(String prj_ld_id) {
		Prj_ld_id = prj_ld_id;
	}
	public String getPrj_name() {
		return Prj_name;
	}
	public void setPrj_name(String prj_name) {
		Prj_name = prj_name;
	}
	public String getPrj_start_date() {
		return Prj_start_date;
	}
	public void setPrj_start_date(String prj_start_date) {
		Prj_start_date = prj_start_date;
	}
	public String getPrj_type() {
		return Prj_type;
	}
	public void setPrj_type(String prj_type) {
		Prj_type = prj_type;
	}
	public String getPrj_update_date() {
		return Prj_update_date;
	}
	public void setPrj_update_date(String prj_update_date) {
		Prj_update_date = prj_update_date;
	}
	public String getPrj_update_user() {
		return Prj_update_user;
	}
	public void setPrj_update_user(String prj_update_user) {
		Prj_update_user = prj_update_user;
	}
	public String getPrj_ld_name() {
		return Prj_ld_name;
	}
	public void setPrj_ld_name(String prj_ld_name) {
		Prj_ld_name = prj_ld_name;
	}	

}
