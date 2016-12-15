package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.*;

import com.aiait.eflow.housekeeping.dao.PerdiemRateDAO;
import com.aiait.eflow.housekeeping.vo.PerdiemRateVO;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class PerdiemRateAction extends DispatchAction {

    public ActionLocation listRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerdiemRateDAO dao = new PerdiemRateDAO(dbManager);
            Collection list = dao.getRateList();
            request.setAttribute("rateList", list);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation editRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editRate";
        String editType = request.getParameter("editType");
        String rateId = request.getParameter("rateId");
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        }

        rateId = CommonUtil.decoderURL(rateId);
        int split = rateId.lastIndexOf('_');
        String location = rateId.substring(0, split);
        String titleGroup = rateId.substring(split + 1);
        PerdiemRateVO perdiemRate = new PerdiemRateVO();
        perdiemRate.setRegion(location);
        perdiemRate.setTitleGroup(titleGroup);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerdiemRateDAO dao = new PerdiemRateDAO(dbManager);
            perdiemRate = dao.getRate(perdiemRate);
            request.setAttribute("perdiemRate", perdiemRate);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation saveRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        String location = request.getParameter("location");
        String titleGroup = request.getParameter("titleGroup");
        String rate = request.getParameter("rate");

        PerdiemRateVO perdiemRate = new PerdiemRateVO();
        perdiemRate.setRegion(location.trim());
        perdiemRate.setTitleGroup(titleGroup);
        perdiemRate.setRate(Integer.parseInt(rate));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerdiemRateDAO dao = new PerdiemRateDAO(dbManager);
            dao.saveRate(perdiemRate);
            return this.listRate(mapping, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation deleteRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "";
        String[] rateIds = request.getParameterValues("rateId");

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerdiemRateDAO dao = new PerdiemRateDAO(dbManager);
            PerdiemRateVO perdiemRate = new PerdiemRateVO();
            for (int i = 0; i < rateIds.length; i++) {
                String rateId = rateIds[i];
                int split = rateId.lastIndexOf('_');
                String location = rateId.substring(0, split);
                String titleGroup = rateId.substring(split + 1);
                perdiemRate.setRegion(location);
                perdiemRate.setTitleGroup(titleGroup);
                dao.deleteRate(perdiemRate);
            }
            return this.listRate(mapping, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null)
                dbManager.freeConnection();
        }
        return mapping.findActionLocation(returnLabel);
    }

}
