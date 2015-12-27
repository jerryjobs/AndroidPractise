package com.common.framework.umeng;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weibo on 15-12-1.
 */
public class UmengService {

  private FeedbackAgent fb;
  private NotificationProcessInterface notificationInterface;

  public void init(Context context) {
    MobclickAgent.setDebugMode(false);
    MobclickAgent.openActivityDurationTrack(false);
    MobclickAgent.updateOnlineConfig(context);
  }

  public void checkUpdate(final Context context, final boolean force) {
    UmengUpdateAgent.setUpdateOnlyWifi(false);

    UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
      @Override
      public void onUpdateReturned(int updateStatus,
                                   UpdateResponse updateInfo) {
        switch (updateStatus) {
          case UpdateStatus.Yes: // has updateView
            UmengUpdateAgent.showUpdateDialog(context, updateInfo);
            break;
          case UpdateStatus.No: // has no updateView
            if (force) {
              Toast.makeText(context, "没有更新", Toast.LENGTH_SHORT).show();
            }
            break;
          case UpdateStatus.NoneWifi: // none wifi
            UmengUpdateAgent.showUpdateDialog(context, updateInfo);
            break;
          case UpdateStatus.Timeout: // time out
            //Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
            break;
        }
      }
    });
    if (force) {
      UmengUpdateAgent.forceUpdate(context);
    } else {
      UmengUpdateAgent.update(context);
    }
  }

  public void syncFeedback(final Context context, final String uid, final String phone) {
    fb = new FeedbackAgent(context);
    // check if the app developer has replied to the feedback or not.
    //fb.sync();
    Conversation conversation = fb.getDefaultConversation();
    conversation.sync(new SyncListener() {
      @Override
      public void onReceiveDevReply(List<Reply> list) {

        if (notificationInterface != null) {
          notificationInterface.onNewFeedBack(list);
        }
      }

      @Override
      public void onSendUserReply(List<Reply> list) {

      }
    });
    fb.openAudioFeedback();
    fb.openFeedbackPush();
    PushAgent.getInstance(context).enable();
    FeedbackPush.getInstance(context).init(false);

    new Thread(new Runnable() {
      @Override
      public void run() {
        UserInfo info = fb.getUserInfo();
        if (info == null)
          info = new UserInfo();
        Map<String, String> contact = info.getContact();
        if (contact == null) {
          contact = new HashMap<String, String>();
        }

        if (!TextUtils.isEmpty(uid)) {
          contact.put("uid", "");
        }
        if (!TextUtils.isEmpty(phone)) {
          contact.put("电话", phone + "");
        }
        info.setContact(contact);
        fb.setUserInfo(info);
        fb.updateUserInfo();
      }
    }).start();
  }

  public void setNotificationInterface(NotificationProcessInterface notificationInterface) {
    this.notificationInterface = notificationInterface;
  }

  public void feedback(Context context) {
    Intent intent = new Intent(context, FeedbackActivity.class);
    context.startActivity(intent);
  }

  public interface NotificationProcessInterface {
    void onNewFeedBack(List<Reply> replyList);
  }
}
