package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.*;

import com.aiait.eflow.housekeeping.dao.HotelRateDAO;
import com.aiait.eflow.housekeeping.vo.HotelRateVO;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class HotelRateAction extends DispatchAction {

    public ActionLocation listRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            HotelRateDAO dao = new HotelRateDAO(dbManager);
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
        String city = rateId.substring(0, split);
        String titleGroup = rateId.substring(split + 1);
        // System.out.println("city_titleGroup: " + city + "_" + titleGroup);
        HotelRateVO hotelRate = new HotelRateVO();
        hotelRate.setCity(city);
        hotelRate.setTitleGroup(titleGroup);

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            HotelRateDAO dao = new HotelRateDAO(dbManager);
            hotelRate = dao.getRate(hotelRate);
            request.setAttribute("hotelRate", hotelRate);
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
        String city = request.getParameter("city");
        String titleGroup = request.getParameter("titleGroup");
        String rate = request.getParameter("rate");

        HotelRateVO hotelRate = new HotelRateVO();
        hotelRate.setCity(city.trim());
        hotelRate.setTitleGroup(titleGroup);
        hotelRate.setRate(Integer.parseInt(rate));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            HotelRateDAO dao = new HotelRateDAO(dbManager);
            dao.saveRate(hotelRate);
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
            HotelRateDAO dao = new HotelRateDAO(dbManager);
            HotelRateVO hotelRate = new HotelRateVO();
            for (int i = 0; i < rateIds.length; i++) {
                String rateId = rateIds[i];
                int split = rateId.lastIndexOf('_');
                String city = rateId.substring(0, split);
                String titleGroup = rateId.substring(split + 1);
                // System.out.println("city_titleGroup: " + city + "_" + titleGroup);
                hotelRate.setCity(city);
                hotelRate.setTitleGroup(titleGroup);
                dao.deleteRate(hotelRate);
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
