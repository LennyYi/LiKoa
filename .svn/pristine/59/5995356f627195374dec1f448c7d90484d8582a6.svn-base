package com.aiait.eflow.util;

import java.util.Collection;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.SystemFieldHelper;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.wkf.vo.WorkFlowItemVO;

public class NodePropertyPageUtil {
	
	/**
	 * 
	 * @param sectionList
	 * @param node
	 * @param nodeFieldChangeType  字段值的变更方式：“update”----在该节点可以修改（即申请时也可以输入）；“new”-----只在该节点允许输入（即申请时不能输入）
	 * @return
	 */
	public static String printFieldList(Collection sectionList,WorkFlowItemVO node,String nodeFieldChangeType){
		StringBuffer resultStr = new StringBuffer("");
		String fieldIdName = "";
		if(sectionList==null || sectionList.size()==0){
			resultStr.append("&nbsp;&nbsp;&nbsp;&nbsp;<br>No fields!");
		  }else{
			  Iterator sectionIt = sectionList.iterator();
			  String tempChangeSectionFields = "";
			  if(CommonName.NODE_FIELD_CHANGE_TYPE_NEW.equals(nodeFieldChangeType)){
			    if(node!=null && node.getFillSectionFields()!=null && !"".equals(node.getFillSectionFields())){
			    	tempChangeSectionFields = node.getFillSectionFields()+",";
			    }
			    fieldIdName = "newSectionFieldId";
			  }else if(CommonName.NODE_FIELD_CHANGE_TYPE_UPDATE.equals(nodeFieldChangeType)){
				  if(node!=null && node.getUpdateSections()!=null && !"".equals(node.getUpdateSections())){
					  tempChangeSectionFields = node.getUpdateSections()+",";
				  }
				  fieldIdName = "updateSectionFieldId";
			  }else if("Hidden".equals(nodeFieldChangeType)){
				  if(node!=null && node.getHiddenSectionFields()!=null && !"".equals(node.getHiddenSectionFields())){
					  tempChangeSectionFields = node.getHiddenSectionFields()+",";
				  }
				  fieldIdName = "hiddenSectionFieldId";
			  }else{
				  return "";
			  }
			  resultStr.append("<table border='0' width='100%'>");
			 // System.out.println("------tempFillSectionFields="+tempFillSectionFields);
			  while(sectionIt.hasNext()){
				  FormSectionVO section = (FormSectionVO)sectionIt.next();
				  resultStr.append("<tr><td colspan='2'><b>"+section.getSectionId()+"."+section.getSectionRemark()+"</b></td></tr>");
				  Collection fieldList = section.getFieldList();
				  Iterator fieldIt = fieldList.iterator();
				  //out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
				  int i = 1;
				  String disable = "";
				  while(fieldIt.hasNext()){
					  FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
					  //如果是系统定义的字段“ID”则跳过不显示该字段
					  if(field.getFieldId().toUpperCase().equals("ID")){
						  continue;
					  }
                      //如果是系统定义的字段“request_no”“request_date”“submitted_by”“request_staff_code”“team_code”则不允许设置
					  if(SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE).getFieldId().equals(field.getFieldId())
			  	    			|| (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE).getFieldId().equals(field.getFieldId()))
			  	    			|| CommonName.SYSTEM_ID_REQUEST_NO.equals(field.getFieldId())
			  	    			|| CommonName.SYSTEM_ID_REQUEST_DATE.equals(field.getFieldId())
			  	    			|| CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE.equals(field.getFieldId())){
						  disable = " disabled='true' ";
					  }
					  if(i%2==1){
						  resultStr.append("<tr>");
					  }
					  resultStr.append("<td align='left'>");
					 
					  //if(tempChangeSectionFields!=null && (tempChangeSectionFields.indexOf(section.getSectionId()+"."+field.getFieldId()+",")>-1 || tempChangeSectionFields.indexOf(","+section.getSectionId()+"."+field.getFieldId())>-1)){
					  if(tempChangeSectionFields!=null && (tempChangeSectionFields.indexOf(section.getSectionId()+"."+field.getFieldId()+",")>-1 )){
						  resultStr.append("&nbsp;&nbsp;<input type='checkbox' checked name='"+fieldIdName+"' value='"+section.getSectionId()+"."+
						        field.getFieldId()+"' checked>"+field.getFieldLabel()+"");
					  }else{
                       ////attachment can not be updated
					     if(!"00".equals(section.getSectionType())){
					    	 resultStr.append("&nbsp;&nbsp;<input type='checkbox' name='"+fieldIdName+"' "+disable+" value='"+section.getSectionId()+"."+
								        field.getFieldId()+"'>"+field.getFieldLabel()+"");
					     }
					  }
					  resultStr.append("</td>");
					  if(i%2==0){
						  resultStr.append("</tr>");
					  }
					  disable = "";
					  i++;
				  }//end while
			   //out.print("<br>");
			  }//end while
			  resultStr.append("</table>");
		  }//end if
		return resultStr.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
