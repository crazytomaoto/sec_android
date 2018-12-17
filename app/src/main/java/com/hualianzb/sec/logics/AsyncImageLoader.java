package com.hualianzb.sec.logics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.hualianzb.sec.commons.cache.FileCache;
import com.hualianzb.sec.commons.cache.MemoryCache;
import com.hualianzb.sec.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wty on 2017/8/22.
 */
public class AsyncImageLoader {

    private Context mContext;

    // 内存缓存
    private MemoryCache memoryCache;

    // 磁盘缓存
    private FileCache fileCache;

    // 线程池
    private ExecutorService executorService;

    // 创建弱引用的保存已经显示对应图片控件的集合
    // HashMap是弱引用对象
    private Map<ImageView, String> mImageViews
            = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    public AsyncImageLoader(Context mContext, MemoryCache memoryCache, FileCache fileCache) {
        this.mContext = mContext;
        this.memoryCache = memoryCache;
        this.fileCache = fileCache;
        // 创建线程池，创建固定大小的线程池
        executorService = Executors.newFixedThreadPool(5);
    }


    /**
     * 加载图片
     */
    public Bitmap loadImage(ImageView target, String imageUrl) {

        // 保存加载关系
        mImageViews.put(target, imageUrl);

        Bitmap bitmap = memoryCache.get(imageUrl);

        // 内存中不存在此图片
        if (bitmap == null) {
            // 处理磁盘缓存和从网络下载
            doneLoadImage(imageUrl, target);
        }

        return bitmap;
    }

    // 任务队列
    private List<LoadImageTask> mTaskQueue
            = Collections.synchronizedList(new ArrayList<LoadImageTask>());

    /**
     * 处理图片加载
     */
    private void doneLoadImage(String imageUrl, ImageView target) {
        // 判断任务队列中是否存在正在加载对应路径图片的任务
        if (isTaskRunning(imageUrl)) {
            return;
        }

        // 开启工作线程

        // 从磁盘中获取
        // 网络下载

        // 加载图片任务
        LoadImageTask loadImageTask = new LoadImageTask(imageUrl, target);

        // 保存任务到任务队列中
        mTaskQueue.add(loadImageTask);

        // 把线程加入线程池中，执行任务
        executorService.execute(loadImageTask);
    }


    /**
     * 加载图片的工作线程
     * 处理磁盘读取和网络下载
     */
    class LoadImageTask implements Runnable {
        private String imageUrl;
        private ImageView target;

        public LoadImageTask(String imageUrl, ImageView targe) {
            this.imageUrl = imageUrl;
            this.target = targe;
        }

        @Override
        public void run() {
            // 判断图片是否已经显示
            if (isImageViewUsed(imageUrl, target)) {
                // 移除当前任务
                mTaskQueue.remove(this);

                return;
            }

            // 加载磁盘文件或者从网络获取
            final Bitmap bitmap = getBitmapWithUrl(imageUrl);

            if (bitmap != null) {
                memoryCache.put(imageUrl, bitmap);

                //target.setImageBitmap(bitmap);

                // Activity.runOnUiThread(Runnable)
                // View.post(Runnable)
                // View.PostDelay(Runnable)

                // GreenTreeApplication
                //mAdapter = new HotelSelectAdapter
                //       <HotelData.ResponseData.HotelItem>(getApplicationContext(), hotelList);
                //Activity activity = (Activity)target.getContext();

                // 在子线程中更新UI
                //activity.runOnUiThread(new Runnable()

                target.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isImageViewUsed(imageUrl, target)) {
                            return;
                        }

                        target.setImageBitmap(bitmap);
                    }
                });
            }

            // 移除当前任务
            mTaskQueue.remove(this);
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public ImageView getTarget() {
            return target;
        }
    }

    /**
     * 获取Bitmap对象
     */
    private Bitmap getBitmapWithUrl(String imageUrl) {
        //imageUrl - http://a1.greentree.com/123.jpg

        // 从磁盘缓存读取
        //String SERVER_URL = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

        String url = new String(
                Base64.encode(imageUrl.getBytes(), Base64.NO_WRAP));

        File saveFile = fileCache.getCacheFile(url);

        if (saveFile != null && saveFile.exists()) {
//            return BitmapFactory.decodeFile(saveFile.getPath());
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getPath(), opts);
            return getBitmapFromFile(saveFile, opts.outWidth, opts.outHeight);
        }

        // 从网络读取
        return ImageUtils.getBitmapFromUrl(saveFile, imageUrl);
    }

    /**
     * 判断队列中是否存在加载任务
     */
    private boolean isTaskRunning(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return true;
        }

        if (mTaskQueue.isEmpty()) {
            return false;
        }

        synchronized (mTaskQueue) {
            for (LoadImageTask task : mTaskQueue) {
                if (imageUrl.equals(task.getImageUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断图片是否使用
     */
    private boolean isImageViewUsed(String imageUrl, ImageView target) {
        if (TextUtils.isEmpty(imageUrl)) {
            return true;
        }

        String url = mImageViews.get(target);

        if (url == null || !url.equals(imageUrl)) {
            return true;
        }

        return false;
    }

    /**
     * 清空缓存
     */
    public void clear() {
        memoryCache.clear();
        memoryCache = null;

        fileCache = null;

        mTaskQueue.clear();
        mTaskQueue = null;

        mImageViews.clear();
        mImageViews = null;
    }


    public Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();            //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);            //这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
