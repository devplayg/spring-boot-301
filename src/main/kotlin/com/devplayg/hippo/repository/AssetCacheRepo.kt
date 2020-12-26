package com.devplayg.hippo.repository

import com.devplayg.hippo.define.CacheAssetPrefix
import com.devplayg.hippo.entity.AssetDto
import com.devplayg.hippo.entity.json
import mu.KLogging
import org.jetbrains.kotlin.com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

/**
 * 논리자산 Cache 관리
 */
@Repository
class AssetCacheRepo(
        private val redisTemplate: RedisTemplate<String, Any>
) {
    companion object : KLogging()

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

}
