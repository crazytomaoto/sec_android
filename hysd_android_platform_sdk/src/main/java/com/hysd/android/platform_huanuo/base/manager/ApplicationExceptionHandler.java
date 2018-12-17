package com.hysd.android.platform_huanuo.base.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import android.content.Context;

import com.hysd.android.platform_huanuo.utils.BeanUtils;
import com.hysd.android.platform_huanuo.utils.DateUtils;
import com.hysd.android.platform_huanuo.utils.FileUtils;
import com.hysd.android.platform_huanuo.utils.Logger;

/**
 * FileName    : ExceptionHandler.java
 * Description : 全局的异常处理
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2013-7-31 下午1:42:41
 **/
public class ApplicationExceptionHandler implements Thread.UncaughtExceptionHandler {

	private static final String TAG = "ApplicationExceptionHandler";
	private static final String BLOCK_SEPARATOR = "********** ********** ********** **********\n";

	private Context mContext;

	public ApplicationExceptionHandler(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String info = getThrowableStack(ex);
		// 保存到文档
		if (FileUtils.isExistSDcard()) {
			saveExceptionLog(thread, info);
		}
		// 打印错误日志
		long threadId = thread.getId();
		Logger.e("Exception:", "Thread.getName()=" + thread.getName() + " id=" + threadId + " state=" + thread.getState());
		Logger.e("Exception:", "Error[" + info + "]");
	}

	/** 获取异常的详细信息 */
	protected String getThrowableStack(Throwable ex) {
		String info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
		} catch (Exception e) {
		} finally {
			BeanUtils.close(printStream);
			BeanUtils.close(baos);
		}
		return info;
	}

	/** 异常信息写入文件 */
	protected void saveExceptionLog(Thread thread, String info) {
		RandomAccessFile writer = null;
		try {
			File exception = new File(FileUtils.getContextPath(mContext, ""));
			if (!exception.exists() && !exception.createNewFile()) {
				return;
			}
			writer = new RandomAccessFile(exception, "rw");
			writer.seek(exception.length());
			writer.write((BLOCK_SEPARATOR + "\n").getBytes());
			writer.write((DateUtils.getNowDate(DateUtils.ALL_FORMAT) + "\n").getBytes());
			writer.write((info + "\n").getBytes());
			writer.write((BLOCK_SEPARATOR + "\n").getBytes());
		} catch (Exception e) {
		} finally {
			BeanUtils.close(writer);
		}

	}

}
