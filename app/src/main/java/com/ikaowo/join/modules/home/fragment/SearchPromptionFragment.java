package com.ikaowo.join.modules.home.fragment;

import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.model.request.SearchRequest;
import com.ikaowo.join.model.response.PromptionListResposne;

import retrofit.Call;
import retrofit.Callback;

/**
 * Created by weibo on 15-12-24.
 */
public class SearchPromptionFragment extends BasePromptionFragment {

  private String value;
  @Override
  protected void sendRequest(Callback callback, int cp, int ps) {
    SearchRequest request = new SearchRequest();
    request.value = this.value;
    request.cp = cp;
    request.ps = ps;
    Call<PromptionListResposne> call = promptionInterface.getPromptionList(request.getMap());
    call.enqueue(callback);
  }

  public void search(String search) {
    this.value = search;
    recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
  }
}
