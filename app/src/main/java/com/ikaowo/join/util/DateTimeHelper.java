package com.ikaowo.join.util;

import android.text.TextUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weibo on 15-12-23.
 */
public class DateTimeHelper {

  private DateFormat dateTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
  private DateFormat notificationDf = new SimpleDateFormat("MM-dd HH:mm");
  //2016-01-29T00:00:00.000Z

  public String getTime(String timeStr) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    try {
      Date date = dateFormat.parse(timeStr);
      return dateFormate.format(date);
    } catch (Exception e) {
      return timeStr;
    }
  }

  public String getNotificationTime(String timeStr) {
    DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = dateFormate.parse(timeStr);
      return notificationDf.format(date);
    } catch (Exception e) {
      return timeStr;
    }
  }

  public Map<Integer, Integer> getDateValue() {
    Calendar calendar = Calendar.getInstance();
    Map<Integer, Integer> map = new HashMap<>();
    map.put(Calendar.YEAR, calendar.get(Calendar.YEAR));
    map.put(Calendar.MONTH, calendar.get(Calendar.MONTH));
    map.put(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
    return map;
  }

  public Map<Integer, Integer> getDateValue(String dateStr) {
    Map<Integer, Integer> map = new HashMap<>();
    if (TextUtils.isEmpty(dateStr)) {
      map = getDateValue();
    } else {
      try {
        Date date = dateFormate.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        map.put(Calendar.YEAR, calendar.get(Calendar.YEAR));
        map.put(Calendar.MONTH, calendar.get(Calendar.MONTH));
        map.put(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
      } catch (ParseException e) {
        map = getDateValue();
      }
    }
    return map;
  }

  public String getFormatedTime(int year, int monthOfYear, int dayOfMonth) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, monthOfYear);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return dateTimeFormate.format(calendar.getTime());
  }

  public String getFormatedDate(int year, int monthOfYear, int dayOfMonth) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, monthOfYear);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    return dateFormate.format(calendar.getTime());
  }
}
