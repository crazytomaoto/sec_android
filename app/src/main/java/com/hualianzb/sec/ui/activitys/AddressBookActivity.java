package com.hualianzb.sec.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.ui.adapters.AdapterAddressBook;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
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
 * describe:地址簿
 */
public class AddressBookActivity extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.iv_rig_top)
    ImageView ivRigTop;
    @BindView(R.id.tv_add_man)
    TextView tvAddMan;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.lv_man)
    ListView lvMan;
    @BindView(R.id.rl_data)
    RelativeLayout rLData;
    @BindView(R.id.tv_add)
    TextView tv_add;
    private List<AddressBookBean> list;
    private AdapterAddressBook adapter;
    private boolean isFromMy;
    private StateBarUtil stateBarUtil;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            isFromMy = bundle.getBoolean("isFromMy");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            isFromMy = savedInstanceState.getBoolean("isFromMy");
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalMessageType.MessgeCode.NOTIFYBOOKLIST:
                    getData();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
    }

    private void initView() {
        tvTitle.setText("地址簿");
        list = new ArrayList<>();
        ivRigTop.setImageResource(R.drawable.icon_scan);
        adapter = new AdapterAddressBook(this, handler, isFromMy);
        lvMan.setAdapter(adapter);
        if (isFromMy) {
            ivRigTop.setVisibility(View.GONE);
        }

        lvMan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isFromMy) {
                    Intent intent = new Intent(AddressBookActivity.this, TransferActivity.class);
                    intent.putExtra("address", list.get(position).getAddress());
                    setResult(2, intent);
                    finish();
                }
            }
        });

    }

    private void getData() {
        list = PlatformConfig.getList(this, Constant.SpConstant.ADDRESSBOOK);
        if (null == list || list.size() <= 0) {
            llEmpty.setVisibility(View.VISIBLE);
            rLData.setVisibility(View.GONE);
        } else {
            rLData.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            list = Util.listAddressBookSort(list);
            adapter.setList(list);
        }
    }


    @OnClick({R.id.iv_rig_top, R.id.iv_back_top, R.id.tv_add_man, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_rig_top:
                startImageByCamera();
                break;
            case R.id.tv_add_man:
            case R.id.tv_add:
                UiHelper.startAddAddressBookActy(this);
                break;
            case R.id.iv_back_top:
                if (isFromMy) {
                    goBacktoMyPage();
                } else {
                    finish();
                }
                break;
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
            }
            if (!StringUtils.isEmpty(result)) {
                Intent intent = new Intent(this, TransferActivity.class);
                intent.putExtra("result", result);
                setResult(1, intent);
                finish();
            } else {
                ToastUtil.show(this, "扫描失败");
            }

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
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFromMy) {
            goBacktoMyPage();
        } else {
            finish();
        }
    }

    private void goBacktoMyPage() {
        UiHelper.goBackHomaPageAc(this, "", true);
    }
}
