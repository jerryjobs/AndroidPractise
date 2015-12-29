package com.ikaowo.join.modules.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.UserLoginData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-8.
 */
public class MineFragment extends BaseFragment {

  @Bind(R.id.icon)
  ImageView iconIv;
  @Bind(R.id.name_title)
  TextView nameTitleTv;
  @Bind(R.id.brand_name)
  TextView brandNameTv;

  @Bind(R.id.promption)
  RelativeLayout promptionLayout;

  @Bind(R.id.company)
  RelativeLayout companyLayout;

  @Bind(R.id.abount)
  RelativeLayout aboutLayout;

  private UserService userService;
  private MineService mineService;
  private ImageLoader imageLoader;
  private int targetImgWidth, targetImgHeight;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    mineService = JApplication.getJContext().getServiceByInterface(MineService.class);
    imageLoader = JApplication.getImageLoader();

    targetImgWidth = targetImgHeight = JApplication.getJContext().dip2px(64);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_mine, null, false);
    ButterKnife.bind(this, view);

    if (userService.isLogined()) {
      UserLoginData user = userService.getUser();
      imageLoader.loadImage(iconIv, user.icon, targetImgWidth, targetImgHeight, R.drawable.brand_icon_default);
      nameTitleTv.setText(user.nickName + " | " + user.title);
      brandNameTv.setText(user.brandInfo != null ? user.brandInfo.company_name : "");
    }

    setupView();
    return view;
  }

  private void setupView() {
    setupItem(promptionLayout, R.drawable.mine_ic_activity, "我的活动");
    setupItem(companyLayout, R.drawable.mine_ic_mybrand, "我的公司");
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

  @Override
  public String getPageName() {
    return "ME";
  }
}
