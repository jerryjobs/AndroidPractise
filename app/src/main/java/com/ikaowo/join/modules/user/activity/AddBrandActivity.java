package com.ikaowo.join.modules.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.eventbus.AddBrandCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.modules.user.helper.InputFiledHelper;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-16.
 */
public class AddBrandActivity extends BaseActivity implements PhotoService.UploadFinishListener {
  @Bind(R.id.brand_icon)
  ImageView brandIconIv;
  @Bind(R.id.brand_name)
  CustomEditTextView brandNameTv;
  @Bind(R.id.brand_licence)
  CustomEditTextView brandLicenceTv;

  private PhotoService photoService = new PhotoService(AddBrandActivity.this);
  private InputFiledHelper inputHelper = new InputFiledHelper();

  private ImageView brandLicenceIv;
  private EditText brandNameEt;
  private Uri licenceImgUri, iconImgUri;
  private ClickPos clickPos;
  private String brandName;
  private int width, height;
  private ImageView targetIv;
  private QiniuUploadHelper qiniuUploadHelper;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_branch);
    ButterKnife.bind(this);

    qiniuUploadHelper = new QiniuUploadHelper();
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupEditText();
    setupOptionMenu();
    displayHomeAsIndicator(R.drawable.nav_ic_close_white);

  }

  private void setupEditText() {
    brandNameTv.setTitle(getString(R.string.brand_name));
    brandNameEt = inputHelper.getEditText(this, R.string.input_hint, new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        brandName = s.toString();
        invalidateOptionsMenu();
      }
    });
    brandNameTv.addRightView(brandNameEt, R.color.c1);

    brandLicenceIv = new ImageView(this);
    brandLicenceIv.setImageResource(R.drawable.register_uppic);
    brandLicenceIv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hideInput(AddBrandActivity.this, toolbar);
        if (licenceImgUri != null) {
          photoService.viewPhoto(AddBrandActivity.this, licenceImgUri);
        } else {
          clickPos = ClickPos.BRAND_LICENCE;
          photoService.takePhoto(AddBrandActivity.this, toolbar, null, true);
        }
      }
    });
    brandLicenceTv.setTitle(getString(R.string.licence));
    brandLicenceTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hideInput(AddBrandActivity.this, toolbar);
        clickPos = ClickPos.BRAND_LICENCE;
        photoService.takePhoto(AddBrandActivity.this, toolbar, null, true);
      }
    });
    brandLicenceTv.addRightView(brandLicenceIv, 0);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  @OnClick(R.id.brand_icon)
  public void onBrandIcon() {
    hideInput(this, toolbar);
    clickPos = ClickPos.BRAND_ICON;
    photoService.takePhoto(AddBrandActivity.this, toolbar, null, false, 4, 3);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.getItem(0).setEnabled(!TextUtils.isEmpty(brandName) && iconImgUri != null && licenceImgUri != null);
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
    final Brand brand = new Brand();
    brand.brand_name = brandName;
    brand.company_icon = brandLicenceIv.getTag().toString();
    brand.brand_logo = brandIconIv.getTag().toString();

    EventBus.getDefault().post(new AddBrandCallback() {
      @Override
      public Brand addBrand() {
        return brand;
      }
    });


    EventBus.getDefault().post(new ClosePageCallback() {
      @Override
      public boolean close() {
        return true;
      }
    });
    finish();
  }

  @Override
  protected String getTag() {
    return "AddBrand";
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }

  @Override
  public void onUpLoadImageFinish(String url, Uri imgUri) {
    if (clickPos != null) {
      invalidateOptionsMenu();
      switch (clickPos) {
        case BRAND_LICENCE:
          targetIv = brandLicenceIv;
          brandLicenceIv.setTag(url);
          width = JApplication.getJContext().dip2px(48);
          height = JApplication.getJContext().dip2px(48);
          licenceImgUri = imgUri;
          break;

        case BRAND_ICON:
          targetIv = brandIconIv;
          brandIconIv.setTag(url);
          width = JApplication.getJContext().dip2px(100);
          height = JApplication.getJContext().dip2px(75);
          iconImgUri = imgUri;
          break;
      }
      Picasso.with(this)
              .load(imgUri).centerCrop().resize(width, height).into(targetIv);

    }
  }

  @Override
  public void onUpLoadImageFailed() {
    Log.e(getTag(), "failed");
  }

  public enum ClickPos {
    BRAND_ICON, BRAND_LICENCE
  }
}
