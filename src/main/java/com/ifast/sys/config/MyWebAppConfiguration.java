package com.ifast.sys.config;

import com.ifast.common.utils.GenUtils;
import com.ifast.sys.filter.WebInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;

/**
 * @author yfj
 */
@Configuration
public class MyWebAppConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * @Description: 对文件的路径进行配置,创建一个虚拟路径/Path/** ，即只要在<img src="/common/picName.jpg" />便可以直接引用图片
         *这是图片的物理路径  "file:/+本地图片的地址"
         * @Date： Create in 14:08 2017/12/20
         *
         */
        org.apache.commons.configuration.Configuration conf= GenUtils.getConfigFile();
        registry.addResourceHandler("/common/**").addResourceLocations("file:"+conf.getString("file")+"/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可添加多个
        registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
    }
}
