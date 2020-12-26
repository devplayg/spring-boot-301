package com.devplayg.hippo.repository


import com.devplayg.hippo.define.CacheMemberPrefix
import com.devplayg.hippo.entity.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository


@Repository
class MemberRepo {
    // devplayg
    /**
     * Username으로 사용자 조회
     */
    fun findByUsername(username: String) = transaction {
        try {
            Members.select { Members.username eq username }.single()
        } catch (e: NoSuchElementException) {
            null
        }
    }


    /**
     * ID로 사용자 조회
     */
    fun findById(id: Long) = transaction {
        Members.select { Members.id.eq(id) }.map {
            toMemberDto(it)
        }.firstOrNull()
    }


    /**
     * 모든 사용자 조회
     */
    fun findAll(): List<MemberDto> {
        val memberTable = HashMap<Long, MemberDto>()
        transaction {
            Members.selectAll().forEach {
                memberTable.put(it[Members.id].value, toMemberDto(it))
            }

            MemberAllowedIpList.selectAll().forEach() {
                if (it[MemberAllowedIpList.memberId] in memberTable) {
                    val m = memberTable.get(it[MemberAllowedIpList.memberId])
                    m!!.accessibleIpList!!.add(it[MemberAllowedIpList.ipCidr])
                }
            }
        }
        return memberTable.values.toList()
    }


    /**
     * 사용자 생성
     */
    fun createIfNotExist(memberDto: MemberDto): InsertionResult {
        var result = InsertionResult(0, 0)
        transaction {
            // 사용자 존재유무 확인
            try {
                val row = Members.select { Members.username eq memberDto.username }.single()
                val m = toMemberDto(row)
                memberDto.id = m.id
            } catch (e: NoSuchElementException) { // 사용자가 없으면
                val lastInsertId = Members.insert {
                    it[username] = memberDto.username
                    it[name] = memberDto.name
                    it[email] = memberDto.email
                    it[password] = memberDto.password ?: ""
                    it[roles] = memberDto.roles
                    it[timezone] = memberDto.timezone
                } get Members.id

                for (net in memberDto.accessibleIpList!!) {
                    MemberAllowedIpList.insert {
                        it[memberId] = lastInsertId.value
                        it[ipCidr] = net
                    }
                }

                memberDto.id = lastInsertId.value
                result.affectedRows = 1
                result.lastInsertId = lastInsertId.value
            }
        }
        return result
    }


    /**
     * 사용자 생성
     */
    fun create(member: MemberDto) = transaction {
        val lastInsertId = Members.insert {
            it[username] = member.username
            it[name] = member.name
            it[email] = member.email
            it[password] = member.password ?: ""
            it[roles] = member.roles
            it[timezone] = member.timezone
            it[assetId] = member.assetId
            it[failedLoginCount] = failedLoginCount
        } get Members.id


        for (net in member.accessibleIpList!!) {
            MemberAllowedIpList.insert {
                it[memberId] = lastInsertId.value
                it[ipCidr] = net
            }
        }

        member.id = lastInsertId.value
        return Response.Created(lastInsertId.value)
    }


    /**
     * 업데이트
     */
    fun update(member: MemberDto) = transaction {
        // 사용자 권한정보 외 업데이트
        Members.update({ Members.username eq member.username }) {
            it[timezone] = member.timezone
            it[name] = member.name
            it[assetId] = member.assetId
        }

        val (currentUsername, currentUserRoles) = currentUsernameAndRoles()

        // 권한 정보 업데이트 조건
        // - 본인의 권한은 업데이트 하지 못함
        // - 상위 권한자가 업데이트 할 수 있도록 (개선이 필요할 수 있음)
        Members.update({
            Members.username.eq(member.username) and Members.username.neq(currentUsername)
        }) {
            it[roles] = member.roles // Admin 권한은 설정 할 수 없도록, service 로직에서 제거
        }
    }


    /**
     * 로그인 거부
     */
    fun denyLogin(username: String) = transaction {
        val affectedRows = Members.update({ Members.username eq username }) {
            it[lastFailedLogin] = DateTime.now()
            with(SqlExpressionBuilder) {
                it.update(Members.failedLoginCount, Members.failedLoginCount + 1)
            }
        }
        UpdateResult(affectedRows)
    }


    /**
     * 로그인 허용
     */
    fun allowLogin(username: String) = transaction {
        val affectedRows = Members.update({ Members.username eq username }) {
            it[lastSuccessLogin] = DateTime.now()
            it[failedLoginCount] = 0 // 로그인 실패 초기화
            with(SqlExpressionBuilder) {
                it.update(Members.loginCount, Members.loginCount + 1)
            }
        }
        UpdateResult(affectedRows)
    }

    /**
     * Asset ID로 사용자 조회
     */
    fun findByAssetId(assetId: Long) = transaction {
        Members.select { Members.assetId.eq(assetId) }.map {
            toMemberDto(it)
        }
    }


    /**
     * 멀티 Asset ID로 사용자 조회
     */
    fun findByAssetIds(vararg assetIdList: Long) = transaction {
        Members.select { Members.assetId.inList(assetIdList.toList()) }.map {
            toMemberDto(it)
        }
    }


    /**
     * 사용자 삭제
     */
    fun deleteById(id: Long) = transaction {
        val affectedRows = Members.deleteWhere { Members.id.eq(id) }
        UpdateResult(affectedRows)
    }


    /**
     * 사용자 권한 회수
     */
    fun revokeRole(id: Long) = transaction {
        val affectedRows = Members.update({ Members.id.eq(id) }) {
            it[roles] = 0
        }
        UpdateResult(affectedRows)
    }

}

