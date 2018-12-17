package com.hualianzb.sec.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceAgreementAct extends BasicActivity {
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.goon)
    TextView goon;
    @BindView(R.id.web_service)
    WebView webService;
    @BindView(R.id.ll_agreemt)
    LinearLayout ll_agreemt;

    private boolean agree;
    StateBarUtil stateBarUtil;
    private boolean isFromGuide;
    private int from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_agreement);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initData();
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            isFromGuide = bundle.getBoolean("isFromGuide");
            from = bundle.getInt("from");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            isFromGuide = savedInstanceState.getBoolean("isFromGuide");
            from = savedInstanceState.getInt("from");
        }
    }

    private void initData() {
        if (!isFromGuide) {
            ll_agreemt.setVisibility(View.GONE);
            goon.setVisibility(View.GONE);
        }

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agree = isChecked;
                if (agree) {
                    goon.setEnabled(true);
                    goon.setBackground(getResources().getDrawable(R.drawable.bg_shadow_my));
                    goon.setTextColor((getResources().getColor(R.color.white)));
                } else {
                    goon.setBackground(getResources().getDrawable(R.drawable.goon_gray));
                    goon.setEnabled(false);
                    goon.setTextColor(getResources().getColor(R.color.text_noChose));
                }
            }
        });
        webService.loadUrl("file:///android_asset/service.html");
    }

    @OnClick(R.id.goon)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.goon:
                PlatformConfig.setValue(Constant.SpConstant.AGREE, agree);
                if (isFromGuide) {
                    UiHelper.startActyCreateInsertWallet(this);
                } else {
                    Intent intent = new Intent();
                    if (from == GlobalMessageType.RequestCode.FromCreate) {
                        intent.setClass(this, CreateWalletActivity.class);
                    } else {
                        intent.setClass(this, ImportWalletActivity.class);
                    }
                    intent.putExtra("iChecked", agree);
                    setResult(from, intent);
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromGuide) {
            return;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        webService.destroy();
        super.onDestroy();
    }
}