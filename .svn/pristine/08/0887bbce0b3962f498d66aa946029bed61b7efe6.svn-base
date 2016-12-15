<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>AIAIT E-Flow</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
body {
    background-color: #D4D0C8;
}

.out {
    position: absolute;
    background: #bbbbbb;
    margin: 10px auto;
    width: 410px;
    height: 100px;
    top: 20%;
    left: 32%;
    bottom: 30%;
    text-align: left;
}

.in {
    background: #ffffff;
    border: 1px solid #555555;
    padding: 10px 5px;
    position: relative;
    top: -5px;
    left: -5px;
    vertical-align: middle;
}

</style>
<script language="javascript">
  function login() {
      window.location = "<%=request.getContextPath()%>";
  }
</script>
</head>
<body>
<div class="out">
<div class="in">
<form name="myForm" method="post" action="<%=request.getContextPath()%>">
<table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0">
    <tr>
        <td>&nbsp;&nbsp;</td>
        <td align="center" style="color: red"><b><i18n:message key="common.logon_network_error" /></b></td>
        <td>&nbsp;&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;</td>
        <td align="center" colspan="2"><input type="button" name="loginBtn" value="  <i18n:message key="button.retry" />  "
            onclick="login()" class="btn3_mouseout"
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"></td>
        <td>&nbsp;&nbsp;</td>
    </tr>
</table>
</form>
</div>
</div>
</body>
</html>