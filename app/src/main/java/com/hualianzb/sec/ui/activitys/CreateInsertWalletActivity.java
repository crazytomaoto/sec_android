package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;

import com.flyco.roundview.RoundRelativeLayout;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateInsertWalletActivity extends BasicActivity {

    @BindView(R.id.rl_create)
    RoundRelativeLayout btnCreate;
    @BindView(R.id.rl_import)
    RoundRelativeLayout btnImport;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_insert);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.rl_create, R.id.rl_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_create:
                UiHelper.startActyCreateWalletActivity(this);
                break;
            case R.id.rl_import:
                UiHelper.startActyImportWalletActivity(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
