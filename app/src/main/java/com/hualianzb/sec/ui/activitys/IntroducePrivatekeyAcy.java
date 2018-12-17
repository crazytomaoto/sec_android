package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/9/26
 * auther:wangtianyun
 * describe:什么是Privatekey
 */
public class IntroducePrivatekeyAcy extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce_pk);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();


    }

    private void initView() {
        tvTitle.setVisibility(View.GONE);
    }
}
