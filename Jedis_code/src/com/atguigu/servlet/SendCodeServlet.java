package com.atguigu.servlet;

import com.atguigu.utils.CodeConfig;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * 发送验证码的Servlet
 */
public class SendCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取用户输入的手机号
        String phoneNo = request.getParameter("phone_no");
        //验空
        if (phoneNo == null || "".equals(phoneNo)) {
            return;
        }
        //创建Jedis对象
        Jedis jedis = new Jedis(CodeConfig.HOST, CodeConfig.PORT);
        //拼接向redis中保存计数器的key
        String countKey = CodeConfig.PHONE_PREFIX + phoneNo + CodeConfig.COUNT_SUFFIX;
        //根据计数器的key从Redis中获取计数器的值
        String sendCount = jedis.get(countKey);
        //判断计数器的值
        if ("3".equals(sendCount)) {
            //该手机已发送3次验证码
            //给前端响应一个limit字符串
            response.getWriter().write("limit");
            return;
        }
        if(sendCount == null) {
            //没发过验证码
            //将该手机号在Redis中对应计数器的值设置为1,设置计数器的有效时间为1天
            jedis.setex(countKey, CodeConfig.SECONDS_PER_DAY, "1");
        }else {

            //当日发送次数不到3次
            jedis.incr(countKey);
        }
            //拼接向Redis中保存验证码的key
            String codeKey = CodeConfig.PHONE_PREFIX + phoneNo + CodeConfig.PHONE_SUFFIX;
            //生成6位验证码
            String code = getCode(6);
            //将生成的验证码保存到redis中，并设置120秒有效时间
            jedis.setex(codeKey, CodeConfig.CODE_TIMEOUT, code);
            //向你的手机屏幕发送验证码
            System.out.println("您的验证码是：" + code);
            //关闭jedis
            jedis.close();
            //给前端响应一个字符串true
            response.getWriter().write("true");

        }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


    //随机生成验证码的方法
    private String getCode(int len){
        String code = "";
        for(int i =0;i < len;i++){
            int rand = new Random().nextInt(10);
            code += rand;

        }
        return code;
    }
}
