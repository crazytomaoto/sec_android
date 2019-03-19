package com.hualianzb.sec.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hualianzb.sec.commons.constants.Constant.SpConstant.SWEEP;

/**
 * Date:2018/10/17
 * auther:wangtianyun
 * describe:新增地址
 */
public class AddAddressActivity extends BasicActivity {
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_remark)
    EditText edRemark;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_name_error)
    TextView tvNameError;
    @BindView(R.id.tv_address_error)
    TextView tvAddressError;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private List<AddressBookBean> list;
    private String address, name;
    private boolean isAddressOk, isNameOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        tvTheme.setText("Address Contact");
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        list = PlatformConfig.getList(this, Constant.SpConstant.ADDRESSBOOK);
        tvRight.setVisibility(View.GONE);
        ivBackTop.setImageDrawable(getResources().getDrawable(R.drawable.icon_close_black));
    }

    private void setData() {
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim();
                checkName(name);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address = s.toString().trim();
                checkAddress(address);
                setBtnClickable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setBtnClickable() {
        if (isNameOk && isAddressOk) {
            tvSave.setEnabled(true);
            tvSave.setBackground(getResources().getDrawable(R.drawable.bg_btn));
        }
    }

    //检验地址
    private void checkName(String name) {
        if (StringUtils.isEmpty(name)) {
            isNameOk = false;
            tvNameError.setText("Contact Name Error");
            tvNameError.setVisibility(View.VISIBLE);
            return;
        } else {
            isNameOk = true;
            tvNameError.setVisibility(View.GONE);
        }
    }

    //检验地址
    private void checkAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            isAddressOk = false;
            tvAddressError.setText("Address Error");
            tvAddressError.setVisibility(View.VISIBLE);
            return;
        } else if (address.length() != 42 || !address.substring(0, 2).equals("0x")) {
            isAddressOk = false;
            tvAddressError.setText("Address Error");
            tvAddressError.setVisibility(View.VISIBLE);
            return;
        } else {
            isAddressOk = true;
            tvAddressError.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_save, R.id.iv_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                String phone = edPhone.getText().toString().trim();
                String mail = edEmail.getText().toString().trim();
                String remark = edRemark.getText().toString().trim();
                if (null != list && list.size() > 0) {
                    boolean isExit = false;
                    for (AddressBookBean nowBean : list) {
                        if (nowBean.getAddress().equals(address) && nowBean.getName().equals(name)) {
                            if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(nowBean.getPhone())) {
                                tvAddressError.setText("Address Exists");
                                tvAddressError.setVisibility(View.VISIBLE);
                                isExit = true;
                                return;
                            }
                            if ((!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(nowBean.getPhone())) && (phone.equals(nowBean.getPhone()))) {
                                tvAddressError.setText("Address Exists");
                                tvAddressError.setVisibility(View.VISIBLE);
                                isExit = true;
                                return;
                            }
                        }
                    }
                    if (isExit == false) {
                        AddressBookBean bean = new AddressBookBean();
                        bean.setName(name);
                        bean.setAddress(address);
                        bean.setPhone(phone);
                        bean.setEmail(mail);
                        bean.setRemarks(remark);
                        bean.setCreatTime(TimeUtil.getDate());
                        list.add(bean);
                        PlatformConfig.putList(Constant.SpConstant.ADDRESSBOOK, list);
                        finish();
                    }

                } else {
                    AddressBookBean bean = new AddressBookBean();
                    bean.setName(name);
                    bean.setAddress(address);
                    bean.setPhone(phone);
                    bean.setEmail(mail);
                    bean.setRemarks(remark);
                    bean.setCreatTime(TimeUtil.getDate());
                    list = new ArrayList<>();
                    list.add(bean);
                    PlatformConfig.putList(Constant.SpConstant.ADDRESSBOOK, list);
                    finish();
                }
                break;
            case R.id.iv_scan:
                startImageByCamera();
                break;
        }

    }

    private void startImageByCamera() {
        //拍照点击拍照无反应 20160906 wp
        if (Build.VERSION.SDK_INT >= 23) {
            //摄像头权限检测
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        5);
                return;
            } else {
                startActivityForResult(new Intent(this, CaptureActivity.class), SWEEP);
            }
        } else {
            ToastUtil.show(this, "请开启对应的权限");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //二维码扫描结果
        if (requestCode == SWEEP) {
            String result = "";
            try {
                result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            } catch (Exception e) {
                ToastUtil.show(this, R.string.scan_error);
                return;
            }
            if (!StringUtils.isEmpty(result)) {
                String[] resultArray = result.split("###");
                if (resultArray != null && resultArray.length > 0) {
                    String myGetAdress = resultArray[0];
                    edAddress.setText(myGetAdress);
                    checkAddress(myGetAdress);
                    setBtnClickable();
                }
            } else {
                ToastUtil.show(this, "please retry");
            }
        }
    }
}
