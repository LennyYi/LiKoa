package com.aiait.eflow.formmanage.action;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.*;

import javax.servlet.http.*;

import jcifs.UniAddress;
import jcifs.smb.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.housekeeping.dao.*;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.util.*;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.i18n.I18NMessageHelper;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.mvc.util.Global;

public class LogonAction extends DispatchAction {

    /**
     * ϵͳĬ�������л�
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation changeLanguage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "showLoginPage";

        // �ı�����
        String targetLanguage = (String) request.getParameter("language");
        if (targetLanguage == null || "".equals(targetLanguage)) {
            targetLanguage = CommonName.LOCALE_LANGUAGE_EN;
        }

        I18NMessageHelper.changeLocale(targetLanguage);

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        if (staff != null) {
            returnLabel = "logOnSuccess";
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation enterLogin(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "showLoginPage";

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        if (staff != null) {
            returnLabel = "logOnSuccess";
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation logOn(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "logOnSuccess";
        Date currentDate = StringUtil.stringToDate(StringUtil.getCurrentDateStr("MM/dd/yyyy"), "MM/dd/yyyy");

        String logonId = request.getRemoteUser();
        if (logonId != null) {
            String[] tmp = StringUtil.split(logonId, "\\");
            logonId = tmp[tmp.length - 1];
        } else {
            StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            if (staff == null) {
                return mapping.findActionLocation("logOutSuccess");
            }
            logonId = staff.getLogonId();
        }

        // logonId = "tester";
        System.out.println("Login Id: " + logonId);
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            // FormManageDAO formDao = new FormManageDAO(dbManager);
            StaffDAO staffDao = new StaffDAO(dbManager);
            // String logonId = (String)request.getParameter("logonId");
            StaffVO currentStaff = staffDao.getStaffByLogonId(logonId);
            // �����û��Ƿ���eflowϵͳ���Ѿ���Ȩ
            if (currentStaff == null) {
                // ������û���eflowϵͳ��û�з����ɫ,���ȡʹ��Ĭ�Ͻ�ɫ��roleId='00'��
                currentStaff = staffDao.getStaffByDefaultRoel(logonId);
                if (currentStaff == null) {
                    String paramUserReg = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_USER_REGISTER);
                    if ("Yes".equalsIgnoreCase(paramUserReg)) {
                        request.setAttribute("logonId", logonId);
                        request.getRequestDispatcher("/housekeeping/userRegister.jsp").forward(request, response);
                        return null;
                    } else if ("IDM".equalsIgnoreCase(paramUserReg)) {
                        request.setAttribute("logonId", logonId);
                        request.getRequestDispatcher("/housekeeping/userRegister_idm.jsp").forward(request, response);
                        return null;
                    }
                    request.setAttribute("error", "You(" + logonId
                            + ") are not authorized.Please contact system Administrator.");
                    return mapping.findActionLocation("fail");
                } else {
                    if (currentStaff.getUserType() != null && "1".equals(currentStaff.getUserType())) { // ������û������ǡ���ʱ�û���������Ҫ�ж��Ƿ��Ѿ�������Ч�ڼ䣬����ǣ��������¼
                        EflowStaffVO vo = staffDao.getActiveStaffByStaffCode(currentStaff.getStaffCode());
                        Date fromDate = StringUtil.stringToDate(vo.getFromdate(), "MM/dd/yyyy");
                        Date toDate = StringUtil.stringToDate(vo.getTodate(), "MM/dd/yyyy");
                        if (currentDate.before(fromDate) || currentDate.after(toDate)) {
                            request
                                    .setAttribute(
                                            "error",
                                            "You("
                                                    + logonId
                                                    + ") are temporary user, Your account beyond the expiration date.<br>Please contact system Administrator.");
                            return mapping.findActionLocation("fail");
                        }
                    }
                }
            } else {
                if (currentStaff.getUserType() != null && "1".equals(currentStaff.getUserType())) { // ������û������ǡ���ʱ�û���������Ҫ�ж��Ƿ��Ѿ�������Ч�ڼ䣬����ǣ��������¼
                    EflowStaffVO vo = staffDao.getActiveStaffByStaffCode(currentStaff.getStaffCode());
                    Date fromDate = StringUtil.stringToDate(vo.getFromdate(), "MM/dd/yyyy");
                    Date toDate = StringUtil.stringToDate(vo.getTodate(), "MM/dd/yyyy");
                    if (currentDate.before(fromDate) || currentDate.after(toDate)) {
                        request
                                .setAttribute(
                                        "error",
                                        "You("
                                                + logonId
                                                + ") are temporary user, Your account beyond the expiration date.<br>Please contact system Administrator.");
                        return mapping.findActionLocation("fail");
                    }
                }
            }
            RoleDAO roleDao = new RoleDAO(dbManager);
            // ÿ���û���Ӧ����Ĭ�Ͻ�ɫ��roleId='00'��������Ȩ��
            Collection functionList = roleDao.getFunctionsByRole(currentStaff.getCurrentRoleId()
                    + CommonName.MODULE_ROLE_SPLIT_SIGN + "00");
            if (functionList == null || functionList.size() == 0) {
                request.setAttribute("error", "You(" + logonId
                        + ") are not authorized. Please contact system Administrator.");
                return mapping.findActionLocation("fail");
            }
            // ��ȡ������Ч��form
            // Collection formList = formDao.getFormList("0");
            // request.setAttribute("formList",formList);
            //
            CompanyDAO companyDao = new CompanyDAO(dbManager);
            // ��ȡ��ǰ��¼�ߵĹ�˾�������µ����й�˾�ļ���
            currentStaff.setOwnCompanyList(companyDao.getCompanyList(currentStaff.getOrgId()));
            // ��ȡ�ù�˾�������ϼ���˾(�����Լ���
            currentStaff.setUpperCompanys(companyDao.getSuperCompanys(currentStaff.getOrgId()));
            // ��ȡ�ù�˾�������¼���˾�������Լ���
            String lowerCompanyIds = "'" + currentStaff.getOrgId() + "'";
            String temp = companyDao.getSubCompanys(currentStaff.getOrgId());
            if (temp != null && !"".equals(temp)) {
                lowerCompanyIds = lowerCompanyIds + "," + temp;
            }
            currentStaff.setLowerCompanys(lowerCompanyIds);
            // ��ȡ������˾�Ĳ��ż���
            TeamDAO teamDao = new TeamDAO(dbManager);
            Collection teamList = teamDao.getTeamListByCompany(currentStaff.getOrgId());
            currentStaff.setTeamList(teamList);

            request.getSession().setAttribute(CommonName.CURRENT_STAFF_INFOR, currentStaff);
            if("1".equals(request.getParameter("nonIE"))){
            	request.getSession().setAttribute("nonIE", "1");
            	request.getSession().setAttribute(Global.LOCALE_LANGUAGE, new Locale("en"));
            }

        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation logOut(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "logOutSuccess";

        StaffVO staff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        // ��������صĻ��������ÿ�
        if (staff != null) {
            BaseDataUtil.setInstanceNull(staff.getCurrentRoleId());
        }

        request.getSession().removeAttribute(CommonName.CURRENT_STAFF_INFOR);
        request.getSession().invalidate();

        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation showMessage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        StringBuffer responseXML = new StringBuffer("<messages>");
        responseXML.append("<message id='1'>Welcome to eFlow System!</message>");
        responseXML.append("</messages>");
        out.write(responseXML.toString());
        if (out != null)
            out.close();
        return null;
    }

    private String validUserByJcifs(String domain, String user, String passwd) {
        try {
            UniAddress mydomaincontroller = UniAddress.getByName(domain, true);
            // UniAddress mydomaincontroller = UniAddress.getByName( "192.168.1.15" );
            NtlmPasswordAuthentication mycreds = new NtlmPasswordAuthentication(domain, user, passwd);
            SmbSession.logon(mydomaincontroller, mycreds);
            return "ok";
            // SUCCESS
        } catch (UnknownHostException uhe) {
            System.out.println("logon Failed0");
            uhe.printStackTrace();
            return "Can't get domain controler. Please contact technique supporter.";
        } catch (SmbAuthException sae) {
            // AUTHENTICATION FAILURE
            System.out.println("logon Failed1");
            sae.printStackTrace();
            return sae.getMessage();
        } catch (SmbException se) {
            // NETWORK PROBLEMS?
            System.out.println("logon Failed2");
            se.printStackTrace();
            return "Logon Failed. Network problem. Please contact technique supporter.";
        }

    }

}
