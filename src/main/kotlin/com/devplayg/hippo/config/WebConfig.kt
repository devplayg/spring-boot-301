package com.devplayg.hippo.config

import com.devplayg.hippo.interceptor.RequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor


@Configuration
class WebConfig : WebMvcConfigurer {

    @Autowired
    lateinit var requestInterceptor: RequestInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        // Normal interceptor
        registry.addInterceptor(requestInterceptor)

//        registry.addInterceptor(localeChangeInterceptor());

        // Add interceptor to registry
//        registry.addInterceptor(new RequestInterceptor(appConfig))
//                .excludePathPatterns(appConfig.getPathPatternsNotToBeIntercepted());

    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor? {
        val lci = LocaleChangeInterceptor()
        lci.setHttpMethods()
        lci.paramName = "lang"
        return lci
    }
}
