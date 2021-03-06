<!-- 
No Task_ID	Author	Modify_Date	Description
1  IT0958   Young	 11/01/2007 DS-012 Assign node type to workflow node
2  IT0958   Robin    11/02/2007 DS-004 Add a full name property for node
 -->
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*"%>
<%@page import="com.aiait.eflow.common.CommonName, com.aiait.eflow.common.helper.*, com.aiait.framework.i18n.*;" %>
<%
  String pageTitle = (String)request.getParameter("pageTitle");
  Collection staffList = (ArrayList)request.getAttribute("staffList");
  String[] selectedStaffs = null;
  if(request.getParameter("selectedStaffs")!=null)selectedStaffs = ((String)request.getParameter("selectedStaffs")).split(",");
  if(pageTitle==null){
	  pageTitle = "E-Flow Select staff";
  }
  
  StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(
			CommonName.CURRENT_STAFF_INFOR);
  
%>
<HTML>
  <HEAD>
		<title><%=pageTitle%></title>	
  </HEAD>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
   <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<STYLE type=text/css>
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
    function submitNode(){
        var obj = document.getElementById('toBox[]');
        var selectIdStr = "";
        var selectNameStr = "";
		for(var no=0;no<obj.options.length;no++){
			//obj.options[no].selected = true;
			selectIdStr = selectIdStr + obj.options[no].value + ",";
			selectNameStr = selectNameStr + obj.options[no].innerText + ", ";
		}
		if(selectNameStr.length>0)selectNameStr=selectNameStr.substring(0,selectNameStr.length-4);
		
        if(selectIdStr==""){
          alert(selectstaff_sel_one_staff);
          return;
        }
        if(confirm(selectstaff_sure_to_sel)){
          selectIdStr = selectIdStr.substring(0,selectIdStr.length-1)
          var returnValue = selectIdStr;
          try{
        	  window.dialogArguments.receiveNames(selectNameStr);
          }catch(e){}
          if(selectIdStr.substring(0,1)==",")selectIdStr = selectIdStr.substring(1);
          window.returnValue = selectIdStr;
	      window.close();
	    }	
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
					    
//			    	if(("<=currentStaff.getTeamCode()%>" == options[i].getAttribute("code")) && (_orgid != "")){
//				    		select.appendChild(createElementWithValue(options[i],"code", true));
//				    }else{
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
				    	if(getToBoxValues().indexOf(options[i].getAttribute("code")+",") < 0){
			        		select.appendChild(createElementWithValue(options[i],"code"));
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

  	function getToBoxValues(){
  	  	var toboxValues = "";
  	  	var tmpToBox = document.getElementById("toBox[]");
		for(var no=0;no<tmpToBox.options.length;no++){
			toboxValues += tmpToBox.options[no].value+",";
		}
		return  toboxValues;
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
 <form id="Form1" method="post">
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
    <TD align='right' width="80px"><i18n:message key="housekeeping_user.team"/></TD><TD align='left'><select id="teamCode" onchange="document.getElementById('staffName').value = '';searchStaffList();" >
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
   <TR>
    <TD align='left' colspan=2>
    <font color="#ff0000"><b><%=request.getParameter("showTips")!=null?I18NMessageHelper.getMessage(request.getParameter("showTips")):""%>
    </b></font>   
	</TD>
   </TR>
   </table>
   <br> 
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
	 <TD style="WIDTH: 323px; HEIGHT: 21px" noWrap>
		<SELECT id=fromBox multiple name=fromBox>
        <%
         if(staffList!=null && staffList.size()>0){
           Iterator staffIt = staffList.iterator();
           while(staffIt.hasNext()){
        	  StaffVO staff = (StaffVO)staffIt.next();
        	  //if(!staff.getTeamCode().equals(currentStaff.getTeamCode()))continue;
        %>
          <option value="<%=staff.getStaffCode()%>"><%=staff.getStaffName()%></option>
        <%
        	}
         }
        %>
	   </SELECT>
		<SELECT id=toBox[] multiple name=toBox[] multiple>
		<%
         if(selectedStaffs!=null && selectedStaffs.length>0){
           for(int i =0;i<selectedStaffs.length;i++){
        	  String StaffName = StaffTeamHelper.getInstance().getStaffNameByCode(selectedStaffs[i]);
        %>
          <option value="<%=selectedStaffs[i]%>"><%=StaffName%></option>
        <%
        	}
         }
        %>
		</SELECT>
		<SCRIPT type=text/javascript>
           createMovableOptions("fromBox","toBox[]",450,300,selectstaff_userlist,selectstaff_seluser);
       </SCRIPT>
	 </TD>
   </TR>
  <TR>
    <TD  align='center'>
        <input id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' onclick="javascript:submitNode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		<input name="btnClose" type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	</TD>
  </TR>  
 </TABLE>
</form>
</BODY>
</HTML>
