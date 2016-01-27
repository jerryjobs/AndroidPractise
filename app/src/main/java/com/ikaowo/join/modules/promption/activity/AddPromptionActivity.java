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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.component.photo.PhotoService;
import com.ikaowo.join.BaseEventBusActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.draggridview.DragGridItemAdapter;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.eventbus.RefreshWebViewCallback;
import com.ikaowo.join.eventbus.UpdatePromptionCallback;
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
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-22.
 */
public class AddPromptionActivity extends BaseEventBusActivity
        implements PhotoService.UploadFinishListener, TextWatcher {


    protected final int MAX_COUNT = 6;
    private final int MAX_CONTENT_LENGTH = 140;
    private final int MAX_TITLE_LENGTH = 15;
    protected int targetImgBgWidth, targetImgBgHeight;
    protected List<ItemImageObj> list = new ArrayList<>(); //图标icon
    protected DragGridItemAdapter itemAdapter;
    protected int titleResId = R.string.title_activity_add_promotion;
    protected TextView endDateTv;
    protected EditText timeInputEt;
    protected EditText addInputEt;
    protected int promptionId;
    protected String promptionBg;
    protected String promptionTitle;
    protected String promptionContent;
    protected String promptionTime;
    protected String promptionAddress;
    protected String promptionEndDate;
    protected String promptNotes;
    protected String endDate; //格式化过的时间，用来展示 格式为 2014-10-11
    protected String state;
    @Bind(R.id.container)
    LinearLayout containerLayout;
    @Bind(R.id.content_container)
    LinearLayout contentContainerLayout;
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
    GridView promptionImgsContainer;
    @Bind(R.id.promption_time)
    CustomEditTextView promptionTimeTv;
    @Bind(R.id.promption_address)
    CustomEditTextView promptionAddressTv;
    @Bind(R.id.promption_end_date)
    CustomEditTextView endDateEt;
    @Bind(R.id.promption_notes_content)
    AppCompatEditText noteEt;
    private QiniuUploadHelper qiniuUploadHelper;
    private InputFiledHelper inputHelper;
    private DateTimeHelper dateTimeHelper;
    private PhotoService photoService;
    private UserService userService;
    private ClickPos clickedPos;

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
        toolbar.setTitle(getString(titleResId));
        setSupportActionBar(toolbar);

        displayHomeAsIndicator(0);

        setupView();
        setupOptionMenu();
    }

    private void setupView() {
        LinearLayout.LayoutParams llp =
                (LinearLayout.LayoutParams) promptionBgContainer.getLayoutParams();
        targetImgBgWidth = JApplication.getJContext().getScreenWidth();
        targetImgBgHeight = targetImgBgWidth * 9 / 16;
        llp.width = targetImgBgWidth;
        llp.height = targetImgBgHeight;
        if (promptionId <= 0) {
            addAddItem(list);
        }
        itemAdapter = new DragGridItemAdapter(this, list, MAX_COUNT, true);
        promptionImgsContainer.setAdapter(itemAdapter);

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
                contentRemainingTv.setText(
                        getString(R.string.content_remaing, s.length(), MAX_CONTENT_LENGTH));
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

        menu.getItem(0)
                .setEnabled(bgSelected
                        && titleInputed
                        && contentInputed
                        && timeInputed
                        && addressInputed
                        && endTimeInputed
                        && noticeInputed);
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

    protected void submit() {
        PromptionRequest request = new PromptionRequest();
        if (promptionId > 0) {
            request.aci_id = promptionId;
        }
        request.aci_name = promptionTitle;
        request.aci_date = promptionTime;
        request.aci_content = promptionContent;
        request.aci_notice = promptNotes;
        request.aci_company_id = userService.getUserCompanyId();
        request.aci_icon = promptionBg;
        request.end_date = promptionEndDate;
        request.aci_address = promptionAddress;

        int addPos = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).type == ItemImageObj.TYPE_ADD) {
                addPos = i;
            }
        }
        if (addPos >= 0) {
            list.remove(addPos);
        }
        request.aci_tumblrs = list;

        hideInput(this, toolbar);

        PromptionInterface promptionInterface =
                JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);
        Call<BaseResponse> call = promptionInterface.postPromption(request.getMap());
        JApplication.getNetworkManager()
                .async(this, Constant.PROCESSING, call, new KwMarketNetworkCallback<BaseResponse>(this) {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (promptionId > 0) {

                            EventBus.getDefault().post(new UpdatePromptionCallback() {
                                @Override
                                public boolean promptionUpdated() {
                                    return true;
                                }

                                @Override
                                public String getNewTitle() {
                                    return promptionTitle;
                                }

                                @Override
                                public String getNewBg() {
                                    return promptionBg;
                                }

                                @Override
                                public String getNewEndTime() {
                                    return promptionEndDate;
                                }

                                @Override
                                public String getNewState() {
                                    if (Constant.PROMPTION_STATE_FAILED.equalsIgnoreCase(state)) {
                                        state = Constant.PROMPTION_STATE_NEW;
                                    }
                                    return state;
                                }
                            });

                            EventBus.getDefault().post(new RefreshWebViewCallback() {
                                @Override
                                public boolean refreshWebView() {
                                    return true;
                                }
                            });

                            new JDialogHelper(AddPromptionActivity.this).showConfirmDialog(
                                    R.string.hint_promption_resubmit, R.string.custom_ok_btn,
                                    new JDialogHelper.DoAfterClickCallback() {
                                        @Override
                                        public void doAction() {
                                            finish();
                                        }
                                    });
                        } else {
                            new JDialogHelper(AddPromptionActivity.this).showConfirmDialog(
                                    R.string.hint_promption_posted,
                                    R.string.custom_ok_btn,
                                    new JDialogHelper.DoAfterClickCallback() {
                                        @Override
                                        public void doAction() {
                                            finish();
                                        }
                                    });
                        }
                    }
                });
    }

    public void onEvent(Boolean b) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        qiniuUploadHelper.uploadImg(this, requestCode, resultCode, data, this);
    }

    @OnClick({R.id.add_promption_bg_btn, R.id.promption_bg})
    public void addBg() {
        hideInput(this, toolbar);
        clickedPos = ClickPos.PROMPTION_BG;
        photoService.takePhoto(this, toolbar, null, false, 16, 9);
    }

    @OnClick(R.id.promption_end_date)
    public void selectEndDate() {
        Map<Integer, Integer> timeValue = dateTimeHelper.getDateValue(endDate);
        DatePickerDialog datePicker =
                new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDateTv.setText(dateTimeHelper.getFormatedDate(year, monthOfYear, dayOfMonth));
                                endDate = dateTimeHelper.getFormatedTime(year, monthOfYear, dayOfMonth);
                            }
                        }, timeValue.get(Calendar.YEAR), timeValue.get(Calendar.MONTH),
                        timeValue.get(Calendar.DAY_OF_MONTH));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Log.e("weibo", calendar.getTime() + "");
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePicker.show();
    }

    @Override
    protected String getTag() {
        return "AddPromptionActivity";
    }

    @Override
    public void onUpLoadImageFinish(String imgUrl, Uri imgUri) {
        if (clickedPos != null) {
            addPromptionBgBtn.setVisibility(View.GONE);
            promptionBgImg.setVisibility(View.VISIBLE);
            if (imgUri != null) {
                promptionBg = imgUrl;
                Picasso.with(this)
                        .load(imgUri)
                        .centerCrop()
                        .resize(targetImgBgWidth, targetImgBgHeight)
                        .into(promptionBgImg);
            } else if (!TextUtils.isEmpty(imgUrl)) {
                promptionBg = imgUrl;
                JApplication.getImageLoader()
                        .loadImage(promptionBgImg, imgUrl, targetImgBgWidth, targetImgBgHeight,
                                R.drawable.brand_icon_default);
            }
            invalidateOptionsMenu();
            clickedPos = null;
        } else {
            int imgSize = list.size();
            ItemImageObj item = new ItemImageObj();
            item.thumbImg = imgUrl;
            item.uri = imgUri;

            if (imgSize == MAX_COUNT) {
                if (list.get(imgSize - 1).type != ItemImageObj.TYPE_ADD) {
                    return;
                } else {
                    list.set(imgSize - 1, item);
                }
            } else {
                list.add(imgSize - 1, item);
            }

            itemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUpLoadImageFailed() {

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
