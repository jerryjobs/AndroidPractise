package com.ikaowo.join.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.common.framework.core.JApplication;
import com.common.framework.util.JLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikaowo.join.model.UserLoginData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-21.
 */
public class SharedPreferenceHelper {

  private static SharedPreferenceHelper instance;

  private final String USER_INFO = "user_info";
  private final String USER = "user";
  private final String HISTORY = "history";
  private final String SEARCH_HISTORY = "search_history";

  private Gson gson;
  private Context context;

  private SharedPreferenceHelper() {
    this.context = JApplication.getInstance().getApplicationContext();
    this.gson = new GsonBuilder().create();
  }

  public static SharedPreferenceHelper getInstance() {
    if (instance == null) {
      instance = new SharedPreferenceHelper();
    }
    return instance;
  }

  /**
   * user releated
   */
  public void saveUser(UserLoginData data) {
    try {
      SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
      SharedPreferences.Editor userEditor = sp.edit();
      userEditor.putString(USER, gson.toJson(data));
      userEditor.commit();
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

  public int getUserCompanyId() {
    UserLoginData user = getUser();
    if (user == null) {
      return 0;
    }
    return user.companyId;
  }

  /*
   * search history related.
   */
  public void clearSearcHistory(Context context) {
    SharedPreferences sp = context.getSharedPreferences(HISTORY, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.remove(SEARCH_HISTORY);
    edit.commit();
  }

  public List<String> getSearchHistory(Context context) {
    SharedPreferences sp = context.getSharedPreferences(HISTORY, 0);
    String history = sp.getString(SEARCH_HISTORY, "");
    if (!TextUtils.isEmpty(history)) {
      List<String> historyList = null;
      try {
        historyList = new GsonBuilder().create().fromJson(history, List.class);
      } catch (Exception e) {

      }
      return historyList;
    }
    return null;
  }

  public void removeHistory(Context context, String history) {
    SharedPreferences sp = context.getSharedPreferences(HISTORY, 0);
    SharedPreferences.Editor edit = sp.edit();
    List<String> historyList = getSearchHistory(context);
    if (historyList == null || historyList.size() == 0) {
      return;
    } else {
      historyList.remove(history);
      String s = new GsonBuilder().create().toJson(historyList);
      edit.putString(SEARCH_HISTORY, s);
      edit.commit();
    }
  }

  public void saveSearchHistory(Context context, String s) {
    SharedPreferences sp = context.getSharedPreferences(HISTORY, 0);
    SharedPreferences.Editor edit = sp.edit();
    List<String> historyList = getSearchHistory(context);
    if (historyList == null) {
      historyList = new ArrayList<>();
      historyList.add(s);
    } else {
      historyList.remove(s);
      if (historyList.size() >= 15) {
        historyList.remove(0);
      }
      historyList.add(s);
    }
    String historyListStr = new GsonBuilder().create().toJson(historyList);
    edit.putString(SEARCH_HISTORY, historyListStr);
    edit.commit();
  }

}
