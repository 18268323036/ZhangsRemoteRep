package com.cy.driver.common.util;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by Administrator on 2015/8/31.
 */
public class XmlUtils {

    /**
     * xml转换成JavaBean
     * @author wyh
     */
    public static <T> T xmlToJavaBean(String xml, Class<T> c) {
        if(StringUtils.isEmpty(xml) || c == null){
            return null;
        }
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T)unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
