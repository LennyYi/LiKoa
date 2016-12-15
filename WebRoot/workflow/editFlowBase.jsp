<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.formmanage.vo.FormManageVO"%>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO,java.util.*" %>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*" %>
<HTML>
  <HEAD>
		<title>Flow Properties Setting</title>
  </HEAD>
   <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">
  
    function submitNode(){
      if(formValidate(document.forms[0])==false) return;
      
	  var url = "<%=request.getContextPath()%>/wkfDesignAction.it?method=saveFlowBaseInfor&saveType="+document.all['saveType'].value
	            +"&flowId="+document.all['flowId'].value + "&flowName=" + encodeURI(encodeURI(document.all['flowName'].value))
	            +"&description="+encodeURI(encodeURI(document.all['description'].value))+"&formSystemId="+document.all['formSystemId'].value
	            +"&afterHandleUrl="+document.all['afterHandleUrl'].value+"&orgId="
                 +document.all['orgId'].value;
	  var  xmlhttp = createXMLHttpRequest();
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
          alert('Work Flow save successfully');  
       }else{
          alert(result);
          return;
       }
       window.opener.location.reload();
       window.close();
	}
	 window.onload=function(){resize(560);}
  </script>
 <%
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
   String saveType = (String)request.getParameter("saveType");
   if(saveType==null || "".equals(saveType)){
	   saveType = "add";
   }
   WorkFlowVO flow = (WorkFlowVO)request.getAttribute("flow");
   Collection formList = (ArrayList)request.getAttribute("formList");
 %>
<BODY>
 <form id="Form1" method="post">
    <input type="hidden" name="saveType" value="<%=saveType%>">
   <input type="hidden" name="flowId" value="<%="edit".equals(saveType)?""+flow.getFlowBaseId():""%>">
 <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
	<TR>
		<TD style="WIDTH:30%" noWrap align='right' class="tr3">
			<i18n:message key="flow_design.flowname"/> :
		</TD>
		<TD style="WIDTH:70%" noWrap>
		  <INPUT id="flowName" required="true" title="Flow Name" type="text" value="<%="edit".equals(saveType)?flow.getFlowName():""%>">(<font color='red'>*</font>)
		</TD>
	</TR>
    <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="common.company"/> :
       </td>
       <td style="WIDTH:70%">
         <select name="orgId">
           <%
            Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(flow!=null && vo.getOrgId().equals(flow.getOrgId())){
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
    <TR>
		<TD style="WIDTH:30%" noWrap align='right' class="tr3">
			<i18n:message key="flow_design.description"/> :
		</TD>
		<TD style="WIDTH:70%" noWrap>
		  <INPUT id="description" type="text" value="<%="edit".equals(saveType)?flow.getDescription():""%>" size='30'>
		</TD>
	</TR>						
    <TR>
		<TD style="WIDTH:30%" noWrap align='right' class="tr3">
			<i18n:message key="flow_design.associatedform"/> :
		</TD>
		<TD style="WIDTH:70%" noWrap>
		    <select name="formSystemId">
							<option value="0">   </option>
							<% 
								if(formList!=null && formList.size()>0){
								    Iterator it = formList.iterator();	
								    while(it.hasNext()){
								         FormManageVO form = (FormManageVO)it.next();
								         out.println("<option value='" + form.getFormSystemId()+"'");
								         if("edit".equals(saveType) && form.getFormSystemId()==flow.getFormSystemId()){
								             out.print("selected");
								         }
								         out.print(">" + form.getFormId() + " - " + form.getFormName() +"</option>");
								     }
								 }
							%>
				</select>
			</TD>
		</TR>
      <tr>
       <td style="WIDTH:30%" align='right' class="tr3">
         <i18n:message key="flow_design_addflow.opafterhandle"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows='3' cols='40' name="afterHandleUrl"><%=(flow!=null && flow.getAfterHandleUrl()!=null && !"".equals(flow.getAfterHandleUrl()))?flow.getAfterHandleUrl():""%></textarea>
       </td>
     </tr>						
       <TR>
           <TD style="WIDTH: 100%" align='center' colspan='2'>
               <INPUT id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' language="javascript"onclick="submitNode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
               &nbsp;&nbsp;
			   <input type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
		   </TD>
	  </TR>
 </table>
</form>
</BODY>
</HTML>
