package com.hysd.android.platform_huanuo.base.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hysd.android.platform_huanuo.utils.BeanUtils;

import java.util.List;

/**
 * Filename		: ResultSimpleAdapter.java
 * @Description	:
 * @Author		: 刘剑
 * @Version		 ：1.0
 * @Date	            ：2011-12-14 下午09:42:44
 */
public class ResultSimpleAdapter extends BaseAdapter{
	
	private Context context;
	private List<?> items;
	private int layoutId;
	private String[] keys;
	private int[] textid;
	/** 解析帮助对象*/
	private ISimpleAdapterHelper helper;
	
	public ResultSimpleAdapter(Context context,List<?> reuslts,int layoutId,String[] keys,int[] txtids){
		this.context = context;
		this.items = reuslts;
		this.layoutId = layoutId;
		this.keys = keys;
		this.textid = txtids;
	}
	
	public void setHelper(ISimpleAdapterHelper helper) {
		this.helper = helper;
	}


	@Override
	public int getCount() {
		return items!=null ? items.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		try{
			return items!=null ? items.get(position) : null;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(layoutId, null);
		}
		for(int i = 0;i < textid.length;i++){
			View view = convertView.findViewById(textid[i]);
			Object obj = BeanUtils.getFieldValue(items.get(position), keys[i]);
			//其他的数据解析方式
			if(this.helper!=null){
				obj = this.helper.parseValue(obj, items, position, keys[i], view);
			}
			//将值设置对视图里
			setViewValue(view, obj);
		}
		//其他的应用
		if(this.helper!=null){
			this.helper.apply(convertView,items.get(position),position);
		}
		return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	/**
	 * 将值设置对视图里
	 * @param view
	 * @param obj
	 */
	protected void setViewValue(View view,Object obj){
		if(view instanceof TextView){
			TextView tempTextView = (TextView)view;
			String context = obj == null ? "" : obj.toString();
			if(context.indexOf("/>") >= 0){
				tempTextView.setText(Html.fromHtml(context));
			}else{
				tempTextView.setText(context);
			}
		}
	}
}
