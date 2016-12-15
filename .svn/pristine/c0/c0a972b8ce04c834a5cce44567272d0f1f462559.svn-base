<%@ page contentType="text/html;charset=GB2312" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<TITLE>AIAIT E-Flow</TITLE>
<META content="MSHTML 6.00.2800.1543" name=GENERATOR>
<META content=JavaScript name=vs_defaultClientScript>
<META content=http://schemas.microsoft.com/intellisense/ie5
	name=vs_targetSchema>
<META charset=GB2312>
<%
	String flowId = (String) request.getAttribute("flowId");
	String requestNo = (String) request.getParameter("requestNo");
%>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<SCRIPT language=javascript>
var xmlHttp;
var xx=0,yy=0,moveok=0
/**
** 加载指定的流程
**/
function loadFlow(){
   var flowId = "<%=flowId%>";
   document.all.item('Designer1_FlowBaseID').value = flowId;
   var url = "<%=request.getContextPath()%>/wkfDesignAction.it?method=loadWorkFlow&flowId="+flowId+"&requestNo=<%=requestNo%>";
   initWorkMap(url,"load");
}
function createXMLHttpRequest(){
	if(window.ActiveXObject){
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}else if(window.XMLHttpequest){
		xmlHttp = new XMLHttpRequest();
	}		
}

function initWorkMap(url,type){
  createXMLHttpRequest(); 
  if(xmlHttp){
    xmlHttp.open("GET", url, true);
	  xmlHttp.onreadystatechange= function(){
	   if(xmlHttp.readyState == 4){
		   if(xmlHttp.status == 200){
			 var xmldata = xmlHttp.responseXML;
			   var   xmldoc   =   new   ActiveXObject("Microsoft.XMLDOM");
              xmldoc.async   =   false; 
			  xmldoc.loadXML(xmlHttp.responseXML.xml);
			  var   xsldoc   =   new   ActiveXObject("Microsoft.XMLDOM");   

              xsldoc.async   =   false;   
              xsldoc.load("<%=request.getContextPath()%>/workflow/WorkFlow.xslt");

			  document.all("Chart").innerHTML   = '<v:group id=WorkFlowGroup style="WIDTH: 200px; POSITION: absolute; HEIGHT: 200px; v-text-anchor: middle-center" coordsize = "2000,2000"><v:polyline id=line1 style="Z-INDEX: 2000; POSITION: absolute" points = "0,0,100,100"><!--钢笔可视化--><v:stroke dashstyle = "shortDash"></v:stroke></v:polyline>' 
			                                       +  xmldoc.transformNode(xsldoc)
			                                      + '</v:group>'; 
			  
			 if(type=="load"){
			   ShowAllLine();
			 }
		   }
	   }
	}
  xmlHttp.setRequestHeader("If-Modified-Since","0");
  xmlHttp.send(null);	
  }
}
</script>
<STYLE>
v\:* {
	BEHAVIOR: url(#default#VML)
}
</STYLE>
</HEAD>
<BODY
	oncontextmenu='if(event.srcElement.tagName!="TEXTAREA")return false'
	onselectstart='if(event.srcElement.tagName!="TEXTAREA"&amp;&amp;event.srcElement.tagName!="INPUT")return false'
	style="CURSOR: default" bottomMargin=0 vLink=#3732cd link=#3732cd
	leftMargin=0 topMargin=0 rightMargin=0>

<!--弹出菜单-->
<SPAN id=WorkFlowItemMenu
	style="DISPLAY: none; position: absolute; z-index: 40000; BACKGROUND-COLOR: menu">
<INPUT class=bon3 id=SelDep
	onclick='WorkFlowItemMenu.style.display="none";SelectDepartment(thisobj);'
	type=button value="Property"> <BR>
<INPUT class=bon3 id=shanchu
	onclick='WorkFlowItemMenu.style.display="none";DeleteRelLine(thisobj)'
	type=button value="Delete"><!--??????????????--> <BR>
<INPUT class=bon3 style='color: black; font-weight: bold'
	onclick='WorkFlowItemMenu.style.display="none"' type=button
	value=Cancel> </SPAN>

<TABLE id=Table1 height="100%" cellSpacing=0 cellPadding=0 width="100%"
	border=0>
	<TBODY>
		<TR>
			<TD vAlign=top noWrap align=left width="100%" height="100%">
			<DIV class=WorkFlowMap id=Chart
				style="Z-INDEX: 101; LEFT: 0px; BORDER-LEFT: silver 1px solid; WIDTH: 100%; BORDER-BOTTOM: silver 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 95.25%">
			<v:group id=WorkFlowGroup
				style="WIDTH: 200px; POSITION: absolute; HEIGHT: 200px; v-text-anchor: middle-center"
				coordsize="2000,2000">
				<v:polyline id=line1 style="Z-INDEX: 2000; POSITION: absolute"
					points="0,0,100,100">
					<!--钢笔可视化-->
					<v:stroke dashstyle="shortDash"></v:stroke>
				</v:polyline>
				<?xml version="1.0" encoding="gb2312"?>
				<v:Group id=WorkFlowItemGroup
					style="Z-INDEX: 9000; LEFT: 1000px; WIDTH: 1000px; POSITION: absolute; TOP: 1400px; HEIGHT: 500px"
					xmlns:v="urn:schemas-microsoft-com:vml" coordsize="1000,500">
					<v:roundrect class=WorkFlowItem id=0 title=0
						style="WIDTH: 1000px; POSITION: absolute; HEIGHT: 500px"
						LimiteDate="0" DepName="开始节点" coordsize="21600,21600"
						fillcolor="red" strokecolor="#5082b9">
						<v:fill type="gradient" color2="yellow" angle="0" method="sigma"></v:fill>
						<v:shadow on="t" type="perspective" color="black" opacity="19660f"
							matrix="" offset="2pt,3pt"></v:shadow>
						<v:TextBox
							style="text-align:center;top:10px;font-weight:bold;font-family:Arial;font-size:12px;word-wrap:break-all;">开始节点</v:TextBox>
						<v:stroke></v:stroke>
					</v:roundrect>
					<v:line class=EndLine style="CURSOR: hand" Refid="0"
						from="1000,250" to="1150,250" strokecolor="black">
						<v:stroke endarrow="classic"></v:stroke>
					</v:line>
					<v:line class=StartLine Refid="0" from="-100,250" to="0,250"
						strokecolor="black">
						<v:stroke startarrow="oval"></v:stroke>
					</v:line>
				</v:Group>
				<v:Group id=WorkFlowItemGroup
					style="Z-INDEX: 9000; LEFT: 5000px; WIDTH: 1000px; POSITION: absolute; TOP: 1400px; HEIGHT: 500px"
					xmlns:v="urn:schemas-microsoft-com:vml" coordsize="1000,500">
					<v:roundrect class=WorkFlowItem id=-1 title=0
						style="WIDTH: 1000px; POSITION: absolute; HEIGHT: 500px"
						LimiteDate="0" DepName="结否节点" coordsize="21600,21600"
						fillcolor="red" strokecolor="#5082b9">
						<v:fill type="gradient" color2="yellow" angle="0" method="sigma"></v:fill>
						<v:shadow on="t" type="perspective" color="black" opacity="19660f"
							matrix="" offset="2pt,3pt"></v:shadow>
						<v:TextBox
							style="text-align:center;top:10px;font-weight:bold;font-family:Arial;font-size:12px;word-wrap:break-all;">结否节点</v:TextBox>
						<v:stroke></v:stroke>
					</v:roundrect>
					<v:line class=EndLine style="CURSOR: hand" Refid="-1"
						from="1000,250" to="1150,250" strokecolor="black">
						<v:stroke endarrow="classic"></v:stroke>
					</v:line>
					<v:line class=StartLine Refid="-1" from="-100,250" to="0,250"
						strokecolor="black">
						<v:stroke startarrow="oval"></v:stroke>
					</v:line>
				</v:Group>
			</v:group></DIV>
			</TD>
			<td><INPUT type="hidden" id=Designer1_FlowBaseID></td>
		</TR>
	</TBODY>
	<tr>
		<td align="center"><input type="button" name="closeBtn"
			value="Close" onclick="javascript:window.close()"></td>
	</tr>
</TABLE>
<INPUT id=Designer1_XMLData type=hidden name="Designer1_XMLData">
<!--保存FlowBaseID-->
<INPUT id=Designer1_FlowBase type=hidden value=0 name=Designer1_FlowBase>
<script language="jscript"
	src="<%=request.getContextPath()%>/js/Control.js">
			</script>
<script language="jscript">
					ShowAllLine();
			</script>
</form>
</body>
</HTML>
<script language="javascript">
  loadFlow();
  //为了控制不显示鼠标右键属性弹出菜单，重写MouseUP函数
 function MouseUP()
{
line1.style.display='none';
var tempEnd=event.srcElement;
	WorkFlowItemMenu.style.display='none';
	if((tempEnd.className=="WorkFlowItem" ||tempEnd.className=="NormalLine") && event.button==2 && tempEnd.id!='0'&& tempEnd.id!='-1')
	{
		//document.all.item('SelDep').style.display='';
		//if(tempEnd.className=="NormalLine")
		//{
		//	 document.all.item('SelDep').style.display='none';
		//}
		//WorkFlowItemMenu.style.left=event.x;
		//WorkFlowItemMenu.style.top=event.y;
		//WorkFlowItemMenu.style.display='';
		//thisobj=event.srcElement;
	}
	else
	{
		if(tempEnd.className=="StartLine")
		{
			//画节点之间的关系
			//加线之前进行判断
			//判断条件为：两个对象是否已经建立关系了。两个对象只建立一次关系。不能与开始节点建立流入关系
			if (!ContainLine(Starline,tempEnd)&& tempEnd.Refid!='0'&& Starline.Refid!='-1')
			{
			DrawLine();
			//引发服务器端事件__doPostBack('Add','');
			}
			else
			{
			  	window.alert('无法建立关系，可能是关系已经存在或在同一节点上建立关系');
			}
		}		
	}
			ShowAllLine();
}
Chart.onmouseup=MouseUP;
</script>
