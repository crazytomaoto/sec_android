package com.hualianzb.sec.ui.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.activity.CaptureActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SignDataBean;
import com.hualianzb.sec.models.SECSendRawBean;
import com.hualianzb.sec.models.SECTransResponseBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.JsonUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
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

import static com.hualianzb.sec.application.SECApplication.secJsApi;
import static com.hualianzb.sec.commons.constants.Constant.SpConstant.SWEEP;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:转账
 */
public class TransferActivity extends BasicActivity {
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.ed_toadress)
    EditText edToadress;
    @BindView(R.id.ed_senMoney)
    EditText edSenMoney;
    @BindView(R.id.ed_remark)
    EditText edRemark;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_address_error)
    TextView tvAddressError;
    @BindView(R.id.tv_amount_error)
    TextView tvAmountError;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private String myGetAdress, myGetAmount, error_address, error_money;
    private boolean isAddressOk, isAmountOk = false;
    private Dialog dialog;
    private String address;
    private Map<String, RememberSEC> map;
    private RememberSEC rememberEth;
    private double setMoney;
    private String title;
    private String tokenValue;
    private String token;
    private Dialog dialogLoading;
    private SECSendRawBean.ParamsBean paramsBean;
    private Map<String, String> tranRemark;
    private boolean isCheckedPass;
    /**
     * 屏幕宽度
     */
    protected int mWidth;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            title = bundle.getString("title");
            address = bundle.getString("address");
            tokenValue = bundle.getString("tokenValue");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            title = savedInstanceState.getString("title");
            address = savedInstanceState.getString("address");
            tokenValue = savedInstanceState.getString("tokenValue");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
        initData();
    }

    private void initData() {
        edToadress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myGetAdress = s.toString().trim();
                checkAddress(myGetAdress);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edSenMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myGetAmount = s.toString().trim();
                checkMoney(myGetAmount);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //检验地址
    private void checkAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            isAddressOk = false;
        } else if (myGetAdress.length() != 42 || !myGetAdress.substring(0, 2).equals("0x")) {
            isAddressOk = false;
            error_address = "Unavailable Address";
        } else {
            isAddressOk = true;
        }
//        else if (myGetAdress.equals(address)) {
//            isAddressOk = false;
//            error_address = "同一地址之间不能转账哦";
//            tvAddressError.setText(error_address);
//        }
        if (isAddressOk) {
            tvAddressError.setVisibility(View.GONE);
        } else if (isAddressOk == false) {
            if (StringUtils.isEmpty(address)) {
                tvAddressError.setVisibility(View.GONE);
            } else {
                tvAddressError.setText(error_address);
                tvAddressError.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkMoney(String money) {
        double moneyD;
        if (!StringUtils.isEmpty(money)) {
            if (money.equals(".")) {
                money = "0" + money;
            }
            moneyD = Double.parseDouble(money);
        } else {
            moneyD = 0.00;
        }
        if (moneyD < 0) {
            isAmountOk = false;
            error_money = "Not correct type of tokens";
        } else if (moneyD == 0) {
            isAmountOk = false;
        } else {
            if (moneyD > Double.parseDouble(tokenValue)) {
                isAmountOk = false;
                error_money = "Not enough tokens";
            } else {
                isAmountOk = true;
            }
        }
        if (isAmountOk) {
            tvAmountError.setVisibility(View.GONE);
        } else {
            if (moneyD == 0) {
                tvAmountError.setVisibility(View.GONE);
            } else {
                tvAmountError.setText(error_money);
                tvAmountError.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setBtnClickable() {
        if (isAddressOk && isAmountOk) {
            tvNext.setClickable(true);
            tvNext.setBackground(getResources().getDrawable(R.drawable.bg_btn));
        } else {
            tvNext.setClickable(false);
            tvNext.setBackground(getResources().getDrawable(R.drawable.bg_btn_cannot));
        }
    }

    private void initView() {
        dialogLoading = DialogUtil.showLoadingDialog(this, "Submitting");
        tvRight.setVisibility(View.GONE);
        tvTheme.setText("SEC transfer");
        ivBackTop.setImageResource(R.drawable.icon_close_black);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        tranRemark = new HashMap<>();
        tranRemark = PlatformConfig.getMap(Constant.SpConstant.TRANSREMARKS);

        if (map != null && !map.isEmpty() && map.values().size() > 0) {
            for (RememberSEC remember : map.values()) {
                if (address.equals(remember.getAddress())) {
                    rememberEth = remember;
                    break;
                }
            }
        }
        switch (title) {
            case "SEC":
                token = RequestHost.ethToken;
                break;
        }
    }

    @OnClick({R.id.tv_next, R.id.rl_constant})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                boolean netWorkOk = isNetworkAvailable(this);
                if (netWorkOk == false) {
                    DialogUtil.noNetTips(this, "Submit Failure.No Network Connection.", () -> {
                        return;
                    });
                }
                showTradeDetailDialog(TransferActivity.this);
                break;
            case R.id.rl_constant:
                UiHelper.startAddressBookActy(this, false);
                break;
        }
    }

    //签名hash
    private String getSign() {
        SignDataBean signDataBean = new SignDataBean();
        signDataBean.setPrivateKey(rememberEth.getPrivateKey());
        signDataBean.setFrom(rememberEth.getAddress().substring(2));
        signDataBean.setTo(edToadress.getText().toString().trim().substring(2));
        signDataBean.setValue(edSenMoney.getText().toString());
        signDataBean.setInputData(edRemark.getText().toString().trim());
        String jsonDataStr = JSON.toJSONString(signDataBean);
        return secJsApi.TxSign(jsonDataStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101:
                if (resultCode == 1) {
                    if (null != data) {
                        String result = data.getExtras().getString("result");
                        if (!StringUtils.isEmpty(result)) {
                            String[] resultArray = result.split("###");
                            if (resultArray != null && resultArray.length > 0) {
                                myGetAdress = resultArray[0];
                                edToadress.setText(myGetAdress);
                                String money = resultArray[1];
                                if (!StringUtils.isEmpty(money)) {
                                    setMoney = Double.parseDouble(resultArray[1]);
                                    edSenMoney.setText(setMoney + "");
                                } else {
                                    setMoney = 0.00;
                                    edSenMoney.setText(setMoney + "");
                                }
                                checkAddress(myGetAdress);
                                checkMoney(money);
                                setBtnClickable();
                            }
                        }
                    }
                }
                if (resultCode == 2) {
                    if (null != data) {
                        myGetAdress = data.getExtras().getString("address");
                        if (!StringUtils.isEmpty(address)) {
                            edToadress.setText(myGetAdress);
                            myGetAmount = 0.00 + "";
                            edSenMoney.setText(myGetAmount);
                        }
                    }
                }
                break;
            case 001://来自密码校验
                if (resultCode == 1) {
                    if (null != data) {
                        isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            dialog.dismiss();
                            secFront();
                        }
                    }
                }
                break;
            default:
                break;
        }


        //二维码扫描结果
        if (requestCode == SWEEP) {

            String result = "";
            try {
                result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            } catch (Exception e) {
                ToastUtil.show(this, R.string.scan_error);
            }
//            if (!StringUtils.isEmpty(result)) {
//
//            } else {
//                ToastUtil.show(this, "扫描失败");
//            }

        }
    }

    public void showTradeDetailDialog(final Activity context) {
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(R.layout.dialog_trade_detail);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
//            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
//            mHeight = metrics.heightPixels;
        }
        dialogWindow.setLayout(mWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        TextView tv_to = dialog.findViewById(R.id.tv_to);
        TextView tv_from = dialog.findViewById(R.id.tv_from);
        TextView tv_money = dialog.findViewById(R.id.tv_money);
        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
        tv_to.setText(myGetAdress);
        tv_from.setText(rememberEth.getAddress());
        tv_money.setText(edSenMoney.getText().toString().trim() + " SEC");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiHelper.startCheckPassActivity(TransferActivity.this, rememberEth.getPass(), 001);
                dialog.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
//
//    private void tranToken() throws Exception {
//        dialogLoading.show();
//        EthGetTransactionCount ethGetTransactionCount = MyWeb3jUtil.getWeb3jInstance().ethGetTransactionCount(
//                rememberEth.getAddress()
//                , DefaultBlockParameterName.LATEST).sendAsync().get();
//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//        double amount = Double.parseDouble(edSenMoney.getText().toString().trim());
//
//
//        String mmp = NumberFormat.getInstance().format(now * Math.pow(10, 9) * 20);
//        mmp = mmp.replaceAll(",", "");
//        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(mmp));
//
////        BigInteger gasPrice = BigInteger.valueOf(Long.parseLong(now * Math.pow(10, 9) + ""));
//        signContractTransaction(
//                token,
//                edToadress.getText().toString().trim(),
//                nonce,
//                gasPrice,
//                myLimit,
//                amount
//        );//单位10的18次方
//    }


    private void secFront() {
        dialogLoading.show();
        String tx = getSign();
        paramsBean = JSON.parseObject(tx, SECSendRawBean.ParamsBean.class);
        try {
            tranSEC();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void tranSEC() {
        List<SECSendRawBean.ParamsBean> list = new ArrayList<>();
        list.add(paramsBean);
        SECSendRawBean bean = new SECSendRawBean();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_sendRawTransaction");
        bean.setParams(list);

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
                        Log.e("web3", "sec_sendRawTransaction-result+\n" + result);
                        if (!StringUtils.isEmpty(result)) {
                            SECTransResponseBean transResponseBean = JsonUtil.parseJson(result, SECTransResponseBean.class);
                            if (null != transResponseBean) {
                                SECTransResponseBean.ResultBean resultBean = transResponseBean.getResult();
                                if (null != resultBean) {
                                    String statrus = resultBean.getStatus();//  1 成功 0 不成功，答应info
                                    String info = resultBean.getInfo();
                                    String txHash = resultBean.getTxHash();
                                    if (!StringUtils.isEmpty(statrus) && statrus.equals("1")) {
                                        if (!StringUtils.isEmpty(txHash) && txHash.length() >= 64) {
                                            String myRemark = edRemark.getText().toString().trim();
                                            if (!StringUtils.isEmpty(myRemark)) {
                                                if (null == tranRemark) {
                                                    tranRemark = new HashMap<>();
                                                }
                                                tranRemark.put(txHash, myRemark);
                                                PlatformConfig.putMap(Constant.SpConstant.TRANSREMARKS, tranRemark);
                                            }
                                            Intent intent = new Intent(TransferActivity.this, TransactionRecordSecActivity.class);
                                            intent.putExtra("reduceMoney", paramsBean.getValue());
                                            setResult(100, intent);
                                            finish();
                                        } else {
//                                            ToastUtil.show(TransferActivity.this, "转账失败");
                                        }
                                    } else {
                                        ToastUtil.show(TransferActivity.this, info);
                                    }
                                }
                                dialogLoading.dismiss();
                                dialog.dismiss();
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
}
