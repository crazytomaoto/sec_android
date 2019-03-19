package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.application.SECApplication.secJsApi;

public class ImportWalletActivity extends BasicActivity {

    @BindView(R.id.tv_mn)
    TextView tvMn;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.re_mn)
    RelativeLayout reMn;
    @BindView(R.id.tv_officialW)
    TextView tvOfficialW;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.rl_official_w)
    RelativeLayout rlOfficialW;
    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.rl_private_key)
    RelativeLayout rlPrivateKey;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_top)
    EditText edTop;
    @BindView(R.id.tv_official_tips)
    TextView tvOfficialTips;
    @BindView(R.id.tv_edit_top_error)
    TextView tvEditTopError;
    @BindView(R.id.ll_middle)
    LinearLayout llMiddle;
    @BindView(R.id.tv_pass_error)
    TextView tvPassError;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.tv_re_pass_error)
    TextView tvRePassError;
    @BindView(R.id.tv_mumber_error)
    TextView tv_mumber_error;
    @BindView(R.id.ed_repass)
    EditText edRepass;
    @BindView(R.id.ed_pass_reminder)
    EditText edPassReminder;
    @BindView(R.id.cb_agree)
    ImageView cbAgree;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_start_import)
    TextView tvStartImport;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.rl_re_pass)
    RelativeLayout rlRePass;
    @BindView(R.id.ll_prompt)
    LinearLayout llPrompt;
    @BindView(R.id.iv_red)
    ImageView ivRed;
    @BindView(R.id.iv_yellow)
    ImageView ivYellow;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    @BindView(R.id.rl_level)
    RelativeLayout rlLevel;
    //    private ViewPageAdapter viewPageAdapter;
//    private List listF;
    private int index = 0;
    private int passLevel = 0;
    private String topToString, mnemonics, officials, privatekeys, pass, rePass, tips;
    private boolean isNameOk = true;//钱包名称不参与判断，默认为真
    private boolean isMnOk, isOfficialOk, isPrivatekeyOk, isPassOk, isRePassOk;
    private Map<String, RememberSEC> map;
    private Dialog dialogLoading, twoButtonTipsDialog, errorPasaDialog;
    private int imgIcon;
    private RememberSEC rememberSec, selectedReme;
    private ECKeyPair ecKeyPair;
    private String walletAddress = null;
    private WalletFile walletFile = null;
    private boolean canGo = true;
    private String initWalletName = "New Import";
    private boolean isWallteNumberOk = true;//钱包数量限制10个
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x000002:
                    if (StringUtils.isEmpty(walletAddress)) {
                        if (walletFile.getAddress().length() > 0) {
                            walletAddress = Constant.PREFIX_16 + walletFile.getAddress();
                            rememberSec.setWalletFile(JSON.toJSONString(walletFile));
                            rememberSec.setAddress(walletAddress);
                            rememberSec.setWalletincon(imgIcon);
                            rememberSec.setPass(rePass);
                            rememberSec.setNow(true);
                            rememberSec.setWalletName(initWalletName);
                            rememberSec.setCreatTime(TimeUtil.getDate());
                            rememberSec.setHowToCreate(2);
                            tips = edPassReminder.getText().toString().trim();
                            if (!StringUtils.isEmpty(tips)) {
                                rememberSec.setTips(tips);
                            }
                            if (map != null && map.values().size() > 0) {
                                rememberSec.setNow(false);
                                map.put(rememberSec.getAddress(), rememberSec);
                            } else {
                                map = new HashMap<>();
                            }
                            map.put(walletAddress, rememberSec);
                            PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                            //以上是钱包信息，以下是每个钱包对应的币种
                            Util.saveTokenKindsForEacthWallet(rememberSec.getAddress());
                            PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberSec.getAddress());//记住当前选中钱包的地址
                            Log.d("web3", "地址+++" + walletAddress);
                        }
                    }
                    dialogLoading.dismiss();
                    UiHelper.startHomaPageAc(ImportWalletActivity.this, walletAddress);
                    finish();
                    break;
                case 0x000001:
                    new Thread(() -> {
                        try {
                            walletFile = OwnWalletUtils.getKeyStore(rePass, ecKeyPair, false);
//                                        walletFile = Wallet.createLight(rePass, ecKeyPair);
                            if (null != walletFile) {
                                Message msg2 = new Message();
                                msg2.what = 0x000002;
                                handler.sendMessage(msg2);
                            }
                        } catch (CipherException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    break;
                case 0x000003:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                ecKeyPair = Wallet.decrypt(rePass, walletFile);
                                if (null != ecKeyPair) {
                                    Message message = new Message();
                                    message.what = 0x000004;
                                    handler.sendMessage(message);
                                }
                            } catch (CipherException e) {
                                if (e.getMessage().equals("Invalid password provided")) {
                                    Message message = new Message();
                                    message.what = 0x000005;
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    }.start();
                    break;
                case 0x000004:
                    walletAddress = Constant.PREFIX_16 + walletFile.getAddress();
                    rememberSec = new RememberSEC();
                    rememberSec.setWalletName(initWalletName);
                    rememberSec.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                    rememberSec.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                    rememberSec.setWalletFile(JSON.toJSONString(walletFile));
                    rememberSec.setPass(rePass);
                    rememberSec.setAddress(walletAddress);
                    rememberSec.setCreatTime(TimeUtil.getDate());
                    rememberSec.setHowToCreate(4);
                    if (map != null && map.values().size() > 0) {
                        for (RememberSEC rememb : map.values()) {
                            if (rememb.isNow() == true) {
                                selectedReme = rememb;
                                break;
                            }
                        }
                    }
                    if (map != null && map.values().size() > 0) {
                        selectedReme.setNow(false);
                        map.put(selectedReme.getAddress(), selectedReme);
                    }
                    rememberSec.setNow(true);
                    map.put(walletAddress, rememberSec);
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                    Util.saveTokenKindsForEacthWallet(rememberSec.getAddress());
                    PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberSec.getAddress());//记住当前选中钱包的地址
                    dialogLoading.dismiss();
                    UiHelper.startHomaPageAc(ImportWalletActivity.this, walletAddress);
                    finish();
                    break;
                case 0x000005:
                    dialogLoading.dismiss();
                    errorPasaDialog.show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_wallet);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        tvTheme.setText("Import Wallet");
        dialogLoading = DialogUtil.showLoadingDialog(this, getString(R.string.importing));
        errorPasaDialog = DialogUtil.TipsDialog(this, R.drawable.error_icon, "Input Password",
                0, "Invalid Password.\nPlease Check and try again.", (v, d) -> d.dismiss());
        initView(index);
        initData();
    }

    private void initData() {
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            map = new HashMap<>();
        } else {
            if (map.values().size() == 10) {
                isWallteNumberOk = false;
                tv_mumber_error.setVisibility(View.VISIBLE);
            } else {
                isWallteNumberOk = true;
                tv_mumber_error.setVisibility(View.GONE);
            }
        }

        edTop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                topToString = s.toString().trim();
                checkMn(topToString);
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
                if (index != 1) {
                    checkPassStrength(pass);
                }
                checkPassword(pass);
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
                checkRePassword(rePass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkMn(String topToString) {
        switch (index) {
            case 0:
                mnemonics = topToString;
                if (StringUtils.isEmpty(mnemonics)) {
                    isMnOk = false;
                    tvEditTopError.setText("Input Phrase");
                    tvEditTopError.setVisibility(View.VISIBLE);
                } else {
                    String[] arr = mnemonics.split("\\s+");
                    if (arr.length != 24) {
                        isMnOk = false;
                        tvEditTopError.setText("Phrase Error");
                        tvEditTopError.setVisibility(View.VISIBLE);
                    } else {
                        tvEditTopError.setVisibility(View.GONE);
                        isMnOk = true;
                    }
                }

                break;
            case 1:
                officials = topToString;
                if (StringUtils.isEmpty(officials)) {
                    isOfficialOk = false;
                    tvEditTopError.setText("Official type error");
                    tvEditTopError.setVisibility(View.VISIBLE);
                } else {
                    isOfficialOk = true;
                    tvEditTopError.setVisibility(View.GONE);
                }
                break;
            case 2:
                privatekeys = topToString;
                if (StringUtils.isEmpty(privatekeys) || privatekeys.length() != 64) {
                    isPrivatekeyOk = false;
                    tvEditTopError.setText("PrivateKey type error");
                    tvEditTopError.setVisibility(View.VISIBLE);
                } else {
                    isPrivatekeyOk = true;
                    tvEditTopError.setVisibility(View.GONE);
                }
                break;
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
            tvPassError.setText("Please Input Password");
            tvPassError.setVisibility(View.VISIBLE);
            return;
        } else if (password.length() < 8) {
            isPassOk = false;
            tvPassError.setText(getString(R.string.format_error));
            tvPassError.setVisibility(View.VISIBLE);
            return;
        } else {
            String regEx4 = getString(R.string.patters_all);
            isPassOk = password.matches(regEx4);
            if (isPassOk) {
                tvPassError.setVisibility(View.GONE);
            } else {
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
            tvRePassError.setText("Please Repeat Password");
            tvRePassError.setVisibility(View.VISIBLE);
            return;
        } else {
            tvRePassError.setVisibility(View.GONE);
            if (!repassword.equals(pass)) {
                isRePassOk = false;
                tvRePassError.setText("Two passwords are inconsistent");
                tvRePassError.setVisibility(View.VISIBLE);
                return;
            } else {
                isRePassOk = true;
                tvRePassError.setVisibility(View.GONE);
            }
        }
    }

    private void setBtnClickable() {
        if (isWallteNumberOk == false) {
            tvStartImport.setEnabled(false);
            tvStartImport.setBackground(getResources().getDrawable(R.drawable.bg_btn_cannot));
        } else {
            switch (index) {
                case 0:
                    if (isNameOk && isMnOk && isPassOk && isRePassOk) {
                        tvStartImport.setEnabled(true);
                        tvStartImport.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                    }
                    break;
                case 1:
                    if (isPassOk && isOfficialOk) {
                        tvStartImport.setEnabled(true);
                        tvStartImport.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                    }
                    break;
                case 2:
                    if (isNameOk && isPrivatekeyOk && isPassOk && isRePassOk) {
                        tvStartImport.setEnabled(true);
                        tvStartImport.setBackground(getResources().getDrawable(R.drawable.bg_btn));
                    }
                    break;
            }
        }
    }

    @OnClick({R.id.re_mn, R.id.rl_official_w, R.id.rl_private_key, R.id.tv_right, R.id.tv_agreement, R.id.tv_start_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_mn:
                index = 0;
                initView(index);
                break;
            case R.id.rl_official_w:
                index = 1;
                initView(index);
                break;
            case R.id.rl_private_key:
                index = 2;
                initView(index);
                break;
            case R.id.tv_right:
                switch (index) {
                    case 0:
                        UiHelper.startIntroduceMnActy(this);
                        break;
                    case 1:
                        UiHelper.startIntroduceKeystoreActy(this);
                        break;
                    case 2:
                        UiHelper.startIntroducePKActy(this);
                        break;
                }
                break;
            case R.id.tv_agreement:
                break;
            case R.id.tv_start_import:
                //避免连续点击
                if (ClickUtil.isNotFirstClick()) {
                    return;
                }
                switch (index) {
                    case 0:
                        dialogLoading.show();
                        imgIcon = (int) (Math.random() * 5);
                        rememberSec = new RememberSEC();
                        //助记词
                        rememberSec.setMnemonics(mnemonics);
                        String privateKey = secJsApi.MnemonicToEntropy(mnemonics);
                        ecKeyPair = OwnWalletUtils.getKeyPair(privateKey);
                        if (null != ecKeyPair) {
                            rememberSec.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                            rememberSec.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                            checkExistPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                            generateStep();
                        }
                        break;
                    case 1:
                        dialogLoading.show();
                        checkExistpublicKey(officials);
                        ObjectMapper objectMapper = new ObjectMapper();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    walletFile = objectMapper.readValue(officials, WalletFile.class);//walletFile = Wallet.createLight(rePass, ecKeyPair);
                                    if (null != walletFile && !StringUtils.isEmpty(walletFile.getAddress())) {
                                        message.what = 0x000003;
                                        handler.sendMessage(message);
                                    }
                                } catch (IOException e) {
                                    Message message30 = new Message();
                                    message30.what = 30;
                                    handler.sendMessage(message30);
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        break;
                    case 2:
                        dialogLoading.show();
                        imgIcon = (int) (Math.random() * 5);
                        rememberSec = new RememberSEC();

                        ecKeyPair = OwnWalletUtils.getKeyPair(topToString);
                        if (null != ecKeyPair) {
                            rememberSec.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                            rememberSec.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                            checkExistPrivateKey(topToString);
                            generateStep();
                        }
                        break;
                }
                break;
        }
    }

    private Message message = new Message();

    private void initView(int index) {
        tvMn.setTextColor(getResources().getColor(R.color.text_gray));
        TextPaint tpMn = tvMn.getPaint();
        tpMn.setFakeBoldText(false);
        line1.setVisibility(View.GONE);

        tvOfficialW.setTextColor(getResources().getColor(R.color.text_gray));
        TextPaint tpoFF = tvOfficialW.getPaint();
        tpoFF.setFakeBoldText(false);
        line2.setVisibility(View.GONE);

        tvPrivateKey.setTextColor(getResources().getColor(R.color.text_gray));
        TextPaint tpoPk = tvPrivateKey.getPaint();
        tpoPk.setFakeBoldText(false);
        line3.setVisibility(View.GONE);

        tvOfficialTips.setVisibility(View.GONE);
        tvEditTopError.setText("");
        tvEditTopError.setVisibility(View.GONE);
        switch (index) {
            case 0:
                tvRight.setText("What is phrase?");
                tvMn.setTextColor(getResources().getColor(R.color.text_green_increase));
                line1.setVisibility(View.VISIBLE);
                tpMn.setFakeBoldText(true);
                llMiddle.setVisibility(View.VISIBLE);
                edTop.setHint(R.string.import_mn_hint);
                goneSomeView();
                break;
            case 1:
                tvRight.setText("What is a keystore?");
                tvOfficialW.setTextColor(getResources().getColor(R.color.text_green_increase));
                line2.setVisibility(View.VISIBLE);
                tpoFF.setFakeBoldText(true);
                tvOfficialTips.setVisibility(View.VISIBLE);
                edTop.setHint(R.string.import_official_hint);
                llMiddle.setVisibility(View.VISIBLE);
                goneSomeView();
                rlRePass.setVisibility(View.GONE);
                llPrompt.setVisibility(View.GONE);
                break;
            case 2:
                tvRight.setText("What is a private key?");
                tvPrivateKey.setTextColor(getResources().getColor(R.color.text_green_increase));
                line3.setVisibility(View.VISIBLE);
                tpoPk.setFakeBoldText(true);
                llMiddle.setVisibility(View.VISIBLE);
                edTop.setHint(R.string.import_privateKey_hint);
                goneSomeView();
                break;
        }
    }

    private void generateStep() {
        new Thread(() -> {
            try {
                walletFile = OwnWalletUtils.getKeyStore(rePass, ecKeyPair, false);
                if (null != walletFile) {
                    Message msg2 = new Message();
                    msg2.what = 0x000002;
                    handler.sendMessage(msg2);
                }
            } catch (CipherException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //获得ekeypair以后就可以根据私钥检测钱包是否已经存在了
    private void checkExistPrivateKey(String privateKey) {
        boolean isExist = false;//是否已经存在
        RememberSEC existSECBean = null;
        if (map != null && map.values().size() > 0) {
            for (RememberSEC rememb : map.values()) {
                if (rememb.getPrivateKey().equals(privateKey)) {
                    isExist = true;//存在了
                    existSECBean = rememb;
                    break;
                }
            }

        }
        if (isExist) {
            dialogLoading.dismiss();
            RememberSEC finalExistSECBean = existSECBean;
            twoButtonTipsDialog = DialogUtil.twoButtonTipsDialog(this, R.drawable.icon_confire_blue,
                    "Are you sure",
                    getString(R.string.wallet_exist), (v, d) -> {
                        finalExistSECBean.setWalletName(initWalletName);
                        finalExistSECBean.setPass(rePass);
                        finalExistSECBean.setNow(true);
                        finalExistSECBean.setCreatTime(finalExistSECBean.getCreatTime());
                        if (!StringUtils.isEmpty(edPassReminder.getText().toString().trim())) {
                            finalExistSECBean.setTips(edPassReminder.getText().toString().trim());
                        } else {
                            finalExistSECBean.setTips("");
                        }
                        if (index == 0) {
                            finalExistSECBean.setHowToCreate(2);
                        }
                        if (index == 2) {
                            finalExistSECBean.setHowToCreate(3);
                        }
                        map.put(finalExistSECBean.getAddress(), finalExistSECBean);
                        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                        twoButtonTipsDialog.dismiss();
                        UiHelper.startHomaPageAc(ImportWalletActivity.this, finalExistSECBean.getAddress());
                        finish();
                    });
            twoButtonTipsDialog.show();
        }
    }

    //获得ekeypair以后就可以根据私钥检测钱包是否已经存在了
    private void checkExistpublicKey(String publicKey) {
        boolean isExist = false;//是否已经存在
        RememberSEC existSECBean = null;
        if (map != null && map.values().size() > 0) {
            for (RememberSEC rememb : map.values()) {
                if (rememb.getPublicKey().equals(publicKey)) {
                    isExist = true;//存在了
                    existSECBean = rememb;
                    break;
                }
            }

        }
        if (isExist) {
            dialogLoading.dismiss();
            RememberSEC finalExistSECBean = existSECBean;
            twoButtonTipsDialog = DialogUtil.twoButtonTipsDialog(this, R.drawable.icon_confire_blue,
                    "Are you sure",
                    getString(R.string.wallet_exist), (v, d) -> {
                        finalExistSECBean.setWalletName(initWalletName);
                        finalExistSECBean.setPass(rePass);
                        finalExistSECBean.setNow(true);
                        finalExistSECBean.setCreatTime(finalExistSECBean.getCreatTime());
                        if (!StringUtils.isEmpty(edPassReminder.getText().toString().trim())) {
                            finalExistSECBean.setTips(edPassReminder.getText().toString().trim());
                        } else {
                            finalExistSECBean.setTips("");
                        }
                        finalExistSECBean.setHowToCreate(4);
                        map.put(finalExistSECBean.getAddress(), finalExistSECBean);
                        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                        twoButtonTipsDialog.dismiss();
                        UiHelper.startHomaPageAc(ImportWalletActivity.this, finalExistSECBean.getAddress());
                        finish();
                    });
            twoButtonTipsDialog.show();
        }
    }

    private void goneSomeView() {
        edTop.setText("");
        edPass.setText("");
        edRepass.setText("");
        edPassReminder.setText("");
        tvEditTopError.setVisibility(View.GONE);
        tvPassError.setVisibility(View.GONE);
        tvRePassError.setVisibility(View.GONE);
        edPass.setBackgroundResource(R.drawable.bg_edit_gray);
        edRepass.setBackgroundResource(R.drawable.bg_edit_gray);
    }
}
