package com.aiait.eflow.common;

public interface CommonName {
    String LOCALE_LANGUAGE_EN = "en";
    String LOCALE_LANGUAGE_CHINESE = "zh_cn";

    String SYSTEM_SUB_TITLE = "system_sub_title";

    String EVERY_PAGE_RECORDS_NUM = "records_num";
    String DEFAULT_RECORDS_NUM = "10";

    String COMMON_ERROR_INFOR = "error";
    String COMMON_TIP_INFO = "tip";
    String COMMON_OK_INFOR = "success_message";
    String COMMON_STATUS = "status";
    String COMMON_MESSAGE = "message";

    String ERROR_PAGE = "fail";
    String SUCCESS_PAGE = "successTipPage";

    String RETURN_URL = "operate_return_url";

    String WINDOW_RETURN_TYPE = "window_return_type";
    String WINDOW_RETURN_TYPE_SELF = "self_window";
    String WINDOW_RETURN_TYPE_PARENT = "parent_window";

    String PAGE_ENCODING = "GBK";

    String CURRENT_STAFF_INFOR = "currentStaffInfor";
    String STRING_JOIN_FLAG = "&";
    String FORM_TYPE_TS = "01";
    String FORM_TYPE_ADMIN = "02";
    String FORM_TYPE_ACCOUNT = "03";
    String FORM_TYPE_HR = "04";

    String FORM_SECTION_TYPE_TABLE = "01";
    String FORM_SECTION_TYPE_JQGRID_TABLE = "04";
    String FORM_SECTION_TYPE_BASIC = "03";
    String FORM_SECTION_TYPE_COMMON = "02";
    String FORM_SECTION_TYPE_ATTACHMENT = "00";

    String SYSTEM_ID_STAFF_CODE = "staff_code";
    String SYSTEM_ID_PROJ_LD_ID = "prj_ld_id";
    String SYSTEM_ID_APPLICATION_ID = "system_id";
    String SYSTEM_ID_DB_OWNER = "data_owner";
    String SYSTEM_ID_RESOURCE_OWNER = "resource_owner_code";
    String SYSTEM_ID_REQUEST_NO = "request_no";
    String SYSTEM_ID_REQUEST_DATE = "request_date";
    String SYSTEM_ID_REQUEST_STAFF_CODE = "request_staff_code";
    String SYSTEM_ID_REQUESTOR_TEAM_CODE = "team_code";
    String SYSTEM_ID_IS_EXCEPTIONAL_CASE = "is_exceptional_case";
    String SYSTEM_ID_SUBMIT_STAFF_CODE = "submit_staff_code";
    String SYSTEM_ID_FILE_NAME = "file_name";
    String SYSTEM_ID_FILE_DESCRIPTION = "file_description";
    String SYSTEM_ID_REFERENCE_FORM = "reference_form";
    String SYSTEM_ID_COMPANY_ID = "company_id";

    String EMAIL_TEMPLATE_PARAM_REQUEST_NO = "@request_no";
    String EMAIL_TEMEPLATE_PARAM_FORM_NAME = "@form_name";
    String EMAIL_TEMPLATE_PARAM_HANDLE_BY = "@handle_by";
    String EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF = "@receive_staff";
    String EMAIL_TEMPLATE_PARAM_COMMENTS = "@comments";
    String EMAIL_TEMPLATE_PARAM_CURRENT_DATE = "@current_date";
    String EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID = "@form_system_id";
    String EMAIL_TEMPLATE_PARAM_REQUESTED_BY = "@requested_by";
    String EMAIL_TEMPLATE_PARAM_DEPUTY_FOR = "@deputy_for";
    String EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_START = "@deputy_mark_start";
    String EMAIL_TEMPLATE_PARAM_DEPUTY_MARK_END = "@deputy_mark_end";
    String EMAIL_TEMPLATE_PARAM_DUETIME_START = "@duetime_start";
    String EMAIL_TEMPLATE_PARAM_DUETIME_END = "@duetime_end";
    String EMAIL_TEMPLATE_PARAM_DUETIME_VAL = "@duetime_val";
    String EMAIL_TEMPLATE_PARAM_URGENT_MARK_START = "@urgent_mark_start";
    String EMAIL_TEMPLATE_PARAM_URGENT_MARK_END = "@urgent_mark_end";

    String PARAM_CONFIG_SHOW_CC_LIST_SUBMIT = "submit_show_cc_list";
    String PARAM_CONFIG_SELECT_APPROVER_TYPE = "select_approver_type";

    // String IS_EXCEPTIONAL_CASE_YES = "01";
    // String IS_EXCEPTIONAL_CASE_NO = "02";
    String NOT_EXCEPTIONAL_CASE = "02";
    String NORMAL_EXCEPTIONAL_CASE = "01";
    String MIDNIGHT_EXCEPTIONAL_CASE = "03";

    int FIELD_TYPE_TEXT = 1;
    int FIELD_TYPE_TEXTAREA = 2;
    int FIELD_TYPE_DATE = 3;
    int FIELD_TYPE_SELECT = 4;
    int FIELD_TYPE_NUMBER = 5;
    int FIELD_TYPE_CHECKBOX = 6;
    int FIELD_TYPE_SYSTEM = 7;
    int FIELD_TYPE_IDENTITY = 8;
    int FIELD_TYPE_COMMENTS = 9;
    int FIELD_TYPE_BASIC = 10;
    int FIELD_TYPE_ATTACH = 11;
    int FIELD_TYPE_REFFORM = 12;
    int FIELD_TYPE_REFCONTRACT = 13;
    int FIELD_TYPE_MULTISTAFF = 14;
    int FIELD_TYPE_REPORT = 15;

    String SYSTEMFIELD_TYPE_COMMON = "01";
    String SYSTEMFIELD_TYPE_SELECT = "02";
    String SYSTEMFIELD_TYPE_REFERENCE = "03";
    String SYSTEMFIELD_TYPE_LABEL = "04";
    String SYSTEMFIELD_TYPE_SELECTBYPARAM = "05";
    String SYSTEMFIELD_TYPE_PROCESSOR = "06";

    int DATA_TYPE_TEXT = 1;
    int DATA_TYPE_DATE = 2;
    int DATA_TYPE_NUMBER = 3;

    String APPROVER_GROUP_TYPE_TL = "01";
    String APPROVER_GROUP_TYPE_STAFF = "02";

    String COMPANY_TYPE_SUB_BRANCH = "03"; // 分支公司
    String COMPANY_TYPE_BRANCH = "02"; // 分公司
    String COMPANY_TYPE_GROUP = "01"; // 总公司

    String PAGE_VO = "pageVo";
    String PAGE_ROW="form_rows";	//the rows show in the exploer  20150318 Justin Bin added

    String MODULE_ROLE_SPLIT_SIGN = "_";

    String SECTION_FIELD_LINK_SYMBOL = "_";

    String PLATFORM_NAME = "platform_name";

    String AIAIT_COMPANY_TITLE = "友邦资讯科技（广州）有限公司 / AIA Information Technology (Guangzhou) Co., Ltd.";
    String COMPANY_LOGO_FILE = "company_logo_file";
    String COMPANY_NAME = "company_name";

    String MergedTableId = "fieldform";

    String STATUS_DRAFT = "00";
    String STATUS_SUBMITTED = "01";
    String STATUS_INPROGRESS = "02";
    String STATUS_REJECTED = "03";
    String STATUS_COMPLETED = "04";

    String HANDLE_TYPE_DRAFT = "00";
    String HANDLE_TYPE_SUBMITT = "01";
    String HANDLE_TYPE_WITHDRAW = "02";
    String HANDLE_TYPE_APPROVE = "03";
    String HANDLE_TYPE_REJECT = "04";
    String HANDLE_TYPE_COMPLETE = "05";
    String HANDLE_TYPE_INVITED_EXPERT = "06";
    String HANDLE_TYPE_EXPERT_ADVISE = "07";
    String HANDLE_TYPE_PAYMENT = "08";
    String HANDLE_INVOICE_REMINDER = "09";
    String HANDLE_CA_REMINDER = "10";
    String HANDLE_TYPE_WAITING_REMINDER = "a1";
    String HANDLE_TYPE_WAITING_REJECT = "a2";
    String HANDLE_TYPE_DH_REJECT = "a3";
    String HANDLE_TYPE_SKIPPED = "SK";
    String HANDLE_SUFFIX = "99";
    String HANDLE_TYPE_COMPLETE_CC = "CC";

    String ACTION_OVERDUE_NOTIFICATION = "overdue_notification";

    String OPERATE_LOG_TYPE_UNLOCK = "01";
    String OPERATE_LOG_TYPE_LOCK = "02";
    String OPERATE_LOG_TYPE_REASSIGN = "03";

    String NODE_FIELD_CHANGE_TYPE_UPDATE = "update";
    String NODE_FIELD_CHANGE_TYPE_NEW = "new";

    String NODE_TYPE_APPROVAL = "2";
    String NODE_TYPE_PROCESS = "3";
    String NODE_TYPE_AUTOFLOW = "4";
    String NODE_TYPE_PAYMENT = "8";
    String NODE_TYPE_WAITING = "a";
    String NODE_TYPE_OPTIONAL = "o";
    String NODE_TYPE_SELECTAPPROVER = "s";
    String NODE_TYPE_MULTIAPPROVER = "m";
    String NODE_TYPE_DELAYED = "d";

    int NODE_SPECIAL_FIELD_TYPE_PROCESSOR = 1;
    int NODE_SPECIAL_FIELD_TYPE_COMPANY = 2;
    int NODE_SPECIAL_FIELD_TYPE_DELAYED = 3;

    String PARAM_UPLOAD_DIR = "upload_file_dir";
    String PARAM_UPLOAD_DIR_BAK = "upload_file_dir_bak";
    String PARAM_OT_RECORD_TABLE_CODE = "ot_record_table_name";
    String PARAM_OT_REQUESTER_TABLE_CODE = "ot_requester_table_name";
    String PARAM_OT_FORM_SYSTEM_ID_CODE = "ot_form_system_id";
    String PARAM_OT_REJECT_TO_NODE = "ot_reject_to_node";
    String PARAM_OT_FEE_STANDARD_BY_TIMES = "ot_fee_by_num";
    String PARAM_LEAVE_RECORD_TABLE_CODE = "leave_record_table_name";
    String PARAM_BATCH_LEAVE_RECORD_TABLE_CODE = "batch_leave_record_table";
    String PARAM_ADMIN_EMAIL = "admin_email";
    String PARAM_CONTACT_EMAIL = "contact_email";
    String PARAM_PERSONAL_OT_SUMMARY_PERIOD = "ot_summary_period";
    String PARAM_RELEASE_VERSION = "release_version";
    String PARAM_RELEASE_DATE = "release_date";
    String PARAM_DELAY_NUM = "delay_apply_num";
    String PARAM_FORM_RETURN = "form_return";
    String PARAM_REF_FORM_SELECT_LIKE = "ref_form_select_like";
    String PARAM_CONTRACT_SELECT_LIKE = "contract_select_like";
    String PARAM_ACE_JOB_TIME = "ace_job_time";
    String PARAM_LEAVE_BALANCE = "leave_balance";
    String PARAM_LEAVE_JOB_TIME = "leave_job_time";
    String PARAM_LEAVE_EXPORT_DIR = "leave_export_dir";
    String PARAM_LEAVE_DAILY_JOB_TIME = "leave_daily_job_time";
    String PARAM_LEAVE_YEARLY_JOB_DAY = "leave_yearly_job_day";
    String PARAM_LEAVE_HR_EMAIL = "leave_hr_email";
    String PARAM_LEAVE_FORM_SYSTEM_ID = "leave_form_system_id";
    String PARAM_MEDICAL_CLAIM = "medical_claim";
    String PARAM_MEDICAL_DAILY_JOB_TIME = "medical_daily_job_time";
    String PARAM_MEDICAL_YEARLY_JOB_DAY = "medical_yearly_job_day";
    String PARAM_MEDICAL_FORM_SYSTEM_ID = "medical_form_system_id";
    String PARAM_EXPERT_MULTI_ADVISE = "expert_multi_advise";
    String PARAM_SMART_DEPUTY = "smart_deputy";
    String PARAM_HIGHLIGHT_FIELDLABEL = "highlight_fieldlabel";
    String PARAM_LOG_TIME = "log_time";
    String PARAM_WAITING_NODE_JOB_TIME = "waiting_node_job_time";
    String PARAM_WAITING_NODE_LIMITED_HOURS = "waiting_node_limited_hours";
    String PARAM_WAITING_NODE_REMINDER_HOURS = "waiting_node_reminder_hours";
    String PARAM_DELAYED_NODE_JOB_TIME = "delayed_node_job_time";
    String PARAM_LIST_CONTENT_FIELDS = "list_content_fields";
    String PARAM_USER_REGISTER = "user_register";
    String PARAM_SERVICE_CALLBACK = "service_callback";
    String PARAM_TEAMSCOPE_QUERY_FORMS = "teamscope_query_forms";
    String PARAM_GRADE_LIST = "grade_list";
    String PARAM_SSO_TYPE = "sso_type";
    String PARAM_HOMEPAGE = "homepage";

    double DEFAULT_WAITING_NODE_LIMITED_HOURS = 37.5;

    String OT_FORM_STATUS_CONFIRM = "01";
    String OT_FORM_STATUS_COMPLETE = "02";

    String EMAIL_HOST_IP = "email_host_ip";
    String EMAIL_USER_NAME = "email_user_name";
    String EMAIL_FROM_ACCOUNT = "email_from_account";

    String STAFF_ADDITIONAL_MAP = "staffAdditionalMap";

    String LEAVE_TYPE_ANNUAL_DAY = "01";
    String LEAVE_TYPE_SICK_LEAVE = "02";

    String AUTOMOBILE_LEASING_FEE = "automobile_leasing_fee";// Automobile leasing fee
    String AUTOMOBILE_EXPENSES_RENTAL = "automobile_expenses_rental"; // Automobile expenses for rental
    String AUTOMOBILE_SALARY_RENTAL = "salary_for_driver_rental";// Salary for driver
    String AUTOMOBILE_COST = "cost_of_automobile"; // Cost of automobile
    String AUTOMOBILE_EXPENSES_PURCHASE = "automobile_expenses_purchase";// Automobile expenses for purchase
    String AUTOMOBILE_SALARY_PURCHASE = "salary_for_driver_purchase";// 'Salary for driver for purchase
    String AUTOMOBILE_INSURANCE = "automobile_insurance"; // Insurance
    String AUTOMOBILE_C20 = "automobile_c20"; // PV @
    String AUTOMOBILE_C21 = "automobile_c21"; // Salary Increase(%)
    String AUTOMOBILE_C22 = "automobile_c22"; // Maintenance Increase(%)

    String FORMAT_DATE = "MM/dd/yyyy";
    String FORMAT_DATETIME = "MM/dd/yyyy HH:mm:ss";
    String FORMAT_DATETIME_DB = "yyyy-MM-dd HH:mm:ss";

    String NEXT_APPROVER_STAFF_CODE = "nextApproverStaffCode";

    String LEAVE_BALANCE_STAFF_PERMANENT = "1";
    String LEAVE_BALANCE_STAFF_INTERN = "2";
    String LEAVE_BALANCE_STAFF_EC = "3";
}
