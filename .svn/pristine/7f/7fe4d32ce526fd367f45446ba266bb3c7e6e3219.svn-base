<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
--%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.wkf.util.DataMapUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@ page contentType="text/html;charset=GBK" %>  	
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

<%}%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;">
<%
Collection formList = (ArrayList) request.getAttribute("resultList"); // search result list
String mergeAccount = (String) request.getAttribute("mergeAccount"); 
double totalAmt = 0.00;
if(ISEXCEL){//EXCEL or HTML?
	response.setContentType("application/vnd.ms-excel;charset=GBK");
}else{
	response.setContentType("text/html;charset=GBK");
%>
<b>总计 <%=formList == null ? "0" : "" + formList.size()%> 条纪录, 金额: <a id=totalAmt>0.00</a></b><br>
<%
}

%>
<table width="100%" border="<%=ISEXCEL?"1":"0"%>" cellpadding="0" cellspacing="1"
	class="sortable" id="mytable" style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
		<td align='center' <%=("N".equals(mergeAccount)?"order='request_no'":"style='FONT-WEIGHT:normal;'")%>>表单流水号</td>
		<td align='center' style="FONT-WEIGHT:normal;">付款帐号开户行</td>
		<td align='center' <%=("Y".equals(mergeAccount)?"order='request_no'":"style='FONT-WEIGHT:normal;'")%>>付款帐号</td>
		<td align='center' style="FONT-WEIGHT:normal;">付款帐号名称</td>
		<td align='center' style="FONT-WEIGHT:normal;">收款帐号开户行</td>
		<td align='center' style="FONT-WEIGHT:normal;">省份名</td>
		<td align='center' style="FONT-WEIGHT:normal;">地市名</td>
		<td align='center' style="FONT-WEIGHT:normal;">收款帐号</td>
		<td align='center' style="FONT-WEIGHT:normal;">收款帐号名称</td>
		<td align='center' style="FONT-WEIGHT:normal;">金额（分）</td>
		<td align='center' style="FONT-WEIGHT:normal;">汇款用途</td>
		<td align='center' style="FONT-WEIGHT:normal;">备注</td>
	</tr>
	<%if (formList != null) {
				Iterator formIt = formList.iterator();
				int i = 1;
				while (formIt.hasNext()) {
					PaymentVO vo = (PaymentVO) formIt.next();
					String[] tmp = {""};
					String prov = "";
					String city = "";
					totalAmt += vo.getAmount();
					if(vo.getPayeeProvince()!=null){
						tmp=vo.getPayeeProvince().split("_");
						if(tmp.length > 1){
							prov = tmp[0];
							city = tmp[tmp.length-1];
						}else {
							prov=vo.getPayeeProvince();
							city=vo.getPayeeCity();
						}
					}
					%>
	<tr class="tr_change">
		<td><%=vo.getRequestNo()%></td>
		<td><%=vo.getPayBank()%></td>
		<td>&nbsp;<%=vo.getPayAccount()%>&nbsp;</td>
		<td><%=vo.getPayName()%></td>
		<td>&nbsp;<%=vo.getPayeeBank()%></td>
		<td><%=prov%></td>
		<td><%=city%></td>
		<td>&nbsp;<%=vo.getPayeeAccount()%>&nbsp;</td>
		<td><%=vo.getPayeeName()%></td>
		<td><%=vo.getAmount()%></td>
		<td><%=vo.getPurpose()%></td>
		<td><%=vo.getRemark()%><input type=hidden name="id" value=""></td>
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
<script language="javascript">
<%if(!ISEXCEL){%>
	if($("id")!=null) {
		parent.document.all('expBtn').disabled = false;
	    parent.document.all('expPDFBtn').disabled = false;
	} else {
		parent.document.all('expBtn').disabled = true;
		parent.document.all('expPDFBtn').disabled = true;
	}
	parent.document.all("iframeName").style.height = document.body.scrollHeight + 50;
    window.onload=function(){enableTooltips()};
<%}%>
document.all("totalAmt").innerText = formatNumber(<%=totalAmt%>,'#0.00');
</script>