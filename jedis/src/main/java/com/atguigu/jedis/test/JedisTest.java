package com.atguigu.jedis.test;


import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisTest {

    /*
    测试连接
     */
    @Test
    public void testConnection(){
        Jedis jedis = new Jedis("192.168.6.100", 6379);
        //ping
        String ping = jedis.ping();
        System.out.println(ping);

    }

    /*
    测试String类型
     */
    @Test
    public void testString(){
        Jedis jedis = new Jedis("192.168.6.100", 6379);
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        String set = jedis.set("k1", "v1");
        Set<String> keys1 = jedis.keys("*");
        System.out.println(keys1);
    }
}
