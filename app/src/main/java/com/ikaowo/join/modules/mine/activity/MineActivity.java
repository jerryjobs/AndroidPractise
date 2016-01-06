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
import com.common.framework.network.NetworkManager;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.AvatarUpdateCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.UpdateAvatarRequest;
import com.ikaowo.join.modules.mine.MineItemWidget;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;

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
  private Map<String, Integer> stateColorMap = new HashMap<>();
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

    stateColorMap.put(Constant.AUTH_STATE_PASSED, R.color.c1);
    stateColorMap.put(Constant.AUTH_STATE_PENDING_APPROVE, R.color.c1);
    stateColorMap.put(Constant.AUTH_STATE_FAILED, R.color.c11);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    setupDate();
  }

  private void setupDate() {
    UserLoginData user = userService.getUser();
    if (user != null) {
      boolean authed = Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.state) && Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.companyState);
      boolean failed = Constant.AUTH_STATE_FAILED.equalsIgnoreCase(user.state) || Constant.AUTH_STATE_FAILED.equalsIgnoreCase(user.companyState);
      boolean processing = Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(user.state) || Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(user.companyState);
      String state = "";
      if (authed) {
        state = "已认证";
      } else if (failed) {
        state = "认证失败";
      } else if (processing) {
        state = "认证中";
      }
      authStateItem.setText(state);
      authStateItem.setTextColor(stateColorMap.get(user.state));
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
  public void onUpLoadImageFinish(final String imgUrl, final Uri imgUri) {
    final ImageView iconIv = userIconItem.getImageView();
    if (iconIv == null) {
      return;
    }

    NetworkManager networkManager = JApplication.getNetworkManager();
    UserInterface userInterface = networkManager.getServiceByClass(UserInterface.class);
    UpdateAvatarRequest request = new UpdateAvatarRequest();
    request.icon = imgUrl;
    Call<BaseResponse> call = userInterface.updateAvatar(request);
    networkManager.async(this, Constant.PROCESSING, call, new KwMarketNetworkCallback<BaseResponse>(this) {

      @Override
      public void onSuccess(BaseResponse baseResponse) {
        if (imgUri != null) {
          Picasso.with(MineActivity.this)
            .load(imgUri).centerCrop().resize(targetWidth, targetHeight).into(iconIv);
        } else if (!TextUtils.isEmpty(imgUrl)) {
          imageLoader.loadImage(iconIv, imgUrl, targetWidth, targetHeight, R.drawable.brand_icon_default);
        }

        userService.updateAvatarInfo(imgUrl);

        EventBus.getDefault().post(new AvatarUpdateCallback() {
          @Override
          public String updatedAvatar() {
            return imgUrl;
          }
        });
      }
    });
  }

  @Override
  public void onUpLoadImageFailed() {

  }
}
