package com.ikaowo.join.modules.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.AddBrandCallback;
import com.ikaowo.join.eventbus.ChooseBrandCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.modules.user.helper.InputFiledHelper;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.ikaowo.join.util.VerifyCodeHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-11.
 */
public abstract class BaseUserInputActivity extends BaseEventBusActivity
  implements TextWatcher, PhotoService.UploadFinishListener, DeletableEditTextView.TextChangeListener {

  protected UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
  protected InputFiledHelper inputHelper = new InputFiledHelper();
  protected PhotoService photoService = new PhotoService(BaseUserInputActivity.this);
  protected VerifyCodeHelper verifyCodeHelper;
  protected QiniuUploadHelper qiniuUploadHelper;

  @Bind(R.id.divider)
  View divider;
  @Bind(R.id.brand_name)
  CustomEditTextView brandNameTv;
  @Bind(R.id.user_name)
  CustomEditTextView userNameTv;
  @Bind(R.id.user_title)
  CustomEditTextView userTitleTv;
  @Bind(R.id.user_card)
  CustomEditTextView userCardTv;

  @Bind(R.id.phone_viewstub)
  ViewStub phoneViewStub;

  PhoneViewHolder phoneViewHoder;

  TextView brandNameEt;
  EditText userNameEt;
  EditText userTitleEt;
  ImageView userCardIv;

  String userName;
  String userTitle;
  String userCardUrl;
  String phone;
  String verifyCode;
  String password;
  Uri imageUri;
  Brand choosedBrand;

  int width = JApplication.getJContext().dip2px(48);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);

    qiniuUploadHelper = new QiniuUploadHelper();
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    displayHomeAsIndicator(0);

    setupView();
    setupOptionMenu();
  }


  protected void setupView() {

    brandNameTv.setTitle(getString(R.string.brand_name));
    brandNameEt = inputHelper.getTextView(this, R.string.choose_hint);
    brandNameEt.setFocusable(false);
    brandNameEt.setTextColor(ContextCompat.getColor(this, R.color.c1));
    brandNameTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        chooseBrand();
      }
    });
    brandNameTv.addRightView(brandNameEt, 0);

    userNameTv.setTitle(getString(R.string.user_name));
    userNameEt = inputHelper.getEditText(this, R.string.input_hint, this);
    userNameTv.addRightView(userNameEt, R.color.c1);

    userTitleTv.setTitle(getString(R.string.user_title));
    userTitleEt = inputHelper.getEditText(this, R.string.input_hint, this);
    userTitleTv.addRightView(userTitleEt, R.color.c1);

    userCardTv.setTitle(getString(R.string.user_card));
    userCardIv = new ImageView(this);
    userCardIv.setImageResource(R.drawable.register_uppic);

    userCardIv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hideInput(BaseUserInputActivity.this, toolbar);
        if (imageUri != null) {
          photoService.viewPhoto(BaseUserInputActivity.this, imageUri);
        } else if (!TextUtils.isEmpty(userCardUrl)) {
          photoService.viewPhoto(BaseUserInputActivity.this, userCardUrl);
        } else {
          photoService.takePhotoAnySize(BaseUserInputActivity.this, toolbar);
          ;
        }
      }
    });
    userCardTv.addRightView(userCardIv, 0);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  @OnClick(R.id.brand_name)
  public void chooseBrand() {
    userService.chooseBrandList(this);
  }


  @OnClick(R.id.user_card)
  public void takePhoto() {
    hideInput(this, toolbar);
    photoService.takePhotoAnySize(BaseUserInputActivity.this, toolbar);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.getItem(0).setEnabled(prepareOptionMenu());
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_submit:
        submit();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  protected boolean prepareOptionMenu() {
    boolean brandNameInputed = choosedBrand != null;
    userName = userNameEt.getText().toString().trim();
    userTitle = userTitleEt.getText().toString().trim();

    boolean userNameInputed = !TextUtils.isEmpty(userName);
    boolean userTitleInputed = !TextUtils.isEmpty(userTitle);
    boolean userCardInputed = !TextUtils.isEmpty(userCardUrl);


    return brandNameInputed && userNameInputed && userTitleInputed && userCardInputed;

  }

  protected abstract void submit();

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }


  @Override
  public void onUpLoadImageFinish(String url, Uri imgUri) {

    imageUri = imgUri;
    Picasso.with(this)
      .load(imgUri).centerCrop().resize(width, width).into(userCardIv);
    userCardUrl = url;
    invalidateOptionsMenu();
  }

  @Override
  public void onUpLoadImageFailed() {

  }


  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    invalidateOptionsMenu();
    if (phoneViewHoder != null) {
      String phone = phoneViewHoder.phoneEt.getText().toString().trim();
      if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
        if (!phoneViewHoder.getVerifyBtn.isEnabled()) {
          phoneViewHoder.getVerifyBtn.setEnabled(true);
        }
      } else {
        if (phoneViewHoder.getVerifyBtn.isEnabled()) {
          phoneViewHoder.getVerifyBtn.setEnabled(false);
        }
      }
    }
  }

  @Override
  public void onChanged(Editable s) {
    invalidateOptionsMenu();
  }

  public void onEvent(ChooseBrandCallback callback) {
    Brand brand = callback.getChoosedBrand();
    brandNameEt.setText(brand.brand_name);
    choosedBrand = brand;
  }

  public void onEvent(AddBrandCallback callback) {
    Brand brand = callback.addBrand();
    brandNameEt.setText(brand.brand_name);
    choosedBrand = brand;
  }

  class PhoneViewHolder {
    @Bind(R.id.phone)
    DeletableEditTextView phoneEt;
    @Bind(R.id.verify_code)
    DeletableEditTextView verifyCodeEt;
    @Bind(R.id.password)
    DeletableEditTextView passwordEt;

    @Bind(R.id.verify_btn)
    TextView getVerifyBtn;

    public PhoneViewHolder(View phoneView) {
      ButterKnife.bind(this, phoneView);
    }
  }
}
