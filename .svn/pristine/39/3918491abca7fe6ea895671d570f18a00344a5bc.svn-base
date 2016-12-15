<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="java.util.*"%>
<%
    //
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Service Test</title>
<style type="text/css">
.out {
    position: absolute;
    background: #bbb;
    margin: 10px auto;
    width: 500px;
    height: 100px;
    top: 15%;
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

</style>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">

    function authenticate() {
        var serviceId = document.getElementById("serviceId");
        if (serviceId.value == "") {
        	alert("Please input the Service Id.");
        	serviceId.focus();
            return false;
        }
        var password = document.getElementById("password");
        if (password.value == "") {
        	alert("Please input the Password.");
        	password.focus();
            return false;
        }
        return true;
    }

    function submitForm() {
        if (!authenticate()) {
            return;
        }
        var frm = document.forms[0];
        if (!formValidate(frm)) return;
        var param = $.Form("testForm").serialize();
        param += "&" + document.getElementById("initdata").value;
      	alert(param);
        param = encodeURI(param);
        var url = "<%=request.getContextPath()%>/formService.it?method=newForm&" + param;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
                alert(x.responseText);
                if (x.responseText.indexOf("Error") < 0) {
                	var serviceId = document.getElementById("serviceId").value;
                	var password = document.getElementById("password").value;
                    window.location = "<%=request.getContextPath()%>/formManageAction.it?method=editForm&requestNo=" + x.responseText
                            + "&serviceId=" + serviceId + "&password=" + password;
                }
            },
            onError: function(x) {
                alert("Failed to create form - " + x.responseText);
            }
            }
        );
    }

    function deleteForm() {
        if (!authenticate()) {
            return;
        }
    	var requestNo = document.getElementById("requestNo").value;
        if (requestNo == "") {
        	alert("Please input the Request No.");
        	document.getElementById("requestNo").focus();
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password").value;
        var url = "<%=request.getContextPath()%>/formService.it?method=deleteForm&requestNo=" + requestNo
                + "&serviceId=" + serviceId + "&password=" + password;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
            	alert(x.responseText);
            	if (x.responseText == "OK") {
                    window.location = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listPersonalApplyForm";
                }
            },
            onError: function(x) {
                alert("Failed to delete form - " + x.responseText);
            }
            }
        );
    }

    function queryField() {
        if (!authenticate()) {
            return;
        }
    	var requestNo = document.getElementById("requestNo").value;
        if (requestNo == "") {
        	alert("Please input the Request No.");
        	document.getElementById("requestNo").focus();
            return;
        }
        var fieldId = document.getElementById("fieldId").value;
        if (fieldId == "") {
        	alert("Please input the Field Id");
        	document.getElementById("fieldId").focus();
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password").value;
        var url = "<%=request.getContextPath()%>/formService.it?method=queryFormField&requestNo=" + requestNo
                + "&fieldId=" + fieldId
                + "&serviceId=" + serviceId + "&password=" + password;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
            	alert(x.responseText);
            },
            onError: function(x) {
                alert("Failed to query form field - " + x.responseText);
            }
            }
        );
    }

    function queryForm() {
        if (!authenticate()) {
            return;
        }
    	var requestNo = document.getElementById("requestNo").value;
        if (requestNo == "") {
        	alert("Please input the Request No.");
        	document.getElementById("requestNo").focus();
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password").value;
        var url = "<%=request.getContextPath()%>/formService.it?method=queryForm&requestNo=" + requestNo
                + "&serviceId=" + serviceId + "&password=" + password;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
            	alert(x.responseText);
            },
            onError: function(x) {
                alert("Failed to query form - " + x.responseText);
            }
            }
        );
    }

    function queryFlow() {
        if (!authenticate()) {
            return;
        }
    	var requestNo = document.getElementById("requestNo").value;
        if (requestNo == "") {
        	alert("Please input the Request No.");
        	document.getElementById("requestNo").focus();
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password").value;
        var url = "<%=request.getContextPath()%>/formService.it?method=queryFlow&requestNo=" + requestNo
                + "&serviceId=" + serviceId + "&password=" + password;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
            	alert(x.responseText);
            },
            onError: function(x) {
                alert("Failed to query flow - " + x.responseText);
            }
            }
        );
    }

    function updatePassword() {
        if (!authenticate()) {
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password").value;
        var url = "<%=request.getContextPath()%>/securityService.it?method=updatePassword"
                + "&serviceId=" + serviceId + "&password=" + password;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
                alert(x.responseText);
                if (x.responseText.indexOf("Error") < 0) {
                	confirmPassword(x.responseText);
                }
            },
            onError: function(x) {
                alert("Failed to update password - " + x.responseText);
            }
            }
        );
    }

    function confirmPassword(newPassword) {
        if (!authenticate()) {
            return;
        }
        var serviceId = document.getElementById("serviceId").value;
    	var password = document.getElementById("password");
        var url = "<%=request.getContextPath()%>/securityService.it?method=confirmPassword"
                + "&serviceId=" + serviceId + "&password=" + password.value + "&newPassword=" + newPassword;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
                alert(x.responseText);
                if (x.responseText.indexOf("Error") < 0) {
                	password.value = newPassword;
                }
            },
            onError: function(x) {
                alert("Failed to confirm password - " + x.responseText);
            }
            }
        );
    }

    function getPassword() {
        var serviceId = "test";
        var url = "<%=request.getContextPath()%>/securityService.it?method=getPassword"
                + "&serviceId=" + serviceId;
        param = "";

        var myAjax = new Ajax.Request(
            url,
            {
            method: "get",
            parameters: param,
            setRequestHeader: {"If-Modified-Since":"0"},
            onComplete: function(x) {
                if (x.responseText.indexOf("Error") < 0) {
                    document.getElementById("serviceId").value = serviceId;
                	document.getElementById("password").value = x.responseText;
                } else {
                    alert(x.responseText);
                }
            },
            onError: function(x) {
                alert("Failed to update password - " + x.responseText);
            }
            }
        );
    }

    window.onload = getPassword;
</script>
</head>
<body>
<div class="out">
<div class="in">
<form name="testForm" action="" method="post">
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr class="tr1">
        <td align='center' colspan='2'><b>Service Test</b></td>
    </tr>
    <tr>
        <td align="right" width="27%">Form Id :</td>
        <td><input type='text' name='formId' size='20' title="Form Id" required="true" value=""> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right">Requester :</td>
        <td><input type='text' name='requester' size='20' title="Requester" required="true" value=""> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right">Submit By :</td>
        <td><input type='text' name='submitby' size='20' title="Submit By" required="true" value=""> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right">Is NT ID :</td>
        <td><select name="isntid" title='Is NT ID'>
            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            <option value="yes">Yes</option>
        </select></td>
    </tr>
    <tr>
        <td align="right">Callback URL :</td>
        <td><input type='text' name='callback' title="Callback URL" size="50" value="http://localhost/eflow_dev/formService.it?method=queryForm"></td>
    </tr>
    <tr>
        <td align="right">Initial Data :</td>
        <td><textarea id='initdata' name='initdata' title="Initial Data" rows="5" cols="60"></textarea></td>
    </tr>
    <tr>
        <td align="right" width="27%">Service Id :</td>
        <td><input type='text' id='serviceId' name='serviceId' size='30' title="Service Id" value=""> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right" width="27%">Password :</td>
        <td><input type='text' id='password' name='password' size='30' title="Password" value=""> (<font color='red'>*</font>)</td>
    </tr>
    <tr>
        <td align="right" width="27%">Request No. :</td>
        <td><input type='text' id='requestNo' name='requestNo' size='30' title="Request No." value=""></td>
    </tr>
    <tr>
        <td align="right" width="27%">Field Id :</td>
        <td><input type='text' id='fieldId' name='fieldId' size='30' title="Field Id" value=""></td>
    </tr>
    <tr>
        <td align="center" colspan="2">
            <input type="button" value='newForm'
            onclick="submitForm()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <input type="button" value='deleteForm'
            onclick="deleteForm()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <input type="button" value='queryField'
            onclick="queryField()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <input type="button" value='queryForm'
            onclick="queryForm()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <input type="button" value='queryFlow'
            onclick="queryFlow()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;&nbsp;
            <br>
            <input type="button" value='updatePassword'
            onclick="updatePassword()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </td>
    </tr>
</table>
</form>
</div>
</div>
</body>
</html>
