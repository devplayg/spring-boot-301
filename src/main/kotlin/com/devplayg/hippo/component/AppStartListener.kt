
/**
 * Application listener
 */
@Component
class AppStartedListener(
        private val appConfig: AppConfig,
        private val memberCacheRepo: MemberCacheRepo,
        private val assetCacheRepo: AssetCacheRepo
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
        auditLog(SystemMemberId, AuditCategory.ApplicationStarted.value)
        this.loadMembers()
        this.loadAssets()
    }


    /**
     * Before application stopping
     */
    @PreDestroy
    fun destroy() {
        auditLog(SystemMemberId, AuditCategory.ApplicationStopped.value)
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
