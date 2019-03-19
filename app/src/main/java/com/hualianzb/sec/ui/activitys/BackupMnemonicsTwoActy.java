package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupMnemonicsTwoActy extends BasicActivity {
    @BindView(R.id.tv_my_mnemonics)
    TextView tvMyMnemonics;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.tv_firt)
    TextView tvFirt;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private String mnemonics;
    private Map<String, RememberSEC> map;
    private String address;
    private Dialog dialogNoShot;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
//            myPass = bundle.getString("myPass");
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
//            myPass = savedInstanceState.getString("myPass");
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankup_mn2);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initData();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("Backup phrase");
        String str = "The phrases is used to restore the  wallet or " + " <font color='#388EDA'>reset the wallet</font>" + "." + "Password,copy it to paper accurately,and store it in a safe place that only you know.";
        tvFirt.setText(Html.fromHtml(str));
        dialogNoShot = DialogUtil.dialogNoShot(this, (v, d) -> d.dismiss());
        dialogNoShot.show();
    }

    private void initData() {
//        tvTitle.setText("备份助记词");
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC rememberEth : map.values()) {
            if (rememberEth.getAddress().equals(address)) {
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
                UiHelper.startBackupMnemonicsActy3(BackupMnemonicsTwoActy.this, mnemonics);
                break;
        }
    }

}