
<%--
    Task_ID    			Author     Modify_Date    Description
1.  ePayment Phase 2   Robert.Ouyang   04/12/2010     initial
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@page import="com.aiait.eflow.util.FieldUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.wkf.util.DataMapUtil"%>
<%@page
	import="com.aiait.eflow.util.StringUtil"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>

<%@page isELIgnored="false"%>

<html>
<head>
<title>Processing Amount Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%!
Collection allforms = null;
public int getFormSystemId(String formId){
	if (allforms != null && allforms.size() > 0) {
		Iterator it = allforms.iterator();
		while (it.hasNext()) {
			FormManageVO vo = (FormManageVO)it.next();
			if(vo.getFormId().equals(formId))
				return vo.getFormSystemId();
		}
	}
	return 0;
}
%>
<%
	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	AuthorityHelper authority = AuthorityHelper.getInstance();

	String formType = (String) request.getAttribute("formType");
	
	allforms = (Collection) request.getAttribute("allforms");

	Collection formList = (ArrayList) request.getAttribute("resultList"); // search result list
	
	String forms = (String) request.getAttribute("qforms");
	if (forms == null)
		forms = "";
	String orgs = (String) request.getAttribute("qorgs");
	if (orgs == null)
		orgs = "";

	boolean ISEXCEL = "EXCEL".equals(request.getParameter("display")) ? true : false;
	if (ISEXCEL) {
		response.setContentType("application/vnd.ms-excel;charset=GBK");
		out.println("<meta Content-Disposition=\"attachment; filename=a.xls\">");
	} else {
%>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">

<i18n:jsmessage jsRelativePath="js" prefixName="message" />

<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>

<script language="javascript">

  function searchForm(reportType){
	  document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingAmount";
	  document.forms[0].target = "";
      document.forms[0].submit();
  }
  
  function exportToExcel(){
     document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingAmount&display=EXCEL";
     document.forms[0].target = "newWindow";
     document.forms[0].submit();
  }

  var xmlhttp = createXMLHttpRequest();
  
  function getOptionList(formType){
	   type = formType;
	   var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"";
	   //&moduleId=<%=ModuleOperateName.MODULE_FORM_INQUIRY%>
	   xmlhttp.open("GET", url, true);
	   var objId = "forms";
	   xmlhttp.onreadystatechange=updateForms;
	   xmlhttp.setRequestHeader("If-Modified-Since","0");
	   xmlhttp.send(null);	
  }  

  function updateForms(){
	     if(xmlhttp.readyState == 4) {
	        if(xmlhttp.status == 200) {
	    	    var select;
	    	    select = document.getElementById("forms");
	    	    select.value = "";
	    	    clearSelect(select);
	    	    //alert(xmlhttp.responseXML.xml)
	    	    var options = xmlhttp.responseXML.getElementsByTagName("option");
	    	    if(options.length>0){
	    	      select.appendChild(createOptionEle("All", "", true));
	    	    }
	    	    for (var i = 0, n = options.length; i < n; i++) {
	    	        select.appendChild(createElementWithValue(options[i],"id"));
	    	    }
	        }
	     }
	  }
  function showDetail(formid, orgid, stat){
	  /*
	 document.forms[0].reset();
	 document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingAmountDetail&status="+stat
			 +"&form="+formid+"&org="+orgid;
     document.forms[0].target = window.open("","newwindow","height=1, width=1, top=0, left=0,toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
     document.forms[0].submit();
     */
     url = "<%=request.getContextPath()%>/reportAction.it?method=processingAmountDetail&status="+stat
	 		+"&form="+formid+"&org="+orgid+"&beginDateStr="+document.forms[0].beginDateStr.value+"&endDateStr="+document.forms[0].endDateStr.value;
     openCenterWindow(url,700,500);
 }
  function getFormID(formSystemId) {
	for (var i = 0; i < $('forms').options.length; i++) {
	  if ($('forms')[i].value == formSystemId) {
		return $('forms')[i].innerText.split('_')[0];
	  }
	}
  } 
</script>
<%
	}
%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;" onload="">

<%
	if (!ISEXCEL) {
%>
<FORM name="statusMonitoringForm" method="post"
	action="<%=request.getContextPath()%>/reportAction.it?method=processingAmount">
<table border="0" width="100%" cellspacing="0" cellpadding="0"
	id="table0">
	<tr>
		<td height="10"></td>
	</tr>
	<!--<tr>
		<td><strong><font color='#5980BB'
			family='Times New Roman'><i18n:message
			key="report_processamount.location" /></font></strong></td>
	</tr>
--></table>
<input type="hidden" name="display">
<table width="100%" bordercolor="#6595D6"
	style="border-collapse: collapse;" border="1" cellpadding="0"
	cellspacing="1">
	<TR align="center">
		<TD height="25" colspan="4" class="tr1"><i18n:message
			key="menu.report.processingAmount" /></TD>
	</TR>
	<TR>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.form_type" /></span></div>
		</TD>
		<TD width="30%" height="20"><select name="formType"
			onchange="getOptionList(this.value)">
			<option value="" <%=(StringUtil.isEmptyString(formType) ? "selected" : "")%> >All</option>
			<%
				Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
					if (typeList != null && typeList.size() > 0) {
						Iterator it = typeList.iterator();
						while (it.hasNext()) {
							FormTypeVO typeVo = (FormTypeVO) it.next();
							if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
									ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
			%>
			<option value="<%=typeVo.getFormTypeId()%>"
				<%=(formType != null && (typeVo.getFormTypeId()).equals(formType)) ? "selected"
									: ""%>><%=typeVo.getFormTypeName()%></option>
			<%
				}
						}
					}
			%>
		</select></TD>
	</TR>	
	<TR>
		<TD width="10%" height="150" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.form" /></span></div>
		</TD>
		<TD width="40%"><select name="forms" style="height: 100%"
			MULTIPLE>
			<option value="" <%=(forms.length() == 0) ? "SELECTED" : ""%>>All</option>
			<%
				if (allforms != null && allforms.size() > 0) {
						Iterator it = allforms.iterator();
						while (it.hasNext()) {
							FormManageVO formVo = (FormManageVO) it.next();
							if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
									ModuleOperateName.MODULE_COMMON_QUERY, formVo.getFormType())) {
								if (forms.indexOf("'" + formVo.getFormSystemId() + "'") >= 0) {
									out.println("<option value='" + formVo.getFormSystemId() + "' selected>" + formVo.getFormId()
											+ " - " + formVo.getFormName() + "</option>");
								} else {
									out.println("<option value='" + formVo.getFormSystemId() + "' >" + formVo.getFormId() + " - "
											+ formVo.getFormName() + "</option>");
								}
							}
						}
					}
			%>
		</select></TD>
		<TD width="10%" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.company" /></span></div>
		</TD>
		<TD width="40%"><select name="orgs" style="height: 100%" MULTIPLE>
			<%
					//Collection companyList = currentStaff.getOwnCompanyList(); 
					//CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
					Collection companyList = CompanyHelper.getInstance().getCompanyList();
					if (companyList != null) {
						Iterator companyIt = companyList.iterator();
						while (companyIt.hasNext()) {
							CompanyVO company = (CompanyVO) companyIt.next();
							if (orgs.indexOf("'" + company.getOrgId() + "'") >= 0) {
								out.println("<option value='" + company.getOrgId() + "' selected>" + company.getOrgName()
										+ "</option>");
							} else {
								out.println("<option value='" + company.getOrgId() + "'>" + company.getOrgName()
										+ "</option>");
							}
						}
					}
			%>
		</select></TD>
	</TR>
	<TR>
		<TD width="10%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.submit_date_from" /></span></div>
		</TD>
		<TD width="40%" height="20"><input type="text"
			name="beginDateStr" onclick='setday(this)'
			value="<%=((String) request.getAttribute("beginDateStr") == null
								|| "".equals((String) request.getAttribute("beginDateStr")) ? "" : (String) request
								.getAttribute("beginDateStr"))%>">(MM/DD/YYYY)
		</TD>
		<TD width="10%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="report_overduesumreport.processedto" /></span></div>
		</TD>
		<TD width="40%" height="20"><input type="text" name="endDateStr"
			onclick='setday(this)'
			value="<%=((String) request.getAttribute("endDateStr") == null
								|| "".equals((String) request.getAttribute("endDateStr")) ? "" : (String) request
								.getAttribute("endDateStr"))%>">(MM/DD/YYYY)
		</TD>
	</TR>
	<tr>
		<td align='center' colspan='4'><input type="button"
			value='<i18n:message key="button.search"/>' name="subBtn"
			onclick="searchForm()" class="btn3_mouseout"
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'" />&nbsp;&nbsp;&nbsp; <input
			type="button" value='<i18n:message key="button.export_file"/>'
			name="expBtn" onclick="exportToExcel()" class="btn3_mouseout"
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'" />&nbsp;&nbsp;</td>
	</tr>
</table>
</FORM>
<%
	}
%>

<table width="100%" border="<%=ISEXCEL ? "1" : "0"%>" cellpadding="0"
	cellspacing="1" class="sortable" id="mytable"
	style="border: 1px #8899cc solid;">

	<%
		if (ISEXCEL) {
	%>
	<tr class="liebiao_tou">
		<td align='center' colspan='7'><Strong><i18n:message
			key="menu.report.processingAmount" /></Strong></td>
	</tr>
	<tr class="liebiao_tou">
		<td align='right' colspan='7'>Transaction between
		${param.beginDateStr} and ${param.endDateStr}</td>
	</tr>
	<%
		}
	%>

	<tr class="liebiao_tou">
		<td align='center'><Strong>Form ID</Strong></td>
		<td align='center'><Strong>Company Name</Strong></td>
		<td align='center'><Strong>Completed</Strong></td>
		<td align='center'><Strong>Completed/Total(%)</Strong></td>
		<td align='center'><Strong>Submitted</Strong></td>
		<td align='center'><Strong>Submitted/Total(%)</Strong></td>
		<td align='center'><Strong>Rejected</Strong></td>
		<td align='center'><Strong>Rejected/Total(%)</Strong></td>
		<td align='center'><Strong>In Progress</Strong></td>
		<td align='center'><Strong>In Progress/Total(%)</Strong></td>
		<td align='center'><Strong>Total</Strong></td>
	</tr>

	<%
		Collection result = (Collection) request.getAttribute("result");

		int com = 0, sub = 0, rej = 0, inp = 0, total = 0;

		if (result != null && result.size() > 0) {
			Iterator it = result.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				String formid = FieldUtil.convertSafeString(map, "FORM_ID");
				String orgid = FieldUtil.convertSafeString(map, "ORG_ID");
				int tcom = FieldUtil.convertSafeInt(map, "COMPLETED", 0);
				int tsub = FieldUtil.convertSafeInt(map, "SUBMITTED", 0);
				int trej = FieldUtil.convertSafeInt(map, "REJECTED", 0);
				int tinp = FieldUtil.convertSafeInt(map, "IN PROGRESS", 0);
				int ttotal = FieldUtil.convertSafeInt(map, "TOTAL", 0);

				com += tcom;
				sub += tsub;
				rej += trej;
				inp += tinp;
				total += ttotal;
	%>
	<tr class="tr_change">
		<td align='left'><%=formid%></td>
		<td align='left'><%=CompanyHelper.getInstance().getOrgName(orgid)%></td>
		<td><%=tcom%></td>
		<td><%=StringUtil.formatPercent(tcom, ttotal)%></td>
		<td><%if(tsub>0){%><a href="javascript:showDetail('<%=getFormSystemId(formid)%>','<%=orgid%>','<%=CommonName.STATUS_SUBMITTED%>')"><%}%><%=tsub%></td>
		<td><%=StringUtil.formatPercent(tsub, ttotal)%></td>
		<td><%if(trej>0){%><a href="javascript:showDetail('<%=getFormSystemId(formid)%>','<%=orgid%>','<%=CommonName.STATUS_REJECTED%>')"><%}%><%=trej%></td>
		<td><%=StringUtil.formatPercent(trej, ttotal)%></td>
		<td><%if(tinp>0){%><a href="javascript:showDetail('<%=getFormSystemId(formid)%>','<%=orgid%>','<%=CommonName.STATUS_INPROGRESS%>')"><%}%><%=tinp%></td>
		<td><%=StringUtil.formatPercent(tinp, ttotal)%></td>
		<td><%=ttotal%></td>
	</tr>
	<%
		}
	%>
	<tr class="tr_change">
		<td><strong>Form Total</strong></td>
		<td><strong>Company Total</strong></td>
		<td><strong><%=com%></strong></td>
		<td><%=StringUtil.formatPercent(com, total)%></td>
		<td><strong><%=sub%></strong></td>
		<td><%=StringUtil.formatPercent(sub, total)%></td>
		<td><strong><%=rej%></strong></td>
		<td><%=StringUtil.formatPercent(rej, total)%></td>
		<td><strong><%=inp%></strong></td>
		<td><%=StringUtil.formatPercent(inp, total)%></td>
		<td><strong><%=total%></strong></td>
	</tr>
	<%
		}else{
			out.println("<tr><td>No Data</td></tr>");
		}
	%>
</table>
<p align="center">--- End of Report ---</p>
</body>
</html>