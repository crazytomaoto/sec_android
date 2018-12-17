package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.models.EthTransLogBean;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.adapters.AdapterTradeRecordAll;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.views.AutoListView;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.NumberUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:         SEC资产详情--交易记录
 */
public class TransactionRecordSecActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_can)
    TextView tvCan;
    @BindView(R.id.tv_can_about)
    TextView tvCanAbout;
    @BindView(R.id.tv_cold)
    TextView tvCold;
    @BindView(R.id.tv_cold_about)
    TextView tvColdAbout;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_change_about)
    TextView tvChangeAbout;
    @BindView(R.id.tv_key_content)
    TextView tvKeyContent;
    @BindView(R.id.lv_record)
    AutoListView lvRecord;
    @BindView(R.id.ll_none)
    LinearLayout ll_none;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private StateBarUtil stateBarUtil;
    private String token;
    private AdapterTradeRecordAll adapter;
    private List<AllTransBean.ResultBean> list1;//获取到的数据
    private List<AllTransBean.ResultBean> lastListCache;//本地缓存的数据


    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> lastListCache_sec;

    private String address;
    private String title, money;
    private KProgressHUD hud;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 002:
                    hud.dismiss();
                    break;
                case 003:
                    String resultData = (String) msg.obj;
                    SecTransactionBean secTransactionBean = JsonUtil.parseJson(resultData, SecTransactionBean.class);
                    if (null != secTransactionBean) {
                        SecTransactionBean.ResultBean resultBean = secTransactionBean.getResult();
                        if (null != resultBean) {
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("1")) {
                                lastListCache_sec = resultBean.getResultInChain();
                                if (null == lastListCache_sec || lastListCache_sec.size() <= 0) {
                                    hud.dismiss();
                                    refreshLayout.finishRefresh();
                                    lvRecord.setVisibility(View.GONE);
                                    ll_none.setVisibility(View.VISIBLE);
                                    return;
                                }
                                for (SecTransactionBean.ResultBean.ResultInChainBeanOrPool resultInChainBeanOrPool : lastListCache_sec) {
                                    AllTransBean.ResultBean lastBean = new AllTransBean.ResultBean();
                                    lastBean.setBlockNumber(resultInChainBeanOrPool.getBlockNumber() + "");
                                    lastBean.setBlockHash(resultInChainBeanOrPool.getBlockHash());
                                    lastBean.setTimeStamp(resultInChainBeanOrPool.getTimeStamp() + "");
                                    lastBean.setHash(resultInChainBeanOrPool.getTxHash() + "");
                                    lastBean.setNonce(resultInChainBeanOrPool.getNonce());
                                    lastBean.setTransactionIndex(resultInChainBeanOrPool.getTransactionIndex() + "");
                                    lastBean.setFrom(resultInChainBeanOrPool.getTxFrom());
                                    lastBean.setTo(resultInChainBeanOrPool.getTxTo());
                                    lastBean.setValue(resultInChainBeanOrPool.getValue() + "");
                                    lastBean.setGas(resultInChainBeanOrPool.getGasUsedByTxn());
                                    lastBean.setGasPrice(resultInChainBeanOrPool.getGasPrice());
                                    lastBean.setInput(resultInChainBeanOrPool.getInputData());
                                    lastBean.setGasUsed(resultInChainBeanOrPool.getCumulativeGasUsed() + "");
                                    lastBean.setKind("SEC");
//                                    lastBean.setKind("ETH");
                                    list1.add(lastBean);
                                }
                                refreshLayout.finishRefresh();
                                hud.dismiss();
                                if (null != list1 && list1.size() > 0) {
                                    if (lastListCache.size() > 0) {
                                        if (list1.size() > lastListCache.size()) {
                                            int num = list1.size() - lastListCache.size();
                                            lastListCache.clear();
                                            lastListCache = list1;
                                            PlatformConfig.putList(address + "##" + title, lastListCache);
                                            adapter.setData(lastListCache);
                                            DialogUtil.showMoreNumberDialog(TransactionRecordSecActivity.this, num + "");
                                        } else {
                                            adapter.setData(lastListCache);
                                        }
                                    } else {
                                        if (list1.size() > 0) {
                                            lastListCache.clear();
                                            lastListCache = list1;
                                            PlatformConfig.putList(address + "##" + title, lastListCache);
                                            adapter.setData(lastListCache);
                                            DialogUtil.showMoreNumberDialog(TransactionRecordSecActivity.this, list1.size() + "");
                                        }
                                    }
                                } else {
                                    lvRecord.setVisibility(View.GONE);
                                    ll_none.setVisibility(View.VISIBLE);
                                }
                            } else {
                                hud.dismiss();
                                refreshLayout.finishRefresh();
                                lvRecord.setVisibility(View.GONE);
                                ll_none.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    break;
//                    EthTransLogBean beanOne = JsonUtil.parseJson(resultData, EthTransLogBean.class);
//                    if (null == beanOne) {
//                        return;
//                    }
//                    String status = beanOne.getStatus();
//                    switch (status) {
//                        case "0":
//                            hud.dismiss();
//                            refreshLayout.finishRefresh();
//                            lvRecord.setVisibility(View.GONE);
//                            ll_none.setVisibility(View.VISIBLE);
//                            break;
//                        case "1":
//                            List<EthTransLogBean.ResultBean> listEth = beanOne.getResult();
//                            for (EthTransLogBean.ResultBean resultBean : listEth) {
//                                AllTransBean.ResultBean lastBean = new AllTransBean.ResultBean();
//                                lastBean.setBlockNumber(resultBean.getBlockNumber());
//                                lastBean.setBlockHash(resultBean.getBlockHash());
//                                lastBean.setTimeStamp(resultBean.getTimeStamp());
//                                lastBean.setHash(resultBean.getHash());
//                                lastBean.setNonce(resultBean.getNonce());
//                                lastBean.setTransactionIndex(resultBean.getTransactionIndex());
//                                lastBean.setFrom(resultBean.getFrom());
//                                lastBean.setTo(resultBean.getTo());
//                                lastBean.setValue(resultBean.getValue());
//                                lastBean.setGas(resultBean.getGas());
//                                lastBean.setGasPrice(resultBean.getGasPrice());
//                                lastBean.setInput(resultBean.getInput());
//                                lastBean.setGasUsed(resultBean.getGasUsed());
//                                lastBean.setKind("ETH");
//                                list1.add(lastBean);
//                            }
//                            refreshLayout.finishRefresh();
//                            hud.dismiss();
//                            if (null != list1 && list1.size() > 0) {
//                                if (lastListCache.size() > 0) {
//                                    if (list1.size() > lastListCache.size()) {
//                                        int num = list1.size() - lastListCache.size();
//                                        lastListCache.clear();
//                                        lastListCache = list1;
//                                        PlatformConfig.putList(address + "##" + title, lastListCache);
//                                        adapter.setData(lastListCache);
//                                        DialogUtil.showMoreNumberDialog(TransactionRecordSecActivity.this, num + "");
//                                    }
//                                } else {
//                                    if (list1.size() > 0) {
//                                        lastListCache.clear();
//                                        lastListCache = list1;
//                                        PlatformConfig.putList(address + "##" + title, lastListCache);
//                                        adapter.setData(lastListCache);
//                                        DialogUtil.showMoreNumberDialog(TransactionRecordSecActivity.this, list1.size() + "");
//                                    }
//                                }
//                            } else {
//                                lvRecord.setVisibility(View.GONE);
//                                ll_none.setVisibility(View.VISIBLE);
//                            }
//                            break;
//                    }
            }

        }
    };

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address").substring(2);
            money = bundle.getString("money");
            Log.e("web", "myAdress+  \n" + address);
            title = bundle.getString("kind");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address").substring(2);
            title = savedInstanceState.getString("kind");
            money = savedInstanceState.getString("money");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionrecord);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText(title);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  加载中  ")
                .setCancellable(true);
        list1 = new ArrayList<>();


        lastListCache_sec = new ArrayList<>();


        lastListCache = new ArrayList<>();
        adapter = new AdapterTradeRecordAll(this, address);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                clearList();
                getData();
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.autoRefresh();
        //上拉加载
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishLoadmore(2000);
            }
        });
    }

    private void getData() {
        token = RequestHost.ethToken;
//        ethRecordRequest();
        secRecordRequest();
    }

    private void initData() {
        tvCan.setText(NumberUtils.round(Double.parseDouble(money), 8) + "");
        lvRecord.setAdapter(adapter);
        lastListCache = PlatformConfig.getList(this, address + "##" + title);
        if (null != lastListCache && lastListCache.size() > 0) {
            lvRecord.setVisibility(View.VISIBLE);
            ll_none.setVisibility(View.GONE);
            adapter.setData(lastListCache);
            hud.dismiss();
        } else {
            lastListCache = new ArrayList<>();
        }
        if (!isNetworkAvailable(this)) {
            ToastUtil.show(this, R.string.net_work_err);
            return;
        }
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UiHelper.startTransaAllActivity(TransactionRecordSecActivity.this, lastListCache.get(position), address);
            }
        });
    }

    private void secRecordRequest() {
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> list = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_getTransactions");
        bean.setParams(list);
        list.add(address);//address
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
//    private void ethRecordRequest() {
//        String path0 = "http://103.60.111.204:3000/eth/getTransactionJson/";
//        String pathRequestEth = path0 + address;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RequestParams params = new RequestParams(pathRequestEth);
//                params.setAsJsonContent(true);
//                x.http().get(params, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.e("web3", "result" + result);
//                        Message message = new Message();
//                        message.what = 003;
//                        message.obj = result;
//                        handler.sendMessage(message);
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Log.e("web3", ex.toString());
//                        handler.sendEmptyMessage(002);
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                        handler.sendEmptyMessage(002);
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        Log.e("web3", "onFinished");
//                    }
//                });
//
//            }
//        }
//        ) {
//        }.start();
//
//    }

    private void clearList() {
        if (list1.size() > 0) {
            list1.clear();
        }
//        if (lastListCache.size() > 0) {
//            lastListCache.clear();
//        }
    }

    @OnClick({R.id.ll_out, R.id.ll_in, R.id.iv_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_out://转账
                UiHelper.startTransferActivity(this, title, address, money);
                break;
            case R.id.ll_in://收款
                UiHelper.startMakeCodeActivity(this, address);
                break;
            case R.id.iv_back_top:
                UiHelper.goBackHomaPageAc(this, address, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiHelper.goBackHomaPageAc(this, address, false);
    }
}
