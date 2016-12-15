<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp"%>
<%@ page import="java.util.*,java.io.*,com.aiait.eflow.util.FileUtil"%>
<%
	String reportSystemId = request.getParameter("reportSystemId");
	if (reportSystemId == null || reportSystemId.equals("")) {
	    reportSystemId = "-1";
	}
	String sectionId = request.getParameter("sectionId");
	String html = (String) request.getAttribute("html");
	html = html == null ? "" : html;
%>
<html>
<head>
<title>Edit Report Function</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript">
    jQuery(function() {
        // For submit report within modal dialog.
        jQuery(document.createElement("base")).attr("target", "_self").prependTo(jQuery("head"));
    });
</script>
<script language="javascript">
	function saveFunction() {
		if (confirm("Are you sure to save?")) {
			document.forms[0].action = "<%=request.getContextPath()%>/reportManageAction.it?method=saveReportHtml&reportSystemId=<%=reportSystemId%>&sectionId=<%=sectionId%>";
			document.forms[0].submit();
		}
	}

	window.onload = function() {
		resize(500);
	}
</script>
</head>

<body>
<form name="mainReport" method='post'>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
	<tr class="liebiao_tou">
		<td align='center'>
		
			<i18n:message key="report_design.user_defined_function_html"/>
		
		</td>
	</tr>
	<tr>
		<td><textarea name='html' rows='35' cols='120' style="font-family: Courier New;"><%=html%></textarea></td>
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