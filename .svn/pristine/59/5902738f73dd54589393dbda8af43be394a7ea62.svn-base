<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="com.aiait.eflow.wkf.vo.WorkFlowVO"%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<TITLE>WorkFlowDesign</TITLE>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<STYLE>
v\:* {
	BEHAVIOR: url(#default#VML)
}
</STYLE>
</HEAD>
<%
	String flowId = (String) request.getParameter("flowId");
	String formSystemId = (String) request.getParameter("formSystemId");
	String maxNodeId = (String) request.getAttribute("maxNodeId");
	WorkFlowVO flow = (WorkFlowVO) request.getAttribute("flow");
%>
<BODY
	oncontextmenu='if(event.srcElement.tagName!="TEXTAREA")return false'
	onselectstart='if(event.srcElement.tagName!="TEXTAREA"&amp;&amp;event.srcElement.tagName!="INPUT")return false'
	style="CURSOR: default" bottomMargin=0 vLink=#3732cd link=#3732cd
	leftMargin=0 topMargin=0 rightMargin=0>
<FORM id=Form1 name=Form1 method=post><input type="hidden"
	name="formSystemId" value="<%=formSystemId%>"> 
<script language="javascript">

var xmlHttp;
var xx=0,yy=0,moveok=0

function createXMLHttpRequest(){
	if(window.ActiveXObject){
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}else if(window.XMLHttpequest){
		xmlHttp = new XMLHttpRequest();
	}		
}

function goBack(){
  if(confirm(flowdesign_save_data)){
    window.location = "<%=request.getContextPath()%>/wkfDesignAction.it?method=listFlow";
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
			  //var xmldata = xmlHttp.responseText;
			   //alert(xmlHttp.responseXML.xml);
			   var   xmldoc   =   new   ActiveXObject("Microsoft.XMLDOM");

              xmldoc.async   =   false; 
			  xmldoc.loadXML(xmlHttp.responseXML.xml);
			  var   xsldoc   =   new   ActiveXObject("Microsoft.XMLDOM");   

              xsldoc.async   =   false;   
              xsldoc.load("<%=request.getContextPath()%>/workflow/WorkFlow.xslt");

			  document.all("Chart").innerHTML   = '<v:group id=WorkFlowGroup style="WIDTH: 200px; POSITION: absolute; HEIGHT: 200px; v-text-anchor: middle-center" coordsize = "2000,2000"><v:polyline id=line1 style="Z-INDEX: 2000; POSITION: absolute" points = "0,0,0,1" strokeweight="4px"><v:stroke dashstyle = "shortDash"></v:stroke></v:polyline>' 
			                                       +  xmldoc.transformNode(xsldoc)
			                                      + '</v:group>'; 
			  //alert(document.all("Chart").innerHTML)
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

/***
* ????????20070407 Robin Hou
**/
function createFlow(){
  
  var url = "<%=request.getContextPath()%>/workflow/createxml.jsp?type=create";
  imgArr = showModalDialog('<%=request.getContextPath()%>/workflow/editFlowId.jsp',window,'dialogWidth:350px; dialogHeight:150px;help:0;status:0;resizeable:1;');
  if (imgArr != null){
    document.all.item('Designer1_FlowBase').value = imgArr['flowId'];
    initWorkMap(url,"create");
  }
}
/**
** ???????
**/
function loadFlow(){
   var flowId = "<%=flowId%>";
   document.all.item('Designer1_FlowBaseID').value = flowId;
   //alert('flowId='+flowId)
   //var url = "<%=request.getContextPath()%>/workflow/createxml.jsp?type=load&flowId="+flowId;
   var url = "<%=request.getContextPath()%>/wkfDesignAction.it?method=loadWorkFlow&flowId="+flowId;
   initWorkMap(url,"load");
}

//?????????
function saveFlow() 
{
  if(confirm(flowdesign_save_flow)==false) return;
  try
  {
   var tempdata=CreateXmlData();
   //alert(tempdata)
   document.all('Designer1_XMLData').value=tempdata;
   //return;
   //createXMLHttpRequest(); 
   //var url = "<%=request.getContextPath()%>/wkfDesignAction.it?method=saveWorkFlow&xmldata="+tempdata+"&flowId="+document.all.item('Designer1_FlowBaseID').value+"&formSystemId=<%=formSystemId%>";
   //window.location = url;
   var url = "<%=request.getContextPath()%>/wkfDesignAction.it?method=saveWorkFlow&flowId="+document.all.item('Designer1_FlowBaseID').value+"&formSystemId=<%=formSystemId%>";
   document.forms['flowForm'].xmldata.value = tempdata;
   
   document.forms['flowForm'].action = url;
   document.forms['flowForm'].submit();
  }
  catch(e)
  {
   alert(e)
   //window.alert("WorkFlow can't be saved. It included empty node!");
  }
}

//*******************************************************************************
//???????????????????
//??????????????XML??????????????
//?????XMLDom.LoadXML??????????
//*******************************************************************************
<!--
//?????????????????????
function DrawLine() {
   //??????
   var tempEndPoint;
   tempEndPoint=event.srcElement;
   //??????
   var strstroke="<v:stroke color='blue' EndArrow='classic'></v:stroke>";
   var tempstroke=document.createElement(strstroke);
   //????
   var strElement="<v:polyline style='Z-Index:-1' color='blue' id =ConnectLine class= NormalLine points="+line1.points.value+" EndShape="+tempEndPoint.Refid+" BeginShape="+Starline.Refid+" strokeweight=\"4px\"></v:polyline>";
   var newPoint = document.createElement(strElement);
   //????????
   newPoint.appendChild(tempstroke);
   //????
   WorkFlowGroup.appendChild(newPoint);
   //???????????
   //if(Starline.Refid=='0')
   //{
   // document.getElementById(tempEndPoint.Refid).isStart='1';
   //}
}
function DrawWorkFlowItem()
{
//????????????????group?????????????
//??????????????????

   MaxID=MaxID+1;
 
     //WorkFlowItem??
   var tempg="<v:Group style='WIDTH: 1000px; POSITION: absolute; HEIGHT: 500px;' id ='WorkFlowItemGroup' coordsize='1000,500'></v:Group>"; 
     //???????????
   var tempgroupel;
   tempgroupel=document.createElement(tempg);
    //?????????
   WorkFlowGroup.appendChild(tempgroupel);
  
   var WorkFlowElement="<v:roundrect id='"+MaxID+"' class='WorkFlowItem' DepName='EmptyNode'  Title='Has not defined Node' style='WIDTH: 1000px; POSITION: absolute; HEIGHT: 500px' coordsize = '21600,21600' fillcolor = 'red' strokecolor = '#5082b9'></v:roundrect>";
   //??WorkFlowItem??
   var NewWorkFlowItem = document.createElement(WorkFlowElement);
   tempgroupel.appendChild(NewWorkFlowItem);
   //??fill??????????
   var tempfill=document.createElement("<v:fill type = 'gradient' color2 = 'yellow' angle = '0' method = 'sigma'></v:fill>");
   NewWorkFlowItem.appendChild(tempfill);
   //??Shadow??????????
   var tempShadow=document.createElement("<v:shadow on = 't' type = 'perspective' color = 'black' opacity = '19660f' matrix = '' offset = '2pt,3pt'></v:shadow>");
   NewWorkFlowItem.appendChild(tempShadow);
   //??Textbox??????????
   var tempTextbox=document.createElement("<v:TextBox style='text-align:center;top:10px;font-weight:bold;font-family:Arial;font-size:12px;word-wrap:break-all;'></v:TextBox>");
   NewWorkFlowItem.appendChild(tempTextbox);
   var temptext=document.createTextNode("Empty Node");
   tempTextbox.appendChild(temptext);
   //???????????,????????
   var tempinLine=document.createElement("<v:line class=EndLine style='POSITION: absolute;CURSOR: hand' Refid='"+MaxID+"' from = '1000,250' to = '1150,250' strokecolor = 'black' strokeweight='4px'></v:line>");
   tempinLine.appendChild(document.createElement("<v:stroke endarrow = 'classic'></v:stroke>"));
   tempgroupel.appendChild(tempinLine);
   //???????????,????????
   var tempoutLine=document.createElement("<v:line class=StartLine style='POSITION: absolute;CURSOR: hand' Refid='"+MaxID+"'  from = '-100,250' to = '0,250'  strokecolor = 'black' strokeweight='3px'></v:line>");
   tempoutLine.appendChild(document.createElement("<v:stroke startarrow = 'oval'></v:stroke>"));
   tempgroupel.appendChild(tempoutLine);
   //window.alert('????????????????????');
}
function btnADD_onclick() {
  DrawWorkFlowItem();
}
//?????????
function SelectDepartment(WorkFlowItemobj) {
     if (WorkFlowItemobj.id=="ConnectLine") {
         // enterConditionSet
       var formSystemId = "<%=formSystemId%>";
       if(formSystemId.Trim()=="0"){
         alert(flowdesign_link_form);
         return;
       }
       //??????????id
       var sourceId = WorkFlowItemobj.BeginShape;
       //??????????id
       var targetId = WorkFlowItemobj.EndShape;
       var AllRect =new Array();
       AllRect=document.getElementsByTagName('roundrect');
       if (AllRect!=null)
       {
         var count=AllRect.length;
         var srcobj = null;
         var endobj = null;
         if (count)
         {
          var i;
          for (i=0;i<count;i++)
          {
            if(AllRect[i].id==sourceId){
              obj = AllRect[i];
              break;
            }
          }
          for (i=0;i<count;i++)
          {
            if(AllRect[i].id==targetId){
              endobj = AllRect[i];
              break;
            }
          }
          //???????
          if(obj==null){
             alert(flowdesign_begin_node);
             return;
          }
          if(endobj==null){
             alert(flowdesign_end_node);
             return;
          }
         // return; // add by 2007-08-30, it need to be commented
          imgArr = showModalDialog('<%=request.getContextPath()%>/wkfDesignAction.it?method=enterConditionSet&flowId='+document.all.item('Designer1_FlowBaseID').value
                   +"&beginNodeId="+obj.id+"&beginNodeName="+obj.DepName+"&endNodeId="+endobj.id+"&endNodeName="+endobj.DepName+"&formSystemId="+formSystemId.Trim()+"&tempDate="+Math.random()*100000,window,'dialogWidth:1200px; dialogHeight:450px;help:0;status:0;resizeable:1;');
          if (imgArr != null){
            WorkFlowItemobj.title = imgArr['conditionStr'];
          }
         }
       }
     } else {
         // enterNodePropertySet
         imgArr = showModalDialog('<%=request.getContextPath()%>/wkfDesignAction.it?method=enterNodePropertySet&flowId='+document.all.item('Designer1_FlowBaseID').value
                   +"&nodeId="+WorkFlowItemobj.id+"&approverList="+WorkFlowItemobj.ApproverList+"&tempDate="+Math.random()*100000,window,'dialogWidth:520px; dialogHeight:765px;help:0;status:0;resizeable:1;');
         if (imgArr != null) {
     //************************************
     //????????????
     //if (ContainDepartMent(imgArr['DepartmentID'])&&WorkFlowItemobj.id!=imgArr['DepartmentID'])
     //{
     // window.alert('??????????????');
     // return;
     //}
     
     //***********************************
      //?????ID
      var oldID=WorkFlowItemobj.id;
      
      //???ID???????????????
      WorkFlowItemobj.id=imgArr['nodeId'];
      //throw 'dd';
      WorkFlowItemobj.children[2].childNodes[0].nodeValue=imgArr['nodeName'];
      //WorkFlowItemobj.title="???"+imgArr['limitDate']+"?";
      WorkFlowItemobj.DepName=imgArr['nodeName'];
      //IT0958 DS-004 Begin
      WorkFlowItemobj.NodeAlias = imgArr['nodeAlias'];
      //IT0958 DS-004 End
      WorkFlowItemobj.LimiteDate=imgArr['LimitHours'];
      WorkFlowItemobj.ApproverList=imgArr['approverList'];
      WorkFlowItemobj.NodeType=imgArr['NodeType'];
      WorkFlowItemobj.processorField=imgArr['processorField'];
      WorkFlowItemobj.companyField=imgArr['companyField'];
      WorkFlowItemobj.delaytimeField=imgArr['delaytimeField'];
      WorkFlowItemobj.approveHandle=imgArr['approveHandle'];
      WorkFlowItemobj.rejectHandle=imgArr['rejectHandle'];
      WorkFlowItemobj.approveAlias=imgArr['approveAlias'];
      WorkFlowItemobj.rejectAlias=imgArr['rejectAlias'];
      WorkFlowItemobj.UpdateSections=imgArr['updateSectionFields'];
      WorkFlowItemobj.NewSectionFields=imgArr['newSectionFields'];
      WorkFlowItemobj.HiddenSectionFields=imgArr['hiddenSectionFields'];
      //WorkFlowItemobj.title=imgArr['nodeAlias'] + ":"+imgArr['LimitHours'];
      //??????????
      WorkFlowItemobj.parentElement.children[1].Refid=imgArr['nodeId'];
      WorkFlowItemobj.parentElement.children[2].Refid=imgArr['nodeId'];
      
      //????????????????endshape?beginshape
      UpdateRel(oldID,imgArr['nodeId']);
     } 
   }
}
//????????
function RetSetEndWorkFlowIetm()
{
var AllRect =new Array();
   AllRect=document.getElementsByTagName('roundrect');
    if (AllRect!=null)
   {
    var count=AllRect.length;
    if (count)
    {
     var i;
     for (i=0;i<count;i++)
     {
     if(AllRect[i].isStart=='-1')
      AllRect[i].isStart='0';
     }
    }
    else
    {
    if(AllRect.isStart=='-1')
     AllRect.isStart='0';
    }
   }
}
//?????????????
function ContainDepartMent(DepartmentID)
{
  var AllRect =new Array();
  AllRect=document.getElementsByTagName('roundrect');
   if (AllRect!=null)
  {
   var count=AllRect.length;
   if (count)
   {
    var i;
    for (i=0;i<count;i++)
    {
     if(AllRect[i].id==DepartmentID) return true;
    }
   }
   else
   {
    if(AllRect.id==DepartmentID) return true;
   }
   return false;
  }
  return false;
     
}

//XML
function CreateXmlData() {
  var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
  var XmlEle;
  xmlDoc.loadXML('<Flow></Flow>');
  var Root=xmlDoc.childNodes[0];
  var AllRect = new Array();
  AllRect=document.getElementsByTagName('roundrect');
  var j;
  var AfterDeparts='';
  var DepartsDays='';
  
  if (AllRect!=null) {
   var count=AllRect.length;
   var nonLineCount = 0;
   if (count) {
     for (j=0; j<count; j++) {
        if(AllRect[j].DepName.length>=9 && AllRect[j].DepName.substring(0,9)=="EmptyNode") {
            throw flowdesign_empty_node;
        }
        //alert(AllRect[j].DepName.substring(0,9))
       
        //??XML??
        //????????????????
         //AstrDepDays//????????????????????????????????????ID?????????????????????????????????????ID1???1???ID2???2?......
       
  
          /// AstrDepAfters//?????????????????????????????????????ID?????ID?????????????????ID?????????????????????????????????ID1???ID1????ID1???ID1????ID2?......???ID2???ID2????ID1???ID2????ID2?......?......
         //??????????
         if (AllRect[j].id!='-1' && AllRect[j].id!='0') {
             AfterDeparts=AfterDeparts+";"+AllRect[j].id+GetAfterDepartments(AllRect[j].id);
             DepartsDays=DepartsDays+";"+AllRect[j].id+","+AllRect[j].LimiteDate;
         }
        //????FlowBaseID,DepartID?id?,Name(DepName),PriDepID(??????)
        //LimiteDate(Title)?
        
        var tempElement=xmlDoc.createElement('OnlySp_FlowDetail');
        Root.appendChild(tempElement);
        //FlowBaseID
        var tempFlowBase=xmlDoc.createElement('FlowBaseID');
        //alert(document.all.item('Designer1_FlowBaseID').value)
        tempFlowBase.text=document.all.item('Designer1_FlowBaseID').value;
        tempElement.appendChild(tempFlowBase);
        //DepartID
        var tempDepartID=xmlDoc.createElement('DepartID');
        tempDepartID.text=AllRect[j].id;
        tempElement.appendChild(tempDepartID);
        //DepartName
        var tempDepartName=xmlDoc.createElement('Name');
        tempDepartName.text = formatXML2(AllRect[j].DepName);
        tempElement.appendChild(tempDepartName);
        //NodeAlias
        var tempNodeAlias = xmlDoc.createElement('NodeAlias');
        tempNodeAlias.text = formatXML2(AllRect[j].NodeAlias);
        tempElement.appendChild(tempNodeAlias);
        
        //IT0958-DS012 begin
        //NodeType
        var tempNodeType = xmlDoc.createElement('NodeType');
        tempNodeType.text = AllRect[j].NodeType;
        //alert(tempNodeType.text);
        tempElement.appendChild(tempNodeType);
        //IT0958-DS012 end
        
        //ProcessorField
        var tempProcessorField = xmlDoc.createElement('processorField');
        tempProcessorField.text = AllRect[j].processorField;
        tempElement.appendChild(tempProcessorField);

        //CompanyField
        var tempCompanyField = xmlDoc.createElement('companyField');
        tempCompanyField.text = AllRect[j].companyField;
        tempElement.appendChild(tempCompanyField);

        //DelaytimeField
        var delaytimeField = xmlDoc.createElement('delaytimeField');
        delaytimeField.text = AllRect[j].delaytimeField;
        tempElement.appendChild(delaytimeField);

        //ApproveHandle
        var tempApproveHandle = xmlDoc.createElement('approveHandle');
        tempApproveHandle.text = AllRect[j].approveHandle;
        tempElement.appendChild(tempApproveHandle);

        //RejectHandle
        var tempRejectHandle = xmlDoc.createElement('rejectHandle');
        tempRejectHandle.text = AllRect[j].rejectHandle;
        tempElement.appendChild(tempRejectHandle);

        //ApproveAlias
        var tempApproveAlias = xmlDoc.createElement('approveAlias');
        tempApproveAlias.text = AllRect[j].approveAlias;
        tempElement.appendChild(tempApproveAlias);

        //RejectAlias
        var tempRejectAlias = xmlDoc.createElement('rejectAlias');
        tempRejectAlias.text = AllRect[j].rejectAlias;
        tempElement.appendChild(tempRejectAlias);
        
        //PriDepID
        var tempPriDepID=xmlDoc.createElement('PriDepID');
        tempPriDepID.text=GetPreWorkFlowItems(AllRect[j].id);
        //alert(tempPriDepID.text)??????????????1?????????1???????????????????????
        if(tempPriDepID.text==''){
          nonLineCount++;
        }
        tempElement.appendChild(tempPriDepID);
        //ApproverList
        var tempApproverList = xmlDoc.createElement("ApproverList");
        tempApproverList.text = AllRect[j].ApproverList;
        tempElement.appendChild(tempApproverList);
        //updateSections
        var tempUpdateSections = xmlDoc.createElement("UpdateSections");
        tempUpdateSections.text = AllRect[j].UpdateSections;
        tempElement.appendChild(tempUpdateSections);
        //NewSectionFields
        var tempNewSectionFieldIds = xmlDoc.createElement("NewSectionFieldsx");
        tempNewSectionFieldIds.text = AllRect[j].NewSectionFields;
        tempElement.appendChild(tempNewSectionFieldIds);


        var tempHiddenSectionFieldIds = xmlDoc.createElement("HiddenSectionFields");
        tempHiddenSectionFieldIds.text = AllRect[j].HiddenSectionFields;
        tempElement.appendChild(tempHiddenSectionFieldIds);

        //LimiteDate
        var tempLimiteDate=xmlDoc.createElement('LimiteDate');
        tempLimiteDate.text=AllRect[j].LimiteDate;
        tempElement.appendChild(tempLimiteDate);
        //PosX
        var tempPosX=xmlDoc.createElement('PosX');
        tempPosX.text=AllRect[j].parentElement.style.left;
        tempElement.appendChild(tempPosX);
        //PosY
        var tempPosY=xmlDoc.createElement('PosY');
        tempPosY.text=AllRect[j].parentElement.style.top;
        tempElement.appendChild(tempPosY);
      }
      if(nonLineCount>1){
        throw flowdesign_no_line;
      }
     }
     else
     {
      //???????XML??
     }
    }
    //?????;?
    //????,?
    if(AfterDeparts.length>0)
    {
     AfterDeparts=AfterDeparts.substring(1);
    }
    if(DepartsDays.length>0)
    {
     DepartsDays=DepartsDays.substring(1);
    }
  //??????
  return xmlDoc.xml;
};

//?????????
function GetAfterDepartments(WorkFlowItemID)
{
//???????,?????
  if(WorkFlowItemID=='-1') return "";
    var allLine = document.body.all.item('ConnectLine');
    var i;
    var preWorkFlowItems='';
    
    if (allLine!=null)
    {
     var count=allLine.length;
     if (count)
     {
      for (i=count-1; i>=0; i--)
      {
      //???0???
       if(allLine[i].BeginShape==WorkFlowItemID && allLine[i].EndShape!='-1')
       preWorkFlowItems=preWorkFlowItems+","+allLine[i].EndShape;
      }
     }
     else
     {
       if(allLine.BeginShpae==WorkFlowItemID && allLine.EndShape!='-1')
       preWorkFlowItems=preWorkFlowItems+","+allLine.EndShape;
     }
     //????,?
     //if(preWorkFlowItems.length>0)
     //{
     // preWorkFlowItems=preWorkFlowItems.substring(1);
     //}
     return preWorkFlowItems;
    } 
}
//???????????
function GetPreWorkFlowItems(WorkFlowItemID)
  {
   var allLine = document.body.all.item('ConnectLine');
   var i;
   var preWorkFlowItems='';
   
   if (allLine!=null)
   {
    var count=allLine.length;
    if (count)
    {
     for (i=count-1; i>=0; i--)
     {
     //???0???
      if(allLine[i].EndShape==WorkFlowItemID )
      preWorkFlowItems=preWorkFlowItems+","+allLine[i].BeginShape;
     }
    }
    else
    {
      if(allLine.EndShape==WorkFlowItemID )
      preWorkFlowItems=preWorkFlowItems+","+allLine.BeginShape;
    }
    //????,?
    if(preWorkFlowItems.length>0)
    {
     preWorkFlowItems=preWorkFlowItems.substring(1);
    }
    return preWorkFlowItems;
   }
}

//?WorkFlowItem?ID??????????
function UpdateRel(odlItemID,NewItemID)
{
  var allLine = document.body.all.item('ConnectLine');
   var i;
   
   if (allLine!=null)
   {
    var count=allLine.length;
    if (count)
    {
     for (i=count-1; i>=0; i--)
     {
      if(allLine[i].BeginShape==odlItemID)
      allLine[i].BeginShape=NewItemID;
      if( allLine[i].EndShape==odlItemID)
      allLine[i].EndShape=NewItemID;
     }
    }
    else
    {
      if(allLine.BeginShape==odlItemID)
      allLine.BeginShape=NewItemID;
      if( allLine.EndShape==odlItemID)
      allLine.EndShape=NewItemID;
    }
   }
}
//??????????
function DeleteRelLine(WorkFlowItem)
{
  if(!window.confirm(flowdesign_delete_line))return;
  //??????
  if(WorkFlowItem.className=="NormalLine")
  {
   WorkFlowItem.outerHTML='';
   return;
  }
  //?????ID
  var tempitemid=WorkFlowItem.id;
  //?????
  WorkFlowItem.parentElement.outerHTML='';
  var allLine = document.body.all.item('ConnectLine');
    var i;
    
    if (allLine!=null)
    {
     var count=allLine.length;
     if (count)
     {
      for (i=count-1; i>=0; i--)
      {
       if(allLine[i].BeginShape==tempitemid||allLine[i].EndShape==tempitemid)
       allLine[i].outerHTML='';
      }
     }
     else
     {
       if(allLine.BeginShape==tempitemid|| allLine.EndShape==tempitemid)
       allLine.outerHTML='';
     }
    }
}
//-->
</SCRIPT>
<STYLE>
.menuitems {
	padding: 2px 1px 2px 10px;
}

A {
	COLOR: white;
	TEXT-DECORATION: none
}

A:hover {
	COLOR: yellow;
	TEXT-DECORATION: underline
}

TD {
	FONT-SIZE: 12px
}

.bon2 {
	BORDER-RIGHT: #eeeeee 1px solid;
	BORDER-TOP: gray 1px solid;
	BORDER-LEFT: gray 1px solid;
	WIDTH: 70px;
	COLOR: yellow;
	BORDER-BOTTOM: #eeeeee 1px solid;
	BACKGROUND-COLOR: #619ce7
}

.bon3 {
	border-bottom: 1 solid maroon;
	border-right: 1 solid maroon;
	border-left: 1 solid menu;
	border-top: 1 solid menu;
	background-color: menu;
	color: blue;
	width: 60;
	cursor: hand;
	font-family: ??
}
</STYLE>

<!--PopupMenu Begin--> <SPAN id=WorkFlowItemMenu
	style="DISPLAY: none; position: absolute; z-index: 40000; BACKGROUND-COLOR: menu">
<INPUT class=bon3 id=SelDep
	onclick='WorkFlowItemMenu.style.display="none";SelectDepartment(thisobj);'
	type=button value="Property"> <BR>
<INPUT class=bon3 id=shanchu
	onclick='WorkFlowItemMenu.style.display="none";DeleteRelLine(thisobj)'
	type=button value="Delete"><!--??????????????--> <BR>
<INPUT class=bon3 
	onclick='WorkFlowItemMenu.style.display="none"' type=button
	value=Cancel> </SPAN> <!--PopupMenu End-->

<TABLE id=Table1 height="100%" cellSpacing=0 cellPadding=0 width="100%"
	border=0>
	<TBODY>
		<TR>
			<TD vAlign=top noWrap align=left width="100%" height="100%">
			<DIV class=WorkFlowMap id=Chart
				style="BORDER-RIGHT: silver 0px solid; BORDER-TOP: silver 0px solid; Z-INDEX: 101; LEFT: 0px; BORDER-LEFT: silver 0px solid; WIDTH: 100%; BORDER-BOTTOM: silver 0px solid; POSITION: absolute; TOP: 0px; HEIGHT: 95.25%">
			<v:group id=WorkFlowGroup
				style="WIDTH: 200px; POSITION: absolute; HEIGHT: 200px; v-text-anchor: middle-center"
				coordsize="2000,2000">
				<?xml version="1.0"  encoding="GB2312"?>
				<v:Group id=WorkFlowItemGroup
					style="Z-INDEX: 9000; LEFT: 1000px; WIDTH: 1000px; POSITION: absolute; TOP: 1400px; HEIGHT: 500px"
					xmlns:v="urn:schemas-microsoft-com:vml" coordsize="1000,500">
					<v:roundrect class=WorkFlowItem id=0 title=0
						style="WIDTH: 1000px; POSITION: absolute; HEIGHT: 500px"
						LimiteDate="0" DepName="????" coordsize="21600,21600"
						fillcolor="red" strokecolor="#5082b9">
						<v:fill type="gradient" color2="yellow" angle="0" method="sigma"></v:fill>
						<v:shadow on="t" type="perspective" color="black" opacity="19660f"
							matrix="" offset="2pt,3pt"></v:shadow>
						<v:TextBox
							style="text-align:center;top:10px;font-weight:bold;font-family:Arial;font-size:12px;word-wrap:break-all;">begin</v:TextBox>
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
						LimiteDate="0" DepName="????" coordsize="21600,21600"
						fillcolor="red" strokecolor="#5082b9">
						<v:fill type="gradient" color2="yellow" angle="0" method="sigma"></v:fill>
						<v:shadow on="t" type="perspective" color="black" opacity="19660f"
							matrix="" offset="2pt,3pt"></v:shadow>
						<v:TextBox
							style="text-align:center;top:10px;font-weight:bold;font-family:Arial;font-size:12px;word-wrap:break-all;">End</v:TextBox>
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
			<TD vAlign=top noWrap align=middle width="15%" height="100%">

			<TABLE cellspacing=0 cellpadding=0 align=center
				style='position: absolute; left: expression(document.body.offsetWidth/ 2 -( this.offsetWidth/ 2) ); top: 1; z-index: 3000; cursor: move'
				bgcolor='#D6DFF7'
				onmousedown='if(event.srcElement.tagName=="CENTER"&&event.button==1){moveok=1;setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;}'
				onmousemove="if(moveok==1){this.style.left=event.x-xx;this.style.top=event.y-yy;}"
				onmouseup='moveok=0;releaseCapture();if(parseInt(this.style.top)<-10){alert("??????????????");this.style.top=1}if(event.srcElement.tagName=="CENTER"&&event.button==2){this.style.zIndex==3000?this.style.zIndex=6000:this.style.zIndex=3000;};'>
				<tr>
					<Td style='padding: 3; border-right: 1 solid gray;'>
					<center id=cen>Tool Bar(<b><%=(flow == null ? "" : flow.getFlowName())%></b>)
					</td>
				</tr>
				<Tr>
					<td style='border-right: 1 solid gray'><!-- <button class=bon2 id=huabi onclick="createFlow()">New Work Flow  -->
					<button class=bon2 id=huabi onclick="return btnADD_onclick()">Add
					Node
					<button class=bon2 id=huabi onclick="saveFlow()">Save
					<button class=bon2 id=huabi onclick="goBack()">Back</button>
					</td>
				</tr>
			</table>
			<INPUT type="hidden" id=Designer1_FlowBaseID></TD>
		</TR>
	</TBODY>
</TABLE>
<INPUT id=Designer1_XMLData type=hidden name="Designer1_XMLData">
<!--??FlowBaseID--><INPUT id=Designer1_FlowBase type=hidden value=0
	name=Designer1_FlowBase> <script language="jscript"
	src="<%=request.getContextPath()%>/js/Control.js">
			</script> <script language="jscript">
					ShowAllLine();
			</script></form>
</body>
</HTML>
<form name="flowForm" method="post"><input type="hidden"
	name="xmldata"></form>
<script language="javascript">
  loadFlow();
  //??????????
  var MaxID = "<%=maxNodeId%>";
  MaxID = parseInt(MaxID)
</script>
