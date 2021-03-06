package com.ikaowo.join.modules.promption.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListFragment;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

/**
 * 这个basefragment现在由tab里面的推广列表以及推广搜索结果页面公用。
 * TODO 后期如果搜索结果页面发生变化，那么需要把这个页面跟搜索结果页面剥离
 * Created by weibo on 15-12-8.
 */
public abstract class BasePromptionFragment
    extends BaseListFragment<BaseListResponse<Promption>, Promption> {

  protected PromptionInterface promptionInterface;

  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private WebViewService webViewService;
  private UserService userService;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    imageLoader = JApplication.getImageLoader();
    promptionInterface =
        JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);

    webViewService = JApplication.getJContext().getServiceByInterface(WebViewService.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    targetImgBgWidth =
        JApplication.getJContext().getScreenWidth() - 2 * JApplication.getJContext().dip2px(12);
    targetImgBgHeight = targetImgBgWidth * 9 / 16;
  }

  @Override protected boolean isSupportLoadMore() {
    return true;
  }

  @Override protected void performCustomItemClick(Promption promption) {
    String url = BuildConfig.PROMPTION_URL + promption.id;

    if (TextUtils.isEmpty(promption.background)) {
      if (url.indexOf("?") > 0) {
        url += "&iconurl=" + promption.background;
      } else {
        url += "?iconurl=" + promption.background;
      }
    }
    WebViewService.WebViewRequest webViewRequest = new WebViewService.WebViewRequest();
    webViewRequest.url = url;
    webViewService.viewPromptionDetail(getActivity(), promption.id, webViewRequest);
  }

  @Override protected JAdapter<Promption> getAdapter(
      RecyclerViewHelper<BaseListResponse<Promption>, Promption> recyclerViewHelper) {
    return new PromptionListAdapter(recyclerViewHelper);
  }

  @Override protected String getEmptyHint() {
    return getResources().getString(R.string.empty_hint_promption);
  }

  class PromptionListAdapter extends JAdapter<Promption> {

    private RecyclerViewHelper helper;

    public PromptionListAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_promption_list, null);
      RecyclerView.ViewHolder viewHolder = new PromptionListViewHolder(view, helper);
      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof PromptionListViewHolder) {
        int screenWidth = JApplication.getJContext().getScreenWidth();

        PromptionListViewHolder viewHolder = (PromptionListViewHolder) holder;
        Promption promption = objList.get(position);
        viewHolder.companyNameTv.setSingleLine();
        viewHolder.companyNameTv.setMaxWidth(screenWidth * 3 / 10);
        viewHolder.companyNameTv.setText(promption.brandName);

        viewHolder.promptionAddressTv.setMaxWidth(screenWidth * 3 / 10);
        viewHolder.promptionAddressTv.setText(promption.address);
        viewHolder.promptionAddressTv.setSingleLine();

        viewHolder.promptionTimeTv.setMaxWidth(screenWidth * 3 / 10);
        viewHolder.promptionTimeTv.setText(promption.date);
        viewHolder.promptionTimeTv.setSingleLine();

        viewHolder.promptionTitleTv.setText(promption.title);
        viewHolder.promptionTitleTv.setSingleLine();

        if (Constant.PROMPTION_STATE_PASS.equalsIgnoreCase(promption.state)) {
          viewHolder.promptionStateIv.setVisibility(View.VISIBLE);
          viewHolder.promptionStateIv.setImageResource(R.drawable.content_ic_join_ing);
        } else if (Constant.PROMPTION_STATE_OVER.equals(promption.state)) {
          viewHolder.promptionStateIv.setVisibility(View.VISIBLE);
          viewHolder.promptionStateIv.setImageResource(R.drawable.content_ic_join_finish);
        } else if (Constant.PROMPTION_STATE_DONE.equalsIgnoreCase(promption.state)) {
          viewHolder.promptionStateIv.setVisibility(View.VISIBLE);
          viewHolder.promptionStateIv.setImageResource(R.drawable.content_ic_join_complete);
        }

        imageLoader.loadImage(viewHolder.promptionBgImg, promption.background, targetImgBgWidth,
            targetImgBgHeight, R.drawable.brand_icon_default);
        viewHolder.promptionContentTv.setText(promption.content);
      }
    }
  }

  class PromptionListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.company_name) TextView companyNameTv;
    @Bind(R.id.promption_address) TextView promptionAddressTv;
    @Bind(R.id.promption_time) TextView promptionTimeTv;
    @Bind(R.id.state) ImageView promptionStateIv;
    @Bind(R.id.promption_bg) ImageView promptionBgImg;
    @Bind(R.id.promption_title) TextView promptionTitleTv;
    @Bind(R.id.promption_content) TextView promptionContentTv;

    public PromptionListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) promptionBgImg.getLayoutParams();
      llp.width = targetImgBgWidth;
      llp.height = targetImgBgHeight;

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          recyclerViewHelper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
