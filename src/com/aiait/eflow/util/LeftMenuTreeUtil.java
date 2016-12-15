package com.aiait.eflow.util;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.ModuleDAO;
import com.aiait.eflow.housekeeping.vo.ModuleVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.framework.db.ConnectionException;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;


public class LeftMenuTreeUtil {
   
	 public static String buildTree(HttpServletRequest request){
		return buildTree(request,0);
	}
	 
	 public static String buildTree(HttpServletRequest request,int topLevel){
		    StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
			String roleId = staff.getCurrentRoleId();
			IDBManager dbManager = null;	
			StringBuffer treeHtml = new StringBuffer("");
			try{
			   dbManager =  DBManagerFactory.getDBManager();
			   ModuleDAO dao = new ModuleDAO(dbManager);
	           //每个用户都应该有默认角色“roleId='00'”包含的权限
			   roleId = roleId + CommonName.MODULE_ROLE_SPLIT_SIGN + "00";
			   Collection topList = dao.getLowerList(0,roleId);
			   Iterator it = topList.iterator();
			   int count = 1;
			   while(it.hasNext()){
				 ModuleVO module = (ModuleVO)it.next();
				 treeHtml.append("<tr><td>&nbsp;</td></tr>")
				         .append("<tr><td>")
				         .append("<table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				 //默认展开第一个menu，其他menu都是未展开状态
				 if(count>1){
				   treeHtml.append("<tr><td height=\"25\" background=\"images/"+module.getImageFileName()+"\" onclick=showmenu("+count+") class=\"td\">&nbsp; <strong>"+module.getModuleName()+"</strong></td></tr>");
				   treeHtml.append("<tr id='menu_"+count+"' style='display:none'>");
				 }else{
				   treeHtml.append("<tr><td height=\"25\" background=\"images/"+module.getImageFileName()+"\" onclick=hidmenu("+count+") class=\"td\">&nbsp; <strong>"+module.getModuleName()+"</strong></td></tr>");
				   treeHtml.append("<tr id='menu_"+count+"' style='display:block'>"); 
				 }
				 treeHtml.append("<td class=\"sec_menu\">")
				         .append("<table cellpadding=0 cellspacing=0 align=center width=150>")
				         .append("<tr><td height=5></td></tr>");
				 
			   	 treeHtml.append(buildItem(request,module.getModuleId(),roleId,dao));
			   	
			   	 treeHtml.append("</table></td>")
			   	         .append("</tr></table>")
			   	         .append("</td></tr>");
			   	 
			   	 count++;
			   }	   
			}catch(ConnectionException e){
			   return "Can not get the database connection, the error is : " + e.getMessage();
			}catch(DAOException ex){
			   return "When build the left menu happen error, the error is : " + ex.getMessage();
			}finally{
				dbManager.freeConnection();
			}
		   	return treeHtml.toString();
		   }
		   
		public static String buildItem(HttpServletRequest request,int parentId,String roleId,ModuleDAO dao)throws DAOException{
		   	Collection list = dao.getLowerList(parentId,roleId);
		   	StringBuffer itemHtml = new StringBuffer("");
		   	if(list!=null && list.size()>0){
		   		Iterator it = list.iterator();
		   		while(it.hasNext()){
				  ModuleVO module = (ModuleVO)it.next();
				  itemHtml.append("<tr><td height=20><img alt src=\"images/"+module.getImageFileName()+"\"")
				          .append(" border=\"0\" width=\"15\" height=\"20\">")
				          .append("<a href=\""+request.getContextPath()+"/").append(module.getTargetUrl())
				          .append("\" target='main'>").append(module.getModuleName()).append("</a></td>")
				          .append("</tr>");
				           
		   		}
		   	  return itemHtml.toString();
		   	}else{
		   	  return "";
		   	}
		   }
		
		 public static String buildTree2(HttpServletRequest request){
				return buildTree2(request,0);
		}
		
		public static String buildTree2(HttpServletRequest request,int topLevel){
			    String defaultMenuItem = (String)request.getParameter("menuItemId");
			    
			    StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
				String roleId = staff.getCurrentRoleId();
				IDBManager dbManager = null;	
				StringBuffer treeHtml = new StringBuffer("");
				try{
				   dbManager =  DBManagerFactory.getDBManager();
				   ModuleDAO dao = new ModuleDAO(dbManager);
		           //每个用户都应该有默认角色“roleId='00'”包含的权限
				   roleId = roleId + CommonName.MODULE_ROLE_SPLIT_SIGN + "00";
				   Collection topList = dao.getLowerList(0,roleId);
				   Iterator it = topList.iterator();
				   int count = 2;
				   String moduleName = "";
				   String imgURL = "";
				   
				   while(it.hasNext()){
					 ModuleVO module = (ModuleVO)it.next();
					 moduleName = module.getModuleName();
					 if(moduleName==null){
						 moduleName = "";
					 }
					 
					 imgURL = "images/" + module.getImageFileName();
					 
					 if(defaultMenuItem!=null && !"".equals(defaultMenuItem)){
						if(defaultMenuItem.equals(""+module.getModuleId())){
							 treeHtml.append("taskMenu"+count+" = new TaskMenu(\""+ I18NMessageHelper.getMessage(moduleName.trim())+"\",1,\""+imgURL+"\");");
						}else{
						     treeHtml.append("taskMenu"+count+" = new TaskMenu(\""+I18NMessageHelper.getMessage(moduleName.trim())+"\",0,\""+imgURL+"\");"); 
						}
					 }else{
					   if(count==2){
					     treeHtml.append("taskMenu"+count+" = new TaskMenu(\""+I18NMessageHelper.getMessage(moduleName.trim())+"\",1,\""+imgURL+"\");");
					   }else{
					     treeHtml.append("taskMenu"+count+" = new TaskMenu(\""+I18NMessageHelper.getMessage(moduleName.trim())+"\",0,\""+imgURL+"\");"); 
					   }
					 }
					 treeHtml.append(buildItem2(request,module.getModuleId(),roleId,dao,"taskMenu"+count));
					 treeHtml.append("taskMenu"+count+".init();");
				   	 count++;
				   	 if("1".equals(request.getSession().getAttribute("nonIE")))break;//！！iPAD访问 ！！
				   }	   
				}catch(ConnectionException e){
				   return "Can not get the database connection, the error is : " + e.getMessage();
				}catch(DAOException ex){
				   return "When build the left menu happen error, the error is : " + ex.getMessage();
				}finally{
					dbManager.freeConnection();
				}
			   	return treeHtml.toString();
		}
		
		public static String buildItem2(HttpServletRequest request,int parentId,String roleId,ModuleDAO dao,String parentItem)throws DAOException{
		   	Collection list = dao.getLowerList(parentId,roleId);
		   	StringBuffer itemHtml = new StringBuffer("");
		   	if(list!=null && list.size()>0){
		   		Iterator it = list.iterator();
		   		int count = 1;
		   		String moduleName = "";
		   		while(it.hasNext()){
				  ModuleVO module = (ModuleVO)it.next();
				  moduleName = module.getModuleName();
				  if(moduleName==null){
						 moduleName = "";
				  }
				  itemHtml.append("item_"+parentId+"_"+count+"=new TaskMenuItem(\""+I18NMessageHelper.getMessage(moduleName.trim())+"\",\"images/"+module.getImageFileName()+"\",\"parent.window.frames[1].location.href='"+module.getTargetUrl()+"'\");")
				          .append(""+parentItem+".add("+"item_"+parentId+"_"+count+");");
				  count++;
		   		}
		   	  return itemHtml.toString();
		   	}else{
		   	  return "";
		   	}
		   }
			   
}
