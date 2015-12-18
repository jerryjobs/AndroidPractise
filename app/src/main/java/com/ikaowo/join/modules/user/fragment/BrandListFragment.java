package com.ikaowo.join.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.model.JResponse;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.AlphaSlideBar;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.response.BrandListResponse;
import com.ikaowo.join.network.BrandInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;

/**
 * Created by weibo on 15-12-18.
 */
public class BrandListFragment extends BaseFragment implements AlphaSlideBar.OnTouchingLetterChangedListener {

    @Bind(R.id.indicator)
    TextView indicatorTv;
    @Bind(R.id.slidebar)
    AlphaSlideBar slideBar;
    @Bind(R.id.listview)
    ScrollMoreRecyclerView recyclerView;

    private RecyclerViewHelper<BrandListResponse> recyclerViewHelper = new RecyclerViewHelper<>();
    private Map<String, Integer> map = new HashMap<>();
    private String firstLetter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_list, null);
        ButterKnife.bind(this, view);
        slideBar.setOnTouchingLetterChangedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerViewHelper.init(recyclerView, new BrandListAdapter(), null);
        recyclerViewHelper.initEmptyView(0, "暂无品牌信息");
        recyclerViewHelper.supportLoadMore(false);
        recyclerViewHelper.setHelperInterface(new RecyclerViewHelperInterface<BrandListResponse, Brand>() {

            @Override
            public boolean checkResponse(JResponse baseResponse) {
                return baseResponse != null&&
                        ((baseResponse instanceof BrandListResponse)
                            && (((BrandListResponse)baseResponse).data) != null);
            }

            @Override
            public List<Brand> getList(BrandListResponse brandListResponse) {
                List<Brand> brandList = brandListResponse.data;
                int length = brandList.size();
                for (int i = 0; i < length; i ++) {
                    firstLetter = brandList.get(i).brandFirstLetter;
                    switch (i) {
                        case 0:
                            map.put(firstLetter, 0);
                            brandList.get(i).showSection = true;
                            break;
                        default:
                            firstLetter = brandList.get(i).brandFirstLetter;
                            boolean showSection = (!firstLetter.equalsIgnoreCase(brandList.get(i - 1).brandFirstLetter));
                            brandList.get(i).showSection = showSection;
                            brandList.get(i - 1).hideSplit = showSection;
                            if (showSection) {
                                map.put(firstLetter, i);
                            }
                            break;
                    }
                }
                return brandListResponse.data;
            }

            @Override
            public void sendRequest(Callback<BrandListResponse> callback, int cp, int ps) {
                BrandInterface brandNetworkService = JApplication.getNetworkManager().getServiceByClass(BrandInterface.class);
                Call<BrandListResponse> call  = brandNetworkService.getBrandList();
                call.enqueue(callback);
            }

            @Override
            public void performItemClick(int position) {

            }
        });
        recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
    }

    @Override
    public String getPageName() {
        return "BrandListFragment";
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        if (indicatorTv.getVisibility() != View.VISIBLE) {
            indicatorTv.setVisibility(View.VISIBLE);
        }
        indicatorTv.setText(s);
        Integer pos = map.get(s);
        if (pos != null) {
            RecyclerView.LayoutManager llm = recyclerView.getLayoutManager();
            ((LinearLayoutManager)llm).scrollToPositionWithOffset(pos, 0);
        }
    }

    @Override
    public void onTouchUp() {
        indicatorTv.setVisibility(View.GONE);
    }

    class BrandListAdapter extends JAdapter<Brand> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_brand_list, null);
            RecyclerView.ViewHolder viewHolder = new BrandListViewHodler(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof BrandListViewHodler) {
                BrandListViewHodler viewHodler = (BrandListViewHodler)holder;
                Brand brand = objList.get(position);
                if (brand != null) {
                    viewHodler.nameTv.setText(brand.brandName);
                    viewHodler.summaryTv.setText(brand.summary);
                    if (brand.showSection) {
                        viewHodler.sectionTv.setVisibility(View.VISIBLE);
                        viewHodler.sectionTv.setText(brand.brandFirstLetter);
                    } else {
                        viewHodler.sectionTv.setVisibility(View.GONE);
                    }

                    viewHodler.sectionSplitView.setVisibility(brand.hideSplit ? View.INVISIBLE : View.VISIBLE);

                    JApplication.getImageLoader().loadImage(
                            viewHodler.iconIv,
                            brand.brandLogo,
                            JApplication.getJContext().dip2px(64),
                            JApplication.getJContext().dip2px(48),
                            R.drawable.umeng_socialize_default_avatar);
                }
            }
        }
    }

    public class BrandListViewHodler extends RecyclerViewHelper.JListViewHolder {
        @Bind(R.id.brand_icon)
        ImageView iconIv;
        @Bind(R.id.brand_name)
        TextView nameTv;
        @Bind(R.id.brand_summary)
        TextView summaryTv;
        @Bind(R.id.section_header)
        TextView sectionTv;
        @Bind(R.id.section_split)
        View sectionSplitView;

        public BrandListViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
