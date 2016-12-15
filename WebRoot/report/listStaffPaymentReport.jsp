<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.wkf.util.DataMapUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%
    boolean mergeAccount = "Y".equalsIgnoreCase((String) request.getAttribute("mergeAccount"));
%>

<html>
<head>
<title>Personal Applied Form List</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<%boolean ISEXCEL="EXCEL".equals(request.getParameter("display"))?true:false;
  if(ISEXCEL){%>
<meta Content-Disposition="attachment; filename=a.xls">
<%} else {%>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>

<script type="text/javascript">

</script>

<%}%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;">
<%
Collection formList = (ArrayList) request.getAttribute("resultList"); // search result list
Double totalAmount = (Double) request.getAttribute("totalAmount");
DecimalFormat df2 = new DecimalFormat("#.00");

if(ISEXCEL){//EXCEL or HTML?
	response.setContentType("application/vnd.ms-excel;charset=GBK");
}else{
	response.setContentType("text/html;charset=GBK");
%>
<b>总计 <%=formList == null ? "0" : "" + formList.size()%> 条纪录, 金额: <%=totalAmount == null ? "0" : "" + df2.format(totalAmount)%></b><br>
<%
}

%>
<table width="100%" border="<%=ISEXCEL?"1":"0"%>" cellpadding="0" cellspacing="1"
	class="sortable" id="mytable" style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
		<td align='center' style="FONT-WEIGHT:normal;">所属公司</td>
		<td align='center' style="FONT-WEIGHT:normal;">表单流水号</td>
		<td align='center' style="FONT-WEIGHT:normal;">客户名称</td>
		<td align='center' style="FONT-WEIGHT:normal;">开户银行</td>
		<td align='center' order="PAYEEACC">银行账号</td>
		<td align='center' style="FONT-WEIGHT:normal;">金额</td>
		<td align='center' style="FONT-WEIGHT:normal;">备注</td>
	</tr>
	<%if (formList != null) {
				Iterator formIt = formList.iterator();
				int i = 1;
				while (formIt.hasNext()) {
					StaffEbankVO vo = (StaffEbankVO) formIt.next();
					%>
	<tr class="tr_change">
		<td><%=vo.getOrgName()%>&nbsp;</td>
		<td><%=vo.getRequestNo()%>&nbsp;</td>
		<td><%=vo.getRequestStaffCNName()%>&nbsp;</td>
		<td><%=vo.getPayeeBank()%>&nbsp;</td>
		<td><%=vo.getPayeeAccount()%>&nbsp;</td>
		<td><%=df2.format(vo.getAmount())%>	<input type=hidden name="id" value=""></td>
        <td><%=vo.getRemark()%></td>
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
	if($("id")!=null) {
		parent.document.all('expBtn').disabled = false;
	    parent.document.all('expPDFBtn').disabled = false;
	} else {
		parent.document.all('expBtn').disabled = true;
		parent.document.all('expPDFBtn').disabled = true;
	}
    parent.document.all("iframeName").style.height = document.body.scrollHeight + 20;
    window.onload=function(){enableTooltips()};
</script>
<%}%>