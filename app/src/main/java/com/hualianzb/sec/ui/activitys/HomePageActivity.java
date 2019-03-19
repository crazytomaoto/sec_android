package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.UpDateBean;
import com.hualianzb.sec.ui.adapters.ViewPageAdapter;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.ui.fragments.MyFragment;
import com.hualianzb.sec.ui.fragments.PropertyFragment;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.views.StillViewPager;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.ActivityUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageActivity extends BasicActivity implements PropertyFragment.MyListener,
        MyFragment.CheckUpdateLinster {
    @BindView(R.id.ll_property)
    LinearLayout llProperty;
    @BindView(R.id.ll_my)
    LinearLayout llMy;
    @BindView(R.id.viewPager)
    StillViewPager viewPager;
    @BindView(R.id.ll_discovery)
    LinearLayout llDiscovery;
    private long exitTime = 0;
    private ViewPageAdapter viewPageAdapter;
    private List listF;
    private String address;

    public String getAddress() {
        return address;
    }

    private Map<String, RememberSEC> map;
    private RememberSEC rememberSec;
    UpDateBean upDateBean;
    private Dialog upDateDialog;
    private int updateStatusInt;
    private Context context;

    public int getUpdateStatusInt() {
        return updateStatusInt;
    }

    public void setUpdateStatusInt(int updateStatusInt) {
        this.updateStatusInt = updateStatusInt;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_homepage);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        context = this;
        initView();
        initData();
        checkUpdate(false);
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case 0x0000100:
                String urlExe = (String) message.obj;
                Uri uri = Uri.parse(urlExe);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
        }
    }

    private void initData() {
        listF = new ArrayList<>();
        map = new HashMap<>();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC rememberSec : map.values()) {
            if (rememberSec.isNow() == true) {
                address = rememberSec.getAddress();
                this.rememberSec = rememberSec;
                break;
            }
        }
//        listF.add(new DiscoveryFragment());
        listF.add(new PropertyFragment());
        listF.add(new MyFragment());
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), listF);
        viewPager.setAdapter(viewPageAdapter);
    }

    @OnClick({R.id.ll_discovery, R.id.ll_property, R.id.ll_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.ll_discovery:
//                viewPager.setCurrentItem(0);
//                tabSelected(llDiscovery);
//                break;
            case R.id.ll_property:
                viewPager.setCurrentItem(0);
                tabSelected(llProperty);
                break;
            case R.id.ll_my:
                viewPager.setCurrentItem(1);
                tabSelected(llMy);
                break;
            default:
                break;
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        llDiscovery.setSelected(false);
        llProperty.setSelected(false);
        llMy.setSelected(false);
        linearLayout.setSelected(true);
        if (viewPager.getCurrentItem() == 0) {
            ImmersionBar.with(this).statusBarColor(R.color.gray_background).init();
        } else {
            ImmersionBar.with(this).statusBarColor(R.color.gray_background_home).init();
        }
    }

    @Override
    public void sendContent(String info) {
        if (info.equals("go")) {
//            drawerLayout.openDrawer(Gravity.END);
        }
    }

    private void initView() {
        tabSelected(llProperty);
        upDateDialog = DialogUtil.upDateDialog(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            moveTaskToBack(true);
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(HomePageActivity.this, "Press the exit procedure again ", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                SECApplication.getInstance().exit();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("address", address);
//        savedInstanceState.putBoolean("isBackMy", isBackMy);
        super.onSaveInstanceState(savedInstanceState);
    }

    //检查更新
    private void checkUpdate(boolean isFromMy) {
        RequestParams params = new RequestParams(RequestHost.url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtils.isEmpty(result)) {
                    upDateBean = JSON.parseObject(result, UpDateBean.class);
                    UpDateBean.AndroidBean androidBean = upDateBean.getAndroid();
                    Log.e("web", JSON.toJSONString(upDateBean));
                    String discrib = androidBean.getDescrib();
                    String urlExe = androidBean.getLink();
                    String version = androidBean.getVersion();
                    String updateStatus = androidBean.getStatus();

                    setUpdateStatusInt(Integer.parseInt(updateStatus));


                    //初始化布局控件
                    TextView tv_version = upDateDialog.findViewById(R.id.tv_version);
                    TextView tv_describ = upDateDialog.findViewById(R.id.tv_describ);
                    TextView tv_cancel = upDateDialog.findViewById(R.id.tv_cancel);
                    TextView tv_sure = upDateDialog.findViewById(R.id.tv_sure);
                    tv_describ.setText(discrib);
                    tv_sure.setVisibility(View.VISIBLE);
                    tv_cancel.setOnClickListener(v -> upDateDialog.dismiss());
                    tv_version.setText(version);

                    switch (updateStatus) {
                        case "1"://no upgrade
                            tv_describ.setText("no upgrade");
                            tv_cancel.setVisibility(View.GONE);
                            tv_sure.setText("Go It");
                            if (isFromMy == true) {
                                upDateDialog.show();
                            }
                            break;
                        case "2":// upgrade not compulsively
                            tv_cancel.setVisibility(View.VISIBLE);
                            tv_describ.setText(discrib);
                            tv_sure.setText("Update");
                            upDateDialog.show();
                            break;
                        case "3":// upgrade compulsively
                            if (!version.equals(ActivityUtil.getVersionName(HomePageActivity.this))) {
                                tv_cancel.setVisibility(View.GONE);
                                tv_sure.setText("Update");
                                upDateDialog.show();
                            } else {
                                tv_describ.setText("no upgrade");
                                tv_cancel.setVisibility(View.GONE);
                                tv_sure.setText("Go It");
                                if (isFromMy == true) {
                                    upDateDialog.show();
                                }
                            }
                            break;
                    }
                    tv_sure.setOnClickListener(v -> {
                        switch (updateStatus) {
                            case "1"://  no update
                                upDateDialog.dismiss();
                                break;
                            case "2":// upgrade not compulsively
                                sendMessage(0x0000100, urlExe);
                                break;
                            case "3":// upgrade compulsively
                                if (!version.equals(ActivityUtil.getVersionName(HomePageActivity.this))) {
                                    sendMessage(0x0000100, urlExe);
                                } else {
                                    upDateDialog.dismiss();
                                }
                                break;
                        }

                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("web3", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Log.e("web3", "onFinished");
            }
        });
    }

    @Override
    public void check() {
        checkUpdate(true);
    }
}

