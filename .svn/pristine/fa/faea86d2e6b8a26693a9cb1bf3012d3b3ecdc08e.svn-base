<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO,com.aiait.eflow.report.vo.OverdueSummaryVO,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.StaffTeamHelper" %>
<html>
<head>
<title>Overdue Summary Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/graphic.js"></script>

<script language="javascript">
  var staffs = new Array();
  var forms = new Array();
  var hours = new Array();
  var nodeName;
  var xmlhttp = createXMLHttpRequest();
  var type;
  var bFlag = true;
  
  function getOptionList(typeValue,paramTwo){

    type = typeValue;
    var url = "";
    if(typeValue=="formList"){
      //url = "<%=request.getContextPath()%>/overdueSummaryAction.it?method=getFormList&formType="+paramTwo;
      url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+paramTwo;
    }else if(typeValue=="nodeList"){
      url = "<%=request.getContextPath()%>/overdueSummaryAction.it?method=getNodeList&formSystemId="+paramTwo;
      if(paramTwo==""){
        clearSelect(document.getElementById("nodeId"));
        return;
      }
    }
    xmlhttp.open("GET", url, true);
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
  }
  
  function handleStateChange(){
     if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          updateList();
        }
     }
  }
  function updateList(){
    var select;
    if(type=="formList"){
      select = document.getElementById("formSystemId");
    }else{
       select = document.getElementById("nodeId");
    }
    //first,remove all old options
    clearSelect(select);
    //alert(xmlhttp.responseText);
    var options = xmlhttp.responseXML.getElementsByTagName("option");
    for (var i = 0, n = options.length; i < n; i++) {
        select.appendChild(createElementWithValue(options[i]));
    }
    if(type=="formList"){
      getOptionList("nodeList",document.all.formSystemId.value)
      bFlag = false;
    }
  }
  function createElementWithValue(text) {
      var element = document.createElement("option");
      element.setAttribute("value", text.getAttribute("id"));
      var text = document.createTextNode(text.firstChild.nodeValue);
      element.appendChild(text);
      return element;
  }
  
  function clearSelect(obj){
    var count = obj.options.length;
    for(var i=0;i<count;i++){
      obj.options.remove(0);
    }
  }
  
  function submitForm(){
  	if(document.all.formSystemId.value==""){
       alert("Please select the Form");
       return;
    }
    if(document.all.nodeId.value==""){
       alert("Please select the Handle Node");
       return;
    }
    
    if(myForm.optList1[0].checked==false &&
    	myForm.optList1[1].checked==false){
	    	alert("Please select the Result Option");
	      	return;
	}
    
    if(myForm.optList2[0].checked==false &&
    	myForm.optList2[1].checked==false){
	    	alert("Please select the Result Mode");
	      	return;
	}
   	
   	document.getElementById('beginDateStr').value=document.getElementById('beginDateStr').value.Trim();
	document.getElementById('endDateStr').value=document.getElementById('endDateStr').value.Trim();
	if(new Date(document.getElementById('beginDateStr').value) > new Date(document.getElementById('endDateStr').value))
	{
		alert("Begin Date must be earlier or equal to End Date!");
		document.getElementById('endDateStr').focus();
		return;
	}
   	
    document.forms[0].submit();
  }
  
</script>
<body>
<%
  String formType = (String)request.getParameter("formType");
  String formSystemId = (String)request.getParameter("formSystemId");
  String nodeId = (String)request.getParameter("nodeId");
  Collection formList = (ArrayList)request.getAttribute("formList");
  Collection resultList = (ArrayList)request.getAttribute("resultList");
  Collection nodeList = (ArrayList)request.getAttribute("nodeList");
%>
<FORM name=myForm method="post" action="<%=request.getContextPath()%>/overdueSummaryAction.it?method=overdueSummary"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0" id="table0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_overduesumreport.location"/></font></strong>
 	 </td>
 </tr>
--></table>
<TABLE id="table1" WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="report_overduesumreport.title"/></TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.formtype"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
        <select name="formType" onchange="getOptionList('formList',this.value)">
          <option value=""></option>
          <option value="01" <%=(formType!=null && CommonName.FORM_TYPE_TS.equals(formType))?"selected" : ""%>>TS Form</option>
          <option value="02" <%=(formType!=null && CommonName.FORM_TYPE_ADMIN.equals(formType))?"selected" : ""%>>Admin Form</option>
          <option value="03" <%=(formType!=null && CommonName.FORM_TYPE_ACCOUNT.equals(formType))?"selected" : ""%>>Account Form</option>
          <option value="04" <%=(formType!=null && CommonName.FORM_TYPE_HR.equals(formType))?"selected" : ""%>>HR Form</option>
        </select>
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.form"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <select name="formSystemId" onchange="getOptionList('nodeList',this.value)">
        <% 
           if(formList!=null && formList.size()>0){
        	   Iterator formIt = formList.iterator();
        	   while(formIt.hasNext()){
        		   FormManageVO form = (FormManageVO)formIt.next();
        		   out.print("<option value='" + form.getFormSystemId()+"'");
        		   if(formSystemId!=null && !"".equals(formSystemId) && formSystemId.equals(""+form.getFormSystemId())){
        			   out.print(" selected ");
        		   }
        		   out.print(">"+form.getFormId() + " - " + form.getFormName()+"</option>");
               }
         }%>
       </select>
      </TD>
    </TR>
   <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.processednode"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
        <select name="nodeId">
 
         <% 
           if(nodeList!=null && nodeList.size()>0){
        	   Iterator nodeIt = nodeList.iterator();
        	   while(nodeIt.hasNext()){
        		   WorkFlowItemVO node = (WorkFlowItemVO)nodeIt.next();   
        		   out.print("<option value='" + node.getItemId() + "'");
        		   if(nodeId!=null && !"".equals(nodeId) && nodeId.equals(""+node.getItemId())){
        			   out.print(" selected ");
        		   }
        		   out.print(">"+node.getName()+"</option>");
               } 
        	   %>
        	   		<script>
        	   			var i;
        	   			var nodeSize='<%=nodeList.size()%>';
        	   			for(i = 0; i < nodeSize; i++)
        	   			{
        	   				if(myForm.nodeId[i].selected == true)
        	   				{
        	   					nodeName = myForm.nodeId[i].text;
        	   				}
        	   			}
        	   		</script>
        	   <%
         }%>
        </select>
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.resultoption"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
      <%
          String optList1 = request.getParameter("optList1");
      %>
        <input type="radio" name="optList1" value="1" <%= optList1 == null ? "checked" : "1".equals(optList1) ? "checked" : "" %>><i18n:message key="report_overduesumreport.overdueforms"/>
		<input type="radio" name="optList1" value="2" <%= "2".equals(optList1) ? "checked" : "" %>><i18n:message key="report_overduesumreport.overduehours"/>
      </TD>
    </TR>
    <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.processedfrom"/></span></div></TD>
      <TD width=35% height="20" > 
        <input type="text" readonly name="beginDateStr" onclick='setday(this)' value="<%=((String)request.getParameter("beginDateStr")==null || "".equals((String)request.getParameter("beginDateStr"))?"":(String)request.getParameter("beginDateStr"))%>">(MM/DD/YYYY)
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.processedto"/></span></div></TD>
      <TD width=30% height="20" >
        <input type="text" readonly name="endDateStr" onclick='setday(this)' value="<%=((String)request.getParameter("endDateStr")==null || "".equals((String)request.getParameter("endDateStr"))?"":(String)request.getParameter("endDateStr"))%>">(MM/DD/YYYY)
      </TD>
    </TR>
    <tr>
    	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_overduesumreport.displaymode"/></span></div></TD>
      	<TD width=30% height="20" colspan = 3><font size="2"> 
        <%
            String optList2 = request.getParameter("optList2");
        %>
	        <input type="radio" name="optList2" value="1" <%= optList2 == null ? "checked" : "1".equals(optList2) ? "checked" : "" %>><i18n:message key="report_overduesumreport.tablemode"/>
			<input type="radio" name="optList2" value="2" <%= "2".equals(optList2) ? "checked" : "" %>><i18n:message key="report_overduesumreport.graphicmode"/>
      	</TD>
    </tr>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="generateBtn" value='<i18n:message key="report_overduesumreport.generate"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
</FORM>

<%	//form -- table
	if("1".equals((String)request.getParameter("optList1")) &&
			"1".equals((String)request.getParameter("optList2")))
	{
	%>
		<table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
			<tr class="liebiao_tou">
	            <td align='center'>Staff Name</td>
	            <td align='center'>Overdue Forms</td>
          	</tr>	
	<%
		if(resultList!=null)
		{
			int i = 1;
        	Iterator resultIt = resultList.iterator();
        	System.out.println(resultList.size());
       		 while(resultIt.hasNext())
       		 {
        		OverdueSummaryVO vo = (OverdueSummaryVO)resultIt.next();
        %>
        		<tr class="<%=(i%2==0?"liebiao_nr3":"liebiao_nr2")%>">             
	              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode())%>&nbsp;&nbsp;</td>
	              <td><%=vo.getOverdueForms()%>&nbsp;&nbsp;</td>            
            	</tr>
        <%
            	i++;
        	}
		}
		else
		{
		%>
			<tr class="liebiao_nr2">
	            <td>&nbsp;&nbsp;</td>
	            <td>&nbsp;&nbsp;</td>
         	</tr>
        <%
		}
	%>
		</table>
	<%
	}//hours -- table
	else if("2".equals((String)request.getParameter("optList1")) &&
			"1".equals((String)request.getParameter("optList2")))
	{
	%>
		<table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
			<tr class="liebiao_tou">
	            <td align='center'>Staff Name</td>
	            <td align='center'>Overdue Hours</td>
          	</tr>
	<%
		if(resultList!=null)
		{
			int i = 1;
        	Iterator resultIt = resultList.iterator();
        	System.out.println(resultList.size());
       		 while(resultIt.hasNext())
       		 {
        		OverdueSummaryVO vo = (OverdueSummaryVO)resultIt.next();
        %>
        		<tr class="<%=(i%2==0?"liebiao_nr3":"liebiao_nr2")%>">             
	              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode())%>&nbsp;&nbsp;</td>
	              <td><%=Math.abs(vo.getOverdueHours())%>&nbsp;&nbsp;</td>            
            	</tr>
        <%
            	i++;
        	}
		}
		else
		{
		%>
			<tr class="liebiao_nr2">
	            <td>&nbsp;&nbsp;</td>
	            <td>&nbsp;&nbsp;</td>
	     	</tr>
	    <%
		}
	%>
		</table>
	<%
	}//forms -- graphic
	else if("1".equals((String)request.getParameter("optList1")) &&
			"2".equals((String)request.getParameter("optList2")))
	{
		System.out.println("-----entered graphic forms-----");	
	%>
		<div id=div1 style="position:absolute;top:220;left:130;cursor:default;text-align:center">
		</div>
		<script>
			var z=0;

		 <%
		 	int i = 1, j;
	    	Iterator resultIt = resultList.iterator();
	    	
	    	for(j=0;resultIt.hasNext();j++)
	  		{
	   			OverdueSummaryVO vo = (OverdueSummaryVO)resultIt.next();
	   			System.out.println(StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode()));
	   			System.out.println(vo.getOverdueForms());
	   			%>
	   				staffs[z]='<%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode())%>';
	   			  	forms[z]='<%=vo.getOverdueForms()%>';
	   			  	z++;
	   			<%
	   			i++;
	  		}
		 %>
		 	if(staffs[0]==null || forms[0]==null)
		 	{
		 		alert("There is no data in this form. Please choose another one!");
		 	}
		 	else
		 	{
			 	var exc1 = "Node,";
			    for(var i=0; i < staffs.length; i++)
			    {
			      exc1 = exc1 + staffs[i] + ",";
			    }
			    exc1 = exc1.substring(0, exc1.length-1);
			    exc1 = exc1 + ";";
			    
			    var exc2 = " " + nodeName + ",";
			    for(var i=0; i < staffs.length; i++)
			    {
			      exc2 = exc2 + forms[i] + ",";
			    }
			    exc2 = exc2.substring(0, exc2.length-1);
			    exc2 = exc2 + ";";
			    
			    var exc3 = "type:bar; showVal:t; vname:From; title:Overdue Summary";
			   	div1.innerHTML=ct1.draw(exc1+exc2, exc3);
			}
		</script>
	<%
	}//hours -- graphic
	else if("2".equals((String)request.getParameter("optList1")) &&
			"2".equals((String)request.getParameter("optList2")))
	{
		System.out.println("-----entered graphic hours-----");	
	%>
		<div id=div1 style="position:absolute;top:220;left:130;cursor:default;text-align:center">
		</div>
		<script>
			var z=0;

		 <%
		 	int i = 1, j;
	    	Iterator resultIt = resultList.iterator();
	    	
	    	for(j=0;resultIt.hasNext();j++)
	  		{
	   			OverdueSummaryVO vo = (OverdueSummaryVO)resultIt.next();
	   			System.out.println(StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode()));
	   			System.out.println(vo.getOverdueHours());
	   			%>
	   				staffs[z]='<%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getHandleStaffCode())%>';
	   			  	hours[z]='<%=vo.getOverdueHours()%>';
	   			  	z++;
	   			<%
	   			i++;
	  		}
		 %>
		 	if(staffs[0] == null || hours[0] == null)
		 	{
		 		alert("There is no data in this form. Please choose another one!");
		 	}
		 	else
		 	{
			 	var exc1 = "Node,";
			    for(var i=0; i < staffs.length; i++)
			    {
			      exc1 = exc1 + staffs[i] + ",";
			    }
			    exc1 = exc1.substring(0, exc1.length-1);
			    exc1 = exc1 + ";";
			    
			    var exc2 = " " + nodeName + ",";
			    for(var i=0; i < staffs.length; i++)
			    {
			      exc2 = exc2 + Math.abs(hours[i]) + ",";
			    }
			    exc2 = exc2.substring(0, exc2.length-1);
			    exc2 = exc2 + ";";
			    
			    var exc3 = "type:bar; showVal:t; vname:Hour; title:Overdue Summary";
			   	div1.innerHTML=ct1.draw(exc1+exc2, exc3);
			}
		</script>
	<%
	}
%>
</body>
</html>