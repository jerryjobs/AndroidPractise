package com.ikaowo.join.common.widget.draggridview;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.common.framework.core.JApplication;
import com.common.framework.core.JFragmentActivity;
import com.component.photo.FullImageView;
import com.component.photo.PhotoService;
import com.ikaowo.join.R;
import com.ikaowo.join.eventbus.AddGridViewImageCallback;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-22.
 */
public class DragGridItemAdapter extends BaseAdapter {

  private ItemImageObj item = null;
  private Context context;
  private int width;
  private List<ItemImageObj> thumbList = new ArrayList<>();
  private int maxCount;
  private PhotoService photoService;
  private boolean anySize = false;

  public DragGridItemAdapter(Context context, List<ItemImageObj> items, int maxCount) {
    this.context = context;
    this.thumbList = items;
    this.width = (JApplication.getJContext().getScreenWidth()
        - JApplication.getJContext().dip2px(12)
        - JApplication.getJContext().dip2px(24)) / 4; // 12　marginLeft + marginRight
    this.maxCount = maxCount;
    this.photoService = new PhotoService(context);
  }

  public DragGridItemAdapter(Context context, List<ItemImageObj> items, int maxCount,
      boolean anySize) {
    this(context, items, maxCount);
    this.anySize = anySize;
  }

  @Override public int getCount() {
    return thumbList.size();
  }

  @Override public Object getItem(int position) {
    return thumbList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemViewType(int position) {
    return thumbList.get(position).type == 0 ? ItemImageObj.TYPE_ADD : ItemImageObj.TYPE_NORMAL;
  }

  @Override public int getViewTypeCount() {
    return 2;
  }

  @Override public View getView(final int position, View convertView, ViewGroup parent) {
    NormalHolderView normalHolder = null;
    AddHolderView addHolder = null;

    int type = getItemViewType(position);
    item = thumbList.get(position);

    switch (type) {
      //增加
      case ItemImageObj.TYPE_ADD:

        convertView =
            LayoutInflater.from(context).inflate(R.layout.widget_drage_gridview_add, null);
        addHolder = new AddHolderView(convertView);
        convertView.setTag(addHolder);
        break;

      //普通类型
      case ItemImageObj.TYPE_NORMAL:
        convertView =
            LayoutInflater.from(context).inflate(R.layout.widget_drage_gridview_item, null);

        convertView.setTag(normalHolder);
        normalHolder = new NormalHolderView(convertView);
        normalHolder.deleteImg.setTag(position);
        break;
    }

    AbsListView.LayoutParams alp = new AbsListView.LayoutParams(width, width);
    convertView.setLayoutParams(alp);
    if (normalHolder != null) {
      normalHolder.thumbImg.setImgUri(item.uri);
      normalHolder.thumbImg.setImgUrl(item.thumbImg);

      int targetWidth = width - JApplication.getJContext().dip2px(12);
      int targetHeight = width - JApplication.getJContext().dip2px(12);
      if (item.uri != null) {
        Picasso.with(context)
            .load(item.uri)
            .centerCrop()
            .resize(targetWidth, targetHeight)
            .into(normalHolder.thumbImg);
      } else if (!TextUtils.isEmpty(item.thumbImg)) {
        JApplication.getImageLoader()
            .loadImage(normalHolder.thumbImg, item.thumbImg, targetWidth, targetHeight,
                R.drawable.brand_icon_default);
      }
    }

    return convertView;
  }

  void addAddItem(List<ItemImageObj> items) {
    ItemImageObj thumb = new ItemImageObj();
    thumb.type = ItemImageObj.TYPE_ADD;
    items.add(thumb);
  }

  /**
   * 正常带删除
   *
   * @author Administrator
   */
  class NormalHolderView {
    //删除和图片
    @Bind(R.id.widget_draggrid_delete_icon) ImageView deleteImg;
    @Bind(R.id.expertsys_thumb_img) FullImageView thumbImg;

    private Dialog dialog;

    public NormalHolderView(View view) {
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.widget_draggrid_delete_layout) public void deleteImg() {

      View.OnClickListener listeners[] = new View.OnClickListener[] {
          new View.OnClickListener() {
            @Override public void onClick(View view) {
              if (dialog != null) dialog.dismiss();
            }
          }, new View.OnClickListener() {
        @Override public void onClick(View view) {
          int pos = (int) deleteImg.getTag();
          thumbList.remove(pos);
          if (thumbList.size() == maxCount - 1
              && thumbList.get(thumbList.size() - 1).type != ItemImageObj.TYPE_ADD) {
            addAddItem(thumbList);
          }
          notifyDataSetChanged();
          dialog.dismiss();
        }
      }
      };

      if (context instanceof JFragmentActivity) {
        dialog = ((JFragmentActivity) context).dialogHelper.createDialog(context, "提示", "确认删除么？",
            new String[] { "取消", "确定" }, listeners);
        dialog.show();
      }
    }
  }

  /**
   * 增加
   *
   * @author Administrator
   */
  class AddHolderView {

    @Bind(R.id.img) ImageView imageView;

    public AddHolderView(View view) {
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.expertsys_add_thumb) public void addThumb(View view) {
      ((JFragmentActivity) context).hideInput(context, view);
      if (anySize) {
        photoService.takePhotoAnySize(context, view);
      } else {
        photoService.takePhoto(context, view, null);
      }

      EventBus.getDefault().post(new AddGridViewImageCallback() {
        @Override public boolean clicked() {
          return true;
        }
      });
    }
  }
}