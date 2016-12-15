
/*jQuery(document).ready(function(){
var oBody = jQuery(this)[0].body;
var strDate = '';
var strSource = 'js/form_au/AUForm.js'+ strDate;
var oScript1= document.createElement("script");    

if (document.readyState == "complete"||document.readyState ==  "loaded") 
{
oScript1.type = "text/javascript";   
oScript1.src = strSource;
oScript1.language = "javascript";
oBody.appendChild(oScript1);
document.onreadystatechange = null;
}

});*/



var requestNo = jQuery('input[name="request_no"]').val();
var requestUrl = jQuery('input[name="requestUrl"]').val();
var formSystemId = jQuery('input[name="formSystemId"]').val();
var requestFormDate = jQuery('input[name="request_date"]').val();
var currentNodeId;
var operateType;


jQuery().ready(function(){
	//20150305 begin
//	jQuery('#div2 #fieldform tr td').eq(4).hide();
//	jQuery('#div2 #fieldform tr td').eq(5).hide();
//	jQuery('#div2 #sectionTable2 tr td').eq(4).hide();
//	jQuery('#div2 #sectionTable2 tr td').eq(5).hide();
	jQuery('[name="field_2_4"]').parent().hide();
	jQuery('[name="field_2_4"]').parent().prev().hide();
	//20150305 end
	jQuery('#div4 #formTable4 tr td:nth-child(4)').hide();
	jQuery('#div4 #formTable4 tr td:nth-child(5)').hide();
	
	//20150312 begin
	jQuery('input[name="updateBtn"]').hide();	
	//20150312 end
	
	currentNodeId=jQuery("[name='currentNodeId']").val();
	operateType = jQuery("[name='operateType']").val();
	
//	alert(operateType + currentNodeId);

	if(operateType=="view" && currentNodeId!="0"){
		jQuery('#formTable3 tr td:nth-child(1)').remove();
		jQuery('#formTable4 tr td:nth-child(1)').remove();
		jQuery('#formTable5 tr td:nth-child(1)').remove();
		jQuery('#formTable6 tr td:nth-child(1)').remove();
		jQuery('[name="field_4_2"]').each(function() {
			var texts = jQuery(this).val().split(",");
			jQuery(this).before(texts[1]);
		});
		//load the bill from date display
		loadBillFrom(false);
		
	}else if(operateType=="deal" && (currentNodeId=="1" || currentNodeId=="2")){
		setUploadButton();
		//20150312 begin
		jQuery('input[name="exportBtn"]').hide();	
		//20150312 end
		
		//jQuery("#upload",document.frames('iAttachementList3').document).find('input[type="button"]').remove();
		jQuery('#formTable4 tr td:nth-child(1)').remove();
		jQuery('#formTable5 tr td:nth-child(1)').remove();
		jQuery('#formTable6 tr td:nth-child(1)').remove();
		
		jQuery('[name="field_4_2"]').each(function() {
			var texts = jQuery(this).val().split(",");
			jQuery(this).before(texts[1]);
		});
		//load the bill from date display
		loadBillFrom(false);
		
	}else{
		//20150312 begin
		jQuery('input[name="exportBtn"]').hide();	
		//20150312 end
		initBtn("formTable4");
	}
	
	//add total section
	if(currentNodeId=="2" || currentNodeId=="5"  || currentNodeId=="-1" ){//04.Confirm; 05.Pay S.D.; end
//		alert(currentNodeId);
		formatTotal();
	}	

});

function setUploadButton(){
	jQuery("#upload",document.frames('iAttachementList3').document).attr("flag","flag");
	jQuery("#upload",document.frames('iAttachementList3').document).attr("onclick","").click(function() {
		setUpload();
	});
	
	setInterval(function(){
		if(typeof(jQuery("#upload",document.frames('iAttachementList3').document).attr("flag"))!="undefined"){
			return;
		}
		jQuery("#upload",document.frames('iAttachementList3').document).attr("flag","flag");
		jQuery("#upload",document.frames('iAttachementList3').document).attr("onclick","").click(function() {
			setUpload();
		});
	},3000);
}

//20150311 Hinson begin
function loadBillFrom(bEditable){
	if(bEditable){
		var billFromDateVal = jQuery('select[name="field_2_3"]').val();
		var polnoVal = jQuery('textarea[name="field_2_1"]').val();
		var billFromDate = jQuery('select[name="field_2_3"]');
	
		billFromDate.empty();
		if(polnoVal!=''){			
			var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVASDLoadBillFrDt",polnoVal);
			if (data.length>0) {
				jQuery.each(data,function(i,item) {					
					value = item.BILLFROM;
					text = value + "("+item.BILLLIST+")";
					jQuery(billFromDate).append("<option value='" + value + "'>" + text + "</option>");				
				});
				if(billFromDateVal!=''){
					jQuery(billFromDate).val(billFromDateVal);
				}else{
					jQuery(billFromDate).find("option:first-child").attr("selected", true);
				}
			}
		}
	}else{
		var polnoVal = jQuery('input[name="field_2_1"]').val();
		var billFromDateVal = jQuery('input[name="field_2_3"]').val();
		var billFromDate = jQuery('input[name="field_2_3"]');
		billFromDate.closest('td').text("").append(billFromDate);
		if(polnoVal!=''){			
			var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVASDLoadBillFrDt",polnoVal+","+billFromDateVal);
			if (data.length>0) {
				jQuery.each(data,function(i,item) {	
					value = item.BILLFROM;
					text = value + "("+item.BILLLIST+")";
					jQuery(billFromDate).after(text);				
				});
			}
		}
	}
}
//20150311 Hinson end

//20150312 Hinson begin
function setRequiredSingle(field,required){
	var selected = jQuery(field)[0];
	if(required){
		selected.setAttribute('required','true');
	}else{
		selected.setAttribute('required','false');
	}
	if(selected.getAttribute('rowIndex')==-1){	//Single field
		setRequiredFlagSingle(field,required);
	}	
}

function setRequiredFlagSingle(field,required){
	var selected = jQuery(field);
	var fieldDesc = selected.closest('td').prev().text();
	var parenthesesIndex = fieldDesc.indexOf('(');
	if(parenthesesIndex>-1){
		fieldDesc = fieldDesc.substring(0,parenthesesIndex)
	}
	if(required){
		fieldDesc = fieldDesc + '(<FONT color=red><B>*</B></FONT>)'
	}
	selected.closest('td').prev().html("");
	selected.closest('td').prev().append(fieldDesc);
}

function newMemDefOnChange(){
	var newMemDef = jQuery('[name="field_2_5"]');
	var newMemDefVal = jQuery('[name="field_2_5"]').val();
	var since = jQuery('[name="field_2_6"]');

	if(newMemDefVal =="2" && newMemDefChecking(newMemDef)){
		setDisable(since,false);	
		setRequiredSingle(since,true);
		since.change();
	}else{
		setFieldVal(since,"");
		setDisable(since,true);	
		setRequiredSingle(since,false);
	}
//	since.change();
}

function setDisable(operItem,disable){
	if(disable){
		jQuery(operItem).attr("disabled","disabled");
	}else{
		jQuery(operItem).removeAttr("disabled");
	}	
}

function setFieldVal(operItem,newValue){	
	var operItem = jQuery(operItem);
	var oldValue = operItem.val();
	operItem.val(newValue);
	if(oldValue!=newValue || newValue==""){
		operItem.change();
	}
}

function sinceOnChange(){
	var newMemDefVal = jQuery('[name="field_2_5"]').val();
	var since = jQuery('[name="field_2_6"]');
	deletePic(since,0);
	if(newMemDefVal =="2"){
		sinceChecking(since);
	}
}

/*For multiple fields.
 * 
 */
//function setRequiredFlagMult(field,required){
//	var requiredFlag = '(<FONT color=red><B>*</B></FONT>)';
//	var selected = jQuery(field);
//	if(parenthesesIndex>-1){
//		fieldDesc = fieldDesc.substring(0,parenthesesIndex)
//	}
//	if(required){
//		fieldDesc = fieldDesc + '(<FONT color=red><B>*</B></FONT>)'
//	}
//	selected.closest('td').prev().html("");
//	selected.closest('td').prev().append(fieldDesc);
//}
//20150312 Hinson end

function initBtn(tblId) {
	

	setUploadButton();
	
	
	jQuery('div#div5 input[name="addRowBtn"]').remove();
	jQuery('div#div5 input[name="deleteRowBtn"]').remove();
	jQuery('div#div4 input[name="addRowBtn"]').remove();
	jQuery('#formTable5 tr td:first').remove();

	//20150327 begin
	jQuery('div#div6 input[name="addRowBtn"]').remove();
	jQuery('div#div6 input[name="deleteRowBtn"]').remove();
	jQuery('#formTable6 tr td:first').remove();
	//20150327 end

	jQuery('textarea[name="field_2_1"]').change(function() {
		var text = jQuery(this).val();
		if(text!=""){
			jQuery(this).val(("0000000000"+text).substring(text.length,text.length+10));
		}
		//Reset other fields
		jQuery('select[name="field_2_3"]').empty();
//		jQuery('textarea[name="field_2_4"]').val("");
//		jQuery('select[name="field_2_5"]').val("");
//		jQuery('input[name="field_2_6"]').val("");
//		jQuery('input[name="field_2_6"]').blur();
		
		setFieldVal(jQuery('textarea[name="field_2_4"]'),"");
		setFieldVal(jQuery('select[name="field_2_5"]'),"");
		setFieldVal(jQuery('input[name="field_2_6"]'),"");
		
		if(policyChecking(this)){
			loadBillFrom(true);
			//20150311 Hinson begin
			loadDefault();
			//20150311 Hinson end
		}
		});
	
	jQuery('textarea[name="field_2_1"]').blur(function() {
		policyChecking(this);
	});

	//20150311 Hinson begin
//	jQuery('textarea[name="field_2_1"]').change();
	loadBillFrom(true);
	//20150311 Hinson end
	
	jQuery('[name="field_2_5"]').change(function() {
		newMemDefOnChange();
		});
	
	jQuery('[name="field_2_6"]').change(function() {
		sinceOnChange();
		});
	
	newMemDefOnChange();
	
	
//	if(billFromDate.val()!=''){
//		jQuery('select[name="field_2_3"]').val(billFromDate.val());
//	}	
	
	jQuery('[name="field_4_2"]').each(function() {
		jQuery(this).empty().append("<option value=''></option>").append(jQuery('textarea[name="field_2_4"]').val());
		var fieldID = jQuery(this).parent().prev().prev().children(":first").val();
		var prodCode = jQuery(this).parent().prev().children(":first").val();		
		
		var sql ="select field_4_2 from teflow_6_4  WHERE  field_4_4 = '"+fieldID+"' and request_no = '"+requestNo+"'";
		if(prodCode!=''){
			sql=sql+" and field_4_5='"+ prodCode+"'";
		}
		var data = selectFromSQLJson("",sql);
		if(data.length>0){
			jQuery(this).val(data[0].FIELD_4_2);
		}
	});
	
	
	//load.gif
}


function setUpload(){
	
	var attachmentIdentity = jQuery("#attachmentId",document.frames('iAttachementList3').document).val();
	var comments = jQuery('input[name="path"]',document.frames('iAttachementList3').document);      
	if(comments.val().Trim()==""){
		alert("Please select file!");
		comments.focus();
		return;
	}
	comments = jQuery('input[name="fileDescription"]',document.frames('iAttachementList3').document);
	
	if(comments.val().Trim()==""){
		alert("Please fill in fileDescription!");
		comments.focus();
		return;
	}
	
	var fileDesc = comments.val().Trim();
	fileDesc = encodeURIComponent(encodeURIComponent(fileDesc));
	
	requestNo = jQuery('input[name="request_no"]').val();
	var uploadUrl = "";
	if(typeof(requestNo) == "undefined" ||requestNo==null || requestNo==''){//new form
		requestNo = "";
		uploadUrl = requestUrl+"/uploadAction.it?method=uploadTempFile";
	}else{//modify form
		uploadUrl = requestUrl+"/uploadAction.it?method=uploadFormFile&formSystemId="+formSystemId+"&sectionId=3&requestNo="+requestNo;
	}
	
	
	document.iAttachementList3.document.forms[0].action = uploadUrl+"&fileDescription=" + fileDesc
	+ "&requestFormDate="+requestFormDate+ "&attachmentIdentity="+attachmentIdentity;
	
	
	if(validateXls("formTable4")){
		document.iAttachementList3.document.forms[0].submit();
		
	}
}

function validateXls(tblId) {
	var excelPath = jQuery('input[name="path"]',document.frames('iAttachementList3').document).val();
	if (excelPath == null || excelPath == '') {
		alert("Please select a Excel file.");
		return false;
	}
	var fileExtend = excelPath.substring(excelPath.lastIndexOf('.')).toLowerCase();
	if (fileExtend != '.xls' && fileExtend != '.xlsx') {
		alert("The file must be '.xls' or 'xlsx'.");
		return false;
	}
	var billFrom = jQuery('select[name="field_2_3"]').val();
	var polno = jQuery('textarea[name="field_2_1"]').val();
	if(polno=='' || billFrom==''){
		alert("Please input Polno & Bill From. ");
		return false;		
	}
	var billFromSP = billFrom.split("/");
	if(billFrom.length!=10 && billFromSP[2].length!=4){
		alert("The Bill From is incorrect.");
		return false;
	}
	var m = tblId.match(/\d{1,100}/);
	var sessionID = m[0];
					
	var outTimer1,outTimer2

	outTimer1 = setTimeout(function(){ShopConfirm("Loading...");
	clearTimeout(outTimer1);}, 50);

					
	var content					
	outTimer2 = setTimeout(function(){
			content = readXls(excelPath, tblId, sessionID);
			clearTimeout(outTimer2);
			}, 1000);
				

	return true;
}

//20150311 Hinson begin
function loadDefault(){
	var polno = jQuery('textarea[name="field_2_1"]').val();	
//	var newMemDef = jQuery('select[name="field_2_5"]').val();
	if(polno!=''){		
		var data = selectFromSPJson("","dbo.NOVAUspSDLoadDefault",polno);
		if (data.length>0) {
			jQuery.each(data,function(i,item) {					
				newMemDef = item.NEWMEMDEF;				
			});
			//New Member Definition
			jQuery('select[name="field_2_5"]').val(newMemDef);
//			jQuery('select[name="field_2_5"]').combobox("setValue",newMemDef); 
			jQuery('[name="field_2_5"]').change();
		}
	}
}
//20150311 Hinson end

//读取xls文件
function readXls(filePath, tableName, sessionID) {
	var sheet_id = 1; // 读取第1个表
	var row_start = 1; // 从第2行开始读取
	var tempStr = '';

	 
	var birthday = '';
	var effectDay = '';
	var oXL;
	
	try {
		oXL = new ActiveXObject("Excel.application");
	}
	// 创建Excel.Application对象
	catch (err) {
		
		alert(err.message);
	}
	
	var oWB = oXL.Workbooks.open(filePath);
    
	try {
		
		oWB.worksheets(sheet_id).select();
		var oSheet = oWB.ActiveSheet;
		
		//var rowNum = oXL.Worksheets(sheet_id).UsedRange.Cells.Rows.Count;
		var rowNum = oXL.Worksheets(sheet_id).Range("A65536").End(-4162).Row;
		var colNum=oXL.Worksheets(sheet_id).UsedRange.Columns.Count;
		var index, self, value, child, tagName, i2
		var tableRow = jQuery('#' + tableName).find('tr').length;
		var headList='';
		for (var i3=1; i3<=colNum; i3++) {
			headList=headList+"<option value='"+i3+"," + jQuery.trim(oSheet.Cells(1, i3).value) + "'>" + jQuery.trim(oSheet.Cells(1, i3).value) + "</option>"
		}
		jQuery('textarea[name="field_2_4"]').val(headList);
		
		//var data = selectFromSQLJson("","select * from TNovaSDFieldLst");
		var polno = jQuery('textarea[name="field_2_1"]').val();
		var billFromDate = jQuery('select[name="field_2_3"]').find('option:selected').val();
		//20150311 Hinson begin
//		var data = selectFromSPJson("","dbo.NOVAUspSDLoadFieldLst",polno+","+billFromDate);
		var newMemDef = jQuery('select[name="field_2_5"]').find('option:selected').val();
		var data = selectFromSPJson("","dbo.NOVAUspSDLoadFieldLst",polno+","+billFromDate+","+newMemDef);
		//20150311 Hinson end
		if(data.length>0){
							
		for (var i1=row_start; i1<=data.length; i1++) {
			
			i2 = tableRow;
			// 添加新行
			createTableSectionRow(tableName, sessionID, "0");
			tableRow++;
			
			jQuery('#' + tableName).find('tr').eq(i2).find('td').each(
				function(indexD) {					
					if (indexD > 0) {
						self = jQuery(this);
									
						//value = jQuery.trim(oSheet.Cells(1, i1).value);
						child = self.children().eq(0);
						if(indexD==1){
							child.text(data[i1-1].FIELDDESC);
						}else if(indexD==2){
							child.text(data[i1-1].PRODDESC);
							//child.empty().append(headList);
						}else if(indexD==3){
							child.text(data[i1-1].FIELDNAME);
							child.parent().hide();
						}else if(indexD==4){
							child.text(data[i1-1].PRODCODE);
							child.parent().hide();
						}else if(indexD==5){
							child.empty().append("<option value=''></option>").append(headList);
							child.children().each(function(index){
								if(jQuery(this).html()==data[i1-1].HDRNAME){
									jQuery(this).attr("selected",true);
									return false;
								}
							});
						}
					}
				});
		}
		}
		

		oXL.DisplayAlerts = false;
		oWB.Close(savechanges=false);
		oWB = null;
		oXL.Quit();
		oXL.Application.Quit(); 
		oXL = null;
		window.setTimeout(CollectGarbage, 10);
		
	} catch (err) {
		alert(err.message);
	
		//oXL.DisplayAlerts = true;
		if(oXL)
		{
			oXL.DisplayAlerts = false;
			if(oWB)
			{
				oWB.Close(savechanges=false);
				oWB = null;
			}
			oXL.Quit();
			oXL.Application.Quit(); 
			oXL = null;
			window.setTimeout(CollectGarbage, 10);
		}
		closeWindow2();
	}
	
	closeWindow2();	
	return tempStr; // 返回
}



function ShopConfirm(ShowText){ 
	var bHeightS=parseInt(document.documentElement.scrollHeight);
	var bWidth=parseInt(document.body.scrollWidth);
	var bHeight=parseInt(document.body.scrollHeight);
	
	var CenterItem = document.getElementById("formTable4");
	var FexliHeigh = getElementTop(CenterItem);

	
	var ShopConfirmLayer=document.createElement("div");
	ShopConfirmLayer.id="ShopConfirmLayer";
	ShopConfirmLayer.onclick = "closeWindow2()";
	
	ShopConfirmLayer.innerHTML=  "<div id='ShopConfirmLayer'>"+ "</div>";
	var styleStr1="top:0px;left:0px;position:absolute;background:#666;width:"+bWidth+"px;height:"+bHeight+"px;";
	styleStr1 = styleStr1 + "filter:alpha(opacity=20);text-align:center; align=center;"
	ShopConfirmLayer.style.cssText=styleStr1;

	document.body.appendChild(ShopConfirmLayer);

	 var webBgLayer=document.createElement("div");
	 webBgLayer.id="webBgLayer";

	 webBgLayer.innerHTML = "<table class=MsoTableGrid border=0 cellspacing=0 cellpadding=0 >"+
	 "<TR>&nbsp</TR><TR width=150 valign=top ><image src = 'images/Checking/Loading10.gif'/></TR>"+
	 "<TR>&nbsp</TR>"+
	 "<TR width=100 valign=top><strong>" + ShowText + "</strong></TR>"+
	 "</table>"
 
     var styleStr2="top:"+ (FexliHeigh) +"px;left:"+(bWidth/2-150)+"px;position:absolute;"+
	 "background:#B22222;color:white ; width:"+(bWidth/10)+"px;height:"+(bHeightS/10)+"px;"+
	 "border: 1px solid rgb(229, 26, 69); color: rgb(255, 255, 255); text-decoration: none; background-color: rgb(229, 26, 69);" +
	 "text-align:center; align=center;";
 
	 webBgLayer.style.cssText=styleStr2;
	 document.body.appendChild(webBgLayer);
		 
	webBgLayer.focus();
	//alert (document.body.scroll);
	document.body.scroll = 'no'; 
	//document.body.style=="overflow:hidden;"; 
} 

function closeWindow2(){
	
	if(document.getElementById('ShopConfirmLayer')!=null)
		document.getElementById('ShopConfirmLayer').parentNode.removeChild(document.getElementById('ShopConfirmLayer'));
	
	if(document.getElementById('webBgLayer')!=null)
		document.getElementById('webBgLayer').parentNode.removeChild(document.getElementById('webBgLayer'));

	document.body.scroll = 'yes'
}

function getElementTop(element){
	var actualTop = element.offsetTop;
	var current = element.offsetParent;

	while (current !== null){
		actualTop += current.offsetTop;
		current = current.offsetParent;
	}

	return actualTop;
}

//20150312 Hinson begin
function policyChecking(OperItem){
//	deletePic(OperItem ,0)
	var polnoVal = jQuery(OperItem).val();
    var strSql = "SELECT 1 FROM TPOLICY WHERE POLNO = '" + polnoVal + "'";
    var data = selectFromSQLJson('compnwJNDI',strSql);
    
    if (data.length>0)
    {
    	RefreshPic(OperItem,2,"");
    	return true;
    }
    else
    {
    	RefreshPic(OperItem,1,"Invalid policy number");
    	jQuery(OperItem).focus();
    	return false;
    }
}

function sinceChecking(OperItem){
	var sinceVal = jQuery(OperItem).val();
	if(sinceVal==''){
		RefreshPic(OperItem,3,"Please input the date here");
		jQuery(OperItem).focus();
		return false;
	}else{
		RefreshPic(OperItem,2,"");
		return true;
	}
}

function newMemDefChecking(OperItem){
	var newMemDefVal = jQuery(OperItem).val();
	if(newMemDefVal==''){
		RefreshPic(OperItem,1,"Cannot be blank");
		jQuery(OperItem).focus();
		return false;
	}else{
		RefreshPic(OperItem,2,"");
		return true;
	}
}

function RefreshPic(OperItem, Param, ShowText){
	OperItem = jQuery(OperItem)[0];
	 var ShowPic  = "";
	 ShowText = "  " + ShowText;
	 deletePic(OperItem,0);
     
	 switch(Param){
	       case 0: //0 - Loading
	       {
	         ShowPic = "images/Checking/Loading.gif"; 
	         break;
	       }
	       case 1: //1 - Existed
	       {
	         ShowPic = "images/Checking/Invalid.png"; 
	         break;
	       }
	       case 2: //2 - New
	       {
	         ShowPic = "images/Checking/Valid.png"; 
	         break;
	       }
	       case 3: //3 - for ID validation
	       {
	         ShowPic= "images/Checking/Warning.png"; 
	         break;
	       }
	       case 4: //4 - Show Client checking
	       {
	         ShowPic= "images/Checking/Search.png"; 
	         break;
	       }
	       default: //- other         
	       {	//no need process anything
	         deletePic(OperItem,0);
	      	 return true;
	       }
	 }  
	 
	 var heightvar  = 16; 
	 var widthhtvar = 16;
	 
	 
	 var newImg = jQuery('<img width="'+widthhtvar+'" height="'+heightvar+'" id="'+OperItem.name
			 +'_ProcessLoading"  style="width: '+widthhtvar+'px; height: '
			 + heightvar+'px; border-width: 0px; cursor: pointer;" src="'
			 + ShowPic +'" />');
	 
	 var SpaceInput1 = jQuery('<span id="'+OperItem.name + '_ProcessSpace">&nbsp;&nbsp;</span>');
	
	 var SpaceInput2 = jQuery('<span id="'+OperItem.name + '_ProcessContent" style="color:red">&nbsp;'+ShowText+'</span>');
	 //var SpaceInput2 = jQuery('<span id="'+OperItem.name + '_ProcessContent" style="color:red">&nbsp;'+1111111+'</span>');
	 

	 
	 var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	 
	 
	 //alert (CurrentRow + "   "+ ObjectCurrentRow);
	 
	 //if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 || (CurrentRow == ObjectCurrentRow))
	 var ObjectCurrentRow = jQuery("#"+OperItem.name+"_ProcessSpace").closest('tr').index();
	 
	 if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 && (CurrentRow == ObjectCurrentRow))
	 {
		 jQuery("#"+OperItem.name+"_ProcessSpace").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(SpaceInput1);
	 }
	 
	 if(jQuery("#"+OperItem.name+"_ProcessLoading").length>0 && (CurrentRow == ObjectCurrentRow))
	 { 
		 jQuery("#"+OperItem.name+"_ProcessLoading").attr('src',ShowPic);
		 jQuery("#"+OperItem.name+"_ProcessLoading").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(newImg);
	 }

	 if(jQuery("#"+OperItem.name+"_ProcessContent").length>0 && (CurrentRow == ObjectCurrentRow))
	 {
		 jQuery("#"+OperItem.name+"_ProcessContent").text(ShowText);
		 jQuery("#"+OperItem.name+"_ProcessContent").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(SpaceInput2);
	 }
	 
	 jQuery(OperItem).parent().css("text-align","left");
	 jQuery(OperItem).parent().css("vertical-align","bottom");
}

function deletePic(OperItem ,Param){
	OperItem = jQuery(OperItem)[0];
	switch(Param){
	       case 0: //0 - clear code only
	       {
	    		try
	    		{
	    			 //var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	    			 //var ObjectCurrentRow = jQuery("#"+OperItem.name+"_ProcessSpace").index();
	    			 //if(CurrentRow == ObjectCurrentRow)
	    			// {
	    				 jQuery("img[id^="+OperItem.name+"]").hide();
	    				 jQuery("span[id^="+OperItem.name+"]").hide();
	    			// }
	    		}
	    		catch(e)
	    		{
	    			alert("deletePic:0 " + e.message);
	    			return false;
	    		}
	            break;
	       }
	       default: //- other         
	       //no need process anything
	      	 return true;
	}  
}

//20150312 Hinson end

//20150326 Hinson begin

function formatTotal(){
	var objLstTR = jQuery('input[name="field_6_1"]:last').closest('tr');
	objLstTR.css('font-weight', 'bold');//
}


//function addTotal(){
//	/**
//	 * td[0]:Product Code;
//	 * td[1]:Product Short Name;
//	 * td[2]:State;
//	 * td[3]:Headcount;
//	 * td[4]:Apportion;
//	 * td[5]:SD Rate
//	 * td[6]:SD Basis
//	 * td[7]:NetOff 
//	 * td[8]:Paid Prem 
//	 * td[9]:Annualised Growth Prem 
//	 * td[10]:Paid SD 
//	 * td[11]:First/Renewal 
//	 */
//	var objLstTR = jQuery('input[name="field_5_1"]:last').closest('tr');
//	var objNewTR = objLstTR.clone();
//
//	alert(objNewTR.html());
//	objNewTR.find('input').remove();	//remove the hidden input
//	objNewTR.find('td').empty();	//clean the content of TD
//	var objNewTDs = objNewTR.find('td');
//	objNewTDs.eq(0).attr("colspan",3);
//	objNewTDs.eq(0).text("Total:");
//	objNewTDs.eq(1).remove();
//	objNewTDs.eq(2).remove();
//	objLstTR.after(objNewTR);
//}

//20150326 Hinson end

