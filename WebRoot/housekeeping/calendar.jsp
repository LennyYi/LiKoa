<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.common.helper.CalendarHelper;" %>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<html>
<head>
<%
	String strYear=request.getParameter("strYear");
	String strMonth = request.getParameter("strMonth");
	
	//String strYear = (String)request.getAttribute("strYear");
	//String strMonth = (String)request.getAttribute("strMonth");
	
	
	String strNWDay="";
	String strSetDay, strSetDayType, dayArray;
	String strBfBtnDsb, strAfBtnDsb;
	strBfBtnDsb="";
	strAfBtnDsb="";
	
	
	if ((strYear==null||"".equals(strYear))&&(strMonth==null||"".equals(strMonth))){	
		Calendar cal = Calendar.getInstance();
		strYear = Integer.toString(cal.get(Calendar.YEAR));
		strMonth = Integer.toString(cal.get(Calendar.MONTH)+1);
	}
	
	strNWDay = CalendarHelper.getholiday(strYear, strMonth);
	
	
%>
<title>Manage Holiday</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>

<script language="javascript">
var solarMonth=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
var seldate = ",";

//initial year and month
function init(){
	var tempDate = new Date();
	if( nWSDateForm.strYear.value == ""){
		nWSDateForm.strYear.value = tempDate.getYear();
		nWSDateForm.strMonth[tempDate.getMonth()].selected = true;
	}else{
		nWSDateForm.strMonth[nWSDateForm.vstrMonth.value-1].selected = true;
	}
	
	//set the state of year buttons
	nWSDateForm.strBfBtnDsb.value = "";
	nWSDateForm.strAfBtnDsb.value = "";
	
}
//submit spDateForm after year add one
function beforeyear(){
	nWSDateForm.strYear.value--;
	if(nWSDateForm.strYear.value <= 2000){
		nWSDateForm.strBfBtnDsb.value = "disabled";
		nWSDateForm.strAfBtnDsb.value = "";
	}
	
	nWSDateForm.action="<%=request.getContextPath()%>/housekeeping/calendar.jsp";	
	nWSDateForm.submit();
}
//submit spDateForm after year less one
function afteryear(){
	if(nWSDateForm.strYear.value >= 2019){
		nWSDateForm.strBfBtnDsb.value = "";
		nWSDateForm.strAfBtnDsb.value = "disabled";
	}
	nWSDateForm.strYear.value++;
	
	nWSDateForm.action="<%=request.getContextPath()%>/housekeeping/calendar.jsp";
	nWSDateForm.submit();
}
//submit spDateForm after month change
function changeMonth(){
	if(nWSDateForm.strYear.value >= 2019){
		nWSDateForm.strBfBtnDsb.value = "";
		nWSDateForm.strAfBtnDsb.value = "disabled";
	}
	if(nWSDateForm.strYear.value <= 2000){
		nWSDateForm.strBfBtnDsb.value = "disabled";
		nWSDateForm.strAfBtnDsb.value = "";
	}
	nWSDateForm.action="<%=request.getContextPath()%>/housekeeping/calendar.jsp";
	nWSDateForm.submit();
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
function Btn_SetPH_clk(){
	if(seldate==","){
		alert("Please select date to handle ! ");
		return;
	}
	nWSDateForm.strSetDay.value = seldate;
	nWSDateForm.strSetDayType.value = "N";
	var varyear = document.all['strYear'].value;
	var varmonth = document.all['strMonth'].value;
	var url="<%=request.getContextPath()%>/holidayAction.it?method=setpublicHoliday&strYear="+varyear+"&strMonth="+varmonth
	         +"&seldate="+seldate;
	         
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
    if(result=="success"){
     	window.location = "<%=request.getContextPath()%>/holidayAction.it?method=listCalendar&strYear="+varyear+"&strMonth="+varmonth;
    }else{
     	alert(result);
    }
}
//set Legal Holiday date
function Btn_SetLH_clk(){
	if(seldate==","){
		alert("Please select date to handle ! ");
		return;
	}
	nWSDateForm.strSetDay.value = seldate;
	nWSDateForm.strSetDayType.value = "N";
	var varyear = document.all['strYear'].value;
	var varmonth = document.all['strMonth'].value;
	var url="<%=request.getContextPath()%>/holidayAction.it?method=setLegalHoliday&strYear="+varyear+"&strMonth="+varmonth
	         +"&seldate="+seldate;
	         
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
    if(result=="success"){
     	window.location = "<%=request.getContextPath()%>/holidayAction.it?method=listCalendar&strYear="+varyear+"&strMonth="+varmonth;
    }else{
     	alert(result);
    }
	
}
//set working date
function Btn_SetW_clk(){
	if(seldate==","){
		alert("Please select date to handle ! ");
		return;
	}
	nWSDateForm.strSetDay.value = seldate;
	nWSDateForm.strSetDayType.value = "W";
	//Ajax transmit
	//nWSDateForm.submit();
	
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////
//change color
function changeColor(obj,intdate){
	if(seldate.indexOf("," + intdate + ",") >= 0){
		//unselect
		seldate = seldate.replace("," + intdate + "," , ",")
		obj.className = "wd";
	}else{
		//select
		seldate += intdate + ",";
		obj.className = "selected";
	}
	//alert(seldate);
}
//write html
function formatTable(){
	var weekday,tempdate,intdate,monthlength;
	var strhtml = "";
	var strNWDay;
	//the string of nonworking day split with ','
	strNWDay = nWSDateForm.vstrNWDay.value;
	//the first date
	if(nWSDateForm.strYear.value==""){
		tempdate = new Date();
		tempdate.setDate(1);
	}else{
		tempdate = new Date(nWSDateForm.strYear.value, nWSDateForm.vstrMonth.value-1,1);
	}
	//day of month
	intdate = tempdate.getDate();
	//day of week of the first date
	weekday = tempdate.getDay();
	//count of all days of current month
	monthlength = solarDays(tempdate.getYear(),tempdate.getMonth());    //the count of current month
	for(var i=1; i<7; i++){
		strhtml += "<tr class='tbtext'>";
		for(var j=1;j<8; j++){
			if (j<weekday+1 && i==1){
				strhtml += "<td class='wd'>";
				//blanks of befor
				continue;
			}
			//here are date
			if (intdate > monthlength){
				strhtml += "<td class='wd'>";
			}else{
				strhtml += "<td class='wd'";
				if (nWSDateForm.strIsAdmin.value == "Y"){
					strhtml += " onclick='changeColor(this," + intdate + ")'style='cursor:hand'>";
				}else{
					strhtml += " >";
				}
				
				if(strNWDay.indexOf("," + intdate + ",") >= 0 ){
					strhtml += "<font class='nwd'>";
				}
				//with hidden var
				strhtml += "<input type='hidden' name='vdate' value='" + intdate + "'>";
				strhtml += intdate;
				intdate++;
			}			
		}	
		strhtml += "</tr>";
	}	
	document.writeln(strhtml);
	
}
//==============================//the count of current month
function solarDays(y,m) {
if(m==1)
return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28);
else
return(solarMonth[m]);
}
</script>
</head>

<body onload="init()">
<form name="nWSDateForm"   method="POST">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="10"></td>
	</tr>
	<!--<tr>
 		<td>
 		<strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_calendar.location"/></font></strong>
 		</td>
	</tr>-->
</table>
<!--<TABLE WIDTH="800" CELLSPACING="1" cellpadding="3" align="center">-->
<table border="0" width="100%" cellspacing="1">	
	<TR class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_calendar.location"/></B></td>
	</TR>
	<TR>
		
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><strong><i18n:message key="housekeeping_calendar.year"/>:</strong></span></div></TD>
		<td width="35%" height="20">
			<input type="text" style="width:100" class="readonlybox" name="strYear" readonly="true" value="<%=strYear%>">
			<input type="button"  value="<" id="Btn_Before" name="Btn_Before"   class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="beforeyear(this)"> 
			<input type="button"  value=">" id="Btn_After" name="Btn_After"   class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="afteryear()"> 
		</td>		
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><strong><i18n:message key="housekeeping_calendar.month"/>:</strong></span></div></TD>
		<td width="35%" height="20">
			<input type="hidden" name="strAfBtnDsb"  value="<%=strAfBtnDsb%>">
			<input type="hidden" name="strBfBtnDsb"  value="<%=strBfBtnDsb%>">
			<input type="hidden" name="strIsAdmin"  value="Y">
			<input type="hidden" name="vstrMonth"  value="<%=strMonth%>">
			<input type="hidden" name="vstrNWDay"	value="<%=strNWDay%>">
			<input type="hidden" name="strSetDay"	value="">
			<input type="hidden" name="strSetDayType"	value="">
			<select name="strMonth" style="width:100px" class="input" onchange="changeMonth()" >
				<option value="1"><i18n:message key="housekeeping_calendar.jan"/></option>
				<option value="2"><i18n:message key="housekeeping_calendar.feb"/></option>
				<option value="3"><i18n:message key="housekeeping_calendar.mar"/></option>
				<option value="4"><i18n:message key="housekeeping_calendar.apr"/></option>
				<option value="5"><i18n:message key="housekeeping_calendar.may"/></option>
				<option value="6"><i18n:message key="housekeeping_calendar.jun"/></option>
				<option value="7"><i18n:message key="housekeeping_calendar.jul"/></option>
				<option value="8"><i18n:message key="housekeeping_calendar.aug"/></option>
				<option value="9"><i18n:message key="housekeeping_calendar.sep"/></option>
				<option value="10"><i18n:message key="housekeeping_calendar.oct"/></option>
				<option value="11"><i18n:message key="housekeeping_calendar.nov"/></option>
				<option value="12"><i18n:message key="housekeeping_calendar.dec"/></option>
			</select>
		</td>
	</tr>
</TABLE>

<!--LE WIDTH="800" CELLSPACING="1" cellpadding="3" align="center">-->
<table border="0" width="100%" cellspacing="1" cellpadding="3">

	<tr class="liebiao_tou">
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.sun"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.mon"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.tue"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.wed"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.thu"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.fri"/></td>
		<td align='center' width="14%"><i18n:message key="housekeeping_calendar.sat"/></td>
	</tr>
<!--Write the table of date-->
<script type="text/javascript">
	formatTable();
</script>

	<tr>
		<td colspan="7" class="staticinput" align="center">
			<input type="button" class="btn3_mouseout" value='<i18n:message key="housekeeping_calendar.setpubh"/>' id="Btn_SetN" name="Btn_SetN" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="Btn_SetPH_clk()" >&nbsp;&nbsp;
			<input type="button" class="btn3_mouseout" value='<i18n:message key="housekeeping_calendar.setlegalh"/>' id="Btn_SetN" name="Btn_SetN" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="Btn_SetLH_clk()" >&nbsp;&nbsp; 
            <!-- input type="button" class="btn3_mouseout" value='<i18n:message key="housekeeping_calendar.querysd"/>' id="Btn_SetW" name="Btn_SetW" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="Btn_SetW_clk()" -->  

			<input type="button" class="btn3_mouseout" value='<i18n:message key="housekeeping_calendar.querysd"/>' class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                   onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="window.open('holiday_special.asp','_self')" id="Btn_QrySpecial" name="Btn_QrySpecial">
		</td>				
	</tr>
	<tr>
		<td colspan="7" class="staticinput"><i18n:message key="housekeeping_calendar.notes"/>: </td>
	<tr>
		<td colspan="7" class="staticinput">
			<ol>
            	<li><i18n:message key="housekeeping_calendar.note1"/>
            	<li><i18n:message key="housekeeping_calendar.note2"/>
            	<li><i18n:message key="housekeeping_calendar.note3"/>
            </ol>
		</td>
	</tr>
</table>
</form>
</body>
</html>
	 