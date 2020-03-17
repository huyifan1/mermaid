package com.uboxol.cloud.mermaid.cfg;

import com.uboxol.cloud.mermaid.aop.RequestUserArgumentResolver;
import com.uboxol.cloud.mermaid.aop.WebApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * model: mermaid
 * <p>
 * 本配置与 CloudServiceConfig 是重复冲突配置,如果使用开放平台,请去除本配置
 *
 * @author liyunde
 * @since 2019/11/4 15:32
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class NoOpenCloudConfig implements WebMvcConfigurer {

    public static final String SERVICE_NAME = "友宝视觉货柜客服";

    @Autowired
    WebApiInterceptor interceptor;

    /**
     * Configure the {@link HttpMessageConverter HttpMessageConverters} to use for reading or writing
     * to the body of the request or response. If no converters are added, a
     * default list of converters is registered.
     * <p><strong>Note</strong> that adding converters to the list, turns off
     * default converter registration. To simply add a converter without impacting
     * default registration, consider using the method
     * {@link #extendMessageConverters(List)} instead.
     *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        //这里配置与默认冲突
//        converters.add(mappingJackson2HttpMessageConverter());
    }

//    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        return converter;
    }

    @Bean
    public CorsFilter webCorsFilter() {
        //swagger ,WebMvcConfigurer 和 WebMvcConfigurationSupport 都不起作用,这里只能用过滤器处理
        //参考: https://docs.spring.io/spring/docs/5.0.6.RELEASE/spring-framework-reference/web.html#mvc-cors-global

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/v2/**", config);

        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestUserArgumentResolver());
    }

//    @Bean
    public RequestUserArgumentResolver requestUserArgumentResolver() {
        return new RequestUserArgumentResolver();
    }

    /**
     * 开启文档，微服务自动注册文档地址
     */
    @Bean
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(SERVICE_NAME)
            .description("美人鱼:友宝视觉货柜客服(Mermaid)服务接口")
            .termsOfServiceUrl("友宝服务条款地URL")
            .version("1.0")
            .contact(contact())
            .build();
    }

    @Bean
    protected Docket getDocket() {
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).select();
        // 要扫描的API(Controller)基础包
        builder.apis(RequestHandlerSelectors.basePackage("com.uboxol.cloud.mermaid"));
        builder.paths(PathSelectors.any());
        Docket docket = builder.build();
        docket.apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return docket;
    }

    /**
     * 当前维护者联系信息
     *
     * @return Contact
     */
    @Bean
    protected Contact contact() {
        return new Contact("huyifan", "", "huyifan@ubox.cn");
    }

}
