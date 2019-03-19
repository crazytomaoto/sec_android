package com.hualianzb.sec.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.interfaces.IRerey;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.models.SecBalanceResultBean;
import com.hualianzb.sec.models.TokenBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.adapters.AdapterWallet;
import com.hualianzb.sec.ui.adapters.PagerRecyclerAdapter;
import com.hualianzb.sec.ui.basic.BasicFragment;
import com.hualianzb.sec.utils.DialogUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hualianzb.sec.views.AutoRecyclerViewPager;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 资产页
 */
@SuppressLint("ValidFragment")
public class PropertyFragment extends BasicFragment {
    @BindView(R.id.lv_property)
    ListView lvProperty;
    @BindView(R.id.vp_ad)
    AutoRecyclerViewPager vpAd;
    @BindView(R.id.custom_space)
    LinearLayout customSpace;
    @BindView(R.id.iv_rig_top)
    ImageView ivBackTop;
    protected List<View> mImageViewList = null;
    protected List<View> mViewList;
    private PagerRecyclerAdapter pageAdapter;
    private List<RememberSEC> listWallet;
    protected View dotView;
    private double myBalance;
    Unbinder unbinder;
    private PropertyFragment instance;
    private View view;
    private MyListener myListener;//作为属性定义
    private AdapterWallet adapterWallet;
    private ArrayList<TokenBean> list;
    private Map<String, RememberSEC> map;
    private RememberSEC rememberSec;
    private String address;
    private Map<String, ArrayList<String>> mapWalletTokens;
    private ArrayList<String> test1;
    private Map<String, ArrayList<TokenBean>> oldData = new HashMap<>();
    private Dialog dialogDialog;

    public PropertyFragment() {
    }

    @OnClick(R.id.iv_rig_top)
    public void onViewClicked() {
        UiHelper.startChangeWalletRecordActyResult(getActivity(), address, true);
    }


    public interface MyListener {
        void sendContent(String info);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myListener = (MyListener) getActivity();
        address = ((HomePageActivity) activity).getAddress();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmengt_property, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        map = new HashMap<>();
        mapWalletTokens = new HashMap<>();
        test1 = new ArrayList<>();
        mViewList = new ArrayList<>();
        mImageViewList = new ArrayList<>();
        dialogDialog = DialogUtil.showLoadingDialog(getActivity(), getString(R.string.loading));
        listWallet = new ArrayList<>();
        lvProperty.setOnItemClickListener((parent, view, position, id) -> UiHelper.TransactionRecordSecActivity(getActivity(), rememberSec.getAddress(), list.get(position).getName(), list.get(position).getToken()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        vpAd.setLayoutManager(mLayoutManager);
        vpAd.setHasFixedSize(true);
        pageAdapter = new PagerRecyclerAdapter(getActivity());
        vpAd.setAdapter(pageAdapter);
        vpAd.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                pageAdapter.notifyDataSetChanged();
                adapterWallet.notifyDataSetChanged();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (vpAd != null) {
                    int childCount = vpAd.getChildCount();
                    int width = vpAd.getChildAt(0).getWidth();
                    int padding = (vpAd.getWidth() - width) / 2;

                    for (int j = 0; j < childCount; j++) {
                        View v = recyclerView.getChildAt(j);
                        //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                        float rate = 0;
                        if (v.getLeft() <= padding) {
                            if (v.getLeft() >= padding - v.getWidth()) {
                                rate = (padding - v.getLeft()) * 1f / v.getWidth();
                            } else {
                                rate = 1;
                            }
                            v.setScaleY(1 - rate * 0.1f);
                            v.setScaleX(1 - rate * 0.1f);
                        } else {
                            //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                            if (v.getLeft() <= recyclerView.getWidth() - padding) {
                                rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                            }
                            v.setScaleY(0.9f + rate * 0.1f);
                            v.setScaleX(0.9f + rate * 0.1f);
                        }
                    }
                }
                adapterWallet.notifyDataSetChanged();
            }
        });
        vpAd.addOnPageChangedListener((oldPosition, newPosition) -> {
            dialogDialog.show();
            mViewList.get(oldPosition).setBackgroundResource(R.drawable.home_banner_normal);
            mViewList.get(newPosition).setBackgroundResource(R.drawable.home_banner_green);
            //
            list.clear();
            mapWalletTokens.clear();
            test1.clear();
            rememberSec = listWallet.get(newPosition);
            address = rememberSec.getAddress();
            getSecBalance(address.substring(2));
            //改变钱包的选中状态 重新保存
            RememberSEC old = listWallet.get(oldPosition);
            old.setNow(false);
            map.put(old.getAddress(), old);
            //当前的钱包改为选中
            rememberSec.setNow(true);
            PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberSec.getAddress());//记住当前选中钱包的地址
            map.put(rememberSec.getAddress(), rememberSec);
            //重新保存
            PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
        });
        vpAd.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (vpAd.getChildCount() < 3) {
                if (vpAd.getChildAt(1) != null) {
                    if (vpAd.getCurrentPosition() == 0) {
                        View v1 = vpAd.getChildAt(1);
                        v1.setScaleY(0.9f);
                        v1.setScaleX(0.9f);
                    } else {
                        View v1 = vpAd.getChildAt(0);
                        v1.setScaleY(0.9f);
                        v1.setScaleX(0.9f);
                    }
                }
            } else {
                if (vpAd.getChildAt(0) != null) {
                    View v0 = vpAd.getChildAt(0);
                    v0.setScaleY(0.9f);
                    v0.setScaleX(0.9f);
                }
                if (vpAd.getChildAt(2) != null) {
                    View v2 = vpAd.getChildAt(2);
                    v2.setScaleY(0.9f);
                    v2.setScaleX(0.9f);
                }
            }
            adapterWallet.notifyDataSetChanged();
        });

    }


    private void reverse() {
        initData();
    }


    private void initData() {
        list.clear();
        map.clear();
        mapWalletTokens.clear();
        test1.clear();
        listWallet.clear();
        mImageViewList.clear();
        mViewList.clear();
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberSEC r : map.values()) {
            listWallet.add(r);
            if (r.isNow() == true) {
                rememberSec = r;
                address = rememberSec.getAddress();
            }
        }
        listWallet = Util.ListSort(listWallet);
        if (null != rememberSec) {
            adapterWallet = new AdapterWallet(getActivity(), rememberSec.getAddress());
            lvProperty.setAdapter(adapterWallet);
            adapterWallet.setList(list);
            address = rememberSec.getAddress();
        }
        try {
            handleViewPager(listWallet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleViewPager(List<RememberSEC> rememberSecList) {
        if (rememberSecList == null || rememberSecList.isEmpty()) {
            return;
        }
        if (customSpace != null) {
            customSpace.removeAllViews();
        } else {
        }
        mViewList.clear();
        mImageViewList.clear();
        for (RememberSEC rememberSec : rememberSecList) {
            addPoint();
        }
        if (mViewList != null && mViewList.size() != 0) {
            int position = listWallet.indexOf(rememberSec);
            mViewList.get(position).setBackgroundResource(R.drawable.home_banner_green);
        }
        vpAd.scrollToPosition(listWallet.indexOf(rememberSec));
        vpAd.stopAutoScroll();//禁止自由滚动
        pageAdapter.setData(rememberSecList);
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case GlobalMessageType.MainRequest.SECTOKEN:
                setData03(message);
                break;
            case GlobalMessageType.MainRequest.SECTOKEN_LAST:
                setData03(message);
                break;
        }
    }

    @Override
    protected void initLogics() {

    }

    private void addPoint() {
        dotView = LayoutInflater.from(view.getContext()).inflate(R.layout.dotview, customSpace, false);
        dotView.setBackgroundResource(R.drawable.home_banner_normal);
        mViewList.add(dotView);
        customSpace.addView(dotView);
    }


    private void setData03(Message message) {
        String cecToken3 = (String) message.obj;
        pageAdapter.setMoney(cecToken3);//为切卡设置钱数
        pageAdapter.notifyDataSetChanged();
        TokenBean tokenBean3 = new TokenBean();
        tokenBean3.setName("SEC");
        tokenBean3.setToken(cecToken3);
        list.clear();
        list.add(tokenBean3);
        Map<String, ArrayList<TokenBean>> oldData = PlatformConfig.getMap(Constant.SpConstant.BANLANCES);
        if (null != oldData) {
            ArrayList<TokenBean> tokenBeans = oldData.get(rememberSec.getAddress());
            if (!Util.isListEqual(tokenBeans, list)) {
                tokenBeans = list;
                oldData.put(rememberSec.getAddress(), tokenBeans);
                PlatformConfig.putMap(Constant.SpConstant.BANLANCES, oldData);
            }
        }
        adapterWallet.notifyDataSetChanged();
        dialogDialog.dismiss();
    }

    //获取SEC余额
    private void getSecBalance(String address) {
        TransRecordTimeRequestBean bean = new TransRecordTimeRequestBean();
        List<Object> list = new ArrayList<>();
        bean.setId(1);
        bean.setJsonrpc("2.0");
        bean.setMethod("sec_getBalance");
        bean.setParams(list);
        list.add(address);//address
        list.add("latest");
        String json = JSON.toJSONString(bean);
        RequestParams params = new RequestParams(RequestHost.secTestUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("web3", "sec_getBalance+\n" + result);
                if (!StringUtils.isEmpty(result)) {
                    String money = null;
                    SecBalanceResultBean balanceResultBean = JSON.parseObject(result, SecBalanceResultBean.class);
                    if (null != balanceResultBean) {
                        SecBalanceResultBean.ResultBean resultBean = balanceResultBean.getResult();
                        if (resultBean != null) {
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("1")) {
                                money = resultBean.getValue() + "";
                                sendMessage(GlobalMessageType.MainRequest.SECTOKEN_LAST, money);
                            }
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("0")) {
                                money = "0";
                                sendMessage(GlobalMessageType.MainRequest.SECTOKEN, money);
                            }
                        } else {
                            dialogDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("web3", ex.toString());
                DialogUtil.noNetTips(getActivity(), getString(R.string.net_work_err), () -> getSecBalance(address.substring(2)));
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Log.e("web3", "onFinished");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        reverse();
    }

}
