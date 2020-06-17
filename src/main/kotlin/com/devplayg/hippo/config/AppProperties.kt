package com.devplayg.hippo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class AppProperties(
        val version: String = "",
        val whatElse: String = "",
        val pathPatternsNotToBeIntercepted: ArrayList<String> = ArrayList()
) {


//    @Bean
//    fun initInMemoryMember(): InMemoryMemberManager {
//        return InMemoryMemberManager()
//    }
}

