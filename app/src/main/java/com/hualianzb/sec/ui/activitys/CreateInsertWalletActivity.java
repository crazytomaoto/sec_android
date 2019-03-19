package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateInsertWalletActivity extends BasicActivity {
    @BindView(R.id.ll_create)
    LinearLayout llCreate;
    @BindView(R.id.ll_import)
    LinearLayout llImport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_insert);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.gray_background).init();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.ll_create, R.id.ll_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_create:
                UiHelper.startActyCreateWalletActivity(this);
                break;
            case R.id.ll_import:
                UiHelper.startActyImportWalletActivity(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
