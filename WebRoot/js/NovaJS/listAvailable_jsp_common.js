
jQuery().ready(function () {
	var item = jQuery("span[id^=spanListHeart]");
	
	var staffCode = jQuery('input#hidCurrentStaffCode',window.parent.document).val();
	var isParent = false;
	
	if(staffCode==undefined)
	{
		staffCode = jQuery('input[name="currentStaffCode"]',window.parent.document).val();
		isParent = true
	}
	
	if(item.length>0)
	{
		var data,sql,m,form_system_id;
		
		item.each(function(){
			m  = jQuery(this).attr("id").match(/\d{1,100}/);
			form_system_id = m[0];
			
			
			sql = "select form_system_id from TNOVA_favorite where Staff_code = '"+staffCode+"' and form_system_id = '"+form_system_id+"';";
			data = selectFromSQLJson('',sql);
			
			if(data!="" && data.length>0)
			{
				jQuery(this).removeClass("spanListHeartBlank");
				jQuery(this).addClass("spanListHeart");
			}
			
			
			jQuery(this).click(function(){
				m  = jQuery(this).attr("id").match(/\d{1,100}/);
				form_system_id = m[0];
				
				if(jQuery(this).hasClass("spanListHeartBlank"))
				{
					sql = "insert into TNOVA_favorite (Staff_code,form_system_id) values ('"+staffCode+"','"+form_system_id+"');";
					data = selectFromSQLJson('',sql);
					
					if(data==undefined)
					{
						jQuery(this).removeClass("spanListHeartBlank");
						jQuery(this).addClass("spanListHeart");
						if(!isParent)
						{
							window.parent.loadShortCut();
							window.parent.loadTdHover();
						}
					}
					
				}else if(jQuery(this).hasClass("spanListHeart"))
				{
					sql = "DELETE FROM TNOVA_favorite where Staff_code = '"+staffCode+"' and form_system_id = '"+form_system_id+"';";
					data = selectFromSQLJson('',sql);
					
					if(data==undefined)
					{
						jQuery(this).removeClass("spanListHeart");
						jQuery(this).addClass("spanListHeartBlank");
						if(!isParent)
						{
							window.parent.loadShortCut();
							window.parent.loadTdHover();
						}
					}
				}
			});
		});
	}
});
