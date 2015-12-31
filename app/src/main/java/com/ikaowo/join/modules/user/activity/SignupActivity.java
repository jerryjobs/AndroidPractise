package com.ikaowo.join.modules.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.AddBrandCallback;
import com.ikaowo.join.eventbus.ChooseBrandCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.request.SignupRequest;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.modules.user.helper.InputFiledHelper;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.ikaowo.join.util.SharedPreferenceHelper;
import com.ikaowo.join.util.VerifyCodeHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-11.
 */
public class SignupActivity extends BaseEventBusActivity
  implements TextWatcher, PhotoService.UploadFinishListener, DeletableEditTextView.TextChangeListener {
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
  @Bind(R.id.verify_btn)
  TextView getVerifyBtn;
  @Bind(R.id.password)
  DeletableEditTextView passwordEt;

  private TextView brandNameEt;
  private EditText userNameEt;
  private EditText userTitleEt;
  private ImageView userCardIv;

  private UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
  private InputFiledHelper inputHelper = new InputFiledHelper();
  private PhotoService photoService = new PhotoService(SignupActivity.this);
  private VerifyCodeHelper verifyCodeHelper;
  private SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance();

  private String userName;
  private String userTitle;
  private String userCardUrl;
  private String phone;
  private String verifyCode;
  private String password;
  private Uri imageUri;
  private Brand choosedBrand;
  private QiniuUploadHelper qiniuUploadHelper;

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


  private void setupView() {
    phoneEt.addTextChangedListener(this);
    verifyCodeEt.addTextChangedListener(this);
    passwordEt.addTextChangedListener(this);
    passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    verifyCodeHelper = new VerifyCodeHelper(this, phoneEt, getVerifyBtn);
    verifyCodeHelper.initVerifyBtn();

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
        if (imageUri != null) {
          photoService.viewPhoto(SignupActivity.this, imageUri);
        } else if (!TextUtils.isEmpty(userCardUrl)) {
          photoService.viewPhoto(SignupActivity.this, userCardUrl);
        } else {
          photoService.takePhoto(SignupActivity.this, toolbar, null, true);
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
    photoService.takePhoto(SignupActivity.this, toolbar, null, true);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean brandNameInputed = choosedBrand != null;
    userName = userNameEt.getText().toString().trim();
    userTitle = userTitleEt.getText().toString().trim();
    phone = phoneEt.getText().toString().trim();
    password = passwordEt.getText().toString().trim();
    verifyCode = verifyCodeEt.getText().toString().trim();
    password = passwordEt.getText().toString().trim();

    boolean userNameInputed = !TextUtils.isEmpty(userName);
    boolean userTitleInputed = !TextUtils.isEmpty(userTitle);
    boolean userCardInputed = !TextUtils.isEmpty(userCardUrl);
    boolean userPhoneInputed = !TextUtils.isEmpty(phone);
    boolean userPaswdInputed = !TextUtils.isEmpty(password);
    boolean verifyInputed = !TextUtils.isEmpty(verifyCode);
    boolean passwordInputed = !TextUtils.isEmpty(password) && password.length() >= 6;
    menu.getItem(0).setEnabled(brandNameInputed && userNameInputed && userTitleInputed
      && userCardInputed && userPhoneInputed
      && userPaswdInputed && verifyInputed && passwordInputed);
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

  private void submit() {
    UserInterface userNetworkService
      = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    SignupRequest request = new SignupRequest();
    if (choosedBrand.company_id > 0) {
      request.company_id = choosedBrand.company_id;
    } else {
      BrandInfo brandInfo = new BrandInfo();
      brandInfo.brand_logo = choosedBrand.brand_logo;
      brandInfo.brand_name = choosedBrand.brand_name;
      brandInfo.company_icon = choosedBrand.company_icon; //营业执照
      request.brand_info = brandInfo;
    }
    request.nickname = userName;
    request.position = userTitle;
    request.tumblr_icon = userCardUrl;
    request.phone = phone;
    request.vcode = verifyCode;
    request.password = password;
    Call<SignupResponse> call = userNetworkService.signup(request);
    call.enqueue(new NetworkCallback<SignupResponse>(SignupActivity.this) {
      @Override
      public void onSuccess(SignupResponse signupResponse) {
        // save the user info into the preference.
        sharedPreferenceHelper.saveUser(signupResponse.data);
        // the the signin page
        EventBus.getDefault().post(new ClosePageCallback() {
          @Override
          public boolean close() {
            return true;
          }
        });

        finish();
      }
    });
  }

  @Override
  protected String getTag() {
    return "SingupActivity";
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }


  @Override
  public void onUpLoadImageFinish(String url, Uri imgUri) {
    int width = JApplication.getJContext().dip2px(48);
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

    String phone = phoneEt.getText().toString().trim();
    if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
      if (!getVerifyBtn.isEnabled()) {
        getVerifyBtn.setEnabled(true);
      }
    } else {
      if (getVerifyBtn.isEnabled()) {
        getVerifyBtn.setEnabled(false);
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
}
