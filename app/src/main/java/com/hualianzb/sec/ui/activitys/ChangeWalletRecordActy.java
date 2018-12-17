package com.hualianzb.sec.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.adapters.AdapterWallets;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.Util;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/8/16
 * auther:wangtianyun
 * describe:切换账户
 */
public class ChangeWalletRecordActy extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.lv_wallet)
    ListView lvWallet;
    private AdapterWallets adapter;
    private List<RememberEth> list;
    private StateBarUtil stateBarUtil;
    private Map<String, RememberEth> map;
    private RememberEth rememberEth, oldReme;
    private String address;
    private boolean isFromHome;

    @Override
    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            address = bundle.getString("address");
            isFromHome = bundle.getBoolean("isFromHome");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            isFromHome = savedInstanceState.getBoolean("isFromHome");
            address = savedInstanceState.getString("address");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_record);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
        initView();
    }

    private void initView() {
        tvTitle.setText("切换账户");
        list = new ArrayList<>();
        ivBackTop.setImageResource(R.drawable.icon_close_black);
        adapter = new AdapterWallets(this, address, isFromHome);
        lvWallet.setAdapter(adapter);
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        if (null != map && !map.isEmpty() && map.values().size() > 0) {
            for (RememberEth rem : map.values()) {
                list.add(rem);
            }
            list = Util.ListSort(list);
            adapter.setList(list);
            rememberEth = map.get(address);
        } else {
            lvWallet.setVisibility(View.GONE);
            ToastUtil.show(this, "您还没有其他钱包");
        }

//        lvWallet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (list.get(position).getAddress().equals(address)) {
//                    return;
//                }
//                oldReme = map.get(address);
//                oldReme.setNow(false);
//                map.put(address, oldReme);
//
//                rememberEth = list.get(position);
//                address = rememberEth.getAddress();
//                //当前的钱包改为选中
//                rememberEth.setNow(true);
//                PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, address);//记住当前选中钱包的地址
//                map.put(address, rememberEth);
//                //重新保存
//                PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
//
//                if (isFromHome) {
//                    UiHelper.startHomaPageAc(ChangeWalletRecordActy.this, rememberEth.getAddress());
//                } else {
//                    Intent intent = new Intent(ChangeWalletRecordActy.this, TransactionRecordActy.class);
//                    intent.putExtra("nowAddress", rememberEth.getAddress());
//                    setResult(100, intent);
//                    finish();
//                }
//            }
//        });
    }

}
