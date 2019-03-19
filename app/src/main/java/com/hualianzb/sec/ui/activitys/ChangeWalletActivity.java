package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.adapters.AdapterWallets;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:切换账户
 */
public class ChangeWalletActivity extends BasicActivity {
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.lv_wallet)
    ListView lvWallet;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private AdapterWallets adapter;
    private List<RememberSEC> list;
    private Map<String, RememberSEC> map;
    private String address;
    private boolean isFromHome;
    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            isFromHome = bundle.getBoolean("isFromHome");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            isFromHome = savedInstanceState.getBoolean("isFromHome");
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_record);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        ivBackTop.setImageResource(R.drawable.icon_close_black);
        tvRight.setVisibility(View.GONE);
        adapter = new AdapterWallets(this, address, isFromHome);
        lvWallet.setAdapter(adapter);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (null != map && !map.isEmpty() && map.values().size() > 0) {
            for (RememberSEC rem : map.values()) {
                list.add(rem);
            }
            list = Util.ListSort(list);
            adapter.setList(list);
        } else {
            lvWallet.setVisibility(View.GONE);
        }
    }
}
