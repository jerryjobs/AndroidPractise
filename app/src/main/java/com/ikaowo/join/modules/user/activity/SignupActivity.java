package com.ikaowo.join.modules.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.ChooseBrandCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.response.TokenResponse;
import com.ikaowo.join.modules.user.helper.InputFiledHelper;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.QiniuInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;

/**
 * Created by weibo on 15-12-11.
 */
public class SignupActivity extends BaseEventBusActivity
        implements TextWatcher, PhotoService.UploadFinishInterface, DeletableEditTextView.TextChangeListener {
  @Bind(R.id.brand_name)
  CustomEditTextView brandNameTv;
  @Bind(R.id.user_name)
  CustomEditTextView userNameTv;
  @Bind(R.id.user_title)
  CustomEditTextView userTitleTv;
  @Bind(R.id.user_card)
  CustomEditTextView userCardTv;
  @Bind(R.id.phone)
  DeletableEditTextView phoneEt;
  @Bind(R.id.verify_code)
  DeletableEditTextView verifyCodeEt;
  @Bind(R.id.password)
  DeletableEditTextView passwordEt;

  private TextView brandNameEt;
  private EditText userNameEt;
  private EditText userTitleEt;
  private ImageView userCardIv;

  private UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
  private NetworkManager networkManager = JApplication.getNetworkManager();
  private InputFiledHelper inputHelper = new InputFiledHelper();
  private PhotoService photoService = new PhotoService(SignupActivity.this);

  private int brandId;
  private BrandInfo brandInfo;
  private String userName;
  private String userTitle;
  private String userCardUrl;
  private String phone;
  private String verifyCode;
  private String password;
  private String imageServerUrl;
  private Brand choosedBrand;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    setupEditText();
    setOptionMenu();
  }


  private void setupEditText() {
    brandNameTv.setTitle(getString(R.string.brand_name));
    brandNameEt = inputHelper.getTextView(this, R.string.choose_hint);
    brandNameEt.setFocusable(false);
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
    userCardTv.addRightView(userCardIv, 0);
  }

  private void setOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  @OnClick(R.id.brand_name)
  public void chooseBrand() {
    userService.chooseBrandList(this);
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean brandNameInputed = brandInfo != null || brandId > 0;
    boolean userNameInputed = !TextUtils.isEmpty(userNameEt.getText().toString());
    boolean userTitleInputed = !TextUtils.isEmpty(userTitleEt.getText().toString());
    boolean userCardInputed = !TextUtils.isEmpty(userCardUrl);
    boolean userPhoneInputed = !TextUtils.isEmpty(phoneEt.getText().toString());
    boolean userPaswdInputed = !TextUtils.isEmpty(passwordEt.getText().toString());
    boolean verifyInputed = !TextUtils.isEmpty(verifyCodeEt.getText().toString());
    boolean passwordInputed = !TextUtils.isEmpty(passwordEt.getText().toString());
    menu.getItem(0).setEnabled(brandNameInputed && userNameInputed && userTitleInputed
            && userCardInputed && userPhoneInputed
            && userPaswdInputed && verifyInputed && passwordInputed);
    return true;
  }

  @Override
  protected String getTag() {
    return "SingupActivity";
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    QiniuInterface qiniuNetworkService = networkManager.getServiceByClass(QiniuInterface.class);
    Map<String, String> map = new HashMap<>();
    map.put("u_id", "0");
    map.put("file_type", "icon");
    Call<TokenResponse> call = qiniuNetworkService.getQiniuToken(map);
    call.enqueue(new KwMarketNetworkCallback<TokenResponse>() {
      @Override
      public void onSuccess(final TokenResponse tokenResponse) {
        imageServerUrl = tokenResponse.url;
        photoService.onUploadPic(requestCode, resultCode,
                tokenResponse.key, tokenResponse.token, data, SignupActivity.this);
      }
    });
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
  }

  @Override
  public void onImageLoadFinish(String fileName, Uri imgUri) {
    int width = JApplication.getJContext().dip2px(48);
    Picasso.with(this)
            .load(imgUri).centerCrop().resize(width, width).into(userCardIv);
    userCardUrl = imageServerUrl + fileName;
  }

  @Override
  public void onImageLoadFailed() {

  }

  @Override
  public void onChanged(Editable s) {
    invalidateOptionsMenu();
  }

  public void onEvent(ChooseBrandCallback callback) {
    Brand brand = callback.getChoosedBrand();
    brandNameEt.setText(brand.brandName);
    choosedBrand = brand;
  }
}
