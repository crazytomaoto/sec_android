package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class AdapterManageWallet extends BaseAdapter {
    private Context context;
    private List<RememberEth> list;
    private List<String> listBalance;

    public AdapterManageWallet(Context context) {
        this.context = context;
        list = new ArrayList<>();
        listBalance = new ArrayList<>();
    }

    public void setList(List<RememberEth> list, List<String> listBalance) {
        this.list = list;
        this.listBalance = listBalance;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_manage_wallet, parent, false);
            holder.iv_avater = convertView.findViewById(R.id.iv_avater);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_money = convertView.findViewById(R.id.tv_money);
            holder.tv_cn_property = convertView.findViewById(R.id.tv_cn_property);
            holder.iv_backup = convertView.findViewById(R.id.iv_backup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RememberEth rememberEth = list.get(position);
        String money = listBalance.get(position);
        if (null != rememberEth) {
            holder.iv_avater.setImageResource(ImageUtils.getWalletImage(rememberEth.getWalletincon()));
            holder.tv_name.setText(rememberEth.getWalletName());
            holder.tv_address.setText(rememberEth.getAddress().substring(0, 10) + "…" + rememberEth.getAddress().substring(32, 42));
            holder.tv_money.setText(money + "ETH");
            if (rememberEth.getHowToCreate() == 1) {//创建的
                if (rememberEth.isBackup()) {//已经保存了
                    holder.iv_backup.setVisibility(View.GONE);
                } else {
                    holder.iv_backup.setVisibility(View.VISIBLE);
                }
            } else {
                holder.iv_backup.setVisibility(View.GONE);
            }
        }
//        holder.iv_backup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UiHelper.startMakeMoneyActicity(context, rememberEth.getAddress());
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_avater, iv_backup;
        TextView tv_name, tv_address, tv_money, tv_cn_property;
    }
}

