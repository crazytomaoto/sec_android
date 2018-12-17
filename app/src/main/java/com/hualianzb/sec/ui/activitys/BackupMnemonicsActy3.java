package com.hualianzb.sec.ui.activitys;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualianzb.sec.R;
import com.hualianzb.sec.application.SECApplication;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.models.MnemonicsBean;
import com.hualianzb.sec.models.RememberEth;
import com.hualianzb.sec.ui.basic.BasicActivity;
import com.hualianzb.sec.utils.StateBarUtil;
import com.hualianzb.sec.utils.ToastUtil;
import com.hualianzb.sec.utils.UiHelper;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupMnemonicsActy3 extends BasicActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_my_mns)//上面
            LinearLayout llMyMns;
    @BindView(R.id.ll_old_mns)//下面
            LinearLayout llOldMns;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private String myMnemonics;
    private List<String> list_my_mns;
    private List<MnemonicsBean> listMNs;
    StateBarUtil stateBarUtil;

    @Override

    protected void getIntentForBundle() {
        super.getIntentForBundle();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            myMnemonics = bundle.getString("myMnemonics");
        }
    }

    @Override
    protected void getIntentForSavedInstanceState(Bundle savedInstanceState) {
        super.getIntentForSavedInstanceState(savedInstanceState);
        myMnemonics = savedInstanceState.getString("myMnemonics");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankup_mn3);
        ButterKnife.bind(this);
        SECApplication.getInstance().addActivity(this);
        stateBarUtil = new StateBarUtil(this);
        stateBarUtil.changeStatusBarTextColor(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        tvTitle.setText("备份助记词");
        list_my_mns = new ArrayList<>();
        listMNs = new ArrayList<>();
        String myString[] = myMnemonics.split(" ");
        for (int i = 0; i < myString.length; i++) {
            MnemonicsBean bean = new MnemonicsBean();
            bean.setMnemonicsItem(myString[i]);
            bean.setSelected(false);
            listMNs.add(bean);
        }
        shulf(listMNs);
        initAutoLL(llOldMns);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if (list_my_mns == null || list_my_mns.size() == 0 || list_my_mns.size() < 12) {
                    ToastUtil.show(this, "助记词未填写完整");
                } else {
                    String now = getHYString();
                    if (now.equals(myMnemonics)) {
                        showDg();
                    } else {
                        ToastUtil.show(this, "助记顺序不正确");
                    }
                }
                break;
        }

    }


    //选中的
    private void initAutoLLMy(final LinearLayout ll_parent) {
        if (list_my_mns.size() > 0) {
            ll_parent.setVisibility(View.VISIBLE);
        }
        ll_parent.removeAllViews();
//        tv_number.setText(100 - list.size() + "");
//        每一行的布局，初始化第一行布局
        LinearLayout rowLL = new LinearLayout(this);
        LinearLayout.LayoutParams rowLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        float rowMargin = dipToPx(12);
        rowLP.setMargins(0, (int) rowMargin, 0, 0);
        rowLL.setLayoutParams(rowLP);
        boolean isNewLayout = false;
        float maxWidth = getScreenWidth() - dipToPx(48);
//        剩下的宽度
        float elseWidth = maxWidth;
        LinearLayout.LayoutParams textViewLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLP.setMargins((int) dipToPx(8), 0, 0, 0);
        for (int i = 0; i < list_my_mns.size(); i++) {
//            若当前为新起的一行，先添加旧的那行
//            然后重新创建布局对象，设置参数，将isNewLayout判断重置为false
            if (isNewLayout) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                isNewLayout = false;
            }
//            计算是否需要换行
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.layout_selected_mns, null);
            textView.setText(list_my_mns.get(i).toString());
            textView.measure(0, 0);
//            若是一整行都放不下这个文本框，添加旧的那行，新起一行添加这个文本框
            if (maxWidth < textView.getMeasuredWidth()) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                rowLL.addView(textView);
                isNewLayout = true;
                continue;
            }
//            若是剩下的宽度小于文本框的宽度（放不下了）
//            添加旧的那行，新起一行，但是i要-1，因为当前的文本框还未添加
            if (elseWidth < textView.getMeasuredWidth()) {
                isNewLayout = true;
                i--;
//                重置剩余宽度
                elseWidth = maxWidth;
                continue;
            } else {
//                剩余宽度减去文本框的宽度+间隔=新的剩余宽度
                elseWidth -= textView.getMeasuredWidth() + dipToPx(8);
                if (rowLL.getChildCount() == 0) {
                    rowLL.addView(textView);
                } else {
                    textView.setLayoutParams(textViewLP);
                    rowLL.addView(textView);
                }
            }
//            textView.setText(list_my_mns.get(i).toString());
            final int finalI = i;
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    for (MnemonicsBean bean : listMNs) {
                        if (bean.getMnemonicsItem().equals(list_my_mns.get(finalI))) {
                            bean.setSelected(false);
                            break;
                        }
                    }
                    list_my_mns.remove(finalI);
                    shulf(listMNs);
                    if (null == list_my_mns || list_my_mns.size() == 0) {
                        llMyMns.removeAllViews();
                        llMyMns.setVisibility(View.GONE);
                    } else {
                        llMyMns.setVisibility(View.VISIBLE);
                        initAutoLLMy(llMyMns);
                    }
                    initAutoLL(llOldMns);
                    return true;
                }
            });
        }
//        添加最后一行，但要防止重复添加
        ll_parent.removeView(rowLL);
        ll_parent.addView(rowLL);
    }

    //下面的
    private void initAutoLL(final LinearLayout ll_parent) {
        ll_parent.removeAllViews();
//        tv_number.setText(100 - list.size() + "");
//        每一行的布局，初始化第一行布局
        LinearLayout rowLL = new LinearLayout(this);
        LinearLayout.LayoutParams rowLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        float rowMargin = dipToPx(12);
        rowLP.setMargins(0, (int) rowMargin, 0, 0);
        rowLL.setLayoutParams(rowLP);
        boolean isNewLayout = false;
        float maxWidth = getScreenWidth() - dipToPx(24);
//        剩下的宽度
        float elseWidth = maxWidth;
        LinearLayout.LayoutParams textViewLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLP.setMargins((int) dipToPx(8), 0, 0, 0);
        for (int i = 0; i < listMNs.size(); i++) {
//            若当前为新起的一行，先添加旧的那行
//            然后重新创建布局对象，设置参数，将isNewLayout判断重置为false
            if (isNewLayout) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                isNewLayout = false;
            }
//            计算是否需要换行
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.layout_text_item, null);
            textView.setText(listMNs.get(i).getMnemonicsItem().toString().trim());
            textView.measure(0, 0);
//            若是一整行都放不下这个文本框，添加旧的那行，新起一行添加这个文本框
            if (maxWidth < textView.getMeasuredWidth()) {
                ll_parent.addView(rowLL);
                rowLL = new LinearLayout(this);
                rowLL.setLayoutParams(rowLP);
                rowLL.addView(textView);
                isNewLayout = true;
                continue;
            }
//            若是剩下的宽度小于文本框的宽度（放不下了）
//            添加旧的那行，新起一行，但是i要-1，因为当前的文本框还未添加
            if (elseWidth < textView.getMeasuredWidth()) {
                isNewLayout = true;
                i--;
//                重置剩余宽度
                elseWidth = maxWidth;
                continue;
            } else {
//                剩余宽度减去文本框的宽度+间隔=新的剩余宽度
                elseWidth -= textView.getMeasuredWidth() + dipToPx(8);
                if (rowLL.getChildCount() == 0) {
                    rowLL.addView(textView);
                } else {
                    textView.setLayoutParams(textViewLP);
                    rowLL.addView(textView);
                }
            }
            if (listMNs.get(i).isSelected()) {
                textView.setBackground(getResources().getDrawable(R.drawable.shape_yellow_select_10));
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setClickable(false);
            } else {
                textView.setBackground(getResources().getDrawable(R.drawable.shape_blue_default_10));
                textView.setTextColor(getResources().getColor(R.color.mn_default));
                textView.setClickable(true);
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!listMNs.get(finalI).isSelected()) {
                        list_my_mns.add(listMNs.get(finalI).getMnemonicsItem().toString().trim());
                        listMNs.get(finalI).setSelected(true);
                        shulf(listMNs);
                        initAutoLL(ll_parent);
                        initAutoLLMy(llMyMns);
                    }
                }
            });
        }
//        添加最后一行，但要防止重复添加
        ll_parent.removeView(rowLL);
        ll_parent.addView(rowLL);
    }

    private String getHYString() {

        String nowString = "";
        for (int i = 0; i < list_my_mns.size(); i++) {
            if (StringUtils.isNotEmpty(nowString)) {
                nowString = nowString + " " + list_my_mns.get(i);
            } else {
                nowString = list_my_mns.get(i);
            }
        }
//        nowString = nowString;
        return nowString;
    }

    //    dp转px
    private float dipToPx(int dipValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                this.getResources().getDisplayMetrics());
    }

    //  获得屏幕宽度
    private float getScreenWidth() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }

    private List<MnemonicsBean> shulf(List<MnemonicsBean> list) {
        Collections.shuffle(list);
        return list;
    }

    private void showDg() {
        //弹出框
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_clear_mn);
        TextView ok = window.findViewById(R.id.tv_ok);
        TextView cancel = window.findViewById(R.id.tv_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Map<String, RememberEth> map = PlatformConfig.getMap(Constant.SpConstant.WALLET);
                for (RememberEth bean : map.values()) {
                    if (StringUtils.isNotEmpty(bean.getMnemonics())&&bean.getMnemonics().equals(myMnemonics)) {
                        bean.setMnemonics("");
                        bean.setBackup(true);//删除了就是已经备份了
                        map.put(bean.getAddress(), bean);
                        PlatformConfig.putMap(Constant.SpConstant.WALLET, map);
                        UiHelper.startHomaPageAc(BackupMnemonicsActy3.this, bean.getAddress());
                        break;
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}
