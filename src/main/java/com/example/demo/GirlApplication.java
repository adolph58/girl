package com.example.demo;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.List;

//extends WebMvcConfigurerAdapter 第一种方法注入fastjson
// extends SpringBootServletInitializer
@SpringBootApplication
public class GirlApplication{

	/**
	 * 第一种方法注入fastjson
	 * 1.需要先定义一个 convert 转换消息的对象；
	 * 2.添加 fastJson 的配置信息，比如：是否要格式化返回的 json 数据；
	 * 3.在 convert 中添加配置信息；
	 * 4.将 convert 添加到 converters 当中。
	 * @param converters
     */
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		super.configureMessageConverters(converters);
//		//1.需要先定义一个 convert 转换消息的对象；
//		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//		//2.添加 fastJson 的配置信息，比如：是否要格式化返回的 json 数据；
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//		//3.在 convert 中添加配置信息；
//		fastConverter.setFastJsonConfig(fastJsonConfig);
//		//4.将 convert 添加到 converters 当中。
//		converters.add(fastConverter);
//	}

	/**
	 * 第二种方法注入fastjson
	 * @return
     */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		//1.需要先定义一个 convert 转换消息的对象；
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		//2.添加 fastJson 的配置信息，比如：是否要格式化返回的 json 数据；
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

		//3、处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);

		//4.在 convert 中添加配置信息；
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(GirlApplication.class);
//	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory config = new MultipartConfigFactory();
		config.setMaxFileSize("5120MB");
		config.setMaxRequestSize("5120MB");
		return config.createMultipartConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(GirlApplication.class, args);


	}
}
