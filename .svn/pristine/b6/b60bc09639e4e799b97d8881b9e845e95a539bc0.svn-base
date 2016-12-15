<%--
    Task_ID    			Author     Modify_Date    Description
1.  ePayment Phase 2   Robert.Ouyang   04/12/2010     initial
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.*"%>
<%@page import="com.aiait.eflow.util.FieldUtil,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@page isELIgnored="false" %>

<html>
<head>
<title><i18n:message key="menu.report.statusmonitoring"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"> 
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0"> 
<%	
	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	AuthorityHelper authority = AuthorityHelper.getInstance();
	String formType = (String) request.getAttribute("formType");
	Collection allforms = (Collection) request.getAttribute("allforms");
	String forms = (String) request.getAttribute("forms");
	if (forms == null)forms = "";
	
	Collection formList = (ArrayList) request.getAttribute("resultList"); // search result list
	
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
	  document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=statusMonitoring";
	  document.forms[0].target = "";
      document.forms[0].submit();
  }
  var xmlhttp = createXMLHttpRequest();
  function getOptionList(formType){
	   type = formType;
	   var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"";
	    
	   xmlhttp.open("GET", url, true);
	   var objId = "forms";
	   xmlhttp.onreadystatechange=updateForms;
	   xmlhttp.setRequestHeader("If-Modified-Since","0");
	   xmlhttp.send(null);	
  }  
  function exportToExcel(){
     document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=statusMonitoring&display=EXCEL";
     document.forms[0].target = "newWindow";
     document.forms[0].submit();
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
	action="<%=request.getContextPath()%>/reportAction.it?method=statusMonitoring">
<table border="0" width="100%" cellspacing="0" cellpadding="0" id="table0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_statusmonitoring.location"/></font></strong>
 	 </td>
 </tr>
--></table>	
<input type="hidden" name="display">
<table width="100%"  bordercolor="#6595D6" style="border-collapse:collapse;"  border="1" cellpadding="0" cellspacing="1">
	<TR align="center">
		<TD height="25" colspan="4" class="tr1"><i18n:message key="menu.report.statusmonitoring"/></TD>
	</TR>		
	<TR>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.form_type" /></span></div>
		</TD>
		<TD width="30%" height="20" colspan=3><select name="formType"
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
		<TD width="60%" colspan=3><select name="forms" style="height: 100%"
			MULTIPLE>
			<option value="" <%=(forms.length() == 0) ? "SELECTED" : ""%>>All                                </option>
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
	</TR>
	<TR>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.submit_date_from" /></span></div>
		</TD>
		<TD width="35%" height="20"><input type="text"
			name="beginDateStr" onclick='setday(this)'
			value="<%=((String) request.getAttribute("beginDateStr") == null
								|| "".equals((String) request.getAttribute("beginDateStr")) ? "" : (String) request
								.getAttribute("beginDateStr"))%>">(MM/DD/YYYY)
		</TD>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="report_overduesumreport.processedto" /></span></div>
		</TD>
		<TD width="30%" height="20"><input type="text" name="endDateStr"
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
	style="border:1px #8899cc solid;">

	<%
		if (ISEXCEL) {
	%>
	<tr class="liebiao_tou">
		<td align='center' colspan='11'><Strong><i18n:message key="menu.report.statusmonitoring"/></Strong></td>
	</tr>
	<tr class="liebiao_tou">
		<td align='right' colspan='11'>Transaction between ${param.beginDateStr} and ${param.endDateStr}</td>
	</tr>
	<%
		}
	%>

	<tr class="liebiao_tou">
		<td align='center'><Strong>Status</Strong></td>
		<td align='center'><Strong>All Company</Strong></td>
		<td align='center'><Strong>CHO</Strong></td>
		<td align='center'><Strong>SH</Strong></td>
		<td align='center'><Strong>BJ</Strong></td>
		<td align='center'><Strong>JS</Strong></td>
		<td align='center'><Strong>GD</Strong></td>
		<td align='center'><Strong>SZ</Strong></td>
		<td align='center'><Strong>FS</Strong></td>
		<td align='center'><Strong>DG</Strong></td>
		<td align='center'><Strong>JM</Strong></td>
	</tr>

	<%
		Collection result = (Collection) request.getAttribute("result");
	
		int bj=0, cho=0, dg=0, fs=0, gd=0, jm=0, js=0, sh=0, sz=0, total=0;
		int cbj=0, ccho=0, cdg=0, cfs=0, cgd=0, cjm=0, cjs=0, csh=0, csz=0, ctotal=0;
		int ibj=0, icho=0, idg=0, ifs=0, igd=0, ijm=0, ijs=0, ish=0, isz=0, itotal=0;
		HashMap draftMap = null;
		
		if (result != null && result.size() > 0) {
			Iterator it = result.iterator();
			
			boolean inProgress = false;
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				String status = FieldUtil.convertSafeString(map, "FLOW_STATUS");
				if("In Progress".equals(status)) {
					inProgress = true;
				}else{
					inProgress = false;
				}
				if("Draft".equals(status)) {
					draftMap = map;
					inProgress = false;
					continue;
				}
				int tcho = FieldUtil.convertSafeInt(map, "CHO", 0);
				int tsh = FieldUtil.convertSafeInt(map, "SH", 0);
				int tbj = FieldUtil.convertSafeInt(map, "BJ", 0);
				int tjs = FieldUtil.convertSafeInt(map, "JS", 0);
				int tgd = FieldUtil.convertSafeInt(map, "GD", 0);
				int tsz = FieldUtil.convertSafeInt(map, "SZ", 0);
				int tfs = FieldUtil.convertSafeInt(map, "FS", 0);
				int tdg = FieldUtil.convertSafeInt(map, "DG", 0);
				int tjm = FieldUtil.convertSafeInt(map, "JM", 0);
				int ttotal = FieldUtil.convertSafeInt(map, "TOTAL", 0);
				
				bj+=tbj; cho+=tcho; dg+=tdg; fs+=tfs; gd+=tgd; jm+=tjm; js+=tjs; sh+=tsh; sz+=tsz; total+=ttotal;
				if("Completed".equals(status) || "Rejected".equals(status)){
					cbj+=tbj; ccho+=tcho; cdg+=tdg; cfs+=tfs; cgd+=tgd; cjm+=tjm; cjs+=tjs; csh+=tsh; csz+=tsz; ctotal+=ttotal;	
				}
				if("In Progress".equals(status)){
					ibj+=tbj; icho+=tcho; idg+=tdg; ifs+=tfs; igd+=tgd; ijm+=tjm; ijs+=tjs; ish+=tsh; isz+=tsz; itotal+=ttotal;	
				}
				
	%>
	<tr class="tr_change" >
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=status%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=ttotal%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tcho%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tsh%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tbj%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tjs%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tgd%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tsz%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tfs%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tdg%><%=inProgress ? "</Strong></font>":"" %></td>
		<td><%=inProgress ? "<font color=\"red\"><Strong>":"" %><%=tjm%><%=inProgress ? "</Strong></font>":"" %></td>
	</tr>
	<%
		}
	%>
	<tr class="tr_change">
		<td><Strong>Total(excl. draft)</Strong></td>
		<td><Strong><%=total%></Strong></td>
		<td><Strong><%=cho%></Strong></td>
		<td><Strong><%=sh%></Strong></td>
		<td><Strong><%=bj%></Strong></td>
		<td><Strong><%=js%></Strong></td>
		<td><Strong><%=gd%></Strong></td>
		<td><Strong><%=sz%></Strong></td>
		<td><Strong><%=fs%></Strong></td>
		<td><Strong><%=dg%></Strong></td>
		<td><Strong><%=jm%></Strong></td>
	</tr>
	<%
		if(draftMap!=null){
	%>
	<tr class="tr_change">
		<td>Draft</td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "TOTAL", 0)%></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "CHO", 0)  %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "SH", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "BJ", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "JS", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "GD", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "SZ", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "FS", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "DG", 0)   %></td>
		<td><%=FieldUtil.convertSafeInt(draftMap, "JM", 0)   %></td>
	</tr>	
	<%
		}
	%>
	<tr class="tr_change">
		<td>(Complete+Reject)/Total%</td>
		<td><%=StringUtil.formatPercent(ctotal, total)%></td>
		<td><%=StringUtil.formatPercent(ccho, cho)%></td>
		<td><%=StringUtil.formatPercent(csh, sh)%></td>
		<td><%=StringUtil.formatPercent(cbj, bj)%></td>
		<td><%=StringUtil.formatPercent(cjs, js)%></td>
		<td><%=StringUtil.formatPercent(cgd, gd)%></td>
		<td><%=StringUtil.formatPercent(csz, sz)%></td>
		<td><%=StringUtil.formatPercent(cfs, fs)%></td>
		<td><%=StringUtil.formatPercent(cdg, dg)%></td>
		<td><%=StringUtil.formatPercent(cjm, jm)%></td>
	</tr>
	<tr class="tr_change">
		<td><font color="red"><Strong>(In Progress)/Total%</Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(itotal, total)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(icho, cho)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(ish, sh)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(ibj, bj)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(ijs, js)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(igd, gd)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(isz, sz)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(ifs, fs)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(idg, dg)%></Strong></font></td>
		<td><font color="red"><Strong><%=StringUtil.formatPercent(ijm, jm)%></Strong></font></td>
	</tr>	
	<%
		}
	%>
</table>
<p align="center">--- End of Report ---</p>
</body>
</html>
<script language="javascript">
//getOptionList(document.all.formType.value);
</script>