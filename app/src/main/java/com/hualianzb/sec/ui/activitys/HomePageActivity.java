package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.adapters.ViewPageAdapter;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.ui.fragments.MyFragment;
import com.hualianzb.sec.ui.fragments.PropertyFragment;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.views.StillViewPager;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.web3j.protocol.Web3j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageActivity extends BasicActivity implements PropertyFragment.MyListener {
    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.iv_property)
    ImageView ivProperty;
    @BindView(R.id.tv_property)
    TextView tvProperty;
    @BindView(R.id.ll_property_small)
    LinearLayout llPropertySmall;
    @BindView(R.id.ll_property)
    LinearLayout llProperty;
    @BindView(R.id.iv_my)
    ImageView ivMy;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.ll_my_small)
    LinearLayout llMySmall;
    @BindView(R.id.ll_my)
    LinearLayout llMy;
    @BindView(R.id.viewPager)
    StillViewPager viewPager;
    @BindView(R.id.rl_all)
    RelativeLayout rlAll;
    private long exitTime = 0;
    //    @BindView(R.id.nav_view)
//    NavigationView navView;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
    private ViewPageAdapter viewPageAdapter;
    private List listF;
    private String address;
    private int index = 0;
    private Web3j web3;
    private StateBarUtil stateBarUtil;

    public String getAddress() {
        return address;
    }

    private Map<String, RememberEth> map;
    private boolean isBackMy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题
        setContentView(R.layout.ac_homepage);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        StatusBarCompat.setTranslucent(getWindow(), true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case 0000000:
//                drawerLayout.openDrawer(Gravity.END);
                break;
        }
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            isBackMy = bundle.getBoolean("isBackMy");
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            isBackMy = savedInstanceState.getBoolean("isBackMy");
            address = savedInstanceState.getString("address");
        }
    }

    private void initData() {
        listF = new ArrayList<>();
        map = new HashMap<>();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth rememberEth : map.values()) {
            if (rememberEth.isNow() == true) {
                address = rememberEth.getAddress();
                break;
            }
        }

        listF.add(new PropertyFragment(isBackMy));
        listF.add(new MyFragment());
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), listF);
        viewPager.setAdapter(viewPageAdapter);
        //开启手势滑动打开侧滑菜单栏，如果要关闭手势滑动，将后面的UNLOCKED替换成LOCKED_CLOSED 即可
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//
//        navView.setNavigationItemSelectedListener(item -> {
//            drawerLayout.closeDrawer(Gravity.END);
//            return true;
//        });

        if (isBackMy) {
            setSelected();
            stateBarUtil.changeStatusBarTextColor(false);
        } else {
            stateBarUtil.changeStatusBarTextColor(true);
        }
    }

    @OnClick({R.id.ll_property, R.id.ll_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_property:
                index = 0;
                viewPager.setCurrentItem(index);
                reInitView();
                ivProperty.setImageResource(R.drawable.icon_pro_yes);
                tvProperty.setTextColor(getResources().getColor(R.color.text_yellow));
                rlAll.setBackground(getResources().getDrawable(R.drawable.shape_white__10));
                stateBarUtil.changeStatusBarTextColor(true);
                break;
            case R.id.ll_my:
                index = 1;
                viewPager.setCurrentItem(index);
                reInitView();
                ivMy.setImageResource(R.drawable.me_yes);
                tvMy.setTextColor(getResources().getColor(R.color.text_yellow));
                rlAll.setBackground(getResources().getDrawable(R.drawable.bg_btn_golden_shadow));
                stateBarUtil.changeStatusBarTextColor(false);
                break;
        }
    }

    @Override
    public void sendContent(String info) {
        if (info.equals("go")) {
//            drawerLayout.openDrawer(Gravity.END);
        }
    }

    private void reInitView() {
        ivProperty.setImageResource(R.drawable.icon_pro_no);
        tvProperty.setTextColor(getResources().getColor(R.color.text_black_home));
        ivMy.setImageResource(R.drawable.me_no);
        tvMy.setTextColor(getResources().getColor(R.color.text_black_home));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void setSelected() {
        index = 1;
        viewPager.setCurrentItem(index);
        reInitView();
        ivMy.setImageResource(R.drawable.me_yes);
        tvMy.setTextColor(getResources().getColor(R.color.text_yellow));
        rlAll.setBackground(getResources().getDrawable(R.drawable.bg_btn_golden_shadow));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            moveTaskToBack(true);
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(HomePageActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                SECApplication.getInstance().exit();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void doSomeUI(boolean netMobile) {
        super.doSomeUI(netMobile);
        if (netMobile) {
            onResume();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("address", address);
        savedInstanceState.putBoolean("isBackMy", isBackMy);
        super.onSaveInstanceState(savedInstanceState);
    }

}

