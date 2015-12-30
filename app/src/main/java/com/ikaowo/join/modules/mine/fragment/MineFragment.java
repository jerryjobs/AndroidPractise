package com.ikaowo.join.modules.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.ikaowo.join.BaseEventBusFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.model.UserLoginData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-8.
 */
public class MineFragment extends BaseEventBusFragment {

  @Bind(R.id.icon)
  ImageView iconIv;
  @Bind(R.id.name_title)
  TextView nameTitleTv;
  @Bind(R.id.brand_name)
  TextView brandNameTv;

  @Bind(R.id.promption)
  RelativeLayout promptionLayout;

  @Bind(R.id.brand_info)
  RelativeLayout companyLayout;

  @Bind(R.id.abount)
  RelativeLayout aboutLayout;

  private UserService userService;
  private MineService mineService;
  private BrandService brandService;
  private ImageLoader imageLoader;
  private int targetImgWidth, targetImgHeight;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    mineService = JApplication.getJContext().getServiceByInterface(MineService.class);
    brandService = JApplication.getJContext().getServiceByInterface(BrandService.class);
    imageLoader = JApplication.getImageLoader();

    targetImgWidth = targetImgHeight = JApplication.getJContext().dip2px(64);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_mine, null, false);
    ButterKnife.bind(this, view);

    setupData();

    setupView();
    return view;
  }

  private void setupData() {
    if (userService.isLogined()) {
      UserLoginData user = userService.getUser();
      imageLoader.loadImage(iconIv, user.icon, targetImgWidth, targetImgHeight, R.drawable.brand_icon_default);
      nameTitleTv.setText(user.nickName + " | " + user.title);
      brandNameTv.setText(user.brandInfo != null ? user.brandInfo.company_name : "");
    }
  }

  private void setupView() {
    setupItem(promptionLayout, R.drawable.mine_ic_activity, "我的推广");
    setupItem(companyLayout, R.drawable.mine_ic_mybrand, "我的品牌");
    setupItem(aboutLayout, R.drawable.mine_ic_about, "关于我们");
  }

  private void setupItem(View parentView, int iconId, String desc) {
    ImageView imageView = (ImageView)parentView.findViewById(R.id.icon);
    promptionLayout.findViewById(R.id.desc);
    imageView.setImageResource(iconId);

    TextView tv = (TextView) parentView.findViewById(R.id.desc);
    tv.setText(desc);
  }

  @OnClick(R.id.user_info)
  public void viewUserInfo() {
    mineService.viewUserInfo(getActivity());
  }

  @OnClick(R.id.promption)
  public void viewMyPromption() {
    mineService.viewMyPromption(getActivity());
  }

  @OnClick(R.id.brand_info)
  public void viewBrandInfo() {
    brandService.viewBrandDetail(getActivity(), userService.getUserCompanyId());
  }

  public void onEvent(SigninCallback callback) {
    if (callback.singined()) {
      setupData();
    }
  }

  @Override
  public String getPageName() {
    return "ME";
  }
}
