package com.shujia.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

    static  JedisPool jedisPool;
    static {

        //创建连接池对象
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMinIdle(10);
        jedisPool = new JedisPool(config, "node1", 6379);
    }

    //获取连接方法
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

}
