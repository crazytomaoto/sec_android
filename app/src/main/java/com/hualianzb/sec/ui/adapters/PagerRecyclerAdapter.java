package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.utils.ImageUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/22
 * auther:wangtianyun
 * describe:首页切卡的适配器
 */
public class PagerRecyclerAdapter extends RecyclerView.Adapter<PagerRecyclerAdapter.ViewHolder> {
    private List<RememberEth> rememberEthList;
    private Context context;
    private int itemIndex;
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public PagerRecyclerAdapter(Context context) {
        rememberEthList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_pager, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RememberEth rememberEth = rememberEthList.get(position);
        if (rememberEth != null) {
            String address = rememberEth.getAddress();
            holder.ivAvater.setImageResource(ImageUtils.getWalletImage(rememberEth.getWalletincon()));
            holder.tvName.setText(rememberEth.getWalletName());
            if (rememberEth.getHowToCreate() == 1) {//创建的
                if (rememberEth.isBackup()) {//已经保存了
                    holder.ivBack.setVisibility(View.GONE);
                } else {
                    holder.ivBack.setVisibility(View.VISIBLE);
                }
            } else {
                holder.ivBack.setVisibility(View.GONE);
            }
            holder.tvAddress.setText(rememberEth.getAddress());
            switch (position % 3) {
                case 0:
                    holder.rlBg.setBackgroundResource(R.drawable.vp_golden);
                    break;
                case 1:
                    holder.rlBg.setBackgroundResource(R.drawable.vp_black);
                    break;
                case 2:
                    holder.rlBg.setBackgroundResource(R.drawable.vp_red);
                    break;
            }
            holder.tvProperty.setText(getMoney() > 0 ? "≈ " + getMoney() : "≈ 0.0 ");
        }

        holder.llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHelper.startMakeMoneyActicity(context, rememberEthList.get(position).getAddress(), getMoney() + "", true);
            }
        });
        holder.ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHelper.startMakeCodeActivity(context, rememberEthList.get(position).getAddress());
            }
        });
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, ArrayList<String>> test1 = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
                ArrayList<String> list = test1.get(rememberEthList.get(position).getAddress());
                UiHelper.startAddPropertyActivity(context, list, rememberEthList.get(position).getAddress());
            }
        });


    }

    @Override
    public int getItemCount() {
        return rememberEthList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setData(List<RememberEth> rememberEthList) {
        this.rememberEthList = rememberEthList;
        notifyDataSetChanged();
    }

    @OnClick({R.id.ivBack, R.id.ll_top, R.id.iv_add, R.id.iv_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
//                Map<String, ArrayList<String>> test1 = PlatformConfig.getMap(Constant.SpConstant.ALLKINDTOKEN);
//                ArrayList<String> list = test1.get(rememberEthList.get(getItemIndex()).getAddress());
//                UiHelper.startAddPropertyActivity(context, list, rememberEthList.get(getItemIndex()).getAddress());
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avater)
        ImageView ivAvater;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ivBack)
        ImageView ivBack;
        @BindView(R.id.ll_top)
        LinearLayout llTop;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_code)
        ImageView ivCode;
        @BindView(R.id.tv_property)
        TextView tvProperty;
        @BindView(R.id.iv_add)
        ImageView ivAdd;
        @BindView(R.id.rl_bg)
        RelativeLayout rlBg;

        ViewHolder(View view) {
            super(view.getRootView());
            ButterKnife.bind(this, view);
        }
    }
}

