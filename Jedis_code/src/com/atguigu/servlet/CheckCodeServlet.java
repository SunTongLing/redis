package com.atguigu.servlet;

import com.atguigu.utils.CodeConfig;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验验证码的Servlet
 */
public class CheckCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取用户输入的手机号
		String phoneNo = request.getParameter("phone_no");
		//获取用户输入的验证码
		String inputCode = request.getParameter("verify_code");
		//验空
		if(phoneNo == null || inputCode ==null){
			return;
		}
		//向Redis中获取验证码的key
		String codeKey=CodeConfig.PHONE_PREFIX + phoneNo + CodeConfig.PHONE_SUFFIX;
		//创建Jedis对象
		Jedis jedis = new Jedis(CodeConfig.HOST, CodeConfig.PORT);
		//获取redis中的验证码
		String redisCode = jedis.get(codeKey);
		//判断用户输入的验证码和从redis中获取的验证码是否一致
		if(inputCode.equals(redisCode)){
		jedis.del(codeKey);
		//给前端响应字符串
			response.getWriter().write("true");
		}
		jedis.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
