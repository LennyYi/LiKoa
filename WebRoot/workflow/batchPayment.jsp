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
<title><i18n:message key="epayment.batch_payment" /></title>
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
	function submitForm() {
		var voucherNo = "";
        /*var voucherNo = document.getElementById('voucherNo').value.Trim();
        if (voucherNo == "") {
            alert('<i18n:message key="epayment.check_voucher_no" />');
            document.getElementById('voucherNo').focus();
            return;
        }*/

        var payDate = document.getElementById('payDate').value.Trim();
        if (payDate == "") {
            alert('<i18n:message key="epayment.check_pay_date" />');
            document.getElementById('payDate').focus();
            return;
        }
        if (isDate(document.forms[0].payDate, "<i18n:message key="epayment.pay_date" />") == false) {
            document.getElementById('payDate').focus();
            return;
        }
        
		window.returnValue = "voucherNo=" + encodeURIComponent(voucherNo) 
                + "&payDate=" + encodeURIComponent(payDate);
	    window.close();
	}
	
</script>
<body>
<form id="Form1" method="post">
<table WIDTH="100%" bordercolor="#6595D6" style="border-collapse: collapse;" BORDER="1" CELLPADDING="3" CELLSPACING="0"
    class="tr0">
    <!-- <tr>
        <td width="25%" height="20" class="tr3">
        <div align="right"><span class="style1"><i18n:message key="epayment.voucher_no" /></span></div>
        </td>
        <td height="20"><font size="2"> <input name="voucherNo" type="text" value=""
            class="text2" style="WIDTH: 200px" id="voucherNo"> </font></td>
    </tr> -->
    <tr>
        <td width="25%" height="20" class="tr3">
        <div align="right"><span class="style1"><i18n:message key="epayment.pay_date" /></span></div>
        </td>
        <td height="20"><font size="2"> <input name="payDate" type="text" value="<%=new SimpleDateFormat("MM/dd/yyyy").format(new Date())%>" class="text2"
            style="WIDTH: 130px" id="payDate" onclick="setday(this)"> (mm/dd/yyyy) </font></td>
    </tr>
    <tr>
        <td align="center" colspan="2"><input id="btnOK" type="button" value='<i18n:message key="button.confirm"/>'
            onclick="javascript:submitForm()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
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