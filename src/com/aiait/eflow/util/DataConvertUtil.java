package com.aiait.eflow.util;


public class DataConvertUtil {
  final static String CHAR_ISO8859_1 = "ISO8859-1";
  final static String CHAR_GB2312 = "GB2312";
  final static String CHAR_UTF_8 = "UTF-8";
  final static String CHAR_GBK = "GBK";
	
  public static String convertISOToGB2312(String origStr){
		//try{
		//	origStr=new String(origStr.getBytes(DataConvertUtil.CHAR_ISO8859_1),DataConvertUtil.CHAR_GB2312);
		//}catch(Exception e){}
		return origStr;
  }
  
  public static String convertISOToUTF8(String origStr){
		//try{
		//	origStr=new String(origStr.getBytes(DataConvertUtil.CHAR_ISO8859_1),DataConvertUtil.CHAR_UTF_8);
		//}catch(Exception e){}
		return origStr;
}
  
  public static String convertISOToGBK(String origStr){
		//try{
		//	origStr=new String(origStr.getBytes(DataConvertUtil.CHAR_ISO8859_1),DataConvertUtil.CHAR_GBK);
		//}catch(Exception e){}
		return origStr;
}
  
  public static String convertGBKToISO(String origStr){
		//try{
		//	origStr=new String(origStr.getBytes(DataConvertUtil.CHAR_GBK),DataConvertUtil.CHAR_ISO8859_1);
		//}catch(Exception e){}
		return origStr;
}
  
  public static String convertGB2312ToISO(String origStr){
		//try{
		//	origStr=new String(origStr.getBytes(DataConvertUtil.CHAR_GB2312),DataConvertUtil.CHAR_ISO8859_1);
		//}catch(Exception e){}
		return origStr;
}
}
