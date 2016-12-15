<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*"%>
<%@page import="com.aiait.framework.util.*"%>
<%@taglib uri="/WEB-INF/purview.tld" prefix="purview"%>
<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="/common/loading.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%
    List<HotelRateVO2> list = (List<HotelRateVO2>) request.getAttribute("rateList");
	String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Hotel Rate</title>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/sorttable.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/resizeCol.js"></script>
<script language="javascript">
function addRate() {
    var url = "<%=ctxPath%>/hotelRateAction2.it?method=editRate&mode=new";
    window.location = url;
};

function deleteRate() {
    if (checkSelect('id') <= 0) {
        alert("Please select the record(s) to delete!");
        return;
    }
    if (confirm("Are you sure to delete the selected record(s)?")) {
        document.forms[0].action = "<%=ctxPath%>/hotelRateAction2.it?method=deleteRate";
        document.forms[0].submit();
    }
};
</script>
</head>

<body>
<form name="form" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td height="10"></td>
    </tr><!--
    <tr>
        <td><strong><font color='#5980BB'><i18n:message key="housekeeping_hotelrate.location" /></font></strong></td>
    </tr>
--></table>
<table width="100%" border="0">
    <tr class="tr1">
        <td align='center'><b><i18n:message key="housekeeping_hotelrate.title" /></b></td>
    </tr>
    <tr>
        <td align='left'><input type="button" name="addBtn" value='<i18n:message key="button.add"/>'
            onclick="addRate()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'"> &nbsp; <input type="button" name="deleteBtn"
            value='<i18n:message key="button.delete"/>' onclick="deleteRate()" class=btn3_mouseout
            onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
            onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
    <tr>
        <td>
        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="mytable"
            style="border: 1px #8899cc solid;">
            <tr class="liebiao_tou" align='center'>
                <td width="30px"><input type="checkbox" name="allBtn" onclick="selectAll(this,'id')"></td>
                <td>Effective Date</td>
                <td>Region/City</td>
                <td><%=TitleVO.getGradeName()%></td>
                <td>Hotel Rate</td>
                <td>Currency</td>
            </tr>
            <%
            for (HotelRateVO2 rate : list) {
                String id = rate.getId();
            %>
            <tr class="tr_change" align='center'>
                <td><input type="checkbox" name="id" value="<%=id%>"></td>
                <td><%=rate.getEffDate2()%></td>
                <td><a href="<%=ctxPath%>/hotelRateAction2.it?method=editRate&mode=edit&id=<%=id%>"><%=rate.getCityName()%></a></td>
                <td><%=rate.getGrade()%></td>
                <td><%=rate.getRate2()%></td>
                <td><%=rate.getCurrName()%></td>
            </tr>
            <%
            }
            %>
        </table>
        </td>
    </tr>
</table>
</form>
</body>
<script language="javascript">
    setResizeAble(mytable);
</script>
</html>
