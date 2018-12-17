package com.hualianzb.sec.ui.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备份钱包
 */
public class BackupMnemonicsActy1 extends BasicActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 0x124;
    @BindView(R.id.iv_jump)
    ImageView ivJump;
    private String myPass;
    private String address;
    //    private Map<String, RememberEth> map;
    private RememberEth rememberEth;
    private Map<String, RememberEth> map;
    StateBarUtil stateBarUtil;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            rememberEth = (RememberEth) bundle.getSerializable("rememberEth");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            rememberEth = (RememberEth) savedInstanceState.getSerializable("rememberEth");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankup_mn);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initData();
    }

    private void initData() {
        address = rememberEth.getAddress();
    }

    @OnClick({R.id.btn_bankup, R.id.iv_jump})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bankup:
                showmyDialog(BackupMnemonicsActy1.this);
                break;
            case R.id.iv_jump:
                UiHelper.startHomaPageAc(this, address);
                finish();
                break;
        }
    }

    /**
     * 请求授权
     */
    /**
     * 当有多个权限需要申请的时候
     * <p>
     */

    public void checkPermission() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }
        );
        // 如果这3个权限全都拥有, 则直接执行读取短信代码
        if (isAllGranted) {
            showmyDialog(BackupMnemonicsActy1.this);
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                showmyDialog(BackupMnemonicsActy1.this);
            } else {

            }
        }
    }

    private void showmyDialog(Context context) {

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
                myPass = ed_pass.getText().toString().trim();
                if (StringUtils.isEmpty(myPass)) {
                    ToastUtil.show(BackupMnemonicsActy1.this, "密码不能为空");
                    dialog.dismiss();
                } else {
                    if (!myPass.equals(rememberEth.getPass())) {
                        ToastUtil.show(BackupMnemonicsActy1.this, "密码不正确");
                        dialog.dismiss();
                    } else {
                        go();
                        dialog.dismiss();
                    }
                }
            }
        });
        ed_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    BackupMnemonicsActy1.this.showSoftInputFromWindow(ed_pass);
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

    private void go() {
        UiHelper.startBackupMnemonicsActy2(this, myPass, address);
//        finish();
    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }

    @Override
    public void onResume() {
        super.onResume();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
    }
}
