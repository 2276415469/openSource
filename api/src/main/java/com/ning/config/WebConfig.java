package com.ning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//@Configuration
//@EnableWebMvc
public class WebConfig  {

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 保留默认的转换器
//        converters.add(new MappingJackson2HttpMessageConverter());
//    }
}
