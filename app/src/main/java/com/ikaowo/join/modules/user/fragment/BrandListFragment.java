package com.ikaowo.join.modules.user.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JAdapter;
import com.common.framework.core.JApplication;
import com.common.framework.core.JFragmentActivity;
import com.common.framework.model.JResponse;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.common.framework.widget.listview.RecyclerViewHelperInterface;
import com.common.framework.widget.listview.ScrollMoreRecyclerView;
import com.ikaowo.join.BaseFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.AlphaSlideBar;
import com.ikaowo.join.eventbus.ChooseBrandCallback;
import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.request.SearchRequest;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.util.Constant;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit.Call;

/**
 * Created by weibo on 15-12-18.
 */
public class BrandListFragment extends BaseFragment
    implements AlphaSlideBar.OnTouchingLetterChangedListener {

  @Bind(R.id.indicator) TextView indicatorTv;
  @Bind(R.id.slidebar) AlphaSlideBar slideBar;
  @Bind(R.id.listview) ScrollMoreRecyclerView recyclerView;
  @Bind(R.id.search_listview) ScrollMoreRecyclerView searchRecyclerView;
  @Bind(R.id.search_view) SearchView searchView;
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  private Map<String, Integer> map = new HashMap<>();
  private String firstLetter = null;
  private RecyclerViewHelper<BaseListResponse<Brand>, Brand> recyclerViewHelper;
  private RecyclerViewHelper<BaseListResponse<Brand>, Brand> searchRecyclerViewHelper;
  private String queryStr;
  private boolean choose; //是否为选择操作
  private BrandService brandService;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recyclerViewHelper = new RecyclerViewHelper<>();
    searchRecyclerViewHelper = new RecyclerViewHelper<>();
    choose = getArguments() == null ? false : getArguments().getBoolean(UserService.CHOOSE, false);
    brandService = JApplication.getJContext().getServiceByInterface(BrandService.class);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_brand_list, null);
    ButterKnife.bind(this, view);
    slideBar.setOnTouchingLetterChangedListener(this);
    slideBar.getParent().requestDisallowInterceptTouchEvent(true);
    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.e("weiboooo", "activityCreated");
    setupView();
  }

  private void setupView() {
    final AutoCompleteTextView searchText =
        (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);

    searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    setupSearchView(searchView);
    setupRecyclerView(searchRecyclerView, true, searchRecyclerViewHelper);
    setupRecyclerView(recyclerView, false, recyclerViewHelper);
  }

  private void setupSearchView(final SearchView searchView) {
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(final String query) {

        slideBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);
        searchRecyclerView.setVisibility(View.VISIBLE);
        queryStr = query;
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            searchRecyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.REFRESH);
            searchView.clearFocus();
            ((JFragmentActivity) getActivity()).hideInput(getActivity(), searchView);
          }
        }, 100);

        return true;
      }

      @Override public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
          recyclerView.setVisibility(View.VISIBLE);
          searchRecyclerView.setVisibility(View.GONE);
          slideBar.setVisibility(View.VISIBLE);
        }
        return false;
      }
    });
  }

  /**
   * @param search 是否为搜索页面的recyclerview
   */
  private void setupRecyclerView(final ScrollMoreRecyclerView recyclerView, final boolean search,
      final RecyclerViewHelper<BaseListResponse<Brand>, Brand> recyclerViewHelper) {

    recyclerViewHelper.init(getActivity(), recyclerView, new BrandListAdapter(recyclerViewHelper),
        swipeRefreshLayout);
    recyclerViewHelper.initEmptyView(0, "暂无品牌信息");
    recyclerViewHelper.supportLoadMore(search);

    RecyclerViewHelperInterface recyclerViewHelperImpl =
        new RecyclerViewHelperInterface<BaseListResponse<Brand>, Brand>() {

          @Override public boolean checkResponse(JResponse baseResponse) {
            return baseResponse != null && ((baseResponse instanceof BaseListResponse)
                && (((BaseListResponse) baseResponse).data) != null);
          }

          @Override public List<Brand> getList(BaseListResponse<Brand> brandListResponse) {
            List<Brand> brandList = brandListResponse.data;
            int length = brandList.size();
            if (search) {
              return brandListResponse.data;
            }
            for (int i = 0; i < length; i++) {
              firstLetter = brandList.get(i).company_py;
              switch (i) {
                case 0:
                  map.put(firstLetter, 0);
                  brandList.get(i).showSection = true;
                  break;
                default:
                  firstLetter = brandList.get(i).company_py;
                  boolean showSection =
                      (!firstLetter.equalsIgnoreCase(brandList.get(i - 1).company_py));
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
          public void sendRequest(NetworkCallback<BaseListResponse<Brand>> callback, int cp,
              int ps) {
            BrandInterface brandNetworkService =
                JApplication.getNetworkManager().getServiceByClass(BrandInterface.class);

            Call<BaseListResponse<Brand>> call;
            if (search) {
              SearchRequest request = new SearchRequest();
              request.type = Constant.SEARCH_TYPE_BRAND;
              request.value = queryStr;
              request.cp = cp;
              request.ps = ps;
              call = brandNetworkService.searchBrand(request.getMap());
            } else {
              call = brandNetworkService.getBrandList();
            }
            JApplication.getNetworkManager().async(call, callback);
          }

          @Override public void performItemClick(final int position) {

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
              return;
            }

            final List objList = ((JAdapter<BaseListResponse<Brand>>) adapter).getObjList();
            if (objList == null) {
              return;
            }

            final Brand brand = (Brand) objList.get(position);
            if (choose) {
              EventBus.getDefault().post(new ChooseBrandCallback() {

                @Override public Brand getChoosedBrand() {
                  return brand;
                }
              });
              getActivity().finish();
            } else { // view company detail
              brandService.viewBrandDetail(getActivity(), brand.company_id);
            }
          }
        };
    recyclerViewHelper.setHelperInterface(recyclerViewHelperImpl);

    if (!search) {
      recyclerViewHelper.sendRequestAndProcess(RecyclerViewHelper.Action.INIT);
    }
  }

  @Override public String getPageName() {
    return "BrandListFragment";
  }

  @Override public void onTouchingLetterChanged(String s) {
    if (indicatorTv.getVisibility() != View.VISIBLE) {
      indicatorTv.setVisibility(View.VISIBLE);
    }
    indicatorTv.setText(s);
    Integer pos = map.get(s);
    if (pos != null) {
      RecyclerView.LayoutManager llm = recyclerView.getLayoutManager();
      ((LinearLayoutManager) llm).scrollToPositionWithOffset(pos, 0);
    }
  }

  @Override public void onTouchUp() {
    indicatorTv.setVisibility(View.GONE);
  }

  class BrandListAdapter extends JAdapter<Brand> {

    private RecyclerViewHelper helper;

    public BrandListAdapter(RecyclerViewHelper helper) {
      this.helper = helper;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_brand_list, null);
      RecyclerView.ViewHolder viewHolder = new BrandListViewHodler(view, helper);
      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof BrandListViewHodler) {
        BrandListViewHodler viewHodler = (BrandListViewHodler) holder;
        Brand brand = objList.get(position);
        if (brand != null) {
          viewHodler.nameTv.setText(brand.brand_name);
          viewHodler.summaryTv.setText(brand.summary);
          if (brand.showSection) {
            viewHodler.sectionTv.setVisibility(View.VISIBLE);
            viewHodler.sectionTv.setText(brand.company_py);
          } else {
            viewHodler.sectionTv.setVisibility(View.GONE);
          }

          viewHodler.sectionSplitView.setVisibility(
              brand.hideSplit ? View.INVISIBLE : View.VISIBLE);

          JApplication.getImageLoader()
              .loadImage(viewHodler.iconIv, brand.brand_logo, JApplication.getJContext().dip2px(64),
                  JApplication.getJContext().dip2px(48), R.drawable.brand_icon_default);
        }
      }
    }
  }

  public class BrandListViewHodler extends RecyclerView.ViewHolder {
    @Bind(R.id.brand_icon) ImageView iconIv;
    @Bind(R.id.brand_name) TextView nameTv;
    @Bind(R.id.brand_summary) TextView summaryTv;
    @Bind(R.id.section_header) TextView sectionTv;
    @Bind(R.id.section_split) View sectionSplitView;

    public BrandListViewHodler(View itemView, final RecyclerViewHelper recyclerViewHelper) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          recyclerViewHelper.getRecyclerHelperImpl().performItemClick(getLayoutPosition());
        }
      });
    }
  }
}
