<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemFieldVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO,com.aiait.eflow.util.StringUtil"%>
<%@page import="com.aiait.eflow.wkf.vo.*,java.text.*"%>
<%
    //
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String voucherNo = dateFormat.format(new Date());
%>
<html>
<head>
<title><i18n:message key="button.batch_reject" /></title>
</head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<style type="text/css">
.multipleSelectBoxControl SPAN {
    FONT-WEIGHT: bold;
    FONT-SIZE: 11px;
    FONT-FAMILY: arial
}

.multipleSelectBoxControl DIV SELECT {
    FONT-FAMILY: arial;
    HEIGHT: 100%
}

.multipleSelectBoxControl INPUT {
    WIDTH: 25px
}

.multipleSelectBoxControl DIV {
    FLOAT: left
}

.multipleSelectBoxDiv {
    
}

FIELDSET {
    MARGIN: 10px;
    WIDTH: 500px
}
</style>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript">	
	function confirm() {
		var comments = "";
        comments = document.getElementById('comments').value.Trim();
        if (comments == "") {
            alert("Please fill in Remark!");
            document.getElementById('comments').focus();
            return;
        }
        
		window.returnValue = "comments=" + encodeURIComponent(encodeURIComponent(comments));
	    window.close();
	}
	
</script>
<body>
<form id="Form1" method="post">
<input type="hidden" name="textareaLimitLength">
<table WIDTH="100%" bordercolor="#6595D6" style="border-collapse: collapse;" BORDER="1" CELLPADDING="3" CELLSPACING="0"
    class="tr0">
    <tr>
        <td width="20%" class="tr3">
        <div align="right"><span class="style1"><i18n:message key="flow_auditpro.remark" /></span></div>
        </td>
        <td><font size="2">
            <textarea cols="50" rows="10" id="comments" name="comments" onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea> 
        </font></td>
    </tr>
    <tr>
        <td align="center" colspan="2"><input id="btnOK" type="button" value='<i18n:message key="button.confirm"/>'
            onclick="javascript:confirm()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; <input name="btnClose" type="button"
            onclick="javascript:window.close();" value='<i18n:message key="button.cancel"/>' class="btn3_mouseout"
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
</table>
</form>
</body>
</html>