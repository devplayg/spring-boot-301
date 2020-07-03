package com.devplayg.hippo.config

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberSecuredDto
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.util.auditLog
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
//@EnableConfigurationProperties
class HippoConfig(
        private val appConfig: AppConfig
) {
    companion object : KLogging()

    @Bean
    fun initDatabase(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
        Database.connect(HikariDataSource(HikariConfig().apply {
            jdbcUrl = appConfig.dataSource.url
            maximumPoolSize = 10
            minimumIdle = 10
            isAutoCommit = false
            username = appConfig.dataSource.username
            password = appConfig.dataSource.password
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }))

        auditLog(0, AuditCategory.APPLICATION_STARTED.value)

        transaction {
            Members.selectAll().map {
                memberCacheRepo.save(toMemberSecuredDto(it))
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
