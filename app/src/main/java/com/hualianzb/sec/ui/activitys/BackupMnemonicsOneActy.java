package com.hualianzb.sec.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备份钱包
 */
public class BackupMnemonicsOneActy extends BasicActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 0x124;
    @BindView(R.id.tv_third)
    TextView tvThird;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    private String myPass;
    private String address;
    private RememberSEC rememberEth;
    private Map<String, RememberSEC> map;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            rememberEth = (RememberSEC) bundle.getSerializable("rememberEth");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            rememberEth = (RememberSEC) savedInstanceState.getSerializable("rememberEth");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankup_mn);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        String str = "Backup wallet,export phrase and  copy them to a " + " <font color='#388EDA'>safe place</font>" + ".";
        tvThird.setText(Html.fromHtml(str));
        tvRight.setText("Skip");
        tvTheme.setText("Backup Wallet");
        ivBackTop.setVisibility(View.GONE);
        initData();
    }

    private void initData() {
        address = rememberEth.getAddress();
    }

    @OnClick({R.id.btn_bank_up, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bank_up:
                UiHelper.startCheckPassActivity(this, rememberEth.getPass(), 006);
                break;
            case R.id.tv_right:
                UiHelper.startHomaPageAc(this, address);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 006://
                if (resultCode == 1) {
                    if (null != data) {
                        boolean isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            UiHelper.startBackupMnemonicsActy2(this, address);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
