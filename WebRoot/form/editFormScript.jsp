<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp"%>
<%@ page import="java.util.*,java.io.*,com.aiait.eflow.util.FileUtil"%>
<%
	String formSystemId = request.getParameter("formSystemId");
	if (formSystemId == null || formSystemId.equals("")) {
	    formSystemId = "-1";
	}
	String script = (String) request.getAttribute("script");
	script = script == null ? "" : script;
%>
<html>
<head>
<title>Edit Form Function</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript">
    jQuery(function() {
        // For submit form within modal dialog.
        jQuery(document.createElement("base")).attr("target", "_self").prependTo(jQuery("head"));
    });
</script>
<script language="javascript">
	function saveFunction() {
		if (confirm("Are you sure to save?")) {
			document.forms[0].action = "<%=request.getContextPath()%>/formManageAction.it?method=saveFormScript&formSystemId=<%=formSystemId%>";
			document.forms[0].submit();
		}
	}

	window.onload = function() {
		resize(500);
	}
</script>
</head>

<body>
<form name="mainForm" method='post'>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
	<tr class="liebiao_tou">
		<td align='center'>
		<% if (formSystemId.equals("-1")) { %>
			<i18n:message key="form_design.user_defined_function_global"/>
		<% } else { %>
			<i18n:message key="form_design.user_defined_function"/>
		<% } %>
		</td>
	</tr>
	<tr>
		<td><textarea name='script' rows='35' cols='120' style="font-family: Courier New;"><%=script%></textarea></td>
	</tr>
	<tr>
		<td colspan='2' align='center'>
		<input type='button' name='submitBtn' value='Submit' onclick='saveFunction()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		<input type='button' name='closeBtn' value='Close' onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		</td>
	</tr>
</table>
</form>
</body>

</html>