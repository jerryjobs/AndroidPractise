package com.ikaowo.join.network;

import com.ikaowo.join.model.response.BrandListResponse;
import com.ikaowo.join.model.response.BrandResponse;
import com.ikaowo.join.model.response.UserListResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by weibo on 15-12-18.
 */
public interface BrandInterface {
  @GET("public/companys")
  Call<BrandListResponse> getBrandList();

  @GET("search/{type}/{query}/{cp}/{ps}")
  Call<BrandListResponse> searchBrand(@Path("type") String type, @Path("query") String query, @Path("cp") int cp, @Path("ps") int ps);

  //获取平牌（公司）成员列表
  @GET("public/companyDetail/{company_id}")
  Call<UserListResponse> getBrandMember(@Path("company_id") int companyId);

  @GET("public/company/{brand_id}")
  Call<BrandResponse> getBrandInfo(@Path("brand_id") int brandId);

}
