package com.ifast.sys.filter;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class WebInterceptor extends HandlerInterceptorAdapter {

   @Override
   public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
       throws Exception {
     // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
   }

   @Override
   public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
       throws Exception {
     // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
   }

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
     // 在请求处理之前进行调用（Controller方法调用之前）
     System.out.println("在请求处理之前进行调用（Controller方法调用之前）");
       HttpServletRequest req =(HttpServletRequest) request;
       Map<String, String[]> map1  = request.getParameterMap();
       ParameterRequestWrapper wrapRequest = new ParameterRequestWrapper(req,map1);
       request = wrapRequest;


     Map<String, String[]> map  = request.getParameterMap();
       SensitiveService sensitiveService = new SensitiveService();
       for (String key : map.keySet()) {
           String[] value = map.get(key);
           if (value.length!=0){
               for (int i=0;i<value.length;i++){
                   value[i] =  sensitiveService.filter(value[i]);
               }
           }
//           map.put(key, value);
       }
     return true;
   }
}