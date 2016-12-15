package com.aiait.eflow.report.action;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import com.aiait.eflow.common.*;
import com.aiait.eflow.common.helper.*;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.housekeeping.dao.*;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.report.dao.EpaymentReportDAO;
import com.aiait.eflow.report.util.EPaymentReportPDFUtil;
import com.aiait.eflow.report.vo.InvoiceInfoVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.action.WorkFlowProcessAction;
import com.aiait.eflow.wkf.dao.*;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.common.GlobalCommonName;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.page.*;
import com.lowagie.text.*;

public class EpaymentReportAction extends WorkFlowProcessAction {

    /**
     * enter summary report page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enterPage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "epaymentReport";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EpaymentReportDAO dao = new EpaymentReportDAO(dbManager);
            // 获取所有有效的form
            Collection cashierList = dao.cashierQuery();
            request.setAttribute("cashierList", cashierList);
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
    public ActionLocation exportToExcel(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        InvoiceInfoVO vo = new InvoiceInfoVO();

        Map reqParams = new HashMap();
        if (request.getSession().getAttribute("cachedParams") != null) {// "查询"用过的参数，"生成报表"时可以直接重用
            Map tempMap = (Map) request.getSession().getAttribute("cachedParams");
            Iterator it = tempMap.keySet().iterator();
            while (it.hasNext()) {// 不能简单地复制，因为这里是传形参
                String key = (String) it.next();
                reqParams.put(key, tempMap.get(key));
            }
        } else {
            Map tempMap = request.getParameterMap();
            Iterator it = tempMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                reqParams.put(key, tempMap.get(key));
            }
            request.getSession().setAttribute("cachedParams", reqParams);
        }

        String reportType = ((String[]) reqParams.get("reportType"))[0];
        String[] fid = ((String[]) reqParams.get("formIds"))[0].split(",");
        Arrays.sort(fid); // important!
        String begin = ((String[]) reqParams.get("beginDateStr"))[0];
        String end = ((String[]) reqParams.get("endDateStr"))[0];
        String companyId = ((String[]) reqParams.get("companyId"))[0];
        String requestedBy = reqParams.get("requestedBy") != null ? ((String[]) reqParams.get("requestedBy"))[0] : null;
        String status = reqParams.get("status") != null ? ((String[]) reqParams.get("status"))[0] : null;
        String cashierCD = ((String[]) reqParams.get("cashierCD"))[0];
        String paymode = reqParams.get("paymode") != null ? ((String[]) reqParams.get("paymode"))[0] : null;
        String flowOrgId = reqParams.get("flowOrgId") != null ? ((String[]) reqParams.get("flowOrgId"))[0] : null;
        String paramMergeAccount = reqParams.get("mergeAccount") != null ? ((String[]) reqParams.get("mergeAccount"))[0]
                : null;
        boolean mergeAccount = paramMergeAccount != null && paramMergeAccount.equals("Y") ? true : false;

        String paramSaveRemark = reqParams.get("save_remark") != null ? ((String[]) reqParams.get("save_remark"))[0]
                : null;
        boolean saveRemark = paramSaveRemark != null && paramSaveRemark.equals("Y") ? true : false;
        String remarkReqNo = reqParams.get("remarkReqNo") != null ? ((String[]) reqParams.get("remarkReqNo"))[0] : null;
        String remark = reqParams.get("remark") != null ? ((String[]) reqParams.get("remark"))[0] : null;

        boolean writeHistory = false;
        if ("EXCEL".equalsIgnoreCase(request.getParameter("display"))) {
            returnLabel = "xls" + reportType + "Report";
            writeHistory = true;
        } else if ("PDF".equalsIgnoreCase(request.getParameter("display"))) {
            writeHistory = true;
        } else {
            returnLabel = "list" + reportType + "Report";
        }
        String orderPhrase = request.getParameter("orderphrase");
        IDBManager dbManager = null;
        Collection list = null;
        double totalAmount = 0;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list
            Collection formList = formDao.getFormList("0");
            request.setAttribute("formList", formList);
            EpaymentReportDAO reportDao = new EpaymentReportDAO(dbManager);

            if (reportType.toUpperCase().equals("INVOICE")) {
                list = reportDao.invoiceQuery(fid, begin, end, companyId, flowOrgId, status, orderPhrase);
            } else if (reportType.toUpperCase().equals("PAYMENT")) {
                System.out.println("reportDao.paymentQuery - writeHistory: " + writeHistory);
                list = reportDao.paymentQuery(fid, begin, end, paymode, cashierCD, orderPhrase, mergeAccount,
                        writeHistory);
            } else if (reportType.toUpperCase().equals("STAFFPAYMENT")) {
                if (saveRemark) {
                    reportDao.savePaymentRemark(remarkReqNo, remark);
                    System.out.println("remark: " + remarkReqNo + " / " + remark);
                }
                System.out.println("reportDao.staffQuery - writeHistory: " + writeHistory);
                HashMap amountMap = new HashMap();
                list = reportDao.staffQuery(fid, begin, end, paymode, cashierCD, orderPhrase, mergeAccount,
                        writeHistory, amountMap);
                totalAmount = (Double) amountMap.get("totalAmount");
                request.setAttribute("totalAmount", totalAmount);
            } else if (reportType.toUpperCase().equals("CA")) {
                list = reportDao.caQuery(fid, begin, end, companyId, requestedBy, status, orderPhrase);
            }

            request.setAttribute("resultList", list);
            request.getSession().setAttribute("resultList", list);
            request.setAttribute("formSystemId", vo.getFormSystemId());
            request.setAttribute("beginDate", begin == null ? "?" : begin);
            request.setAttribute("endDate", end == null ? "?" : end);
            request.setAttribute("companyId", companyId == null ? "?" : companyId);
            request.setAttribute("flowOrgId", flowOrgId == null ? "?" : flowOrgId);
            request.setAttribute("cashierCD", cashierCD == null ? "?" : cashierCD);
            request.setAttribute("paymode", paymode == null ? "?" : paymode);
            request.setAttribute("display", request.getParameter("display"));
            request.setAttribute("requestedBy", requestedBy == null ? "?" : requestedBy);
            request.setAttribute("status", status == null ? "?" : status);
            request.setAttribute("mergeAccount", mergeAccount == true ? "Y" : "N");
        } catch (DAOException e) {
            e.printStackTrace();
            returnLabel = "fail";
            return mapping.findActionLocation(returnLabel);
        } finally {
            dbManager.freeConnection();
        }

        if ("PDF".equalsIgnoreCase(request.getParameter("display"))) {
            boolean landscape = false; // reserved for enhancement
            String pageSize = "2"; // reserved for enhancement
            if (reportType.toUpperCase().equals("PAYMENT")) {
                landscape = true;
            } else if (reportType.toUpperCase().equals("STAFFPAYMENT")) {
                landscape = false;
            }

            Rectangle page = null;
            if (!landscape) {
                if ("".equals(pageSize) || "2".equals(pageSize)) {
                    page = PageSize.A4;
                } else if ("1".equals(pageSize)) {
                    page = PageSize.A3;
                } else if ("3".equals(pageSize)) {
                    page = PageSize.B3;
                } else if ("4".equals(pageSize)) {
                    page = PageSize.B4;
                }
            } else {
                if ("".equals(pageSize) || "2".equals(pageSize)) {
                    page = PageSize.A4.rotate();
                } else if ("1".equals(pageSize)) {
                    page = PageSize.A3.rotate();
                } else if ("3".equals(pageSize)) {
                    page = PageSize.B3.rotate();
                } else if ("4".equals(pageSize)) {
                    page = PageSize.B4.rotate();
                }
            }

            ByteArrayOutputStream baosPDF = null;
            try {
                if (reportType.toUpperCase().equals("PAYMENT")) {
                    baosPDF = EPaymentReportPDFUtil.exportPayment(page, list);
                } else if (reportType.toUpperCase().equals("STAFFPAYMENT")) {
                    baosPDF = EPaymentReportPDFUtil.exportStaffPayment(page, list, totalAmount);
                } else {
                    throw new DocumentException("Sorry! can not export this type of report.");
                }

                StringBuffer sbFilename = new StringBuffer();
                sbFilename.append("filename_");
                sbFilename.append(System.currentTimeMillis());
                sbFilename.append(".pdf");

                response.setHeader("Cache-Control", "max-age=30");
                response.setContentType("application/pdf");

                StringBuffer sbContentDispValue = new StringBuffer();
                sbContentDispValue.append("inline");
                sbContentDispValue.append("; filename=");
                sbContentDispValue.append(sbFilename);

                response.setHeader("Content-disposition", sbContentDispValue.toString());

                response.setContentLength(baosPDF.size());
                ServletOutputStream sos;
                sos = response.getOutputStream();

                baosPDF.writeTo(sos);
                sos.flush();

            } catch (DocumentException ex) {
                response.setContentType("text/html");
                PrintWriter writer = response.getWriter();
                writer.println(this.getClass().getName() + " caught an exception: " + ex.getClass().getName() + "<br>");
                writer.println("<pre>");
                ex.printStackTrace(writer);
                writer.println("</pre>");
            } finally {
                if (baosPDF != null) {
                    baosPDF.reset();
                }
            }

            return null;
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
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation summaryReportHandle(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            request.getSession().setAttribute("cachedParams", null);// 清除上次的参数
            return this.exportToExcel(mapping, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findActionLocation("fail");
        } finally {
        }
    }

    public ActionLocation overdueNotify(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // System.out.println(request.getParameterValues("mail_form_id")[0]);
        String[] requestNoList = request.getParameterValues("mail_form_id")[0].split(",");
        if (requestNoList == null || "".equals(request.getParameterValues("mail_form_id")[0])) {
            return this.summaryReportHandle(mapping, request, response);
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);

            EmailTemplateVO template = null;
            if ("Invoice".equals(request.getParameter("reportType")))
                template = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_INVOICE_REMINDER);
            else
                template = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_CA_REMINDER);

            if (template == null) {
                String msg = "Can not find email template ";
                // System.out.println(msg);
                request.setAttribute(CommonName.COMMON_ERROR_INFOR, msg);
                return mapping.findActionLocation("fail");
            }

            WorkFlowProcessDAO processDao = new WorkFlowProcessDAO(dbManager);
            for (int i = 0; i < requestNoList.length; i++) {
                String requestNo = requestNoList[i].split("=")[0];
                WorkFlowProcessVO processVo = processDao.getProcessVO(requestNo);
                String processor = processVo.getRequestStaffCode();// .getCurrentProcessor();
                if (processor == null || processor.equals("")) {
                    continue;
                }
                String overtime = "-1";// request.getParameter(requestNo);
                int index = overtime.indexOf("-");
                overtime = index == -1 ? overtime : overtime.substring(index + 1);
                String[] toStaff = StringUtil.split(processor + "," + currentStaff.getStaffCode(), ",");
                sendEmail(template, processVo.getRequestStaffCode(), requestNo, currentStaff.getStaffCode(), overtime,
                        toStaff, Integer.toString(processVo.getFormSystemId()), processVo);
            }
        } catch (DAOException ex) {
            ex.printStackTrace();
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        return this.summaryReportHandle(mapping, request, response);
    }

    /**
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation invoiceReceive(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            EpaymentReportDAO dao = new EpaymentReportDAO(dbManager);
            if (dao.invoiceReceive(request.getParameterValues("mail_form_id")[0].split(",")) != 0)
                throw new DAOException();
            dbManager.commit();
        } catch (DAOException e) {
            dbManager.rollback();
            e.printStackTrace();
            return mapping.findActionLocation("fail");
        } finally {
            dbManager.freeConnection();
        }
        return this.summaryReportHandle(mapping, request, response);
    }

    /**
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation caRepaid(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            dbManager.startTransaction();
            EpaymentReportDAO dao = new EpaymentReportDAO(dbManager);
            if (dao.caRepaid(request.getParameterValues("mail_form_id")[0].split(",")) != 0)
                throw new DAOException();
            dbManager.commit();
        } catch (DAOException e) {
            e.printStackTrace();
            dbManager.rollback();
            if (e.getMessage().indexOf("duplicate key") > -1)
                request.setAttribute("error", "重复标记还款！");
            return mapping.findActionLocation("fail");
        } finally {
            dbManager.freeConnection();
        }
        return this.summaryReportHandle(mapping, request, response);
    }

    public ActionLocation paymentInquiry(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String resultLabel = "paymentInquiry";
        String formType = (String) request.getParameter("formType");
        formType = "12"; // ePayment
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();

        // 增加分页查询功能
        String pagenum = (String) request.getParameter(GlobalCommonName.PAGE_CURRENT_NUM);
        if (pagenum == null) {
            pagenum = "1";
        }
        // HashMap paramMap = new HashMap(); //用来保存查询参数
        PageVO page = new PageVO(Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(
                CommonName.EVERY_PAGE_RECORDS_NUM, CommonName.DEFAULT_RECORDS_NUM)));
        page.setCurrentPage(Integer.parseInt(pagenum));

        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
        }
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();
        String lowerOrgs = currentStaff.getLowerCompanys();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);
            request.setAttribute("formSelectList", formSelectList);

            String orgId = (String) request.getParameter("companyId");

            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                Collection list = new ArrayList();
                request.setAttribute("overtimeFormList", list);
                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginSubmissionDate = "";
                String endSubmissionDate = "";
                SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date now = new Date();
                endSubmissionDate = bartDateFormat.format(now);
                beginSubmissionDate = StringUtil.afterNDay(days, "MM/dd/yyyy");
                request.setAttribute("beginSubmissionDate", beginSubmissionDate);
                request.setAttribute("endSubmissionDate", endSubmissionDate);

                // default same as submission date
                request.setAttribute("beginCompletedDate", beginSubmissionDate);
                request.setAttribute("endCompletedDate", endSubmissionDate);
            } else {
                String requestNo = (String) request.getParameter("requestNo");
                String status = (String) request.getParameter("status");

                String requestedBy = (String) request.getParameter("requestedBy");
                String beginSubmissionDate = (String) request.getParameter("beginSubmissionDate");
                String endSubmissionDate = (String) request.getParameter("endSubmissionDate");

                if (requestNo != null && !"".equals(requestNo)) {
                    vo.setRequestNo(requestNo);
                } else {
                    vo.setRequestNo("");
                }
                if (status != null && !"".equals(status)) {
                    vo.setStatus(status);
                }

                vo.setOrgId(orgId);

                if (requestedBy != null && !"".equals(requestedBy)) {
                    vo.setRequestStaffCode(requestedBy);
                } else {
                    vo.setRequestStaffCode("");
                }

                vo.setBeginSubmissionDate(beginSubmissionDate);
                vo.setEndSubmissionDate(endSubmissionDate);

                String formSystemId = (String) request.getParameter("formSystemId");
                if (formSystemId != null && !"".equals(formSystemId)) {
                    vo.setFormSystemId(Integer.parseInt(formSystemId));
                }

                String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
                vo.setBeginCompleteDate(beginCompletedDate);
                String endCompletedDate = (String) request.getParameter("endCompletedDate");
                vo.setEndCompleteDate(endCompletedDate);

                String cashier = (String) request.getParameter("cashier");
                if (cashier != null && !"".equals(cashier)) {
                    vo.setPreviousProcessor(cashier);
                    vo.setStatus("04");
                }

                ListInquiryFormDAO dao = new ListInquiryFormDAO(dbManager);

                int totalRecordsNum = dao.getTotalRecordsNum(vo, page, supperOrgs);
                page = PageUtil.createPage(page, totalRecordsNum);
                Collection list = dao.searchInquiryForm(vo, page, supperOrgs);
                request.setAttribute(GlobalCommonName.PAGE_INFORMATION_NAME, page);
                request.setAttribute("inquiryFormList", list);
            }

            if (orgId == null) {
                StaffDAO staffDao = new StaffDAO(dbManager);
                Collection staffList = staffDao.getStaffListByCompany(currentStaff.getOrgId());
                request.setAttribute("staffList", staffList);
            } else if (!orgId.equals("")) {
                StaffDAO staffDao = new StaffDAO(dbManager);
                Collection staffList = staffDao.getStaffListByCompany(orgId);
                request.setAttribute("staffList", staffList);
            }

            EpaymentReportDAO dao = new EpaymentReportDAO(dbManager);
            Collection cashierList = dao.cashierQuery();
            request.setAttribute("cashierList", cashierList);
        } catch (DAOException e) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation interPaymentInquiry(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "interPaymentInquiry";
        IDBManager dbManager = null;
        WorkFlowProcessVO vo = new WorkFlowProcessVO();

        String param = "tables_with_cost_company";
        List<String[]> tableConfig = (List<String[]>) request.getAttribute(param);
        if (tableConfig == null) {
            tableConfig = new ArrayList<String[]>();
            String paramValue = ParamConfigHelper.getInstance().getParamValue(param);
            if (paramValue != null && !(paramValue = paramValue.trim()).equals("")) {
                String[] _tableConfig = paramValue.split(";");
                for (int i = 0; i < _tableConfig.length; i++) {
                    tableConfig.add(_tableConfig[i].trim().split("_"));
                }
                request.setAttribute(param, tableConfig);
            }
        }

        String formType = (String) request.getParameter("formType");
        formType = "12"; // ePayment
        if (formType != null && !"".equals(formType)) {
            vo.setFormType("'" + formType + "'");
        } else {
            // 如果是查询所有form，则需要知道当前user可以查看到的form类型
            String formTypes = "";
            StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if (typeList != null && typeList.size() > 0) {
                Iterator it = typeList.iterator();
                while (it.hasNext()) {
                    FormTypeVO typeVo = (FormTypeVO) it.next();
                    if (authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),
                            ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                        formTypes = formTypes + "'" + typeVo.getFormTypeId() + "',";
                    }
                }
            }
            if (!"".equals(formTypes)) {
                formTypes = formTypes.substring(0, formTypes.length() - 1);
            }
            vo.setFormType(formTypes);
        }

        String formSystemId = (String) request.getParameter("formSystemId");
        if (formSystemId != null && !"".equals(formSystemId)) {
            vo.setFormSystemId(Integer.parseInt(formSystemId));
        }

        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String supperOrgs = currentStaff.getUpperCompanys();
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            Collection _formSelectList = formDao.getFormListByTypeList(vo.getFormType(), supperOrgs, null);
            Iterator it = _formSelectList.iterator();
            List formSelectList = new ArrayList();
            List<String[]> _tableConfig = new ArrayList<String[]>();
            while (it.hasNext()) {
                FormManageVO form = (FormManageVO) it.next();
                for (int i = 0; i < tableConfig.size(); i++) {
                    if (form.getFormSystemId() == Integer.parseInt(tableConfig.get(i)[0])) {
                        formSelectList.add(form);
                        if (vo.getFormSystemId() == 0 || vo.getFormSystemId() == form.getFormSystemId()) {
                            _tableConfig.add(tableConfig.get(i));
                        }
                        break;
                    }
                }
            }
            request.setAttribute("formSelectList", formSelectList);

            // if user enter the page by clicking the left menu tree,it needn't
            // to query!
            String needQuery = (String) request.getParameter("needquery");
            if (needQuery != null && "false".equals(needQuery)) {
                List list = new ArrayList();
                request.setAttribute("inquiryList", list);

                // 赋予一个初始化的时间段（一个星期）
                ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
                String defaultDays = paramHelper.getParamValue("list_data_days");
                int days = 0;
                if (defaultDays == null || "".equals(defaultDays)) {
                    days = -7;
                } else {
                    days = -Integer.parseInt(defaultDays);
                }
                String beginCompletedDate = "";
                String endCompletedDate = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                endCompletedDate = dateFormat.format(new Date());
                beginCompletedDate = StringUtil.afterNDay(days, "MM/dd/yyyy");
                request.setAttribute("beginCompletedDate", beginCompletedDate);
                request.setAttribute("endCompletedDate", endCompletedDate);
            } else {
                String requestOrgId = (String) request.getParameter("requestOrgId");
                vo.setOrgId(requestOrgId);
                String costOrgId = (String) request.getParameter("costOrgId");
                vo.setOrgName(costOrgId);

                String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
                vo.setBeginCompleteDate(beginCompletedDate);
                String endCompletedDate = (String) request.getParameter("endCompletedDate");
                vo.setEndCompleteDate(endCompletedDate);

                EpaymentReportDAO dao = new EpaymentReportDAO(dbManager);
                List list = dao.queryInterPayment(vo, _tableConfig);
                request.setAttribute("inquiryList", list);
            }
        } catch (DAOException e) {
            resultLabel = "fail";
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, e.toString());
            e.printStackTrace();
        } finally {
            dbManager.freeConnection();
        }
        return mapping.findActionLocation(resultLabel);
    }

    public ActionLocation interPaymentReportXls(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String resultLabel = "interPaymentReportXls";
        this.interPaymentInquiry(mapping, request, response);
        if (request.getAttribute(CommonName.COMMON_ERROR_INFOR) != null) {
            resultLabel = "fail";
        }
        return mapping.findActionLocation(resultLabel);
    }

}
