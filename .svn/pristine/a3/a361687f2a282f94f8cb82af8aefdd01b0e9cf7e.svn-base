<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*"%>
<%@page import="com.aiait.framework.util.*"%>
<%@taglib uri="/WEB-INF/purview.tld" prefix="purview"%>
<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="/common/loading.jsp"%>
<%@taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%
    List<ExchangeRateVO2> list = (List<ExchangeRateVO2>) request.getAttribute("rateList");
    List<CurrencyVO> currencyList = (List<CurrencyVO>) request.getAttribute("currencyList");
    String ctxPath = request.getContextPath();
%>
<html>
<head>
<title>Exchange Rate</title>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<link href="<%=ctxPath%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=ctxPath%>/js/jquery.min.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/common.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/sorttable.js"></script>
<script language="javascript" src="<%=ctxPath%>/js/resizeCol.js"></script>
<script language="javascript">
function addRate() {
    var url = "<%=ctxPath%>/exchangeRateAction2.it?method=editRate&mode=new";
    window.location = url;
};

function deleteRate() {
    if (checkSelect('id') <= 0) {
        alert("Please select the record(s) to delete!");
        return;
    }
    if (confirm("Are you sure to delete the selected record(s)?")) {
        document.forms[0].action = "<%=ctxPath%>/exchangeRateAction2.it?method=deleteRate";
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
    </tr>
    <!--<tr>
        <td><strong><font color='#5980BB'><i18n:message key="housekeeping_exrate.location" /></font></strong></td>
    </tr>
--></table>
<table width="100%" border="0">
    <tr class="tr1">
        <td align='center'><b><i18n:message key="housekeeping_exrate.title" /></b></td>
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
                <td>Year / Month</td>
                <%
                for (CurrencyVO currency : currencyList) {
                %>
                <td><%=currency.getName()%></td>
                <%
                }
                %>
            </tr>
            <%
            String KEY_YEAR = "KEY_YEAR";
            String KEY_MONTH = "KEY_MONTH";
            int year = 0;
            int month = 0;
            Map map = null;
            List<Map> _list = new ArrayList<Map>();
            for (ExchangeRateVO2 rate : list) {
                if (year != rate.getEffyear() || month != rate.getEffmonth()) {
                    year = rate.getEffyear();
                    month = rate.getEffmonth();
                    map = new HashMap();
                    map.put(KEY_YEAR, Integer.valueOf(year));
                    map.put(KEY_MONTH, Integer.valueOf(month));
                    _list.add(map);
                }
                map.put(rate.getCurrCode(), rate);
            }
            for (Map _map : _list) {
                year = (Integer) _map.get(KEY_YEAR);
                month = (Integer) _map.get(KEY_MONTH);
                String id = year + "_" + month;
                String yearMonth = year + " / " + (month < 10 ? "0" : "") + month;
            %>
            <tr class="tr_change" align='center'>
                <td><input type="checkbox" name="id" value="<%=id%>"></td>
                <td><a href="<%=ctxPath%>/exchangeRateAction2.it?method=editRate&mode=edit&id=<%=id%>"><%=yearMonth%></a></td>
                <%
                for (CurrencyVO currency : currencyList) {
                    ExchangeRateVO2 rate = (ExchangeRateVO2) _map.get(currency.getCode());
                %>
                <td><%=rate == null ? "" : rate.getRate2()%></td>
                <%
                }
                %>
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
