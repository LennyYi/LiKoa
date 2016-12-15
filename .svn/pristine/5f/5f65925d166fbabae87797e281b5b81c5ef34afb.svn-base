<%--
    Task_ID    			Author     Modify_Date    Description
1.  ePayment Phase 2   Tinger Li   07/19/2010     initial
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@page import="com.aiait.eflow.util.FieldUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@page
	import="com.aiait.eflow.report.vo.*,java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.common.helper.*,com.aiait.eflow.wkf.util.DataMapUtil"%>
<%@page
	import="com.aiait.eflow.util.StringUtil"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>

<%@page isELIgnored="false"%>

<html>
<head>
<title>Processing Progress Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%
	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	AuthorityHelper authority = AuthorityHelper.getInstance();
    
	ProcessProgressTable progressTable = (ProcessProgressTable) request.getAttribute("progressTable");
	int[] measureDays = (int[]) request.getAttribute("measureDays");

	String formType = (String) request.getAttribute("formType");
    
	String progressType = (String) request.getParameter("progressType");
	
	Collection allforms = (Collection) request.getAttribute("allforms");

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
	  document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingProgress";
	  document.forms[0].target = "";
      document.forms[0].submit();
  }
  
  function exportToExcel(){
     document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingProgress&display=EXCEL";
     document.forms[0].target = "progressReport";
     document.forms[0].submit();
     document.forms[0].target = "";
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
  function runCode(val) {
	var winname = window.open('<%=request.getContextPath()%>/common/successmessage.jsp?success_message='+val, "_blank", '');
	//winname.document.open('text/html', 'replace');
	//winname.document.write(val);
	//winname.document.close();
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
	action="<%=request.getContextPath()%>/reportAction.it?method=processingProgress">
<table border="0" width="100%" cellspacing="0" cellpadding="0"
	id="table0">
	<tr>
		<td height="10"></td>
	</tr>
	<!--<tr>
		<td><strong><font color='#5980BB'
			family='Times New Roman'><i18n:message
			key="report_processprogress.location" /></font></strong></td>
	</tr>
--></table>
<input type="hidden" name="display">
<table width="100%" bordercolor="#6595D6"
	style="border-collapse: collapse;" border="1" cellpadding="0"
	cellspacing="1">
	<TR align="center">
		<TD height="25" colspan="4" class="tr1"><i18n:message
			key="menu.report.processing_progress" /></TD>
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
        <TD width="15%" height="20" class="tr3">
        <div align="right"><span class="style1"><i18n:message
            key="report_processprogress.progressscope" /></span></div>
        </TD>
        <TD width="30%" height="20"><select name="progressType">
            <option value="1" <%="1".equals(progressType) ? "selected" : ""%>>From Submit Form</option>
            <option value="2" <%="2".equals(progressType) ? "selected" : ""%>>From Receive Hard Copy</option>
            <%
              if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
            %>
            <option value="Fin" <%="Fin".equals(progressType) ? "selected" : ""%>>Holding In Finance</option>
            <option value="CHO Fin" <%="CHO Fin".equals(progressType) ? "selected" : ""%>>Holding In CHO Finance</option>
            <option value="Local Fin" <%="Local Fin".equals(progressType) ? "selected" : ""%>>Holding In Local Finance</option>
            <%}%>
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
			key="menu.report.processing_progress" /></Strong></td>
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
        <%
        String column = null;
        int fromDays = 0;
        int toDays = 0;
        for (int i = 0; i < measureDays.length; i++) {
            fromDays = toDays + 1;
            toDays = measureDays[i];
            column = fromDays + "~" + toDays;
        %>
        <td align='center'><Strong><%=column%></Strong></td>
        <%
        }
        column = ">" + toDays;
        %>
        <td align='center'><Strong><%=column%></Strong></td>
        <%
        fromDays = 0;
        toDays = 0;
        for (int i = 0; i < measureDays.length; i++) {
            fromDays = toDays + 1;
            toDays = measureDays[i];
            column = fromDays + "~" + toDays + "/%";
        %>
        <td align='center'><Strong><%=column%></Strong></td>
        <%
        }
        column = ">" + toDays + "/%";
        %>
        <td align='center'><Strong><%=column%></Strong></td>
		<td align='center'><Strong>Total</Strong></td>
	</tr>

	<%
        List progressList = progressTable.getProgressList();

		if (progressList != null && !progressList.isEmpty()) {
			Iterator it = progressList.iterator();
			while (it.hasNext()) {
			    ProcessProgressVO progress = (ProcessProgressVO) it.next();
	%>
	<tr class="tr_change">
		<td align='left'><%=progress.getFormId()%></td>
		<td align='left'><%=progress.getOrgName()%></td>
            <%
		        for (int i = 0; i < progress.getSubtotals().length; i++) {
            %>
            <td>
            <%if(i==progress.getSubtotals().length-1 && progress.getSubtotals()[i]>0){//只显示>21天的 %>
            	<a href="javascript:runCode('<%=progress.getDetail()[i]%>');">
            <%} %>
            <%=progress.getSubtotals()[i]%>
            </td>
            <%
                }
                for (int i = 0; i < progress.getPercents().length; i++) {
            %>
            <td><%=progress.getPercents()[i]%>%</td>
            <%
                }
            %>
		<td><%=progress.getTotal()%></td>
	</tr>
	    <%
		    }
            
			ProcessProgressVO totalProgress = progressTable.getTotalProgress();
	    %>
	<tr class="tr_change">
		<td><strong>Form Total</strong></td>
		<td><strong>Company Total</strong></td>
		    <%
                for (int i = 0; i < totalProgress.getSubtotals().length; i++) {
            %>
            <td><strong><%=totalProgress.getSubtotals()[i]%></strong></td>
            <%
                }
                for (int i = 0; i < totalProgress.getPercents().length; i++) {
            %>
            <td><strong><%=totalProgress.getPercents()[i]%>%</strong></td>
            <%
                }
            %>
		<td><strong><%=totalProgress.getTotal()%></strong></td>
	</tr>
	<%
		} else {
			out.println("<tr><td>No Data</td></tr>");
		}
	%>
</table>
<p align="center">--- End of Report ---</p>
</body>
</html>