package com.hualianzb.sec.ui.fragments;

import android.annotation.SuppressLint;
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
import org.web3j.crypto.WalletFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class MnemonicImportFr extends BasicFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.ed_mn)
    EditText edMn;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.ed_repass)
    EditText edRepass;
    @BindView(R.id.ed_tips)
    EditText edTips;
    @BindView(R.id.cb_agree_mn)
    CheckBox cbAgree_mn;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.btn_start_import)
    TextView btnStartImport;
    Unbinder unbinder;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    private MnemonicImportFr instance;
    private View view;

    private Map<String, RememberEth> map;
    private String pass, rePass, remind;
    private boolean iCheck = false;
    private RememberEth rememberEth, selectedReme;
    private ECKeyPair ecKeyPair;
    private String mnemonics;
    private String walletAddress = null;
    private boolean canGo = true;
    private WalletFile walletFile = null;
    private int imgIcon;
    Timer timer = new Timer();
    private KProgressHUD hud;
    private String initWalletName;
    private int walletNowSize = 0;

    public MnemonicImportFr getInstance() {
        return instance;
    }

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
    TimerTask task2 = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 222;
            if (null != walletFile) {
                handler.sendMessage(msg);
                task2.cancel();
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 222:
                    if (StringUtils.isEmpty(walletAddress)) {
                        if (walletFile.getAddress().length() > 0) {
                            walletAddress = Constant.PREFIX_16 + walletFile.getAddress();
                            if (map != null && map.values().size() > 0) {
                                for (RememberEth rememb : map.values()) {
                                    if (rememb.isNow() == true) {
                                        selectedReme = rememb;
                                    }
                                    if (rememb.getAddress().equals(walletAddress)) {
                                        canGo = false;
                                        break;
                                    }
                                }
                            }
                            if (!canGo) {
                                ToastUtil.show(getActivity(), "钱包已存在");
                                hud.dismiss();
                                return;
                            }
                            rememberEth.setWalletFile(JSON.toJSONString(walletFile));
                            rememberEth.setAddress(walletAddress);
                            rememberEth.setWalletincon(imgIcon);
                            rememberEth.setNow(true);
                            rememberEth.setWalletName(initWalletName);
                            rememberEth.setCreatTime(TimeUtil.getDate());
                            rememberEth.setHowToCreate(2);
                            if (map != null && map.values().size() > 0) {
                                selectedReme.setNow(false);
                                map.put(selectedReme.getAddress(), selectedReme);
                            }
                            map.put(walletAddress, rememberEth);
                            PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                            //以上是钱包信息，以下是每个钱包对应的币种
                            Util.saveTokenKindsForEacthWallet(rememberEth.getAddress());
                            PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址

                            Log.d("web3", "地址+++" + walletAddress);
                        }
                    }
                    hud.dismiss();
                    UiHelper.startHomaPageAc(getActivity(), walletAddress);
                    getActivity().finish();
                    break;
                case 111:
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        walletFile = OwnWalletUtils.getKeyStore(rePass, ecKeyPair, false);
//                                        walletFile = Wallet.createLight(rePass, ecKeyPair);
                                    } catch (CipherException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ) {
                    }.start();
                    timer.schedule(task2, 0, 1000);
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
        view = inflater.inflate(R.layout.frg_import_mn, container, false);
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

    private void initData() {
        map = new HashMap<>();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            map = new HashMap<>();
            initWalletName = "wallet01";
        } else {
            walletNowSize = map.values().size();
            initWalletName(walletNowSize);
        }
        cbAgree_mn.setOnCheckedChangeListener(this);
        setEnable(iCheck);
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

    @OnClick({R.id.tv_agreement, R.id.btn_start_import, R.id.iv_clear1, R.id.iv_clear2, R.id.tv_mn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                Intent intent = new Intent(getActivity(), ServiceAgreementAct.class);
                intent.putExtra("isFromGuide", false);
                intent.putExtra("from", GlobalMessageType.RequestCode.FromMnFragment);
                startActivityForResult(intent, GlobalMessageType.RequestCode.FromMnFragment);
                break;
            case R.id.btn_start_import:
                if (!iCheck) {
                    ToastUtil.show(getActivity(), "请仔细阅读并同意 服务条款");
                    return;
                }
                pass = edPass.getText().toString().trim();
                rePass = edRepass.getText().toString().trim();
                remind = edTips.getText().toString().trim();
                mnemonics = edMn.getText().toString().trim();
                if (StringUtils.isEmpty(mnemonics)) {
                    ToastUtil.show(getActivity(), "助记词为空");
                    return;
                }
                if (StringUtils.isEmpty(pass)) {
                    ToastUtil.show(getActivity(), "密码为空");
                    return;
                }
                String regEx4 = getString(R.string.patters_all);

                boolean boolLast = pass.matches(regEx4);

                if (boolLast == false) {
                    ToastUtil.show(getActivity(), "密码格式不正确");
                    return;
                }
                if (StringUtils.isEmpty(rePass) || !rePass.equals(pass)) {
                    ToastUtil.show(getActivity(), "两次密码不一致");
                    return;
                }
                hud.show();
                imgIcon = (int) (Math.random() * 5);
                rememberEth = new RememberEth();
                rememberEth.setPass(edPass.getText().toString().trim());
                rememberEth.setTips(edTips.getText().toString().trim());
                //助记词
                rememberEth.setMnemonics(mnemonics);
                Log.d("web3", "助记词+++" + mnemonics);

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ecKeyPair = OwnWalletUtils.generateKeyPair(mnemonics);
                        if (null != ecKeyPair) {
                            rememberEth.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
                            rememberEth.setPublicKey(ecKeyPair.getPublicKey().toString(16));
                        }
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
            case R.id.tv_mn:
                UiHelper.startIntroduceMnActy(getActivity());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        iCheck = isChecked;
        setEnable(iCheck);
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
                case GlobalMessageType.RequestCode.FromMnFragment:
                    iCheck = data.getBooleanExtra("iChecked", false);
                    cbAgree_mn.setChecked(iCheck);
                    setEnable(iCheck);
                    break;
            }
        }
    }

}