package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.interfaces.IRerey;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.adapters.AdapterTradeRecordEach;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.ClickUtil;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.views.AutoListView;
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
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.commons.interfaces.GlobalMessageType.MessgeCode.ContainMessage;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:         SEC资产详情--交易记录
 */
public class TransactionRecordSecActivity extends BasicActivity {
    @BindView(R.id.tv_can)
    TextView tvCan;//总资产
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
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_frozen)
    TextView tvFrozen;
    private AdapterTradeRecordEach adapter;
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> lastListCache;//本地缓存的数据
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listChain;
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listPoor;
    private CopyOnWriteArrayList<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listGet;//获取到的数据
    private String address;
    private String title,
            money,//是可用资产
            availableMoney;//
    private Dialog dialogLoading, noNetDialog;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            money = bundle.getString("money");
            Log.e("layout_web", "myAdress+  \n" + address);
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
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case GlobalMessageType.MessgeCode.CANCELORERROR:
                dialogLoading.dismiss();
                noNetDialog.show();
                break;
            case ContainMessage:
                String resultData = (String) message.obj;
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
                        } else {
                            dialogLoading.dismiss();
                            refreshLayout.finishRefresh();
                            llData.setVisibility(View.GONE);
                            ll_none.setVisibility(View.VISIBLE);
                        }
                    } else {
                        setDataResult();
                    }

                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionrecord);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.loading));
        initData();
    }

    private void getData() {
        secRecordRequest();
    }

    private void initData() {
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        tvTheme.setText("SEC Receipt");
        tvRight.setVisibility(View.GONE);
        noNetDialog = DialogUtil.noNetTips(this, getString(R.string.net_work_err), new IRerey() {
            @Override
            public void retry() {
                clearList();
                getData();
            }
        });
        listChain = new ArrayList<>();
        listPoor = new ArrayList<>();
        listGet = new CopyOnWriteArrayList<>();
        lastListCache = new ArrayList<>();
        adapter = new AdapterTradeRecordEach(this, address);
        refreshLayout.setRefreshHeader(new PullDownHeader(this));
        refreshLayout.setRefreshFooter(new PullUpFooter(this));
        tvCan.setText(money);
        lvRecord.setAdapter(adapter);
        lastListCache = PlatformConfig.getList(this, address + "ALL" + title);
        if (null != lastListCache && lastListCache.size() > 0) {
            adapter.setData(lastListCache);
            llData.setVisibility(View.VISIBLE);
            ll_none.setVisibility(View.GONE);
        } else {
            llData.setVisibility(View.GONE);
            ll_none.setVisibility(View.VISIBLE);
            lastListCache = new ArrayList<>();
        }
        lvRecord.setOnItemClickListener((parent, view, position, id) -> UiHelper.startTransaAllActivity(TransactionRecordSecActivity.this, lastListCache.get(position), address));
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            clearList();
            getData();
            refreshLayout.finishRefresh();
        });
        refreshLayout.autoRefresh();
        //上拉加载
        refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshLayout.finishLoadmore(2000));
    }

    private void secRecordRequest() {
        dialogLoading.show();
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> list = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_getTransactions");
        bean.setParams(list);
        list.add(address.substring(2));//address
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.secTestUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        new Thread(() -> x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("web3", "result" + result);
                sendMessage(ContainMessage, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("web3", ex.toString());
//                handler.sendEmptyMessage(GlobalMessageType.MessgeCode.CANCELORERROR);
                sendEmptyMessage(GlobalMessageType.MessgeCode.CANCELORERROR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                sendEmptyMessage(GlobalMessageType.MessgeCode.CANCELORERROR);
            }

            @Override
            public void onFinished() {
                Log.e("web3", "onFinished");
            }
        })
        ) {
        }.start();
    }

    private void clearList() {
        if (listGet.size() > 0) {
            listGet.clear();
        }
    }

    @OnClick({R.id.tv_transfer, R.id.tv_receipt, R.id.iv_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_transfer://转账
                ClickUtil.checkFisrtAndNet(this);
                UiHelper.startTransferActivity(this, title, address, money);
                break;
            case R.id.tv_receipt://收款
                UiHelper.startMakeCodeActivity(this, address);
                break;
            case R.id.iv_back_top:
                UiHelper.goBackHomaPageAc(this, address, false);
                break;
        }
    }

    private void setTvAvailableAndFrozen() {
        double frozen = 0;
        for (int i = 0; i < lastListCache.size(); i++) {
            if (lastListCache.get(i).getTxReceiptStatus().equals("pending")) {
                frozen += Double.parseDouble(lastListCache.get(i).getValue());
            }
        }
        adapter.setData(lastListCache);
        setTextViewMoney(tvFrozen, frozen);
        setTextViewMoney(tvAvailable, Double.parseDouble(money));
        setTextViewMoney(tvCan, Double.parseDouble(money) + frozen);
    }

    private void setTextViewMoney(TextView tv, double args) {
        if (args == 0) {
            tv.setText("0.00000000");
        } else if (args > 0) {
            if ((args + "").length() > 10) {
                tv.setText((args + "").substring(0, 10));
            } else {
                String moneyTemp = args + "";
                if (moneyTemp.contains(".")) {
                    moneyTemp = moneyTemp.replace(".", "A");//点的话split无法返回String[]，所以替换
                    String[] moneys = moneyTemp.split("A");
                    if (moneys[1].length() > 8) {
                        moneyTemp = moneys[0] + "." + moneys[1].substring(0, 8);
                    } else {
                        moneyTemp = moneys[0] + "." + moneys[1];
                    }
                }
                tv.setText(moneyTemp);
            }
        } else {
            tv.setText((args + ""));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiHelper.goBackHomaPageAc(this, address, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            double reduceMoney = Double.parseDouble(data.getStringExtra("reduceMoney"));
            money = String.valueOf((Double.parseDouble(money) - reduceMoney));
            clearList();
            getData();
        }
    }

    private void setDataResult() {

        //清除收款方pengding状态的记录
        if (null != listGet && listGet.size() > 0) {
            for (SecTransactionBean.ResultBean.ResultInChainBeanOrPool bean : listGet) {
                if (bean.getTxReceiptStatus().equals("pending") && bean.getTxTo().equals(address.substring(2))) {
                    listGet.remove(bean);
                }
            }
        }
        if (null == listGet || listGet.size() == 0) {
            if (null == lastListCache || lastListCache.size() == 0) {
                refreshLayout.finishRefresh();
                llData.setVisibility(View.GONE);
                ll_none.setVisibility(View.VISIBLE);
            } else {
                setTvAvailableAndFrozen();
            }
            dialogLoading.dismiss();
        } else {
            lastListCache.clear();
            lastListCache.addAll(listGet);
            PlatformConfig.putList(address + "ALL" + title, lastListCache);
            setTvAvailableAndFrozen();
            if (listGet.size() > lastListCache.size()) {
                if (dialogLoading.isShowing()) {
                    TextView textView = (dialogLoading.findViewById(R.id.tv_content));
                    textView.setText((listGet.size() - lastListCache.size()) + " data have been updated…");
                }
            }
            refreshLayout.finishRefresh();
            llData.setVisibility(View.VISIBLE);
            ll_none.setVisibility(View.GONE);
            dialogLoading.dismiss();
        }
    }
}
