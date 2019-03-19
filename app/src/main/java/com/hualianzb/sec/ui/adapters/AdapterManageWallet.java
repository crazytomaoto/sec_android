package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.ManageWalletBean;
import com.hualianzb.sec.models.RememberSEC;

import java.util.ArrayList;
import java.util.List;

public class AdapterManageWallet extends BaseAdapter {
    private Context context;
    private List<ManageWalletBean> list;

    public AdapterManageWallet(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<ManageWalletBean> list) {
        this.list = list;
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
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_money = convertView.findViewById(R.id.tv_money);
            holder.tv_backup = convertView.findViewById(R.id.tv_backup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ManageWalletBean rememberSec = list.get(position);
        if (null != rememberSec) {
            holder.tv_name.setText(rememberSec.getWalletName());
            holder.tv_address.setText(rememberSec.getAddress().substring(0, 10) + "…" + rememberSec.getAddress().substring(32, 42));
            double myMoney = Double.parseDouble(rememberSec.getMoney());
            if (myMoney == 0) {
                holder.tv_money.setText("0 SEC");
            } else if (myMoney > 0) {
                if ((myMoney + "").length() > 10) {
                    holder.tv_money.setText((myMoney + "").substring(0,10) + " SEC");
                } else {
                    holder.tv_money.setText((myMoney + " SEC"));
                }
            } else {
                holder.tv_money.setText((myMoney + " SEC"));
            }

            if (rememberSec.getHowToCreate() == 1) {//创建的
                if (rememberSec.isBackup()) {//已经保存了
                    holder.tv_backup.setVisibility(View.GONE);
                } else {
                    holder.tv_backup.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tv_backup.setVisibility(View.GONE);
            }
        }
//        holder.tv_backup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UiHelper.startMakeMoneyActicity(context, rememberSec.getAddress());
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_address, tv_money, tv_backup;
    }
}

