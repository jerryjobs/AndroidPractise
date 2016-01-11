package com.ikaowo.join.modules.promption.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.request.PromptionListRequest;
import retrofit.Call;

/**
 * Created by weibo on 15-12-8.
 */
public class PromptionListFragment extends BasePromptionFragment {

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    showState = true;
    super.onCreate(savedInstanceState);
  }

  @Override protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    PromptionListRequest request = new PromptionListRequest();
    request.company_id = -1;
    request.cp = cp;
    request.ps = ps;
    Call<BaseListResponse<Promption>> call = promptionInterface.getPromptionList(request.getMap());
    JApplication.getNetworkManager().async(call, callback);
  }

  @Override public String getPageName() {
    return "PromptionListFragment";
  }
}
