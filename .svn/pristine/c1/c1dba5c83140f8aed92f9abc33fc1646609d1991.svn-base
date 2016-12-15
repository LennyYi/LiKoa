package com.aiait.eflow.housekeeping.action;

import java.util.Collection;

import javax.servlet.http.*;

import com.aiait.eflow.housekeeping.dao.CurrencyExDAO;
import com.aiait.eflow.housekeeping.vo.CurrencyExVO;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.*;
import com.aiait.framework.util.CommonUtil;

public class CurrencyExAction extends DispatchAction {

    public ActionLocation listRate(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "listRate";
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CurrencyExDAO dao = new CurrencyExDAO(dbManager);
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
        String code = request.getParameter("code");
        int year = 0;
        int month = 0;
        
        if (editType != null && "new".equals(editType)) {
            return mapping.findActionLocation(returnLabel);
        } else {
            year = Integer.parseInt(request.getParameter("year"));
            month = Integer.parseInt(request.getParameter("month"));
        	
        }

        code = CommonUtil.decoderURL(code);
        
        CurrencyExVO vo = new CurrencyExVO();
        vo.setCurrencyCode(code);
        vo.setYear(year);
        vo.setMonth(month);
        
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CurrencyExDAO dao = new CurrencyExDAO(dbManager);
            vo = dao.getRate(vo);
            request.setAttribute("vo", vo);
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
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String rate = request.getParameter("rate");
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        CurrencyExVO vo = new CurrencyExVO();
        vo.setCurrencyCode(code.trim());
        vo.setCurrencyName(name);
        vo.setExchangeRate(Double.parseDouble(rate));
        vo.setYear(Integer.parseInt(year));
        vo.setMonth(Integer.parseInt(month));

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CurrencyExDAO dao = new CurrencyExDAO(dbManager);
            dao.saveRate(vo);
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
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            CurrencyExDAO dao = new CurrencyExDAO(dbManager);
            CurrencyExVO vo = new CurrencyExVO();
            for (int i = 0; i < request.getParameterValues("code").length; i++) {
            	String[] id = request.getParameterValues("code")[i].split("_");
                vo.setCurrencyCode(id[0]);
                vo.setYear(Integer.parseInt(id[1]));
                vo.setMonth(Integer.parseInt(id[2]));
                dao.deleteRate(vo);
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
