package com.devplayg.hippo.config

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.entity.Audit
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.util.AuditMessage
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.kotlin.com.google.gson.Gson
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class HippoConfig {
    @Bean
    fun initDatabase(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
//        Database.connect(HikariDataSource(hikariConfig()))
        Database.connect(HikariDataSource(HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://127.0.0.1:3306/sb201?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC"
            maximumPoolSize = 10
            minimumIdle = 10
            isAutoCommit = false
            username = "root"
            password = "devplayg12!@"
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }))
        transaction {
//            SchemaUtils.create(Members, Audits)
            Audit.new {
                memberId = 0
                category = AuditCategory.APPLICATION_STARTED.value
                message = Gson().toJson(AuditMessage(AuditCategory.APPLICATION_STARTED.description))
                ip = 0
            }
            Members.selectAll().map {
                memberCacheRepo.save(toMemberDto(it))
            }
        }
    }

//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    fun dataSource(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
//        Database.connect(DataSourceBuilder.create().type(HikariDataSource::class.java).build())
//    }

//    @Bean
//    fun initDatabase(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
////        Database.connect(DataSourceBuilder.create().type(HikariDataSource::class.java).build())
////        Database.connect(HikariDataSource(HikariConfig().apply {
////            DataSourceProperties()
////        }))
//        Database.connect(HikariDataSource(
//                DataSourceBuilder.create().type(HikariDataSource::class.java).build()
//                HikariConfig("spring.datasource")
//                HikariConfig("/hikari.properties")
//        ))

//        transaction {
//            Audit.new {
//                memberId = 0
//                category = 3333
//                message = "started"
//                ip = 132331
//            }
//        }
}
//}
//    @Bean
//    @Throws(SQLException::class)
//    fun dataSource(): DataSource? {
//        return HikariDataSource(this)
//    }
//    @Bean
//    fun initDatabase(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
////        Database.connect(HikariDataSource(hikariConfig()))
//        Database.connect(HikariDataSource(HikariConfig().apply {
//            jdbcUrl = "jdbc:mysql://127.0.0.1:3306/sb201?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC"
//            maximumPoolSize = 10
//            minimumIdle = 10
//            isAutoCommit = false
//            username = "root"
//            password = "Uniiot12!@"
//            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
//
//            validate()
//        }))
//        transaction {
////            SchemaUtils.create(Members, Audits)
////            Audit.new {
////                memberId = 0
////                category = 3333
////                message = "started"
////                ip = 132331
////            }
////            Member.all().map {
////                memberCacheRepo.save(it)
////            }
//            Members.selectAll().map {
//                memberCacheRepo.save(toMemberDto(it))
//            }
//        }
//    }
//}

//
//private fun hikariConfig() =
//        HikariConfig().apply {
//            driverClassName = "com.mysql.cj.jdbc.Driver"
//            jdbcUrl = "jdbc:mysql://127.0.0.1:3306/sb201?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC"
//            maximumPoolSize = 10
//            minimumIdle = 10
//            isAutoCommit = false
//            username = "root"
//            password = "Uniiot12!@"
//            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
//
//            validate()
//        }

// Database.connect("", driver = "", user = "root", password = "Uniiot12!@")
//suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
//    transaction {
//        block()
//    }
//}
