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
 * describe:什么是助记词
 */
public class IntroduceMnActy extends BasicActivity {
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
        setContentView(R.layout.introduce_mn);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("What is phrase?");
        tvContent1.setText(Html.fromHtml(getString(R.string.mmn_1)));
        tvContent2.setText(Html.fromHtml(getString(R.string.mmn_2)));
        tvContent3.setText(Html.fromHtml(getString(R.string.mmn_3)));
        tvContent4.setText(Html.fromHtml(getString(R.string.mmn_4)));
        tvContent5.setText(Html.fromHtml(getString(R.string.mmn_5)));
        tvContent6.setText(Html.fromHtml(getString(R.string.mmn_6)));
    }
}
