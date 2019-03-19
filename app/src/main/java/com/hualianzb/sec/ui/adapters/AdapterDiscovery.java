package com.hualianzb.sec.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.models.TokenBean;
import com.hysd.android.platform_huanuo.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class AdapterDiscovery extends BaseAdapter {
    private Context context;
    private List<Object> list;

    public AdapterDiscovery(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        return list.size();
        return 2;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discovery, parent, false);
            holder.iv_avater = convertView.findViewById(R.id.iv_avater);
            holder.tv_kind = convertView.findViewById(R.id.tv_kind);
            holder.tv_money = convertView.findViewById(R.id.tv_money);
            holder.tv_cn_property = convertView.findViewById(R.id.tv_cn_property);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.iv_avater.setImageDrawable(context.getDrawable(R.drawable.wallet_avater));
//        } else {
//            holder.iv_avater.setImageResource(R.drawable.wallet_avater);
//        }
//        TokenBean tokenBean = list.get(position);
//        holder.tv_kind.setText(tokenBean.getName());
//        holder.tv_money.setText(NumberUtils.round(Double.parseDouble(tokenBean.getToken()), 8) + "");
        return convertView;
    }

    class ViewHolder {
        ImageView iv_avater;
        TextView tv_kind, tv_money, tv_cn_property;
    }
}

