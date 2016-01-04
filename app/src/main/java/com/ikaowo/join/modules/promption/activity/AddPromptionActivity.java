package com.ikaowo.join.modules.promption.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.util.JToast;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.draggridview.DragGridItemAdapter;
import com.ikaowo.join.common.widget.draggridview.DragGridView;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.PromptionRequest;
import com.ikaowo.join.modules.user.helper.InputFiledHelper;
import com.ikaowo.join.modules.user.widget.CustomEditTextView;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.DateTimeHelper;
import com.ikaowo.join.util.QiniuUploadHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;

/**
 * Created by weibo on 15-12-22.
 */
public class AddPromptionActivity extends BaseActivity
  implements DragGridItemAdapter.GridViewItemDeleteListener,
  PhotoService.UploadFinishListener,
  DragGridView.OnChanageListener, TextWatcher {

  private final int MAX_COUNT = 6;
  private final int MAX_CONTENT_LENGTH = 140;
  private final int MAX_TITLE_LENGTH = 15;
  @Bind(R.id.add_promption_bg_container)
  FrameLayout promptionBgContainer;
  @Bind(R.id.promption_bg)
  ImageView promptionBgImg;
  @Bind(R.id.promption_title)
  AppCompatEditText promptTitleEt;
  @Bind(R.id.promption_content)
  AppCompatEditText promptContentEt;
  @Bind(R.id.content_remaing)
  TextView contentRemainingTv;
  @Bind(R.id.add_promption_bg_btn)
  RelativeLayout addPromptionBgBtn;
  @Bind(R.id.promption_imgs_container)
  DragGridView promptionImgsContainer;
  @Bind(R.id.promption_time)
  CustomEditTextView promptionTimeTv;
  @Bind(R.id.promption_address)
  CustomEditTextView promptionAddressTv;
  @Bind(R.id.promption_end_date)
  CustomEditTextView endDateEt;
  @Bind(R.id.promption_notes_content)
  AppCompatEditText noteEt;

  private TextView endDateTv;
  private EditText timeInputEt;
  private EditText addInputEt;
  private EditText endTimeEt;
  private int targetImgBgWidth, targetImgBgHeight;
  private DragGridItemAdapter itemAdapter;
  private QiniuUploadHelper qiniuUploadHelper;
  private InputFiledHelper inputHelper;
  private DateTimeHelper dateTimeHelper;
  private PhotoService photoService;
  private UserService userService;
  private ClickPos clickedPos;

  private String promptionBg;
  private String promptionTitle;
  private String promptionContent;
  private List<ItemImageObj> list = new ArrayList<>(); //图标icon
  private String promptionTime;
  private String promptionAddress;
  private String promptionEndDate;
  private String promptNotes;
  private String endDate;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_promotion);
    ButterKnife.bind(this);

    qiniuUploadHelper = new QiniuUploadHelper();
    inputHelper = new InputFiledHelper();
    dateTimeHelper = new DateTimeHelper();
    photoService = new PhotoService(this);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getString(R.string.title_activity_add_promotion));
    setSupportActionBar(toolbar);

    displayHomeAsIndicator(0);

    setupView();
    setupOptionMenu();
  }

  private void setupView() {
    LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) promptionBgContainer.getLayoutParams();
    targetImgBgWidth = JApplication.getJContext().getScreenWidth();
    targetImgBgHeight = targetImgBgWidth * 9 / 16;
    llp.width = targetImgBgWidth;
    llp.height = targetImgBgHeight;

    addAddItem(list);
    itemAdapter = new DragGridItemAdapter(this, list, MAX_COUNT);
    itemAdapter.setDeleteListener(this);
    promptionImgsContainer.setAdapter(itemAdapter);
    promptionImgsContainer.setSwapLastItem(false);
    promptionImgsContainer.setOnChangeListener(this);

    promptTitleEt.addTextChangedListener(this);
    promptTitleEt.setSingleLine();
    promptTitleEt.setFilters(new InputFilter[]{
      new InputFilter.LengthFilter(MAX_TITLE_LENGTH)
    });
    promptContentEt.addTextChangedListener(this);

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
      }
    });

    timeInputEt = inputHelper.getEditText(this, R.string.input_hint, this);
    promptionTimeTv.addRightView(timeInputEt, R.color.c1);

    addInputEt = inputHelper.getEditText(this, R.string.input_hint, this);
    promptionAddressTv.addRightView(addInputEt, R.color.c1);

    endDateTv = inputHelper.getTextView(this, R.string.input_hint);
    endDateTv.addTextChangedListener(this);
    endDateTv.setTextColor(ContextCompat.getColor(this, R.color.c1));
    endDateEt.addRightView(endDateTv, 0);

    noteEt.addTextChangedListener(this);
  }

  void addAddItem(List<ItemImageObj> items) {
    ItemImageObj thumb = new ItemImageObj();
    thumb.type = ItemImageObj.TYPE_ADD;
    items.add(thumb);
  }

  private void setupOptionMenu() {
    menuResId = R.menu.menu_add_submit;
    invalidateOptionsMenu();
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean bgSelected = !TextUtils.isEmpty(promptionBg);
    promptionTitle = promptTitleEt.getText().toString();
    boolean titleInputed = !TextUtils.isEmpty(promptionTitle);
    promptionContent = promptContentEt.getText().toString();
    boolean contentInputed = !TextUtils.isEmpty(promptionContent);
    promptionTime = timeInputEt.getText().toString().trim();
    boolean timeInputed = !TextUtils.isEmpty(promptionTime);
    promptionAddress = addInputEt.getText().toString().trim();
    boolean addressInputed = !TextUtils.isEmpty(promptionAddress);
    promptionEndDate = endDateTv.getText().toString().trim();
    boolean endTimeInputed = !TextUtils.isEmpty(promptionEndDate);
    promptNotes = noteEt.getText().toString().trim();
    boolean noticeInputed = !TextUtils.isEmpty(promptNotes);

    menu.getItem(0).setEnabled(bgSelected && titleInputed
      && contentInputed && timeInputed && addressInputed && endTimeInputed && noticeInputed);
    return true;
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
    PromptionRequest request = new PromptionRequest();
    request.aci_name = promptionTitle;
    request.aci_date = promptionTime;
    request.aci_content = promptionContent;
    request.aci_notice = promptNotes;
    request.aci_company_id = userService.getUserCompanyId();
    request.aci_icon = promptionBg;
    request.end_date = promptionEndDate;
    request.aci_address = promptionAddress;
    request.aci_tumblrs = list;

    PromptionInterface promptionInterface = JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);
    Call<BaseResponse> call = promptionInterface.postPromption(request.getMap());
    JApplication.getNetworkManager().async(this, Constant.PROCESSING, call, new KwMarketNetworkCallback<BaseResponse>(this) {
      @Override
      public void onSuccess(BaseResponse baseResponse) {
        JToast.toastShort("推广发布成功");
        finish();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
  }

  @OnClick(R.id.add_promption_bg_btn)
  public void addBg() {
    hideInput(this, toolbar);
    clickedPos = ClickPos.PROMPTION_BG;
    photoService.takePhoto(this, toolbar, null, false, 16, 9);
  }

  @OnClick(R.id.promption_end_date)
  public void selectEndDate() {
    Map<Integer, Integer> timeValue = dateTimeHelper.getDateValue(endDate);
    DatePickerDialog datePicker = new DatePickerDialog(this,
      android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
      new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          endDateTv.setText(dateTimeHelper.getFormatedDate(year, monthOfYear, dayOfMonth));
          endDate = dateTimeHelper.getFormatedTime(year, monthOfYear, dayOfMonth);
        }
      }, timeValue.get(Calendar.YEAR),
      timeValue.get(Calendar.MONTH),
      timeValue.get(Calendar.DAY_OF_MONTH));

    datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    datePicker.show();
  }

  @Override
  protected String getTag() {
    return "AddPromptionActivity";
  }

  @Override
  public void setGridViewLastItemSwaple(boolean swaple) {
    promptionImgsContainer.setSwapLastItem(swaple);
  }

  @Override
  public void onUpLoadImageFinish(String imgUrl, Uri imgUri) {
    if (clickedPos != null) {
      addPromptionBgBtn.setVisibility(View.GONE);
      promptionBgImg.setVisibility(View.VISIBLE);
      if (imgUri != null) {
        promptionBg = imgUrl;
        Picasso.with(this)
          .load(imgUri).centerCrop().resize(targetImgBgWidth, targetImgBgHeight)
          .into(promptionBgImg);

      } else if (!TextUtils.isEmpty(imgUrl)) {
        promptionBg = imgUrl;
        JApplication.getImageLoader().loadImage(
          promptionBgImg, imgUrl, targetImgBgWidth, targetImgBgHeight, R.drawable.brand_icon_default);
      }
      invalidateOptionsMenu();
      clickedPos = null;
    } else {
      int imgSize = list.size();
      ItemImageObj item = new ItemImageObj();
      item.thumbImg = imgUrl;

      if (imgSize == MAX_COUNT) {
        if (list.get(imgSize - 1).type != ItemImageObj.TYPE_ADD) {
          return;
        } else {
          list.set(imgSize - 1, item);
          promptionImgsContainer.setSwapLastItem(true);
        }
      } else {
        list.add(imgSize - 1, item);
        promptionImgsContainer.setSwapLastItem(false);
      }

      itemAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onUpLoadImageFailed() {

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
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    invalidateOptionsMenu();
  }

  public enum ClickPos {
    PROMPTION_BG
  }
}
