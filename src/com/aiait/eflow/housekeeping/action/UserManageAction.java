package com.aiait.eflow.housekeeping.action;

/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	User Management Action			  */
/*IT1029	Young	06/30/2008	DS-002 Team filter values vary depending on company filter*/
/*IT1303	Mario	07/24/2012	FS-2.5 Modify getCompanyStaffList */
/******************************************************************/
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.BaseDataHelper;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.ApproverGroupDAO;
import com.aiait.eflow.housekeeping.dao.ApproverGroupMemberDAO;
import com.aiait.eflow.housekeeping.dao.RoleDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.eflow.housekeeping.dao.TitleDAO;
import com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO;
import com.aiait.eflow.housekeeping.vo.EflowStaffVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.util.*;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.mvc.form.WebForm;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;
import com.aiait.eflow.common.helper.StaffTeamHelper;

import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;

public class UserManageAction extends DispatchAction {

	/**
	 * 动态获取某个公司下的staff集合，返回一个xml集合，给Ajax调用
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation getCompanyStaffList(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgId = (String) request.getParameter("orgId"); // 获取该公司的id
		String createNullFlag = (String) request.getParameter("createNullFlag"); // 是否生成一个空白选项的标识。如果该参数存在，则需要
		IDBManager dbManager = null;
		response.setContentType("text/xml;charset=GBK"); // it is very important
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		try {
			StringBuffer responseXML = new StringBuffer("");
			responseXML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			responseXML.append("<staffs>");
			if (createNullFlag != null && !"".equals(createNullFlag)) {
				responseXML.append("<staff code=''></staff>");
			}
			if (orgId != null && !"".equals(orgId)) {
				dbManager = DBManagerFactory.getDBManager();
				StaffDAO dao = new StaffDAO(dbManager);
				Collection staffList = null;
				
				if("Y".equals(ParamConfigHelper.getInstance().getParamValue("show_terminated_staffs"))){
					staffList = dao.getATStaffListByCompany(orgId);
				} else {
					staffList = dao.getStaffListByCompany(orgId);
				}
				int i = 0;
				if (staffList != null && staffList.size() > 0) {
					Iterator it = staffList.iterator();
					while (it.hasNext()) {
						// if(i>10) break;
						StaffVO staff = (StaffVO) it.next();
						responseXML.append("<staff code='").append(staff.getStaffCode()).append("'>").append(
								StringUtil.formatXML(StringUtil.replace(staff.getStaffName().trim(), "&", "-")))
								.append("</staff>");
						i++;
					}
				}
			}
			responseXML.append("</staffs>");
			out.write(responseXML.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation getTeamStaffList(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String teamCode = (String) request.getParameter("teamCode");
		String createNullFlag = (String) request.getParameter("createNullFlag"); // 是否生成一个空白选项的标识。如果该参数存在，则需要
		IDBManager dbManager = null;
		response.setContentType("text/xml;charset=GBK"); // it is very important
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		try {
			StringBuffer responseXML = new StringBuffer("");
			responseXML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			responseXML.append("<staffs>");
			if (createNullFlag != null && !"".equals(createNullFlag)) {
				responseXML.append("<staff code=''></staff>");
			}

			dbManager = DBManagerFactory.getDBManager();
			StaffDAO dao = new StaffDAO(dbManager);
			Collection staffList = dao.getStaffListByTeam(teamCode);
			int i = 0;
			if (staffList != null && staffList.size() > 0) {
				Iterator it = staffList.iterator();
				while (it.hasNext()) {
					// if(i>10) break;
					StaffVO staff = (StaffVO) it.next();
					responseXML.append("<staff code='").append(staff.getStaffCode()).append("'>").append(
							StringUtil.formatXML(StringUtil.replace(staff.getStaffName().trim(), "&", "-"))).append(
							"</staff>");
					i++;
				}
			}
			responseXML.append("</staffs>");
			out.write(responseXML.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	public ActionLocation searchStaffByName(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String staffName = (String) request.getParameter("staffName");
		String teamCode = (String) request.getParameter("teamCode");
		String orgId = (String) request.getParameter("orgId");
		
		String createNullFlag = (String) request.getParameter("createNullFlag"); // 是否生成一个空白选项的标识。如果该参数存在，则需要
		IDBManager dbManager = null;
		response.setContentType("text/xml;charset=GBK"); // it is very important
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		try {
			StringBuffer responseXML = new StringBuffer("");
			responseXML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			responseXML.append("<staffs>");
			if (createNullFlag != null && !"".equals(createNullFlag)) {
				responseXML.append("<staff code=''></staff>");
			}
			dbManager = DBManagerFactory.getDBManager();

			String teamarrStr = "" ;
			if (teamCode != null && !"".equals(teamCode)) {
				// 构造逗号分割的Team_code串
				TeamDAO teamDao = new TeamDAO(dbManager);
				teamarrStr = teamDao.groupSubTeam(teamCode);
				if (teamarrStr == null || "".equals(teamarrStr)) {
					teamarrStr = teamCode;
				} else {
					teamarrStr = teamarrStr.substring(0, teamarrStr.lastIndexOf(","));
					teamarrStr = teamCode + "," + teamarrStr;
				}
			}
			
			StaffDAO dao = new StaffDAO(dbManager);
			Collection staffList = dao.getStaffListByStaffName(staffName, teamarrStr, orgId);
			int i = 0;
			if (staffList != null && staffList.size() > 0) {
				Iterator it = staffList.iterator();
				while (it.hasNext()) {
					// if(i>10) break;
					StaffVO staff = (StaffVO) it.next();
					responseXML.append("<staff code='").append(staff.getStaffCode()).append("'>").append(
							StringUtil.formatXML(StringUtil.replace(staff.getStaffName().trim(), "&", "-"))).append(
							"</staff>");
					i++;
				}
			}
			responseXML.append("</staffs>");
			out.write(responseXML.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	/**
	 * 进入员工approver group选择设置界面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterApproverGroupSetting(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "listApproverGroupSetting";
		String staffCode = request.getParameter("staffCode");
		// 获取某个员工已经拥有和未拥有的approver group集合
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			ApproverGroupDAO groupDao = new ApproverGroupDAO(dbManager);
			Collection hasApproverGroupList = groupDao.getApproverGroupByStaff(staffCode);
			Collection noApproverGroupList = groupDao.getNotApproverGroupByStaff(staffCode);
			request.setAttribute("hasApproverGroupList", hasApproverGroupList);
			request.setAttribute("noApproverGroupList", noApproverGroupList);
		} catch (DAOException e) {
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 保存给某个员工所设置的approver group信息
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveApproverGroupSetting(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] approverGroupId = request.getParameterValues("groupId");
		String staffCode = request.getParameter("staffCode");
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try {
			dbManager = DBManagerFactory.getDBManager();
			ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
			dbManager.startTransaction();
			memberDao.deleteStaffApproverGroup(staffCode);
			if (approverGroupId != null && approverGroupId.length > 0) {
				ApproverGroupMemberVO member = new ApproverGroupMemberVO();
				for (int i = 0; i < approverGroupId.length; i++) {
					member.setGroupId(approverGroupId[i]);
					member.setStaffCode(staffCode);
					memberDao.save(member);
				}
			}
			dbManager.commit();
			BaseDataHelper.getInstance().refreshApproverGroup();
			out.print("success");
		} catch (Exception e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	/**
	 * 进入员工role选择设置界面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterRoleSetting(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "listRoleSetting";
		String staffCode = request.getParameter("staffCode");

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			RoleDAO dao = new RoleDAO(dbManager);
			Collection noRoleList = dao.getNoRoleByStaff(staffCode);
			request.setAttribute("noRoleList", noRoleList);
			// 获取某个员工已经拥有的role集合
			Collection hasRoleList = dao.getRoleByStaff(staffCode);
			request.setAttribute("hasRoleList", hasRoleList);
		} catch (DAOException e) {
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 保存给某个员工所设置的role信息
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveRoleSetting(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] roleId = request.getParameterValues("roleId");
		String staffCode = request.getParameter("staffCode");
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try {
			dbManager = DBManagerFactory.getDBManager();
			RoleDAO dao = new RoleDAO(dbManager);
			dbManager.startTransaction();
			dao.deleteRoleByStaff(staffCode);
			if (roleId != null && roleId.length > 0) {
				for (int i = 0; i < roleId.length; i++) {
					dao.saveRoleMember(roleId[i], staffCode);
				}
			}
			dbManager.commit();
			out.print("success");
		} catch (Exception e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	/**
	 * 进入一个通用的staff选择页面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterSelectStaff(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "listSelectStaff";

		// 准备staff信息
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO staffDao = new StaffDAO(dbManager);
			Collection staffList = staffDao.getAllStaff();
			request.setAttribute("staffList", staffList);
		} catch (DAOException e) {
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.getMessage());
			e.printStackTrace();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * Search user with query condition
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation searchUser(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "listUser";
		String staffCode = (String) request.getParameter("staffCode");
		String logonId = (String) request.getParameter("logonId");
		// String teamId = (String)request.getParameter("teamId");
		String recursionteam = (String) request.getParameter("recursionteam");
		String staffname = (String) request.getParameter("staffname");
		String chinesename = (String) request.getParameter("chinesename");
		String status = (String) request.getParameter("status");
		String email = (String) request.getParameter("email");
		String usertype = (String) request.getParameter("usertype");
		String begindate = (String) request.getParameter("beginDate");
		String enddate = (String) request.getParameter("endDate");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		// 获取其所有下级公司（包括本公司）
		String teamId = (String) request.getParameter("team_code");
		String orgIdfrmpage = (String) request.getParameter("orgId");
		String lowerOrgIds = "";
		if (orgIdfrmpage == null || "".equals(orgIdfrmpage)) {
			lowerOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
			// lowerOrgIds = "'"+currentStaff.getOrgId()+"'";
		} else {
			orgIdfrmpage = "'" + orgIdfrmpage + "'";
			lowerOrgIds = orgIdfrmpage;
		}

		HashMap paramMap = new HashMap();

		IDBManager dbManager = null;
		try {

			dbManager = DBManagerFactory.getDBManager();
			StaffDAO staffDao = new StaffDAO(dbManager);
			TeamDAO teamDao = new TeamDAO(dbManager);
			// EflowStaffVO queryVo = new EflowStaffVO();
			// set query condition begin
			if (staffCode != null) {
				// queryVo.setStaffCode(staffCode);
				paramMap.put("staffCode", staffCode);
			}
			if (logonId != null) {
				// queryVo.setLogonId(logonId);
				paramMap.put("logonId", logonId);
			}
			if (teamId != null && !"".equals(teamId)) {
				if ("true".equals(recursionteam)) {
					// 构造逗号分割的Team_code串
					String teamarrStr = teamDao.groupSubTeam(teamId);
					if (teamarrStr == null || "".equals(teamarrStr)) {
						teamarrStr = teamId;
					} else {
						teamarrStr = teamarrStr.substring(0, teamarrStr.lastIndexOf(","));
						teamarrStr = teamId + "," + teamarrStr;
					}
					// queryVo.setTeamCode(teamarrStr);
					paramMap.put("teamCode", teamarrStr);
				} else {
					// queryVo.setTeamCode(teamId);
					paramMap.put("teamCode", teamId);
				}
				paramMap.put("orgId", lowerOrgIds);
			} else {// 否则设置可以查询的公司
				// queryVo.setOrgId(lowerOrgIds);
				paramMap.put("orgId", lowerOrgIds);
			}
			if (staffname != null) {
				// queryVo.setStaffName(staffname);
				paramMap.put("staffName", staffname);
			}
			if (chinesename != null) {
				paramMap.put("chinesename", chinesename);
			}
			if (status != null) {
				// queryVo.setStatus(status);
				paramMap.put("status", status);
			}
			if (email != null) {
				// queryVo.setEmail(email);
				paramMap.put("email", email);
			}
			if ("-1".equals(usertype)) {
				// queryVo.setUsertype("");
			} else {
				// queryVo.setUsertype(usertype);
				paramMap.put("userType", usertype);
			}
			if ("0".equals(usertype)) {
				// queryVo.setFromdate("");
				// queryVo.setTodate("");
			} else {
				// queryVo.setFromdate(begindate);
				// queryVo.setTodate(enddate);
				paramMap.put("beginDate", begindate);
				paramMap.put("endDate", enddate);
			}
			// set query condition end

			// Collection staffList = staffDao.getSearchStaff(queryVo);

			// 分页查询begin
			String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
			if (pagenum == null) {
				pagenum = "1";
			}
			PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
					CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
			page.setParamMap(paramMap);
			page.setCurrentPage(Integer.parseInt(pagenum));
			int totalRecordsNum = staffDao.getTotalRecordsNum(page);
			page = PageUtil.createPage(page, totalRecordsNum);
			Collection staffList = staffDao.searchStaff(page);
			request.setAttribute("eflowuserList", staffList);
			request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
			// 分页查询end

			// 获取所有的Team列表
			Collection TeamList = null;
			TeamDAO tdao = new TeamDAO(dbManager);
			// TeamList = tdao.getMergedTeamList();
			TeamList = tdao.getTeamList(lowerOrgIds);
			request.setAttribute("teamList", TeamList);

			// get the companyList
			Collection companyList = currentStaff.getOwnCompanyList();
			// companyList =
			// CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());
			request.setAttribute("companyList", companyList);

		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
			returnLabel = "fail";
		} finally {
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	// Export inquiry user data
	public ActionLocation exportInquiryUser(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "exportInquiryUser";
		String staffCode = (String) request.getParameter("staffCode");
		String logonId = (String) request.getParameter("logonId");
		// String teamId = (String)request.getParameter("teamId");
		String recursionteam = (String) request.getParameter("recursionteam");
		String staffname = (String) request.getParameter("staffname");
		String chinesename = (String) request.getParameter("chinesename");
		String status = (String) request.getParameter("status");
		String email = (String) request.getParameter("email");
		String usertype = (String) request.getParameter("usertype");
		String begindate = (String) request.getParameter("beginDate");
		String enddate = (String) request.getParameter("endDate");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		// 获取其所有下级公司（包括本公司）
		String teamId = (String) request.getParameter("team_code");
		String orgIdfrmpage = (String) request.getParameter("orgId");
		String lowerOrgIds = "";
		if (orgIdfrmpage == null || "".equals(orgIdfrmpage)) {
			lowerOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
			// lowerOrgIds = "'"+currentStaff.getOrgId()+"'";
		} else {
			orgIdfrmpage = "'" + orgIdfrmpage + "'";
			lowerOrgIds = orgIdfrmpage;
		}

		HashMap paramMap = new HashMap();

		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO staffDao = new StaffDAO(dbManager);
			TeamDAO teamDao = new TeamDAO(dbManager);
			if (staffCode != null) {
				paramMap.put("staffCode", staffCode);
			}
			if (logonId != null) {
				paramMap.put("logonId", logonId);
			}
			if (teamId != null && !"".equals(teamId)) {
				if ("true".equals(recursionteam)) {
					// 构造逗号分割的Team_code串
					String teamarrStr = teamDao.groupSubTeam(teamId);
					if (teamarrStr == null || "".equals(teamarrStr)) {
						teamarrStr = teamId;
					} else {
						teamarrStr = teamarrStr.substring(0, teamarrStr.lastIndexOf(","));
						teamarrStr = teamId + "," + teamarrStr;
					}
					paramMap.put("teamCode", teamarrStr);
				} else {
					paramMap.put("teamCode", teamId);
				}
				paramMap.put("orgId", lowerOrgIds);
			} else {// 否则设置可以查询的公司
				paramMap.put("orgId", lowerOrgIds);
			}
			if (staffname != null) {
				paramMap.put("staffName", staffname);
			}
			if (chinesename != null) {
				paramMap.put("chinesename", chinesename);
			}
			if (status != null) {
				paramMap.put("status", status);
			}
			if (email != null) {
				paramMap.put("email", email);
			}
			if ("-1".equals(usertype)) {
			} else {
				paramMap.put("userType", usertype);
			}
			if ("0".equals(usertype)) {
				// queryVo.setFromdate("");
				// queryVo.setTodate("");
			} else {
				paramMap.put("beginDate", begindate);
				paramMap.put("endDate", enddate);
			}

			// List all records
			PageVO page = new PageVO();
			page.setPageSize(0);
			page.setCurrentPage(1);

			page.setParamMap(paramMap);
			Collection staffList = staffDao.searchStaff2(page);
			request.setAttribute("eflowuserList", staffList);
		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * 进入user信息修改页面
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterEditUser(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "newUserPage";
		String type = (String) request.getParameter("type");
		if (type == null) {
			type = "new";
		}
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		// 获取其所有下级公司（包括本公司）
		String lowerOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO dao = new StaffDAO(dbManager);
			if (!"new".equals(type)) {// 编辑type="edit"
				String staffcode = request.getParameter("staffcode");
				EflowStaffVO staffvo = dao.getStaffByStaffCode(staffcode);
				request.setAttribute("eflowuser", staffvo);
			}
			// 获取所有的Team列表
			Collection TeamList = null;
			TeamDAO tdao = new TeamDAO(dbManager);
			// TeamList = tdao.getMergedTeamList();
			// TeamList = StaffTeamHelper.getInstance().getTeamList();
			TeamList = tdao.getTeamList(lowerOrgIds);
			request.setAttribute("teamList", TeamList);
			// 获取title列表
			TitleDAO titleDao = new TitleDAO(dbManager);
			Collection titleList = titleDao.getActiveTitleList();
			request.setAttribute("titleList", titleList);
		} catch (Exception e) {
			e.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * Save user information
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveUser(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String saveType = (String) request.getParameter("saveType");
		saveType = HtmlUtil.decoderURL(saveType);
		if (saveType == null || "".equals(saveType)) {
			saveType = "new";
		}

		EflowStaffVO estaffvo = (EflowStaffVO) form.toVoAjax(EflowStaffVO.class);

		estaffvo.setTeamCode((String) form.get("team_code"));
		estaffvo.setFromdate(HtmlUtil.decoderURL((String) form.get("beginDate")));
		estaffvo.setTodate(HtmlUtil.decoderURL((String) form.get("endDate")));
		estaffvo.setTitle((String) form.get("titleId"));

		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO sdao = new StaffDAO(dbManager);
			TeamDAO tdao = new TeamDAO(dbManager);

			EflowStaffVO oldStaff = sdao.getStaffByStaffCode(estaffvo.getStaffCode());
			String logonStaff = sdao.getStaffCodeByLogonId(estaffvo.getLogonId());

			WorkFlowProcessDAO wkfdao = new WorkFlowProcessDAO(dbManager);
			if ("new".equals(saveType)) {

				if (oldStaff != null) {
					out.print("Staff Code (" + estaffvo.getStaffCode() + ") already exists!");
					return null;
				}

				if (logonStaff != null) {
					out.print("Logon ID (" + estaffvo.getLogonId() + ") already exists!");
					return null;
				}

				sdao.save(estaffvo, request);
				StaffTeamHelper.getInstance().addStaff(estaffvo.getStaffCode());
			} else {
				if (!oldStaff.getLogonId().equals(estaffvo.getLogonId())) {
					if (logonStaff != null) {
						out.print("Logon ID (" + estaffvo.getLogonId() + ") already exists!");
						return null;
					}
				}

				if ("T".equals(estaffvo.getStatus())) {
					boolean isleader = sdao.IsTorPLeader(estaffvo.getLogonId());
					if (isleader) {
						out.print("Staff(" + estaffvo.getStaffCode()
								+ ") is a Team or Project Leader, it's status cannot be Terminated!");
						return null;
					}

					if ("A".equals(oldStaff.getStatus())) {
						if (!wkfdao.isStaffProcessCompleted(estaffvo.getStaffCode())) {
							out.print("Staff(" + estaffvo.getStaffCode()
									+ ") has flow in processing, it's status cannot be Terminated!");
							return null;
						}
					}
					new ApproverGroupMemberDAO(dbManager).deleteStaffApproverGroup(estaffvo.getStaffCode());
				} else {
					tdao.updateNTIDForTL(oldStaff.getLogonId(), estaffvo.getLogonId());
				}
				StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

				sdao.update(estaffvo, staff);
				StaffTeamHelper.getInstance().refresh();
			}
			out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("fail:" + e.getMessage());
			return null;
		} finally {
			if (out != null)
				out.close();
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return null;
	}

	/**
	 * 将指定的用户删除
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteUsers(ModuleMapping mapping, WebForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "enterListUser";
		String[] staffCodes = request.getParameterValues("staffcodeId");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String modifyDate = StringUtil.getDateStr(new java.util.Date(), "MM/dd/yyyy HH:mm:ss");
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO sdao = new StaffDAO(dbManager);
			WorkFlowProcessDAO wkfdao = new WorkFlowProcessDAO(dbManager);
			dbManager.startTransaction();
			// 每删除一条纪录之前，将该纪录写入历史库
			for (int i = 0; i < staffCodes.length; i++) {
				EflowStaffVO staff = sdao.getStaffByStaffCode(staffCodes[i]);
				boolean isleader = sdao.IsTorPLeader(staff.getLogonId());
				if (isleader) {
					throw new Exception("Staff(" + staff.getStaffCode()
							+ ") is a Team or Project Leader, it's status cannot be Terminated!");
				}

				if (!wkfdao.isStaffProcessCompleted(staff.getStaffCode())) {
					throw new Exception("Staff(" + staff.getStaffCode()
							+ ") has flows in processing, it's status cannot be Terminated!");
				}

				if (wkfdao.getForDealFormListByStaff(staff.getStaffCode(),new WorkFlowProcessVO()).size()>0) {
					throw new Exception("Staff(" + staff.getStaffCode()
							+ ") has forms to handle, it's status cannot be Terminated!");
				}
				sdao.saveHistory(staff, modifyDate, currentStaff.getStaffCode());
				sdao.deleteStaffByStaffcode(staffCodes[i]);
				sdao.updateduputytable(staffCodes[i]);
				new ApproverGroupMemberDAO(dbManager).deleteStaffApproverGroup(staffCodes[i]);
			}
			dbManager.commit();
		} catch (Exception e) {
			returnLabel = "fail";
			request.setAttribute("error", e.getMessage());
			dbManager.rollback();
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		if (!"fail".equals(returnLabel)) {
			StaffTeamHelper.getInstance().refresh();
		}
		return mapping.findActionLocation(returnLabel);
	}

    public ActionLocation userRegisterSvc(ModuleMapping mapping, WebForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EflowStaffVO staff = new EflowStaffVO();
        staff.setLogonId(StringUtil.decoderURL(request.getParameter("logonId")));
        // System.out.println("logonId: " + staff.getLogonId());
        staff.setStaffCode(StringUtil.decoderURL(request.getParameter("staffCode")));
        // System.out.println("staffCode: " + staff.getStaffCode());
        staff.setStaffName(StringUtil.decoderURL(request.getParameter("staffName")));
        // System.out.println("staffName: " + staff.getStaffName());
        staff.setChineseName(StringUtil.decoderURL(request.getParameter("chineseName")));
        // System.out.println("chineseName: " + staff.getChineseName());
        staff.setOrgId(StringUtil.decoderURL(request.getParameter("orgId")));
        // System.out.println("orgId: " + staff.getOrgId());
        staff.setTeamCode(StringUtil.decoderURL(request.getParameter("team_code")));
        // System.out.println("team_code: " + staff.getTeamCode());
        staff.setEmail(StringUtil.decoderURL(request.getParameter("email")));
        // System.out.println("email: " + staff.getEmail());

        PrintWriter out = response.getWriter();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            StaffDAO sdao = new StaffDAO(dbManager);
            EflowStaffVO oldStaff = sdao.getStaffByStaffCode(staff.getStaffCode());
            if (oldStaff != null) {
                out.print("The Staff Code (" + staff.getStaffCode() + ") has been registered by others!");
                return null;
            }
            sdao.userRegister(staff);
            StaffTeamHelper.getInstance().addStaff(staff.getStaffCode());
            out.print("Register success!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

    public ActionLocation checkStaffIDMSvc(ModuleMapping mapping, WebForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/xml;charset=GBK");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream os = response.getOutputStream();
        PrintWriter out = new PrintWriter(os);

        String staffCode = StringUtil.decoderURL(request.getParameter("staffCode"));
        if (staffCode == null || staffCode.trim().equals("")) {
            out.print("Error: Global Employee ID cannot be null");
            return null;
        }
        staffCode = staffCode.trim();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            StaffDAO sdao = new StaffDAO(dbManager);
            EflowStaffVO staff = sdao.getStaffByIDM(staffCode);
            if (staff == null) {
                out.print("Error: Global Employee ID (" + staffCode + ") doesn't exists");
                return null;
            }
            Document document = XMLUtil.newDocument("staff");
            Element element = document.getDocumentElement();
            element.setAttribute("code", staff.getStaffCode());
            element.setAttribute("name", staff.getStaffName());
            element.setAttribute("orgId", staff.getOrgId());
            element.setAttribute("orgName", staff.getOrgName());
            element.setAttribute("teamCode", staff.getTeamCode());
            element.setAttribute("teamName", staff.getTeamName());
            element.setAttribute("titleCode", staff.getTitle());
            element.setAttribute("titleName", staff.getTitleName());
            element.setAttribute("email", staff.getEmail());
            XMLUtil.writeXML(document, os, "GBK");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error: " + ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return null;
    }

}
