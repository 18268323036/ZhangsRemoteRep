package com.cy.driver.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Type ListObjConverter
 * @Desc 对象集合相互转换，对象必须有对称get、set方法，不适用于复杂类型
 * @author xianchao.yan
 * @date 2015.07.17
 */
public class ListObjConverter {
    private static Logger LOG = LoggerFactory.getLogger(ListObjConverter.class);

    public static <T> List<T> convert(List<?> source, Class<T> clazz, ObjConverter.ForceMatch... forceMatchs) {
        List<T> target = new ArrayList<T>();
        try {
            if (source == null) {
                if(LOG.isErrorEnabled())LOG.error("对象集合转换异常，来源对象集合为空", source, clazz, target);
                return target;
            }
            for (Object sourceObj : source) {
                target.add(ObjConverter.convert(sourceObj, clazz, forceMatchs));
            }
        } catch (Exception e) {
            if(LOG.isErrorEnabled())LOG.error("对象集合转换异常", e, source, clazz);
            throw new RuntimeException(e);
        }
        return target;

    }
}
