<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@ page import="com.aiait.eflow.util.StringUtil"%>
<%@ page import="com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.ParamConfigHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%
    String logonId = request.getRemoteUser();
    if (logonId != null) {
        String[] tmp = StringUtil.split(logonId, "\\");
        logonId = tmp[tmp.length - 1];
    } else {
        logonId = "";
    }

    String message = (String) request.getAttribute("message");
    message = message == null ? "" : message;

    // For email login
    String menuItemId = (String) request.getParameter("menuItemId");
    menuItemId = menuItemId == null ? "" : menuItemId;

    String formSystemId = (String) request.getParameter("formSystemId");
    formSystemId = formSystemId == null ? "" : formSystemId;

    String operateType = (String) request.getParameter("operateType");
    operateType = operateType == null ? "" : operateType;

    String requestNo = (String) request.getParameter("requestNo");
    requestNo = requestNo == null ? "" : requestNo;

    String header = request.getHeader("user-agent");
    // System.out.println("header: " + header);
    boolean isIE = header.indexOf("MSIE") != -1;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><%=ParamConfigHelper.getInstance().getParamValue(CommonName.PLATFORM_NAME)%> Login</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<style type="text/css">
body {
    background-color: #D4D0C8;
}

.out {
    position: absolute;
    background: #BBBBBB;
    margin: 10px auto;
    width: 430px;
    height: <%=isIE ? "190px" : "240px"%>;
    top: 20%;
    left: 32%;
    text-align: left;
}

.in {
    background: #FFFFFF;
    border: 1px solid #555555;
    padding: 0px;
    position: relative;
    top: -5px;
    left: -5px;
    vertical-align: middle;
}
</style>

<script language="javascript">
function login() {
    var _loginId = document.all['logonId'];
    if (_loginId.value == "") {
        alert("Please input your windows' login id!");
        _loginId.focus();
        return false;
    }
    var _password = document.all['password'];
    if (_password.value == "") {
        alert("Please input your windows' login password!");
        _password.focus();
        return false;
    }
    if (navigator.appName.indexOf("Microsoft") == -1) {
        document.forms[0].action += "&nonIE=1";
    }
    return true;
};

if (top.location !== self.location) {
    top.location = self.location; 
}
</script>

</head>

<body>
<div class="out">
<div class="in">
<form name="myForm" method="post" action="<%=request.getContextPath()%>/logonAction.it?method=logOn&type=noneSSO"
    onsubmit="return login();">
    <input type="hidden" name="menuItemId" value="<%=menuItemId%>">
    <input type="hidden" name="operateType" value="<%=operateType%>">
    <input type="hidden" name="formSystemId" value="<%=formSystemId%>">
    <input type="hidden" name="requestNo" value="<%=requestNo%>">
    <input type="hidden" name="postData" value="yes">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="45" colspan="3" background="images/topbg.gif">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="3"></td>
                <%
                String platformName = ParamConfigHelper.getInstance().getParamValue(CommonName.PLATFORM_NAME).trim();
                if (platformName.equals("")) {
                %>
                <td width="86" height="50" align="center" valign="middle" class="eflow_title">E-Flow</td>
                <td width="3"></td>
                <td width="58" bgcolor="white" align="center" valign="middle"><img
                    src="images/<%=ParamConfigHelper.getInstance().getParamValue(CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
                <%
                } else if (platformName.length() <= 10) {
                %>
                <td width="86" height="50" align="center" valign="middle" class="eflow_title"><%=platformName%></td>
                <td width="3"></td>
                <td width="58" bgcolor="white" align="center" valign="middle"><img
                    src="images/<%=ParamConfigHelper.getInstance().getParamValue(CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
                <%
                } else {
                %>
                <td width="179" height="50" align="center" valign="middle" class="eflow_title"><%=platformName%></td>
                <td width="3"></td>
                <td width="58" bgcolor="white" align="center" valign="middle"><img
                    src="images/<%=ParamConfigHelper.getInstance().getParamValue(CommonName.COMPANY_LOGO_FILE)%>.gif"></td>
                <%
                }
                %>
                <td width="10"></td>
                <td height="45" align="left" valign="middle" class="eflow_subtitle"><%=ParamConfigHelper.getInstance().getParamValue(CommonName.SYSTEM_SUB_TITLE)%></td>
            </tr>
        </table>
        </td>
    </tr>
    <tr>
        <td height="30" align="center" valign="middle" colspan="2"></td>
        <td width="34%" rowspan="4" align="center" valign="middle"><img src="images/logon_key.gif"></td>
    </tr>
    <tr>
        <td width="35%" align="right" style="padding: 0px 5px;"><b>User Id:</b></td>
        <td align="left"><input type="text" name="logonId" value="<%=logonId%>" maxlength="20" size="20" title="Your windows' login id"></td>
    </tr>
    <tr>
        <td width="35%" align="right" style="padding: 0px 5px;"><b>Password:</b></td>
        <td align="left"><input type="password" name="password" maxlength="20" size="20" title="Your windows' login password"></td>
    </tr>
    <tr>
        <td align="center" colspan="3" style="color: red"><%=message%>&nbsp;</td>
    </tr>
    <tr>
        <td align="center" colspan="3"><input type="submit" name="subBtn" value="  Login  " class="btn3_mouseout"
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        <input type="reset" name="resetBtn" value="  Reset  " class="btn3_mouseout"
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</div>
</div>
</body>
</html>