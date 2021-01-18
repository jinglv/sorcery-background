package com.sorcery.api.common.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

/**
 * 拷贝实体工具类
 *
 * @author jingLv
 * @date 2021/01/18
 */
public class CopyUtils {
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new HashMap<String, BeanCopier>();

    /**
     * 使用Cglib的BeanCopier实现对象的拷贝
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesCglib(Object source, Object target) {
        String beanKey = generateBeanKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIER_MAP.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_MAP.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    public static String generateBeanKey(Class<?> source, Class<?> target) {
        return source.getName() + "@" + target.getName();
    }
}
