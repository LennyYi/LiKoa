<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="../common/loading.jsp" %>
<%@page import="java.util.*,java.math.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.CommonName,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.housekeeping.vo.OTSummaryVO" %>
<%@page import="com.aiait.eflow.formmanage.vo.FormSectionFieldVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.ParamConfigHelper"%>
<%@page import="com.aiait.eflow.housekeeping.vo.CheckInoutVO" %>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<html>

<%
    String yearMonth = (String)request.getParameter("yearMonth");
    String printFlag = (String)request.getParameter("printFlag");
    String type = (String)request.getParameter("type");
    String status = (String)request.getParameter("status"); // status='01'-----未结束（正在申请中），可以修改；status='02'-----已结束（已经付款），不可以修改
    FormManageVO form = (FormManageVO)request.getAttribute("otForm");			
    Collection fieldList = (ArrayList)request.getAttribute("fieldList");
    Collection validApplyOTRecordList = (ArrayList)request.getAttribute("validApplyOTRecordList");    
    StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);    
    OTSummaryVO summaryVo = (OTSummaryVO)request.getAttribute("summaryVo");
    String canUpdate = (String)request.getAttribute("canUpate"); //是否在可以更新的日期内，false---不可以更新；true----可以更新
    
    //added by young
   
    String staffNameCn = "";
    
    if("new".equals(type)){
    	staffNameCn = (String)request.getAttribute("staffNameCn");
    }else{   
    	staffNameCn = summaryVo.getStaffNameCn();
    }   
    
    
    String kind= (String)request.getAttribute("kind");
    String staff_code,staff_name,staff_team,staff_teamcode ;
    if (kind==null || "".equals(kind)){
    	staff_code = staff.getStaffCode();
    	staff_name = staff.getStaffName();
    	staff_team = staff.getTeamName();
    	staff_teamcode = staff.getTeamCode();
    	kind = "";
    }else{
    	staff_code = summaryVo.getStaffCode();
    	staff_name = summaryVo.getStaffNameEn();
    	staff_team = StaffTeamHelper.getInstance().getTeamNameByCode(""+summaryVo.getTeamCode());
    	staff_teamcode = ""+summaryVo.getTeamCode();
    }
    
    //add by robin for showing column 'check in' and 'check out'
    HashMap checkMap = (HashMap)request.getAttribute("resultMap");

    
%>
<head>
<title>Personal Summary For Meal Allowance</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
  
  function addForm(){
    if(confirm("Are you sure to add additional meal allowance records")){
      var url = "<%=request.getContextPath()%>/otFormAction.it?method=listAddtionalOTRecord&yearMonth=<%=yearMonth%>";
      openCenterWindow(url,800,600);
    }
  }

  function saveForm(){
     if(document.all['staffNameCn'].value.Trim()==""){
        alert(updateSummaryChineseName)
        document.all.staffNameCn.focus();
        return;
     }

     if (document.all['actualNum'].value==0){
     	alert(has_no_record);
     	return;
     }
    if(!confirm(confirm_save)){
        return;
     }
     var param = $.Form("detailForm").serialize({ regTime:new Date().toGMTString()});
     var url = "<%=request.getContextPath()%>/otFormAction.it?method=updateOTSummary&yearMonth=<%=yearMonth%>";
     param = encodeURI(encodeURI(param)); 
     var myAjax = new Ajax.Request(
         url,
        {
            method:"post",       //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
              if(x.responseText.Trim()=="success"){
                    window.location = "<%=request.getContextPath()%>/otFormAction.it?method=enterPersonalOTSummary";
              }else if(x.responseText.Trim()=="noconfirm"){
                alert(updateSumarryNoConfirm)
              }else{
                alert(x.responseText.Trim())
              }
            },
            onError:function(x){          //
                    alert(save_unsuccess);
            } 
        } 
     ); //end myAjax
  }
  
  function deleteForm(){
     if(!confirm("Are you sure to delete this summary")){
       return;
     }
     var url = "<%=request.getContextPath()%>/otFormAction.it?method=deleteOTSummary"+"&yearMonth=<%=yearMonth%>";
     var  xmlhttp = createXMLHttpRequest();
          var result;
          if(xmlhttp){
            
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="success"){
                   alert("Successfully delete this month summary!")
                   window.document.location = "<%=request.getContextPath()%>/otFormAction.it?method=enterPersonalOTSummary";
                 }else{
                   alert(result.Trim());
                 }
              }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
        }
  }
  
  function printForm(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showPersonalOTSummary&printFlag=true&yearMonth=<%=yearMonth%>&type="
                +document.all['type'].value +"&status=<%=status%>";
    openCenterWindow(url,800,600);
  }
</script>
<style media=print> 
.Noprint{display:none;} 
</style> 
</head>
<body onload="<%="".equals(kind)?"":"resize(1500)"%>">
<form name="detailForm">
<input type="hidden" name="type" value="<%=type%>">
 <%if(printFlag!=null && !"".equals(printFlag)){%>
  <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0>
  </OBJECT> 
 <%}%>
<%
    ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
    BigDecimal feeStandard = new BigDecimal(75); //default is 75 (RMB)
    try {
        feeStandard = new BigDecimal(paramHelper.getParamValue(CommonName.PARAM_OT_FEE_STANDARD_BY_TIMES));
    } catch (Exception e) {
        //
    }
%>
 <table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <%
	    String companyTitle = paramHelper.getParamValue("company_name");
	    if(companyTitle==null || "".equals(companyTitle)){
	    	companyTitle = CommonName.AIAIT_COMPANY_TITLE;
	    }
        %>
        <tr>
           <td colspan='2' align='center'>&nbsp;<!-- img src="<%=request.getContextPath()%>/images/aiait_logo.jpg"--></td>
        </tr>
        <tr>
          <td colspan='2' align='center'> <b> <font size='2'><%=companyTitle%></font></b> </td>
        </tr>

     <tr>
       <td colspan='2' align='center'> <b><font size='2'> <%=form.getFormName()%> </font></b></td>
     </tr>
 </table>
  <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
       <td>
              英文名  English Name: <b><u><%=staff_name==null?"":staff_name%></u></b>
       <input type="hidden" name="staffNameEn" value="<%=staff_name==null?"":staff_name%>">
       </td>
       <td>中文名　Chinese Name: 
       <%if((printFlag!=null && "true".equals(printFlag)) || "02".equals(status)){%>
         <b><u><%=(staffNameCn!=null?staffNameCn:"")%></u></b>
       <%}else{%>
         <input type="text" value="<%=(staffNameCn!=null?staffNameCn:"")%>" name="staffNameCn" size='15'>(<font color='red'>*</font>)
       <%}%>
       </td>
       <td>
           编号  Staff Code: <b><u><%=staff_code==null?"":staff_code%></u></b>
         <input type="hidden" name="staffCode" value="<%=staff_code==null?"":staff_code%>">
       </td>
       <td>
           部门  Team: <b><u><%=staff_team%><u></b>
         <input type="hidden" name="teamCode" value="<%=staff_teamcode%>">
       </td>
       <td>年月　Year/Month: <b><u><%=yearMonth%></u></b></td>
 </table>
   <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
 	  <td height="10"></td>
    </tr>
    </table>
 <table id='formTable02' width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
   <tr class="liebiao_tou">
   <%
      if(fieldList!=null && fieldList.size()>0){
    	  Iterator fieldIt = fieldList.iterator();
    	  
    	  while(fieldIt.hasNext()){
    		  FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
    		  //if("ID".equals(field.getFieldId().toUpperCase()) || "REQUEST_NO".equals(field.getFieldId().toUpperCase())){
    		  if("ID".equals(field.getFieldId().toUpperCase())) {
    			//out.println(" <td align='center'><input type=\"checkbox\" name=\"allBtn\" onclick=\"selectAll(this,'id')\"></td>");
    			continue;
    		  }
   %>
      <td align='center' ><%=field.getFieldLabel()%></td>
    <% }
     }
     if(checkMap!=null){
    	out.println(" <td align='center' >Check in</td>");
    	out.println(" <td align='center' >Check out</td>");
     }
    %>
   </tr>
   <%
     BigDecimal weekNum = BigDecimal.ZERO;
     BigDecimal publicNum = BigDecimal.ZERO;
     BigDecimal nightNum = BigDecimal.ZERO;
     BigDecimal dayNum = BigDecimal.ZERO;
     BigDecimal mealNum = BigDecimal.ZERO;
     BigDecimal taxiFee = BigDecimal.ZERO;
     BigDecimal totalTaxAmount = BigDecimal.ZERO;
	 String requestNo = "";
	 String otFormSystemId = paramHelper.getParamValue(CommonName.PARAM_OT_FORM_SYSTEM_ID_CODE);
     //double 
     if(validApplyOTRecordList!=null && validApplyOTRecordList.size()>0){
    	 java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    	 Iterator otIt = validApplyOTRecordList.iterator();
    	 while(otIt.hasNext()){
    		 out.println("<tr class=\"tr_change\">");
    		 HashMap map = (HashMap)otIt.next();
    		 out.println("<input type='hidden' name='id' value='"+map.get("ID")+"'>");
    		 Iterator fieldIt = fieldList.iterator();
    		 String otDate = "";
             
    		 BigDecimal _weekNum = BigDecimal.ZERO;
    	     BigDecimal _publicNum = BigDecimal.ZERO;
    	     BigDecimal _mealNum = BigDecimal.ZERO;
    		 while(fieldIt.hasNext()){
    			 FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
    			 //request_no
    			 if("REQUEST_NO".equals(field.getFieldId().toUpperCase())){
    				 out.println("<input type='hidden' name='requestNo' value='" + map.get("REQUEST_NO")+"'>");
    				 // IT1321
    	    		 requestNo = (String)map.get("REQUEST_NO");
    			       String url = "<a href=\"javascript:openCenterWindow('"
                           + request.getContextPath()
                           + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&pop=true&requestNo="
                           + requestNo + "&formSystemId=" + otFormSystemId + "',800,600)\">" + requestNo + "</a>";
   				     out.println("<td>" + url + "</td>");
   				  	 continue;
    			 }
                 String fieldId = field.getFieldId().toUpperCase();
    			 if ("WEEKY_NUM".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
    			     _weekNum = new BigDecimal((String)map.get(fieldId));
                     weekNum = weekNum.add(_weekNum);
    			 } else if ("PUB_HOLIDAY_NUM".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
    			     _publicNum = new BigDecimal((String)map.get(fieldId));
                     publicNum = publicNum.add(_publicNum);
    			 } else if ("OT_MID_NIGHT_NUM".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
    			     nightNum = nightNum.add(new BigDecimal((String)map.get(fieldId)));
                 } else if ("OT_DAY_TIME_NUM".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
                     dayNum = dayNum.add(new BigDecimal((String)map.get(fieldId)));
                 } else if ("OT_MEAL_ALLOWANCE_NUM".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
                     _mealNum = new BigDecimal((String)map.get(fieldId));
                 } else if ("TAXI_FEE_AMOUNT".equals(fieldId) && map.get(fieldId) != null && !"".equals((String)map.get(fieldId))) {
    			     taxiFee = taxiFee.add(new BigDecimal((String)map.get(fieldId)));
    			 }
    			 
    			 if(!"ID".equals(fieldId)){
    			   if("IS_EXCEPTIONAL_CASE".equals(fieldId) && "01".equals(map.get("IS_EXCEPTIONAL_CASE"))){
    				   out.println("<td>Normal Exceptional Case</td>");
    			   }else if("IS_EXCEPTIONAL_CASE".equals(fieldId) && "02".equals(map.get("IS_EXCEPTIONAL_CASE"))){
    				   out.println("<td>Not Exceptional Case</td>");
    			   }else if("IS_EXCEPTIONAL_CASE".equals(fieldId) && "03".equals(map.get("IS_EXCEPTIONAL_CASE"))){
    				   out.println("<td>Mid-night Support Case</td>");
    			   }else if("IS_EXCEPTIONAL_CASE".equals(fieldId) && "04".equals(map.get("IS_EXCEPTIONAL_CASE"))){
    				   out.println("<td>Day Time Support Case</td>");
    			   }else if("OT_PLAN_DATE".equals(fieldId) && map.get("OT_PLAN_DATE")!=null){
    	               java.util.Date cDate = df.parse((String) map.get("OT_PLAN_DATE"));  
    	               otDate = com.aiait.eflow.util.StringUtil.getDateStr(cDate,"MM/dd/yyyy");
    	    	       out.println("<td>"+ otDate + "</td>");
    			   } else {
   %>
       <td><%=map.get(fieldId)==null?"":map.get(fieldId)%></td>
   <%
    			   }
    			 }
    		 }
             
             // Check for old forms without meal allowance column
             mealNum = mealNum.add(_mealNum.equals(BigDecimal.ZERO) ? _weekNum.add(_publicNum) : _mealNum);

    		 if(checkMap!=null){
    		   CheckInoutVO checkVo = (CheckInoutVO)checkMap.get(otDate);
    		   if(checkVo!=null){
    			   out.print("<td>"+checkVo.getArrivalTime()+"</td>");
    			   out.print("<td>"+checkVo.getLeaveTime()+"</td>");
    		   }else{
    			   out.print("<td></td>");
    			   out.print("<td></td>");
    		   }
    		 }
    		 out.print("</tr>");
    	 }
   }
   %>
   </table>
   <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
 	  <td height="10"></td>
    </tr>
   </table>
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
    <tr>
 	  <td height="10">误餐总金额（不含的士费）(Total Amount, without Taxi fee)</td>
 	  <td height="10">实际误餐总次数 (Total Number of Meal Allowance)</td>
 	  <td height="10">的士费总数(Total Taxi Fee.)</td>
    </tr>
    <%
      totalTaxAmount = feeStandard.multiply(mealNum);
    %>
    <tr>
      <td height="10"><b><%=totalTaxAmount%></b><input type="hidden" name="totalAmount" value="<%=totalTaxAmount%>"></td>
 	  <td height="10"><b><%=mealNum%></b><input type="hidden" name="actualNum" value="<%=mealNum%>">
 	  <input type="hidden" name="weekyNum" value="<%=weekNum%>">
 	  <input type="hidden" name="publicNum" value="<%=publicNum%>">
      <input type="hidden" name="nightNum" value="<%=nightNum%>">
      <input type="hidden" name="dayNum" value="<%=dayNum%>">
      <input type="hidden" name="mealNum" value="<%=mealNum%>">
 	  </td>
 	  <td height="10"><b><%=(taxiFee)%></b><input type="hidden" name="taxiFee" value="<%=taxiFee%>"></td>
    </tr>
    </table>
   <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
      <td height="10"></td>
      </tr>
      <tr>
 	  <td height="10" align="center"><b>* Remark: No need to print out and submit summary if No Taxi Fee reimbursement request.</b></td>
      </tr>
      <tr>
 	  <td height="10"></td>
      </tr>
    <tr>
    <td align='center' colspan='5'>
    <%if(printFlag!=null && "true".equals(printFlag)){
        if(kind==null || "".equals(kind)){%>
    <center class="Noprint" > 
 <input type="button" name="setupBtn" value="Page Setup" onclick='document.all.WebBrowser.ExecWB(8,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value="Print Preview" onclick='document.all.WebBrowser.ExecWB(7,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value='<i18n:message key="button.print"/>' onclick='document.all.WebBrowser.ExecWB(6,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
  <%} %>
 <input type="button" name="closeBtn" value="<i18n:message key="button.close"/>" onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">

   </center>
    <%}else{%>
    <%if("01".equals(status) && "true".equals(canUpdate)){%>
     <input type="button" name="saveBtn" value='Save' onclick='javascript:saveForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
<!-- 
     <input type="button" name="addBtn" value="Add other records" onclick='javascript:addForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 -->
    <%}%>
     <input type="button" name="printBtn" value='<i18n:message key="button.print"/>' <%=((summaryVo!=null)?"":"disabled")%>  onclick='javascript:printForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <input type="button" name="backBtn" value='<i18n:message key="button.back"/>' onclick='javascript:history.go(-1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
<%}%>
    </td>
    </tr>
    </table>
    </form>
 </body>
 </html>
