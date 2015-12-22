package com.ikaowo.join.common.widget.draggridview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.common.framework.core.JApplication;
import com.common.framework.core.JFragmentActivity;
import com.component.photo.PhotoService;
import com.ikaowo.join.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-22.
 */
public class DrawGridItemAdapter extends BaseAdapter {

  private ItemImageObj item = null;
  private Context context;
  private int width;
  private List<ItemImageObj> thumbList = new ArrayList<>();
  private int maxCount;
  private GridViewItemDeleteListener deleteListener;
  private PhotoService photoService;

  public DrawGridItemAdapter(Context context, List<ItemImageObj> items, int maxCount) {
    this.context = context;
    this.thumbList.addAll(items);
    this.width  = JApplication.getJContext().getScreenWidth() / 4;
    this.maxCount = maxCount;
    this.photoService = new PhotoService(context);
  }

  @Override
  public int getCount() {
    return thumbList.size();
  }

  @Override
  public Object getItem(int position) {
    return thumbList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemViewType(int position) {
    return thumbList.get(position).type;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    NormalHolderView normalHolder = null;
    AddHolderView addHolder = null;

    int type = getItemViewType(position);
    item = thumbList.get(position);

    switch (type) {
      //增加类型
      case ItemImageObj.TYPE_ADD:

        convertView = LayoutInflater.from(context)
                .inflate(R.layout.widget_drage_gridview_add, null);
        convertView.setTag(addHolder);
        addHolder = new AddHolderView(convertView);
        break;

      //普通类型
      case ItemImageObj.TYPE_NORMAL:
        convertView = LayoutInflater.from(context)
                .inflate(R.layout.widget_drage_gridview_item, null);

        convertView.setTag(normalHolder);
        normalHolder = new NormalHolderView(convertView);
        normalHolder.deleteImg.setTag(position);
        break;
    }

    if (normalHolder != null) {
      JApplication.getImageLoader().loadImage(
              normalHolder.thumbImg, item.thumbImg, width, width, R.drawable.brand_icon_default);
    }
    return convertView;
  }

  public void setDeleteListener(GridViewItemDeleteListener listener) {
    this.deleteListener = listener;
  }

  /**
   * 正常带删除
   *
   * @author Administrator
   */
  class NormalHolderView {
    //删除和图片
    @Bind(R.id.expertsys_thumb_delete)
    ImageView deleteImg;
    @Bind(R.id.expertsys_thumb_img)
    ImageView thumbImg;

    private Dialog dialog;

    public NormalHolderView(View view) {
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.expertsys_thumb_delete)
    public void deleteThumb(final View view) {

      View.OnClickListener listeners[] = new View.OnClickListener[]{
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (dialog != null)
              dialog.dismiss();
          }
        },
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            int pos = (int) view.getTag();
            thumbList.remove(pos);
            if (thumbList.size() == maxCount - 1
                    && thumbList.get(thumbList.size() - 1).type != ItemImageObj.TYPE_ADD) {
              addAddItem(thumbList);
              if (deleteListener != null) {
                deleteListener.setGridViewLastItemSwaple(false);
              }
            }
            notifyDataSetChanged();
          }
        }
      };

      if (context instanceof JFragmentActivity) {
        dialog = ((JFragmentActivity)context).dialogHelper.createDialog(context,
          "提示",
          "确认删除么？",
          new String[]{"取消", "确定"}, listeners);
        dialog.show();
      }
    }
  }

  void addAddItem(List<ItemImageObj> items) {
    ItemImageObj thumb = new ItemImageObj();
    thumb.type = ItemImageObj.TYPE_ADD;
    items.add(thumb);
  }

  /**
   * 增加
   *
   * @author Administrator
   */
  class AddHolderView {

    public AddHolderView(View view) {
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.expertsys_add_thumb)
    public void addThumb(View view) {
      photoService.takePhoto(context, view, null, true);
    }
  }

  public interface GridViewItemDeleteListener {
    void setGridViewLastItemSwaple(boolean swaple);
  }
}