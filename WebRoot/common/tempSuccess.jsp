<%@ page contentType="text/html; charset=gb2312"%>
<%@page import="com.aiait.eflow.common.CommonName" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>Show Message</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/loading.css" />
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>

</head>
<body>
<% 
  String url = (String)request.getAttribute("url"); //
  String refreshType = (String)request.getAttribute("refreshType");
  String message = (String)request.getAttribute(CommonName.COMMON_OK_INFOR);
  if(refreshType==null){
	  refreshType = "";
  }
 
  if(url==null){
	  url = "";
	  url = (String)session.getAttribute("url");
	  if(url==null){url="";}
  }
  if("parent1".equals(refreshType) && !"".equals(url)){
	 // String message = (String)request.getAttribute(CommonName.COMMON_OK_INFOR);
	  //url = "common/successmessage.jsp?operate_return_url="+url+"&success_message="+message;
	  url = "wkfProcessAction.it?method=successTipPage&operate_return_url="+url+"&success_message="+message;
  }
  
  if("parent".equals(refreshType) && !"".equals(url)){
		 // String message = (String)request.getAttribute(CommonName.COMMON_OK_INFOR);
		  //url = "common/successmessage.jsp?operate_return_url="+url+"&success_message="+message;
	  url = "wkfProcessAction.it?method=successTipPage&operate_return_url="+url+"&success_message="+message;
  }
%>
 <div id="loading" style="display:block;top:250px; left:350px;">
   <div class="loading-indicator">
	  <!-- It is processing... -->
	  Refreshing, please wait ...
   </div>
 </div>
</body>
</html>
<script language="javascript">
    var refreshType = "<%=refreshType%>";
    var url = "<%=url%>";
    if(url!=""){
      if(refreshType==""){
        //alert('Operate successfuly');
        window.opener.document.location = encodeURI(encodeURI("<%=request.getContextPath()%>/<%=url%>"));
        window.close();
      }else if(refreshType=="self"){
        //alert('Operate successfuly');
        	window.document.location =  encodeURI(encodeURI("<%=request.getContextPath()%>/<%=url%>"));
      }else if(refreshType=="parent1"){
    	  	window.document.location =  encodeURI(encodeURI("<%=request.getContextPath()%>/<%=url%>"));
      }else if(refreshType=="parent"){
	        window.opener.document.location = encodeURI(encodeURI("<%=request.getContextPath()%>/<%=url%>"));
	        window.close();
      }
    }else{
       //alert('Operate successfuly');
       window.close();
    }
</script>