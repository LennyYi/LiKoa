<%@include file="/common/head.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="com.aiait.eflow._thailand.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.*"%>
<%@page import="java.util.*"%>
<%
	ExpenseVO expense = (ExpenseVO) request.getAttribute("expense");
	String[] spExpenses = (String[]) request.getAttribute("spExpenses");
	String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Edit Expense</title>
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/calendar.js"></script>

<script language="javascript">
function submitExpense() {
    var form = document.forms[0];
    if (!formValidate(form)) return false;
    var desc = $("[name='desc']");
    if (desc.val() == "") {
        alert("Please input the " + desc.attr("title"));
        desc.focus();
        return false;
    }
    form.action = "<%=ctxPath%>/_thailand/expenseAction.it?method=saveExpense";
    form.submit();
};
</script>
</head>

<body>
<form name="form" action="" method="post">
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr class="tr1">
        <td align='center' colspan='2'>Expense</td>
    </tr>
    <tr>
        <td class='tr3' align="right" width="35%">Type (<font color="red">*</font>)</td>
        <td><select name="type">
        <%
            String value = expense == null ? "" : expense.getType();
        %>
            <option value="GOE" <%=value.equalsIgnoreCase("GOE") ? "selected" : ""%>>GOE</option>
            <option value="VEA" <%=value.equalsIgnoreCase("VEA") ? "selected" : ""%>>VEA</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align='right'>A/C Code (<font color="red">*</font>)</td>
        <%
            value = expense == null ? "" : expense.getAcCode();
        %>
        <td><input type='text' name='code' size='20' maxLength='20' required='true' value='<%=value%>' title="A/C Code"></td>
    </tr>
    <tr>
        <td class='tr3' align='right'>A/C Description (<font color="red">*</font>)</td>
        <%
            value = expense == null ? "" : expense.getAcDesc();
        %>
        <td><textarea name='desc' rows="4" cols="40" maxlength="200" required='true' title="A/C Description"><%=value%></textarea></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Relate HR</td>
        <td><select name="relateHR">
        <%
            value = expense == null ? "" : expense.getRelateHR();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Relate RE</td>
        <td><select name="relateRE">
        <%
            value = expense == null ? "" : expense.getRelateRE();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Relate IT</td>
        <td><select name="relateIT">
        <%
            value = expense == null ? "" : expense.getRelateIT();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Specific Expense</td>
        <td><select name="spExpense">
            <option value=""></option>
        <%
            for (String spExpense : ExpenseVO.getSpExpenses()) {
                spExpense = spExpense.trim();
                String selected = expense != null && expense.getSpExpense().equalsIgnoreCase(spExpense) ? "selected" : "";
        %>
            <option value="<%=spExpense%>" <%=selected%>><%=spExpense%></option>
        <%
            }
        %>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Finance</td>
        <td><select name="finance">
        <%
            value = expense == null ? "" : expense.getFinance();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">FSI</td>
        <td><select name="fsi">
        <%
            value = expense == null ? "" : expense.getFsi();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td class='tr3' align="right">CAPEX</td>
        <td><select name="capex">
        <%
            value = expense == null ? "" : expense.getCapex();
        %>
            <option value="No" <%=value.equalsIgnoreCase("No") ? "selected" : ""%>>No</option>
            <option value="Yes" <%=value.equalsIgnoreCase("Yes") ? "selected" : ""%>>Yes</option>
        </select></td>
    </tr>
    <tr>
        <td align="center" colspan="2"><input type="button" name="smBtn"
            value='<i18n:message key="button.submit"/>' onclick="submitExpense()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"> &nbsp; <input
            type="button" name="returnBtn" value='<i18n:message key="button.cancel"/>'
            onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</body>
</html>