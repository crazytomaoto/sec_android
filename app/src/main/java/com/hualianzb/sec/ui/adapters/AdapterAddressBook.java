package com.hualianzb.sec.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.interfaces.GlobalMessageType;
import com.hualianzb.sec.models.AddressBookBean;
import com.hualianzb.sec.ui.activitys.TransferActivity;
import com.hualianzb.sec.utils.StringUtils;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2018/10/17
 * auther:wangtianyun
 * describe:
 */
public class AdapterAddressBook extends BaseAdapter {
    private List<AddressBookBean> list;
    private Activity context;
    Handler handler;
    private boolean isFromMy;

    public AdapterAddressBook(Activity context, Handler handler, boolean isFromMy) {
        this.context = context;
        this.list = new ArrayList<>();
        this.handler = handler;
        this.isFromMy = isFromMy;
    }

    public void setList(List<AddressBookBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_book, parent, false);
            holder.iv_change = convertView.findViewById(R.id.iv_change);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_phone = convertView.findViewById(R.id.tv_phone);
            holder.tv_cn_property = convertView.findViewById(R.id.tv_cn_property);
            holder.tv_delete = convertView.findViewById(R.id.tv_delete);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.line = convertView.findViewById(R.id.line);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddressBookBean bean = list.get(position);
        if (null != bean) {
            holder.tv_name.setText(bean.getName());
            holder.tv_address.setText(bean.getAddress().substring(0, 8) + "…" + bean.getAddress().substring(34));
            if (StringUtils.isEmpty(bean.getPhone())) {
                holder.tv_phone.setVisibility(View.GONE);
            } else {
                holder.tv_phone.setText(bean.getPhone());
            }
        }
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                PlatformConfig.putList(Constant.SpConstant.ADDRESSBOOK, list);
                handler.sendEmptyMessage(GlobalMessageType.MessgeCode.NOTIFYBOOKLIST);
            }
        });
        holder.iv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHelper.startChangeAddressBookActys(context, position);
            }
        });
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFromMy) {
                    Intent intent = new Intent(context, TransferActivity.class);
                    intent.putExtra("address", list.get(position).getAddress());
                    context.setResult(2, intent);
                    context.finish();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_change;
        TextView tv_name, tv_phone, tv_address, tv_cn_property, tv_delete;
        RelativeLayout rl_item;
        View line;
    }
}
