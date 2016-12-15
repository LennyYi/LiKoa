<%@include file="/common/head.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.*"%>
<%@page import="java.util.*"%>
<%
    String mode = request.getParameter("mode");
    boolean isNew = "new".equalsIgnoreCase(mode);
    String id = request.getParameter("id");
    ExchangeRateVO2 rate = new ExchangeRateVO2();
    if (id != null) {
        rate.setId(id);
    }
    List<ExchangeRateVO2> list = (List<ExchangeRateVO2>) request.getAttribute("rateList");
    List<CurrencyVO> currencyList = (List<CurrencyVO>) request.getAttribute("currencyList");
    String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Edit Exchange Rate</title>
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>

<script language="javascript">
function submitRate() {
    var frm = document.forms[0];
    if (!formValidate(frm)) return;
    frm.action = "<%=ctxPath%>/exchangeRateAction2.it?method=saveRate";
    frm.submit();
};
</script>
</head>

<body>
<form name="form" action="" method="post">
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr class="tr1">
        <td align='center' colspan='2'><i18n:message key="housekeeping_exrate.title" /></td>
    </tr>
    <tr>
        <td class='tr3' align="right" width="45%">Year (<font color="red">*</font>)</td>
        <%
            if (isNew) {
                Calendar cal = Calendar.getInstance();
                int year0 = cal.get(Calendar.YEAR);
                int year1 = year0 - 1;
                int year2 = year0 - 2;
        %>
        <td><select name="year">
            <option value="<%=year0%>"><%=year0%></option>
            <option value="<%=year1%>"><%=year1%></option>
            <option value="<%=year2%>"><%=year2%></option>
        </select></td>
        <%
            } else {
        %>
        <td><input type="hidden" name="id" value="<%=id%>"><%=rate.getEffyear()%></td>
        <%
            }
        %>
    </tr>
    <tr>
        <td class='tr3' align="right">Month (<font color="red">*</font>)</td>
        <%
            if (isNew) {
        %>
        <td><select name="month">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            <option value="11">11</option>
            <option value="12">12</option>
        </select></td>
        <%
            } else {
        %>
        <td><%=rate.getEffmonth()%></td>
        <%
            }
        %>
    </tr>
    <%
    Map<String, ExchangeRateVO2> map = new HashMap<String, ExchangeRateVO2>();
    for (ExchangeRateVO2 _rate : list) {
        map.put(_rate.getCurrCode(), _rate);
    }
    for (CurrencyVO currency : currencyList) {
        String required = currency.isRequired() ? " required='true'" : "";
        String _required = currency.isRequired() ? " (<font color='red'>*</font>)" : "";
        rate = map.get(currency.getCode());
        String amount = rate == null ? "" : rate.getRate().toPlainString();
    %>
    <tr>
        <td class='tr3' align="right"><%=currency.getName()%><%=_required%></td>
        <td><input type='text' name='<%=currency.getCode()%>' style='text-align: right;' size='8' maxLength='10'
            isNumber='true' <%=required%> value='<%=amount%>'
            onKeyPress='if (event.keyCode!=46 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'
            title="<%=currency.getName()%> Exchange Rate"></td>
    </tr>
    <%
    }
    %>
    <tr>
        <td align="center" colspan="2"><input type="button" name="smBtn"
            value='<i18n:message key="button.submit"/>' onclick="submitRate()" class=btn3_mouseout
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