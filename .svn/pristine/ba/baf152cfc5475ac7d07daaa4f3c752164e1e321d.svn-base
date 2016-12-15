<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.ParamConfigVO, com.aiait.eflow.common.helper.*" %>
<html>
<head>
 <title>Add New Param</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      frm.action = "<%=request.getContextPath()%>/paramConfigAction.it?method=saveParamConfig";
      frm.submit();
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  ParamConfigVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new ParamConfigVO();
  }else{
	  vo = (ParamConfigVO)request.getAttribute("vo");
  }
%>

<form name="paramConfigForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <input type="hidden" name="configId" value="<%=vo.getConfigId()%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_parameter.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_parameter.code"/> : 
       </td>
       <td>
        <input type='text' name='paramCode' size='30' maxLength='30' title="Param Code" required="true" value="<%=(vo.getParamCode()==null?"":vo.getParamCode())%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_parameter.name"/> : 
       </td>
       <td>
        <input type='text' name='paramName' size='50' length='50' title="Param Name" required="true" value="<%=(vo.getParamName()==null?"":vo.getParamName())%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_parameter.value"/> : 
       </td>
       <td>
        <input type='text' name='paramValue' size='100' length='100' title="Param Value" required="true" value="<%=(vo.getParamValue()==null?"":vo.getParamValue())%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_parameter.des"/> : 
       </td>
       <td>
        <input type='text' name='description' size='100' length='100' title="Description"  value="<%=(vo.getDescription()==null?"":vo.getDescription())%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_parameter.orderid"/> : 
       </td>
       <td>
        <input type='text' name='orderId' size='4' length='4' title="Order Id" value="<%=(vo.getOrderId()==0?0:vo.getOrderId())%>">
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>