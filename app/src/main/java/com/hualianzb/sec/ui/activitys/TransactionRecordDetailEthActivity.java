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
import com.hualianzb.sec.models.EthTransLogBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.web3j.utils.Convert;

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
public class TransactionRecordDetailEthActivity extends BasicActivity {
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
    private EthTransLogBean.ResultBean resultBean;
    private String hashTrans;
    private StateBarUtil stateBarUtil;
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
            resultBean = (EthTransLogBean.ResultBean) bundle.getSerializable("resultBean");
            hashTrans = bundle.getString("hashTrans");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
            resultBean = (EthTransLogBean.ResultBean) savedInstanceState.getSerializable("resultBean");
            hashTrans = savedInstanceState.getString("hashTrans");
        }
    }

    //保留八位小数
    private String save8num(String num) {
        double cny = Double.parseDouble(num);//转换成Double
        DecimalFormat df = new DecimalFormat("0.00000000");//格式化
        String now8 = df.format(cny / Math.pow(10, 18));
        return now8;
    }

    private void initData() {
        String gasUsed = resultBean.getGasUsed();
        String gasPrice = resultBean.getGasPrice();
        String money = resultBean.getValue();
        money = save8num(money);
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
        mapRemarks = PlatformConfig.getMap(Constant.SpConstant.TRANSREMARKS);
        if (null != mapRemarks && !mapRemarks.values().isEmpty() && mapRemarks.values().size() > 0) {
            myRemark = mapRemarks.get(resultBean.getHash());
            if (!StringUtils.isEmpty(myRemark)) {
                tvRemarks.setText(myRemark);
            }
        }
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
