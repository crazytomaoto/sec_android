package com.hysd.android.platform_huanuo.net.base;

import com.hysd.android.platform_huanuo.utils.Logger;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileName    : BaseRequestManager.java
 * Description : 请求管理
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-25 下午5:38:25
 **/
public abstract class BaseRequestProcesser implements IRequestProcesser{

	protected String TAG = this.getClass().getSimpleName();
	
	/**所有的请求对象*/
	private ConcurrentHashMap<String, IRequestTask> requestTasks = new ConcurrentHashMap<String, IRequestTask>();

	@Override
	public void addRequest(IRequestItem request) {
		Logger.d(TAG, "addRequest requestId "+request.getRequestId());
		//创建任务
		IRequestTask task = buildTask();
		requestTasks.put(request.getRequestId(), task);
		//执行
		task.init(request);
		task.exec();
		//执行完成后移除
		requestTasks.remove(request.getRequestId());
		Logger.d(TAG, "remove requestId "+request.getRequestId());
	}

	@Override
	public void cancelRequest(String requestId) {
		IRequestTask task = requestTasks.get(requestId);
		if(task !=null){
			task.cancel();
			requestTasks.remove(requestId);
		}else{
			Logger.d(TAG, "cancelRequest not find requestId: "+requestId);
		}
	}

	@Override
	public Collection<IRequestTask> list() {
		return requestTasks.values();
	}

	@Override
	public void clear() {
		try{
			Collection<IRequestTask> tasks = list();
			for(IRequestTask task : tasks){
				task.cancel();
			}
		}catch(Exception e){
			e.printStackTrace();
			Logger.e(TAG, "remove requestId "+e.getMessage());
		}
	}
	
	/**
	 * 创建ITask任务
	 * @param
	 * @return
	 */
	public abstract IRequestTask buildTask();
	
}
