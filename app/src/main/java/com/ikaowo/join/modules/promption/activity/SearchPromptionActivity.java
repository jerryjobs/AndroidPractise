package com.ikaowo.join.modules.promption.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.ikaowo.join.base.BaseFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.promption.fragment.SearchHistoryFragment;
import com.ikaowo.join.modules.promption.fragment.SearchPromptionFragment;
import com.ikaowo.join.util.SharedPreferenceHelper;
import java.lang.reflect.Field;

/**
 * Created by weibo on 15-12-24.
 */
public class SearchPromptionActivity extends BaseFragmentActivity {

  private MenuItem mSearchItem;
  private SearchView searchView;
  private SearchPromptionFragment searchResultFragment;
  private SearchHistoryFragment searchHistoryFragment;
  private SharedPreferenceHelper sharedPreferenceHelper;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    searchResultFragment = new SearchPromptionFragment();
    searchHistoryFragment = new SearchHistoryFragment();

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    updateFragment(R.id.frament_container, searchResultFragment);
    updateFragment(R.id.frament_container, searchHistoryFragment);

    sharedPreferenceHelper = SharedPreferenceHelper.getInstance();
    setupOptionMenu();
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_search;
    invalidateOptionsMenu();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (mSearchItem != null) {
      return true;
    }
    super.onCreateOptionsMenu(menu);

    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    /*
     * setup the search with white cursor.
     */
    // final int textViewID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null, null);
    final AutoCompleteTextView searchTextView =
        (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);

    try {
      Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
      mCursorDrawableRes.setAccessible(true);
      mCursorDrawableRes.set(searchTextView,
          R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
    } catch (Exception e) {
      e.printStackTrace();
    }
    // end setting the search view.

    mSearchItem = menu.findItem(R.id.action_search);

    MenuItemCompat.collapseActionView(mSearchItem);
    MenuItemCompat.setOnActionExpandListener(mSearchItem,
        new MenuItemCompat.OnActionExpandListener() {
          @Override public boolean onMenuItemActionExpand(MenuItem item) {
            return true;
          }

          @Override public boolean onMenuItemActionCollapse(MenuItem item) {
            hideInput(SearchPromptionActivity.this, toolbar);
            finish();
            return false;
          }
        });
    MenuItemCompat.expandActionView(mSearchItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(final String s) {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            sharedPreferenceHelper.saveSearchHistory(SearchPromptionActivity.this, s);
            if (searchResultFragment != null) {
              updateFragment(R.id.frament_container, searchResultFragment);
              searchResultFragment.search(s);
            }
            searchView.clearFocus();
            hideInput(SearchPromptionActivity.this, searchView);
          }
        }, 200);

        return true;
      }

      @Override public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
          updateFragment(R.id.frament_container, searchHistoryFragment);
        }
        return false;
      }
    });

    //mSearchView.setFocusable(true);
    searchView.setQuery("", false);
    searchView.setQueryHint(getResources().getString(R.string.hint_promption_query));

    return true;
  }

  @Override protected void onResume() {
    if (searchView != null) {
      new Handler().postDelayed(new Runnable() {
        @Override public void run() {
          searchView.clearFocus();
        }
      }, 20);
    }
    super.onResume();
  }

  public void setSearchQuery(String str) {
    searchView.setQuery(str, true);
  }

  @Override protected String getTag() {
    return "SearchPromptionActivity";
  }
}
