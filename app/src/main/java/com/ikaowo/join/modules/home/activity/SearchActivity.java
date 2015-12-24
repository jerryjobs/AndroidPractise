package com.ikaowo.join.modules.home.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.ikaowo.join.BaseFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.home.fragment.SearchHistoryFragment;
import com.ikaowo.join.modules.home.fragment.SearchPromptionFragment;
import com.ikaowo.join.util.SharedPreferenceHelper;

/**
 * Created by weibo on 15-12-24.
 */
public class SearchActivity extends BaseFragmentActivity {

  private MenuItem mSearchItem;
  private SearchView searchView;
  private SearchPromptionFragment searchResultFragment;
  private SearchHistoryFragment searchHistoryFragment;
  private String searchKey;
  private final String KEYWORDS = "keywords";
  private SharedPreferenceHelper sharedPreferenceHelper;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    searchResultFragment = new SearchPromptionFragment();
    searchHistoryFragment = new SearchHistoryFragment();

    if (TextUtils.isEmpty(searchKey)) {
      updateFragment(R.id.homesys_frament_container, searchHistoryFragment);
    } else {
      updateFragment(R.id.homesys_frament_container, searchResultFragment);
    }

    sharedPreferenceHelper = SharedPreferenceHelper.getInstance();
    setupOptionMenu();
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_search;
    invalidateOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (mSearchItem != null) {
      return true;
    }
    super.onCreateOptionsMenu(menu);

    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    mSearchItem = menu.findItem(R.id.action_search);

    MenuItemCompat.collapseActionView(mSearchItem);
    MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
      @Override
      public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
      }

      @Override
      public boolean onMenuItemActionCollapse(MenuItem item) {
        hideInput(SearchActivity.this, toolbar);
        finish();
        return false;
      }
    });
    MenuItemCompat.expandActionView(mSearchItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String s) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            sharedPreferenceHelper.saveSearchHistory(SearchActivity.this, s);
            if (searchResultFragment != null) {
              if (TextUtils.isEmpty(searchKey)) {
                searchKey = s;
                Bundle bundle = new Bundle();
                bundle.putString(KEYWORDS, s);
                searchResultFragment.getArguments().putString(KEYWORDS, s);
                updateFragment(R.id.homesys_frament_container, searchHistoryFragment);
              } else {
                updateFragment(R.id.homesys_frament_container, searchResultFragment);
                searchResultFragment.search(s);
              }
            }
            searchView.clearFocus();
            hideInput(SearchActivity.this, searchView);
          }
        }, 200);

        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
          updateFragment(R.id.homesys_frament_container, searchHistoryFragment);
        }
        return false;
      }
    });

    //mSearchView.setFocusable(true);
    searchView.setQuery(searchKey, false);
    searchView.setQueryHint("搜索行业、智客姓名");
    if (!TextUtils.isEmpty(searchKey)) {
      searchView.clearFocus();
    }

    return true;
  }

  public void setSearchQuery(String str) {
    searchView.setQuery(str, true);
  }

  @Override
  protected String getTag() {
    return "SearchActivity";
  }
}
