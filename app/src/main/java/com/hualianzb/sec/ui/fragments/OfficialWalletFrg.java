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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.activitys.ServiceAgreementAct;
import com.hualianzb.sec.ui.basic.BasicFragment;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OfficialWalletFrg extends BasicFragment {
    @BindView(R.id.ed_json)
    EditText edJson;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.cb_agree)
    CheckBox cb_Agree;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.btn_start_import)
    TextView btnStartImport;
    Unbinder unbinder;
    private OfficialWalletFrg instance;
    private View view;
    private String initWalletName;
    private Map<String, RememberEth> map;
    private RememberEth rememberEth, selectedReme;
    private String address;
    private boolean isAgree = false;
    private WalletFile walletFile = null;
    private ECKeyPair ecKeyPair;
    private String pass;
    private int walletNowSize = 0;
    private KProgressHUD hud;
    private Message message = new Message();
    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (null != walletFile && !StringUtils.isEmpty(walletFile.getAddress())) {
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    };
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            if (null != ecKeyPair) {
                Log.e("web3", "私钥+++" + ecKeyPair.getPrivateKey().toString());
                Message messa2 = new Message();
                messa2.what = 2;
                handler.sendMessage(messa2);
            }

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    task.cancel();
//                    try {
//                        ecKeyPair = Wallet.decrypt(pass, walletFile);
//                        Log.e("web3", "-----" + pass);
//                    } catch (CipherException e) {
//                        e.printStackTrace();
//                    }
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                ecKeyPair = Wallet.decrypt(pass, walletFile);
                            } catch (CipherException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    timer.schedule(task2, 0, 1000);
                    break;
                case 2:
//                    task2.cancel();
                    address = Constant.PREFIX_16 + walletFile.getAddress();
                    if (map != null && map.values().size() > 0) {
                        boolean hasWallet = false;//钱包是否已经存在
                        for (RememberEth rememb : map.values()) {
                            if (rememb.getAddress().equals(address)) {
                                hasWallet = true;
                                break;
                            }
                        }
                        if (hasWallet == true) {
                            ToastUtil.show(getActivity(), "钱包已存在");
                            return;
                        }
                    }


                    rememberEth = new RememberEth();
                    rememberEth.setWalletName(initWalletName);
                    rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                    rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                    rememberEth.setWalletFile(JSON.toJSONString(walletFile));
                    rememberEth.setPass(pass);
                    rememberEth.setAddress(address);
                    rememberEth.setCreatTime(TimeUtil.getDate());
                    rememberEth.setHowToCreate(4);

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
                    map.put(address, rememberEth);
                    task2.cancel();
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);

                    Util.saveTokenKindsForEacthWallet(rememberEth.getAddress());
                    PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址


                    hud.dismiss();
                    UiHelper.startHomaPageAc(getActivity(), address);
                    getActivity().finish();
                    break;
                case 30:
                    hud.dismiss();
                    ToastUtil.show(getActivity(), "Keystore文件为空或格式不正确");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmengt_official_wallet, container, false);
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
        edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass = s.toString().trim();
                if (!StringUtils.isEmpty(pass)) {
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        cb_Agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgree = isChecked;
                setEnable(isAgree);
            }
        });
        setEnable(isAgree);
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

    @Override
    protected void initLogics() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_agreement, R.id.btn_start_import, R.id.iv_clear, R.id.tv_what_keys})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                Intent intent = new Intent(getActivity(), ServiceAgreementAct.class);
                intent.putExtra("isFromGuide", false);
                intent.putExtra("from", GlobalMessageType.RequestCode.FromOfficialFragment);
                startActivityForResult(intent, GlobalMessageType.RequestCode.FromOfficialFragment);
                break;
            case R.id.btn_start_import:
                if (!isAgree) {
                    ToastUtil.show(getActivity(), "请仔细阅读并同意 服务条款");
                    return;
                }
                String json = edJson.getText().toString().trim();
                pass = edPass.getText().toString().trim();
                ObjectMapper objectMapper = new ObjectMapper();

                if (StringUtils.isEmpty(json)) {
//                    hud.dismiss();
                    ToastUtil.show(getActivity(), "keystore文件为空或格式不正确");
                    return;
                }
                if (StringUtils.isEmpty(pass)) {
//                    hud.dismiss();
                    ToastUtil.show(getActivity(), "密码为空");
                    return;
                }

                if (map == null || map.isEmpty() || map.values().size() == 0) {
                    map = new HashMap<>();
                } else {
                    boolean isExist = false;
                    for (RememberEth reth : map.values()) {
                        if (reth.getWalletFile().equals(json)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        ToastUtil.show(getActivity(), "钱包已存在");
                        return;
                    }
                }


                hud.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            walletFile = objectMapper.readValue(json, WalletFile.class);//                                        walletFile = Wallet.createLight(rePass, ecKeyPair);
                        } catch (IOException e) {
                            Message message30 = new Message();
                            message30.what = 30;
                            handler.sendMessage(message30);
                            e.printStackTrace();
                        }
                    }
                }.start();
                timer.schedule(task, 0, 1000);
                break;
            case R.id.iv_clear:
                pass = "";
                edPass.setText(pass);
                break;
            case R.id.tv_what_keys:
                UiHelper.startIntroduceKeystoreActy(getActivity());
                break;
        }
    }

    private void setEnable(boolean isOK) {
        if (isOK) {
            btnStartImport.setClickable(true);
            btnStartImport.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_shadow_my));
            btnStartImport.setTextColor((getActivity().getResources().getColor(R.color.white)));
        } else {
            btnStartImport.setClickable(false);
            btnStartImport.setBackgroundColor(getActivity().getResources().getColor(R.color.line_color));
            btnStartImport.setTextColor((getActivity().getResources().getColor(R.color.text_noChose)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (requestCode) {
                case GlobalMessageType.RequestCode.FromOfficialFragment:
                    isAgree = data.getBooleanExtra("iChecked", false);
                    cb_Agree.setChecked(isAgree);
                    setEnable(isAgree);
                    break;
            }
        }
    }

}

