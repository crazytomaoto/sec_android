package com.hysd.android.platform_huanuo.base.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.hysd.android.platform_huanuo.base.manager.MessageCenter;

/**
 * FileName    : BaseFragment.java
 * Description : Fragment基类
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-14 下午1:58:06
 **/
public abstract class BaseFragment extends Fragment {
	public static long ceachTime = 0;
	private Handler mhanlder;
	protected final String TAG = this.getClass().getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLogics();
		MessageCenter.getInstance().addHandler(getHandler());
	}

	/** 消息处理 */
	protected abstract void handleStateMessage(Message message);
	
	/** logic */
	protected abstract void initLogics() ;

	/** handler */
	protected Handler getHandler() {
		if (mhanlder == null) {
			mhanlder = new Handler() {
				public void handleMessage(Message msg) {
					handleStateMessage(msg);
				}
			};
		}
		return mhanlder;
	}

	protected void sendMessage(Message message) {
		getHandler().sendMessage(message);
	}

	protected void sendMessage(int what, Object obj) {
		Message message = new Message();
		message.what = what;
		message.obj = obj;
		getHandler().sendMessage(message);
	}

	protected void sendEmptyMessage(int what) {
		getHandler().sendEmptyMessage(what);
	}

	/** 统一的 Toast */
	protected void showToast(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	/** 统一的 Toast */
	protected void showToast(int resId) {
		showToast(getString(resId));
	}

	/** 统一的 ProgressDialog */
	protected Dialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(getDialogContext());
		dialog.setMessage(message);
		dialog.setCancelable(true);
		return dialog;
	}

	/** 统一的 ProgressDialog */
	protected Dialog showProgressDialog(int resId) {
		return showProgressDialog(getString(resId));
	}

	protected void dimssDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void onDestroy() {
		// 移除回收的handler
		MessageCenter.getInstance().removeHandler(getHandler());
		super.onDestroy();
	}

	/** 获取最上层的Activity负责 弹dialog,防止出现 can't add dialog 出错 */
	protected Context getDialogContext() {
		Activity activity = getActivity();
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		Log.d("Dialog", "context:" + activity);
		return activity;
	}

	
}
