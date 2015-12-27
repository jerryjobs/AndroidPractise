package com.component.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

/**
 * Created by weibo on 15-12-10.
 */
public class CropPhotoActivity extends AppCompatActivity {

  private final String TAG = "CropPhotoActivity";

  private String filePath;
  private boolean aspectCrop;
  private String cropToPath;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    filePath = getIntent().getStringExtra(PhotoUtil.FILE_KEY);
    aspectCrop = getIntent().getBooleanExtra(PhotoUtil.ASPECT_KEY, false);
    try {
      cropToPath = PhotoUtil.getFilePath() + "/" + System.currentTimeMillis() + ".jpg";
      cropPhoto(cropToPath);
    } catch (IllegalAccessException exception) {
      Log.e(TAG, exception.getMessage());
    }
  }

  private void cropPhoto(String path) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.putExtra("return-data", false)
      .putExtra(PhotoUtil.NOFACEDETECTION, true)
      .putExtra(PhotoUtil.CROP, "true")
      .putExtra(PhotoUtil.SCALE, true)
      .putExtra(PhotoUtil.SCALEUPIFNEEDED, true)
      .putExtra(PhotoUtil.OUTPUTFORMAT, Bitmap.CompressFormat.JPEG.toString());

    if (aspectCrop) {
      intent.putExtra(PhotoUtil.ASPECTX, 1) // 剪切的宽高比为1：1
        .putExtra(PhotoUtil.ASPECTY, 1);
    } else {
      Bundle b = getIntent().getBundleExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME);
      if (null != b) {
        intent.putExtra(PhotoUtil.ASPECTX, b.getInt(PhotoUtil.ASPECTX));
        intent.putExtra(PhotoUtil.ASPECTY, b.getInt(PhotoUtil.ASPECTY));
      }
    }

    intent.setDataAndType(Uri.fromFile(new File(filePath)), PhotoUtil.IMG_TYPE);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
    startActivityForResult(intent, PhotoUtil.REQUEST_CROP_PHOTO);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case PhotoUtil.REQUEST_CROP_PHOTO:
        if (resultCode == RESULT_CANCELED) {
          cancel();
        } else {
          cropSuc(cropToPath);
        }
        break;

      default:
        break;
    }
  }

  // 直接返回数据
  private void cropSuc(String path) {
    Intent intent = new Intent();
    intent.setData(Uri.fromFile(new File(path)));
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  public void onBackPressed() {
    cancel();
  }

  private void cancel() {
    setResult(RESULT_CANCELED);
    finish();
  }
}
