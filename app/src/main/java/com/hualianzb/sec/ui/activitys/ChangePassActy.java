package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.utils.DialogUtil.showToastDialog;

public class ChangePassActy extends BasicActivity {
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_old_pass)
    EditText edOldPass;
    @BindView(R.id.ed_new_pass)
    EditText edNewPass;
    @BindView(R.id.ed_re_pass)
    EditText edRePass;
    @BindView(R.id.ll_import)
    LinearLayout llImport;
    @BindView(R.id.ed_pass_tips)
    EditText edPassTips;
    @BindView(R.id.tv_pass_error)
    TextView tvPassError;
    @BindView(R.id.tv_new_pass_error)
    TextView tvNewPassError;
    @BindView(R.id.tv_re_pass_error)
    TextView tvRePassError;
    @BindView(R.id.rl_pass)
    RelativeLayout rlPass;
    @BindView(R.id.rl_new_pass)
    RelativeLayout rlNewPass;
    @BindView(R.id.rl_re_pass)
    RelativeLayout rlRePass;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    @BindView(R.id.iv_clear3)
    ImageView ivClear3;
    @BindView(R.id.iv_red)
    ImageView ivRed;
    @BindView(R.id.iv_yellow)
    ImageView ivYellow;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    @BindView(R.id.ll_pass_reminder)
    LinearLayout llPassReminder;
    @BindView(R.id.rl_level)
    RelativeLayout rlLevel;
    private RememberSEC rememberEth;
    private String address;
    private Map<String, RememberSEC> map;
    private boolean isOldPassOk, isNewPassOk, isRepassOk;
    private String oldPass, newPass, rePass;
    int passLevel = 0;
    private String strTips = "Forget Password?\nImport phrase or Private key to " + " <font color='#388ED8'>reset the password</font>" + ".";

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        tvTheme.setText("Change Password");
        tvRight.setText("Complete");
        tvTips.setText(Html.fromHtml(strTips));
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC rememb : map.values()) {
            if (rememb.getAddress().equals(address)) {
                rememberEth = rememb;
                break;
            }
        }
        edOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPass = s.toString().trim();
                if (!StringUtils.isEmpty(oldPass)) {
                    ivClear1.setVisibility(View.VISIBLE);
                } else {
                    ivClear1.setVisibility(View.GONE);
                }
                checkOldPass(oldPass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPass = s.toString().trim();
                if (!StringUtils.isEmpty(newPass)) {
                    ivClear2.setVisibility(View.VISIBLE);
                } else {
                    ivClear2.setVisibility(View.GONE);
                }
                checkPassStrength(newPass);
                checkPassword(newPass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edRePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rePass = s.toString().trim();
                if (!StringUtils.isEmpty(rePass)) {
                    ivClear3.setVisibility(View.VISIBLE);
                } else {
                    ivClear3.setVisibility(View.GONE);
                }
                checkRePass(rePass);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                llPassReminder.setVisibility(View.VISIBLE);
                rlLevel.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.GONE);
                ivYellow.setVisibility(View.GONE);
                return;
            }
            if (passLevel == 3 && password.length() >= 8 && password.length() <= 12) {
                rlLevel.setVisibility(View.VISIBLE);
                ivYellow.setVisibility(View.VISIBLE);
                llPassReminder.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.GONE);
                return;
            }
            if (passLevel == 3 && password.length() >= 12) {
                llPassReminder.setVisibility(View.VISIBLE);
                ivYellow.setVisibility(View.VISIBLE);
                ivBlue.setVisibility(View.VISIBLE);
                rlLevel.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    //原密码是否正确
    private void checkOldPass(String password) {
        if (StringUtils.isEmpty(password)) {
            isOldPassOk = false;
            tvPassError.setTextColor(getResources().getColor(R.color.text_error));
            tvPassError.setVisibility(View.VISIBLE);
            tvPassError.setText("Please Input Password");
            return;
        } else {
            if (!password.equals(rememberEth.getPass())) {
                isOldPassOk = false;
                tvPassError.setTextColor(getResources().getColor(R.color.text_error));
                tvPassError.setVisibility(View.VISIBLE);
                tvPassError.setText("Password Error");
                return;
            } else {
                tvPassError.setVisibility(View.GONE);
                isOldPassOk = true;
            }
        }
    }


    //检查新密码的合法性
    private void checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            isNewPassOk = false;
            llPassReminder.setVisibility(View.VISIBLE);
            tvNewPassError.setText("Please Input Password");
            tvNewPassError.setVisibility(View.VISIBLE);
            return;
        } else if (password.length() < 8) {
            isNewPassOk = false;
            llPassReminder.setVisibility(View.VISIBLE);
            tvNewPassError.setText(getString(R.string.format_error));
            tvNewPassError.setVisibility(View.VISIBLE);
            return;
        } else {
            String regEx4 = getString(R.string.patters_all);
            isNewPassOk = password.matches(regEx4);
            if (isNewPassOk) {
                tvPassError.setVisibility(View.GONE);
            } else {
                llPassReminder.setVisibility(View.VISIBLE);
                tvNewPassError.setText(getString(R.string.format_error));
                tvNewPassError.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    //重复密码是否正确
    private void checkRePass(String password) {
        if (StringUtils.isEmpty(password)) {
            isRepassOk = false;
            tvRePassError.setText("Please Repeat Password");
            tvRePassError.setVisibility(View.VISIBLE);
            return;
        } else {
            tvRePassError.setVisibility(View.GONE);
            if (!password.equals(newPass)) {
                isRepassOk = false;
                llPassReminder.setVisibility(View.VISIBLE);
                tvNewPassError.setText("Two passwords are inconsistent");
                tvNewPassError.setVisibility(View.VISIBLE);
                return;
            } else {
                isRepassOk = true;
            }
        }
    }

    private void setBtnClickable() {
        if (isOldPassOk && isNewPassOk && isRepassOk) {
            tvRight.setEnabled(true);
        } else {
            tvRight.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_right, R.id.ll_import, R.id.iv_clear1, R.id.iv_clear2, R.id.iv_clear3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if (StringUtils.isEmpty(oldPass)) {
                    rlPass.setBackgroundResource(R.drawable.bg_edit_error);
//                    showToastDialog(this, "密码为空");
                    return;
                }
                if (StringUtils.isEmpty(newPass)) {
                    rlNewPass.setBackgroundResource(R.drawable.bg_edit_error);
//                    showToastDialog(this, "新密码为空");
                    return;
                }
                if (StringUtils.isEmpty(rePass)) {
                    rlRePass.setBackgroundResource(R.drawable.bg_edit_error);
//                    showToastDialog(this, "再次输入密码为空");
                    return;
                }
                String regEx4 = getString(R.string.patters_all);

                boolean boolLast = newPass.matches(regEx4);
                if (!oldPass.equals(rememberEth.getPass())) {
                    showToastDialog(this, "原密码不正确");
                    return;
                }

                if (boolLast == false) {
                    showToastDialog(this, "新密码格式不正确");
                    return;
                }
                if (!rePass.equals(newPass)) {
                    showToastDialog(this, "两次输入新密码不一致");
                    return;
                }

                String tip = edPassTips.getText().toString().trim();
                if (!StringUtils.isEmpty(tip)) {
                    rememberEth.setTips(tip);
                }
                rememberEth.setPass(rePass);
                map.put(address, rememberEth);
                PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                finish();
                break;
            case R.id.ll_import:
                UiHelper.startActyImportWalletActivity(this);
                break;
            case R.id.iv_clear1:
                edOldPass.setText("");
                break;
            case R.id.iv_clear2:
                edNewPass.setText("");
                break;
            case R.id.iv_clear3:
                edRePass.setText("");
                break;
        }
    }
}
