package com.hysd.android.platform_huanuo.base.ui.adapter;

import java.util.List;

import android.view.View;

/**
 * Filename		: ISimpleAdapterHelper.java
 * @Description	:
 * @Author		: 刘剑
 * @Version		 ：1.0
 * @Date	            ：2015年11月17日 上午10:43:56
 */
public interface ISimpleAdapterHelper {
	/**
	 * 数据解析
	 * @param currentobj
	 * @param items
	 * @param position
	 * @param key
	 * @param view
	 * @return
	 */
	public Object parseValue(Object currentobj,List<?> items,int position,String key,View view);
	
	/**
	 * 其他应用
	 * @param convertView
	 */
	public void apply(View convertView,Object obj,int positon);
}
