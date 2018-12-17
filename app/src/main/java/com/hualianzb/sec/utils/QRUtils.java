package com.hualianzb.sec.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QRUtils {
    public QRUtils() {
    }

    public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
        try {
            if (content != null && !"".equals(content)) {
                Map<EncodeHintType, Object> hints = new HashMap();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                BitMatrix bitMatrix = (new QRCodeWriter()).encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
                int[] pixels = new int[widthPix * heightPix];

                for (int y = 0; y < heightPix; ++y) {
                    for (int x = 0; x < widthPix; ++x) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * widthPix + x] = -16777216;
                        } else {
                            pixels[y * widthPix + x] = -1;
                        }
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
                if (logoBm != null) {
                    bitmap = addLogo(bitmap, logoBm);
                }

                return bitmap;
            } else {
                return null;
            }
        } catch (WriterException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        } else if (logo == null) {
            return src;
        } else {
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            int logoWidth = logo.getWidth();
            int logoHeight = logo.getHeight();
            if (srcWidth != 0 && srcHeight != 0) {
                if (logoWidth != 0 && logoHeight != 0) {
                    float scaleFactor = (float) srcWidth * 1.0F / 5.0F / (float) logoWidth;
                    Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Config.ARGB_8888);

                    try {
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawBitmap(src, 0.0F, 0.0F, (Paint) null);
                        canvas.scale(scaleFactor, scaleFactor, (float) (srcWidth / 2), (float) (srcHeight / 2));
                        canvas.drawBitmap(logo, (float) ((srcWidth - logoWidth) / 2), (float) ((srcHeight - logoHeight) / 2), (Paint) null);
                        canvas.save();
                        canvas.restore();
                    } catch (Exception var9) {
                        bitmap = null;
                        var9.getStackTrace();
                    }

                    return bitmap;
                } else {
                    return src;
                }
            } else {
                return null;
            }
        }
    }
}
