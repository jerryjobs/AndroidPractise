package com.common.framework.widget.listview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.core.R;
import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;

import retrofit.Callback;

/**
 * Created by weibo on 15-12-7.
 */
public class RecyclerViewHelper<T extends JResponse, P> {

    protected int pe;
    protected int cp;//
    protected int ps = 10;

    private RecyclerViewHelperInterface helperInterface;
    private ScrollMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GetDataListener listener;
    private JAdapter<P> adapter;
    private NetworkCallback<T> callback;
    private Context context;


    public enum Action {
        INIT, REFRESH, LOADMORE
    }

    //step #1
    public void init(Context context, final ScrollMoreRecyclerView recyclerView,
                     JAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.recyclerView.setAdapter(adapter);
        this.listener = new GetDataListener() {
            @Override
            public void onStart(Action action) {
                startRefresh(true);
                sendRequestAndProcess(action);
            }
            @Override
            public void onStop() {
                startRefresh(false);
                recyclerView.onFinishload();
            }
        };

        this.swipeRefreshLayout = swipeRefreshLayout;
        if (this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData(Action.REFRESH);
                }
            });
        }
    }

    //step #2
    public void initEmptyView(int drawId, String hint) {
        View emptyView = LayoutInflater.from(JApplication.getInstance().getApplicationContext())
                .inflate(R.layout.view_empty, null);
        if (drawId > 0) {
            ((ImageView) emptyView.findViewById(R.id.kw_common_emptyview_icon)).setImageResource(drawId);
        }

        if (!TextUtils.isEmpty(hint)) {
            ((TextView) emptyView.findViewById(R.id.kw_common_emptyview_text)).setText(hint);
        }
        recyclerView.setEmptyView(emptyView);
    }

    //step #3
    public void sendRequestAndProcess(final Action action) {
        if (helperInterface == null) {
            Log.e("RecyclerHelper", "You have to setHelperInterface at first");
            return;
        }
        if (callback == null ) {
            callback = new NetworkCallback<T>(context) {
                @Override
                public void onSuccess(T t) {
                    listener.onStop();
                    if (!helperInterface.checkResponse(t)) {
                        return;
                    }
                    recyclerView.enableLoadMore(((cp * ps) + helperInterface.getList(t).size()) < t.getTotals());
                    onFinishLoadData();
                    if (!((NetworkCallback)callback).getAction().equals(Action.LOADMORE)) {
                        adapter.setPostList(helperInterface.getList(t));
                    } else {
                        adapter.appendPostList(helperInterface.getList(t));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    listener.onStop();
                }
            };
        }

        if (!action.equals(Action.LOADMORE)) {
            pe = 0;
            cp = 0;
        }
        ((NetworkCallback)callback).setAction(action);
        helperInterface.sendRequest(callback, cp, ps);
    }

    private void getData(Action action) {
        if (listener != null) {
            listener.onStart(action);
        }
    }

    private void startRefresh(final boolean start) {
        if (swipeRefreshLayout == null ) {
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(start);
            }
        });
    }

    private void onFinishLoadData() {
        cp += 1;
    }

    public void setHelperInterface(RecyclerViewHelperInterface helperInterface) {
        this.helperInterface = helperInterface;
    }

    //设置对应的RecyclerView是否支持加载更多操作
    public void supportLoadMore(boolean b) {
        if (b) {
            recyclerView.setLoadMoreListener(new ScrollMoreRecyclerView.OnScrollMoreListener() {
                @Override
                public void onLoadMore() {
                    getData(Action.LOADMORE);
                }
            });
        } else {
            recyclerView.setLoadMoreListener(null);
            recyclerView.enableLoadMore(false);
        }
    }


    public interface GetDataListener {
        void onStart(Action action);
        void onStop();
    }

    public class JListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public JListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (helperInterface != null) {
                helperInterface.performItemClick(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public JAdapter<P> getAdapter() {
        return adapter;
    }

    public RecyclerViewHelperInterface getRecyclerHelperImpl() {
        return helperInterface;
    }
}
