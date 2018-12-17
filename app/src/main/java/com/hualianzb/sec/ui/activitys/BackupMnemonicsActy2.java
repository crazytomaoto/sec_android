package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupMnemonicsActy2 extends BasicActivity {
    String myPass;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_my_mnemonics)
    TextView tvMyMnemonics;
    @BindView(R.id.btn_next)
    Button btnNext;
    private String mnemonics;
    private Map<String, RememberEth> map;
    StateBarUtil stateBarUtil;
    private String address;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            myPass = bundle.getString("myPass");
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            myPass = savedInstanceState.getString("myPass");
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankup_mn2);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initData();
    }

    private void initView() {
        DialogUtil.showTips(this, getString(R.string.zhujici_dialog_tips));
    }

    private void initData() {
        tvTitle.setText("备份助记词");
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth rememberEth : map.values()) {
            if (rememberEth.getPass().equals(myPass) && rememberEth.getAddress().equals(address)) {
                mnemonics = rememberEth.getMnemonics();
                tvMyMnemonics.setText(mnemonics);
                break;
            }
        }
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                UiHelper.startBackupMnemonicsActy3(this, mnemonics);
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }
}