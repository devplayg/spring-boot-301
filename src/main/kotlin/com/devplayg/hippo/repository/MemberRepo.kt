package com.devplayg.hippo.repository


import com.devplayg.hippo.entity.MemberDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository


@Repository
class MemberRepo


// Cache
@Repository
class MemberCacheRepo(
//        private val redisRepo: RedisRepo,
        val redisTemplate: RedisTemplate<String, String>

) {
//    @Autowired
//    var redisTemplate: redisTem
//    redis

    //    fun save(memberDto: MemberDto) = redisRepo.save(memberDto)
    fun save(memberDto: MemberDto) {
        redisTemplate.opsForValue().set("wonkey", "sukval")

//        var vop: ValueOperations<*, *> = redisTemplate.opsForValue()
//        vop.set()
//        vop.set("jdkSerial", "jdk")
//        vop.set("jdkSerial", "jdk");
//        redisTemplate.opsForList().leftPush(1 as Nothing, 3 as Nothing);
//        redisTemplate.execute { connection ->
//            val size = connection.dbSize()
//            (connection as StringRedisConnection)["key"] = "value"
//        }

    }
//    fun save2(memberDto: MemberDto) = {

//    }
//    fun findByUsername(username: String)
}
//
//@GetMapping("/keys")
//public String keys() {
//    Set<byte[]> keys = redisTemplate.keys("*");
//    return "keys";
//}
