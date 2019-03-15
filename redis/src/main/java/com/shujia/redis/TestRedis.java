package com.shujia.redis;

import org.junit.Test;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.Map;

public class TestRedis {

    @Test
    public void testredis() {

        //创建连接
        Jedis jedis = new Jedis("node1", 6379);

        //默认连接db0  可以手动修改
        jedis.select(3);

        //执行操作
        jedis.set("java", "redis");
        System.out.println(jedis.get("java"));

        jedis.set("k1", "a");
        jedis.set("k2", "b");

        //String ... keys   可变参数，可变参数只能放在最后面
        jedis.bitop(BitOP.OR, "k3", "k1", "k2");
        System.out.println(jedis.get("k3"));


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("f1", "v1");
        map.put("f2", "v2");
        map.put("f3", "v3");

        //将java 中的map保存到redis
        jedis.hmset("h1",map);

        //查询map
        Map<String, String> map1 = jedis.hgetAll("h1");
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }


        //设置参数
        SetParams setParams = new SetParams();
        //5秒过期
        setParams.ex(5);
        jedis.set("k4","v4",setParams);

        //关闭连接
        jedis.close();

    }

}
