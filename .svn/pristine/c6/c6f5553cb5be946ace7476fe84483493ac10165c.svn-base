package com.aiait.eflow.util;

/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT1002	Robin	04/16/2008	DS-002 ·¢ËÍemail*/
/*4293096   Mario   10/10/2009  Change email suffix to a template*/
/******************************************************************/
import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.ResourceHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.dao.FormManageDAO;
import com.aiait.eflow.formmanage.vo.FormManageVO;
import com.aiait.eflow.housekeeping.dao.EmailTemplateDAO;
import com.aiait.eflow.housekeeping.vo.EmailTemplateVO;
import com.aiait.eflow.wkf.dao.WorkFlowProcessDAO;
import com.aiait.eflow.wkf.vo.WorkFlowProcessVO;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class EFlowEmailUtil {

    public static void sendEmail(String emailSubject, String emailContent, String emailToAddress,
            WorkFlowProcessVO processVo) throws Exception {

        if (emailToAddress == null || emailToAddress.length() == 0) {
            System.out.println("---------2--------:Receive Email Staff is null------------");
            return;
        }

        emailContent = "<font face='Arial' size='2'>Dear All</font><br><br>" + emailContent;

        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue(CommonName.EMAIL_HOST_IP);
        String emailUserName = paramHelper.getParamValue(CommonName.EMAIL_USER_NAME);
        String emailAccount = getEmailFromAccount(processVo.getFormType());
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailToAddress);
        sendmail.setFrom(emailAccount);
        sendmail.setSubject(emailSubject);
        // No. 4293096
        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();

        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX, processVo
                    .getFormType());
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        sendmail.setContent(emailContent);
        sendmail.sendMail();
    }

    public static void sendEmail(String emailSubject, String emailContent, String[] receiveStaffCode) throws Exception {
        if (receiveStaffCode == null || receiveStaffCode.length < 1) {
            return;
        }
        String emailTo = "";
        String staffName = "";

        for (int i = 0; i < receiveStaffCode.length; i++) {
            if (StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]) == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]).getEmail() == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]).getEmail() != null
                    && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]).getEmail())) {
                emailTo = emailTo + StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]).getEmail().trim()
                        + ",";
                staffName = staffName
                        + StaffTeamHelper.getInstance().getStaffByCode(receiveStaffCode[i]).getStaffName() + ",";
            }
        }
        if (emailTo.length() > 0) {
            emailTo = emailTo.substring(0, emailTo.length() - 1);
        } else {
            System.out.println("---------2--------:Receive Email Staff is null------------");
            return;
        }

        emailContent = "Dear <font color='blue'> " + staffName + "</font> <br><br>" + emailContent;

        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue(CommonName.EMAIL_HOST_IP);
        String emailUserName = paramHelper.getParamValue(CommonName.EMAIL_USER_NAME);
        String emailAccount = paramHelper.getParamValue(CommonName.EMAIL_FROM_ACCOUNT);
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailTo);
        sendmail.setFrom(emailAccount);
        sendmail.setSubject(emailSubject);
        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX);
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        sendmail.setContent(emailContent);

        sendmail.sendMail();

    }

    public static void sendEmail(EmailTemplateVO template, String[] toEmailStaffs, String[] paramNames,
            String[] paramValues) throws Exception {
        String emailTo = "";
        String staffName = "";
        if (toEmailStaffs == null) {
            System.out.println("--------1---------:Receive Email Staff is null------------");
            return;
        }
        for (int i = 0; i < toEmailStaffs.length; i++) {
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                    && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                emailTo = emailTo + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail().trim()
                        + ",";
            }
        }
        if (emailTo.length() > 0) {
            emailTo = emailTo.substring(0, emailTo.length() - 1);
        } else {
            System.out.println("---------2--------:Receive Email Staff is null------------");
            return;
        }

        for (int i = 0; i < toEmailStaffs.length; i++) {
            // if staff is null or staff's email is null, then ignore him
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                    && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                staffName = staffName + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getStaffName()
                        + ",";
            }
        }
        if (staffName.length() > 0) {
            staffName = staffName.substring(0, staffName.length() - 1);
        }

        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue(CommonName.EMAIL_HOST_IP);
        String emailUserName = paramHelper.getParamValue(CommonName.EMAIL_USER_NAME);
        String emailAccount = paramHelper.getParamValue(CommonName.EMAIL_FROM_ACCOUNT);
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailTo);
        sendmail.setFrom(emailAccount);
        sendmail.setSubject(template.getEmailSubject());
        String emailContent = template.getEmailContent();
        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();
        IDBManager dbManager = null;
        try {
            dbManager = DBManagerFactory.getDBManager();
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX);
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }
        for (int i = 0; i < paramNames.length; i++) {
            emailContent = // emailContent.replaceAll(paramList[i],paramValue[i]);
            StringUtil.replace(emailContent, paramNames[i], paramValues[i]);
        }
        sendmail.setContent(emailContent);

        sendmail.sendMail();
    }

    public static String getEmailFromAccount(String form_type) {
        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailFromAccount = null;
        if (form_type != null) {
            emailFromAccount = paramHelper.getParamValue(CommonName.EMAIL_FROM_ACCOUNT + "_" + form_type);
        }
        if (emailFromAccount == null || "".equals(emailFromAccount.trim())) {
            emailFromAccount = paramHelper.getParamValue(CommonName.EMAIL_FROM_ACCOUNT);
        }
        return emailFromAccount;
    }
    
    public static void sendEmail(EmailTemplateVO template, String formRequesterStaffCode, String requestNo,
            String handleStaffCode, String handleComments, String[] toEmailStaffs, String formSystemId,
            String[] ccStaffs, WorkFlowProcessVO processVo) throws Exception {

        String emailTo = "";
        String staffName = "";
        String ccTo = "";
        boolean isUrgent = false;
        for (int i = 0; i < toEmailStaffs.length; i++) {
            // System.out.println(toEmailStaffs[i]);
            // if staff is null or staff's email is null, then ignore him
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                    && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                emailTo = emailTo + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail().trim()
                        + ",";
            }
        }
        if (emailTo.length() > 0) {
            emailTo = emailTo.substring(0, emailTo.length() - 1);
        } else {
            System.out.println("-----------------:Receive Email Staff is null------------");
            return;
        }
        if (ccStaffs != null) {
            for (int i = 0; i < ccStaffs.length; i++) {
                if (StaffTeamHelper.getInstance().getStaffByCode(ccStaffs[i]) == null) {
                    continue;
                }
                if (StaffTeamHelper.getInstance().getStaffByCode(ccStaffs[i]).getEmail() == null) {
                    continue;
                }
                if (StaffTeamHelper.getInstance().getStaffByCode(ccStaffs[i]).getEmail() != null
                        && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(ccStaffs[i]).getEmail())) {
                    ccTo = ccTo + StaffTeamHelper.getInstance().getStaffByCode(ccStaffs[i]).getEmail().trim() + ",";
                }
            }
            if (ccTo.length() > 0) {
                ccTo = ccTo.substring(0, ccTo.length() - 1);
            }
        }
        for (int i = 0; i < toEmailStaffs.length; i++) {
            // if staff is null or staff's email is null, then ignore him
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]) == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() == null) {
                continue;
            }
            if (StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail() != null
                    && !"".equals(StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getEmail())) {
                staffName = staffName + StaffTeamHelper.getInstance().getStaffByCode(toEmailStaffs[i]).getStaffName()
                        + ",";
            }
        }
        if (staffName.length() > 0) {
            staffName = staffName.substring(0, staffName.length() - 1);
        }

        ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
        String emailHost = paramHelper.getParamValue("email_host_ip");
        String emailUserName = paramHelper.getParamValue("email_user_name");
        String emailAccount = EFlowEmailUtil.getEmailFromAccount(processVo.getFormType());
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost(emailHost);
        sendmail.setUserName(emailUserName);

        sendmail.setTo(emailTo);
        if (!"".equals(ccTo)) {
            sendmail.setCopyTo(ccTo);
        }

        // sendmail.setCopyTo(teamLeaderEmails);
        sendmail.setFrom(emailAccount);
        sendmail.setSubject(template.getEmailSubject());
        String emailContent = template.getEmailContent();

        // String emailSuffix = ResourceHelper.getInstance().getEmailSuffix();
        IDBManager dbManager = null;
        dbManager = DBManagerFactory.getDBManager();
        FormManageDAO formDao = new FormManageDAO(dbManager);
        FormManageVO form = formDao.getFormBySystemId(Integer.parseInt(formSystemId));
        String formName = form.getFormName();
        try {
            EmailTemplateDAO emailTemplateDao = new EmailTemplateDAO(dbManager);
            EmailTemplateVO templateSuf = emailTemplateDao.getEmailTemplateByAction(CommonName.HANDLE_SUFFIX,
                    processVo.getFormType());
            // System.out.println("Form Type: " + processVo.getFormType());
            String emailSuffix = templateSuf.getEmailContent();
            if (emailSuffix != null) {
                emailContent = emailContent + emailSuffix;
            }
            /** IT1177 begin */
            WorkFlowProcessDAO workFlowProcessDao = new WorkFlowProcessDAO(dbManager);
            if (workFlowProcessDao.isUrgent(processVo.getFormSystemId(), processVo.getRequestNo())) {
                isUrgent = true;
                sendmail.setSubject(template.getEmailSubject() + "  (Urgent)");
            }
            /** IT1177 end */
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (dbManager != null) {
                dbManager.freeConnection();
            }
        }

        // Process for Deputy
        String deputyFor = "";
        String deputyMarkStart = " <!-- ";
        String deputyMarkEnd = " --> ";
        if (processVo.isDealByDeputy()) {
            deputyFor = StaffTeamHelper.getInstance().getStaffNameByCode(processVo.getOriginProcessor()).trim();
            deputyMarkStart = " ";
            deputyMarkEnd = " ";
        }
        // IT1177
        String duetime = "";
        String duetimeStart = " <!-- ";
        String duetimeEnd = " --> ";

        // IT1197 begin
        String urgentStart = " <!-- ";
        String urgentEnd = " --> ";
        if (isUrgent) {
            urgentStart = "";
            urgentEnd = "";
        } else if (processVo != null && processVo.getLimitedHours() != 0.0) {
            // IT1197 end
            duetime = Double.toString(processVo.getLimitedHours());
            duetimeStart = "";
            duetimeEnd = "";
        }

        String[] paramList = {CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO,
        		CommonName.EMAIL_TEMEPLATE_PARAM_FORM_NAME, CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY,
                CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF, CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE,
                CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS, CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID,
                CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY, CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_START,
                CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_FOR, CommonName.EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_END,
                CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_VAL, CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_START,
                CommonName.EMAIL_TEMPLATE_PARAM_DUETIME_END, CommonName.EMAIL_TEMPLATE_PARAM_URGENT_MARK_START,
                CommonName.EMAIL_TEMPLATE_PARAM_URGENT_MARK_END};

        String[] paramValue = {requestNo,formName, StaffTeamHelper.getInstance().getStaffNameByCode(handleStaffCode), staffName,
                StringUtil.getCurrentDateStr("MM/dd/yyyy HH:mm:ss"), handleComments, formSystemId,
                StaffTeamHelper.getInstance().getStaffNameByCode(formRequesterStaffCode), deputyMarkStart, deputyFor,
                deputyMarkEnd, duetime, duetimeStart, duetimeEnd, urgentStart, urgentEnd};

        for (int i = 0; i < paramList.length; i++) {
            emailContent = // emailContent.replaceAll(paramList[i],paramValue[i]);
            StringUtil.replace(emailContent, paramList[i], paramValue[i]);
        }
        sendmail.setContent(emailContent);

        // sendmail.sendMail(); 2013-11-19
        sendmail.sendMailAsync();
    }

}
