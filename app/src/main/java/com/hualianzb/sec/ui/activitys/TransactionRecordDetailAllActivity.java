package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Date:2018/8/19
 * auther:wangtianyun
 * describe:交易记录的详情
 */
public class TransactionRecordDetailAllActivity extends BasicActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avater)
    CircleImageView ivAvater;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_kind)
    TextView tvKind;
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
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_toWeb)
    LinearLayout ll_toWeb;
    private String address;
    private AllTransBean.ResultBean resultBean;
    private String time;
    private String hashTrans;
    private StateBarUtil stateBarUtil;
    private String kind;
    private Map<String, String> mapRemarks;
    private String myRemark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_record_detail);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.setStatusBarFullTransparent();
        initData();
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            resultBean = (AllTransBean.ResultBean) bundle.getSerializable("resultBean");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
            resultBean = (AllTransBean.ResultBean) savedInstanceState.getSerializable("resultBean");
        }
    }

    private void initData() {
        kind = resultBean.getKind();
        time = resultBean.getTimeStamp();
        hashTrans = resultBean.getHash();
        tvKind.setText(kind.toLowerCase());
        if (kind.equals("ETH")) {
            String gasUsed = resultBean.getGasUsed();
            String gasPrice = resultBean.getGasPrice();
            String money = resultBean.getValue();
            money = save8num(Double.parseDouble(money) / Math.pow(10, 18) + "");
            String blockNum = resultBean.getBlockNumber();
            String time = resultBean.getTimeStamp();
            String fei = Long.parseLong(gasUsed) * Long.parseLong(gasPrice) + "";
            fei = Convert.fromWei(fei, Convert.Unit.ETHER).toPlainString();
            tvGas.setText(fei + " ether");
            if (resultBean.getFrom().equals(address)) {//发出
                money = "- " + money;
            } else {
                money = "+ " + money;
            }
            tvSendAddress.setText(resultBean.getFrom());
            tvGetAddress.setText(resultBean.getTo());
            tvMoney.setText(money);
            tvBlock.setText(blockNum);
            tvTradeNum.setText(hashTrans);
            Long ddd = Long.parseLong(time);//单位秒
            String transTimeLast = TimeUtil.getTime1(ddd * 1000);
            tvDate.setText(transTimeLast);
        } else if (kind.equals("SEC")) {
            String gasUsed = resultBean.getGasUsed();
            String gasPrice = resultBean.getGasPrice();//0x   36进制的
            gasPrice = gasPrice.substring(2);
            gasPrice = StringUtils.HexToInt(gasPrice) + "";
            String money = resultBean.getValue();
//            money = save8num(Double.parseDouble(money) / Math.pow(10, 18) + "");
            String blockNum = resultBean.getBlockNumber();
            String time = resultBean.getTimeStamp();
            String fei = Long.parseLong(gasUsed) * Long.parseLong(gasPrice) + "";
            fei = Convert.fromWei(fei, Convert.Unit.ETHER).toPlainString();
            tvGas.setText(fei + " sec");
            if (resultBean.getFrom().equals(address)) {//发出
                money = "- " + money;
            } else {
                money = "+ " + money;
            }
            tvSendAddress.setText(resultBean.getFrom());
            tvGetAddress.setText(resultBean.getTo());
            tvMoney.setText(money);
            tvBlock.setText(blockNum);
            tvTradeNum.setText(hashTrans);
            Long ddd = Long.parseLong(time);//单位秒
            String transTimeLast = TimeUtil.getTime1(ddd * 1000);
            tvDate.setText(transTimeLast);
        } else {
            String gasUsed = resultBean.getGasUsed();
            String gasPrice = resultBean.getGasPrice();
            Long gasInt = Long.parseLong(new BigInteger(gasUsed.substring(2), 16).toString(10));
            Long gasPriceInt = Long.parseLong(new BigInteger(gasPrice.substring(2), 16).toString(10));
            Long MinerFee = gasInt * gasPriceInt;
            double gasFeeDouble = MinerFee / 2 / Math.pow(10, 18);
            String Fee = new BigDecimal(gasFeeDouble).toPlainString();
            String from = resultBean.getFrom();
            String to = resultBean.getTo();
            String input = resultBean.getInput();
            String num = resultBean.getBlockNumber();//16进制BigInteger
            String showAddress = "0x" + input.substring(34, 74);
            String blocknum = new BigInteger(num.substring(2), 16).toString(10);
            String money1 = input.substring(74);//64位16进制左边带00000000
            //得到16进制去除0x0000……的16进制字符串
            String noZeroResult = money1.substring(getIndexNoneZore(money1));
            String str = new BigInteger(noZeroResult, 16).toString(10);
            String money = (Double.parseDouble(str) / (Math.pow(10, 18))) + "";

            if (from.equals(address)) {
                tvMoney.setText("-" + money);
                tvSendAddress.setText(from);
                tvGetAddress.setText(showAddress);
            }
            if (!from.equals(address)) {
                tvMoney.setText("+" + money);
                tvSendAddress.setText(from);
                tvGetAddress.setText(address);
            }


            String timeLast = time.toString().substring(2);//16进制去掉0x
            BigInteger d = new BigInteger(timeLast, 16);
            Long ddd = Long.parseLong(d.toString(10));//单位秒
            String transTimeLast = TimeUtil.getTime1(ddd * 1000);
            tvDate.setText(transTimeLast);
            tvTradeNum.setText(hashTrans);
            tvBlock.setText(blocknum);
            tvGas.setText(save14num(Fee) + " ether");
        }
        mapRemarks = PlatformConfig.getMap(Constant.SpConstant.TRANSREMARKS);
        if (null != mapRemarks && !mapRemarks.values().isEmpty() && mapRemarks.values().size() > 0) {
            myRemark = mapRemarks.get(resultBean.getHash());
            if (!StringUtils.isEmpty(myRemark)) {
                tvRemarks.setText(myRemark);
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

    private String save14num(String num) {
        Double cny = Double.parseDouble(num);//转换成Double
        DecimalFormat df = new DecimalFormat("0.00000000000000");//格式化
        String now8 = df.format(cny);
        return now8;
    }

    @OnClick({R.id.iv_back, R.id.ll_toWeb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_toWeb:
                if (isNetworkAvailable(this)) {
                    UiHelper.startWebActivity(this, resultBean.getHash());
                } else {
                    ToastUtil.show(this, getString(R.string.net_work_err));
                }
                break;
        }
    }
}
