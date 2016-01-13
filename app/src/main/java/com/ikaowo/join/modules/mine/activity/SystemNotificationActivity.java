package com.ikaowo.join.modules.mine.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.network.NetworkManager;
import com.common.framework.util.JLog;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.R;
import com.ikaowo.join.model.JoinedUser;
import com.ikaowo.join.model.Notification;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.modules.common.BaseListActivity;
import com.ikaowo.join.modules.push.factory.PushProcesserFactory;
import com.ikaowo.join.modules.push.processer.PushDataProcesser;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.NotificationInterface;
import com.ikaowo.join.util.DateTimeHelper;
import java.util.HashMap;
import java.util.Map;
import retrofit.Call;

/**
 * Created by weibo on 16-1-11.
 */

public class SystemNotificationActivity
    extends BaseListActivity<BaseListResponse<Notification>, Notification> {
  private NetworkManager networkManager;
  private NotificationInterface notificationInterface;
  private DateTimeHelper dateTimeHelper = new DateTimeHelper();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    networkManager = JApplication.getNetworkManager();

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.title_activity_system_notification);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override protected boolean isSupportLoadMore() {
    return true;
  }

  @Override protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    if (networkManager == null) {
      networkManager = JApplication.getNetworkManager();
    }

    if (notificationInterface == null) {
      notificationInterface = networkManager.getServiceByClass(NotificationInterface.class);
    }

    Map map = new HashMap();
    map.put("cp", cp);
    map.put("ps", ps);
    Call<BaseListResponse<Notification>> call = notificationInterface.getNoticeList(map);
    networkManager.async(call, callback);
  }

  @Override protected void performCustomItemClick(final Notification notification) {
    PushDataProcesser processer = new PushProcesserFactory().getDataProcesser(notification.type);
    if (processer == null) {
      JLog.e(getTag(), "暂不支持该类型的推送消息处理:" + notification.type);
      return;
    }
    processer.action(this, String.valueOf(notification.targetId));

    if (!notification.isRead) {
      Map<String, Integer> map = new HashMap<>();
      map.put("nt_id", notification.id);
      Call<BaseResponse> call = notificationInterface.markAsRed(map);
      networkManager.async(call, new KwMarketNetworkCallback<BaseResponse>(this) {
        @Override public void onSuccess(BaseResponse response) {
          notification.isRead = true;
          JAdapter<JoinedUser> adapter = (JAdapter<JoinedUser>) recyclerView.getAdapter();
          adapter.notifyDataSetChanged();
        }
      });
    }
  }

  @Override protected JAdapter getAdapter(RecyclerViewHelper recyclerViewHelper) {
    return new NotificationAdapter(recyclerViewHelper);
  }

  @Override protected String getEmptyHint() {
    return "暂无系统消息";
  }

  @Override protected String getTag() {
    return "SystemNotificationActivity";
  }

  class NotificationAdapter extends JAdapter<Notification> {
    private RecyclerViewHelper<BaseListResponse<JoinedUser>, JoinedUser> recyclerViewHelper;

    public NotificationAdapter(RecyclerViewHelper helper) {
      this.recyclerViewHelper = helper;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(SystemNotificationActivity.this)
          .inflate(R.layout.item_system_notification, null);
      RecyclerView.ViewHolder viewHolder = new NotificationViewHolder(view, recyclerViewHelper);
      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof NotificationViewHolder) {
        NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
        Notification notification = objList.get(position);
        viewHolder.titleTv.setText(notification.title);
        viewHolder.contentTv.setText(notification.content);
        if (TextUtils.isEmpty(notification.time)) {
          viewHolder.timeTv.setText("2016-01-02");
        } else {
          viewHolder.timeTv.setText(dateTimeHelper.getTime(notification.time));
        }

        if (notification.isRead) {
          viewHolder.timeTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c9));
          viewHolder.titleTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c9));
          viewHolder.contentTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c9));
        } else {
          viewHolder.timeTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c9));
          viewHolder.titleTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c3));
          viewHolder.contentTv.setTextColor(ContextCompat.getColor(SystemNotificationActivity.this, R.color.c10));
        }
      }
      super.onBindViewHolder(holder, position);
    }
  }

  class NotificationViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.time) TextView timeTv;
    @Bind(R.id.title) TextView titleTv;
    @Bind(R.id.content) TextView contentTv;

    public NotificationViewHolder(View itemView, final RecyclerViewHelper helper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          helper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
