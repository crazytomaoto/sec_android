package com.hualianzb.sec.ui.activitys;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.interfaces.IOutPrivatekey;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.DeviceUtil;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.ImageUtils;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeMoneyActicity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_eth)
    TextView tvEth;
    @BindView(R.id.tv_eth_all)
    TextView tvEthAll;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
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
    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    @BindView(R.id.iv_issee)
    ImageView ivIssee;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.btn_backups)
    TextView btnBackups;
    private boolean canSee = false;
    private Map<String, RememberEth> map;
    private String address, checkedAddress;
    private String money;
    private RememberEth bean, checkedReme;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_money);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
//        stateBarUtil = new StateBarUtil(this);
//        stateBarUtil.setHalfTransparent();
        StatusBarCompat.setTranslucent(getWindow(), true);
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
//        money = PlatformConfig.getDouble(Constant.SpConstant.MYBANLANCE) + "";
        tvEth.setText(money + "Ether");
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        bean = map.get(address);
        tvTitle.setText(bean.getWalletName());
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        tvRight.setTextColor(getResources().getColor(R.color.white));
        tvEthAll.setText(bean.getAddress());
        if (StringUtils.isEmpty(bean.getTips())) {
            llTip.setVisibility(View.GONE);
        } else {
            llTip.setVisibility(View.VISIBLE);
        }
        checkCanSee();
        edPass.setHint(bean.getTips());
        ivAvater.setImageResource(ImageUtils.getWalletImage(bean.getWalletincon()));
        ivBackTop.setImageResource(R.drawable.icon_back_white);
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
    }

    @OnClick({R.id.tv_right, R.id.iv_issee, R.id.iv_qr_code, R.id.ll_changepass, R.id.ll_out_privatekey, R.id.ll_out_keystore, R.id.btn_backups, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                String name = edWname.getText().toString().trim();
                if (!StringUtils.isEmpty(name)) {

                    if (map != null && map.values().size() > 0) {
                        boolean isRe = false;
                        for (RememberEth reme : map.values()) {
                            if (reme.getWalletName().equals(name)) {
                                isRe = true;
                                break;
                            }
                        }
                        if (isRe) {
                            ToastUtil.show(this, "钱包名称重复");
                            return;
                        }
                    }
                    bean.setWalletName(name);
                    map.put(bean.getAddress(), bean);
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                    ToastUtil.show(this, "修改成功");
                    map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
                    tvTitle.setText(name);
                } else {
                    ToastUtil.show(this, "钱包名称为空");
                }
                break;
            case R.id.iv_qr_code:
                UiHelper.startMakeCodeActivity(this, address);
                break;
            case R.id.ll_changepass:
                UiHelper.startChangePassActy(this, address);
                break;
            case R.id.ll_out_privatekey:
                showmyDialog(this, bean.getPass(), bean.getPrivateKey(), 1);
                break;
            case R.id.ll_out_keystore:
                showmyDialog(this, bean.getPass(), bean.getPrivateKey(), 2);
                break;
            case R.id.btn_delete:
                showmyDialog(this, bean.getPass(), 2);
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
                showmyDialog(this, bean.getPass(), 1);
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
            for (RememberEth rememberEth : map.values()) {
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
                for (RememberEth rememberEth : map.values()) {
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

    private void showmyDialog(Context context, String pass, int from) {// 1 备注助记词  2 删除钱包
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_bankup_mnemonics);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView yes = dialog.findViewById(R.id.tv__ok);
        final EditText ed_pass = dialog.findViewById(R.id.ed_pass);
        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myPass = ed_pass.getText().toString().trim();
                if (StringUtils.isEmpty(myPass)) {
                    ToastUtil.show(MakeMoneyActicity.this, "密码为空");
                } else {
                    if (!myPass.equals(pass)) {
                        ToastUtil.show(MakeMoneyActicity.this, "密码不正确");
                    } else {
                        if (from == 1) {
                            UiHelper.startBackupMnemonicsActy2(context, myPass, address);
                        }
                        if (from == 2) {
                            IntentMove();
                        }
                        dialog.dismiss();
                    }
                }
            }
        });

        ed_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MakeMoneyActicity.this.showSoftInputFromWindow(ed_pass);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void checkCanSee() {
        if (canSee) {
            //如果选中，显示密码提示
//            edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edPass.setText(bean.getTips());
            ivIssee.setImageResource(R.drawable.icon_eye_open);
        } else {
            //否则隐藏密码
//            edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edPass.setText("******");
            ivIssee.setImageResource(R.drawable.icon_eye_close);
        }
    }

    private void showmyDialog(Context context, String pass, String privatekey, int whereUse) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_bankup_mnemonics);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView yes = dialog.findViewById(R.id.tv__ok);
        final EditText ed_pass = dialog.findViewById(R.id.ed_pass);
        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myPass = ed_pass.getText().toString().trim();
                if (StringUtils.isEmpty(myPass)) {
                    ToastUtil.show(MakeMoneyActicity.this, "密码为空");
                } else {
                    if (!myPass.equals(pass)) {
                        ToastUtil.show(MakeMoneyActicity.this, "密码不正确");
                    } else {
                        if (whereUse == 1) {
                            showAnotherDialog(privatekey);
                        }
                        if (whereUse == 2) {
                            UiHelper.startOutKeyStoreAcy(MakeMoneyActicity.this, address);
                        }
                        dialog.dismiss();
                    }
                }
            }
        });
        ed_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MakeMoneyActicity.this.showSoftInputFromWindow(ed_pass);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showAnotherDialog(String privatekey) {
        DialogUtil.showIdIdentifyDialog(this, privatekey, new IOutPrivatekey() {
            @Override
            public void cancelDialog(View v, Dialog d) {
                d.dismiss();
            }

            @Override
            public void copy(View v, Dialog d) {
                DeviceUtil.copy(MakeMoneyActicity.this, privatekey);
                ToastUtil.show(MakeMoneyActicity.this, "已复制到剪切板");
                d.dismiss();
            }
        });
    }

    public static class UserBean {
        private String id;
        private String birthday;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }
}
