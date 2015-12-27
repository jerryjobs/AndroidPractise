package com.common.framework.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by weibo on 15-12-1.
 */
public abstract class JFragmentActivity extends AppCompatActivity {

  public JDialogHelper dialogHelper;

  protected Toolbar toolbar;
  protected int menuResId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dialogHelper = new JDialogHelper(this);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (menuResId > 0) {
      getMenuInflater().inflate(menuResId, menu);
      return true;
    } else {
      return super.onCreateOptionsMenu(menu);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }

  protected abstract String getTag();

  public void hideInput(Context context, View view) {
    /**
     * solve:
     * Attempt to invoke virtual method 'android.os.IBinder android.view.View.getWindowToken()'
     * on a null object reference
     */
    if (view != null) {
      InputMethodManager inputMethodManager =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
