package com.hualianzb.sec.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberSEC;
import com.hualianzb.sec.utils.StringUtils;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterWallets extends BaseAdapter {
    private Activity context;
    private List<RememberSEC> list;
    private String address;
    private boolean isFromHome;
    private Map<String, RememberSEC> map;
    private String nowAddress;

    public AdapterWallets(Activity context, String address, boolean isFromHome) {
        this.context = context;
        this.address = address;
        this.isFromHome = isFromHome;
        list = new ArrayList<>();
        getMaps();
        nowAddress = PlatformConfig.getString(Constant.SpConstant.NOWADDRESS);//当前选中钱包的地址
    }

    private void getMaps() {
        map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
    }

    public void setList(List<RememberSEC> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_change_wal, parent, false);
            holder.iv_logo = convertView.findViewById(R.id.iv_logo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RememberSEC rememberEth = list.get(position);
        if (null != rememberEth) {
            String name = rememberEth.getWalletName();
            if (!StringUtils.isEmpty(name)) {
                holder.tv_name.setText(name);
            }
            if (address.equals(rememberEth.getAddress())) {
                holder.ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_btn_can_green));
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.white));
                holder.iv_checked.setVisibility(View.VISIBLE);
            } else {
                holder.ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_edit_gray));
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.text_black));
                holder.iv_checked.setVisibility(View.GONE);
            }
        }
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.equals(list.get(position).getAddress())) {
                    return;
                } else {
                    RememberSEC old = map.get(address);
                    old.setNow(false);
                    map.put(address, old);
                    //当前的钱包改为选中
                    RememberSEC newReme = list.get(position);
                    newReme.setNow(true);
                    map.put(newReme.getAddress(), newReme);
                    //重新保存
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                    PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, newReme.getAddress());//记住当前选中钱包的地址
                    if (isFromHome) {
                        context.finish();
                    } else {
//                        Intent intent = new Intent(context, TransactionRecordActy.class);
//                        intent.putExtra("nowAddress", newReme.getAddress());
//                        context.setResult(100, intent);
                        context.finish();
                    }
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView iv_logo, iv_checked;
        TextView tv_name;
        LinearLayout ll_item;
    }

}