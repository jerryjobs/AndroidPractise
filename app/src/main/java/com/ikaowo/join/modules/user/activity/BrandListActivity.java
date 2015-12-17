package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.AlphaSlideBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-17.
 */
public class BrandListActivity extends BaseActivity implements AlphaSlideBar.OnTouchingLetterChangedListener {

    @Bind(R.id.indicator)
    TextView indicatorTv;
    @Bind(R.id.slidebar)
    AlphaSlideBar slideBar;

    private UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        slideBar.setOnTouchingLetterChangedListener(this);

        setOptionMenu();
    }

    private void setOptionMenu() {
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

    @Override
    public void onTouchingLetterChanged(String s) {
        if (indicatorTv.getVisibility() != View.VISIBLE) {
            indicatorTv.setVisibility(View.VISIBLE);
        }
        indicatorTv.setText(s);
    }

    @Override
    public void onTouchUp() {
        indicatorTv.setVisibility(View.GONE);
    }
}
