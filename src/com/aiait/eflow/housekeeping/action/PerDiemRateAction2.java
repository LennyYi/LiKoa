package com.aiait.eflow.housekeeping.action;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.housekeeping.dao.*;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class PerDiemRateAction2 extends DispatchAction {

    public ActionLocation listRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerDiemRateDAO2 dao = new PerDiemRateDAO2(dbManager);
            List<PerDiemRateVO2> list = dao.getRateList();
            request.setAttribute("rateList", list);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation editRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "editRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            if ("edit".equalsIgnoreCase(request.getParameter("mode"))) {
                String id = request.getParameter("id");
                PerDiemRateVO2 rate = new PerDiemRateVO2();
                rate.setId(id);
                PerDiemRateDAO2 dao = new PerDiemRateDAO2(dbManager);
                rate = dao.getRate(rate);
                request.setAttribute("rate", rate);
            } else {
                CityDAO cityDao = new CityDAO(dbManager);
                List<CityVO> cityList = cityDao.getCityList();
                request.setAttribute("cityList", cityList);
                request.setAttribute("gradeList", TitleVO.getGradeList());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            returnLabel = "fail";
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return mapping.findActionLocation(returnLabel);
    }

    public ActionLocation saveRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerDiemRateDAO2 dao = new PerDiemRateDAO2(dbManager);
            PerDiemRateVO2 rate = new PerDiemRateVO2();
            String id = request.getParameter("id");
            if (id == null) {
                Date effDate = PerDiemRateVO2.dateFormat.parse(request.getParameter("effdate"));
                rate.setEffDate(new java.sql.Date(effDate.getTime()));
                rate.setCityCode(request.getParameter("city"));
                rate.setGrade(Integer.parseInt(request.getParameter("grade")));
                rate.setCurrCode(request.getParameter("currency"));
            } else {
                rate.setId(id);
                rate = dao.getRate(rate);
            }
            rate.setRate(new BigDecimal(request.getParameter("rate")));
            dbManager.startTransaction();
            dao.saveRate(rate);
            dbManager.commit();
        } catch (Exception ex) {
            dbManager.rollback();
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return this.listRate(mapping, request, response);
    }

    public ActionLocation deleteRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            PerDiemRateDAO2 dao = new PerDiemRateDAO2(dbManager);
            PerDiemRateVO2 rate = new PerDiemRateVO2();
            String[] ids = request.getParameterValues("id");
            if (ids != null) {
                dbManager.startTransaction();
                for (String id : ids) {
                    rate.setId(id);
                    dao.deleteRate(rate);
                }
                dbManager.commit();
            }
        } catch (Exception ex) {
            dbManager.rollback();
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            return mapping.findActionLocation("fail");
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        return this.listRate(mapping, request, response);
    }

}
