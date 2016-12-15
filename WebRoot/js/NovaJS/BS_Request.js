
var lang = jQuery('select[name="field_BS_003_1"] option:selected').text();
jQuery('select[name="field_BS_003_1"]').change(function(){
	 lang = jQuery(this).find("option:selected").text();
	 if(jQuery("select[name='EXCL_ID'] option:selected").text() != null && jQuery("select[name='EXCL_ID'] option:selected").text() != "" ){
		 displayExclusion();
	 }
})

jQuery("td").delegate("select[name='EXCL_ID']","onchange",function(){
	displayExclusion();
})

function displayExclusion(){
	var results="";
	var code = jQuery("select[name='EXCL_ID']").find("option:selected").text();
	jQuery("textarea[name='field_7_2']").empty();

	if(lang == "English only"){
	
		var sql =  "select SEQID,ENGDESC from TNBEXCLUSION where CODEENDDESC = '"+code.toString()+"'";
		var data = selectFromSQLJson('',sql);
		if(data!="" && data != null)
		{
			for(var i = 0 ; i<data.length ; i++){
				SeqID = data[i].SEQID;
				EngDESC = data[i].ENGDESC;
				results += SeqID+'  '+EngDESC+'\r\n';
			}
			results = code+':\r\n'+results;
		}
		else
			return false;
	}else if(lang == "English and Traditional Chinese"){
		var sql =  "select DISTINCT CODETRACHNDESC,SEQID,ENGDESC,TRACHNDESC from TNBEXCLUSION where CODEENDDESC = '"+code.toString()+"'";
		var data = selectFromSQLJson('',sql);
		if(data!="" && data != null){
			Title = data[0].CODETRACHNDESC;
			for(var i = 0 ; i<data.length ; i++){
				SeqID = data[i].SEQID;
				EngDESC = data[i].ENGDESC;
				TrachnDESC = data[i].TRACHNDESC;
				results += SeqID+'  '+EngDESC+TrachnDESC+'\r\n'	;
			}
			results = code +":"+ Title +'\r\n' + results;
		}
	}else if(lang == "English and Simplified Chinese"){
		var sql =  "select DISTINCT CODESIMCHNDESC,SEQID,ENGDESC,SIMCHNDESC from TNBEXCLUSION where CODEENDDESC = '"+code.toString()+"'";
		var data = selectFromSQLJson('',sql);
		if(data!="" && data != null){
			Title = data[0].CODESIMCHNDESC;
			for(var i = 0 ; i<data.length ; i++){
				SeqID = data[i].SEQID;
				EngDESC = data[i].ENGDESC;
				SimchnDESC = data[i].SIMCHNDESC;
				results += SeqID+'  '+EngDESC+'  '+SimchnDESC+'\r\n'	;
			}
			results = code +":"+ Title +'\r\n' + results;
		}
	}
	jQuery("textarea[name='field_7_2']").attr("maxlength",20000);
	jQuery("textarea[name='field_7_2']").attr("rows",5);
	jQuery("textarea[name='field_7_2']").val(results);
}

