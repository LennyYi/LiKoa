package com.aiait.eflow.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.formmanage.vo.DictionaryDataVO;
import com.aiait.eflow.housekeeping.dao.SystemFieldDAO;
import com.aiait.eflow.housekeeping.vo.SystemFieldVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.eflow.housekeeping.vo.StaffVO;

public class SystemFieldUtil {
    public final static String PARAM_USER_COMPANY_CODE = "@orgId";
    public final static String PARAM_USER_TEAM_CODE="@teamCode";
    public final static String PARAM_USER_LOGON_ID="@logonId";
    public final static String PARAM_CURRENT_TIME="@currentTime";
    public final static String PARAM_CURRENT_DATE="@currentDate";
    public final static String PARAM_CATEGORY_ID="@categoryId";
    public final static String PARAM_FORM_ID="@formid";
    
    /**
     * 
     * @param systemFieldId
     * @param currentStaff
     * @param otherParamVauleMap
     * @return
     */
    //获得动态数据，，？？？？？
	public static Collection getDynamicData(int formSystemId, String systemFieldId,StaffVO currentStaff,HashMap otherParamVauleMap){
		Collection list = null;
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   SystemFieldDAO dao = new SystemFieldDAO(dbManager);
		   SystemFieldVO field = dao.getField(systemFieldId);
		   if(field==null) return null;
		   //取得sql语句
		   String dataSQL = field.getSrcSQL();
		   if(dataSQL==null || "".equals(dataSQL)) return null;
		   String[] paramName = new String[]{PARAM_USER_COMPANY_CODE,PARAM_USER_TEAM_CODE,PARAM_CATEGORY_ID,PARAM_FORM_ID};
		   String[] paramValue = new String[]{"'"+currentStaff.getOrgId()+"'","'"+currentStaff.getTeamCode()+"'","'"+(String)otherParamVauleMap.get(PARAM_CATEGORY_ID)
				   +"'","'"+ Integer.toString(formSystemId) + "'" };
	
		   //使用参数值替换sql语句中含有相应的参数
		   for(int i=0;i<paramName.length;i++){
			   dataSQL = StringUtil.replace(dataSQL,paramName[i],paramValue[i]);
		   }
		  // System.out.println(dataSQL);
		   //执行已经格式好的sql语句
		   Collection dataList = dbManager.query(dataSQL);
		   if(dataList==null || dataList.size()==0) return null;
		   list = new ArrayList();
		   Iterator it = dataList.iterator();
		   while(it.hasNext()){
			   HashMap map = (HashMap)it.next();
			   DictionaryDataVO bVo = new DictionaryDataVO();
			   bVo.setId((String)map.get("OPTION_VALUE"));
			   bVo.setValue((String)map.get("OPTION_LABEL"));
			   list.add(bVo);
		   }
		   return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			dbManager.freeConnection();
		}
	}
	
}
