package com.hualianzb.sec.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.basic.BasicFragment;
import com.hualianzb.sec.utils.ClickUtil;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.NetUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyFragment extends BasicFragment {
    @BindView(R.id.ll_manage)
    LinearLayout llManage;
    @BindView(R.id.ll_transaction_record)
    LinearLayout llTransactionRecord;
    @BindView(R.id.ll_addressbook)
    LinearLayout llAddressbook;
    @BindView(R.id.ll_check_update)
    LinearLayout llCheck;
    Unbinder unbinder;
    TextView tvVersion, tvUpdate;
    private MyFragment instance;
    private View view;
    //    private AdapterWallet adapterWallet;
    private List<Object> list;
    private String address;
    private CheckUpdateLinster listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        address = ((HomePageActivity) activity).getAddress();
        if (activity instanceof MyFragment.CheckUpdateLinster) {
            listener = (CheckUpdateLinster) activity; // 2.2 获取到宿主activity并赋值
        } else {
            throw new IllegalArgumentException("activity must implements CheckUpdateLinster");
        }
    }

    public interface CheckUpdateLinster {
        void check();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmengt_my, container, false);
        initView();
        initData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        tvVersion = view.findViewById(R.id.tv_version);
        tvUpdate = view.findViewById(R.id.tv_update);
    }

    private void initData() {
        String versionName = ActivityUtil.getVersionName(getActivity());
        tvVersion.setText(versionName);
        if (((HomePageActivity) getActivity()).getUpdateStatusInt() > 1) {
            tvUpdate.setVisibility(View.VISIBLE);
        } else {
            tvUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initLogics() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_manage, R.id.ll_transaction_record, R.id.ll_addressbook, R.id.ll_check_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_manage:
                ClickUtil.checkFisrtAndNet(getActivity());
                UiHelper.startManagerWalletActy(getActivity());
                break;
            case R.id.ll_transaction_record:
                ClickUtil.checkFisrtAndNet(getActivity());
                UiHelper.startTransactionRecordActy(getActivity(), address);
                break;
            case R.id.ll_addressbook:
                UiHelper.startAddressBookActy(getActivity(), true);
                break;
            case R.id.ll_check_update:
                ClickUtil.checkFisrtAndNet(getActivity());
                listener.check();
                break;
        }
    }

}
