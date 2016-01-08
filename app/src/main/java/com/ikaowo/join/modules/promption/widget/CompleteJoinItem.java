package com.ikaowo.join.modules.promption.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.ikaowo.join.R;
import com.ikaowo.join.model.JoinedUser;

/**
 * Created by weibo on 16-1-8.
 */
public class CompleteJoinItem extends LinearLayout {

  private Context context;
  private ItemClickInterface itemClickInterface;
  private View view;

  public CompleteJoinItem(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public CompleteJoinItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init();
  }

  private void init() {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    view = layoutInflater.inflate(R.layout.widget_complte_join_item, this);
  }

  public void setLeft(final JoinedUser joinedUser) {
    setupLeftOrRightItem(joinedUser, R.id.stub_left, false);
  }

  public void setRight(JoinedUser joinedUser) {
    setupLeftOrRightItem(joinedUser, R.id.stub_right, true);
  }

  private void setupLeftOrRightItem(final JoinedUser joinedUser, int stubLeft, boolean right) {
    ViewStub viewStub = (ViewStub) view.findViewById(stubLeft);
    View inflated = viewStub.inflate();

    if (joinedUser == null) {
      inflated.setVisibility(INVISIBLE);
      return;
    }

    final ImageView iconIv = (ImageView) inflated.findViewById(R.id.state_icon);
    ImageView brandIv = (ImageView) inflated.findViewById(R.id.brand_icon);
    TextView brandTv = (TextView) inflated.findViewById(R.id.brand_name);

    ImageLoader imageLoader = JApplication.getImageLoader();
    imageLoader.loadImage(brandIv, joinedUser.brandIcon,
      JApplication.getJContext().dip2px(40), JApplication.getJContext().dip2px(30), R.drawable.brand_icon_default);
    brandTv.setText(joinedUser.brandName);
    inflated.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        iconIv.setSelected(!iconIv.isSelected());
        if (itemClickInterface != null) {
          itemClickInterface.itemClicked(joinedUser.uId, iconIv.isSelected());
        }
      }
    });
  }

  public void setItemClickInterface(ItemClickInterface itemClickInterface) {
    this.itemClickInterface = itemClickInterface;
  }

  public interface ItemClickInterface {
    void itemClicked(int id, boolean selected);
  }

}
