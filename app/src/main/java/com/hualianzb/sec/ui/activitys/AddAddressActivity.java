package com.hualianzb.sec.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
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
import static com.hualianzb.sec.utils.DialogUtil.showToastDialog;

/**
 * Date:2018/10/17
 * auther:wangtianyun
 * describe:新增地址
 */
public class AddAddressActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    private List<AddressBookBean> list;
    private StateBarUtil stateBarUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
    }

    private void initView() {
        list = PlatformConfig.getList(this, Constant.SpConstant.ADDRESSBOOK);
        tvTitle.setText("新增地址");
        tvSave.setBackgroundResource(R.drawable.btn_sure_add);
    }

    private void setData() {
        edName.setHint(getString(R.string.insert_man_name));
        edAddress.setHint(getString(R.string.insert_man_address));
        edPhone.setHint(getString(R.string.insert_man_phone));
        edEmail.setHint(getString(R.string.insert_man_email));
        edRemark.setHint("选填");
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @OnClick({R.id.tv_save, R.id.iv_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                String name = edName.getText().toString().trim();
                String address = edAddress.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String mail = edEmail.getText().toString().trim();
                String remark = edRemark.getText().toString().trim();
                if (StringUtils.isEmpty(name)) {
                    showToastDialog(this, "姓名不能为空");
                    return;
                }
                if (StringUtils.isEmpty(address)) {
                    showToastDialog(this, "钱包地址不正确或格式有误");
                    return;
                } else if ((address.length() != 42 || !address.substring(0, 2).equals("0x"))) {
                    showToastDialog(this, "钱包地址不正确或格式有误");
                    return;
                }
                if (null != list && list.size() > 0) {
                    boolean isExit = false;
                    for (AddressBookBean nowBean : list) {
                        if (nowBean.getAddress().equals(address) && nowBean.getName().equals(name)) {
                            if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(nowBean.getPhone())) {
                                showToastDialog(this, "该地址已存在");
                                isExit = true;
                                return;
                            }
                            if ((!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(nowBean.getPhone())) && (phone.equals(nowBean.getPhone()))) {
                                showToastDialog(this, "该地址已存在");
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
                }
            } else {
                ToastUtil.show(this, "扫描失败");
            }
        }
    }

    @Override
    protected void doSomeUI(boolean netMobile) {
        super.doSomeUI(netMobile);
    }
}
