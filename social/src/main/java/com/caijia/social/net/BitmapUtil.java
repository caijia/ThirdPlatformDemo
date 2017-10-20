package com.caijia.social.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BitmapUtil {

    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    private static Bitmap getSmallBitmap(Context context, int resId, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static BitmapFactory.Options getOnlyBounds(Context context, int bitmapId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), bitmapId, options);
        return options;
    }

    public static ByteArrayOutputStream bitmapToStream(String filePath, int width, int height) {
        Bitmap bm = getSmallBitmap(filePath, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return out;
    }

    public static ByteArrayOutputStream bitmapToStream(Context context, int resId, int width, int height) {
        Bitmap bm = getSmallBitmap(context, resId, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return out;
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath, int width, int height) {
        ByteArrayOutputStream out = bitmapToStream(filePath, width, height);
        byte[] b = out.toByteArray();
        return Base64.encodeToString(b, 0, b.length, Base64.DEFAULT);
    }

    public static String bitmapToString(String filePath) {
        return bitmapToString(filePath, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static String bitmapToString(Context context, int resId, int width, int height) {
        ByteArrayOutputStream out = bitmapToStream(context, resId, width, height);
        byte[] b = out.toByteArray();
        return Base64.encodeToString(b, 0, b.length, Base64.DEFAULT);
    }

    public static byte[] bitmapToByte(Bitmap bm,int percent) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, percent, out);
        return out.toByteArray();
    }

    public static byte[] bitmapToByte(Context context, int resId, int width, int height) {
        Bitmap bm = getSmallBitmap(context, resId, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return out.toByteArray();
    }

    public static byte[] bitmapToByte(File file, int width, int height,int percent) {
        Bitmap bitmap = getSmallBitmap(file.getAbsolutePath(), width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, percent, out);
        return out.toByteArray();
    }

    public static File bitmapToFile(Bitmap bitmap, String path,int percent) {
        File saveFile = new File(path);
        if (saveFile.exists() && saveFile.length() > 0) {
            return saveFile;
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(saveFile);
        } catch (FileNotFoundException e) {
        }
        if (out != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, percent, out);
        }
        return saveFile;
    }

    //base64字符串转化成图片
    public static File Base64ToImage(String imgStr, String path,int percent) {
        if (imgStr != null) {
            byte[] bitmapArray = Base64.decode(imgStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return bitmapToFile(bitmap, path,percent);
        }
        return null;
    }
}
