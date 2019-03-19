package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.ui.fragments.ExportKeystoreAttentionDialogFragment;
import com.hualianzb.sec.ui.fragments.ExportKeystoreDialogFragment;
import com.hualianzb.sec.ui.fragments.ExportPrivatekeyDialogFragment;
import com.hualianzb.sec.ui.fragments.QrCodeDialogFragment;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeMoneyActicity extends BasicActivity implements QrCodeDialogFragment.InfacnCopy
        , ExportKeystoreAttentionDialogFragment.ExportAttenion, ExportKeystoreDialogFragment.ExportLinster
        , ExportPrivatekeyDialogFragment.ExporPrivateKeytLinster {
    @BindView(R.id.tv_wallet_name)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_sec)
    TextView tvSec;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qr_code)
    TextView ivQrCode;
    @BindView(R.id.ed_wname)
    EditText edWname;
    @BindView(R.id.ll_changepass)
    LinearLayout llChangepass;
    @BindView(R.id.ll_out_privatekey)
    LinearLayout llOutPrivatekey;
    @BindView(R.id.ll_out_keystore)
    LinearLayout llOutKeystore;
    @BindView(R.id.btn_delete)
    TextView btn_delete;
    @BindView(R.id.iv_issee)
    ImageView ivIssee;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.btn_backups)
    TextView btnBackups;
    @BindView(R.id.tv_name_error)
    TextView tvNameError;
    @BindView(R.id.re_prompt)
    RelativeLayout rePrompt;
    private boolean canSee = false;
    private Map<String, RememberSEC> map;
    private String address, checkedAddress;
    private String money;
    private RememberSEC bean, checkedReme;
    private boolean isWallteNameOk;
    private String walletName;
    private Dialog dialogTips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_money);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.gray_background).init();
        dialogTips = DialogUtil.showLoadingDialog(MakeMoneyActicity.this, "Saved");
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
            money = savedInstanceState.getString("money");
        }
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            money = bundle.getString("money");
        }
    }

    private void initView() {
        if (money.contains(".")) {
            money = money.replace(".", "A");//点的话split无法返回String[]，所以替换
            String[] moneys = money.split("A");
            if (moneys[1].length() > 8) {
                money = moneys[0] + "." + moneys[1].substring(0, 8);
            } else {
                money = moneys[0] + "." + moneys[1];
            }
        }
        tvSec.setText(money + "SEC");
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        bean = map.get(address);
        tvTitle.setText(bean.getWalletName());
        tvTitle.setTextColor(getResources().getColor(R.color.text_black));
//        tvRight.setTextColor(getResources().getColor(R.color.white));
        tvAddress.setText(bean.getAddress());
        checkCanSee();
        edPass.setHint(bean.getTips());
        ivBackTop.setImageResource(R.drawable.icon_back);
        edWname.setHint(bean.getWalletName());

        if (bean.isBackup() == true) {
            btnBackups.setVisibility(View.GONE);
        } else {
            if (bean.getHowToCreate() == 2) {
                btnBackups.setVisibility(View.VISIBLE);
            } else {
                if (StringUtils.isEmpty(bean.getMnemonics())) {
                    btnBackups.setVisibility(View.GONE);
                }
            }
        }
        if (StringUtils.isEmpty(bean.getTips())) {
            rePrompt.setVisibility(View.GONE);
        } else {
            rePrompt.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_right, R.id.iv_issee, R.id.iv_qr_code, R.id.ll_changepass, R.id.ll_out_privatekey, R.id.ll_out_keystore, R.id.btn_backups, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                dialogTips.show();
                map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
                if (map == null || map.values().size() == 0) {
                    map = new HashMap<>();
                }
                bean.setWalletName(walletName);
                map.put(bean.getAddress(), bean);
                PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                tvTitle.setText(walletName);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialogTips.dismiss();
                    }
                }, 3000);
                break;
            case R.id.iv_qr_code:
                QrCodeDialogFragment qrCodeDialogFragment = new QrCodeDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("address", address);
                qrCodeDialogFragment.setArguments(bundle);//数据传递到fragment中
                FragmentManager fragmentManager = getSupportFragmentManager();
                qrCodeDialogFragment.show(fragmentManager, QrCodeDialogFragment.class.getSimpleName());
                break;
            case R.id.ll_changepass:
                UiHelper.startChangePassActy(this, address);
                break;
            case R.id.ll_out_privatekey:
                UiHelper.startCheckPassActivity(this, bean.getPass(), 002);
                break;
            case R.id.ll_out_keystore:
                UiHelper.startCheckPassActivity(this, bean.getPass(), 003);
                break;
            case R.id.btn_delete:
                UiHelper.startCheckPassActivity(this, bean.getPass(), 005);
                break;
            case R.id.iv_issee:
                if (canSee) {
                    canSee = false;
                } else {
                    canSee = true;
                }
                checkCanSee();
                break;
            case R.id.btn_backups:
                UiHelper.startCheckPassActivity(this, bean.getPass(), 004);
                break;
        }
    }

    private void IntentMove() {
        map.remove(address);
        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (map.isEmpty()) {
            UiHelper.startActyCreateInsertWallet(this);
            finish();
        } else {
            boolean hasSelect = false;
            for (RememberSEC rememberEth : map.values()) {
                if (rememberEth.isNow() == true) {
                    hasSelect = true;
                    checkedReme = rememberEth;
                    checkedAddress = rememberEth.getAddress();
                    break;
                }
            }
            if (hasSelect) {
                finish();
            } else {
                for (RememberSEC rememberEth : map.values()) {
                    rememberEth.setNow(true);
                    map.put(rememberEth.getAddress(), rememberEth);
                    PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                    break;
                }
                finish();
            }
        }
    }

    //检测钱包名称的合法性
    private void checkWalletName(String walletName) {
        if (map == null || map.isEmpty() || map.values().size() == 0) {
            if (StringUtils.isEmpty(walletName)) {//名称为空
                isWallteNameOk = false;
                tvNameError.setVisibility(View.VISIBLE);
                tvNameError.setText(getString(R.string.wallet_name_null));
                return;
            } else {
                isWallteNameOk = true;
                tvNameError.setVisibility(View.GONE);
                return;
            }
        } else {
            if (StringUtils.isEmpty(walletName)) {//名称为空
                isWallteNameOk = false;
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
                    tvNameError.setText(getString(R.string.wallet_exists));
                    tvNameError.setVisibility(View.VISIBLE);
                    return;
                } else {
                    isWallteNameOk = true;
                    tvNameError.setVisibility(View.GONE);
                    return;
                }

            }
        }
    }

    private void setClickeble() {
        if (isWallteNameOk) {
            tvRight.setEnabled(true);
        } else {
            tvRight.setEnabled(false);
        }
    }

    private void checkCanSee() {
        if (canSee) {
            //如果选中，显示密码提示
            edPass.setText(bean.getTips());
            ivIssee.setImageResource(R.drawable.icon_eye_open);
        } else {
            //否则隐藏密码
            edPass.setText("******");
            ivIssee.setImageResource(R.drawable.icon_eye_close);
        }
    }

    public ImmersionBar getActivityImmersionBar() {
        return ImmersionBar.with(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
    }

    private void initData() {
        edWname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                walletName = s.toString().trim();
                checkWalletName(walletName);
                setClickeble();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //传过来的地址，在宿主Activity中操作
    @Override
    public void copy(String str) {
        Util.copy(this, address);
    }

    @Override
    public void goIt() {
        ExportKeystoreDialogFragment exportKeystoreDialogFragment = new ExportKeystoreDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        exportKeystoreDialogFragment.setArguments(bundle);//数据传递到fragment中
        FragmentManager fragmentManager = getSupportFragmentManager();
        exportKeystoreDialogFragment.show(fragmentManager, ExportKeystoreDialogFragment.class.getSimpleName());
    }

    //复制keystore
    @Override
    public void copyKeyStore(String str) {
        if ("1".equals(str)) {
            Util.copy(this, bean.getWalletFile());
        }
    }

    //复制私钥
    @Override
    public void copyPrivatekey(String str) {
        if ("1".equals(str)) {
            Util.copy(this, bean.getPrivateKey());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 002://私钥
                if (resultCode == 1) {
                    if (null != data) {
                        boolean isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            ExportPrivatekeyDialogFragment privatekeyDialogFragment = new ExportPrivatekeyDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("address", address);
                            privatekeyDialogFragment.setArguments(bundle);//数据传递到fragment中
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            privatekeyDialogFragment.show(fragmentManager, ExportPrivatekeyDialogFragment.class.getSimpleName());
                        }
                    }
                }
                break;
            case 003://keyStore
                if (resultCode == 1) {
                    if (null != data) {
                        boolean isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            ExportKeystoreAttentionDialogFragment exportAttentionDialogFragment = new ExportKeystoreAttentionDialogFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            exportAttentionDialogFragment.show(fragmentManager, ExportKeystoreAttentionDialogFragment.class.getSimpleName());
                        }
                    }
                }
                break;
            case 004://助记词
                if (resultCode == 1) {
                    if (null != data) {
                        boolean isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            UiHelper.startBackupMnemonicsActy2(MakeMoneyActicity.this, address);
                        }
                    }
                }
                break;
            case 005://删除钱包
                if (resultCode == 1) {
                    if (null != data) {
                        boolean isCheckedPass = data.getExtras().getBoolean("isCheckedPass");
                        if (isCheckedPass) {
                            IntentMove();
                        }
                    }
                }
                break;
            default:
                break;
        }

    }
}