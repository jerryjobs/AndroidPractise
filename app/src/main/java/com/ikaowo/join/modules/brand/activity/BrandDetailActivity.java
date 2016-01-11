package com.ikaowo.join.modules.brand.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.BaseEventBusFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.response.BrandResponse;
import com.ikaowo.join.modules.brand.adapter.BrandPromptionTabLayoutAdapter;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.util.Constant;
import java.util.List;
import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class BrandDetailActivity extends BaseEventBusFragmentActivity {

  @Bind(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
  @Bind(R.id.brand_icon) ImageView brandIcon;
  @Bind(R.id.brand_name) TextView brandName;
  @Bind(R.id.company_name) TextView companyName;
  @Bind(R.id.tabs) TabLayout slidingTabs;
  @Bind(R.id.viewpager) ViewPager viewpager;

  private int brandId;
  private String[] titlesArray;
  private int targetHeight, targetWidth;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brand_detail);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_brand_detail);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    try {
      List<String> pathSegments = getIntent().getData().getPathSegments();
      if (pathSegments != null && pathSegments.size() > 0) {
        brandId = Integer.valueOf(pathSegments.get(pathSegments.size() - 1));
      }
    } catch (Exception e) {

    }
    if (brandId <= 0) {
      Bundle bundle = getIntent().getExtras();
      if (bundle != null) {
        brandId = bundle.getInt(Constant.BRAND_ID, 0);
      }
    }

    if (brandId <= 0) {
      dialogHelper.createDialog(this, "注意", "品牌信息不正确，请返回重试", new String[] { "确定" },
          new View.OnClickListener[] {
              new View.OnClickListener() {
                @Override public void onClick(View v) {
                  finish();
                }
              }
          }).show();
    }

    targetWidth = JApplication.getJContext().dip2px(120);
    targetHeight = JApplication.getJContext().dip2px(90);

    setupView();
    getBrandInfo();
  }

  private void setupView() {
    collapsingToolbarLayout.setTitleEnabled(false);

    titlesArray = new String[3];
    titlesArray[0] = getString(R.string.tab_title_join_promption, 0);
    titlesArray[1] = getString(R.string.tab_title_posted_promption, 0);
    titlesArray[2] = getString(R.string.tab_title_member, 0);

    viewpager.setOffscreenPageLimit(3);
    viewpager.setAdapter(
        new BrandPromptionTabLayoutAdapter(getSupportFragmentManager(), titlesArray, brandId));
    slidingTabs.setupWithViewPager(viewpager);
  }

  private void getBrandInfo() {
    NetworkManager networkManager = JApplication.getNetworkManager();
    final ImageLoader imageLoader = JApplication.getImageLoader();

    BrandInterface brandInterface = networkManager.getServiceByClass(BrandInterface.class);
    Call<BrandResponse> call = brandInterface.getBrandInfo(brandId);
    networkManager.async(this, Constant.DATAGETTING, call,
        new KwMarketNetworkCallback<BrandResponse>(this) {
          @Override public void onSuccess(BrandResponse brandResponse) {
            Brand brand = brandResponse.data;
            if (brand != null) {
              imageLoader.loadImage(brandIcon, brand.brand_logo, targetWidth, targetHeight,
                  R.drawable.brand_icon_default);
              brandName.setText(brand.brand_name);
              companyName.setText(brand.company_name);
            }
          }
        });
  }

  public void onEvent(GetListCountCallback callback) {
    GetListCountCallback.MapObj map = callback.getCountMap();
    String title = null;
    if (map != null) {
      TabLayout.Tab tab = slidingTabs.getTabAt(map.index);
      if (tab != null) {
        switch (map.index) {
          case 0:
            title = getString(R.string.tab_title_join_promption, map.count);
            break;

          case 1:
            title = getString(R.string.tab_title_posted_promption, map.count);
            break;

          case 2:
            title = getString(R.string.tab_title_member, map.count);
            break;
        }
        slidingTabs.getTabAt(map.index).setText(title);
      }
    }
  }

  @Override protected String getTag() {
    return "BrandDetailActivity";
  }
}
