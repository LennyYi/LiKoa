<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.delegation.vo.DelegationVO,com.aiait.eflow.common.helper.*" %>
<html>
<head>
<title>Deputy handled List</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
    function searchForm(){
      if(document.forms[0].handleBeginDate.value.Trim()!=""){
        if(isDate(document.forms[0].handleBeginDate,"Handled From Date ")==false){
          return;
        }
      }
      if(document.forms[0].handleEndDate.value.Trim()!=""){
        if(isDate(document.forms[0].handleEndDate,"Handled End Date ")==false){
          return;
        }
      }
      if(document.forms[0].handleBeginDate.value.Trim()!="" && document.forms[0].handleEndDate.value.Trim()!=""){
         if(compareDate(document.forms[0].handleBeginDate.value.Trim(),document.forms[0].handleEndDate.value.Trim())==false){
            alert("From Date must be earlier or equal to To Date");
            document.forms[0].handleBeginDate.focus();
            return;
         }
      }  
     document.forms[0].submit();
    }
    function resetForm(){
      document.forms[0].approver.value = "";
      document.forms[0].deputy.value = "";
      document.forms[0].handleBeginDate.value = "";
      document.forms[0].handleEndDate.value = "";
    }
</script>
</head>
<% 
  Collection list = (ArrayList)request.getAttribute("resultList");
  DelegationVO queryVo = (DelegationVO)request.getAttribute("queryVo");
  Collection staffList = (ArrayList)request.getAttribute("staffList");
  String type = (String)request.getAttribute("type");
%>
<body>
<FORM nane=AVActionForm method="post" action="<%=request.getContextPath()%>/delegateAction.it?method=listDeputyHandle"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="delegate_processed.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="delegate_processed.title"/></TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="delegate_processed.staffname"/></span></div></TD>
      <TD width=35% height="20" > 
         <%
           if("self".equals(type)){
        	   out.println("<input type='hidden' name='approver' value='"+queryVo.getAuthorityApprover()+"'>");
           }
         %>
         <select name="approver" <%="self".equals(type)?"disabled":""%>>
           <option value="">&nbsp;&nbsp;</option>
           <%
           if(staffList!=null){
        	   Iterator staffIt = staffList.iterator();
        	   while(staffIt.hasNext()){
        		   StaffVO staff = (StaffVO)staffIt.next();
        		   if(queryVo.getAuthorityApprover()!=null && !"".equals(queryVo.getAuthorityApprover()) && staff.getStaffCode().equals(queryVo.getAuthorityApprover())){
        			   out.print("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName().trim()+"</>");
        		   }else{
        		      out.print("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName().trim()+"</>");
        		   }
        	   }
           }
         %>
         </select>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="delegate_processed.deputyname"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
        <select name="deputy">
          <option value="">&nbsp;&nbsp;</option>
           <%
           if(staffList!=null){
        	   Iterator staffIt = staffList.iterator();
        	   while(staffIt.hasNext()){
        		   StaffVO staff = (StaffVO)staffIt.next();
        		   if(queryVo.getAuthorityDeputy()!=null && !"".equals(queryVo.getAuthorityDeputy()) && staff.getStaffCode().equals(queryVo.getAuthorityDeputy())){
        			   out.print("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName().trim()+"</>");
        		   }else{
        		      out.print("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName().trim()+"</>");
        		   }
        	   }
           }
         %>
        </select>
      </TD>
    </TR>
     <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="delegate_processed.from"/></span></div></TD>
      <TD width=35% height="20" > 
        <input type="text"  value="<%=queryVo.getHandledBeginDate()==null?"":queryVo.getHandledBeginDate()%>" name="handleBeginDate" onclick='setday(this)'>(MM/DD/YYYY)
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="delegate_processed.to"/></span></div></TD>
      <TD width=30% height="20" >
        <input type="text"  name="handleEndDate" value="<%=queryVo.getHandledEndDate()==null?"":queryVo.getHandledEndDate()%>" onclick='setday(this)' >(MM/DD/YYYY)
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="button" name="resetBtn" value='<i18n:message key="button.reset"/>' onclick="resetForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
 <input type=hidden name="isSearching" value="Y">
 </FORM>
   <table width="100%"  border="0">
     <tr>
       <td align='left'>
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><i18n:message key="delegate_processed.requestno"/></td>
            <td align='center' ><i18n:message key="common.company"/></td>
            <td align='center' ><i18n:message key="delegate_processed.staffname"/></td>
            <td align='center' ><i18n:message key="delegate_processed.deputyname"/></td>
            <td align='center' ><i18n:message key="delegate_processed.processeddate"/></td>
            <td align='center' ><i18n:message key="delegate_processed.processednode"/></td>
          </tr>
          <%
           if(list!=null){
        	int i = 1;
            Iterator listIt = list.iterator();
            while(listIt.hasNext()){
            	WorkFlowProcessTraceVO vo = (WorkFlowProcessTraceVO)listIt.next();
          %>
            <tr class="tr_change">
              <td ><a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>"><%=vo.getRequestNo()%></a>&nbsp;&nbsp;</td>
              <td ><%=CompanyHelper.getInstance().getOrgName(vo.getFilePathName())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode())%>&nbsp;&nbsp;</td>
              <td ><%=vo.getHandleDateStr()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getCurrentNodeId()%>&nbsp;&nbsp;</td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
</body>
</html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>