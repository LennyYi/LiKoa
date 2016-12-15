<%@include file="/common/head.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.*"%>
<%@page import="java.util.*"%>
<%
    String mode = request.getParameter("mode");
	boolean isNew = "new".equalsIgnoreCase(mode);
	HotelRateVO2 rate = (HotelRateVO2) request.getAttribute("rate");
    String amount = rate == null ? "" : rate.getRate().toPlainString();
	List<CityVO> cityList = (List<CityVO>) request.getAttribute("cityList");
	String[] gradeList = (String[]) request.getAttribute("gradeList");
	String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Edit Hotel Rate</title>
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/calendar.js"></script>

<script language="javascript">
$(function() {
    var city = $("[name='city']");
    city.change(selectCity).change();
});

function selectCity(event) {
    var region = $(this).find("option:selected").attr("region");
    var currency = region == "A01" ? "RMB" : "HKD";
    $("[name='currency']").val(currency);
    $("#currname").html(currency);
};

function submitRate() {
    var frm = document.forms[0];
    if (!formValidate(frm)) return;
    frm.action = "<%=ctxPath%>/hotelRateAction2.it?method=saveRate";
    frm.submit();
};
</script>
</head>

<body>
<form name="form" action="" method="post">
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse: collapse;">
    <tr class="tr1">
        <td align='center' colspan='2'><i18n:message key="housekeeping_hotelrate.title" /></td>
    </tr>
    <tr>
        <td class='tr3' align="right" width="45%">Effective Date (<font color="red">*</font>)</td>
        <%
            if (isNew) {
        %>
        <td><input type='text' name='effdate' size='10' maxLength='10' isDate='true' required='true' value=''
            onclick='setday(this)' style="cursor: pointer;" title="Effective Date"> (MM/DD/YYYY)</td>
        <%
            } else {
        %>
        <td><input type="hidden" name="id" value="<%=rate.getId()%>"><%=rate.getEffDate2()%></td>
        <%
            }
        %>
    </tr>
    <tr>
        <td class='tr3' align="right">Region/City (<font color="red">*</font>)</td>
        <%
            if (isNew) {
        %>
        <td><select name="city">
            <%
                for (CityVO city : cityList) {
            %>
            <option value="<%=city.getCode()%>" region="<%=city.getRegionCode()%>"><%=city.getName()%></option>
            <%
                }
            %>
        </select></td>
        <%
            } else {
        %>
        <td><%=rate.getCityName()%></td>
        <%
            }
        %>
    </tr>
    <tr>
        <td class='tr3' align="right"><%=TitleVO.getGradeName()%> (<font color="red">*</font>)</td>
        <%
            if (isNew) {
        %>
        <td><select name="grade">
            <%
                for (String grade : gradeList) {
            %>
            <option value="<%=grade%>"><%=grade%></option>
            <%
                }
            %>
        </select></td>
        <%
            } else {
        %>
        <td><%=rate.getGrade()%></td>
        <%
            }
        %>
    </tr>
    <tr>
        <td class='tr3' align='right'>Hotel Rate (<font color="red">*</font>)</td>
        <td><input type='text' name='rate' style='text-align: right;' size='5' maxLength='8' isNumber='true'
            required='true' value='<%=amount%>'
            onKeyPress='if (event.keyCode!=46 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false'
            title="Hotel Rate"></td>
    </tr>
    <tr>
        <td class='tr3' align="right">Currency</td>
        <%
            if (isNew) {
        %>
        <td><input type="hidden" name="currency" value=""><span id="currname"></span></td>
        <%
            } else {
        %>
        <td><%=rate.getCurrName()%></td>
        <%
            }
        %>
    </tr>
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