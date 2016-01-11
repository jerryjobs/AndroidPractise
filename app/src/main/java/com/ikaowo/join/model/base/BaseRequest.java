package com.ikaowo.join.model.base;

import com.common.framework.util.JLog;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weibo on 15-12-2.
 */
public class BaseRequest implements Serializable {
  public Map<String, Object> getMap() {

    Map<String, Object> map = new HashMap<String, Object>();
    Class cls = getClass();
    getFieldMap(map, cls);

    while (cls.getSuperclass() != null && cls.getSuperclass() != Object.class) {
      cls = cls.getSuperclass();
      getFieldMap(map, cls);
    }
    return map;
  }

  private Map<String, Object> getFieldMap(Map<String, Object> map, Class cls) {
    try {
      Field[] fields = cls.getDeclaredFields();

      for (Field f : fields) {
        f.setAccessible(true);
        map.put(f.getName(), f.get(this));
      }
    } catch (IllegalArgumentException e) {
      JLog.printStackTraceAndMore(e);
    } catch (IllegalAccessException e) {
      JLog.printStackTraceAndMore(e);
    }
    return map;
  }
}