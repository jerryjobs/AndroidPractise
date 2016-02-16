package com.ikaowo.join.util;

import com.ikaowo.join.R;

/**
 * Created by leiweibo on 12/27/15.
 */
public class Constant {
  public static final String PROCESSING = "正在处理中, 请稍后";
  public static final String DATAGETTING = "正在获取数据, 请稍后";
  public static final String LOGINING = "正在获取数据, 请稍后";

  public static final String BRAND_ID = "brand_id";
  public static final String SHOW_STATE = "show_state";
  public static final String PROMPTION_ID = "promption_id";
  public static final String NOTIFICATION_TYPE = "notification_type";
  public static final String USER_ID = "user_id";
  public static final String URL = "url";
  public static final String SHAREW_TITLE = "title";
  public static final String SHAREW_SUMMARY = "summary";
  public static final String SHAREW_IMG_URL = "bg_url";
  public static final String SHOW_OPTION_MENU = "show_option_menu";

  public static final String NEED_RETRIEVE_LATEST_STATE = "need_check_latest_state";

  public static final int SEARCH_TYPE_PROMPTION = 1;
  public static final int SEARCH_TYPE_BRAND = 2;

  public static final String PROMPTION_STATE_NEW = "N"; //审核中
  public static final String PROMPTION_STATE_PASS = "T"; //已上线
  public static final String PROMPTION_STATE_FAILED = "F"; //审核未通过
  public static final String PROMPTION_STATE_DONE = "O"; //已完成
  public static final String PROMPTION_STATE_OVER = "E"; //报名已结束
  public static final String PROMPTION_STATE_CANCEL = "C";//已取消

  public static final String JOIN_STATE_PASSED = "T"; //已同意
  public static final String JOIN_STATE_FAILED = "F"; //已拒绝
  public static final String JOIN_STATE_PENDING_APPROVE = "J";//待确认
  public static final String JOIN_STATE_USER_CANCEL = "X";//用户取消
  public static final String JOIN_STATE_CANCEL = "C";//主办方已取消
  public static final String JOIN_STATE_JOINED = "P"; //已参加
  public static final String JOIN_STATE_NOT_JOINED = "Q"; //未参加

  public static final String AUTH_STATE_PASSED = "T";
  public static final String AUTH_STATE_FAILED = "F";
  public static final String AUTH_STATE_PENDING_APPROVE = "N";

  public static final String CHANGE_TAB = "change_tab";

  public static final String USER_STATE_PREFIX = "user_state";
  public static final String JOIN_STATE_PREFIX = "join_state";
  public static final String PROMPTION_STATE_PREFIX = "activity_state";
  public static final int[] HEADER_COLOR_ARRAY = new int[] {
      R.color.header_c1, R.color.header_c2, R.color.header_c3, R.color.header_c4, R.color.header_c5,
      R.color.header_c6, R.color.header_c7, R.color.header_c8, R.color.header_c9, R.color.header_c10
  };
  public static final String GETUI_STATE_T = "T";
  public static final String SYS = "android";
  public static final String PUSH_PROMPTION_PASSED = "G_SOC_001"; //活动审核通过
  public static final String PUSH_PROMPTION_FAILED = "G_SOC_002"; //活动审核未通过
  public static final String PUSH_JOIN_NEW = "G_JOIN_001"; //新的报名成功 - 合作伙伴列表
  public static final String PUSH_JOIN_JOINED = "G_JOIN_002"; //您已参加了一个活动 - 我的品牌主页
  public static final String PUSH_ACCT_PASSED = "G_REG_001"; //账号审核通过 - 查看个人主页
  public static final String PUSH_ACCT_FAILED = "G_REG_002"; //账号审核未通过 -重新认证页面
  public static final String PUSH_SYS_1 = "G_SYS_001"; //　公告，跳转到  h5
  public static final String PUSH_SYS_2 = "G_SYS_002"; //  系统活动 h5
  public static final String IM_CHAT = "IM_CHAT";
  public static final String PUSH_INTENT_EXTRA = "data";
  public static final String INTRODUCE_INTENT_EXTRA = "introduce";
  public static Integer SHARE = 1;
  public static int EDIT = 2;
  public static int NONE = 3;

  public static int getRandomColor(int userId) {
    int index = userId % 10;
    return HEADER_COLOR_ARRAY[index];
  }
}
