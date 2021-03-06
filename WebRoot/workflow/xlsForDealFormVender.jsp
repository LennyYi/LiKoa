<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@page import="com.aiait.framework.i18n.*"%>
<html>
<head>
<title>Wait For Deal Form List</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=a.xls">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%  Collection formList = (ArrayList)request.getAttribute("dealFormList"); 
response.setContentType("application/vnd.ms-excel;charset=GBK");%>
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%"  border="1" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
      <tr class="liebiao_tou">     
        <td align='center'><strong><i18n:message key="common.request_no"/></strong></td>
        <td align='center'><strong>Vender</strong></td>
        <!--<td align='center'><strong><i18n:message key="common.form_type"/></strong></td> -->
        <td align='center'><strong><i18n:message key="report_invoicereport.amount"/></strong></td>
        <td align='center'><strong><i18n:message key="common.status"/></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_date"/></strong></td>
        <td align='center'><strong><i18n:message key="common.request_by"/></strong></td>
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
              <td ><%=vo.getCcStaffs()%></td>
              <!--<td><%=FormTypeHelper.getInstance().getFormTypeName(vo.getFormType())%></td> -->
              <td ><%=vo.getHighlightContent(true)%></td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%></td>
              <td><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%></td>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%></td>
            </tr>
          <%
            i++;}
          }%>
       </table>
   </body>
</html>