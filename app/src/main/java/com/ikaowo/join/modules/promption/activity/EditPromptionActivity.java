package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.ErrorHintLayout;
import com.ikaowo.join.eventbus.AnimationUpdateCallback;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.response.PromptionResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.DateTimeHelper;

import retrofit.Call;

/**
 * Created by weibo on 16-1-4.
 */
public class EditPromptionActivity extends AddPromptionActivity {

    private NetworkManager networkManager;
    private String notificationType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        titleResId = R.string.title_activity_edit_promotion;
        promptionId = getIntent().getExtras().getInt(Constant.PROMPTION_ID);
        if (promptionId <= 0) {
            finish();
        }
        notificationType = getIntent().getExtras().getString(Constant.NOTIFICATION_TYPE, "");
        super.onCreate(savedInstanceState);

        networkManager = JApplication.getNetworkManager();
        PromptionInterface promptionNetworkService =
                networkManager.getServiceByClass(PromptionInterface.class);
        Call<PromptionResponse> call = promptionNetworkService.getPromption(promptionId);
        networkManager.async(this, Constant.DATAGETTING, call,
                new KwMarketNetworkCallback<PromptionResponse>(this) {
                    @Override
                    public void onSuccess(PromptionResponse response) {
                        Promption promption = null;
                        if (response == null || (promption = response.data) == null) {
                            finish();
                        }

                        //如果来自于推送或者从系统消息点入，则想要判断状态是否已经发生变更
                        if (!TextUtils.isEmpty(notificationType)
                                && notificationType.equalsIgnoreCase(Constant.PUSH_PROMPTION_FAILED)) {

                            if (!promption.state.equalsIgnoreCase(Constant.PROMPTION_STATE_FAILED)) {
                                new JDialogHelper(EditPromptionActivity.this)
                                        .showConfirmDialog(R.string.hint_state_update, R.string.custom_ok_btn,
                                                new JDialogHelper.DoAfterClickCallback() {
                                                    @Override
                                                    public void doAction() {
                                                        finish();
                                                    }
                                                });
                                return;
                            }
                        }

                        if (Constant.PROMPTION_STATE_FAILED.equalsIgnoreCase(promption.state)
                                && !TextUtils.isEmpty(promption.comment)) {
                            ErrorHintLayout errorHintLayout = new ErrorHintLayout(EditPromptionActivity.this);
                            errorHintLayout.setText(promption.comment);
                            containerLayout.addView(errorHintLayout, 0);
                        }
                        addPromptionBgBtn.setVisibility(View.GONE);
                        promptionBgImg.setVisibility(View.VISIBLE);

                        promptionBg = promption.background;
                        promptionTitle = promption.title;
                        promptionContent = promption.content;
                        promptionTime = promption.date;
                        promptionAddress = promption.address;
                        promptionEndDate = promption.endDate;
                        promptNotes = promption.note;
                        promptionEndDate = promption.endDate;
                        state = promption.state;
                        endDate = new DateTimeHelper().getTime(promptionEndDate);

                        JApplication.getImageLoader()
                                .loadImage(promptionBgImg, promptionBg, targetImgBgWidth, targetImgBgHeight,
                                        R.drawable.brand_icon_default);
                        promptTitleEt.setText(promptionTitle);
                        promptTitleEt.setSelection(promptionTitle.length());
                        promptContentEt.setText(promptionContent);

                        list.addAll(promption.aci_tumblrs);
                        if (list.size() < MAX_COUNT) {
                            addAddItem(list);
                        }
                        itemAdapter.notifyDataSetChanged();

                        timeInputEt.setText(promptionTime);
                        addInputEt.setText(promptionAddress);
                        endDateTv.setText(endDate);

                        noteEt.setText(promptNotes);
                    }
                });
    }

    public void onEvent(AnimationUpdateCallback callback) {
        contentContainerLayout.setTranslationY(callback.getUpdatedValue());
    }
}
