package com.aiait.eflow.reportmanage.util;

/*********************************************************
Task_ID	Author	Modify_Date	Description
IT1002     Robin   04/12/2008 DS-006 重构reportDisplay.jsp页面中report显示部分，该类以后用来专门负责显示report所有内容。
                     在方法“displayReportWithContent”中，如果该section是表格，则不需要显示空白行，只显示列表标题（每个字段的名称）
IT1092	   Queenie 10/19/2009	Change Exception case type
IT1288	   Mario   04/01/2012   Make sections foldable
************************************************************/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.reportmanage.helper.ReportFieldHelper;
import com.aiait.eflow.reportmanage.vo.ReportManageVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;


public class DisplayReportPageUtil {
	public String imgStr, divBeginStr, divEndStr;
	public DisplayReportPageUtil(){
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
	public DisplayReportPageUtil(boolean isNonIE){
		if(isNonIE){
			imgStr = "";
	    	divBeginStr = ""; 
	    	divEndStr = "";
		}
	}
	private ReportManageVO report;
	private HashMap sectionFieldMap;
	private HashMap onlyFillSectionFieldMap;
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

	public  ReportManageVO getReport() {
		return report;
	}

	public void setReport(ReportManageVO report) {
		this.report = report;
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

	private  String getReportName(ReportManageVO report){
		if(this.getLockedStaffCode()==null || "".equals(this.getLockedStaffCode().trim())){
		  return report.getReportName()+"<div  id='message'></div>";
		}else{
			return report.getReportName()+"<div id='message'>" + "( <font color='red'>"
			        +StaffTeamHelper.getInstance().getStaffNameByCode(this.getLockedStaffCode())+" locked the report </font>)</div>";	
		}
	}
	
	private  String showReportName(ReportManageVO report,int sectionNum){
		StringBuffer str = new StringBuffer("");
        //show report's name
		str.append("<tr><td colspan='2' align='center' style=' height:50px;color:#000000;font-size:15px;'><b>").append(getReportName(report)).append("</b></td></tr>");
		//no report section to show
		if(sectionNum==0){
			str.append("<tr><td><font color='red'><b>The report has not any field !</font></td></tr>");
			str.append("</table>");
		}
		return str.toString();
	}
	
	/**
	 * 显示report(带内容)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public  String displayReportWithContent(HttpServletRequest request)throws Exception{
		//StringBuffer str = new StringBuffer("<table width='100%'  border='0' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>");
		StringBuffer str = new StringBuffer("<div class='Wpage'>");
		//Collection traceList = (ArrayList)request.getAttribute("traceList");
		Collection sectionList = this.getReport().getSectionList();
		int sectionNum = sectionList.size();
		  					
		// Check if Deputy
		
		
		if(sectionNum==0) return str.toString();
		
		Iterator sectionIt = sectionList.iterator();
		Iterator sectionIt1 = sectionList.iterator();
	    int sectionId = 1;
	    int printFlat = 0;
	    HashMap<String,TreeSet<String>> remarkSectionMap = new HashMap<String, TreeSet<String>>();

	    while(sectionIt1.hasNext()){
	    	ReportSectionVO section =  (ReportSectionVO)sectionIt1.next();
	    	if(section.getRemakSection()!=null && !"".equals(section.getRemakSection())){
	    		remarkSectionMap.put(section.getRemakSection(), new TreeSet<String>());
	    	}
	    }
	    
	    
	    
	    while(sectionIt.hasNext()){//遍历显示所有section
	    	ReportSectionVO section =  (ReportSectionVO)sectionIt.next();
	    	
	    	if(printFlat==0){
	    	if("title".equals(section.getSectionId())){
	    		List<ReportSectionFieldVO> list = (List<ReportSectionFieldVO>) section.getFieldList();
	    		Collection fieldContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
	            HashMap fieldContentMap = null;
	            if (fieldContentList == null || fieldContentList.size() == 0) {
	                fieldContentMap = new HashMap();
	            } else {
	                fieldContentMap = (HashMap) fieldContentList.iterator().next();
	            }
	    		String fieldValue = (String) fieldContentMap.get(list.get(0).getFieldId().toUpperCase());
	    		if(fieldValue!=null && !"".equals(fieldValue) || (list.get(0).getDefaultValue()!=null && !"".equals(list.get(0).getDefaultValue()))){
	    			String alignStr = "";
	        		 if(list.get(0).getAligned()==1){
	        			 alignStr = "left";
	        		 }else if(list.get(0).getAligned()==2){
	        			 alignStr="center";
	        		 }else if(list.get(0).getAligned()==3){
	        			 alignStr="right";
	        		 }
	        		 String[] tmp = list.get(0).getCssStr().split(";");
	                 String[] valueTmp = tmp[1].split(",");	                 
	                 String valueCssStyle="left:"+valueTmp[0]+"px;top:"+valueTmp[1]+"px;font:"+valueTmp[3]+" "+valueTmp[4]+" "+valueTmp[2]+"px "+valueTmp[5]+";";
	    		    str.append("<div align='"+alignStr+"' style='height:50px;color:#000000;'><span style='position:relative;"+valueCssStyle+"'>").append((fieldValue!=null && !"".equals(fieldValue)) ? fieldValue:list.get(0).getDefaultValue()).append("</span></div>");
	    		    //str.append("<div align='"+alignStr+"' style='position:relative;"+valueCssStyle+"><b>").append((fieldValue!=null && !"".equals(fieldValue)) ? fieldValue:list.get(0).getDefaultValue()).append("</b></div>");
	    		}
	    	}else if("0A".equals(section.getSectionType()) || "09".equals(section.getSectionType())){	    		
	    		Collection paramContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
				Iterator paramIt = paramContentList.iterator();
                
				String print = "";
				String value = "";
				while (paramIt.hasNext()) {
					HashMap paramContentMap = (HashMap) paramIt.next();
					print = (String) paramContentMap.get("VARNAME");	
					if("@@PrintInd".equals(print.trim())){
						value = (String) paramContentMap.get("VARVALUE");	
						break;
					}
					
			    }
				if("N".equals(value.trim())){
					if(!"09".equals(section.getSectionType())){
						printFlat=1;
					}
					
				}else{
					int startIndex = str.length();
					int RCindex;
					str.append(showSection(request,section,this.getSectionFieldMap(),sectionId));
					if(remarkSectionMap.size()!=0){
					if(section.getRemakSection()!=null && !"".equals(section.getRemakSection())){						
						while (true) {
							RCindex = str.indexOf("@RC", startIndex);
							if(RCindex!=-1){
								remarkSectionMap.get(section.getRemakSection()).add(str.substring(RCindex+3, RCindex+9));
								startIndex = RCindex+9;
							}else{
								break;
							}						
						}
					
			    	}
					}
				}
	    		
	    	}else if("04".equals(section.getSectionType())){
	    		str.append(showSection(request,section,this.getSectionFieldMap(),sectionId));
	    		String strTMP = str.toString();
	    		if(remarkSectionMap.get(section.getSectionId())!=null && remarkSectionMap.get(section.getSectionId()).size()!=0){	    			    			
	    			int i = 1;
	    			for(String tmp :remarkSectionMap.get(section.getSectionId())){
	    				String remarkContent = (String)sectionFieldMap.get(tmp);

	    				strTMP = strTMP.replace("@Remark"+i+"</li>", remarkContent!=null?remarkContent:""+"</li>").replaceAll("@RC"+tmp, ""+i);
	    				i++;
	    			}
	    			for(int j =1;j<11;j++){	    		
	    				strTMP = strTMP.replaceAll("<li>@Remark"+j+"</li>","");
	    	    		    		
	    	    	}

	    			Collection paramContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());

	    			Iterator paramIt = paramContentList.iterator();

	    			while (paramIt.hasNext()) {
	    				HashMap paramContentMap = (HashMap) paramIt.next();		
	    				strTMP = strTMP.replaceAll((String) paramContentMap.get("VARNAME"),quoteReplacement((String) paramContentMap.get("VARVALUE") != null ? (String) paramContentMap.get("VARVALUE"): ""));    	 
	    		    }
	    			str = new StringBuffer(strTMP);
	    		}
	    		
	    	}else{
	    		int startIndex = str.length();
				int RCindex;
				str.append(showSection(request,section,this.getSectionFieldMap(),sectionId));
				if(remarkSectionMap.size()!=0){
				if(section.getRemakSection()!=null && !"".equals(section.getRemakSection())){						
					while (true) {
						RCindex = str.indexOf("@RC", startIndex);
						if(RCindex!=-1){
							remarkSectionMap.get(section.getRemakSection()).add(str.substring(RCindex+3, RCindex+9));
							startIndex = RCindex+9;
						}else{
							break;
						}						
					}
										
		    	}
				}
	    	}
	    	}else{
	    		if("0B".equals(section.getSectionType())){
	    			printFlat=0;
	    		}
	    	}
	    	sectionId++;
	    }
	    str.append("</div>");
	    
	    //System.out.println(str.toString());
	    int index1 = 0;
	    int index=0;
	    String strTmp="";
	    int headerEnd = 0;
	    int footerStart=0;
	    int footerEnd=0;
	    int tableHeaderdStart=0;
	    int tableHeaderdEnd=0;
	    int indexFlag =0;
	    int tmpIndex;
	    int trStr;
	    int trStr2;
	    int headerStart;
	    for(int j = 0;j<10;j++){
	    	headerStart = str.indexOf("@headerStart",index1);
	    	if(headerStart==-1){
    			break;
    		}	    
	    headerEnd = str.indexOf("@headerEnd",index1);
	    footerStart = str.indexOf("@footerStart",index1);
	    if(footerStart==-1){
			break;
		}
	    footerEnd = str.indexOf("@footerEnd",index1);
	    tableHeaderdStart = str.indexOf("@tableHeaderdStart",index1);
	    tableHeaderdEnd = str.indexOf("@tableHeaderdEnd",index1);
	    

	    trStr=headerEnd;
	    int trNo;
	    
        int remarkExist = str.indexOf("@RC",headerEnd);
        if(remarkExist==-1 || remarkExist>footerStart){
        	trNo = 25;
        }else{
        	trNo = 19;
        }
        if(remarkSectionMap.size()!=0){
        	trNo = 25;
        }
        if(report.getReportSystemId()==5){
        	trNo=27;
        }
        int sectionIndex = 1;
	    while(trStr<footerStart){
	    	indexFlag=0;
	    	for(int i=0;i<trNo;i++){
	    		tmpIndex = trStr;
		    	trStr=str.indexOf("<tr>", tmpIndex+1);
		    	//System.out.println(i+":"+trStr);
		    	if(trStr>footerStart || trStr==-1){
		    		trStr = tmpIndex;
		    		indexFlag = 1;
		    		break;
		    	}
	    	}
	    	if(indexFlag==1){
	    		strTmp = strTmp+str.substring(index, footerEnd);
	    		if(remarkSectionMap.size()==0){
	    		strTmp = replaceRenark(strTmp, headerEnd);
	    		}
	    		index = footerEnd;
	    		 for(int i =1;i<6;i++){	    		
	    	    	strTmp = strTmp.replaceAll("<li>@Remark"+i+"</li>","");	    	    		    		
	    	     }
	    		//System.out.println(str.substring(headerEnd, footerStart));
	    		break;
	    	}
	    	tmpIndex = trStr;
	    	trStr = str.lastIndexOf("@Weight", tmpIndex+1);
	    	//trStr1 = str.lastIndexOf("@Weight", tmpIndex+1);
	    	trStr2 = str.indexOf("@colPage", index+1);
	    	/*if(trStr==-1 || trStr >footerStart){
	    		trStr = tmpIndex;
	    	}*/
	    	if(trStr==-1 || trStr <headerEnd){
	    		trStr = tmpIndex;
	    	}
	    	int sectionid = str.lastIndexOf("reportTable",trStr);
			int sectionid1 = str.indexOf("'", sectionid);
			String sectionid2 = str.substring(sectionid+12, sectionid1);
	    	if(trStr2!=-1 && trStr2<trStr){
	    		trStr = trStr2;
	    		tableHeaderdStart = str.indexOf("@tableHeaderdStart",trStr2);
	    	    tableHeaderdEnd = str.indexOf("@tableHeaderdEnd",trStr2);
	    	    strTmp=strTmp+str.substring(index, trStr)+"</table></div><div style='height:20px'></div>"+str.substring(footerStart,footerEnd)+str.substring(headerStart,headerEnd)+"<div><table id='subSectionTable"+sectionid2+"_"+sectionIndex+"' class='reportTable_"+sectionid2+"' width='100%' style='border-collapse:collapse;'>";
		    	
	    	}else{
	    		strTmp=strTmp+str.substring(index, trStr)+"</table></div><div style='height:20px'></div>"+str.substring(footerStart,footerEnd)+str.substring(headerStart,headerEnd)+"<div><table id='subSectionTable"+sectionid2+"_"+sectionIndex+"' class='reportTable_"+sectionid2+"' width='100%' style='border-collapse:collapse;'>"+str.substring(tableHeaderdStart,tableHeaderdEnd);
		    	
	    	}

	    	index=trStr;
	    	if(remarkSectionMap.size()==0){
	    		strTmp = replaceRenark(strTmp, headerEnd);
	    	}
	    	sectionIndex++;    		
	    }
	    
	    index1 = footerEnd+1;
	    int headerStart1 = str.indexOf("@headerStart",index1);
    	if(headerStart1==-1){
    		strTmp = strTmp+str.substring(index, str.length());
    		
		}
	    	  	    
	    }
	    
	    
	    
	    
	    if(!"".equals(strTmp)){
	    for(int i =1;i<6;i++){	    		
    		strTmp = strTmp.replaceAll("<li>@Remark"+i+"</li>","");
    		    		
    	}
	    
	    strTmp = strTmp.replaceAll("@headerStart", "").replaceAll("@headerEnd", "")
	    .replaceAll("@footerStart", "").replaceAll("@footerEnd", "").replaceAll("@tableHeaderdStart", "").replaceAll("@colPage", "")
	    .replaceAll("@tableHeaderdEnd", "").replaceAll("@Weight", "").replaceAll("<span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'></div>", "")
			.replaceAll("<div class='Wpage'></div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span>", "");
	    //System.out.println(strTmp);
	    return strTmp;
	    }else{
	    	//System.out.println(str.toString());
	    	return str.toString().replaceAll("@headerStart", "").replaceAll("@headerEnd", "").replaceAll("@colPage", "")
		    .replaceAll("@footerStart", "").replaceAll("@footerEnd", "").replaceAll("@tableHeaderdStart", "")
		    .replaceAll("@tableHeaderdEnd", "").replaceAll("@Weight", "").replaceAll("<div class='Wpage'></div>", "");
	    }
	}
	private String replaceRenark(String strTmp, int headerEnd) {
		int RCindex;
		int RCindex1=headerEnd;

		TreeSet<String> treeset = new TreeSet<String>();
		while (true) {
			RCindex = strTmp.indexOf("@RC", RCindex1);
			if(RCindex!=-1){
				treeset.add(strTmp.substring(RCindex+3, RCindex+9));
				RCindex1 = RCindex+9;
			}else{
				break;
			}						
		}
		if(treeset.size()!=0){
			
			int sectionid2 = strTmp.indexOf("@Remark1");
			
			int i = 1;
			for(String tmp :treeset){
				
				String remarkContent = (String)sectionFieldMap.get(tmp);
				strTmp = strTmp.replace("@Remark"+i+"</li>", remarkContent!=null?remarkContent:""+"</li>").replaceAll("@RC"+tmp, ""+i);
				i++;
			}
			for(int j =1;j<11;j++){	    		
	    		strTmp = strTmp.replaceAll("<li>@Remark"+j+"</li>","");
	    		    		
	    	}
			int sectionid = strTmp.indexOf("sectionTable",sectionid2-500);
			int sectionid1 = strTmp.indexOf("'", sectionid);
			Collection paramContentList = (ArrayList) sectionFieldMap.get(strTmp.substring(sectionid+12, sectionid1));

			Iterator paramIt = paramContentList.iterator();

			while (paramIt.hasNext()) {
				HashMap paramContentMap = (HashMap) paramIt.next();		
				strTmp = strTmp.replaceAll((String) paramContentMap.get("VARNAME"),quoteReplacement((String) paramContentMap.get("VARVALUE") != null ? (String) paramContentMap.get("VARVALUE"): ""));    	 
		    }
		}
		
		return strTmp;
	}
	
	private StringBuffer showSection(HttpServletRequest request, ReportSectionVO section, HashMap sectionFieldMap,
            int sectionId) {
        StringBuffer sectionStr = new StringBuffer("");
        Collection fieldList = section.getFieldList();
        // Iterator it = fieldList.iterator();
        // int count = 1;
        String sectionType = section.getSectionType();
        // 显示该section的名称       

        
        String _divBeginStr = divBeginStr.replaceAll("@sectionId", ""+sectionId);
        
        if (CommonName.FORM_SECTION_TYPE_TABLE.equals(sectionType)) { // 01 -- 表格形式
        	//sectionStr.append(_divBeginStr);
            sectionStr.append(getTableSection(request, section, sectionFieldMap, fieldList));
            //sectionStr.append(divEndStr);
        }// end 表格形式
        else if ("02".equals(sectionType) || "03".equals(sectionType)) { // 02 report形式 03Base Information
        	//String tmp = getCommonSection(request, section, sectionFieldMap, fieldList).toString().replaceAll("<table", _divBeginStr+"<table");
        	String tmp = getCommonSection(request, section, sectionFieldMap, fieldList).toString();
        	//tmp = tmp.replaceAll("</table>", "</table>"+divEndStr);
            sectionStr.append(tmp);
            
        } else if ("06".equals(sectionType)) {
        	//sectionStr.append(_divBeginStr);
        	
        	sectionStr.append(getDefinedTableSection(request, section, sectionFieldMap, fieldList));
        	//sectionStr.append(divEndStr);
        } else if ("0A".equals(sectionType)) {
        	// src
        	sectionStr.append("@headerStart");
        	sectionStr.append("</div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'>");        	
        	//sectionStr.append(_divBeginStr);
        	sectionStr.append(getHtmlSection(section,request));
        	//sectionStr.append(divEndStr);
        	sectionStr.append("@headerEnd");
        }else if ("0B".equals(sectionType)) {
        	// src
        	sectionStr.append("@footerStart");
        	//sectionStr.append(_divBeginStr);
        	sectionStr.append(getHtmlSection(section,request));
        	//sectionStr.append(divEndStr);
        	sectionStr.append("</div><div style='height:20px'></div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'>");
        	sectionStr.append("@footerEnd");
        }else if("08".equals(sectionType)){
        	//sectionStr.append(_divBeginStr);
        	sectionStr.append(getPageSection(request, section, sectionFieldMap, fieldList));      	
        	//sectionStr.append(divEndStr);
        	
        }else if("09".equals(sectionType) || "04".equals(sectionType)){
        	sectionStr.append(getHtmlSection(section,request));
        }
        
        return sectionStr;
    }
	
	//Tytron20150522
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	public static String addComma(String str){  
	    boolean neg = false;  
	    if (str.startsWith("-")){  //处理负数  
	        str = str.substring(1);  
	        neg = true;  
	    }  
	    String tail = null;  
	    if (str.indexOf('.') != -1){ //处理小数点  
	        tail = str.substring(str.indexOf('.'));  
	        str = str.substring(0, str.indexOf('.'));  
	    }  
	    StringBuilder sb = new StringBuilder(str);  
	    sb.reverse();  
	    for (int i = 3; i < sb.length(); i += 4){  
	        sb.insert(i, ',');  
	    }  
	    sb.reverse();  
	    if (neg){  
	        sb.insert(0, '-');  
	    }  
	    if (tail != null){  
	        sb.append(tail);  
	    }  
	    return sb.toString();  
	} 
	
	//Tytron20150522
	private String getDefinedTableSection(HttpServletRequest request, ReportSectionVO section, HashMap sectionFieldMap,
            Collection fieldList) {
    
            int colNo = Integer.parseInt((String) sectionFieldMap.get(section.getSectionId()+"_colNo"));
            int rowNo = Integer.parseInt((String) sectionFieldMap.get(section.getSectionId()+"_rowNo"));
                         
            // 开始显示每行数据          
            int iFlag = 0;
            int rowIndex = 0;           
            // 获取该表格形式section的结果记录集
            Collection fieldContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
            // 如果存在数据
            if (fieldContentList != null && fieldContentList.size() > 0) {
            	Iterator fieldContentIt = fieldContentList.iterator();
            	Iterator tmpIt = fieldContentList.iterator();         	
            	int colPageNo =0;
            	for (int j = 0; j < colNo; j++) {// 02                	
                	if(tmpIt.hasNext()){
                	HashMap fieldContent = (HashMap) tmpIt.next();
                    String style = (String)fieldContent.get("class".toUpperCase());
                    style = style.trim();
                    if("h0_P".equals(style.substring(0, style.length()-2))){
                    	colPageNo++;
                    }
                	}
                }
            	if(colPageNo==0){
            		colPageNo = 1;
            	}
            	if(section.getPageColCount()==0){
            		section.setPageColCount(colNo);
            	}
            	Double colCount = section.getPageColCount()*1.0;
            	int PageNo = (int)Math.ceil(colPageNo/colCount); 
                
            	String sectionStr[] = new String[PageNo];
            	for(int t=0;t<PageNo;t++){
            		sectionStr[t] = "";
            	}            	         	
                for(int i =0;i<rowNo;i++){// 01   
                	if(i==0){
                		for(int t=0;t<PageNo;t++){
                			sectionStr[t] +="@tableHeaderdStart<tr class='tr2'>";
                		}                		              		
                	}else{
                		for(int t=0;t<PageNo;t++){
                			sectionStr[t] +="<tr>";
                		}               		
                	}               	                                                  
                    iFlag = 0;
                    int arrIndex = 0;
                    int ColSpanNo=1;
                    String lastValue="<**>";
                    int MergedFalt = 0;
                    for (int j = 0; j < colNo; j++) {// 02                    	
                    	if(fieldContentIt.hasNext()){
                    	HashMap fieldContent = (HashMap) fieldContentIt.next();
                    	String style = (String)fieldContent.get("class".toUpperCase());
                    	style = style.trim();
                    	String Merged = (String)fieldContent.get("Merged".toUpperCase());
                    	String itemvalue = (String)fieldContent.get("itemvalue".toUpperCase());           
              
                    	//得改
                    	String edit = (String)fieldContent.get("Editable".toUpperCase());
                    	String id  = (String)fieldContent.get("id".toUpperCase());
                    	if("Y".equals(Merged.trim())){
                    		
                    		if(lastValue.equals(itemvalue.trim()) && MergedFalt==1){
                    			
                    			ColSpanNo++;
                    			if(arrIndex != ((j-colNo+colPageNo)/section.getPageColCount())){
                    				for(int t=0;t<PageNo;t++){
                    					if(t==PageNo-1){
                    						sectionStr[t] = sectionStr[t].replace("@ColSpanNo", ""+(ColSpanNo-1-section.getPageColCount()+colPageNo%section.getPageColCount()));
                    					}else{                 					                					
                            			sectionStr[t] = sectionStr[t].replace("@ColSpanNo", ""+(ColSpanNo-1));
                    					}          
                            		}
                    				//lastValue="";
                    				
                    				if(ColSpanNo>(section.getPageColCount()+colNo-colPageNo)){
                    					
                    					for(int l=j+1;l<colNo;l++ ){
                    						fieldContentIt.next();
                    					}
                    					break;
                    				}
                    				ColSpanNo = 1;
                    				arrIndex = (j-colNo+colPageNo)/section.getPageColCount();                				
                    			}else{
                    				if(j==colNo-1){
                    					lastValue="<**>";
                    					for(int t=0;t<PageNo;t++){
                                			sectionStr[t] = sectionStr[t].replace("@ColSpanNo", ""+(ColSpanNo));
                                			          
                                		}
                    					ColSpanNo = 1;
                    					
                    				}
                    				continue;
                    			}
                    			
                    			
                    		}else{
                    			if(!"<**>".equals(lastValue)){
                    				for(int t=0;t<PageNo;t++){
                            			sectionStr[t] = sectionStr[t].replace("@ColSpanNo", ""+ColSpanNo);
                            			          
                            		}
                    				//lastValue="";
                    				ColSpanNo = 1;
                    			}
                    		}
                   		
                            	for(int t=0;t<PageNo;t++){
                            		
                            		if(j<colNo-colPageNo){
                            			if("Y".equals(edit.trim())){                           				
                            				sectionStr[t] += "<td class='"+style+"_Merge' colspan='@ColSpanNo' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>";
                            			}else{
                            				sectionStr[t] += "<td class='"+style+"_Merge' colspan='@ColSpanNo'>";
                            			}
                            			
                            		}else { 
                            			if("Y".equals(edit.trim())){
                            				if(j==colNo-1 && ColSpanNo==1){
                            					sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"' colspan='@ColSpanNo' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>";
                            				}else{
                            					sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"_Merge' colspan='@ColSpanNo' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>";
                            				}
                            				
                            			}else{
                            				if(j==colNo-1 && ColSpanNo==1){
                            					sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"' colspan='@ColSpanNo'>";                         			    
                            				}else{
                            					sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"_Merge' colspan='@ColSpanNo'>";                           					
                            				}
                            			}
                            			
                            			break;
                            		}                  			
                        		} 
                            	// 00
                            	
                             
                    		if(j==colNo-1){
            					lastValue="<**>";
            					for(int t=0;t<PageNo;t++){
                        			sectionStr[t] = sectionStr[t].replace("@ColSpanNo", ""+(ColSpanNo));
                        			          
                        		}
            					ColSpanNo = 1;
            					
            				}
                    		MergedFalt=1;
                    	}else{
                    		MergedFalt=0;  
                    		
                    		
                        	for(int t=0;t<PageNo;t++){
                        		if(j<colNo-colPageNo){
                        			if("Y".equals(edit.trim())){
                        				sectionStr[t] += "<td class='"+style+"' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>";
                        			}else{
                        				sectionStr[t] += "<td class='"+style+"'>";
                        			}
                        			
                        		}else {
                        			if("Y".equals(edit.trim())){
                        				sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>";
                        			}else{
                        				sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += "<td class='"+style+"'>";
                        			}
                        			
                        			break;
                        		}                  			
                    		} 
                        	// 00
                        	
                         
                    	}                   
                      String remark = (String)fieldContent.get("RemarkCode".toUpperCase());
                      String remarktmp="";
                      String remarkStr="";
                      if(remark!=null&& !"".equals(remark.trim())){
                            String[] remarks = remark.split(",");
                            for(int k =0;k<remarks.length;k++){
                                	remarktmp = remarktmp+",@RC"+remarks[k];
                            }
                           remarkStr="<span class='remarkNo'>"+remarktmp.substring(1)+"</span>";
                      } 
                      String lablecss = (String)fieldContent.get("class".toUpperCase());
                     // String cssStr="";
                      if(lablecss!=null && !"".equals(lablecss.trim())){
                          
                    	  if("h1".equals(lablecss.substring(0, 2))){
                    		  if(j==0){  
                            	 for(int t=0;t<PageNo;t++){
                            		 int index = sectionStr[t].lastIndexOf("<tr>");
                            		 if(index!=-1){
                            			 StringBuffer tmp = new StringBuffer(sectionStr[t]);
                            			 tmp.replace(index, index+4, "@Weight<tr>");
                            			 sectionStr[t] = tmp.toString();
                            		 }
                                  } 
                    		  } 
                                  //cssStr="font-weight:800;font-size:13px";
                             }else if("h2".equals(lablecss.trim())){
                                			 //cssStr="font-weight:800;font-size:12px";
                             }
                          
                      }
                      
                      String tmp = "";
              		if(isNumeric(itemvalue.trim())){
              			tmp = addComma(itemvalue.trim());
              		}else{
              			tmp = itemvalue.trim();
              		}
                      
                      for(int t=0;t<PageNo;t++){
 //  列分页可编辑               	  
                  		if(j<colNo-colPageNo){
                  		
                  			if("Y".equals(edit.trim())){
                  				sectionStr[t] += (itemvalue==null || "".equals(itemvalue.trim()))?"&nbsp":("<span id='text_"+section.getSectionId()+"_"+id+"'>"+tmp+"</span>"+remarkStr+"<input type='text' id='id_"+section.getSectionId()+"_"+id+"' style='display:none' name='field_ID' value='"+itemvalue.trim()+"' onblur='saveFileBox(\""+section.getSectionId()+"_"+id+"\")'></td>");
                  			}else{
                  				sectionStr[t] += (itemvalue==null || "".equals(itemvalue.trim()))?"&nbsp":(tmp+remarkStr+"</td>");
                  			}
                  			
                  		}else {
                  			if("Y".equals(edit.trim())){
                  				sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += (itemvalue==null || "".equals(itemvalue.trim()))?"&nbsp":("<span id='text_"+section.getSectionId()+"_"+id+"'>"+tmp+"</span>"+remarkStr+"<input type='text' id='id_"+section.getSectionId()+"_"+id+"' style='display:none' name='field_ID' value='"+itemvalue.trim()+"' onblur='saveFileBox(\""+section.getSectionId()+"_"+id+"\")'></td>");
                  			}else{
                  				sectionStr[((j-colNo+colPageNo)/section.getPageColCount())] += (itemvalue==null || "".equals(itemvalue.trim()))?"&nbsp":(tmp+remarkStr+"</td>");
                  			}
                  			
                  		    break;
                  		}                  			
              		} 
                      lastValue = itemvalue.trim();
                      iFlag++;
                      
                    }else{
                    	for(int t=0;t<PageNo;t++){
                			sectionStr[t] +="<td>&nbsp</td>";
                		}
                    }
                    	
                    	arrIndex = (j-colNo+colPageNo)/section.getPageColCount();
                    	
                    }// 02
                    rowIndex++;
                    
                    if(i==0){
                    	for(int t=0;t<PageNo;t++){
                			sectionStr[t] +="</tr>@tableHeaderdEnd";
                		}
                	}else{
                		for(int t=0;t<PageNo;t++){
                			sectionStr[t] +="</tr>";
                		}
                	}
                   
                }// 01 end while
               
                String allSectionStr ="";
                
                for(int t=0;t<PageNo;t++){
                	//System.out.println(sectionStr[t]);
                	allSectionStr =  allSectionStr+sectionStr[t]+"@colPage";
        		} 
              
                allSectionStr="<div><table id='reportTable"
                    + section.getSectionId()
                    + "' width=\"100%\" class='reportTable_"+section.getSectionId()+"' cellpadding=\"3\"  bordercolor=\"#CDCDCD\" cellspacing=\"0\"  style=\"border-collapse:collapse;\">"
                    +allSectionStr
                    +"</table></div><div style='height:20px'></div>";
                
                return allSectionStr;
            
            } else {// 否则没有数据
            	
            	System.out.println("否则没有数据");
                
                return ""; 
                
            }
                 

        
    }
	
	
	private StringBuffer getHtmlSection(ReportSectionVO section,HttpServletRequest request) {

        
        StringBuffer sectionStr = new StringBuffer("");
        if("09".equals(section.getSectionType())){
        	sectionStr.append("</div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'><div id='sectionTable"+ section.getSectionId()+"'>");
        }else if("0A".equals(section.getSectionType()) || "04".equals(section.getSectionType())){
        	sectionStr.append("<div id='sectionTable"+ section.getSectionId()+"'>");
        }else if("0B".equals(section.getSectionType())){
        	sectionStr.append("<div id='sectionTable"+ section.getSectionId()+"' style='position:absolute;bottom:20px'>");
        }
        
        String HtmlTmp = section.getHtmlCode()!=null?section.getHtmlCode().replace("@path",request.getContextPath()):"";
        
        Collection paramContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());	
        
        Iterator paramIt = paramContentList.iterator();
        
        

	    while(paramIt.hasNext()){
	    	 HashMap paramContentMap =  (HashMap)paramIt.next();
	    	 /*for(Object key: paramContentMap.keySet()){
	    		 System.out.println("key:"+key+",value:"+paramContentMap.get(key));
	    	 }*/
	    	 if(!"".equals(HtmlTmp)){
	    		 String edit = (String)paramContentMap.get("EDITABLE");
	    		 String name = (String)paramContentMap.get("VARNAME");
	    		 String value = (String)paramContentMap.get("VARVALUE");
	    		 String id = (String)paramContentMap.get("ID");
	    		 if("Y".equals(edit)){
	    			HtmlTmp = HtmlTmp.replaceAll(name,
	    					 quoteReplacement(value!=null?
	    					 "<span id='text_"+section.getSectionId()+"_"+id+"' ondblclick='showEditBox(\""+section.getSectionId()+"_"+id+"\")'>"+value
	    					 +"</span><input type='text' id='id_"+section.getSectionId()+"_"+id+"' style='display:none' name='field_ID' value='"
	    					 +value+"' onblur='saveFileBox(\""+section.getSectionId()+"_"+id+"\")'>":""));
	    		 }else{
	    			 HtmlTmp = HtmlTmp.replaceAll((String)paramContentMap.get("VARNAME"),
	    					 quoteReplacement((String)paramContentMap.get("VARVALUE")!=null?(String)paramContentMap.get("VARVALUE"):""));		    		  
	    		 }
	    		 	    		 
	    	 }
	    	 
	    }
        sectionStr.append(HtmlTmp);
                     
        sectionStr.append("</div>");
        if("0A".equals(section.getSectionType()) || "04".equals(section.getSectionType())){
        	sectionStr.append("<div style='height:20px'></div>");
        }else if("09".equals(section.getSectionType())){
        	sectionStr.append("</div><div style='height:20px'></div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'>");
        }
        return sectionStr;
    }
	
	public static String quoteReplacement(String s) {   
        if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))   
            return s;   
        StringBuffer sb = new StringBuffer();   
        for (int i=0; i<s.length(); i++) {   
            char c = s.charAt(i);   
            if (c == '\\') {   
                sb.append('\\'); sb.append('\\');   
            } else if (c == '$') {   
                sb.append('\\'); sb.append('$');   
            } else {   
                sb.append(c);   
            }   
        }   
        return sb.toString();   
    }   
	
	
	private StringBuffer getPageSection(HttpServletRequest request, ReportSectionVO section, HashMap sectionFieldMap,
            Collection fieldList) {
        String viewFlag = (String) request.getParameter("viewFlag");
        String dateFormat = (String) request.getAttribute("dateFormat");
        if(dateFormat==null || "".equals(dateFormat)){
        	dateFormat="MM/dd/yyyy";
        }
        int line = 2;
        
        StringBuffer sectionStr = new StringBuffer("");
        String requestReportDate = "";
        Iterator it = fieldList.iterator();
        sectionStr
                .append("</div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'><div><table id='sectionTable"
                        + section.getSectionId()
                        + "' width=\"100%\" class='reportTable' cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
        int count = 1;
        Collection fieldContentList = (ArrayList) sectionFieldMap.get(section.getSectionId());
        HashMap fieldContentMap = null;
        if (fieldContentList == null || fieldContentList.size() == 0) {
            fieldContentMap = new HashMap();
        } else {
            fieldContentMap = (HashMap) fieldContentList.iterator().next();
        }
        // SYSTEM FIELD: is_exceptional_case
        // 是否存在系统字段“is_exceptional_case”的标志，默认不存在;如果存在，如果该report是特例，则“isExceptionalCase”为true,否则为false
        boolean existIsExceptionalCase = false;
        // boolean isExceptionalCase = false;
        ReportSectionFieldVO fieldPrv = null;
        int prvIsSingleRow = 0;
        while (it.hasNext()) {
            if (fieldPrv != null) {
                prvIsSingleRow = fieldPrv.getIsSingleRow();
            }
            ReportSectionFieldVO field = (ReportSectionFieldVO) it.next();
 
            fieldPrv = field;
            
            // IT0958-----begin
            if ("1".equals("" + field.getIsSingleRow())) {
                sectionStr.append("<tr>");
                if (count > 1) {
                    count = count - 1;
                }
            } else if (count % line == 1) {
                sectionStr.append("<tr>");
            } else if (prvIsSingleRow == 1) {
                sectionStr.append("<tr>");
                if (count > 1) {
                    count = count - 1;
                }
            }
            
            String borderstyle = "";
            if("1".equals(""+field.getBorder())){
            	borderstyle ="border-top:1px solid #cdcdcd;";
            }else if("2".equals(""+field.getBorder())){
            	borderstyle ="border-bottom:1px solid #cdcdcd;";
            }else if("3".equals(""+field.getBorder())){
            	borderstyle ="border-left:1px solid #cdcdcd;";
            }else if("4".equals(""+field.getBorder())){
            	borderstyle ="border-right:1px solid #cdcdcd;";
            }else if("5".equals(""+field.getBorder())){
            	borderstyle ="border-top:1px solid #cdcdcd;border-bottom:1px solid #cdcdcd;";
            }else if("6".equals(""+field.getBorder())){
            	borderstyle ="border-left:1px solid #cdcdcd;border-right:1px solid #cdcdcd;";
            }else if("7".equals(""+field.getBorder())){
            	borderstyle ="border-top:1px solid #cdcdcd;border-left:1px solid #cdcdcd;";
            }else if("8".equals(""+field.getBorder())){
            	borderstyle ="border-right:1px solid #cdcdcd;border-bottom:1px solid #cdcdcd;";
            }else if("9".equals(""+field.getBorder())){
            	borderstyle ="border:1px solid #cdcdcd;";
            }
            
            String[] tmp = field.getCssStr().split(";");
            String[] textTmp = tmp[0].split(",");
            String[] valueTmp = tmp[1].split(",");
            String textCssStyle="left:"+textTmp[0]+"px;top:"+textTmp[1]+"px;font:"+textTmp[3]+" "+textTmp[4]+" "+textTmp[2]+"px "+textTmp[5]+";";
            String valueCssStyle="left:"+valueTmp[0]+"px;top:"+valueTmp[1]+"px;font:"+valueTmp[3]+" "+valueTmp[4]+" "+valueTmp[2]+"px "+valueTmp[5]+";";
            String align="left";
			  if("2".equals(""+field.getAligned())){
				 align="center";
			  }else if("3".equals(""+field.getAligned())){
				 align="right";
			  }
            if("2".equals(""+field.getFieldType())){
            	field.setFieldComments(field.getFieldComments().replace("@path",request.getContextPath()));
            	 
            	if("0".equals(""+field.getControlsHeight())){
            		sectionStr.append("<td colspan=4 class='' isSingle='Yes' align='"+align+"'><div style='position:relative;"+valueCssStyle+"'>"+field.getFieldComments()+"</div></td>"); 
   	 			 }else{
   	 				sectionStr.append("<td colspan=4 class='' isSingle='Yes' align='"+align+"'><div style='position:relative;"+valueCssStyle+"height:"+field.getControlsHeight()+"'>"+field.getFieldComments()+"</div></td>"); 
   	 			 }
            	
            }else{
            	
            
            if("0".equals("" + field.getIsSingleLabel())){
            	            
            if ("1".equals("" + field.getIsSingleRow())) {
                sectionStr.append("<td isSingle='Yes' class='' width='28%' colspan="+2*line+" align='"+align+"' style='"+borderstyle+"'><div style='position:relative;"+textCssStyle+"'>" + field.getFieldLabel()
                        + ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</div></td>");
            } else {           	
                sectionStr.append("<td isSingle='No' class='' width='33%' colspan=2 align='"+align+"' style='"+borderstyle+"'><div style='position:relative;"+textCssStyle+"'>" + field.getFieldLabel()
                        + ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</div></td>");

            }
            }else if("1".equals("" + field.getIsSingleLabel())){
            	
            	if ("1".equals("" + field.getIsSingleRow())) {
                    sectionStr.append("<td isSingle='Yes' class='' width='28%' colspan="+2*line+" align='"+align+"' style='"+borderstyle+"'><div style='position:relative;"+valueCssStyle+"'>"); 
                } else {
                    sectionStr.append("<td isSingle='No' class='' width='33%' colspan=2 align='left' style='"+borderstyle+"'><div style='position:relative;"+valueCssStyle+"'>"); 
                }
            	
            	
            	
            	
            	
            	if (this.getPrintFlag() != null && !"".equals(this.getPrintFlag())) {
                    if ("1".equals("" + field.getHighLevel())) {
                        // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01")+"</div>");
                        sectionStr.append("<div class='high_light'>"
                                + ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap, true,dateFormat)
                                + "</div>");
                    } else {
                        // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01"));
                        sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap,
                                true,dateFormat));
                    }
                } else {// 3 begin
                    // 如果是‘拒绝状态’并且不是第一个节点，则只能显示该节点可以修改的field为可编辑状态(即其他节点都是只读的）
                    if ("00".equals(status) || "03".equals(status)) {
                        if ("false".equals(viewFlag)
                                || ("03".equals(status) && !"0".equals(this.getProcess().getNodeId()))) {
                            sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                    fieldContentMap, true,dateFormat));
                        } 
                    } else {
                        // Highlight
                        if ("1".equals("" + field.getHighLevel())) {
                            // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status)+"</div>");
                            sectionStr.append("<div class='high_light'>"
                                    + ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap, true,dateFormat)
                                    + "</div>");
                        } else {
                            // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status));
                            sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                    fieldContentMap, true,dateFormat));
                        }
                    }
                }// 3 end
            
       
                  sectionStr.append("</div></td>");
            	
            }else{
            	if ("1".equals("" + field.getIsSingleRow())) {
                    sectionStr.append("<td isSingle='Yes' class='' width='10%' align='left' style='"+borderstyle+"'><div style='position:relative;"+textCssStyle+"'>" + field.getFieldLabel()
                            + ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + ":</div></td>");
                    sectionStr.append("<td width='26%' colspan="+(2*line-1)+" style='"+borderstyle+"'><div style='position:relative;"+valueCssStyle+"'>");
                } else {
                    sectionStr.append("<td isSingle='No' class='' width='10%' align='left' style='"+borderstyle+"'><div style='position:relative;"+textCssStyle+"'>" + field.getFieldLabel()
                            + ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + ":</div></td>");
                    sectionStr.append("<td width='26%' style='"+borderstyle+"'><div style='position:relative;"+valueCssStyle+"'>");
                }
            
            // IT0958-----end
            // 如果当前节点是“Begin(开始)“节点，并且该sectionField是需要在流转过程才可以输入的，则不需要requestor在申请时填写
            
                
                    if (this.getPrintFlag() != null && !"".equals(this.getPrintFlag())) {
                        if ("1".equals("" + field.getHighLevel())) {
                            // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01")+"</div>");
                            sectionStr.append("<div class='high_light'>"
                                    + ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap, true,dateFormat)
                                    + "</div>");
                        } else {
                            // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status,"01"));
                            sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap,
                                    true,dateFormat));
                        }
                    } else {// 3 begin
                        // 如果是‘拒绝状态’并且不是第一个节点，则只能显示该节点可以修改的field为可编辑状态(即其他节点都是只读的）
                        if ("00".equals(status) || "03".equals(status)) {
                            if ("false".equals(viewFlag)
                                    || ("03".equals(status) && !"0".equals(this.getProcess().getNodeId()))) {
                                sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                        fieldContentMap, true,dateFormat));
                            } 
                        } else {
                            // Highlight
                            if ("1".equals("" + field.getHighLevel())) {
                                // out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status)+"</div>");
                                sectionStr.append("<div class='high_light'>"
                                        + ReportFieldHelper.showLabelField(field, this.getReport(), fieldContentMap, true,dateFormat)
                                        + "</div>");
                            } else {
                                // out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status));
                                sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                        fieldContentMap, true,dateFormat));
                            }
                        }
                    }// 3 end
                
           
            sectionStr.append("</div></td>");
            // IT0958 begin
            }
          }
            if (("1".equals("" + field.getIsSingleRow())) || (count % line == 0)) {
                sectionStr.append("</tr>");
            }
            if (!"1".equals("" + field.getIsSingleRow())) {
                count++;
            }
            // IT0958 young end
        }// -- while(it.hasNext())
        if (count % line == 0) {
            sectionStr.append("</tr>");
        }
        sectionStr.append("</table>");
        sectionStr.append("</div></div>");
        sectionStr.append("<div style='height:20px'></div></div><span class='newPage' lang=EN-US"+
			"style='font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:'mce_style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:'"+
			"Times New Roman\';mso-fareast-font-family:宋体;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA\'>"+
	        "<br clear=all style='page-break-before: always'"+
			"mce_style='page-break-before:always'></span><div class='Wpage'>");
        return sectionStr;
    }
	
	private StringBuffer getCommonSection(HttpServletRequest request,
			ReportSectionVO section, HashMap sectionFieldMap,
			Collection fieldList) {
		String viewFlag = (String) request.getParameter("viewFlag");
		String dateFormat = (String) request.getAttribute("dateFormat");
		if (dateFormat == null || "".equals(dateFormat)) {
			dateFormat = "MM/dd/yyyy";
		}
		int line = 0;
		if ("02".equals(section.getSectionType())) {
			line = 2;
		} else if ("03".equals(section.getSectionType())) {
			line = 3;
		} else {
			line = 2;
		}

		StringBuffer sectionStr = new StringBuffer("");
		Iterator it = fieldList.iterator();
		sectionStr
				.append("<div><table id='sectionTable"
						+ section.getSectionId()
						+ "' width=\"100%\" class='reportTable'  cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
		int count = 1;
		Collection fieldContentList = (ArrayList) sectionFieldMap.get(section
				.getSectionId());
		HashMap fieldContentMap = null;
		if (fieldContentList == null || fieldContentList.size() == 0) {
			fieldContentMap = new HashMap();
		} else {
			fieldContentMap = (HashMap) fieldContentList.iterator().next();
		}
		// SYSTEM FIELD: is_exceptional_case
		// 是否存在系统字段“is_exceptional_case”的标志，默认不存在;如果存在，如果该report是特例，则“isExceptionalCase”为true,否则为false
		boolean existIsExceptionalCase = false;
		// boolean isExceptionalCase = false;
		ReportSectionFieldVO fieldPrv = null;
		int prvIsSingleRow = 0;
		while (it.hasNext()) {
			if (fieldPrv != null) {
				prvIsSingleRow = fieldPrv.getIsSingleRow();
			}
			ReportSectionFieldVO field = (ReportSectionFieldVO) it.next();
			if (existIsExceptionalCase == false
					&& CommonName.SYSTEM_ID_IS_EXCEPTIONAL_CASE.equals(field
							.getFieldId())) {
				existIsExceptionalCase = true;
				String fieldValue = (String) fieldContentMap.get(field
						.getFieldId().toUpperCase());
				if (fieldValue != null
						&& (CommonName.NORMAL_EXCEPTIONAL_CASE
								.equals(fieldValue) || CommonName.MIDNIGHT_EXCEPTIONAL_CASE
								.equals(fieldValue))) {
					this.setIsExceptionalCase(true);
				}
			}
			fieldPrv = field;
			
			// IT0958-----begin
			if ("1".equals("" + field.getIsSingleRow())) {
				sectionStr.append("<tr>");
				if (count > 1) {
					count = count - 1;
				}
			} else if (count % line == 1) {
				sectionStr.append("<tr>");
			} else if (prvIsSingleRow == 1) {
				sectionStr.append("<tr>");
				if (count > 1) {
					count = count - 1;
				}
			}

			String borderstyle = "";
			if ("1".equals("" + field.getBorder())) {
				borderstyle = "border-top:1px solid #cdcdcd;";
			} else if ("2".equals("" + field.getBorder())) {
				borderstyle = "border-bottom:1px solid #cdcdcd;";
			} else if ("3".equals("" + field.getBorder())) {
				borderstyle = "border-left:1px solid #cdcdcd;";
			} else if ("4".equals("" + field.getBorder())) {
				borderstyle = "border-right:1px solid #cdcdcd;";
			} else if ("5".equals("" + field.getBorder())) {
				borderstyle = "border-top:1px solid #cdcdcd;border-bottom:1px solid #cdcdcd;";
			} else if ("6".equals("" + field.getBorder())) {
				borderstyle = "border-left:1px solid #cdcdcd;border-right:1px solid #cdcdcd;";
			} else if ("7".equals("" + field.getBorder())) {
				borderstyle = "border-top:1px solid #cdcdcd;border-left:1px solid #cdcdcd;";
			} else if ("8".equals("" + field.getBorder())) {
				borderstyle = "border-right:1px solid #cdcdcd;border-bottom:1px solid #cdcdcd;";
			} else if ("9".equals("" + field.getBorder())) {
				borderstyle = "border:1px solid #cdcdcd;";
			}

			String[] tmp = field.getCssStr().split(";");
			String[] textTmp = tmp[0].split(",");
			String[] valueTmp = tmp[1].split(",");
			String textCssStyle = "left:" + textTmp[0] + "px;top:" + textTmp[1]
					+ "px;font:" + textTmp[3] + " " + textTmp[4] + " "
					+ textTmp[2] + "px " + textTmp[5] + ";";
			String valueCssStyle = "left:" + valueTmp[0] + "px;top:"
					+ valueTmp[1] + "px;font:" + valueTmp[3] + " "
					+ valueTmp[4] + " " + valueTmp[2] + "px " + valueTmp[5]
					+ ";";

			if ("2".equals("" + field.getFieldType())) {
				if ("1".equals("" + field.getIsSingleRow())) {
					field.setFieldComments(field.getFieldComments().replace(
							"@path", request.getContextPath()));

					if ("0".equals("" + field.getControlsHeight())) {
						sectionStr
								.append("<td colspan="
										+ 2
										* line
										+ " isSingle='Yes'><div style='position:relative;"
										+ textCssStyle + "'>"
										+ field.getFieldComments()
										+ "</div></td>");
					} else {
						sectionStr
								.append("<td colspan="
										+ 2
										* line
										+ " isSingle='Yes' ><div style='position:relative;"
										+ textCssStyle + "height:"
										+ field.getControlsHeight() + "'>"
										+ field.getFieldComments()
										+ "</div></td>");
					}
				} else {
					if ("0".equals("" + field.getControlsHeight())) {
						sectionStr
								.append("<td colspan='2' isSingle='No'><div style='position:relative;"
										+ textCssStyle
										+ "'>"
										+ field.getFieldComments()
										+ "</div></td>");
					} else {
						sectionStr
								.append("<td colspan='2' isSingle='No' ><div style='position:relative;"
										+ textCssStyle
										+ "height:"
										+ field.getControlsHeight()
										+ "'>"
										+ field.getFieldComments()
										+ "</div></td>");
					}

				}

			} else {
				if ("0".equals("" + field.getIsSingleLabel())) {

					if ("1".equals("" + field.getIsSingleRow())) {
						sectionStr
								.append("<td isSingle='Yes' class='' width='28%' colspan="
										+ 2
										* line
										+ " align='left' style='"
										+ borderstyle
										+ "'><div style='position:relative;"
										+ textCssStyle
										+ "'>"
										+ field.getFieldLabel()
										+ ReportFieldHelper
												.conrolIsRequiredFlag(field
														.getIsRequired())
										+ "</div></td>");
					} else {

						sectionStr
								.append("<td isSingle='No' class='' width='33%' colspan=2 align='left' style='"
										+ borderstyle
										+ "'><div style='position:relative;"
										+ textCssStyle
										+ "'>"
										+ field.getFieldLabel()
										+ ReportFieldHelper
												.conrolIsRequiredFlag(field
														.getIsRequired())
										+ "</div></td>");
					}
				} else if ("1".equals("" + field.getIsSingleLabel())) {

					if ("1".equals("" + field.getIsSingleRow())) {
						sectionStr
						.append("<td isSingle='Yes' class='' width='28%' colspan="
								+ 2
								* line
								+ " align='left' style='"
								+ borderstyle
								+ "'><div style='position:relative;"
								+ valueCssStyle + "'>");
						if("1".equals(""+field.getIsReadonly())){}else{
							
						
						sectionStr
								.append("<td isSingle='Yes' class='' width='28%' colspan="
										+ 2
										* line
										+ " align='left' style='"
										+ borderstyle
										+ "' ondblclick='showEditBox(\""+section.getSectionId()+"_,"+field.getFieldId()+"\")'><div style='position:relative;"
										+ valueCssStyle + "'>");
						}
					} else {
						if("1".equals(""+field.getIsReadonly())){
							sectionStr
							.append("<td isSingle='No' class='' width='33%' colspan=2 align='left' style='"
									+ borderstyle
									+ "'><div style='position:relative;"
									+ valueCssStyle + "'>");
						}else{
						sectionStr
								.append("<td isSingle='No' class='' width='33%' colspan=2 align='left' style='"
										+ borderstyle
										+ "' ondblclick='showEditBox(\""+section.getSectionId()+"_,"+field.getFieldId()+"\")'><div style='position:relative;"
										+ valueCssStyle + "'>");
						}
					}

					if ("1".equals("" + field.getHighLevel())) {
						// out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status)+"</div>");
						sectionStr.append("<div class='high_light'>"
								+ ReportFieldHelper.showLabelField(field, this
										.getReport(), fieldContentMap, true,
										dateFormat) + "</div>");
					} else {
						// out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status));
			
						
						String fieldValue = (String) fieldContentMap.get(field.getFieldId().toUpperCase());
						if(fieldValue==null){
							fieldValue="";
						}
						if("1".equals(""+field.getIsReadonly())){
							sectionStr.append(fieldValue.trim());
						}else{						
						sectionStr.append("<span id='text_"+section.getSectionId()+"_,"+field.getFieldId()+"'>"+fieldValue.trim()
								+"</span><input type='text' id='id_"+section.getSectionId()+"_,"+field.getFieldId()
								+"' style='display:none' name='field_ID' value='"+fieldValue.trim()
								+"' onblur='saveFileBox(\""+section.getSectionId()+"_,"+field.getFieldId()+"\")'>");
						}
					}

					sectionStr.append("</div></td>");

				} else {
					if ("1".equals("" + field.getIsSingleRow())) {
						sectionStr
								.append("<td isSingle='Yes' class='' width='5%' align='left' style='"
										+ borderstyle
										+ "'><div style='position:relative;"
										+ textCssStyle
										+ "'>"
										+ field.getFieldLabel()
										+ ReportFieldHelper
												.conrolIsRequiredFlag(field
														.getIsRequired())
										+ ":</div></td>");
						if("1".equals(""+field.getIsReadonly())){
							sectionStr.append("<td width='26%' colspan="
									+ (2 * line - 1) + " style='" + borderstyle
									+ "' ><div style='position:relative;"
									+ valueCssStyle + "'>");
						}else{
						sectionStr.append("<td width='26%' colspan=" 
								+ (2 * line - 1) + " style='" + borderstyle
								+ "' ondblclick='showEditBox(\""+section.getSectionId()+"_,"+field.getFieldId()+"\")'><div style='position:relative;"
								+ valueCssStyle + "'>");
						}
					} else {
						sectionStr
								.append("<td isSingle='No' class='' width='10%' align='left' style='"
										+ borderstyle
										+ "'><div style='position:relative;"
										+ textCssStyle
										+ "'>"
										+ field.getFieldLabel()
										+ ReportFieldHelper
												.conrolIsRequiredFlag(field
														.getIsRequired())
										+ ":</div></td>");
						sectionStr.append("<td width='26%' style='"
								+ borderstyle
								+ "'><div style='position:relative;"
								+ valueCssStyle + "'>");
					}

					// Highlight
					if ("1".equals("" + field.getHighLevel())) {
						// out.print("<div class='high_light'>"+FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status)+"</div>");
						sectionStr.append("<div class='high_light'>"
								+ ReportFieldHelper.showLabelField(field, this
										.getReport(), fieldContentMap, true,
										dateFormat) + "</div>");
					} else {
						// out.print(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,status));

						String fieldValue = (String) fieldContentMap.get(field.getFieldId().toUpperCase());
						if(fieldValue==null){
							fieldValue="";
						}
						if("1".equals(""+field.getIsReadonly())){
							sectionStr.append(fieldValue.trim());
						}else{
						sectionStr.append("<span id='text_"+section.getSectionId()+"_,"+field.getFieldId()+"'>"+fieldValue.trim()
								+"</span><input type='text' id='id_"+section.getSectionId()+"_,"+field.getFieldId()
								+"' style='display:none' name='field_ID' value='"+fieldValue.trim()
								+"' onblur='saveFileBox(\""+section.getSectionId()+"_,"+field.getFieldId()+"\")'>");
						}
					}

					sectionStr.append("</div></td>");
					// IT0958 begin
				}
			}

			if (("1".equals("" + field.getIsSingleRow()))
					|| (count % line == 0)) {
				sectionStr.append("</tr>");
			}
			if (!"1".equals("" + field.getIsSingleRow())) {
				count++;
			}
			// IT0958 young end
		}// -- while(it.hasNext())
		if (count % line == 0) {
			sectionStr.append("</tr>");
		}
		sectionStr.append("</table>");
		sectionStr.append("</div>");
		sectionStr.append("<div style='height:20px'></div>");
		return sectionStr;
	}
	
	private StringBuffer getTableSection(HttpServletRequest request, ReportSectionVO section, HashMap sectionFieldMap,
            Collection fieldList) {
        String viewFlag = (String) request.getParameter("viewFlag");
        String dateFormat = (String) request.getAttribute("dateFormat");
        StringBuffer sectionStr = new StringBuffer("");
        Iterator it = fieldList.iterator();
        String borderstyle = "";
        if("04".equals(section.getSectionType())){
        	borderstyle ="border-top:1px solid #cdcdcd;border-bottom:1px solid #cdcdcd;";
        }else if("05".equals(section.getSectionType())){
        	borderstyle ="border-left:1px solid #cdcdcd;border-right:1px solid #cdcdcd;";
        }else{
        	borderstyle ="border:1px solid #cdcdcd;";
        }
        
        String sectioType = section.getSectionType();
        // 如果该sectionType是'附件'，并且是可以修改的状态（'00'--draft,'03'---reject）,则需要开放可以修改
        if (!"false".equals(viewFlag)
                && "00".equals(sectioType)
                && ("00".equals(this.getStatus()) || "03".equals(this.getStatus()) || ("deal".equals(request
                        .getParameter("operateType")) && this.process.getRequestStaffCode().equalsIgnoreCase(
                        this.process.getCurrentProcessor())))) {
            
        } else {
            sectionStr
                    .append("<table id='reportTable"
                            + section.getSectionId()
                            + "' width=\"100%\" class='reportTable' cellpadding=\"3\" cellspacing=\"0\"  style=\"border-collapse:collapse;\">");
            sectionStr.append("<tr  class='tr2'>");

            String defaultColumnWidth = 100 / fieldList.size() + "%";
            // 显示所有列头
            int SectionFieldcount = 0;
            int currentNodeonlyFillSectionFieldcnt = 0;
            boolean requesterEditable = false;
            while (it.hasNext()) {
                ReportSectionFieldVO field = (ReportSectionFieldVO) it.next();
                if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                    continue;
                }
                if (field.getControlsWidth() > 0) {
                    defaultColumnWidth = "" + field.getControlsWidth();
                }
                sectionStr.append("<td  align='center' style='"+borderstyle+"'>" + field.getFieldLabel()
                        + ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) + "</td>");

                if (this.getOnlyFillSectionFieldMap() != null
                        && this.getOnlyFillSectionFieldMap().containsKey(
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
                        ReportSectionFieldVO field = (ReportSectionFieldVO) fieldArray[i];
                        if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) { // 忽略系统递增字段(显示个隐藏字段)
                            sectionStr.append("<input type='hidden' name='" + section.getSectionId() + "_ID' value='"
                                    + (String) fieldContent.get(field.getFieldId().toUpperCase()) + "'>");
                            continue;
                        }
                        
                        sectionStr.append("<td align='center' style='word-wrap:break-word;table-layout:fixed;"+borderstyle+"'>");// 00
                       
                        
                          
                                if ("00".equals(this.getStatus())
                                        || ("03".equals(this.getStatus()) && "0".equals(this.getCurrentNodeId().trim()))) {
                                    canShowAddDelbutton = true;
                                }

                                if (this.getPrintFlag() != null && !"".equals(this.getPrintFlag())) {// 3
                                    // 如果有需要高亮度显示的标识，则需要加粗显示内容
                                    if ("1".equals("" + field.getHighLevel())) {
                                        sectionStr.append("<div class='high_light'>"
                                                + ReportFieldHelper.showLabelField(field, this.getReport(), fieldContent,
                                                        false,dateFormat) + "</div>");
                                    } else {
                                        sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                                fieldContent, false,dateFormat));
                                    }
                                } else {// 3
                                    // 如果有需要高亮度显示的标识，则需要加粗显示内容
                                    // if("1".equals(""+field.getHighLevel())){
                                    // sectionStr.append("<div class='high_light'>"+ReportFieldHelper.showLabelField(field,this.getReport(),fieldContent,true)+"</div>");
                                    // }else{//4
                                    // if("00".equals(this.getStatus()) || "03".equals(this.getStatus())){
                                    // if("03".equals(status) && !"0".equals(this.getProcess().getNodeId())){
                                    // sectionStr.append(ReportFieldHelper.showLabelField(field,this.getReport(),fieldContent,true));
                                    // }else{
                                    // sectionStr.append(ReportFieldHelper.showEditField(field,this.getReport(),this.getStaff(),fieldContent,rowIndex));
                                    // }
                                    // }else{
                                    // sectionStr.append(ReportFieldHelper.showLabelField(field,this.getReport(),fieldContent,true));
                                    // }
                                    // }//4

                                    
                                        if ("1".equals("" + field.getHighLevel())) {
                                            sectionStr.append("<div class='high_light'>"
                                                    + ReportFieldHelper.showLabelField(field, this.getReport(),
                                                            fieldContent, true,dateFormat) + "</div>");
                                        } else {
                                            sectionStr.append(ReportFieldHelper.showLabelField(field, this.getReport(),
                                                    fieldContent, true,dateFormat));
                                        }
                                  

                                }// 3
                           
                      
                        sectionStr.append("</td>");// 00
                        iFlag++;
                    }// 02
                    rowIndex++;
                    sectionStr.append("</tr>");
                }// 01 end while
            } else {// 否则没有数据
            	
            	System.out.println("否则没有数据");
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
            sectionStr.append("");
            sectionStr.append("</table>");

            if ("false".equals(viewFlag)) {
                canShowAddDelbutton = false;
            }

            // Young edited 08/05/2008
            // if("00".equals(this.getStatus()) || ("03".equals(this.getStatus()) &&
            // "0".equals(this.getCurrentNodeId().trim()))){//Draf状态，或“拒绝”状态并且是回到第一个节点
            if (canShowAddDelbutton && !"00".equals(section.getSectionType())) {// 判断是否对当前section字段有权限
                sectionStr
                        .append("<br><input type='button' name='addRowBtn' value='Add' onclick='createTableSectionRow(\"reportTable"
                                + section.getSectionId()
                                + "\",\""
                                + section.getSectionId()
                                + "\",\""
                                + this.getCurrentNodeId().trim()
                                + "\")'>&nbsp;&nbsp;<input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"reportTable"
                                + section.getSectionId() + "\",\"chkid_" + section.getSectionId() + "\")'></td></tr>");
                //sectionStr.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>");
            } else if (("03".equals(this.getStatus()) && !"0".equals(this.getCurrentNodeId().trim()))
                    && (!"".equals(this.getUpdateSections() + this.getNewSectionFields()) && (this.getUpdateSections() + this
                            .getNewSectionFields()).indexOf("," + section.getSectionId()) > -1)) {// 拒绝状态，非第一个节点
                sectionStr
                        .append("<br><input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"reportTable"
                                + section.getSectionId() + "\",\"chkid\")'></td></tr>");
            }
        }

        sectionStr.append("<div style='height:20px'></div>");
        return sectionStr;
    }
	
	/**
	 * 显示空白report
	 * @param request
	 * @return
	 */
	public  String displayBlankReport(HttpServletRequest request)throws Exception{
		StringBuffer str = new StringBuffer("");
		ReportManageVO report = (ReportManageVO)request.getAttribute("listReport");
		String dateFormat = (String) request.getAttribute("dateFormat");
		if(dateFormat==null || "".equals(dateFormat)){
        	dateFormat="MM/dd/yyyy";
        }
		Date date = new Date();
		String attachmentIdentity = ""+date.getTime(); //附件的标适，与当前填写report关联的标示
		str.append("<input type='hidden' name='attachmentIdentity' value='" + attachmentIdentity + "'>");
		HashMap onlyFillSectionFieldMap = (HashMap)request.getAttribute("onlyFillSectionFieldMap");
		String type= (String)request.getParameter("type");
		Collection sectionList = report.getSectionList();
		int sectionNum = sectionList.size();
		String requestReportDate = "";
		str.append("<table width='96%'  style='border: 1px #D4D4D5 solid; background-color:#F0F0F0;' cellpadding='3' cellspacing='0' bordercolor='#D31145' style='border-collapse:collapse;'>");
		StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		str.append(showReportName(report,sectionNum));

		if(sectionNum==0) return str.toString();
		
		Iterator sectionIt = sectionList.iterator();
	    int sectionId = 1;
	    while(sectionIt.hasNext()){//遍历显示所有section
	    	ReportSectionVO section =  (ReportSectionVO)sectionIt.next();
	    	Collection fieldList = section.getFieldList();
	    	int fieldNum = fieldList.size();
	        Iterator it = fieldList.iterator();
	        Iterator it1 = fieldList.iterator();
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
	    		//str.append("<table id='reportTable"+sectionId+"' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#6595D6\" style=\"border-collapse:collapse;\">");
	    		str.append(divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<table id='reportTable"+section.getSectionId()+"' width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
	    		str.append("<tr  class='tr2'>")
	    		   .append("<td align='center' width='1%'><input type='checkbox' name='chkAll' onclick='selectAll(this,\"chkid_"+section.getSectionId()+"\")'>All</td>")
	    		   .append("");
	    		String defaultColumnWidth = "10";
                //遍历该section的所有字段，显示其中需要显示的字段的label
	    		while(it.hasNext()){
	            	 ReportSectionFieldVO field = (ReportSectionFieldVO)it.next();
	          	     if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){ //id系统自动生成的序号，不需要列出来
	          		     continue;
	          	     }
	          	     if(field.getColumnWidth()>0){
	          	         defaultColumnWidth = "" +field.getColumnWidth();
	          	     }  
	          	     str.append("<td align='center' width='"+defaultColumnWidth+"%' style='word-break:break-all;'>"+field.getFieldLabel()+ ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>");
	          	     
	          	     
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
	    			str.append("<input type='button' name='addRowBtn' value='Add' onclick='createTableSectionRow(\"reportTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+this.getCurrentNodeId().trim()+"\")'>&nbsp;&nbsp;<input type='button' name='deleteRowBtn' value='Delete' onclick='delAllRow(\"reportTable"+section.getSectionId()+"\",\"chkid_"+section.getSectionId()+"\")'>");
	    			str.append("<br><br>");
	    		}
	    		str.append(divEndStr+"</td></tr>");
           	 	str.append("<tr><td colspan='2'>&nbsp;&nbsp;</td></tr>"); 
	    		SectionFieldcount = 0;
	    		currentNodeonlyFillSectionFieldcnt=0;
	    	}
	    	
	    	if("02".equals(sectionType) || "03".equals(sectionType)){// 02 -- Common形式; 03 -- Basic Inreportation of Requester   
	    		String tableid = CommonName.MergedTableId;
	    		str.append("<tr><td colspan='2'>"+divBeginStr.replaceAll("@sectionId", ""+sectionId)+"<table id="+tableid+" width=\"100%\"  border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CDCDCD\" style=\"border-collapse:collapse;\">");
	    		count = 1;
    	        ReportSectionFieldVO fieldPrv=null;
    	        int prvIsSingleRow=0; //是否单独占用一行显示
    	        //遍历该section的所有字段，显示其中需要显示的字段
    	        while(it.hasNext()){
    	        	if (fieldPrv!=null){
    	    	    	prvIsSingleRow = fieldPrv.getIsSingleRow();
    	    	    }
    	        	ReportSectionFieldVO field = (ReportSectionFieldVO)it.next();
    	        	fieldPrv = field;
    	        	if("1".equals(""+field.getIsSingleRow())){ //需要单独一行
    	        		str.append("<tr>");
    	        		if (count>1){
    		        		count = count-1;	
    		        	}   
    	        	}else if(count%3==1){
    	        		str.append("<tr>");
    		        }else if(prvIsSingleRow==1){
    		        	str.append("<tr>");
    		        	if (count>1){
	        				count = count-1;	
	        			}
    	 	        }
    	        	if("1".equals(""+field.getIsSingleRow())){
    	        		str.append("<td isSingle='Yes' class='' width='5%' align = 'right'>"+field.getFieldLabel()+ ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>")
    	        		   .append("<td width='26%' colspan=5>");
    	        	}else{
    	        		str.append("<td isSingle='No' class='' width='10%' align = 'right'>"+field.getFieldLabel()+ ReportFieldHelper.conrolIsRequiredFlag(field.getIsRequired()) +"</td>")
    	        		   .append("<td width='26%'>");
    	        	}
                    
    	        	//如果该字段是只能在流转过程中修改的，则不需要requestor在申请时填写
                	if(onlyFillSectionFieldMap.containsKey((section.getSectionId()+"."+field.getFieldId()).toUpperCase())){
                	   //str.append(FieldControlHelper.ShowDisabledField(field));
                		str.append(ReportFieldHelper.showLabelField(field,report,null,false,dateFormat));
                	}else if("02".equals(sectionType)){ //common 形式
                		//str.append(FieldControlHelper.createControl(field,sectionType,1));
                		str.append(ReportFieldHelper.showEditField(field,report,currentStaff,null,dateFormat));
                	}else if("03".equals(sectionType)){ //basic information 形式
                		//str.append(FieldControlHelper.createBaseInforControl(true,field,report,request));
                		str.append(ReportFieldHelper.showEditField(field,report,currentStaff,null,dateFormat));
                		requestReportDate = StringUtil.getDateStr(new java.util.Date(),dateFormat+" HH:mm:ss");
                	}
                	
                	str.append("</td>");
                	if(("1".equals(""+field.getIsSingleRow())) || count%3==0) {
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
    	            		  +request.getContextPath()+"/uploadAction.it?method=enter&requestReportDate="+"&attachmentIdentity="+attachmentIdentity+"&tempDate="+(new Date().getTime())+"></iframe></div>")
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
