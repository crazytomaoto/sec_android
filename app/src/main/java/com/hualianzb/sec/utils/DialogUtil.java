package com.hualianzb.sec.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.hualianzb.sec.R;
import com.hualianzb.sec.interfaces.IOutPrivatekey;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DialogUtil {
    //    public static void showToHisMainDialog(final Context context, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_to_main);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
//        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
//        tv_cancel.setText("取消");
//        tv_sure.setText("立即访问");
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//
//        tv_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.determineDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showPublishDialog(final Context context, final IPublish dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_publish);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        dialog.show();
//        ImageView iv_cancle = dialog.findViewById(R.id.iv_cancle);
//        LinearLayout ll_news = dialog.findViewById(R.id.ll_news);
//        LinearLayout ll_infos = dialog.findViewById(R.id.ll_infos);
//        LinearLayout ll_activitie = dialog.findViewById(R.id.ll_activitie);
//        iv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//
//        ll_news.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.publishNews(view, dialog);
//            }
//        });
//        ll_infos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.publishInfos(view, dialog);
//            }
//        });
//        ll_activitie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.publishActivities(view, dialog);
//            }
//        });
//    }
//
//    public static void showIdIdentifyDialog(final Context context, String tishi, final IIdentitifyID dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_id_idengtify);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//        ImageView iv_cancle = dialog.findViewById(R.id.iv_cancle);
//        TextView tv_tishi = dialog.findViewById(R.id.tv_tishi);
//        TextView tv_tobeVip = dialog.findViewById(R.id.tv_tobeVip);
//        TextView tv_identify = dialog.findViewById(R.id.tv_identify);
//        iv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        if (StringUtils.isNotEmpty(tishi)) {
//            tv_tishi.setText(tishi);
//        }
//        tv_tobeVip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.toBeVip(view, dialog);
//            }
//        });
//        tv_identify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.toIndentifyID(view, dialog);
//            }
//        });
//    }
//
//    public static void showWaitingDialog(final Context context, final IYueJian dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_waiting);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        TextView tv_one = dialog.findViewById(R.id.tv_one);
//        TextView tv_two = dialog.findViewById(R.id.tv_two);
//        TextView tv_cancle = dialog.findViewById(R.id.tv_cancle);
//        dialog.show();
//        tv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        tv_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.oneHour(view, dialog);
//            }
//        });
//        tv_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.twoHour(view, dialog);
//            }
//        });
//    }
//
//
//    public static void showDeleteTopicDialog(final Context context, String theme, String yes, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_delete_topic);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_them = dialog.findViewById(R.id.tv_theme);
//        TextView tv_cancle = dialog.findViewById(R.id.tv_cancle);
//        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
//        tv_them.setText(theme);
//        tv_sure.setText(yes);
//        dialog.show();
//        tv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        tv_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.determineDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showSendMessageToHim(final Context context, String hisName, String count, final ISendMessage dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_send_msg_tohim);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_name = dialog.findViewById(R.id.tv_name);
//        TextView tv_count = dialog.findViewById(R.id.tv_count);
//        TextView tv_send = dialog.findViewById(R.id.tv_send);
//        final EditText ed_content = dialog.findViewById(R.id.ed_content);
//        tv_name.setText(hisName);
//        tv_count.setText(count);
//        dialog.show();
//        tv_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String content = ed_content.getText().toString().trim();
//                if (StringUtils.isNotEmpty(content)) {
//                    dialogInterface.send(view, dialog, content);
//                } else {
//                    ToastUtil.show(context, "发送内容不能为空");
//                }
//            }
//        });
//    }
//
//    /**
//     * 融资阶段
//     *
//     * @param context
//     * @param
//     */
//    public static void showRZJD(final Context context, final List<String> stringList, final IChooseText dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_send_rzjd);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        ImageView iv_close = dialog.findViewById(R.id.iv_close);
//        AdapterText adapter = new AdapterText(context);
//        adapter.setList(stringList);
//        ListView lv_rzjd = dialog.findViewById(R.id.lv_rzjd);
//        lv_rzjd.setAdapter(adapter);
//        dialog.show();
//        lv_rzjd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dialogInterface.getText(stringList.get(position).toString(), view, dialog);
//            }
//        });
//
//
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogInterface.cancelDialog(v, dialog);
//            }
//        });
//    }
//
//    public static void showCancleBaoMingDialog(final Context context, String date, String theme, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_cancle_baoming_topic);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_date = dialog.findViewById(R.id.tv_date);
//        TextView tv_topic = dialog.findViewById(R.id.tv_topic);
//        TextView tv_cancle = dialog.findViewById(R.id.tv_cancle);
//        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
//        tv_date.setText(date);
//        tv_topic.setText(theme);
//        dialog.show();
//        tv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        tv_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.determineDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showHisInfoDialog1(final Context context, String headUrl, String name, int level, String company
//            , String duty, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_showhisinfo1);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        AvatarView iv_head = dialog.findViewById(R.id.iv_head);
//        TextView tv_name = dialog.findViewById(R.id.tv_name);
//        ImageView iv_level = dialog.findViewById(R.id.iv_level);
//        TextView tv_company_duty = dialog.findViewById(R.id.tv_company_duty);
//        TextView tv_duty = dialog.findViewById(R.id.tv_duty);
//        ImageView iv_close = dialog.findViewById(R.id.iv_close);
//        iv_head.setAvatarUrl("");
//        tv_name.setHint(name);
//        if (level > 1) {
//            iv_level.setImageResource(ImageUtils.getLevelRes(level));
//        } else {
//            iv_level.setVisibility(View.GONE);
//        }
//        tv_company_duty.setText(company + " " + duty);
//        tv_duty.setText(duty);
//        dialog.show();
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showHisInfoDialog2(final Context context, String headUrl, String name, int level, String company
//            , String duty, String introduce, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_showhisinfo2);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        AvatarView iv_head = dialog.findViewById(R.id.iv_head);
//        TextView tv_name = dialog.findViewById(R.id.tv_name);
//        ImageView iv_level = dialog.findViewById(R.id.iv_level);
//        TextView tv_company_duty = dialog.findViewById(R.id.tv_company_duty);
//        TextView tv_jianjie = dialog.findViewById(R.id.tv_jianjie);
//        TextView tv_tohispage = dialog.findViewById(R.id.tv_tohispage);
//        iv_head.setAvatarUrl("");
//        tv_name.setHint(name);
//        if (level > 1) {
//            iv_level.setImageResource(ImageUtils.getLevelRes(level));
//        } else {
//            iv_level.setVisibility(View.GONE);
//        }
//        tv_company_duty.setText(company + " " + duty);
//        tv_jianjie.setText(introduce);
//        dialog.show();
//        tv_tohispage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.determineDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showShareReportDialog(Context context, final IShareReport dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
//        dialog.setContentView(R.layout.dialog_share_report);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_share = dialog.findViewById(R.id.tv_share);
//        TextView tv_report = dialog.findViewById(R.id.tv_report);
//        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        tv_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.toShare(view, dialog);
//            }
//        });
//        tv_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.toReport(view, dialog);
//            }
//        });
//    }
//
//    public static void showReportDialog(final Context context, String sbName, final IReport dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.fullScreendialog);
//        dialog.setContentView(R.layout.dialog_report);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_report_sb = dialog.findViewById(R.id.tv_report_sb);
//        TextView tv_idInfoNotFit = dialog.findViewById(R.id.tv_idInfoNotFit);
//        TextView tv_botherMe = dialog.findViewById(R.id.tv_botherMe);
//        TextView tv_notFitQHB = dialog.findViewById(R.id.tv_notFitQHB);
//        tv_report_sb.setText(sbName);
//        dialog.show();
//        tv_idInfoNotFit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.idInfoNotFit(view, dialog);
//            }
//        });
//        tv_botherMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.botherMe(view, dialog);
//            }
//        });
//        tv_notFitQHB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.notFitQHB(view, dialog);
//            }
//        });
//    }
//
//    public static void showOtherMianReportDialog(Context context, final IToHisMain dialogInterface) {
//        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
//        dialog.setContentView(R.layout.dialog_othermain_report);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        TextView tv_report = dialog.findViewById(R.id.tv_report);
//        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.cancelDialog(view, dialog);
//            }
//        });
//        tv_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogInterface.determineDialog(view, dialog);
//            }
//        });
//    }
//
//    public static void showCancleDialog(final Activity content) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(content);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setTitle("");
//        builder.setMessage("你确定要取消吗");//提示内容
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                content.finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public static void showIdPop(final Activity context, String title) {
//        DialogUtil.showIdIdentifyDialog(context, title, new IIdentitifyID() {
//            @Override
//            public void cancelDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//
//            @Override
//            public void toBeVip(View v, Dialog d) {
//                d.dismiss();
//                UiHelper.startToBeVipActivity(context);
//            }
//
//            @Override
//            public void toIndentifyID(View v, Dialog d) {
//                d.dismiss();
//                UiHelper.startIdentitifyIdActivity(context);
//            }
//        });
//    }
    public static void showTips(Context context, String tips) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_nocut_tips);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btn_know = dialog.findViewById(R.id.btn_know);
        TextView tv_tips = dialog.findViewById(R.id.tv_tips);
        btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_tips.setText(tips);
        dialog.show();
    }

    public static void showIdIdentifyDialog(final Context context, String privatekey, final IOutPrivatekey dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_outprivatekey);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        ImageView iv_cancle = dialog.findViewById(R.id.iv_cancle);
        RoundTextView tv_private_key = dialog.findViewById(R.id.tv_private_key);
        TextView btn_copy = dialog.findViewById(R.id.btn_copy);
        if (!StringUtils.isEmpty(privatekey)) {
            tv_private_key.setText(privatekey);
        }
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.cancelDialog(view, dialog);
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.copy(view, dialog);
            }
        });
    }

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

}
