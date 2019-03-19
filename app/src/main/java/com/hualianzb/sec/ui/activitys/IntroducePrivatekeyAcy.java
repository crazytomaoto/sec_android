package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.text.Html;
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
 * describe:什么是Privatekey
 */
public class IntroducePrivatekeyAcy extends BasicActivity {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce_pk);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("What is a private key?");
        tvContent1.setText(Html.fromHtml(getString(R.string.private_1)));
        tvContent2.setText(Html.fromHtml(getString(R.string.private_2)));
        tvContent3.setText(Html.fromHtml(getString(R.string.private_3)));
        tvContent4.setText(Html.fromHtml(getString(R.string.private_4)));
    }
}
