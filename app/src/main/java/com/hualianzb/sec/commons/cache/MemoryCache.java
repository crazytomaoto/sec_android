package com.hualianzb.sec.commons.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by jerry on 15/9/11.
 */
public class MemoryCache {

    // 4.0 LruCache
    private static LruCache<String, Bitmap> lruCache;
    private static MemoryCache memoryCache;

    private MemoryCache() {

    }

    public static MemoryCache getInstance() {
        if (memoryCache == null) {
            initLruCache();
            memoryCache = new MemoryCache();
        }
        return memoryCache;
    }

    private static void initLruCache() {
        int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // return bitmap.getByteCount();
                return bitmap.getWidth() * bitmap.getHeight();

                // 二次采样
            }
        };
    }

    /**
     * 保存图片到内存缓存
     *
     * @param imageUrl
     * @param bitmap
     */
    public void put(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl, bitmap);
    }

    /**
     * 获取内存中图片地址对应的图片
     *
     * @param imageUrl
     * @return
     */
    public Bitmap get(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    /**
     * 清空内存缓存中的内容
     */
    public void clear() {
        lruCache.evictAll();
    }
}
