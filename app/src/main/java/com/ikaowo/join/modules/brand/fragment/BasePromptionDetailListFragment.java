package com.ikaowo.join.modules.brand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListFragment;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-28.
 */
public abstract class BasePromptionDetailListFragment extends BaseListFragment<BaseListResponse<Promption>, Promption> {

  protected PromptionInterface promptionInterface;
  protected int brandId;

  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private WebViewService webViewService;
  private UserService userService;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    imageLoader = JApplication.getImageLoader();
    promptionInterface = JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);

    webViewService = JApplication.getJContext().getServiceByInterface(WebViewService.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    targetImgBgWidth
            = JApplication.getJContext().dip2px(120);
    targetImgBgHeight = targetImgBgWidth * 9 / 16;

    Bundle bundle = getArguments();
    if (bundle != null) {
      brandId = bundle.getInt(Constant.BRAND_ID, 0);
    }
  }

  @Override
  protected boolean isSupportLoadMore() {
    return true;
  }

  @Override
  protected void performCustomItemClick(Promption promption) {
    String url = BuildConfig.PROMPTION_URL + promption.id;

    if (promption.companyId > 0) {
      url += "?companyid=" + promption.companyId;
    }
    if (TextUtils.isEmpty(promption.background)) {
      if (url.indexOf("?") > 0) {
        url += "&iconurl=" + promption.background;
      } else {
        url += "?iconurl=" + promption.background;
      }
    }
    WebViewService.WebViewRequest webViewRequest = new WebViewService.WebViewRequest();
    webViewRequest.url = url;
    webViewService.openWebView(getActivity(), webViewRequest);
  }

  protected void doAfterGetData(BaseListResponse<Promption> response) {
    final GetListCountCallback.MapObj map = new GetListCountCallback.MapObj();
    map.index = getIndex();
    map.count = response.getTotals();
    EventBus.getDefault().post(new GetListCountCallback() {
      @Override
      public MapObj getCountMap() {
        return map;
      }
    });
  }

  @Override
  protected JAdapter<Promption> getAdapter(RecyclerViewHelper<BaseListResponse<Promption>, Promption> recyclerViewHelper) {
    return new PromptionListAdapter(recyclerViewHelper);
  }

  @Override
  protected String getEmptyHint() {
    return "暂无推广信息";
  }


  class PromptionListAdapter extends JAdapter<Promption> {

    private RecyclerViewHelper helper;

    public PromptionListAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_promption_detail_list, null);
      RecyclerView.ViewHolder viewHolder = new PromptionListViewHolder(view, helper);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof PromptionListViewHolder) {
        PromptionListViewHolder viewHolder = (PromptionListViewHolder) holder;
        Promption promption = objList.get(position);

        imageLoader.loadImage(viewHolder.promptionIconIv,
                promption.background, targetImgBgWidth,
                targetImgBgHeight, R.drawable.brand_icon_default);
        viewHolder.promptionTitleTv.setText(promption.title);
        viewHolder.promptionBrandNameTv.setText(promption.companyName);
        viewHolder.promptionEndDateTv.setText(promption.endDate);
      }
    }
  }

  class PromptionListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.promption_icon)
    ImageView promptionIconIv;

    @Bind(R.id.promption_title)
    TextView promptionTitleTv;

    @Bind(R.id.brand_name)
    TextView promptionBrandNameTv;

    @Bind(R.id.end_date)
    TextView promptionEndDateTv;

    public PromptionListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      LinearLayout.LayoutParams llp =
              (LinearLayout.LayoutParams) promptionIconIv.getLayoutParams();
      llp.width = targetImgBgWidth;
      llp.height = targetImgBgHeight;

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          recyclerViewHelper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }

  protected abstract int getIndex();
}
