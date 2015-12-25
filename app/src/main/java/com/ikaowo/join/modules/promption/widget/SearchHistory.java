package com.ikaowo.join.modules.promption.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ikaowo.join.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-10-13.
 */
public class SearchHistory {

  @Bind(R.id.kw_homesys_search_history_txt)
  TextView mSearchHistoryTv;
  Context mContext;
  private View view;
  private DeleteListener mDeleteListener;
  private String text;

  public SearchHistory(Context context) {
    this.mContext = context;
  }


  public View getView(String history) {
    view = LayoutInflater.from(mContext).inflate(R.layout.widget_search_history_view, null);
    ButterKnife.bind(this, view);
    this.text = history;
    mSearchHistoryTv.setText(history);

    return view;
  }

  @OnClick(R.id.kw_homesys_search_history_delete_btn)
  public void deleteHistory() {
    if (mDeleteListener != null) {
      mDeleteListener.delete();
    }
  }

  public String getText() {
    return text;
  }

  public void setDeleteListener(DeleteListener l) {
    this.mDeleteListener = l;
  }

  public void setOnClickListener(View.OnClickListener l) {
    view.setOnClickListener(l);
  }

  public interface DeleteListener {
    public void delete();
  }

}
