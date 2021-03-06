<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.common.helper.*" %>

<html>
<head>
<title>Synch Data</title>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
.loading-indicator{
    font-size:9pt; 
    background-repeat: no-repeat;  
    background-position:top left; 
    padding-left:20px;
	height:18px;
	text-align:left;
}
</style>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">

   var xmlhttp = createXMLHttpRequest();
   
   function synchDataNew(){
     var url = "<%=request.getContextPath()%>/dataSynchAction.it?method=beginDataSynch";
     var result;
     if(xmlhttp){
      xmlhttp.open('POST',url,true);
      xmlhttp.onreadystatechange = function()
      {
             if(xmlhttp.readyState == 4)
             {
             if(xmlhttp.status == 200){
                 result = xmlhttp.responseText;
                 document.getElementById("displayMessage").innerHTML = "Completed to refresh data.";
                 document.getElementById("beginBtn").disabled = "";
             }
             }else{
               document.getElementById("displayMessage").innerHTML = "<img src='../images/loading.gif'>&nbsp;&nbsp;Refreshing data, please wait...";
               document.getElementById("beginBtn").disabled = "true";
             }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);
    }
   }
 
   function synchData(objValue){
     var url = "<%=request.getContextPath()%>/dataSynchAction.it?method=beginDataSynch&paramName="+objValue;
     var result;
     if(xmlhttp){
       xmlhttp.open('GET',url,true);
       xmlhttp.onreadystatechange = function()
       {
        //document.getElementById("displayMessage").innerHTML = document.getElementById("displayMessage").innerHTML + "refresh..."; 
        if (xmlhttp.readyState == 4){
          //document.getElementById("displayMessage").innerHTML = document.getElementById("displayMessage").innerHTML + "end..."; 
          if(xmlhttp.status == 200) {
           result = xmlhttp.responseText;
           //document.getElementById("displayMessage").innerHTML = document.getElementById("displayMessage").innerHTML + result;
          }
         }
       }
       xmlhttp.send(null);
     }
   }
   function deal(){
     var paramName = new  Array();
     paramName[0]="basedata";
     paramName[1]="systemfield";
     paramName[2]="staffteam";
     paramName[3]="workflow";
     paramName[4]="parameterconfig";
     for(var i=0;i<5;i++){
       synchData(paramName[i]);
     }
   }

</script>
</head>
<body>
<body>
<div align='center'>
<input type="button" name="beginBtn" value='<i18n:message key="button.refreshdata"/>' onclick="synchDataNew()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
</div>
<br>
<div id="displayMessage" class="loading-indicator">
</div>
</body>

</html>