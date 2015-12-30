package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.model.JoinInfo;
import com.ikaowo.join.model.request.JoinInfoRequest;
import com.ikaowo.join.model.request.JoinRequest;
import com.ikaowo.join.model.response.JoinInfoResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;

/**
 * Created by leiweibo on 12/30/15.
 */
public class JoinDetailActivity extends BaseActivity {

  private int promptionId;
  private int brandId;

  private PromptionInterface promptionInterface =
    JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);

  private ImageLoader imageLoader = JApplication.getImageLoader();

  private int brandIconTargetWidth = JApplication.getJContext().dip2px(64);
  private int brandIconTargetHeight = JApplication.getJContext().dip2px(48);

  private int userIconTargetWidth =  JApplication.getJContext().dip2px(64);
  private int userIconTargetHeight =  JApplication.getJContext().dip2px(64);

  private int tumblrWidth = JApplication.getJContext().dip2px(100);
  private int tumblrHeight = JApplication.getJContext().dip2px(100);

  private int margin = JApplication.getJContext().dip2px(12);

  @Bind(R.id.content)
  TextView contentTv;

  @Bind(R.id.tumblrs_layout)
  LinearLayout tumblrsLayout;

  @Bind(R.id.brand_logo)
  ImageView brandLogo;

  @Bind(R.id.brand_name)
  TextView brandName;

  @Bind(R.id.user_icon)
  ImageView iconIv;

  @Bind(R.id.name_title)
  TextView nameTitleTv;



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join_detail);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.title_activity_join_detail);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    getIntentData();
    getJoinInfoData();
  }

  private void getIntentData() {
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      brandId = bundle.getInt(Constant.BRAND_ID, 0);
      promptionId = bundle.getInt(Constant.PROMPTION_ID, 0);
    }

    if (brandId <= 0 || promptionId <= 0) {
      showConfirmDialog("注意", "报名详情数据不存在，请返回重试");  }
  }

  private void getJoinInfoData() {
    JoinInfoRequest request = new JoinInfoRequest();
    request.aci_id = promptionId;
    request.company_id = brandId;
    Call<JoinInfoResponse> call = promptionInterface.getJoinInfo(request.getMap());
    JApplication.getNetworkManager().async(this, Constant.DATAGETTING, call,
      new KwMarketNetworkCallback<JoinInfoResponse>(this) {
      @Override
      public void onSuccess(JoinInfoResponse joinInfoResponse) {

        JoinInfo joinInfo = joinInfoResponse.data;
        if (joinInfo == null) {
          return;
        }
        contentTv.setText(joinInfo.extra);
        List<String> tumblrs = joinInfo.tumblrs;
        int tumblrsSize = tumblrs.size();
        for (int i = 0; i < tumblrsSize; i++) {
          ImageView imageView = new ImageView(JoinDetailActivity.this);
          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
          String tumblrUrl = tumblrs.get(i);
          imageLoader.loadImage(imageView, tumblrUrl,
            tumblrWidth, tumblrWidth, R.drawable.brand_icon_default);
          LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(tumblrWidth, tumblrHeight);
          llp.leftMargin = margin;
          llp.topMargin = margin;
          if (i == tumblrsSize - 1) {
            llp.rightMargin = margin;
          } else {
            llp.rightMargin = 0;
          }
          llp.bottomMargin = margin;
          tumblrsLayout.addView(imageView, llp);
        }
        imageLoader.loadImage(brandLogo, joinInfo.brandLogo,
          brandIconTargetWidth, brandIconTargetHeight, R.drawable.brand_icon_default);
        brandName.setText(joinInfo.brandName);

        imageLoader.loadImage(iconIv, joinInfo.userIcon,
          userIconTargetWidth, userIconTargetHeight, R.drawable.brand_icon_default);
        nameTitleTv.setText(joinInfo.nickname + " | " + joinInfo.title);
      }
    });
  }

  @Override
  protected String getTag() {
    return "JoinDetailActivity";
  }
}
