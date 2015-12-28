package com.ikaowo.join.modules.brand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.User;
import com.ikaowo.join.model.response.BrandListResponse;
import com.ikaowo.join.model.response.PromptionListResposne;
import com.ikaowo.join.model.response.UserListResponse;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.network.UserInfo;
import com.ikaowo.join.util.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class PromptionMemberListFragment extends BaseFragment {

  @Bind(R.id.recycler_view)
  ScrollMoreRecyclerView recyclerView;
  @Bind(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;

  private RecyclerViewHelper<PromptionListResposne, Promption> recyclerViewHelper;
  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private int brandId;
  private BrandInterface brandInterface;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recyclerViewHelper = new RecyclerViewHelper<>();
    imageLoader = JApplication.getImageLoader();
    brandInterface = JApplication.getNetworkManager().getServiceByClass(BrandInterface.class);

    targetImgBgWidth = JApplication.getJContext().dip2px(48);
    targetImgBgHeight = JApplication.getJContext().dip2px(48);

    Bundle bundle = getArguments();
    if (bundle != null) {
      brandId = bundle.getInt(Constant.BRAND_ID, 0);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_promption, null, false);
    ButterKnife.bind(this, view);

    setupRecyclerView();
    return view;
  }

  private void setupRecyclerView() {
    recyclerViewHelper.init(getActivity(), recyclerView, new PromptionMemberAdapter(recyclerViewHelper), swipeRefreshLayout);
    recyclerViewHelper.initEmptyView(0, "暂无成员信息");
    recyclerViewHelper.supportLoadMore(true);

    RecyclerViewHelperInterface recyclerViewHelperImpl = new RecyclerViewHelperInterface<UserListResponse, User>() {
      @Override
      public boolean checkResponse(JResponse baseResponse) {
        boolean result = baseResponse != null &&
                ((baseResponse instanceof UserListResponse)
                        && (((UserListResponse) baseResponse).data) != null);
        if (result) {
          final GetListCountCallback.MapObj map = new GetListCountCallback.MapObj();
          map.index = 2;
          map.count = baseResponse.getTotals();
          EventBus.getDefault().post(new GetListCountCallback() {
            @Override
            public MapObj getCountMap() {
              return map;
            }
          });
        }
        return result;
      }

      @Override
      public List<User> getList(UserListResponse jResponse) {
        List<User> list = jResponse.data;
        return list;
      }

      @Override
      public void sendRequest(NetworkCallback callback, int cp, int ps) {
        Call<UserListResponse> call =brandInterface.getBrandMember(brandId);
        JApplication.getNetworkManager().async(call, callback);
      }

      @Override
      public void performItemClick(int position) {
        // do nothing
      }
    };
    recyclerViewHelper.setHelperInterface(recyclerViewHelperImpl);
    recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
  }

  class PromptionMemberAdapter extends JAdapter<User> {
    private RecyclerViewHelper helper;

    public PromptionMemberAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_brand_member, null);
      RecyclerView.ViewHolder viewHolder = new MemberListViewHolder(view, helper);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof MemberListViewHolder) {
        MemberListViewHolder memberListViewHolder = (MemberListViewHolder)holder;
        User user = objList.get(position);

        if (TextUtils.isEmpty(user.userIcon)) {
          memberListViewHolder.userIconIv.setVisibility(View.GONE);
          memberListViewHolder.userIconTv.setVisibility(View.VISIBLE);
          memberListViewHolder.userIconTv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.c1));
          memberListViewHolder.userIconTv.setText(user.nickName.substring(0, 2));
        } else {
          memberListViewHolder.userIconIv.setVisibility(View.VISIBLE);
          memberListViewHolder.userIconTv.setVisibility(View.GONE);

          imageLoader.loadImage(memberListViewHolder.userIconIv,
                  user.userIcon, targetImgBgWidth, targetImgBgHeight, R.drawable.brand_icon_default);
        }
        memberListViewHolder.nameTitleTv.setText(user.nickName + " | " + user.title);
      }
    }
  }

  class MemberListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.user_icon_tv)
    TextView userIconTv;

    @Bind(R.id.user_icon_iv)
    ImageView userIconIv;

    @Bind(R.id.name_title)
    TextView nameTitleTv;


    public MemberListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

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

  @Override
  public String getPageName() {
    return "PromptionMemberListFragment";
  }
}
