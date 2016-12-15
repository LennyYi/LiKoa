package com.aiait.eflow.housekeeping.action;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.*;

import com.aiait.eflow.housekeeping.dao.*;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;

public class ExchangeRateAction2 extends DispatchAction {

    public ActionLocation listRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            ExchangeRateDAO2 dao = new ExchangeRateDAO2(dbManager);
            List<ExchangeRateVO2> list = dao.getRateList();
            request.setAttribute("rateList", list);

            CurrencyDAO currencyDao = new CurrencyDAO(dbManager);
            List<CurrencyVO> currencyList = currencyDao.getCurrencyList2();
            request.setAttribute("currencyList", currencyList);
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
            List<ExchangeRateVO2> list = new ArrayList<ExchangeRateVO2>();
            if ("edit".equalsIgnoreCase(request.getParameter("mode"))) {
                String id = request.getParameter("id");
                ExchangeRateVO2 rate = new ExchangeRateVO2();
                rate.setId(id);
                ExchangeRateDAO2 dao = new ExchangeRateDAO2(dbManager);
                list = dao.getRatesByMonth(rate.getEffyear(), rate.getEffmonth());
            }
            request.setAttribute("rateList", list);
            CurrencyDAO currencyDao = new CurrencyDAO(dbManager);
            List<CurrencyVO> currencyList = currencyDao.getCurrencyList2();
            request.setAttribute("currencyList", currencyList);
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
            ExchangeRateDAO2 dao = new ExchangeRateDAO2(dbManager);
            ExchangeRateVO2 rate = new ExchangeRateVO2();
            String id = request.getParameter("id");
            if (id == null) {
                rate.setEffyear(Integer.parseInt(request.getParameter("year")));
                rate.setEffmonth(Integer.parseInt(request.getParameter("month")));
            } else {
                rate.setId(id);
            }
            CurrencyDAO currencyDao = new CurrencyDAO(dbManager);
            List<CurrencyVO> currencyList = currencyDao.getCurrencyList2();
            dbManager.startTransaction();
            for (CurrencyVO currency : currencyList) {
                rate.setCurrCode(currency.getCode());
                String _rate = request.getParameter(currency.getCode());
                if (_rate != null && !(_rate = _rate.trim()).equals("")) {
                    rate.setRate(new BigDecimal(_rate));
                    dao.saveRate(rate);
                } else {
                    dao.deleteRate(rate);
                }
            }
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
            ExchangeRateDAO2 dao = new ExchangeRateDAO2(dbManager);
            ExchangeRateVO2 rate = new ExchangeRateVO2();
            String[] ids = request.getParameterValues("id");
            if (ids != null) {
                dbManager.startTransaction();
                for (String id : ids) {
                    rate.setId(id);
                    dao.deleteRatesByMonth(rate.getEffyear(), rate.getEffmonth());
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
