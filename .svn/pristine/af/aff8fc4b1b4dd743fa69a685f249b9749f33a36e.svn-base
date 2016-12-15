package com.aiait.eflow.report.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.common.*;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.housekeeping.dao.StaffDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.report.dao.ReportDAO;
import com.aiait.eflow.report.vo.*;
import com.aiait.eflow.util.*;
import com.aiait.eflow.wkf.dao.*;
import com.aiait.eflow.wkf.vo.*;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.*;

public class ReportAction extends DispatchAction {

    /**
     * enter personal deal summary report page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterPersonalDeal(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "personalSummary";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // 获取所有有效的form
            Collection formList = formDao.getFormList("0");
            request.setAttribute("formList", formList);
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        request.setAttribute("resultList", new ArrayList());
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation summaryPersonalHandle(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "personalSummary";
        PersonalHandleSummaryVO vo = new PersonalHandleSummaryVO();
        vo.setFormSystemId((String) request.getParameter("formSystemId"));
        vo.setNodeId((String) request.getParameter("nodeId"));
        if ((String) request.getParameter("beginDateStr") != null
                && !"".equals((String) request.getParameter("beginDateStr"))) {
            vo.setBeginDateStr((String) request.getParameter("beginDateStr"));
        }
        if ((String) request.getParameter("endDateStr") != null
                && !"".equals((String) request.getParameter("endDateStr"))) {
            vo.setEndDateStr((String) request.getParameter("endDateStr"));
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list
            Collection formList = formDao.getFormList("0");
            request.setAttribute("formList", formList);
            ReportDAO reportDao = new ReportDAO(dbManager);
            Collection list = reportDao.personalSummary(vo);
            request.setAttribute("resultList", list);
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            // get all node list for the form
            Collection nodeList = flowDao.getNodeListByForm(Integer.parseInt(vo.getFormSystemId()));
            request.setAttribute("nodeList", nodeList);
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * dynamic creating the form select options
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation getFormList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        try {
            StringBuffer responseXML = new StringBuffer("<options>");
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list
            Collection formList = formDao.getFormList("0", formType);
            if (formList != null && formList.size() > 0) {
                Iterator it = formList.iterator();
                while (it.hasNext()) {
                    FormManageVO form = (FormManageVO) it.next();
                    responseXML.append("<option id='" + form.getFormSystemId() + "'>").append(form.getFormName() + "(")
                            .append(form.getFormId()).append(")").append("</option>");
                }
            }
            responseXML.append("</options>");
            System.out.println(responseXML.toString());
            out.write(responseXML.toString());

        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    /**
     * dynamic creating the form's node select options
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation getNodeList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formSystemId = (String) request.getParameter("formSystemId");
        IDBManager dbManager = null;
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        try {
            StringBuffer responseXML = new StringBuffer("<options>");
            dbManager = DBManagerFactory.getDBManager();
            WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
            // get all node list for the form
            Collection nodeList = flowDao.getNodeListByForm(Integer.parseInt(formSystemId));
            if (nodeList != null && nodeList.size() > 0) {
                Iterator it = nodeList.iterator();
                while (it.hasNext()) {
                    WorkFlowItemVO node = (WorkFlowItemVO) it.next();
                    responseXML.append("<option id='" + node.getItemId() + "'>").append(node.getName())
                            .append("</option>");
                }
            }
            responseXML.append("</options>");
            out.write(responseXML.toString());

        } catch (DAOException e) {
            e.printStackTrace();
            out.print("fail");
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
            if (out != null)
                out.close();
        }
        return null;
    }

    public ActionLocation statusMonitoring(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String returnLabel = "statusMonitoring";
        String beginDateStr = (String) request.getParameter("beginDateStr");
        String endDateStr = (String) request.getParameter("endDateStr");
        String[] tforms = request.getParameterValues("forms");
        String formType = request.getParameter("formType");
        boolean needquery = "false".equals((String) request.getParameter("needquery")) ? false : true;

        String forms = "";

        if (tforms != null && tforms.length > 0) {
            for (int i = 0; i < tforms.length; i++) {
                if (tforms[i].length() == 0)
                    continue;
                if (forms.length() == 0) {
                    forms += "'" + tforms[i] + "'";
                } else {
                    forms += ",'" + tforms[i] + "'";
                }
            }
        }
        if (forms.length() == 0 && formType != null && formType.length() > 0) {
            forms = "select form_system_id from teflow_form where form_type='" + formType + "'";
        }

        if (StringUtil.isEmptyString(beginDateStr)) {
            beginDateStr = StringUtil.afterNDay(-30, "MM/dd/yyyy");
        }
        if (StringUtil.isEmptyString(endDateStr)) {
            endDateStr = StringUtil.afterNDay(0, "MM/dd/yyyy");
        }

        request.setAttribute("forms", forms);
        request.setAttribute("formType", formType);
        request.setAttribute("beginDateStr", beginDateStr);
        request.setAttribute("endDateStr", endDateStr);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportDAO dao = new ReportDAO(dbManager);

            Collection result = null;
            if (needquery) {
                result = dao.statusMonitoring(beginDateStr, endDateStr, forms);
            }

            FormManageDAO formdao = new FormManageDAO(dbManager);
            request.setAttribute("result", result);
            // all published form
            Collection allforms = formdao.getFormListByTypeList(formType, null, "0");
            request.setAttribute("allforms", allforms);

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation processingTrail(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        AuthorityHelper authority = AuthorityHelper.getInstance();

        String returnLabel = "processingTrail";

        String beginDateStr = (String) request.getParameter("beginDateStr");
        String endDateStr = (String) request.getParameter("endDateStr");
        String compBeginDateStr = (String) request.getParameter("compBeginDateStr");
        String compEndDateStr = (String) request.getParameter("compEndDateStr");

        String orgId = (String) request.getParameter("companyId");
        String teamId = (String) request.getParameter("team_code");
        String requestNo = (String) request.getParameter("requestNo");
        String requestedBy = (String) request.getParameter("requestedBy");
        String formSystemId = (String) request.getParameter("formSystemId");
        String formType = (String) request.getParameter("formType");
        boolean needquery = "false".equals((String) request.getParameter("needquery")) ? false : true;
        if (formType == null
                && authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                        ModuleOperateName.MODULE_FORM_INQUIRY, "12"))
            formType = "12";// ePayment

        String status = (String) request.getParameter("status");
        if (status == null)
            status = CommonName.STATUS_COMPLETED;

        boolean ISEXCEL = "EXCEL".equals(request.getParameter("display")) ? true : false;

        // 增加分页查询功能
        String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
        if (pagenum == null) {
            pagenum = "1";
        }
        // HashMap paramMap = new HashMap(); //用来保存查询参数
        PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
        page.setCurrentPage(Integer.parseInt(pagenum));

        if ("04".equals(status) && StringUtil.isEmptyString(beginDateStr) && StringUtil.isEmptyString(compBeginDateStr)) {
            compBeginDateStr = StringUtil.afterNDay(-7, "MM/dd/yyyy");
        }
        if ("04".equals(status) && StringUtil.isEmptyString(endDateStr) && StringUtil.isEmptyString(compEndDateStr)) {
            compEndDateStr = StringUtil.afterNDay(0, "MM/dd/yyyy");
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();

            if (orgId == null || orgId.equals("")) {
                orgId = currentStaff.getOrgId();
            }

            String supperOrgs = currentStaff.getUpperCompanys();// CompanyHelper.getInstance().getUpperCompany(currentStaff.getOrgId());

            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getFormListByTypeList(formType, supperOrgs, null);

            StaffDAO staffDao = new StaffDAO(dbManager);
            Collection staffList = staffDao.getStaffListByTeam(teamId);

            ReportDAO dao = new ReportDAO(dbManager);

            if (!needquery) {
                request.setAttribute("result", null);
            } else {
                if (ISEXCEL) {
                    Collection result = dao.processingTrail(beginDateStr, endDateStr, compBeginDateStr, compEndDateStr,
                            orgId, teamId, requestNo, requestedBy, formSystemId, formType, status);
                    request.setAttribute("result", result);
                } else {
                    int totalRecordsNum = dao.processingTrailCount(beginDateStr, endDateStr, compBeginDateStr,
                            compEndDateStr, orgId, teamId, requestNo, requestedBy, formSystemId, formType, status);
                    page = PageUtil.createPage(page, totalRecordsNum);
                    request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
                    Collection result = dao.processingTrail(beginDateStr, endDateStr, compBeginDateStr, compEndDateStr,
                            orgId, teamId, requestNo, requestedBy, formSystemId, formType, status, page.getPageSize(),
                            page.getCurrentPage());
                    request.setAttribute("result", result);
                }
            }
            request.setAttribute("beginDateStr", beginDateStr);
            request.setAttribute("endDateStr", endDateStr);
            request.setAttribute("compBeginDateStr", compBeginDateStr);
            request.setAttribute("compEndDateStr", compEndDateStr);
            request.setAttribute("staffList", staffList);
            request.setAttribute("formSelectList", formSelectList);
            request.setAttribute("companyId", orgId);
            request.setAttribute("teamId", teamId);
            request.setAttribute("status", status);

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation processingAmount(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String returnLabel = "processingAmount";
        String beginDateStr = (String) request.getParameter("beginDateStr");
        String endDateStr = (String) request.getParameter("endDateStr");
        boolean needquery = "false".equals((String) request.getParameter("needquery")) ? false : true;

        if (StringUtil.isEmptyString(beginDateStr)) {
            beginDateStr = StringUtil.afterNDay(-30, "MM/dd/yyyy");
        }
        if (StringUtil.isEmptyString(endDateStr)) {
            endDateStr = StringUtil.afterNDay(0, "MM/dd/yyyy");
        }

        request.setAttribute("beginDateStr", beginDateStr);
        request.setAttribute("endDateStr", endDateStr);

        String[] tforms = request.getParameterValues("forms");
        String[] torgs = request.getParameterValues("orgs");
        String formType = request.getParameter("formType");

        String forms = "";
        String orgs = "";

        if (tforms != null && tforms.length > 0) {
            for (int i = 0; i < tforms.length; i++) {
                if (tforms[i].length() == 0)
                    continue;
                if (forms.length() == 0) {
                    forms += "'" + tforms[i] + "'";
                } else {
                    forms += ",'" + tforms[i] + "'";
                }
            }
        }

        if (torgs != null && torgs.length > 0) {
            for (int i = 0; i < torgs.length; i++) {
                if (torgs[i].length() == 0)
                    continue;
                if (orgs.length() == 0) {
                    orgs += "'" + torgs[i] + "'";
                } else {
                    orgs += ",'" + torgs[i] + "'";
                }
            }
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ReportDAO dao = new ReportDAO(dbManager);

            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            if (orgs == null || orgs.equals("")) {
                orgs = "'" + currentStaff.getOrgId() + "'";
            }

            Collection result = null;
            if (needquery) {
                result = dao.processingAmount(beginDateStr, endDateStr, orgs, forms, formType);
            }
            request.setAttribute("result", result);

            FormManageDAO formdao = new FormManageDAO(dbManager);
            // all published form
            Collection allforms = formdao.getFormListByTypeList(formType, null, "0");

            request.setAttribute("allforms", allforms);
            request.setAttribute("qforms", forms);
            request.setAttribute("qorgs", orgs);
            request.setAttribute("formType", formType);

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * 调用综合查询的DAO得到具体的表单号
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation processingAmountDetail(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String returnLabel = "successTipPage";
        String beginDateStr = (String) request.getParameter("beginDateStr");
        String endDateStr = (String) request.getParameter("endDateStr");

        request.setAttribute("beginDateStr", beginDateStr);
        request.setAttribute("endDateStr", endDateStr);

        String tform = request.getParameter("form");
        String torg = request.getParameter("org");
        String formType = request.getParameter("formType");
        String status = request.getParameter("status");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            // ReportDAO dao = new ReportDAO(dbManager);
            ListInquiryFormDAO dao = new ListInquiryFormDAO(dbManager);

            // Collection allRequestNos = dao.processingAmount(beginDateStr, endDateStr, orgs, forms, formType);
            WorkFlowProcessVO queryVo = new WorkFlowProcessVO();
            queryVo.setBeginSubmissionDate(beginDateStr);
            queryVo.setEndSubmissionDate(endDateStr);
            queryVo.setOrgId(torg);
            queryVo.setFormSystemId(Integer.parseInt(tform));
            queryVo.setFormType(formType);
            queryVo.setStatus(status);
            PageVO page = new PageVO();
            page.setCurrentPage(0);
            page.setPageSize(65535);

            Collection allForms = dao.searchInquiryForm(queryVo, page, null);
            Iterator it = allForms.iterator();
            String requestNos = "";
            StaffTeamHelper sh = StaffTeamHelper.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            while (it.hasNext()) {
                WorkFlowProcessVO vo = (WorkFlowProcessVO) it.next();
                requestNos += vo.getRequestNo()
                        + " :"
                        + sh.getStaffNameByCode(vo.getRequestStaffCode())
                        + " -> "
                        + sh.getStaffNameByCode(vo.getCurrentProcessor())
                        + ", "
                        + Math.round(OverDueUtil.computeInvertalDays(vo.getSubmissionDateStr().substring(0, 19),
                                df.format(new Date()))) + " days,<br>";
            }

            request.setAttribute(CommonName.COMMON_OK_INFOR, requestNos);

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation processingProgress(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String returnLabel = "processingProgress";
        String beginDateStr = (String) request.getParameter("beginDateStr");
        String endDateStr = (String) request.getParameter("endDateStr");
        boolean needquery = "false".equals((String) request.getParameter("needquery")) ? false : true;

        if (StringUtil.isEmptyString(beginDateStr)) {
            // beginDateStr = StringUtil.afterNDay(-30, "MM/dd/yyyy");
            beginDateStr = StringUtil.afterNDay(-7, "MM/dd/yyyy");
        }
        if (StringUtil.isEmptyString(endDateStr)) {
            endDateStr = StringUtil.afterNDay(0, "MM/dd/yyyy");
        }

        request.setAttribute("beginDateStr", beginDateStr);
        request.setAttribute("endDateStr", endDateStr);

        String[] tforms = request.getParameterValues("forms");
        String[] torgs = request.getParameterValues("orgs");
        String formType = request.getParameter("formType");

        String forms = "";
        String orgs = "";

        if (tforms != null && tforms.length > 0) {
            for (int i = 0; i < tforms.length; i++) {
                if (tforms[i].length() == 0)
                    continue;
                if (forms.length() == 0) {
                    forms += "'" + tforms[i] + "'";
                } else {
                    forms += ",'" + tforms[i] + "'";
                }
            }
        }

        if (torgs != null && torgs.length > 0) {
            for (int i = 0; i < torgs.length; i++) {
                if (torgs[i].length() == 0)
                    continue;
                if (orgs.length() == 0) {
                    orgs += "'" + torgs[i] + "'";
                } else {
                    orgs += ",'" + torgs[i] + "'";
                }
            }
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        if (orgs == null || orgs.equals("")) {
            orgs = "'" + currentStaff.getOrgId() + "'";
        }

        String progressType = request.getParameter("progressType");

        IDBManager dbManager = null;
        List dataList = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            if (needquery) {
                ReportDAO dao = new ReportDAO(dbManager);
                if (progressType.indexOf("Fin") != -1) {
                    dataList = dao.processingProgressByNode(beginDateStr, endDateStr, orgs, forms, formType,
                            progressType);
                } else {
                    dataList = dao.processingProgress(beginDateStr, endDateStr, orgs, forms, formType);
                }
            }

            FormManageDAO formdao = new FormManageDAO(dbManager);
            // all published form
            Collection allforms = formdao.getFormListByTypeList(formType, null, "0");

            request.setAttribute("allforms", allforms);
            request.setAttribute("qforms", forms);
            request.setAttribute("qorgs", orgs);
            request.setAttribute("formType", formType);

        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
            return mapping.findActionLocation(returnLabel);
        } finally {
            dbManager.freeConnection();
        }

        // Should be configurable in the future.
        int[] measureDays = {3, 5, 7, 10, 21};
        request.setAttribute("measureDays", measureDays);

        // Count for progress table
        ProcessProgressTable progressTable = new ProcessProgressTable(measureDays);
        if (needquery) {
            progressTable.loadData(dataList, progressType);
        }
        request.setAttribute("progressTable", progressTable);

        return mapping.findActionLocation(returnLabel);
    }

}
