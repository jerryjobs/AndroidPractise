package com.ikaowo.join.modules.promption.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.promption.activity.SearchPromptionActivity;
import com.ikaowo.join.modules.promption.widget.SearchHistory;
import com.ikaowo.join.util.SharedPreferenceHelper;
import com.umeng.analytics.MobclickAgent;
import java.util.Collections;
import java.util.List;

/**
 * Created by weibo on 15-10-13.
 */
public class SearchHistoryFragment extends BaseFragment {
  @Bind(R.id.history_view_container) protected LinearLayout mContanerView;

  @Bind(R.id.history_view_clear_btn) protected Button mClearBtn;

  private SharedPreferenceHelper preferenceHelper;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preferenceHelper = SharedPreferenceHelper.getInstance();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search_history, container, false);
    ButterKnife.bind(this, view);

    initSearchHistory();
    return view;
  }

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {
      initSearchHistory();
    }
  }

  @Override public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("SearchHistory");
  }

  @Override public String getPageName() {
    return "SearchHistoryFragment";
  }

  @Override public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart("SearchHistory");
  }

  @OnClick(R.id.history_view_clear_btn) public void clearSearchHistory() {
    preferenceHelper.clearSearcHistory(getActivity());
    mContanerView.removeAllViews();
    mClearBtn.setVisibility(View.GONE);
  }

  private void initSearchHistory() {
    mContanerView.removeAllViews();

    List<String> historyList = preferenceHelper.getSearchHistory(getActivity());
    if (historyList != null && historyList.size() > 0) {
      Collections.reverse(historyList);
      for (final String history : historyList) {
        final SearchHistory searchHistory = new SearchHistory(getActivity());
        final View historyView = searchHistory.getView(history);
        searchHistory.setDeleteListener(new SearchHistory.DeleteListener() {
          @Override public void delete() {
            preferenceHelper.removeHistory(getActivity(), searchHistory.getText());
            mContanerView.removeView(historyView);
            if (mContanerView.getChildCount() == 0) {
              mClearBtn.setVisibility(View.GONE);
            }
          }
        });
        searchHistory.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            ((SearchPromptionActivity) getActivity()).setSearchQuery(searchHistory.getText());
          }
        });
        mContanerView.addView(historyView);
      }
      mClearBtn.setVisibility(View.VISIBLE);
    } else {
      mContanerView.removeAllViews();
      mClearBtn.setVisibility(View.GONE);
    }
  }
}
