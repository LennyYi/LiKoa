
var remarkIndex=3;

function refreshPage(){
	jQuery('input[name="btnEdit"]').show();
	jQuery('input[name="btnEdit"]').click(function(){
		jQuery('input[type="button"]').show();
		jQuery(this).hide();
	});
	jQuery('input[name="btnSave"]').hide();
	jQuery('input[name="btnSave"]').click(function(){
		jQuery('input[type="button"]').hide();
		jQuery('input[name="btnEdit"]').show();
		jQuery('input[name="btnPrint"]').show();
	});
} 

function editClick(objBtn){
	jQuery('input[name="addBenefits"]').show();
	jQuery('input[name="addRemarks"]').show();
	jQuery(objBtn).hide();
}

function saveClick(objBtn){
	jQuery('input[type="button"]').hide();
	jQuery('input[name="btnEdit"]').show();
	jQuery('input[name="btnPrint"]').show();	
}
//
//jQuery(document).ready(function(){
//	refreshPage();
//});


function SelectRow(objSeleted)
 {
	 var objTD = jQuery(objSeleted).closest('td')[0];
	 objTD.bgColor="#E7EAF5";
	 objTD=jQuery(objTD);
	 var objTR = objTD.closest('tr');
	 if(objTR.attr("name")=="Ben_Dtl"){
		 objTD.find('input[type="button"]').show();
	 }else if(objTR.attr("name")=="Ben_Master"){
		 objTD.find('input[type="button"]').show();
	 }
} 

function delBenDtl(objBtn){
	objBtn = jQuery(objBtn);
	objBtn.closest('tr').remove();
}

function delBenMaster(objBtn){
	objBtn = jQuery(objBtn);
	objBtn.closest('tr').remove();
	jQuery('tr[name="Ben_Dtl"]').remove();
	
}

 function unSelectRow(objSeleted)
 {
// var objTR =objTD.parentElement;
	 var objTD = jQuery(objSeleted).closest('td')[0];
	 objTD.bgColor="#ffffff"; 
	 jQuery(objTD).find('input[type="button"]').hide();
} 
 
 function selectRemark(objTag)
 {
	 objTag.bgColor="#E7EAF5"; 
} 
 
 function unSelectRemark(objTag)
 {
	 objTag.bgColor="#ffffff"; 
} 
 function editRemarkValue(objTag){
	 var objTag = jQuery(objTag);
	 var value = objTag.text();
	 objTag.empty();
	 objTag.append('<textarea style="OVERFLOW-X: visible; OVERFLOW-Y: visible; WIDTH: 700px; text-align:left" class=input title="Reimbursement Amount" required="true" maxLength="100" rowIndex="-1" type="text"></textarea>');
	 //set value
	 var objTextarea = objTag.find('textarea');
	 objTextarea.val(value);
	 objTextarea.blur(function(){
		 saveRemarkValue(this);
	 });
	 objTextarea.focus();
 }
 
 function saveRemarkValue(objTextarea){
	 	var objTextarea = jQuery(objTextarea);
		var objTag = objTextarea.closest('SPAN');
		var value = objTextarea.val();
		if(value==""){
			objTag.closest('p').empty();
			remarkIndex=remarkIndex-1;
		}else{
			//clear TD
			objTag.empty();
			//append value to display
			objTag.append(value);
		}
 }
 
 function addRemarks(){
	 var objRmk=jQuery('p[name="remark_copy"]').clone();
	 objRmk.find('SPAN:first').text(remarkIndex);
	 var objRmkContent=objRmk.find('SPAN:last')
	 objRmkContent.text("");
	 jQuery('p[name="remark_last"]').before(objRmk);
	 editRemarkValue(objRmkContent);
	 remarkIndex=remarkIndex+1;
 }

 function saveFieldValue(objTextarea){
	 	var objTextarea = jQuery(objTextarea);
		var objP = objTextarea.closest('P');
		var value = objTextarea.val();
		//clear TD
		objP.empty();
		//append value to display
		objP.append(value);
 }
 
 function editFieldValue(objTD){
	 objTD=jQuery(objTD);
	 var objP =  objTD.find('P');
	 var value = objP.text();
//	 alert(value);
	 //clear the content of TD
	 objP.empty();
	 //add text area
	 objP.append('<textarea style="OVERFLOW-X: visible; OVERFLOW-Y: visible; WIDTH: 100px; text-align:right" class=input title="Reimbursement Amount" required="true" maxLength="100" rowIndex="-1" type="text"></textarea>');
	 //set value
	 var objTextarea = objP.find('textarea');
	 objTextarea.val(value);
	 objTextarea.blur(function(){
		 saveFieldValue(this);
	 });
	 objTextarea.focus();
 }
 
 function editliValue(objLi){
	 objLi=jQuery(objLi);
	 //get field value
	 var value = objLi.text();
	 //clear the content of TD
	 objLi.empty();
	 //add text area
	 objLi.append('<textarea style="OVERFLOW-X: visible; OVERFLOW-Y: visible; WIDTH: 1500px" class=input title="Company Code" required="true" maxLength="1000" rowIndex="-1" type="text"></textarea>');
	 //set value
	 var objTextarea = objLi.find('textarea');
	 objTextarea.val(value);
	 objTextarea.blur(function(){
		 saveLiValue(this);
	 });
	 objTextarea.focus();
 }
 

 function saveLiValue(objTextarea){
	 	var objTextarea = jQuery(objTextarea);
		var objLi = objTextarea.closest('li');
		var value = objTextarea.val();
		if(value!=""){
			//clear TD
			objLi.empty();
			//append value to display
			objLi.text(value);
		}else{
			objLi.remove();
		}
		
 }
 
 function addRemark(objBtn){
	 var objUl = jQuery(objBtn).closest('h1').next('ul');
	 objUl.append('<li onmouseover="selectLi(this)" onmouseout="unSelectLi(this)" ondblclick="editliValue(this)"></li>');
	 editliValue(objUl.children('li:last'));	 
 }
 
 function addBenefits(objBtn){
	 //get the last product row
	 objLstTR = jQuery('tr[name="lastRow"]:last');
//	 var objProductTR = objLstTR.after('<tr name="APR_Product"></tr>').append('<td></td>').append('<td></td>').append('<td></td>');
	 
	 objLstTR.before('<TR name="newBenefit"><TD><P style="LINE-HEIGHT: 6.5pt; MARGIN: 0.5pt 0in 0pt" class=MsoNormal><SPAN style="FONT-SIZE: 6.5pt">&nbsp;</SPAN></P><P class=s2>Daily Room and Board</P></TD><TD></TD><TD></TD></TR>');
	 objP = jQuery('tr[name="newBenefit"]:last P.s2');
	 var objBenSelect = genBenList(objP);
	 objBenSelect.focus();
	 //add even to dropdown list
	 
 }
 
  
 function genBenList(objP){
	 objP = jQuery(objP);
	 objP.empty();
	 objP.append(' ');
	 objP.append('<SELECT title="Benefit List" onmousewheel=event.returnValue="false" name="Benefit List" required="true"></SELECT>');
	 var objBenSelect=objP.find('select');
	 objBenSelect.append('<OPTION selected value="OR">Operation Room</OPTION>');
	 objBenSelect.append('<OPTION selected value="SS">Simplified Surgical</OPTION>');
	 objBenSelect.blur(function(){
		 loadBenDetail(objP,this);
	 });
	 return objBenSelect;
 }
 
 function loadBenDetail(objP,objBenSelect){
//	 objLstTR.after('<TR><TD><P style="LINE-HEIGHT: 6.5pt; MARGIN: 0.5pt 0in 0pt" class=MsoNormal><SPAN style="FONT-SIZE: 6.5pt">&nbsp;</SPAN></P><P class=s2>Daily Room and Board</P></TD><TD></TD><TD></TD></TR>');
//	 objTD = jQuery(objTD);
//	 objTD.empty();
//	 objTD.append(product);
	 var selectedBenContent = jQuery(objBenSelect).find('option:selected').text();
	 jQuery(objP).empty();
	 jQuery(objP).append(selectedBenContent);
	 var newRow = jQuery('tr[name="addBenefitCopy_dtl"]').clone();
	 newRow.removeAttr("name");
	 newRow.find('p.s4').text("");
	 jQuery(objP).closest('tr').after(newRow);
 }
 
 
//
// function numberToString(value){
//	 value = value.toString();
//	 var intPart = value.split(".")[0];
//	 var intPartLen = intPart.length;
//	 while(){
//		 
//		 if(intPartLen>=3){
//			 intPartLen = intPartLen-3;
//			 slice()
//		 }
//		 
//	 }
//		 substring(3)
// }
// 
// 
// function stringToNumber(value){
//	 var num = new Number(value.replace(",",""));
//	 num = num.toFixed(2);
////	 alert(num);
//	 return num;
// }