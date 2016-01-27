package com.ikaowo.join.modules.promption.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.component.photo.FullImageView;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.eventbus.JoinStateUpdateCallback;
import com.ikaowo.join.model.JoinInfo;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.JoinInfoRequest;
import com.ikaowo.join.model.request.UpdateJoinStateRequest;
import com.ikaowo.join.model.response.JoinInfoResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.util.AvatarHelper;
import com.ikaowo.join.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by leiweibo on 12/30/15.
 */
public class JoinDetailActivity extends BaseActivity {

    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.content)
    TextView contentTv;
    @Bind(R.id.tumblrs_layout)
    LinearLayout tumblrsLayout;
    @Bind(R.id.avatar_layout)
    FrameLayout avatarLayout;
    @Bind(R.id.brand_logo)
    ImageView brandLogo;
    @Bind(R.id.brand_name)
    TextView brandName;
    @Bind(R.id.icon)
    ImageView iconIv;
    @Bind(R.id.short_name)
    TextView shortNameTv;
    @Bind(R.id.name_title)
    TextView nameTitleTv;
    @Bind(R.id.btn_layout)
    LinearLayout btnLayout;
    @Bind(R.id.rejectBtn)
    TextView rejectBtn;
    @Bind(R.id.approveBtn)
    TextView approveBtn;
    Dialog dialog = null;
    private int promptionId;
    private int brandId;
    private int u_id; //参与活动人的id
    private String phone;
    private String wxId;
    private PromptionInterface promptionInterface =
            JApplication.getNetworkManager().getServiceByClass(PromptionInterface.class);
    private UserService userService =
            JApplication.getJContext().getServiceByInterface(UserService.class);
    private BrandService brandService =
            JApplication.getJContext().getServiceByInterface(BrandService.class);
    private ImageLoader imageLoader = JApplication.getImageLoader();
    private int brandIconTargetWidth = JApplication.getJContext().dip2px(48);
    private int brandIconTargetHeight = JApplication.getJContext().dip2px(36);
    private int userIconTargetWidth = JApplication.getJContext().dip2px(48);
    private int userIconTargetHeight = JApplication.getJContext().dip2px(48);
    private int tumblrWidth = JApplication.getJContext().dip2px(100);
    private int tumblrHeight = JApplication.getJContext().dip2px(100);
    private int margin = JApplication.getJContext().dip2px(12);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_join_detail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData();
        getJoinInfoData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            brandId = bundle.getInt(Constant.BRAND_ID, 0);
            promptionId = bundle.getInt(Constant.PROMPTION_ID, 0);
        }

        if (brandId <= 0 || promptionId <= 0) {
            showConfirmDialog("注意", "报名详情数据不存在，请返回重试");
        }
    }

    private void getJoinInfoData() {
        JoinInfoRequest request = new JoinInfoRequest();
        request.aci_id = promptionId;
        request.company_id = brandId;
        Call<JoinInfoResponse> call = promptionInterface.getJoinInfo(request.getMap());
        JApplication.getNetworkManager()
                .async(this, Constant.DATAGETTING, call,
                        new KwMarketNetworkCallback<JoinInfoResponse>(this) {
                            @Override
                            public void onSuccess(JoinInfoResponse joinInfoResponse) {

                                JoinInfo joinInfo = joinInfoResponse.data;
                                if (joinInfo == null) {
                                    Log.e(getTag(), "the join info is empty.");
                                    return;
                                }
                                u_id = joinInfo.userId;
                                phone = joinInfo.phone;
                                wxId = joinInfo.wx;
                                contentTv.setText(joinInfo.extra);
                                List<ItemImageObj> tumblrs = joinInfo.tumblrs;
                                int tumblrsSize = tumblrs.size();
                                for (int i = 0; i < tumblrsSize; i++) {
                                    FullImageView imageView = new FullImageView(JoinDetailActivity.this);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    String tumblrUrl = tumblrs.get(i).thumbImg;
                                    imageView.setImgUrl(tumblrUrl);
                                    imageLoader.loadImage(imageView, tumblrUrl, tumblrWidth, tumblrWidth,
                                            R.drawable.brand_icon_default);
                                    LinearLayout.LayoutParams llp =
                                            new LinearLayout.LayoutParams(tumblrWidth, tumblrHeight);
                                    if (i == 0) {
                                        llp.leftMargin = 0;
                                    } else {
                                        llp.leftMargin = margin / 2;
                                    }

                                    llp.topMargin = margin;
                                    if (i == tumblrsSize - 1) {
                                        llp.rightMargin = 0;
                                    } else {
                                        llp.rightMargin = margin / 2;
                                    }
                                    llp.bottomMargin = margin;
                                    tumblrsLayout.addView(imageView, llp);
                                }
                                imageLoader.loadImage(brandLogo, joinInfo.brandLogo, brandIconTargetWidth,
                                        brandIconTargetHeight, R.drawable.brand_icon_default);
                                brandName.setText(joinInfo.brandName);

                                LinearLayout.LayoutParams llp =
                                        (LinearLayout.LayoutParams) avatarLayout.getLayoutParams();
                                llp.width = userIconTargetWidth;
                                llp.height = userIconTargetHeight;
                                llp.leftMargin = JApplication.getJContext().dip2px(12);

                                AvatarHelper.getInstance()
                                        .showAvatar(JoinDetailActivity.this, joinInfo.userId, iconIv, shortNameTv, userIconTargetWidth,
                                                userIconTargetHeight, joinInfo.userIcon, joinInfo.nickname);

                                nameTitleTv.setText(getString(R.string.user_name_and_title, joinInfo.nickname, joinInfo.title));

                                if (userService.isLogined()
                                        && userService.getUserId() == joinInfo.publishUId
                                        && joinInfo.prompState.equalsIgnoreCase(Constant.PROMPTION_STATE_PASS)) {
                                    if (Constant.JOIN_STATE_FAILED.equalsIgnoreCase(joinInfo.state)) {
                                        btnLayout.setVisibility(View.VISIBLE);
                                        approveBtn.setVisibility(View.VISIBLE);
                                        rejectBtn.setVisibility(View.GONE);
                                    } else if (Constant.JOIN_STATE_PASSED.equalsIgnoreCase(joinInfo.state)) {
                                        btnLayout.setVisibility(View.GONE);
                                    } else if (Constant.JOIN_STATE_PENDING_APPROVE.equalsIgnoreCase(joinInfo.state)) {
                                        btnLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        btnLayout.setVisibility(View.GONE);
                                    }
                                } else {
                                    FrameLayout.LayoutParams flp =
                                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.MATCH_PARENT);
                                    flp.bottomMargin = 0;
                                    scrollView.setLayoutParams(flp);
                                }
                            }
                        });
    }

    @OnClick(R.id.chat)
    public void chat() {
        userService.imChat(this, wxId);
    }

    @OnClick(R.id.call)
    public void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phone));
        startActivity(intent);
    }

    @OnClick(R.id.brand_layout)
    public void viewBrand() {
        brandService.viewBrandDetail(this, brandId);
    }

    @OnClick(R.id.rejectBtn)
    public void reject() {
        sendRequest(Constant.JOIN_STATE_FAILED);
    }

    @OnClick(R.id.approveBtn)
    public void approve() {
        sendRequest(Constant.JOIN_STATE_PASSED);
    }

    private void sendRequest(final String state) {
        int hintRes = (state == Constant.JOIN_STATE_FAILED) ? R.string.hint_reject_join
                : R.string.hint_accept_join;
        dialog = dialogHelper.createDialog(hintRes, new View.OnClickListener[]{
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UpdateJoinStateRequest updateJoinStateRequest = new UpdateJoinStateRequest();
                updateJoinStateRequest.aci_id = promptionId;
                updateJoinStateRequest.u_act = state;
                List<Integer> uIdList = new ArrayList<>();
                uIdList.add(u_id);
                updateJoinStateRequest.u_id = uIdList;
                Call<BaseResponse> call = promptionInterface.updateJoinState(updateJoinStateRequest);
                JApplication.getNetworkManager()
                        .async(JoinDetailActivity.this, Constant.PROCESSING, call,
                                new KwMarketNetworkCallback<BaseResponse>(JoinDetailActivity.this) {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        EventBus.getDefault().post(new JoinStateUpdateCallback() {

                                            @Override
                                            public String newState() {
                                                return state;
                                            }
                                        });

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 300);
                                    }
                                });
            }
        }
        });
        dialog.show();
    }

    @Override
    protected String getTag() {
        return "JoinDetailActivity";
    }
}
