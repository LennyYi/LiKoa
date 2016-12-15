<%@ include file="/common/head.jsp"%>
<%@ include file="/common/loading.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@ page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.*"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview"%>

<html>
<head>
<title></title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
function back() {
    history.back();
};
  
</script>
</head>
<%
    Collection list = (Collection) request.getAttribute("monthlyList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td height="10"></td>
    </tr>
</table>
<table width="100%" border="0">
    <tr class="tr1">
        <td align='center'><B><i18n:message key="housekeeping_leave.monthly_records" /></B></td>
    </tr>
    <tr>
        <td align='left'><input type="button" name="backBtn" value='<i18n:message key="button.back"/>'
            onclick="back()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'"
            onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'"
            onmouseup="this.className='btn3_mouseup'"></td>
    </tr>
    <tr>
        <td>
        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="sortable" id="mytable"
            style="border: 1px #8899cc solid;">
            <tr class="liebiao_tou">
                <td align='center'><i18n:message key="housekeeping_leave.year_month" /></td>
                <td align='center'><i18n:message key="housekeeping_leave.total" /></td>
            </tr>
            <%
                //
            			if (list != null) {
            				int i = 1;
            				Iterator it = list.iterator();
            				while (it.hasNext()) {
            					LeaveMonthlyVO vo = (LeaveMonthlyVO) it.next();
                                String month = vo.getMonth();
                                month = month.length() == 1 ? "0" + month : month;
            %>
            <tr class="tr_change">
                <td align='center'><a target="_blank"
                    href="<%=request.getContextPath()%>/leaveAIASSAction.it?method=monthlyRecords&year=<%=vo.getYear()%>&month=<%=vo.getMonth()%>"><%=vo.getYear()%>-<%=month%></a>
                &nbsp;</td>
                <td align='center'><%=vo.getTotal()%>&nbsp;</td>
            </tr>
            <%
                i++;
            				}
            			}
            %>
        </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>
<script language="javascript">
    setResizeAble(mytable);
</script>