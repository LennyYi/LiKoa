package com.aiait.eflow.common.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import com.aiait.eflow.housekeeping.vo.ViewTeamTypeVO;

public class ScriptBuildTreeHelper {
	
	private static final String NODE_TO_SKIP = "-999";
	
	public static String buildtree(HttpServletRequest request, Collection teamList, StringBuffer buf)throws IOException,Exception{
		StringBuffer strbuf = null;
		if (buf==null){
			strbuf = new StringBuffer(" ");
		}else{
			strbuf = buf;
		}			
		Iterator it = teamList.iterator();
  	  	while(it.hasNext()){    		
  	  	ViewTeamTypeVO vo = (ViewTeamTypeVO)it.next();
  			
  	  		if(NODE_TO_SKIP.equals(""+request.getAttribute(vo.getTeamName().trim()+vo.getTeamCode().toString())))
  	  				continue;//已有节点，不再显示
  	  		
  			strbuf.append("var node"+(vo.getTeamCode())+" = new xyTree.NodeNormal('" + vo.getTeamName().trim()+"');");
  			strbuf.append("node"+vo.getTeamCode()+".id='"+(vo.getTeamCode())+"';");  			
  			if ("-1".equals(vo.getSuperiorsCode())){
  				strbuf.append("treeTeam.add(node"+vo.getTeamCode()+");");
  			}else{  	
  				strbuf.append("node"+vo.getTeamCode()+".fieldType='"+vo.getTeam_type()+"';");
  				strbuf.append("node"+vo.getSuperiorsCode()+".add(node"+vo.getTeamCode()+");");
  			}
  	  		Collection subteamList = (ArrayList)request.getAttribute(vo.getTeamName().trim()+vo.getTeamCode().toString());
  	  		
      		//************************* 取完一个，擦去一个，以免死循环*******************************
      		request.setAttribute(vo.getTeamName().trim()+vo.getTeamCode().toString(), NODE_TO_SKIP);
      		//*************************************************************************************
      		
      		if(subteamList!=null && subteamList.size()>0){
      			buildtree(request, subteamList,strbuf);
      		}
      	}
  	  	return strbuf.toString();
	}
	
}
