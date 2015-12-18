package com.ikaowo.join.network;

import com.ikaowo.join.model.response.BrandListResponse;


import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by weibo on 15-12-18.
 */
public interface BrandInterface {
    @GET("public/companys")
    Call<BrandListResponse> getBrandList();
}
