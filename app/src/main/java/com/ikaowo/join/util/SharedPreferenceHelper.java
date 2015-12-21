package com.ikaowo.join.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.common.framework.core.JApplication;
import com.common.framework.util.JLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikaowo.join.model.UserLoginData;

/**
 * Created by weibo on 15-12-21.
 */
public class SharedPreferenceHelper {

  private static SharedPreferenceHelper instance;

  private final String USER_INFO = "user_info";
  private final String USER = "user";

  private Gson gson;
  private Context context;
  private SharedPreferenceHelper() {
    this.context = JApplication.getInstance().getApplicationContext();
    this.gson = new GsonBuilder().create();
  }

  public static SharedPreferenceHelper getInstance(){
    if (instance == null) {
      instance = new SharedPreferenceHelper();
    }
    return instance;
  }

  public void saveUser(UserLoginData data) {
    try {
      SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
      SharedPreferences.Editor userEditor = sp.edit();
      userEditor.putString(USER, gson.toJson(data));
    } catch (Exception e) {
      JLog.d("SharePreference", e.getMessage());
    }
  }

  public UserLoginData getUser() {
    try {
      SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
      String str = sp.getString(USER, "");
      if (TextUtils.isEmpty(str)) {
        return null;
      }
      Gson gson = new Gson();
      return gson.fromJson(str, UserLoginData.class);
    } catch (Exception e) {
      JLog.d("SharePreference", e.getMessage());
      return null;
    }
  }

  public void clearUser() {
    SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    SharedPreferences.Editor userEditor = sp.edit();
    userEditor.clear().commit();
  }

  public boolean isLogined() {
    UserLoginData user = getUser();
    return (user != null && user.uId > 0);
  }

  public int getUserId() {
    UserLoginData user = getUser();
    if (user == null) {
      return 0;
    }
    return user.uId;
  }
}
