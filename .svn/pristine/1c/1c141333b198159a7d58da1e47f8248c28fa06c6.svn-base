package com.aiait.eflow.housekeeping.action;

import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class EmailTemplateAction extends DispatchAction {
    /**
     * list all template
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation listTemplate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listPage";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            Collection list = dao.getEmailTemplateList();
            request.setAttribute("resultList", list);
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * show edit page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation enternEditPage(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editPage";
        String id = (String) request.getParameter("id");
        IDBManager dbManager = null;
        if (id != null && !"".equals(id)) {
            try {
                dbManager = DBManagerFactory.getDBManager();
                EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
                EmailTemplateVO vo = dao.getEmailTemplate(Integer.parseInt(id));
                request.setAttribute("template", vo);
            } catch (Exception e) {
                returnLabel = "fail";
                e.printStackTrace();
            } finally {
                if (dbManager != null)
                    dbManager.freeConnection();
            }
        } else {
            request.setAttribute("template", new EmailTemplateVO());
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * save the template
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation saveTemplate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "saveSuccess";
        EmailTemplateVO vo = new EmailTemplateVO();
        vo.setName((String) request.getParameter("name"));
        vo.setEmailSubject((String) request.getParameter("emailSubject"));
        vo.setEmailContent((String) request.getParameter("emailContent"));
        vo.setDescription((String) request.getParameter("description"));
        String id = (String) request.getParameter("id");
        vo.setAppliedAction((String) request.getParameter("appliedAction"));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            if (id != null && !"0".equals(id)) {
                vo.setId(Integer.parseInt(id));
                dao.update(vo);
            } else {
                dao.save(vo);
            }
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * delete the template
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation deleteTemplate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "saveSuccess";
        EmailTemplateVO vo = new EmailTemplateVO();
        String[] ids = request.getParameterValues("id");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            dbManager.startTransaction();
            for (int i = 0; i < ids.length; i++) {
                vo.setId(Integer.parseInt(ids[i]));
                dao.delete(vo);
            }
            dbManager.commit();
        } catch (Exception e) {
            returnLabel = "fail";
            dbManager.rollback();
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    /**
     * to show bind action page
     * 
     * @param mapping
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionLocation showBindActionPage(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "bindActionPage";
        String id = (String) request.getParameter("id");
        String existActionIds = (String) request.getParameter("appliedAction");
        Collection fromList = new ArrayList();
        Collection toList = new ArrayList();

        List<String> _actionIds = new ArrayList<String>();
        _actionIds.add(CommonName.HANDLE_TYPE_DRAFT);
        _actionIds.add(CommonName.HANDLE_TYPE_SUBMITT);
        _actionIds.add(CommonName.HANDLE_TYPE_WITHDRAW);
        _actionIds.add(CommonName.HANDLE_TYPE_APPROVE);
        _actionIds.add(CommonName.HANDLE_TYPE_REJECT);
        _actionIds.add(CommonName.HANDLE_TYPE_COMPLETE);
        _actionIds.add(CommonName.HANDLE_TYPE_INVITED_EXPERT);
        _actionIds.add(CommonName.HANDLE_TYPE_EXPERT_ADVISE);
        _actionIds.add(CommonName.ACTION_OVERDUE_NOTIFICATION);
        _actionIds.add(CommonName.HANDLE_TYPE_WAITING_REJECT);
        _actionIds.add(CommonName.HANDLE_TYPE_WAITING_REMINDER);
        _actionIds.add(CommonName.HANDLE_TYPE_DH_REJECT);        
        _actionIds.add(CommonName.HANDLE_SUFFIX);
        _actionIds.add(CommonName.HANDLE_TYPE_COMPLETE_CC);
        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
            _actionIds.add(CommonName.HANDLE_TYPE_PAYMENT);
            _actionIds.add(CommonName.HANDLE_INVOICE_REMINDER);
            _actionIds.add(CommonName.HANDLE_CA_REMINDER);
        }
        String[] srcActionIds = _actionIds.toArray(new String[_actionIds.size()]);

        String otherTemplateAppliedAction = "";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            otherTemplateAppliedAction = dao.getOtherExistAction(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }

        if (existActionIds == null || existActionIds.length() == 0) {
            for (int i = 0; i < srcActionIds.length; i++) {
                if (otherTemplateAppliedAction.indexOf(srcActionIds[i]) > -1) {
                    continue;
                } else {
                    FlowActionVO action = new FlowActionVO();
                    action.setActionId(srcActionIds[i]);
                    action.setActionName(DataMapUtil.convertHandleType(srcActionIds[i]));
                    fromList.add(action);
                }
            }
        } else {
            String[] existActionId = StringUtil.split(existActionIds, ",");
            for (int i = 0; i < existActionId.length; i++) {
                FlowActionVO action = new FlowActionVO();
                action.setActionId(existActionId[i]);
                action.setActionName(DataMapUtil.convertHandleType(existActionId[i]));
                toList.add(action);
            }
            for (int i = 0; i < srcActionIds.length; i++) {
                if (otherTemplateAppliedAction.indexOf(srcActionIds[i]) > -1
                        || existActionIds.indexOf(srcActionIds[i]) > -1) {
                    continue;
                } else {
                    FlowActionVO action = new FlowActionVO();
                    action.setActionId(srcActionIds[i]);
                    action.setActionName(DataMapUtil.convertHandleType(srcActionIds[i]));
                    fromList.add(action);
                }
            }
        }
        request.setAttribute("fromList", fromList);
        request.setAttribute("toList", toList);
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation addByFormType(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editByFormType";
        String id = (String) request.getParameter("id");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO vo = dao.getEmailTemplate(Integer.parseInt(id));
            request.setAttribute("template", vo);
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation saveTemplateByFormType(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "saveSuccess";
        String formType = request.getParameter("formType");
        EmailTemplateVO vo = new EmailTemplateVO();
        vo.setId(Integer.parseInt(request.getParameter("id")));
        vo.setEmailSubject(request.getParameter("emailSubject"));
        vo.setEmailContent(request.getParameter("emailContent"));
        vo.setDescription(request.getParameter("description"));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            dao.saveByFormType(vo, formType);
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation editByFormType(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editByFormType";
        String id = (String) request.getParameter("id");
        String formType = request.getParameter("formType");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO vo = dao.getEmailTemplate(Integer.parseInt(id));
            request.setAttribute("template", vo);
            EmailTemplateVO templateFormType = dao.getByFormType(Integer.parseInt(id), formType);
            request.setAttribute("templateFormType", templateFormType);
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation deleteTemplateByFormType(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String returnLabel = "saveSuccess";
        String id = (String) request.getParameter("id");
        String formType = request.getParameter("formType");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO dao = new EmailTemplateDAO(dbManager);
            dao.deleteByFormType(Integer.parseInt(id), formType);
        } catch (Exception e) {
            returnLabel = "fail";
            e.printStackTrace();
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

}
