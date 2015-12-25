package com.common.framework.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by weibo on 15-12-7.
 */
public class ImageLoader {

    private Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void loadImage(ImageView imageView, String url, int targetWidth, int defaultImg) {
        int mode = 1;
        load(context, imageView, url, targetWidth, -1, mode, null, defaultImg);
    }

    public void loadImage(ImageView imageView, String url, int targetWidth, Target target, int defaultImg) {
        int mode = 1;
        load(context, imageView, url, targetWidth, -1, mode, target, defaultImg);
    }

    public void loadImage(ImageView imageView, String url, int targetWidth, int targetHeight, int defaultImg) {
        load(context, imageView, url, targetWidth, targetHeight, 1, null, defaultImg);
    }

    public void loadImage(ImageView imageView, String url, int targetWidth, int targetHeight, Target target, int defaultImg) {
        load(context, imageView, url, targetWidth, targetHeight, 1, target, defaultImg);
    }

    public void loadImage(ImageView imageView, String url, int targetWidth, int targetHeight, int mode, int defaultImg) {
        load(context, imageView, url, targetWidth, targetHeight, mode, null, defaultImg);
    }

    private void load(Context context, ImageView imageView, String url, int targetWidth,
                      int targetHeight, int mode, Target target, int defaultImg) {
        String imgUrl = url + getModeUrl(mode, targetWidth, targetHeight);
        Log.e("ImageLoader", "the imgUrl:" + imgUrl);
        if (!TextUtils.isEmpty(imgUrl)) {
            if (target != null) {
                Picasso.with(context).load(url).config(Bitmap.Config.RGB_565).placeholder(defaultImg).into(target);
            } else {
                Picasso.with(context).load(url).config(Bitmap.Config.RGB_565).placeholder(defaultImg).into(imageView);
            }
        }
    }

    /**
     * 获取7牛的不同的模式的缩略图，具体参考
     * <a>http://developer.qiniu.com/docs/v6/api/reference/fop/image/imageview2.html</a>
     */
    public String getModeUrl(int mode, int width, int height) {
        if (width < 0 && height < 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder("?imageView2/").append(mode);
        if (width > 0) {
            sb.append("/w/").append(width);
        }

        if (height > 0) {
            sb.append("/h/").append(height);
        }

        return sb.toString();
    }
}
