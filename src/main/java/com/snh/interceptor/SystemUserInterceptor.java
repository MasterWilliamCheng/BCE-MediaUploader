package com.snh.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by snh007 on 2016/12/21.
 */
public class SystemUserInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		if(requestURI.indexOf("console")>0 || requestURI.indexOf("index")>0 || requestURI.indexOf("player")>0){
			Object user = request.getSession().getAttribute("dataUser");
			if(user!=null){
				//登陆成功的用户
				return true;
			}else{
				//没有登陆，转向登陆界面
				response.sendRedirect(request.getContextPath() + "/login");
				return false;
			}
		}else{
			return true;
		}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
