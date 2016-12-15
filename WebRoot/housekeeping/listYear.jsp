<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*" %>
<html>
<head>
	<title>Manage Holiday</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function addForm(){
	     var url = "<%=request.getContextPath()%>/holidayAction.it?method=editHoliday&saveType=new";
	     window.location = url;
	  }
	  
	  function deleteForm(){
	    if(checkSelect('holidayId')<=0){
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if(confirm("Are you sure to delete the selected records")){
	      document.forms[0].action = "<%=request.getContextPath()%>/holidayAction.it?method=deleteHoliday";
	      document.forms[0].submit();
	    }
	  }
	</script>
</head>
<% 
  Collection list = (ArrayList)request.getAttribute("yearList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_companyholiday.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr>
       <td align='center' class="tr1"><B><i18n:message key="housekeeping_companyholiday.title"/></B></td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable>
          <tr class="liebiao_tou">
            <td align='center'><i18n:message key="housekeeping_companyholiday.columnyear"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.columndays"/></td>
          </tr>
          <%
           if(list!=null){
        	int i=1;   
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	HashMap map = (HashMap)listIt.next();
          %>
            <tr class="tr_change">
              <td><a href='<%=request.getContextPath()%>/holidayAction.it?method=listHoliday&setYear=<%=(String)map.get("SET_YEAR")%>'><%=(String)map.get("SET_YEAR")%></a>&nbsp;&nbsp;</td>
              <td><%=(String)map.get("HOLIDAYS")%>&nbsp;&nbsp;</td>
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
	 