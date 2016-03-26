package com.ikaowo.join.modules.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JAdapter;
import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.base.BaseFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.model.base.BaseListResponse;
import java.util.List;

/**
 * Created by weibo on 15-12-29.
 */
public abstract class BaseListFragment<T extends BaseListResponse, P> extends BaseFragment {

  @Bind(R.id.swipe_refresh_layout) protected SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.recycler_view) protected ScrollMoreRecyclerView recyclerView;

  protected RecyclerViewHelper<T, P> recyclerViewHelper;
  protected int clicedPos;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getViewId(), null, false);
    ButterKnife.bind(this, view);

    setupRecyclerView();

    return view;
  }

  private void setupRecyclerView() {
    recyclerViewHelper = new RecyclerViewHelper<>();
    recyclerViewHelper.init(getActivity(), recyclerView, getAdapter(recyclerViewHelper),
        swipeRefreshLayout);
    recyclerViewHelper.initEmptyView(0, getEmptyHint());
    recyclerViewHelper.supportLoadMore(isSupportLoadMore());

    RecyclerViewHelperInterface recyclerViewHelperImpl = new RecyclerViewHelperInterface<T, P>() {
      @Override public boolean checkResponse(JResponse baseResponse) {
        boolean result = baseResponse != null && ((baseResponse instanceof BaseListResponse)
            && (((BaseListResponse) baseResponse).data) != null);

        if (result) {
          doAfterGetData((T) baseResponse);
        }
        return result;
      }

      @Override public List<P> getList(T jResponse) {
        List<P> list = jResponse.data;
        processObjList(list);
        return list;
      }

      @Override public void sendRequest(NetworkCallback<T> callback, int cp, int ps) {
        sendHttpRequest(callback, cp, ps);
      }

      @Override public void performItemClick(int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
          return;
        }

        clicedPos = position;
        List objList = ((JAdapter<P>) adapter).getObjList();
        if (objList == null) {
          return;
        }

        P obj = (P) objList.get(position);
        performCustomItemClick(obj);
      }
    };
    recyclerViewHelper.setHelperInterface(recyclerViewHelperImpl);
    recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  protected abstract boolean isSupportLoadMore();

  protected abstract void sendHttpRequest(NetworkCallback call, int cp, int ps);

  protected abstract void performCustomItemClick(P p);

  protected abstract JAdapter<P> getAdapter(RecyclerViewHelper<T, P> recyclerViewHelper);

  protected abstract String getEmptyHint();

  protected int getViewId() {
    return R.layout.common_list_layout;
  }

  protected void processObjList(List<P> l) {
  }

  protected void doAfterGetData(T t) {
  }
}
