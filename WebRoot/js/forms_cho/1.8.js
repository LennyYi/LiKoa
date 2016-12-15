function document.onreadystatechange(){
  //overwrite begin
try{
initFinPayBy();
  onload_func();
  onchangeRequester($("request_staff_code").value);

  if(document.getElementsByName("subBtn")[0]) {
            $("subBtn").onmouseover=function(){changeAmount("02_9","04_16");}
  }
  if(document.getElementsByName("addBtn")[0]) {
            $("addBtn").onmouseover=function(){changeAmount("02_9","04_16");}
  }

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
  for(i=0;i<tbl.length;i++){
    sum = sum + parseFloat(tbl[i].value.replace(/,/g,''));
  }
  $("field_"+summary).value = formatNumber(sum, '#,##0.00');
  recountFinal();
}

function changeCAForm(obj){
var strLabel = obj[obj.selectedIndex].text.split("|");
$("field_04_15").value=formatNumber(strLabel[2].trim(),'#,##0.00');
$("field_04_12").value=strLabel[1].trim();
$("field_04_17").value=strLabel[0].trim();
recountFinal();
}

function recountFinal(){
var i=$("field_04_16").value.replace(/,/g,'');//-$("field_04_15").value.replace(/,/g,'');

//$("field_04_7").value=formatNumber(i,'#,##0.00');
$("field_05_1").value=formatNumber(i,'#,##0.00');
$("field_05_17").value=formatNumber(i,'#,##0.00');
}

function onchangeRequester(staffCode) {
   sql = "select right(account_no,4) option_value1, chinese_name option_value2 from teflow_user where rtrim(staff_code)='"+staffCode+"'";
  var xmlObj = selectFromSQL(sql,2);

  $("field_01_3").value="";
  $("field_01_5").value="";
  if(xmlObj){
    var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      $("field_01_3").value= "***********"+options[0].getElementsByTagName("OPTION_VALUE1")[0].childNodes[0].nodeValue;
      $("field_01_5").value= options[0].getElementsByTagName("OPTION_VALUE2")[0].childNodes[0].nodeValue;
        document.getElementsByName("field_01_5")[0].style.color = "rgb(0,0,255)";
    }
  }
}

function rowTotal(ob){
	var i=ob.rowIndex;
	var v7=0;
	var exrate = 1.000;
	var total;
	
	v7=0+document.getElementsByName("field_02_7")[i].value;
	exrate=document.getElementsByName("field_02_10")[i].value;

	total=parseFloat(v7)*exrate;
	document.getElementsByName("field_02_9")[i].value=formatNumber(total,'#,##0.00');
	changeAmount("02_9","04_16");
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
  }
  if(tbl=="formTable02"){
    changeAmount("02_9","04_16");
  }
}

afterSetDay = function(obj){
	if(afterSetDayFlag === 1){
    changeCurrencyByDate('field_02_8','field_02_10',obj);
    rowTotal(obj);
    afterSetDayFlag = 0;
	}
}

//This function used for validation values just before approving process.
function validationUpdate(nodeId){
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

function validationSubmit(){
  notifyEpaymentApplicant();
}


function test(){
 alert("test");
  var sql = "SELECT operate_id, module_id FROM teflow_module_operate WHERE (form_type_id = '13')";
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