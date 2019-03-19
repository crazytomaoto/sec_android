package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/10/22
 * auther:wangtianyun
 * describe:首页切卡的适配器
 */
public class PagerRecyclerAdapter extends RecyclerView.Adapter<PagerRecyclerAdapter.ViewHolder> {
    private List<RememberSEC> rememberSecList;
    private Context context;
    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public PagerRecyclerAdapter(Context context) {
        rememberSecList = new ArrayList<>();
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
        RememberSEC rememberSec = rememberSecList.get(position);
        if (rememberSec != null) {
            holder.tvName.setText(rememberSec.getWalletName());
            if (rememberSec.getHowToCreate() == 1) {//创建的
                if (rememberSec.isBackup()) {//已经保存了
                    holder.tvBack.setVisibility(View.GONE);
                } else {
                    holder.tvBack.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvBack.setVisibility(View.GONE);
            }
            holder.tvAddress.setText(rememberSec.getAddress().substring(0, 10) + "…" + (rememberSec.getAddress()).substring(32, 42));
            money = getMoney();
            if (!StringUtils.isEmpty(money)) {
                holder.tvProperty.setText(money);
                if (money.equals("0")) {
                    holder.tvProperty.setText(money);
                    return;
                }
                if (money.length() > 10) {
                    holder.tvProperty.setText((money + "").substring(0,10));
                    return;
                }
                if (Double.parseDouble(money) < 1) {
                    holder.tvProperty.setText(money);
                    return;
                }
            } else {
                holder.tvProperty.setText("0");
            }
        }
        holder.ivCode.setOnClickListener(v -> UiHelper.startMakeCodeActivity(context, rememberSecList.get(position).getAddress()));
        holder.ll_base.setOnClickListener(v -> UiHelper.startManagerWalletActy(context));
    }

    @Override
    public int getItemCount() {
        return rememberSecList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setData(List<RememberSEC> rememberSecList) {
        this.rememberSecList = rememberSecList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tvBack)
        TextView tvBack;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_code)
        ImageView ivCode;
        @BindView(R.id.tv_property)
        TextView tvProperty;
        @BindView(R.id.ll_base)
        LinearLayout ll_base;

        ViewHolder(View view) {
            super(view.getRootView());
            ButterKnife.bind(this, view);
        }
    }
}

