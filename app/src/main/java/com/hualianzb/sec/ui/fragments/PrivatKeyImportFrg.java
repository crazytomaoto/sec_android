package com.hualianzb.sec.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.activitys.ServiceAgreementAct;
import com.hualianzb.sec.ui.basic.BasicFragment;
import com.hualianzb.sec.utils.ClickUtil;
import com.hualianzb.sec.utils.OwnWalletUtils;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PrivatKeyImportFrg extends BasicFragment {
    @BindView(R.id.ed_private_key)
    EditText edPrivateKey;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.ed_repass)
    EditText edRepass;
    @BindView(R.id.ed_tips)
    EditText edTips;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.btn_start_import)
    TextView btnStartImport;
    Unbinder unbinder;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    private PrivatKeyImportFrg instance;
    private View view;
    private Map<String, RememberEth> map;
    private String initWalletName;
    private RememberEth rememberEth, selectedReme;
    private String myPrivatekey;
    private String pass;
    private String rePass;
    private String tips;
    private boolean isCan = false;
    private ECKeyPair ecKeyPair;
    private WalletFile walletFile;

    private String walletName, remind;
    private int imgIcon;
    //钱包地址
    private String walletAddress = null;
    private boolean canSend = true;
    private boolean canClick = false;
    private KProgressHUD hud;
    Timer timer = new Timer(true);
    TimerTask task = new TimerTask() {
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
//                    rememberEth.setWalletFile(walletFile);
//                            walletAddress = OwnWalletUtils.generateNewWalletFile("123456789", new File(this.getFilesDir(), ""), false);
                    rememberEth.setAddress(walletAddress);
                    rememberEth.setWalletincon(imgIcon);
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
    //任务
    TimerTask task1 = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 111;
            if (null != ecKeyPair) {
                handler.sendMessage(msg);
                task1.cancel();
            }
        }
    };


    TimerTask task3 = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 333;
            if (null != walletFile) {
                handler.sendMessage(msg);
                task3.cancel();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 222:
                    canSend = false;
                    String myAddr = (String) msg.obj;
                    if (myAddr.length() > 2) {
                        rememberEth.setPass(rePass);
                        if (!StringUtils.isEmpty(tips)) {
                            rememberEth.setTips(tips);
                        }
                        rememberEth.setWalletName(initWalletName);
                        rememberEth.setWalletFile(JSON.toJSONString(walletFile));
                        rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                        rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                        rememberEth.setHowToCreate(3);
                        rememberEth.setCreatTime(TimeUtil.getDate());
                        if (map != null && map.values().size() > 0) {
                            for (RememberEth rememb : map.values()) {
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
                        rememberEth.setNow(true);
                        map.put(walletAddress, rememberEth);
                        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);

                        Util.saveTokenKindsForEacthWallet(rememberEth.getAddress());
                        PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址

                        hud.dismiss();
                        UiHelper.startHomaPageAc(getActivity(), walletAddress);
                        getActivity().finish();
                    }
                    break;
                case 333:
                    if (StringUtils.isEmpty(walletAddress)) {
                        if (walletFile.getAddress().length() > 0) {
                            walletAddress = Constant.PREFIX_16 + walletFile.getAddress();
                            if (map != null && map.values().size() > 0) {
                                boolean hasWallet = false;//钱包是否已经存在
                                for (RememberEth rememb : map.values()) {
                                    if (rememb.getAddress().equals(walletAddress)) {
                                        hasWallet = true;
                                        break;
                                    }
                                }
                                if (hasWallet == true) {
                                    ToastUtil.show(getActivity(), "钱包已存在");
                                    return;
                                }
                            }
                            rememberEth.setAddress(walletAddress);
                            rememberEth.setWalletincon(imgIcon);
                            Log.d("web3", "地址+++" + walletAddress);
                            Message message1 = new Message();
                            message1.what = 222;
                            message1.obj = walletAddress;
                            if (canSend && null != task) {
                                handler.sendMessage(message1);
                            }
                        }
                    }
                    break;
                case 111:
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        walletFile = OwnWalletUtils.getKeyStore(rePass, ecKeyPair, false);
                                    } catch (CipherException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ) {
                    }.start();
                    timer.schedule(task3, 0, 1000);
                    break;
            }
        }
    };
    private int walletNowSize = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmengt_privatekey_import, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  导入中  ")
                .setCancellable(false);
    }

    private void initData() {
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            map = new HashMap<>();
            initWalletName = "wallet01";
        } else {
            walletNowSize = map.values().size();
            initWalletName(walletNowSize);
        }
        rememberEth = new RememberEth();

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                canClick = isChecked;
                setEnable(isChecked);
            }
        });
        setEnable(canClick);
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
    }

    @Override
    protected void initLogics() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_agreement, R.id.btn_start_import, R.id.iv_clear1, R.id.iv_clear2, R.id.tv_what_private})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                Intent intent = new Intent(getActivity(), ServiceAgreementAct.class);
                intent.putExtra("isFromGuide", false);
                intent.putExtra("from", GlobalMessageType.RequestCode.FromPrivatKey);
                startActivityForResult(intent, GlobalMessageType.RequestCode.FromPrivatKey);
                break;
            case R.id.btn_start_import:
                //避免连续点击
                if (ClickUtil.isNotFirstClick()) {
                    return;
                }
                if (isCan = false) {
                    ToastUtil.show(getActivity(), "请仔细阅读服务条款，并同意");
                    return;
                }
                hud.show();
                myPrivatekey = edPrivateKey.getText().toString().trim();
                pass = edPass.getText().toString().trim();
                rePass = edRepass.getText().toString().trim();
                tips = edTips.getText().toString().trim();
                if (StringUtils.isEmpty(myPrivatekey)) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "明文私钥为空或格式不正确");
                    return;
                } else if (myPrivatekey.length() != 64) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "明文私钥为空或格式不正确");
                    return;
                }
                if (StringUtils.isEmpty(pass)) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "密码为空");
                    return;
                }
                if (StringUtils.isEmpty(rePass)) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "重复密码为空");
                    return;
                }

                String regEx4 = getString(R.string.patters_all);

                boolean boolLast = pass.matches(regEx4);

                if (boolLast == false) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "密码格式不正确");
                    return;
                }

                if (StringUtils.isEmpty(rePass) || !rePass.equals(pass)) {
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "两次密码不一致");
                    return;
                }
                if (null != map && !map.isEmpty() && map.values().size() > 0) {
                    boolean isHave = false;
                    for (RememberEth rememberEth : map.values()) {
                        if (myPrivatekey.equals(rememberEth.getPrivateKey())) {
                            isHave = true;
                            break;
                        }
                    }
                    if (isHave) {
                        hud.dismiss();
                        ToastUtil.show(getActivity(), "钱包已存在");
                        return;
                    }
                }
                imgIcon = (int) (Math.random() * 5);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ecKeyPair = OwnWalletUtils.getKeyPair(myPrivatekey);
//                        if (null != ecKeyPair) {
//                            rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
//                            rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
//                        }
                    }
                }.start();
                timer.schedule(task1, 0, 1000);
                break;
            case R.id.iv_clear1:
                pass = "";
                edPass.setText(pass);
                break;
            case R.id.iv_clear2:
                rePass = "";
                edRepass.setText(rePass);
                break;
            case R.id.tv_what_private:
                UiHelper.startIntroducePKActy(getActivity());
                break;
        }
    }

    private void setEnable(boolean isOK) {
        if (isOK) {
            btnStartImport.setClickable(true);
            btnStartImport.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_shadow_my));
            btnStartImport.setTextColor((getResources().getColor(R.color.white)));
        } else {
            btnStartImport.setClickable(false);
            btnStartImport.setBackgroundColor(getActivity().getResources().getColor(R.color.line_color));
            btnStartImport.setTextColor((getResources().getColor(R.color.text_noChose)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (requestCode) {
                case GlobalMessageType.RequestCode.FromPrivatKey:
                    canClick = data.getBooleanExtra("iChecked", false);
                    cbAgree.setChecked(canClick);
                    setEnable(canClick);
                    break;
            }
        }
    }

}

