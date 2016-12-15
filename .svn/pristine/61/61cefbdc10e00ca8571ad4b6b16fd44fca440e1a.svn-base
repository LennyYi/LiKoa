<%@page import="java.util.*,com.aiait.eflow._thailand.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*"%>
<%@page import="com.aiait.framework.util.*"%>
<%@taglib uri="/WEB-INF/purview.tld" prefix="purview"%>
<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="/common/loading.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%
    List<ExpenseVO> list = (List<ExpenseVO>) request.getAttribute("expenseList");
	String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Expense</title>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/sorttable.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/resizeCol.js"></script>
<script language="javascript">
function addExpense() {
    var url = "<%=ctxPath%>/_thailand/expenseAction.it?method=editExpense";
    window.location = url;
};

function deleteExpense() {
    if (checkSelect('id') <= 0) {
        alert("Please select the record(s) to delete!");
        return;
    }
    if (confirm("Are you sure to delete the selected record(s)?")) {
        document.forms[0].action = "<%=ctxPath%>/_thailand/expenseAction.it?method=deleteExpense";
        document.forms[0].submit();
    }
};

function uploadExpense() {
    var url = "<%=ctxPath%>/_thailand/expenseAction.it?method=selectUploadFile&dialog=modal";
    var value = window.showModalDialog(url, window, "dialogWidth=450px;dialogHeight=100px;center=yes;status=no;resizable=no");
    if (value != null) {
        window.location.reload();
    }
};
</script>
</head>

<body>
	<form name="form" method="post">
		<table border="0" width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td height="10"></td>
			</tr>
			<!--<tr>
				<td><strong><font color='#5980BB'>Current Location: > House Keeping > Expense</font></strong></td>
			</tr>-->
		</table>
		<table width="100%" border="0">
			<tr class="tr1">
				<td align='center'><b>Expense</b></td>
			</tr>
			<tr>
				<td align='left'>
				    <input type="button" name="addBtn" value='<i18n:message key="button.add"/>'
					onclick="addExpense()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
					onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
					onmouseup="this.className='btn3_mouseup'"> &nbsp; 
					<input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' 
					onclick="deleteExpense()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" 
					onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" 
					onmouseup="this.className='btn3_mouseup'"> &nbsp;
					<input type="button" name="uploadBtn" value='Upload Expense' 
					onclick="uploadExpense()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" 
					onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" 
					onmouseup="this.className='btn3_mouseup'"/>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="mytable"
						style="border: 1px #8899cc solid;">
						<tr class="liebiao_tou" align='center'>
							<td width="30px"><input type="checkbox" name="allBtn" onclick="selectAll(this, 'id')"></td>
							<td>Type</td>
							<td>A/C Code</td>
							<td>A/C Description</td>
							<td>Relate HR</td>
							<td>Relate RE</td>
							<td>Relate IT</td>
							<td>Specific Expense</td>
							<td>Finance</td>
							<td>FSI</td>
							<td>CAPEX</td>
						</tr>
						<%
						    for (ExpenseVO expense : list) {
								String id = expense.getId();
						%>
						<tr class="tr_change" align='center'>
							<td><input type="checkbox" name="id" value="<%=id%>"></td>
							<td><%=expense.getType()%></td>
							<td><a href="<%=ctxPath%>/_thailand/expenseAction.it?method=editExpense&id=<%=id%>"><%=expense.getAcCode()%></a></td>
							<td align='left'><%=expense.getAcDesc()%></td>
							<td><%=expense.getRelateHR()%></td>
							<td><%=expense.getRelateRE()%></td>
							<td><%=expense.getRelateIT()%></td>
							<td><%=expense.getSpExpense()%></td>
							<td><%=expense.getFinance()%></td>
							<td><%=expense.getFsi()%></td>
							<td><%=expense.getCapex()%></td>
						</tr>
						<%
						    }
						%>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
<script language="javascript">
    setResizeAble(mytable);
</script>
</html>
