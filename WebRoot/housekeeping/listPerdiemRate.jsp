<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
	<title>Perdiem Rate</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function addRate() {
	     var url = "<%=request.getContextPath()%>/perdiemRateAction.it?method=editRate&editType=new";
	     window.location = url;
	  }
	  
	  function deleteRate() {
	    if (checkSelect('rateId') <= 0) {
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if (confirm("Are you sure to delete the selected records")) {
	      document.forms[0].action = "<%=request.getContextPath()%>/perdiemRateAction.it?method=deleteRate";
	      document.forms[0].submit();
	    }
	  }
	</script>
</head>

<% 
  Collection list = (Collection) request.getAttribute("rateList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_perdiemrate.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_perdiemrate.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
		<input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addRate()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		&nbsp;
		<input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteRate()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'rateId')"></td>
            <td align='center' ><i18n:message key="housekeeping_perdiemrate.region"/></td>
            <td align='center' ><i18n:message key="housekeeping_perdiemrate.titlegroup"/></td>
            <td align='center' ><i18n:message key="housekeeping_perdiemrate.rate"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
				    PerdiemRateVO vo = (PerdiemRateVO) it.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="rateId" value="<%=vo.getRegion() + "_" + vo.getTitleGroup()%>"></td>
              <td><%=vo.getRegion()%>&nbsp;</td>
              <td>
                <a href='<%=request.getContextPath()%>/perdiemRateAction.it?method=editRate&editType=edit&rateId=<%=CommonUtil.encoderURL(vo.getRegion() + "_" + vo.getTitleGroup())%>'>
                <%=BaseDataHelper.getInstance().getLabelValue("22","",vo.getTitleGroup())%></a>
              </td>
              <td><%=vo.getRate()%>&nbsp;</td>
            </tr>
          <%i++;}
          }%>
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