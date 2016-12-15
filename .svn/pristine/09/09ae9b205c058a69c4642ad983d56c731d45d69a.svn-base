package com.aiait.eflow.housekeeping.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.housekeeping.dao.ApproverGroupDAO;
import com.aiait.eflow.housekeeping.dao.ApproverGroupMemberDAO;
import com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO;
import com.aiait.eflow.housekeeping.vo.ApproverGroupVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class ApproverGroupAction extends DispatchAction {
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listApproverGroup(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listPage";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  ApproverGroupDAO dao = new ApproverGroupDAO(dbManager);
		  Collection list = dao.getApproverGroupList();
		  request.setAttribute("resultList",list);
		}catch(Exception e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * enter the edit page
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterEditPage(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "editPage";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null || "".equals(saveType)){
			saveType = "new";
		}
		if(!"new".equals(saveType)){
			String groupId = (String)request.getParameter("groupId");
			IDBManager dbManager = null;
			try{
				  dbManager =  DBManagerFactory.getDBManager();
				  ApproverGroupDAO dao = new ApproverGroupDAO(dbManager);
				  ApproverGroupVO group = dao.getById(groupId);
				  request.setAttribute("approverGroup",group);
				}catch(Exception e){
					e.printStackTrace();
					returnLabel = "fail";
				}finally{
					if(dbManager!=null) dbManager.freeConnection();
				}
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * save the approver group
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveApproverGroup(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "saveSuccess";
		String saveType = (String)request.getParameter("saveType");
		if(saveType==null || "".equals(saveType)){
			saveType = "new";
		}
		ApproverGroupVO group = new ApproverGroupVO();
		group.setGroupId((String)request.getParameter("groupId"));
		group.setGroupName((String)request.getParameter("groupName"));
		group.setGroupType((String)request.getParameter("groupType"));
		group.setDescription((String)request.getParameter("description"));
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   ApproverGroupDAO dao = new ApproverGroupDAO(dbManager);
		   if("new".equals(saveType)){
			   ApproverGroupVO temp =  dao.getById(group.getGroupId());
			   if(temp!=null){
				   out.print("Group Id ("+group.getGroupId()+") already exists!");
				   return null;
			   }
			   dao.save(group);
		   }else{
			   dao.update(group);
		   }
		   out.print("success");
		}catch(Exception e){
		   e.printStackTrace();
		   out.print(e.getMessage());
		   //returnLabel = "fail";
		}finally{
			if(out!=null) out.close();  
		   if(dbManager!=null) dbManager.freeConnection();
		}
		return null;
		//return mapping.findActionLocation(returnLabel);
	}
	
	/**
     * delete the selected records
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteApproverGroup(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "saveSuccess";
        String[] groupIds = request.getParameterValues("groupId");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ApproverGroupDAO dao = new ApproverGroupDAO(dbManager);
            ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
            dbManager.startTransaction();
            ApproverGroupVO group = new ApproverGroupVO();
            // 如果该group已经被流程引用了，则不允许删除
            for (int i = 0; i < groupIds.length; i++) {
                if (dao.checkGroupUsed(groupIds[i])) {
                    request.setAttribute("error", "Authorized Group(" + groupIds[i]
                            + ") can't be deleted, because it is used by workflow!");
                    return mapping.findActionLocation("fail");
                }
            }
            // Delete
            for (int i = 0; i < groupIds.length; i++) {
                group.setGroupId(groupIds[i]);
                memberDao.deleteMemberByGroup(group.getGroupId());
                dao.delete(group);
            }
            dbManager.commit();
        } catch (Exception e) {
            dbManager.rollback();
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }
	
	/**
	 * list all group's member
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation listApproverGroupMember(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "listGroupMember";
		String approverGroupId = (String)request.getParameter("groupId");
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   Collection memberList = null;
		   ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
		   //如果是01（PL）07(SystemOwner)08(DBOwner)，则需要从其相应的定义表中查找数据
		   if("01".equals(approverGroupId)){
			   memberList = memberDao.getProjectLeaderList();
		   }else if("07".equals(approverGroupId)){
			   memberList = memberDao.getSystemOwnerList();
		   }else if("08".equals(approverGroupId)){
			   memberList = memberDao.getDBOwnerList();
		   }else{
		     memberList = memberDao.getMemberList(approverGroupId);
		   }
		   request.setAttribute("memberList",memberList);
		}catch(Exception e){
		   e.printStackTrace();
		   returnLabel = "fail";
		}finally{
		   if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * eidt  group's member
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterMemberPage(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "memberEditPage";
		String approverGroupId = (String)request.getParameter("groupId");
		String groupType = (String)request.getParameter("groupType");
		IDBManager dbManager = null;
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
		   Collection memberList = memberDao.getMemberList(approverGroupId);
		   request.setAttribute("memberList",memberList);
		   Collection staffList = memberDao.getAvailableStaffList(approverGroupId,groupType);
		   request.setAttribute("staffList",staffList);
		}catch(Exception e){
		   e.printStackTrace();
		   returnLabel = "fail";
		}finally{
		   if(dbManager!=null) dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}
	
	/**
	 * save  group's member
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveGroupMember(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String groupId = (String)request.getParameter("groupId");
		String memberList = (String)request.getParameter("memberList");
		
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try{
		   dbManager =  DBManagerFactory.getDBManager();
		   ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
		   dbManager.startTransaction();
		   //delete all memeber first;
		   memberDao.deleteMemberByGroup(groupId);
		   //save all new member
		   if(memberList!=null && !"".equals(memberList)){
		     String[] staffCode = StringUtil.split(memberList,",");
		     if(staffCode!=null && staffCode.length>0){
		       for(int i=0;i<staffCode.length;i++){
		    	 ApproverGroupMemberVO member = new ApproverGroupMemberVO();
		    	 member.setGroupId(groupId);
		    	 member.setStaffCode(staffCode[i]);
		    	 memberDao.save(member);
		       }
		     }
		   }
		   dbManager.commit();
		   out.print("success");
		}catch(Exception e){
		   dbManager.rollback();
		   e.printStackTrace();
		   out.print("fail");
		}finally{
		   if(dbManager!=null) dbManager.freeConnection();
		   if(out!=null) out.close();
		}
		return null;
	}
		
}
