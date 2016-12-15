package com.aiait.eflow.service.action;

import java.io.PrintWriter;

import javax.servlet.http.*;

import com.aiait.eflow.service.dao.SecurityServiceDAO;
import com.aiait.eflow.service.vo.ServiceUserVo;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

/**
 * Security Service Action
 * 
 * @version 2011-01-13
 */
public class SecurityServiceAction extends ServiceAction {

    public ActionLocation updatePassword(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        if (!authenticate(request, response)) {
            out.print("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String serviceId = StringUtil.decoderURL(request.getParameter("serviceId"));
        String newPassword = Long.toString(System.currentTimeMillis());

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SecurityServiceDAO securityDao = new SecurityServiceDAO(dbManager);
            dbManager.startTransaction();
            ServiceUserVo serviceUser = securityDao.getServiceUser(serviceId);
            serviceUser.setNewPassword(newPassword);
            securityDao.updatePassword(serviceUser);
            dbManager.commit();
            out.print(newPassword);
        } catch (Exception ex) {
            dbManager.rollback();
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

    public ActionLocation confirmPassword(ModuleMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        if (!authenticate(request, response)) {
            out.print("Error: " + ServiceAction.AUTHEN_ERROR);
            return null;
        }
        String serviceId = StringUtil.decoderURL(request.getParameter("serviceId"));
        String newPassword = StringUtil.decoderURL(request.getParameter("newPassword"));
        if (newPassword == null || newPassword.equals("")) {
            out.print("Error: The new password cannot be null");
            return null;
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SecurityServiceDAO securityDao = new SecurityServiceDAO(dbManager);
            dbManager.startTransaction();
            ServiceUserVo serviceUser = securityDao.getServiceUser(serviceId);
            if (newPassword.equals(serviceUser.getNewPassword())) {
                serviceUser.setPassword(serviceUser.getNewPassword());
                serviceUser.setNewPassword(null);
                securityDao.updatePassword(serviceUser);
                dbManager.commit();
                out.print("OK");
            } else {
                dbManager.rollback();
                out.print("Error: The new password is wrong");
            }
        } catch (Exception ex) {
            dbManager.rollback();
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

    /**
     * For test only
     */
    public ActionLocation getPassword(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        String serviceId = StringUtil.decoderURL(request.getParameter("serviceId"));
        if (serviceId == null || !serviceId.equalsIgnoreCase("Test")) {
            out.print("Error: Invalid service id");
            return null;
        }
        serviceId = serviceId.toLowerCase();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SecurityServiceDAO securityDao = new SecurityServiceDAO(dbManager);
            ServiceUserVo serviceUser = securityDao.getServiceUser(serviceId);
            if (serviceUser != null) {
                out.print(serviceUser.getPassword());
            } else {
                out.print("");
            }
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
