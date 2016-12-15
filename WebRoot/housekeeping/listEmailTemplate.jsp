<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.i18n.*"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<html>
<head>
<title>Manage Email Template</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
  function addForm(){
     var url = "<%=request.getContextPath()%>/emailTemplateAction.it?method=enternEditPage";
     window.location = url;
  }
  
  function deleteForm(){
    if(checkSelect('id')<=0){
      alert("You have not selected any records to delete!");
      return;
    }
    if(confirm("Are you sure to delete the selected records")){
      document.forms[0].action = "<%=request.getContextPath()%>/emailTemplateAction.it?method=deleteTemplate";
      document.forms[0].submit();
    }
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
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_emailtemplate.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_emailtemplate.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
       <purview:purview moduleId='<%=ModuleOperateName.MODULE_EMAIL_TEMPLATE%>' operateId='<%=ModuleOperateName.OPER_EMAIL_TEMPLATE_ADD%>' isButton='true' labelValue='Add'>
         <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </purview:purview> 
        &nbsp;
      <purview:purview moduleId='<%=ModuleOperateName.MODULE_EMAIL_TEMPLATE%>' operateId='<%=ModuleOperateName.OPER_EMAIL_TEMPLATE_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </purview:purview>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'id')"></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.id"/></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.name"/></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.subject"/></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.content"/></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.appliedaction"/></td>
            <td align='center' ><i18n:message key="housekeeping_emailtemplate.des"/></td>
            <td align='center' ><i18n:message key="common.form_type"/></td>
          </tr>
          <%
           if(list!=null){
        	int i=1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	EmailTemplateVO vo = (EmailTemplateVO)listIt.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="id" value="<%=vo.getId()%>"></td>
              <td >
              <purview:purview moduleId='<%=ModuleOperateName.MODULE_EMAIL_TEMPLATE%>' operateId='<%=ModuleOperateName.OPER_EMAIL_TEMPLATE_UPDATE%>' isButton='false' labelValue='<%=(""+vo.getId())%>'>
               <a href='<%=request.getContextPath()%>/emailTemplateAction.it?method=enternEditPage&id=<%=vo.getId()%>'><%=vo.getId()%></a>
              </purview:purview>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getName()%>&nbsp;</td>
              <td ><%=vo.getEmailSubject()%>&nbsp;</td>
              <td ><%=vo.getEmailContent()%>&nbsp;</td>
              <td ><%=vo.convertActionToName()%>&nbsp;</td>              
              <td ><%=vo.getDescription()%>&nbsp;</td>
              <td ><%
              out.write("&nbsp;<a href='" + request.getContextPath() + "/emailTemplateAction.it?method=addByFormType&id=" + vo.getId() + "'>New</a>&nbsp;<br><br>");
              List formtypes = vo.getFormtypes();
              for (int j = 0; j < formtypes.size(); j++) {
                  FormTypeVO formType = (FormTypeVO) formtypes.get(j);
                  out.write("<a href='" + request.getContextPath() + "/emailTemplateAction.it?method=editByFormType&id=" + vo.getId() + "&formType=" + formType.getFormTypeId() + "'>");
                  out.write(formType.getFormTypeName() + "</a><br><br>");
              }
              %></td>
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