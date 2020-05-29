package com.devplayg.hippo.config

import com.devplayg.hippo.interceptor.RequestInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors


@Configuration
class WebConfig(
        val requestInterceptor: RequestInterceptor,
        val appConfig: AppConfig
) : WebMvcConfigurer {


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

    /**
     * Timezone
     */
//    @Bean
//    fun timezoneList(): List<TimeZone>? {
//        val now: LocalDateTime = LocalDateTime.now()
//        return ZoneId.getAvailableZoneIds().stream()
//                .map { zoneId: String? -> ZoneId.of(zoneId) }
//                .sorted(ZoneComparator())
//                .map { id: ZoneId -> TimeZone(id.id, getOffset(now, id)) }
//                .collect(Collectors.toList())
//    }
//
//    private class ZoneComparator : Comparator<ZoneId?> {
//        fun compare(zoneId1: ZoneId, zoneId2: ZoneId): Int {
//            return zoneId1.toString().compareTo(zoneId2.toString(), ignoreCase = true)
//        }
//    }
//
//    private fun getOffset(dateTime: LocalDateTime, id: ZoneId): String? {
//        return dateTime
//                .atZone(id)
//                .getOffset()
//                .getId()
//                .replace("Z", "+00:00")
//    }
}
