package com.hysd.android.platform_huanuo.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

	private static final String TAG = "StringUtils";

	/**
	 * 生成oid的最后随机数的长度
	 */
	private static final int OID_RANDOM_LENGTH = 5;

	private StringUtils() {
	}

	/**
	 * 是否为null或空字符串
	 * 
	 * @param str
	 *            字符串
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())  || "null".equals(str.trim().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 包含 null 字符串的为空判断
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 * */
	public static boolean isNEmpty(String str) {
		return isEmpty(str) || (isNotEmpty(str) && "null".equals(str.trim().toLowerCase()));
	}

	/**
	 * 非null或非空
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 用于判断输入框输入是否是中文
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isCN(String str) {
		try {
			byte[] bytes = str.getBytes("UTF-8");
			if (bytes.length == str.length()) {
				return false;
			} else {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			Logger.d(TAG, "StringUtil 中isCN() 方法报异常:" + e);
		}
		return false;
	}

	/**
	 * <判断是否为手机号码>
	 * 
	 * @param phoneNumber
	 *            手机号
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		String reg = "1[3,4,5,8]{1}\\d{9}";
		return phoneNumber.matches(reg);
	}

	/**
	 * <判断是否是数字>
	 * 
	 * @param str
	 *            字符串
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+";
		return str.matches(reg);
	}

	/**
	 * 是否是邮件
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 字符串转为整数(如果转换失败,则返回 -1)
	 * 
	 * @param str
	 *            字符串
	 * @return int [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int stringToInt(String str) {
		if (isEmpty(str)) {
			return -1;
		}
		try {
			return Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * 字符串转为整数(如果转换失败,则返回 defaultValue)
	 * 
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return int [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int stringToInt(String str, int defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * <字符串转为long(如果转换失败,则返回 -1)> <功能详细描述>
	 * 
	 * @param str
	 *            字符串转
	 * @return long [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static long stringToLong(String str) {
		if (isEmpty(str)) {
			return -1;
		}
		try {
			return Long.parseLong(str.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * 字体串转为boolean (如果转换失败,则返回false)
	 * 
	 * @param str
	 *            字符串转
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean stringToBoolean(String str) {
		if (isEmpty(str)) {
			return false;
		}
		try {
			return Boolean.parseBoolean(str.trim());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * boolean转为字体串
	 * 
	 * @param bool
	 *            boolean
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String booleanToString(Boolean bool) {
		String booleanString = "false";
		if (bool) {
			booleanString = "true";
		}
		return booleanString;
	}

	/**
	 * <从异常中获取调用栈>
	 * 
	 * @param ex
	 *            Throwable
	 * @return String [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getExceptionStackTrace(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		return writer.toString();
	}

	/**
	 * <Unicode转化为中文>
	 * 
	 * @param dataStr
	 *            Unicod字体串
	 * @return String [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String decodeUnicode(String dataStr) {
		final StringBuffer buffer = new StringBuffer();
		String tempStr = "";
		String operStr = dataStr;
		if (operStr != null && operStr.indexOf("\\u") == -1) {
			return buffer.append(operStr).toString();
		}
		if (operStr != null && !operStr.equals("") && !operStr.startsWith("\\u")) {
			tempStr = StringUtils.substring(operStr, 0, operStr.indexOf("\\u"));
			operStr = StringUtils.substring(operStr, operStr.indexOf("\\u"), operStr.length());
		}
		buffer.append(tempStr);
		// 循环处理,处理对象一定是以unicode编码字符打头的字符串
		while (operStr != null && !operStr.equals("") && operStr.startsWith("\\u")) {
			tempStr = StringUtils.substring(operStr, 0, 6);
			operStr = StringUtils.substring(operStr, 6, operStr.length());
			String charStr = "";
			charStr = StringUtils.substring(tempStr, 2, tempStr.length());
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串
			buffer.append(letter);
			if (operStr.indexOf("\\u") == -1) {
				buffer.append(operStr);
			} else { // 处理operStr使其打头字符为unicode字符
				tempStr = StringUtils.substring(operStr, 0, operStr.indexOf("\\u"));
				operStr = StringUtils.substring(operStr, operStr.indexOf("\\u"), operStr.length());
				buffer.append(tempStr);
			}
		}
		return buffer.toString();
	}

	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return String [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start, int end) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > end) {
			return "";
		}
		if (start > len) {
			return "";
		}
		if (end > len) {
			return str.substring(start, len);
		}
		return str.substring(start, end);
	}

	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @return String [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > len) {
			return "";
		}
		return str.substring(start);
	}

	/**
	 * <将字符串截取为较短的字符串>
	 * 
	 * @param content
	 *            字符串
	 * @param length
	 *            长度
	 * @return CharSequence
	 * @see [类、类#方法、类#成员]
	 */
	public static String cutString(String content, int length) {
		if (StringUtils.isEmpty(content)) {
			return "";
		}
		if (content.length() <= length) {
			return content;
		}
		return content.substring(0, length);
	}

	/**
	 * <将字符串多空格，多换行替换成一个空格> <功能详细描述>
	 * 
	 * @param content
	 *            字符串
	 * @return String [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String tirmString(String content) {
		if (StringUtils.isEmpty(content)) {
			return "";
		}

		return content.replaceAll("[ \n\r\t]+", " ");
	}

	/**
	 * 判断字符是否数字
	 * 
	 * @param str
	 *            字符
	 * @return 数字
	 */
	public static boolean isDigital(String str) {
		return str.matches("(-)?\\d+");
	}

	/**
	 * 判断字符是否带小数
	 * 
	 * @param str
	 *            字符
	 * @return boolean
	 */
	public static boolean isDouble(String str) {
		if (isDigital(str)) {
			return true;
		}
		return str.matches("(-)?\\d+\\.\\d+");
	}

	/**
	 * 判断是否存在自定特殊字符
	 * 
	 * @param str
	 *            字符
	 * @return boolean [返回类型说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isErrorCodeStr(String str) {
		// 正则表达匹配是否有特殊字符
		// Pattern p = Pattern.compile(Common.STR_CREATE_CLOUD_FILE_ERROR);
		// Matcher m = p.matcher(str);
		//
		// return m.find();

		// "\\", "/", ":", "*", "?", "\"", "<", ">", "|"
		boolean b1 = str.contains("\\") || str.contains("/") || str.contains(":");
		boolean b2 = str.contains("*") || str.contains("?") || str.contains("\"");
		boolean b3 = str.contains("*") || str.contains("?") || str.contains("\"");
		return b1 || b2 || b3;
	}

	/**
	 * 返回x小数,如果小数部分不够两位则自动填充小数部分
	 * 
	 * @param process
	 *            字符
	 * @return [参数说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getProcess(String process) {
		// 空字符
		if (null == process || "".equals(process.trim())) {
			return "";
		}

		// 非整数或小数
		if (!(isDigital(process) || isDouble(process))) {
			return process;
		}

		int index = process.indexOf('.');

		// 无小数部分
		if (-1 == index) {
			return process + ".00";
		}

		// 整数部分
		String prefix = process.substring(0, index);

		// 小数部分
		String postfix = process.substring(index + 1);

		StringBuilder result = new StringBuilder();

		// 小数部分长度
		switch (postfix.length()) {
		// 无小数部分
			case 0:
				result.append(prefix).append(".00");
				break;
			// 只有一位小数
			case 1:
				result.append(prefix).append('.').append(postfix).append('0');
				break;
			// 两位小数
			case 2:
				result.append(prefix).append('.').append(postfix);
				break;
			// 三位或以上小数,需要进行四舍五入
			default:
				result.append(String.valueOf(Math.round(Double.parseDouble(prefix + postfix.substring(0, 3)) / 10))).insert(result.length() - 2, '.');
				break;
		}
		return result.toString();
	}

	/**
	 * 字符串转数字
	 * 
	 * @param str
	 *            字符串
	 * @param defualtValue
	 *            自定义整型
	 * @return 整型
	 */
	public static int getInt(String str, int defualtValue) {
		return isDigital(str) ? Integer.parseInt(str) : defualtValue;
	}


	/**
	 * 去掉url中多余的斜杠
	 * 
	 * @param url
	 *            字符串
	 * @return 去掉多余斜杠的字符串
	 */
	public static String fixUrl(String url) {
		if (null == url) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer(url);
		for (int i = stringBuffer.indexOf("//", stringBuffer.indexOf("//") + 2); i != -1; i = stringBuffer.indexOf("//", i + 1)) {
			stringBuffer.deleteCharAt(i);
		}
		return stringBuffer.toString();
	}

	/**
	 * 替换字符串中特殊字符
	 * 
	 * @param strData
	 *            源字符串
	 * @return 替换了特殊字符后的字符串，如果strData为NULL，则返回空字符串
	 */
	public static String encodeString(String strData) {
		if (strData == null) {
			return "";
		}
		return strData.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
	}

	/**
	 * 获取记录唯一ID
	 * 
	 * @return String
	 */
	public static String getOID() {
		return System.currentTimeMillis() + getRandomNum(OID_RANDOM_LENGTH);
	}

	/**
	 * 返回特定长度的随机数
	 * 
	 * @param length
	 *            返回长度
	 * @return
	 */
	private static String getRandomNum(int length) {
		length = (length > 0) ? length : 10;
		StringBuffer sRand = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sRand.append((int) (Math.random() * 10));
		}
		return sRand.toString();
	}

	/**
	 * 获取指定编码字符串的长度
	 * 
	 * @param str
	 *            字符串
	 * @param encoding
	 *            编码
	 * @return int
	 */
	public static int getStringLength(String str, String encoding) {
		if (isEmpty(str)) {
			return 0;
		}
		try {
			return str.getBytes(encoding).length;
		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, "获取字符串长度时异常!", e);
			return -1;
		}
	}

	/**
	 * 判断相同
	 * 
	 * @param str0
	 *            字符串
	 * @param str1
	 *            字符串
	 * @return boolean
	 */
	public static boolean areStringEqual(String str0, String str1) {
		// if(str0!=null){
		// return str0.equals(str1);
		// }
		// return str1==null || str1.length()==0;

		if (str0 == null || str0.length() == 0) {
			return str1 == null || str1.length() == 0;
		} else {
			return str0.equals(str1);
		}
	}

	/**
	 * 隐藏字符串中间过长的部分
	 * 
	 * @param src
	 *            原字符串
	 * @param maxLength
	 *            最大长度
	 * @param endLength
	 *            结尾长度
	 * @return 隐藏后的字符串
	 */
	public static String hideMiddleString(String src, int maxLength, int endLength) {
		if (src == null) {
			Logger.w(TAG, "getMiddleString, src is null");
			return null;
		}

		String ellipsis = "...";
		if (maxLength <= endLength + ellipsis.length()) {
			return src;
		}
		if (src.length() <= maxLength) {
			return src;
		}

		// 截取后半段
		String endStr = src.substring(src.length() - endLength);

		// 截取前半段
		String startStr = src.substring(0, maxLength - endLength - ellipsis.length());
		return startStr + ellipsis + endStr;
	}

	/***
	 * 字符串的长度是否某个区间
	 * 
	 * @param str
	 *            字符串
	 * @param minLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @return boolean
	 */
	public static boolean isStringLengthOk(String str, int minLength, int maxLength) {
		if (!isEmpty(str)) {
			int length = str.length();
			if (length >= minLength && length <= maxLength) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 转换百分百(统一保留小数点后两位数)
	 * @param num
	 * @return
	 */
	public static String getPercent(double num) {
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format(num);
	}

	/**
	 * 显示统一格式的数字(统一保留小数点后两位数)
	 * @param num
	 * @return
	 */
	public static String getDefaultNumber(double num) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(num);
	}

	/**
	 * 显示统一格式的数字(统一保留小数点后两位数)
	 * @param num
	 * @return
	 */
	public static String getDefaultNumber(String num) {
		String result = "";
		try {
			Double n = Double.parseDouble(num);
			result = getDefaultNumber(n);
		} catch (Exception e) {
			result = num;
		}
		return result;
	}

	/**
	 * 显示货币金额数字（固定小数点后显示两位数字）
	 * @param num
	 * @return
	 */
	public static String getCashNumber(double num) {
		//DecimalFormat df = new DecimalFormat("0,000.##");
		//DecimalFormat df = new DecimalFormat("0,000.00");
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(num);
	}

	/**
	 * 显示货币金额数字（固定小数点后显示两位数字）
	 * @param num
	 * @return
	 */
	public static String getCashNumber(String num) {
		String result = "";
		try {
			Double n = Double.parseDouble(num);
			result = getCashNumber(n);
		} catch (Exception e) {
			result = num;
		}
		return result;
	}

	/**
	 * 检测输入值是否合法
	 * @param number
	 * @return
	 */
	public static boolean checkNumber(String number) {
		boolean isValid = true;
		try {
			double num = Double.parseDouble(number);
			if (num <= 0) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * 判断字符串是否为最多只有scale位小数的数值
	 * @param number
	 * @param scale
	 * @return
	 */
	public static boolean checkDecimal(String number, int scale) {
		if (scale < 1) {
			throw new IllegalArgumentException("The scale must be a positive integer!");
		}
		String regex = "^[0-9]+(.[0-9]{1,%d})?$";
		//String regex = "^(?:[0-9]\\d*?|0)(?:\\.[0-9]{1,2})?$";
		regex = String.format(regex, scale);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(String.valueOf(number));
		return matcher.matches();
	}

	/**
	 * 创建高亮字符串
	 * @param srcTxt
	 * @param highlightTxt
	 * @param highlightColor
	 * @return
	 */
	public static SpannableStringBuilder getHighlightText(String srcTxt, String highlightTxt, int highlightColor) {
		return getHighlightText(srcTxt, new String[] { highlightTxt }, highlightColor);
	}

	/**
	 * 创建高亮字符串
	 * @param srcTxt
	 * @param highlightTxt
	 * @param highlightColor
	 * @return
	 */
	public static SpannableStringBuilder getHighlightText(String srcTxt, String[] highlightTxt, int highlightColor) {
		SpannableStringBuilder spannable = null;
		if (StringUtils.isNotEmpty(srcTxt)) {
			spannable = new SpannableStringBuilder(srcTxt);
			if (BeanUtils.isNotEmpty(highlightTxt)) {
				for (String txt : highlightTxt) {
					if (StringUtils.isNotEmpty(txt)) {
						int index = srcTxt.indexOf(txt);
						if (index != -1) {
							ForegroundColorSpan span = new ForegroundColorSpan(highlightColor);
							spannable.setSpan(span, index, index + txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
		}
		return spannable;
	}

}
