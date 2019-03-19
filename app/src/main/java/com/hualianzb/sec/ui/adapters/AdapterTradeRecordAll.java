package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.AllTransBean;
import com.hualianzb.sec.models.SecTransactionBean;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterTradeRecordAll extends BaseAdapter {
    private Context context;
    private String address;
    private String today;
    private List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listAll;

    public AdapterTradeRecordAll(Context context) {
        this.context = context;
        listAll = new ArrayList<>();
        today = TimeUtil.getDay();
    }

    public void setData(List<SecTransactionBean.ResultBean.ResultInChainBeanOrPool> listAll, String address) {
        this.listAll = listAll;
        this.address = address;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listAll.size();
    }

    @Override
    public Object getItem(int position) {
        return listAll.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trade_record, parent, false);
            holder.tvBalance = convertView.findViewById(R.id.tv_balance);
            holder.tvKind = convertView.findViewById(R.id.tv_kind);
            holder.tvDate = convertView.findViewById(R.id.tv_date);
            holder.tv_state = convertView.findViewById(R.id.tv_state);
            holder.tvAddress = convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SecTransactionBean.ResultBean.ResultInChainBeanOrPool bean = listAll.get(position);
        if (null != bean) {
            String from = bean.getTxFrom();
            String status = bean.getTxReceiptStatus();
            String to = bean.getTxTo();
            String money = bean.getValue() + " SEC";
            long time_Stamp = bean.getTimeStamp();
            String tvStringStatus;
            if (to.equals(address.substring(2))) {
                holder.tvAddress.setText(("0x" + from).substring(0, 10) + "…" + ("0x" + from).substring(32, 42));
            } else {
                holder.tvAddress.setText(("0x" + to).substring(0, 10) + "…" + ("0x" + to).substring(32, 42));
            }
            switch (status) {
                case "pending":
                    tvStringStatus = "(Pending)";
                    holder.tv_state.setTextColor(context.getResources().getColor(R.color.text_yellow02));
                    holder.tv_state.setText(tvStringStatus);
                    break;
                case "success":
                    holder.tv_state.setVisibility(View.GONE);
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_selected_green));
                    break;
                case "fail":
                    tvStringStatus = "Failed";
                    holder.tv_state.setTextColor(context.getResources().getColor(R.color.text_yellow02));
                    holder.tv_state.setText(tvStringStatus);
                    break;
                case "mine":
                    tvStringStatus = "Mined";
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_selected_green));
                    holder.tv_state.setTextColor(context.getResources().getColor(R.color.text_yellow02));
                    holder.tv_state.setText(tvStringStatus);
                    break;
            }
            if (to.equals(address.substring(2))) {
                money = "+" + money;
            } else {
                money = "-" + money;
            }
            holder.tvBalance.setText(money);
            String timeTemp = TimeUtil.getTime12(time_Stamp);
            if (timeTemp.equals(today)) {
                holder.tvDate.setText(TimeUtil.getTime11(time_Stamp));
            } else {
                holder.tvDate.setText(TimeUtil.getTime2(time_Stamp));
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvAddress, tvBalance, tvKind, tvDate, tv_state;
    }
}

