package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.adapters.AdapterTradeRecordAll;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.views.PullDownHeader;
import com.hualianzb.sec.views.PullUpFooter;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易记录//所有币种
 */
public class TransactionRecordActy extends BasicActivity {
    @BindView(R.id.lv_record)
    ListView lv;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.line_wallet)
    RelativeLayout lineWallet;
    @BindView(R.id.tv_wallet_name)
    TextView tvWalletName;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private AdapterTradeRecordAll adapter;
    private String address;
    private String title = "SEC";
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> lastListCache;//本地缓存的数据
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listChain;
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listPoor;
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listGet;//获取到的数据
    private Map<String, RememberSEC> map;
    private RememberSEC walletBean;
    private Dialog dialogLoading;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 002:
                    dialogLoading.dismiss();
                    break;
                case 003:
                    String resultData = (String) msg.obj;
                    SecTransactionBean secTransactionBean = JsonUtil.parseJson(resultData, SecTransactionBean.class);
                    if (null != secTransactionBean) {
                        SecTransactionBean.ResultBean resultBean = secTransactionBean.getResult();
                        if (null != resultBean) {
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("1")) {
                                listChain = resultBean.getResultInChain();
                                listPoor = resultBean.getResultInPool();
                                listGet.addAll(listChain);
                                listGet.addAll(listPoor);
                                setDataResult();
                                //
                            } else {
                                dialogLoading.dismiss();
                                refreshLayout.finishRefresh();
                                lv.setVisibility(View.GONE);
                                llNone.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        setDataResult();
                    }
                    break;
            }

        }
    };

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tride_record);
        SECApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("SEC Receipt");
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.loading));
        adapter = new AdapterTradeRecordAll(this);
        lv.setAdapter(adapter);
        listChain = new ArrayList<>();
        listPoor = new ArrayList<>();
        listGet = new ArrayList<>();
        lastListCache = new ArrayList<>();
        refreshLayout.setRefreshHeader(new PullDownHeader(this));
        refreshLayout.setRefreshFooter(new PullUpFooter(this));
//        refreshLayout.autoRefresh();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC remembe : map.values()) {
            if (remembe.isNow()) {
                walletBean = remembe;
                address = remembe.getAddress();
                break;
            }
        }
        tvWalletName.setText(walletBean.getWalletName());
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            clearLList();
            getData(address);
        });
        //上拉加载
        refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshLayout.finishLoadmore(2000));
        lv.setOnItemClickListener((parent, view, position, id) -> UiHelper.startTransaAllActivity(TransactionRecordActy.this, lastListCache.get(position), address));
    }

    private void secRecordRequest(String nowAddress) {
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> list = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_getTransactions");
        bean.setParams(list);
        list.add(nowAddress.substring(2));//address
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.secTestUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("web3", "result" + result);
                        Message message = new Message();
                        message.what = 003;
                        message.obj = result;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("web3", ex.toString());
                        handler.sendEmptyMessage(002);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        handler.sendEmptyMessage(002);
                    }

                    @Override
                    public void onFinished() {
                        Log.e("web3", "onFinished");
                    }
                });

            }
        }
        ) {
        }.start();
    }

    @OnClick({R.id.tv_change_wallet, R.id.iv_back_top})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_change_wallet:
                UiHelper.startChangeWalletRecordActyResult(this, address, false);
                break;
            case R.id.iv_back_top:
                goBacktoMyPage();
                break;
        }
    }

    private void goBacktoMyPage() {
        UiHelper.goBackHomaPageAc(this, address, true);
    }

    @Override
    public void onBackPressed() {
        goBacktoMyPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        clearLList();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC remembe : map.values()) {
            if (remembe.isNow()) {
                walletBean = remembe;
                address = remembe.getAddress();
                break;
            }
        }
        tvWalletName.setText(walletBean.getWalletName());
        lineWallet.setLayoutParams(new RelativeLayout.LayoutParams(25 * (walletBean.getWalletName().length()), 4));
        lineWallet.setBackgroundColor(getResources().getColor(R.color.text_green_increase));
        getData(address);
    }

    private void clearLList() {
        listChain.clear();
        listPoor.clear();
        listGet.clear();
    }

    private void getData(String address) {
        dialogLoading.show();
        clearLList();
        lastListCache = PlatformConfig.getList(this, address + "ALL" + title);
        secRecordRequest(address);
    }

    private void setDataResult() {
        //清除收款方pengding状态的记录
        if (null == listGet || listGet.size() == 0) {
            for (SecTransactionBean.ResultBean.ResultInChainBeanOrPool bean : listGet) {
                if (bean.getTxReceiptStatus().equals("pending") && bean.getTxTo().equals(address.substring(2))) {
                    listGet.remove(bean);
                }
            }
        }
        if (null == listGet || listGet.size() == 0) {
            if (null == lastListCache || lastListCache.size() == 0) {
                dialogLoading.dismiss();
                refreshLayout.finishRefresh();
                lv.setVisibility(View.GONE);
                llNone.setVisibility(View.VISIBLE);
            } else {
                adapter.setData(lastListCache, address);
                dialogLoading.dismiss();
            }
        } else {
            adapter.setData(listGet, address);
            if (null == lastListCache || listGet.size() > lastListCache.size()) {
                lastListCache = new ArrayList<>();
                TextView textView = (dialogLoading.findViewById(R.id.tv_content));
                textView.setText((listGet.size() - lastListCache.size()) + " data have been updated…");
            }
            PlatformConfig.putList(address + "ALL" + title, listGet);
            lastListCache.clear();
            lastListCache.addAll(listGet);
            dialogLoading.dismiss();
            refreshLayout.finishRefresh();
            lv.setVisibility(View.VISIBLE);
            llNone.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                if (null != data) {
                    String nowAddress = data.getStringExtra("nowAddress");
                    if (!StringUtils.isEmpty(nowAddress)) {
                        if (!nowAddress.equals(address)) {
                            clearLList();
                            for (RememberSEC remembe : map.values()) {
                                if (remembe.isNow()) {
                                    walletBean = remembe;
                                    address = remembe.getAddress();
                                    break;
                                }
                            }
                            tvWalletName.setText(walletBean.getWalletName());
                            getData(address);
                        }
                    }
                }
                break;
        }
    }
}
