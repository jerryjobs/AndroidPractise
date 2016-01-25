package com.ikaowo.join.modules.promption.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.common.framework.network.NetworkManager;
import com.common.framework.util.JToast;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.eventbus.RefreshWebViewCallback;
import com.ikaowo.join.model.JoinedUser;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.CompletePromptionRequest;
import com.ikaowo.join.modules.promption.widget.CompleteJoinItem;
import com.ikaowo.join.modules.promption.widget.MediaItem;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.network.UpdatePromptionStateRequest;
import com.ikaowo.join.util.Constant;
import de.greenrobot.event.EventBus;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import retrofit.Call;

/**
 * Created by weibo on 16-1-8.
 */
public class CompletePromptionActivity extends BaseActivity {

  @Bind(R.id.approved_user_container_stub) ViewStub userContainerStub;

  @Bind(R.id.media_url_1) MediaItem mediaUrl1;

  @Bind(R.id.media_url_3) MediaItem mediaUrl3;

  @Bind(R.id.media_url_2) MediaItem mediaUrl2;

  private NetworkManager networkManager = JApplication.getNetworkManager();

  private int promptionId;
  private Set<Integer> idSets = new HashSet<>();
  private int idSize;
  private Dialog dialog;
  private JDialogHelper dialogHelper;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_promption);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(com.ikaowo.join.R.string.title_activity_complete_promption);
    setSupportActionBar(toolbar);
    android.support.v7.app.ActionBar ab = getSupportActionBar();
    dialogHelper = new JDialogHelper(CompletePromptionActivity.this);
    ab.setDisplayHomeAsUpEnabled(true);
    setOptionMenu();
    getIntentData();
    getApprovedUserList();
  }

  private void setOptionMenu() {
    menuResId = R.menu.menu_add_submit;
  }

  private void getIntentData() {
    try {
      List<String> pathList = getIntent().getData().getPathSegments();
      promptionId = Integer.valueOf(pathList.get(pathList.size() - 1));
      if (promptionId <= 0) {
        promptionId = getIntent().getExtras().getInt(Constant.PROMPTION_ID, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getApprovedUserList() {
    if (promptionId <= 0) {
      showConfirmDialog("注意", "活动信息不对，请返回重试");
      return;
    }
    userContainerStub.setLayoutResource(R.layout.stub_promption_joined_container);
    final ViewGroup container = (ViewGroup) userContainerStub.inflate();
    PromptionInterface promptionInterface =
        networkManager.getServiceByClass(PromptionInterface.class);
    Map<String, Integer> map = new HashMap<>();
    map.put("aci_id", promptionId);
    Call<BaseListResponse<JoinedUser>> call = promptionInterface.getApprovedList(map);
    networkManager.async(this, Constant.DATAGETTING, call,
        new KwMarketNetworkCallback<BaseListResponse<JoinedUser>>(this) {
          @Override public void onSuccess(BaseListResponse<JoinedUser> listResponse) {

            List<JoinedUser> joinedUserList;
            if (listResponse != null && (joinedUserList = listResponse.data) != null && joinedUserList.size() > 0) {
              idSize = joinedUserList.size();
              CompleteJoinItem completeJoinItem = null;
              for (int i = 0; i < joinedUserList.size(); i++) {
                JoinedUser joinedUser = joinedUserList.get(i);
                idSets.add(joinedUser.uId);
                switch (i % 2) {
                  case 0: // left
                    completeJoinItem = new CompleteJoinItem(CompletePromptionActivity.this);
                    completeJoinItem.setItemClickInterface(
                        new CompleteJoinItem.ItemClickInterface() {
                          @Override public void itemClicked(int id, boolean selected) {
                            invalidateOptionsMenu();
                            if (selected) {
                              idSets.remove(id);
                            } else {
                              idSets.add(id);
                            }
                          }
                        });
                    container.addView(completeJoinItem);
                    completeJoinItem.setLeft(joinedUser);

                    //如果为奇数个时候，最后一个添加一个空的view占位，后续优化
                    if (i == joinedUserList.size() - 1) {
                      completeJoinItem.setRight(null);
                    }
                    break;
                  case 1: //right
                    if (completeJoinItem != null) {
                      completeJoinItem.setRight(joinedUser);
                    }
                    break;
                }
              }
            } else {
              dialog = dialogHelper.createDialog(R.string.hint_nobody_joined,
                  new String[] { "我知道了", "结束推广" }, new View.OnClickListener[] {
                      new View.OnClickListener() {
                        @Override public void onClick(View v) {
                          dialog.dismiss();
                          CompletePromptionActivity.this.finish();
                        }
                      }, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                      UpdatePromptionStateRequest request = new UpdatePromptionStateRequest();
                      request.aci_id = promptionId;
                      request.comment = "无人参加，选择结束活动";
                      request.u_act = Constant.JOIN_STATE_CANCEL;
                      PromptionInterface promptionInterface =
                          networkManager.getServiceByClass(PromptionInterface.class);
                      Call<BaseResponse> call = promptionInterface.updatePromptionState(request);

                      networkManager.async(CompletePromptionActivity.this, Constant.PROCESSING,
                          call, new KwMarketNetworkCallback<BaseResponse>(
                              CompletePromptionActivity.this) {
                            @Override public void onSuccess(BaseResponse response) {
                              EventBus.getDefault().post(new RefreshWebViewCallback() {
                                @Override public boolean refreshWebView() {
                                  return true;
                                }
                              });

                              new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                  finish();
                                }
                              }, 300);
                            }
                          });
                      dialog.dismiss();
                    }
                  }
                  });
              dialog.show();
            }
          }
        });
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    menu.getItem(0).setEnabled(idSize > idSets.size());
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_submit:
        submit();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void submit() {
    final CompletePromptionRequest request = new CompletePromptionRequest();

    request.aci_id = promptionId;
    request.u_id = idSets;
    try {
      StringBuilder sb = getMediarUrlStr(mediaUrl1.getValue(),
          mediaUrl2.getValue(),
          mediaUrl3.getValue());
      if (sb.length() > 0) {
        request.media_link = sb.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (dialog == null) {
      dialog = dialogHelper.createDialog(R.string.dialog_title, R.string.hint_pick_joined_user, new String[] { "取消" , "确定"},
        new View.OnClickListener[] {
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              dialog.dismiss();
            }
          },
          new View.OnClickListener() {
            @Override public void onClick(View v) {

              PromptionInterface promptionInterface =
                  networkManager.getServiceByClass(PromptionInterface.class);
              Call<BaseResponse> call = promptionInterface.completePromption(request);

              networkManager.async(CompletePromptionActivity.this, Constant.PROCESSING, call,
                new KwMarketNetworkCallback<BaseResponse>(CompletePromptionActivity.this) {
                  @Override public void onSuccess(BaseResponse response) {
                    EventBus.getDefault().post(new RefreshWebViewCallback() {
                      @Override public boolean refreshWebView() {
                        return true;
                      }
                    });
                    JToast.toastShort("操作完成");
                    new Handler().postDelayed(new Runnable() {
                      @Override public void run() {
                        finish();
                      }
                    }, 300);
                  }
                });
              dialog.dismiss();
            }
          }
        }
      );
      dialog.show();
    } else if(!dialog.isShowing()) {
      dialog.show();
    }
  }

  private StringBuilder getMediarUrlStr(String... urls) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < urls.length; i++) {
      if (TextUtils.isEmpty(urls[i])) {
        continue;
      }
      String url = urls[i];
      if (url.startsWith("www")) {
        url = "http://" + url;
      } else if (!url.startsWith("http") && !url.startsWith("www")) {
        url = "http://www." + url;
      }

      sb.append(TextUtils.isEmpty(sb.toString()) ? url : "," + url);
    }
    return sb;
  }

  @Override protected String getTag() {
    return "CompletePromptionActivity";
  }
}
