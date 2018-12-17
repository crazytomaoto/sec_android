package com.hualianzb.sec.application;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.hualianzb.sec.BuildConfig;
import com.hualianzb.sec.commons.cache.FileCache;
import com.hualianzb.sec.commons.cache.MemoryCache;
import com.hualianzb.sec.commons.constants.Constant;
import com.hualianzb.sec.commons.constants.RequestHost;
import com.hualianzb.sec.logics.AsyncImageLoader;
import com.hualianzb.sec.logics.IRequest;
import com.hualianzb.sec.logics.RequestMain;
import com.hualianzb.sec.models.PropertyBean;
import com.hualianzb.sec.models.RememberEth;
import com.hysd.android.platform_huanuo.base.config.PlatformConfig;
import com.hysd.android.platform_huanuo.base.manager.BaseApplication;
import com.hysd.android.platform_huanuo.base.manager.LogicFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @auther:Created by wangtianyun on 2018/4/2.
 * des:
 */

public class SECApplication extends BaseApplication {
    private static SECApplication instance = null;
    public static List<Activity> activityList = new LinkedList<>();
    private static Context mContext;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static String imagePath;//图片保存的路径
    private AsyncImageLoader asyncImageLoader;
    public static DisplayImageOptions options;//默认头像
    public static DisplayImageOptions optionsl;//默认加载图片
    public static DisplayImageOptions optionsHead;//默认头像
    public static DisplayImageOptions optionCompany;//默认公司照片
    public Map<String, RememberEth> map = new HashMap<>();
    public Map mapKind = new HashMap();
    public List<PropertyBean> listToken = new ArrayList<>();
//    public static SpeechSynthesizer speech;//讯飞

    //    public BMapManager mBMapManager = null;
//    private MediaPlayer mMediaPlayer;
    public static synchronized Context getContext() {
        return mContext;
    }

    public SECApplication() {
    }

    @Override
    public void onCreate() {
        try {
//            MultiDex.install(this);//解决多个jar包的问题
            //bug日志打印上传
            mContext = getApplicationContext();
//            imagePath = "/ningmengdisong/cache/images";
//            initImageLoader(this);
//            initImageOptionsl();//初始化默认图片
//            initImageOptions();
//            initImageHead();
//            initAsyncImageLoader();//自定义异步加载图片
//            initImageCompany();
            initAllLogics();
//            SDKInitializer.initialize(this);
//            initXunFei();
//            initKind();
            CrashReport.initCrashReport(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        super.onCreate();
        initKind02();
//        对xUtils进行初始化
        x.Ext.init(this);
        //是否是开发、调试模式
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启debug会影响性能
        EventBus.getDefault().unregister(this);
    }

    public void initKind() {
        boolean isFirstLogin = PlatformConfig.getBoolean(Constant.SpConstant.FIRST);
        if (isFirstLogin == false) {
            mapKind = new HashMap<>();
            mapKind.put(Constant.SpConstant.CEC, RequestHost.cecToken);
            mapKind.put(Constant.SpConstant.ETH, RequestHost.ethToken);
            PlatformConfig.putMap(Constant.SpConstant.KINDTOKEN, mapKind);
        }
    }
    public void initKind02() {
        boolean isFirstLogin = PlatformConfig.getBoolean(Constant.SpConstant.FIRST);
        if (isFirstLogin == false) {
            PropertyBean bean1 = new PropertyBean();
            PropertyBean bean2 = new PropertyBean();
            PropertyBean bean3 = new PropertyBean();
            PropertyBean bean4 = new PropertyBean();

            bean2.setName(Constant.SpConstant.ETH);
            bean2.setAllName("Ethereum");
            bean2.setTokenAddress(RequestHost.ethToken);
            bean2.setChecked(true);

            bean1.setName(Constant.SpConstant.CEC);
            bean1.setAllName("CEC");
            bean1.setChecked(true);
            bean1.setTokenAddress(RequestHost.cecToken);

            bean3.setName(Constant.SpConstant.SEC);
            bean3.setAllName("Social Ecommerce Chain");
            bean3.setTokenAddress(RequestHost.cecToken);
            bean3.setChecked(false);

            bean4.setName(Constant.SpConstant.INT);
            bean4.setAllName("INT chain");
            bean4.setTokenAddress(RequestHost.cecToken);
            bean4.setChecked(false);

            listToken.add(bean2);
            listToken.add(bean1);
            listToken.add(bean3);
            listToken.add(bean4);

            PlatformConfig.putList(Constant.SpConstant.KINDTOKEN, listToken);
        }
    }

    public ArrayList getTokenKindList() {
        return (ArrayList) PlatformConfig.getList(getContext(), Constant.SpConstant.KINDTOKEN);
    }

    public Map<String, String> getTokenKind() {
        return PlatformConfig.getMap(Constant.SpConstant.KINDTOKEN);
    }


    public static SECApplication getInstance() {
        if (instance == null) {
            synchronized (SECApplication.class) {
                if (instance == null) {
                    instance = new SECApplication();
                }
            }
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public void finishActivity(Activity activity) {

        if (activity != null) {
            this.activityList.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityclass(Class<?> cls) {
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(cls)) {
                    this.activityList.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }

    }

    // 清除Activity容器
    public void clearActivityList() {
        activityList.clear();
    }
    // 遍历所有Activity并finish

    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * ImageLoader实例对象的配置
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }
    /**
     * DisplayImageOptions实例对象的配置
     * 默认头像
     */
    /**
     * DisplayImageOptions实例对象的配置
     * 默认加载图片
     */
    private void initImageOptionsl() {
        optionsl = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.default_bg)         //没有图片资源时的默认图片
//                .showImageOnFail(R.drawable.default_bg)                  //加载失败时的图片
                .cacheInMemory(false)                              //启用内存缓存
                .cacheOnDisk(true)                                //启用外存缓存
                .considerExifParams(true)                         //启用EXIF和JPEG图像格式
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
    }

    private void initImageOptions() {
        options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.logo)         //没有图片资源时的默认图片
//                .showImageOnFail(R.drawable.logo)              //加载失败时的图片
                .cacheInMemory(false)                              //启用内存缓存
                .cacheOnDisk(true)                                //启用外存缓存
                .considerExifParams(true)                         //启用EXIF和JPEG图像格式
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
    }

    private void initImageHead() {
        optionsHead = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.icon_client_touxiang)         //没有图片资源时的默认图片
//                .showImageOnFail(R.drawable.icon_client_touxiang)              //加载失败时的图片
                .cacheInMemory(true)                              //启用内存缓存
                .cacheOnDisk(true)                                //启用外存缓存
                .considerExifParams(true)                         //启用EXIF和JPEG图像格式
                .build();
    }

    private void initImageCompany() {
        optionCompany = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.corporateinformation_logo_default)         //没有图片资源时的默认图片
//                .showImageOnFail(R.drawable.corporateinformation_logo_default)              //加载失败时的图片
                .cacheInMemory(false)                              //启用内存缓存
                .cacheOnDisk(true)                                //启用外存缓存
                .considerExifParams(true)                         //启用EXIF和JPEG图像格式
                .build();
    }

    // 自定义异步ImageLoader
    private void initAsyncImageLoader() {
        MemoryCache memoryCache = MemoryCache.getInstance();// 内存缓存
        FileCache fileCache = new FileCache(getApplicationContext(), null);// 文件缓存
        asyncImageLoader = new AsyncImageLoader(getApplicationContext(), memoryCache, fileCache);
    }

    /**
     * 初始化全局Logic
     */
    private void initAllLogics() {
        LogicFactory.registerLogic(IRequest.class, new RequestMain(getApplicationContext()));
    }

    public void set(boolean isEnglish) {

        Configuration configuration = getContext().getResources().getConfiguration();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        if (isEnglish) {
            //设置英文
            configuration.locale = Locale.ENGLISH;
        } else {
            //设置中文
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }
        //更新配置
        getContext().getResources().updateConfiguration(configuration, displayMetrics);
    }

    public void setMap(RememberEth rememberEth) {
        map.put(rememberEth.getAddress(), rememberEth);
    }

}