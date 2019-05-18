package com.example.demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * @author herna
 * @see <a href="https://github.com/spring-projects/spring-security-oauth/issues/140">https://github.com/spring-projects/spring-security-oauth/issues/140</a>
 */
public class OauthSessionInvalidationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null && modelAndView.getView() instanceof RedirectView) {
			RedirectView redirect = (RedirectView) modelAndView.getView();
			String url = redirect.getUrl();
			if (url.contains("error=")) {
				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate();
					SecurityContextHolder.clearContext();
				}
			}
		}
	}
}
