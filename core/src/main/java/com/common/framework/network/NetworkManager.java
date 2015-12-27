package com.common.framework.network;

import android.content.Context;

import com.common.framework.core.JFragmentActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-2.
 */
public class NetworkManager {

  private PersistentCookieStore cookieStore;
  private OkHttpClient okHttpClient;
  private Retrofit retrofit;
  private Map<Class, Object> serviceMap = new HashMap<>();
  private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

  public NetworkManager(Context context, String baseUrl) {
    okHttpClient = getUnSafeOkHttpClient();
    cookieStore = new PersistentCookieStore(context);
    CookieManager cookieManager = (new CookieManager(
      cookieStore,
      CookiePolicy.ACCEPT_ALL));
    okHttpClient.setCookieHandler(cookieManager);

    okHttpClient.interceptors().add(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Customize the request
        Request request = original.newBuilder()
          .header("Accept", "application/json")
          .header("user-agent", getUserAgent())
          .method(original.method(), original.body())
          .build();
        Response response = chain.proceed(request);

        // Customize or return the response
        return response;
      }
    });

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    okHttpClient.interceptors().add(logging);

    retrofit = new Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();
  }

  //构建一个不用证书就可以访问https的httpclient
  private static OkHttpClient getUnSafeOkHttpClient() {
    try {
      TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        }
      };

      // Install the all-trusting trust manager
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
      OkHttpClient okHttpClient = new OkHttpClient();
      okHttpClient.setFollowSslRedirects(true);
      okHttpClient.setSslSocketFactory(sslSocketFactory);
      okHttpClient.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      return okHttpClient;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected static String getUserAgent() {
    return "";
  }

  public void registerService(Class serviceCls) {
    Object serviceClsImpl = retrofit.create(serviceCls);
    serviceMap.put(serviceCls, serviceClsImpl);
  }

  public <T> T getServiceByClass(Class serviceCls) {
    return (T) serviceMap.get(serviceCls);
  }

  public void clearCookieStore() {
    if (cookieStore != null) {
      cookieStore.removeAll();
    }
  }

  public void async(Context context, String progressStr, Call call, NetworkCallback networkCallback) {
    boolean avaliableShowProgress = (context != null && context instanceof JFragmentActivity);
    if (avaliableShowProgress) {
      ((JFragmentActivity) context).dialogHelper.showProgressDialog(progressStr);
    }
    if (call == null || networkCallback == null) {
      throw new RuntimeException("call or callback incomplete!");
    }
    call.enqueue(networkCallback);
  }

  public void async(Call call, NetworkCallback networkCallback) {
    if (call == null || networkCallback == null) {
      throw new RuntimeException("call or callback incomplete!");
    }
    call.enqueue(networkCallback);
  }

}
