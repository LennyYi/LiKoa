package com.aiait.eflow.report.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.report.dao.OverdueSummaryDAO;
import com.aiait.eflow.report.vo.OverdueSummaryVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.dao.WorkFlowDefineDAO;
import com.aiait.eflow.wkf.vo.WorkFlowItemVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class OverdueSummaryAction extends DispatchAction{
	
	public ActionLocation enterOverdueSummary(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String returnLabel = "overdueSummary";
		IDBManager dbManager = null;
		try{
		  dbManager =  DBManagerFactory.getDBManager();
		  FormManageDAO formDao = new FormManageDAO(dbManager);
		  Collection formList = formDao.getFormList("0");
		  request.setAttribute("formList",formList);
		}catch(DAOException e){
			e.printStackTrace();
			returnLabel = "fail";
		}finally{
			dbManager.freeConnection();
		}
		request.setAttribute("resultList",new ArrayList());
		return mapping.findActionLocation(returnLabel);
	}
	
	public ActionLocation overdueSummary(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "overdueSummary";
        OverdueSummaryVO vo = new OverdueSummaryVO();
        String formType = request.getParameter("formType");
        vo.setFormSystemId((String) request.getParameter("formSystemId"));
        vo.setNodeId((String) request.getParameter("nodeId"));
        String beginDate = request.getParameter("beginDateStr");
        String endDate = request.getParameter("endDateStr");
        if (beginDate != null && !"".equals(beginDate)) {
            vo.setBeginDate(beginDate);
        }
        if (endDate != null && !"".equals(endDate)) {
            vo.setEndDate(endDate);
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            FormManageDAO formDao = new FormManageDAO(dbManager);
            // get all form list
            Collection formList = formDao.getFormList("0", formType);
            request.setAttribute("formList", formList);
            OverdueSummaryDAO overdueDao = new OverdueSummaryDAO(dbManager);

            int opt1 = Integer.parseInt((String) request.getParameter("optList1"));
            int opt2 = Integer.parseInt(request.getParameter("optList2"));

            Collection list = overdueDao.overdueSummary(vo, opt1, opt2);
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
	
	public ActionLocation getFormList(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String formType = (String) request.getParameter("formType");
        IDBManager dbManager = null;
        // response.setContentType("text/xml");
        response.setContentType("text/xml;charset=GB2312");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
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
                    responseXML.append("<option id='" + form.getFormSystemId() + "'>").append(
                            StringUtil.formatXML(form.getFormName()) + "(").append(
                            StringUtil.formatXML(form.getFormId())).append(")").append("</option>");
                }
            }
            responseXML.append("</options>");
            // System.out.println(responseXML.toString());
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
	
	public ActionLocation getNodeList(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String formSystemId = (String)request.getParameter("formSystemId");
		IDBManager dbManager = null;
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		try{
		   StringBuffer responseXML = new StringBuffer("<options>");
		   dbManager =  DBManagerFactory.getDBManager();
		   WorkFlowDefineDAO flowDao = new WorkFlowDefineDAO(dbManager);
		   //get all node list for the form
		   Collection nodeList = flowDao.getNodeListByForm(Integer.parseInt(formSystemId));
		   if(nodeList!=null && nodeList.size()>0){
			   Iterator it = nodeList.iterator();
			   while(it.hasNext()){
				   WorkFlowItemVO node = (WorkFlowItemVO)it.next();
				  responseXML.append("<option id='" + node.getItemId() + "'>")
				             .append(node.getName())
				             .append("</option>");
			   }
		   }
		   responseXML.append("</options>");
		   out.write(responseXML.toString());
		   
		}catch(DAOException e){
			 e.printStackTrace();
			 out.print("fail");
		}finally{
			if(dbManager!=null) dbManager.freeConnection();
			if(out!=null) out.close();
		}
		return null;
	}

}
