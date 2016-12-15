<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*"%>
<%@page import="com.aiait.framework.i18n.*"%>
<html>
<head>
<title>Export Deal Form List to Excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=a.xls">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%  Collection formList = (ArrayList)request.getAttribute("dealFormList"); 
response.setContentType("application/vnd.ms-excel;charset=GBK");
boolean contentField2 = false;
boolean contentField3 = false;
String contentFields = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LIST_CONTENT_FIELDS);
if (contentFields != null && !"".equals(contentFields)) {
	  String[] aryContentFields = contentFields.split(";");
	  for (int i = 0; i < aryContentFields.length; i++) {
	      if (aryContentFields[i].equals("2")) {
	          contentField2 = true;
	      } else if (aryContentFields[i].equals("3")) {
	          contentField3 = true;
      }
	  }
}
%>
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%"  border="1" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
      <tr class="liebiao_tou">     
        <td align='center'><strong><i18n:message key="common.request_no"/></strong></td>
        <td align='center'><strong><i18n:message key="common.request_form"/></strong></td>
        <td align='center'><strong><i18n:message key="common.highlight_content"/></strong></td>
<% if (contentField2) { %>
        <td align='center'><strong><i18n:message key="common.highlight_content"/> 2</td>
<% }
   if (contentField3) {%>
        <td align='center'><strong><i18n:message key="common.highlight_content"/> 3</td>
<% } %>  
        <td align='center'><strong><i18n:message key="common.status"/></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_date"/></strong></td>
<%      if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {%>
		<td align='center'><strong><i18n:message key="common.company"/></strong></td><% } %>
        <td align='center'><strong><i18n:message key="common.request_by"/></strong></td>
        <td align='center'><strong><i18n:message key="common.processed_by"/></strong></td>
        <td align='center'><strong><i18n:message key="common.remaining"/></strong></td>
                    
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
              <td ><%=vo.getRequestNo()%></td>
              <td ><%=vo.getFormName()%></td>
              <td ><%=vo.getHighlightContent()%></td>
<% if (contentField2) { %>
              <td><%=vo.getHighlightContent2()%></td>
<% }
   if (contentField3) {%>
              <td><%=vo.getHighlightContent3()%></td>
<% } %>                  
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%></td>
              <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%></td>
              <%
              StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(vo.getRequestStaffCode());
              String orgName = requester == null ? "" : CompanyHelper.getInstance().getOrgName(requester.getOrgId());
              
   if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {%>              
              <td><%=orgName%></td>
<% } %>              
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%></td>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%></td>              
               <td align='right'>
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
              </td>                       
            </tr>
          <%
            i++;}
          }%>
       </table>
   </body>
</html>