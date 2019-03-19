package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hysd.android.platform_huanuo.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/18
 * auther:wangtianyun
 * describe:网站数据
 */
public class WebActivity extends BasicActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private String url;
    private String allPath;
    private Dialog dialogLoading;

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
        setContentView(R.layout.layout_web);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.loading));
        initData();
    }


    private void initData() {
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (StringUtils.isNotEmpty(url)) {
            allPath = RequestHost.webUrl + "0x" + url;
            webView.loadUrl(allPath);//加载url
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialogLoading.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialogLoading.dismiss();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
