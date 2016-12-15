function document.onreadystatechange(){
	//overwrite begin
try{
	onload_func();
                     onchangeRequester($("request_staff_code").value);
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

function changeAmount(addend,summary){
	var tbl=document.getElementsByName("field_"+addend);
	var sum=0;
	for(i=0;i<tbl.length;i++){
		sum = sum + parseFloat(tbl[i].value);
	}
	$("field_"+summary).value = Math.round(sum);
	recountFinal();
}

function recalMonth(ob){
var tmp=parseFloat(ob.value*65.00);
if($("team_code")[$("team_code").selectedIndex].text.toLowerCase().indexOf("claim")>-1){
  if(tmp>800)tmp=800.00;
} else {
  if(tmp>500)tmp=500.00;
}
$("field_02_10").value=formatNumber(tmp,"#,##0.00");
$("field_05_1").value=formatNumber(tmp,"#,##0.00");
$("field_05_17").value=formatNumber(tmp,"#,##0.00");
}

function recountFinal(){
$("field_04_7").value=$("field_04_2").value-$("field_04_5").value;
}
function onchangeRequester(staffCode) {
   sql = "select right(account_no,4) option_value1, chinese_name option_value2 from teflow_user where rtrim(staff_code)='"+staffCode+"'";
  var xmlObj = selectFromSQL(sql,2);

  $("field_01_3").value="";
  $("field_01_4").value="";
  if(xmlObj){
    var options = xmlObj.getElementsByTagName("item");
    if(options && options.length){
      $("field_01_3").value= "***********"+options[0].getElementsByTagName("OPTION_VALUE1")[0].childNodes[0].nodeValue;
      $("field_01_4").value= options[0].getElementsByTagName("OPTION_VALUE2")[0].childNodes[0].nodeValue;
      document.getElementsByName("field_01_4")[0].style.color =cn_name_color;
    }
  }
}

function afterDel(tbl,chk){
  if(tbl=="formTable06"){
    changeDRCR();
    recalT6();
    reBindTCEvent();
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

//This function used for validation values just before submitting form.
function validationSubmit(){
  notifyEpaymentApplicant();
}