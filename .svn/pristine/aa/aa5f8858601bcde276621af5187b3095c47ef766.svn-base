<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.ParamConfigVO, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<html>
<head>
	<title>Param Config</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function addForm(){
	     var url = "<%=request.getContextPath()%>/paramConfigAction.it?method=editParamConfig&saveType=new";
	     window.location = url;
	  }
	  
	  function saveForm(){
	  	var frm = document.forms[0];
	  	if(formValidate(frm)==false) return;
	    frm.action = "<%=request.getContextPath()%>/paramConfigAction.it?method=saveParamConfig&saveType=update";
	    frm.submit();
	  }
	</script>
</head>

<% 
  Collection list = (ArrayList)request.getAttribute("resultList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_parameter.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_parameter.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_DATA_DICTIONARY%>' operateId='<%=ModuleOperateName.OPER_DATA_DICTIONARY_ADD%>' isButton='true' labelValue='Add'>
         <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>
        &nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_DATA_DICTIONARY%>' operateId='<%=ModuleOperateName.OPER_DATA_DICTIONARY_UPDATE%>' isButton='true' labelValue='Save'>
         <input type="button" name="saveBtn" value='<i18n:message key="button.save"/>' onclick="saveForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable>
          <tr class="liebiao_tou">
            <!--<td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'holidayId')"></td>-->
            <td align='center' width='30%'><i18n:message key="housekeeping_parameter.name"/></td>
            <td align='center' width='30%'><i18n:message key="housekeeping_parameter.value"/></td>
            <td align='center' width='40%'><i18n:message key="housekeeping_parameter.des"/></td>
          </tr>
           
          <%
           if(list!=null){
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	ParamConfigVO vo = (ParamConfigVO)listIt.next();
          %>
            <tr class="liebiao_nr2">
            	<input type="hidden" name="configId" value="<%=vo.getConfigId()%>">
            	<td>
            		<%=vo.getParamName()%>&nbsp;&nbsp;
            	</td>
            	<td>
            		<input type='text' name='paramValue' size='60' length='60' required="false" value="<%=(vo.getParamValue()==null?"":vo.getParamValue())%>">
            	</td>
            	<td>
            		<textarea class='input' name='description'  type='text' onkeydown="textCounterForInput(this,190,event)" style="width:250;overflow-x:visible;overflow-y:visible;"><%=(vo.getDescription()==null?"":vo.getDescription())%></textarea>
            	</td>
            </tr>
          <%}
          }%>
        </table>
      </td>
     </tr>
   </table>
</form>
</body>
</html>