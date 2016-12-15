<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.*" %>
<%@page import="java.util.*" %>
<html>
<head>
 <title>Edit Hotel Rate</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
	function submitRate() {
		var frm = document.forms[0];
		if (formValidate(frm) == false) return;
		frm.action = "<%=request.getContextPath()%>/hotelRateAction.it?method=saveRate";
		frm.submit();
	}
 </script>
<body>
<% 
  String editType = request.getParameter("editType");
  HotelRateVO hotelRate = (HotelRateVO) request.getAttribute("hotelRate");
  if (hotelRate == null) {
      hotelRate = new HotelRateVO();
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="editType" value="<%=editType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_hotelrate.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_hotelrate.city"/> : 
       </td>
       <td>
         <input type='text' name='city' size='20' maxLength='20' required="true" value="<%=hotelRate.getCity() == null ? "" : hotelRate.getCity()%>"
          <%=hotelRate.getCity() == null ? "" : "readonly"%>>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_hotelrate.titlegroup"/> : 
       </td>
       <td>
         <select name="titleGroup">
           <%
           //HashMap titleGroupMap = PerdiemRateVO.getTitleGroupMap();
           //Iterator it = titleGroupMap.keySet().iterator();
           List titleList = (ArrayList)BaseDataHelper.getInstance().getDetailMap().get("22");
           Iterator it = titleList.iterator();
           while (it.hasNext()) {
        	   DictionaryDataVO titleGroupMap = (DictionaryDataVO) it.next();
               String titleGroupId = titleGroupMap.getId();
               String titleGroupName = titleGroupMap.getValue();
           %>
           <option value="<%=titleGroupId%>" <%=titleGroupId.equals(hotelRate.getTitleGroup()) ? "selected" : ""%>><%=titleGroupName%></option>
           <%
           }
           %>
         </select>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_hotelrate.rate"/> : 
       </td>
       <td>
         <input type='text' name='rate' size='5' maxLength='5' required="true" value="<%=hotelRate.getRate() == 0 ? "" : Integer.toString(hotelRate.getRate())%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitRate()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>