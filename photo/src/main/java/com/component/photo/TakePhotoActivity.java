package com.component.photo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by weibo on 15-12-10.
 */
public class TakePhotoActivity extends AppCompatActivity {

  private final String TAG = "TakePhotoActivity";
  private final String CONTENT_PREFIX = "content://";

  private boolean aspectCrop;
  private boolean pickPhoto;
  private String tmpPicPath;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    aspectCrop = getIntent().getBooleanExtra(PhotoUtil.ASPECT_KEY, false);
    pickPhoto = getIntent().getBooleanExtra(PhotoUtil.PHOTO_ACTION, false);

    if (pickPhoto) {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType(PhotoUtil.IMG_TYPE);
      startActivityForResult(intent, PhotoUtil.REQUEST_PICK_PHOTO);
    } else {
      try {
        tmpPicPath = PhotoUtil.getFilePath() + "/" + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tmpPicPath)));
        startActivityForResult(intent, PhotoUtil.REQUEST_TAKE_PHOTO);
      } catch (IllegalAccessException exception) {
        Log.e(TAG, exception.getMessage());
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case PhotoUtil.REQUEST_TAKE_PHOTO:
        takePhoto(resultCode);
        break;

      case PhotoUtil.REQUEST_PICK_PHOTO:
        pickPhoto(resultCode, data);
        break;

      case PhotoUtil.REQUEST_CROP_PHOTO:
        cropPhoto(resultCode, data);
        break;
    }
  }

  private void takePhoto(int resultCode) {
    if (RESULT_CANCELED == resultCode) {
      setResult(RESULT_CANCELED);
      finish();
      return;
    }

    File file = new File(tmpPicPath);

    if (file.exists()) {
      Intent intent = new Intent(this, CropPhotoActivity.class);
      intent.putExtra(PhotoUtil.FILE_KEY, tmpPicPath);
      intent.putExtra(PhotoUtil.ASPECT_KEY, aspectCrop);
      intent.putExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME, getIntent().getBundleExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME));
      startActivityForResult(intent, PhotoUtil.REQUEST_CROP_PHOTO);
    } else {
      Log.e(TAG, "file not exists! : [" + file.getAbsolutePath() + "]");
    }
  }

  private void pickPhoto(int resultCode, Intent data) {
    if (data == null) {
      setResult(RESULT_CANCELED);
      finish();
    } else {
      new ImageLoaderTask().execute(data.getData());
    }
  }

  private void cropPhoto(int resultCode, Intent data) {
    if (resultCode == RESULT_CANCELED) {
      setResult(RESULT_CANCELED);
    } else {
      setResult(RESULT_OK, data);
    }
    finish();
  }

  private class ImageLoaderTask extends AsyncTask<Uri, Void, String> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected String doInBackground(Uri... params) {
      Cursor cursor = null;
      try {
        Uri contentUri = params[0];
        if (contentUri.toString().startsWith(CONTENT_PREFIX)) {
          String[] proj = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
          cursor = getContentResolver().query(contentUri, proj, null, null, null);
          int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          cursor.moveToFirst();
          String path = cursor.getString(columnIndex);
          if (path == null) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            path = cursor.getString(columnIndex);
          }
          if (new File(path).exists()) {
            return path;
          }
          return saveImgToDisk(contentUri);
        } else {
          return contentUri.toString();
        }
      } catch (Exception e) {
        Log.e(TAG, "failed to load image", e);
        return null;
      } finally {
        if (null != cursor) {
          cursor.close();
        }
      }
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);

      if (s != null) {
        Intent intent = new Intent(TakePhotoActivity.this, CropPhotoActivity.class);
        intent.putExtra(PhotoUtil.FILE_KEY, s);
        intent.putExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME,
          getIntent().getBundleExtra(PhotoUtil.MEASURABLE_BUNDLE_NAME));
        intent.putExtra(PhotoUtil.ASPECT_KEY, aspectCrop);
        startActivityForResult(intent, PhotoUtil.REQUEST_CROP_PHOTO);
      } else {
        finish();
      }
    }

    private String saveImgToDisk(Uri uri) throws FileNotFoundException {
      String filePath = tmpPicPath + "/" + System.currentTimeMillis() + ".jpg";
      Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
      saveBitmap(filePath, bitmap);

      return filePath;
    }

    private void saveBitmap(String filename, Bitmap bitmap) {
      File f = new File(filename);
      FileOutputStream fOut = null;
      try {
        f.createNewFile();
        fOut = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();

      } catch (Exception e) {
        Log.e(TAG, "failed to save image", e);
      } finally {
        try {
          fOut.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
