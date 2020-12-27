package com.devplayg.hippo.config

import com.devplayg.hippo.framework.RequestInterceptor
import mu.KLogging
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import vo.TimezoneView
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.zone.ZoneRulesException
import java.util.*


@Configuration
// @EnableWebSecurity
class WebConfig(
        val requestInterceptor: RequestInterceptor,
        val appConfig: AppConfig
) : WebMvcConfigurer {

    companion object : KLogging()

    override fun addInterceptors(registry: InterceptorRegistry) {
        // Normal interceptor
//        registry.addInterceptor(requestInterceptor)

//        registry.addInterceptor(localeChangeInterceptor());

        // Add interceptor to registry
        registry.addInterceptor(requestInterceptor)
                .excludePathPatterns(appConfig.pathPatternsNotToBeIntercepted);

    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor? {
        val lci = LocaleChangeInterceptor()
        lci.setHttpMethods()
        lci.paramName = "lang"
        return lci
    }

    @Bean
    fun messageSource(): MessageSource? {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:i18n/messages")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setCacheSeconds(10)
        return messageSource
    }

//    @Bean
//    fun transactionManager(dataSource: HikariDataSource): SpringTransactionManager {
//        return SpringTransactionManager(
//                dataSource, DEFAULT_REPETITION_ATTEMPTS)
//    }

    /**
     * Timezone
     */
    /**
     * Timezone
     */


    @Bean
    fun timezoneList() = TimeZone.getAvailableIDs().mapNotNull { id ->
        val timeZone = TimeZone.getTimeZone(id) ?: return@mapNotNull null
        val zone: ZoneId
        try {
            zone = ZoneId.of(id)
        } catch (e: ZoneRulesException) {
            return@mapNotNull null
        }

        TimezoneView(id, name = timeZone.displayName, offsetName = OffsetDateTime.now(zone).offset.id.replace("Z", "+00:00"), offset = timeZone.rawOffset)
    }.sortedBy { it.id }
}
