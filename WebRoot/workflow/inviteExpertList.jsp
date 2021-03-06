<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.ParamConfigHelper,com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.common.CommonName"%>
<%
  String[] selectedExpertList = (String[])request.getAttribute("selectedExpertList");
  String requestNo = (String)request.getParameter("requestNo");
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
			CommonName.CURRENT_STAFF_INFOR);
	
  Iterator teamIt=null;		
  Collection teamList = StaffTeamHelper.getInstance().getTeamList();
  if(teamList!=null){
	teamIt = teamList.iterator();
  }	
  
  String sInviteSelf = ParamConfigHelper.getInstance().getParamValue("invite_expert_self", "false");
  boolean inviteSelf = false;
  if("true".equalsIgnoreCase(sInviteSelf) || "yes".equalsIgnoreCase(sInviteSelf)){
	  inviteSelf = true;
  }
%>
<HTML>
  <HEAD>
		<title>Select advisers</title>
  </HEAD>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
   <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<STYLE type='text/css'>
.multipleSelectBoxControl SPAN {
	FONT-WEIGHT: bold; FONT-SIZE: 11px; FONT-FAMILY: arial
}
.multipleSelectBoxControl DIV SELECT {
	FONT-FAMILY: arial; HEIGHT: 100%
}
.multipleSelectBoxControl INPUT {
	WIDTH: 25px
}
.multipleSelectBoxControl DIV {
	FLOAT: left
}
.multipleSelectBoxDiv {
	
}
FIELDSET {
	MARGIN: 10px; WIDTH: 500px
}
</STYLE>
<script language="javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
    function submitStaff(){
        var obj = document.getElementById('toBox[]');
        var selectIdStr = "";
		for(var no=0;no<obj.options.length;no++){
			//obj.options[no].selected = true;
			selectIdStr = selectIdStr + obj.options[no].value + ",";
		}
		if(selectIdStr==""){
          alert("You must select one staff at least!");
          return;
        }
		var comments = "";
		if(document.all['reason'].value!=""){
		   comments = formatStringAllChar(document.all['reason'].value);
		}
        
        var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=saveSelectedExpert&requestNo=<%=requestNo%>&expertList="
        		+ selectIdStr + "&reason=" + encodeURI(encodeURI(comments));
        document.forms[0].action = url;                
        document.forms[0].submit();
        setTimeout("window.opener.location.reload();alert('Success');window.close();", 500);
        //window.close();
        /*var param = "reason="+comments;
        
      	param = encodeURI(encodeURI(param)); //
        var myAjax = new Ajax.Request(
         url,
        {
            method:"get",       //
            parameters:param,   //
            asynchronous:false,  //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
                 result = x.responseText;
                 if(result.Trim()=="success"){    
                    alert('It is successful to invite the selected advisers.');
                    window.opener.location.reload();
                 }else{
                    alert(result.Trim());
                    return;
                 }
	             window.close();	
            },
            onError:function(x){          //
                 alert(x.responseText);
            } 
        } 
       );*/ 
	}

	function ie_UpdateTeamList(_orgid, isInit){
    	var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";
    	var param = "orgId="+_orgid +"&enableSearchAll=true";
     	
     	var myAjax = new Ajax.Request(
         url,
         {
            method:"get",       //
            parameters:param,   //
            asynchronous:false,  //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
				    var select;
				    select = document.getElementById("teamCode");
				    clearSelect(select);
				    select.appendChild(createOptionEle("All", "", true));
				    var options = x.responseXML.getElementsByTagName("team");
				    for (var i = 0, n = options.length; i < n; i++) {
				    	select.appendChild(createElementWithValue(options[i],"code"));
				    	
//				    	if(("<=currentStaff.getTeamCode()>" == options[i].getAttribute("code")) && (_orgid != "")){
//				    		select.appendChild(createElementWithValue(options[i],"code", true));
//					    }else{
//					    	select.appendChild(createElementWithValue(options[i],"code"));
//					    }							   
				    }

				    document.getElementById("staffName").value = "";
					if(!isInit){
				    	searchStaffList();//To improve loading speed, replace the search with a java loop filter by team_code
					}
            },
            onError:function(x){          //
                    alert('Fail to get team list for company');
            }
         }
       ); 
  	}  		

  	function doSearchStaffList(staffName, teamCode, orgId){

  		document.getElementById("searchBtn").disable = true;
  		document.getElementById("resetBtn").disable = true;
  		
   		var url = "<%=request.getContextPath()%>/userManageAction.it?method=searchStaffByName";
    	var param = "staffName="+staffName+"&teamCode="+teamCode+"&orgId="+orgId;
     	
     	var myAjax = new Ajax.Request(
         url,
         {
            method:"get",       //
            parameters:param,   //
            asynchronous:false,  //
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
				    var select;
				    select = document.getElementById("fromBox");
				    clearSelect(select);
				    var options = x.responseXML.getElementsByTagName("staff");
				    for (var i = 0, n = options.length; i < n; i++) {
				    	if("<%=currentStaff.getStaffCode()%>" == options[i].getAttribute("code")){
					    	if(<%=inviteSelf%> ){
						    	if(getInvitedStaffs().indexOf(options[i].getAttribute("code")+",") < 0){
					        		select.appendChild(createElementWithValue(options[i],"code"));
						    	}
					    	}
					    }else{
					    	if(getInvitedStaffs().indexOf(options[i].getAttribute("code")+",") < 0){
				        		select.appendChild(createElementWithValue(options[i],"code"));
					    	}
					    }		
				    }
				    select.style.width = '200px';
			 		document.getElementById("searchBtn").disable = false;
			  		document.getElementById("resetBtn").disable = false;
            },
            onError:function(x){          //
                    alert('Fail to get staff list for the specified staff name');
             		document.getElementById("searchBtn").disable = false;
              		document.getElementById("resetBtn").disable = false;
            }
         }
       );
  	}		

	function searchStaffList(){
	
		var staffName = document.getElementById("staffName").value;
		var teamCode = document.getElementById("teamCode").value;
		var orgId = document.getElementById("orgId").value;

		doSearchStaffList(staffName, teamCode,orgId);
  	}

  	function getInvitedStaffs(){
  	  	var invitedStaffs = "";
  	  	var tmpToBox = document.getElementById("toBox[]");
		for(var no=0;no<tmpToBox.options.length;no++){
			invitedStaffs += tmpToBox.options[no].value+",";
		}
		return  invitedStaffs;
	}

	function init(){
		document.getElementById("orgId").value = "<%=currentStaff.getOrgId()%>";
		ie_UpdateTeamList("<%=currentStaff.getOrgId()%>", true);
		//todo to delay to 
		document.getElementById("staffName").value = "";
		//searchStaffList();
  	}  	

  window.onload=function(){
	  resize(500);
	  init();
  }
  </script>
<BODY>
 <form enctype="multipart/form-data" id="myForm" method="post">
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
       <TD align='right' width="80px"><i18n:message key="common.company"/></TD><TD align='left'><select id="orgId" onChange="ie_UpdateTeamList(this.value, false)">
           <%
           out.print("<option value=''>All</option>");
            //Collection companyList = currentStaff.getOwnCompanyList(); 
           Collection companyList = CompanyHelper.getInstance().getCompanyList();
            if(companyList!=null && companyList.size()>0){
            	Iterator it = companyList.iterator();
            	while(it.hasNext()){
            	   CompanyVO vo = (CompanyVO)it.next();
            	   if(vo.getOrgId() != null && vo.getOrgId().equals(currentStaff.getOrgId())){
	           	     out.print("<option selected value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }else{
            		 out.print("<option value='"+vo.getOrgId()+"'>" + vo.getOrgName()+"</option>");
            	   }
                }
            }
           %>
         </select>
      </TD>
   </TR>
   <TR>
    <TD align='right' width="80px"><i18n:message key="housekeeping_user.team"/></TD><TD align='left'><select id="teamCode" onchange="document.getElementById('staffName').value = '';searchStaffList();">
         </select>    
	</TD>
   </TR>    
   <TR>
    <TD align='right' width="80px"><i18n:message key="housekeeping_user.staffname"/></TD><TD align='left'><input type='text' id='staffName' size='30' maxLength='50' value='' />
    </TD>
   </TR> 
   <TR>
    <TD align='center' colspan="2">
    <input type="button" id="searchBtn" value='<i18n:message key="button.search"/>' onclick="searchStaffList();" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	&nbsp;&nbsp;
	<input type="button" id="resetBtn" value='<i18n:message key="button.reset"/>' onclick="init();" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           		onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
    </TD>
   </TR>    
   </table>
   <br>
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
	 <TD style="WIDTH: 323px; HEIGHT: 21px" colspan="2" nowrap >
		<SELECT id="fromBox" name="fromBox" multiple >
        <%
         Collection staffList = StaffTeamHelper.getInstance().getStaffList();
         if(staffList!=null && staffList.size()>0){
           Iterator staffIt = staffList.iterator();
           boolean selectedFlag = false;
           while(staffIt.hasNext()){
        	  StaffVO staff = (StaffVO)staffIt.next();
        	  if(!staff.getOrgId().equals(currentStaff.getOrgId()))continue;
        	  if(selectedExpertList!=null && selectedExpertList.length>0){
        		  for(int i=0;i<selectedExpertList.length;i++){
        			  if(selectedExpertList[i].equals(staff.getStaffCode())){
        				  selectedFlag = true;
        				  break;
        			  }
        		  }
        		  if(staff.getStaffCode().equals(currentStaff.getStaffCode())){
        			  selectedFlag = true;
        		  }
        		  if(selectedFlag==false){
        			  out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
        		  }
        		  selectedFlag=false;
        	  }else{
        		  if(staff.getStaffCode().equals(currentStaff.getStaffCode())){
        			 if(inviteSelf){
        				 out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
        			 }
        		  }else{
        			  out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>"); 
        		  }
        	  }
        	}
         }
        %>
	   </SELECT>
		<SELECT id='toBox[]' name='toBox[]' multiple >
         <%
          if(selectedExpertList!=null && selectedExpertList.length>0){
        	for(int i=0;i<selectedExpertList.length;i++){
        		StaffVO staff = StaffTeamHelper.getInstance().getStaffByCode(selectedExpertList[i]);
        		out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
        	}
         }
        %>
		</SELECT>
		<SCRIPT type='text/javascript'>
           createMovableOptions("fromBox","toBox[]",450,300,'Staff List','Selected advisers');
       </SCRIPT>
	 </TD>
   </TR>
   <TR>
     <TD align='right' valign="middle" width="80px">
       Invited reason: 
     </TD>
     <TD align='left' valign="middle">
       <textarea name="reason" rows='4' cols='70' onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea>
     </TD>
   </TR>
   <TR>
     <TD width="80px"><div align="right"><span class="style1"><i18n:message key="flow_auditpro.attachment"/></span></div></TD>
     <TD><font size="2"><input type="file" name="path" size='20'></font></TD>
   </TR>
  <TR>
    <TD align='center' colspan="2">
        <input id="btnSelect"  type="button" value="Submit" onclick="javascript:submitStaff()" class="btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		<input name="btnClose" type="button" onclick="javascript:window.close();" value="Close" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	</TD>
  </TR>
 </TABLE>

</form>
</BODY>
</HTML>