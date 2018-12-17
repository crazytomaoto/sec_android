package com.hualianzb.sec.ui.activitys;

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
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.models.TokenBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.ClickUtil;
import com.hualianzb.sec.utils.OwnWalletUtils;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateWalletActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_layout_work_top)
    ImageView ivLayoutWorkTop;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
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
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.btn_create)
    TextView btnCreate;
    @BindView(R.id.tv_import)
    TextView tvImport;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    private KProgressHUD hud;
    private StateBarUtil stateBarUtil;
    private boolean iChecked = false;
    private String walletName, pass, rePass, remind;
    //钱包地址
    private String walletAddress = null;
    private int imgIcon;//0-5之间的随机整数
    private RememberEth rememberEth, oldReme;
    private String mnemonics;
    private ECKeyPair ecKeyPair;
    private WalletFile walletFile = null;
    private String tips;
    private Map<String, RememberEth> map;
    private Map<String, ArrayList<TokenBean>> mapTokenBean;
    private Map<String, ArrayList<String>> mapName;
    private String initWalletName;
    private int walletNowSize = 0;
    private boolean canSend = true;
    boolean boolLast = false;//密码三种规则满足其中之二才可以
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
                        hud.dismiss();
                        UiHelper.startBackupMnemonicsActy1(CreateWalletActivity.this, rememberEth);
                    }
                    break;
            }
        }
    };

    @Override
    protected void initLogics() {
        super.initLogics();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  创建中  ")
                .setCancellable(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvTitle.setText("创建钱包");
        rememberEth = new RememberEth();
        map = new HashMap<>();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            map = new HashMap<>();
            initWalletName = "wallet01";
        } else {
            walletNowSize = map.values().size();
            initWalletName(walletNowSize);
        }
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edWname.setHint(initWalletName);
        setEnable(iChecked);

    }

    private void initWalletName(int walletNowSize) {
        if (walletNowSize == 9) {
            initWalletName = "wallet10";
        }
        if (walletNowSize > 9) {
            initWalletName = "wallet" + (walletNowSize + 1);
        }
        if (walletNowSize < 9) {
            initWalletName = "wallet0" + (walletNowSize + 1);
        }
        for (RememberEth rememberEth : map.values()) {
            if (initWalletName.equals(rememberEth.getWalletName())) {
                walletNowSize++;
                initWalletName(walletNowSize);
                break;
            }
        }
    }

    @OnClick({R.id.btn_create, R.id.tv_import, R.id.tv_agreement, R.id.iv_clear1, R.id.iv_clear2, R.id.cb_agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                //避免连续点击
                if (ClickUtil.isNotFirstClick()) {
                    return;
                }
                if (iChecked == false) {
                    ToastUtil.show(this, "请仔细阅读并同意 服务条款");
                    return;
                }
                walletName = edWname.getText().toString().trim();
                pass = edPass.getText().toString().trim();
                rePass = edRepass.getText().toString().trim();
                remind = edPassReminder.getText().toString().trim();

                if (StringUtils.isEmpty(walletName)) {
                    walletName = initWalletName;
                }

                if (map != null && map.values().size() > 0) {
                    boolean isRe = false;
                    for (RememberEth reme : map.values()) {
                        if (reme.getWalletName().equals(walletName)) {
                            isRe = true;
                            break;
                        }
                    }
                    if (isRe) {
                        ToastUtil.show(this, "钱包名称重复");
                        return;
                    }
                }


                if (StringUtils.isEmpty(pass)) {
                    ToastUtil.show(this, "密码为空");
                    return;
                }

                String regEx4 = getString(R.string.patters_all);

                boolLast = pass.matches(regEx4);

                if (boolLast == false) {
                    ToastUtil.show(this, "密码格式不正确");
                    return;
                }
                if (StringUtils.isEmpty(rePass) || !rePass.equals(pass)) {
                    ToastUtil.show(this, "两次密码不一致");
                    return;
                }

                hud.show();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        hud.show();
//                    }
//                });
                imgIcon = (int) (Math.random() * 5);
                //助记词
                mnemonics = OwnWalletUtils.generateMnemonics();
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

//                    SECApplication.getInstance().setMap(rememberEth);
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
            case R.id.tv_import:
                UiHelper.startActyImportWalletActivity(this);
                break;
            case R.id.tv_agreement:
                UiHelper.startServiceAgreementAct(this, false, GlobalMessageType.RequestCode.FromCreate);
                break;
            case R.id.iv_clear1:
                pass = "";
                edPass.setText(pass);
                break;
            case R.id.iv_clear2:
                rePass = "";
                edRepass.setText(rePass);
                break;
            case R.id.cb_agree:
                if (iChecked) {
                    iChecked = false;
                } else {
                    iChecked = true;
                }
                setEnable(iChecked);
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

            btnCreate.setClickable(true);
            btnCreate.setBackground(getResources().getDrawable(R.drawable.bg_shadow_my));
            btnCreate.setTextColor((getResources().getColor(R.color.white)));
        } else {
            cbAgree.setBackground(getResources().getDrawable(R.drawable.cb_agree_no));

            btnCreate.setClickable(false);
            btnCreate.setBackgroundColor(getResources().getColor(R.color.line_color));
            btnCreate.setTextColor((getResources().getColor(R.color.text_noChose)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void setOtherWalletFalse() {
        if (null != map && !map.isEmpty() || map.values().size() > 0) {
            for (RememberEth reme : map.values()) {
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
