package com.hualianzb.sec.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.ui.adapters.AdapterAddressBook;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/17
 * auther:wangtianyun
 * describe:地址簿
 */
public class AddressBookActivity extends BasicActivity {
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
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
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private List<AddressBookBean> list;
    private AdapterAddressBook adapter;
    private boolean isFromMy;

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
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        ivBackTop.setImageDrawable(getResources().getDrawable(R.drawable.icon_close_black));
        tvTheme.setText("Address Book");
        list = new ArrayList<>();
        adapter = new AdapterAddressBook(this, handler, isFromMy);
        lvMan.setAdapter(adapter);
        tvRight.setVisibility(View.GONE);
        lvMan.setOnItemClickListener((parent, view, position, id) -> {
            if (!isFromMy) {
                Intent intent = new Intent(AddressBookActivity.this, TransferActivity.class);
                intent.putExtra("address", list.get(position).getAddress());
                setResult(2, intent);
                finish();
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


    @OnClick({R.id.iv_back_top, R.id.tv_add_man, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_man:
            case R.id.tv_add:
                UiHelper.startAddAddressBookActy(this);
                break;
            case R.id.iv_back_top:
                finish();
                break;
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
        finish();
    }
}
