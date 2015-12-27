package com.ikaowo.join.util;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.component.photo.PhotoService;
import com.ikaowo.join.model.response.TokenResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.QiniuInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;

/**
 * Created by weibo on 15-12-23.
 */
public class QiniuUploadHelper {
  private NetworkManager networkManager = JApplication.getNetworkManager();
  private String imageServerUrl;
  private PhotoService photoService;

  public void uploadImg(Context context, final int requestCode, final int resultCode, final Intent data, final PhotoService.UploadFinishListener listener) {
    QiniuInterface qiniuNetworkService = networkManager.getServiceByClass(QiniuInterface.class);
    photoService = new PhotoService(context);

    Map<String, String> map = new HashMap<>();
    map.put("u_id", "0");
    map.put("file_type", "icon");
    Call<TokenResponse> call = qiniuNetworkService.getQiniuToken(map);
    networkManager.async(context, Constant.PROCESSING, call, new KwMarketNetworkCallback<TokenResponse>(context) {
      @Override
      public void onSuccess(final TokenResponse tokenResponse) {
        imageServerUrl = tokenResponse.url;
        photoService.onUploadPic(imageServerUrl, requestCode, resultCode,
                tokenResponse.key, tokenResponse.token, data, listener);
      }
    });
  }
}
