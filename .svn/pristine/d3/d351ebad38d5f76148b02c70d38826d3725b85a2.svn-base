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
	 * 个人申请请假form，在保存之前需要调用该方法进行校验
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
		String[] leaveFromDate = (String[])request.getParameterValues("leave_from_date");  //请假开始日期
		String[] leaveFromType = (String[])request.getParameterValues("leave_from_type");  //请假开始的类型：01--上午；02---下午
		String[] leaveToDate = (String[])request.getParameterValues("leave_to_date");      //请假结束日期
		String[] leaveToType = (String[])request.getParameterValues("leave_to_type");      //请假结束的类型：01--上午；02--下午
		String[] leaveType = (String[])request.getParameterValues("leave_type");            //请假类型：年假，带薪病假，事假...
		String[] leaveDays = (String[])request.getParameterValues("leave_days");           //请假天数
		
		if(leaveFromDate==null || leaveFromDate.length<1){
			out.print("1没有输入请假记录");
			return null;
		}
		
		Date beginDate,endDate;
		Date firstBeginDate,firstEndDate;
		//如果是批量申请请假操作，则需要检查本次申请中，是否存在重复或者重叠请假记录
		if(leaveFromDate.length>1){
			String[] staffCode = request.getParameterValues("staff_code");
			for(int i=0;i<leaveFromDate.length;i++){
				for(int j=i+1;j<leaveFromDate.length;j++){
					//如果是完全重复,同一个请假人，同一样的请假时间
					if(staffCode[i].equals(staffCode[j]) &&  leaveFromDate[i].equals(leaveFromDate[j]) && leaveFromType[i].equals(leaveFromType[j])
							&&  leaveToDate[i].equals(leaveToDate[j])){
						out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"的请假记录存在重复");
						return null;
					}
					//如果是请假时间存在重叠,同一个请假人，两条记录的日期存在重叠
					if(staffCode[i].equals(staffCode[j])){
						 beginDate = StringUtil.stringToDate(leaveFromDate[j],"MM/dd/yyyy");
						 endDate = StringUtil.stringToDate(leaveToDate[j],"MM/dd/yyyy");
						 firstBeginDate = StringUtil.stringToDate(leaveFromDate[i],"MM/dd/yyyy");
						 firstEndDate = StringUtil.stringToDate(leaveToDate[i],"MM/dd/yyyy");
						 /**
						 if((firstBeginDate==beginDate || firstBeginDate.after(beginDate)) && endDate.after(beginDate) && (leaveFromType[i].equals(leaveFromType[j]) || 
								 ("02".equals(leaveFromType[i]) && "01".equals(leaveFromType[j])))){
							 out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"的请假记录在时间上存在重叠");
							 return null;
						 }else if((firstEndDate==beginDate || firstEndDate.after(beginDate)) && endDate.after(beginDate) && (leaveFromType[i].equals(leaveFromType[j]) || 
								 ("02".equals(leaveFromType[i]) && "01".equals(leaveFromType[j])))){
							 out.print("1"+StaffTeamHelper.getInstance().getStaffNameByCode(staffCode[i]).trim()+"的请假记录在时间上存在重叠");
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
			out.print("1请假天数（Number of Working Days）不能为空");
			return null;
		  }
		  //校验开始日期不能后于结束日期
		  if(beginDate.after(endDate)){ //如果开始日期后于结束日期
			out.print("1请假开始日期不能大于结束日期");
			return null;
		  }
          //检查是否存在重复请假的日期
		  
		  //校验用户输入的请假天数与实际的有效天数相符（实际的有效天数：请假的开始日期与结束日期之间的天数，除掉公司日历中的法定和公众假期后的天数）
		  double days = StringUtil.getWorkingDays(beginDate,leaveFromType[i],endDate,leaveToType[i]);
		  if(days!=Double.parseDouble(leaveDays[i])){
			out.print("1输入的请假天数(Number of Working Days)是："+leaveDays[i]+",与两个日期之间间隔的天数("+days+")不一致");
			return null;
		  }
          
		  //校验用户输入的请假天数是否已经超过了其拥有的假期天数
		}
		return null;
	}
   
	
	/**
	 * 申请加班form，在保存之前需要调用该方法进行校验
	 * 输出说明：在输出的提示信息中，在其开始的位置增加一位数字用来标识是 提示信息呢（2） 还是警告信息（非2）
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
		String isExceptionalCase = (String)request.getParameter("is_exceptional_case"); //是否是异常：01---Yes,02---No
		StaffVO staff = (StaffVO) request.getSession().getAttribute(
				CommonName.CURRENT_STAFF_INFOR);
		//获取requst_by作为后面比较的对象
		String requestStaffCode = (String)request.getParameter(CommonName.SYSTEM_ID_REQUEST_STAFF_CODE);
		if(requestStaffCode==null){
			requestStaffCode = ((StaffVO) request.getSession().getAttribute(
							CommonName.CURRENT_STAFF_INFOR)).getStaffCode();
		}
		//不能帮别人申请加班记录
		if(!staff.getStaffCode().equals(requestStaffCode)){
			out.print("1You can only apply meal allowance form for yourself.");
			return null;
		}
			

		//检查申请的这些加班日期是否已经申请过了
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
			 * 处理逻辑：
			 * 1.来自本次申请的，不能出现同一天的重复记录；
			 * 2.如果某天的加班申请，曾经已经申请过了，分两种情况：
			 * 3.(2008-09-11)滞后申请的OT记录次数不能超过系统设定的次数，如果超过了，则不允许申请
			 */
             //added by Young
			HashMap dateApplidCountsMap = new HashMap();
			for(int j=0;j<otPlanDate.length;j++){
				if (dateApplidCountsMap.containsKey(otPlanDate[j])){ //如果本次申请出现重复记录，则不允许
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
					if((NormalExceptionTimes+MidNightExceptionTimes)==1){//如果是‘例外’的情况已经申请过一次了，则给出一个提醒
						  out.print("2You have applied the Meal Allowance Date(" + otPlanDate[j]+") for twice.");
						  return null;
					}
				}
				times = 0;
				NormalExceptionTimes = 0;
				MidNightExceptionTimes = 0;
				//检查存在的已经申请过的当天加班记录数（非'例外'）  
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
					if((NormalExceptionTimes+MidNightExceptionTimes)>1){//如果存在超过一次了则不允许再申请（即如果已经存在了一次，则还允许再申请一次.）
						  out.print("3You already had applied twice Exceptional Meal Allowance Date(" + otPlanDate[j]+") ,so you can't applied it again.");
						  return null;
						}else if((NormalExceptionTimes+MidNightExceptionTimes)==1){//如果是‘例外’的情况已经申请过一次了，则给出一个提醒
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
			int delayNum = 0; //滞后申请的次数。当其大于系统参数中设定的次数时，不允许用户提交（申请）
			int maxNum = 3;
			if(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM)!=null && !"".equals(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM))){
				maxNum = Integer.parseInt(ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_DELAY_NUM));
			}
			//3.1首先判断本次提交中的属于滞后申请的次数
			for(int j=0;j<otPlanDate.length;j++){
				if(StringUtil.stringToDate(otPlanDate[j],"MM/dd/yyyy").before(StringUtil.stringToDate(todayDate,"MM/dd/yyyy"))){
					delayNum = delayNum + 1;
				}
			}
			if(delayNum>maxNum){
				  out.print("3The times of deplaying to apply meal allowance form is over the limitative times("+maxNum+"),so it can't continue to submit.");
				  return null;
			}
			//3.2如果本次申请中的没有超过先定的滞后次数，则再检查本月申请的历史记录中属于滞后的次数
			maxNum = maxNum + dao.getDelayingNum(requestStaffCode);
			if(delayNum>maxNum){
				  out.print("3The times of deplaying to apply meal allowance form is over the limitative times("+maxNum+"),so it can't continue to submit.");
				  return null;
			}
			
			/**
			//遍历HashMap
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
					if(!"01".equals(isExceptionalCase)){ //如果不是异常的情况，则不允许大于2次
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
