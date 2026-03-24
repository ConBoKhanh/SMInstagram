package com.example.sminstagram.bases;

import java.lang.reflect.Field;

public class BaseService {
    protected <T> void mergeFields(T source, T target) {
        Class<?> clazz = source.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot merge field: " + field.getName(), e);
            }
        }
    }
}
