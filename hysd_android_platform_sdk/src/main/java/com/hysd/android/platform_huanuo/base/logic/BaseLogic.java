package com.hysd.android.platform_huanuo.base.logic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hysd.android.platform_huanuo.base.manager.ExecutorFactory;
import com.hysd.android.platform_huanuo.base.manager.MessageCenter;

/**
 * FileName    : BaseLogic.java
 * Description : 所有Logic的基类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : wangtianyun
 * @version    : 1.0
 * Create Date : 2015-12-8 上午10:51:46
 **/
public class BaseLogic implements ILogic {

	protected Context mcontext;
	protected final String TAG = this.getClass().getName();

	public BaseLogic(Context context) {
		super();
		this.mcontext = context;
	}

	@Override
	public final void addHandler(Handler handler) {
		MessageCenter.getInstance().addHandler(handler);
	}

	@Override
	public final void romoveHander(Handler handler) {
		MessageCenter.getInstance().removeHandler(handler);
	}

	@Override
	public final void invokeAsync(Runnable runnable) {
		ExecutorFactory.executeLogic(runnable);
	}

	public void sendMessage(int what, Object obj) {
		MessageCenter.getInstance().sendMessage(what, obj);
	}

	public void sendMessage(Message message) {
		MessageCenter.getInstance().sendMessage(message);
	}

	public void sendEmptyMesage(int what) {
		MessageCenter.getInstance().sendEmptyMesage(what);
	}

	public void removeMessage(int what){
		MessageCenter.getInstance().removeMessage(what);
	}

	public void sendEmptyMessageDelay(int what, long delayTime){
		MessageCenter.getInstance().sendEmptyMessageDelay(what, delayTime);
	}
}
