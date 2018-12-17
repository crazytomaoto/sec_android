package com.hysd.android.platform_huanuo.net.http.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import android.widget.ImageView;

import com.hysd.android.platform_huanuo.utils.Logger;

/**
 * FileName    : ImageLoader.java
 * Description : 图片统一加载管理
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-24 上午9:41:42
 **/
public class ImageLoaderManager {

	private String TAG = "ImageLoaderManager";

	/**并发加载的数量*/
	private int loaderCounts = 3;

	/**内存缓存的大小*/
	private long maxCache = 10 * 1024 * 1024;

	/**图片缓存的默认存放地址*/
	private String cachePath = "";

	private static ImageLoaderManager manager;

	private boolean isListenering = false;

	private boolean isLoader = true;

	/**任务执行堆栈*/
	private Stack<ImageLoaderTask> tasksStack = new Stack<ImageLoaderTask>();

	/**正在执行的任务*/
	private List<ImageLoaderTask> runningTask = new ArrayList<ImageLoaderTask>();

	/**本地的总线程数*/
	private ExecutorService localExcutor = Executors.newFixedThreadPool(10);

	/**网络请求的总线程数*/
	private ExecutorService netExcutor;

	/**任务添加队列*/
	private LinkedBlockingQueue<ImageLoaderTask> taskQueue = new LinkedBlockingQueue<ImageLoaderTask>();

	/**单例*/
	private ImageLoaderManager() {
		setLoaderCounts(6);
		startAddTaskListener();
	}

	/**单例方式获取实例*/
	public static synchronized ImageLoaderManager getIntance() {
		if (manager == null) {
			manager = new ImageLoaderManager();
		}
		return manager;
	}

	public void setLoaderCounts(int loaderCounts) {
		if (this.loaderCounts != loaderCounts && loaderCounts > 0) {
			if (netExcutor != null) {
				netExcutor.shutdown();
			}

			netExcutor = Executors.newFixedThreadPool(loaderCounts, new ThreadFactory() {

				private final AtomicInteger poolNumber = new AtomicInteger(1);

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "Images-loader-" + poolNumber.getAndIncrement());
				}
			});
		}
		this.loaderCounts = loaderCounts;

	}

	public void setMaxCache(long maxCache) {
		this.maxCache = maxCache;
	}

	public String getCachePath() {
		return cachePath;
	}

	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

	/**
	 * 停止加载
	 */
	public void stopLoader() {
		isLoader = false;
	}

	/**
	 * 开始加载
	 */
	public void startLoader() {
		isLoader = true;
		schedule();
	}

	/***
	 * 请求堆栈正在加载的任务
	 */
	public void clear() {
		tasksStack.clear();
	}

	/***
	 * 退出应用时候调用的
	 */
	public void exit(){
		try{
			isListenering = false;
			isLoader = false;
			localExcutor.shutdown();
			netExcutor.shutdown();
			
			tasksStack.clear();
			runningTask.clear();
			taskQueue.clear();
		}catch(Exception e){
			e.printStackTrace();
			Logger.e(TAG, "exit error "+e.getMessage());
		}
	}
	
	/***
	 * 任务的核心调度
	 */
	protected void schedule() {
		if (runningTask.size() < loaderCounts && isLoader) {
			if (!tasksStack.isEmpty()) {
				int addNumber = loaderCounts - runningTask.size();
				addNumber = addNumber > tasksStack.size() ? tasksStack.size() : addNumber;
				for (int i = 0; i < addNumber; i++) {
					ImageLoaderTask task = tasksStack.pop();
					runningTask.add(task);
					netExcutor.execute(task);
					Logger.d(TAG, "start image Task:" + task.getUrl());
				}
				Logger.d(TAG, "runningTask.size() :" + runningTask.size());
			}else{
				Logger.d(TAG, "no net image task");
			}
		}
	}

	/***
	 * 任务完成
	 * @param imageLoaderTask
	 */
	protected void finishTask(ImageLoaderTask imageLoaderTask) {
		Logger.d(TAG, "end image Task:" + imageLoaderTask.getUrl());
		runningTask.remove(imageLoaderTask);
		schedule();
	}

	/**
	 * 加载本地视频
	 * @param filePath
	 * @param imageView
	 */
	public void displayLocal(Object operation, String filePath, ImageView imageView) {
		addTask(new ImageLoaderTask(operation, new File(filePath), imageView));
	}

	/**
	 * 加载本地视频
	 * @param filePath
	 * @param imageView
	 */
	public void displayLocal(Object operation, String filePath, ImageView imageView, ImageLoaderListener listener) {
		addTask(new ImageLoaderTask(operation, new File(filePath), imageView, listener));
	}

	/**
	 * 加载本地视频
	 * @param filePath
	 * @param imageView
	 */
	public void displayLocal(Object operation, String filePath, ImageView imageView, int defaultId, int failId) {
		addTask(new ImageLoaderTask(operation, new File(filePath), imageView, defaultId, failId));
	}

	/**
	 * 加载本地视频
	 * @param filePath
	 * @param imageView
	 */
	public void displayLocal(Object operation, String filePath, ImageView imageView, int defaultId, int failId, ImageLoaderListener listener) {
		addTask(new ImageLoaderTask(operation, new File(filePath), imageView, defaultId, failId, listener));
	}

	/**
	 * 加载网络资源
	 * @param url
	 * @param imageView
	 */
	public void display(Object operation, String url, ImageView imageView) {
		addTask(new ImageLoaderTask(operation, url, imageView));
	}

	/**
	 * 加载网络资源
	 * @param url
	 * @param imageView
	 */
	public void display(Object operation, String url, ImageView imageView, ImageLoaderListener listener) {
		addTask(new ImageLoaderTask(operation, url, imageView));
	}

	/**
	 * 加载网络资源
	 * @param url
	 * @param imageView
	 */
	public void display(Object operation, String url, ImageView imageView, int defaultId, int failId) {
		addTask(new ImageLoaderTask(operation, url, imageView, defaultId, failId));
	}

	/**
	 * 加载网络资源
	 * @param url
	 * @param imageView
	 */
	public void display(Object operation, String url, ImageView imageView, int defaultId, int failId, ImageLoaderListener listener) {
		addTask(new ImageLoaderTask(operation, url, imageView, defaultId, failId, listener));
	}

	/**
	 * 添加图片加载任务
	 * @param task
	 */
	protected void addTask(ImageLoaderTask task) {
		taskQueue.offer(task);
	}

	/***
	 * 下载监听
	 */
	protected void startAddTaskListener() {
		if (!isListenering) {
			isListenering = true;
			new Thread() {
				public void run() {
					Logger.d(TAG, "startAddTaskListener ===");
					while (isListenering) {
						try {
							//只有开始加载才进行图片刷新和下载
							ImageLoaderTask task = taskQueue.take();
							task.check();
							Logger.d(TAG, "add image Task:" + task.getUrl());
							if (!task.isNetTask()) {
								Logger.d(TAG, "find image cache:" + task.getUrl());
								//本地缓存过,直接刷新UI展示
								localExcutor.execute(task);
							} else {
								//需要下载放入堆栈中开发处理
								tasksStack.push(task);
								if (isLoader) {
									schedule();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							Logger.d(TAG, "addTaskListener error:" + e.getMessage());
						}
					}
				}
			}.start();
		}
	}
}
