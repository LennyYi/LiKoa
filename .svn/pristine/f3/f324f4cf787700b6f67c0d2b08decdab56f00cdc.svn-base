 <script language="javascript">
      //定义树使用了命名空间
      var treeFenju3 = new xyTree.DivTreeNormal('Master Data');
      <%
        for(int i=0;i<size;i++){
        	out.println("var node"+i+" = new xyTree.NodeNormal('" + typeVos[i].getFormTypeName()+"');");
        	out.println("node"+i+".id='"+(typeVos[i].getFormTypeId())+"';");
        	out.println("treeFenju3.add(node"+i+");");
        	Collection fieldList = (ArrayList)request.getAttribute(typeVos[i].getFormTypeName());
        	if(fieldList!=null && fieldList.size()>0){
        		Iterator fieldIt = fieldList.iterator();
        		int index = 0;
        		while(fieldIt.hasNext()){
        			BaseDataVO data = (BaseDataVO)fieldIt.next();
        			out.println("var node"+i+"_"+index+" = new xyTree.NodeNormal('" + data.getFieldName() + "');");
        			out.println("node"+i+"_"+index+".id="+data.getMasterId()+";");
        			out.println("node"+i+"_"+index+".fieldType='"+typeVos[i].getFormTypeId()+"';");
        			out.println("node"+i+".add("+"node"+i+"_"+index+");");
        			index++;
        		}
        	}
        }
 %>
 
//这个函数需要用户写
function selectNode(node){
  if(node.level==2){
    document.all['type'].value = "update";
    document.all['masterName'].value = node.name;
    document.all['masterIdShow'].value = node.id;
    document.all['masterId'].value = node.id;
    document.all['fieldType'].value = node.fieldType;
    document.getElementById("div2").style.display  = "block";
    createDetailList(node.id);
  }else if(node.level==1){//如果选定的是第一级的节点（即为字段所属类型），则显示可以新增该类型的master数据
    var result = "<table width='100%'  border='0' cellpadding='0' cellspacing='1' class=sortable id=mytable style='border:1px #8899cc solid;'>"
                +"<tr class='liebiao_tou'>"
                +"<td align='center' ><input type='checkbox' name='allBtn' onclick=selectAll(this,'fieldId')></td>"
                +"<td align='center' >Option Value</td><td align='center' >Option Label</td>"
                +"</tr>"
                +"</table>";
    //document.getElementById("div2").style.display  = "none";
    document.all['type'].value = "new";
    document.all['masterName'].value = "";
    document.all['masterIdShow'].value = "";
    document.all['masterId'].value = "";
    document.all['fieldType'].value = node.id;
    document.getElementById("detailData").innerHTML = result;
  }
}
//初始化方法
function init(){
  document.getElementById('ceshi6').appendChild(treeFenju3.div);
  treeFenju3.init(selectNode);//绑定回调方法
}
//动态获取某个master字段的所有值集合
function createDetailList(masterId){
  var xmlhttp = createXMLHttpRequest();
  var url = "<%=request.getContextPath()%>/baseDataManageAction.it?method=getBaseDataList&masterId=" + masterId+"&rnd="+Math.random();
  var result;
  if(xmlhttp){
     xmlhttp.open('POST',url,false);
     xmlhttp.onreadystatechange = function()
     {
       if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
          result = xmlhttp.responseText;
       }
     }
     xmlhttp.setRequestHeader("If-Modified-Since","0");
     xmlhttp.send(null);
  }
  if(result.Trim()=="fail"){
    alert(basedatamain_fail_detail_list)
  }else{
    document.getElementById("detailData").innerHTML = result;
  }
}
//
function addOption(){
   if(document.all['masterId'].value==""){
     alert(basedatamain_master_data_first);
     return;
   }
   openCenterWindow("<%=request.getContextPath()%>/baseDataManageAction.it?method=enterUpdateOptionLabel&type=new&masterId="+document.all['masterId'].value,350,150);
}
function reviveOption(){
	if(document.all['masterId'].value==""){
	   alert(basedatamain_master_data_first);
	   return;
	}
	if(checkSelect('optionValue')<=0){
	  alert(basedatamain_no_rectodel);
	  return;
	}
	var optionValueStr = getTableSelectRecordStr("optionValue","optionValue");
  	if(confirm("Are you sure?")){
      var url = "<%=request.getContextPath()%>/baseDataManageAction.it?method=reviveOptions&masterId="+document.all['masterId'].value+"&"+optionValueStr;
      var  xmlhttp = createXMLHttpRequest();
      var result;
      if(xmlhttp){
          xmlhttp.open('GET',url,true);
          xmlhttp.onreadystatechange = function()
          {
             if(xmlHttp.readyState!=4){
             	document.getElementById("loading").style.display="block";
             }
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 document.getElementById("loading").style.display="none";
                 if(result.Trim()=="success"){
                   createDetailList(document.all['masterId'].value);
                 }
                 alert(result.Trim());
             }
          }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
  	  }
  	}
}
function deleteOption(){
   if(document.all['masterId'].value==""){
     alert(basedatamain_master_data_first);
     return;
   }
   if(checkSelect('optionValue')<=0){
     alert(basedatamain_no_rectodel);
     return;
   }
   var optionValueStr = getTableSelectRecordStr("optionValue","optionValue");
   if(confirm(basedatamain_sure_to_del)){
      var url = "<%=request.getContextPath()%>/baseDataManageAction.it?method=deleteOptions&masterId="+document.all['masterId'].value+"&"+optionValueStr;
      var  xmlhttp = createXMLHttpRequest();
      var result;
      if(xmlhttp){
          xmlhttp.open('GET',url,true);
          xmlhttp.onreadystatechange = function()
          {
             if(xmlHttp.readyState!=4){
             	document.getElementById("loading").style.display="block";
             }
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 document.getElementById("loading").style.display="none";
                 if(result.Trim()=="success"){
                   alert(basedatamain_suc_to_del)
                   createDetailList(document.all['masterId'].value);
                 }else if(result.Trim()=="fail"){
                   alert(basedatamain_fail_to_del);
                 }else{
                   alert(result.Trim());
                 }
              }
          }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
      }
   }
}
//
function submitForm(){
   var type = document.all['type'].value;
   var url= "<%=request.getContextPath()%>/baseDataManageAction.it?method=updateMasterData&masterId="+document.all['masterId'].value+"&"
          +"masterName="+document.all['masterName'].value.Trim()+"&fieldType="+document.all['fieldType'].value
          +"&type="+type+"&rnd="+Math.random();
   if(document.all['masterName'].value.Trim()==""){
     alert("Master Name can't be null!")
     document.all['masterName'].focus();
     return;
   }
   if(confirm(basedatamain_sure_submit)){
     var  xmlhttp = createXMLHttpRequest();
     var result;
     if(xmlhttp){
          document.getElementById("loading").style.display="block";
          xmlhttp.open('GET',url,true);
          xmlhttp.onreadystatechange = function()
          {
             if(xmlhttp.readyState==1){
  	            document.getElementById("loading").style.display="block";
  	         }
             else if(xmlhttp.readyState==2){
 	           document.getElementById("loading").style.display="block";
             }
             else if(xmlhttp.readyState==3){ 
 	           document.getElementById("loading").style.display="block";
             }else if (xmlhttp.readyState == 4 && xmlhttp.statusText=="OK") {
                if(xmlhttp.status == 200){
                   result = xmlhttp.responseText;
                   document.getElementById("loading").style.display="none";
                   if(result.Trim()=="success"){//update successfully
                     alert(basedatamain_suc_save)
                   }else if(result.Trim()=="fail"){
                     alert("Fail to save!");
                   }else{//new save successfully
                     alert(basedatamain_suc_save);
                     document.all['masterId'].value=result.Trim();
                   }      
                 }
              }
          }
         xmlhttp.setRequestHeader("If-Modified-Since","0");
         xmlhttp.send(null);
      }
   }
}
function selectMaster(){
  if(document.all['masterId'].value==""){
     alert(basedatamain_not_sel_masterf);
     return;
   }
   //根据option_value的长度来计算该字段的长度
   var optionValus = window.document.getElementsByName("optionValue");
   var fieldLength = 0;
   if(optionValus!=null){
     fieldLength = (optionValus[0].value.length + 1) * optionValus.length;
   }else{
    alert(basedatamain_master_no_options);
    return;
   }
   window.opener.document.getElementById("showMasterName").innerHTML = "<b>"+document.all['masterName'].value+"</b>";
   window.opener.document.all['fieldLabel'].value = document.all['masterName'].value;
   window.opener.document.all['masterId'].value = document.all['masterId'].value;
   window.opener.document.all['length'].value = fieldLength;
   window.close();
}

//定义初始化程序
window.onload = init;	
</script>