package com.hualianzb.sec.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.adapters.AdapterDiscovery;
import com.hualianzb.sec.ui.basic.BasicFragment;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DiscoveryFragment extends BasicFragment {
    Unbinder unbinder;
    TextView tvVersion;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.ll_discovery)
    LinearLayout llDiscovery;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.ll_games)
    LinearLayout llGames;
    private DiscoveryFragment instance;
    private View view;
    //    private AdapterWallet adapterWallet;
    private List<Object> list;
    private String address;
    private AdapterDiscovery adapterWallet;
    private int index = 0;
    Dialog dialogLoading;

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

    private void tabSelected(int index) {
        llDiscovery.setSelected(false);
        llGames.setSelected(false);
        viewOne.setVisibility(View.GONE);
        viewTwo.setVisibility(View.GONE);
        switch (index) {
            case 0:
                llDiscovery.setSelected(true);
                viewOne.setVisibility(View.VISIBLE);
                break;
            case 1:
                llGames.setSelected(true);
                viewTwo.setVisibility(View.VISIBLE);
                break;
        }
        getData();
    }

    private void getData() {
        dialogLoading.show();
        switch (index) {
            case 0:
                ToastUtil.show(getActivity(), "exchanges");
                break;
            case 1:
                ToastUtil.show(getActivity(), "games");
                break;
        }
        dialogLoading.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmengt_discovery, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        dialogLoading = DialogUtil.showLoadingDialog(getActivity(), getResources().getString(R.string.loading));
        tabSelected(0);
        tvVersion = view.findViewById(R.id.tv_version);
        adapterWallet = new AdapterDiscovery(getActivity());
        listView.setAdapter(adapterWallet);
    }

    private void initData() {
//        adapterWallet.setList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show(getActivity(), position + "");
            }
        });
    }

    @Override
    protected void initLogics() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_discovery, R.id.ll_games})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_discovery:
                index = 0;
                tabSelected(index);
                break;
            case R.id.ll_games:
                index = 1;
                tabSelected(index);
                break;
        }
    }
}
