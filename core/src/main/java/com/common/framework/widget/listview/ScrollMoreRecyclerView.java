package com.common.framework.widget.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import com.common.framework.core.JApplication;

/**
 * Created by weibo on 15-12-7.
 */
public class ScrollMoreRecyclerView extends RecyclerView {

  private Context context;
  private LinearLayoutManager layoutManager;
  private OnScrollMoreListener onScrollMoreListener;
  private View emptyView;
  private RelativeLayout emptyViewContainer;

  private boolean isLoadingMore; //是否正在加载更多 默认为false
  private boolean enableLoadMore = true; //是否允许加载更多，默认为true

  private AdapterDataObserver observer = new AdapterDataObserver() {
    @Override public void onChanged() {
      showOrHideEmptyView();
    }

    @Override public void onItemRangeInserted(int positionStart, int itemCount) {
      showOrHideEmptyView();
    }

    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
      showOrHideEmptyView();
    }
  };

  public ScrollMoreRecyclerView(Context context) {
    super(context);
    init(context);
  }

  public ScrollMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    layoutManager = new LinearLayoutManager(context);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    setLayoutManager(layoutManager);

    addOnScrollListener(new OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (isParentSwipeRefresh()) {
          View view = (View) ScrollMoreRecyclerView.this.getParent().getParent();
          view.setEnabled(false);
          if ((layoutManager.findFirstVisibleItemPosition() == 0
              && layoutManager.getChildAt(0).getTop() == 0) || layoutManager.getItemCount() == 0) {
            view.setEnabled(true);
          }

          switch (newState) {
            case SCROLL_STATE_IDLE:
              if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
                  && !isLoadingMore
                  && enableLoadMore) {
                isLoadingMore = true;

                if (onScrollMoreListener != null) {
                  onScrollMoreListener.onLoadMore();
                }
              }
              break;
          }
        }
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });
  }

  private boolean isParentSwipeRefresh() {
    View view = (View) ScrollMoreRecyclerView.this.getParent().getParent();

    return (view != null && view instanceof SwipeRefreshLayout);
  }

  @Override public void setAdapter(Adapter adapter) {

    final Adapter oldAdapter = getAdapter();
    if (oldAdapter != null) {
      oldAdapter.unregisterAdapterDataObserver(observer);
    }
    super.setAdapter(adapter);
    if (adapter != null) {
      adapter.registerAdapterDataObserver(observer);
    }
    showOrHideEmptyView();
  }

  @Override public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    if (null != emptyView && (visibility == GONE || visibility == INVISIBLE)) {
      emptyView.setVisibility(GONE);
    } else {
      showOrHideEmptyView();
    }
  }

  public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
    addEmptyView();
    this.emptyView.setVisibility(View.GONE);
  }

  private void addEmptyView() {
    ViewGroup.LayoutParams lp = this.getLayoutParams();
    ViewParent parent = this.getParent();

    emptyViewContainer = new RelativeLayout(context);
    RelativeLayout.LayoutParams rlp =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT);
    rlp.topMargin = JApplication.getJContext().dip2px(60);
    rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    emptyView.setLayoutParams(rlp);

    ViewGroup group = (ViewGroup) parent;
    int index = group.indexOfChild(this);
    group.removeView(this);
    group.addView(emptyViewContainer, index, lp);
    emptyViewContainer.addView(this);
    emptyViewContainer.addView(emptyView);
    group.invalidate();
  }

  private void showOrHideEmptyView() {
    if (emptyView != null && getAdapter() != null && !isLoadingMore) {
      emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
    }
  }

  public void enableLoadMore(boolean enable) {
    enableLoadMore = enable;
  }

  public void onFinishload() {
    isLoadingMore = false;
  }

  public void setLoadMoreListener(OnScrollMoreListener listener) {
    this.onScrollMoreListener = listener;
  }

  public interface OnScrollMoreListener {
    void onLoadMore();
  }
}
