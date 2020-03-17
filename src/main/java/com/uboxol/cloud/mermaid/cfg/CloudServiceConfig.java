//package com.uboxol.cloud.mermaid.cfg;
//
//import com.uboxol.cloud.mermaid.aop.WebApiInterceptor;
//import com.uboxol.cloud.tengu.common.server.configuration.AlipayServiceConfiguration;
//import com.uboxol.cloud.tengu.common.server.swagger.SwaggerSupport;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * @author liyunde
// * @since 2019/10/18 15:06
// */
//@Configuration
//public class CloudServiceConfig extends AlipayServiceConfiguration {
//
//    public static final String SERVICE_NAME = "友宝视觉货柜客服";
//
////    @Autowired
////    PrometheusInterceptor interceptor;
//
//    @Autowired
//    WebApiInterceptor interceptor;
//
//    /**
//     * 开启文档，微服务自动注册文档地址
//     */
//    @Configuration
//    @EnableSwagger2
//    public static class Swagger2 extends SwaggerSupport {
//        @Override
//        protected String getDocTitle() {
//            return SERVICE_NAME;
//        }
//
//        @Override
//        protected ApiInfo apiInfo() {
//            return new ApiInfoBuilder()
//                .title(getDocTitle())
//                .description("美人鱼:友宝视觉货柜客服(Mermaid)服务接口")
//                .termsOfServiceUrl("友宝服务条款地URL")
//                .version("1.0")
//                .contact(contact())
//                .build();
//        }
//
//        @Override
//        protected Docket getDocket() {
//            ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).select();
//            // 要扫描的API(Controller)基础包
//            builder.apis(RequestHandlerSelectors.basePackage("com.uboxol.cloud.mermaid"));
//            builder.paths(PathSelectors.any());
//            Docket docket = builder.build();
//            docket.apiInfo(apiInfo());
//            docket.useDefaultResponseMessages(false);
//            return docket;
//        }
//
//        /**
//         * 当前维护者联系信息
//         *
//         * @return Contact
//         */
//        protected Contact contact() {
//            return new Contact("huyifan", "", "huyifan@ubox.cn");
//        }
//    }
//
//    @Bean
//    public CorsFilter webCorsFilter() {
//        //swagger ,WebMvcConfigurer 和 WebMvcConfigurationSupport 都不起作用,这里只能用过滤器处理
//        //参考: https://docs.spring.io/spring/docs/5.0.6.RELEASE/spring-framework-reference/web.html#mvc-cors-global
//
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
////        config.addAllowedOrigin("http://swagger.ubox.liyunde.com");
//        config.addAllowedOrigin("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/v2/**", config);
//
//        return new CorsFilter(source);
//    }
//
//    @Override
//    protected void addInterceptors(final InterceptorRegistry registry) {
//        super.addInterceptors(registry);
//        registry.addInterceptor(interceptor).addPathPatterns("/api/**");
//    }

//@Override
//public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
//    converters.add(mappingJackson2HttpMessageConverter());
//    }
//
//@Bean
//public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
//    return converter;
//    }
//}
