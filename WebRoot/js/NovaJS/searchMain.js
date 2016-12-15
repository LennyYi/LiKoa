var serarchContext = 'P'; //global parameter serarchContext , P:Policy ; M:Member
var global = new Object();//global parameter
function enterSearch(event){
	var e = event ? event :(window.event ? window.event : null);
	if(e.keyCode==13)  search2reflush();
}

function enterFilter(event,type){
	var e = event ? event :(window.event ? window.event : null);
	if(e.keyCode==13 && 'policy' == type) filterPolicyList();
	else if(e.keyCode==13 && 'client' == type) filterClientList();
}

function detailsByPolicy(searchType,policyNum,clientCode,deptCode,certNo){
	var href = basePath+"/searchAction.it?method=getSearchDetailData&searchType="+searchType+"&policyNumber="+policyNum;
	
	if(document.getElementById("divTop")){		
		document.getElementById("divTop").style.display="none";
	}else{
		parent.document.getElementById("divTop").style.display="none";
	}

	if("policy"==searchType){
		$('#policySlide').slideUp('fast');
		$('#clientSlide').slideUp('fast');
		allround("policyListhead","up");
		allround("clientlisthead","up");
		//setTimeout("$('#policySlide').slideUp('slow');$('#clientSlide').slideUp('slow')",4000);
	}else{
		$('#memberSlide').slideUp('fast');
		allround("memberlisthead","up");
		//setTimeout("$('#memberSlide').slideUp('slow')",4000);
		href = href+"&clientCode="+clientCode+"&deptCode="+deptCode+"&certNo="+certNo;
	}
	
	if(document.getElementById('searchDetailIframe')){		
		//document.frames('searchDetailIframe').location.href=href;
		document.getElementById("searchDetailIframe").contentWindow.location.href=href;
	}else{
		this.location.href=href;
	}
	//$("#searchResultDetailDiv").slideDown('slow');
	$("#searchResultDetailDiv").css({display:''});
	//document.frames('searchDetailIframe').location.reload();
}

function slideUpSearch(searchType){
	if("policy"==searchType){
		$('#policySlide').slideUp('fast');
		$('#clientSlide').slideUp('fast');
		allround("policyListhead","up");
		allround("clientlisthead","up");
	}else{
		$('#memberSlide').slideUp('fast');
		allround("memberlisthead","up");
	}
}


function filterPolicyList(){
	var polPackage = $('#polPackage').val();
	var policy = $('#policy').val();
	var branchCode = $('#branchCode').val();
	var channel = $('#channel').val();
	
	filterPolicyData = new Array();// 定义全局变量存储过滤后的list 对象
	var policyData = global.policyData;
	if(common.isNotEmpty(policyData)){//policyData用于存储 policy list的全局对象变量
		for(var i=0;i<policyData.length;i++){
			var temp = 0;
			if(common.isEmpty(polPackage) || (common.isNotEmpty(polPackage) && policyData[i].PACKAGED == polPackage)){
				temp++;
			}
			if(common.isEmpty(policy) || (common.isNotEmpty(policy) && common.isNotEmpty(policyData[i].POLNO) && policyData[i].POLNO.search(new RegExp(policy,'i')) != -1)){
				temp++;
			}
			if(common.isEmpty(branchCode) || (common.isNotEmpty(branchCode) && common.isNotEmpty(policyData[i].BRANCHCODE) &&  policyData[i].BRANCHCODE.search(new RegExp(branchCode,'i')) != -1)){
				temp++;
			} 
			if(common.isEmpty(channel) || (common.isNotEmpty(channel) && common.isNotEmpty(policyData[i].BUSNSRCE) &&  policyData[i].BUSNSRCE.search(new RegExp(channel,'i')) != -1)){
				temp++;
			}
			/*if(common.isEmpty(subOfficeCode) || (common.isNotEmpty(subOfficeCode) && common.isNotEmpty(policyData[i].SUBOFFCODE) && policyData[i].SUBOFFCODE.search(new RegExp(subOfficeCode,'i')) != -1)){
				temp++;
			}*/
			if(temp ==4 ){
				filterPolicyData.push(policyData[i]);
			}
		}
		cleanTableData('policyListTable');
		appendDataToTable(filterPolicyData,'policyListTable','policy');
		var alink = '<a href=\'javascript:appendDataToTable(filterPolicyData,"policyListTable","policy")\'>More(total:'+filterPolicyData.length+')...</a>';
		document.getElementById('policyListPager').innerHTML = alink;
	}
}
function filterClientList(){
	var polPackage = $('#polPackage2').val();
	var policy = $('#policy2').val();
	var branchCode = $('#branchCode2').val();
	var channel = $('#channel2').val();
	
	filterClientData = new Array();// 定义全局变量存储过滤后的list 对象
	var clientData = global.clientData;
	if(common.isNotEmpty(clientData)){//clientData用于存储 client list的全局对象变量
		for(var i=0;i<clientData.length;i++){
			var temp = 0;
			if(common.isEmpty(polPackage) || (common.isNotEmpty(polPackage) && clientData[i].PACKAGED == polPackage)){
				temp++;
			}
			if(common.isEmpty(policy) || (common.isNotEmpty(policy) && common.isNotEmpty(clientData[i].POLNO) && clientData[i].POLNO.search(new RegExp(policy,'i')) != -1)){
				temp++;
			}
			if(common.isEmpty(branchCode) || (common.isNotEmpty(branchCode) && common.isNotEmpty(clientData[i].BRANCHCODE) &&  clientData[i].BRANCHCODE.search(new RegExp(branchCode,'i')) != -1)){
				temp++;
			} 
			if(common.isEmpty(channel) || (common.isNotEmpty(channel) && common.isNotEmpty(clientData[i].BUSNSRCE) &&  clientData[i].BUSNSRCE.search(new RegExp(channel,'i')) != -1)){
				temp++;
			}
			if(temp ==4 ){
				filterClientData.push(clientData[i]);
			}
		}
		cleanTableData('clientListTable');
		appendDataToTable(filterClientData,'clientListTable','policy');
		var alink = '<a href=\'javascript:appendDataToTable(filterClientData,"clientListTable","policy")\'>More(total:'+filterClientData.length+')...</a>';
		document.getElementById('clientListPager').innerHTML = alink;
	}
}

function filterMemberList(){
	var status = $('#status').val();
	var subOfficeCode = $('#subOfficeCode').val();
	
	filterMemberData = new Array();// 定义全局变量存储过滤后的list 对象
	var memberData = global.memberData;
	if(common.isNotEmpty(memberData)){//memberData用于存储 policy list的全局对象变量
		for(var i=0;i<memberData.length;i++){
			var temp = 0;
			if(common.isEmpty(status) || (common.isNotEmpty(status) && common.isNotEmpty(memberData[i].STATUS) && memberData[i].STATUS.search(new RegExp(status,'i')) != -1)){
				temp++;
			}
			if(common.isEmpty(subOfficeCode) || (common.isNotEmpty(subOfficeCode) && common.isNotEmpty(memberData[i].SUBOFFCODE) && memberData[i].SUBOFFCODE.search(new RegExp(subOfficeCode,'i')) != -1)){
				temp++;
			}
			if(temp ==2 ){
				filterMemberData.push(memberData[i]);
			}
		}
		cleanTableData('memberListTable');
		appendDataToTable(filterMemberData,'memberListTable','member');
		var alink = '<a href=\'javascript:appendDataToTable(filterMemberData,"memberListTable","member")\'>More(total:'+filterMemberData.length+')...</a>';
		document.getElementById('memberListPager').innerHTML = alink;
	}
}
function searchByMainPage(searchContext,searchKey){
	setSerarchContext(searchContext);
	$("#searchKey").val(searchKey);
	search();
}

function search(){  
	var searchKey = $("#searchKey").val().Trim(); 
	$("#searchResultDetailDiv").css({display:'none'});
	if(common.isNotEmpty(searchKey)){
		if(serarchContext == 'P'){
			$("#policyList").css({display:''});
			$("#clientList").css({display:''});
			$("#memberList").css({display:'none'});
			var policyUrl = basePath+"/searchAction.it?method=search&searchType=policy";//&searchKey="+searchKey;
			//policyData = new Object();//定义全局变量存放policyList数据
			cleanTableData("policyListTable");
			generateSerachDataTable("policyListTable","policyListPager",policyUrl,"policyData","policy",searchKey,global);

			var clientUrl = basePath+"/searchAction.it?method=search&searchType=client";
			//clientData = new Object();//定义全局变量存放clientList数据
			cleanTableData("clientListTable");
			 generateSerachDataTable("clientListTable","clientListPager",clientUrl,"clientData","client",searchKey,global);
			
			$('#policySlide').slideDown('fast');
			$('#clientSlide').slideDown('fast');
			allround("policyListhead","down");
			allround("clientlisthead","down");
		}else{
			$("#policyList").css({display:'none'});
			$("#clientList").css({display:'none'});
			$("#memberList").css({display:''});
			
			var memberUrl = basePath+"/searchAction.it?method=search&searchType=member";
			//memberData = new Object();//定义全局变量存放memberList数据
			cleanTableData("memberListTable");
			generateSerachDataTable("memberListTable","memberListPager",memberUrl,"memberData","member",searchKey,global);

			$('#memberSlide').slideDown('fast');
			allround("memberlisthead","down");
		}
	}

	$('#polPackage').val("");
	$('#policy').val("");
	$('#branchCode').val("");
	$('#channel').val("");
	$('#status').val("");
	$('#subOfficeCode').val("");
	
}

function cleanTableData(tableId){
	var existRowNum = document.getElementById(tableId).rows.length;
	for(var i=0; i<existRowNum; i++){ 
		document.getElementById(tableId).deleteRow(0);
	}
	
}

function appendDataToTable(data,tableId,searchType){
	var columnNum = 4;
	var addRowNum = 5;
	if(data&&data!=null){
		var existRowNum = document.getElementById(tableId).rows.length;
		for(var i=0; i<addRowNum; i++){ //每次要增加的行数 
			if(columnNum*(existRowNum+i) < data.length){
				var addRow = document.getElementById(tableId).insertRow(existRowNum+i);
				for(var j=0; j<columnNum; j++){ //每个行要增加的列
					if(columnNum*(existRowNum+i)+j < data.length){
						var cell = addRow.insertCell(j);
						if(searchType == "member"){				
				   			var searchType = '"member"';
				   			var policyNum = '"'+data[columnNum*(existRowNum+i)+j].POLNO+'"';
				   			var depCode = '"'+data[columnNum*(existRowNum+i)+j].DEPCODE+'"';
				   			var clientCode = '"'+data[columnNum*(existRowNum+i)+j].CLNTCODE+'"';
				   			var certNo = '"'+data[columnNum*(existRowNum+i)+j].CERTNO+'"';
							cell.innerHTML = "<a href='javascript:detailsByPolicy("+searchType+","+policyNum+","+clientCode+","+depCode+","+certNo+")'>"+data[columnNum*(existRowNum+i)+j].DISPLAYCOLUMN+"</a>";
						}else{							
							var searchType = '"policy"';
							var policyNum = '"'+data[columnNum*(existRowNum+i)+j].POLNO+'"';
							cell.innerHTML = "<a href='javascript:detailsByPolicy("+searchType+","+policyNum+")'>"+data[columnNum*(existRowNum+i)+j].DISPLAYCOLUMN+"</a>";
						}
					}
				}
			}
		}
	}
}

//var policyData = new Object();
function generateSerachDataTable (tableId,divId,url,dataStoreParaName,searchType,key,globalData){
	var columnNum = 4;
	var addRowNum = 5;
	var dataStoreObject = new Object();
	//globalData = dataStoreObject;
	var searchKey=encodeURIComponent(encodeURIComponent(key));	
	//tableId = "#"+tableId;
	var dataReturn = function(data){
		globalData[dataStoreParaName] = data;
		appendDataToTable(data,tableId,searchType);
		var alink = '<a href=\'javascript:appendDataToTable(global.'+dataStoreParaName+',"'+tableId+'","'+searchType+'")\'>More(total:'+data.length+')...</a>';
		document.getElementById(divId).innerHTML = alink;
	}
	$.ajax({
  	  	type: "POST", 
        url: url, 
        data : {"searchKey" : searchKey},
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        async: true,
        beforeSend : function() {
        /*	 var sta="dialogWidth:310px;dialogHeight:110px;status:no;help:no";
        	  window.showModalDialog("../common/loadBox.jsp",window,sta);*/
			$(".ui-overlay").css({
				display : 'block'
			});
		},
		success: dataReturn, 
		complete : function() {
			/* var sta="dialogWidth:310px;dialogHeight:110px;status:no;help:no";
			  window.showModalDialog("completeBox.html",window,sta);*/
			$(".ui-overlay").css({
				display : 'none'
			});
		},
        error: function (XMLHttpRequest, textStatus, errorThrown) { 
  				result = "";
        } 
	});
	
	return dataStoreObject;
}



function initJqGrid(url,tableId,pagerId,columnNames,columnModel,caption,postData,defaultFilter){
	for(var i in columnModel){
		columnModel[i].searchoptions = eval('(' + columnModel[i].searchoptions + ')'); 
	}
	$("#"+tableId).jqGrid({
	   	url:url,
		datatype: 'json',
	   	colNames:columnNames,
	   	colModel:columnModel,
	   	rowNum: 10,
		rowList : [10,20,30,50],
	   	mtype: "GET",
	   	loadonce:true,
	   	rownumbers: true,
		gridview: true,
	   	pager: "#"+pagerId,
	    viewrecords: true,
	    autowidth: true,
	    height: 200,
	    ignoreCase: true,
		caption: caption,
		postData:postData,
		shrinkToFit:false,
		/*gridComplete:function(){
			
		var date = $("td[aria-describedby$='DATE']").text().substr(0, 10);
			var day = $("td[aria-describedby$='DAY']").text().substr(0, 10);
		$("td[aria-describedby$='DATE']").text(date);
		$("td[aria-describedby$='DAY']").text(day);
		$("td[title='']").text("");
		},*/
		loadComplete:triggerFilter('#'+tableId)
		//autoScroll: true

	});
	
	$("#"+tableId).jqGrid('filterToolbar',{searchOperators : true});
	//setTimeout("triggerFilter ('"+tableId+"')",5000);
}


// triggerFilter function begin; only call 3 times setTimeout() that can trigger all jqgrid to filter
function triggerFilter (id) {
	window.setTimeout("triggerFilter3('"+id+"')",10);
	//alert(label);
}
function triggerFilter3(id){
	window.setTimeout("triggerFilter2('"+id+"')",10);
	//window.setTimeout("$('"+id+"').jqGrid('filterToolbar',{searchOperators : true})[0].triggerToolbar()",50);
}
function triggerFilter2(id){
	window.setTimeout("triggerFilter1('"+id+"')",10);
}
function triggerFilter1(id){
	$(id).jqGrid('filterToolbar',{searchOperators : true})[0].triggerToolbar();
}
//triggerFilter function end 

function setSerarchContext(context){
	serarchContext = context;
}

function goMemberList(value, grid, rows){		
		var searchType = '"member"';
		var policyNum = '"'+rows.POLNO+'"';
		var depCode = '"'+rows.DEPCODE+'"';
		var clientCode = '"'+rows.CLNTCODE+'"';
		var certNo = '"'+rows.CERTNO+'"';
		return "<a href='javascript:detailsByPolicy("+searchType+","+policyNum+","+clientCode+","+depCode+","+certNo+")'>"+rows.CERTNO+"</a>";
}


function getResult() {//获取结果结合的函数，可以通过此函数获取查询后匹配的所有数据行。
    var o = jQuery("#poli_memb_table");
    var rows = o.jqGrid('getRowData'); //获取当前显示的记录
    console.log(rows)

    var rowNum = o.jqGrid('getGridParam', 'rowNum'); //获取显示配置记录数量
    var total = o.jqGrid('getGridParam', 'records'); //获取查询得到的总记录数量
    o.jqGrid('setGridParam', { rowNum: total }).trigger('reloadGrid'); //设置rowNum为总记录数量并且刷新jqGrid，使所有记录现出来调用getRowData方法才能获取到所有数据
    var rows = o.jqGrid('getRowData');  //输出所有匹配的

    o.jqGrid('setGridParam', { rowNum: rowNum }).trigger('reloadGrid'); //还原原来显示的记录数量
    return rows;
}


function export2Execl(jqId,fileName){
	
	var reccount = $("#"+jqId).getGridParam("reccount");
	var result = getJQGridData(jqId);
	var rowString = JSON.stringify(result.rows);
	var filterColModel = JSON.stringify(result.filterColModel);
	var filterColNames = JSON.stringify(result.filterColNames);
	var url = basePath+"/searchAction.it?method=export2Execl";
if(reccount>0){
	if(!window.parent.document.getElementById('download_frame')){
		$(window.parent.document.body).append("<iframe id=download_frame name=download_frame style='display:none;margin:0px;padding:0px;' src=''></iframe>");
	}
	
	if(!$("#download_form")[0]){
		$(document.body).append("<form method='post' id='download_form' action='"+url+"'" +
				"' style='display:hide;margin:0px;padding:0px;' target=download_frame >" +
				"<input type=hidden name=rows id=rows value='" + rowString+"'/>" +
				"<input type=hidden name=colModels id=colModels value='" + filterColModel+"'/>" +
				"<input type=hidden name=colNames id=colNames value='" + filterColNames+"'/>" +
				"<input type=hidden name=fileName id=fileName value='" + fileName+"'/>" +
				"</form>");
	} else {
		$("#rows").val(rowString);
		$("#colModels").val(filterColModel);
		$("#colNames").val(filterColNames);
	}
	$("#download_form").submit();
	
    }else if(reccount===0){
    	displayDialog();
    	$(".exportButton").live('click',function( event ) {
    		$( "#dialog" ).dialog( "open" );
    		event.preventDefault();
    	});
    	
    }
}

function displayDialog(){
   
    	var px =window.screen.height/2;
 		var py =window.screen.width/2;
 		$( "#dialog" ).css({
 			display : 'block'
 		});
 		$( "#datamessage" ).css({
 			display : 'block'
 		});
 		$( "#dialog" ).dialog({
 			autoOpen: false,
 			width: 200,
 			height:160,
 			position: [px,py],
 			buttons: [
 				{
 					css:{ "color": "#000000", "background": "gray" },
 					text: "Ok",
 					click: function() {
 						$( this ).dialog( "close" );
 					}
 				},
 				{
 					text: "Cancel",
 					css:{ "color": "#000000", "background": "gray" },
 					click: function() {
 						$( this ).dialog( "close" );
 					}
 				}
 			],
 			close: function(event, ui) {$(this).dialog("destroy")}
 		});
     
}

function getJQGridData(jqId) {//获取结果结合的函数，可以通过此函数获取查询后匹配的所有数据行。
    var o = jQuery("#"+jqId);
    
    var rows = o.jqGrid('getRowData'); //获取当前显示的记录
    var rowNum = o.jqGrid('getGridParam', 'rowNum'); //获取显示配置记录数量
    var total = o.jqGrid('getGridParam', 'records'); //获取查询得到的总记录数量
    o.jqGrid('setGridParam', { rowNum: total }).trigger('reloadGrid'); //设置rowNum为总记录数量并且刷新jqGrid，使所有记录现出来调用getRowData方法才能获取到所有数据
    var rows = o.jqGrid('getRowData');  //输出所有匹配的
    o.jqGrid('setGridParam', { rowNum: rowNum }).trigger('reloadGrid'); //还原原来显示的记录数量
    
    var colModel = o.jqGrid('getGridParam','colModel');
    var colNames = o.jqGrid('getGridParam','colNames');
    var filterColModel = new Array();
    var filterColNames = new Array();
    for(var i = 0,j=0 ; i < colModel.length ; i++){
    	if(!colModel[i].hidden && "rn"!=colModel[i].name){
    		filterColModel[j] = colModel[i].name;
    		filterColNames[j] = colNames[i];
    		j++
    	}
    }
    var result = new Object();
    result.rows = rows;
    result.filterColModel = filterColModel;
    result.filterColNames = filterColNames;
    return result;
}

function allround(obj_id,action){
	if (action==""){
		if (!jQuery("#"+obj_id).hasClass("optitem2")){
			setTimeout("jQuery('#"+obj_id+"').addClass('optitem2');", 500);
		}
		else{
			jQuery("#"+obj_id).removeClass("optitem2");
		}
	}
	else{
		if (action=="up"){
			jQuery("#"+obj_id).addClass("optitem2");
		}
		else{
			jQuery("#"+obj_id).removeClass("optitem2");
		}
	}
}
function search2reflush(){
	var url=""
	var prefix= getRootPath();
	var searchKey = $("#searchKey").val().Trim();
	searchKey=encodeURIComponent(searchKey);
	var encodeSearchKey =encodeURIComponent(encodeURIComponent(encodeURIComponent(searchKey)));
	if(serarchContext=="P")
	url = prefix+"/index.jsp?FramePage=search%2FsearchMain.jsp&searchContext=P&encodeSearchKey="+encodeSearchKey+"&searchKey="+searchKey;
	else if(serarchContext=="M")
	url = prefix+"/index.jsp?FramePage=search%2FsearchMain.jsp&searchContext=M&&encodeSearchKey="+encodeSearchKey+"&searchKey="+searchKey;
	parent.window.location.href=url; 
	
}

function getRootPath(){
		var curWwwPath=window.document.location.href;
		var pathName=window.document.location.pathname;
		var pos=curWwwPath.indexOf(pathName);
		var localhost=curWwwPath.substring(0,pos);
		var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
		return  (localhost+projectName)
}
//用于做baseview的方案
//function search4View(){
//	//获取当前单元格herf中的值或关键字
//	var herfVal = $(this).attr("href").val();
//	alert(herfVal);
//	var dataReturn=function(){};
//	$.ajax({
//	  	type: "POST", 
//      url: url, 
//      data : {"searchKey" : searchKey},
//      dataType: "json",
//      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//      async: true,
//      beforeSend : function() {
//       
//		},
//		success: dataReturn, 
//		complete : function() {
//		
//		},
//      error: function (XMLHttpRequest, textStatus, errorThrown) { 
//				result = "";
//      } 
//	
//	
//}