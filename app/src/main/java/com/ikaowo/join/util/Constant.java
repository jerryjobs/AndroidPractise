package com.ikaowo.join.util;

import com.ikaowo.join.R;

import java.util.Random;

/**
 * Created by leiweibo on 12/27/15.
 */
public class Constant {
  public static final String PROCESSING = "正在处理中, 请稍后";
  public static final String DATAGETTING = "正在获取数据, 请稍后";
  public static final String LOGINING = "正在获取数据, 请稍后";

  public static final String BRAND_ID = "brand_id";
  public static final String PROMPTION_ID = "promption_id";
  public static final String USER_ID = "user_id";
  public static final String URL = "url";
  public static final String SHAREW_TITLE = "title";
  public static final String SHAREW_SUMMARY ="summary";
  public static final String SHAREW_IMG_URL = "bg_url";
  public static final String SHOW_OPTION_MENU = "show_option_menu";

  public static final int SEARCH_TYPE_PROMPTION = 1;
  public static final int SEARCH_TYPE_BRAND = 2;

  public static final String PROMPTION_STATE_NEW = "N";
  public static final String PROMPTION_STATE_PASS = "T";
  public static final String PROMPTION_STATE_FAILED = "F";
  public static final String PROMPTION_STATE_DONE = "O";
  public static final String PROMPTION_STATE_OVER = "E";
  public static final String PROMPTION_STATE_CANCEL = "C";

  public static final String JOIN_STATE_PASS = "T";
  public static final String JOIN_STATE_FAILED = "F";
  public static final String JOIN_STATE_PENDING_APPROVE = "J";
  public static final String JOIN_STATE_USER_CANCEL = "X";//用户取消
  public static final String JOIN_STATE_CANCEL = "X";//主办方取消
  public static final String JOIN_STATE_JOINED = "P"; //审核通过，并且参与
  public static final String JOIN_STATE_NOT_JOINED = "q"; //审核通过，但是没有参与

  public static final String AUTH_STATE_PASS = "T";
  public static final String AUTH_STATE_FAILED = "F";

  public static Integer SHARE = 1;
  public static int EDIT = 2;
  public static int NONE = 3;

  public static final int[] HEADER_COLOR_ARRAY = new int[]{
    R.color.header_c1,
    R.color.header_c2,
    R.color.header_c3,
    R.color.header_c4,
    R.color.header_c5,
    R.color.header_c6,
    R.color.header_c7,
    R.color.header_c8,
    R.color.header_c9,
    R.color.header_c10
  };

  public static int getRandomColor() {
    Random random = new Random(System.currentTimeMillis());
    int index = Math.abs(random.nextInt()) % 10;
    return HEADER_COLOR_ARRAY[index];
  }
}
