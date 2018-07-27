package com.zhxh.xtouchsystem.touch.conflict;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UIUtils {

    public static int getGalleryVelocity(Context context) {
        if (null == context) {
            return -1;
        }

        float beishu = 2.47f;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            beishu = 1.52f;
        }

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int velocity = -(int) (dm.widthPixels * beishu);

        return velocity;
    }

    public static Bitmap resource2Bitmap(Context mContext, int id) {
        return null == mContext ? null : BitmapFactory.decodeResource(mContext.getResources(), id);
    }


    public static BitmapNull resource2BitmapNull(Context mContext, int id) {

        if (null == mContext) {
            return new BitmapNull(0, 0);
        }

        Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(), id);

        if (temp == null) {
            return new BitmapNull(0, 0);
        }

        BitmapNull ret = new BitmapNull(temp.getWidth(), temp.getHeight());

        temp.recycle();
        temp = null;

        return ret;
    }

    public static int computeSampleSize(Options options,
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

    private static int computeInitialSampleSize(Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap zoomBitmap(Context context, byte[] _b) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        int d = displayMetrics.densityDpi;

        Options opts = new Options();
        opts.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeByteArray(_b, 0, _b.length, opts);

            //int x = 2;
            int x = computeSampleSize(opts, w > h ? w : h, w * h);
            opts.inTargetDensity = d;
            opts.inSampleSize = x;
            opts.inJustDecodeBounds = false;

            opts.inDither = false;
            opts.inPurgeable = true;

            return BitmapFactory.decodeByteArray(_b, 0, _b.length, opts);
        } catch (OutOfMemoryError ooe) {
            ooe.printStackTrace();
            System.gc();
            //			VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        }
        return null;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return null == bitmap ? null : new BitmapDrawable(bitmap);
    }

    public static View inflateView(Context context, int resource, ViewGroup root) {
        return View.inflate(context, resource, root);
    }

    public static void toast(Context _ctx, Object msg) {
        toast(_ctx, msg, Toast.LENGTH_LONG);
    }

    /**
     * 自定义toast图片显示时间方法
     */
    public static void toastImg(Context _ctx, Object msg, int time) {
        Handler mHandler = new Handler();
        final Toast toast = Toast.makeText(_ctx, "", Toast.LENGTH_LONG);
        toast.setView((ImageView) msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                toast.cancel();
            }
        }, time);
    }

    /**
     * toast 新增加 duration 参数，目前仅支持 duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG
     *
     * @param _ctx
     * @param msg
     * @param duration
     */
    public static void toast(Context _ctx, Object msg, int duration) {

        if (!(duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG)) {
            duration = Toast.LENGTH_LONG;
        }

        if (null != _ctx) {
            if (msg instanceof Integer) {
                Toast.makeText(_ctx, ((Integer) msg), duration).show();
            } else if (msg instanceof ImageView) {
                Toast toast = Toast.makeText(_ctx, "", duration);
                toast.setView((ImageView) msg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Toast.makeText(_ctx, String.valueOf(msg), duration).show();
            }
        }
    }

    public static int dipToPx(Context ctx, int dipValue) {
        if (null == ctx) {
            return (int) dipValue;
        }

        final float scale = ctx.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }

    /**
     * dip转换成像素值
     */
    public static int dip2px(Context ctx, float dipValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 像素值转换成dip
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Options getBitmapOption(Context mContext) {
        Options option = new Options();
        option.inScaled = true;
        option.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        option.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;

        return option;
    }

    public static int pxToDip(Context mContext, float pxValue) {
        if (null == mContext) {
            return (int) pxValue;
        }

        final float scale = mContext.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }

    public static byte[] Bitmap2Bytes(Bitmap _b) {
        if (null == _b) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap drawable2Bitmap(Drawable _d) {
        return null == _d ? null : ((BitmapDrawable) _d).getBitmap();
    }

    public static byte[] drawable2ByteArray(Drawable _d) {
        return null == _d ? Bitmap2Bytes(drawable2Bitmap(_d)) : null;
    }

    public static Bitmap byteArray2Bitmap(byte[] _b, Options option) {
        return null == _b ? null : BitmapFactory.decodeByteArray(_b, 0, _b.length, option);
    }

    public static Bitmap byteArray2Bitmap(byte[] _b) {
        return null == _b ? null : BitmapFactory.decodeByteArray(_b, 0, _b.length);
    }

    public static Bitmap byteArray2Bitmap(Context context, byte[] _b) {
        return null == _b ? null : BitmapFactory.decodeByteArray(_b, 0, _b.length, getBitmapOption(context));
    }

    public static Drawable resource2Drawable(Context mContext, int id) {
        return null == mContext ? null : bitmap2Drawable(BitmapFactory.decodeResource(mContext.getResources(), id));
    }

    public static Drawable byteArray2Drawable(Context context, byte[] _b) {
        Bitmap b = zoomBitmap(context, _b);
        //Bitmap b = BitmapFactory.decodeByteArray(_b, 0, _b.length, getBitmapOption(context));
        return null == _b ? null : bitmap2Drawable(b);
    }

    /**
     * zhxh
     * 获取边界压缩的bitmap流
     *
     * @param context
     * @param _b
     * @return
     */
    public static Bitmap byteArray2ImgBitmap(Context context, byte[] _b) {
        Bitmap b = zoomBitmap(context, _b);
        //Bitmap b = BitmapFactory.decodeByteArray(_b, 0, _b.length, getBitmapOption(context));
        return null == _b ? null : b;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;

    }

    public static void showSoftKeyboard(Activity mActivity) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (null != mActivity.getCurrentFocus()) {
            imm.showSoftInput(mActivity.getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftkeyboard(Activity mActivity) {
        if (null != mActivity && null != mActivity.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != mInputMethodManager) {
                mInputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * add by zhxh 强制收起键盘
     *
     * @param mActivity
     * @param now
     */
    public static void hideSoftkeyboard(Activity mActivity, boolean now) {
        if (null != mActivity && null != mActivity.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != mInputMethodManager) {
                mInputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * @author zhxh
     * 只用于保存图片大小
     */
    public static class BitmapNull {
        private int mWidth = 0;
        private int mHeight = 0;

        public BitmapNull(int w, int h) {
            mWidth = w;
            mHeight = h;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

    /**
     * 创建图片Uri
     */
    public static Uri getImageUri(Activity activity) {
        //获取系统相册存储地址
        Uri uri_DCIM = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String DCIMPath = "";
        Cursor cr = activity.getContentResolver().query(uri_DCIM,
                new String[]{MediaStore.Images.Media.DATA}, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cr.moveToNext()) {
            DCIMPath = cr.getString(cr.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cr.close();
        DCIMPath = DCIMPath.substring(0, DCIMPath.lastIndexOf("/") + 1);

        //使用系统当前日期加以调整作为照片的名称
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        String picName = dateFormat.format(date) + ".png";

        String IMAGE_FILE_LOCATION = "file://" + DCIMPath + picName;
        return Uri.parse(IMAGE_FILE_LOCATION);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
