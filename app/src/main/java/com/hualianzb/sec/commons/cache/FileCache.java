package com.hualianzb.sec.commons.cache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件缓存
 */
public class FileCache {

    private File mCacheDir;

    public FileCache(Context context, File cacheDir) {
        if (cacheDir == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mCacheDir = Environment.getExternalStorageDirectory();
            } else {
                mCacheDir = context.getCacheDir();
            }

            mCacheDir = new File(mCacheDir.getPath() + "/qinker/images");
        } else {
            mCacheDir = cacheDir;
        }

        mCacheDir.mkdirs();

//        try
//        {
//            int appVersion = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 1).versionCode;
//
//            diskLruCache = DiskLruCache.open(mCacheDir,appVersion,500,300*300);
//        }
//        catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

//        if (!mCacheDir.exists())
////        {
//        mCacheDir.mkdirs();
//        }
    }

    public File getCacheFile(String imageFileName) {
        return new File(mCacheDir, imageFileName);
//        try
//        {
//            diskLruCache.get(imageFileName).getInputStream(0);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }

}
