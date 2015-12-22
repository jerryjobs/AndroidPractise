package com.ikaowo.join.modules.home.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.draggridview.DragGridView;
import com.ikaowo.join.common.widget.draggridview.DrawGridItemAdapter;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-22.
 */
public class AddPromotionActivity extends BaseActivity {

  @Bind(R.id.add_promption_bg_container)
  FrameLayout promptionBgContainer;

  @Bind(R.id.promption_imgs_container)
  DragGridView promptionImgsContainer;

  private DrawGridItemAdapter itemAdapter;
  private List<ItemImageObj> list = new ArrayList<>();
  private final int MAx_COUNT = 4;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_promotion);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getString(R.string.title_activity_add_promotion));
    setSupportActionBar(toolbar);

    setupView();
    setupOptionMenu();
  }

  private void setupView() {
    LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) promptionBgContainer.getLayoutParams();
    llp.width = JApplication.getJContext().getScreenWidth();
    llp.height = llp.width * 9 / 16;

    addAddItem(list);
    itemAdapter = new DrawGridItemAdapter(this, list, MAx_COUNT);
    promptionImgsContainer.setAdapter(itemAdapter);
  }

  void addAddItem(List<ItemImageObj> items) {
    ItemImageObj thumb = new ItemImageObj();
    thumb.type = ItemImageObj.TYPE_ADD;
    items.add(thumb);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  @Override
  protected String getTag() {
    return "AddPromotionActivity";
  }
}
