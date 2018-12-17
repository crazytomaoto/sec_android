package com.hualianzb.sec.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.activitys.TransactionRecordActy;
import com.hualianzb.sec.utils.ImageUtils;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterWallets extends BaseAdapter {
    private Activity context;
    private List<RememberEth> list;
    private String address;
    private boolean isFromHome;
    private Map<String, RememberEth> map;
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

    public void setList(List<RememberEth> list) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RememberEth rememberEth = list.get(position);
        if (null != rememberEth) {
            holder.iv_logo.setImageResource(ImageUtils.getWalletImage(rememberEth.getWalletincon()));
            String name = rememberEth.getWalletName();
            if (!StringUtils.isEmpty(name)) {
                holder.tv_name.setText(name);
            }
            if (address.equals(rememberEth.getAddress())) {
                holder.ll_item.setBackground(context.getResources().getDrawable(R.drawable.btn_golde));
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.ll_item.setBackground(context.getResources().getDrawable(R.drawable.btn_normal_golden));
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.text_black_home));
            }
        }
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.equals(list.get(position).getAddress())) {
                    return;
                } else {
                    RememberEth old = map.get(address);
                    old.setNow(false);
                    map.put(address, old);
                    //当前的钱包改为选中
                    RememberEth newReme = list.get(position);
                    newReme.setNow(true);
                    map.put(newReme.getAddress(), newReme);
                    //重新保存
                    PlatformConfig.putMap(Constant.SpConstant.WALLET, map);

                    PlatformConfig.setValue(Constant.SpConstant.NOWADDRESS, newReme.getAddress());//记住当前选中钱包的地址
                    if (isFromHome) {
                        UiHelper.startHomaPageAc(context, newReme.getAddress());
                    } else {
                        Intent intent = new Intent(context, TransactionRecordActy.class);
                        intent.putExtra("nowAddress", newReme.getAddress());
                        context.setResult(100, intent);
                        context.finish();
                    }
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView iv_logo;
        TextView tv_name;
        LinearLayout ll_item;
    }

}