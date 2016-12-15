<%@page import="com.aiait.eflow.tableopretion.dao.DatabaseDao"%>
<%@page import="com.aiait.framework.db.DBManagerFactory"%>
<%@page import="com.aiait.framework.db.IDBManager"%>


<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>main</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=request.getContextPath()%>/js/NovaJS/jqGrid/CSS/jquery-ui-custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=request.getContextPath()%>/js/NovaJS/jqGrid/CSS/ui.jqgrid.css" />

<style>
html,body {
	margin: 0;
	padding: 0;
	font-size: 75%;
}
</style>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/grid.locale-en.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery.jqGrid.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/grid.celledit.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function getSelectTable() {
		var selected = null;
		$('#selected option:first').attr('disabled', true);
		selected = $("#selected").find("option:selected").text();
		return selected;
	}

	function sendTableName() {
		var tn = getSelectTable();
		if (tn == "") {
			alert("haha");
		}
		$.post("http://localhost:9090/pagerebuild/jqgridDataServlet?method=table",
				{
				select : tn
				}, function(data) {
				//	alert("提交表名成功！！");
		});
	}
	function deldate(){
 		var datas = null;
		jQuery.ajaxSettings.async = false;
		$.post("http://localhost:9090/pagerebuild/jqgridDataServlet?method=key",{table:getSelectTable()},
			function(data){
				datas = data; 					 
		 		// alert("``````````"+datas);
		 	}
		); 
		var key = datas;
		//alert(key);
		var gr = jQuery("#gridTable").jqGrid('getGridParam', 'selarrrow');
		var my = "";
 		if(gr.length) {
 			for(var i=0;i<gr.length;i++)
 			{
 		  		var myNIF = jQuery('#gridTable').jqGrid('getRowData',gr[i]);
 		  		var mykey = myNIF[key];
 		  		if (my.length > 0){
					my += ",";
				} 		  		 
 		  		my += "{"+ key + ":" + mykey+ "}";
 			}
 		}
 		my = eval("[" + my + "]");
 		//	alert(my);
 		//	alert(mykey);
 	   var delOption = {
 			reloadAfterSubmit : true,
 			url:"http://localhost:9090/pagerebuild/jqgridDataServlet?method=del",
 			delData:{table:getSelectTable(),colkey:my,prinKey:key}
 		};
 	  	var gr = jQuery("#gridTable").jqGrid('getGridParam', 'selarrrow');
 	
 	 	var grmax = Math.max.apply(Math,gr);
  		//alert(grmax);
  		//alert(gr.length);
 	    if (gr != ""  && grmax <= 5 ){
	  		if(confirm("您是否确认删除？")){
	  			jQuery("#gridTable").jqGrid('delGridRow', gr, delOption); 
 	    	}	
   		}
 	    else
 	    alert("Please Select Rows ,NOT BLANK,  to delete!");
	}

	function createTable() {
		var datas = null;
		jQuery.ajaxSettings.async = false;
		var id = $("#selected").find("option:selected").text();
		//tablename = id;
		// alert(id);
		$.post("http://localhost:9090/pagerebuild/jqgridDataServlet?method=col",
				{
					selected : id
				}, 
				function(data) {
					datas = data;
					//	alert("接收到colModelr的数据了！！"+datas);
				}
		);

		var colmodel = datas;
		//alert(colmodel);
		var colmodeljson = eval("(" + colmodel + ")");
		//alert("从字符串变为jsonarray：" + colmodeljson);
		var editRowOption = {
			keys : true,
			restoreAfterError : true,
			oneditfunc : function() {
				alert("editing");
			},
			successfunc : function() {
				// jQuery(this).jqGrid('saveRow', rowid);
				alert("SAVED");
				return true;
			},
			errorfunc : function() {
				alert("error");
			},
			url : "http://localhost:9090/pagerebuild/jqgridDataServlet?method=edit",
			extraparam : {
				table : getSelectTable()
			}
		};

		var addRowOption = {
			keys : true,
			restoreAfterError : true,
			oneditfunc : function() {
			//	alert("start add,firstly add one blank row");
				jQuery(this).jqGrid('addRow', parameters);
			},
			successfunc : function() {
				// jQuery(this).jqGrid('saveRow', rowid);
				alert("success");
				return true;
			},
			errorfunc : function() {
				alert("error");
			},
			url : "http://localhost:9090/pagerebuild/jqgridDataServlet?method=add",
			extraparam : {
				table : getSelectTable()
			}
		};

		parameters = {
			rowID : "new_rowid",
			initdata : [ {} ],
			position : "last",
			useDefValues : true,
			useFormatter : true,
			addRowParams : {
				extraparam : {}
			}
		};
		var lastsel;
		$('#gridTable').jqGrid('GridUnload');
		$("#gridTable").jqGrid(
			{
			postData : {
				select11 : id
			},
			url : 'http://localhost:9090/pagerebuild/jqgridDataServlet?method=context',//获取表内容的
			datatype : "json",
			height : 250,
			colModel : colmodeljson,
			sortname : 'id',
			sortorder : 'asc',
			viewrecords : true,
			rownumbers : true,
			rownumWidth : 20,
			celledit : true,
			cellsubmit : "remote",
			rowNum : 5,
			rowList : [ 5, 10, 15 ],
			multiselect: true,
			refresh : true,
			ondblClickRow : function(rowid) {
				if (rowid <= 5) {
					//alert("edit");
					if (rowid && rowid !== lastsel) {
						jQuery(this).jqGrid('restoreRow',lastsel);
						lastsel = rowid;
					}
				jQuery(this).jqGrid('editRow', rowid, editRowOption);
				} 
				else {
					//alert("add");
					var rid = jQuery(this).jqGrid('getInd',	rowid, false);
					jQuery(this).jqGrid('editRow', rowid, addRowOption);
				}
			},
			loadComplete : function() {
				$("#del_btn").attr("disabled", false); 
				jQuery(this).jqGrid('addRow', parameters);
			},
			editurl : "http://localhost:9090/pagerebuild/jqgridDataServlet?method=",
			jsonReader : {
							repeatitems : false
						},
			pager : "#gridPager"
		}).navGrid('#gridPager', {
					edit : false,
					add : false,
					del : false
			}).setCaption(getSelectTable()).jqGrid('addRow', parameters)
				.trigger("reloadGrid", [ 
				{
					current : true
				} 
				]);
	}
</script>

</head>
<body>
	<%
	IDBManager dbManager = null;
	dbManager = DBManagerFactory.getDBManager();
	DatabaseDao db = new DatabaseDao(dbManager);
		String[] arr = (String[]) db.getTableName();
	%>
	<div>
	<form action="">
		<select id="selected" name="choice"
			onchange="getSelectTable(),sendTableName(),createTable()">
			<option>---请选择表---</option>
			<%
				for (int i = 0; i < arr.length - 2; i++) {
			%>
			<option><%=arr[i]%></option>
			<%
				}
			%>
		</select>
		
	<button id="del_btn" value="delete" disabled="disabled" onclick ="deldate()"  >delete</button>
	</form>
	</div>
	<table class="gridTable" id="gridTable"></table>
	<div id="gridPager"></div>
</body>
</html>