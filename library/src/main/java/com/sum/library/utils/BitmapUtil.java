package com.sum.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理类
 * 支持读写图片，缩放，倒影，压缩图片
 */
public class BitmapUtil {
    private static final String LOG_TAG = "BitmapUtil";

    public static final int INT_FULL_SCLAE = 0;
    public static final int INT_UN_FULL_SCLAE = 1;
    public static final int INT_NO_SCLAE = 2;
    private static int m_iMaxBitmapSize = 1 * 1024 * 1024;

    private static int m_iDefaultMaxWidth = 350;

    private static int m_iDefaultMaxHeight = 450;

    public static void setMaxBitmapSize(int iSize) {
        m_iMaxBitmapSize = iSize;
    }

    public static void setDefaultMaxSize(int iMaxWidth, int iMaxHeight) {
        m_iDefaultMaxWidth = iMaxWidth;
        m_iDefaultMaxHeight = iMaxHeight;
    }

    /**
     * 通知到系统上下文中
     */
    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 其次把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
    }

    public static Bitmap scaleBitmap(int idstWidth, int idstHeight,
                                     Bitmap srcBitmap, int scaleStyle) {
        if (null == srcBitmap) {
            return null;
        }

        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();

        Bitmap bitmap = null;

        try {
            switch (scaleStyle) {
                case INT_NO_SCLAE: {
                    bitmap = srcBitmap;
                    break;
                }
                case INT_FULL_SCLAE: {
                    bitmap = Bitmap.createScaledBitmap(srcBitmap, idstWidth,
                            idstHeight, true);
                    break;
                }
                case INT_UN_FULL_SCLAE: {
                    if (width > idstWidth && height > idstHeight) {
                        float scale = 0;
                        float scaleWidth = (float) width / (float) idstWidth;
                        float scaleHeight = (float) height / (float) idstHeight;
                        if (scaleWidth > scaleHeight) {
                            scale = scaleWidth;
                        } else {
                            scale = scaleHeight;
                        }
                        int tmpWidth = (int) ((float) width / scale);
                        int tmpHigth = (int) ((float) height / scale);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                tmpHigth, true);
                    } else if (width <= idstWidth && height > idstHeight) {
                        int tmpWidth = (int) ((float) idstHeight
                                * (float) width / (float) height);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                idstHeight, true);
                    } else if (width > idstWidth && height <= idstHeight) {
                        int tmpHeight = (int) ((float) idstWidth
                                * (float) height / (float) width);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap,
                                idstWidth, tmpHeight, true);
                    } else {
                        float scale = 0;
                        float scaleWidth = (float) idstWidth / (float) width;
                        float scaleHeight = (float) idstHeight / (float) height;
                        if (scaleWidth > scaleHeight) {
                            scale = scaleHeight;
                        } else {
                            scale = scaleWidth;
                        }
                        int tmpWidth = (int) ((float) width * scale);
                        int tmpHigth = (int) ((float) height * scale);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                tmpHigth, true);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (OutOfMemoryError e) {
            LogUtil.e(LOG_TAG, e);
        }

        return bitmap;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        if (null == inStream) {
            LogUtil.w("inStream is null!");
            return null;
        }

        int iTotalDataCount = inStream.available();
        if (0 == iTotalDataCount) {
            LogUtil.w("iTotalDataCount is zero!");
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[iTotalDataCount];
        int iLen = 0;
        while ((iLen = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, iLen);

            if (outStream.size() > m_iMaxBitmapSize) {
                LogUtil.w("Exceed max bitmap size:" + outStream.size()
                        + ",max:" + m_iMaxBitmapSize + ",iTotalDataCount="
                        + iTotalDataCount);
                return null;
            }
        }

        return outStream.toByteArray();
    }

    public static Bitmap readBitMap(Context ctx, int iResId) {
        if (null == ctx || iResId <= 0) {
            return null;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), iResId,
                    initOptions());
        } catch (OutOfMemoryError e) {
            LogUtil.e(LOG_TAG, e);
        }

        return bitmap;
    }

    public static Bitmap readAdaptiveBitMap(Context ctx, int resId,
                                            int toWidth, int toHeight) {
        if (null == ctx || resId <= 0) {
            return null;
        }

        Bitmap bitmap = null;

        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeResource(ctx.getResources(), resId, options);
            int iSampleSize = calculateInSampleSize(options, toWidth, toHeight);

            options = initOptions();
            options.inSampleSize = iSampleSize;
            options.inJustDecodeBounds = false;

            InputStream is = ctx.getResources().openRawResource(resId);
            bitmap = readBitmap(is, options);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            LogUtil.e(LOG_TAG, e);
        } catch (Exception e) {
            LogUtil.e("Exception", e);
        }

        return bitmap;
    }

    public static Bitmap readBitmap(String strPathName) {
        return readBitmap(strPathName, null);
    }

    public static Bitmap readBitmap(String strPathName, Options option) {
        if (TextUtils.isEmpty(strPathName)) {
            return null;
        }

        byte[] data = null;
        try {
            data = readStream(new FileInputStream(new File(strPathName)));

            if (null == data || data.length < 0) {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    null != option ? option : initOptions());
        } catch (OutOfMemoryError e) {
            LogUtil.e(LOG_TAG, e);
        }

        return bitmap;
    }

    public static Bitmap readBitmap(InputStream is) {
        return readBitmap(is, null);
    }

    public static Bitmap readBitmap(InputStream is, Options option) {
        if (null == is) {
            return null;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(is, null,
                    null != option ? option : initOptions());
        } catch (OutOfMemoryError e) {
            LogUtil.e(LOG_TAG, e);
        }

        return bitmap;
    }

    public static Bitmap readBitmapForFixMaxSize(String strPathName) {
        return readBitmapForFixMaxSize(strPathName, m_iDefaultMaxWidth,
                m_iDefaultMaxHeight);
    }

    public static Bitmap readBitmapForFixMaxSize(String strPathName,
                                                 int iMaxWidth, int iMaxHeight) {
        if (TextUtils.isEmpty(strPathName)) {
            LogUtil.w("strPathName is empty!");
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(strPathName, options);
            int iSampleSize = calculateInSampleSize(options, iMaxWidth,
                    iMaxHeight);

            options = initOptions();
            options.inSampleSize = iSampleSize;
            options.inJustDecodeBounds = false;

            LogUtil.d("inSampleSize=" + options.inSampleSize
                    + ",strPathName=" + strPathName);

            Bitmap bmpReturn = BitmapFactory.decodeFile(strPathName, options);
            if (null != bmpReturn) {
                return bmpReturn;
            } else {
                LogUtil.e("Read bitmap file error! File:" + strPathName);
                try {
                    bmpReturn = BitmapFactory.decodeStream(new FileInputStream(
                            strPathName), null, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != bmpReturn) {
                    return bmpReturn;
                } else {
                    bmpReturn = BitmapFactory
                            .decodeStream(new FlushedInputStream(
                                    new FileInputStream(strPathName)));
                    if (null != bmpReturn) {
                        return bmpReturn;
                    } else {
                        LogUtil.e("Read bitmap file error! File:"
                                + strPathName);
                        return null;
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            LogUtil.e(e.getMessage());
        } catch (Exception e) {
            LogUtil.e("Read bitmap file error! File:" + strPathName
                    + " " + e.getMessage());
        }

        return null;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    private static int calculateInSampleSize(Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }

        return inSampleSize;
    }

    private static Options initOptions() {
        Options opts = new Options();
        opts.inPreferredConfig = Config.ARGB_8888;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;

        opts.inTempStorage = new byte[12 * 1024];

        return opts;
    }

    /**
     * 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 倒影图片
     */
    public static Bitmap createReflectImages(Bitmap bImap) {
        if (null == bImap) {
            return null;
        }

        final int reflectionGap = 4;
        int width = bImap.getWidth();
        int height = bImap.getHeight();

        Bitmap bitmapWithReflection = null;
        try {
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            Bitmap reflectionImage = Bitmap.createBitmap(bImap, 0, height / 2,
                    width, height / 2, matrix, false);
            bitmapWithReflection = Bitmap.createBitmap(width,
                    (height + height / 2), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapWithReflection);
            canvas.drawBitmap(bImap, 0, 0, null);
            Paint deafaultPaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap,
                    deafaultPaint);
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0, bImap.getHeight(), 0,
                    bitmapWithReflection.getHeight() + reflectionGap,
                    0x80ffffff, 0x00ffffff, TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    Mode.DST_IN));
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                    + reflectionGap, paint);
        } catch (OutOfMemoryError e) {
            LogUtil.w(LOG_TAG, e);
        }

        return bitmapWithReflection;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (null == bm) {
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (OutOfMemoryError e) {
            LogUtil.w(LOG_TAG, e);
        }

        return null;
    }


    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        if (null == drawable || w < 0 || h < 0) {
            return null;
        }

        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap oldbmp = drawableToBitmap(drawable);
            Matrix matrix = new Matrix();
            float scaleWidth = 1;
            float scaleHeight = 1;
            if (w > 0) {
                scaleWidth = ((float) w / width);
            }
            if (h > 0) {
                scaleHeight = ((float) h / height);
            }

            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                    matrix, true);
            return new BitmapDrawable(newbmp);
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, e);
            return null;
        }
    }

    static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    // default 500
    public static byte[] compressImage(Bitmap image) {
        if (image == null) {
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length / 1024 > 500) {
                baos.reset();
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            return baos.toByteArray();
        } catch (OutOfMemoryError e) {
            LogUtil.w(LOG_TAG, e);
        }

        return null;
    }

    public static byte[] compressImage(String filepath) {
        Bitmap tmpBitmap = readBitmapForFixMaxSize(filepath, 480, 800);
        return compressImage(tmpBitmap);
    }
}
