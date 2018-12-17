package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DeviceUtil;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.web3j.crypto.WalletFile;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OutKeyStoreAcy extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_key_content)
    RoundTextView tvKeyContent;
    private Map<String, RememberEth> map;
    private RememberEth rememberEth;
    WalletFile walletFile;
    private String address;
    private String json;
    private StateBarUtil stateBarUtil;

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_out_keystore);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        DialogUtil.showTips(this, getString(R.string.out_keystore_tips));
        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText("导出keystore");
    }

    private void initData() {
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth bean : map.values()) {
            if (address.equals(bean.getAddress())) {
                rememberEth = bean;
                break;
            }
        }
//        walletFile = rememberEth.getWalletFile();
//        json = JSON.toJSONString(walletFile);
        json = rememberEth.getWalletFile();
        tvKeyContent.setText(json);
    }


    @OnClick(R.id.btn_copy)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                DeviceUtil.copy(this, json);
                ToastUtil.show(this, "已复制到剪切板");
                break;
        }
    }
}
