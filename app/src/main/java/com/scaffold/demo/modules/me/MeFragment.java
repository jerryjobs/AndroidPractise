package com.scaffold.demo.modules.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scaffold.demo.BaseFragment;
import com.scaffold.demo.R;

/**
 * Created by weibo on 15-12-8.
 */
public class MeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null, false);
        return view;
    }

    @Override
    public String getPageName() {
        return "ME";
    }
}
