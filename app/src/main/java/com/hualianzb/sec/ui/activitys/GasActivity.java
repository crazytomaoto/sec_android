package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/8/18
 * auther:wangtianyun
 * describe:Gas费用
 */
public class GasActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);
        ButterKnife.bind(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        tvTitle.setVisibility(View.GONE);
    }
}
