function document.onreadystatechange(){
  //overwrite begin
try{
  initFinPayBy();
  onload_func();
  onchangeRequester($("request_staff_code").value);

//  if(document.getElementsByName("subBtn")[0]) {
//            $("subBtn").onmouseover=function(){changeAmount("02_9","04_16");}
//  }
//  if(document.getElementsByName("addBtn")[0]) {
//            $("addBtn").onmouseover=function(){changeAmount("02_9","04_16");}
//  }

}catch(e){} 
 
  //overwrite end
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

function initFinPayBy(){
  if($("field_05_2")&&$("field_05_2").value==""){
     $("field_05_2").value = $("field_01_2").value;
  }
}

function checkPayBy(){
   var payby1 = $("field_01_2").value;
   var payby2 = $("field_05_2").value;

  if(payby1 != payby2){
      alert("选择的付款/收款方式与申请人填写的不一样");
  }
}
function changeAmount(addend,summary){
  var tbl=document.getElementsByName("field_"+addend);
  var sum=0;
  for(var i=0;i<tbl.length;i++){
    if(tbl[i].value != ""){
      sum = sum + parseFloat(tbl[i].value.replace(/,/g,''));
    }
  }
  $("field_"+summary).value = formatNumber(sum, '#,##0.00');
  recountFinal();
}

function recountFinal(){
var i=$("field_04_16").value.replace(/,/g,'');//-$("field_04_15").value.replace(/,/g,'');

//$("field_04_7").value=formatNumber(i,'#,##0.00');
$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');

}

function onchangeRequester(staffCode) {
 var  sql = "select right(account_no,4) option_value1, chinese_name option_value2 from teflow_user where rtrim(staff_code)='"+staffCode+"'";
  var xmlObj = selectFromSQL(sql,2);

  $("field_01_3").value="";
  $("field_01_5").value="";
  if(xmlObj){
    var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      $("field_01_3").value= "***********"+options[0].getElementsByTagName("OPTION_VALUE1")[0].childNodes[0].nodeValue;
      $("field_01_5").value= options[0].getElementsByTagName("OPTION_VALUE2")[0].childNodes[0].nodeValue;
      document.getElementsByName("field_01_5")[0].style.color =cn_name_color;
    }
  }
}

function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;
	
	if(document.getElementsByName("field_02_7")[i].value != "") 
                                v7=document.getElementsByName("field_02_7")[i].value.replace(/,/g,'');
	if(document.getElementsByName("field_02_10")[i].value != "")
                                exrate=document.getElementsByName("field_02_10")[i].value.replace(/,/g,'');

	total=parseFloat(v7)*exrate;
                     if (document.getElementsByName("field_02_9")[i] ) 
                            document.getElementsByName("field_02_9")[i].value=formatNumber(total,'#,##0.00');

	changeAmount("02_9","04_16");
                     changeAmount("02_7","04_18");
                     checkRateCal();
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable02"){
    changeAmount("02_9","04_16");
    changeAmount("02_7","04_18");
    checkRateCal();
  }
}

afterSetDay = function(obj){
	if(afterSetDayFlag == 1){
    changeCurrencyByDate('field_02_8','field_02_10',obj);
    rowTotal(obj);
    afterSetDayFlag = 0;
	}
}

//This function used for validation values just before approving process.
function validationUpdate(nodeId){
   if( !checkRateCal() )  return false;
	if($("field_08_3").required == undefined){
                      	return true;
	}

	var dcEqual = $("field_08_3").value;
	if(dcEqual === 'Yes'){
		return true;
	}else{
		alert("相同DB Code下,借贷不相等.");
		return false;
	}
}

//This function used for validation values just before submit form.
function validationSubmit(){
   if( !checkRateCal() )  return false;
  notifyEpaymentApplicant();
}

function checkRateCal(){
  var tbl7=document.getElementsByName("field_02_7");
  var tbl9=document.getElementsByName("field_02_9");
  var tbl10=document.getElementsByName("field_02_10");
  var sum7 = 0;
  var sum9 = 0;
  for(var i=0;i<tbl7.length;i++){
    if(tbl7[i].value == ""){
      sum7 = sum7 + 0 ;
      sum9 = sum9 + parseFloat(tbl9[i].value.replace(/,/g,''));
    }else{
      sum7 = sum7 + parseFloat(tbl7[i].value.replace(/,/g,'')) * parseFloat(tbl10[i].value.replace(/,/g,'')) ;
      sum9 = sum9 + parseFloat(tbl9[i].value.replace(/,/g,''));
    }
  }
  
  if( parseInt(sum7) !=  parseInt(sum9)){
    alert("申请报销金额可能计算不正确，请检查验证!");
    return false;
  }  

  return true;
}

//This function used for validation values just before save form.
function validationSave(){
   if( !checkRateCal() )  return false;
}

function test(){
 alert("test");
  var aa ;
  var v7 = 0;
  var exrate = 1.000;

  v7=0+document.getElementsByName("field_02_7")[0].value;
  exrate=document.getElementsByName("field_02_10")[0].value;
   aa = v7*exrate;

 alert( formatNumber(aa,'#,##0.00'));

  var sql = "select field_02_9, field_02_7, field_02_10 from teflow_88_02 where request_no = '1.2_03222010_09' ";
  var xmlObj = selectDataListFromSQLTest(sql);
}

function selectDataListFromSQLTest(sql) {
    var url = "/formSectionAction.it?method=getXmlDataListBySQL";
    if (document.all['requestUrl']) {
        url = document.all['requestUrl'].value + url;
    }
    var param = "dataSQL=" + sql;
    param = encodeURI(encodeURI(param));
    var result = "";
    var myAjax = new Ajax.Request (
        url,
        {
            method:"post",
            parameters:param,
            asynchronous:false,  // 同步
            setRequestHeader:{"If-Modified-Since":"0"},
            onComplete:function(x) {
                result = x.responseXML;
                alert(x.responseText);
            },
            onError:function(x) {
                alert(x.responseText);
            }
        }
    );
    return result;
}