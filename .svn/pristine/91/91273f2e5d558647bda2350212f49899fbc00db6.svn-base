package com.aiait.eflow.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static boolean isWindowsPlatform = false;

	private static String exp1 = "&";

	private static String exp2 = ">";

	private static String exp3 = "<";

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 格式化double型数据，由于超大数据直接以double型显示时，会显示成科学计数形式，所以需要调用该方法进行格式化后再显示
	 * 
	 * @param d
	 * @return
	 */
	public static final String formatDouble(double d) {
		if (d == 0)
			return "0";
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(d);
	}

	public static final long getLongValue(double d) {
		return (int) Math.round(d);
	}

	/**
	 * null对象转换为空对象
	 * 
	 * @param value
	 *            对象
	 * @return String对象
	 */
	public static final String nullToString(Object value) {
		String strRet = (String) value;
		if (null == strRet) {
			return strRet = "";
		}
		return strRet;
	}

	public static final String replace(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final boolean isEmptyString(String s) {
		if (s == null)
			return true;
		if ("".equals(s.trim()))
			return true;
		return false;
	}

	public static final String[] split(String string, String delim) {
		StringTokenizer token = new StringTokenizer(string, delim);
		String[] result = new String[token.countTokens()];
		List tmp = new ArrayList();
		while (token.hasMoreTokens()) {
			tmp.add(token.nextToken());
		}
		tmp.toArray(result);
		return result;
	}

	/**
	 * 返回值定为String[2]
	 */
	public static final String[] splitStringForOracle(String s) {
		if (s == null)
			return NullStringArray2;
		int length = s.length();
		if (length <= 650)
			return new String[] { s, null };
		String a = s.substring(0, 650);
		String b = s.substring(650, length);
		return new String[] { a, b };
	}

	private static final String[] NullStringArray2 = new String[2];

	/**
	 * copy a byte array to another array , and use a byte to full fill it if
	 * src is not enough
	 * 
	 * @param src
	 * @param srcPos
	 * @param dest
	 * @param destPos
	 * @param length
	 * @param replace
	 * @return
	 */
	public static byte[] byteArrayCopy(byte[] src, int srcPos, byte[] dest,
			int destPos, int length, byte replace) {
		if (src != null) {
			for (int i = 0; i < length; i++) {
				dest[destPos + i] = (i + srcPos < src.length) ? src[i + srcPos]
						: replace;
			}
		}

		return dest;
	}

	/**
	 * Replace all occurrences of the String <code>from</code> to the String
	 * <code>to</code> in the String <code>in</code>.
	 * 
	 * @param in
	 *            the String to be modified.
	 * @param from
	 *            the String occurrences to be replaced.
	 * @param to
	 *            the String to replace the <code>from</code>
	 * @return the new String
	 */
	public static String replaceInString(String in, String from, String to) {
		if (in == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(in.length() * 2);
		String posString = in.toLowerCase();
		String cmpString = from.toLowerCase();
		int i = 0;
		boolean done = false;
		while (i < in.length() && !done) {
			int start = posString.indexOf(cmpString, i);
			if (start == -1) {
				done = true;
			} else {
				sb.append(in.substring(i, start) + to);
				i = start + from.length();
			}
		}
		if (i < in.length()) {
			sb.append(in.substring(i));
		}
		return sb.toString();
	}

	/**
	 * Change the string into a locale encoded string for proper display.
	 * 
	 * @param oldString
	 *            the String to be converted
	 * @return the properly encoded String
	 */
	public static String convertString(String oldString) {
		String newString = null;
		try {
			newString = new String(oldString.getBytes("8859_1"), "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newString;
	}

	public static java.sql.Date stringToSqlDate(String dateString,
			String dateFormat) throws ParseException {
		return new java.sql.Date((stringToDate(dateString, dateFormat))
				.getTime());
	}

	/**
	 * Conver the date format string to util date
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return util.Date
	 */
	public static java.util.Date stringToDate(String dateString,
			String dateFormat) throws ParseException {
		Date returnDate;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		returnDate = sdf.parse(dateString);

		return returnDate;

	}

	public static String getDateStr(java.util.Date date, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
		return formatter.format(date);
	}

	public static String getCurrentDateStr(String dateFormat) {
		java.util.Date currentDate = new java.util.Date();
		return getDateStr(currentDate, dateFormat);
	}

	public static String getDateStr() {
		java.util.Date currentDate = new java.util.Date();
		return getDateStr(currentDate, "yyyyMMddHHmmss");
	}

	/**
	 * 获取上一个年月
	 * 
	 * @param currentDate
	 *            yyyy/MM/dd or yyyy-MM-dd
	 * @return yyyy/mm
	 */
	public static String getPreviousYearMonth(String currentDate) {
		String yearMonth = "";
		if (currentDate != null) {
			if (currentDate != null) {
				yearMonth = currentDate.substring(0, 7);
			}
			String currentYear = currentDate.substring(0, 4);
			String currentMonth = currentDate.substring(5, 7);
			if (Integer.parseInt(currentMonth) == 1) {
				currentYear = "" + (Integer.parseInt(currentYear) - 1);
				currentMonth = "12";
			} else {
				if (Integer.parseInt(currentMonth) <= 10) {
					currentMonth = "0" + (Integer.parseInt(currentMonth) - 1);
				} else {
					currentMonth = "" + (Integer.parseInt(currentMonth) - 1);
				}
			}
			yearMonth = currentYear + "/" + currentMonth;
		}
		return yearMonth;
	}

	/**
	 * 用于去除用户名，密码等输入中的非法字符。
	 */
	public static String validaeInput(String input) {
		if (input != null && input.length() != 0) {
			input = input.trim();
			input = replaceInString(input, "'", "");
			input = replaceInString(input, "\"", "");
		}
		return input;
	}

	/**
	 * 判断用户输入的字符串是否只包含字母和数字。
	 * 
	 * @param input
	 *            用户输入
	 * @return
	 */
	public static boolean isInputValid(String input) {
		boolean isValid = true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * 简单判断是否正确的EMAIL地址。即判断是否含有@和.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) {
		boolean isValid = false;
		int aIndex = email.indexOf("@");
		int dotIndex = email.indexOf(".", aIndex);
		if (aIndex > 0 && dotIndex > 0 && dotIndex < email.length() - 1) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 检查字符串是否为数字
	 */
	public static boolean isDigit(String input) {
		boolean isValid = true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (!Character.isDigit(c)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * 检查字符串是否为英文字母
	 */
	public static boolean isEnglishLetter(String input) {
		boolean isValid = true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (!Character.isLetter(c)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * 首字母大写
	 */
	public static String firstCaseUpper(String input) {
		return String.valueOf((char) (input.charAt(0) - 32))
				+ String.valueOf(input.substring(1));
	}

	/**
	 * 转换字符串编码为gb2312
	 */
	public static String toGB(String isoString) {
		if (!isWindowsPlatform) {
			String gbString = "";
			try {
				gbString = new String(isoString.getBytes("iso8859-1"), "gb2312");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return gbString;
		}

		return isoString;
	}

	public static String FormatHtml(String content) {
		content = replaceInString(content, "'", "");
		content = replaceInString(content, "\"", "");
		content = replaceInString(content, "  ", "&nbsp;");
		content = replaceInString(content, "<script", "&#60;script");
		content = replaceInString(content, "</script", "&#60;/script");

		byte[] enter = new byte[1];
		enter[0] = 0x0a;
		content = replaceInString(content, new String(enter), "");
		enter[0] = 0x0d;
		content = replaceInString(content, new String(enter), "");

		return content;
	}

	public static String FormatSQL(String content) {
		content = replaceInString(content, "'", "''");
		content = replaceInString(content, "\"", "&#34;");
		// content = replaceInString (content , " ", "&nbsp;");
		// content = replaceInString (content , "<script" ,"&#60;script");
		// content = replaceInString (content , "</script" ,"&#60;/script");
		return content;
	}

	public static String FormatHTMLEnter(String content) {
		content = replaceInString(content, "\n", "<br>");
		return content;
	}

	public static String unFormatHtml(String content) {
		content = replaceInString(content, "&#34;", "\"");
		content = replaceInString(content, "&#39;", "'");
		content = replaceInString(content, "&nbsp;", "  ");
		content = replaceInString(content, "&#47;", "\\");
		content = replaceInString(content, "&lt;", "<");
		content = replaceInString(content, "&gt;", ">");

		return content;
	}

	/**
	 * Encode for HTML.
	 */
	public static String htmlEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = replaceInString(str, "<", "&lt;");
		res_str = replaceInString(res_str, ">", "&gt;");
		res_str = replaceInString(res_str, "\"", "&quot;");
		res_str = replaceInString(res_str, "'", "&#039;");
		return res_str;
	}

	public static String formatXML(String xmlData) {
		if (xmlData == null || "".equals(xmlData))
			return "";
		xmlData = StringUtil.replaceInString(xmlData, "&", "&amp;");
		xmlData = StringUtil.replaceInString(xmlData, "<", "&lt;");
		xmlData = StringUtil.replaceInString(xmlData, ">", "&gt;");
		xmlData = StringUtil.replaceInString(xmlData, "'", "&apos;");
		xmlData = StringUtil.replaceInString(xmlData, "\"", "&quot;");
		return xmlData;
	}

	/**
	 * 获取当前日期前后N天的日期
	 * 
	 * @param n
	 * @return
	 */
	public static String afterNDay(int n, String dateFormat) {
		if (dateFormat == null) {
			dateFormat = "MM/dd/yyyy";
		}
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(dateFormat);
		c.setTime(new Date());
		c.add(Calendar.DATE, n);
		Date d2 = c.getTime();
		String s = df.format(d2);
		return s;
	}

	public static String afterNDay(int n, String dateFormat, String beginDate)
			throws Exception {
		if (dateFormat == null) {
			dateFormat = "MM/dd/yyyy";
		}
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(dateFormat);

		Date d = df.parse(beginDate);

		c.setTime(d);
		c.add(Calendar.DATE, n);
		Date d2 = c.getTime();
		String s = df.format(d2);
		return s;
	}

	/**
	 * 获取指定日期所在月份的第一天日期
	 * 
	 * @param origDate
	 * @param dateFormat
	 * @return
	 */
	public static String getMonthFirstDay(Date origDate, String dateFormat) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date()); // someDate 为你要获取的那个月的时间
		ca.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDate = ca.getTime();
		return StringUtil.getDateStr(firstDate, dateFormat);
	}

	/**
	 * 获取指定两个日期之间的间隔的工作天数（包括半天）
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param beginType
	 *            开始日期的类型，例如 01：上午；02：下午
	 * @param endDate
	 *            结束日期
	 * @param endType
	 *            结束日期的类型，例如 01：上午；02：下午
	 * @return 相隔的天数 例如：getDays('04/04/2008','01','04/04/2008','01')=0.5
	 */
	public static double getWorkingDays(Date beginDate, String beginType,
			Date endDate, String endType) {
		double result = 0;
		if (beginType == null || "".equals(beginType)) {
			beginType = "01"; // default is '上午'
		}
		if (endType == null || "".equals(endType)) {
			endType = "02"; // default is '下午'
		}
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(beginDate);
		calEnd.setTime(endDate);
		int days = getDaysBetween(calStart, calEnd);
		result = days;
		if (beginType.equals(endType)) {
			result = result + 0.5;
		} else {
			result = result + 1;
		}
		return result;
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDaysBetween(java.util.Calendar d1,
			java.util.Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR)
				- d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}
	
	public static String formatPercent(int numerator, int denominator){
		if( 0 == denominator) return "0.00%";
		float value = (float)numerator / denominator;
		return percentFormat.format(value); 
	}

    public static String decoderURL(String value) {
        if (value != null) {
            try {
                value = java.net.URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return value;
    }
	
	private static final DecimalFormat  percentFormat = new DecimalFormat("#0.00%"); 
	
	public static String generateId(String prefix){
	     SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	     return prefix+"_"+sdf.format(new Date())+RandomUtil.createRandomStr(3, null);
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(StringUtil.getCurrentDateStr("MMddyyyy
		// hh:mm:ss"));
		// System.out.println(StringUtil.getWorkingDays(StringUtil.stringToDate("05/05/2008","MM/dd/yyyy"),"01",StringUtil.stringToDate("05/06/2008","MM/dd/yyyy"),"01"));
		//double d = Float.parseFloat(("12345678.5"));
		//System.out.println(StringUtil.formatDouble(d));
		System.out.println(StringUtil.isDigit("4345.4"));
	}

}