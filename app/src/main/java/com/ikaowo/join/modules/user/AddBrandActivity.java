package com.ikaowo.join.modules.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.QiniuInterface;
import com.ikaowo.join.network.bean.response.TokenResponse;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;

/**
 * Created by weibo on 15-12-16.
 */
public class AddBrandActivity extends BaseActivity implements PhotoService.UploadFinishInterface {
    @Bind(R.id.brand_icon)
    ImageView brandIconIv;
    @Bind(R.id.brand_name)
    CustomEditTextView brandNameTv;
    @Bind(R.id.brand_licence)
    CustomEditTextView brandLicenceTv;

    private PhotoService photoService = new PhotoService(AddBrandActivity.this);
    private NetworkManager networkManager = JApplication.getNetworkManager();

    private ImageView licenceIv;
    private Uri licenceImgUri, iconImgUri;
    private ClickPos clickPos;
    private String brandName;

    public enum ClickPos {
        BRAND_ICON, BRAND_LICENCE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupEditText();
        setOptionMenu();
        displayHomeAsIndicator(R.drawable.nav_ic_close_white);

    }

    private void setupEditText() {
        brandNameTv.setTitle("品牌名称");
        EditText editText = new EditText(this);
        editText.setTextSize(14);
        editText.setHint("请输入");
        editText.addTextChangedListener(new TextWatcher() {
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
        //editText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        brandNameTv.addRightView(editText, R.color.c1);

        licenceIv = new ImageView(this);
        licenceIv.setImageResource(R.drawable.register_uppic);
        licenceIv.setOnClickListener(new View.OnClickListener() {
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
        brandLicenceTv.setTitle("营业执照");
        brandLicenceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput(AddBrandActivity.this, toolbar);
                clickPos = ClickPos.BRAND_LICENCE;
                photoService.takePhoto(AddBrandActivity.this, toolbar, null, true);
            }
        });
        brandLicenceTv.addRightView(licenceIv, 0);
    }

    private void setOptionMenu() {
        menuResId = R.menu.menu_add_brand;
        invalidateOptionsMenu();
    }

    @OnClick(R.id.brand_icon)
    public void onBrandIcon(){
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
    protected String getTag() {
        return "AddBrand";
    }

    private int width, height;
    private ImageView targetIv;
    private String imageServerUrl;
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
                        tokenResponse.key, tokenResponse.token, data, AddBrandActivity.this);
            }
        });
    }


    @Override
    public void onImageLoadFinish(String fileName, Uri imgUri) {
        if (clickPos != null) {
            invalidateOptionsMenu();
            switch (clickPos) {
                case BRAND_LICENCE:
                    targetIv = licenceIv;
                    licenceIv.setTag(imageServerUrl + fileName);
                    width = JApplication.getJContext().dip2px(48);
                    height = JApplication.getJContext().dip2px(48);
                    licenceImgUri = imgUri;
                    break;

                case BRAND_ICON:
                    targetIv = brandIconIv;
                    brandIconIv.setTag(imageServerUrl + fileName);
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
    public void onImageLoadFailed() {
        Log.e(getTag(), "failed");
    }
}
