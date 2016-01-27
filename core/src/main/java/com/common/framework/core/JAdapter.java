package com.common.framework.core;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leiweibo on 5/13/15.
 */
public class JAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int EMPTYVIEW = 0;
    protected static final int NORMALVIEW = 1;
    protected List<T> objList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (objList.size() > 0) {
            return NORMALVIEW;
        } else {
            return EMPTYVIEW;
        }
    }

    public void addPostToListFirst(List<T> list) {
        notifyDataSetChanged();
    }

    public void setPostList(List<T> list) {
        objList.clear();
        objList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendPostList(List<T> list) {
        objList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return objList.size();
    }

    public List<T> getObjList() {
        return objList;
    }

}
