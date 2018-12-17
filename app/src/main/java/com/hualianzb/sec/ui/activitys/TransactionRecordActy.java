package com.hualianzb.sec.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.models.EthTransLogBean;
import com.hualianzb.sec.models.JsonRpcBean;
import com.hualianzb.sec.models.PropertyBean;
import com.hualianzb.sec.models.RecordTimeBean;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.models.RequestHasParams;
import com.hualianzb.sec.models.TagBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.models.TransRecorderBeanOne;
import com.hualianzb.sec.models.TransactionByHashBean;
import com.hualianzb.sec.ui.adapters.AdapterTradeRecordAll;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易记录
 */
public class TransactionRecordActy extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_rig_top)
    ImageView ivRight;
    @BindView(R.id.lv_record)
    ListView lv;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private KProgressHUD hud;
    private AdapterTradeRecordAll adapter;
    private List<TransRecorderBeanOne.ResultBean> list1, list11, list111;
    private List<TagBean> list2;
    TagBean tagBean;
    private List<RecordTimeBean> listTime;
    private String address;
    private StateBarUtil stateBarUtil;
    private String tokenAddress;
    private String tag;
    private List<PropertyBean> beanArrayList;
    private List<PropertyBean> allList;
    private List<AllTransBean.ResultBean> notEthList;
    private List<AllTransBean.ResultBean> ethList;
    private List<AllTransBean.ResultBean> dataList;
    private List<AllTransBean.ResultBean> lastListCache;

    @Override
    protected void handleStateMessage(Message msg) {
        switch (msg.what) {
            case 0x20:
                hud.dismiss();
                break;
            case 0x30:
                String getData30 = (String) msg.obj;
                TransRecorderBeanOne beanOne30 = JsonUtil.parseJson(getData30, TransRecorderBeanOne.class);
                list1 = beanOne30.getResult();
                sendMessage(0x31, list1);
                break;
            case 0x40:
                String getData40 = (String) msg.obj;
                TransRecorderBeanOne beanOne40 = JsonUtil.parseJson(getData40, TransRecorderBeanOne.class);
                list11 = beanOne40.getResult();
                sendMessage(0x41, list11);
                break;
            case 0x50:
                String getData50 = (String) msg.obj;
                TransRecorderBeanOne beanOne50 = JsonUtil.parseJson(getData50, TransRecorderBeanOne.class);
                list111 = beanOne50.getResult();
                sendMessage(0x51, list111);
                break;
            case 0x31:
                try {
                    if (list1 != null && list1.size() > 0) {
                        for (int i = 0; i < list1.size(); i++) {
                            getData2(list1, list1.get(i).getTransactionHash(), i, "CEC");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x41:
                try {
                    if (list11 != null && list11.size() > 0) {
                        for (int i = 0; i < list11.size(); i++) {
                            getData2(list11, list11.get(i).getTransactionHash(), i, "SEC");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x51:
                try {
                    if (list111 != null && list111.size() > 0) {
                        for (int i = 0; i < list111.size(); i++) {
                            getData2(list111, list111.get(i).getTransactionHash(), i, "INT");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 0x0067://非eth   time记录
                String resultData = (String) msg.obj;
                EthTransLogBean beanOne = JsonUtil.parseJson(resultData, EthTransLogBean.class);
                String status = beanOne.getStatus();
                switch (status) {
                    case "1":
                        List<EthTransLogBean.ResultBean> listEth = beanOne.getResult();
                        for (EthTransLogBean.ResultBean resultBean : listEth) {
                            AllTransBean.ResultBean lastBean = new AllTransBean.ResultBean();
                            lastBean.setBlockNumber(resultBean.getBlockNumber());
                            lastBean.setBlockHash(resultBean.getBlockHash());
                            lastBean.setTimeStamp(resultBean.getTimeStamp());
                            lastBean.setHash(resultBean.getHash());
                            lastBean.setNonce(resultBean.getNonce());
                            lastBean.setTransactionIndex(resultBean.getTransactionIndex());
                            lastBean.setFrom(resultBean.getFrom());
                            lastBean.setTo(resultBean.getTo());
                            lastBean.setValue(resultBean.getValue());
                            lastBean.setGas(resultBean.getGas());
                            lastBean.setGasPrice(resultBean.getGasPrice());
                            lastBean.setInput(resultBean.getInput());
                            lastBean.setGasUsed(resultBean.getGasUsed());
                            lastBean.setKind("ETH");
                            ethList.add(lastBean);
                        }
                        Log.e("ppp", "ethList长度+++" + ethList.size());
                        dataList.addAll(ethList);
                        dataList.addAll(notEthList);
                        insertData(dataList);
                        break;
                    case "0":
                        dataList.addAll(notEthList);
                        insertData(dataList);
                        break;
                }
                break;
            case 0x0066:
                //获取ETH记录
                tokenAddress = RequestHost.ethToken;
                ethRecordRequest();
                break;
        }
    }

    private void insertData(List<AllTransBean.ResultBean> list) {
        refreshLayout.finishRefresh();
        hud.dismiss();
        if (null != list && list.size() > 0) {

            if (lastListCache.size() > 0) {
                if (list.size() > lastListCache.size()) {
                    lastListCache.clear();
                    lastListCache = Util.ListSortRecord(list);
                    PlatformConfig.putList(address + "ALL", lastListCache);
                    lv.setVisibility(View.VISIBLE);
                    llNone.setVisibility(View.GONE);
                    adapter.setData(lastListCache);
                }
            } else {
                if (list.size() > 0) {
                    lastListCache.clear();
                    lastListCache = Util.ListSortRecord(list);
                    PlatformConfig.putList(address + "ALL", lastListCache);
                    lv.setVisibility(View.VISIBLE);
                    llNone.setVisibility(View.GONE);
                    adapter.setData(lastListCache);
                }
            }
        } else {
            lv.setVisibility(View.GONE);
            llNone.setVisibility(View.VISIBLE);
        }
    }


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
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        ButterKnife.bind(this);
        tvTitle.setText("交易记录");
        ivRight.setImageResource(R.drawable.icon_refresh);
        initView();
    }

    private void initView() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  加载中  ")
                .setCancellable(true);
        adapter = new AdapterTradeRecordAll(this, address);
        lv.setAdapter(adapter);
        list1 = new ArrayList<>();
        list11 = new ArrayList<>();
        list111 = new ArrayList<>();
        list2 = new ArrayList<>();
        listTime = new ArrayList<>();
        notEthList = new ArrayList<>();
        dataList = new ArrayList<>();
        lastListCache = new ArrayList<>();
        beanArrayList = new ArrayList<>();
        allList = new ArrayList<>();
        ethList = new ArrayList<>();
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.autoRefresh();
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UiHelper.startTransaAllActivity(TransactionRecordActy.this, lastListCache.get(position), address);
            }
        });
    }

    private void getTimeData() {
        if (list2 != null && list2.size() > 0) {
            for (int i = 0; i < list2.size(); i++) {
                getTimes(list2, list2.get(i).getBean().getBlockHash(), i);
            }
        } else {
            hud.dismiss();
        }
    }

    private void getTimes(List<TagBean> list2, String hash, int index) {
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
                        if (list2.size() == index + 1) {
                            for (RecordTimeBean recordTimeBean : listTime) {
                                for (TagBean hanhBean : list2) {
                                    if (recordTimeBean.getResult().getHash().equals(hanhBean.getBean().getBlockHash())) {
                                        AllTransBean.ResultBean lastBean = new AllTransBean.ResultBean();
                                        lastBean.setBlockNumber(hanhBean.getBean().getBlockNumber());
                                        lastBean.setBlockHash(hanhBean.getBean().getBlockHash());
                                        lastBean.setTimeStamp(recordTimeBean.getResult().getTimestamp());
                                        lastBean.setHash(hanhBean.getBean().getHash());
                                        lastBean.setNonce(hanhBean.getBean().getNonce());
                                        lastBean.setTransactionIndex(hanhBean.getBean().getTransactionIndex());
                                        lastBean.setFrom(hanhBean.getBean().getFrom());
                                        lastBean.setTo(hanhBean.getBean().getTo());
                                        lastBean.setValue(hanhBean.getBean().getValue());
                                        lastBean.setGas(hanhBean.getBean().getGas());
                                        lastBean.setGasPrice(hanhBean.getBean().getGasPrice());
                                        lastBean.setInput(hanhBean.getBean().getInput());
                                        lastBean.setGasUsed(recordTimeBean.getResult().getGasUsed());
                                        lastBean.setKind(hanhBean.getKind());
                                        notEthList.add(lastBean);
                                        Log.e("ppp", "notEthList长度++++" + notEthList.size());
                                    }
                                }
                            }
                            //非eth记录完了以后发消息，判断列表是否为空   再调接口请求eth记录
                            sendEmptyMessage(0x0066);
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

    private void getData2(List<TransRecorderBeanOne.ResultBean> listNow, String transactionHash, int indexs, String kind) {
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
                                        tagBean = new TagBean();
                                        tagBean.setBean(resultBean);
                                        tagBean.setKind(kind);
                                        list2.add(tagBean);
                                    }
                                    if ((listNow.size() == indexs + 1)) {
                                        if (list2.size() == 0) {
                                            hud.dismiss();
                                        } else {
                                            switch (kind) {
                                                case "CEC":
                                                    if (indexs + 1 == list1.size()) {
                                                        Log.e("ppp", "当前脚标----" + indexs + "----list1长度+++++" + list1.size() +
                                                                "-----list2长度++++" + list2.size());
                                                        getTimeData();
                                                    }
                                                    break;
                                                case "SEC":
                                                    if (indexs + 1 == list11.size()) {
                                                        getTimeData();
                                                    }
                                                    break;
                                                case "INT":
                                                    if (indexs + 1 == list111.size()) {
                                                        getTimeData();
                                                    }
                                                    break;
                                            }
                                        }
                                    }

                                }
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
        }).start();
    }

    private void ethRecordRequest() {
        String path0 = "http://103.60.111.204:3000/eth/getTransactionJson/";
        String pathRequestEth = path0 + address;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams(pathRequestEth);
                params.setAsJsonContent(true);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("web3", "result" + result);
                        sendMessage(0x0067, result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("web3", ex.toString());
                        sendEmptyMessage(0x20);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        sendEmptyMessage(0x20);
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

    private void requestFirst(String tokenAddress, String tag, int i) {
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
        data.setAddress(tokenAddress);
        data.setFromBlock("0x1");
        listTopic.add("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
        listTopic.add(null);//from过滤
        listTopic.add(null);//to  过滤
//        data.setAddress(RequestHost.eceToken);
        String json = JSON.toJSONString(jsonRpcBean);
        if (!hud.isShowing()) {
            hud.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams(RequestHost.url);
                params.setAsJsonContent(true);
                params.setBodyContent(json);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        switch (tag) {
                            case "CEC":
                                sendMessage(0x30, result);
                                break;
                            case "SEC":
                                sendMessage(0x40, result);
                                break;
                            case "INT":
                                sendMessage(0x50, result);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("web3", ex.toString());
                        sendEmptyMessage(0x20);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        sendEmptyMessage(0x20);
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

    @OnClick({R.id.iv_rig_top, R.id.iv_back_top})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_rig_top:
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
    }

    private void clearLList() {
        list1.clear();
        list11.clear();
        list111.clear();
        list2.clear();
        listTime.clear();
        notEthList.clear();
        ethList.clear();
        dataList.clear();
        beanArrayList.clear();
        allList.clear();
        adapter.setData(lastListCache);
    }

    private void getData() {
        clearLList();
        allList = SECApplication.getInstance().getTokenKindList();
        for (PropertyBean rr : allList) {
            if (rr.isChecked()) {
                beanArrayList.add(rr);
            }
        }
        if (!isNetworkAvailable(this)) {
            ToastUtil.show(this, R.string.net_work_err);
            return;
        }

        Map<String, RememberEth> map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth remembe : map.values()) {
            if (remembe.isNow()) {
                address = remembe.getAddress();
                break;
            }
        }
        hud.show();
        lastListCache = PlatformConfig.getList(this, address + "ALL");
        if (null != lastListCache && lastListCache.size() > 0) {
            lv.setVisibility(View.VISIBLE);
            llNone.setVisibility(View.GONE);
            adapter.setData(lastListCache);
            hud.dismiss();
        } else {
            lastListCache = new ArrayList<>();
        }
        requestData();
    }

    private void requestData() {

        for (int i = 0; i < beanArrayList.size(); i++) {
            PropertyBean bean = beanArrayList.get(i);
            if (bean.getName().equals("CEC") && bean.isChecked() == true) {
                tokenAddress = RequestHost.cecToken;
                tag = "CEC";
                requestFirst(tokenAddress, tag, i);
                continue;
            }
            if (bean.getName().equals("SEC") && bean.isChecked() == true) {
                tokenAddress = RequestHost.secToken;
                tag = "SEC";
                requestFirst(tokenAddress, tag, i);
                continue;
            }
            if (bean.getName().equals("INT") && bean.isChecked() == true) {
                tokenAddress = RequestHost.intToken;
                tag = "INT";
                requestFirst(tokenAddress, tag, i);
                continue;
            }
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
                            getData();
                        }
                    }
                }
                break;
        }
    }
}
