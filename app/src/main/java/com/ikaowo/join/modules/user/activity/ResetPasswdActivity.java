package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.util.VerifyCodeHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-17.
 */
public class ResetPasswdActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.phone)
    DeletableEditTextView phoneEt;

    @Bind(R.id.verify_btn)
    TextView getVerifyBtn;

    @Bind(R.id.verify_code)
    DeletableEditTextView verifyCodeEt;

    @Bind(R.id.password)
    DeletableEditTextView passwordEt;

    private VerifyCodeHelper verifyCodeHelper;
    private String userName;
    private String password;
    private String vcode;
    private UserService userService =
            JApplication.getJContext().getServiceByInterface(UserService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_resetpasswd);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupView();
        setupOptionMenu();
    }

    private void setupView() {

        phoneEt.addTextChangedListener(this);
        verifyCodeEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);

        verifyCodeHelper = new VerifyCodeHelper(this, phoneEt, getVerifyBtn);
        verifyCodeHelper.initVerifyBtn();

        phoneEt.setSingleLine();
        passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        verifyCodeEt.setSingleLine();
    }

    private void setupOptionMenu() {
        menuResId = R.menu.menu_add_submit;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        userName = phoneEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        vcode = verifyCodeEt.getText().toString().trim();
        menu.getItem(0).setEnabled(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                resetPasswd();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetPasswd() {
        userService.resetPasswd(this, userName, vcode, password);
    }

    @Override
    protected String getTag() {
        return "ResetPasswdActivity";
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        invalidateOptionsMenu();

        userName = phoneEt.getText().toString().trim();
        if (!TextUtils.isEmpty(userName) && userName.length() == 11) {
            if (!getVerifyBtn.isEnabled()) {
                getVerifyBtn.setEnabled(true);
            }
        } else {
            if (getVerifyBtn.isEnabled()) {
                getVerifyBtn.setEnabled(false);
            }
        }
    }
}
