package com.component.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by weibo on 28/5/15.
 */
public class PicHelper {
    public static Bitmap getBitmap(String oldPath, int bitmapMaxWidth) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(oldPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight = 0;
        int reqWidth = Math.min(bitmapMaxWidth, width);
        reqHeight = (reqWidth * height) / width;
        // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
        options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
        if (reqWidth != width) {
            bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
        }
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static ByteArrayOutputStream compressImageStream(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            options -= 5;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        return baos;
    }
}
