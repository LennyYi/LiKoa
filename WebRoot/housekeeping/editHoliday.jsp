
<%@ include file="/common/head.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.HolidayVO, com.aiait.eflow.common.helper.*" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
 <title>Edit Holiday</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
 
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      
      document.getElementById('holidayFromDate').value=document.getElementById('holidayFromDate').value.Trim();
      document.getElementById('holidayToDate').value=document.getElementById('holidayToDate').value.Trim();
      if(new Date(document.getElementById('holidayFromDate').value) > new Date(document.getElementById('holidayToDate').value))
      {
      	alert("From Date must be earlier or equal to To Date!");
      	document.getElementById('holidayToDate').focus();
		return;
	  }
      
      var holidayYear = document.forms[0].holidayYear.value.Trim()
      frm.action = "<%=request.getContextPath()%>/holidayAction.it?method=saveHoliday&setYear="+holidayYear+"&submitType=0";
      frm.submit();
   }
   
   function Ajaxsubmit(){   		
   		var frm = document.forms[0];
      	if(formValidate(frm)==false) return;
      	      
      	document.getElementById('holidayFromDate').value=document.getElementById('holidayFromDate').value.Trim();
      	document.getElementById('holidayToDate').value=document.getElementById('holidayToDate').value.Trim();
      	if(new Date(document.getElementById('holidayFromDate').value) > new Date(document.getElementById('holidayToDate').value))
      	{
      		alert("From Date must be earlier or equal to To Date!");
      		document.getElementById('holidayToDate').focus();
			return;
	  	}      
      	var holidayYear = document.forms[0].holidayYear.value.Trim()
   		var url = "<%=request.getContextPath()%>/holidayAction.it?method=saveHoliday&setYear="+holidayYear
   					+"&holidayYear="+holidayYear
   					+"&holidayFromDate="+document.forms[0].holidayFromDate.value.Trim()
   					+"&holidayToDate="+document.forms[0].holidayToDate.value.Trim()
   					+"&holidayDescription="+document.forms[0].holidayDescription.value.Trim()
   					+"&holidayStatus="+document.forms[0].holidayStatus.value.Trim()
   					+"&submitType=1"
   					+"&holidaykind="+document.forms[0].holidaykind.value;   					
   		//alert(url);
   		var  xmlhttp = createXMLHttpRequest();
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
      	if(result=="saveSuccess"){
      		alert("Save Successfully!");
         	window.location = "<%=request.getContextPath()%>/holidayAction.it?method=editHoliday&saveType=new";
      	}else{
        	alert(result);
      	}
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  HolidayVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new HolidayVO();
  }else{
	  vo = (HolidayVO)request.getAttribute("vo");
  }
%>

<form name="holidayForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <input type="hidden" name="holidayId" value="<%=vo.getHolidayId()%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr>
       <td align='center' colspan='2' class=tr1>
          <b> <i18n:message key="housekeeping_companyholiday.editholiday"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_companyholiday.columnyear"/> : 
       </td>
       <td>
        <input type='text' name='holidayYear' size='4' maxLength='4' title="Holiday Year" required="true" value="<%=(vo.getHolidayYear()==null?"":vo.getHolidayYear())%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_companyholiday.from"/> : 
       </td>
       <td>
        <input type='text' name='holidayFromDate' size='40' length='40' title="Holiday From Date" required="true" value="<%=(vo.getHolidayFromDate()==null?"":vo.getHolidayFromDate())%>" onclick='setday(this)'>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_companyholiday.to"/> : 
       </td>
       <td>
        <input type='text' name='holidayToDate' size='40' length='40' title="Holiday To Date" required="true" value="<%=(vo.getHolidayToDate()==null?"":vo.getHolidayToDate())%>" onclick='setday(this)'>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_companyholiday.des"/> : 
       </td>
       <td>
        <input type='text' name='holidayDescription' size='100' length='100' title="Holiday Description" required="true" value="<%=(vo.getHolidayDescription()==null?"":vo.getHolidayDescription())%>">
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_companyholiday.type"/> : 
       </td>
       <td>
	       <select name="holidayStatus" title="Holiday Status">
			<%
       			String[] list = new String[6];
				list[0] = "";
       			list[1] = "From Date am";
       			list[2] = "From Date pm";
       			list[3] = "To Date am";
       			list[4] = "To Date pm";
       			list[5] = "Full Date";
       		
       			//System.out.println(vo.getHolidayStatus());
       			
       			if(vo.getHolidayStatus()== 0)
       			{
      				int j = 0;
       				for(int i = 0; i < 6; i++)
       				{
       					System.out.println("i = "+i);

       					if( i == 5)
       					{
       						out.print("<option value=" + j + " selected>" + list[i] + "</option>");
       					}
       					else if(i == 0)
       					{
       					}
       					else
       					{
       						out.print("<option value=" + j + ">" + list[i] + "</option>");
       					}
       					j++;
       				}
       			}
       			else
       			{
       				int j = 0;
       				for(int i = 0; i < 6; i++)
       				{
       					System.out.println("i = "+i);
       					System.out.println("status = "+vo.getHolidayStatus());
       					
       					if(j == vo.getHolidayStatus())
       					{
       						out.print("<option value=" + vo.getHolidayStatus() + " selected>" + list[vo.getHolidayStatus()] + "</option>");
       					}
       					else if(i == 0)
       					{
       					}
       					else
       					{
       						out.print("<option value=" + j + ">" + list[i] + "</option>");
       					}
       					j++;
       				}
       			}
       		%>
	       </select>
       </td>
     </tr>
     <tr>
     	<td align="right">
     		<i18n:message key="housekeeping_companyholiday.type"/>:
     	</td>
     	<td>
     		<select name="holidaykind">
     			<option value="1" <%=(vo.getHolidayType()!=null && "1".equals(vo.getHolidayType())?"selected":"")%>><i18n:message key="housekeeping_companyholiday.pubholiday"/></option>
     			<option value="2" <%=(vo.getHolidayType()!=null && "2".equals(vo.getHolidayType())?"selected":"")%>><i18n:message key="housekeeping_companyholiday.statutoryholiday"/></option>
     		</select>
     	</td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="smBtn" value='<i18n:message key="button.asynsubmit"/>' onclick="Ajaxsubmit()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>