package com.ikaowo.join.modules.brand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.eventbus.UpdatePromptionCallback;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListFragment;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.DateTimeHelper;
import com.ikaowo.join.util.SharedPreferenceHelper;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weibo on 15-12-28.
 */
public abstract class BasePromptionDetailListFragment
    extends BaseListFragment<BaseListResponse<Promption>, Promption> {

  protected PromptionInterface promptionInterface;
  protected int brandId;
  protected boolean showState;
  protected String statePrefix;
  LinearLayout.LayoutParams tmpLlp =
      new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          JApplication.getJContext().dip2px(36));
  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private WebViewService webViewService;
  private UserService userService;
  private DateTimeHelper dateTimeHelper = new DateTimeHelper();
  private Map<String, Integer> stateColorMap = new HashMap();
  private Map<String, String> map;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    imageLoader = JApplication.getImageLoader();
    promptionInterface =
        JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);

    webViewService = JApplication.getJContext().getServiceByInterface(WebViewService.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    targetImgBgWidth = JApplication.getJContext().dip2px(120);
    targetImgBgHeight = targetImgBgWidth * 9 / 16;

    Bundle bundle = getArguments();
    if (bundle != null) {
      brandId = bundle.getInt(Constant.BRAND_ID, 0);
      showState = bundle.getBoolean(Constant.SHOW_STATE, false);
    }
    initStateColorMap();
  }

  private void initStateColorMap() {
    stateColorMap.clear();
    // 作为发起方的状态颜色
    stateColorMap.put(Constant.PROMPTION_STATE_NEW, R.color.c1);
    stateColorMap.put(Constant.PROMPTION_STATE_FAILED, R.color.c11);
    stateColorMap.put(Constant.PROMPTION_STATE_PASS, R.color.c1);
    stateColorMap.put(Constant.PROMPTION_STATE_OVER, R.color.c4);
    stateColorMap.put(Constant.PROMPTION_STATE_CANCEL, R.color.c4);
    stateColorMap.put(Constant.PROMPTION_STATE_DONE, R.color.c4);

    //　作为参与方的状态颜色
    stateColorMap.put(Constant.JOIN_STATE_FAILED, R.color.c11);
    stateColorMap.put(Constant.JOIN_STATE_CANCEL, R.color.c11);
    stateColorMap.put(Constant.JOIN_STATE_USER_CANCEL, R.color.c11);
    stateColorMap.put(Constant.JOIN_STATE_PASSED, R.color.c1);
    stateColorMap.put(Constant.JOIN_STATE_PENDING_APPROVE, R.color.c1);
    stateColorMap.put(Constant.JOIN_STATE_JOINED, R.color.c1);
    stateColorMap.put(Constant.JOIN_STATE_NOT_JOINED, R.color.c1);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    map = SharedPreferenceHelper.getInstance().getEnumValue(getActivity());
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

  protected void doAfterGetData(BaseListResponse<Promption> response) {
    final GetListCountCallback.MapObj map = new GetListCountCallback.MapObj();
    map.index = getIndex();
    map.count = response.getTotals();
    EventBus.getDefault().post(new GetListCountCallback() {
      @Override public MapObj getCountMap() {
        return map;
      }
    });
  }

  @Override protected JAdapter<Promption> getAdapter(
      RecyclerViewHelper<BaseListResponse<Promption>, Promption> recyclerViewHelper) {
    return new PromptionListAdapter(recyclerViewHelper);
  }

  protected abstract int getIndex();

  public void onEvent(UpdatePromptionCallback callback) {
    if (callback.promptionUpdated()) {
      JAdapter<Promption> adapter = (JAdapter<Promption>) recyclerView.getAdapter();
      List<Promption> list = adapter.getObjList();
      Promption clickedPromption = null;
      if (list != null && (clickedPromption = list.get(clicedPos)) != null) {
        clickedPromption.title = callback.getNewTitle();
        clickedPromption.endDate = callback.getNewEndTime();
        clickedPromption.state = callback.getNewState();
        clickedPromption.background = callback.getNewBg();
        adapter.notifyDataSetChanged();
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  class PromptionListAdapter extends JAdapter<Promption> {

    private RecyclerViewHelper helper;

    public PromptionListAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(getActivity()).inflate(R.layout.item_promption_detail_list, null);
      RecyclerView.ViewHolder viewHolder = new PromptionListViewHolder(view, helper);
      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof PromptionListViewHolder) {
        PromptionListViewHolder viewHolder = (PromptionListViewHolder) holder;
        Promption promption = objList.get(position);

        imageLoader.loadImage(viewHolder.promptionIconIv, promption.background, targetImgBgWidth,
            targetImgBgHeight, R.drawable.brand_icon_default);
        viewHolder.promptionTitleTv.setText(promption.title);
        viewHolder.promptionBrandNameTv.setText(
            getString(R.string.posted_brand_name, promption.brandName));
        viewHolder.promptionEndDateTv.setText(
            getString(R.string.posted_join_end_date, dateTimeHelper.getTime(promption.endDate)));
        if (viewHolder.textView != null) {
          if (map == null) {
            viewHolder.textView.setText(promption.stateDesc);
          } else {
            viewHolder.textView.setText(map.get(statePrefix + promption.state));
          }

          viewHolder.textView.setTextColor(
              ContextCompat.getColor(getActivity(), stateColorMap.get(promption.state)));
        }
      }
    }
  }

  class PromptionListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_contaienr) LinearLayout itemContainerLayout;

    @Bind(R.id.promption_icon) ImageView promptionIconIv;

    @Bind(R.id.promption_title) TextView promptionTitleTv;

    @Bind(R.id.brand_name) TextView promptionBrandNameTv;

    @Bind(R.id.end_date) TextView promptionEndDateTv;

    TextView textView;

    public PromptionListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) promptionIconIv.getLayoutParams();

      llp.width = targetImgBgWidth;
      llp.height = targetImgBgHeight;

      if (showState) {
        LinearLayout.LayoutParams llp2 =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llp2.bottomMargin = JApplication.getJContext().dip2px(16);
        itemView.setLayoutParams(llp2);

        textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER_VERTICAL);
        itemContainerLayout.addView(textView, tmpLlp);
      }

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          recyclerViewHelper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
