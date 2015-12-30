package com.ikaowo.join.modules.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.modules.mine.MineItemWidget;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-29.
 */
public class MineActivity extends BaseEventBusFragmentActivity implements PhotoService.UploadFinishListener {

  @Bind(R.id.auth_state)
  MineItemWidget authStateItem;

  @Bind(R.id.user_icon)
  MineItemWidget userIconItem;

  @Bind(R.id.user_name)
  MineItemWidget userNameItem;

  @Bind(R.id.brand_name)
  MineItemWidget userBrandNameItem;

  @Bind(R.id.user_title)
  MineItemWidget userTitleItem;

  @Bind(R.id.user_phone)
  MineItemWidget userPhoneItem;

  private UserService userService;
  private MineService mineService;
  private PhotoService photoService;
  private ImageLoader imageLoader;
  private QiniuUploadHelper qiniuUploadHelper;
  private final String PASSED = "T", FAILED = "F", PROCESSING = "N";
  private int targetWidth = JApplication.getJContext().dip2px(64);
  private int targetHeight = JApplication.getJContext().dip2px(64);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mine);
    ButterKnife.bind(this);
    mineService = JApplication.getJContext().getServiceByInterface(MineService.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    photoService = new PhotoService(this);
    imageLoader = JApplication.getImageLoader();
    qiniuUploadHelper = new QiniuUploadHelper();

    if (!userService.isLogined()) {
      finish();
      return;
    }
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.title_ativity_mine);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    setupDate();
  }

  private void setupDate() {
    UserLoginData user = userService.getUser();
    if (user != null) {
      boolean authed = PASSED.equalsIgnoreCase(user.state) && PASSED.equalsIgnoreCase(user.companyState);
      boolean failed = FAILED.equalsIgnoreCase(user.state) || FAILED.equalsIgnoreCase(user.companyState);
      boolean processing = PROCESSING.endsWith(user.state) && PROCESSING.equalsIgnoreCase(user.companyState);
      String state = "";
      if (authed) {
        state = "已认证";
      } else if (failed) {
        state = "认证失败";
      } else if (processing) {
        state = "认证中";
      }
      authStateItem.setText(state);

      ImageView imageView = userIconItem.getImageView();
      if (userIconItem.getImageView() != null) {

        imageLoader.loadImage(imageView, user.icon, targetWidth, targetHeight, R.drawable.brand_icon_default);
      }

      userNameItem.setText(user.nickName);
      userBrandNameItem.setText(user.brandInfo != null ? user.brandInfo.brand_name : "");
      userTitleItem.setText(user.title);
      userPhoneItem.setText(user.phone);

    }
  }

  @OnClick(R.id.user_icon)
  public void updateIcon() {
    photoService.takePhoto(this, toolbar, null);
  }

  @OnClick(R.id.user_update_passwd)
  public void updatePasswd() {
    mineService.updatePassword(this);
  }

  @OnClick((R.id.logout))
  public void logout() {
    userService.logout(this);
  }

  @Override
  protected String getTag() {
    return "MineActivity";
  }

  public void onEvent(ClosePageCallback callback) {
    if (callback.close()) {
      finish();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    qiniuUploadHelper.uploadImg(this,  requestCode, resultCode, data, this);
  }

  @Override
  public void onUpLoadImageFinish(String imgUrl, Uri imgUri) {
    //TODO 修改头像api请求
    ImageView iconIv = userIconItem.getImageView();
    if (iconIv == null) {
      return;
    }
    if (imgUri != null) {
      Picasso.with(this)
              .load(imgUri).centerCrop().resize(targetWidth, targetHeight).into(iconIv);
    } else if (!TextUtils.isEmpty(imgUrl)) {
      imageLoader.loadImage(iconIv, imgUrl, targetWidth, targetHeight, R.drawable.brand_icon_default);
    }
  }

  @Override
  public void onUpLoadImageFailed() {

  }
}
