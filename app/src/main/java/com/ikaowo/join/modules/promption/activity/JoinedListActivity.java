package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.network.NetworkManager;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.R;
import com.ikaowo.join.model.User;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.common.BaseListActivity;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinedListActivity extends BaseListActivity {

  private PromptionInterface promptionInterface;
  private NetworkManager networkManager;

  private int promptionId;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
      dialogHelper.createDialog(this, "注意", "推广信息不正确，请返回重试", new String[] {"确定"}, new View.OnClickListener[] {
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        }
      }).show();
    }
  }

  @Override
  protected boolean isSupportLoadMore() {
    return false;
  }

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    networkManager = JApplication.getNetworkManager();
    promptionInterface = networkManager.getServiceByClass(PromptionInterface.class);
    Call<BaseListResponse<User>> call = promptionInterface.getJoinedList(promptionId, cp, ps);
    networkManager.async(this, Constant.DATAGETTING, call, callback);

  }

  @Override
  protected void performCustomItemClick(Object o) {

  }

  @Override
  protected JAdapter getAdapter(RecyclerViewHelper recyclerViewHelper) {
    return null;
  }

  @Override
  protected String getEmptyHint() {
    return "暂无参加人员";
  }

  @Override
  protected String getTag() {
    return "JoinedListActivity";
  }

  class JoinedUserAdapter extends JAdapter<User> {
    private RecyclerViewHelper<BaseListResponse<User>, User> recyclerViewHelper;
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
        User user = objList.get(position);
//        viewHolder.brandNameTv.setText(user.);
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
