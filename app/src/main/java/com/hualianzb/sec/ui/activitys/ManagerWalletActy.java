package com.hualianzb.sec.ui.activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.adapters.AdapterManageWallet;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.MyWeb3jUtil;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.NumberUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerWalletActy extends BasicActivity {
    /**
     * 管理钱包
     */
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_wallets)
    ListView lvWallets;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    private List<RememberEth> list;
    private AdapterManageWallet adapter;
    private Map<String, RememberEth> map;
    private StateBarUtil stateBarUtil;
    private List<String> listBanlce;
    private KProgressHUD hud;
    private RememberEth rememberEth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_manage_wallet);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("管理钱包");
        adapter = new AdapterManageWallet(this);
        list = new ArrayList<>();
        listBanlce = new ArrayList<>();
        lvWallets.setAdapter(adapter);
        llTitle.setBackgroundColor(getResources().getColor(R.color.white));
        lvWallets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UiHelper.startMakeMoneyActicity(ManagerWalletActy.this, list.get(position).getAddress(), listBanlce.get(position),
                        false);
            }
        });
        if (!isNetworkAvailable(this)) {
            ToastUtil.show(this, getString(R.string.net_work_err));
            return;
        }
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case GlobalMessageType.MainRequest.GETTOKEN:
                String money = (String) message.obj;
                listBanlce.add(money);
                break;
            case GlobalMessageType.MainRequest.GETTOKEN_LAST:
                String moneyLst = (String) message.obj;
                listBanlce.add(moneyLst);
                Collections.reverse(listBanlce);
                adapter.setList(list, listBanlce);
                hud.dismiss();
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
                goBacktoMyPage();
                break;
        }
    }

    private void getDaiBi() {
        if (null != list && list.size() > 0) {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("  加载中  ")
                    .setCancellable(false);
            hud.show();
            int i = list.size() - 1;
            for (; i > -1; i--) {
                TaskCheckBanlance task = new TaskCheckBanlance(list.get(i), i);
                task.execute(RequestHost.url);
            }
        }
    }

    private class TaskCheckBanlance extends AsyncTask<String, String, String> {
        private RememberEth reme;
        private int nowIndex;

        public TaskCheckBanlance(RememberEth reme, int nowIndex) {
            this.reme = reme;
            this.nowIndex = nowIndex;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            EthGetBalance ethGetBalance1 = null;
            try {
                ethGetBalance1 = MyWeb3jUtil.getWeb3jInstance().ethGetBalance(reme.getAddress(), DefaultBlockParameterName.LATEST).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BigDecimal nig = Convert.fromWei(ethGetBalance1.getBalance().toString(), Convert.Unit.ETHER);
            result = nig.stripTrailingZeros().toPlainString();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            String myBalance = null;
            if (StringUtils.isEmpty(s)) {
                myBalance = "0.0000";
            } else {
                myBalance = NumberUtils.round(Double.parseDouble(s), 4) + "";
            }
            if (nowIndex == 0) {
                sendMessage(GlobalMessageType.MainRequest.GETTOKEN_LAST, myBalance);
            } else {
                sendMessage(GlobalMessageType.MainRequest.GETTOKEN, myBalance);
            }
        }
    }


    private int getIndexNoneZore(String num) {
        char[] temp = num.toCharArray();
        int index = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != '0') {
                index = i;
                break;
            }
        }
        return index;
    }

    //保留八位小数
    private String save8num(String num) {
        Double cny = Double.parseDouble(num);//转换成Double
        DecimalFormat df = new DecimalFormat("0.00000000");//格式化
        String now8 = df.format(cny);
        return now8;
    }

    private void goBacktoMyPage() {
        UiHelper.goBackHomaPageAc(this, rememberEth.getAddress(), true);
    }

    @Override
    public void onBackPressed() {
        goBacktoMyPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        map = null;
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (list.size() > 0) {
            list.clear();
        }
        if (null != map && !map.isEmpty() && map.values().size() > 0) {
            for (RememberEth re : map.values()) {
                list.add(re);
                if (re.isNow()) {
                    rememberEth = re;
                }
            }
        }
        list = Util.ListSort(list);
        getDaiBi();
    }
}
