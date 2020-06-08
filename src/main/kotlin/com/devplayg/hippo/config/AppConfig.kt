package com.devplayg.hippo.config

import com.devplayg.hippo.entity.Audit
import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.framework.InMemoryMemberManager
import com.zaxxer.hikari.HikariConfig

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppConfig(
        var name: String = "",
        var version: String = "",
        var whatElse: String = "",
        var pathPatternsNotToBeIntercepted: ArrayList<String> = ArrayList()
) {



//    @Bean
//    fun initInMemoryMember(): InMemoryMemberManager {
//        return InMemoryMemberManager()
//    }
}

