package com.ikaowo.join.modules.common;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JFragmentActivity;
import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.R;
import com.ikaowo.join.model.base.BaseListResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-29.
 */
public abstract class BaseListActivity<T extends BaseListResponse, P> extends JFragmentActivity {

    @Bind(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    protected ScrollMoreRecyclerView recyclerView;

    protected RecyclerViewHelper<T, P> recyclerViewHelper;
    protected int clickedPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        ButterKnife.bind(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerViewHelper = new RecyclerViewHelper<>();
        recyclerViewHelper.init(this, recyclerView, getAdapter(recyclerViewHelper), swipeRefreshLayout);
        recyclerViewHelper.initEmptyView(0, getEmptyHint());

        recyclerViewHelper.supportLoadMore(isSupportLoadMore());

        RecyclerViewHelperInterface recyclerViewHelperImpl = new RecyclerViewHelperInterface<T, P>() {
            @Override
            public boolean checkResponse(JResponse baseResponse) {
                boolean result = baseResponse != null && ((baseResponse instanceof BaseListResponse)
                        && (((T) baseResponse).data) != null);
                if (result) {
                    doAfterGetData();
                }
                return result;
            }

            @Override
            public List<P> getList(T jResponse) {
                List<P> list = jResponse.data;
                return list;
            }

            @Override
            public void sendRequest(NetworkCallback<T> callback, int cp, int ps) {
                sendHttpRequest(callback, cp, ps);
            }

            @Override
            public void performItemClick(int position) {

                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) {
                    return;
                }

                clickedPos = position;

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

    protected abstract boolean isSupportLoadMore();

    protected abstract void sendHttpRequest(NetworkCallback callback, int cp, int ps);

    protected abstract void performCustomItemClick(P p);

    protected abstract JAdapter<P> getAdapter(RecyclerViewHelper<T, P> recyclerViewHelper);

    protected abstract String getEmptyHint();

    protected void doAfterGetData() {

    }
}
