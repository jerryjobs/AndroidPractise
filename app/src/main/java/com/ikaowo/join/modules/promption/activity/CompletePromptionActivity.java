package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.model.JoinedUser;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.modules.promption.widget.CompleteJoinItem;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;

/**
 * Created by weibo on 16-1-8.
 */
public class CompletePromptionActivity extends BaseActivity {

  @Bind(R.id.approved_user_container_stub)
  ViewStub userContainerStub;

  NetworkManager networkManager = JApplication.getNetworkManager();

  private int promptionId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_promption);
    ButterKnife.bind(this);

    getIntentData();
    getApprovedUserList();
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
    PromptionInterface promptionInterface = networkManager.getServiceByClass(PromptionInterface.class);
    Map<String, Integer> map = new HashMap<>();
    map.put("aci_id", promptionId);
    Call<BaseListResponse<JoinedUser>> call = promptionInterface.getApprovedList(map);
    networkManager.async(this, Constant.DATAGETTING, call, new KwMarketNetworkCallback<BaseListResponse<JoinedUser>>(this) {
      @Override
      public void onSuccess(BaseListResponse<JoinedUser> listResponse) {

        List<JoinedUser> joinedUserList;
        if (listResponse != null && (joinedUserList = listResponse.data) != null) {
          CompleteJoinItem completeJoinItem = null;
          for (int i = 0; i < joinedUserList.size(); i++) {
            JoinedUser joinedUser = joinedUserList.get(i);
            switch (i % 2) {
              case 0: // left
                completeJoinItem = new CompleteJoinItem(CompletePromptionActivity.this);
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
        }
      }
    });
  }

  @Override
  protected String getTag() {
    return "CompletePromptionActivity";
  }
}
