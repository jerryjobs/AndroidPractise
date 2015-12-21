package com.ikaowo.join;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.common.framework.core.JApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewImageActivity extends BaseActivity {

  Button viewImgBtn;
  ImageView imageView;

  Button viewImgBtn2;
  ImageView imageView2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);

    viewImgBtn = (Button) findViewById(R.id.viewImgBtn);
    imageView = (ImageView) findViewById(R.id.imgView);

    viewImgBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        JApplication.getImageLoader().loadImage(
                imageView, "https://futurestud.io/images/books/picasso.png",
                -1, R.mipmap.ic_launcher);
      }
    });

    viewImgBtn2 = (Button) findViewById(R.id.viewImgBtn2);
    imageView2 = (ImageView) findViewById(R.id.imgView2);

    viewImgBtn2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        JApplication.getImageLoader().loadImage(imageView2, "https://futurestud.io/images/books/picasso.png", -1, new Target() {
          @Override
          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            imageView2.setImageBitmap(bitmap);
          }

          @Override
          public void onBitmapFailed(Drawable errorDrawable) {

          }

          @Override
          public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
        }, R.mipmap.ic_launcher);
      }
    });
  }

  @Override
  protected String getTag() {
    return "ViewImageActivity";
  }
}
