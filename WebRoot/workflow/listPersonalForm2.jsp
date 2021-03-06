<%--
    Task_ID    Author     Modify_Date    Description
1.  IT0973     Robin.Hou  12/27/2007     DS-013:add the link of “Copy” for every list records
2.  IT1029     Young Yan  06/26/2008     DS-001:Revise the condition of showing the deputy logo 
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.housekeeping.vo.StaffVO"%>
<%@page import="com.aiait.framework.i18n.*,com.aiait.eflow.common.*"%>
<%@ include file="/common/loading.jsp" %>
<html>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<head>
<title>Personal Applied Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
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
     window.location = "<%=request.getContextPath()%>/wkfProcessAction.it?method=enterAdvanceQuery&queryType=01";
   }
  
   function searchForm(isExport){
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
	            alert(from_to_date_compare);
	            document.forms[0].beginSubmissionDate.focus();
	            return;
	         }
	      }
	      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listPersonalApplyForm&isExport="+isExport;
	      if(isExport=="1")document.forms[0].target="_blank"
	      document.forms[0].action = url;
	      document.forms[0].submit();
	   }
      
   function deleteForm(requestNo){
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=deleteRequestedForm&requestNo="+requestNo;
      if(confirm(confirm_delete_form + requestNo)){
        var result="";
        if(xmlhttp){
          xmlhttp.open("POST", url, true);
          xmlhttp.onreadystatechange=function()
          {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                result = xmlhttp.responseText;
                if(result.Trim()=="success"){
                   alert(form_delete_success);
                   searchForm();
                }else{
                   alert(result.Trim());
                }
             }
          }
          xmlhttp.setRequestHeader("If-Modified-Since","0");
          xmlhttp.send(null);	
        }

      }
   }
   function copyRequestedForm(requestNo){
     var url = "<%=request.getContextPath()%>/formManageAction.it?method=copyRequetedForm&requestNo="+requestNo;
      if(confirm(confirm_copy_form + requestNo)){
        var result="";
        if(xmlhttp){
          xmlhttp.open("POST", url, true);
          xmlhttp.onreadystatechange=function()
          {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                result = xmlhttp.responseText;
                if(result.Trim()=="success"){
                   alert(form_copy_success);
                   document.all['requestNo'].value = "";
                   searchForm();
                }else{
                   alert(result.Trim());
                }
             }
          }
          xmlhttp.setRequestHeader("If-Modified-Since","0");
          xmlhttp.send(null);	
        }

      }
   }
</script>
<%
//返回到本页面，强制刷新本页面
//response.setHeader("Pragma","No-Cache");   
//response.setHeader("Cache-Control","No-Cache");   
//response.setDateHeader("Expires",   0);   
//
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

  Collection formList = (ArrayList)request.getAttribute("formList"); // search result list
  
  Collection formSelectList = (ArrayList)request.getAttribute("formSelectList"); // select form list
  
  String formSystemId = (String)request.getParameter("formSystemId");
  
  String requestNo = (String)request.getParameter("requestNo");
  if(requestNo==null){
	  requestNo = (String)request.getAttribute("requestNo");
  }
  String formType = (String)request.getParameter("formType");
  String status = (String)request.getParameter("status");
  String team_forms = request.getParameter("team_forms");
  String submittedBy = (String)request.getParameter("submittedBy");
  String beginSubmissionDate = (String)request.getParameter("beginSubmissionDate");
  String endSubmissionDate = (String)request.getParameter("endSubmissionDate");
  String comeFrom = (String)request.getParameter("comefrom");
  if(comeFrom!=null && "left".equals(comeFrom)){
	  beginSubmissionDate = (String)request.getAttribute("beginSubmissionDate");
	  endSubmissionDate = (String)request.getAttribute("endSubmissionDate");
  }
%>
<body>
<form nane="AVActionForm" method="post"> 
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
		 <tr><td height="10"></td></tr>
		 <!--<tr>
		 	<td>
		 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="navigate.request_form_list"/></font></strong>
		 	 </td> 
		 </tr>-->
	</table>
	 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
	   <TR align="center" >
	      <TD height="25" colspan="4" class="tr1"><i18n:message key="module.request_form"/></TD>
	   </TR>
	   <TR > 
	      <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_no"/></span></div></TD>
	      <TD width=38% height="20" > 
	      <font size="2"> 
	      <INPUT Name="requestNo" Type="text" value="<%=requestNo==null?"":requestNo%>" class="text2" style="WIDTH: 130px" id="requestNo">
	      </font>
	      </TD>
	      <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
	      <TD width=38% height="20" ><font size="2"></font>
	        <select name="formType"  onchange="getOptionList(this.value)">
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
	      <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form"/></span></div></TD>
	      <TD width=38% height="20" colspan='3'><font size="2"></font>
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
	   <tr>
	    <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_by"/></span></div></TD>
	    <TD width=38% height="20">
	        <select name="submittedBy" id="submittedBy">
	          <option value=""></option>
	          <%
	
	           Collection staffList = StaffTeamHelper.getInstance().getStaffListByCompany(currentStaff.getOrgId());
	           //Collection staffList = StaffTeamHelper.getInstance().getStaffList();
	
	           if(staffList!=null){
	        	   Iterator staffIt = staffList.iterator();
	        	   while(staffIt.hasNext()){
	        		   StaffVO staff = (StaffVO)staffIt.next();
	        		   if(submittedBy!=null && staff.getStaffCode().equals(submittedBy)){
	        			 out.println("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName().trim()+"("+staff.getLogonId()+")"+"</option>");  
	        		   }else{
	        		     out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName().trim()+"("+staff.getLogonId()+")"+"</option>");
	        		   }
	        	   }
	           }
	          %>
	        </select>
	        <%
	        String teamForms = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_TEAMSCOPE_QUERY_FORMS);
	        if (teamForms != null && !teamForms.equals("")) {
	        %>
	        &nbsp;&nbsp;
	        <span title="<i18n:message key="common.teamscope_form_title"/>">
	        <input type="checkbox" id="team_forms" name="team_forms" value="yes" <%=team_forms == null ? "" : "checked"%>>
	        <i18n:message key="common.teamscope_form"/></span>
	        <%
	        }
	        %>
	    </TD>
	    <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.status"/></span></div></TD>
	    <TD width=38% height="20">
	          <select name="status" id="status">
	          <option value="">All</option>
	          <option value="00" <%=(status!=null && "00".equals(status))?"selected" : ""%>>Drafted</option>
	          <option value="01" <%=(status!=null && "01".equals(status))?"selected" : ""%>>Submitted</option>
	          <option value="02" <%=(status!=null && "02".equals(status))?"selected" : ""%>>IN Progress</option>
	          <option value="03" <%=(status!=null && "03".equals(status))?"selected" : ""%>>Rejected</option>
	          <option value="04" <%=(status!=null && "04".equals(status))?"selected" : ""%>>Completed</option>
	        </select>
	    </TD>
	   </tr>
	   <TR> 
	      <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_from"/></span></div></TD>
	      <TD width=38% height="20" > 
	      <font size="2"> 
	       <INPUT Name="beginSubmissionDate" onclick='setday(this)'  Type="text" value="<%=beginSubmissionDate==null?"":beginSubmissionDate%>" class="text2" style="WIDTH: 130px" id="beginSubmissionDate">(mm/dd/yyyy)
	      </font>
	      </TD>
	      <TD width=12% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_to"/></span></div></TD>
	      <TD width=38% height="20" ><font size="2"> 
	       <INPUT Name="endSubmissionDate" onclick='setday(this)'  Type="text" value="<%=endSubmissionDate==null?"":endSubmissionDate%>" class="text2" style="WIDTH: 130px" id="endSubmissionDate">(mm/dd/yyyy) 
	      </TD>
	    </TR>
	
	    <tr>
	      <td colspan='4' align='center'>
	         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
	           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="searchForm()">&nbsp;&nbsp;
	         <input type="button" name="advanceSearchBtn" value='<i18n:message key="button.advance_search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
	           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="advanceSearchForm()">&nbsp;&nbsp;      
	         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
	           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; 
	          <input type="button" name="expBtn" value="<i18n:message key="button.export_excel"/>" onclick="searchForm(1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
	           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	      </td>
	    </tr>
	 </TABLE>
</form>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
	<tr class="liebiao_tou">
		<td align='center' ><i18n:message key="common.request_no"/></td>
		<td align='center' ><i18n:message key="common.request_form"/></td>
		<td align='center' ><i18n:message key="common.highlight_content"/></td>
		<td align='center' ><i18n:message key="common.status"/></td>
		<td align='center' ><i18n:message key="common.submit_draft_date"/></td>
		<td align='center' ><i18n:message key="common.submit_by"/></td>
		<td align='center' ><i18n:message key="common.processed_by"/></td>
		<td align='center' ><i18n:message key="common.processing_by"/></td>
		<td align='center' ><i18n:message key="common.operation"/></td>
	</tr>
      <%
        if(formList!=null){
        	SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Iterator formIt = formList.iterator();
        	int i = 1;
        	String tempStatus = "";
        while(formIt.hasNext()){
        	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
        	Date cDate = null;
        	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
        	   cDate = df.parse(vo.getSubmissionDateStr());
        	}
        	if(!"0".equals(vo.getNodeId()) && "03".equals(vo.getStatus())){ //如果不是开始节点，并且是“拒绝”状态，则将其传入到dealForm.jsp的值修改成“02”
        		tempStatus = "02";
        	}else{
        		tempStatus = vo.getStatus();
        	}
            String viewFlag = "";
            if (!currentStaff.getStaffCode().equalsIgnoreCase(vo.getRequestStaffCode()) 
                    && !currentStaff.getStaffCode().equalsIgnoreCase(vo.getSubmittedBy())) {
                // Can only view the form requested by others
                viewFlag = "&viewFlag=false";
            }
            String somebodyLocked = "";
        	if("1".equals(vo.getInProcess()) && !vo.getInProcessStaffCode().equals(currentStaff.getStaffCode())
        			&& (vo.getInvitedExpert()+",").indexOf(currentStaff.getStaffCode())==-1){
        		somebodyLocked=" style=\"color:#AFAFAF\" ";
        	}        
            if(!"-1".equals(vo.getInProcess())){
      %>
     <tr class="tr_change">
        <td >
              <%if("2".equals(vo.getIsUrgent())){%>
                 <image src='<%=request.getContextPath()%>/images/urgent.gif' alt="Urgent"></image>
              <%}%>
              <%if("0".equals(vo.getOpenFlag())){%>
                 <image src='<%=request.getContextPath()%>/images/new1.gif' alt="New"></image>
              <%}%>
              <%if("1".equals(vo.getInProcess())){
                    String staffName = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getInProcessStaffCode()).trim();
					String title = staffName + " " + I18NMessageHelper.getMessage("form.locked_form");
              %>
                <a href='#' title='<%=title%>'><img border='0' src='<%=request.getContextPath()%>/images/lock.gif'></a>
              <%}%>
              
              <%
				String strDeputy = "";
				if (vo.isDealByDeputy()) {
					String staffName = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor()).trim();
					strDeputy = I18NMessageHelper.getMessage("form.deal_by_deputy", new String[] { "", staffName, "" });
					strDeputy = "<a href='#' title='" + strDeputy + "'><img border=0 src='" + request.getContextPath() + "/images/deputy.gif'></a>";
				}
              %>
              <%=strDeputy%>
              <%if(vo.getExpertAdviceFlag()){%>
                <image src='<%=request.getContextPath()%>/images/expert_advise.gif' alt="Advise">
                <a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=advise&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
                	<%=vo.getHtmlTitleAttr()%> <%=somebodyLocked %>><%=vo.getRequestNo()%>
              <%}else{%>
                <%if (vo.isExpertReplied()) {%>
                  <image src='<%=request.getContextPath()%>/images/expert_replied.gif' alt="Advised">
                <%} else if (vo.getInvitedExpert() != null && !"".equals(vo.getInvitedExpert())) {%>
                  <image src='<%=request.getContextPath()%>/images/invite_expert.gif' alt="Invited">
                <%}%>
                <a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=deal&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
                	<%=vo.getHtmlTitleAttr()%> <%=somebodyLocked %>><%=vo.getRequestNo()%>
              <%}%>
              &nbsp;&nbsp;</td>
              
       <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
       <td ><%=vo.getHighlightContent()%></td>
       <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>
       <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
       <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getSubmittedBy())%>&nbsp;&nbsp;</td>
       <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
       <td >
       <%
       String processing_by = null;
       if (CommonName.NODE_TYPE_WAITING.equals(vo.getNodeType())) {
           processing_by = vo.getNodeName();
       } else {
           processing_by = (!"-1".equals(vo.getNodeId()) && vo.getIsDeputy()!=null && "1".equals(vo.getIsDeputy()) && vo.getOriginProcessor()!=null && vo.getOriginProcessor().indexOf(",")==-1 && (!"".equals(vo.getCurrentProcessor())))?"<a href='#' title=\"It is "+StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor()).trim()+"'s deputy.\"><img  border=0 src='"+request.getContextPath()+"/images/deputy.gif'></a>":"";
           processing_by += StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor());
       }
       %>
        <%=processing_by%>&nbsp;&nbsp;
       </td>
       <td>
       </td>
     </tr>
   <%
            }
            else
            {
   %>
   	  <tr class="tr_change">
       <td ><a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent<%=viewFlag%>&operateType=view&status=<%=tempStatus%>&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
       		<%=vo.getHtmlTitleAttr()%>><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
       <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
       <td ><%=vo.getHighlightContent()%></td>
       <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>
       <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
       <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getSubmittedBy())%>&nbsp;&nbsp;</td>
       <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
       <td >
       <%
       String processing_by = null;
       if (CommonName.NODE_TYPE_WAITING.equals(vo.getNodeType())) {
           processing_by = vo.getNodeName();
       } else {
           processing_by = (!"-1".equals(vo.getNodeId()) && vo.getIsDeputy()!=null && "1".equals(vo.getIsDeputy()) && vo.getOriginProcessor()!=null && vo.getOriginProcessor().indexOf(",")==-1 && (!"".equals(vo.getCurrentProcessor())))?"<a href='#' title=\"It is "+StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor()).trim()+"'s deputy.\"><img  border=0 src='"+request.getContextPath()+"/images/deputy.gif'></a>":"";
           processing_by += StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor());
       }
       %>
        <%=processing_by%>&nbsp;&nbsp;
       </td>
       <td>
       <%
        if(viewFlag.equals("") && ("00".equals(vo.getStatus()) || ("0".equals(vo.getNodeId()) && "03".equals(vo.getStatus())))){ //如果是Draft状态 或者 （是Reject状态并且当前节点是开始节点），则用户可以删除该form
       %>
       <a href="javascript:deleteForm('<%=vo.getRequestNo()%>')"><i18n:message key="button.delete"/></a>
       <%}else{%>
        <i18n:message key="button.delete"/>
       <%}%>
       &nbsp;&nbsp;
       <a href="javascript:copyRequestedForm('<%=vo.getRequestNo()%>')"><i18n:message key="button.copy"/></a>
       
	<%if (CompanyHelper.getInstance().getEFlowCompany().equals(
     CompanyHelper.EFlow_AIA_CHINA) && "04".equals(vo.getStatus())) {%>
          &nbsp;&nbsp;
          <a href="javascript:openCenterWindow('<%=request.getContextPath()%>/formManageAction.it?method=supplementAttachFile&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>','800','600')"> 
          <i18n:message key="button.upload" /> </a> <%}%>
       </td>
     </tr>
   <%
            }
    i++; }
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
