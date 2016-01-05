package com.ikaowo.join.modules.promption.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.util.JToast;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseFragmentActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.draggridview.DragGridItemAdapter;
import com.ikaowo.join.common.widget.draggridview.DragGridView;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.eventbus.JoinedActivityCallback;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.JoinRequest;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.QiniuUploadHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinActivity extends BaseFragmentActivity implements PhotoService.UploadFinishListener,
        DragGridItemAdapter.GridViewItemDeleteListener, DragGridView.OnChanageListener{

  @Bind(R.id.promption_content)
  AppCompatEditText promptContentEt;
  @Bind(R.id.content_remaing)
  TextView contentRemainingTv;
  @Bind(R.id.promption_imgs_container)
  DragGridView dragGridView;

  private final int MAX_COUNT = 6;
  private final int MAX_CONTENT_LENGTH = 140;

  private int promptionId = -1;
  private String content;
  private List<ItemImageObj> list = new ArrayList<>(); //图标icon
  private DragGridItemAdapter itemAdapter;

  private UserService userService;
  private PromptionInterface promptionInterface;
  private QiniuUploadHelper qiniuUploadHelper;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_join);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    promptionInterface = JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);
    qiniuUploadHelper = new QiniuUploadHelper();


    try {
      List<String> pathList = getIntent().getData().getPathSegments();
      promptionId = Integer.valueOf(pathList.get(pathList.size() - 1));
      if (promptionId <= 0) {
        promptionId = getIntent().getExtras().getInt(Constant.PROMPTION_ID, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (promptionId < 0) {
      dialogHelper.createDialog(this, "注意", "推广信息不正确，请返回重试", new String[] {"确定"}, new View.OnClickListener[] {
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  finish();
                }
              }
      }).show();
    }

    setupView();
    setupOptionMenu();
    addAddItem(list);
  }

  private void setupView() {
    contentRemainingTv.setText(getString(R.string.content_remaing, 0, MAX_CONTENT_LENGTH));
    promptContentEt.setFilters(new InputFilter[]{
            new InputFilter.LengthFilter(MAX_CONTENT_LENGTH)
    });
    promptContentEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        contentRemainingTv.setText(getString(R.string.content_remaing, MAX_CONTENT_LENGTH - s.length(), MAX_CONTENT_LENGTH));
        invalidateOptionsMenu();
      }
    });

    itemAdapter = new DragGridItemAdapter(this, list, MAX_COUNT);
    itemAdapter.setDeleteListener(this);
    dragGridView.setAdapter(itemAdapter);
    dragGridView.setSwapLastItem(false);
    dragGridView.setOnChangeListener(this);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    content = promptContentEt.getText().toString().trim();
    boolean enable = !TextUtils.isEmpty(content)
            && content.length() <= MAX_CONTENT_LENGTH  && userService.isLogined();
    menu.getItem(0).setEnabled(enable);
    return true;
  }

  void addAddItem(List<ItemImageObj> items) {
    ItemImageObj thumb = new ItemImageObj();
    thumb.type = ItemImageObj.TYPE_ADD;
    items.add(thumb);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_submit:
        submit();
        break;

      default:
        super.onOptionsItemSelected(item);
        break;
    }
    return true;
  }

  private void submit() {
    if (!userService.isLogined()) {
      userService.goToSignin(this);
      return;
    }
    JoinRequest request = new JoinRequest();
    request.aci_id = promptionId;
    request.u_id = userService.getUserId();
    request.comment = content;
    int addPos = -1;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).type == ItemImageObj.TYPE_ADD) {
        addPos = i;
        break;
      }
    }
    if (addPos >= 0) {
      list.remove(addPos);
    }
    request.aci_tumblrs = list;
    Call<BaseResponse> call = promptionInterface.join(request);
    JApplication.getNetworkManager().async(this, Constant.PROCESSING, call, new KwMarketNetworkCallback<BaseResponse>(this) {
      @Override
      public void onSuccess(BaseResponse o) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            EventBus.getDefault().post(new JoinedActivityCallback() {
              @Override
              public boolean joined() {
                return true;
              }
            });
          }
        }, 500);
        JToast.toastShort("参加活动成功");
        finish();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }

  @Override
  protected String getTag() {
    return "JoinActivity";
  }

  @Override
  public void onChange(int from, int to) {
    //直接交互item
    //这里的处理需要注意下
    if (from < to) {
      for (int i = from; i < to; i++) {
        Collections.swap(list, i, i + 1);
      }
    } else if (from > to) {
      for (int i = from; i > to; i--) {
        Collections.swap(list, i, i - 1);
      }
    }

    itemAdapter.notifyDataSetChanged();
  }

  @Override
  public void onUpLoadImageFinish(String imgUrl, Uri imgUri) {
    int imgSize = list.size();
    ItemImageObj item = new ItemImageObj();
    item.thumbImg = imgUrl;

    if (imgSize == MAX_COUNT) {
      if (list.get(imgSize - 1).type != ItemImageObj.TYPE_ADD) {
        return;
      } else {
        list.set(imgSize - 1, item);
        dragGridView.setSwapLastItem(true);
      }
    } else {
      list.add(imgSize - 1, item);
      dragGridView.setSwapLastItem(false);
    }

    itemAdapter.notifyDataSetChanged();
  }

  @Override
  public void onUpLoadImageFailed() {

  }

  @Override
  public void setGridViewLastItemSwaple(boolean swaple) {
    dragGridView.setSwapLastItem(swaple);
  }
}
