/**
 * 논리자산 Cache 관리
 */
@Repository
class AssetCacheRepo(
        private val redisTemplate: RedisTemplate<String, Any>
) {
    /**
     * 저장
     */
    fun save(assetDto: AssetDto) = redisTemplate.opsForValue().set(
            CacheAssetPrefix+assetDto.id, assetDto.json()
    )

    /**
     * 조회
     */
    fun findAll() = redisTemplate.keys("$CacheAssetPrefix*").map {
        val json = redisTemplate.opsForValue().get(it)?:return@map null
        Gson().fromJson(json.toString(), AssetDto::class.java)
    }.filterNotNull()


    /**
     * 선택 조회
     */
    fun findById(id: Long): AssetDto? {
        val json = redisTemplate.opsForValue().get(CacheAssetPrefix + id) ?: return null
        return Gson().fromJson(json.toString(), AssetDto::class.java)
    }


    /**
     * 삭제
     */
    fun deleteById(id: Long) = redisTemplate.delete(CacheAssetPrefix + id)
    
    
    //
        /**
     * 전체 조회
     */
    fun findAll(filter: MyDataFilter) = transaction {
        predicate()
                .orderBy(getSortOrder(MyData, filter))
                .map {
                    toMyData(it)
                }
    }


    /**
     * 선택 조회
     */
    @Throws(Exception::class)
    fun findById(id: Int) = transaction {
        predicate()
                .andWhere { MyData.id eq id }
                .map { toMyData(it) } // returns a standard Kotlin List
                .singleOrNull()
    }


    /**
     * 등록
     */
    @Throws(Exception::class)
    fun create(filter: MyDataFilter, _memberId: Long) = transaction {
        val now = DateTime.now()
        MyData.insert {
            it[updated] = now
        } get MyData.id
    }


    /**
     * 변경
     */
    @Throws(Exception::class)
    fun update(id: Int, filter: MyDataFilter, _memberId: Long) = transaction {
        val _old = findById(id) ?: return@transaction 0
        val affectedRows = MyData.update({ MyData.id.eq(id) }) {
        }

        val _new = findById(id)
        if (_new!!.changed(_old)) { // 정책이 변경여부
        }

        affectedRows
    }


    /**
     * 변경
     */
    @Throws(Exception::class)
    fun delete(id: Int, _memberId: Long) = transaction {
        MyData.deleteWhere { MyData.id.eq(id) }
    }


}
