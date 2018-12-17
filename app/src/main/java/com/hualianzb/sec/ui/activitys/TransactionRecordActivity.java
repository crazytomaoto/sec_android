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
import com.hualianzb.sec.models.JsonRpcBean;
import com.hualianzb.sec.models.RecordTimeBean;
import com.hualianzb.sec.models.RequestHasParams;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.models.TransRecorderBeanOne;
import com.hualianzb.sec.models.TransactionByHashBean;
import com.hualianzb.sec.ui.adapters.AdapterTradeRecord2;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
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
 * describe:资产详情--交易记录-----非ETH
 */
public class TransactionRecordActivity extends BasicActivity {
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
    private AdapterTradeRecord2 adapter2;
    private List<TransRecorderBeanOne.ResultBean> list1;
    private List<TransRecorderBeanOne.ResultBean> list3;
    private List<TransactionByHashBean.ResultBean> list2;
    private List<String> listDate;
    private List<RecordTimeBean> listTime;
    private AllTransBean.ResultBean lastBean;
    private List<AllTransBean.ResultBean> lastList;
    private List<AllTransBean.ResultBean> lastListCache;
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
                    TransRecorderBeanOne beanOne = JsonUtil.parseJson(resultData, TransRecorderBeanOne.class);
                    list1 = beanOne.getResult();
                    if (null == list1 || list1.size() == 0) {
                        lvRecord.setVisibility(View.GONE);
                        ll_none.setVisibility(View.VISIBLE);
//                        hud.dismiss();
                    } else {
                        lvRecord.setVisibility(View.VISIBLE);
                        ll_none.setVisibility(View.GONE);
//                        adapter2.setData(list, list2);
                        handler.sendEmptyMessage(004);
                    }
                    break;
                case 004:
                    try {
                        if (list1 != null && list1.size() > 0) {
                            for (int i = 0; i < list1.size(); i++) {
                                getData2(list1.get(i).getTransactionHash(), i);//拿块的信息
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 005:
                    refreshLayout.finishRefresh();
                    hud.dismiss();
                    if (null != lastList && lastList.size() > 0) {
                        if (null != lastListCache && lastListCache.size() > 0) {
                            if (lastList.size() > lastListCache.size()) {
                                int num = lastList.size() - lastListCache.size();
                                lastListCache = Util.ListSortRecord(lastList);
                                PlatformConfig.putList(address + "##" + title, lastListCache);
                                adapter2.setData(lastListCache);
                                DialogUtil.showMoreNumberDialog(TransactionRecordActivity.this, num + "");
                            }
                        } else {
                            if (lastList.size() > 0) {
                                lastListCache = Util.ListSortRecord(lastList);
                                PlatformConfig.putList(address + "##" + title, lastListCache);
                                adapter2.setData(lastListCache);
                                DialogUtil.showMoreNumberDialog(TransactionRecordActivity.this, lastListCache.size() + "");
                            }
                        }
                    } else {
                        lvRecord.setVisibility(View.GONE);
                        ll_none.setVisibility(View.VISIBLE);
                    }
                    break;
            }

        }
    };

    private void getTimes(String hash) {
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> list = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("eth_getBlockByHash");
        bean.setParams(list);
        list.add(hash);
        list.add(false);
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.url);
        params.setAsJsonContent(true);
        params.setBodyContent(json);

        new Thread(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("web3", "result-eth_getblockByHash+\n" + result);

                        if (!StringUtils.isEmpty(result)) {
                            RecordTimeBean recordTimeBean = JSON.parseObject(result, RecordTimeBean.class);
                            if (null != recordTimeBean) {
                                listTime.add(recordTimeBean);
                            }
                        }

                        if (listTime.size() == list2.size()) {
                            for (RecordTimeBean recordTimeBean : listTime) {
                                for (TransactionByHashBean.ResultBean bean2 : list2) {
                                    if (recordTimeBean.getResult().getHash().equals(bean2.getBlockHash())) {
                                        lastBean = new AllTransBean.ResultBean();
                                        lastBean.setTimeStamp(recordTimeBean.getResult().getTimestamp());
                                        lastBean.setGasUsed(recordTimeBean.getResult().getGasUsed());
                                        lastBean.setBlockNumber(bean2.getBlockNumber());
                                        lastBean.setBlockHash(bean2.getBlockHash());
                                        lastBean.setHash(bean2.getHash());
                                        lastBean.setNonce(bean2.getNonce());
                                        lastBean.setTransactionIndex(bean2.getTransactionIndex());
                                        lastBean.setFrom(bean2.getFrom());
                                        lastBean.setTo(bean2.getTo());
                                        lastBean.setValue(bean2.getValue());
                                        lastBean.setGas(bean2.getGas());
                                        lastBean.setGasPrice(bean2.getGasPrice());
                                        lastBean.setInput(bean2.getInput());
                                        lastBean.setKind(title);
                                        lastList.add(lastBean);
                                    }
                                }
                            }

                            Message message05 = new Message();
                            message05.what = 005;
                            handler.sendMessage(message05);
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
        })

        {
        }.start();
    }

    private void getData2(String transactionHash, int index) {
        List<String> list = new ArrayList<>();
        list.add(transactionHash);
        RequestHasParams bean = new RequestHasParams();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("eth_getTransactionByHash");
        bean.setParams(list);
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.url);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("web3", "result-eth_getTransactionByHash+\n" + result);
                        if (!StringUtils.isEmpty(result)) {
                            TransactionByHashBean transactionByHashBean = JsonUtil.parseJson(result, TransactionByHashBean.class);
                            if (null != transactionByHashBean) {
                                TransactionByHashBean.ResultBean resultBean = transactionByHashBean.getResult();
                                if (null != resultBean) {
                                    String showAddress = "0x" + resultBean.getInput().substring(34, 74);
                                    if (resultBean.getFrom().equals(address) || showAddress.equals(address)) {
                                        list3.add(list1.get(index));
                                        list2.add(resultBean);
                                        getTimes(resultBean.getBlockHash());
                                    }
                                }
                            }
                        }
                        if (index == list1.size() - 1) {
                            if (list2.size() <= 0) {
                                lvRecord.setVisibility(View.GONE);
                                ll_none.setVisibility(View.VISIBLE);
//                                if (hud.isShowing()) {
//                                    hud.dismiss();
//                                }
                                refreshLayout.finishRefresh();
                                hud.dismiss();
                            } else {
                                lvRecord.setVisibility(View.VISIBLE);
                                ll_none.setVisibility(View.GONE);
                            }
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
        }) {
        }.start();
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            money = bundle.getString("money");
            Log.e("web", "myAdress+  \n" + address);
            title = bundle.getString("kind");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
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

        adapter2 = new AdapterTradeRecord2(this, address, title);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.autoRefresh();
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                clearList();
                if (null != lastListCache && lastListCache.size() > 0) {
                    lastListCache.clear();
                }
                lastListCache = PlatformConfig.getList(TransactionRecordActivity.this, address + "##" + title);
                getData();
            }
        });
        //上拉加载
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishLoadmore(2000);
            }
        });

    }

    private void clearList() {
        if (list1.size() > 0) {
            list1.clear();
        }
        if (list2.size() > 0) {
            list2.clear();
        }
        if (list3.size() > 0) {
            list3.clear();
        }
        if (listDate.size() > 0) {
            listDate.clear();
        }
        if (listTime.size() > 0) {
            listTime.clear();
        }
        if (lastList.size() > 0) {
            lastList.clear();
        }
        if (lastListCache.size() > 0) {
            lastListCache.clear();
        }
    }

    private void initData() {
        tvCan.setText(NumberUtils.round(Double.parseDouble(money), 8) + "");
        lvRecord.setAdapter(adapter2);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        listDate = new ArrayList<>();
        listTime = new ArrayList<>();
        lastList = new ArrayList<>();
        lastListCache = new ArrayList<>();
        hud.show();
        lastListCache = PlatformConfig.getList(this, address + "##" + title);
        if (null != lastListCache && lastListCache.size() > 0) {
            lvRecord.setVisibility(View.VISIBLE);
            ll_none.setVisibility(View.GONE);
            adapter2.setData(lastListCache);
            hud.dismiss();
        } else {
            lastListCache = new ArrayList<>();
        }
        if (!isNetworkAvailable(this)) {
            ToastUtil.show(this, R.string.net_work_err);
            return;
        }
//        getData();
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UiHelper.startTransaAllActivity(TransactionRecordActivity.this, lastListCache.get(position), address);
            }
        });
    }

    private void getData() {
        switch (title) {
            case "CEC":
                token = RequestHost.cecToken;
                notEthRecordRequest();
                break;
            case "SEC":
                token = RequestHost.secToken;
                notEthRecordRequest();
                break;
            case "INT":
                token = RequestHost.intToken;
                notEthRecordRequest();
                break;
            case "ETH":
                token = RequestHost.ethToken;
                ethRecordRequest();
                break;
        }
    }

    ;

    private void ethRecordRequest() {
        String path0 = "http://103.60.111.204:3000/eth/getTransactionJson/";
        String pathRequestEth = path0 + token;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams(RequestHost.url);
                params.setAsJsonContent(true);
                params.setBodyContent(pathRequestEth);
                x.http().get(params, new Callback.CommonCallback<String>() {
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

    private void notEthRecordRequest() {
        JsonRpcBean jsonRpcBean = new JsonRpcBean();
        jsonRpcBean.setId(1);
        jsonRpcBean.setMethod("eth_getLogs");
        jsonRpcBean.setJsonrpc("2.0");
        List<JsonRpcBean.ParamsBean> listP = new ArrayList<>();
        jsonRpcBean.setParams(listP);
        JsonRpcBean.ParamsBean data = new JsonRpcBean.ParamsBean();
        listP.add(data);
        List<String> listTopic = new ArrayList<>();
        data.setTopics(listTopic);
        data.setAddress(token);
        data.setFromBlock("0x1");
        listTopic.add("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
        listTopic.add(null);//from过滤
        listTopic.add(null);//to  过滤
//        data.setAddress(RequestHost.eceToken);
        String json = JSON.toJSONString(jsonRpcBean);
        Log.e("web3", "json++\n" + json);
//        hud.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams(RequestHost.url);
                params.setAsJsonContent(true);
                params.setBodyContent(json);
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

    @OnClick({R.id.ll_out, R.id.ll_in, R.id.iv_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_out://转账
                UiHelper.startTransferActivity(this, title, address, money);
                break;
            case R.id.ll_in://收款
                UiHelper.startMakeCodeActivity(this, address);
                break;
            case R.id.iv_back_top://收款
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
