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
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.TimeUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AdapterTradeRecord2 extends BaseAdapter {
    private Context context;
    private String address, title;
    private String taday;
    private List<AllTransBean.ResultBean> list;

    public AdapterTradeRecord2(Context context, String address, String title) {
        this.context = context;
        list = new ArrayList<>();
        this.address = address;
        this.title = title;
        taday = TimeUtil.getDay();
    }

    public void setData(List<AllTransBean.ResultBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trade_record2, parent, false);
            holder.ivLogo = convertView.findViewById(R.id.iv_logo);
            holder.tvBalance = convertView.findViewById(R.id.tv_balance);
            holder.tvKind = convertView.findViewById(R.id.tv_kind);
            holder.tvDate = convertView.findViewById(R.id.tv_date);
            holder.tv_state = convertView.findViewById(R.id.tv_state);
            holder.tvAddress = convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvKind.setText(title.toLowerCase());
//        if (null != list1 && list1.size() > 0) {
//            TransRecorderBeanOne.ResultBean resultBean1 = list1.get(position);
//            if (null != resultBean1) {
//                String address = resultBean1.getAddress();
//                if (!StringUtils.isEmpty(address)) {
//                    holder.tvAddress.setText(address);
//                }
//            }
//        }
        AllTransBean.ResultBean resultBean = list.get(position);
        if (null != resultBean) {
            String from = resultBean.getFrom();
            String moneyStr = resultBean.getInput();
            String input = resultBean.getInput();
            String showAddress = "0x" + input.substring(34, 74);
//            holder.tvAddress.setText(showAddress);
            String money1 = input.substring(74);//64位16进制左边带00000000
            //得到16进制去除0x0000……的16进制字符串
            int indext = getIndexNoneZore(money1);
            if (indext > 0) {
                String noZeroResult = money1.substring(indext);
                String moneystr = new BigInteger(noZeroResult, 16).toString(10);
                String money = (Double.parseDouble(moneystr) / (Math.pow(10, 18))) + "";
                if (!StringUtils.isEmpty(from)) {
                    switch (title) {
                        case "CEC":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
//                        case "ETH":
//                            if (!from.equals(address)) {
//                                holder.tvBalance.setText("+" + money);
//                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
//                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
//                            } else {
//                                holder.tvBalance.setText("-" + money);
//                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
//                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
//                            }
//                            break;
                        case "SEC":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
                        case "INT":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
                    }
                }
                Log.e("web3", "moneyStr--+\n" + moneyStr);

            } else {
                String money = "0.00000000";
                if (!StringUtils.isEmpty(from)) {
                    switch (title) {
                        case "CEC":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
//                        case "ETH":
//                            if (!from.equals(address)) {
//                                holder.tvBalance.setText("+" + money);
//                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
//                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
//                            } else {
//                                holder.tvBalance.setText("-" + money);
//                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
//                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
//                            }
//                            break;
                        case "SEC":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
                        case "INT":
                            if (!from.equals(address)) {
                                holder.tvBalance.setText("+" + money);
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                                holder.tvAddress.setText(from);
                            } else {
                                holder.tvBalance.setText("-" + money);
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                                holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                                holder.tvAddress.setText(showAddress);
                            }
                            break;
                    }
                }
            }

            String transTimeLast = null;
            String timeLast = resultBean.getTimeStamp().toString().substring(2);//16进制去掉0x
            BigInteger d = new BigInteger(timeLast, 16);
            Long ddd = Long.parseLong(d.toString(10));//单位秒
            String date = TimeUtil.getTime12(ddd * 1000);
            if (date.equals(taday)) {
                transTimeLast = TimeUtil.getTime11(ddd * 1000);
            } else {
                transTimeLast = TimeUtil.getTime2(ddd * 1000);
            }
            holder.tvDate.setText(transTimeLast);

        }
        return convertView;
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

    static class ViewHolder {
        ImageView ivLogo;
        TextView tvAddress, tvBalance, tvKind, tvDate, tv_state;
    }
}

