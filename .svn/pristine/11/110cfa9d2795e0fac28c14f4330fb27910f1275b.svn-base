<!-- Task_ID	Author	Modify_Date	Description------------------------------------------->
<!-- IT0958		Young	 10/18/2007 DS011 Form Design can authorized for every form type-->
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*,java.util.*" %>
<%@page import="com.aiait.eflow.reportmanage.vo.ReportTypeVO,com.aiait.eflow.reportmanage.helper.*"%>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO" %>
<%@page import="com.aiait.eflow.util.*" %>
<% 
  String type = (String)request.getParameter("type");
  if(type==null){
	  type = "new";
  }
  ReportManageVO report = (ReportManageVO)request.getAttribute("wholeReport");
  if(report==null){
	  report = new ReportManageVO();
  }
  String reportType = (String)request.getParameter("reportType");
  if(reportType==null || "".equals(reportType)){
	  reportType = report.getReportType();
  }
   
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  AuthorityHelper authority = AuthorityHelper.getInstance();
%>
<html>
<head>
 <title><i18n:message key="report_design.report_edit_title"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
 <script language="javascript">
     var obj = "<%=type%>";
    function initReport(){
      if(obj=="edit"){
        document.myReport.saveType = "edit";
      }else{
        document.myReport.saveType = "new";
      }
    }
    function submitReport(){
       var frm = document.forms[0];
       document.all['btnSubmit'].disabled = "true";
       //if(reportValidate(frm)==false){
       //  document.all['btnSubmit'].disabled = "";
       //  return;
      // }
	   //var validationstr = document.all["Validation"].value;
	   //validationstr = reportatStringAllChar(validationstr);
       var param = "&reportId="+frm.reportId.value
                 +"&reportName="+frm.reportName.value+"&reportDescription="+encodeURIComponent(frm.reportDescription.value)+"&saveType=<%=type%>"
                 +"&reportType="+frm.reportType.value+"&reportSystemId="+frm.reportSystemId.value + "&orgId=" + frm.orgId.value+"&displayType="+frm.displayType.value;
       var url = "<%=request.getContextPath()%>/reportManageAction.it?method=saveBaseReport";
      
      	param = encodeURI(encodeURI(param)); //
        var myAjax = new Ajax.Request(
         url,
        {
            method:"post",       //
            parameters:param,   //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                  alert(save_success);
                 window.opener.document.forms[0].submit();
                 window.close();
            },
            onError:function(x){          //
                 alert(save_unsuccess);
                   document.all['btnSubmit'].disabled = "";
            } 
        } 
       ); 
       
    }
    window.onload=function(){resize(420);}
 </script>
</head>
<body>
<form name="myReport" method='Post'>
   <input type="hidden" name="saveType" value="<%=type%>"> 
   <input type="hidden" name="reportSystemId" value="<%=report.getReportSystemId()%>">
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="report_design.report_id"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='reportId' size='20' maxlength='50' title='<i18n:message key="report_design.report_id"/>' required="true" value='<%=(report.getReportId()!=null && !"".equals(report.getReportId()))?report.getReportId():""%>'>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.report"/> : 
       </td>
       <td style="WIDTH:70%">
         <input type='text' name='reportName' size='30' maxlength='100' title='<i18n:message key="common.report"/>' required="true" value='<%=(report.getReportName()!=null && !"".equals(report.getReportName()))?report.getReportName():""%>'>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.report_display_type"/> : 
       </td>
       <td style="WIDTH:70%">
       <select name="displayType">
           <option value="01" <%=(report.getDisplayType()!=null && "01".equals(report.getDisplayType()))?"selected":""%>>perpendicular</option>
           <option value="02" <%=(report.getDisplayType()!=null && "02".equals(report.getDisplayType()))?"selected":""%>>horizontal</option>
            
       </select>
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.report_type"/> : 
       </td>
       <td style="WIDTH:70%">
       <select name="reportType">
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
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.company"/> :
       </td>
       <td style="WIDTH:70%">
         <select name="orgId">
           <%
            Collection companyList = currentStaff.getOwnCompanyList();  //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(vo.getOrgId().equals(report.getOrgId())){
            		   out.print("<option value='"+vo.getOrgId()+"' selected>" + vo.getOrgName()+"</option>");   
            	   }else{
            	     out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
            	}
            }
           %>
         </select>
       </td>
    </tr>
    <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.description"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows='5' cols='40' name="reportDescription"><%=(report.getDescription()!=null && !"".equals(report.getDescription()))?report.getDescription():""%></textarea>
       </td>
     </tr>
    
     <tr>
       <td align='center' colspan='2'>
         <input type='button' name='btnSubmit' value='<i18n:message key="button.submit"/>' onclick='submitReport()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='Reset' name='reset1' value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='close1' value='<i18n:message key="button.close"/>' onclick='window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
   </table>
  </form>

</html>