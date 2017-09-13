package com.cy.driver.common.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     **/
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     **/
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     **/
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clear() {
        applicationContext = null;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    public void destroy() throws Exception {
        SpringContextHolder.clear();
    }

    private static void assertContextInjected() {
        if (applicationContext == null) throw new IllegalStateException(
                                                                        "applicaitonContext 未注入, 请在 applicationContext.xml 中定义 SpringContextHolder");
    }

//    public static UserInfo getCurrentUser() {
//        try {
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            HttpSession session = attr.getRequest().getSession();
//            UserInfo userInfo = (UserInfo) session.getAttribute(Constants.LOGIN_USER);
//            return userInfo;
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
}
