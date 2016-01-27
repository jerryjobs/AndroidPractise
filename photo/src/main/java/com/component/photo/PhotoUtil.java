package com.component.photo;

import android.content.Context;

import java.io.File;

/**
 * Created by weibo on 15-12-10.
 */
public class PhotoUtil {

    public static final int REQUEST_TAKE_PHOTO = 10001;
    public static final int REQUEST_PICK_PHOTO = 10002;
    public static final int REQUEST_CROP_PHOTO = 10003;
    public static final String ASPECT_KEY = "aspect";
    public static final String PHOTO_ACTION = "photo_action";
    public static final String FILE_KEY = "file";
    public static final String MEASURABLE_BUNDLE_NAME = "measurable";
    public static final String NOFACEDETECTION = "noFaceDetection";
    public static final String CROP = "crop";
    public static final String SCALE = "scale";
    public static final String SCALEUPIFNEEDED = "scaleUpIfNeeded";
    public static final String OUTPUTFORMAT = "outputFormat";

    public static final String ASPECTX = "aspectX";
    public static final String ASPECTY = "aspectY";

    public static final String IMG_TYPE = "image/*";

    public static String getFilePath(Context context) throws IllegalAccessException {
        try {
            String path = context.getExternalCacheDir()
                    + File.separator
                    + "crop_image";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return path;
        } catch (Exception e) {
            throw new IllegalAccessException("缓存目录不可用");
        }
    }

    public static void dropCropImageFolder(Context context) throws IllegalAccessException {
        try {
            String path = context.getExternalCacheDir() + File.separator + "crop_image";

            File dir = new File(path);
            delete(dir);
        } catch (Exception e) {
            throw new IllegalAccessException("缓存目录不可用");
        }
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
}
