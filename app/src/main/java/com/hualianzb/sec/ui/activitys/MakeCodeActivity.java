package com.hualianzb.sec.ui.activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.QRUtils;
import com.hualianzb.sec.utils.Util;

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
    @BindView(R.id.iv_code_pic)
    ImageView ivCodePic;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    Bitmap bitmap = null;
    private String address;

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
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.gray_background).init();
        tvAddress.setText(address);
        setBitmap();
    }

    @OnClick({R.id.iv_back, R.id.tv_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_copy:
                Util.copy(this, address);
                finish();
                break;
        }
    }

    private void setBitmap() {
        bitmap = QRUtils.createQRCode(address.trim() + "###" + "0", 460, 460, null);
        ivCodePic.setImageBitmap(bitmap);
    }
}
