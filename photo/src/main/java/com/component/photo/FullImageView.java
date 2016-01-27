package com.component.photo;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 自定义一下，直接查看大图
 * Created by weibo on 16-1-7.
 */
public class FullImageView extends ImageView {

    private String imgUrl;
    private Uri imgUri;
    private Context context;

    public FullImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FullImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoService photoService = new PhotoService(context);
                if (imgUri != null) {
                    photoService.viewPhoto(context, imgUri);
                } else if (!TextUtils.isEmpty(imgUrl)) {
                    photoService.viewPhoto(context, imgUrl);
                }
            }
        });
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }
}
