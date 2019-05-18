package com.example.demo.filter;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FinalLogFilter extends ZuulFilter{
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		log.debug("=========================================================================== TRACE START ===========================================================================");
		log.debug("SHOW ALL HEADERS");
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			if(!StringUtils.isEmpty(headerName)) {
				String headerValue = request.getHeader(headerName);
				if(!StringUtils.isEmpty(headerValue)) {
					log.debug(headerName + " : " + headerValue);
				}
			}
		}
		
//		log.debug("SHOW ALL ATTRIBUTES");
//		Enumeration<String> attributeNames = request.getAttributeNames();
//		while(attributeNames.hasMoreElements()) {
//			String attributeName = attributeNames.nextElement();
//			if(!StringUtils.isEmpty(attributeName)) {
//				Object attributeValue = request.getAttribute(attributeName);
//				if(!StringUtils.isEmpty(attributeValue)) {
//					log.debug(attributeName + " : " + attributeValue);
//				}
//			}
//		}
		
		log.debug("SHOW ALL PARAMETERS");
		Map<String, String[]> parameters = request.getParameterMap();
		if(parameters != null && !parameters.isEmpty()) {
			parameters.forEach((k,v ) -> {
				if(v.length > 0 && !StringUtils.isEmpty(k)) {
					log.debug(k + " : " + separateWithComma(v));
				}
			});
		}
		
		log.debug("SHOW ALL COOKIES");
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie != null) {
					log.debug(cookie.getName() + " : " + cookie.getValue() + ", path : " + cookie.getPath() + ", domain: " + cookie.getDomain());
				}
			}
		}
		
		log.debug("SHOW FULL URL");
		String url = getFullURL(request);
		log.debug("url: " + url);
		
		log.debug("=========================================================================== TRACE END ===========================================================================");
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return Integer.MAX_VALUE;
	}
	
	private String separateWithComma(String[] values) {
		String newStr = "";
		for(String str : values) {
			if(str != null && !str.trim().isEmpty()) {
				newStr = str + ",";
			}
		}
		if(!newStr.isEmpty()) {
			newStr = newStr.substring(0, newStr.length() - 1);
		}
		return newStr;
	}
	
	private String getFullURL(HttpServletRequest request) {
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
}
