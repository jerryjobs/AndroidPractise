package com.scaffold.demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.common.framework.umeng.UmengService;
import com.common.framework.umeng.UmengShareService;
import com.component.photo.PhotoService;
import com.scaffold.demo.network.KwMarketNetworkCallback;
import com.scaffold.demo.network.TestInterface;
import com.scaffold.demo.network.bean.request.LoginRequest;
import com.scaffold.demo.network.bean.response.LoginResponse;
import com.scaffold.demo.network.bean.response.UserInfoResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends BaseActivity {

    private UmengService umengService = new UmengService();
    private UmengShareService umengShareService = new UmengShareService();
    private NetworkManager networkManager = JApplication.getNetworkManager();
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        umengService.init(this);
        umengService.syncFeedback(this, null, null);

        Button updateBtn = (Button) findViewById(R.id.um_update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umengService.checkUpdate(MainActivity.this, true);
            }
        });

        Button shareBtn = (Button) findViewById(R.id.um_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umengShareService.openShareWindow(MainActivity.this, toolbar, "test", "test", "http://www.baidu.com", null);
            }
        });

        Button feedback = (Button) findViewById(R.id.um_feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umengService.feedback(MainActivity.this);
            }
        });

        Button netAsync = (Button) findViewById(R.id.network_async);
        netAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInterface testNetworkService = networkManager.getServiceByClass(TestInterface.class);
                Call<UserInfoResponse> call = testNetworkService.getUserInfo();
                call.enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Response<UserInfoResponse> response, Retrofit retrofit) {
                        Log.e("weiboooo", "status code:" + response.code());
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

        Button loginBtn = (Button) findViewById(R.id.login);
        Button logoutBtn = (Button) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkManager.clearCookieStore();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInterface testNetworkService = networkManager.getServiceByClass(TestInterface.class);
                LoginRequest request = new LoginRequest();
                request.username = "bfx123";
                request.password = "123123";
                Call<LoginResponse> call = testNetworkService.login(request);

                call.enqueue(new KwMarketNetworkCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {

                    }

                    @Override
                    public void onFailed(Response response, Retrofit retrofit) {
                        Log.e("weibo", "the code is：" + response.code());
                    }
                });
            }
        });

        Button showProgressBtn = (Button) findViewById(R.id.progress);
        showProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.showProgressDialog("数据加载中");
            }
        });
        Button showDialogBtn = (Button) findViewById(R.id.dialog);
        final TextView contentTv = new TextView(this);
        contentTv.setText("这个是内容.....");
        contentTv.setTextColor(getResources().getColor(R.color.umeng_socialize_edit_bg));
        showDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = dialogHelper.createDialog(MainActivity.this, "信息",
                        "对了上次有邻居说了解怎么去政府抗议，我觉得这个事情也可以计划起来了，" +
                                "总觉得周六去了也没法马上起到很好的效果，多半得几条路一起走",
                        new String[]{"Cancel", "OK"}, new View.OnClickListener[]{
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                },

                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("weiboooo", "I will not dismiss....");
                                    }
                                },
                        });
                dialog.show();
            }
        });

        Button viewImageBtn = (Button) findViewById(R.id.viewImage);
        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewImageActivity.class);
                startActivity(intent);
            }
        });

        final Button takePhoto = (Button) findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoService photoService = new PhotoService(MainActivity.this);
                photoService.takePhoto(MainActivity.this, takePhoto, null);
            }
        });
    }

    @Override
    public String getTag() {
        return "MainActivity";
    }
}
