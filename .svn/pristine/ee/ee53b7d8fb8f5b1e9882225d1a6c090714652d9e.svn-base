package com.aiait.eflow.housekeeping.action;

/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	Team Management Action			  */
/******************************************************************/
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.property.Parent;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.dao.TeamDAO;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;
import com.aiait.framework.page.PageUtil;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.util.CommonUtil;
import com.aiait.eflow.housekeeping.dao.ApproverGroupMemberDAO;
import com.aiait.eflow.housekeeping.vo.ViewTeamTypeVO;
import com.aiait.eflow.util.StringUtil;

public class TeamManageAction extends DispatchAction {

	private String treeNodes;
	
	/**
	 * 动态获取某个公司下的team集合，返回一个xml集合，给Ajax调用
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation getTeamList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orgId = (String) request.getParameter("orgId"); // 获取该公司的id
		String createNullFlag = (String) request.getParameter("createNullFlag"); // 是否生成一个空白选项的标识。如果该参数存在，则需要
		String enableSearchAll = (String) request.getParameter("enableSearchAll"); // 获取该公司的id

		if (enableSearchAll == null || enableSearchAll.length() == 0) {
			enableSearchAll = "false"; // default is false
		}

		IDBManager dbManager = null;
		response.setContentType("text/xml;charset=GBK"); // it is very important
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		try {
			StringBuffer responseXML = new StringBuffer("");
			responseXML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			responseXML.append("<teams>");
			if (createNullFlag != null && !"".equals(createNullFlag)) {
				responseXML.append("<team code=''></team>");
			}
			if ((orgId != null && !"".equals(orgId)) || "true".equalsIgnoreCase(enableSearchAll)) {
				dbManager = DBManagerFactory.getDBManager();
				TeamDAO dao = new TeamDAO(dbManager);
				Collection teamList = dao.getTeamListByCompany(orgId);
				int i = 0;
				if (teamList != null && teamList.size() > 0) {
					Iterator it = teamList.iterator();
					// responseXML.append("<team code=''></team>");
					while (it.hasNext()) {
						// if(i>10) break;
						TeamVO team = (TeamVO) it.next();
						responseXML.append("<team code='").append(team.getTeamCode()).append("'>").append(
								StringUtil.replace(team.getTeamName().trim(), "&", "-")).append("</team>");
						i++;
					}
				}
			}
			responseXML.append("</teams>");
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
	 * enter config Team page
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation ListConfPage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String returnLabel = "ListConfPage";
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			TeamDAO teamdao = new TeamDAO(dbManager);
			setSubTeam(request, "-1", teamdao);
			Collection teamTypeList = teamdao.getTeamType();
			request.setAttribute("teamTypeList", teamTypeList);
		} catch (DAOException e) {
			request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * Recursion Function for getting the teamtree
	 * 
	 */
	private void setSubTeam(HttpServletRequest request, String teamcode, TeamDAO teamdao) {

		try {
			Collection teamrootlist = teamdao.getSubteamArr(teamcode);
			if (teamrootlist != null && teamrootlist.size() > 0) {
				if ("-1".equals(teamcode)) {
					request.setAttribute("root", teamrootlist);
				}
				Iterator it = teamrootlist.iterator();
									
				while (it.hasNext()) {
					TeamVO vo = (TeamVO) it.next();
					
					if(treeNodes.indexOf(","+vo.getTeamCode()+",")>-1)
						return; //存在回路，结束递归
					else
						treeNodes += vo.getTeamCode()+",";
						
					Collection subteamlist = teamdao.getSubteamArr(vo.getTeamCode());
					if (subteamlist != null && subteamlist.size() > 0) {
						request.setAttribute(vo.getTeamName().trim() + vo.getTeamCode().toString(), subteamlist);
						setSubTeam(request, vo.getTeamCode(), teamdao);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
		}
	}

	/**
	 * enter list Team page
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation updateTeamConfig(ModuleMapping mapping, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String teamcode = (String) request.getParameter("teamCode");
		String teamtype = (String) request.getParameter("teamType");

		PrintWriter out = response.getWriter();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			TeamDAO teamdao = new TeamDAO(dbManager);
			ViewTeamTypeVO vttvo = new ViewTeamTypeVO();
			vttvo.setTeamCode(teamcode);
			vttvo.setTeam_type(teamtype);
			dbManager.startTransaction();

			if (teamdao.Isexist(vttvo)) {
				teamdao.updateteamconf(vttvo);
			} else {
				teamdao.insertteamconf(vttvo);
			}

			dbManager.commit();
			out.print("success");
		} catch (Exception e) {
		    dbManager.rollback();
			e.printStackTrace();
			out.print("fail");
			return null;
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}

		return null;
	}

	/**
	 * enter list Team page
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             public ActionLocation listTeam( ModuleMapping mapping,
	 *             HttpServletRequest request, HttpServletResponse response)
	 *             throws Exception { String returnLabel = "listTeam"; StaffVO
	 *             currentStaff =
	 *             (StaffVO)request.getSession().getAttribute(CommonName
	 *             .CURRENT_STAFF_INFOR); //获取其所有下级公司（包括本公司） String lowerOrgIds
	 *             = CompanyHelper.getInstance().getLowerCompany(currentStaff.
	 *             getOrgId());
	 * 
	 *             IDBManager dbManager = null; dbManager =
	 *             DBManagerFactory.getDBManager(); try { TeamDAO dao = new
	 *             TeamDAO(dbManager); //Collection list =
	 *             dao.getEflowTeamList(); Collection list =
	 *             dao.getTeamList(lowerOrgIds);
	 * 
	 *             request.setAttribute("eflowteamList", list); //获取所有的Team列表,
	 *             Collection TeamList = null; TeamDAO tdao = new
	 *             TeamDAO(dbManager); TeamList = tdao.getMergedTeamList();
	 *             request.setAttribute("teamList",TeamList); } catch
	 *             (DAOException e) { e.printStackTrace(); request.setAttribute(
	 *             "fatalerror",
	 *             "During search Team list happen error ,please contact Admin!"
	 *             ); return mapping.findActionLocation("fail"); } finally {
	 *             dbManager.freeConnection(); } return
	 *             mapping.findActionLocation(returnLabel); }
	 **/

	/**
	 * Search Team with query condition
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation searchTeam(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "listTeam";
		String teamcode = (String) request.getParameter("teamCode");
		String teamname = (String) request.getParameter("teamName");
		String status = (String) request.getParameter("status");
		String teamleader = (String) request.getParameter("teamleader");
		String orgId = (String) request.getParameter("orgId");
		String department = (String) request.getParameter("department");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		HashMap paramMap = new HashMap();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			TeamDAO teamDao = new TeamDAO(dbManager);
			TeamVO tvo = new TeamVO();
			if (teamcode != null && !"".equals(teamcode)) {
				// tvo.setTeamCode(teamcode);
				paramMap.put("teamCode", teamcode.trim());
			}
			if (teamname != null && !"".equals(teamname)) {
				// tvo.setTeamName(teamname);
				paramMap.put("teamName", teamname.trim());
			}
			if (status != null && !"".equals(status)) {
				// tvo.setStatus(status);
				paramMap.put("status", status);
			}
			if (teamleader != null && !"".equals(teamleader)) {
				// tvo.setTLeaderName(teamleader);
				paramMap.put("teamLeader", teamleader);
			}
			if (department != null && !"".equals(department)) {
				// tvo.setDepartment(department);
				paramMap.put("department", department);
			}
			if (orgId != null && !"".equals(orgId)) {
				orgId = "'" + orgId + "'";
				// tvo.setOrgId(orgId);
				paramMap.put("orgId", orgId);
			} else {
				// tvo.setOrgId(currentStaff.getLowerCompanys());
				paramMap.put("orgId", currentStaff.getLowerCompanys());
			}

			// 分页查询begin
			String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
			if (pagenum == null) {
				pagenum = "1";
			}
			PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
					CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
			page.setParamMap(paramMap);
			page.setCurrentPage(Integer.parseInt(pagenum));
			int totalRecordsNum = teamDao.getTotalRecordsNum(page);
			page = PageUtil.createPage(page, totalRecordsNum);
			Collection teaList = teamDao.searchTeamList(page);
			request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
			// 分页查询end
			request.setAttribute("eflowteamList", teaList);
		} catch (DAOException e) {
			e.printStackTrace();
			returnLabel = "fail";
		} finally {
			dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	// Export inquiry team data
	public ActionLocation exportInquiryTeam(ModuleMapping mapping, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnLabel = "exportInquiryTeam";
		String teamcode = (String) request.getParameter("teamCode");
		String teamname = (String) request.getParameter("teamName");
		String status = (String) request.getParameter("status");
		String teamleader = (String) request.getParameter("teamleader");
		String orgId = (String) request.getParameter("orgId");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		HashMap paramMap = new HashMap();
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			TeamDAO teamDao = new TeamDAO(dbManager);
			TeamVO tvo = new TeamVO();
			if (teamcode != null && !"".equals(teamcode)) {
				// tvo.setTeamCode(teamcode);
				paramMap.put("teamCode", teamcode.trim());
			}
			if (teamname != null && !"".equals(teamname)) {
				// tvo.setTeamName(teamname);
				paramMap.put("teamName", teamname.trim());
			}
			if (status != null && !"".equals(status)) {
				// tvo.setStatus(status);
				paramMap.put("status", status);
			}
			if (teamleader != null && !"".equals(teamleader)) {
				tvo.setTLeaderName(teamleader);
			}
			if (orgId != null && !"".equals(orgId)) {
				orgId = "'" + orgId + "'";
				// tvo.setOrgId(orgId);
				paramMap.put("orgId", orgId);
			} else {
				// tvo.setOrgId(currentStaff.getLowerCompanys());
				paramMap.put("orgId", currentStaff.getLowerCompanys());
			}

			// List all records
			PageVO page = new PageVO();
			page.setPageSize(0);
			page.setCurrentPage(1);

			page.setParamMap(paramMap);
			Collection teaList = teamDao.searchTeamList(page);
			request.setAttribute("eflowteamList", teaList);
		} catch (DAOException e) {
			e.printStackTrace();
			returnLabel = "fail";
		} finally {
			if (dbManager != null) {
				dbManager.freeConnection();
			}
		}
		return mapping.findActionLocation(returnLabel);
	}

	/**
	 * Enter edit page for creating or editing team
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation enterEditTeam(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "newTeamPage";
		String type = (String) request.getParameter("type");
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		// 获取其所有下级公司（包括本公司）
		String lowerOrgIds = currentStaff.getLowerCompanys(); // CompanyHelper.getInstance().getLowerCompany(currentStaff.getOrgId());

		if (type == null) {
			type = "new";
		}
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			StaffDAO sdao = new StaffDAO(dbManager);
			TeamDAO dao = new TeamDAO(dbManager);
			if (!"new".equals(type)) {
				// 编辑type="edit"
				// fetch
				String teamcode = request.getParameter("teamcode");
				TeamVO eflowteamvo = dao.getTeamByTeamCode(teamcode);
				request.setAttribute("eflowteam", eflowteamvo);

				// for sub-team tree
				request.setAttribute(eflowteamvo.getTeamName().trim() + eflowteamvo.getTeamCode().toString(), dao
						.getSubteamArr(teamcode));
				this.treeNodes = ","+teamcode+",";//Init
				setSubTeam(request, teamcode, dao);
			}

			// 获取所有Team列表
			Collection teamList = null;
			// if ("new".equals(type)){
			// TeamList = dao.getEflowTeamList();
			// TeamList = dao.getViewTeamList();
			if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
				teamList = dao.getTeamList(null);
			} else {
				teamList = dao.getTeamList(lowerOrgIds);
			}
			// }
			/**
			 * else{ //获取除当前被编辑Team以外的Team List String teamcode =
			 * request.getParameter("teamcode"); teamList =
			 * dao.getEflowTeamListWithoutTeamCode(teamcode); }
			 **/
			request.setAttribute("teamList", teamList);

			// 获取所有的EflowStaff列表
			Collection staffList = null;
			// staffList = sdao.getEflowAllStaff();
			staffList = sdao.getStaffListByCompanyAndSubCompany(currentStaff.getLowerCompanys());
			request.setAttribute("efusrlist", staffList);

			// 获取所有PMA系统的teamleader
			// ApproverGroupMemberDAO approverdao = new
			// ApproverGroupMemberDAO(dbManager);
			// Collection teamLeaderList = approverdao.getMemberList("02") ;
			// request.setAttribute("teamLeaderList", teamLeaderList);

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
	 * Save Team Information
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation saveTeam(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String saveType = (String) request.getParameter("type");
		if (saveType == null || "".equals(saveType)) {
			saveType = "new";
		}
		StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		String teamname = CommonUtil.decoderURL((String) request.getParameter("teamname"));
		String teamcode = (String) request.getParameter("teamcode");
		String superiorteamcode = (String) request.getParameter("superiorteamcode");
		String status = (String) request.getParameter("status");
		String tlid = (String) request.getParameter("tlid");
		String orgChart = (String) request.getParameter("orgChart");
		String department = (String) request.getParameter("department");
		String orgId = (String) request.getParameter("orgId");
		String t2Code = (String) request.getParameter("t2Code");
		if (orgId == null || "".equals(orgId)) {
			orgId = currentStaff.getOrgId();
		}

		TeamVO teamvo = new TeamVO();

		teamvo.setTeamName(teamname);
		teamvo.setSuperiorsCode(superiorteamcode);
		teamvo.setStatus(status);
		teamvo.setTlid(tlid);
		if (orgChart == null || "".equals(orgChart)) {
			teamvo.setOrgChart("Y");
		} else {
			teamvo.setOrgChart(orgChart);
		}
		if (department != null && "Y".equals(department)) {
			teamvo.setDepartment("Y");
		} else {
			teamvo.setDepartment("N");
		}
		teamvo.setOrgId(orgId);
		teamvo.setT2Code(t2Code);

		IDBManager dbManager = null;
		response.setContentType("text/xml;charset=GBK");
		PrintWriter out = response.getWriter();
		try {
			dbManager = DBManagerFactory.getDBManager();
			dbManager.startTransaction();
			TeamDAO tdao = new TeamDAO(dbManager);
			StaffDAO staffdao = new StaffDAO(dbManager);

			ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
			ApproverGroupMemberVO apgm = null;
			String staffcode = staffdao.getStaffCodeByLogonId(tlid);

			if ("new".equals(saveType)) {
				tdao.save(teamvo, request);
			} else {
				teamvo.setTeamCode(teamcode);

				TeamVO oteamvo = tdao.getTeamByTeamCode(teamcode);

				if ("A".equals(oteamvo.getStatus()) && "T".equals(teamvo.getStatus())) {
					// delete team

					if (!deleteTeam(request, teamcode, tdao, staffdao, memberDao)) {
						return mapping.findActionLocation("fail");
					}
					dbManager.commit();

					out.print("success");

					return null;
				} else {
					// update team

					tdao.saveHistory(oteamvo, request);
					tdao.update(teamvo, request);
					tdao.saveT2(teamvo, request);

					if (!(oteamvo.getOrgId() != null && teamvo.getOrgId().equals(oteamvo.getOrgId()))) {
						StaffVO curstaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

						staffdao.updateStaffCompany(teamcode, teamvo.getOrgId(), curstaff.getStaffCode());
					}

					// try to remove old approver group member. (to use '<= 1',
					// do not use '== 1')
					if (oteamvo != null && oteamvo.getTlid() != null) {
						if (tdao.getTeamAmountForTL(oteamvo.getTlid(), "Y".equals(oteamvo.getDepartment())) < 1) {
							String ostaffcode = staffdao.getStaffCodeByLogonId(oteamvo.getTlid());
							if (ostaffcode != null) {
								apgm = new ApproverGroupMemberVO();								
								if ("Y".equals(oteamvo.getDepartment())) {
									apgm.setGroupId("12"); // Department Header
									apgm.setStaffCode(ostaffcode);
									memberDao.delete(apgm);
									if ("".equals(tdao.getManagedTeamCodesByTL(ostaffcode).trim())) {
										apgm.setGroupId("02"); // Team Leader
										memberDao.delete(apgm);
									}
								} else {
									apgm.setGroupId("02"); // Team Leader
									apgm.setStaffCode(ostaffcode);
									memberDao.delete(apgm);
								}
							}
						}
					}
				}
			}

			// try to save new approver group member
			if (staffcode != null) {
				apgm = new ApproverGroupMemberVO();
				if ("Y".equals(teamvo.getDepartment())) {
					apgm.setGroupId("12"); // Department Header
				} else {
					apgm.setGroupId("02"); // Team Leader
				}
				apgm.setStaffCode(staffcode);
				memberDao.save(apgm);
			}

			dbManager.commit();

			if ("Y".equals(teamvo.getDepartment())) {
				if (checkSuperiorTeam(teamvo.getSuperiorsCode(), out, tdao)) {
					out.print("success");
				}
			} else {
				out.print("success");
			}

		} catch (Exception e) {
			dbManager.rollback();
			e.printStackTrace();
			out.print(e.toString());
		} finally {
			if (out != null)
				out.close();
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}

	private boolean checkSuperiorTeam(String superiorTeamCode, PrintWriter out, TeamDAO tdao) throws DAOException {

		if (superiorTeamCode == null || superiorTeamCode.length() == 0)
			return true;

		TeamVO superiorteam = tdao.getTeamByTeamCode(superiorTeamCode);
		if (superiorteam == null)
			return true;

		if ("Y".equals(superiorteam.getDepartment())) {
			if (superiorteam.getTeamCode() != null && !"".equals(superiorteam.getTeamCode())) {
				return checkSuperiorTeam(superiorteam.getSuperiorsCode(), out, tdao);
			} else {
				return true;
			}
		} else {
			out.print("Success, but superior team[" + superiorteam.getTeamCode() + ":" + superiorteam.getTeamName()
					+ "] is not a department team.");
			return false;
		}
	}

	/**
	 * 将指定的Team删除
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation deleteTeams(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String returnLabel = "enterListTeam";
		String[] teamCode = (String[]) request.getParameterValues("teamcodeId");
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			TeamDAO tdao = new TeamDAO(dbManager);
			StaffDAO sdao = new StaffDAO(dbManager);
			ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);

			dbManager.startTransaction();
			for (int i = 0; i < teamCode.length; i++) {
				if (!deleteTeam(request, teamCode[i], tdao, sdao, memberDao)) {
					return mapping.findActionLocation("fail");
				}
			}

			dbManager.commit();

		} catch (Exception e) {
			dbManager.rollback();
			request.setAttribute("error", e.getMessage());
			return mapping.findActionLocation("fail");

		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return mapping.findActionLocation(returnLabel);
	}

	private boolean deleteTeam(HttpServletRequest request, String teamCode, TeamDAO tdao, StaffDAO staffdao,
			ApproverGroupMemberDAO memberDao) throws DAOException {
		// 检查该team是否存在有效状态的员工，如果存在则不允许删除
		Collection staffList = staffdao.getActiveEflowStaffByTeam(teamCode);
		if (staffList != null && staffList.size() > 0) {
			request.setAttribute("error", "Team(" + teamCode + ") has " + staffList.size()
					+ " active staffs, it can't be deleted!");
			return false;
		}
		// 检查该team是否存在有效状态的直接下级team，如果存在则不允许删除
		boolean hasSubTeam = tdao.checkHasSubTeam(teamCode);
		if (hasSubTeam) {
			request.setAttribute("error", "Team(" + teamCode + ") has active sub teams, it can't be deleted!");
			return false;
		}
		// 写历史纪录
		TeamVO teamvo = tdao.getTeamByTeamCode(teamCode);
		tdao.saveHistory(teamvo, request);

		// try to remove the approver group member
		ApproverGroupMemberVO apgm = new ApproverGroupMemberVO();
		// try to remove old approver group member.
		if (teamvo != null) {
			if (tdao.getTeamAmountForTL(teamvo.getTlid(), "Y".equals(teamvo.getDepartment())) <= 1) {
				String ostaffcode = staffdao.getStaffCodeByLogonId(teamvo.getTlid());
				apgm.setStaffCode(ostaffcode);
				if ("Y".equals(teamvo.getDepartment())) {
					apgm.setGroupId("12"); // Department Header
					memberDao.delete(apgm);
					if ("".equals(tdao.getManagedTeamCodesByTL(ostaffcode).trim())) {
						apgm.setGroupId("02"); // Team Leader
						memberDao.delete(apgm);
					}
				} else {
					apgm.setGroupId("02"); // Team Leader
					memberDao.delete(apgm);
				}
			}
		}

		// 删除
		tdao.deleteTeam(teamCode);
		return true;
	}
}
