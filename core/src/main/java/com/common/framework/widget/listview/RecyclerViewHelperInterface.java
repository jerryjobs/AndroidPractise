package com.common.framework.widget.listview;

import com.common.framework.model.JResponse;

import java.util.List;

import retrofit.Callback;

/**
 * Created by weibo on 15-12-7.
 */
public interface RecyclerViewHelperInterface<T extends JResponse, P> {

    boolean checkResponse(JResponse baseResponse);
    List<P> getList(T t);
    void sendRequest(Callback<T> callback, int cp, int ps);
    void performItemClick(int position);
}
