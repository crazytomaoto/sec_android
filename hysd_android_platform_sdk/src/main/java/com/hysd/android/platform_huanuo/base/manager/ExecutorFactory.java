package com.hysd.android.platform_huanuo.base.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FileName    : ExecutorFactory.java
 * Description : 线程池工厂类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-10 上午11:06:33
 **/
public class ExecutorFactory {

	/** Logic的线程池管理器 */
	private static ExecutorService EXCUTOR_LOGIC_POOL = Executors.newFixedThreadPool(10);

	private ExecutorFactory() {
	}

	/** 执行Logic的异步线程 */
	public static void executeLogic(Runnable runnable) {
		if (runnable != null) {
			EXCUTOR_LOGIC_POOL.execute(runnable);
		}
	}
}
