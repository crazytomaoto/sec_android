package com.hualianzb.sec.ui.basic;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hysd.android.platform_huanuo.base.ui.BaseFragment;

import me.leefeng.promptlibrary.PromptDialog;

public abstract class BasicFragment extends BaseFragment implements View.OnClickListener {
    private int positions;
    public static PromptDialog promptDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promptDialog = new PromptDialog((Activity) getDialogContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 消息处理
     */
    protected void handleStateMessage(Message message) {
        switch (message.what) {
            case 0:
                break;

            default:
                break;
        }
    }

    /**
     * logic
     */
    protected abstract void initLogics();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(getActivity());
    }

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
