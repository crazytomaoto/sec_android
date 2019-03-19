package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.ui.basic.BasicActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/9/26
 * auther:wangtianyun
 * describe:什么是keystore
 */
public class IntroduceKeystoreAcy extends BasicActivity {
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_content3)
    TextView tvContent3;
    @BindView(R.id.tv_content4)
    TextView tvContent4;
    @BindView(R.id.tv_content5)
    TextView tvContent5;
    @BindView(R.id.tv_content6)
    TextView tvContent6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce_keystore);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("What is a keystore?");
        tvContent1.setText(getString(R.string.keystore_1));
        tvContent2.setText(getString(R.string.keystore_2));
        tvContent3.setText(getString(R.string.keystore_3));
        tvContent4.setText(getString(R.string.keystore_4));
        tvContent5.setText(getString(R.string.keystore_5));
        tvContent6.setText(getString(R.string.keystore_6));
//        tvContent1.setText(getString(R.string.keystore_1));
    }
}
