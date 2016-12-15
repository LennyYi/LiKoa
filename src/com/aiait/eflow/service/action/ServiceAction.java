package com.aiait.eflow.service.action;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.service.dao.SecurityServiceDAO;
import com.aiait.eflow.service.vo.ServiceUserVo;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.*;
import com.aiait.framework.mvc.action.DispatchAction;

/**
 * Service Action
 * 
 * @version 2011-01-11
 */
public abstract class ServiceAction extends DispatchAction {

    public static final String AUTHEN_ERROR = "Invalid service id or password";

    protected SimpleDateFormat dateFormat = new SimpleDateFormat(CommonName.FORMAT_DATE, Locale.US);
    protected SimpleDateFormat datetimeFormat = new SimpleDateFormat(CommonName.FORMAT_DATETIME, Locale.US);

    public static boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String serviceId = StringUtil.decoderURL(request.getParameter("serviceId"));
        String password = StringUtil.decoderURL(request.getParameter("password"));
        if (serviceId == null || serviceId.equals("") || password == null || password.equals("")) {
            return false;
        }

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            SecurityServiceDAO securityDao = new SecurityServiceDAO(dbManager);
            ServiceUserVo serviceUser = securityDao.getServiceUser(serviceId);
            if (serviceUser == null) {
                return false;
            }
            return password.equals(serviceUser.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
    }

}
