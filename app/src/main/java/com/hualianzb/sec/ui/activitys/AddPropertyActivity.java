package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.PropertyBean;
import com.hualianzb.sec.ui.adapters.AdapterProperty;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.views.AutoListView;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/8/19
 * auther:wangtianyun
 * describe:添加资产
 */
public class AddPropertyActivity extends BasicActivity {
    @BindView(R.id.lv_kind)
    AutoListView lvKind;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private AdapterProperty adapter;
    private ArrayList<PropertyBean> list;
    private StateBarUtil stateBarUtil;
    private Map<String, ArrayList<String>> mapKinds;
    private ArrayList<String> tokenKinds;
    private ArrayList<PropertyBean> listToken;
    private String address;


    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            tokenKinds = bundle.getStringArrayList("tokenKinds");
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            tokenKinds = savedInstanceState.getStringArrayList("tokenKinds");
            address = savedInstanceState.getString("address");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        tvTitle.setText("添加资产");
    }

    private void initData() {
        list = new ArrayList<>();
        listToken = new ArrayList<>();
        list = SECApplication.getInstance().getTokenKindList();
        mapKinds = new HashMap<>();
        mapKinds = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
        adapter = new AdapterProperty(this);
        lvKind.setAdapter(adapter);
//        for (PropertyBean bean : list) {
//            listToken.add(bean);
//        }
        adapter.setList(list, tokenKinds, address, mapKinds);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onBackPressed() {
        backToHome();
    }

    private void backToHome() {
        UiHelper.startHomaPageAc(this, address);
    }

    @OnClick(R.id.iv_back_top)
    public void onViewClicked() {
        backToHome();
    }
}
