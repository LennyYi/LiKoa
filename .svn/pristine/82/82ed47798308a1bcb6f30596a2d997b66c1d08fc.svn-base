package com.aiait.eflow.wkf.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.housekeeping.dao.OTRecordDAO;
import com.aiait.eflow.housekeeping.dao.StaffHolidayDAO;
import com.aiait.eflow.housekeeping.dao.SynchronizeEContractDAO;
import com.aiait.eflow.housekeeping.vo.*;
import com.aiait.eflow.wkf.util.WKAfterProccessTread;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class WKAfterProcessAction extends DispatchAction {

    public ActionLocation otherProcess(ModuleMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String returnLabel = "tempsuccess";
        String requestNo = (String) request.getParameter("requestNo");
        String formSystemId = (String) request.getParameter("formSystemId");
        String saveType = (String) request.getParameter("saveType");
        String requestStaffCode = (String) request.getParameter(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
        StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
        String ccToStaffs = request.getParameter("ccStaffCode");
        String functionLabel = (String) request.getAttribute("functionLabel");
        if (functionLabel != null) {
            functionLabel = functionLabel.trim().toLowerCase();
        }

        
    	WKAfterProccessTread workTread = new WKAfterProccessTread(requestNo,currentStaff, returnLabel, formSystemId, functionLabel, requestStaffCode, ccToStaffs);
    	Thread t1 = new Thread(workTread);
    	t1.start();
        
        // 设定处理完毕后需要显示的页面
        return mapping.findActionLocation(returnLabel);
    }
}
