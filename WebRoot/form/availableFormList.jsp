<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.FormTypeVO" %>
<%@page import="com.aiait.framework.db.*,com.aiait.eflow.common.*,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<html>
<head>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>

<script type="text/javascript">
window.onload=function(){enableTooltips()};
</script>

</head>
<%
  Collection formTypeList = (ArrayList)FormTypeHelper.getInstance().getFormTypeList();
%>

<body class="noimage">
 <TABLE WIDTH=100% bordercolor="#767676" style="border-collapse:collapse;"  BORDER=0 CELLPADDING=0 CELLSPACING=0 class="tr0" >
   <tr class="tr1">
      <td align='center' style="border: 0px solid #F1F1F1; background: #767676 50% 50% repeat-x; color: #ffffff; font-weight: bold; height:35px;font-size:14px;"> <b><i18n:message key="menu.workspace.new_form"/></b> </td>
   </tr>
 <% 
   if(formTypeList!=null && formTypeList.size()>0){
	 Iterator typeIt = formTypeList.iterator();
	 StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	 while(typeIt.hasNext()){			
		FormTypeVO typeVo = (FormTypeVO)typeIt.next();
		
		if(CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA)
				&&!AuthorityHelper.getInstance().checkAuthorityByFormType(staff.getCurrentRoleId(),ModuleOperateName.MODULE_FORM_INVERTORY,typeVo.getFormTypeId())){
			continue;
		}
		Collection formList = (ArrayList)request.getAttribute(typeVo.getFormTypeName());
		if(formList!=null && formList.size()>0){
 %>
<!--   <tr>-->
<!--     <td bgcolor='#EEEEE6'><b><%=typeVo.getFormTypeName()%></b></td>-->
<!--   </tr>-->
   <%
     if(formList==null || formList.size()==0){
       out.println("<tr><td>&nbsp;&nbsp;</td></tr> ");
       continue;
     }
     Iterator it = formList.iterator();
     int count = 1;
   %>
  <tr>
    <td>
       <table width="100%"  border="1" bordercolor = '#BCBCBC' cellpadding="0" cellspacing="0" class="newform">
   <%
     while(it.hasNext()){
    	 FormManageVO form = (FormManageVO)it.next();
    	 if(count%3==1){
     		out.println("<tr>");
     	 }
   %>
     <!-- Add outer hyperlink(optional) -->
     <td align='left' width="33%">

		<table border=0 cellspacing=0 cellpadding=0>
			<tr>
			 	<td rowspan=4 style='width:90px;height:100%'>
			 	<img style='margin-left:10px;' src='<%=request.getContextPath()%>/images/<%=form.getFormSystemImg()%>'>
			 	</td>
				<td valign=middle style='width:264pt;height:20pt;color:#000000;font-size:14px;font-family:Arial, sans-serif,Lucida Grande, Lucida Sans,黑体'>
					<a href='javascript:openFormWithLayer("<%=request.getContextPath()%>/formManageAction.it?method=displayFormFill&formSystemId=<%=form.getFormSystemId()%>")'><%=form.getFormName()%></a>
				</td>
			</tr>
		
			<tr>
				<td  valign=top style='width:264pt;height:15pt;color:#5A5A5A;font-size:11px;'>
				<%=typeVo.getFormTypeName()%>
				</td>
			</tr>
		
			<tr>
		  		<td valign='top' style='width:264pt;height:20pt;color:#5A5A5A;font-size:11px;'>
		  		<%=form.getDescription()%>
		  		</td>
		 	</tr>
		 	<tr >
		  		<td valign='top' style='width:264pt;height:13px;text-align:right'>
		  			<span id="spanListHeart<%=form.getFormSystemId()%>" class="spanListHeartBlank"></span>
		  		</td>
		 	</tr>
		</table>

<!--   <% if(form.getDescription()!=null&& form.getDescription().indexOf("http")>=0){ %>-->
<!--     <a href='<%=form.getDescription().substring(form.getDescription().indexOf("http"))%>' target="_blank" title='<%=form.getDescription()%>'><%=form.getFormId()%></a>&nbsp;[<%=form.getFormName()%>]   -->
<!--   <%} else {%>-->
<!--     <a href='javascript:openFormWithLayer("<%=request.getContextPath()%>/formManageAction.it?method=displayFormFill&formSystemId=<%=form.getFormSystemId()%>")' title='<%=form.getDescription()%>'><%=form.getFormId()%></a>&nbsp;[<%=form.getFormName()%>]-->
<!--   <%} %>  -->
     </td>

   <% 
	  if(count%3==0){
    		out.println("</tr>");
      }  
      count++;
     }
	   if(count%3==0){
   		out.println("</tr>");
      }  
   %>
      </table>
      </td>
   </tr>
 <%}
 }
}
 %>
</table>
    <!-- 
    <div id="loading2" style="display:none;align:center">
	    <div class="loading-indicator">
		    It is loading page...
	    </div>
    </div>
    -->
</body>
</html>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/NovaJS/listAvailable_jsp_common.js"></script>
