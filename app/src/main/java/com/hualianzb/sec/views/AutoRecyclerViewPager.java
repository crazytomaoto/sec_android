package com.hualianzb.sec.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.lang.ref.WeakReference;

/**
 * Created by wangtianyun on 2018/10/22.
 */

public class AutoRecyclerViewPager extends RecyclerViewPager {

    public static final int DEFAULT_INTERVAL = 5 * 1000;

    private boolean isAutoScroll = false;
    private boolean isStopByTouch = false;
    private Handler handler;
    public static final int SCROLL_WHAT = 0;

    public AutoRecyclerViewPager(Context context) {
        super(context);
        init();
    }

    public AutoRecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
    }

    public void startAutoScroll() {
        startAutoScroll(DEFAULT_INTERVAL);
    }

    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        stopAutoScroll();
//        if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
//            isStopByTouch = false;
//            stopAutoScroll();
//        } else if (ev.getAction() == MotionEvent.ACTION_UP && !isStopByTouch) {
//            startAutoScroll();
//        } else if (ev.getAction() == MotionEvent.ACTION_CANCEL && !isStopByTouch) {
//            startAutoScroll();
//        }

        return super.dispatchTouchEvent(ev);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<AutoRecyclerViewPager> autoRecyclerViewPager;

        public MyHandler(AutoRecyclerViewPager autoRecyclerViewPager) {
            this.autoRecyclerViewPager = new WeakReference<AutoRecyclerViewPager>(autoRecyclerViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCROLL_WHAT:
                    AutoRecyclerViewPager pager = this.autoRecyclerViewPager.get();
                    if (pager != null) {
                        pager.sendScrollMessage(DEFAULT_INTERVAL);
                        pager.scrollOnce();
                    }
                default:
                    break;
            }
        }
    }

    public void scrollOnce() {
        int totalCount;
        AutoRecyclerViewPager mRecyclerView = this;
        RecyclerView.Adapter myRecyclerAdapter = mRecyclerView.getAdapter();
        int currentItem = mRecyclerView.getCurrentPosition();
        if (myRecyclerAdapter == null || (totalCount = myRecyclerAdapter.getItemCount()) <= 1) {
            return;
        }

        int nextItem = ++currentItem;
        if (nextItem < 0) {
            mRecyclerView.smoothScrollToPosition(totalCount - 1);
        } else if (nextItem == totalCount) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(nextItem);
        }
    }


}
