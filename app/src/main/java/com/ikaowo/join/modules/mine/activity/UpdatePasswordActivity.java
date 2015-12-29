package com.ikaowo.join.modules.mine.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.common.framework.core.JApplication;
import com.common.framework.util.JToast;
import com.ikaowo.join.BaseFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.UpdatePasswordRequest;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;

/**
 * Created by weibo on 15-12-29.
 */
public class UpdatePasswordActivity extends BaseFragmentActivity implements DeletableEditTextView.TextChangeListener {

  @Bind(R.id.new_password)
  DeletableEditTextView  newPasswordEt;

  @Bind(R.id.old_password)
  DeletableEditTextView oldPasswordEt;

  private String newPassowrd;
  private String oldPassword;

  private UserService userService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_passwd);
    ButterKnife.bind(this);

    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.title_activity_update_password);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    setupOptionMenu();
    setupView();
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  private void setupView() {
    newPasswordEt.setSingleLine();
    newPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    oldPasswordEt.setSingleLine();
    oldPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    newPasswordEt.setTextChangeListener(this);
    oldPasswordEt.setTextChangeListener(this);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean oldPassworldInputed = !TextUtils.isEmpty(oldPassword = oldPasswordEt.getText().toString().trim());
    boolean newPasswordInputed = !TextUtils.isEmpty(newPassowrd = newPasswordEt.getText().toString().trim());
    boolean passwordEquals = newPasswordInputed && oldPassworldInputed;
    boolean passwordAtLast6 = newPassowrd.length() >=6;
    menu.getItem(0).setEnabled(passwordEquals && passwordAtLast6);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_submit:
        submit();
        break;
      default:
        super.onOptionsItemSelected(item);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void submit() {
    UpdatePasswordRequest request = new UpdatePasswordRequest();
    request.new_password = newPassowrd;
    request.old_password = oldPassword;

    UserInterface userInterface = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    Call<BaseResponse> call = userInterface.updatePasswd(request);
    JApplication.getNetworkManager().async(this, Constant.PROCESSING, call, new KwMarketNetworkCallback(this) {
      @Override
      public void onSuccess(Object o) {
        JToast.toastShort("修改密码成功，请重新登录");
        finish();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            userService.logout(UpdatePasswordActivity.this);
          }
        }, 300);

      }
    });
  }

  @Override
  protected String getTag() {
    return "UpdatePasswordActivity";
  }

  @Override
  public void onChanged(Editable s) {
    invalidateOptionsMenu();
  }
}
