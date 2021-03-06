<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.RoleVO,com.aiait.eflow.common.*" %>

<%
   Collection noRoleList = (ArrayList)request.getAttribute("noRoleList");
   Collection hasRoleList = (ArrayList)request.getAttribute("hasRoleList");
   String staffCode = (String)request.getParameter("staffCode");
%>

<HTML>
  <HEAD>
		<title>Set Roles for staff</title>
  </HEAD>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
   <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
       <link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/loading.css" />
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
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">
    function submitSelect(){
        var obj = document.getElementById('toBox[]');
        var selectIdStr = "";
		for(var no=0;no<obj.options.length;no++){
			//obj.options[no].selected = true;
			selectIdStr = selectIdStr +"&roleId=" +obj.options[no].value ;
		}
        if(selectIdStr==""){
          alert(setroleforstaff_sel_one_role);
          return;
        }
        /*if(obj.options.length>1){
          alert(setroleforstaff_one_role);
          return;
        }*/
        if(confirm(setroleforstaff_sure_to_save)){
         var url = "<%=request.getContextPath()%>/userManageAction.it?method=saveRoleSetting&staffCode=<%=staffCode%>";
            if(selectIdStr!=""){
              url = url + selectIdStr;
            }
             var  xmlhttp = createXMLHttpRequest();
             var result;
             if(xmlhttp){
               xmlhttp.open('GET',url,true);
               xmlhttp.onreadystatechange = function()
              {
                if(xmlHttp.readyState!=4){
             	   document.getElementById("loading").style.display="block";
                 }
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                   result = xmlhttp.responseText;
                   document.getElementById("loading").style.display="none";
                   if(result.Trim()=="success"){
                      alert(setroleforstaff_suc_set_rolestaff)
                      window.close();
                   }else if(result.Trim()=="fail"){
                      alert(setroleforstaff_fail_toset);
                   }
                }
              }
               xmlhttp.setRequestHeader("If-Modified-Since","0");
               xmlhttp.send(null);
            }
         }
   
	 }	
	window.onload=function(){resize(500);}
  </script>
<BODY>
 <form id="Form1" method="post">
      <div id="loading" style="display:none;top:250px; left:40px;">
	   <div class="loading-indicator">
		 It is processing...
	   </div>
     </div>
<table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <TR>
	 <TD style="WIDTH: 323px; HEIGHT: 21px" noWrap>
		<SELECT id=fromBox multiple name=fromBox>
        <%
         if(noRoleList!=null && noRoleList.size()>0){
           Iterator roleIt = noRoleList.iterator();
           while(roleIt.hasNext()){
        	  RoleVO role = (RoleVO)roleIt.next();
        %>
          <option value="<%=role.getRoleId()%>"><%=role.getRoleName()%></option>
        <%
        	}
         }
        %>
	   </SELECT>
		<SELECT id=toBox[] multiple name=toBox[] multiple>
         <%
          if(hasRoleList!=null && hasRoleList.size()>0){
        	 Iterator it = hasRoleList.iterator();
        	 while(it.hasNext()){
        		 RoleVO role = (RoleVO)it.next();
        %>
             <option value="<%=role.getRoleId()%>"><%=role.getRoleName()%></option>
        <%
        	 }
         }
        %>
		</SELECT>
		<SCRIPT type=text/javascript>
           createMovableOptions("fromBox","toBox[]",450,300,house_user_rolelist,house_user_selectedlist);
       </SCRIPT>
	 </TD>
   </TR>
  <TR>
    <TD  align='center'>
        <input id="btnSelect"  type="button" value='<i18n:message key="button.submit"/>' onclick="submitSelect()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
		<input name="btnClose" type="button" onclick="javascript:window.close();" value='<i18n:message key="button.close"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
	</TD>
  </TR>
 </TABLE>
</form>
</BODY>
</HTML>

