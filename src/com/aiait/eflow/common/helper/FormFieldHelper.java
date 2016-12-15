package com.aiait.eflow.common.helper;

import java.text.*;
import java.util.*;

import com.aiait.eflow.common.*;
import com.aiait.eflow.formmanage.vo.*;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.eflow.util.*;
import com.aiait.framework.i18n.I18NMessageHelper;

/**
 * 专门处理每个field的显示
 */

public class FormFieldHelper {
    private static String serverUrl = "";

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        FormFieldHelper.serverUrl = serverUrl;
    }

    /**
     * 
     * @param field
     * @param form
     * @param fieldContentMap
     * @param hasHiddenField
     * @return
     */
    public static String showLabelField(FormSectionFieldVO field, FormManageVO form, StaffVO staff,
    		 HashMap fieldContentMap,boolean hasHiddenField,String dateFormat, int formSystemId) {
        return showLabelField(field, form, staff, fieldContentMap, hasHiddenField, -1, null,dateFormat,formSystemId);
    }

    /**
     * 只显示该字段的值（）
     * 
     * @param field
     * @param form
     * @param fieldContentMap
     * @param hasHiddenField
     *            是否生成一个隐藏的该字段 true-----生成；false-----不生成
     * @param rowIndex
     *            所在行的序号(从0开始，0表示第一行）
     * @param displayType
     *            : null/"" - HTML, "text" - Plain Text, etc.
     * @return
     */
    public static String showLabelField(FormSectionFieldVO field, FormManageVO form, StaffVO staff, 
    		HashMap fieldContentMap, boolean hasHiddenField, int rowIndex, String displayType,String dateFormat, int formSystemId) {
    	
    	if(dateFormat==null || "".equals(dateFormat)){
    		dateFormat="MM/dd/yyyy";
    	}
    	
        if (field == null)
            return "";
        if (fieldContentMap == null)
            return "";
        int fieldType = field.getFieldType();
        StringBuffer fieldStr = new StringBuffer("");
        String fieldValue = (String) fieldContentMap.get(field.getFieldId().toUpperCase());
        if (fieldValue == null && fieldType != CommonName.FIELD_TYPE_COMMENTS && fieldType != CommonName.FIELD_TYPE_REPORT) {
            if (hasHiddenField) {
                fieldStr.append("<input type='hidden' name='" + field.getFieldId() + "' value=''> ");
            }
            return fieldStr.toString();
        }

        fieldValue = StringUtil.htmlEncoder(fieldValue);

        String labelValue = ""; // 用来保存显示的值

        switch (fieldType) {
        case CommonName.FIELD_TYPE_TEXT: // 1-单文本框
        case CommonName.FIELD_TYPE_IDENTITY: // 8-identity 递增字段（主键）
        case CommonName.FIELD_TYPE_TEXTAREA: // 2-多文本框
            labelValue = StringUtil.FormatHTMLEnter(fieldValue);
            break;
        case CommonName.FIELD_TYPE_DATE: // 3-日期
            // format the date
            if (fieldValue != null && !"".equals(fieldValue)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    java.util.Date cDate = df.parse(fieldValue);
                    
                    labelValue = StringUtil.getDateStr(cDate, dateFormat);
                } catch (Exception e) {
                    e.printStackTrace();
                    labelValue = fieldValue;
                }
            }
            break;
        case CommonName.FIELD_TYPE_SELECT: // 4-多选一
            BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
            Collection selectOptionList = new ArrayList();
            selectOptionList.addAll((ArrayList) dataHelper1.getDetailMap().get(
            		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId()));
            try {
                selectOptionList.addAll((ArrayList) dataHelper1.getDetailMap().get(
                		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId() + "&O"));
            } catch (NullPointerException e) {
            }// IT1321

            if (selectOptionList != null && selectOptionList.size() > 0) {
                Iterator opIt = selectOptionList.iterator();
                while (opIt.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                    if (fieldValue.equals(vo.getId())) {
                        labelValue = vo.getValue();
                        break;
                    }
                }
            } else {
                labelValue = fieldValue;
            }
            break;
        case CommonName.FIELD_TYPE_NUMBER: // 5-数字
            labelValue = HandlingNumber(fieldValue, field);
            break;
        case CommonName.FIELD_TYPE_CHECKBOX: // 6-多选多
            BaseDataHelper dataHelper = BaseDataHelper.getInstance();
            Collection optionList = new ArrayList();
            optionList.addAll((ArrayList) dataHelper.getDetailMap().get(
            		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId()));
            try {
                optionList.addAll((ArrayList) dataHelper.getDetailMap().get(
                		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId() + "&O"));
            } catch (NullPointerException e) {
            }// IT1321

            if (optionList != null && optionList.size() > 0) {
                Iterator opIt = optionList.iterator();
                while (opIt.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                    // if (fieldValue.indexOf(vo.getId()) > -1) { //IT1080
                    if (fieldValue.indexOf("," + vo.getId() + ",") > -1 || fieldValue.endsWith("," + vo.getId())
                            || fieldValue.startsWith(vo.getId() + ",") || fieldValue.equals(vo.getId())) {

                        if (hasHiddenField) {
                            labelValue = labelValue + "<input type='checkbox' checked disabled>" + vo.getValue()
                                     + "<br>";
                            if (rowIndex > 0) {
                                labelValue = labelValue
                                        + ("<input  type='hidden'  name='" + field.getFieldId() + "_" + rowIndex
                                                + "' value='" + vo.getId() + "'>");
                            } else {
                                labelValue = labelValue
                                        + ("<input  type='hidden'  name='" + field.getFieldId() + "' value='"
                                                + vo.getId() + "'>");
                            }
                        } else {
                            labelValue = labelValue + vo.getValue() + "   ";
                        }
                    }
                }
            } else {
                labelValue = fieldValue;
            }
            break;
        case CommonName.FIELD_TYPE_SYSTEM: // 7-SystemField 系统字段
            labelValue = parseSystemField(formSystemId, field, fieldValue, hasHiddenField, staff);
            break;
        case CommonName.FIELD_TYPE_COMMENTS: // 9-comments 注释说明字段
            // labelValue = field.getCommentContent();
            // System.out.println("field.getCommentContent()="+field.getCommentContent());
            labelValue = field.getCommentContent();
            break;
        case CommonName.FIELD_TYPE_BASIC: // 10-申请者基础信息字段
            // basic information field
            if (CommonName.SYSTEM_ID_REQUEST_NO.equals(field.getFieldId())) {
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = FieldUtil
                            .getRequestNo(formSystemId, form.getFormId(), field.getSectionId());
                }
                fieldStr.append(fieldValue);
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE)
                    .getFieldId().equals(field.getFieldId())) { // requestStaffCode
                labelValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue);
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE)
                    .getFieldId().equals(field.getFieldId())) { // requesterTeamCode
                labelValue = StaffTeamHelper.getInstance().getTeamNameByCode(fieldValue);
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE)
                    .getFieldId().equals(field.getFieldId())) { // submitStaffCode
                labelValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue);
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_DATE)
                    .getFieldId().equals(field.getFieldId())) { // requestDate
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    java.util.Date cDate = df.parse(fieldValue);
                    labelValue = StringUtil.getDateStr(cDate, dateFormat+" HH:mm:ss");
                } catch (Exception e) {
                    e.printStackTrace();
                    labelValue = fieldValue;
                }
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_COMPANY_ID).getFieldId()
                    .equals(field.getFieldId())) {// company
                fieldStr.append(CompanyHelper.getInstance().getOrgName(fieldValue));
            }
            break;
        case CommonName.FIELD_TYPE_ATTACH: // 11-附件字段
            if ("file_name".equals(field.getFieldId())) {
                if (hasHiddenField) {
                    labelValue = "<a href='javascript:openFile(\"" + fieldValue + "\")'>" + fieldValue + "</a>";
                } else {
                    labelValue = fieldValue;
                }
            } else {
                labelValue = fieldValue;
            }
            break;
        case CommonName.FIELD_TYPE_REFFORM: // 12 - Reference Form
        	
        	if (!CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())) {                
        		labelValue = 
		                ("<input type='button' name='exportPdfBtn' value='Export to PDF' onclick='clickPDF(\""
		                        + field.getFieldId()
		                        + "\")'  class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            }
            labelValue += HandleRefFormField2(fieldValue, displayType);
            break;
        case CommonName.FIELD_TYPE_REFCONTRACT: // 13 - Reference Contract
            labelValue = HandleRefContract(fieldValue);
            break;
        case CommonName.FIELD_TYPE_MULTISTAFF: // 14 - Multi-Staffs
            labelValue = StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue);
            break;
            
        case CommonName.FIELD_TYPE_REPORT: // 15 - Report button.
        	fieldStr.append(generateButtonField(field, fieldValue));
        break;
        
        default:
            labelValue = StringUtil.FormatHTMLEnter(fieldValue);
        }
        if (hasHiddenField) {
            if (field.getFieldType() != CommonName.FIELD_TYPE_CHECKBOX) {
                fieldStr.append("<input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue + "'> ");
            }
        }

        fieldStr.append(labelValue);
        // System.out.println("fieldStr: " + fieldStr);
        return fieldStr.toString();
    }

    /**
     * 
     * @param field
     * @param form
     * @param staff
     * @param fieldContentMap
     * @return
     */
    public static String showEditField(FormSectionFieldVO field, FormManageVO form, StaffVO staff,
            HashMap fieldContentMap,String dateFormat, int formSystemId, boolean isTableSection) {
        return showEditField(field, form, staff, fieldContentMap, -1,dateFormat,formSystemId,isTableSection);
    }

    /**
     * 显示一个可编辑控件（如果该字段有值的话，并将其值显示出来）
     * 
     * @param field
     * @param form
     * @param staff
     * @param fieldContentMap
     * @param rowIndex
     *            所在行的序号(从0开始，0表示第一行）
     * @return
     */
    public static String showEditField(FormSectionFieldVO field, FormManageVO form, StaffVO staff,
            HashMap fieldContentMap, int rowIndex,String dateFormat, int formSystemId, boolean isTableSection) {
        if (field == null)
            return "";
        
        if(dateFormat==null || "".equals(dateFormat)){
        	dateFormat="MM/dd/yyyy";
        }

        String fieldValue = "";
		StringBuffer fieldStr = new StringBuffer("");
		String disabled = "";
		if (field.getIsDisabled() == 1
				&& (field.getFieldType() == CommonName.FIELD_TYPE_TEXT || field
						.getFieldType() == CommonName.FIELD_TYPE_TEXTAREA)) {
			disabled = "  disabled='disabled'  ";
		}
		String style = "";
		if (field.getIsReadonly() == 1
				&& (field.getFieldType() == CommonName.FIELD_TYPE_TEXT
						|| field.getFieldType() == CommonName.FIELD_TYPE_TEXTAREA
						|| field.getFieldType() == CommonName.FIELD_TYPE_NUMBER || field
						.getFieldType() == CommonName.FIELD_TYPE_DATE)) {
			if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
				style = " style='border: medium none; background:transparent; text-align:right;' readonly ";
			} else {
				style =  " style='border: medium none; background:transparent' readonly ";
			}
		} else if (field.getFieldType() == CommonName.FIELD_TYPE_NUMBER) {
			style = " style='text-align:right;' ";
		}

        if (fieldContentMap != null && fieldContentMap.size() > 0) {
            fieldValue = (String) fieldContentMap.get(field.getFieldId().toUpperCase());
            // Add by Mario 20090902
            if (fieldValue == null && field.getDefaultValue() != null) {
                fieldValue = field.getDefaultValue();
            }
            //
            fieldValue = StringUtil.htmlEncoder(fieldValue);
        } else {// 如果是打开一张新的form进行申请填写时，如果该字段设定了默认值，则显示默认值
            if (field.getDefaultValue() != null) {
                fieldValue = field.getDefaultValue();
            }
        }
        if (fieldValue == null) {
            fieldValue = "";
        }

        String title = "";
        if (field.getFieldComments() != null && !"".equals(field.getFieldComments())) {
            title = " tooltipText='" + field.getFieldComments() + "' ";
        }
        String required = " required='false' ";
        if (field.getIsRequired()) {
            required = " required='true' ";
        }

        String rowFlag = " rowIndex='" + rowIndex + "' ";

        StringBuffer onEventStr = new StringBuffer(""); // 用来保存设置事件的字符串
        if (field.getClickEvent() != null && !"".equals(field.getClickEvent())) {
            onEventStr.append(" onClick='" + StringUtil.htmlEncoder(field.getClickEvent()) + "' ");
        }
        if (field.getDbclickEvent() != null && !"".equals(field.getDbclickEvent())) {
            onEventStr.append(" ondblclick='" + StringUtil.htmlEncoder(field.getDbclickEvent()) + "' ");
        }
        if (field.getChangeEvent() != null && !"".equals(field.getChangeEvent())) {
            onEventStr.append(" onchange='" + StringUtil.htmlEncoder(field.getChangeEvent()) + "' ");
        }
        if (field.getOnfocusEvent() != null && !"".equals(field.getOnfocusEvent())) {
            onEventStr.append(" onFocus='" + StringUtil.htmlEncoder(field.getOnfocusEvent()) + "' ");
        }

        String onBlur = "";
        if (field.getFieldType() == CommonName.FIELD_TYPE_TEXT) {
            onBlur = "textCheckOnLostFocus(this, " + field.getFieldLength() + ");";
        }
        if (field.getLostfocusEvent() != null && !"".equals(field.getLostfocusEvent())) {
            onBlur += StringUtil.htmlEncoder(field.getLostfocusEvent());
        }
        if (!"".equals(onBlur)) {
            onEventStr.append(" onblur='" + onBlur + "' ");
        }

        // String labelValue = ""; // 用来保存显示的值
        int fieldType = field.getFieldType();
        double controlWidth = 90;
        if (field.getControlsWidth() > 50) {
            controlWidth = field.getControlsWidth();
        }
        switch (fieldType) {
        case CommonName.FIELD_TYPE_TEXT: // 1-单文本框
            fieldValue = StringUtil.replace(fieldValue, "'", "&#39;");
            fieldStr.append(
                            "<textarea  "+disabled+" class='input'  type='text' " + rowFlag + " " + onEventStr.toString() + " name='"
                                    + field.getFieldId().trim() + "' maxLength='" + field.getFieldLength()
                                    + "' onkeydown=\"textCounterForInput(this," + field.getFieldLength() + ",event)\" "
                                    + title).append(
                            required + "  style=\"width:" + controlWidth
                                    + "px;overflow-x:visible;overflow-y:visible;\" title='" + field.getFieldLabel()
                                    + "' " + style + " >" + fieldValue + "</textarea>");
            
            if (field.getIsDisabled() == 1)
            	fieldStr.append("<input name='"+field.getFieldId().trim()+"' type='hidden' "+required+" value='"+fieldValue+"'/>");
            
            break;
        case CommonName.FIELD_TYPE_IDENTITY: // 8-identity 递增字段（主键）
            break;
        case CommonName.FIELD_TYPE_TEXTAREA: // 2-多文本框
            fieldStr.append("<textarea "+disabled+" name='" + field.getFieldId() + "' " + style+" " + rowFlag + " "
                    + onEventStr.toString() + " title='" + field.getFieldLabel() + "' " + title + " rows='"
                    + field.getControlsHeight() + "' cols='" + field.getControlsWidth() + "' maxLength='"
                    + field.getFieldLength() + "' " + title
                    + " onKeyDown=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"
                    + field.getFieldLength()
                    + ")\" onKeyUp=\"javascript:textCounter(this,document.getElementById('textareaLimitLength'),"
                    + field.getFieldLength() + ")\" style='border: 1px solid #e4e4e4;width:100%;' " + required + ">"
                    + fieldValue + "</textarea>");
            
            if (field.getIsDisabled() == 1)
            	fieldStr.append("<input name='"+field.getFieldId().trim()+"' type='hidden' "+required+" value='"+fieldValue+"'/>");
            
            break;
        case CommonName.FIELD_TYPE_DATE: // 3-日期
            // format the date
            if (fieldValue != null && !"".equals(fieldValue)) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date cDate = df.parse(fieldValue);
                    fieldValue = StringUtil.getDateStr(cDate, dateFormat);
                } catch (Exception e) {
                }
            }
            fieldStr.append("<input class='date' type='text' " + style + " isDate='true' " + rowFlag + " " + title + " title='"
                    + field.getFieldLabel() + "' " + onEventStr.toString() + " size='" + field.getControlsWidth()
                    + "' " + title + " name='" + field.getFieldId() + "' style='width:90' onclick='setday(this)' ");
            
            if(isTableSection)
            	fieldStr.append(required + " value='" + fieldValue + "'>");
            else
            	fieldStr.append(required + " value='" + fieldValue + "'>("+dateFormat+")");
            
            break;
        case CommonName.FIELD_TYPE_SELECT: // 4-多选一
            BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
            Collection selectOptionList = (ArrayList) dataHelper1.getDetailMap().get(
            		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId());
            fieldStr.append("<select class='combox' onmousewheel='event.returnValue=false' name='" + field.getFieldId() + "' "
                    + rowFlag + " " + onEventStr.toString() + " title='" + field.getFieldLabel() + "' " + required
                    + " " + title + ">");
            fieldStr.append("<option value=''>  </option>");
            if (selectOptionList != null && selectOptionList.size() > 0) {
                Iterator opIt = selectOptionList.iterator();
                boolean selected = false;
                while (opIt.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                    if (fieldValue.equals(vo.getId())) {
                        fieldStr.append("<option value='" + vo.getId() + "' selected>" + vo.getValue() + "</option>");
                        selected = true;
                    } else {
                        fieldStr.append("<option value='" + vo.getId() + "'>" + vo.getValue() + "</option>");
                    }
                }
                // 如果没有选择的值，则显示为空
                // if(selected==false){
                // fieldStr.append("<option value='' selected> </option>");
                // }
            }
            fieldStr.append("</select>");
            break;
        case CommonName.FIELD_TYPE_NUMBER: // 5-数字
            fieldValue = HandlingNumber2(fieldValue, field);
            // fieldValue = StringUtil.replace(fieldValue,"'","&#39;");
            String onchange = "";
            // if (field.getIsMoney() == 1 && field.getIsReadonly() == 1) {
            // onchange = " onchange='convertMoneyObj(this)' ";
            // }
            fieldStr
                    .append(
                            "<input type='text' isNumber='true' " + style + " " + onEventStr.toString() + "  "
                                    + rowFlag + " name='" + field.getFieldId() + "'  maxLength='"
                                    + field.getFieldLength() + "' " + title + " value='" + fieldValue + "' ")
                    .append(
                            required
                                    + " title='"
                                    + field.getFieldLabel()
                                    + "' size='"
                                    + field.getControlsWidth()
                                    + "' onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"
                                    + onchange + ">");
            break;
        case CommonName.FIELD_TYPE_CHECKBOX: // 6-多选多
            BaseDataHelper dataHelper = BaseDataHelper.getInstance();
            Collection optionList = (ArrayList) dataHelper.getDetailMap().get(
            		formSystemId + "&" + field.getSectionId() + "&" + field.getFieldId());
            if (optionList != null && optionList.size() > 0) {
                Iterator opIt = optionList.iterator();
                while (opIt.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) opIt.next();
                    // if(fieldValue.indexOf(vo.getId())>-1){ //IT1080
                    if (fieldValue.indexOf("," + vo.getId() + ",") > -1 || fieldValue.endsWith("," + vo.getId())
                            || fieldValue.startsWith(vo.getId() + ",") || fieldValue.equals(vo.getId())) {
                        /*if (rowIndex > 0) {
                            fieldStr.append("<input " + title + " title='" + field.getFieldLabel() + "' " + required
                                    + " checked type='checkbox' " + onEventStr.toString() + "  name='"
                                    + field.getFieldId() + "_" + rowIndex + "' " + title + " value='" + vo.getId()
                                    + "'>" + vo.getValue() + " &nbsp;&nbsp;");
                        } else {*/
                            fieldStr.append("<input " + title + " title='" + field.getFieldLabel() + "' " + required
                                    + " checked type='checkbox' " + onEventStr.toString() + " name='"
                                    + field.getFieldId() + "' " + title + " value='" + vo.getId() + "'>"
                                    + vo.getValue() + " &nbsp;&nbsp;");
                       /* }*/
                    } else {
                        /*if (rowIndex > 0) {
                            fieldStr.append("<input " + title + " title='" + field.getFieldLabel() + "' " + required
                                    + " type='checkbox' " + onEventStr.toString() + " name='" + field.getFieldId()
                                    + "_" + rowIndex + "' " + title + " value='" + vo.getId() + "'>" + vo.getValue()
                                    + " &nbsp;&nbsp; ");
                        } else {*/
                            fieldStr.append("<input " + title + " title='" + field.getFieldLabel() + "' " + required
                                    + " type='checkbox' " + onEventStr.toString() + " name='" + field.getFieldId()
                                    + "' value='" + vo.getId() + "' " + title + ">" + vo.getValue()
                                    + " &nbsp;&nbsp; ");
                        /*}*/
                    }
                    if(CommonName.FORM_SECTION_TYPE_TABLE.equals(field.getSectionType())||CommonName.FORM_SECTION_TYPE_JQGRID_TABLE.equals(field.getSectionType()))
                    	fieldStr.append("<br/>");
                }
            }
            break;
        case CommonName.FIELD_TYPE_SYSTEM: // 7-SystemField 系统字段
            fieldStr.append(parseSystemEditField(formSystemId, field, fieldValue, staff));
            break;
        case CommonName.FIELD_TYPE_COMMENTS: // 9-comments 注释说明字段
            fieldStr.append(field.getCommentContent());
            break;
        case CommonName.FIELD_TYPE_BASIC: // 10-申请者基础信息字段
            // basic information field
            if (CommonName.SYSTEM_ID_REQUEST_NO.equals(field.getFieldId())) {
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = FieldUtil
                            .getRequestNo(formSystemId, form.getFormId(), field.getSectionId());
                }
                fieldStr.append(fieldValue).append(
                        "<input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue + "'>");
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE)
                    .getFieldId().equals(field.getFieldId())) { // requestStaffCode
                String teamCode = staff.getTeamCode();
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = staff.getStaffCode();
                } else {
                    teamCode = StaffTeamHelper.getInstance().getStaffByCode(fieldValue).getTeamCode();
                }
                fieldStr.append(HtmlUtil.createRequestStaffSelect(fieldValue, teamCode, field));
                // ///////////////////////////////////////////////////////////////////////////////////////
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUESTOR_TEAM_CODE)
                    .getFieldId().equals(field.getFieldId())) { // requesterTeamCode
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = staff.getTeamCode();
                }
                fieldStr.append(HtmlUtil.createRequestTeamSelect(fieldValue, field, staff));
                // ///////////////////////////////////////////////////////////////////////////////////////
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_SUBMIT_STAFF_CODE)
                    .getFieldId().equals(field.getFieldId())) { // submitStaffCode
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = staff.getStaffCode();
                }
                StaffVO tempStaff = StaffTeamHelper.getInstance().getStaffByCode(fieldValue);
                fieldStr.append(tempStaff.getStaffName() + "(" + tempStaff.getLogonId() + ")").append(
                        "<input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue + "'>");
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_REQUEST_DATE)
                    .getFieldId().equals(field.getFieldId())) { // requestDate
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    if (fieldValue == null || "".equals(fieldValue)) {
                        fieldValue = StringUtil.getDateStr(new java.util.Date(), dateFormat+" HH:mm:ss");
                    } else {
                        java.util.Date cDate = df.parse(fieldValue);
                        fieldValue = StringUtil.getDateStr(cDate, dateFormat+" HH:mm:ss");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fieldStr.append(fieldValue).append(
                        "<input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue + "'>");
            } else if (SystemFieldHelper.getInstance().getBasicFieldById(CommonName.SYSTEM_ID_COMPANY_ID).getFieldId()
                    .equals(field.getFieldId())) {// company
                fieldValue = CompanyHelper.getInstance().getOrgName(staff.getOrgId());
                fieldStr.append(fieldValue).append(
                        "<input type='hidden' name='" + field.getFieldId() + "' value='" + staff.getOrgId() + "'>");
                ;
            }
            break;
        case CommonName.FIELD_TYPE_ATTACH: // 11-附件字段
            break;
        case CommonName.FIELD_TYPE_REFFORM: // 12 - Reference Form
            String divId = "div_";
            String tagId = "";
            if (rowIndex != -1) {
                divId += field.getFieldId() + "_" + rowIndex;
                tagId += field.getFieldId() + "_" + rowIndex;
            } else {
                divId += field.getFieldId();
                tagId += field.getFieldId();
            }
            fieldStr
                    .append("<input type='button' name='selectRefBtn' value='"
                            + I18NMessageHelper.getMessage("button.select")
                            + "' onclick='showRefFormWindow2(\""
                            + field.getFieldId()
                            + "\",\""
                            + divId
                            + "\",\""
                            + tagId
                            + "\")' class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            if (CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())) {
                fieldStr
                        .append("<input type='button' name='naBtn' value='"
                                + I18NMessageHelper.getMessage("button.ref_form_na")
                                + "' onclick='clickNA(\""
                                + tagId
                                + "\")'  class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            } else {
            	fieldStr
		                .append("<input type='button' name='exportPdfBtn' value='Export to PDF' onclick='clickPDF(\""
		                        + divId
		                        + "\")'  class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            }
            // For testing -----
            // fieldValue = "IT_V2_04092009_01.39,IT_V2_04092009_03.39";
            // ------------------
            fieldStr.append("<input type=\"hidden\" " + rowFlag + " id=\"" + tagId + "\" name=\"" + field.getFieldId()
                    + "\" value=\"" + fieldValue + "\" " + onEventStr.toString() + " " + required + ">");
            fieldStr.append("<div style='display: inline' id='" + divId + "'>" + HandleRefFormField2(fieldValue)
                    + "</div>");
            break;
        case CommonName.FIELD_TYPE_REFCONTRACT: // 13 - Reference Contract
            divId = "div_";
            tagId = "";
            if (rowIndex != -1) {
                divId += field.getFieldId() + "_" + rowIndex;
                tagId += field.getFieldId() + "_" + rowIndex;
            } else {
                divId += field.getFieldId();
                tagId += field.getFieldId();
            }
            fieldStr
                    .append("<input type='button' name='selectRefBtn' value='"
                            + I18NMessageHelper.getMessage("button.select")
                            + "' onclick='selectRefContract(\""
                            + field.getFieldId()
                            + "\",\""
                            + divId
                            + "\",\""
                            + tagId
                            + "\")' class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            // For testing -----
            // fieldValue = "123456787,123456788";
            // ------------------
            fieldStr.append("<input type=\"hidden\" " + rowFlag + " id=\"" + tagId + "\" name=\"" + field.getFieldId()
                    + "\" value=\"" + fieldValue + "\" " + onEventStr.toString() + ">");
            fieldStr.append("<div style='display: inline' id='" + divId + "'>" + HandleRefContract(fieldValue)
                    + "</div>");
            break;
        case CommonName.FIELD_TYPE_MULTISTAFF: // 14 - Multi-staff field
            divId = "div_";
            tagId = "";
            if (rowIndex != -1) {
                divId += field.getFieldId() + "_" + rowIndex;
                tagId += field.getFieldId() + "_" + rowIndex;
            } else {
                divId += field.getFieldId();
                tagId += field.getFieldId();
            }
            fieldStr
                    .append("<input type='button' name='selectStaffBtn' value='"
                            + I18NMessageHelper.getMessage("button.select")
                            + "' onclick='selectMultiStaff(\""
                            + field.getFieldId()
                            + "\",\""
                            + divId
                            + "\",\""
                            + tagId
                            + "\")' class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">&nbsp;&nbsp;");
            // fieldValue = "C0600,C0097";
            // ------------------
            fieldStr.append("<input type=\"hidden\" " + rowFlag + " id=\"" + tagId + "\" name=\"" + field.getFieldId()
                    + "\" value=\"" + fieldValue + "\" " + onEventStr.toString() + " " + required + ">");
            fieldStr.append("<div style='display: inline' id='" + divId + "'>"
                    + StaffTeamHelper.getInstance().getStaffNameByCode(fieldValue) + "</div>");
            break;

        case CommonName.FIELD_TYPE_REPORT: // 15 - Report button.
        	fieldStr.append(generateButtonField(field, fieldValue));
        break;
        default:
            fieldStr.append(StringUtil.FormatHTMLEnter(fieldValue));
        }

        // fieldStr.append("  "+conrolIsRequiredFlag(field.getIsRequired()));
        // System.out.println("fieldStr: " + fieldStr);
        return fieldStr.toString();
    }

	private static StringBuffer generateButtonField(FormSectionFieldVO field,String fieldValue) {
		StringBuffer fieldStr = new StringBuffer();
		fieldStr.append("<input type='button' name='"+field.getFieldId()+"_button' id='"+field.getFieldId()+"_button' value='"
		         + I18NMessageHelper.getMessage("button.generateReport")
		         + "' onclick='generateReport(\""+field.getFieldId()+"\",\""+ field.getReportSystemId()+ "\",\""+ fieldValue+ "\",\""+field.getFieldDataSrSQL()
		         + "\")' class='btn3_mouseout' onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\" />&nbsp;&nbsp;");
		 
		fieldStr.append("<input type='hidden'  id='" + field.getFieldId() + "' name='" + field.getFieldId()+"' />");
		 fieldStr.append("<input id='"+field.getFieldId()+"_checkbox' name='"+field.getFieldId()+"_checkbox'  type='checkbox'  value='Y'/>&nbsp;"+I18NMessageHelper.getMessage("text.getNew"));
		 return fieldStr;
	}

    private static String getStaffNames(String fieldValue) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 解析系统字段
     * 
     * @param field
     * @return
     */
    private static String parseSystemField(int formSystemId,FormSectionFieldVO field, String fieldValue, boolean hasHiddenField,StaffVO staff) {
        String labelValue = "";
        //FormSectionFieldVO systemField = SystemFieldHelper.getInstance().getSystemFieldById(field.getFieldId());
        SystemFieldVO systemField =	SystemFieldHelper.getInstance().getSystemFieldVOById(field.getFieldId());

        if (CommonName.SYSTEMFIELD_TYPE_SELECT.equals(systemField.getFieldType())) { // 通常的不带参数的sql语句生成的下拉选项集合
            Collection sysOpList = systemField.getOptionList();
            if (sysOpList != null && sysOpList.size() > 0) {
                Iterator it = sysOpList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO op = (DictionaryDataVO) it.next();
                    if (fieldValue.equals(op.getId().trim())) {
                        labelValue = op.getValue();
                        break;
                    }
                }
            } else {
                labelValue = fieldValue;
            }
        } else if (CommonName.SYSTEMFIELD_TYPE_REFERENCE.equals(systemField.getFieldType())) {// Reference Form
            // labelValue = fieldValue.trim();
            if (hasHiddenField) {
                labelValue = "<div id='divrefeformId'>" + HandleRefFormField(fieldValue, field) + "</div>";
            } else {
                labelValue = fieldValue;
            }
        } else if (CommonName.SYSTEMFIELD_TYPE_LABEL.equals(systemField.getFieldType())) { // Label
            // (只需要将其中的值显示出来就可以)
            labelValue = fieldValue;
        } else if (CommonName.SYSTEMFIELD_TYPE_SELECTBYPARAM.equals(systemField.getFieldType())) { // 通常的带参数的sql语句生成的下拉选项集合
            
            HashMap otherParamNameMap = new HashMap();
            
            Collection optionList = SystemFieldUtil.getDynamicData(formSystemId, field.getFieldId(), staff, otherParamNameMap);
            
            if (optionList != null && optionList.size() > 0) {
                Iterator it = optionList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO op = (DictionaryDataVO) it.next();
                    if (fieldValue.equals(op.getId().trim())) {
                        labelValue = op.getValue();
                        break;
                    }
                }
            } else {
                labelValue = fieldValue;
            }
        } else if (CommonName.SYSTEMFIELD_TYPE_PROCESSOR.equals(systemField.getFieldType())) { // Supervisor's
            // approval
            labelValue = fieldValue;
        } else if (CommonName.SYSTEMFIELD_TYPE_COMMON.equals(systemField.getFieldType())) {// common type
            if (systemField.getColumnType() == 2) {// date
                if (fieldValue != null && !"".equals(fieldValue)) {
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date cDate = df.parse(fieldValue);
                        labelValue = StringUtil.getDateStr(cDate, "MM/dd/yyyy");
                        if (hasHiddenField) {
                            if ("ot_plan_date".equals(systemField.getFieldId())) {// 如果是 加班申请日期 ，则增加一个查看打卡记录的链接
                                labelValue = StringUtil.getDateStr(cDate, "MM/dd/yyyy E");
                                String linkValue = StringUtil.getDateStr(cDate, "MM/dd/yyyy");
                                labelValue = "<a href='javascript:showCheckInout(\"" + getServerUrl()
                                        + "/otFormAction.it?method=listStaffCheckInout&otPlanDate=" + linkValue
                                        + "\")'>" + labelValue + "</a>";
                            }
                        }
                    } catch (Exception e) {
                        labelValue = fieldValue;
                    }
                }
            } else if (systemField.getColumnType() == 3) {// numeric
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = "0";
                }
                labelValue = StringUtil.replace(fieldValue, "'", "&#39;");
            }
        }
        return labelValue;
    }

    /**
     * 解析系统字段
     * 
     * @param field
     * @return
     */
    private static String parseSystemEditField(int formSystemId, FormSectionFieldVO field, String fieldValue, StaffVO staff) {
        //FormSectionFieldVO systemField = SystemFieldHelper.getInstance().getSystemFieldById(field.getFieldId());
        SystemFieldVO systemField =	SystemFieldHelper.getInstance().getSystemFieldVOById(field.getFieldId());
        
        StringBuffer str = new StringBuffer("");
        String required = " required='false' ";
        if (field.getIsRequired()) {
            required = " required='true' ";
        }
        String labelValue = "";

        String isReadonly = "";
        if (field.getIsReadonly() == 1 && systemField.getFieldType().equals(CommonName.SYSTEMFIELD_TYPE_COMMON)) {
            isReadonly = " style='border: medium none;background:transparent' readonly ";
        }

        String title = "";
       
        if (field.getFieldLabel() != null && !"".equals(field.getFieldLabel())) {
            title = " title='" + field.getFieldLabel() + "' ";
        }
        if (field.getFieldComments() != null && !"".equals(field.getFieldComments())) {
            title = title + " tooltipText='" + field.getFieldComments() + "' ";
        }

        StringBuffer onEventStr = new StringBuffer(""); // 用来保存设置事件的字符串
        if (field.getClickEvent() != null && !"".equals(field.getClickEvent())) {
            onEventStr.append(" onClick='" + StringUtil.htmlEncoder(field.getClickEvent()) + "' ");
        }
        if (field.getDbclickEvent() != null && !"".equals(field.getDbclickEvent())) {
            onEventStr.append(" ondblclick='" + StringUtil.htmlEncoder(field.getDbclickEvent()) + "' ");
        }
        if (field.getChangeEvent() != null && !"".equals(field.getChangeEvent())) {
            onEventStr.append(" onchange='" + StringUtil.htmlEncoder(field.getChangeEvent()) + "' ");
        }
        if (field.getOnfocusEvent() != null && !"".equals(field.getOnfocusEvent())) {
            onEventStr.append(" onFocus='" + StringUtil.htmlEncoder(field.getOnfocusEvent()) + "' ");
        }
        if (field.getLostfocusEvent() != null && !"".equals(field.getLostfocusEvent())) {
            onEventStr.append(" onblur='" + StringUtil.htmlEncoder(field.getLostfocusEvent()) + "' ");
        }

        if (CommonName.SYSTEMFIELD_TYPE_SELECT.equals(systemField.getFieldType())) { // 通常的不带参数的sql语句生成的下拉选项集合
            Collection sysOpList = systemField.getOptionList();
            str.append("<select class='combox' name='" + field.getFieldId() + "' " + required + " " + title + " "
                    + onEventStr.toString() + " onmousewheel='event.returnValue=false'>");
            str.append("<option value=''> </option>");
            if (sysOpList != null && sysOpList.size() > 0) {
                Iterator it = sysOpList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO op = (DictionaryDataVO) it.next();
                    String selected = fieldValue.equals(op.getId()) ? " selected" : "";
                    String misc = op.getMisc() == null ? "" : " misc='" + op.getMisc() + "'";
                    str.append("<option value='" + op.getId() + "'" + selected + misc + ">" + op.getValue()
                            + "</option>");
                }
            }
            else
            {
            	str.append("<option value='"+fieldValue+"' selected></option>");
            }
            str.append("</select>");
        } else if (CommonName.SYSTEMFIELD_TYPE_REFERENCE.equals(systemField.getFieldType())) {// Reference Form
            str.append("<div id='divrefeformId'>" + HandleRefFormField(fieldValue, field) + "</div>");
            str
                    .append("<input type='button' name='addRefBtn' value='Add Reference' onclick='showRefFormWindow(\""
                            + formSystemId
                            + "\",\""
                            + field.getSectionId()
                            + "\")' class=btn3_mouseout onmouseover=\"this.className='btn3_mouseover'\" onmouseout=\"this.className='btn3_mouseout'\" onmousedown=\"this.className='btn3_mousedown'\" onmouseup=\"this.className='btn3_mouseup'\">");
        } else if (CommonName.SYSTEMFIELD_TYPE_LABEL.equals(systemField.getFieldType())) { // Label //
            // (只需要将其中的值显示出来就可以)
            str.append(fieldValue + "<input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue
                    + "'>");
        } else if (CommonName.SYSTEMFIELD_TYPE_SELECTBYPARAM.equals(systemField.getFieldType())) { // 通常的带参数的sql语句生成的下拉选项集合
            HashMap otherParamNameMap = new HashMap();
            // --- For CHO ---
            if ("sub_category".equalsIgnoreCase(field.getFieldId()) && fieldValue != null) {
                int index = fieldValue.indexOf("_");
                if (index != -1) {
                    String categoryId = fieldValue.substring(0, index);
                    otherParamNameMap.put(SystemFieldUtil.PARAM_CATEGORY_ID, categoryId);
                }
            }
            // ---------------
            // Collection optionList = SystemFieldUtil.getDynamicData("account_dc",staff,otherParamNameMap);
            Collection optionList = SystemFieldUtil.getDynamicData(formSystemId, field.getFieldId(), staff, otherParamNameMap);
            // System.out.println("field.getFieldId(): " + field.getFieldId());
            str.append("<select class='combox' name='" + field.getFieldId() + "' " + required + " " + title + " "
                    + onEventStr.toString() + " onmousewheel='event.returnValue=false'>");
            if (optionList != null && optionList.size() > 0) {
                Iterator it = optionList.iterator();
                boolean selected = false;
                while (it.hasNext()) {
                    DictionaryDataVO op = (DictionaryDataVO) it.next();
                    if (fieldValue.equals(op.getId())) {
                        str.append("<option value='" + op.getId() + "' selected>" + op.getValue() + "</option>");
                        selected = true;
                    } else {
                        str.append("<option value='" + op.getId() + "'>" + op.getValue() + "</option>");
                    }
                }
                // 如果没有选择的值，则显示为空
                if (selected == false) {
                    str.append("<option value='' selected> </option>");
                } else {
                    str.append("<option value=''> </option>");
                }
            }
            str.append("</select>");
        } else if (CommonName.SYSTEMFIELD_TYPE_PROCESSOR.equals(systemField.getFieldType())) { // Supervisor's
            // approval
            if (fieldValue == null || "".equals(fieldValue)) {
                str.append(staff.getStaffName() + "(" + StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss") + ")"
                        + "<input type='hidden' name='" + field.getFieldId() + "' value='" + staff.getStaffName() + "("
                        + StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss") + ")'>");
            } else {
                str.append(fieldValue).append(
                        " <input type='hidden' name='" + field.getFieldId() + "' value='" + fieldValue + "'>");
            }
        } else if (CommonName.SYSTEMFIELD_TYPE_COMMON.equals(systemField.getFieldType())) {// common type
            if (systemField.getColumnType() == CommonName.DATA_TYPE_DATE) {// date
                if (fieldValue != null && !"".equals(fieldValue)) {
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date cDate = df.parse(fieldValue);
                        labelValue = StringUtil.getDateStr(cDate, "MM/dd/yyyy");
                    } catch (Exception e) {
                        labelValue = fieldValue;
                    }
                }
                str.append("<input type='text' isDate='true' " + isReadonly + "  " + title + " size='"
                        + field.getControlsWidth() + "' name='" + field.getFieldId()
                        + "' style='width:90' onclick='setday(this)' " + required + " value='" + labelValue
                        + "'>");
            } else if (systemField.getColumnType() == CommonName.DATA_TYPE_NUMBER) {// numeric
                String style = " style='text-align:right;' ";
                fieldValue = StringUtil.replace(fieldValue, "'", "&#39;");
                str
                        .append("<input type='text' isNumber='true' "
                                + isReadonly
                                + " name='"
                                + field.getFieldId()
                                + "' value='"
                                + fieldValue
                                + "' "
                                + required
                                + style
                                + " "
                                + title
                                + " size='10' onKeyPress='if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'"
                                + ">");
            }
        }
        return str.toString();
    }

    /**
     * 专门用来显示disabled的field
     * 
     * @return
     */
    public static String ShowDisabledField(FormSectionFieldVO field) {
        StringBuffer str = new StringBuffer(" ");
        str.append("<input type='hidden' name='" + field.getFieldId() + "' value=''>");
        return str.toString();
    }
    
    public static String addDisabledField(FormSectionFieldVO field,String value) {
        StringBuffer str = new StringBuffer(" ");
        str.append("<input type='hidden' name='" + field.getFieldId() + "' value='"+value+"'>");
        return str.toString();
    }

    public static String conrolIsRequiredFlag(boolean requrired) {
        if (requrired) {
            return "(<font color='red'><b>*</b></font>)";
        } else {
            return "";
        }
    }

    public static String showTextField(HashMap field, Object value) {
        FormSectionFieldVO fieldVO = (FormSectionFieldVO) field.get("field");
        if (fieldVO == null) {
            fieldVO = convertMapToVo(field);
            field.put("field", fieldVO);
        }
        return showTextField(fieldVO, value);
    }

    public static String showTextField(FormSectionFieldVO field, Object value) {
        if (value == null) {
            return "";
        }
        String fieldId = field.getFieldId();

        switch (field.getFieldType()) {
        case CommonName.FIELD_TYPE_SYSTEM: {
            //FormSectionFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldById(fieldId);
            
        	SystemFieldVO vo = SystemFieldHelper.getInstance().getSystemFieldVOById(fieldId);
            
            Collection optList = vo.getOptionList();
            if (optList != null && optList.size() > 0) {
                Iterator it = optList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO op = (DictionaryDataVO) it.next();
                    if (value.equals(op.getId())) {
                        return op.getValue();
                    }
                }
            } else {
                return value.toString();
            }
            break;
        }
        case CommonName.FIELD_TYPE_CHECKBOX: {
            BaseDataHelper dataHelper = BaseDataHelper.getInstance();
            Collection optList = new ArrayList();
            optList.addAll((ArrayList) dataHelper.getDetailMap().get(
                    field.getFormSystemId() + "&" + field.getSectionId() + "&" + fieldId));
            try {
                optList.addAll((ArrayList) dataHelper.getDetailMap().get(
                        field.getFormSystemId() + "&" + field.getSectionId() + "&" + fieldId + "&O"));
            } catch (NullPointerException e) {
            }// IT1321

            StringBuffer str = new StringBuffer("");
            if (optList != null && optList.size() > 0) {
                Iterator it = optList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) it.next();
                    if (((String) value).indexOf(vo.getId()) > -1) {
                        str.append(vo.getValue()).append("  ");
                    }
                }
            } else {
                str.append(value);
            }
            return str.toString();
        }
        case CommonName.FIELD_TYPE_SELECT: {
            BaseDataHelper dataHelper = BaseDataHelper.getInstance();
            Collection optList = new ArrayList();
            optList.addAll((ArrayList) dataHelper.getDetailMap().get(
                    field.getFormSystemId() + "&" + field.getSectionId() + "&" + fieldId));
            try {
                optList.addAll((ArrayList) dataHelper.getDetailMap().get(
                        field.getFormSystemId() + "&" + field.getSectionId() + "&" + fieldId + "&O"));
            } catch (NullPointerException e) {
            }// IT1321

            if (optList != null && optList.size() > 0) {
                Iterator it = optList.iterator();
                while (it.hasNext()) {
                    DictionaryDataVO vo = (DictionaryDataVO) it.next();
                    if (value.equals(vo.getId())) {
                        return vo.getValue();
                    }
                }
            } else {
                return value.toString();
            }
            break;
        }
        case CommonName.FIELD_TYPE_DATE: {
            SimpleDateFormat df = new SimpleDateFormat(CommonName.FORMAT_DATE);
            return df.format((Date) value);
        }
        default:
            if (field.getDataType() == CommonName.DATA_TYPE_DATE) {
                SimpleDateFormat df = new SimpleDateFormat(CommonName.FORMAT_DATETIME);
                return df.format((Date) value);
            }
            return value.toString();
        }
        return value.toString();
    }

    public static FormSectionFieldVO convertMapToVo(HashMap map) {
        FormSectionFieldVO vo = new FormSectionFieldVO();
        vo.setFormSystemId(FieldUtil.convertSafeInt(map, "FORM_SYSTEM_ID", 0));
        vo.setSectionId(FieldUtil.convertSafeString(map, "SECTION_ID"));
        vo.setFieldId(FieldUtil.convertSafeString(map, "FIELD_ID"));
        if ("0".equals(FieldUtil.convertSafeString(map, "IS_REQUIRED"))) {
            vo.setIsRequired(true);
        } else {
            vo.setIsRequired(false);
        }
        vo.setFieldLabel(FieldUtil.convertSafeString(map, "FIELD_LABEL"));
        vo.setFieldType(FieldUtil.convertSafeInt(map, "FIELD_TYPE", 1));
        return vo;
    }

    /**
     * 专门处理Reference_Form System Field
     * 
     * @return
     */
    private static String HandleRefFormField(String strvalue, FormSectionFieldVO field) {
        String[] arrValue;
        String returnstr = "";
        arrValue = strvalue.split(",");
        for (int i = 0; i < arrValue.length; i++) {
            returnstr += "<a href=\"javascript:openCenterWindow('" + getServerUrl()
                    + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo="
                    + arrValue[i] + "&formSystemId=" + field.getFormSystemId() + "',700,700)\">" + arrValue[i]
                    + "</a>,";
        }
        returnstr = returnstr.substring(0, returnstr.length() - 1);
        returnstr += "<input type=\"hidden\" name=\"reference_form\" value=\"" + strvalue + " \">";
        return returnstr;
    }

    private static String HandleRefFormField2(String strvalue) {
        return HandleRefFormField2(strvalue, null);
    }

    private static String HandleRefFormField2(String strvalue, String displayType) {
        String[] arrValue;
        String returnstr = "";
        if (!strvalue.equals("")) {
            arrValue = strvalue.split(",");
            for (int i = 0; i < arrValue.length; i++) {
                int dot = arrValue[i].lastIndexOf(".");
                String requestNo = arrValue[i].substring(0, dot);
                String formSysId = arrValue[i].substring(dot + 1);
                if ("text".equalsIgnoreCase(displayType)) {
                    returnstr += requestNo + ", ";
                } else {
                    returnstr += "<a href=\"javascript:openCenterWindow('"
                            + getServerUrl()
                            + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&pop=true&requestNo="
                            + requestNo + "&formSystemId=" + formSysId + "',800,600)\">" + requestNo + "</a>, ";
                }
            }
            if (arrValue.length > 0) {
                returnstr = returnstr.substring(0, returnstr.length() - 2);
            }
        }
        return returnstr;
    }

    private static String HandleRefContract(String strvalue) {
        String[] arrValue;
        String returnstr = "";
        if (!strvalue.equals("")) {
            arrValue = strvalue.split(",");
            for (int i = 0; i < arrValue.length; i++) {
                returnstr += "<a href=\"javascript:openCenterWindow('" + getServerUrl()
                        + "/contractAction.it?method=editCntr&editType=edit&pop=true&contractNo=" + arrValue[i]
                        + "',800,600)\">" + arrValue[i] + "</a>, ";
            }
            if (arrValue.length > 0) {
                returnstr = returnstr.substring(0, returnstr.length() - 2);
            }
        }
        return returnstr;
    }

    private static String HandlingNumber(String str, FormSectionFieldVO field) {
        String tmpstr, capstr, mergedstr;
        NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
        if (field.getIsMoney() == 1) {
            tmpstr = usFormat.format(Double.parseDouble(str));
            capstr = MoneyCapital.parseMoney(str);
            mergedstr = tmpstr + "(" + capstr + ")";
        } else {
            // 显示时最少保留4位小数
            usFormat.setMaximumFractionDigits(4);
            mergedstr = usFormat.format(Double.parseDouble(str));
        }
        return mergedstr;
    }

    private static String HandlingNumber2(String str, FormSectionFieldVO field) {
        // System.out.println("str: " + str);
        if (str != null && !str.equals("") && field.getIsMoney() == 1 && field.getIsReadonly() == 1) {
            NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
            return usFormat.format(Double.parseDouble(str));
        } else {
            return str;
        }
    }

}
