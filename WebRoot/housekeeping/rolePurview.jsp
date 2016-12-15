<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page import="com.aiait.eflow.housekeeping.vo.ModuleVO,com.aiait.eflow.housekeeping.vo.ModuleOperateVO,java.util.*,com.aiait.eflow.common.CommonName,com.aiait.framework.i18n.I18NMessageHelper;"%>
<%
  String roleId = (String)request.getParameter("roleId");
  String roleName = (String)request.getParameter("roleName");
  Collection rolePurview = (ArrayList)request.getAttribute("rolePurviewList");
  ArrayList moduleList = (ArrayList)request.getAttribute("moduleList");
%>
<html>
<head>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel ="stylesheet" type="text/css" href="css/TreeView.css">
<script language="JavaScript" src="js/tv.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>Role Purview</title>
</head>
<script language="JavaScript">
   demo = new TreeView();
   demo.setImagePath("images/purview/");
  <%
      boolean isRoot = true;
      int parentId = 0;

      int treeSize = moduleList.size();
      int value = -10000;
      
      Iterator operateIter = null;
      
      for (int i = 0; i < treeSize; i++) {
         ModuleVO module = (ModuleVO)moduleList.get(i);
         isRoot = module.getParentId()==0?true:false;
         if(!isRoot){
           parentId = module.getParentId();
         }else{
           parentId = 0;
         }
         if(module.getOperateList()!=null && module.getOperateList().size()>0)
         {//I18NMessageHelper.getMessage(module.getModuleName().trim())
  %>			
           demo.add(<%=module.getModuleId()%>,<%=parentId%>,"<%=I18NMessageHelper.getMessage(module.getModuleName().trim())%>","javascript:void(0)",null,"checkall",null,true);
  <%
         }else{
  %>
           demo.add(<%=module.getModuleId()%>,<%=parentId%>,"<%=I18NMessageHelper.getMessage(module.getModuleName().trim())%>","javascript:void(0)",null,"checkall",null,false);
  <%
        }
      if(module.getOperateList()!=null){

       operateIter = module.getOperateList().iterator();
       while(operateIter.hasNext()){
           ModuleOperateVO operate = (ModuleOperateVO)operateIter.next();
           String valueStr = ""+ operate.getModuleId()+CommonName.MODULE_ROLE_SPLIT_SIGN+operate.getOperateId();
           if(rolePurview!=null && rolePurview.contains(valueStr)){//I18NMessageHelper.getMessage(operate.getOperateName().trim())
  %>
             demo.addcheck(<%=value++%>,<%=module.getModuleId()%>,"<%=operate.getOperateName().trim()%>","javascript:void(0)",null,"checkbox","<%=valueStr%>",true);
  <%
           }else{
  %>        
            demo.add(<%=value++%>,<%=module.getModuleId()%>,"<%=operate.getOperateName().trim()%>","javascript:void(0)",null,"checkbox","<%=valueStr%>",true);
   <%
           }
          }//end while
         }
        }//end for 
   %>         
</script>
<body>
<form name="roleForm" method="post">
<input type="hidden" name="roleId" value="<%=roleId%>">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <tr>
 	<td></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="1" cellpadding="0">
	<tr align="center">
		<td class="tr1"><i18n:message key="housekeeping_role.ausetting"/>-<%=roleName%></td>
	</tr>
</table>
<table border="0" cellspacing="3" cellpadding="0" align="center" width="100%">
  <tr>
      <td  class=tdbg3 height="127" width="90%" valign="top"> 
        <table border=0 cellspacing=1 class=size-9-black width="100%">
          <tr>
            <td height=20 colspan="2" class="tbtext">
              <div align="center">
			  &nbsp;
                <input type="button" name="saveBtn"  value='<i18n:message key="button.save"/>' onclick="savePurview()" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
                &nbsp; <input type="button" name="backBtn"  value='<i18n:message key="button.back"/>' onclick="javascript:history.back()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              </div>
            </td>
          </tr>
		  <tr>
		   <td  class="tbtext" height="127" width="12%" valign="top"> 
            <i18n:message key="housekeeping_role.pleaseselect"/>
           </td>
		   <td height=20  class="tbtext">
        
		     <script language="JavaScript">
                          <!--
                             demo.setCheckbox(true);
                             demo.setName = "gns";
                             document.write(demo)
                             //-->
             </script>
	
			 </td>
		  </tr>
          
        </table>
      </td>
      <td width="2" class=tdbg2 height="127"></td>
  </tr>
</table>
</form>
</body>
</html>
<script language="JavaScript">
   demo.checkoriginal(0);
   demo.closeAll(0);
   function savePurview(){
     document.roleForm.action = "<%=request.getContextPath()%>/roleAction.it?method=saveRolePurview";
     document.roleForm.submit();
   }
</script>
