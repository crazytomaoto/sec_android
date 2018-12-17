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
    @BindView(R.id.tv_addressbook)
    TextView tvAddressbook;
    Unbinder unbinder;
    TextView tvVersion;
    private MyFragment instance;
    private View view;
    //    private AdapterWallet adapterWallet;
    private List<Object> list;
    private String address;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        address = ((HomePageActivity) activity).getAddress();
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
//        adapterWallet = new AdapterWallet(getActivity());
    }

    private void initData() {
//        adapterWallet.setList(list);
        String versionName = ActivityUtil.getVersionName(getActivity());
        tvVersion.setText(versionName);
    }

    @Override
    protected void initLogics() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_manage, R.id.ll_transaction_record, R.id.tv_addressbook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_manage:
                UiHelper.startManagerWalletActy(getActivity());
                break;
            case R.id.ll_transaction_record:
                UiHelper.startTransactionRecordActy(getActivity(), address);
                break;
            case R.id.tv_addressbook:
                UiHelper.startAddressBookActy(getActivity(), true);
                break;
        }
    }

}
