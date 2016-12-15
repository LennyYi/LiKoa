package com.aiait.eflow.common.helper;

import com.aiait.eflow.util.FileUtil;
import com.aiait.framework.mvc.util.Global;

public class ResourceHelper {
	
	private static ResourceHelper helper;
	private static String emailSuffix = "";

	public static ResourceHelper getInstance(){
		if(helper==null){
			helper = new ResourceHelper();
		}
		return helper;
	}
	
	private ResourceHelper(){
		//Global.WEB_ROOT_PATH
		emailSuffix = FileUtil.readfile(Global.WEB_ROOT_PATH+"/WEB-INF/email_suffix.txt");
	}
	
	public String getEmailSuffix(){
		return emailSuffix;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(ResourceHelper.getInstance().getEmailSuffix());
	}

}
