package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkCallback;
import com.common.framework.network.NetworkManager;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.JoinStateUpdateCallback;
import com.ikaowo.join.model.JoinedUser;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListActivity;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinedListActivity extends BaseListActivity<BaseListResponse<JoinedUser>, JoinedUser> {

  private PromptionInterface promptionInterface;
  private NetworkManager networkManager;
  private ImageLoader imageLoader;
  private int targetWidth, targetHeight;
  private PromptionService promptionService;
  private UserService userService;
  private Map<String, Integer> stateColorMap = new HashMap();
  private Map<String, String> stateDesMap = new HashMap<>();

  private int promptionId;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    try {
      List<String> pathList = getIntent().getData().getPathSegments();
      promptionId = Integer.valueOf(pathList.get(pathList.size() - 1));
      if (promptionId <= 0) {
        promptionId = getIntent().getExtras().getInt(Constant.PROMPTION_ID, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (promptionId <= 0) {
      showConfirmDialog("注意", "推广活动信息不正确，请返回重试");
    }

    super.onCreate(savedInstanceState);

    imageLoader = JApplication.getImageLoader();
    promptionService = JApplication.getJContext().getServiceByInterface(PromptionService.class);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.title_activity_joined_list);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    targetWidth = JApplication.getJContext().dip2px(64);
    targetHeight = JApplication.getJContext().dip2px(48);

    stateColorMap.put(Constant.JOIN_STATE_JOINED, R.color.c1);
    stateColorMap.put(Constant.JOIN_STATE_FAILED, R.color.c9);
    stateDesMap.put(Constant.JOIN_STATE_JOINED, "已同意");
    stateDesMap.put(Constant.JOIN_STATE_FAILED, "已拒绝");
    EventBus.getDefault().register(this);
  }

  @Override
  protected boolean isSupportLoadMore() {
    return true;
  }

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    if (networkManager == null) {
      networkManager = JApplication.getNetworkManager();
    }
    promptionInterface = networkManager.getServiceByClass(PromptionInterface.class);
    Call<BaseListResponse<JoinedUser>> call = promptionInterface.getJoinedList(promptionId, cp, ps);
    networkManager.async(this, Constant.DATAGETTING, call, callback);
  }

  @Override
  protected void performCustomItemClick(JoinedUser user) {
    if (userService == null) {
      userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    }
    promptionService.viewJoinDetail(this, user.companyId, user.promption_id);
  }

  @Override
  protected JAdapter getAdapter(RecyclerViewHelper recyclerViewHelper) {
    return new JoinedUserAdapter(recyclerViewHelper);
  }

  @Override
  protected String getEmptyHint() {
    return "暂无参加人员";
  }

  @Override
  protected String getTag() {
    return "JoinedListActivity";
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  public void onEvent(JoinStateUpdateCallback callback) {
    JAdapter<JoinedUser> adapter = (JAdapter<JoinedUser>)recyclerView.getAdapter();
    List<JoinedUser> list = adapter.getObjList();
    if (list != null) {
      JoinedUser user = list.get(clickedPos);
      user.state = callback.newState();
      String stateDesc = stateDesMap.get(callback.newState());
      if (stateDesc != null) {
        user.stateDesc = stateDesc;
      }
      adapter.notifyDataSetChanged();
    }
  }

  class JoinedUserAdapter extends JAdapter<JoinedUser> {
    private RecyclerViewHelper<BaseListResponse<JoinedUser>, JoinedUser> recyclerViewHelper;
    public JoinedUserAdapter(RecyclerViewHelper helper) {
      this.recyclerViewHelper = helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(JoinedListActivity.this).inflate(R.layout.item_joined_list, null);
      RecyclerView.ViewHolder viewHolder = new JoinedUserViewHolder(view, recyclerViewHelper);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof JoinedUserViewHolder) {
        JoinedUserViewHolder viewHolder = (JoinedUserViewHolder)holder;
        JoinedUser user = objList.get(position);
//        viewHolder.brandNameTv.setText(user.);
        viewHolder.brandNameTv.setText(user.brandName);
        viewHolder.joinCommentTv.setText(user.extra);
        if (user.state.equalsIgnoreCase(Constant.JOIN_STATE_PASSED)
          || user.state.equalsIgnoreCase(Constant.JOIN_STATE_FAILED)) {
          viewHolder.joinedStateTv.setVisibility(View.VISIBLE);
          viewHolder.joinedStateTv.setText(user.stateDesc);
          viewHolder.joinedStateTv.setTextColor(stateColorMap.get(user.state));
        } else {
          viewHolder.joinedStateTv.setVisibility(View.GONE);
        }
        imageLoader.loadImage(viewHolder.brandIconIv, user.brandIcon, targetWidth, targetHeight, R.drawable.brand_icon_default);

      }
      super.onBindViewHolder(holder, position);
    }
  }

  class JoinedUserViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.brand_icon)
    ImageView brandIconIv;
    @Bind(R.id.brand_name)
    TextView brandNameTv;
    @Bind(R.id.join_comment)
    TextView joinCommentTv;
    @Bind(R.id.join_state)
    TextView joinedStateTv;

    public JoinedUserViewHolder(View itemView, final RecyclerViewHelper helper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          helper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
