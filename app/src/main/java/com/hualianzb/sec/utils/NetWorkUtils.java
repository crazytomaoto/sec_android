package com.hualianzb.sec.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hualianzb.sec.R;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class NetWorkUtils {
    private static SSLContext mSSLContext = null;
    private static final String TAG = "NetWorkUtils";
    public static int DEFAULT_CACHE_EXPIRY_TIME = 15 * 1000;
    private static int cishu=0;
    /**
     * http get请求
     *
     * @param params   请求参数 get请求使用 addQueryStringParameter方法添加参数
     * @param callback 回调对象
     */
    public static Callback.Cancelable getHttpRequest(Context context, RequestParams params, final HttpCallback callback) {
        return sendHttpRequest(context, HttpMethod.GET, params, callback);
    }
    /**
     * http post请求
     *
     * @param params   请求参数 post请求使用 addBodyParameter方法添加参数
     * @param callback 回调对象
     */
    public static Callback.Cancelable postHttpRequest(Context context , RequestParams params, final HttpCallback callback) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null){
                return sendHttpRequest(context, HttpMethod.POST, params, callback);
            }else {
                ToastUtil.show(context, R.string.net_work_err);
            }
        }
        return null;

    }
    public static Callback.Cancelable sendHttpRequest(final Context context  , HttpMethod method, RequestParams params, final HttpCallback callback) {

        if (params == null) {
            params = new RequestParams();
        }
        params.setCacheMaxAge(1000 * 0); //为请求添加缓存时间
        params.setConnectTimeout(DEFAULT_CACHE_EXPIRY_TIME);
        return x.http().request(method, params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException msg) {
                callback.onfailed();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e(TAG, "onError: "+ arg0.toString());
                if(arg0.toString().contains("SocketTimeoutException")){
                    callback.onSuccess("{'code':'fail','msg':'Time Out!'}");
                }else{
                    callback.onfailed();
                }

            }

            @Override
            public void onSuccess(String result) {
                if (result == null) {
                    return;
                }
                if (result instanceof String) {
                }
                callback.onSuccess(result);
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 获取Https的证书
     *
     * @param context 上下文
     * @return SSL的上下文对象
     */
    private static SSLContext getSSLContext(Context context) {
        CertificateFactory certificateFactory = null;
        InputStream inputStream = null;
        Certificate cer = null;
        KeyStore keystore = null;
        TrustManagerFactory trustManagerFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            inputStream = context.getAssets().open("wallet.cer");//这里导入SSL证书文件
            try {
                cer = certificateFactory.generateCertificate(inputStream);
            } finally {
                inputStream.close();
            }
            //创建一个证书库，并将证书导入证书库
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null); //双向验证时使用
            keystore.setCertificateEntry("trust", cer);
            // 实例化信任库
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return mSSLContext;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static abstract class HttpCallback {
        public abstract void onSuccess(String result);
        public abstract void onfailed();
        public abstract void onZhongXinHuoQU();
    }




}


