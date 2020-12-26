package com.devplayg.hippo.service

import com.devplayg.hippo.filter.AuditFilter
import com.devplayg.hippo.entity.toAuditDto
import com.devplayg.hippo.repository.AuditRepo
import com.devplayg.hippo.util.PageData
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuditService(
        private val auditRepo: AuditRepo,
        private val memberCacheRepo: MemberCacheRepo
) {
    /**
     * 조회 (Fast paging)
     */
    fun find(filter: AuditFilter) = auditRepo.find(filter).map{
        it.member = memberCacheRepo.findById(it.memberId)
        return@map it
    }


    /**
     * 조회 (Normal paging)
     */
    fun findWithTotal(filter: AuditFilter) = auditRepo.findWithTotal(filter)


    /**
     * 보안감사 기록
     */
    fun audit(category: Int, message: Any? = null) {
        _audit(currentUsername(), category, message)
    }
    fun _audit(username: String, category: Int, message: Any? = null) {
        // wondory: 캐시가 사라졌을 경우, 추후 대비 필요
        val memberDto = memberCacheRepo.findByUsername(username)
        auditLog(memberDto?.id ?: 0, category, message)
    }

    fun auditBySystem(category: Int, message: Any? = null) {
        auditLog(SystemMemberId, category, message)
    }
}
