package com.ikaowo.join.modules.brand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.model.User;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListFragment;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.util.AvatarHelper;
import com.ikaowo.join.util.Constant;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class PromptionMemberListFragment extends BaseListFragment<BaseListResponse<User>, User> {

  private ImageLoader imageLoader;
  private int targetImgBgWidth, targetImgBgHeight;
  private int brandId;
  private BrandInterface brandInterface;
  private UserService userService;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recyclerViewHelper = new RecyclerViewHelper<>();
    imageLoader = JApplication.getImageLoader();
    brandInterface = JApplication.getNetworkManager().getServiceByClass(BrandInterface.class);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    targetImgBgWidth = JApplication.getJContext().dip2px(48);
    targetImgBgHeight = JApplication.getJContext().dip2px(48);

    Bundle bundle = getArguments();
    if (bundle != null) {
      brandId = bundle.getInt(Constant.BRAND_ID, 0);
    }
  }

  @Override protected boolean isSupportLoadMore() {
    return false;
  }

  @Override protected void doAfterGetData(BaseListResponse<User> userBaseListResponse) {
    final GetListCountCallback.MapObj map = new GetListCountCallback.MapObj();
    map.index = 2;
    map.count = userBaseListResponse.getTotals();
    EventBus.getDefault().post(new GetListCountCallback() {
      @Override public MapObj getCountMap() {
        return map;
      }
    });
  }

  @Override protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    Call<BaseListResponse<User>> call = brandInterface.getBrandMember(brandId);
    JApplication.getNetworkManager().async(call, callback);
  }

  @Override protected void performCustomItemClick(User user) {
    if (!(user.id == userService.getUserId())) {
      userService.imChat(getActivity(), user.wx);
    }
  }

  @Override protected JAdapter<User> getAdapter(
      RecyclerViewHelper<BaseListResponse<User>, User> recyclerViewHelper) {
    return new PromptionMemberAdapter(recyclerViewHelper);
  }

  @Override protected String getEmptyHint() {
    return "暂无成员信息";
  }

  @Override public String getPageName() {
    return "PromptionMemberListFragment";
  }

  class PromptionMemberAdapter extends JAdapter<User> {
    private RecyclerViewHelper helper;

    public PromptionMemberAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_brand_member, null);
      RecyclerView.ViewHolder viewHolder = new MemberListViewHolder(view, helper);
      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof MemberListViewHolder) {
        MemberListViewHolder memberListViewHolder = (MemberListViewHolder) holder;
        User user = objList.get(position);

        AvatarHelper.getInstance()
            .showAvatar(getContext(), user.id, memberListViewHolder.userIconIv,
                memberListViewHolder.shortNameTv, targetImgBgWidth, targetImgBgHeight,
                user.userIcon, user.nickName);

        memberListViewHolder.nameTitleTv.setText(getActivity().getString(R.string.user_name_and_title, user.nickName, user.title));
        memberListViewHolder.chatIconView.setVisibility(
            userService.getUserId() == user.id ? View.GONE:View.VISIBLE);
      }
    }
  }

  class MemberListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.short_name) TextView shortNameTv;

    @Bind(R.id.icon) ImageView userIconIv;

    @Bind(R.id.name_title) TextView nameTitleTv;

    @Bind(R.id.chat_icon) View chatIconView;

    public MemberListViewHolder(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.findViewById(R.id.chat_icon).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          recyclerViewHelper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
