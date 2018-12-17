package com.hysd.android.platform_huanuo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

/**
 * FileName    : FileUtils.java
 * Description : 文件和IO的操作帮助类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-9 下午1:33:07
 **/
public final class FileUtils {

	/**
	 * 文件对应MIME类型
	 */
	private static final Map<String, String> FILE_MIME_MAPPING = new HashMap<String, String>(50);

	static {
		// 初始化多媒体类型映射关系
		initMIMEMapping();
	}

	/** 是否有sdcard */
	public static boolean isExistSDcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/** 获取应用目录的根目录 */
	public static String getContextPath(Context context) {
		String path = "";
		try {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + context.getPackageName()
					+ File.separator;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
		}
		return path;
	}

	/** 获取应用目录的相对路径 */
	public static String getContextPath(Context context, String path) {
		return getContextPath(context) + path;
	}

	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean exists(String path) {
		return new File(path).exists();
	}

	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean checkExists(String path) {
		return new File(path).mkdir();
	}

	/**
	 * 创建文件夹
	 * @param directory directory
	 * @return boolean
	 */
	public static boolean createDir(String directory) {
		if (StringUtils.isEmpty(directory)) {
			return false;
		}

		File destDir = new File(directory);

		if (destDir.isDirectory()) {
			return true;
		} else {
			if (isExistSDcard()) {
				return destDir.mkdirs();
			}
		}

		return false;
	}

	/**
	 * 获取文件内容
	 * 
	 * @param in
	 * @param encode
	 * @return
	 */
	public static String getContent(InputStream in, String encode) {
		String mesage = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			int len = 0;
			byte[] data = new byte[1024];
			while ((len = in.read(data)) != -1) {
				outputStream.write(data, 0, len);
			}
			mesage = new String(outputStream.toByteArray(), encode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BeanUtils.close(in);
		}
		return mesage;
	}

	/**
	 * 保存文件
	 * 
	 * @param inps
	 * @param filePath
	 */
	public static void saveFile(InputStream inps, String filePath) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			int len = 0;
			byte[] data = new byte[1024];
			while ((len = inps.read(data)) != -1) {
				out.write(data, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BeanUtils.close(inps);
			BeanUtils.close(out);
		}
	}

	/**
	 * 获取文件内容
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(File file) throws Exception {
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[1024];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BeanUtils.close(in);
		}
		return out.toByteArray();
	}

	/**
	 * return a file based on the mFileUri that always has the format file://xyz/xyz
	 */
	public static File getFileFromUri(Uri fileUri) {
		if (fileUri != null) {
			try {
				URI uri;
				if (fileUri.toString().startsWith("file://")) {
					// normal path
					uri = URI.create(fileUri.toString());
				} else {
					// support path
					uri = URI.create("file://" + fileUri.toString());
				}
				File file = new File(uri);
				if (file != null) {
					if (file.canRead()) {
						return file;
					}
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取文件对应MIME类型
	 * @param fileName fileName
	 * @return String
	 */
	public static String getFileMIME(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return null;
		}
		String extName = getFileExtends(fileName);
		String dataType = FILE_MIME_MAPPING.get(extName);

		if (dataType == null || dataType.length() == 0) {
			MimeTypeMap map = MimeTypeMap.getSingleton();
			dataType = map.getMimeTypeFromExtension(extName);
		}
		return dataType;
	}

	/**
	 * 获取文件扩展名
	 * @param name name
	 * @return String
	 */
	public static String getFileExtends(String name) {
		return getFileExtends(name, null);
	}

	/**
	 * 获取文件扩展名
	 * @param name name
	 * @param defaultName defaultName
	 * @return String
	 */
	public static String getFileExtends(String name, String defaultName) {
		String extName = defaultName;
		if (name != null && name.length() > 0) {
			int dotIdx = name.lastIndexOf('.');
			// 后缀名称
			if (-1 != dotIdx) {
				extName = name.substring(dotIdx + 1).toLowerCase();
			}
		}
		return extName;
	}

	/**
	 * 初始化多媒体类型映射关系
	 */
	private static void initMIMEMapping() {
		// 文档对应媒体类型
		FILE_MIME_MAPPING.put("3gp", "video/3gpp");
		FILE_MIME_MAPPING.put("asf", "video/x-ms-asf");
		FILE_MIME_MAPPING.put("avi", "video/x-msvideo");
		FILE_MIME_MAPPING.put("bin", "application/octet-stream");
		FILE_MIME_MAPPING.put("apk", "application/vnd.android.package-archive");
		FILE_MIME_MAPPING.put("bmp", "image/bmp");
		FILE_MIME_MAPPING.put("c", "text/plain");
		FILE_MIME_MAPPING.put("class", "application/octet-stream");
		FILE_MIME_MAPPING.put("conf", "text/plain");
		FILE_MIME_MAPPING.put("cpp", "text/plain");
		FILE_MIME_MAPPING.put("doc", "application/msword");
		FILE_MIME_MAPPING.put("exe", "application/octet-stream");
		FILE_MIME_MAPPING.put("gtar", "application/x-gtar");
		FILE_MIME_MAPPING.put("gz", "application/x-gzip");
		FILE_MIME_MAPPING.put("h", "text/plain");
		FILE_MIME_MAPPING.put("htm", "text/html");
		FILE_MIME_MAPPING.put("html", "text/html");
		FILE_MIME_MAPPING.put("jar", "application/java-archive");
		FILE_MIME_MAPPING.put("java", "text/plain");
		FILE_MIME_MAPPING.put("jpeg", "image/jpeg");
		FILE_MIME_MAPPING.put("jpg", "image/jpeg");
		FILE_MIME_MAPPING.put("js", "application/x-javascript");
		FILE_MIME_MAPPING.put("log", "text/plain");
		FILE_MIME_MAPPING.put("m3u", "audio/x-mpegurl");
		FILE_MIME_MAPPING.put("m4a", "audio/mp4a-latm");
		FILE_MIME_MAPPING.put("m4b", "audio/mp4a-latm");
		FILE_MIME_MAPPING.put("m4p", "audio/mp4a-latm");
		FILE_MIME_MAPPING.put("m4u", "video/vnd.mpegurl");
		FILE_MIME_MAPPING.put("m4v", "video/x-m4v");
		FILE_MIME_MAPPING.put("mov", "video/quicktime");
		FILE_MIME_MAPPING.put("mp2", "audio/x-mpeg");
		FILE_MIME_MAPPING.put("mp3", "audio/x-mpeg");
		FILE_MIME_MAPPING.put("aac", "audio/x-mpeg");
		FILE_MIME_MAPPING.put("mp4", "video/mp4");
		FILE_MIME_MAPPING.put("mpc", "application/vnd.mpohun.certificate");
		FILE_MIME_MAPPING.put("mpe", "video/mpeg");
		FILE_MIME_MAPPING.put("mpeg", "video/mpeg");
		FILE_MIME_MAPPING.put("mpg", "video/mpeg");
		FILE_MIME_MAPPING.put("mpg4", "video/mp4");
		FILE_MIME_MAPPING.put("mpga", "audio/mpeg");
		FILE_MIME_MAPPING.put("msg", "application/vnd.ms-outlook");
		FILE_MIME_MAPPING.put("ogg", "audio/ogg");
		FILE_MIME_MAPPING.put("pdf", "application/pdf");
		FILE_MIME_MAPPING.put("png", "image/png");
		FILE_MIME_MAPPING.put("pps", "application/vnd.ms-powerpoint");
		FILE_MIME_MAPPING.put("ppt", "application/vnd.ms-powerpoint");
		FILE_MIME_MAPPING.put("prop", "text/plain");
		FILE_MIME_MAPPING.put("rar", "application/x-rar-compressed");
		FILE_MIME_MAPPING.put("rc", "text/plain");
		FILE_MIME_MAPPING.put("rmvb", "video/*");
		FILE_MIME_MAPPING.put("rtf", "application/rtf");
		FILE_MIME_MAPPING.put("sh", "text/plain");
		FILE_MIME_MAPPING.put("tar", "application/x-tar");
		FILE_MIME_MAPPING.put("tgz", "application/x-compressed");
		FILE_MIME_MAPPING.put("txt", "text/plain");
		FILE_MIME_MAPPING.put("wav", "audio/x-wav");
		FILE_MIME_MAPPING.put("wma", "audio/x-ms-wma");
		FILE_MIME_MAPPING.put("wmv", "video/x-ms-wmv");
		FILE_MIME_MAPPING.put("wps", "application/vnd.ms-works");
		FILE_MIME_MAPPING.put("xml", "text/xml");
		FILE_MIME_MAPPING.put("xml", "text/plain");
		FILE_MIME_MAPPING.put("z", "application/x-compress");
		FILE_MIME_MAPPING.put("zip", "application/zip");
	}

}
