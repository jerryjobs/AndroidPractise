package com.common.framework.widget.listview;

import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;

import java.util.List;

/**
 * Created by weibo on 15-12-7.
 */
public interface RecyclerViewHelperInterface<T extends JResponse, P> {

  boolean checkResponse(JResponse baseResponse);

  List<P> getList(T t);

  void sendRequest(NetworkCallback<T> callback, int cp, int ps);

  void performItemClick(int position);
}
