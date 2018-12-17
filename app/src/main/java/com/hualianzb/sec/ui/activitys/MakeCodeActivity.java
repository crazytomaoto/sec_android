package com.hualianzb.sec.ui.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DeviceUtil;
import com.hualianzb.sec.utils.ImageUtils;
import com.hualianzb.sec.utils.QRUtils;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/8/17
 * auther:wangtianyun
 * describe:生成收款码
 */
public class MakeCodeActivity extends BasicActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.iv_code_pic)
    ImageView ivCodePic;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    Bitmap bitmap = null;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    private String address;
    private StateBarUtil stateBarUtil;
    private RememberEth bean;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makecode);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.setStatusBarFullTransparent();
        initView();
    }

    private void initView() {
        tvAddress.setText(address);
        Map<String, RememberEth> map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth rememberEth : map.values()) {
            if (address.equals(rememberEth.getAddress())) {
                bean = rememberEth;
                break;
            }
        }
        getAllTextMoney(edMoney.getText().toString().trim());

        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAllTextMoney(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_copy:
                DeviceUtil.copy(this, address);
                ToastUtil.show(this, "已复制到剪切板");
                break;
        }
    }

    private void getAllTextMoney(String money) {
        String nowMoney = money;
        if (StringUtils.isEmpty(money)) {
            nowMoney = "0.00";
        }
        bitmap = QRUtils.createQRCode(address.toString().trim() + "###" + nowMoney, 380, 380, BitmapFactory.decodeResource(getResources(), -1));
        ivAvater.setImageResource(ImageUtils.getWalletImage(bean.getWalletincon()));
        ivCodePic.setImageBitmap(bitmap);
    }
}
