package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.ClickUtil;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.OwnWalletUtils;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateWalletActivity extends BasicActivity {

    @BindView(R.id.ed_wname)
    EditText edWname;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.ed_repass)
    EditText edRepass;
    @BindView(R.id.ed_pass_reminder)
    EditText edPassReminder;
    @BindView(R.id.cb_agree)
    ImageView cbAgree;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    @BindView(R.id.btn_create_new)
    TextView btnCreate;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_star)
    TextView tvNameStar;
    @BindView(R.id.tv_name_error)
    TextView tvNameError;
    @BindView(R.id.tv_pass)
    TextView tvPass;
    @BindView(R.id.tv_pass_star)
    TextView tvPassStar;
    @BindView(R.id.tv_pass_error)
    TextView tvPassError;
    @BindView(R.id.tv_re_pass)
    TextView tvRePass;
    @BindView(R.id.ll_edPass)
    LinearLayout llEdPass;
    @BindView(R.id.ll_rePass)
    LinearLayout llRePass;
    @BindView(R.id.tv_mumber_error)
    TextView tvMumbErroe;
    @BindView(R.id.tv_re_pass_star)
    TextView tvRePassStar;
    @BindView(R.id.tv_re_pass_error)
    TextView tvRePassError;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.iv_red)
    ImageView ivRed;
    @BindView(R.id.iv_yellow)
    ImageView ivYellow;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    @BindView(R.id.rl_level)
    RelativeLayout rlLevel;
    private boolean iChecked = true;//功能暂时去掉，默认改成true，不影响功能
    private boolean isWallteNumberOk = true;//钱包数量限制10个
    private String walletName, pass, rePass, remind;
    private boolean isWallteNameOk, isPassOk, isRePassOk;
    //钱包地址
    private String walletAddress = null;
    private int imgIcon;//0-5之间的随机整数
    private RememberSEC rememberEth, oldReme;
    private String mnemonics;
    private ECKeyPair ecKeyPair;
    private WalletFile walletFile = null;
    private String tips;
    private Map<String, RememberSEC> map;
    private boolean canSend = true;
    private Dialog dialogLoading, TipsDialog;
    private int passLevel = 0;
    Timer timer = new Timer();
    TimerTask task = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    canSend = false;
                    String myAddr = (String) msg.obj;
                    if (myAddr.length() > 2) {
                        Log.e("web3", "dialog-------------dismisss");
//                        FileUtil.saveDataToFile(CreateWalletActivity.this, map, FileUtil.TEMP_IMAGE);
                        rememberEth.setWalletName(walletName);
                        rememberEth.setPass(edPass.getText().toString().trim());
                        rememberEth.setTips(edPassReminder.getText().toString().trim());
                        rememberEth.setMnemonics(mnemonics);
                        rememberEth.setWalletFile(JSON.toJSONString(walletFile));
//                            walletAddress = OwnWalletUtils.generateNewWalletFile("123456789", new File(this.getFilesDir(), ""), false);
                        rememberEth.setAddress(walletAddress);
                        rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                        rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                        setOtherWalletFalse();//如果有选中的钱包，置为不选中
                        rememberEth.setNow(true);//把当前钱包置为选中
                        rememberEth.setWalletincon(imgIcon);
                        rememberEth.setHowToCreate(1);
                        rememberEth.setCreatTime(TimeUtil.getDate());
                        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
                        if (map == null || map.isEmpty() || map.values().size() == 0) {
                            map = new HashMap<>();
                        }
                        map.put(myAddr, rememberEth);
                        String oldAddress = PlatformConfig.getString(Constant.SpConstant.NOWADDRESS);
                        if (!StringUtils.isEmpty(oldAddress)) {
                            oldReme = map.get(oldAddress);
                        }
                        if (null != oldReme) {
                            oldReme.setNow(false);
                            map.put(oldAddress, oldReme);
                        }
                        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);//本地保存钱包的信息
                        PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址
                        Util.saveTokenKindsForEacthWallet(rememberEth.getAddress());
                        dialogLoading.dismiss();
                        TipsDialog.show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void initLogics() {
        super.initLogics();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvRight.setText("Import Wallet");
        tvTheme.setText("Create Wallet");
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.creating));
        TipsDialog = DialogUtil.TipsDialog(this, R.drawable.icon_success_green, "Create Wallet",
                1, "Wallet create successfully", (v, d) -> {
                    UiHelper.startBackupMnemonicsActy1(CreateWalletActivity.this, rememberEth);
                    d.dismiss();
                    finish();
                });
        rememberEth = new RememberSEC();
        map = new HashMap<>();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            map = new HashMap<>();
        } else {
            if (map.values().size() == 10) {
                isWallteNumberOk = false;
                tvMumbErroe.setVisibility(View.VISIBLE);
            } else {
                isWallteNumberOk = true;
                tvMumbErroe.setVisibility(View.GONE);
            }
        }
        edWname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                walletName = s.toString().trim();
                checkWalletName(walletName);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass = s.toString().trim();
                if (!StringUtils.isEmpty(pass)) {
                    ivClear1.setVisibility(View.VISIBLE);
                } else {
                    ivClear1.setVisibility(View.GONE);
                }
                checkPassword(pass);
                checkPassStrength(pass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edRepass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rePass = s.toString().trim();
                if (!StringUtils.isEmpty(rePass)) {
                    ivClear2.setVisibility(View.VISIBLE);
                } else {
                    ivClear2.setVisibility(View.GONE);
                }
                checkRePassword(rePass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //检测钱包名称的合法性
    private void checkWalletName(String walletName) {
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            if (StringUtils.isEmpty(walletName)) {//名称为空
                isWallteNameOk = false;
                edWname.setBackgroundResource(R.drawable.bg_edit_error);
                tvNameStar.setTextColor(getResources().getColor(R.color.text_error));
                tvNameError.setVisibility(View.VISIBLE);
                tvNameError.setText(getString(R.string.wallet_name_null));
                return;
            } else {
                isWallteNameOk = true;
                edWname.setBackgroundResource(R.drawable.bg_edit_gray);
                tvNameStar.setTextColor(getResources().getColor(R.color.text_blue));
                tvNameError.setVisibility(View.GONE);
                return;
            }
        } else {
            if (StringUtils.isEmpty(walletName)) {//名称为空
                isWallteNameOk = false;
                edWname.setBackgroundResource(R.drawable.bg_edit_error);
                tvNameStar.setTextColor(getResources().getColor(R.color.text_error));
                tvNameError.setText(getString(R.string.wallet_name_null));
                tvNameError.setVisibility(View.VISIBLE);
                return;
            } else {
                boolean hasRe = false;//是否有重名的
                for (RememberSEC rememberEth : map.values()) {
                    if (walletName.equals(rememberEth.getWalletName())) {
                        hasRe = true;
                        break;
                    }
                }
                if (hasRe) {//有重名的
                    isWallteNameOk = false;
                    edWname.setBackgroundResource(R.drawable.bg_edit_error);
                    tvNameStar.setTextColor(getResources().getColor(R.color.text_error));
                    tvNameError.setText(getString(R.string.wallet_exists));
                    tvNameError.setVisibility(View.VISIBLE);
                    return;
                } else {
                    isWallteNameOk = true;
                    edWname.setBackgroundResource(R.drawable.bg_edit_gray);
                    tvNameStar.setTextColor(getResources().getColor(R.color.text_blue));
                    tvNameError.setVisibility(View.GONE);
                    return;
                }
            }
        }
    }

    //检测密码强度
    private void checkPassStrength(String password) {
        if (StringUtils.isEmpty(password)) {
            rlLevel.setVisibility(View.GONE);
        } else {
            passLevel = StringUtils.getPassLevale(password);
            if (passLevel == 0 && password.length() < 8) {
                rlLevel.setVisibility(View.GONE);
                return;
            }
            if (passLevel == 2 && password.length() >= 8) {
                rlLevel.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.GONE);
                ivYellow.setVisibility(View.GONE);
                return;
            }
            if (passLevel == 3 && password.length() >= 8 && password.length() <= 12) {
                rlLevel.setVisibility(View.VISIBLE);
                ivYellow.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.GONE);
                return;
            }
            if (passLevel == 3 && password.length() >= 12) {
                rlLevel.setVisibility(View.VISIBLE);
                ivYellow.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    //检查密码的合法性
    private void checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            isPassOk = false;
            llEdPass.setBackgroundResource(R.drawable.bg_edit_error);
            tvPassStar.setTextColor(getResources().getColor(R.color.text_error));
            tvPassError.setText(getString(R.string.input_password));
            tvPassError.setVisibility(View.VISIBLE);
            return;
        } else if (password.length() < 8) {
            isPassOk = false;
            llEdPass.setBackgroundResource(R.drawable.bg_edit_error);
            tvPassStar.setTextColor(getResources().getColor(R.color.text_error));
            tvPassError.setText(getString(R.string.format_error));
            tvPassError.setVisibility(View.VISIBLE);
            return;
        } else {
            String regEx4 = getString(R.string.patters_all);
            isPassOk = password.matches(regEx4);
            if (isPassOk) {
                llEdPass.setBackgroundResource(R.drawable.bg_edit_gray);
                tvPassStar.setTextColor(getResources().getColor(R.color.text_blue));
                tvPassError.setVisibility(View.GONE);
            } else {
                llEdPass.setBackgroundResource(R.drawable.bg_edit_error);
                tvPassStar.setTextColor(getResources().getColor(R.color.text_error));
                tvPassError.setText(getString(R.string.format_error));
                tvPassError.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    //检查重复密码的合法性
    private void checkRePassword(String repassword) {

        if (StringUtils.isEmpty(repassword)) {
            isRePassOk = false;
            tvRePassError.setText("Repeat The Password");
            tvRePassError.setVisibility(View.VISIBLE);
            llRePass.setBackgroundResource(R.drawable.bg_edit_error);
            tvRePassStar.setTextColor(getResources().getColor(R.color.text_error));
        } else {
            if (!repassword.equals(pass)) {
                isRePassOk = false;
                tvPassError.setText("Two passwords are inconsistent");
                tvPassError.setVisibility(View.VISIBLE);
                llRePass.setBackgroundResource(R.drawable.bg_edit_error);
                tvRePassStar.setTextColor(getResources().getColor(R.color.text_error));
                return;
            } else {
                isRePassOk = true;
                llRePass.setBackgroundResource(R.drawable.bg_edit_gray);
                tvRePassStar.setTextColor(getResources().getColor(R.color.text_blue));
                tvRePassError.setVisibility(View.GONE);
                tvPassError.setVisibility(View.GONE);
            }
        }
    }

    private void setBtnClickable() {
        if (isWallteNumberOk == false) {
            btnCreate.setEnabled(false);
            btnCreate.setBackground(getResources().getDrawable(R.drawable.bg_btn_cannot));
        } else {
            if (isWallteNameOk && isPassOk && isRePassOk) {
                btnCreate.setEnabled(true);
                btnCreate.setBackground(getResources().getDrawable(R.drawable.bg_btn));
            }
        }
    }

    @OnClick({R.id.btn_create_new, R.id.tv_right, R.id.cb_agree, R.id.tv_agreement, R.id.iv_clear1, R.id.iv_clear2,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_new:
                //避免连续点击
                if (ClickUtil.isNotFirstClick()) {
                    return;
                }
                remind = edPassReminder.getText().toString().trim();
                dialogLoading.show();

                imgIcon = (int) (Math.random() * 5);
                //产生助记词
                getMnes();
                //私钥  公钥
//                    ECKeyPair ecKeyPair = OwnWalletUtils.geECKeyPair(mnemonics, "123456789");
                ecKeyPair = OwnWalletUtils.generateKeyPair(mnemonics);
                Log.d("web3", "助记词+++" + mnemonics);
                Log.d("web3", "私钥+++" + ecKeyPair.getPrivateKey().toString(16));
                Log.d("web3", "公钥+++" + ecKeyPair.getPublicKey().toString(16));
                task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (null == walletFile) {
                                walletFile = Wallet.createLight(rePass, ecKeyPair);
                            }
                        } catch (CipherException e) {
                            e.printStackTrace();
                        }
                        if (StringUtils.isEmpty(walletAddress)) {
                            if (walletFile.getAddress().length() > 0) {
                                walletAddress = Constant.PREFIX_16 + walletFile.getAddress();
                                Log.d("web3", "地址+++" + walletAddress);
                                Message message1 = new Message();
                                message1.what = 1;
                                message1.obj = walletAddress;
                                if (canSend && null != task) {
                                    handler.sendMessage(message1);
                                }
                            }
                        }
                    }
                };
                if (null != task) {
                    timer.schedule(task, 0, 1000);
                }
                break;
            case R.id.tv_right:
                UiHelper.startActyImportWalletActivity(this);
                break;
            case R.id.cb_agree:
                if (iChecked) {
                    iChecked = false;
                    cbAgree.setBackground(getResources().getDrawable(R.drawable.cb_agree_no));
                } else {
                    iChecked = true;
                    cbAgree.setBackground(getResources().getDrawable(R.drawable.cb_agree_yes));
                }
                setBtnClickable();
                break;
            case R.id.tv_agreement:
                break;
            case R.id.iv_clear1:
                edPass.setText("");
                tvPassError.setText("Input Password");
                tvPassError.setVisibility(View.VISIBLE);
                rlLevel.setVisibility(View.GONE);
                break;
            case R.id.iv_clear2:
                edRepass.setText("");
                tvRePassError.setText("Repeat The Password");
                tvRePassError.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (null != task) {
            task.cancel();
            task = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    //产生没有重复单词的助记词
    private String getMnes() {
        boolean hasRepeat = false;
        while (hasRepeat == false) {
            mnemonics = OwnWalletUtils.generateMnemonics();
            String[] strings = mnemonics.split("\\s+");
            hasRepeat = StringUtils.cheakIsRepeat(strings);
        }
        return mnemonics;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case GlobalMessageType.RequestCode.FromCreate:
                    iChecked = data.getBooleanExtra("iChecked", false);
                    setEnable(iChecked);
                    break;
            }
        }
    }

    private void setEnable(boolean isOK) {
        if (isOK) {
            cbAgree.setBackground(getResources().getDrawable(R.drawable.cb_agree_yes));
        } else {
            cbAgree.setBackground(getResources().getDrawable(R.drawable.cb_agree_no));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void setOtherWalletFalse() {
        if (null != map && !map.isEmpty() || map.values().size() > 0) {
            for (RememberSEC reme : map.values()) {
                if (reme.isNow() == true) {
                    reme.setNow(false);
                    map.put(reme.getAddress(), reme);
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                    break;
                }
            }
        }
    }
}
