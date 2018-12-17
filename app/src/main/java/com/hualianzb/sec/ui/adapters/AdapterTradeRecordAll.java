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

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterTradeRecordAll extends BaseAdapter {
    private Context context;
    private String address;
    private String taday;
    private List<AllTransBean.ResultBean> listAll;

    public AdapterTradeRecordAll(Context context, String address) {
        this.context = context;
        this.address = address;
        listAll = new ArrayList<>();
        taday = TimeUtil.getDay();
    }

    public void setData(List<AllTransBean.ResultBean> listAll) {
        this.listAll = listAll;
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

        AllTransBean.ResultBean bean = listAll.get(position);
        String kind = null;
        if (null != bean) {
            kind = bean.getKind();
            holder.tvKind.setText(kind.toLowerCase());
            String from = bean.getFrom();
            if (kind.equals("ETH")) {
                String tm = bean.getTimeStamp();
                Long ddd = Long.parseLong(tm);//单位秒
                String date = TimeUtil.getTime12(ddd * 1000);
                String transTimeLast = null;
                if (date.equals(taday)) {
                    transTimeLast = TimeUtil.getTime11(ddd * 1000);
                } else {
                    transTimeLast = TimeUtil.getTime2(ddd * 1000);
                }
                holder.tvDate.setText(transTimeLast);
                String to = bean.getTo();
                String money = bean.getValue();
                String lastMoney = null;
                if (!StringUtils.isEmpty(money)) {
                    BigDecimal monryBig = Convert.fromWei(money, Convert.Unit.ETHER);
                    lastMoney = monryBig.toPlainString();
                    lastMoney = save8num(lastMoney);
                }
                if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to) && from.equals(address)) {
                    lastMoney = "-" + lastMoney;
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                    holder.tvBalance.setText(lastMoney);
                    holder.tvAddress.setText(to);
                }
                if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to) && to.equals(address)) {
                    lastMoney = "+" + lastMoney;
                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                    holder.tvBalance.setText(lastMoney);
                    holder.tvAddress.setText(from);
                }
            } else if (kind.equals("SEC")) {
                if (!from.equals(address)) {
                    holder.tvBalance.setText("+" + bean.getValue());
                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
                    holder.tvAddress.setText(from);
                } else {
                    holder.tvBalance.setText("-" + bean.getValue());
                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
                    holder.tvAddress.setText(bean.getTo());
                }
                String transTimeLast = null;
                String timeLast = bean.getTimeStamp().toString().substring(2);//16进制去掉0x
                BigInteger d = new BigInteger(timeLast, 16);
                Long ddd = Long.parseLong(d.toString(10));//单位秒
                String date = TimeUtil.getTime12(ddd);
                if (date.equals(taday)) {
                    transTimeLast = TimeUtil.getTime11(ddd * 1000);
                } else {
                    transTimeLast = TimeUtil.getTime2(ddd * 1000);
                }
                holder.tvDate.setText(transTimeLast);
            } else {


//                String toTokenAddress = bean.getTo();
                String moneyStr = bean.getInput();
                String input = bean.getInput();
                String showAddress = "0x" + input.substring(34, 74);
//                holder.tvAddress.setText(showAddress);
                String money1 = input.substring(74);//64位16进制左边带00000000
                //得到16进制去除0x0000……的16进制字符串
                int indext = getIndexNoneZore(money1);
                if (indext > 0) {
                    String noZeroResult = money1.substring(indext);
                    String moneystr = new BigInteger(noZeroResult, 16).toString(10);
                    String money = (Double.parseDouble(moneystr) / (Math.pow(10, 18))) + "";
                    if (!StringUtils.isEmpty(from)) {
                        switch (kind) {
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
//                            case "SEC":
//                                if (!from.equals(address)) {
//                                    holder.tvBalance.setText("+" + money);
//                                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
//                                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
//                                    holder.tvAddress.setText(from);
//                                } else {
//                                    holder.tvBalance.setText("-" + money);
//                                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
//                                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
//                                    holder.tvAddress.setText(showAddress);
//                                }
//                                break;
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
                        switch (kind) {
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
//                            case "SEC":
//                                if (!from.equals(address)) {
//                                    holder.tvBalance.setText("+" + money);
//                                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_int);
//                                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_green_increase));
//                                    holder.tvAddress.setText(from);
//                                } else {
//                                    holder.tvBalance.setText("-" + money);
//                                    holder.tvBalance.setTextColor(context.getResources().getColor(R.color.text_red_reduce));
//                                    holder.ivLogo.setImageResource(R.drawable.icon_transfer_out);
//                                    holder.tvAddress.setText(showAddress);
//                                }
//                                break;
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
                String timeLast = bean.getTimeStamp().toString().substring(2);//16进制去掉0x
                BigInteger d = new BigInteger(timeLast, 16);
                Long ddd = Long.parseLong(d.toString(10));//单位秒
                String date = TimeUtil.getTime12(ddd);
                if (date.equals(taday)) {
                    transTimeLast = TimeUtil.getTime11(ddd * 1000);
                } else {
                    transTimeLast = TimeUtil.getTime2(ddd * 1000);
                }
                holder.tvDate.setText(transTimeLast);
            }
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

