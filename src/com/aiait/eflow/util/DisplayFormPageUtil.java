package com.aiait.eflow.util;

/*********************************************************
Task_ID	Author	Modify_Date	Description
IT1002     Robin   04/12/2008 DS-006 重构formDisplay.jsp页面中form显示部分，该类以后用来专门负责显示form所有内容。
                     在方法“displayFormWithContent”中，如果该section是表格，则不需要显示空白行，只显示列表标题（每个字段的名称）
IT1092	   Queenie 10/19/2009	Change Exception case type
IT1288	   Mario   04/01/2012   Make sections foldable
************************************************************/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.i18n.*;
import com.aiait.eflow.common.CommonName;
//import com.aiait.eflow.common.helper.FieldControlHelper;
import com.aiait.eflow.common.helper.FormFieldHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.formmanage.vo.FormSectionVO;
import com.aiait.eflow.housekeeping.dao.ParamConfigDAO;
import com.aiait.eflow.housekeeping.vo.ParamConfigVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;

/**
 * 该工具类的作用：
 *    用来动态生成每张Form：1.displayBlankForm(request):用户新申请时，生成空白form；
 *                       2.displayFormWithContent(request):用户查看或者修改时，根据已有form内容，生成填充好的form
 * @author asnpgj3
 *
 */
/**
 * @author asnpgye
 *
 */
public class DisplayFormPageUtil{
	public String imgStr, divBeginStr, divEndStr;
	public DisplayFormPageUtil(){
		ParamConfigHelper param = ParamConfigHelper.getInstance();
    	String isFoldaway = param.getParamValue("display_foldaway", "N");
    	    	
    	if("Y".equals(isFoldaway)){
    		imgStr = "<image border='0' framespacing='0' frameborder='0' id=\"imgcon@sectionId\" src=\"images/Rminus.png\" style=\"float:left;\" />";
    		divBeginStr = "<div id='div@sectionId' style=\"position:relative;zoom:1px;\">"; 
    		divEndStr = "</div>";
    	} else {
    		imgStr = "";
    		divBeginStr = ""; 
    		divEndStr = "";
    	}
	}
	public DisplayFormPageUtil(boolean isNonIE){
		if(isNonIE){
			imgStr = "";
	    	divBeginStr = ""; 
	    	divEndStr = "";
		}
	}
	private FormManageVO form;
	private HashMap sectionFieldMap;
	private HashMap onlyFillSectionFieldMap;
	private HashMap hiddenSectionFieldMap;
	private String requestNo;
	private WorkFlowProcessVO process;
	private String updateSections;
	private String newSectionFields;
	private String printFlag;
	private String currentNodeId;
	private String status;
	private StaffVO staff;
	private boolean isExceptionalCase = false;
	private String lockedStaffCode;
	
	public String getLockedStaffCode() {
		return lockedStaffCode;
	}

	public void setLockedStaffCode(String lockedStaffCode) {
		this.lockedStaffCode = lockedStaffCode;
	}

	public boolean getIsExceptionalCase() {
		return isExceptionalCase;
	}

	public void setIsExceptionalCase(boolean isExceptionalCase) {
		this.isExceptionalCase = isExceptionalCase;
	}

	public StaffVO getStaff() {
		return staff;
	}

	public void setStaff(StaffVO staff) {
		this.staff = staff;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentNodeId() {
		return currentNodeId;
	}

	public void setCurrentNodeId(String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public String getNewSectionFields() {
		return newSectionFields;
	}

	public void setNewSectionFields(String newSectionFields) {
		this.newSectionFields = newSectionFields;
	}

	public String getUpdateSections() {
		return updateSections;
	}

	public void setUpdateSections(String updateSections) {
		this.updateSections = updateSections;
	}

	public  FormManageVO getForm() {
		return form;
	}

	public void setForm(FormManageVO form) {
		this.form = form;
	}

	public HashMap getOnlyFillSectionFieldMap() {
		return onlyFillSectionFieldMap;
	}

	public void setOnlyFillSectionFieldMap(HashMap onlyFillSectionFieldMap) {
		this.onlyFillSectionFieldMap = onlyFillSectionFieldMap;
	}

	public WorkFlowProcessVO getProcess() {
		return process;
	}

	public void setProcess(WorkFlowProcessVO process) {
		this.process = process;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public HashMap getSectionFieldMap() {
		return sectionFieldMap;
	}

	public void setSectionFieldMap(HashMap sectionFieldMap) {
		this.sectionFieldMap = sectionFieldMap;
	}
	

	public HashMap getHiddenSectionFieldMap() {
		return hiddenSectionFieldMap;
	}
	public void setHiddenSectionFieldMap(HashMap hiddenSectionFieldMap) {
		this.hiddenSectionFieldMap = hiddenSectionFieldMap;
	}
	
	private  String getFormName(FormManageVO form){
		if(this.getLockedStaffCode()==null || "".equals(this.getLockedStaffCode().trim())){
		  return form.getFormName()+"("+form.getFormId()+")"+"<div  id='message'></div>";
		}else{
			return form.getFormName()+"("+form.getFormId()+")"+"<div id='message'>" + "( <font color='red'>"
			        +StaffTeamHelper.getInstance().getStaffNameByCode(this.getLockedStaffCode())+" locked the form </font>)</div>";	
		}
	}
	
	private  String showFormName(FormManageVO form,int sectionNum){
		StringBuffer str = new StringBuffer("");
        //show form's name
		str.append("<tr><td colspan='2' align='center' style='background-color:#D31145; height:50px;color:#FFFFFF;font-size:15px;'><b>").append(getFormName(form)).append("</b></td></tr>");
		//no form section to show
		if(sectionNum==0){
			str.append("<tr><td><font color='red'><b>The form has not any field !</font></td></tr>");
			str.append("</table>");
		}
		return str.toString();
	}
	
	/**
	 * 显示form(带内容)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public  String displayFormWithContent(HttpServletRequest request)throws Exception{
		//StringBuffer str = new StringBuffer("<table width='100%'  border='0' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>");
		StringBuffer str = new StringBuffer("");
		
		//Collection traceList = (ArrayList)request.getAttribute("traceList");
		Collection sectionList = this.getForm().getSectionList();
		int sectionNum = sectionList.size();
		  
		if(this.getPrintFlag()!=null && !"".equals(this.getPrintFlag())){
		   ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
		   String companyTitle = paramHelper.getParamValue(CommonName.COMPANY_NAME);
		   String companyLogoFilePrefix = paramHelper.getParamValue(CommonName.COMPANY_LOGO_FILE);
		   if(companyTitle==null || "".equals(companyTitle)){
			    	companyTitle = CommonName.AIAIT_COMPANY_TITLE;
		   }
		   if(companyLogoFilePrefix==null || "".equals(companyLogoFilePrefix)){
			   companyLogoFilePrefix = "aiait";
		   }
		   /*str.append("<tr><td colspan='2' align='center'><img src='" + request.getContextPath()+"/images/"+companyLogoFilePrefix+"_logo.jpg'></td></tr>")
		      .append("<tr><td colspan='2' align='center'> <b> <font size='3'>"+companyTitle+"</td></tr>");*/
		   str.append("<tr><td colspan='2' align='center'> <b> <font size='3'>"+companyTitle+"</td></tr>");
		}
		str.append(showFormName(this.getForm(),sectionNum));
		
		// Check if Deputy
		if ("deal".equals(request.getParameter("operateType"))) {
			if (this.process != null && this.process.isDealByDeputy()) {
				String staffName = StaffTeamHelper.getInstance()
						.getStaffNameByCode(this.process.getOriginProcessor())
						.trim();
				String strDeputy = I18NMessageHelper.getMessage(
						"form.deal_by_deputy", new String[] { "<b>", staffName, "</b>" });
				// System.out.println("-------- strDeputy: " + strDeputy);
				str.append("<tr><td colspan='2' align='center'>(* " + strDeputy
						+ ")</td></tr>");
			}
		}
		
		if(sectionNum==0) return str.toString();
		
		Iterator sectionIt = sectionList.iterator();
	    int sectionId = 1;
	    while(sectionIt.hasNext()){//遍历显示所有section
	    	FormSectionVO section =  (FormSectionVO)sectionIt.next();
	    	str.append(showSection(request,section,this.getSectionFieldMap(),sectionId));
	    	sectionId++;
	    }
	    
	  	//输出 * 间隔行
	    if(printFlag!=null && !"".equals(printFlag)){
	    	str.append("<tr><td colspan='2' align='center'>");
	         for(int i=0;i<80;i++){
	         	str.append("* ");
	         }
	        str.append("</td></tr>");
	    }
	    
	    
		return str.toString();
	}
	
	private StringBuffer showSection(HttpServletRequest request, FormSectionVO section, HashMap sectionFieldMap,
            int sectionId) {
				      
        StringBuffer sectionStr = new StringBuffer("");
        Collection fieldList = section.getFieldList();
        Iterator it1 = fieldList.iterator();
        int hiddenIndex=0;
        while(it1.hasNext()){
       	 FormSectionFieldVO field = (FormSectionFieldVO)it1.next();  
       	if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){ //id系统自动生成的序号，不需要列出来
 		     continue;
 	     }
       	  if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
       		hiddenIndex=1;
       	  }
        }
        if(hiddenIndex==0){
        	return sectionStr;
        }
        
        // Iterator it = fieldList.iterator();
        // int count = 1;
        String sectionType = section.getSectionType();
        // 显示该section的名称       
        sectionStr
                .append("<tr><td colspan='2' class='tdSectionTitle' onClick='disp(" + ""+ sectionId + ")'>" + imgStr.replaceAll("@sectionId", ""+sectionId)+"<b><span class='spSectionName'>" + sectionId + "</span> " + section.getSectionRemark() + ":</b></td></tr>")
                .append("<tr><td colspan='2'>");
        
        String _divBeginStr = divBeginStr.replaceAll("@sectionId", ""+sectionId);
        
        if (CommonName.FORM_SECTION_TYPE_TABLE.equals(sectionType)
                ) { // 01 -- 表格形式
        	sectionStr.append(_divBeginStr);
            sectionStr.append(getTableSection(request, section, sectionFieldMap, fieldList));
            sectionStr.append(divEndStr);
        }// end 表格形式
        else if (CommonName.FORM_SECTION_TYPE_JQGRID_TABLE.equals(sectionType)){
        	sectionStr.append(_divBeginStr);
            sectionStr.append(getJQgridTableSection(request, section, sectionFieldMap, fieldList));
            sectionStr.append(divEndStr);
        }
        else if (CommonName.FORM_SECTION_TYPE_ATTACHMENT.equals(sectionType)){
        	String tmp = getTableSection(request, section, sectionFieldMap, fieldList).toString();
        	if(tmp.indexOf("<div")>=0){
        		tmp = tmp.replace("<div", _divBeginStr+"<div");
        		tmp = tmp.replace("</div>", "</div></div>");
        	} else if(tmp.indexOf("<table")>=0){
        		tmp = tmp.replace("<table", _divBeginStr+"<table");
        		tmp = tmp.replace("</table>", "</table></div>");        		
        	}
            sectionStr.append(tmp);
            
        } else if ("02".equals(sectionType) || "03".equals(sectionType)) { // 02 form形式 03Base Information
        	String tmp = getCommonSection(request, section, sectionFieldMap, fieldList).toString().replaceAll("<table", _divBeginStr+"<table");
        	tmp = tmp.replaceAll("</table>", "</table>"+divEndStr);
            sectionStr.append(tmp);
            
        } else if ("04".equals(sectionType)) { // src
            sectionStr.append("<tr><td width='100%' colspan='6'>"+_divBeginStr+"<div id='ospan'>");
            sectionStr
                    .append("<iframe border=1 framespacing='0' frameborder='0' onload=\"iframeHeightAutoAdjust('ospan','iframeList')\" name='iframeList' scrolling='no' width='100%' src='"
                            + request.getContextPath()
                            + "/"
                            + section.getTableName()
                            + "?tempDate="
                            + (new Date()).getTime() + "'></iframe></div>");
            sectionStr.append(divEndStr);
            sectionStr.append("</td></tr>");
        }
        return sectionStr;
    }
	
	private StringBuffer getCommonSection(HttpServletRequest request, FormSectionVO section, HashMap sectionFieldMap, Collection fieldList) {
        String viewFlag = (String) request.getParameter("viewFlag");
        String dateFormat = (String) request.getAttribute("dateFormat");
        
        if(dateFormat==null || "".equals(dateFormat)){
        	dateFormat="MM/dd/yyyy";
        }
        
        int formColumn=getFormColumn();		//20150318 Justin Bin Added
        
        StringBuffer sectionStr = new StringBuffer("");
        String requestFormDate = "";
        Iterator it = fieldList.iterator();
        sectionStr
                .append("<tr><td colspan='2'><table id='sectionTable"
                        + section.getSectionId()
                        + "' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
        int count = 1;
        Collection fieldContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
        HashMap fieldContentMap = null;
        if (fieldContentList == null || fieldContentList.size() == 0) {
            fieldContentMap = new HashMap();
        } else {
            fieldContentMap = (HashMap) fieldContentList.iterator().next();
        }
        // SYSTEM FIELD: is_exceptional_case
        // 是否存在系统字段“is_exceptional_case”的标志，默认不存在;如果存在，如果该form是特例，则“isExceptionalCase”为true,否则为false
        boolean existIsExceptionalCase = false;
        // boolean isExceptionalCase = false;
        FormSectionFieldVO fieldPrv = null;
        int prvIsSingleRow = 0;
        while (it.hasNext()) {
            if (fieldPrv != null) {
                prvIsSingleRow = fieldPrv.getIsSingleRow();
            }
            FormSectionFieldVO field = (FormSectionFieldVO) it.next();
            if (existIsExceptionalCase == false && CommonName.SYSTEM_ID_IS_EXCEPTIONAL_CASE.equals(field.getFieldId())) {
                existIsExceptionalCase = true;
                String fieldValue = (String) fieldContentMap.get(field.getFieldId().toUpperCase());
                if (fieldValue != null && (CommonName.NORMAL_EXCEPTIONAL_CASE.equals(fieldValue) || CommonName.MIDNIGHT_EXCEPTIONAL_CASE.equals(fieldValue))) {
                    this.setIsExceptionalCase(true);
                }
            }
            fieldPrv = field;
            if ("request_date".equals(field.getFieldId())) {
                requestFormDate = StringUtil.getDateStr(new java.util.Date(), dateFormat+" HH:mm:ss");
            }
            // IT0958-----begin
            if ("1".equals("" + field.getIsSingleRow())) {
                sectionStr.append("<tr>");
                if (count > 1) {
                	if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
		        		count = count-1;
	        		}
                }
            } else if (count % formColumn == 1) {		//20150318 Justin Bin
                sectionStr.append("<tr>");
            } else if (prvIsSingleRow == 1) {
                sectionStr.append("<tr>");
                if (count > 1) {
                	if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
		        		count = count-1;
	        		}
                }
            }
            
            if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
            if ("1".equals("" + field.getIsSingleRow())) {
                sectionStr.append("<td isSingle='Yes' class='tr3' width='2%' align='right'>" + field.getFieldLabel()
                        + FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</td>");
                sectionStr.append("<td width='26%' colspan=5>");
            } else {
                sectionStr.append("<td isSingle='No' class='tr3' width='7%' align='right'>" + field.getFieldLabel()
                        + FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</td>");
                sectionStr.append("<td width='26%'>");
            }
            // IT0958-----end
            // 如果当前节点是“Begin(开始)“节点，并且该sectionField是需要在流转过程才可以输入的，则不需要requestor在申请时填写
            if ("0".equals(this.getProcess().getNodeId())
                    && this.getOnlyFillSectionFieldMap() != null
                    && this.getOnlyFillSectionFieldMap().containsKey(
                            (section.getSectionId() + "." + field.getFieldId()).toUpperCase())) {
                // out.println(FieldControlHelper.ShowDisabledField(field));
                sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),fieldContentMap, false,dateFormat, this.getForm().formSystemId));
            } else {// 1
                // if(this.getUpdateSections()!=null && !"".equals(this.getUpdateSections()) &&
                // this.getUpdateSections().indexOf(","+section.getSectionId()+"."+field.getFieldId())>-1){
                if (this.getUpdateSections() != null
                        && !"".equals(this.getUpdateSections())
                        && (this.getUpdateSections().indexOf(
                                "," + section.getSectionId() + "." + field.getFieldId() + ",") > -1 || (this
                                .getUpdateSections().endsWith("," + section.getSectionId() + "." + field.getFieldId())))) {
                    sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(),
                            fieldContentMap,dateFormat, this.getForm().formSystemId,false));
                    // }else if(this.getNewSectionFields()!=null && !"".equals(this.getNewSectionFields()) &&
                    // this.getNewSectionFields().indexOf(","+section.getSectionId()+"."+field.getFieldId())>-1){
                    // //当前处理环节才可以输入的sectionField
                } else if (this.getNewSectionFields() != null
                        && !"".equals(this.getNewSectionFields())
                        && (this.getNewSectionFields().indexOf(
                                "," + section.getSectionId() + "." + field.getFieldId() + ",") > -1 || (this
                                .getNewSectionFields()
                                .endsWith("," + section.getSectionId() + "." + field.getFieldId())))) { // 当前处理环节才可以输入的sectionField
                    sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(),
                            fieldContentMap,dateFormat , this.getForm().formSystemId,false));
                } else {// 2
                    if (this.getPrintFlag() != null && !"".equals(this.getPrintFlag())) {
                        if ("1".equals("" + field.getHighLevel())) {
                            // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01")+"</div>");
                            sectionStr.append("<div class='high_light'>"
                                    + FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(), 
                                    		fieldContentMap, true,dateFormat,this.getForm().formSystemId)
                                    + "</div>");
                        } else {
                            // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01"));
                            sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                            		fieldContentMap, true,dateFormat,this.getForm().formSystemId));
                        }
                    } else {// 3 begin
                        // 如果是‘拒绝状态’并且不是第一个节点，则只能显示该节点可以修改的field为可编辑状态(即其他节点都是只读的）
                        if ("00".equals(status) || "03".equals(status)) {
                            if ("false".equals(viewFlag)
                                    || ("03".equals(status) && !"0".equals(this.getProcess().getNodeId()))) {
                                sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                        fieldContentMap, true,dateFormat,this.getForm().formSystemId));
                            } else {
                                sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(),
                                        fieldContentMap,dateFormat , this.getForm().formSystemId,false));
                            }
                        } else {
                            // Highlight
                            if ("1".equals("" + field.getHighLevel())) {
                                // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status)+"</div>");
                                sectionStr.append("<div class='high_light'>"
                                        + FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                        		fieldContentMap, true,dateFormat,this.getForm().formSystemId)
                                        + "</div>");
                            } else {
                                // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status));
                                sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                        fieldContentMap, true,dateFormat,this.getForm().formSystemId));
                            }
                        }
                    }// 3 end
                }
            }// 1 end
            sectionStr.append("</td>");
            // IT0958 begin
            }
            if (("1".equals("" + field.getIsSingleRow())) || (count % formColumn == 0)) {		//20150318 Justin Bin
                sectionStr.append("</tr>");
            }
            if (!"1".equals("" + field.getIsSingleRow())) {
                count++;
            }
            // IT0958 young end
        }// -- while(it.hasNext())
        if (count % formColumn == 0) {			//20150318 Justin Bin Modified
            sectionStr.append("</tr>");
        }
        sectionStr.append("</table>");
        sectionStr.append("<script language=\"javascript\">");
        sectionStr.append("mergeColumn(\"sectionTable" + section.getSectionId() + "\")");
        sectionStr.append("</script>");
        sectionStr.append("</td></tr>");
        sectionStr.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>");
        return sectionStr;
    }
	
	private StringBuffer getTableSection(HttpServletRequest request, FormSectionVO section, HashMap sectionFieldMap, Collection fieldList) {
        String viewFlag = (String) request.getParameter("viewFlag");
        String dateFormat = (String) request.getAttribute("dateFormat");
        StringBuffer sectionStr = new StringBuffer("");
        Iterator it = fieldList.iterator();
        
        String sectioType = section.getSectionType();
        // 如果该sectionType是'附件'，并且是可以修改的状态（'00'--draft,'03'---reject）,则需要开放可以修改
        if (!"false".equals(viewFlag) && "00".equals(sectioType)
                && ("00".equals(this.getStatus()) || "03".equals(this.getStatus()) || ("deal".equals(request.getParameter("operateType")) 
                		&& this.process.getRequestStaffCode().equalsIgnoreCase(this.process.getCurrentProcessor())))) {
            sectionStr.append("<tr><td width='100%' colspan='6'><div id='aospan"+section.getSectionId()+"'>")
                    .append( "<iframe border=1 framespacing='0' frameborder='0' onload=\"iframeHeightAutoAdjust('aospan"+section.getSectionId()+"','iAttachementList"+section.getSectionId()+"')\" id=\"iAttachementList"+section.getSectionId()+"\" name=\"iAttachementList"+section.getSectionId()+"\" height='100%' width='100%' "
                                    + "src=" + request.getContextPath() + "/uploadAction.it?method=enter&requestFormDate="
                                    + "&requestNo=" + this.getRequestNo()
                                    + "&sectionId=" + section.getSectionId()
                                    + "&formSystemId=" + section.getFormSystemId() 
                                    + "&tempDate=" + new Date() + "></iframe>").append(
                            "</div></td></tr>");
        } else {
            sectionStr.append("<table id='formTable" + section.getSectionId() +  "' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
            sectionStr.append("<tr class='tr2'>");
            if (this.getPrintFlag() == null || "".equals(this.getPrintFlag())) {
                sectionStr.append("<td align='center' width='1%'><input type='checkbox' name='chkAll' onclick='selectAll(this,\"chkid_" + section.getSectionId() + "\")'>All</td>");
            }
            String defaultColumnWidth = 100 / fieldList.size() + "%";
            // 显示所有列头
            int SectionFieldcount = 0;
            int currentNodeonlyFillSectionFieldcnt = 0;
            boolean requesterEditable = false;
            while (it.hasNext()) {
                FormSectionFieldVO field = (FormSectionFieldVO) it.next();
                if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                    continue;
                }
                if (field.getControlsWidth() > 0) {
                    defaultColumnWidth = "" + field.getControlsWidth();
                }
                if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
                	sectionStr.append("<td  align='center' >" + field.getFieldLabel()
                            + FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</td>");
                }
                

                if (this.getOnlyFillSectionFieldMap() != null && this.getOnlyFillSectionFieldMap().containsKey(
                                (section.getSectionId() + "." + field.getFieldId()).toUpperCase())) {
                    currentNodeonlyFillSectionFieldcnt = currentNodeonlyFillSectionFieldcnt + 1;
                }
                SectionFieldcount = SectionFieldcount + 1;
            }

            if ("0".equals(this.getCurrentNodeId().trim()) && currentNodeonlyFillSectionFieldcnt != SectionFieldcount) {
                requesterEditable = true;
            }

            sectionStr.append("</tr>");
            // 开始显示每行数据
            sectionStr.append("<tbody id='tbDetailPrepare" + section.getSectionId() + "'>");
            int iFlag = 0;
            int rowIndex = 0;
            boolean canShowAddDelbutton = false;
            // 获取该表格形式section的结果记录集
            Collection fieldContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
            // 如果存在数据

            if (fieldContentList != null && fieldContentList.size() > 0) {
                Iterator fieldContentIt = fieldContentList.iterator();
                while (fieldContentIt.hasNext()) {// 01
                    sectionStr.append("<tr>");
                    HashMap fieldContent = (HashMap) fieldContentIt.next();
                    Object[] fieldArray = fieldList.toArray();
                    iFlag = 0;
                    for (int i = 0; i < fieldArray.length; i++) {// 02
                        FormSectionFieldVO field = (FormSectionFieldVO) fieldArray[i];
                        if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) { // 忽略系统递增字段(显示个隐藏字段)
                            sectionStr.append("<input type='hidden' name='" + section.getSectionId() + "_ID' value='"
                                    + (String) fieldContent.get(field.getFieldId().toUpperCase()) + "'>");
                            continue;
                        }
                        if ((iFlag == 0) && (this.getPrintFlag() == null || "".equals(this.getPrintFlag()))) {
                            sectionStr.append("<td align='center'><input type='checkbox' name='chkid_"
                                    + section.getSectionId() + "' value='" + field.getFieldId() + "'></td>");
                        }
                        
                        if(!this.getHiddenSectionFieldMap().containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
                        	
                        
                        sectionStr.append("<td align='left' style='word-wrap:break-word;table-layout:fixed;'>");// 00
                        // 如果当前节点是“Begin(开始)“节点，并且该sectionField是需要在流转过程才可以输入的，则不需要requestor在申请时填写
                        if ("0".equals(this.getProcess().getNodeId())
                                && this.getOnlyFillSectionFieldMap() != null
                                && this.getOnlyFillSectionFieldMap().containsKey(
                                        (section.getSectionId() + "." + field.getFieldId()).toUpperCase())) {
                            sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                            		null, false,dateFormat, this.getForm().formSystemId));
                        } else {// 1
                            // 当前处理环节有可以修改的sectionField
                            // if(this.getUpdateSections()!=null && !"".equals(this.getUpdateSections()) &&
                            // this.getUpdateSections().indexOf(","+section.getSectionId()+"."+field.getFieldId())>-1){
                            if (this.getUpdateSections() != null
                                    && !"".equals(this.getUpdateSections())
                                    && (this.getUpdateSections().indexOf(
                                            "," + section.getSectionId() + "." + field.getFieldId() + ",") > -1 || (this
                                            .getUpdateSections().endsWith("," + section.getSectionId() + "."
                                            + field.getFieldId())))) {
                                if (!canShowAddDelbutton) {
                                    canShowAddDelbutton = true;
                                }
                                sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(),
                                        fieldContent, rowIndex,dateFormat, this.getForm().formSystemId,true));
                                // }else if(this.getNewSectionFields()!=null && !"".equals(this.getNewSectionFields())
                                // &&
                                // this.getNewSectionFields().indexOf(","+section.getSectionId()+"."+field.getFieldId())>-1){
                                // //当前处理环节才可以输入的sectionField
                            } else if (this.getNewSectionFields() != null
                                    && !"".equals(this.getNewSectionFields())
                                    && (this.getNewSectionFields().indexOf(
                                            "," + section.getSectionId() + "." + field.getFieldId() + ",") > -1 || (this
                                            .getNewSectionFields().endsWith("," + section.getSectionId() + "."
                                            + field.getFieldId())))) { // 当前处理环节才可以输入的sectionField
                                // 当前处理环节才可以输入的sectionField
                                if (!canShowAddDelbutton) {
                                    canShowAddDelbutton = true;
                                }
                                sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(),
                                        fieldContent, rowIndex,dateFormat, this.getForm().formSystemId,true));
                            } else {// 2
                                if ("00".equals(this.getStatus())
                                        || ("03".equals(this.getStatus()) && "0".equals(this.getCurrentNodeId().trim()))) {
                                    canShowAddDelbutton = true;
                                }

                                if (this.getPrintFlag() != null && !"".equals(this.getPrintFlag())) {// 3
                                    // 如果有需要高亮度显示的标识，则需要加粗显示内容
                                    if ("1".equals("" + field.getHighLevel())) {
                                        sectionStr.append("<div class='high_light'>"
                                                + FormFieldHelper.showLabelField(field, this.getForm(),  this.getStaff(),
                                                		fieldContent, false,dateFormat, this.getForm().formSystemId) + "</div>");
                                    } else {
                                        sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                                fieldContent, false,dateFormat, this.getForm().formSystemId));
                                    }
                                } else {// 3


                                    if (!"false".equals(viewFlag)
                                            && ("00".equals(this.getStatus()) || ("03".equals(this.getStatus()) && "0"
                                                    .equals(this.getProcess().getNodeId())))) {
                                        sectionStr.append(FormFieldHelper.showEditField(field, this.getForm(), this.getStaff(), 
                                        		fieldContent, rowIndex,dateFormat , this.getForm().formSystemId,true));
                                    } else {
                                        if ("1".equals("" + field.getHighLevel())) {
                                            sectionStr.append("<div class='high_light'>"
                                                    + FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                                            fieldContent, true,dateFormat, this.getForm().formSystemId) + "</div>");
                                        } else {
                                            sectionStr.append(FormFieldHelper.showLabelField(field, this.getForm(), this.getStaff(),
                                                    fieldContent, true,dateFormat, this.getForm().formSystemId));
                                        }
                                    }

                                }// 3
                            }// 2
                        }// 1
                        sectionStr.append("</td>");// 00
                        }
                        iFlag++;
                        
                    }// 02
                    rowIndex++;
                    sectionStr.append("</tr>");
                }// 01 end while
            } else {// 否则没有数据
                if (this.getUpdateSections() != null && !"".equals(this.getUpdateSections())
                        && this.getUpdateSections().indexOf("," + section.getSectionId() + ".") > -1) {
                    // System.out.println("1--------section="+section.getSectionId()+","+this.getUpdateSections());
                    canShowAddDelbutton = true;
                } else if (getNewSectionFields() != null && !"".equals(getNewSectionFields())
                        && getNewSectionFields().indexOf("," + section.getSectionId() + ".") > -1) {
                    // System.out.println("2-------section="+section.getSectionId()+","+this.getNewSectionFields());
                    canShowAddDelbutton = true;
                } else if (requesterEditable) {
                    canShowAddDelbutton = true;
                }
                /*
                 * sectionStr.append("<tr>"); for(int i=0;i<fieldList.size();i++){
                 * sectionStr.append("<td align='left'>&nbsp;&nbsp;</td>"); } sectionStr.append("</tr>");
                 */
            }
            sectionStr.append("</tbody>");
            sectionStr.append("</table>");

            if ("false".equals(viewFlag)) {
                canShowAddDelbutton = false;
            }

            // Young edited 08/05/2008
            // if("00".equals(this.getStatus()) || ("03".equals(this.getStatus()) &&
            // "0".equals(this.getCurrentNodeId().trim()))){//Draf状态，或“拒绝”状态并且是回到第一个节点
            if (canShowAddDelbutton && !"00".equals(section.getSectionType())) {// 判断是否对当前section字段有权限
                sectionStr
                        .append("<br><input type='button' name='addRowBtn' value='Add' onclick='createTableSectionRow(\"formTable"
                                + section.getSectionId()
                                + "\",\""
                                + section.getSectionId()
                                + "\",\""
                                + this.getCurrentNodeId().trim()
                                + "\")'>&nbsp;&nbsp;<input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"formTable"
                                + section.getSectionId() + "\",\"chkid_" + section.getSectionId() + "\")'></td></tr>");
                //sectionStr.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>");
            } else if (("03".equals(this.getStatus()) && !"0".equals(this.getCurrentNodeId().trim()))
                    && (!"".equals(this.getUpdateSections() + this.getNewSectionFields()) && (this.getUpdateSections() + this
                            .getNewSectionFields()).indexOf("," + section.getSectionId()) > -1)) {// 拒绝状态，非第一个节点
                sectionStr
                        .append("<br><input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"formTable"
                                + section.getSectionId() + "\",\"chkid\")'></td></tr>");
            }
        }

        sectionStr.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>");
        return sectionStr;
    }
	
	private StringBuffer getJQgridTableSection(HttpServletRequest request, FormSectionVO section, HashMap sectionFieldMap, Collection fieldList){
		StringBuffer sectionStr = new StringBuffer("");
		return sectionStr;
	}
	
	/**
	 * 显示空白form
	 * @param request
	 * @return
	 */
	public  String displayBlankForm(HttpServletRequest request)throws Exception{
		
		StringBuffer str = new StringBuffer("");
		FormManageVO form = (FormManageVO)request.getAttribute("listForm");
		String dateFormat = (String) request.getAttribute("dateFormat");
		int formColumn=getFormColumn(); 		//20150318 Justin Bin Added
		
		
		if(dateFormat==null || "".equals(dateFormat)){
        	dateFormat="MM/dd/yyyy";
        }
		Date date = new Date();
		String attachmentIdentity = ""+date.getTime(); //附件的标适，与当前填写form关联的标示
		str.append("<input type='hidden' name='attachmentIdentity' value='" + attachmentIdentity + "'>");
		HashMap onlyFillSectionFieldMap = (HashMap)request.getAttribute("onlyFillSectionFieldMap");
		HashMap hiddenSectionFieldMap = (HashMap)request.getAttribute("hiddenSectionFieldMap");
		
		String type= (String)request.getParameter("type");
		Collection sectionList = form.getSectionList();
		int sectionNum = sectionList.size();
		String requestFormDate = "";
		str.append("<table width='96%'  style='border: 1px #D4D4D5 solid; background-color:#F0F0F0;' cellpadding='3' cellspacing='0' bordercolor='#D31145' style='border-collapse:collapse;'>");
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		str.append(showFormName(form,sectionNum));

		if(sectionNum==0) return str.toString();
		
		Iterator sectionIt = sectionList.iterator();
	    int sectionId = 1;
	    
	    while(sectionIt.hasNext()){//遍历显示所有section
	    	FormSectionVO section =  (FormSectionVO)sectionIt.next();
	    	Collection fieldList = section.getFieldList();
	    	int fieldNum = fieldList.size();
	        Iterator it = fieldList.iterator();
	        Iterator it1 = fieldList.iterator();
	        int hiddenIndex=0;
	        while(it1.hasNext()){
           	 FormSectionFieldVO field = (FormSectionFieldVO)it1.next();  
           	if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){ //id系统自动生成的序号，不需要列出来
     		     continue;
     	     }
           	  if(!hiddenSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
           		hiddenIndex=1;
           	  }
	        }
	        if(hiddenIndex==0){
	        	continue;
	        }
	        
	        
	        
	        int count = 1;
	    	String sectionType = section.getSectionType();	
    		//显示该section的名称	    	
	    	str.append("<tr><td colspan='2' class='tdSectionTitle' onClick='disp(" + ""+ sectionId + ")'>");
    		str.append(imgStr.replaceAll("@sectionId", ""+sectionId));
    		str.append("<b><span class='spSectionName'>"+sectionId+"</span>"+section.getSectionRemark()+":</b></td></tr>");
    		
    		    		
	    	if(CommonName.FORM_SECTION_TYPE_TABLE.equals(sectionType)){ // 01 -- 该section是表格形式
	    		//显示该section的具体字段
	    		
	    		boolean canshowadddeletebtn = true;
	    		int SectionFieldcount = 0;
	    		int currentNodeonlyFillSectionFieldcnt = 0;
	    		str.append("<tr><td colspan='2'>");
	    		//str.append("<table id='formTable"+sectionId+"' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#6595D6\" style=\"border-collapse:collapse;\">");
	    		str.append(divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<table id='formTable"+section.getSectionId()+"' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
	    		str.append("<tr  class='tr2'>")
	    		   .append("<td align='center' width='1%'><input type='checkbox' name='chkAll' onclick='selectAll(this,\"chkid_"+section.getSectionId()+"\")'>All</td>")
	    		   .append("");
	    		String defaultColumnWidth = "10";
                //遍历该section的所有字段，显示其中需要显示的字段的label
	    		while(it.hasNext()){
	            	 FormSectionFieldVO field = (FormSectionFieldVO)it.next();
	          	     if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){ //id系统自动生成的序号，不需要列出来
	          		     continue;
	          	     }
	          	     if(field.getColumnWidth()>0){
	          	         defaultColumnWidth = "" +field.getColumnWidth();
	          	     } 
	          	    if(!hiddenSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){              	  
	          	        str.append("<td align='center' width='"+defaultColumnWidth+"%' style='word-break:break-all;'>"+field.getFieldLabel()+ FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>");
	          	    }
	          	     
	          	    if(onlyFillSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
	          	    	currentNodeonlyFillSectionFieldcnt = currentNodeonlyFillSectionFieldcnt+1;
	          	    }
	          	     
	          	   SectionFieldcount = SectionFieldcount+1; 
	    		}
	    		str.append("</tr>")
	    		   .append("</table><br>");
	    		//显示“Add”与“Delete”按钮
	    		//System.out.println("SectionFieldcount:"+ SectionFieldcount+" ----currentNodeonlyFillSectionFieldcnt:"+currentNodeonlyFillSectionFieldcnt);
	    		if (currentNodeonlyFillSectionFieldcnt==SectionFieldcount){
	    			canshowadddeletebtn = false;
	    		}
	    		if (canshowadddeletebtn){
	    			str.append("<input type='button' name='addRowBtn' value='Add' onclick='createTableSectionRow(\"formTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+this.getCurrentNodeId().trim()+"\")'>&nbsp;&nbsp;<input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"formTable"+section.getSectionId()+"\",\"chkid_"+section.getSectionId()+"\")'>");
	    			str.append("<br><br>");
	    		}
	    		str.append(divEndStr+"</td></tr>");
           	 	str.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>"); 
	    		SectionFieldcount = 0;
	    		currentNodeonlyFillSectionFieldcnt=0;
	    	}else if("02".equals(sectionType) || "03".equals(sectionType)){// 02 -- Common形式; 03 -- Basic Information of Requester   
	    		String tableid = CommonName.MergedTableId;
	    		str.append("<tr><td colspan='2'>"+divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<table id="+tableid+" width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
	    		count = 1;
    	        FormSectionFieldVO fieldPrv=null;
    	        int prvIsSingleRow=0; //是否单独占用一行显示
    	        //遍历该section的所有字段，显示其中需要显示的字段
    	        while(it.hasNext()){
    	        	if (fieldPrv!=null){
    	    	    	prvIsSingleRow = fieldPrv.getIsSingleRow();
    	    	    }
    	        	FormSectionFieldVO field = (FormSectionFieldVO)it.next();
    	        	fieldPrv = field;
    	        	if("1".equals(""+field.getIsSingleRow())){ //需要单独一行
    	        		str.append("<tr>");
    	        		if (count>1){
    	        			if(!hiddenSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
    		        		count = count-1;
    	        			}
    		        	}   
    	        	}else if(count%formColumn==1){	//20150318 Justin Bin Added
    	        		str.append("<tr>");
    		        }else if(prvIsSingleRow==1){
    		        	str.append("<tr>");
    		        	if (count>1){
    		        		if(!hiddenSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
        		        		count = count-1;
        	        		}	
	        			}
    	 	        }
                    if(!hiddenSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
    	        		
    	        	
    	        	if("1".equals(""+field.getIsSingleRow())){
    	        		str.append("<td isSingle='Yes' class='tr3' width='2%' align = 'right'>"+field.getFieldLabel()+ FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>")
    	        		   .append("<td width='26%' colspan=5>");
    	        	}else{
    	        		str.append("<td isSingle='No' class='tr3' width='7%' align = 'right'>"+field.getFieldLabel()+ FormFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>")
    	        		   .append("<td width='26%'>");
    	        	}
                    
    	        	//如果该字段是只能在流转过程中修改的，则不需要requestor在申请时填写
    	        	if(onlyFillSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
                	   //str.append(FieldControlHelper.ShowDisabledField(field));
                		str.append(FormFieldHelper.showLabelField(field,form,currentStaff,null,false,dateFormat, form.formSystemId));
                	}else if("02".equals(sectionType)){ //common 形式
                		//str.append(FieldControlHelper.createControl(field,sectionType,1));
                		str.append(FormFieldHelper.showEditField(field,form,currentStaff,null,dateFormat, form.formSystemId,false));
                	}else if("03".equals(sectionType)){ //basic information 形式
                		//str.append(FieldControlHelper.createBaseInforControl(true,field,form,request));
                		str.append(FormFieldHelper.showEditField(field,form,currentStaff,null,dateFormat, form.formSystemId,false));
                		requestFormDate = StringUtil.getDateStr(new java.util.Date(),dateFormat+" HH:mm:ss");
                	}
                	
                	str.append("</td>");
                    }
                	if(("1".equals(""+field.getIsSingleRow())) || count%formColumn==0) {	//20150318
                		str.append("</tr>");
                	}
                	if(!"1".equals(""+field.getIsSingleRow())){
                		count++;
                	}
    	          }// end while
                  if(count%3==0){
                		str.append("</tr>");
                  }
                  str.append("</table>"+divEndStr+"</td></tr>")
                	 .append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>"); 
    	       } // end 02 / 03 section
	    	 if("00".equals(sectionType)){ //attachment
    	         str.append("<tr><td width='100%' colspan='6'>"+divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<div id='aospan"+sectionId+"'>")
    	            .append("<iframe border=1 framespacing='0' frameborder='0' onload=\"iframeHeightAutoAdjust('aospan"+sectionId+"','iAttachementList"+sectionId+"')\" id=\"iAttachementList"+sectionId+"\"  name=\"iAttachementList"+sectionId+"\" height='100%' width=\"100%\" src="
    	            		  +request.getContextPath()+"/uploadAction.it?method=enter&requestFormDate="+"&attachmentIdentity="+attachmentIdentity+"&tempDate="+(new Date().getTime())+"></iframe></div>")
    	            		  .append(""+divEndStr+"</td></tr>");
    	         str.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>"); 
    	     }
	    	 if("04".equals(sectionType)){
	    		 str.append("<tr><td width='100%' colspan='6'>"+divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<div id='ospan'>")
	  	       .append("<iframe border=1 framespacing='0' frameborder='0' onload=\"iframeHeightAutoAdjust('ospan','iframeList')\" name='iframeList' height='100%' width='100%'  scrolling='no' src='"
	  	    		    +request.getContextPath()+"/"+section.getTableName()+"?tempDate="+(new Date()).getTime()+"'></iframe></div>")
	  	       .append("</td></tr>");
	    		 
	    	 }
	    	 sectionId++;
	    }//end section while
	   
		return str.toString();
	}
	
	public String displayProcessTraceList(Collection traceList){
		StringBuffer str = new StringBuffer("");
		str.append("<tr>")
		    .append("<td colspan='2'>")
		    .append("<table width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#CDCDCD' style='border-collapse:collapse;'>")
		     .append("<tr  class='tr2'>")
		     .append("<td align='center'>Processed Node</td>")
		    .append("<td align='center'>Processed By</td>")
		    .append("<td align='center'>Processed Date</td>")
		   .append("<td align='center'>Processed Type</td>")
		    .append("<td align='center'>Processed Remark</td>")
		    .append("<td align='center'>Attachment</td>")
		    .append("</tr>");

		     if(traceList!=null && traceList.size()>0){
		        Iterator traceIt = traceList.iterator();  
		        while(traceIt.hasNext()){
		            	 WorkFlowProcessTraceVO traceVo = (WorkFlowProcessTraceVO)traceIt.next();
		        }
		     }
		return str.toString();
	}
	

	// 20150318 Justin Bin Added
	private Integer getFormColumn(){
		 int formColumn=3;
	        String tempColumnStr=ParamConfigHelper.getInstance().getParamValue(CommonName.PAGE_ROW);
	        if(tempColumnStr.matches("[0-9]") && !tempColumnStr.equals("")){
	        	if(Integer.parseInt(tempColumnStr)>0){
	        		formColumn=Integer.parseInt(tempColumnStr);
	        	}
	        }
	        return formColumn;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
