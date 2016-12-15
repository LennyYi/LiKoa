package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.LeaveAIASSDAO;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class LeaveAIASSAction extends DispatchAction {

    public ActionLocation monthlyList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "monthlyList";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            LeaveAIASSDAO leaveAIASSDAO = new LeaveAIASSDAO(dbManager);
            Collection monthlyList = leaveAIASSDAO.getMonthlyList();
            request.setAttribute("monthlyList", monthlyList);
        } catch (Exception ex) {
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation monthlyRecords(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "monthlyRecords";
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            LeaveAIASSDAO leaveAIASSDAO = new LeaveAIASSDAO(dbManager);
            Collection monthlyRecords = leaveAIASSDAO.getMonthlyRecords(year, month);
            request.setAttribute("leaveRecords", monthlyRecords);
        } catch (Exception ex) {
            request.setAttribute(CommonName.COMMON_ERROR_INFOR, ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

}
