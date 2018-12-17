package com.hualianzb.sec.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.models.SecBalanceResultBean;
import com.hualianzb.sec.models.TokenBean;
import com.hualianzb.sec.models.TransRecordTimeRequestBean;
import com.hualianzb.sec.ui.Myservice;
import com.hualianzb.sec.ui.activitys.HomePageActivity;
import com.hualianzb.sec.ui.adapters.AdapterWallet;
import com.hualianzb.sec.ui.adapters.PagerRecyclerAdapter;
import com.hualianzb.sec.ui.basic.BasicFragment;
import com.hualianzb.sec.utils.MyWeb3jUtil;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hualianzb.sec.utils.Util;
import com.hualianzb.sec.views.AutoRecyclerViewPager;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.NumberUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class PropertyFragment extends BasicFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener {
    @BindView(R.id.lv_property)
    SlideAndDragListView lvProperty;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.iv_rig_top)
    ImageView ivRigTop;
    @BindView(R.id.vp_ad)
    AutoRecyclerViewPager vpAd;
    @BindView(R.id.custom_space)
    LinearLayout customSpace;
    protected List<View> mImageViewList = null;
    protected List<View> mViewList;
    @BindView(R.id.iv_no_net)
    ImageView ivNoNet;
    private PagerRecyclerAdapter pageAdapter;
    private List<RememberEth> listWallet;
    private int currentItem = 0;
    protected View dotView;
    private double myBalance;
    Unbinder unbinder;
    private PropertyFragment instance;
    private View view;
    private MyListener myListener;//作为属性定义
    private AdapterWallet adapterWallet;
    private ArrayList<TokenBean> list;
    private TokenBean tokenBean;
    private Map<String, RememberEth> map;
    private RememberEth rememberEth;
    private TextView tv_eth_token;
    private KProgressHUD hud;
    private String address;
    private boolean isBackMy;
    private Map<String, ArrayList<String>> mapWalletTokens;
    private ArrayList<String> test1;
    private String test1Item;
    private Menu mMenu;

    public PropertyFragment(boolean isBackMy) {
        this.isBackMy = isBackMy;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {

    }

    @Override
    public void onDragViewStart(int beginPosition) {
        tokenBean = list.get(beginPosition);
        test1Item = test1.get(beginPosition);
    }

    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {
        TokenBean tokenBean = list.remove(fromPosition);
        list.add(toPosition, tokenBean);
        String test1ItemNow = test1.remove(fromPosition);
        test1.add(toPosition, test1ItemNow);
    }

    @Override
    public void onDragViewDown(int finalPosition) {
        list.set(finalPosition, tokenBean);
        test1.set(finalPosition, test1Item);

        mapWalletTokens.put(address, test1);
        PlatformConfig.putMap(Constant.SpConstant.ALLKINDTOKEN, mapWalletTokens);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.show(getActivity(), "onItemClick   position--->" + position);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.show(getActivity(), "onItemClick   position--->" + position);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        return 0;
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {

    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {

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
        mMenu = new Menu(false);//设置取消左右滑
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
        lvProperty.setMenu(mMenu);
        list = new ArrayList<>();
        map = new HashMap<>();
        mapWalletTokens = new HashMap<>();
        test1 = new ArrayList<>();
        mViewList = new ArrayList<>();
        mImageViewList = new ArrayList<>();
        listWallet = new ArrayList<>();
        lvProperty.setOnItemClickListener(this);
        lvProperty.setOnDragDropListener((this));
        lvProperty.setOnItemLongClickListener(this);
        lvProperty.setOnSlideListener(this);
        lvProperty.setOnMenuItemClickListener(this);
        lvProperty.setOnScrollListener(this);
        pageAdapter = new PagerRecyclerAdapter(getActivity());
        lvProperty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = list.get(position).getName();
//                if (title.equals("ETH")) {
//                    UiHelper.startTransactionRecordEth(getActivity(), rememberEth.getAddress(), list.get(position).getName(), myBalance + "");
//                } else {
//                    UiHelper.startTransactionRecordActivity(getActivity(), rememberEth.getAddress(), list.get(position).getName(), list.get(position).getToken());
//                }
                UiHelper.TransactionRecordSecActivity(getActivity(), rememberEth.getAddress(), list.get(position).getName(), list.get(position).getToken());

            }
        });

        tvTitle.setText("我的钱包");
        ivBackTop.setVisibility(View.GONE);
        ivRigTop.setVisibility(View.VISIBLE);
        ivRigTop.setImageResource(R.drawable.icon_more_black);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        vpAd.setLayoutManager(mLayoutManager);
        vpAd.setHasFixedSize(true);
        pageAdapter = new PagerRecyclerAdapter(getActivity());
        vpAd.setAdapter(pageAdapter);
        vpAd.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (vpAd != null) {
                    int childCount = vpAd.getChildCount();
                    int width = vpAd.getChildAt(0).getWidth();
                    int padding = (vpAd.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

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
            }
        });
        vpAd.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                currentItem = newPosition;
                mViewList.get(oldPosition).setBackgroundResource(R.drawable.home_banner_normal);
                mViewList.get(newPosition).setBackgroundResource(R.drawable.home_banner_golden);
                //
                list.clear();
//                map.clear();
                mapWalletTokens.clear();
                test1.clear();

                rememberEth = listWallet.get(newPosition);
                address = rememberEth.getAddress();
                getDaiBi(rememberEth);

                //改变钱包的选中状态 重新保存
                RememberEth old = listWallet.get(oldPosition);
                old.setNow(false);
                map.put(old.getAddress(), old);

                //当前的钱包改为选中
                rememberEth.setNow(true);
                PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, rememberEth.getAddress());//记住当前选中钱包的地址
                map.put(rememberEth.getAddress(), rememberEth);
                //重新保存
                PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
            }
        });
        vpAd.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
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
            }
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
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
        for (RememberEth r : map.values()) {
            listWallet.add(r);
            if (r.isNow() == true) {
                rememberEth = r;
                address = rememberEth.getAddress();
            }
        }
        listWallet = Util.ListSort(listWallet);
        if (null != rememberEth) {
            adapterWallet = new AdapterWallet(getActivity(), rememberEth.getAddress());
            lvProperty.setAdapter(adapterWallet);
            adapterWallet.setList(list);
            address = rememberEth.getAddress();
        }
        handleViewPager(listWallet);
    }

    private void handleViewPager(List<RememberEth> rememberEthList) {
        if (rememberEthList == null || rememberEthList.isEmpty()) {
            return;
        }
        if (customSpace != null) {
            customSpace.removeAllViews();
        } else {
        }
        mViewList.clear();
        mImageViewList.clear();
        for (RememberEth rememberEth : rememberEthList) {
            addPoint();
        }
        if (mViewList != null && mViewList.size() != 0) {
            mViewList.get(0).setBackgroundResource(R.drawable.home_banner_golden);
        }
        vpAd.scrollToPosition(listWallet.indexOf(rememberEth));
        vpAd.stopAutoScroll();//禁止自由滚动
//        if (rememberEthList.size() > 1) {
//            vpAd.startAutoScroll();
//        }
        pageAdapter.setData(rememberEthList);
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case GlobalMessageType.MainRequest.CECTOKEN:
                setData01(message);
                break;
            case GlobalMessageType.MainRequest.CECTOKEN_LAST:
                setData01(message);
                adapterWallet.notifyDataSetChanged();
                hud.dismiss();
                break;
            case GlobalMessageType.MainRequest.ETHTOKEN:
                setData02(message);
                break;
            case GlobalMessageType.MainRequest.ETHTOKEN_LAST:
                setData02(message);
                adapterWallet.notifyDataSetChanged();
                hud.dismiss();
                break;
            case GlobalMessageType.MainRequest.SECTOKEN:
                setData03(message);
                break;
            case GlobalMessageType.MainRequest.SECTOKEN_LAST:
                setData03(message);
                adapterWallet.notifyDataSetChanged();
                hud.dismiss();
                break;
            case GlobalMessageType.MainRequest.INTTOKEN:
                setData04(message);
                break;
            case GlobalMessageType.MainRequest.INTTOKEN_LAST:
                setData04(message);
                adapterWallet.notifyDataSetChanged();
                hud.dismiss();
                break;
        }
    }

    private void addPoint() {
        dotView = LayoutInflater.from(view.getContext()).inflate(R.layout.dotview, customSpace, false);
        dotView.setBackgroundResource(R.drawable.home_banner_normal);
        mViewList.add(dotView);
        customSpace.addView(dotView);
    }

    private void setData01(Message message) {
        String cecToken1 = (String) message.obj;
        TokenBean tokenBean1 = new TokenBean();
        tokenBean1.setName("CEC");
        tokenBean1.setToken(cecToken1);
        list.add(tokenBean1);

        Map<String, ArrayList<TokenBean>> oldData = PlatformConfig.getMap(Constant.SpConstant.BANLANCES);
        if (null != oldData) {
            ArrayList<TokenBean> tokenBeans = oldData.get(rememberEth.getAddress());
            if (!Util.isListEqual(tokenBeans, list)) {
                tokenBeans = list;
                oldData.put(rememberEth.getAddress(), tokenBeans);
                PlatformConfig.putMap(Constant.SpConstant.BANLANCES, oldData);
            }
        }
        if (test1.size() == list.size()) {
            hud.dismiss();
        }
    }

    private void setData02(Message message) {
        String cecToken2 = (String) message.obj;
        pageAdapter.setMoney(Double.parseDouble(cecToken2));//为切卡设置钱数
        pageAdapter.notifyDataSetChanged();
        TokenBean tokenBean2 = new TokenBean();
        tokenBean2.setName("ETH");
        tokenBean2.setToken(cecToken2);
//        if (!list.isEmpty() || list.size() > 0) {
//            list.clear();
//        }
        list.add(tokenBean2);
        Map<String, ArrayList<TokenBean>> oldData = PlatformConfig.getMap(Constant.SpConstant.BANLANCES);
        if (null != oldData) {
            ArrayList<TokenBean> tokenBeans = oldData.get(rememberEth.getAddress());
            if (!Util.isListEqual(tokenBeans, list)) {
                tokenBeans = list;
                oldData.put(rememberEth.getAddress(), tokenBeans);
                PlatformConfig.putMap(Constant.SpConstant.BANLANCES, oldData);
            }
        }
        if (test1.size() == list.size()) {
            hud.dismiss();
        }
    }

    private void setData03(Message message) {
        String cecToken3 = (String) message.obj;
        pageAdapter.setMoney(Double.parseDouble(cecToken3));//为切卡设置钱数
        pageAdapter.notifyDataSetChanged();
        TokenBean tokenBean3 = new TokenBean();
        tokenBean3.setName("SEC");
        tokenBean3.setToken(cecToken3);
        list.add(tokenBean3);

        Map<String, ArrayList<TokenBean>> oldData = PlatformConfig.getMap(Constant.SpConstant.BANLANCES);
        if (null != oldData) {
            ArrayList<TokenBean> tokenBeans = oldData.get(rememberEth.getAddress());
            if (!Util.isListEqual(tokenBeans, list)) {
                tokenBeans = list;
                oldData.put(rememberEth.getAddress(), tokenBeans);
                PlatformConfig.putMap(Constant.SpConstant.BANLANCES, oldData);
            }
        }
        if (test1.size() == list.size()) {
            hud.dismiss();
        }
    }

    private void setData04(Message message) {
        String cecToken4 = (String) message.obj;
        TokenBean tokenBean4 = new TokenBean();
        tokenBean4.setName("INT");
        tokenBean4.setToken(cecToken4);
        list.add(tokenBean4);

        Map<String, ArrayList<TokenBean>> oldData = PlatformConfig.getMap(Constant.SpConstant.BANLANCES);
        if (null != oldData) {
            ArrayList<TokenBean> tokenBeans = oldData.get(rememberEth.getAddress());
            if (!Util.isListEqual(tokenBeans, list)) {
                tokenBeans = list;
                oldData.put(rememberEth.getAddress(), tokenBeans);
                PlatformConfig.putMap(Constant.SpConstant.BANLANCES, oldData);
            }
        }
        if (test1.size() == list.size()) {
            hud.dismiss();
        }
    }

    @Override
    protected void initLogics() {

    }

    //当前钱包的余额
    private class TaskCheckBanlance extends AsyncTask<String, String, String> {
        private int nowIndex;

        public TaskCheckBanlance(int nowIndex) {
            this.nowIndex = nowIndex;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            EthGetBalance ethGetBalance1 = null;
            try {
                ethGetBalance1 = MyWeb3jUtil.getWeb3jInstance().ethGetBalance(rememberEth.getAddress(), DefaultBlockParameterName.LATEST).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BigDecimal nig = Convert.fromWei(ethGetBalance1.getBalance().toString(), Convert.Unit.ETHER);
            result = nig.stripTrailingZeros().toPlainString();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            myBalance = NumberUtils.round(Double.parseDouble(s), 2);

            if (nowIndex == test1.size() - 1) {
                sendMessage(GlobalMessageType.MainRequest.ETHTOKEN_LAST, myBalance + "");
            } else {
                sendMessage(GlobalMessageType.MainRequest.ETHTOKEN, myBalance + "");
            }
            PlatformConfig.setValue(Constant.SpConstant.MYBANLANCE, myBalance);
//            tvProperty.setText("≈ " + myBalance);
//            pageAdapter.setMoney(myBalance);
        }
    }

    private void getDaiBi(RememberEth rememberEth) {
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("  加载中  ")
                .setCancellable(false);
        if (!isNetworkAvailable(getActivity())) {
            ToastUtil.show(getActivity(), getResources().getString(R.string.net_work_err));
            return;
        }
        mapWalletTokens = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
        test1 = mapWalletTokens.get(rememberEth.getAddress());
        if (null == test1 || test1.isEmpty() || test1.size() <= 0) {
            test1 = new ArrayList<String>();
            test1.add("ETH");
            test1.add("CEC");
        }
        if (!isBackMy) {
            hud.show();
        }
        for (int i = 0; i < test1.size(); i++) {
            String tokenAddress = test1.get(i);
            if (tokenAddress.equals("ETH")) {
                TaskCheckBanlance taskCheckBanlance = new TaskCheckBanlance(i);
                taskCheckBanlance.execute();
                continue;
            }
            if (tokenAddress.equals("CEC")) {
                AccountsTask task1 = new AccountsTask("CEC", i);
                task1.execute(RequestHost.url);
                continue;
            }
            if (tokenAddress.equals("SEC")) {
                AccountsTask task1 = new AccountsTask("SEC", i);
                task1.execute(RequestHost.url);
                continue;
            }
            if (tokenAddress.equals("INT")) {
                AccountsTask task1 = new AccountsTask("INT", i);
                task1.execute(RequestHost.url);
                continue;
            }
        }


    }

    private class AccountsTask extends AsyncTask<String, String, String> {
        private String tag;
        private int nowIndex;

        public AccountsTask(String tag, int nowIndex) {
            this.tag = tag;
            this.nowIndex = nowIndex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            //获取余额
            Web3j web3j = MyWeb3jUtil.getWeb3jInstance();
            String xAddress = null;
            try {
                switch (tag) {
                    case "CEC":
                        xAddress = rememberEth.getAddress().toString().trim().substring(2);
                        result = web3j.ethCall(Transaction.createEthCallTransaction(rememberEth.getAddress(), RequestHost.cecToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();
                        break;
                    case "SEC"://获取SEC余额
//                        xAddress = rememberEth.getAddress().toString().trim().substring(2);
//                        result = web3j.ethCall(Transaction.createEthCallTransaction(rememberEth.getAddress(), RequestHost.secToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();
                        getSecBalance(address.substring(2), nowIndex);
                        break;
                    case "INT":
                        xAddress = rememberEth.getAddress().toString().trim().substring(2);
                        result = web3j.ethCall(Transaction.createEthCallTransaction(rememberEth.getAddress(), RequestHost.intToken, "0x70a08231000000000000000000000000" + xAddress), DefaultBlockParameterName.PENDING).send().getValue();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!StringUtils.isEmpty(result)) {
                return result.substring(2);
            } else {
                return "000";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //得到16进制去除0x0000……的16进制字符串
            int index = getIndexNoneZore(result);
            if (index > 0) {
                String noZeroResult = result.substring(index);
                String str = new BigInteger(noZeroResult, 16).toString(10);
                String money = (Double.parseDouble(str) / (Math.pow(10, 18))) + "";
//                mmp.put(tag, money);
                switch (tag) {
                    case "CEC":
                        if (nowIndex == test1.size() - 1) {
                            sendMessage(GlobalMessageType.MainRequest.CECTOKEN_LAST, money);
                        } else {
                            sendMessage(GlobalMessageType.MainRequest.CECTOKEN, money);
                        }
                        break;
//                    case "SEC":
//                        if (nowIndex == test1.size() - 1) {
//                            sendMessage(GlobalMessageType.MainRequest.SECTOKEN_LAST, money);
//                        } else {
//                            sendMessage(GlobalMessageType.MainRequest.SECTOKEN, money);
//                        }
//                        break;
                    case "INT":
                        if (nowIndex == test1.size() - 1) {
                            sendMessage(GlobalMessageType.MainRequest.INTTOKEN_LAST, money);
                        } else {
                            sendMessage(GlobalMessageType.MainRequest.INTTOKEN, money);
                        }
                        break;
                }
            } else {
                switch (tag) {
                    case "CEC":
                        if (nowIndex == test1.size() - 1) {
                            sendMessage(GlobalMessageType.MainRequest.CECTOKEN_LAST, "0");
                        } else {
                            sendMessage(GlobalMessageType.MainRequest.CECTOKEN, "0");
                        }
                        break;
                    case "ETH":
                        sendMessage(GlobalMessageType.MainRequest.ETHTOKEN, "0");
                        break;
//                    case "SEC":
//                        if (nowIndex == test1.size() - 1) {
//                            sendMessage(GlobalMessageType.MainRequest.SECTOKEN_LAST, "0");
//                        } else {
//                            sendMessage(GlobalMessageType.MainRequest.SECTOKEN, "0");
//                        }
//                        break;
                    case "INT":
                        if (nowIndex == test1.size() - 1) {
                            sendMessage(GlobalMessageType.MainRequest.INTTOKEN_LAST, "0");
                        } else {
                            sendMessage(GlobalMessageType.MainRequest.INTTOKEN, "0");
                        }
                        break;
                }
            }
        }

    }

    //获取SEC余额
    private void getSecBalance(String address, int nowIndex) {
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
                            }
                            if (!StringUtils.isEmpty(resultBean.getStatus()) && resultBean.getStatus().equals("0")) {
                                money = "0";
                            }
                        }
                    }
                    if (nowIndex == test1.size() - 1) {
                        sendMessage(GlobalMessageType.MainRequest.SECTOKEN_LAST, money);
                    } else {
                        sendMessage(GlobalMessageType.MainRequest.SECTOKEN, money);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("web3", ex.toString());
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

    private int getIndexNoneZore(String num) {
        char[] temp = num.toCharArray();
        int index = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != '0') {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_rig_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_rig_top:
                if (null == rememberEth || StringUtils.isEmpty(rememberEth.getAddress())) {
                    UiHelper.startChangeWalletRecordActy(getActivity(), "", true);
                } else {
                    UiHelper.startChangeWalletRecordActy(getActivity(), rememberEth.getAddress(), true);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reverse();
//        connectToService("0");
    }

    private void connectToService(String position) {
        Intent startServiceIntent = new Intent(getActivity(), Myservice.class);
        startServiceIntent.putExtra("reme", rememberEth);
        getActivity().startService(startServiceIntent);
    }

}
