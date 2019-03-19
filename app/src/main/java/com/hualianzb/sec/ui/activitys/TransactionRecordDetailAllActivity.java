package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.NetUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/8/19
 * auther:wangtianyun
 * describe:交易记录的详情
 */
public class TransactionRecordDetailAllActivity extends BasicActivity {
    @BindView(R.id.iv_back_top)
    ImageView ivBack;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.tv_gas)
    TextView tvGas;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.tv_trade_num)
    TextView tvTradeNum;
    @BindView(R.id.tv_block)
    TextView tvBlock;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_toWeb)
    LinearLayout ll_toWeb;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.ll_remark)
    LinearLayout llRemark;
    private String address;
    private SecTransactionBean.ResultBean.ResultInChainBeanOrPool resultBean;
    private String today = TimeUtil.getDay();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_record_detail);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.bg_detail).statusBarDarkFont(true).init();
        initData();
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            resultBean = (SecTransactionBean.ResultBean.ResultInChainBeanOrPool) bundle.getSerializable("resultBean");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
            resultBean = (SecTransactionBean.ResultBean.ResultInChainBeanOrPool) savedInstanceState.getSerializable("resultBean");
        }
    }

    private void initData() {
        String money = resultBean.getValue();
        String moneyTemp;
        if (money.contains(".")) {
            moneyTemp = money.replace(".", "A");//点的话split无法返回String[]，所以替换
            String[] moneys = moneyTemp.split("A");
            double smallPart = Double.parseDouble(moneys[1]);//小数部分
            if (smallPart > 0) {
                tvMoney.setText(money + " sec");
            } else {
                tvMoney.setText(moneys[0] + " sec");
            }
        } else {
            tvMoney.setText(money + " sec");
        }

        String status = resultBean.getTxReceiptStatus();
        String from = resultBean.getTxFrom();
        String to = resultBean.getTxTo();
        String minerCose = resultBean.getGasUsedByTxn();
        String remarks = resultBean.getInputData();
        String transNumber = resultBean.getTxHash();
        String block = resultBean.getTxHeight() + "";
        long time_Stamp = resultBean.getTimeStamp();

        switch (status) {
            case "pending":
                ivAvater.setImageResource(R.drawable.icon_trans_ing);
                tvMoney.setTextColor(getResources().getColor(R.color.text_yellow02));
                tvState.setTextColor(getResources().getColor(R.color.text_yellow02));
                tvState.setText("Transfer Pending");
                break;
            case "success":
                tvMoney.setTextColor(getResources().getColor(R.color.text_selected_green));
                tvState.setTextColor(getResources().getColor(R.color.text_selected_green));
                if (to.equals(address.substring(2))) {
                    ivAvater.setImageResource(R.drawable.icon_trans_received);
                    tvState.setText("Receive Succesful");
                } else {
                    ivAvater.setImageResource(R.drawable.icon_send_success);
                    tvState.setText("Send successful");
                }
                break;
            case "failed":
                ivAvater.setImageResource(R.drawable.icon_trans_failed);
                tvMoney.setTextColor(getResources().getColor(R.color.text_error));
                tvState.setTextColor(getResources().getColor(R.color.text_error));
                tvState.setText("Transfer failed");
                break;
            case "mined":
                ivAvater.setImageResource(R.drawable.icon_mined);
                tvMoney.setTextColor(getResources().getColor(R.color.text_yellow02));
                tvState.setTextColor(getResources().getColor(R.color.text_yellow02));
                tvState.setText("Mined");
                break;
        }

        if (to.equals(address.substring(2))) {
            tvMoney.setText("+" + tvMoney.getText());
        } else {
            tvMoney.setText("-" + tvMoney.getText());
        }

        tvSendAddress.setText(from);
        tvGetAddress.setText(to);
        tvGas.setText(minerCose + " SEC");
        tvTradeNum.setText("0x" + transNumber.substring(0, 8) + "…" + transNumber.substring(30, 40));
        tvBlock.setText(block);

        if (StringUtils.isEmpty(remarks)) {
            llRemark.setVisibility(View.GONE);
        } else {
            tvRemarks.setText(remarks);
        }
        String timeTemp = TimeUtil.getTime12(time_Stamp);
        if (timeTemp.equals(today)) {
            tvDate.setText(TimeUtil.getTime11(time_Stamp));
        } else {
            tvDate.setText(TimeUtil.getTime2(time_Stamp));
        }

        if (status.equals("pending")) {
            ll_toWeb.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_back_top, R.id.ll_toWeb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_top:
                finish();
                break;
            case R.id.ll_toWeb:
                boolean netOk = isNetworkAvailable(this);
                reGo(netOk);
                break;
        }
    }

    private void reGo(boolean netOk) {
        NetUtil.getNetWorkState(this);
        UiHelper.startWebActivity(this, resultBean.getTxHash());
    }

}
