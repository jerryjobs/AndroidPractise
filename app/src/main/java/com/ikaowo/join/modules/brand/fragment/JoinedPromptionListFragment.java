package com.ikaowo.join.modules.brand.fragment;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.model.response.PromptionListResposne;

import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class JoinedPromptionListFragment extends BasePromptionDetailListFragment {
  @Override
  public String getPageName() {
    return "JoinedPromptionListFragment";
  }

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    Call<PromptionListResposne> call = promptionInterface.getJoinedPromptionList(brandId, cp, ps);
    JApplication.getNetworkManager().async(call, callback);
  }

  @Override
  protected int getIndex() {
    return 0;
  }
}
