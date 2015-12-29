package com.ikaowo.join.network;

import com.ikaowo.join.model.Brand;
import com.ikaowo.join.model.User;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.response.BrandResponse;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 15-12-18.
 */
public interface BrandInterface {
  @GET("public/companys")
  Call<BaseListResponse<Brand>> getBrandList();

  @GET("search")
  Call<BaseListResponse<Brand>> searchBrand(@QueryMap Map map);

  //获取平牌（公司）成员列表
  @GET("public/companyDetail/{company_id}")
  Call<BaseListResponse<User>> getBrandMember(@Path("company_id") int companyId);

  @GET("public/company/{brand_id}")
  Call<BrandResponse> getBrandInfo(@Path("brand_id") int brandId);

}
