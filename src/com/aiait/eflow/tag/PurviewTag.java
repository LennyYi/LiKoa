package com.aiait.eflow.tag;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;

public class PurviewTag extends TagSupport {
	private int moduleId; //function module id , come from table "tts_module" 
	private int operateId;
	//funtion operation id, come from table "tts_module_operate"

	private boolean isButton = true;
	// if the control field is Button ,then it value is True; else is False
	
	private String buttonWidth = "";
	
	private boolean isUrl = false;
	// if the control fied is URL, then it value is True; else is False

	private boolean changeImage = false;
	//if the control is Button,also the Button's status changed need to change image,
	//then it value is True; else is False;
	private String imageName;
	// if the changeImage is True,the imageName need to hava value

	private String labelValue = ""; // the button to show value

	private String controlName;
	

	/**
	 * @return
	 */
	public String getControlName() {
		return controlName;
	}

	/**
	 * @return
	 */
	public boolean isChangeImage() {
		return changeImage;
	}

	/**
	 * @return
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @return
	 */
	public boolean getIsButton() {
		return isButton;
	}

	/**
	 * @return
	 */
	public boolean isUrl() {
		return isUrl;
	}

	/**
	 * @return
	 */
	public String getLabelValue() {
		return labelValue;
	}

	/**
	 * @return
	 */
	public int getModuleId() {
		return moduleId;
	}

	/**
	 * @return
	 */
	public int getOperateId() {
		return operateId;
	}

	/**
	 * @param string
	 */
	public void setControlName(String string) {
		controlName = string;
	}

	/**
	 * @param b
	 */
	public void setChangeImage(boolean b) {
		changeImage = b;
	}

	/**
	 * @param string
	 */
	public void setImageName(String string) {
		imageName = string;
	}

	/**
	 * @param b
	 */
	public void setIsButton(boolean b) {
		isButton = b;
	}

	/**
	 * @param b
	 */
	public void setUrl(boolean b) {
		isUrl = b;
	}

	/**
	 * @param string
	 */
	public void setLabelValue(String string) {
		labelValue = string;
	}

	/**
	 * @param i
	 */
	public void setModuleId(int i) {
		moduleId = i;
	}

	/**
	 * @param i
	 */
	public void setOperateId(int i) {
		operateId = i;
	}
	
	public int doStartTag() throws JspException{
		int processBodyOrNot = SKIP_BODY;
		HttpSession session = pageContext.getSession();
        
		StaffVO staff = (StaffVO)session.getAttribute(CommonName.CURRENT_STAFF_INFOR);
		
		String currentRole = staff.getCurrentRoleId();

		AuthorityHelper authority = AuthorityHelper.getInstance();
        
		if(authority.checkAuthority(currentRole,this.getModuleId(),this.getOperateId())){
			processBodyOrNot = EVAL_BODY_INCLUDE;
		}else{
			JspWriter out = pageContext.getOut();
			try{
			if(this.getLabelValue()!=null){

			  if(this.getIsButton()){

			  	 if(this.getControlName()!=null){
			  		if(!"".equals(this.getButtonWidth())){ 
					  out.print("<input type='button' class=btn3_mouseout  name='" +
											this.getControlName() + "' disabled value='" +
											this.getLabelValue() + "' style='width:"+this.getButtonWidth()+"'>");
			  		}else{
			  		  out.print("<input type='button'  class=btn3_mouseout name='" +
								this.getControlName() + "' disabled value='" +
								this.getLabelValue() + "'>");
			  		}
			  	 }else{
			  		if(!"".equals(this.getButtonWidth())){  
					  out.print("<input type='button' class=btn3_mouseout  name='button" +
										   this.getOperateId() + "' disabled value='" +
										   this.getLabelValue() + "' style='width:"+this.getButtonWidth()+"'>");
			  		}else{
					  out.print("<input type='button' class=btn3_mouseout  name='button" +
								   this.getOperateId() + "' disabled value='" +
								   this.getLabelValue() + "'>");
			  		}
			  	 }
			  }else{
				out.print(this.getLabelValue());
			  }
			}else{
			  processBodyOrNot = SKIP_BODY;
			}
			}catch(IOException e){
			  e.printStackTrace();
			}
		}
		
		return processBodyOrNot;
	}

	public String getButtonWidth() {
		return buttonWidth;
	}

	public void setButtonWidth(String buttonWidth) {
		this.buttonWidth = buttonWidth;
	}
}
