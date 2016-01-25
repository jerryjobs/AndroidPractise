package com.ikaowo.join.modules.webview.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.common.framework.core.JApplication;
import com.common.framework.umeng.UmengShareService;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.response.PromptionResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;
import retrofit.Call;

/**
 * Created by weibo on 15-12-31.
 */
public class PromptionDetailWebViewActivity extends WebViewActivity {

  private UmengShareService shareService = new UmengShareService();
  private String title, content, promptionBgUrl;
  private int promptionId;
  private int showOptionMenu;
  private PromptionService promptionService;
  private UserService userService =
      JApplication.getJContext().getServiceByInterface(UserService.class);
  private Dialog dialog;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    promptionService = JApplication.getJContext().getServiceByInterface(PromptionService.class);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);

  }

  @Override protected void getIntentData() {
    if (getIntent().getExtras() == null) {
      return;
    }
    super.getIntentData();
    promptionId = intent.getIntExtra(Constant.PROMPTION_ID, 0);

    PromptionInterface promptionInterface =
        JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);
    Call<PromptionResponse> call = promptionInterface.getPromption(promptionId);
    JApplication.getNetworkManager()
        .async(call, new KwMarketNetworkCallback<PromptionResponse>(this) {

          @Override public void onSuccess(PromptionResponse promptionResponse) {
            Promption promption = null;
            if (promptionResponse == null || (promption = promptionResponse.data) == null) {
              return;
            }

            title = promption.title;
            content = promption.content;
            promptionBgUrl = promption.background;

            boolean promptionShowEdit =
                Constant.PROMPTION_STATE_FAILED.equalsIgnoreCase(promption.state)
                    || Constant.PROMPTION_STATE_NEW.equalsIgnoreCase(promption.state);
            if (userService.isLogined()) {
              if (userService.getUserId() == promption.publishUid) {
                if (promptionShowEdit) {
                  showOptionMenu = Constant.EDIT;
                } else {
                  showOptionMenu = Constant.SHARE;
                }
              } else {
                if (promptionShowEdit) {
                  showOptionMenu = Constant.NONE;
                } else {
                  showOptionMenu = Constant.SHARE;
                }
              }
            } else {
              if (promptionShowEdit) { //
                showOptionMenu = Constant.NONE;
              } else {
                showOptionMenu = Constant.SHARE;
              }
            }

            if (showOptionMenu == Constant.SHARE) {
              menuResId = R.menu.menu_promption_detail_share;
            } else if (showOptionMenu == Constant.EDIT) {
              menuResId = R.menu.menu_promption_detail_edit;
            }
            invalidateOptionsMenu();

            if (Constant.PROMPTION_STATE_FAILED.equalsIgnoreCase(promption.state)
                && promption.publishUid == userService.getUserId()) {
              new Handler().post(new Runnable() {
                @Override public void run() {
                dialog = dialogHelper.createDialog(R.string.dialog_title,
                  R.string.hint_promption_failed, new String[] { "取消", "前往修改" },
                  new View.OnClickListener[] {
                    new View.OnClickListener() {
                      @Override public void onClick(View v) {
                        dialog.dismiss();
                      }
                    }, new View.OnClickListener() {
                      @Override public void onClick(View v) {
                        promptionService.goToEditPromptionActivity(
                            PromptionDetailWebViewActivity.this, promptionId);
                        dialog.dismiss();
                      }
                    }
                  });
                  dialog.show();
                }
              });
            }
          }
        });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_edit:
        promptionService.goToEditPromptionActivity(this, promptionId);
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
