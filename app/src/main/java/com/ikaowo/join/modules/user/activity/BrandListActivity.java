package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.modules.user.fragment.BrandListFragment;

import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-17.
 */
public class BrandListActivity extends BaseFragmentActivity {

  private UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
  private BrandListFragment brandListFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brand_list);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_brand_list);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setupFragment();
    setupOptionMenu();
  }

  private void setupFragment() {
    boolean choose = getIntent().getBooleanExtra(UserService.CHOOSE, false);
    brandListFragment = new BrandListFragment();

    Bundle bundle = new Bundle();
    bundle.putBoolean(UserService.CHOOSE, choose);
    brandListFragment.setArguments(bundle);
    updateFragment(R.id.fragment_container, brandListFragment);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_choose_brand;
    invalidateOptionsMenu();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_add:
        userService.addBrand(this);
        break;

      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }

  @Override
  protected String getTag() {
    return "BrandListActivity";
  }

}
