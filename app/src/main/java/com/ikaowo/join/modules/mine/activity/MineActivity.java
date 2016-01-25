package com.ikaowo.join.modules.mine.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkManager;
import com.component.photo.FullImageView;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.AvatarUpdateCallback;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.CheckStateRequest;
import com.ikaowo.join.model.request.UpdateAvatarRequest;
import com.ikaowo.join.model.response.CheckStateResponse;
import com.ikaowo.join.modules.mine.MineItemWidget;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.AvatarHelper;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.ikaowo.join.util.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.Map;
import retrofit.Call;

/**
 * Created by weibo on 15-12-29.
 */
public class MineActivity extends BaseEventBusFragmentActivity
    implements PhotoService.UploadFinishListener {

  @Bind(R.id.auth_state) MineItemWidget authStateItem;

  @Bind(R.id.user_icon) MineItemWidget userIconItem;

  @Bind(R.id.user_name) MineItemWidget userNameItem;

  @Bind(R.id.brand_name) MineItemWidget userBrandNameItem;

  @Bind(R.id.user_title) MineItemWidget userTitleItem;

  @Bind(R.id.user_phone) MineItemWidget userPhoneItem;

  private UserService userService;
  private MineService mineService;
  private PhotoService photoService;
  private ImageLoader imageLoader;
  private QiniuUploadHelper qiniuUploadHelper;
  private Map<String, Integer> stateColorMap = new HashMap<>();
  private int targetWidth = JApplication.getJContext().dip2px(64);
  private int targetHeight = JApplication.getJContext().dip2px(64);

  private FullImageView imageView;
  private TextView shortNameTv;
  private Dialog dialog = null;

  @Override public void onCreate(Bundle savedInstanceState) {
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
    setupOptionMenu();
  }

  private void setupDate() {
    UserLoginData user = userService.getUser();
    if (user != null) {
      boolean authed = Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.state)
          && Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.companyState);
      boolean failed = Constant.AUTH_STATE_FAILED.equalsIgnoreCase(user.state)
          || Constant.AUTH_STATE_FAILED.equalsIgnoreCase(user.companyState);
      boolean processing = Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(user.state)
          || Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(user.companyState);

      Map<String, String> stateDescMap = SharedPreferenceHelper.getInstance().getEnumValue(this);
      String state = "";
      if (stateDescMap != null) {
        if (authed) {
          state = stateDescMap.get(Constant.USER_STATE_PREFIX + Constant.AUTH_STATE_PASSED);
        } else if (failed) {
          state = stateDescMap.get(Constant.USER_STATE_PREFIX + Constant.AUTH_STATE_FAILED);
        } else if (processing) {
          state =
              stateDescMap.get(Constant.USER_STATE_PREFIX + Constant.AUTH_STATE_PENDING_APPROVE);
        }
      } else {
        if (authed) {
          state = "已认证";
        } else if (failed) {
          state = "认证失败";
        } else if (processing) {
          state = "认证中";
        }
      }

      authStateItem.setText(state);
      authStateItem.setTextColor(stateColorMap.get(user.state));
      imageView = userIconItem.getImageView();
      shortNameTv = userIconItem.getShortNameTv();
      if (shortNameTv != null) {
        shortNameTv.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            photoService.takePhoto(MineActivity.this, toolbar, null);
          }
        });
      }
      if (imageView != null) {
        imageView.setImgUrl(user.icon);
        AvatarHelper.getInstance()
            .showAvatar(MineActivity.this, user.uId, imageView, userIconItem.getShortNameTv(), targetWidth,
                targetHeight, user.icon, user.nickName);
      }

      userNameItem.setText(user.nickName);
      userBrandNameItem.setText(user.brandInfo != null ? user.brandInfo.brand_name : "");
      userTitleItem.setText(user.title);
      userPhoneItem.setText(user.phone);

      if (failed) {
        new Handler().post(new Runnable() {
          @Override public void run() {
            dialog = dialogHelper.createDialog(MineActivity.this, "认证未通过", "认证没有通过，可以修改重新认证",
                new String[] { "取消", "前往修改" }, new View.OnClickListener[] {
                    new View.OnClickListener() {
                      @Override public void onClick(View v) {
                        dialog.dismiss();
                      }
                    }, new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    userService.reSubmitInfo(MineActivity.this);
                    dialog.dismiss();
                  }
                }
                });
            dialog.show();
          }
        });
      }
    }
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_resubmit_userinfo;
    invalidateOptionsMenu();
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    if (userService.isAuthFailed()) {
      menu.getItem(0).setVisible(true);
    } else {
      menu.getItem(0).setVisible(false);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.resubmit:
        userService.reSubmitInfo(MineActivity.this);
        break;
      default:
        super.onOptionsItemSelected(item);
    }
    return true;
  }

  @OnClick(R.id.user_icon) public void updateIcon() {
    photoService.takePhoto(this, toolbar, null);
  }

  @OnClick(R.id.user_update_passwd) public void updatePasswd() {
    mineService.updatePassword(this);
  }

  @OnClick((R.id.logout)) public void logout() {
    userService.logout(this);
  }

  @Override protected String getTag() {
    return "MineActivity";
  }

  public void onEvent(ClosePageCallback callback) {
    if (callback.close()) {
      finish();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }

  @Override public void onUpLoadImageFinish(final String imgUrl, final Uri imgUri) {
    if (imageView == null) {
      return;
    }
    imageView.setImgUrl(imgUrl);
    imageView.setImgUri(imgUri);
    NetworkManager networkManager = JApplication.getNetworkManager();
    UserInterface userInterface = networkManager.getServiceByClass(UserInterface.class);
    UpdateAvatarRequest request = new UpdateAvatarRequest();
    request.icon = imgUrl;
    Call<BaseResponse> call = userInterface.updateAvatar(request);
    networkManager.async(this, Constant.PROCESSING, call,
        new KwMarketNetworkCallback<BaseResponse>(this) {

          @Override public void onSuccess(BaseResponse baseResponse) {

            if (shortNameTv != null) {
              shortNameTv.setVisibility(View.GONE);
            }
            if (imgUri != null) {
              Picasso.with(MineActivity.this)
                  .load(imgUri)
                  .centerCrop()
                  .resize(targetWidth, targetHeight)
                  .into(imageView);
            } else if (!TextUtils.isEmpty(imgUrl)) {
              imageLoader.loadImage(imageView, imgUrl, targetWidth, targetHeight,
                  R.drawable.brand_icon_default);
            }

            userService.updateAvatarInfo(imgUrl);

            EventBus.getDefault().post(new AvatarUpdateCallback() {
              @Override public String updatedAvatar() {
                return imgUrl;
              }
            });
          }
        });
  }

  @Override public void onUpLoadImageFailed() {

  }
}
