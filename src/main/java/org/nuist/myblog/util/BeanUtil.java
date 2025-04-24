package org.nuist.myblog.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanUtil {
    public static Map<String, Object> getMapFromBean(Object bean) {
        if (bean == null) {
            return new HashMap<>();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        try {
            // 遍历对象的所有字段（包括私有字段）
            for (Field field : bean.getClass().getDeclaredFields()) {
                field.setAccessible(true); // 允许访问私有字段
                Object value = field.get(bean);
                map.put(field.getName(), value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to convert bean to map", e);
        }
        return map;
    }

}
