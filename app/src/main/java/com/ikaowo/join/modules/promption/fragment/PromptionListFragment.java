package com.ikaowo.join.modules.promption.fragment;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.model.request.PromptionListRequest;
import com.ikaowo.join.model.response.PromptionListResposne;
import retrofit.Call;
import retrofit.Callback;

/**
 * Created by weibo on 15-12-8.
 */
public class PromptionListFragment extends BasePromptionFragment {

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    PromptionListRequest request = new PromptionListRequest();
    request.company_id = -1;
    request.cp = cp;
    request.ps = ps;
    Call<PromptionListResposne> call = promptionInterface.getPromptionList(request.getMap());
    //call.enqueue(callback);
    JApplication.getNetworkManager().async(call, callback);
  }

}
