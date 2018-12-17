package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.PropertyBean;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterProperty extends BaseAdapter {
    private Context context;
    private ArrayList<PropertyBean> list;
    private ArrayList<String> tokenKinds;
    private String address;
    private Map<String, ArrayList<String>> maps;

    public AdapterProperty(Context context) {
        this.context = context;
        list = new ArrayList<>();
        tokenKinds = new ArrayList<>();
        maps = new HashMap<>();
    }

    public void setList(ArrayList<PropertyBean> list, ArrayList<String> tokenKinds, String address, Map<String, ArrayList<String>> maps) {
        this.list = list;
        this.tokenKinds = tokenKinds;
        this.address = address;
        this.maps = maps;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_property_kind, parent, false);
            holder.iv_avater = convertView.findViewById(R.id.iv_avater);
            holder.tv_symbol = convertView.findViewById(R.id.tv_symbol);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_token = convertView.findViewById(R.id.tv_token);
            holder.iv_choose = convertView.findViewById(R.id.iv_choose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PropertyBean bean = list.get(position);
        if (null != bean) {
            holder.tv_symbol.setText(bean.getName());
            holder.tv_name.setText(bean.getAllName());
            holder.tv_token.setText(bean.getTokenAddress());
            if (position == 0 || position == 1) {
                holder.iv_choose.setVisibility(View.GONE);
            } else {
                if (tokenKinds.contains(bean.getName())) {
                    holder.iv_choose.setBackground(context.getResources().getDrawable(R.drawable.icon_tongle_golden));
                } else {
                    holder.iv_choose.setBackground(context.getResources().getDrawable(R.drawable.icon_tongle_gray));
                }
            }
        }
        ViewHolder finalHolder = holder;
        holder.iv_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
                if (tokenKinds.contains(bean.getName())) {
                    tokenKinds.remove(bean.getName());
                    maps.put(address, tokenKinds);
                    PlatformConfig.putMap(Constant.SpConstant.ALLKINDTOKEN, maps);
                    finalHolder.iv_choose.setBackground(context.getResources().getDrawable(R.drawable.icon_tongle_gray));
                    notifyDataSetChanged();
                } else {
                    tokenKinds.add(bean.getName());
                    maps.put(address, tokenKinds);
                    PlatformConfig.putMap(Constant.SpConstant.ALLKINDTOKEN, maps);
                    finalHolder.iv_choose.setBackground(context.getResources().getDrawable(R.drawable.icon_tongle_golden));
                    notifyDataSetChanged();
                }
            }
        });
        switch (position) {
            case 0:
                holder.iv_avater.setImageResource(R.drawable.icon_cec);
                break;
            case 1:
                holder.iv_avater.setImageResource(R.drawable.icon_eth);
                break;
            case 2:
                holder.iv_avater.setImageResource(R.drawable.icon_sec);
                break;
            case 3:
                holder.iv_avater.setImageResource(R.drawable.icon_int);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_avater;
        TextView tv_symbol, tv_name, tv_token;
        CheckBox iv_choose;
    }
}

