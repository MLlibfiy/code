package com.shujia.service;

import com.shujia.bean.WeiBo;
import com.shujia.dao.WeiBoDao;
import com.shujia.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

@Service
public class WeiBoServiceImpl  implements WeiBoService{

    @Autowired
    private WeiBoDao weiBoDao;

    @Override
    public WeiBo findById(String id) {

        /**
         * 加入缓存
         *
         */

        //1、查询缓存数据库
        Jedis jedis = RedisClient.getJedis();
        jedis.select(6);

        String content = jedis.get(id);

        //如果缓存数据库里面有数据，直接返回
        if (content != null){
            //更新过期时间为15分钟
            jedis.expire(id,15*60);
            return new WeiBo(id, content);
        }

        // 如果缓存数据库没有数据，查询关系型数据库
        WeiBo weiBo = weiBoDao.findById(id);

        SetParams setParams = new SetParams();
        setParams.ex(15*60);

        //将查询回来的结果存到缓存数据库中，设置过期时间为15分钟
        jedis.set(weiBo.getId(),weiBo.getContent(),setParams);

        return weiBo;
    }
}
