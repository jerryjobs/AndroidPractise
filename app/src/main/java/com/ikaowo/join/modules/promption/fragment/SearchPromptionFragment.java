package com.ikaowo.join.modules.promption.fragment;

import android.text.TextUtils;

import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.request.SearchRequest;
import com.ikaowo.join.util.Constant;

import retrofit.Call;

/**
 * Created by weibo on 15-12-24.
 */
public class SearchPromptionFragment extends BasePromptionFragment {

  private String value;

  @Override
  protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    //如果没有搜索关键词，则不搜索
    if (TextUtils.isEmpty(value)) {
      return;
    }
    SearchRequest request = new SearchRequest();
    request.value = this.value;
    request.cp = cp;
    request.ps = ps;
    request.type = Constant.SEARCH_TYPE_PROMPTION;
    Call<BaseListResponse<Promption>> call = promptionInterface.searchPromptionList(request.getMap());
    call.enqueue(callback);
  }

  public void search(String search) {
    this.value = search;
    recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
  }

  @Override
  public String getPageName() {
    return "SearchPromptionFragment";
  }
}
