package com.ikaowo.join.modules.promption.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.common.framework.model.JResponse;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.response.BrandListResponse;
import com.ikaowo.join.model.response.PromptionListResposne;
import com.ikaowo.join.network.PromptionInterface;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;

/**
 * 这个basefragment现在由tab里面的推广列表以及推广搜索结果页面公用。
 * TODO 后期如果搜索结果页面发生变化，那么需要把这个页面跟搜索结果页面剥离
 * Created by weibo on 15-12-8.
 */
public abstract class BasePromptionFragment extends BaseFragment {

  @Bind(R.id.recycler_view)
  ScrollMoreRecyclerView recyclerView;
  @Bind(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;

  protected PromptionInterface promptionInterface;
  protected RecyclerViewHelper<PromptionListResposne, Promption> recyclerViewHelper;

  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private WebViewService webViewService;
  private UserService userService;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recyclerViewHelper = new RecyclerViewHelper<>();
    imageLoader = JApplication.getImageLoader();
    promptionInterface = JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);

    webViewService = JApplication.getJContext().getServiceByInterface(WebViewService.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    targetImgBgWidth
            = JApplication.getJContext().getScreenWidth() - 2 * JApplication.getJContext().dip2px(12);
    targetImgBgHeight = targetImgBgWidth * 9 / 16;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, null, false);
    ButterKnife.bind(this, view);

    setupRecyclerView();

    return view;
  }

  private void setupRecyclerView() {
    recyclerViewHelper.init(recyclerView, new PromptionListAdapter(recyclerViewHelper), swipeRefreshLayout);
    recyclerViewHelper.initEmptyView(0, "暂无推广信息");
    recyclerViewHelper.supportLoadMore(true);

    RecyclerViewHelperInterface recyclerViewHelperImpl = new RecyclerViewHelperInterface<PromptionListResposne, Promption>() {
      @Override
      public boolean checkResponse(JResponse baseResponse) {
        return baseResponse != null &&
                ((baseResponse instanceof PromptionListResposne)
                        && (((PromptionListResposne) baseResponse).data) != null);
      }

      @Override
      public List<Promption> getList(PromptionListResposne jResponse) {
        List<Promption> list = jResponse.data;
        return list;
      }

      @Override
      public void sendRequest(Callback callback, int cp, int ps) {

        sendHttpRequest(callback, cp, ps);
      }

      @Override
      public void performItemClick(int position) {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
          return;
        }

        List objList = ((JAdapter<BrandListResponse>) adapter).getObjList();
        if (objList == null) {
          return;
        }

        Promption promption = (Promption)objList.get(position);
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
    };
    recyclerViewHelper.setHelperInterface(recyclerViewHelperImpl);
    recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
  }

  protected abstract void sendHttpRequest(Callback call, int cp, int ps);

  @Override
  public String getPageName() {
    return "Home";
  }

  class PromptionListAdapter extends JAdapter<Promption> {

    private RecyclerViewHelper helper;

    public PromptionListAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_promption_list, null);
      RecyclerView.ViewHolder viewHolder = new PromptionListViewHolder(view, helper);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof PromptionListViewHolder) {
        PromptionListViewHolder viewHolder = (PromptionListViewHolder) holder;
        Promption promption = objList.get(position);
        viewHolder.companyNameTv.setText(promption.companyName);
        viewHolder.promptionAddressTv.setText(promption.address);
        viewHolder.promptionTimeTv.setText(promption.date);
        viewHolder.promptionTitleTv.setText(promption.title);
        imageLoader.loadImage(viewHolder.promptionBgImg,
                                promption.background, targetImgBgWidth,
                                targetImgBgHeight, R.drawable.brand_icon_default);
        viewHolder.promptionContentTv.setText(promption.content);
      }
    }
  }

  class PromptionListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.company_name)
    TextView companyNameTv;
    @Bind(R.id.promption_address)
    TextView promptionAddressTv;
    @Bind(R.id.promption_time)
    TextView promptionTimeTv;
    @Bind(R.id.promption_bg)
    ImageView promptionBgImg;
    @Bind(R.id.promption_title)
    TextView promptionTitleTv;
    @Bind(R.id.promption_content)
    TextView promptionContentTv;

    public PromptionListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      LinearLayout.LayoutParams llp =
              (LinearLayout.LayoutParams) promptionBgImg.getLayoutParams();
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

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
