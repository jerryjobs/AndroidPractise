package com.ikaowo.join.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.ikaowo.join.R;

/**
 * Created by leiweibo on 1/3/16.
 */
public class AvatarHelper {

  private static AvatarHelper instance;

  private AvatarHelper() {}

  public static AvatarHelper getInstance() {
    if (instance == null) {
      instance = new AvatarHelper();
    }

    return instance;
  }

  public void showAvatar(Context context, ImageView iconIv, TextView shortNameTv, int width, int height, String avatarIconUrl, String userName) {
    ImageLoader imageLoader = JApplication.getImageLoader();
    if (TextUtils.isEmpty(avatarIconUrl)) {
      iconIv.setBackgroundColor(ContextCompat.getColor(context, Constant.getRandomColor()));
      shortNameTv.setVisibility(View.VISIBLE);
      shortNameTv.setTextColor(ContextCompat.getColor(context, android.R.color.white));
      if (isChinese(userName)) {
        shortNameTv.setText(userName.substring(Math.max(userName.length() - 2, 0), userName.length()));
      } else {
        shortNameTv.setText(userName.substring(0, Math.min(userName.length(), 2)));
      }
    } else {
      shortNameTv.setVisibility(View.GONE);
      iconIv.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
      imageLoader.loadImage(iconIv, avatarIconUrl, width, height, R.drawable.brand_icon_default);
    }
  }

  public boolean isChinese(String strName) {
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c)) {
        return true;
      }
    }
    return false;
  }

  private boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
      || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
      || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
      || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
      || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

}
