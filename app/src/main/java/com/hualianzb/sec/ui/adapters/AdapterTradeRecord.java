package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.EthTransLogBean;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterTradeRecord extends BaseAdapter {
    private Context context;
    private List<EthTransLogBean.ResultBean> list;
    private String taday;
    private String address;
    private String title;

    public AdapterTradeRecord(Context context, String address, String title) {
        this.context = context;
        list = new ArrayList<>();
        this.address = address;
        this.title = title;
        taday = TimeUtil.getDay();
    }

    public void setList(List<EthTransLogBean.ResultBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trade_record, parent, false);
            holder.iv_logo = convertView.findViewById(R.id.iv_logo);
            holder.tv_balance = convertView.findViewById(R.id.tv_balance);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EthTransLogBean.ResultBean resultBean = list.get(position);
        if (null != resultBean) {
            String tm = resultBean.getTimeStamp();
            Long ddd = Long.parseLong(tm);//单位秒
            String date = TimeUtil.getTime12(ddd * 1000);
            String transTimeLast = null;
            if (date.equals(taday)) {
                transTimeLast = TimeUtil.getTime11(ddd * 1000);
            } else {
                transTimeLast = TimeUtil.getTime2(ddd * 1000);
            }
            holder.tv_date.setText(transTimeLast);

            String from = resultBean.getFrom();
            String to = resultBean.getTo();
            String money = resultBean.getValue();
            String lastMoney = null;
            if (!StringUtils.isEmpty(money)) {
                BigDecimal monryBig = Convert.fromWei(money, Convert.Unit.ETHER);
                lastMoney = monryBig.toPlainString();
                lastMoney = save8num(lastMoney);
            }
            if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
                if (from.equals(address)) {
                    lastMoney = "-" + lastMoney;
                    holder.tv_balance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                    holder.iv_logo.setImageResource(R.drawable.icon_transfer_out);
                    holder.tv_balance.setText(lastMoney);
                    holder.tv_address.setText(to);
                } else {
                    lastMoney = "+" + lastMoney;
                    holder.iv_logo.setImageResource(R.drawable.icon_transfer_int);
                    holder.tv_balance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                    holder.tv_balance.setText(lastMoney);
                    holder.tv_address.setText(from);
                }

            }
//            if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to) && to.equals(address)) {
//
//            }
        }
        return convertView;
    }

    //保留八位小数
    private String save8num(String num) {
        Double cny = Double.parseDouble(num);//转换成Double
        DecimalFormat df = new DecimalFormat("0.00000000");//格式化
        String now8 = df.format(cny);
        return now8;
    }

    static class ViewHolder {
        TextView tv_balance, tv_address, tv_date;
        ImageView iv_logo;
    }
}

