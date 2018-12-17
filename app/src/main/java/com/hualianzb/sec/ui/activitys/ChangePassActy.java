package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.utils.DialogUtil.showToastDialog;

public class ChangePassActy extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_old_pass)
    EditText edOldPass;
    @BindView(R.id.ed_new_pass)
    EditText edNewPass;
    @BindView(R.id.ed_re_pass)
    EditText edRePass;
    @BindView(R.id.tv_import)
    TextView tvImport;
    @BindView(R.id.ed_pass_tips)
    EditText edPassTips;
    @BindView(R.id.iv_clear1)
    ImageView ivClear1;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    @BindView(R.id.iv_clear3)
    ImageView ivClear3;
    private RememberEth rememberEth;
    private String address;
    private Map<String, RememberEth> map;
    private StateBarUtil stateBarUtil;

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
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        tvTitle.setText("更改密码");
        tvRight.setText("完成");
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth rememb : map.values()) {
            if (rememb.getAddress().equals(address)) {
                rememberEth = rememb;
                break;
            }
        }
//        if (StringUtils.isEmpty(rememberEth.getTips().toString().trim())) {
//            edPassTips.setVisibility(View.GONE);
//        } else {
//            edPassTips.setVisibility(View.VISIBLE);
//            edPassTips.setHint(rememberEth.getTips().toString().trim());
//        }
        edOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(s.toString().trim())) {
                    ivClear1.setVisibility(View.VISIBLE);
                } else {
                    ivClear1.setVisibility(View.GONE);
                }
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
                if (!StringUtils.isEmpty(s.toString().trim())) {
                    ivClear2.setVisibility(View.VISIBLE);
                } else {
                    ivClear2.setVisibility(View.GONE);
                }
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
                if (!StringUtils.isEmpty(s.toString().trim())) {
                    ivClear3.setVisibility(View.VISIBLE);
                } else {
                    ivClear3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.tv_right, R.id.tv_import, R.id.iv_clear1, R.id.iv_clear2, R.id.iv_clear3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                String oldPass = edOldPass.getText().toString().trim();
                String newPass = edNewPass.getText().toString().trim();
                String rePass = edRePass.getText().toString().trim();
//                String tip = edPassTips.getText().toString().trim();
                if (StringUtils.isEmpty(oldPass)) {
                    showToastDialog(this, "密码为空");
                    return;
                }
                if (StringUtils.isEmpty(newPass)) {
                    showToastDialog(this, "新密码为空");
                    return;
                }
                if (StringUtils.isEmpty(rePass)) {
                    showToastDialog(this, "再次输入密码为空");
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
            case R.id.tv_import:
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
