package com.hysd.android.platform_huanuo.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteClosable;

import com.hysd.android.platform_huanuo.net.base.ResultItem;
import com.hysd.android.platform_huanuo.net.http.HttpRequest;

/**
 * FileName    : BeanUtils.java
 * Description : 通用的对象操作帮助类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 下午2:12:26
 **/
public class BeanUtils {

	/** 判断对象不为null或为空  */
	public static boolean isNotEmpty(Object obj) {
		return !isNEmpty(obj);
	}

	/** 判断对象为null或为空  */
	public static boolean isNEmpty(Object obj) {
		return obj == null ? true : isEmpty(obj);
	}

	/** 判断字符串为空，集合为空，数组为空(后续可以拓展hashSet,hashMap ...) */
	public static boolean isEmpty(Object obj) {
		boolean isEmpty = true;
		if (obj != null) {
			if (obj instanceof String) {
				// 字符串
				String tmp = obj.toString();
				isEmpty = tmp.trim().equals("");

			} else if (obj instanceof Collection<?>) {
				// 集合
				Collection<?> collections = (Collection<?>) obj;
				isEmpty = collections.size() == 0;

			} else if (obj instanceof Map<?, ?>) {
				// Map
				Map<?, ?> map = (Map<?, ?>) obj;
				isEmpty = map.size() == 0;

			} else if (obj instanceof Object[]) {
				// 数组
				Object[] objarray = (Object[]) obj;
				isEmpty = objarray.length == 0;
			}
		}
		return isEmpty;
	}

	/** 关闭所有需要关闭的资源 */
	public static void close(Object closeObj) {
		if (closeObj != null) {
			try {
				if (closeObj instanceof Closeable) {
					// IO 操作的流对象
					((Closeable) closeObj).close();

				} else if (closeObj instanceof Cursor) {
					// 关闭Cursor
					((Cursor) closeObj).close();

				} else if (closeObj instanceof SQLiteClosable) {
					// 关闭SQLite数据库相关引用
					((SQLiteClosable) closeObj).releaseReference();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 同一个根据键值获取值的方法
	 * @param obj
	 * @param key
	 * @return
	 */
	public static Object getFieldValue(Object obj, String key) {
		if (obj != null) {
			if ((obj instanceof Map))
				return ((Map) obj).get(key);
			if ((obj instanceof ResultItem))
				return ((ResultItem) obj).get(key);
			if ((obj instanceof String)) {
				return obj;
			}
		}
		return null;
	}

	/** 关闭正在执行的线程 */
	public static void stopThread(Thread thread) {
		if (thread != null) {
			try {
				thread.interrupt();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * md5加密
	 * @param source
	 * @return
	 */
	public static String md532(String source) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = source.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];

				str[(k++)] = hexDigits[(b >> 4 & 0xF)];
				str[(k++)] = hexDigits[(b & 0xF)];
			}
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取手机WIFI的MAC地址
	 * @return mac
	 */
	public static String getMAC() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		if (macSerial == null) {
			macSerial = "123456";
		}
		return macSerial;
	}

	/**
	 * 生成请求签名
	 * @param apps
	 * @return
	 */
	public static void buildSign(HttpRequest request) {
		TreeMap<String, String> args = new TreeMap<String, String>();
		int length = request.getParamNames().size();
		//收集参数
		for (int i = 0; i < length; i++) {
			try {
				Object value = request.getParamValues().get(i);
				boolean isSupport = value instanceof File;
				if (!isSupport) {
					args.put(request.getParamNames().get(i).trim(), value == null ? "" : value.toString().trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//遍历参数生成签名
		StringBuilder stbur = new StringBuilder("xxxx.com");
		for (Entry<String, String> entry : args.entrySet()) {
			stbur.append(entry.getKey() + entry.getValue());
		}
		request.addParam("sign", md532(stbur.toString()));
	}
}
