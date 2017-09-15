package com.cy.driver.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangxy 2017/9/5 17:18
 */
@Repository
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, Object> valOpsStr;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOpsObj;

    /**
     * 根据指定key获取String
     * @param key
     * @return
     */
    public Object getStr(String key){
        return valOpsStr.get(key);
    }

    /**
     * 设置Str缓存
     * @param key
     * @param val
     */
    public void setStr(String key, String val){
        valOpsStr.set(key,val);
    }

    public void setStr(String key, String val, Date delTime){
        long time = delTime.getTime()-new Date().getTime();
        valOpsStr.set(key,val,time/1000, TimeUnit.SECONDS);
    }

    /**
     * 放入缓存服务器
     * @param key   缓存key值
     * @param val 缓存value值(object对象)
     * @param ttl   缓存有效时间,单位秒,-1表示永久缓存
     */
    public void setStr(String key, String val, long ttl){
        if(ttl==-1){
            valOpsStr.set(key,val);
        }else{
            valOpsStr.set(key,val,ttl,TimeUnit.SECONDS);
        }
    }

    public void setStr(String key,Object obj, Date delTime){
        long time = delTime.getTime()-new Date().getTime();
        valOpsStr.set(key,obj,time/1000);
    }

    /**
     * 删除指定key
     * @param key
     */
    public void del(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 根据指定o获取Object
     * @param o
     * @return
     */
    public Object getObj(Object o){
        return valOpsObj.get(o);
    }

    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     */
    public void setObj(Object o1, Object o2){
        valOpsObj.set(o1, o2);
    }

    /**
     * 删除Obj缓存
     * @param o
     */
    public void delObj(Object o){
        redisTemplate.delete(o);
    }

}
