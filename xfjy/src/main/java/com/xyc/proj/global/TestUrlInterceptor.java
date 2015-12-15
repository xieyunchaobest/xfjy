package com.xyc.proj.global;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.TestMain;

@Component
public class TestUrlInterceptor implements HandlerInterceptor {
	@Autowired
	Properties prop;

	public TestUrlInterceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// System.out.println("key==========="+prop.getWechatkey());
		// if(request.getRequestURI().equals("/error")){
		// System.out.println("-------------- input error path
		// ----------------");
		// //request.getRequestDispatcher("/invalidPage");
		// response.sendRedirect("/invalidPage");
		// }
		// System.out.println("-------------- TestUrlInterceptor work
		// -----------------" + request.getRequestURI());
		//
		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		boolean isok = TestMain.getLocalFilter(Constants.wechatkey);
		if (!isok) {
			response.sendRedirect("/err.html");
		}
			
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (ex != null) {
			throw ex;
		}
	}
}
