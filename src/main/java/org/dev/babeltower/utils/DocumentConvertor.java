package org.dev.babeltower.utils;

import java.lang.reflect.Field;
import org.bson.Document;

public class DocumentConvertor {

    public static <T> T convertTo(Document document, Class<T> tClass)
        throws ReflectiveOperationException {
        tClass.getDeclaredConstructor().setAccessible(true);
        T tInstance = tClass.getDeclaredConstructor().newInstance();
        for (Field field : tClass.getDeclaredFields()) {
            String fieldName = field.getName();
            if (document.containsKey(fieldName)) {
                Object value = document.get(fieldName);
                if (value == null) {
                    continue;
                }
                setFieldValue(tInstance, field, value);
            }
        }
        return tInstance;
    }

    private static void setFieldValue(Object instance, Field field, Object value)
        throws ReflectiveOperationException {
        try {
            field.setAccessible(true); // 필드의 접근성 설정
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectiveOperationException("Error setting field value.", e);
        }
    }
}
