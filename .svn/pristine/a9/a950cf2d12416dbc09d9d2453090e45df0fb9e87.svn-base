<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@ include file="/common/loading.jsp" %>
<html>
<head>
<title>My had handled Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
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
  
   function advanceSearchForm(){
     window.location = "<%=request.getContextPath()%>/wkfProcessAction.it?method=enterAdvanceQuery&queryType=02";
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
      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listMyDealedForm";
      document.forms[0].action = url;
      document.forms[0].submit();
   }
</script>
<%
  Collection formList = (ArrayList)request.getAttribute("dealedFormList");

Collection formSelectList = (ArrayList)request.getAttribute("formSelectList"); // select form list

String formSystemId = (String)request.getParameter("formSystemId");

  String requestNo = (String)request.getParameter("requestNo");
  String formType = (String)request.getParameter("formType");
  String beginSubmissionDate = (String)request.getParameter("beginSubmissionDate");  
  String endSubmissionDate = (String)request.getParameter("endSubmissionDate");
  String beginHandleDate = (String)request.getParameter("beginHandleDate");
  String endHandleDate = (String)request.getParameter("endHandleDate");
  String needquery = (String)request.getParameter("needquery");
  if(needquery!=null && "false".equals(needquery)){
	  beginSubmissionDate = (String)request.getAttribute("beginSubmissionDate");
	  endSubmissionDate = (String)request.getAttribute("endSubmissionDate");
	  endHandleDate = (String)request.getParameter("endHandleDate");
	  needquery = (String)request.getParameter("needquery");
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
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="navigate.processed_list"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="menu.workspace.process_history"/></TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_no"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
      <INPUT Name="requestNo" Type="text" value="<%=requestNo==null?"":requestNo%>" class="text2" style="WIDTH: 130px" value="" id="requestNo" size="20">
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
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
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form"/></span></div></TD>
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
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginSubmissionDate" onclick='setday(this)'  Type="text" value="<%=beginSubmissionDate==null?"":beginSubmissionDate%>" class="text2" style="WIDTH: 130px" id="beginSubmissionDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_to"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="endSubmissionDate" onclick='setday(this)'  Type="text" value="<%=endSubmissionDate==null?"":endSubmissionDate%>" class="text2" style="WIDTH: 130px" id="endSubmissionDate">(mm/dd/yyyy) 
      </TD>
    </TR>
    <TR> 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.processing_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginHandleDate" onclick='setday(this)'  Type="text" value="<%=beginHandleDate==null?"":beginHandleDate%>" class="text2" style="WIDTH: 130px" id="beginHandleDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.processing_to"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="endHandleDate" onclick='setday(this)'  Type="text" value="<%=endHandleDate==null?"":endHandleDate%>" class="text2" style="WIDTH: 130px" id="endHandleDate">(mm/dd/yyyy) 
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="button" name="advanceSearchBtn" value='<i18n:message key="button.advance_search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="advanceSearchForm()">&nbsp;&nbsp;  
         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
 </FORM>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><i18n:message key="common.request_no"/></td>
            <td align='center' ><i18n:message key="common.request_form"/></td>
            <td align='center' ><i18n:message key="common.form_type"/></td>
            <td align='center' ><i18n:message key="common.highlight_content"/></td>
            <td align='center' ><i18n:message key="common.status"/></td>            
            <td align='center' ><i18n:message key="common.submit_date"/></td>
            <td align='center' ><i18n:message key="common.processing_by"/></td>
            <td align='center' ><i18n:message key="common.remaining"/></td>
          </tr>
          <%
            if(formList!=null){
            	int i=1;
            	SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Iterator formIt = formList.iterator();
            while(formIt.hasNext()){
            	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
            	Date cDate = null;
            	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
            	   cDate = df.parse(vo.getSubmissionDateStr());
            	}
          %>
            <tr class="tr_change">
              <td ><a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
              		<%=vo.getHtmlTitleAttr()%>><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=FormTypeHelper.getInstance().getFormTypeName(vo.getFormType())%>&nbsp;&nbsp;</td>
              <td ><%=vo.getHighlightContent()%></td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>          
              <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <td >
              <%
              String processing_by = null;
              if (CommonName.NODE_TYPE_WAITING.equals(vo.getNodeType())) {
                  processing_by = vo.getNodeName();
              } else {
                  processing_by = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor());
              }
              %>
              <%=processing_by%>&nbsp;&nbsp;
              </td>
              <td align='right' >
              <%
                String remainTime = vo.getRemainTime();
                if(!"".equals(remainTime) && !"0".equals(remainTime)){
                	double tmp = Double.parseDouble(remainTime);
                	if(tmp<0){
                		out.print("<b><font color='red'>" + tmp + "</font></b>");
                	}else{
                		out.print(tmp);
                	}
                }
              %>
              &nbsp;&nbsp;
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
       <div id="loading2" style="display:none;align:center">
	     <div class="loading-indicator">
		    It is loading page...
	     </div>
      </div>
  </body>
  </html>
   <script language="javascript">
	setResizeAble(mytable);
	window.onload=function(){enableTooltips()};
 </script>