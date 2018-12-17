package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hysd.android.platform_huanuo.utils.StringUtils;

/**
 * Date:2018/10/18
 * auther:wangtianyun
 * describe:网站数据
 */
public class WebActivity extends BasicActivity {
    private WebView web;
    private String url;
    private StateBarUtil stateBarUtil;

    @Override
    protected void getIntentForBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            url = savedInstanceState.getString("url");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
        initData();
    }

    private void initView() {
        web = findViewById(R.id.web);
    }

    private void initData() {
        WebSettings webSettings = web.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (StringUtils.isNotEmpty(url)) {
            web.loadUrl(RequestHost.webUrl + url);//加载url
        }
    }
}
