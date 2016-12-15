<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@ include file="/common/loading.jsp" %>
<html>
<head>
<title>Closed Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
   var xmlhttp = createXMLHttpRequest();
  function getOptionList(formType){
    type = formType;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType;
    xmlhttp.open("GET", url, true);
    var objId = "formSystemId";
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
  }
   function searchForm(){
     if(document.forms[0].beginSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].beginSubmissionDate,"Submitted From")==false){
          return;
        }
      }
      if(document.forms[0].endSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].endSubmissionDate,"Submitted To")==false){
          return;
        }
      }
      if(document.forms[0].beginSubmissionDate.value.Trim()!="" && document.forms[0].endSubmissionDate.value.Trim()!=""){
         if(compareDate(document.forms[0].beginSubmissionDate.value.Trim(),document.forms[0].endSubmissionDate.value.Trim())==false){
            alert("From Date must be earlier or equal to To Date");
            document.forms[0].beginSubmissionDate.focus();
            return;
         }
      }
      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listClosedForm";
      document.forms[0].action = url;
      document.forms[0].submit();
   }
</script>
<%
  Collection formList = (ArrayList)request.getAttribute("closedFormList");

Collection formSelectList = (ArrayList)request.getAttribute("formSelectList"); // select form list

String formSystemId = (String)request.getParameter("formSystemId");

  String requestNo = (String)request.getParameter("requestNo");
  String formType = (String)request.getParameter("formType");
  String beginSubmissionDate = (String)request.getParameter("beginSubmissionDate");
  String endSubmissionDate = (String)request.getParameter("endSubmissionDate");
  String needquery = (String)request.getParameter("needquery");
  if(needquery!=null && "false".equals(needquery)){
	  beginSubmissionDate = (String)request.getAttribute("beginSubmissionDate");
	  endSubmissionDate = (String)request.getAttribute("endSubmissionDate");
  }
%>
<body>
<FORM nane=AVActionForm method="post"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'>Current Location:</font> <font color='#5980BB' family='Times New Roman'> &gt;Form Monitoring</font> <font color='#5980BB' family='Times New Roman'> &gt; <span class='submenu'>Completed Form</span></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1">Completed Form</TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1">Request No.</span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
      <INPUT Name="requestNo" Type="text" value="<%=requestNo==null?"":requestNo%>" class="text2" style="WIDTH: 130px" value="" id="requestNo" size="20">
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1">Form Type</span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
        <select name="formType" onchange="getOptionList(this.value)">
          <option value="">All</option>
          <% 
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    FormTypeVO typeVo = (FormTypeVO)it.next();
          %>
          <option value="<%=typeVo.getFormTypeId()%>" <%=(formType!=null && (typeVo.getFormTypeId()).equals(formType))?"selected" : ""%>><%=typeVo.getFormTypeName()%></option>
          <%}
          }
          %>
        </select>
      </TD>
    </TR>
    <tr>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1">Form</span></div></TD>
      <TD width=35% height="20" colspan='3'><font size="2"> 
        <select name="formSystemId">
        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        <% 
           if(formSelectList!=null && formSelectList.size()>0){
        	   Iterator formIt = formSelectList.iterator();
        	   while(formIt.hasNext()){
        		   FormManageVO form = (FormManageVO)formIt.next();
        		   out.print("<option value='" + form.getFormSystemId()+"'");
        		   if(formSystemId!=null && !"".equals(formSystemId) && formSystemId.equals(""+form.getFormSystemId())){
        			   out.print(" selected ");
        		   }
        		   out.print(">"+form.getFormId()+" - "+form.getFormName()+"</option>");
               }
         }%>
       </select>
      </TD>
    </tr>
    <TR> 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1">Submitted From</span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginSubmissionDate" onclick='setday(this)'  Type="text" value="<%=beginSubmissionDate==null?"":beginSubmissionDate%>" class="text2" style="WIDTH: 130px" id="beginSubmissionDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1">To</span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="endSubmissionDate" onclick='setday(this)'  Type="text" value="<%=endSubmissionDate==null?"":endSubmissionDate%>" class="text2" style="WIDTH: 130px" id="endSubmissionDate">(mm/dd/yyyy) 
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value="Search" onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="Reset" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
 </FORM>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' >Request No.</td>
            <td align='center' >Request Form</td>
            <td align='center' >Form Type</td>
            <td align='center' >Requester</td>
            <td align='center' >Submitted Date</td>
            <td align='center' >Completed Date</td>
            <td align='center' >Completed by</td>
          </tr>
          <%
            if(formList!=null){
            	int i = 1;
            	SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Iterator formIt = formList.iterator();
            while(formIt.hasNext()){
            	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
            	Date cDate = null;
            	Date hDate = null;
            	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
            	   cDate = df.parse(vo.getSubmissionDateStr());
            	}
            	if(vo.getHandleDateStr()!=null && !"".equals(vo.getHandleDateStr())){
            		hDate = df.parse(vo.getHandleDateStr());
             	}
          %>
            <tr class="tr_change">
              <td ><a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>"><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=FormTypeHelper.getInstance().getFormTypeName(vo.getFormType())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode()).trim()%>&nbsp;&nbsp;</td>
              <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <td ><%=(vo.getHandleDateStr()!=null && !"".equals(vo.getHandleDateStr()))?StringUtil.getDateStr(hDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <td align='right' >
                <%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor().trim()).trim()%>&nbsp;
              </td>
            </tr>
          <%
            i++;}
          }else{
          %>
            <tr class="liebiao_nr2">
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
            </tr>
          <%}%>
        </table>
  </body>
  </html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>