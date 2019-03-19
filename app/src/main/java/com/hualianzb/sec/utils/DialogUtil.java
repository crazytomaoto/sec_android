package com.hualianzb.sec.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.interfaces.IDialogSure;
import com.hualianzb.sec.interfaces.IRerey;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DialogUtil {

    //提示弹框
    public static void showToastDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.layout_toast);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView yes = dialog.findViewById(R.id.tv__ok);
        TextView tv_content = dialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showMoreNumberDialog(final Activity context, String num) {
        Dialog dialog = new Dialog(context, R.style.ActionTopDialogStyle);
        dialog.setContentView(R.layout.dialog_top_new_trade);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.TOP;
        dialogWindow.setAttributes(lp);
        dialog.show();
        TextView tv_new = dialog.findViewById(R.id.tv_new);
        tv_new.setText("已更新" + num + "条数据");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
    }

    public static Dialog showNoNetDialog(final Activity context) {
        Dialog dialog = new Dialog(context, R.style.ActionTopDialogStyle);
        dialog.setContentView(R.layout.layout_network_tip);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        Window mWindow = dialog.getWindow();
        int mWidth, mHeight;
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
        mWindow.setLayout(mWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.TOP;
        mWindow.setAttributes(lp);


        return dialog;
    }

    //Loading弹框
    public static Dialog showLoadingDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialogLoading);
        dialog.setContentView(R.layout.layout_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tv_content = dialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
        return dialog;
    }

    //提示弹框
    public static Dialog TipsDialog(Context context, int img_id, String title, int titleColor, String content, IDialogSure iDialogSure) {
        final Dialog dialog = new Dialog(context, R.style.dialog_tips);
        dialog.setContentView(R.layout.layout_tipsdialog);
        //改变蒙版的透明度
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setCanceledOnTouchOutside(false);//点击dialog区域以外的地方dialog是否可以取消
        dialog.setCancelable(false);//是否可以取消dialog
        //初始化布局控件
        ImageView iv_img = dialog.findViewById(R.id.iv_img);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_content = dialog.findViewById(R.id.tv_content);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        //设置控件
        iv_img.setBackgroundResource(img_id);
        tv_title.setText(title);
        switch (titleColor) {
            case 0:
                tv_title.setTextColor(context.getResources().getColor(R.color.text_error));
                break;
            case 1:
                tv_title.setTextColor(context.getResources().getColor(R.color.text_blue));
                break;
            case 2:

                break;
        }
        tv_content.setText(content);
        //事件
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogSure.sure(v, dialog);
            }
        });
        return dialog;
    }

    //版本更新提示弹框
    public static Dialog upDateDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog_tips);
        dialog.setContentView(R.layout.layout_update_dialog);
        //改变蒙版的透明度
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);//点击dialog区域以外的地方dialog是否可以取消
        dialog.setCancelable(false);//是否可以取消dialog
        return dialog;
    }

    //不要截屏提示弹框
    public static Dialog dialogNoShot(Activity context, IDialogSure iDialogSure) {
        final Dialog dialog = new Dialog(context, R.style.dialog_tips);
        dialog.setContentView(R.layout.layout_dialog_noshot);
        //改变蒙版的透明度
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        Window mWindow = dialog.getWindow();
        int mWidth, mHeight;
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
        mWindow.setLayout(mWidth, mHeight);
        mWindow.setAttributes(lp);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //初始化布局控件
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        //事件
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogSure.sure(v, dialog);
            }
        });
        return dialog;
    }

    //提示弹框
    public static Dialog noNetTips(Context context, String tips, IRerey iRerey) {
        final Dialog dialog = new Dialog(context, R.style.dialog_tips);
        dialog.setContentView(R.layout.layout_no_net);
        //改变蒙版的透明度
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);//点击dialog区域以外的地方dialog是否可以取消
        dialog.setCancelable(false);//是否可以取消dialog
        //初始化布局控件

        TextView tv_content = dialog.findViewById(R.id.tv_content);
        TextView tv_retry = dialog.findViewById(R.id.tv_retry);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_content.setText(tips);
        //设置控件
        //事件
        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRerey.retry();
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }

    //提示弹框
    public static Dialog twoButtonTipsDialog(Context context, int img_id, String title, String content, IDialogSure iDialogSure) {
        final Dialog dialog = new Dialog(context, R.style.dialog_tips);
        dialog.setContentView(R.layout.layout_twobtn_dialog);
        //改变蒙版的透明度
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);//点击dialog区域以外的地方dialog是否可以取消
        dialog.setCancelable(false);//是否可以取消dialog
        //初始化布局控件
        ImageView iv_img = dialog.findViewById(R.id.iv_img);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_content = dialog.findViewById(R.id.tv_content);
        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        //设置控件
        iv_img.setBackgroundResource(img_id);
        tv_title.setText(title);
        tv_content.setText(content);
        //事件
        tv_sure.setOnClickListener(v -> iDialogSure.sure(v, dialog));
        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }


}
