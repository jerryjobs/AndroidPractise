package com.ikaowo.join.modules.brand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.R;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.util.Constant;
import retrofit.Call;

/**
 * Created by weibo on 15-12-28.
 */
public class BrandJoinedPromptionListFragment extends BasePromptionDetailListFragment {

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    statePrefix = Constant.JOIN_STATE_PREFIX;
  }

  @Override protected void sendHttpRequest(NetworkCallback callback, int cp, int ps) {
    Call<BaseListResponse<Promption>> call =
        promptionInterface.getBrandJoinedPromptionList(brandId, cp, ps);
    JApplication.getNetworkManager().async(call, callback);
  }

  @Override protected int getIndex() {
    return 1;
  }

  @Override public String getPageName() {
    return "BrandJoinedPromptionListFragment";
  }

  @Override protected String getEmptyHint() {
    return getActivity().getString(R.string.empty_hint_joined_promption);
  }
}
