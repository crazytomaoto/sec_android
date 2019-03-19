package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.interfaces.IRerey;
import com.hualianzb.sec.models.ManageWalletBean;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SecBalanceResultBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.adapters.AdapterManageWallet;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

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

public class ManagerWalletActy extends BasicActivity {
    /**
     * 管理钱包
     */
    @BindView(R.id.lv_wallets)
    ListView lvWallets;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private List<RememberSEC> list;
    private AdapterManageWallet adapter;
    private Map<String, RememberSEC> map;
    private Dialog dialogLoading;
    private RememberSEC rememberEth;
    private List<ManageWalletBean> listItems;
    private ManageWalletBean itemBeam;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_manage_wallet);
        SECApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTheme.setText("Manage Wallet");
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        tvRight.setVisibility(View.GONE);
        adapter = new AdapterManageWallet(this);
        list = new ArrayList<>();
        listItems = new ArrayList<>();
        lvWallets.setAdapter(adapter);
        llTitle.setBackgroundColor(getResources().getColor(R.color.white));
        lvWallets.setOnItemClickListener((parent, view, position, id) -> UiHelper.startMakeMoneyActicity(ManagerWalletActy.this, list.get(position).getAddress(), listItems.get(position).getMoney(),
                false));
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case GlobalMessageType.MainRequest.GETTOKEN_LAST:
//                Collections.reverse(listItems);
                adapter.setList(listItems);
                dialogLoading.dismiss();
                break;
        }
    }

    @OnClick({R.id.ll_create, R.id.ll_import, R.id.iv_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_create:
                UiHelper.startActyCreateWalletActivity(this);
                break;
            case R.id.ll_import:
                UiHelper.startActyImportWalletActivity(this);
                break;
            case R.id.iv_back_top:
                finish();
                break;
        }
    }

    //    private void getDaiBi(int index) {
//        if (null != list && list.size() > 0) {
//            dialogLoading.show();
//            for (int i = 0; i < list.size(); i++) {
//                itemBeam = new ManageWalletBean();
//                itemBeam.setAddress(list.get(i).getAddress());
//                itemBeam.setWalletName(list.get(i).getWalletName());
//                itemBeam.setHowToCreate(list.get(i).getHowToCreate());
//                itemBeam.setBackup(list.get(i).isBackup());
//                getSecBalance(list.get(i).getAddress().substring(2), i);
//            }
//        }
//    }
    private void getDaiBi(int index) {
        try {
            getSecBalance(index);
        } catch (Exception e) {
            e.printStackTrace();
            if (dialogLoading.isShowing()) {
                dialogLoading.dismiss();
            }
            if (list.size() > 0) {
                list.clear();
                DialogUtil.noNetTips(this, getString(R.string.net_work_err), () -> finish());
            }
        } finally {

        }
    }
//    //获取SEC余额
//    private void getSecBalance(String address, int position) {
//        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
//        List<Object> listRequestData = new ArrayList<>();
//        bean.setId(1);
//        bean.setJsonrpc("2.0");
//        bean.setMethod("sec_getBalance");
//        bean.setParams(listRequestData);
//        listRequestData.add(address);//address
//        listRequestData.add("latest");
//        String json = JSON.toJSONString(bean);
//        RequestParams params = new RequestParams(RequestHost.secTestUrl);
//        params.setAsJsonContent(true);
//        params.setBodyContent(json);
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e("web3", "sec_getBalance+\n" + result);
//                if (!StringUtils.isEmpty(result)) {
//                    String money = null;
//                    SecBalanceResultBean balanceResultBean = JSON.parseObject(result, SecBalanceResultBean.class);
//                    if (null != balanceResultBean) {
//                        SecBalanceResultBean.ResultBean resultBean = balanceResultBean.getResult();
//                        if (resultBean != null) {
//                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("1")) {
//                                money = resultBean.getValue() + "";
//                                itemBeam.setMoney(money);
//                                listItems.add(itemBeam);
//                                if (position == (list.size() - 1)) {
//                                    sendEmptyMessage(GlobalMessageType.MainRequest.GETTOKEN_LAST);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("web3", ex.toString());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//                Log.e("web3", "onFinished");
//            }
//        });
//    }

    //获取SEC余额
    private void getSecBalance(int position) {
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> listRequestData = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_getBalance");
        bean.setParams(listRequestData);
        listRequestData.add(list.get(position).getAddress().substring(2));//address
        listRequestData.add("latest");
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.secTestUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("web3", "sec_getBalance+\n" + result);
                if (!StringUtils.isEmpty(result)) {
                    String money = null;
                    SecBalanceResultBean balanceResultBean = JSON.parseObject(result, SecBalanceResultBean.class);
                    if (null != balanceResultBean) {
                        SecBalanceResultBean.ResultBean resultBean = balanceResultBean.getResult();
                        if (resultBean != null) {
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("1")) {
                                money = resultBean.getValue() + "";
                                itemBeam = new ManageWalletBean();
                                itemBeam.setAddress(list.get(position).getAddress());
                                itemBeam.setWalletName(list.get(position).getWalletName());
                                itemBeam.setHowToCreate(list.get(position).getHowToCreate());
                                itemBeam.setBackup(list.get(position).isBackup());
                                itemBeam.setMoney(money);
                                listItems.add(itemBeam);
                                if (position == (list.size() - 1)) {
                                    sendEmptyMessage(GlobalMessageType.MainRequest.GETTOKEN_LAST);
                                } else {
                                    getSecBalance(position + 1);
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

    @Override
    public void onChangeListener(int status) {

    }

    @Override
    public void onResume() {
        super.onResume();
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.loading));
        map = null;
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (list.size() > 0) {
            list.clear();
        }
        if (listItems.size() > 0) {
            listItems.clear();
        }
        if (null != map && !map.isEmpty() && map.values().size() > 0) {
            for (RememberSEC re : map.values()) {
                list.add(re);
                if (re.isNow()) {
                    rememberEth = re;
                }
            }
        } else {
            map = new HashMap<>();
        }
        if (null != list && list.size() > 0) {
            list = Util.ListSort(list);
        }
        dialogLoading.show();
        getDaiBi(0);
    }
}
