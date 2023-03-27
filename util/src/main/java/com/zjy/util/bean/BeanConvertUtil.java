package com.zjy.util.bean;

import cn.hutool.core.bean.BeanUtil;

import java.util.Map;

/**
 * @author zjy
 * @date 2022/5/19
 */
public class BeanConvertUtil {

    public static Map convertBeanToMap(Object bean) {
        Map<String, Object> beanMap = BeanUtil.beanToMap(bean);
        return beanMap;
    }

}
