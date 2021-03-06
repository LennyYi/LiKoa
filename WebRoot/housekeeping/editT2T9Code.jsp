<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="java.util.*" %>
<html>
<head>
 <title>Edit Finance Code</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
	function submitCode() {
		var frm = document.forms[0];
		if (formValidate(frm) == false) return;
		frm.action = "<%=request.getContextPath()%>/financeCodeAction.it?method=saveCode&flg=<%=(String)request.getAttribute("flg")%>";
		frm.submit();
	}
 </script>
<body>
<% 
  String orgId = (String) request.getParameter("orgId");
  orgId = orgId == null ? "" : orgId;
  String editType = request.getParameter("editType");
  FinanceCodeVO vo = (FinanceCodeVO) request.getAttribute("t2t9");
  if (vo == null) {
	  vo = new FinanceCodeVO();
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="orgId" value="<%=orgId%>">
  <input type="hidden" name="editType" value="<%=editType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_financecode.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_financecode.code"/> : 
       </td>
       <td>
         <input type='text' name='code' size='20' 
          maxLength='<%
          	if("T2".equals((String)request.getAttribute("flg"))){
        		out.print("3"); 
         	}else if("T6".equals((String)request.getAttribute("flg"))){
        		out.print("20");
         	}else{
         		out.print("10");
         	}%>'
          required="true" value="<%=vo.getCode() == null ? "" : vo.getCode()%>" <%=vo.getCode() == null ? "" : "readonly"%>/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_financecode.name"/> : 
       </td>
       <td>
         <input type='text' name='name' size='30' maxLength='50' required="true" value="<%=vo.getName() == null ? "" : vo.getName()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <%if("SUNAC".equals((String)request.getAttribute("flg"))){%>
     <tr>
       <td colspan=2><b>T code Relationship</b></td>
     </tr>
     <tr>
       <td align="right">T0</td>
       <td>
         <input type='checkbox' name='t0' value="1" <%=vo.getT0() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T1</td>
       <td>
         <input type='checkbox' name='t1' value="1" <%=vo.getT1() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T2</td>
       <td>
         <input type='checkbox' name='t2' value="1" <%=vo.getT2() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T3</td>
       <td>
         <input type='checkbox' name='t3' value="1" <%=vo.getT3() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T4</td>
       <td>
         <input type='checkbox' name='t4' value="1" <%=vo.getT4() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T5</td>
       <td>
         <input type='checkbox' name='t5' value="1" <%=vo.getT5() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <tr>
       <td align="right">T6(DB CODE)</td>
       <td>
         <input type='checkbox' name='t6' value="1" <%=vo.getT6() == 1 ? "checked" : ""%>/>
       </td>
     </tr>
     <%} %>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
       </td>
     </tr>
  </table>
</form>
</body>
</html>