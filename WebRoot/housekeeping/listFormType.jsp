<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.FormTypeVO, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
	<title>Manage DB Owner</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function addForm(){
	     var url = "<%=request.getContextPath()%>/formTypeAction.it?method=preEdit&saveType=new";
	     window.location = url;
	  }
	  
	  function deleteForm(){
	    if(checkSelect('dbId')<=0){
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if(confirm("Are you sure to delete the selected records")){
	      document.forms[0].action = "<%=request.getContextPath()%>/formTypeAction.it?method=deleteFormType";
	      document.forms[0].submit();
	    }
	  }
	</script>
</head>

<% 
  Collection list = (ArrayList)request.getAttribute("formTypeList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_formtype.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_formtype.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_TYPE%>' operateId='<%=ModuleOperateName.OPER_FORM_TYPE_ADD%>' isButton='true' labelValue='Add'>
         <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>
        &nbsp;
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'dbId')"></td>
            <td align='center' ><i18n:message key="housekeeping_formtype.formtypecode"/></td>
            <td align='center' ><i18n:message key="housekeeping_formtype.formtypename"/></td>
            <td align='center' ><i18n:message key="housekeeping_formtype.des"/></td>
          </tr>
          <%
           if(list!=null){
        	int i = 1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	FormTypeVO vo = (FormTypeVO)listIt.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="dbId" value="<%=vo.getFormTypeId()%>"></td>
              <td >
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_TYPE%>' operateId='<%=ModuleOperateName.OPER_FORM_TYPE_EDIT%>' isButton='false' labelValue='<%=vo.getFormTypeId()%>'>
               <a href='<%=request.getContextPath()%>/formTypeAction.it?method=preEdit&saveType=edit&typeCode=<%=vo.getFormTypeId()%>'><%=vo.getFormTypeId()%></a>
              </purview:purview>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getFormTypeName()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getDescription()%>&nbsp;&nbsp;</td>
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