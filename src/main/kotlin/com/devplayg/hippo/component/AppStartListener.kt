package com.devplayg.hippo.component

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.define.DBTZ
import com.devplayg.hippo.define.SystemMemberId
import com.devplayg.hippo.entity.Assets
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.repository.AssetCacheRepo
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.util.auditLog
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KLogging
import org.apache.http.client.utils.URLEncodedUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTimeZone
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Application listener
 */
@Component
class AppStartedListener(
        private val appConfig: AppConfig,
        private val memberCacheRepo: MemberCacheRepo,
        private val assetCacheRepo:AssetCacheRepo

) : ApplicationListener<ApplicationStartedEvent> {

    companion object : KLogging()

    /**
     * initializes application
     */
    @PostConstruct
    fun init() {

        // 데이터베이스 초기화
        this.initDatabase()
    }


    /**
     * After application starting
     */
    @Override
    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        auditLog(SystemMemberId, AuditCategory.APPLICATION_STARTED.value)
        this.loadMembers()
        this.loadAssets()
    }


    /**
     * Before application stopping
     */
    @PreDestroy
    fun destroy() {
        auditLog(SystemMemberId, AuditCategory.APPLICATION_STOPPED.value)
    }


    /**
     * HikariCP configuration
     */
    fun getHikariConfig(): HikariConfig {
        val prop = Properties()
        prop.setProperty("driverClassName", appConfig.dataSource.driverClassName)
        prop.setProperty("jdbcUrl", appConfig.dataSource.jdbcUrl)
        prop.setProperty("dataSource.user", appConfig.dataSource.user)
        prop.setProperty("dataSource.password", appConfig.dataSource.password)
        prop.setProperty("dataSource.portNumber", appConfig.dataSource.portNumber)
        prop.setProperty("dataSource.serverName", appConfig.dataSource.serverName)

        val config = HikariConfig(prop)
        config.minimumIdle = appConfig.dataSource.minimumIdle
        config.maximumPoolSize = appConfig.dataSource.maximumPoolSize

        return config
    }


    /**
     * 데이터베이스 초기화
     */
    fun initDatabase() {
        val config = this.getHikariConfig()
        Database.connect(HikariDataSource(config))

        val dbtz = URLEncodedUtils.parse(config.jdbcUrl, Charset.defaultCharset())
                .find { "serverimezone".equals(it.name) }
        DBTZ = if (dbtz == null) {
            DateTimeZone.getDefault()
        } else {
            DateTimeZone.forTimeZone(TimeZone.getTimeZone(dbtz.value))
        }
    }


    /**
     * 사용자 로딩
     */
    fun loadMembers() = transaction {
        var count = 0
        Members.selectAll().forEach {
            memberCacheRepo.save(toMemberMinDto(it))
            logger.debug("{}", toMemberMinDto(it))
            count++
        }
        logger.debug("Loaded {} member(s)", count)
    }


    /**
     * 조직구성 로딩
     */
    fun loadAssets() = transaction {
        Assets.selectAll().forEach{
            assetCacheRepo.save(toAssetDto(it))
        }
    }
}
