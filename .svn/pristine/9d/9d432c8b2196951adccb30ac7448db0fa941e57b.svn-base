<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
--%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@ page contentType="application/vnd.ms-excel;charset=GBK" %>  	
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
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript" >
function changeCheck(ob,idx){
	var allIds = parent.document.all("mail_form_id");
	var repayable = parent.document.all("repayable");
	repayable.value = "true";
	if(allIds.value.indexOf(ob.value+",")==-1 && ob.checked==true){
		allIds.value += ob.value + ",";
	} else if(allIds.value.indexOf(ob.value+",")>-1 && ob.checked==false){
		allIds.value = allIds.value.replace(new RegExp(ob.value+",",'g'),"");
	}
	for(i=0;i<document.all("id").length;i++){
		if(document.all("id")[i].checked==true&&
		document.all("hasMidwayPayment").length>0&&
		document.all("hasMidwayPayment")[i].value.indexOf("Y")>-1){
			repayable.value = "false";
		}
	} 
}

function addAll(ob){
	var allIds = parent.document.all("mail_form_id");
	var repayable = parent.document.all("repayable");
	repayable.value = "true";
	if(ob.checked==true && document.all("id")!=null){
		allIds.value = "";
		for(i=0;i<document.all("id").length;i++){
			allIds.value += document.all("id")[i].value + ","
			if(document.all("hasMidwayPayment").length>0&&
			document.all("hasMidwayPayment")[i].value.indexOf("Y")>-1){
				repayable.value = "false";
			}
		} 
	//} else if(allIds.value.indexOf(ob.value+",")>-1 && ob.checked==false){
	} else if(ob.checked==false) {
		allIds.value = "";
	}
}
</script>
<%}%>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;" onload="">
<form name=subFORM method="post">
<%
if(ISEXCEL){//EXCEL or HTML?
	response.setContentType("application/vnd.ms-excel;charset=GBK");
}else{
	response.setContentType("text/html;charset=GBK");
}
Collection formList = (ArrayList) request
					.getAttribute("resultList"); // search result list
%>
<table width="100%" border="<%=ISEXCEL?"1":"0"%>" cellpadding="0" cellspacing="1"
	class="sortable" id="mytable" style="border:1px #8899cc solid;">
<%if(ISEXCEL){%>
	<tr class="liebiao_tou">
		<td align='center' colspan='8'><i18n:message key="report_careport.title"/></td>		
	</tr>
	<tr class="liebiao_tou">
		<td align='center' colspan='8'>Transaction between <%=(String)request.getAttribute("beginDate")%> and <%=(String)request.getAttribute("endDate")%> 
		</td>
	</tr>
<%}
int tmpCol = 0;
%>	
	<tr class="liebiao_tou">
		<td align='center' order=""><input type="checkbox" name="allBtn" onclick="selectAll(this, 'id');addAll(this)"></td>
		<td align='center' order="request_no"><i18n:message key="common.request_no"/></td>
		<td align='center' order="ca_amount"><i18n:message key="report_careport.caamount"/></td>	
		<td align='center' style="FONT-WEIGHT:normal; "><i18n:message key="common.request_by"/></td>
		<td align='center' order="receiving_date"><i18n:message key="common.complete_date"/></td>
		<td align='center' order="status"><i18n:message key="common.status"/></td>
		<td align='center' order="repaydate"><i18n:message key="report_careport.repaydate"/></td>
		<td align='center' style="FONT-WEIGHT:normal; "><i18n:message key="report_careport.paymode"/></td>
		<td align='center' style="FONT-WEIGHT:normal; ">ePayment ID</td>
		<td align='center' style="FONT-WEIGHT:normal; "><i18n:message key="report_invoicereport.amount"/></td>	
		<td align='center' style="FONT-WEIGHT:normal; "><i18n:message key="report_invoicereport.remark"/></td>
	</tr>
	<%if (formList != null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Iterator formIt = formList.iterator();
		int i = 1;
		while (formIt.hasNext()) {
			CAInfoVO vo = (CAInfoVO) formIt.next();
			Date cDate = null; Date rDate =null;
			if (vo.getCompleteDate() != null&& !"".equals(vo.getCompleteDate())) 
				cDate = df.parse(vo.getCompleteDate());
			if (vo.getRepayDate() != null&& !"".equals(vo.getRepayDate())) 
				rDate = df.parse(vo.getRepayDate());
			
			StaffVO staff = StaffTeamHelper.getInstance().getStaffByCode(vo.getRequestStaffCode());
			%>
	<tr class="tr_change">
		<td><input type="checkbox" name="id"
			value="<%=vo.getRequestNo()+"="+vo.getFormSystemId()%>"
			onchange="changeCheck(this,<%=i-1%>)">
			<input type="hidden" name="hasMidwayPayment" value="<%=vo.getHasMidwayPayment()%>">
			</td>
		<td>
		  <a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>&openType=sub"/>
		  <%=vo.getRequestNo()%></td>
		<td><%=new DecimalFormat("#.00").format(vo.getCaAmount())%></td>
		<td><%=staff== null? "" : staff.getStaffName()%></td>
		<td><%=cDate!= null? df.format(cDate): ""%>&nbsp;&nbsp;</td>
		<td><%=vo.getStatus()%></td>
		<td><%=rDate!= null? df.format(rDate): ""%>&nbsp;&nbsp;</td>
		<td><%=vo.getPaymodes()%></td>
		<td><%=vo.getFormNo()%></td>
		<td><%=vo.getRemark()%></td>
		<td><%=vo.getCheckNo()%></td>
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
	</tr>
	<%}%>
	<input type=hidden name="id" value="N">
	<input type=hidden name="hasMidwayPayment" value="N">
</table>
</form>
</body>
</html>
<%if(!ISEXCEL){%>
<script language="javascript"><!--
	var ids = document.getElementsByName("id"); 
	parent.document.all('expBtn').disabled = true; 
	parent.document.all('notifyBtn').disabled = true;
	parent.document.all('receiveBtn').disabled = true;
	if(ids.length>0){
		parent.document.all('expBtn').disabled=false; 
		parent.document.all('notifyBtn').disabled=false;
		parent.document.all('receiveBtn').disabled = false;
	}
    parent.document.all("iframeName").style.height = document.body.scrollHeight + 80;
    window.onload=function(){enableTooltips()};
</script>
<%}%>