<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%String ctx=request.getContextPath();%>
<script type="text/javascript" src="">
</script>

<!--[if lt IE 10]> 
<script type=text/javascript src="<%=request.getContextPath()%>/js/PIE.js"></script>
<![endif]--> 


<script type=text/javascript src="<%=ctx%>/js/jquery.min.js"></script>
<script type=text/javascript src="<%=ctx%>/js/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ctx%>/css/loading.css" />
<link href="<%=ctx%>/css/jquery-ui.css" rel="stylesheet">




<div id="mask" class="mask opacity"></div> 
<div class="ui-overlay" id="loading" >
    <div>
	    <img alt="nova loading" src="<%=request.getContextPath()%>/css/NovaLoad.gif">
	</div>
</div>









<script type="text/javascript">

document.onreadystatechange=function()
{
	try
	{
		if (document.readyState == "complete") 
		{
		
			 delNode("loading");
			 
	    }
    }
    catch(e)
    {
    	alert("Fail to load page!");
    }
}

//DIV
function  delNode(nodeId)
{   
  try
  {   
	  var div =document.getElementById(nodeId);
	  var div1 =   document.getElementById("mask");
	  if(div !==null)
	  {
		  div.style.display="none";
		  div1.style.display="none";
		  //parentNode.removeChild(div);   
		  //div=null;    
		  //CollectGarbage(); 
	  }  
  }
  catch(e)
  {   
  	   alert("It can find ID "+e.message);
  }   
}
</script>

