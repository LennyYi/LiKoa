package com.aiait.eflow.common.helper;
/***********************************************************/
/*Task_ID	Author	Modify_Date	Description                */
/*IT0958	Young	10/23/2007	Add Comments Type of Field */
/*IT0973	Young	12/28/2007	Add function for Reference form */
/*IT1002	Robin	04/11/2008	OT Form 
 *IT1002	Young	04/11/2008	OT Form */
/***********************************************************/
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.text.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.formmanage.vo.DictionaryDataVO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.HtmlUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.eflow.common.MoneyCapital;
import com.aiait.eflow.housekeeping.dao.SystemFieldDAO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;


public class FieldControlHelper {
	
	private static String serverUrl = "";
	private static String currentNodeId = "";
	private static String currentStaffCode = "";
	
	public static String getCurrentStaffCode() {
		return currentStaffCode;
	}

	public static void setCurrentStaffCode(String currentStaffCode) {
		FieldControlHelper.currentStaffCode = currentStaffCode;
	}

	public static String getCurrentNodeId() {
		return currentNodeId;
	}

	public static void setCurrentNodeId(String currentNodeId) {
		FieldControlHelper.currentNodeId = currentNodeId;
	}

	public static String getServerUrl() {
		return serverUrl;
	}
    
	public static void setServerUrl(String serverUrl) {
		FieldControlHelper.serverUrl = serverUrl;
	}

	public static String conrolIsRequiredFlag(boolean requrired){
		if(requrired){
		  return "(<font color='red'><b>*</b></font>)";
		}else{
		  return "";
		}
	}
		
    
	public static String createBaseInforControl(boolean needDisplayValue,FormSectionFieldVO field,FormManageVO form,HttpServletRequest request)throws IOException,Exception{
		StringBuffer resultStr = new StringBuffer("");
		int a = field.getFieldType();
		String required = " required='false' ";
		if(field.getIsRequired()){
			required = " required='true' ";
		}
		
	   String value = "";
	   String label = "";
	   String disabled = "";
	   //IT1002
	   if(!"0".equals(form.getStatus())){ //not published, it is designing		   
		   resultStr.append(createControl(field,"03",1)); //sectionType=03 base information
		   return resultStr.toString();
	   }
	   if(field.getFieldType()!=CommonName.FIELD_TYPE_BASIC){		   
		   resultStr.append(createControl(field,"03",1)); //sectionType=03 base information
		   return resultStr.toString();
	   }
  	   //this form had been published    
	   if(CommonName.SYSTEM_ID_REQUEST_DATE.equals(field.getFieldId())){
  		    value = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
  		    label = value;
  		    disabled = "disabled";
  	   }
   	   if(CommonName.SYSTEM_ID_REQUEST_NO.equals(field.getFieldId())){
	        value = FieldUtil.getRequestNo(field.getFormSystemId(),form.getFormId(),field.getSectionId());
	        label = value;
	        disabled = "disabled";
	   }
       if(CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE.equals(field.getFieldId())){
 	         StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
 	         value = staff.getStaffCode();
 	         label = staff.getStaffName().trim()+"("+staff.getLogonId()+")";
 	         disabled = "disabled";
       }
       if(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE.equals(field.getFieldId())){
 	      StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
 	      resultStr.append(HtmlUtil.createRequestTeamSelect(staff.getTeamCode(),field));
 	      disabled = "";
       }  	
       if(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE.equals(field.getFieldId())){
   	         StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
   	         disabled = "";
   	         resultStr.append(HtmlUtil.createRequestStaffSelect(staff.getStaffCode(),staff.getTeamCode(),field));
       }
       if(!"".equals(disabled)){
 	       if(needDisplayValue==false){
 	    	  resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value='" + value + "'>");
 	    	  resultStr.append("<input type='text' "+ disabled +" name='"+field.getFieldId()+"' size='"+field.getControlsWidth() + "' title='"+field.getFieldLabel()+"'");
 	    	  resultStr.append(" value='' "+required +">"+ conrolIsRequiredFlag(field.getIsRequired()));
 	       }else{
 	    	  resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value='" + value + "'>");
 	    	  resultStr.append("<input type='text' "+ disabled +" name='"+field.getFieldId()+"' size='"+field.getControlsWidth() + "' title='"+field.getFieldLabel()+"'");
 	    	  resultStr.append(" value=\""+label+"\" "+required +">"+ conrolIsRequiredFlag(field.getIsRequired()));
 	       }
 	   }
       return resultStr.toString();
	}
	
	public static String createControl(FormSectionFieldVO field,String sectionType,int rowIndex)throws IOException,Exception{
		StringBuffer resultStr = new StringBuffer("");
		int a = field.getFieldType();
		String required = " required='false' ";
		if(field.getIsRequired()){
			required = " required='true' ";
		}
		String title = "";
		if(field.getFieldComments()!=null && !"".equals(field.getFieldComments())){
			title = " tooltipText='"+field.getFieldComments()+"' ";
		}
   	    switch (a){
   	    case CommonName.FIELD_TYPE_TEXT://单文本框
     	   //out.println("<input type='text' name='"+field.getFieldId()+"' maxlength='"+field.getFieldLength()+"' size='"+field.getControlsWidth() +"' " + title);
     	   //out.print(required +" title='"+field.getFieldLabel()+"'>"+ conrolIsRequiredFlag(field.getIsRequired()));     	 
   	    	resultStr.append("<textarea class='input' type='text' name='"+field.getFieldId()+"' maxLength='"+field.getFieldLength()+"' onkeydown=\"textCounterForInput(this,"+field.getFieldLength()+",event)\" " + title);
   	    	resultStr.append(required +" style=\"width:130;overflow-x:visible;overflow-y:visible;\" title='"+field.getFieldLabel()+"'></textarea>"+ conrolIsRequiredFlag(field.getIsRequired()));
   		  break;
   	    case CommonName.FIELD_TYPE_TEXTAREA://2多文本框
   	    	resultStr.append("<textarea name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' rows='"+field.getControlsHeight() +"' cols='"+field.getControlsWidth() +"' maxLength='"+field.getFieldLength()+"' " + title + " onKeyDown=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"+field.getFieldLength()+")\" onKeyUp=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"+field.getFieldLength()+")\" " + required +
   	      		" style='border: 1px solid #CDCDCD;width:100%;'></textarea>"+conrolIsRequiredFlag(field.getIsRequired()));
   	      break;
   	    case CommonName.FIELD_TYPE_DATE://3日期选择框
   	    	resultStr.append("<input type='text' isDate='true' "+title+" size='"+field.getControlsWidth() +"' name='"+field.getFieldId()+"'  onclick='setday(this)' ");
   	    	resultStr.append(required +">(MM/DD/YYYY)"+ conrolIsRequiredFlag(field.getIsRequired()));  
     	      break;
   	    case CommonName.FIELD_TYPE_SELECT://4下拉选择框
   	      resultStr.append("<select name='"+field.getFieldId()+"' " +title + " "+ required +" >");
    	  BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
          Collection selectOptionList = (ArrayList)dataHelper1.getDetailMap().get(field.getFormSystemId()+"&"+field.getSectionId()+"&"+field.getFieldId());
    	  if (selectOptionList!=null && selectOptionList.size()>0){
    	    	Iterator opIt = selectOptionList.iterator();
          	    while(opIt.hasNext()){
          		 DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
          		 resultStr.append("<option value='"+vo.getId()+"'>"+vo.getValue()+"</option>");
          	    }
    	  }else{
    		  resultStr.append("<option value=''>     </option>");
    	  }
    	  resultStr.append("</select>" + conrolIsRequiredFlag(field.getIsRequired()));
       	  break;
   	   case CommonName.FIELD_TYPE_NUMBER: //5 Number
   		  resultStr.append("<input type='text' isNumber='true' size='"+field.getControlsWidth() +"'  name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' " + title);
   		  resultStr.append(" "+required +"onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"+">"+ conrolIsRequiredFlag(field.getIsRequired())); 
   	      break; 	
   	   case CommonName.FIELD_TYPE_CHECKBOX:  //6 checkbox
   	     BaseDataHelper dataHelper = BaseDataHelper.getInstance();
         Collection optionList = (ArrayList)dataHelper.getDetailMap().get(field.getFormSystemId()+"&"+field.getSectionId()+"&"+field.getFieldId());
	      if (optionList!=null && optionList.size()>0){
	    	Iterator opIt = optionList.iterator();
      	      while(opIt.hasNext()){
      		   DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
      		    if("01".equals(sectionType)){ //sectionType = 01  table
      		    	if(rowIndex!=0){
      		    		resultStr.append("<input type='checkbox' "+required+" " +title +" name='"+field.getFieldId()+"_"+rowIndex+"' title='"+field.getFieldLabel()+"' value='"+vo.getId()+"'>"+vo.getValue()+"&nbsp;&nbsp;");	
      		    	}else{
      		    		resultStr.append("<input type='checkbox' "+required+" " +title +" name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' value='"+vo.getId()+"'>"+vo.getValue()+"&nbsp;&nbsp;");
      		    	}
      		    }else{
      		    	resultStr.append("<input type='checkbox' "+required+" " +title +" name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' value='"+vo.getId()+"'>"+vo.getValue()+"&nbsp;&nbsp;");
      		    }
      	      }
      	     resultStr.append(conrolIsRequiredFlag(field.getIsRequired()));
	     }
	      break;
   	   case CommonName.FIELD_TYPE_SYSTEM: //7 SystemField   		  
		   //原来用ColumnType来区分SystemField是否带参数
   		   //现在通过SystemFieldHelper容器是否返回空来区分带参SystemField
   		   FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(field.getFieldId());   		   
   		   if ("02".equals(vo.getSystemFieldType())){
   			   if(vo.getColumnType()==3){
   				  resultStr.append("");
   			   }else{
   			     Collection sysOpList = vo.getOptionList();
   			     resultStr.append("<select name='"  + field.getFieldId()+"' " + required +" title='"+field.getFieldLabel()+"'>");
			     if(sysOpList!=null && sysOpList.size()>0){
				   Iterator it = sysOpList.iterator();
				   while(it.hasNext()){
					   DictionaryDataVO op = (DictionaryDataVO)it.next();
					   resultStr.append("<option value='"+op.getId()+"'>"+op.getValue()+"</option>");
				   }
			     }
			     resultStr.append("</select>"+ conrolIsRequiredFlag(field.getIsRequired()));
   			   }
		   }else if("03".equals(vo.getSystemFieldType())){ //reference form
			   resultStr.append("<div id='divrefeformId'></div>");	
			   resultStr.append("<input type='button' name='addRefBtn' value='Add Reference' onclick='showRefFormWindow()' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">");
		   }else if("04".equals(vo.getSystemFieldType())){
			   resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value=''>");
		   }else if("06".equals(vo.getSystemFieldType())){
			   resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value=''>");
		   }else if("01".equals(vo.getSystemFieldType())){//common data
			   if(vo.getColumnType()==3){ //numeric
				   resultStr.append("<input type='text' isNumber='true' size='"+field.getControlsWidth() +"'  name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' " + title);
			       resultStr.append(" "+required +"onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"+">"+ conrolIsRequiredFlag(field.getIsRequired()));
			   }else if (vo.getColumnType()==2){//date
				   resultStr.append("<input type='text' isDate='true' "+title+" title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' name='"+field.getFieldId()+"'  onclick='setday(this)' ");
	   	    	   resultStr.append(required +">(MM/DD/YYYY)"+ conrolIsRequiredFlag(field.getIsRequired()));
			   }else{
				   resultStr.append("<textarea class='input' type='text' name='"+field.getFieldId()+"' maxLength='"+field.getFieldLength()+"' onkeydown=\"textCounterForInput(this,"+field.getFieldLength()+",event)\" " + title);
		   	       resultStr.append(required +" style=\"width:130;overflow-x:visible;overflow-y:visible;\" title='"+field.getFieldLabel()+"'></textarea>"+ conrolIsRequiredFlag(field.getIsRequired()));
			   }
		   }
   		   break;
      //IT0958 begin
   	   case CommonName.FIELD_TYPE_COMMENTS:
   		   resultStr.append(field.getCommentContent());
   		   break;
   	  //It0958 end 
   	    default:
   	      resultStr.append("<input type='text' name='"+field.getFieldId()+"' maxlength='"+field.getFieldLength()+"' title='"+field.getFieldLabel()+"' " +title+" ");
   	      resultStr.append(required +">"+ conrolIsRequiredFlag(field.getIsRequired()));  
   		  break;
   	  }
   	  return resultStr.toString();
	}
	
	
	public static String displayFieldInfo(String sectionType,FormSectionFieldVO field,HashMap fieldContentMap,String status)throws Exception{
		return displayFieldInfo(sectionType,field,fieldContentMap,status,"00");
	}
	
	/**
	 * 
	 * @param sectionType
	 * @param field
	 * @param fieldContentMap
	 * @param status
	 * @param type (输出方式)----00输出完整的部分，01---只需要输出该字段的值
	 * @return
	 * @throws Exception
	 */
	public static String displayFieldInfo(String sectionType,FormSectionFieldVO field,HashMap fieldContentMap,String status,String type)throws Exception{
		StringBuffer resultStr = new StringBuffer(""); 
		int a = field.getFieldType();
		String fieldValue = (String)fieldContentMap.get(field.getFieldId().toUpperCase());

		if(fieldValue==null){
			fieldValue = "";
		}
		String required = " required='false' ";
		if(field.getIsRequired()){
			required = " required='true' ";
		}
//		IT1002
       	if("03".equals(sectionType)){//baseinformation for requestor
       		 String tempValue = "";
  	    	 if(SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE).getFieldId().equals(field.getFieldId())){
  	    		tempValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue);
  	    	 }else if(SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE).getFieldId().equals(field.getFieldId())){
  	    		 tempValue = StaffTeamHelper.getInstance().getTeamNameByCode(fieldValue);
  	    	 }else if(SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE).getFieldId().equals(field.getFieldId())){
  	    		 tempValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue);
  	    	 }else if(CommonName.SYSTEM_ID_PROJ_LD_ID.equals(field.getFieldId())){
  	    		tempValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue.trim());
  	    	 }else if(CommonName.SYSTEM_ID_DB_OWNER.equals(field.getFieldId()) || CommonName.SYSTEM_ID_RESOURCE_OWNER.equals(field.getFieldId())){
  	    		tempValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue.trim());
  	    	 }else{
  	    		tempValue = fieldValue;
  	    	 }
 
  	    	//if is date,then format the date
  	    	if(field.getFieldType()==CommonName.FIELD_TYPE_DATE && fieldValue!=null && !"".equals(fieldValue)){
  	    		SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
                java.util.Date   cDate   =   df.parse(fieldValue);  
                tempValue = StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss");
  	    	}
  	    	//if the field isn't the requestor baseinformation, then it can edit
  	    	//处于“初始化（00）”状态,处于“03(reject)”状态的form可以进行form内容编辑（修改）操作
  	    	if(SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE).getFieldId().equals(field.getFieldId())
  	    			|| (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE).getFieldId().equals(field.getFieldId()))
  	    			|| CommonName.SYSTEM_ID_REQUEST_NO.equals(field.getFieldId())
  	    			|| CommonName.SYSTEM_ID_REQUEST_DATE.equals(field.getFieldId())
  	    			|| CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE.equals(field.getFieldId())){
  	  	    	  //out.println("<input type='text' name='"+field.getFieldId()+"1' disabled  title='"+field.getFieldLabel()+"' value='" + tempValue + "'");
  	  	    	  //out.print(required +">"+ conrolIsRequiredFlag(field.getIsRequired()));
  	    	      if("00".equals(type)){//输出完整的部分
  	    		    //如果是requestStaffCode和team则可以修改
  	    	        if(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE.equals(field.getFieldId()) && ("00".equals(status) || "03".equals(status))){
  	    	        	resultStr.append(HtmlUtil.createRequestTeamSelect(fieldValue,field));
  	    	        }else if(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE.equals(field.getFieldId()) && ("00".equals(status) || "03".equals(status))){
  	    	        	resultStr.append(HtmlUtil.createRequestStaffSelect(fieldValue,"",field));
  	    	        }else{
  	    	    	  resultStr.append(tempValue + " " + conrolIsRequiredFlag(field.getIsRequired()));
  	    		      resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value='" + fieldValue + "'");
  	    	        }
  	    	      }else{
  	    	    	resultStr.append(tempValue);  
  	    	      }
  	    	}else{
  	    		if("00".equals(status) || "03".equals(status)){
  	    		  if(CommonName.SYSTEM_ID_DB_OWNER.equals(field.getFieldId()) || CommonName.SYSTEM_ID_DB_OWNER.equals(field.getFieldId())){
  	    			FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(field.getFieldId());
  	       		    Collection sysOpList = vo.getOptionList();
  	       		    if("00".equals(type)){
  	       		      resultStr.append("<select name='"  + field.getFieldId()+"' " + required +" >");
  	       		      if(sysOpList!=null && sysOpList.size()>0){
  	       			    Iterator it = sysOpList.iterator();
  	       			    boolean selected = false;
  	       			    while(it.hasNext()){
  	       				  DictionaryDataVO op = (DictionaryDataVO)it.next();
  	       			      resultStr.append("<option value='"+op.getId()+"' ");
  	       				  if(fieldValue.equals(op.getId())){
  	       				     resultStr.append(" selected");
  	       				     selected = true;
  	       				  }
  	       			      resultStr.append(">"+op.getValue()+"</option>");
  	       			    }
  	       			    //如果没有选择的值，则显示为空
  	       			    if(selected==false){
  	       			    	resultStr.append("<option value='' selected> </option>");
  	       			    }
  	       		       }
  	       		       resultStr.append("</select>"+ conrolIsRequiredFlag(field.getIsRequired()));
  	       		    }else{
  	       		       if(sysOpList!=null && sysOpList.size()>0){
  	       		    	 Iterator it = sysOpList.iterator();
  	       		         while(it.hasNext()){
  	       		           DictionaryDataVO op = (DictionaryDataVO)it.next();
  	       		           if(fieldValue.equals(op.getId())){
  	       		        	tempValue = op.getValue();
  	       		        	break;
  	       		           }
  	       		         }
  	       		         if("".equals(tempValue)){
  	       		           tempValue = fieldValue;
  	       		         }
  	       		       }
  	       		       resultStr.append(tempValue);  
    	    	    }
  	    		  }else{
  	    			resultStr.append(printOutField(a,status,sectionType,field,fieldContentMap,type));
  	    		  }
  	    		}else{
  	    		  tempValue = DataConvertUtil.convertISOToGBK(tempValue);
  	    	      //out.println("<input type='text' name='"+field.getFieldId()+"1' disabled  title='"+field.getFieldLabel()+"' value='" + tempValue + "'");
  	    	      //out.print(required +">"+ conrolIsRequiredFlag(field.getIsRequired()));
  	    		  if("00".equals(type)){
  	    		     //resultStr.append(tempValue + " " + conrolIsRequiredFlag(field.getIsRequired()));
  	    			resultStr.append(printOutField(a,status,sectionType,field,fieldContentMap,type));
  	    		     resultStr.append("<input type='hidden' name='"+field.getFieldId()+"' value='" + fieldValue + "'");
  	    		  }else{
  	    	    	 //resultStr.append(tempValue);
  	    			 resultStr.append(printOutField(a,status,sectionType,field,fieldContentMap,type));
  	    	      }
  	    		}
  	    	}
       		return resultStr.toString();
       	} 
       	if("00".equals(sectionType)){//attached file
    		if(fieldValue!=null && !"".equals(fieldValue)){
    			fieldValue = DataConvertUtil.convertISOToGBK(fieldValue);
    		}
    		if("00".equals(type)){
       		  if("file_name".equals(field.getFieldId())){
       			resultStr.append("<a href='javascript:openFile(\""+fieldValue+"\")'>"+fieldValue+"</a>");
       		  }else{
       			resultStr.append(fieldValue);
       		  }
    		}else{
    			resultStr.append(fieldValue);
    		}
       		if("00".equals(status) || "03".equals(status)){
       			
       		}
       		return resultStr.toString();
       	}
       	
      return printOutField(a,status,sectionType,field,fieldContentMap,type);
	 // return true;
	}
	
	private static String printOutField(int fieldType,String status,String sectionType,FormSectionFieldVO field,HashMap fieldContentMap,String type)throws Exception{
		return printOutField(fieldType,status,sectionType,field,fieldContentMap,type,-1);
	}
	
	public static String printOutField(int fieldType,String status,String sectionType,FormSectionFieldVO field,HashMap fieldContentMap,String type,int rowIndex)throws Exception{
		StringBuffer str = new StringBuffer("");
		
		String fieldValue = (String)fieldContentMap.get(field.getFieldId().toUpperCase());
		fieldValue = DataConvertUtil.convertISOToGBK(fieldValue);
		
		fieldValue = StringUtil.htmlEncoder(fieldValue);
		
		//fieldValue = StringUtil.replace(fieldValue,"'","\'");
		if(fieldValue==null){
			fieldValue = "";
		}
		String title = "";
		if(field.getFieldComments()!=null && !"".equals(field.getFieldComments())){
			title = " tooltipText='"+field.getFieldComments()+"' ";
		} 
		String required = " required='false' ";
		if(field.getIsRequired()){
			required = " required='true' ";
		}
     	 String disableStr = "disabled";
       	 if("00".equals(status)  || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
       			disableStr = "";
       	  }
       	if("01".equals(type)){
       		required = "";
       	}
		switch (fieldType){
   	    case 1://单文本框 (处于“03(reject)”状态下的也可以编辑，因为又回到初始状态)
   	     if("01".equals(type)){
   	    	 str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   	     }else{
   	       if("00".equals(status) || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
   	    	 if(fieldValue==null || "".equals(fieldValue)){
   	    		//str.append("<input type='text' name='"+field.getFieldId()+"' "+title+" value='" + fieldValue + "'  ");
   	    		str.append("<textarea class='input' type='text' name='"+field.getFieldId()+"' maxLength='"+field.getFieldLength()+"' onkeydown=\"textCounterForInput(this,"+field.getFieldLength()+",event)\" " + title);
   	    		str.append(required +" style=\"width:130;overflow-x:visible;overflow-y:visible;\" title='"+field.getFieldLabel()+"'></textarea>"+ conrolIsRequiredFlag(field.getIsRequired()));
   	    	 }else{
   	    		fieldValue = StringUtil.replace(fieldValue,"'","&#39;");
   	    		//str.append("<input type='text' name='"+field.getFieldId()+"'  "+title+" value='" + fieldValue + "' ");
   	    		str.append("<textarea class='input' type='text' name='"+field.getFieldId()+"' maxLength='"+field.getFieldLength()+"' onkeydown=\"textCounterForInput(this,"+field.getFieldLength()+",event)\" " + title);
   	    		str.append(required +" style=\"width:150;overflow-x:visible;overflow-y:visible;\" title='"+field.getFieldLabel()+"'>"+fieldValue+"</textarea>"+ conrolIsRequiredFlag(field.getIsRequired()));
   	    	 }
   	    	//str.append(required + " title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' maxLength='"+field.getFieldLength()+"' " +">"+ conrolIsRequiredFlag(field.getIsRequired()));
   	       }else{
   	    	str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   	       }
   	     }
   		 break;
   	    case 2://2多文本框
      	     if("01".equals(type)){
       	    	 str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
       	     }else{
   	    	   if("00".equals(status) || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
   	    	    	str.append("<textarea name='"+field.getFieldId()+"' title='"+field.getFieldLabel()+"' "+title+" rows='"+field.getControlsHeight() +"' cols='"+field.getControlsWidth() +"' maxLength='"+field.getFieldLength()+"' "+title+" onKeyDown=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"+field.getFieldLength()+")\" onKeyUp=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"+field.getFieldLength()+")\" style='border: 1px solid #e4e4e4;width:100%;' " + required + ">"
   	    	      		+fieldValue+"</textarea>"+conrolIsRequiredFlag(field.getIsRequired()));
   	    	   }else{
   	    	    	str.append(StringUtil.FormatHTMLEnter(fieldValue)).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   	    	   }
       	     }
   	      break;
   	    case 3://3日期选择框
   	    	//format the date
   	    	if(fieldValue!=null && !"".equals(fieldValue)){
   	    	  SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd");   
              java.util.Date   cDate   =   df.parse(fieldValue);  
   	    	   fieldValue = StringUtil.getDateStr(cDate,"MM/dd/yyyy");
   	    	}
      	     if("01".equals(type)){
       	    	 str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
       	     }else{
   	    	   if("00".equals(status) || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
   	    		 str.append("<input type='text' isDate='true' "+title+" title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' "+title+" name='"+field.getFieldId()+"' style='width:100' onclick='setday(this)' ");
   	    		 str.append(required + " value='" + fieldValue +"'>(MM/DD/YYYY)"+ conrolIsRequiredFlag(field.getIsRequired()));  
   	    	   }else{
   	    		 str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   	    	   }
       	     }
     	     break;
   	    case 4://4下拉选择框
   	       BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
           Collection selectOptionList = (ArrayList)dataHelper1.getDetailMap().get(field.getFormSystemId()+"&"+field.getSectionId()+"&"+field.getFieldId());
      	   if("01".equals(type)){
      		 if (selectOptionList!=null && selectOptionList.size()>0){
     	    	Iterator opIt = selectOptionList.iterator();
           	    while(opIt.hasNext()){
           		 DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
           		 if(fieldValue.equals(vo.getId())){
           			str.append(vo.getValue()).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
           			break;
           		 }
           	    }
     	     }else{
     	    	str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
     	     }
       	   }else{   	    	
       		 if(!"".equals(disableStr)){
       			 str.append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
       		 }
   	         str.append("<select name='"+field.getFieldId()+"' "+required+" "+title+"  "+disableStr+">");
    	     if (selectOptionList!=null && selectOptionList.size()>0){
    	    	Iterator opIt = selectOptionList.iterator();
    	    	boolean selected = false;
          	    while(opIt.hasNext()){
          		 DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
          		 if(fieldValue.equals(vo.getId())){
          			str.append("<option value='"+vo.getId()+"' selected>"+vo.getValue()+"</option>");
          			selected = true;
          		 }else{
          			str.append("<option value='"+vo.getId()+"'>"+vo.getValue()+"</option>"); 
          		 }
          	    }
     			//如果没有选择的值，则显示为空
     			if(selected==false){
     			   str.append("<option value='' selected> </option>");
     			}
    	     }else{
    		    str.append("<option value=''>  </option>");
    	     }
    	     str.append("</select>");
       	   }
       	  break;
   	   case 5: //5 Number
   		//Formating the type of Number
   		String	formattedNum = "";   		
   		if(fieldValue!=null && !"".equals(fieldValue)){
   		   formattedNum = HandlingNumber(fieldValue,field);
   		}
   		if("01".equals(type)){
   			str.append(formattedNum).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   		}else{
   		  if("00".equals(status)  || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
   		    if(fieldValue==null || "".equals(fieldValue)){
   			  str.append("<input type='text' isNumber='true'  name='"+field.getFieldId()+"'  "+title+" value='" + fieldValue + "' ");
   		    }else{
   			  fieldValue = StringUtil.replace(fieldValue,"'","&#39;");
   			  //fieldValue = StringUtil.unFormatHtml(fieldValue);
   			  str.append("<input type='text' isNumber='true'  name='"+field.getFieldId()+"'  "+title+" value='" + fieldValue + "' "); 
   		    }
   		    str.append(required +" title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"+">"+ conrolIsRequiredFlag(field.getIsRequired()));
   		 }else{
   			str.append(formattedNum).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   		 }
   		}
   	      break; 	
   	   case 6:  //6 checkbox
   	     BaseDataHelper dataHelper = BaseDataHelper.getInstance();
   	     //The format of fieldValue is "02,03"
   	     //String[] fieldValueList = StringUtil.split(fieldValue,",");
         Collection optionList = (ArrayList)dataHelper.getDetailMap().get(field.getFormSystemId()+"&"+field.getSectionId()+"&"+field.getFieldId());
         if("01".equals(type)){
        	 if (optionList!=null && optionList.size()>0){
        		 Iterator opIt = optionList.iterator();
        		 while(opIt.hasNext()){
            		DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
            		if(fieldValue.indexOf(vo.getId())>-1){
            			str.append(vo.getValue()).append("  ");
           		       if(rowIndex>0){
            			     str.append("<input  type='hidden'  name='"+field.getFieldId()+"_"+rowIndex+"' value='"+vo.getId()+"'>");
            		    }else{
            		    	 str.append("<input  type='hidden'  name='"+field.getFieldId()+"' value='"+vo.getId()+"'>");  
            		   }
            		}
        		 }
        	 }else{
        		 str.append(fieldValue);
     		     if(rowIndex>0){
      			     str.append("<input  type='hidden'  name='"+field.getFieldId()+"_"+rowIndex+"' value='"+fieldValue+"'>");
      		     }else{
      		    	 str.append("<input  type='hidden'  name='"+field.getFieldId()+"' value='"+fieldValue+"'>");  
      		    }
        	 }
         }else{

           if (optionList!=null && optionList.size()>0){
	    	 Iterator opIt = optionList.iterator();
      	     while(opIt.hasNext()){
      		   DictionaryDataVO vo = (DictionaryDataVO)opIt.next();
      		     if(fieldValue.indexOf(vo.getId())>-1){
      		       if(rowIndex>0){
      			     str.append("<input "+disableStr+" "+title+" checked type='checkbox'  name='"+field.getFieldId()+"_"+rowIndex+"' value='"+vo.getId()+"'>"+vo.getValue()+" &nbsp;&nbsp;");
      		       }else{
      		    	 str.append("<input "+disableStr+" "+title+" checked type='checkbox'  name='"+field.getFieldId()+"' value='"+vo.getId()+"'>"+vo.getValue()+" &nbsp;&nbsp;");  
      		       }
      		     }else{
      		         if(rowIndex>0){
      		        	str.append("<input "+disableStr+" "+title+" type='checkbox'  name='"+field.getFieldId()+"_"+rowIndex+"' value='"+vo.getId()+"'>"+vo.getValue()+" &nbsp;&nbsp;"); 	 
      		         }else{
      			       str.append("<input "+disableStr+" "+title+" type='checkbox'  name='"+field.getFieldId()+"' value='"+vo.getId()+"'>"+vo.getValue()+" &nbsp;&nbsp;");
      		         }
      		     }
      		   if(!"".equals(disableStr)){
       		       if(rowIndex>0){
      			      str.append("<input  type='hidden'  name='"+field.getFieldId()+"_"+rowIndex+"' value='"+fieldValue+"'>");
      		       }else{
      		    	  str.append("<input  type='hidden'  name='"+field.getFieldId()+"' value='"+fieldValue+"'>");  
      		      }
      		   }
      	     }
      	     str.append(conrolIsRequiredFlag(field.getIsRequired()));
	       }
         }
	     break;
   	   case 7: //7 SystemField
   		  FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(field.getFieldId());
   		  //System.out.println(vo.getSystemFieldType()+":"+vo.getFieldId());
   		  if("02".equals(vo.getSystemFieldType())){ //System Field的fieldType=="02",不带参数的systemField
   			  Collection sysOpList = vo.getOptionList();
   			  if("01".equals(type)){
   				  if(sysOpList!=null && sysOpList.size()>0){
   					  Iterator it = sysOpList.iterator();
   					  while(it.hasNext()){
   						  DictionaryDataVO op = (DictionaryDataVO)it.next();
   						  if(fieldValue.equals(op.getId().trim())){
   							  str.append(op.getValue()).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   							  break;
   						  }
   					  }
   				  }else{
   					  str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   				  }
   			  }else{
   				  str.append("<select name='" + field.getFieldId()+"' "+required+" title='"+field.getFieldLabel()+"' "+disableStr+">");   			
   				  if(sysOpList!=null && sysOpList.size()>0){
   					  Iterator it = sysOpList.iterator();
   					  boolean selected = false;
   					  while(it.hasNext()){
   						  DictionaryDataVO op = (DictionaryDataVO)it.next();
   						  if(fieldValue.equals(op.getId())){
   							  str.append("<option value='"+op.getId()+"' selected>"+op.getValue()+"</option>");
   							  selected = true;
   						  }else{
   							  str.append("<option value='"+op.getId()+"'>"+op.getValue()+"</option>");
   						  }
   					  }
	       			    //如果没有选择的值，则显示为空
	       			    if(selected==false){
	       			    	str.append("<option value='' selected> </option>");
	       			    }
   				  }
   				  str.append("</select>"+ conrolIsRequiredFlag(field.getIsRequired()));
   				  if(!"".equals(disableStr)){
   					str.append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   				  }
   			  }
   		 }else if("03".equals(vo.getSystemFieldType())){//Reference Form 	
   			if("01".equals(type)){
   			  str.append(fieldValue.trim()).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   			}else{
   			  str.append("<div id='divrefeformId'>"+HandleRefFormField(fieldValue,field)+"</div>");
   			  if ("00".equals(status)||("03".equals(status)&&"0".equals(getCurrentNodeId()))){   				
   				str.append("<input type='button' name='addRefBtn' value='Add Reference' onclick=\"showRefFormWindow('"+field.getFormSystemId()+"','"+field.getSectionId()+"')\" class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">") ;
   			  }
   			}
   		 }else if("04".equals(vo.getSystemFieldType())){ //Label (只需要将其中的值显示出来就可以)
   			str.append(fieldValue+"<input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
   		 }else if("06".equals(vo.getSystemFieldType())){ //Supervisor's approval
			   if("01".equals(type)){
				   str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
			   }
			   else{
    	    	   if("00".equals(status) || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
    	    		   str.append(StaffTeamHelper.getInstance().getStaffByCode(FieldControlHelper.getCurrentStaffCode()).getStaffName()
    	    				   +"("+StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss")+")"
    	    				   +"<input type='hidden' name='"+field.getFieldId()+"' value='"+StaffTeamHelper.getInstance().getStaffByCode(FieldControlHelper.getCurrentStaffCode()).getStaffName()
    	    				   +"("+StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss")+")'>");
    	    	   }else{
    	    		   str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>");
    	    	   }
			   }
		   }else if("01".equals(vo.getSystemFieldType())){//common type
			   String hiddenStr = "";
			   String showStr = "";
			   if(vo.getColumnType()==2){//date
	    	    	if(fieldValue!=null && !"".equals(fieldValue)){
	     	    	   SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd");   
	                   java.util.Date   cDate   =   df.parse(fieldValue);  
	     	    	   fieldValue = StringUtil.getDateStr(cDate,"MM/dd/yyyy");
	     	    	}
	    	    	showStr = "<input type='text' isDate='true' "+title+" title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' "+title+" name='"+field.getFieldId()+"' style='width:100' onclick='setday(this)' "
	    	    	          + required + " value='" + fieldValue +"'>(MM/DD/YYYY)"+ conrolIsRequiredFlag(field.getIsRequired());
			   }else if(vo.getColumnType()==3){//numeric
				   if(fieldValue==null || "".equals(fieldValue)){
					   fieldValue = "0";
				   }
				   fieldValue = StringUtil.replace(fieldValue,"'","&#39;");
				   showStr = showStr + "<input type='text' isNumber='true'  name='"+field.getFieldId()+"'  "+title+" value='" + fieldValue + "' "; 
				   showStr = showStr + required +" title='"+field.getFieldLabel()+"' size='"+field.getControlsWidth() +"' onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"+">"+ conrolIsRequiredFlag(field.getIsRequired());
			   }
			   hiddenStr = "<input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>";
			   if("01".equals(type)){
      	    	 str.append(fieldValue).append(hiddenStr);
      	       }else{
      	    	 if("00".equals(status) || "03".equals(status)){ //处于“初始化（00）”状态的form可以进行form内容编辑（修改）操作
      	    		str.append(showStr);
      	    	 }else{
    	    		 str.append(fieldValue).append(hiddenStr);
  	    	     }
      	       }
		   }
         break;
       //IT0958 begin
   	   case CommonName.FIELD_TYPE_COMMENTS:
   		   str.append(field.getCommentContent());
   		   break;
   	   //It0958 end 
   	 default:
   	      str.append(fieldValue).append(" <input type='hidden' name='"+field.getFieldId()+"' value='"+fieldValue+"'>"); 
   		  break;
   	  }
	  return str.toString();
	}
	/**
	 * 专门处理Reference_Form System Field
	 * @return
	 */
	private static String HandleRefFormField(String strvalue,FormSectionFieldVO field){
		String[] arrValue;
		String returnstr="";
		arrValue = strvalue.split(",");
		for(int i=0; i<arrValue.length; i++){
			returnstr += "<a href=\"javascript:openCenterWindow('"+getServerUrl()+"/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo="+arrValue[i]+"&formSystemId="+field.getFormSystemId()+"',700,700)\">"+arrValue[i]+"</a>,";
		}
		returnstr = returnstr.substring(0,returnstr.length()-1);		
		returnstr+= "<input type=\"hidden\" name=\"reference_form\" value=\""+strvalue+" \">";
		return returnstr;
	}
	/**
	 * 专门用来显示disabled的field
	 * @return
	 */
	public static String ShowDisabledField(FormSectionFieldVO field){
		StringBuffer str = new StringBuffer(" ");
		str.append("<input type='hidden' name='"+field.getFieldId()+"' value=''>");
		return str.toString();
	}
	
	private static	String HandlingNumber(String str, FormSectionFieldVO field){
		
		String tmpstr,capstr,mergedstr;
		
		if (field.getIsMoney()==1){
			NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);		
			tmpstr=usFormat.format(Double.parseDouble(str));
			capstr=MoneyCapital.parseMoney(str);
			mergedstr=tmpstr+"("+capstr+")";
		}else{
			mergedstr = str;
		}			
		return   mergedstr;		
	}
}
