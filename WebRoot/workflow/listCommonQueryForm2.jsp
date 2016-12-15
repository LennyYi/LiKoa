<%--
    Task_ID    Author     Modify_Date    Description
1.  IT0958     Robin.Hou   11/01/2007     initial
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@ include file="/common/loading.jsp"%>
<%@ include file="/common/ajaxmessage.jsp" %>
<html>
<head>
<title>Personal Applied Form List</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<%
	boolean contentField2 = false;
	boolean contentField3 = false;
	String contentFields = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LIST_CONTENT_FIELDS);
	if (contentFields != null && !"".equals(contentFields)) {
	    String[] aryContentFields = contentFields.split(";");
	    for (int i = 0; i < aryContentFields.length; i++) {
	        if (aryContentFields[i].equals("2")) {
	            contentField2 = true;
	        } else if (aryContentFields[i].equals("3")) {
	            contentField3 = true;
	      }
	   }
	}
  boolean ISEXCEL="EXCEL".equals(request.getParameter("display"))?true:false;
  if(ISEXCEL){%>
<meta Content-Disposition="attachment; filename=a.xls">
<%} else {%>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>

<script language="javascript">
  function goTo(url){
    parent.document.location = url;
  }
 function changeCheck(ob){
	var allIds = parent.document.all("exp_form_id");
	if(allIds.value.indexOf(ob.value+",")==-1 && ob.checked==true){
		allIds.value += ob.value + ",";
	} else if(allIds.value.indexOf(ob.value+",")>-1 && ob.checked==false){
		allIds.value = allIds.value.replace(new RegExp(ob.value+",",'g'),"");
	}
}

function addAll(ob){
	var allIds = parent.document.all("exp_form_id");
	if(ob.checked==true && document.all("id")!=null){
		allIds.value = "";
		for(i=0;i<document.all("id").length;i++){
			allIds.value += document.all("id")[i].value + ","
		} 
	} else if(allIds.value.indexOf(ob.value+",")>-1 && ob.checked==false){
		allIds.value = "";
	}
}
</script>
<%}%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;" onload="">
<%
if(ISEXCEL){//EXCEL or HTML?
	response.setContentType("application/vnd.ms-excel;charset=GBK");
}else{
	response.setContentType("text/html;charset=GBK");
}
Collection formList = (ArrayList) request
					.getAttribute("commonQueryFormList"); // search result list
%>
<table width="100%" border="<%=ISEXCEL?"1":"0"%>" cellpadding="0" cellspacing="1"
	class="sortable" id="mytable" style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
       <%
       if (!ISEXCEL) {
       %>
        <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this, 'id');addAll(this)"></td>
      <%}%>
		<td align='center'><i18n:message key="common.request_no"/></td>
		<td align='center'><i18n:message key="common.request_form"/></td>
        <td align='center'><i18n:message key="common.highlight_content"/></td>
      <%
        if (contentField2) {
      %>
        <td align='center' ><i18n:message key="common.highlight_content"/> 2</td>
      <%
        }
        if (contentField3) {
      %>
        <td align='center' ><i18n:message key="common.highlight_content"/> 3</td>
      <%
        }
      %>
		<td align='center'><i18n:message key="common.status"/></td>
		<td align='center'><i18n:message key="common.submit_draft_date"/></td>
        <td align='center'><i18n:message key="common.request_by"/></td>
		<td align='center'><i18n:message key="common.processed_by"/></td>
		<td align='center'><i18n:message key="common.processing_by"/></td>
		<td align='center'><i18n:message key="common.complete_date"/></td>
	</tr>
	<%if (formList != null) {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Iterator formIt = formList.iterator();
				int i = 1;
				while (formIt.hasNext()) {
					WorkFlowProcessVO vo = (WorkFlowProcessVO) formIt.next();
					Date cDate=null;
					Date rDate=null;
					if (vo.getSubmissionDateStr() != null
							&& !"".equals(vo.getSubmissionDateStr())) {
						cDate = df.parse(vo.getSubmissionDateStr());
					}
					if (vo.getReceivingDateStr() != null
							&& !"".equals(vo.getReceivingDateStr())) {
						rDate = df.parse(vo.getReceivingDateStr());
					}

					%>
	<tr class="tr_change">
      <%
      if (!ISEXCEL) {
      %>
        <td align='center'><input type="checkbox" name="id" value="<%=vo.getRequestNo()%>" onchange="changeCheck(this)"></td>
      <%}%>
		<td><a <%=vo.getHtmlTitleAttr()%>
			href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&status=<%=vo.getStatus()%>&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>&openType=sub"><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
		<td><%=vo.getFormName()%><input type="hidden" name="requestNo" value="<%=vo.getRequestNo()%>">&nbsp;&nbsp;</td>
        <td ><%=vo.getHighlightContent()%></td>
        <%
        if (contentField2) {
        %>
        <td ><%=vo.getHighlightContent2()%></td>
        <%
        }
        if (contentField3) {
        %>
        <td ><%=vo.getHighlightContent3()%></td>
        <%
        }
        %>
		<td><%=DataMapUtil.covertNodeStatus(vo
											.getStatus())%>&nbsp;&nbsp;</td>
		<td><%=(vo.getSubmissionDateStr() != null && !""
											.equals(vo.getSubmissionDateStr())) ? StringUtil
									.getDateStr(cDate, "MM/dd/yyyy HH:mm:ss")
									: ""%>&nbsp;&nbsp;</td>
        <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%>&nbsp;&nbsp;</td>
		<td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
		<td>
              <%
              String processing_by = null;
              if (CommonName.NODE_TYPE_WAITING.equals(vo.getNodeType())) {
                  processing_by = vo.getNodeName();
              } else {
                  processing_by = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor());
              }
              %>
              <%=processing_by%>&nbsp;&nbsp;
        </td>
		<td><%=(vo.getReceivingDateStr() != null && !"".equals(vo.getReceivingDateStr())) 
										&& "04".equals(vo.getStatus())
										? StringUtil.getDateStr(rDate, "MM/dd/yyyy HH:mm:ss")
										: ""%>&nbsp;&nbsp;</td>
	</tr>
	<%i++;
				}
			} else {

			%>
	<tr class="liebiao_nr2">
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
        <%
        if (contentField2) {
        %>
        <td>&nbsp;&nbsp;</td>
        <%
        }
        if (contentField3) {
        %>
        <td>&nbsp;&nbsp;</td>
        <%
        }
        %>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
	</tr>
	<%}%>
</table>
</body>
</html>
<%if(!ISEXCEL){%>
<script language="javascript">
	parent.document.all("exp_form_id").value = "";
	if($("requestNo")!=null)
		parent.document.all('hasResult').value = "true";
	else
		parent.document.all('hasResult').value = "false";
    parent.document.all("iframeName").style.height = document.body.scrollHeight + 20;
    window.onload=function(){enableTooltips()};
</script>
<%}%>