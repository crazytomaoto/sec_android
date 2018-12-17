package com.hualianzb.sec.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.ui.adapters.ViewPageAdapter;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.ui.fragments.MnemonicImportFr;
import com.hualianzb.sec.ui.fragments.OfficialWalletFrg;
import com.hualianzb.sec.ui.fragments.PrivatKeyImportFrg;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.views.StillViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportWalletActivity extends BasicActivity {

    @BindView(R.id.viewPager)
    StillViewPager viewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_mn)
    TextView tvMn;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.re_mn)
    RelativeLayout reMn;
    @BindView(R.id.tv_officialW)
    TextView tvOfficialW;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.rl_official_w)
    RelativeLayout rlOfficialW;
    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.rl_private_key)
    RelativeLayout rlPrivateKey;
    private ViewPageAdapter viewPageAdapter;
    private List listF;
    private int index = 0;
    private boolean isChecked;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_wallet);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initData();
    }

    private void initData() {
        listF = new ArrayList<>();
        tvTitle.setText("导入钱包");
        listF.add(new MnemonicImportFr());
        listF.add(new OfficialWalletFrg());
        listF.add(new PrivatKeyImportFrg());
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), listF);
        viewPager.setAdapter(viewPageAdapter);
    }

    @OnClick({R.id.re_mn, R.id.rl_official_w, R.id.rl_private_key})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_mn:
                viewPager.setCurrentItem(0);
                index = 0;
                resetView(index);
                break;
            case R.id.rl_official_w:
                viewPager.setCurrentItem(1);
                index = 1;
                resetView(index);
                break;
            case R.id.rl_private_key:
                viewPager.setCurrentItem(2);
                index = 2;
                resetView(index);
                break;
        }
    }

    private void resetView(int nowIndex) {
        initView();
        switch (nowIndex) {
            case 0:
                tvMn.setTextColor(getResources().getColor(R.color.text_yellow));
                line1.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvOfficialW.setTextColor(getResources().getColor(R.color.text_yellow));
                line2.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvPrivateKey.setTextColor(getResources().getColor(R.color.text_yellow));
                line3.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView() {
        tvMn.setTextColor(getResources().getColor(R.color.text_gray));
        tvOfficialW.setTextColor(getResources().getColor(R.color.text_gray));
        tvPrivateKey.setTextColor(getResources().getColor(R.color.text_gray));
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        line3.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (resultCode) {
                case GlobalMessageType.RequestCode.FromMnFragment:
                    isChecked = data.getBooleanExtra("iChecked", false);
//                    checkBoxListener.setState(isChecked);
                    break;
                case GlobalMessageType.RequestCode.FromOfficialFragment:
                    isChecked = data.getBooleanExtra("iChecked", false);
//                    checkBoxListener.setState(isChecked);
                    break;
                case GlobalMessageType.RequestCode.FromPrivatKey:
                    isChecked = data.getBooleanExtra("iChecked", false);
//                    checkBoxListener.setState(isChecked);
                    break;
            }
        }
    }
}
