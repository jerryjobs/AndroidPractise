package com.ikaowo.join.modules.brand.fragment;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.model.request.PromptionListRequest;
import com.ikaowo.join.model.response.PromptionListResposne;

import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class PostedPromptionListFragment extends BasePromptionDetailListFragment {

  @Override
  public String getPageName() {
    return "PostedPromptionListFragment";
  }

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    PromptionListRequest request = new PromptionListRequest();
    request.company_id = brandId;
    request.cp = cp;
    request.ps = ps;
    Call<PromptionListResposne> call = promptionInterface.getPromptionList(request.getMap());
    JApplication.getNetworkManager().async(call, callback);
  }

  @Override
  protected int getIndex() {
    return 1;
  }
}
