<%@ page contentType="text/html; charset=gb2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%@page import="com.aiait.eflow.common.CommonName" %>
<%@page import="com.aiait.framework.i18n.*"%>
  <% 
    String returnUrl = (String)request.getAttribute(CommonName.RETURN_URL);
    String message = (String)request.getAttribute(CommonName.COMMON_OK_INFOR);
    //String returnType = (String)request.getAttribute(CommonName.WINDOW_RETURN_TYPE);//返回方式，如果是打开的窗口，则关闭本窗口，父窗口按returnUrl返回；否则本窗口按returnUrl返回
   // if(returnType==null){
   // 	returnType = CommonName.WINDOW_RETURN_TYPE_SELF;
   // }
    if(returnUrl==null){
    	 returnUrl = (String)request.getParameter(CommonName.RETURN_URL);
    }
    if(message==null){
    	message = (String)request.getParameter(CommonName.COMMON_OK_INFOR);
    }
    if(message==null){
    	message = "Operate Successfully";
    }
    if(returnUrl==null){
    	returnUrl = "";
    }else if(!"".equals(returnUrl)){
      if(!"/".equals(returnUrl.substring(0,1))){
    	  returnUrl = "/"+returnUrl;
      }
    }
    
    String tip = "";
    //System.out.println("returnUrl: " + returnUrl);
    if (returnUrl.indexOf("listPersonalApplyForm") != -1) {
        tip = I18NMessageHelper.getMessage("common.timeout_to_request_list");
    } else if (returnUrl.indexOf("listWaitForDealForm") != -1) {
        tip = I18NMessageHelper.getMessage("common.timeout_to_waiting_list");
    }
  %>
<html>
<head>
<title>E-Flow successful message</title>
<style type="text/css">
body  {
	margin:0px;
	SCROLLBAR-FACE-COLOR: #799AE1;
	SCROLLBAR-HIGHLIGHT-COLOR: #799AE1;
	SCROLLBAR-SHADOW-COLOR: #799AE1;
	SCROLLBAR-DARKSHADOW-COLOR: #799AE1;
	SCROLLBAR-3DLIGHT-COLOR: #799AE1;
	SCROLLBAR-ARROW-COLOR: #FFFFFF;
	SCROLLBAR-TRACK-COLOR: #AABFEC;
	font-size: 9px;
	color: #333333;
	margin-top: 10px;
	margin-right: 10px;
	margin-left: 10px;
	margin-bottom: 10px;
	background-color :#FBFBFC;
}

.btn3_mouseout {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mouseover {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #EBE5D3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mousedown {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

.btn3_mouseup {
    BORDER-RIGHT: #4F4F4F 1px solid;
    PADDING-RIGHT: 2px;
    BORDER-TOP: #4F4F4F 1px solid;
    PADDING-LEFT: 2px;
    FONT-SIZE: 12px;
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr = #DAD0B3);
    BORDER-LEFT: #4F4F4F 1px solid;
    CURSOR: hand;
    COLOR: black;
    PADDING-TOP: 2px;
    BORDER-BOTTOM: #4F4F4F 1px solid
}

INPUT {
	FONT-SIZE: 12px;
	border: 1px solid #AAC0F6;
}
.out {
  position:absolute;
  background:#bbb; 
  margin:10px auto;
  width:455px;
  height:110px;
  top:20%;
  left:30%;
  bottom:30%;
  text-align:left;
  }
.in {
  background:#fff; 
  border:1px solid #555;
  padding:10px 5px;
  position:relative;
  top:-5px;
  left:-5px;
  vertical-align:middle;
  }
.table2 {background-image: url(<%=request.getContextPath()%>/images/eflow-success.gif);background-repeat: no-repeat;background-position: right top;}
</style>
<script language="javascript">
  var url = "<%=returnUrl%>";
  function goBack(){
     if(url==""){
       window.history.go(-1);
     }else{
       window.location = "<%=request.getContextPath()%>"+url;
     }
  }
  function parentGoBack(){
     if(url==""){
      window.opener.history.go(-1);
     }else{
       window.opener.history.location = "<%=request.getContextPath()%>"+url;
     }
     window.close();
  }

  function closeWindow() {
      window.top.close();
  }
  
  //5秒后自动执行goBack()函数进行跳转
  var timename=setTimeout("goBack();",5000); 

</script>
<body>
<div class="out"><div class="in">
     <table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0" class="table2">
       <tr>
         <td  colspan='2'>&nbsp;&nbsp;<br>&nbsp;</td>
       </tr>
       <tr>
         <td align="center" width="30%">&nbsp;&nbsp;</td>
         <td><font color='green' face='Arial' size='2'><b><%=message%></b></font></td>
       </tr>
       <tr>
         <td  align='center' colspan=2>
           &nbsp;&nbsp;&nbsp;<input type="button" name="subBtn" value="   <i18n:message key="common.ok"/>   " onclick="javascript:goBack()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
             onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           <!-- input type="button" name="closeBtn" value="<i18n:message key="common.close_window"/>" onclick="closeWindow();" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
             onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" -->
         </td>
       </tr>
       <tr>
         <td align="center" width="30%">&nbsp;&nbsp;</td>
         <td><font color='#444444' face='Arial' size='2'><%=tip%></font></td>
       </tr>
     </table>
  </div>
  </div>
</body>
</html>