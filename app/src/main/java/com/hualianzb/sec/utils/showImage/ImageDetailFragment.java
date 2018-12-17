package com.hualianzb.sec.utils.showImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hualianzb.sec.application.SECApplication;
import com.hysd.android.platform_huanuo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private String text;
    private ImageView mImageView;
    private TextView tv_text;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private Bitmap bImage;

    public static ImageDetailFragment newInstance(String imageUrl, String text) {
        Log.d("newInstance", "创建fragment");
        ImageDetailFragment f = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putString("text", text);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        text = getArguments() != null ? getArguments().getString("text") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
//        mImageView = v.findViewById(R.id.image);
//        tv_text = v.findViewById(R.id.tv_text);
//        mAttacher = new PhotoViewAttacher(mImageView);
//
//        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
//
//            @Override
//            public void onPhotoTap(View arg0, float arg1, float arg2) {
//                if (context != null) {
//                    ((Activity) context).finish();
////					getActivity().overridePendingTransition(R.anim.photo_activity_out, 0);
//                }
//            }
//        });

//        progressBar = (ProgressBar) v.findViewById(R.id.loading);
//        return v;
        return null;
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public Bitmap getbImage() {
        return bImage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("onActivityCreated", "onActivityCreated展示图片");

        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, SECApplication.options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        Toast.makeText(SECApplication.getContext(), message, Toast.LENGTH_SHORT).show();

                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        Toast.makeText(SECApplication.getContext(), message, Toast.LENGTH_SHORT).show();

                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        Toast.makeText(SECApplication.getContext(), message, Toast.LENGTH_SHORT).show();

                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        Toast.makeText(SECApplication.getContext(), message, Toast.LENGTH_SHORT).show();

                        break;
                    case UNKNOWN:
                        /**
                         * 本地图片
                         */
                        Log.d("onActivityCreated", "onActivityCreated本地图片");
                        Bitmap bitmap = getBitmapFromFile(mImageUrl);
                        int degree = BitmpUtil.getBitmapDegree(mImageUrl);
                        mImageView.setImageBitmap(BitmpUtil.rotateBitmapByDegree(bitmap, degree));
                        if (StringUtils.isNotEmpty(text)) {
                            tv_text.setText(text);
                        } else {
                            tv_text.setVisibility(View.GONE);
                        }
                        mAttacher.update();
                        break;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                bImage = loadedImage;
//                bImage = loadedImage.copy(loadedImage.getConfig(), true);
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("onActivityCreated", "onstop");
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImageView != null && mImageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

            mImageView.setImageDrawable(null);

            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                Log.d("onActivityCreated", "onstop回收");
//                oldBitmap.recycle();
                oldBitmap = null;

            }

        }
    }

    /**
     * 获得本地图片的bitmap对象
     *
     * @param localurl
     * @return
     */
    private Bitmap getBitmapFromFile(String localurl) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(localurl, opts);
        return getBitmapFromFile(new File(localurl), opts.outWidth, opts.outHeight);
    }


    public Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (context != null) {
            WindowManager wm = ((Activity) context).getWindowManager();
            if (null != dst && dst.exists()) {
                BitmapFactory.Options opts = null;
                try {
                    if (width > 0 && height > 0) {
                        opts = new BitmapFactory.Options();            //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                        opts.inJustDecodeBounds = true;
//                  BitmapFactory.decodeFile(dst.getPath(), opts);
                        double ww = wm.getDefaultDisplay().getWidth();
                        double hh = wm.getDefaultDisplay().getHeight();
                        int be = 1;//be=1表示不缩放
                        if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
                            be = (int) (width / ww) * 2;
                        } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
                            be = (int) (height / hh) * 2;
                        }
                        if (be <= 1)
                            be = 1;
                        opts.inSampleSize = be;

                        //这里一定要将其设置回false，因为之前我们将其设置成了true
                        opts.inJustDecodeBounds = false;
                        opts.inInputShareable = true;
                        opts.inPurgeable = true;
                    }
                    return BitmapFactory.decodeFile(dst.getPath(), opts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
