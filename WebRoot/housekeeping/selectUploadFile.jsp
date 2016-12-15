<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%
	String action = (String) request.getAttribute("action");
    if (action == null) {
        action = request.getParameter("action");
    }
	action = action == null ? "" : action;

	String title = (String) request.getAttribute("title");
	if (title == null) {
	    title = request.getParameter("title");
    }
	title = title == null ? "" : title;

	String dialog = request.getParameter("dialog");
	dialog = dialog == null ? "" : dialog;
	String target = dialog.toLowerCase().equals("modal") ? "" : "_blank";

	String status = (String) request.getAttribute("status");
	status = status == null ? "" : status.toUpperCase();

	String message = (String) request.getAttribute("message");
    message = message == null ? "" : message;

    String value = (String) request.getAttribute("value");
    value = value == null ? "" : value;
%>
<html>
<title><i18n:message key="button.selectFile" /></title>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script type="text/javascript">
    jQuery(function() {
        // For submit form within modal dialog.
        jQuery(document.createElement("base")).attr("target", "_self").prependTo(jQuery("head"));
    });
</script>
<script language="javascript">
    var action = "<%=action%>";
    var title = "<%=title%>";
    var dialog = "<%=dialog%>";
    
    var status = "<%=status%>";
    var message = "<%=message%>";
    var value = "<%=value%>";
    
    function onLoading() {
        if (status == "") {
            return;
        } else if (status == "OK") {
            if (message != "") {
                if (message == "[OK]") {
                    message = "Operation completed successfully";
                }
                alert(message);
            }
            window.returnValue = value;
            window.close();
            return;
        } else {
            if (message != "") {
                alert(message);
            }
            return;
        }
    };
    window.onload = onLoading;
    
    function submitForm() {
        if (action == "") {
            alert("No action defined!");
            window.close();
            return;
        }
        
        var path = document.all['path'].value;
        if (path.Trim() == "") {
            alert("Please select upload file!");
            document.all['path'].focus();
            return;
        }

        var myForm = document.getElementById("myForm");
        myForm.action = "<%=request.getContextPath()%>/" + action 
            + "&action=" + encodeURIComponent(action) + "&title=" + encodeURIComponent(title) + "&dialog=" + dialog;
        myForm.submit();
    };
</script>
<body style="margin: 0px">
<form id="myForm" enctype="multipart/form-data" method="post" action="" target="<%=target%>">
<table width='100%' border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse: collapse;'>
    <tr height="25">
        <td align="center" colspan="2"><b><%=title%></b></td>
    </tr>
    <tr>
        <td align='right' class="tr3"><i18n:message key="button.selectFile" /> :</td>
        <td align='left'><input type="file" name="path" size="38" /></td>
    </tr>
    <tr>
        <td colspan='2' align='center'><input type="button" name="submitBtn"
            value='<i18n:message key="button.upload"/>' onclick="submitForm()" class="btn3_mouseout"
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        <input type="button" name="closeBtn" value='<i18n:message key="button.cancel"/>'
            onclick="javascript:window.close()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</body>
</html>