
<%@page import="com.aiait.eflow.common.CommonName" %>
<%@page import="com.aiait.framework.i18n.*"%>
<style type="text/css">
<%-- .table2 {background-image: url(<%=request.getContextPath()%>/images/eflow-success.gif);background-repeat: no-repeat;background-position: right top;} --%>
</style> 
<script language="javascript">
//初始化该弹出框
	jQuery(function(){
		jQuery("#returnDialog").dialog({
		      height: 400,
		      width: 500,
		      modal: true,//冻结主页
		      draggable: true,//移动
		      resizable:false ,//页面
		      title:"Confirm",
		      position :"center",
			  autoOpen: false
		});
	})
	
	/**
	*call by the parent page
	*/
	function callMsgBox(returnObj){
       var result = eval('('+returnObj.responseText.Trim()+')');
       if("success"== result.status){
       	  common.isEmpty(result.operate_return_url)?returnUrl="":returnUrl=result.operate_return_url;
       	  jQuery("#returnDialog").dialog({close:goBack});
       	  setTimeout("jQuery('#returnDialog').dialog('close');",5000); 
       	}
     	  jQuery("#showMsg").html("<p>"+result.message+"</p>");
     	  jQuery("#returnDialog").dialog("open");
     	  var top = jQuery(document).height() - 600;
     	  jQuery("div[role='dialog']").css({"position":"absolute","top":top,"left":"50%","margin-left":"-200px" ,"margin-bottom":"-250px"});//重新定位该弹出框居中	       	 
		 
	}
  var returnUrl = "";
  function goBack(){
     if(returnUrl==""){
       window.history.go(-1);
     }else{
       window.location = "<%=request.getContextPath()%>"+returnUrl;
     }
  }

</script>
<div id="returnDialog" class="out"><div class="in">
     <table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0" class="table2">
       <tr>
         <td align="center" width="30%">&nbsp;&nbsp;</td>
         <td><font id='showMsg' color='green' face='Arial' size='2'></font></td>
       </tr>
       <tr>
         <td  align='center' colspan=2>
           <input type="button" name="subBtn" value="   <i18n:message key="common.ok"/>   " onclick="javascript:jQuery('#returnDialog').dialog('close');" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
             onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         </td>
       </tr>
     </table>
  </div>
  </div>