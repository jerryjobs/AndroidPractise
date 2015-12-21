package com.ikaowo.join.modules.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-8.
 */
public class MessageFragment extends BaseFragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_message, null, false);
    return view;
  }

  @Override
  public String getPageName() {
    return "Message";
  }
}
