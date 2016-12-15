package com.aiait.eflow.formmanage.action;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT1002	Robin	04/12/2008	For Validate before saving form
 *IT1092	Queenie 10/19/2009	Update the OT validate function according to the new exception case
 * */

/******************************************************************/
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.housekeeping.dao.OTRecordDAO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.mvc.action.ActionLocation;
import com.aiait.framework.mvc.action.DispatchAction;
import com.aiait.framework.mvc.action.ModuleMapping;

public class FormValidateAction extends DispatchAction {
	
	
	/**
	 * �����������form���ڱ���֮ǰ��Ҫ���ø÷�������У��
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation validateLeave(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
	        throws Exception {
		response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		String[] leaveFromDate = (String[])request.getParameterValues("leave_from_date");  //��ٿ�ʼ����
		String[] leaveFromType = (String[])request.getParameterValues("leave_from_type");  //��ٿ�ʼ�����ͣ�01--���磻02---����
		String[] leaveToDate = (String[])request.getParameterValues("leave_to_date");      //��ٽ�������
		String[] leaveToType = (String[])request.getParameterValues("leave_to_type");      //��ٽ��������ͣ�01--���磻02--����
		String[] leaveType = (String[])request.getParameterValues("leave_type");            //������ͣ���٣���н���٣��¼�...
		String[] leaveDays = (String[])request.getParameterValues("leave_days");           //�������
		
		if(leaveFromDate==null || leaveFromDate.length<1){
			out.print("1û��������ټ�¼");
			return null;
		}
		
		Date beginDate,endDate;
		Date firstBeginDate,firstEndDate;
		//���������������ٲ���������Ҫ��鱾�������У��Ƿ�����ظ������ص���ټ�¼
		if(leaveFromDate.length>1){
			String[] staffCode = request.getParameterValues("staff_code");
			for(int i=0;i<leaveFromDate.length;i++){
				for(int j=i+1;j<leaveFromDate.length;j++){
					//�������ȫ�ظ�,ͬһ������ˣ�ͬһ�������ʱ��
					if(staffCode[i].equals(staffCode[j]) &&  leaveFromDate[i].equals(leaveFromDate[j]) && leaveFromType[i].equals(leaveFromType[j])
							&&  leaveToDate[i].equals(leaveToDate[j])){
						out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"����ټ�¼�����ظ�");
						return null;
					}
					//��������ʱ������ص�,ͬһ������ˣ�������¼�����ڴ����ص�
					if(staffCode[i].equals(staffCode[j])){
						 beginDate = StringUtil.stringToDate(leaveFromDate[j],"MM/dd/yyyy");
						 endDate = StringUtil.stringToDate(leaveToDate[j],"MM/dd/yyyy");
						 firstBeginDate = StringUtil.stringToDate(leaveFromDate[i],"MM/dd/yyyy");
						 firstEndDate = StringUtil.stringToDate(leaveToDate[i],"MM/dd/yyyy");
						 /**
						 if((firstBeginDate==beginDate || firstBeginDate.after(beginDate)) && endDate.after(beginDate) && (leaveFromType[i].equals(leaveFromType[j]) || 
								 ("02".equals(leaveFromType[i]) && "01".equals(leaveFromType[j])))){
							 out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"����ټ�¼��ʱ���ϴ����ص�");
							 return null;
						 }else if((firstEndDate==beginDate || firstEndDate.after(beginDate)) && endDate.after(beginDate) && (leaveFromType[i].equals(leaveFromType[j]) || 
								 ("02".equals(leaveFromType[i]) && "01".equals(leaveFromType[j])))){
							 out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"����ټ�¼��ʱ���ϴ����ص�");
							 return null;
						 }
						 **/
						 
					}
				}
			}
		}
		
		
		for(int i=0;i<leaveFromDate.length;i++){
		   beginDate = StringUtil.stringToDate(leaveFromDate[i],"MM/dd/yyyy");
		   endDate = StringUtil.stringToDate(leaveToDate[i],"MM/dd/yyyy");
		   if(leaveDays==null || "".equals(leaveDays)){
			out.print("1���������Number of Working Days������Ϊ��");
			return null;
		  }
		  //У�鿪ʼ���ڲ��ܺ��ڽ�������
		  if(beginDate.after(endDate)){ //�����ʼ���ں��ڽ�������
			out.print("1��ٿ�ʼ���ڲ��ܴ��ڽ�������");
			return null;
		  }
          //����Ƿ�����ظ���ٵ�����
		  
		  //У���û���������������ʵ�ʵ���Ч���������ʵ�ʵ���Ч��������ٵĿ�ʼ�������������֮���������������˾�����еķ����͹��ڼ��ں��������
		  double days = StringUtil.getWorkingDays(beginDate,leaveFromType[i],endDate,leaveToType[i]);
		  if(days!=Double.parseDouble(leaveDays[i])){
			out.print("1������������(Number of Working Days)�ǣ�"+leaveDays[i]+",����������֮����������("+days+")��һ��");
			return null;
		  }
          
		  //У���û��������������Ƿ��Ѿ���������ӵ�еļ�������
		}
		return null;
	}
   
	
	/**
	 * ����Ӱ�form���ڱ���֮ǰ��Ҫ���ø÷�������У��
	 * ���˵�������������ʾ��Ϣ�У����俪ʼ��λ������һλ����������ʶ�� ��ʾ��Ϣ�أ�2�� ���Ǿ�����Ϣ����2��
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionLocation validateOTForm(
			ModuleMapping mapping,
			HttpServletRequest request,
			HttpServletResponse response)
	        throws Exception {
		response.setContentType("text/html;charset=GB2312"); //it is very important
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		
		String[] otPlanDate = request.getParameterValues("ot_plan_date");
		String requestNo = (String)request.getParameter("request_no");
		String isExceptionalCase = (String)request.getParameter("is_exceptional_case"); //�Ƿ����쳣��01---Yes,02---No
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		//��ȡrequst_by��Ϊ����ȽϵĶ���
		String requestStaffCode = (String)request.getParameter(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
		if(requestStaffCode==null){
			requestStaffCode = ((StaffVO) request.getSession().getAttribute(
							CommonName.CURRENT_STAFF_INFOR)).getStaffCode();
		}
		//���ܰ��������Ӱ��¼
		if(!staff.getStaffCode().equals(requestStaffCode)){
			out.print("1You can only apply meal allowance form for yourself.");
			return null;
		}
			

		//����������Щ�Ӱ������Ƿ��Ѿ��������
		if(otPlanDate==null || otPlanDate.length==0){
			out.print("1It should at least has one Meal Allowance record.");
			return null;
		}
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			OTRecordDAO dao = new OTRecordDAO(dbManager);
			String tempDate = "";
						
			/**
			 * Robin Hou(2008-05-09)
			 * �����߼���
			 * 1.���Ա�������ģ����ܳ���ͬһ����ظ���¼��
			 * 2.���ĳ��ļӰ����룬�����Ѿ�������ˣ������������
			 * 3.(2008-09-11)�ͺ������OT��¼�������ܳ���ϵͳ�趨�Ĵ�������������ˣ�����������
			 */
             //added by Young
			HashMap dateApplidCountsMap = new HashMap();
			for(int j=0;j<otPlanDate.length;j++){
				if (dateApplidCountsMap.containsKey(otPlanDate[j])){ //���������������ظ���¼��������
					int exitvalue = Integer.parseInt((String)dateApplidCountsMap.get(otPlanDate[j]))+1;
					dateApplidCountsMap.put(otPlanDate[j], ""+exitvalue);
					out.print("3You have applied the Meal Allowance Date(" + otPlanDate[j]+") over twice.");
					return null;
				}else{
					dateApplidCountsMap.put(otPlanDate[j], ""+1);
				}
			}						
			//cannot be over twice for a day
			int times=0;
			//int NotExceptionTimes = 0;
			int NormalExceptionTimes = 0;
			int MidNightExceptionTimes = 0;
			
			for(int j=0;j<otPlanDate.length;j++){
				times = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo);
				NormalExceptionTimes = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo,CommonName.NORMAL_EXCEPTIONAL_CASE);
				MidNightExceptionTimes = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo,CommonName.MIDNIGHT_EXCEPTIONAL_CASE);
				
				if(times >=2){
					out.print("3You have applied the Meal Allowance twice Date(" + otPlanDate[j]+"), so you can't applied it again.");
					return null;
				}else{
					if((NormalExceptionTimes+MidNightExceptionTimes)==1){//����ǡ����⡯������Ѿ������һ���ˣ������һ������
						  out.print("2You have applied the Meal Allowance Date(" + otPlanDate[j]+") for twice.");
						  return null;
					}
				}
				times = 0;
				NormalExceptionTimes = 0;
				MidNightExceptionTimes = 0;
				//�����ڵ��Ѿ�������ĵ���Ӱ��¼������'����'��  
				//Not Exceptional Case --> 02; Normal Exceptional Case --> 01; Mid-night Exceptional Case --> 03
			/*	NotExceptionTimes = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo,CommonName.NOT_EXCEPTIONAL_CASE);
				NormalExceptionTimes = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo,CommonName.NORMAL_EXCEPTIONAL_CASE);
				MidNightExceptionTimes = dao.getOTRecord(otPlanDate[j],requestStaffCode,requestNo,CommonName.MIDNIGHT_EXCEPTIONAL_CASE);
				
				//if user has been applied 1 not exception OT 
				if(NotExceptionTimes == 1){
					if(MidNightExceptionTimes >= 1 || NormalExceptionTimes >= 1){
						out.print("3You have applied the Meal Allowance twice Date(" + otPlanDate[j]+").");
						return null;
					}
				}else if(NotExceptionTimes > 1){ //if user has been applied >1 not exception OT
					out.print("3You have applied the Meal Allowance twice Date(" + otPlanDate[j]+").");
					return null;
				}else{ //if user has been applied 0 not exception OT
					if((NormalExceptionTimes+MidNightExceptionTimes)>1){//������ڳ���һ���������������루������Ѿ�������һ�Σ�������������һ��.��
						  out.print("3You already had applied twice Exceptional Meal Allowance Date(" + otPlanDate[j]+") ,so you can't applied it again.");
						  return null;
						}else if((NormalExceptionTimes+MidNightExceptionTimes)==1){//����ǡ����⡯������Ѿ������һ���ˣ������һ������
						  out.print("2You have applied the Meal Allowance Date(" + otPlanDate[j]+") for twice.");
						  return null;
						}
				}
				NotExceptionTimes = 0;
				NormalExceptionTimes = 0;
				MidNightExceptionTimes = 0;*/
				
			}
			
			//3.for checking the delaying apply
			String todayDate = StringUtil.getCurrentDateStr("MM/dd/yyyy");
			int delayNum = 0; //�ͺ�����Ĵ������������ϵͳ�������趨�Ĵ���ʱ���������û��ύ�����룩
			int maxNum = 3;
			if(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM)!=null && !"".equals(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM))){
				maxNum = Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM));
			}
			//3.1�����жϱ����ύ�е������ͺ�����Ĵ���
			for(int j=0;j<otPlanDate.length;j++){
				if(StringUtil.stringToDate(otPlanDate[j],"MM/dd/yyyy").before(StringUtil.stringToDate(todayDate,"MM/dd/yyyy"))){
					delayNum = delayNum + 1;
				}
			}
			if(delayNum>maxNum){
				  out.print("3The times of deplaying to apply meal allowance form is over the limitative times("+maxNum+"),so it can't continue to submit.");
				  return null;
			}
			//3.2������������е�û�г����ȶ����ͺ���������ټ�鱾���������ʷ��¼�������ͺ�Ĵ���
			maxNum = maxNum + dao.getDelayingNum(requestStaffCode);
			if(delayNum>maxNum){
				  out.print("3The times of deplaying to apply meal allowance form is over the limitative times("+maxNum+"),so it can't continue to submit.");
				  return null;
			}
			
			/**
			//����HashMap
			Iterator keyIter = dateApplidCountsMap.keySet().iterator();
			String key = "";
			while(keyIter.hasNext()){
				key = (String)keyIter.next();
				CountsFromDataBase = dao.checkOTRecord(key,staff.getStaffCode(),requestNo);
				//CountsFromCurForm = Integer.parseInt(((String)dateApplidCountsMap.get((String)keyIter.next())));
				CountsFromCurForm = Integer.parseInt((String)dateApplidCountsMap.get(key));
				if ((CountsFromDataBase+CountsFromCurForm)>Times){
					tempDate = tempDate + " " + key;
					out.print("3The OT Date(" + tempDate+") might be applied by you for over twice.");
					return null;
				}else if((CountsFromDataBase+CountsFromCurForm)==Times){
					tempDate = tempDate + " " + key;
					if(!"01".equals(isExceptionalCase)){ //��������쳣����������������2��
					  out.print("3You have applied the OT Date(" + tempDate+") for twice,and they are not Exceptional Case.");
					}else{
					  out.print("2You have applied the OT Date(" + tempDate+") for twice.");
					}
					return null;
				}
			}
			//added by Young
			 * 
			 */
			 out.print("success");
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			out.print("1It has error During validate the OT date:" + e.getMessage());
		} finally {
			if (dbManager != null)
				dbManager.freeConnection();
		}
		return null;
	}
	
}
