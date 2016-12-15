<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.HolidayVO, com.aiait.eflow.common.helper.*" %>
<html>
<head>
	<title>Manage Holiday</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
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
	      document.forms[0].action = "<%=request.getContextPath()%>/holidayAction.it?method=deleteHoliday&setYear="+document.forms[0].curYear.value.Trim();
	      document.forms[0].submit();
	    }
	  }
	  
	  function batchForm(){
	    if(checkSelect('holidayId')<=0){
	      alert("You have not selected any records to batch!");
	      return;
	    }
	    if(checkSelect('holidayId')>1){
	      alert("You can only selected one record to batch!");
	      return;
	    }
	    document.forms[0].action = "<%=request.getContextPath()%>/holidayAction.it?method=batchHoliday";
	    document.forms[0].submit();
	  }
	  function importPMA(){
		if(confirm("Are you sure to sync off days from PMA?")){
			var url = "<%=request.getContextPath()%>/holidayAction.it?method=importPMA&setYear="+document.forms[0].curYear.value.Trim();
			var xmlhttp = createXMLHttpRequest();
	      	var result;
	      	if(xmlhttp){
	        	xmlhttp.open('POST',url,false);
	           	xmlhttp.onreadystatechange = function()
	           	{
	            	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	                	result = xmlhttp.responseText;
	             	}
	           	}
	           	xmlhttp.setRequestHeader("If-Modified-Since","0");
	           	xmlhttp.send(null);
	      	}
	      	if(isNaN(result)){
	      		alert(result);
	      	} else if(parseInt(result)>0){
	      		alert("Success, "+result+" days imported from PMA.");
	         	document.forms[0].action = "<%=request.getContextPath()%>/holidayAction.it?method=listHoliday&setYear="+document.forms[0].curYear.value.Trim();;
	    		document.forms[0].submit(); 
	      	}else{
	        	alert("There are no suitable record to import.");
	      	}
		}
	  }
	</script>
</head>

<% 
  	Collection list = (ArrayList)request.getAttribute("resultList");
	String setYear = (String)request.getParameter("setYear");
%>
<body>
<form name="myForm" method="post">
	<input type="hidden" name="curYear" value="<%=setYear%>">
   <table width="100%"  border="0">
     <tr>
       <td align='center' class="tr1"><B><i18n:message key="housekeeping_companyholiday.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
        <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <input type="button" name="batchBtn" value='<i18n:message key="button.batch"/>' onclick="batchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <input type="button" name="backBtn" value='<i18n:message key="button.back"/>' onclick="window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIAIT)) {%>
        <input type="button" name="importBtn" value='Sync with PMA' onclick="importPMA()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%} %>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable>
          <tr class="liebiao_tou">
            <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'holidayId')"></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.columnyear"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.from"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.to"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.des"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.type"/></td>
            <td align='center'><i18n:message key="housekeeping_companyholiday.kind"/></td>
          </tr>
           
          <%
           if(list!=null){
            Iterator listIt = list.iterator();
            int i = 0;
            while(listIt.hasNext()){
            	HolidayVO vo = (HolidayVO)listIt.next();
          %>
            <tr class="tr_change">
              <td align='center'><input type="checkbox" name="holidayId" value="<%=vo.getHolidayId()%>"></td>
              <td><a href='<%=request.getContextPath()%>/holidayAction.it?method=editHoliday&saveType=edit&holidayId=<%=vo.getHolidayId()%>'><%=vo.getHolidayYear()%></a>&nbsp;&nbsp;</td>
              <td><%=vo.getHolidayFromDate()%>&nbsp;&nbsp;</td>
              <td><%=vo.getHolidayToDate()%>&nbsp;&nbsp;</td>
              <td><%=vo.getHolidayDescription()%>&nbsp;&nbsp;</td>
              <td>
              	<%
              		if(vo.getHolidayStatus()==1){%>
              			From Date am
              		<%}
              		else if(vo.getHolidayStatus()==2){%>
              			From Date pm
              		<%}
              		else if(vo.getHolidayStatus()==3){%>
              			To Date am
              		<%}
              		else if(vo.getHolidayStatus()==4){%>
              			To Date pm
              		<%}
              		else if(vo.getHolidayStatus()==5){%>
              			Full Day
              		<%}
              	%>
              	&nbsp;&nbsp;
              </td>
              <td><% if("1".equals(vo.getHolidayType())) {%>
            	        Public Holiday
            	     <%}
                     else if ("2".equals(vo.getHolidayType())){%>
                        Statutory Holiday
                     <%}
                  %>
                  &nbsp;&nbsp;
              </td>
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