package com.component.photo;

import android.os.Environment;

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

  public static String getFilePath() throws IllegalAccessException {
    try {
      String path = Environment.getExternalStorageDirectory()
        + File.separator + Environment.DIRECTORY_DCIM
        + File.separator + "Camera";

      File dir = new File(path);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      return path;
    } catch (Exception e) {
      throw new IllegalAccessException("SD卡不可用");
    }
  }
}
