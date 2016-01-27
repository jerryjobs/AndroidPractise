package com.ikaowo.join.modules.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.BaseEventBusFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.AvatarUpdateCallback;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.eventbus.UpdatedataCallback;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.request.CheckStateRequest;
import com.ikaowo.join.model.response.CheckStateResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.AvatarHelper;
import com.ikaowo.join.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-8.
 */
public class MineFragment extends BaseEventBusFragment {

    @Bind(R.id.icon)
    ImageView iconIv;
    @Bind(R.id.name_title)
    TextView nameTitleTv;
    @Bind(R.id.short_name)
    TextView shortNameTv;
    @Bind(R.id.brand_name)
    TextView brandNameTv;

    @Bind(R.id.promption)
    RelativeLayout promptionLayout;

    @Bind(R.id.brand_info)
    RelativeLayout companyLayout;

    @Bind(R.id.abount)
    RelativeLayout aboutLayout;

    private UserService userService;
    private MineService mineService;
    private BrandService brandService;
    private ImageLoader imageLoader;
    private int targetImgWidth, targetImgHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = JApplication.getJContext().getServiceByInterface(UserService.class);
        mineService = JApplication.getJContext().getServiceByInterface(MineService.class);
        brandService = JApplication.getJContext().getServiceByInterface(BrandService.class);
        imageLoader = JApplication.getImageLoader();

        targetImgWidth = targetImgHeight = JApplication.getJContext().dip2px(64);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null, false);
        ButterKnife.bind(this, view);

        setupData();

        setupView();
        return view;
    }

    private void setupData() {
        if (userService.isLogined()) {
            UserLoginData user = userService.getUser();
            AvatarHelper.getInstance()
                    .showAvatar(getContext(), user.uId, iconIv, shortNameTv, targetImgWidth, targetImgHeight, user.icon,
                            user.nickName);
            nameTitleTv.setText(getContext().getString(R.string.user_name_and_title, user.nickName, user.title));
            brandNameTv.setText(user.brandInfo != null ? user.brandInfo.brand_name : "");
        }
    }

    private void setupView() {
        setupItem(promptionLayout, R.drawable.mine_ic_activity, "我的推广");
        setupItem(companyLayout, R.drawable.mine_ic_mybrand, "我的品牌");
        setupItem(aboutLayout, R.drawable.mine_ic_about, "关于我们");
    }

    private void setupItem(View parentView, int iconId, String desc) {
        ImageView imageView = (ImageView) parentView.findViewById(R.id.icon);
        promptionLayout.findViewById(R.id.desc);
        imageView.setImageResource(iconId);

        TextView tv = (TextView) parentView.findViewById(R.id.desc);
        tv.setText(desc);
    }

    @OnClick(R.id.user_info)
    public void viewUserInfo() {
        if (userService.isAuthed()) {
            mineService.viewUserInfo(getActivity());
        } else {
            NetworkManager networkManager = JApplication.getNetworkManager();
            UserInterface userNetworkService = networkManager.getServiceByClass(UserInterface.class);
            CheckStateRequest request = new CheckStateRequest();
            request.u_id = userService.getUserId();
            Call<CheckStateResponse> call = userNetworkService.checkLatestState(request);
            networkManager.async(getActivity(), Constant.DATAGETTING, call,
                    new KwMarketNetworkCallback<CheckStateResponse>(getActivity()) {
                        @Override
                        public void onSuccess(final CheckStateResponse stateResponse) {
                            if (stateResponse != null && stateResponse.data != null) {
                                userService.updateLocalUserInfo(stateResponse.data);
                                EventBus.getDefault().post(new CheckLatestStateCallback() {
                                    @Override
                                    public UserLatestState getLatestState() {
                                        return stateResponse.data;
                                    }
                                });
                                mineService.viewUserInfo(getActivity());
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.promption)
    public void viewMyPromption() {
        userService.interceptorCheckUserState(getActivity(), R.string.action_view_my_promption,
                new UserService.AuthedAction() {
                    @Override
                    public void doActionAfterAuthed() {
                        mineService.viewMyPromption(getActivity());
                    }
                });
    }

    @OnClick(R.id.brand_info)
    public void viewBrandInfo() {
        userService.interceptorCheckUserState(getActivity(), R.string.action_view_my_brand,
                new UserService.AuthedAction() {
                    @Override
                    public void doActionAfterAuthed() {
                        brandService.viewBrandDetail(getActivity(), userService.getUserCompanyId());
                    }
                });
    }

    public void onEvent(SigninCallback callback) {
        if (callback.singined()) {
            setupData();
        }
    }

    public void onEvent(UpdatedataCallback callback) {
        if (callback.update()) {
            setupData();
        }
    }

    public void onEvent(AvatarUpdateCallback callback) {
        UserLoginData user = userService.getUser();
        if (!TextUtils.isEmpty(callback.updatedAvatar())) {
            AvatarHelper.getInstance()
                    .showAvatar(getContext(), user.uId, iconIv, shortNameTv, targetImgWidth, targetImgHeight, user.icon,
                            user.nickName);
        }
    }

    @Override
    public String getPageName() {
        return "ME";
    }
}
