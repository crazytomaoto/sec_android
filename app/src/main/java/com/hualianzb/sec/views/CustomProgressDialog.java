package com.hualianzb.sec.views;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置默认高度为160，宽度120，并且可根据屏幕像素密度自动进行大小调整
 */
public class CustomProgressDialog extends Dialog {

    private static int default_width = 360; //默认宽度
    private static int default_height = 640;//默认高度

    public CustomProgressDialog(Context context, int layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public CustomProgressDialog(Context context, int width, int height, int layout, int style) {
        super(context, style);

        //set content
        setContentView(layout);

        //set window params
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        //set width,height by density and gravity
        float density = getDensity(context);
        params.width = (int) (width * density);
        params.height = (int) (height * density);
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

}