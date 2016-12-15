//	====resizeCol.js=========
/*	功能：用拖动来实现动态改变列宽(测试版）
作者：liuzxit
完成时间：2003/7/4
使用方法：	把本js文件在htm文件里引用，然后对指定的table，执行setResizeAble(Table's ID)即可
不足：目前只限定于改变列，不过对于最末列的改变还是会引起一些问题，
	而对于改变行高，功能虽实现，不过操作起来也会引起一些问题，因会
	产生错误提示，所以现在全部注解掉；另外对于cellspacing太大的TABLE会不太好看
备注：在IE5下测试通过
*/

var onDrag=0,gblResizeObj;

function setResizeAble(argTable){	//组件接口函数
	with(argTable){
		attachEvent('onmousedown',colResizeStart)
		attachEvent('onmousemove',colResizeIng)
		attachEvent('onmouseup',colResizeEnd)
//		attachEvent('onmouseout',colResizeOut)		//针对最末列引起的问题加入，但又影响到其他列，还是不加
	}
}

function findPos(obj){		//取得本元素的绝对坐标
	var x=obj.offsetLeft,	y=obj.offsetTop;
	while(obj=obj.offsetParent){x += obj.offsetLeft;y += obj.offsetTop;}
	this.intX=x;	this.intY=y;
	return this
}

function colResizeStart(){	//开始拖动
	var srcObj=event.srcElement,tblParent=srcObj.parentElement;
    if(tblParent.tagName==undefined) return;	
	while(tblParent.tagName!='TABLE')
	{  
	 if(tblParent.parentElement==undefined) return;   
	 tblParent=tblParent.parentElement;
	}

	if(srcObj.tagName!='TD')return
	if(srcObj.parentElement.rowIndex==0){
//		if(srcObj.offsetWidth - event.offsetX <=3 && srcObj.cellIndex!=srcObj.parentElement.cells.length - 1){
		if(srcObj.offsetWidth - event.offsetX <=3){		//如果取消最末列的动态改列宽功能可用上句
			gblResizeObj=tblParent.rows[srcObj.parentElement.rowIndex].cells[srcObj.cellIndex];
			onDrag=1;
		}
		if(event.offsetX <=3 && srcObj.cellIndex != 0){
			gblResizeObj=tblParent.rows[srcObj.parentElement.rowIndex].cells[srcObj.cellIndex - 1];
			onDrag=1;
		}
	}	//else{
		//if(srcObj.offsetHeight - event.offsetY <=3 && srcObj.parentElement.rowIndex < tblParent.rows.length - 1){
		//	gblResizeObj=tblParent.rows[srcObj.parentElement.rowIndex].cells[srcObj.cellIndex];
		//	onDrag=2;
		//}
	//}
}

function colResizeIng(){	//正在拖动
	var objSrcTd=event.srcElement,tblParent=objSrcTd.parentElement;
	while(tblParent=tblParent.parentElement)if(tblParent.tagName=='TABLE')break;
	if(objSrcTd.tagName == "TD" && (Math.abs(event.offsetX) <= 3 || Math.abs(objSrcTd.offsetWidth - event.offsetX)<=3) && objSrcTd.cellIndex != 0 && objSrcTd.parentElement.rowIndex==0){
		objSrcTd.document.body.style.cursor='col-resize';
	}
	else{
//		if(objSrcTd.tagName == "TD" && Math.abs(objSrcTd.offsetHeight - event.offsetY)<=3 && objSrcTd.parentElement.rowIndex > 0 && objSrcTd.parentElement.rowIndex < tblParent.rows.length - 1){
//			objSrcTd.document.body.style.cursor='row-resize';
//		}else{
			objSrcTd.document.body.style.cursor='default';
//		}
	}
	if(onDrag!=1 && onDrag!=2)return;
	var trSrc=gblResizeObj.parentElement;
	
	if(onDrag==1){
		var intPosX=(new findPos(gblResizeObj)).intX
		gblResizeObj.style.pixelWidth = event.x - intPosX;
	}
//	if(onDrag==2){		//改变行高
//		var intHeight=0,intPosY=(new findPos(gblResizeObj)).intY
//		intHeight = objSrcTd.offsetHeight + event.y - intPosY;
//		tblParent.style.pixelHeight = intHeight * tblParent.rows.length
//	}
}

function colResizeEnd(){	//拖动结束
	onDrag=0;
	document.body.style.cursor='default'
}

/*	function colResizeOut(){	//鼠标移出Table范围则拖动结束
	if(event.srcElement.tagName=="TABLE"){
		onDrag=0;
		document.body.style.cursor='default';
	}
}
*/
