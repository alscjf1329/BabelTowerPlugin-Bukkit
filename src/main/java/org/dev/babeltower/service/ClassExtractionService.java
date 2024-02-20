package org.dev.babeltower.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ClassExtractionService {

    public static List<String> extractFieldNames(Class<?> v) {
        return Arrays.stream(v.getDeclaredFields()).map(Field::getName).toList();
    }
}
