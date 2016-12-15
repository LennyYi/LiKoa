<%@ page contentType="text/html; charset=gb2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />

<%
	String message = (String) request.getAttribute("error");
	if (message == null) {
		message = (String) request.getAttribute("tip");
	}
	if (message == null) {
		message = (String) request.getParameter("error");
	}
	if (message == null) {
		message = (String) request.getParameter("tip");
	}
	if (message == null) {
		message = "There is a system error. Please contact administrator!";
	}
	String returnType = (String) request.getAttribute("returnType");
	if (returnType == null) {
		returnType = "back";
	}
	//AppScan Remediation: sanitize the message content to avoid cross-site scripting
	message = message.replaceAll("<","[");
	message = message.replaceAll(">","]"); 
%>
<html>
<head>
<title>Error Display</title>
<style type="text/css">
body {
	margin: 0px;
	SCROLLBAR-FACE-COLOR: #799AE1;
	SCROLLBAR-HIGHLIGHT-COLOR: #799AE1;
	SCROLLBAR-SHADOW-COLOR: #799AE1;
	SCROLLBAR-DARKSHADOW-COLOR: #799AE1;
	SCROLLBAR-3DLIGHT-COLOR: #799AE1;
	SCROLLBAR-ARROW-COLOR: #FFFFFF;
	SCROLLBAR-TRACK-COLOR: #AABFEC;
	font-size: 9px;
	color: #333333;
	margin-top: 10px;
	margin-right: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	background-color: #FBFBFC;
}

.btn3_mouseout {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mouseover {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #EBE5D3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mousedown {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mouseup {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

INPUT {
	FONT-SIZE: 12px;
	border: 1px solid #AAC0F6;
}

.out {
	position: absolute;
	background: #bbb;
	margin: 10px auto;
	width: 450px;
	height: 100px;
	top: 20%;
	left: 30%;
	bottom: 30%;
	text-align: left;
}

.in {
	background: #fff;
	border: 1px solid #555;
	padding: 10px 5px;
	position: relative;
	top: -5px;
	left: -5px;
	vertical-align: middle;
}

.table2 {
	background-image: url(<%=request.getContextPath()%>/images/eflow_error.gif);
	background-repeat: no-repeat;
	background-position: right top;
}
</style>
<body>
<div class="out">
<div class="in">
<table width="100%" height="100%" border="0" cellpadding="3"
	cellspacing="0" class="table2">
	<tr>
		<td colspan='2'>&nbsp;&nbsp;<br>
		&nbsp;</td>
	</tr>
	<tr>
		<td align="right" width="30%">&nbsp;&nbsp;</td>
		<td><font color='red' face='Arial' size='2'><%=message%></font></td>
	</tr>
	<tr>
		<td align='center' colspan=2>
		<%
			if ("back".equals(returnType)) {
		%> <input type="button" name="subBtn" value="  <i18n:message key="button.back"/>  "
			onclick="window.history.go(-1)" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"> <%
 	} else if ("close".equals(returnType)) {
 %> <!-- input type="button" name="subBtn" value="  Close  "
			onclick="top.window.close();" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'" -->
			&nbsp; <%
 	}
 %>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>