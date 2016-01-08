package com.ikaowo.join.modules.webview.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.common.framework.core.JApplication;
import com.common.framework.umeng.UmengShareService;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 15-12-31.
 */
public class PromptionDetailWebViewActivity extends WebViewActivity {

  private UmengShareService shareService = new UmengShareService();
  private String title, content, promptionBgUrl;
  private int promptionId;
  private int showOptionMenu;
  private PromptionService promptionService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    promptionService = JApplication.getJContext().getServiceByInterface(PromptionService.class);
  }

  @Override
  protected void getIntentData() {
    super.getIntentData();

    if (intent.getExtras() != null) {
      title = intent.getStringExtra(Constant.SHAREW_TITLE);
      content = intent.getStringExtra(Constant.SHAREW_SUMMARY);
      promptionBgUrl = intent.getStringExtra(Constant.SHAREW_IMG_URL);
      showOptionMenu = intent.getIntExtra(Constant.SHOW_OPTION_MENU, 0);
      promptionId = intent.getIntExtra(Constant.PROMPTION_ID, 0);
    }

    if (showOptionMenu == Constant.SHARE) {
      menuResId = R.menu.menu_promption_detail_share;
    } else if (showOptionMenu == Constant.EDIT) {
      menuResId = R.menu.menu_promption_detail_edit;
    }
    invalidateOptionsMenu();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_edit:
        promptionService.goToEditPromptionActivigty(this, promptionId);
        break;

      case R.id.action_share:
        shareService.openShareWindow(this, toolbar, title, content, url, promptionBgUrl);
        break;

      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }
}
