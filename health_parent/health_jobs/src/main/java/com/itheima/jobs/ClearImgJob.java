package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //找出redis中存储两个图片set中的差集
        Set<String> set =
                jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                //在七牛上面删除
                QiniuUtils.deleteFileFromQiniu(picName);
                //在redis中删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }
    }
}
