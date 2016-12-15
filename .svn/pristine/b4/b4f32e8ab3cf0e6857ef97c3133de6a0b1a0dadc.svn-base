<!-- Task_ID	Author	Modify_Date	Description------------------------------------------->
<!-- IT0958		Young	 10/18/2007 DS011 Form Design can authorized for every form type-->
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.reportmanage.vo.ReportTypeVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.reportmanage.helper.*"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
<title>Manage Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">

  function searchReport(){
    document.forms[0].action = "<%=request.getContextPath()%>/reportManageAction.it?method=manageReport";
    document.forms[0].submit();
  }

  function addReport(){
     var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReport&type=new";
     openCenterWindow(url,460,300);
  }
  function editReport(reportSystemId){
    //var str = getTableSelectRecordStr("formSystemId","formSystemId");
    //var formSystemId = getObjectValue("formSystemId");
    if(reportSystemId==""){
      return;
    }
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReport&type=edit&reportSystemId=" + reportSystemId;
    openCenterWindow(url,460,300);
  }
  
  function publishReport(action){
    var message = "";
    var confirmMsg = "";
    if(action=="publish"){
      message = have_no_select;
      confirmMsg = confirm_publish_report;
    }else{
      message = have_no_select;
      confirmMsg = confirm_disable_report;
    }

    if(checkSelect('reportSystemId')<=0){
      alert(message);
      return;
    }

    if(confirm(confirmMsg)){
       //var str = getTableSelectRecordStr("formSystemId","formSystemId");
       var reportSystemIdStr = getTableSelectRecordStr("reportSystemId","reportSystemId");
       var url = "<%=request.getContextPath()%>/reportManageAction.it?";
       if(action=="publish"){
           url = url + "method=publishReport&" + reportSystemIdStr;
       }else if(action=="disable"){
           url = url + "method=disableReport&" + reportSystemIdStr;
       }
       var  xmlhttp = createXMLHttpRequest();
       var result;
       if(xmlhttp){
           xmlhttp.open('POST',url,false);
           xmlhttp.onreadystatechange = function()
           {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 alert(result)
                 document.location.reload();
             }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
       }
    }
  }
  
  function revokeReport(){
    var reportSystemId = getObjectValue("reportSystemId");
    if(reportSystemId==""){
      return;
    }
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=revokeReport&reportSystemId="+reportSystemId;
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
       window.location.reload();
     }else{
       alert("Fail to Revoke report");
     }
  }
  
  function copyReport(reportSystemId){
    /**
     if(checkSelect('formSystemId')!=1){
      alert("You have not selected any form to copy!");
      return;
    }
    **/
    //var formSystemId = getObjectValue("formSystemId");
    if(reportSystemId==""){
      return;
    }
    if(confirm(confirm_copy_report)==false){
      return;
    }
    //var str = getTableSelectRecordStr("formSystemId","formSystemId");
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=copyReport&reportSystemId="+reportSystemId;
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
       window.location.reload();
     }else{
       alert("Fail to Copy Report");
     }
  }
  
  function deleteReport(){
    if(checkSelect('reportSystemId')<=0){
      alert(have_no_select);
      return;
    }
    //var formSystemId = getObjectValue("formSystemId");
    //if(formSystemId==""){
    //  return;
   // }
    if(confirm(confirm_delete_report)==false){
       return;
    }
    var reportSystemIdStr = getTableSelectRecordStr("reportSystemId","reportSystemId");
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=deleteReport&"+reportSystemIdStr;
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
       window.location.reload();
     }else{
       alert(result);
     }
  }
 function selectRowChange(obj)   
 {       
  var flag = "false";
  for(var i=1;i<obj.rows.length;i++)   
  {   
    if(i==event.srcElement.parentElement.rowIndex)   
    {   
      obj.rows(i).className="liebiao_nr1"; 
      changeObjValue("reportSystemId",document.getElementById("reportSystemId"+i).value);
      flag = "true";
    }else{   
      if((i-1)%2==0){
        obj.rows(i).className="liebiao_nr2";   
      }else{
        obj.rows(i).className="liebiao_nr3";   
      }
    }
  }   
  if(flag=="false"){
    changeObjValue("reportSystemId","");
  }
 }   
</script>

</head>
<%
  String orgId = (String)request.getParameter("orgId");
  String reportId = (String)request.getParameter("reportId");
  String reportType = (String)request.getParameter("reportType");
  String status = (String)request.getParameter("status");
  Collection reportList = (ArrayList)request.getAttribute("reportList");
  Iterator reportIt = reportList.iterator();
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  AuthorityHelper authority = AuthorityHelper.getInstance();
  if(orgId==null || "".equals(orgId)){
	  orgId = currentStaff.getOrgId();
  }
  
%>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="form_design.navigate"/></font></strong>
 	 </td>
 </tr>-->
</table>
<form name="myReport" method="post">
<input type="hidden" name="reportSystemId" value="">
<TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
     <tr class="tr1">
       <td align='center' colspan='4'><B><i18n:message key="menu.design.report_design"/></B></td>
     </tr>
     <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_design.report_id"/></span></div></TD>
      <TD width=35% height="20" > 
        <input type="text"  value="<%=reportId==null?"":reportId%>" name="reportId">
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.report_type"/></span></div></TD>
      <TD width=30% height="20">
         <select name="reportType">
          <option value="">All</option>
          <% 
            Collection typeList = ReportTypeHelper.getInstance().getReportTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    ReportTypeVO typeVo = (ReportTypeVO)it.next();
  			    if(authority.checkAuthorityByReportType(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_FORM_MANAGE,typeVo.getReportTypeId())){
  			    %>
  			    <option value="<%=typeVo.getReportTypeId()%>" <%=(reportType!=null && (typeVo.getReportTypeId()).equals(reportType))?"selected" : ""%>><%=typeVo.getReportTypeName()%></option>
  			    <% 	
  			    }
              }
          }
          %>
        </select>
      </TD>
    </TR>
    <tr>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></TD>
      <TD width=35% height="20" > 
         <select name="orgId">
           <%
            Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(vo.getOrgId().equals(orgId)){
            		   out.print("<option value='"+vo.getOrgId()+"' selected>" + vo.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.status"/></span></div></TD>
      <TD width=35% height="20"> 
         <select name="status">
           <option value=""></option>
           <option value="0" <%="0".equals(status)?"selected":""%>>Published</option>
           <option value="1" <%="1".equals(status)?"selected":""%>>Design</option>
           <option value="2" <%="2".equals(status)?"selected":""%>>Disabled</option>
         </select>
      </TD>
    </tr>
     <tr>
       <td align='center' colspan='4'>
                <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchReport()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="<i18n:message key="button.reset"/>" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_ADD%>' isButton='true' labelValue='Add'>
        <input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addReport()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        </purview:purview>
        &nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_DELETE%>' isButton='true' labelValue='Delete'>
        <input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteReport()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        </purview:purview>
        &nbsp;
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_PUBLISH%>' isButton='true' labelValue='Publish'>
          <input type="button" name="publishBtn" value='<i18n:message key="button.publish"/>' onclick="publishReport('publish')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;
        </purview:purview>
        <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_PUBLISH%>' isButton='true' labelValue='Disable'>
         <input type="button" name="disabledBtn" value='<i18n:message key="button.disable"/>' onclick="publishReport('disable')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        </purview:purview>   
       </td>
     </tr>
     </table>
   </form>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable  style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'reportSystemId')"></td>
            <td align='center' ><i18n:message key="report_design.report_id"/></td>
            <td align='center' ><i18n:message key="common.request_report"/></td>
            <td align='center' ><i18n:message key="common.report_type"/></td>
            <!--<td align='center'>Description</td>  -->
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="report_design.create_date"/></td>
            <td align='center' ><i18n:message key="report_design.action"/></td>
          </tr>
          <%
            int i = 1;
            SimpleDateFormat   df  =null;
            if(reportIt!=null){
               df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            while(reportIt.hasNext()){
            	ReportManageVO report = (ReportManageVO)reportIt.next();
            	Date cDate = null;
            	if(report.getCreateDateStr()!=null && !"".equals(report.getCreateDateStr())){
            	   cDate = df.parse(report.getCreateDateStr());
            	}
          %>
            <tr class="tr_change">
         
              <td align='center' ><input type="checkbox" name="reportSystemId" value="<%=report.getReportSystemId()%>"></td>
              <td >
               <%=report.getReportId()%>&nbsp;&nbsp;
              </td>
              <td ><%=report.getReportName()%>&nbsp;&nbsp;</td>
              <td ><%=ReportTypeHelper.getInstance().getReportTypeName(report.getReportType())%>&nbsp;&nbsp;</td>
              <td >
              <%
               if("0".equals(report.getStatus())){
            	  out.print("Published");
               }else if("1".equals(report.getStatus())){
            	   out.print("Design");
               }else if("2".equals(report.getStatus())){
            	   out.print("Disabled");
               }
              %>
              &nbsp;&nbsp;
              </td>
              <td ><%=(report.getCreateDateStr()!=null && !"".equals(report.getCreateDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%> &nbsp;&nbsp;</td>
              <td >
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_DESIGN%>' isButton='false' labelValue='<i18n:message key="button.design"/>'>
               <a href='<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReport&type=editWholeReport&reportSystemId=<%=report.getReportSystemId()%>'><i18n:message key="button.design"/></a>
               </purview:purview>
               &nbsp;/&nbsp;
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_UPDATE%>' isButton='false' labelValue='<i18n:message key="button.edit"/>'>
               <a href="javascript:editReport('<%=report.getReportSystemId()%>')"><i18n:message key="button.edit"/></a>
               </purview:purview>
               &nbsp;/&nbsp;
               <purview:purview moduleId='<%=ModuleOperateName.MODULE_FORM_MANAGE%>' operateId='<%=ModuleOperateName.OPER_FORM_COPY%>' isButton='false' labelValue='<i18n:message key="button.copy"/>'>
               <a href="javascript:copyReport('<%=report.getReportSystemId()%>')"><i18n:message key="button.copy"/></a>
               </purview:purview>
               </td>
            </tr>
          <%i++;}%>
        </table>

</body>
</html>
   <script language="javascript">
	//setResizeAble(mytable);
 </script>