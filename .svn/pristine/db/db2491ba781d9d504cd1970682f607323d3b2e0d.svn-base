<%--
    Task_ID    			Author     Modify_Date    Description
1.  ePayment Phase 2   Robert.Ouyang   04/12/2010     initial
--%>

<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil"%>
<%@page import="com.aiait.eflow.util.FieldUtil"%>
<%@page
	import="java.util.*,com.aiait.eflow.housekeeping.vo.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>

<%@page isELIgnored="false"%>

<html>
<head>
<title><i18n:message key="menu.report.processingTrail" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<%
	boolean ISEXCEL = "EXCEL".equals(request.getParameter("display")) ? true : false;
	if (ISEXCEL) {
%>
<meta Content-Disposition="attachment; filename=a.xls">
<%
	} else {
%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
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
	  document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingTrail";
	  document.forms[0].target = "";
      document.forms[0].submit();
  }
  
  function exportToExcel(){
     document.forms[0].action = "<%=request.getContextPath()%>/reportAction.it?method=processingTrail&display=EXCEL";
     document.forms[0].target = "newWindow";
     document.forms[0].submit();
  }

  var xmlhttp = createXMLHttpRequest();
  
  function getOptionListF(formType){
   type = formType;
   var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"&moduleId=<%=ModuleOperateName.MODULE_FORM_INQUIRY%>";
   xmlhttp.open("GET", url, true);
   var objId = "formSystemId";
   xmlhttp.onreadystatechange=handleStateChange;
   xmlhttp.setRequestHeader("If-Modified-Since","0");
   xmlhttp.send(null);	
 }
  function getOptionListC(orgId){
 	var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";
  	var param = "orgId="+orgId;
  	
  	var myAjax = new Ajax.Request(
      url,
      {
         method:"post",       //
         parameters:param,   //
         setRequestHeader:{"If-Modified-Since":"0"},     //
         onComplete:function(x){    //
                updateListTeam(x.responseXML);
         },
         onError:function(x){          //
                 alert('Fail to get team list for company');
         }
      }
    ); 
  }

 function getStaffList(teamCode){
   	var url = "<%=request.getContextPath()%>/staffAction.it?method=getStaffList";
    	var param = "teamCode="+teamCode;
    	var myAjax = new Ajax.Request(
        url,
       {
           method:"post",       //
           parameters:param,   //
           setRequestHeader:{"If-Modified-Since":"0"},     //
           onComplete:function(x){    //
                  updateStaffList(x.responseXML);
           },
           onError:function(x){          //
                   alert('Fail to get staff list for company');
           } 
       } 
      ); 
 	}  
</script>
<%
	}
%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;" onload="">

<%
	if (ISEXCEL) {//EXCEL or HTML?
		response.setContentType("application/vnd.ms-excel;charset=GBK");
	} else {
		response.setContentType("text/html;charset=GBK");
	}

	StaffVO currentStaff = (StaffVO) session.getAttribute(CommonName.CURRENT_STAFF_INFOR);
	AuthorityHelper authority = AuthorityHelper.getInstance();

	String formSystemId = (String) request.getParameter("formSystemId");
	String requestedBy = (String) request.getParameter("requestedBy");
	String requestNo = (String) request.getParameter("requestNo");
	String formType = (String) request.getParameter("formType");
	String companyId = (String) request.getParameter("companyId");
	String teamId = (String) request.getAttribute("teamId");
	String status = (String) request.getParameter("status");

	Collection formSelectList = (ArrayList) request.getAttribute("formSelectList"); // select form list
	Collection staffList = (Collection) request.getAttribute("staffList");

	Iterator teamIt=null;

    Collection teamList = StaffTeamHelper.getInstance().getTeamList();
    if(teamList!=null){
	   teamIt = teamList.iterator();
    }
%>

<%
	if (!ISEXCEL) {
%>
<FORM name="statusMonitoringForm" method="post"
	action="<%=request.getContextPath()%>/reportAction.it?method=processingTrail">
<table border="0" width="100%" cellspacing="0" cellpadding="0"
	id="table0">
	<tr>
		<td height="10"></td>
	</tr>
	<!--<tr>
		<td><strong><font color='#5980BB'
			family='Times New Roman'><i18n:message
			key="report_processtrail.location" /></font></strong></td>
	</tr>
--></table>
<input type="hidden" name="display">
<table width="100%" bordercolor="#6595D6" style="border-collapse:collapse;"  border="1" cellpadding="0" cellspacing="1">
	<TR align="center">
		<TD height="25" colspan="4" class="tr1"><i18n:message
			key="menu.report.processingTrail" /></TD>
	</TR>
	<TR>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.request_no" /></span></div>
		</TD>
		<TD width="35%" height="20"><font size="2"> <INPUT
			Name="requestNo" Type="text" value="${param.requestNo}" class="text2"
			style="WIDTH: 130px" id="requestNo"> </font></TD>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.form_type" /></span></div>
		</TD>
		<TD width="30%" height="20"><select name="formType"
			onchange="getOptionListF(this.value)">
			<option value="">All</option>
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
	<tr>
		<TD width="18%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.form" /></span></div>
		</TD>
		<TD width="30%" height="20"><select
			name="formSystemId">
			<option value="">All</option>
			<%
				if (formSelectList != null && formSelectList.size() > 0) {
						Iterator formIt = formSelectList.iterator();
						while (formIt.hasNext()) {
							FormManageVO form = (FormManageVO) formIt.next();
							out.print("<option value='" + form.getFormSystemId() + "'");
							if (formSystemId != null && !"".equals(formSystemId)
									&& formSystemId.equals("" + form.getFormSystemId())) {
								out.print(" selected ");
							}
							out.print(">" + form.getFormId() + " - " + form.getFormName() + "</option>");
						}
					}
			%>
		</select></TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message
			key="common.status" /></span></div></TD>
      <TD width=35% height="20">
        <select name="status" id="status">
          <option value="01" <%=(status!=null && "01".equals(status))?"selected" : ""%>>Submitted</option>
          <option value="02" <%=(status!=null && "02".equals(status))?"selected" : ""%>>IN Progress</option>
          <option value="03" <%=(status!=null && "03".equals(status))?"selected" : ""%>>Rejected</option>
          <option value="04" <%=(status==null || "04".equals(status) || "".equals(status))?"selected" : ""%>>Completed</option>
        </select>
      </TD>
	</tr>
	<tr>
		<TD width="18%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.company" /></span></div>
		</TD>
		<TD width="35%" height="20"><select name="companyId"
			onchange="getOptionListC(this.value)">
			<%
				if (companyId == null) {
						companyId = currentStaff.getOrgId();
					}
					//Collection companyList = currentStaff.getOwnCompanyList(); 
					//CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
					Collection companyList = CompanyHelper.getInstance().getCompanyList();
					if (companyList != null) {
						Iterator companyIt = companyList.iterator();
						while (companyIt.hasNext()) {
							CompanyVO company = (CompanyVO) companyIt.next();
							if (company.getOrgId().equals(companyId)) {
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
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_user.team"/></span></div></TD>
		<TD width=35% height="20" > 
         <select name="team_code" onchange="getStaffList(this.value)">
           <option value=""></option>
           <%
           		if(teamIt!=null){           			
           			while(teamIt.hasNext()){
           				TeamVO tvo = (TeamVO)teamIt.next();
           				if(companyId!=null && !"".equals(companyId) && !companyId.equals(tvo.getOrgId())){
           					continue;
           				}
           %>
           <option value="<%=tvo.getTeamCode()%>" <%=(teamId!=null&&teamId.equals(tvo.getTeamCode()))?"selected":""%>><%=tvo.getTeamName()%></option>
           <%
           			}
           		}
           %>
         </select>
	    </TD>		
	</tr>
	<tr>
	<TD width="18%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.request_by" /></span></div>
		</TD>
		<TD width="35%" height="20" colspan=3><select name="requestedBy">
			<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			<%
				if (staffList != null) {
						Iterator staffIt = staffList.iterator();
						while (staffIt.hasNext()) {
							StaffVO staff = (StaffVO) staffIt.next();
							if (staff.getStaffCode().equals(requestedBy)) {
								out.println("<option value='" + staff.getStaffCode() + "' selected>" + staff.getStaffName()
										+ "</option>");
							} else {
								out.println("<option value='" + staff.getStaffCode() + "'>" + staff.getStaffName()
										+ "</option>");
							}
						}
					}
			%>
		</select></TD>
	</tr>
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
			key="common.submit_date_to" /></span></div>
		</TD>
		<TD width="30%" height="20"><input type="text" name="endDateStr"
			onclick='setday(this)'
			value="<%=((String) request.getAttribute("endDateStr") == null
								|| "".equals((String) request.getAttribute("endDateStr")) ? "" : (String) request
								.getAttribute("endDateStr"))%>">(MM/DD/YYYY)
		</TD>
	</TR>
	<TR>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.complete_date_from" /></span></div>
		</TD>
		<TD width="35%" height="20"><input type="text"
			name="compBeginDateStr" onclick='setday(this)'
			value="<%=((String) request.getAttribute("compBeginDateStr") == null
								|| "".equals((String) request.getAttribute("compBeginDateStr")) ? "" : (String) request
								.getAttribute("compBeginDateStr"))%>">(MM/DD/YYYY)
		</TD>
		<TD width="15%" height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message
			key="common.complete_date_to" /></span></div>
		</TD>
		<TD width="30%" height="20"><input type="text" name="compEndDateStr"
			onclick='setday(this)'
			value="<%=((String) request.getAttribute("compEndDateStr") == null
								|| "".equals((String) request.getAttribute("compEndDateStr")) ? "" : (String) request
								.getAttribute("compEndDateStr"))%>">(MM/DD/YYYY)
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
	cellspacing="2" id="mytable" style="border: 1px #6595D6 solid;">

	<%
		if (ISEXCEL) {
	%>
	<tr>
		<td align='center' colspan='10'><STRONG><i18n:message
			key="menu.report.processingTrail" /></STRONG></td>
	</tr>
	<tr>
		<td align='right' colspan='10'>Transaction between
		${param.beginDateStr} and ${param.endDateStr}</td>
	</tr>
	<%
		}
	%>

	<tr class="liebiao_tou">
		<td align='center'><STRONG>Request No</STRONG></td>
		<td align='center'><STRONG>Requestor</STRONG></td>
		<td align='center'><STRONG>Requestor's Team</STRONG></td>
		<td align='center'><STRONG>Content</STRONG></td>
		<td align='center'><STRONG>Processed Node</STRONG></td>
		<td align='center'><STRONG>Processed Date</STRONG></td>
		<td align='center'><STRONG>Processed By</STRONG></td>
		<td align='center'><STRONG>Processed Type</STRONG></td>
		<td align='center'><STRONG>Processed Remark</STRONG></td>
		<td align='center'><STRONG>Attachment</STRONG></td>
	</tr>

	<%
		Collection result = (Collection) request.getAttribute("result");

		if (result != null && result.size() > 0) {

			int rows = 0;
			int processingNum = 0;
			String curRequestNo = "";
			String curSubmiter = "";
			String curTeam_name = "";
			String curContent = "";

			StringBuilder reqTrHead = new StringBuilder();
			StringBuilder reqTrEnd = new StringBuilder();
			StringBuilder nextTrHead = new StringBuilder();
			StringBuilder nextTrEnd = new StringBuilder();
			StringBuilder trs = new StringBuilder();

			Iterator it = result.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				String REQUEST_NO = FieldUtil.convertSafeString(map, "REQUEST_NO");
				String SUBMITTER = FieldUtil.convertSafeString(map, "REQUESTOR");
				String team_name = FieldUtil.convertSafeString(map, "TEAM_NAME");
				String content = FieldUtil.convertSafeString(map, "CONTENT");
				String node_name = FieldUtil.convertSafeString(map, "NODE_NAME");
				if(node_name.equals("Begin"))node_name="Requestor";
				String handle_date = FieldUtil.convertSafeString(map, "HANDLE_DATE");
				if(handle_date != null && handle_date.length() >= 19){
					handle_date = handle_date.substring(0,19);
				}
				String processor = FieldUtil.convertSafeString(map, "PROCESSOR");
				String handle_type = FieldUtil.convertSafeString(map, "HANDLE_TYPE");
				if ("00".equals(handle_type)) {
					handle_type = "Draft";
				} else if ("01".equals(handle_type)) {
					handle_type = "Submit";
				} else if ("02".equals(handle_type)) {
					handle_type = "Withdraw";
				} else if ("03".equals(handle_type)) {
					handle_type = "Approve";
				} else if ("04".equals(handle_type)) {
					handle_type = "Reject";
				} else if ("05".equals(handle_type)) {
					handle_type = "Complete";
				} else if ("06".equals(handle_type)) {
					handle_type = "Invite Expert";
				} else if ("07".equals(handle_type)) {
					handle_type = "Expert advise";
				}
				String handle_comments = FieldUtil.convertSafeString(map, "HANDLE_COMMENTS");
				String attach_file = FieldUtil.convertSafeString(map, "ATTACH_FILE");

				if (rows == 0) {
					// the first row

					reqTrEnd.append("<td nowrap >" + node_name + "</td>");
					reqTrEnd.append("<td nowrap style=\"mso-number-format:\\@ \" >" + handle_date + "</td>");
					reqTrEnd.append("<td nowrap >" + processor + "</td>");
					reqTrEnd.append("<td nowrap >" + handle_type + "</td>");
					reqTrEnd.append("<td>&nbsp;" + handle_comments + "</td>");
					reqTrEnd.append("<td>&nbsp;" + attach_file + "</td>");
					reqTrEnd.append("</tr>");


					if (result.size() == 1) {
						// only one row
						reqTrHead.append("<tr>");
						reqTrHead.append("<td nowrap >" + REQUEST_NO + "</td>");
						reqTrHead.append("<td nowrap >" + SUBMITTER + "</td>");
						reqTrHead.append("<td nowrap >" + team_name + "</td>");
						reqTrHead.append("<td>&nbsp;" + content + "</td>");

						out.print(reqTrHead.toString());
						out.print(reqTrEnd.toString());

						continue;
					}

					processingNum = 1;
					curRequestNo = REQUEST_NO;
					curSubmiter = SUBMITTER;
					curTeam_name = team_name;
					curContent = content;

					rows++;

					continue;
				}

				if (curRequestNo.equals(REQUEST_NO) && !ISEXCEL) {
					trs.append("<tr class=\"tr_change\">");
					trs.append("<td nowrap >" + node_name + "</td>");
					trs.append("<td nowrap style=\"mso-number-format:\\@ \">" + handle_date + "</td>");
					trs.append("<td nowrap >" + processor + "</td>");
					trs.append("<td nowrap >" + handle_type + "</td>");
					trs.append("<td>&nbsp;" + handle_comments + "</td>");
					trs.append("<td>&nbsp;" + attach_file + "</td>");
					trs.append("</tr>");

					processingNum++;

				} else {

					//the rowspan is determinate
					reqTrHead.append("<tr>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curRequestNo
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curSubmiter
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curTeam_name
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">&nbsp;" + curContent
							+ "</td>");

					//output the previous row
					out.println(reqTrHead.toString());
					out.println(reqTrEnd.toString());
					out.println(trs.toString());

					//clean the buffer of previous row
					if (reqTrHead.length() > 0)
						reqTrHead.delete(0, reqTrHead.length());
					if (reqTrEnd.length() > 0)
						reqTrEnd.delete(0, reqTrEnd.length());
					if (trs.length() > 0)
						trs.delete(0, trs.length());

					//
					reqTrEnd.append("<td>" + node_name + "</td>");
					reqTrEnd.append("<td style=\"mso-number-format:\\@ \">" + handle_date + "</td>");
					reqTrEnd.append("<td>" + processor + "</td>");
					reqTrEnd.append("<td>" + handle_type + "</td>");
					reqTrEnd.append("<td>&nbsp;" + handle_comments + "</td>");
					reqTrEnd.append("<td>&nbsp;" + attach_file + "</td>");
					reqTrEnd.append("</tr>");

					processingNum = 1;
					curRequestNo = REQUEST_NO;
					curSubmiter = SUBMITTER;
					curTeam_name = team_name;
					curContent = content;
				}

				if (rows == result.size() - 1) {
					// the last row
					reqTrHead.append("<tr>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curRequestNo
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curSubmiter
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">" + curTeam_name
							+ "</td>");
					reqTrHead.append("<td valign=\"top\" rowspan=\"" + processingNum + "\">&nbsp;" + curContent
							+ "</td>");

					out.println(reqTrHead.toString());
					out.println(reqTrEnd.toString());
					out.println(trs.toString());

					if (reqTrHead.length() > 0)
						reqTrHead.delete(0, reqTrHead.length());
					if (reqTrEnd.length() > 0)
						reqTrEnd.delete(0, reqTrEnd.length());
					if (trs.length() > 0)
						trs.delete(0, trs.length());
				}

				rows++;
			}
		}else{
			out.println("<tr><td>No Data</td></tr>");
		}
	%>
</table>
	<%
		if (!ISEXCEL) {
	%>
<pageTag:page action="/reportAction.it?method=processingTrail"></pageTag:page>
	<%
		}else{
			out.println("<p align=\"center\">--- End of Report ---</p>");
		}
	%>
</body>
</html>
<script language="javascript">
	if("<%=formType%>"=="null"&&$("formType").value=="") {
		<%if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
			ModuleOperateName.MODULE_FORM_INQUIRY, "12")) {
		%>
		$("formType").value="12";
		<%}%>
	}
    <%
    if (CompanyHelper.getInstance().getEFlowCompany().equals(
            CompanyHelper.EFlow_AIAIT) && "false".equals(request.getParameter("needquery"))) {
    %>
		$("status").value="02";
		$("formType").value="";
		$("compBeginDateStr").value="";
		$("compEndDateStr").value="";
    <%}%>
</script>