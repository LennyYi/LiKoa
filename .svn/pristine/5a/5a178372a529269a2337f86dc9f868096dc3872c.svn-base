<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@page import="com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<%@ include file="/common/loading.jsp" %> 
<%!
String level = "";
String types = "";
String formids = "";
private final boolean checkScope(String type, String formid){
		   if("null".equals(level)){
			   return true;
		   }
		   if("1".equals(level)){
			   return true;
		   }
		   if(level.indexOf("2")>=0){
			   if( types.indexOf(","+type+",")>=0)
				   return true;
		   }
		   if(level.indexOf("3")>=0){
			   if( formids.indexOf(","+formid+",")>=0)
				   return true;
		   }
		   return false;
}
%>
<% 
  String deputyCode = (String)request.getParameter("deputyCode");
  String staffCode = (String)request.getParameter("staffCode");
  this.level = (String)request.getParameter("level");
  this.types = (String)request.getParameter("types");
  this.formids = (String)request.getParameter("formids");
  String deputyName = StaffTeamHelper.getInstance().getStaffNameByCode(deputyCode);
  String approverName = StaffTeamHelper.getInstance().getStaffNameByCode(staffCode);
%>
<html>
<head>
<title>Personal Applied Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
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
      if(checkSelect('requestNo')<=0){
	      alert("You have not selected any records to reassign!");
	      return;
	  }
	  if(confirm("Are you sure to reassign the selected records to <%=deputyName%>")){
	    var requestNoStr = getTableSelectRecordStr("requestNo","requestNo");
	    var url = "<%=request.getContextPath()%>/delegateAction.it?method=reassignForm&staffCode=<%=staffCode%>&deputyCode=<%=deputyCode%>&" + requestNoStr;
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
        if(result.Trim()=="success"){
          alert("It is successful to reassign");
          window.close();
        }else{
          alert("Fail to reassign");
        }
	  }      
   }
</script>
<%
  Collection formList = (ArrayList)request.getAttribute("inquiryFormList"); // search result list
%>
<body>
<FORM nane=myForm method="post"> 
<input type="hidden" name="staffCode" value="<%=((String)request.getParameter("staffCode")==null?"":(String)request.getParameter("staffCode"))%>">
<input type="hidden" name="deputyCode" value="<%=((String)request.getParameter("deputyCode")==null?"":(String)request.getParameter("deputyCode"))%>">

 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1">Adjust Form Processor (From <b><%=approverName%></b> To <b><%=deputyName%></b>)</TD>
   </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value="Submit" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="searchForm()">&nbsp;&nbsp;
         <input type="button" name="resetBtn" value="Close" onclick="javascript:window.close()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'requestNo')"></td>
            <td align='center' >Request No.</td>
            <td align='center' >Request Form</td>
            <td align='center' >Status</td>
            <td align='center' >Submitted/Drafted Date</td>
            <td align='center' >Processed By</td>
            <td align='center' >Processing By</td>
          </tr>
          <%
            if(formList!=null){
            	SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Iterator formIt = formList.iterator();
            	int i = 1;
            while(formIt.hasNext()){
            	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
            	Date cDate = null;
            	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
            	   cDate = df.parse(vo.getSubmissionDateStr());
            	}
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="requestNo" value="<%=vo.getRequestNo()%>"
          <%if(!checkScope(vo.getFormType(),""+vo.getFormSystemId()))out.print("disabled");%>></td>
              <td >          
              <a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&status=<%=vo.getStatus()%>&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>&adjust=true"><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>
              <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor())%>&nbsp;&nbsp;</td>
            </tr>
          <%
           i++; }
          }else{
          %>
            <tr class="liebiao_nr2">
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
            </tr>
          <%}%>
        </table>
   </FORM>
  </body>
  </html>
   <script language="javascript">
	setResizeAble(mytable);
 </script>
