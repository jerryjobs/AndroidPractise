package com.component.photo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends AppCompatActivity {

    PhotoView photoView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        photoView = (PhotoView) findViewById(R.id.mainsys_fullimg_img);
        progressBar = (ProgressBar) findViewById(R.id.mainsys_fullimg_progress);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        getIntentData();
    }

    private void getIntentData() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            Object imgData = data.get("image");
            if (imgData == null) {
                return;
            }

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    progressBar.setVisibility(View.GONE);
                    photoView.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            };

            if (imgData instanceof Uri) {
                Picasso.with(PhotoViewActivity.this).load((Uri) imgData).into(photoView);
            } else if (imgData instanceof String) {
                if (!TextUtils.isEmpty(imgData.toString())) {
                    Picasso.with(PhotoViewActivity.this).load(imgData.toString()).into(target);
                }
            }
        }
    }
}
