package com.yandexmobilization.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class Utils {

    public static Type getGenericType(Object object) {
        Type[] cl = object.getClass().getGenericInterfaces();
        return ((ParameterizedType)(cl[0])).getActualTypeArguments()[0];
    }

}
