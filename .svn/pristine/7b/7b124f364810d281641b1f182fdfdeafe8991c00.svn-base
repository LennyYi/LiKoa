var canmove=false;
var scaleFactor=10.26;

var tempOffsetX=0;
var tempOffsetY=0;

var mapOffsetX=0;
var mapOffsetY=0;
var Starline;
var activeConcept;
var thisobj=null   //为了完成各种基本编辑功能，如“置前”“复制”“删除”等
var StartPointX;
var StartPointY;
var MaxID=1;
function MoveStart()
{
if(event.srcElement.className!="WorkFlowItem" && event.srcElement.className!="EndLine" &&event.srcElement.className!="WorkFlowStartLine") return;
	if (event.srcElement.className=="WorkFlowItem")
	{
	event.srcElement.parentElement.style.position="absolute";
	activeConcept=event.srcElement.parentElement;
	//window.alert(GetPreWorkFlowItems(event.srcElement.id));
	//CreateXmlData();
	canmove=true;
	}
	tempOffsetX = event.offsetX;
	tempOffsetY = event.offsetY;
	
	mapOffsetX = GetMapOffsetY();
	mapOffsetY = GetMapOffsetX();	
	//为节点间画线而设
	if (event.srcElement.className=="EndLine" || event.srcElement.className=="WorkFlowStartLine" )
		{
		line1.style.display='';
		//保存起始线段
			Starline=event.srcElement;
			//保存开始点信息
			StartPointX=((event.x  - tempOffsetX -mapOffsetX + document.body.scrollLeft ) * scaleFactor)
			StartPointY=((event.y - tempOffsetY - mapOffsetY + document.body.scrollTop ) * scaleFactor+80);
			line1.points.value=StartPointX+","+StartPointY+","+((event.x  - tempOffsetX -mapOffsetX + document.body.scrollLeft ) * scaleFactor)+","+((event.y - tempOffsetY - mapOffsetY + document.body.scrollTop ) * scaleFactor+80);
		}
	Chart.attachEvent('onmousemove',Moving); 
	
}

function Moving()
{

	if (event.button !=1) return;
	
	if(canmove)
	{
		document.selection.empty();
		activeConcept.style.pixelLeft = (event.x  - tempOffsetX -mapOffsetX + document.body.scrollLeft ) * scaleFactor;
		activeConcept.style.pixelTop = (event.y - tempOffsetY - mapOffsetY + document.body.scrollTop ) * scaleFactor;
	
	}
	else
	{
	window.status=event.srcElement.className;	
if(event.srcElement.className=="StartLine")
{
	event.srcElement.style.cursor="hand";
	event.srcElement.title='Put here';
	}

	//记录点的集合
	var EndX=((event.x  - tempOffsetX -mapOffsetX + document.body.scrollLeft ) * scaleFactor);
	var EndY=((event.y - tempOffsetY - mapOffsetY + document.body.scrollTop ) * scaleFactor+80);
	//前趋线段
	//增加点集合使曲线闭合
	if(EndX>StartPointX)
		{
			line1.points.value=StartPointX+","+StartPointY+","+(StartPointX+400)+","+StartPointY+","+(StartPointX+400)+","+EndY+","+EndX+","+EndY+","+(StartPointX+400)+","+EndY+","+(StartPointX+400)+","+StartPointY+","+StartPointX+","+StartPointY;
		
		}
		//后趋线段
		else
		{
			line1.points.value=StartPointX+","+StartPointY+","+(StartPointX+400)+","+StartPointY+","+(StartPointX+400)+","+(EndY+600)+","+EndX+","+(EndY+600)+","+EndX+","+EndY+","+
			EndX+","+(EndY+600)+","+(StartPointX+400)+","+(EndY+600)+","+(StartPointX+400)+","+StartPointY+","+StartPointX+","+StartPointY;
		}
		
		}
}

//显示并调整所有的线段
function ShowAllLine()
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
				UpdateOneLinePos(allLine[i]);
				allLine[i].className = "NormalLine";
			}
		}
		else
		{
			UpdateOneLinePos(allLine);
			allLine.className="NormalLine";	
		}
	}
}

function UpdateOneLinePos(line)
{
	var beginShape;
	var endShape;
	
	beginShape = document.all.item(line.getAttribute("BeginShape")).parentElement;
	endShape = document.all.item(line.getAttribute("EndShape")).parentElement;
	SetJoinLine(beginShape,endShape,line);	
}

function SetJoinLine(fromShape,toShape,Line)
{
	var fromShapeCenterX = GetCenterX(fromShape);
	var fromShapeCenterY = GetCenterY(fromShape);
	
	var toShapeCenterX = GetCenterX(toShape);
	var toShapeCenterY = GetCenterY(toShape);	
	//取得开始点与结束点
	//此处附值时，必须用（括起来。
	var StartX=(fromShape.style.pixelLeft + fromShape.style.pixelWidth+150);
	var StartY=fromShapeCenterY;
	var EndXLine=(toShape.style.pixelLeft-130);
	var EndYLine=toShapeCenterY;
	//设定新的线段点集合
	if(EndXLine>StartX)
	{
		Line.points.value=EndXLine+","+EndYLine+","+(StartX+200)+","+EndYLine+","+(StartX+200)+","+StartY+","+StartX+","+StartY
		+","+(StartX+200)+","+StartY+","+(StartX+200)+","+EndYLine+","+EndXLine+","+EndYLine;
	
	}
	else
	{
		Line.points.value=EndXLine+","+EndYLine+","+EndXLine+","+(EndYLine+500)+","+(StartX+200)+","+(EndYLine+500)+","+(StartX+200)+","+StartY+","+StartX+","+StartY+","+
		(StartX+200)+","+StartY+","+
		(StartX+200)+","+(EndYLine+500)+","+
		EndXLine+","+(EndYLine+500)+","+
		EndXLine+","+EndYLine;
	}
}




function GetCenterX(shape)
{
	 return shape.style.pixelLeft + shape.style.pixelWidth / 2;
}

function GetCenterY(shape)
{
	return shape.style.pixelTop + shape.style.pixelHeight / 2;
}


function EndMove()
{
	canmove=false;
	document.selection.empty();

	
}

function GetMapOffsetY()
{
	var tempMap = Chart;
	var tempY = 0;
	
	while (tempMap.tagName!="BODY")
	{		
		tempY = tempY + tempMap.offsetTop;
		tempMap = tempMap.offsetParent;
	}
	
	return 	tempY + document.body.topMargin ;
}

function GetMapOffsetX()
{
	var tempMap = Chart;
	var tempX=0;
	
	while (tempMap.tagName!="BODY")
	{		
		tempX = tempX + tempMap.offsetLeft;
		tempMap = tempMap.offsetParent;
	}
	
	return 	tempX + document.body.leftMargin ;
}

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
		WorkFlowItemMenu.style.left=event.x;
		WorkFlowItemMenu.style.top=event.y;
		WorkFlowItemMenu.style.display='';
		thisobj=event.srcElement;
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
			  	//window.alert('无法建立关系，可能是关系已经存在或在同一节点上建立关系');
			  	window.alert("It can't build relation！");
			}
		}		
	}
			ShowAllLine();
}

	//判断节点间是否已经有关系了
	function ContainLine(fromline,Toline)
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
					if((allLine[i].BeginShape==fromline.Refid && allLine[i].EndShape==Toline.Refid)||fromline.Refid==Toline.Refid)
					return true;
				}
			}
			else
			{
				if((allLine.BeginShape==fromline.Refid && allLine.EndShape==Toline.Refid)||fromline.Refid==Toline.Refid)
				return true;
			}
				return false;
		}
	}
	document.onclick = EndMove;
	Chart.onmousedown = MoveStart;
	Chart.onmouseup=MouseUP;
