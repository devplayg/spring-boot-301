package com.devplayg.hippo.service

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.define.*
import com.devplayg.hippo.entity.*
import com.devplayg.hippo.framework.CustomUserDetails
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.repository.MemberRepo
import com.devplayg.hippo.util.SubnetUtils
import com.devplayg.hippo.util.currentUsername
import mu.KLogging
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import org.joda.time.Days
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * Member service (UserDetailsService)
 */
@Service
class MemberService(
        private val appConfig: AppConfig,
        private val auditService: AuditService,
        private val memberRepo: MemberRepo,
        private val memberCacheRepo: MemberCacheRepo
) : UserDetailsService {
    
    companion object : KLogging()

    /**
     * 전체 조회
     */
    fun findAll() = memberRepo.findAll()


    /**
     * 선택 조회
     */
    fun findById(id: Long) = memberRepo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    /**
     * 선택 조회
     */
    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepo.findByUsername(username)?.let { CustomUserDetails.from(toMemberDto(it, true)) }
                ?: throw UsernameNotFoundException("User $username not found")
    }


    /**
     * 사용자 아이디로 사용자 조회
     */
    fun findByUsername(username: String): MemberDto? {
        val row = memberRepo.findByUsername(username) ?: return null
        return toMemberDto(row)
    }


    /**
     * 사용자 아이디 존재여부 확인
     */
    fun usernameExists(username: String) = (memberRepo.findByUsername(username) != null)


    /**
     * 사용자 등록
     */
    fun create(member: MemberDto) {
        member.password = "YOUR_PPP" // 임의의 비밀번호 자동 등록
        val ipList = member.accessibleIpListText.trim().split("\\s+|,+".toRegex())
        val networks = ipList.mapNotNull {
            if (it.isBlank()) {
                return@mapNotNull null
            }
            val ipCidr = if (it.contains("/")) {
                it
            } else {
                "$it/32"
            }

            SubnetUtils(ipCidr)
            ipCidr
        }
        member.accessibleIpList = networks.toMutableList()
        member.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(member.password)
        memberRepo.create(member)
        logger.debug("{}", member)
    }


    /**
     * 로그인 실패 수 증가
     */
    fun denyLogin(username: String) = memberRepo.denyLogin(username)


    /**
     * 로그인 허용대 대한 처리
     */
    fun allowLogin(username: String, sessionId: String) = memberRepo.allowLogin(username)


    /**
     * 사용자 정보 변경
     */
    fun update(memberDto: MemberDto) {
        // 사용자 기존 정보 조회
        val rowOld = memberRepo.findByUsername(memberDto.username) ?: return
        val memberDtoOld = toMemberDto(rowOld)

        // 사용자 DB 정보 업데이트
        val isAdmin = memberDtoOld.roles and MemberRole.Admin.value
        memberDto.roles = (memberDto.roles and getDefensiveMemberRoles()) or isAdmin
        memberRepo.update(memberDto)

        // 변경된 사용자 다시 조회 (사용자 Role 변경이 조건대로 되지 않을 수 있기 때문에 재조회)
        // (ex: 본인보다 높은 계정 사용자 권한을 가진 사용자 정보를 변경 시도하는 경우)
        val rowNew = memberRepo.findByUsername(memberDto.username) ?: return
        val memberMinDtoNew = toMemberMinDto(rowNew)
        memberCacheRepo.save(memberMinDtoNew)

        // 권한변경 확인 및 감사로깅
        if (memberDtoOld.roles != memberMinDtoNew.roles) {
//            auditService.audit(
//                    AuditCategory.MemberRolesChanged.value,
//                    hashMapOf(
//                            Pair("username", memberMinDtoNew.username),
//                            Pair("oldRoles", memberDtoOld.roles),
//                            Pair("newRoles", memberMinDtoNew.roles)
//                    )
//            )
        }
    }

    /**
     * dn 파싱; ldapUserDetail.dn
     *      "cn=YOUR_CN,ou=ORG_UNIT,ou=Group,ou=YOUR_TEAM,dc=AAA,dc=BBBB"
     */
    fun parseMeta(meta: String): MutableMap<String, String> {
        val m: MutableMap<String, String> = mutableMapOf()

        meta.split(",").forEach { it: String ->
            val (key, value) = it.split("=", limit = 2)
            if (m.containsKey(key)) {
                m[key] = m[key] + " " + value
                return@forEach
            }
            m[key] = value
            return@forEach
        }
        return m
    }


    /**
     * LDAP/DB 사용자 동기화
     */
    fun createIfNotExist(ldapUserDetail: LdapUserDetailsImpl): Pair<MemberDto, Int> {
        val username = ldapUserDetail.username

        // DB 사용자 정보 조회
        var memberDto = findByUsername(username)
        if (memberDto != null) { // 이미 존재하는 사용자면
            return Pair(memberDto, 0)
        }

        /**
         * 신규 사용자 등록
         */
        // DB에 사용자 정보가 없으면, ldap 메타정보를 DB에 입력
        val memberMeta = parseMeta(ldapUserDetail.dn)
        // 기본 권한
        val defaultRoles = 0 // MemberRole.Sheriff.value or MemberRole.Admin.value

//        // wondory: 사용자 권한을 DB와 연동해야함
//        // DB에 사용자 정보 입력
//        memberDto = MemberDto(
//                0,
//                0,
//                ldapUserDetail.username,
//                memberMeta["cn"] ?: "Unknown",
//                true,
//                ldapUserDetail.username + "@" + appConfig.emailDomain,
//                defaultRoles,
//                "",
//                "Asia/Seoul",
//                "",
//                0,
//                "",
//                mutableListOf("0.0.0.0/0"),
//                false
//        )
        val result = memberRepo.createIfNotExist(memberDto!!)
        return memberDto
    }

    /**
     * LDAP 사용자 로그인 처리
     */
    fun handleLdapLogin(auth: Authentication, sessionId: String) {
        //memberCacheRepo.updateTwoFactorAuth(auth.name, false) // 2FA 인증상태를 "미인증"으로 설정
        // memberCacheRepo.mark2FA(sessionId)
    }


    /**
     * DB 사용자 로그인 처리
     */
    fun handleLocalLogin(auth: Authentication) {
        val member = auth.principal as CustomUserDetails

        auditService._audit(member.username, AuditCategory.SignIn.value, loginInfo(member.username, member.name, member.id, "local"))
    }


    fun loginInfo(username: String, name: String, id: Long, mode: String) = hashMapOf(
            Pair("username", username),
            Pair("name", name),
            Pair("id", id),
            Pair("mode", mode)
    )


    /**
     * asset으로 사용자 조회
     */
    fun findByAssetId(assetId: Long) = memberRepo.findByAssetId(assetId)

    fun findByAssetIds(vararg assetIdList: Long) = memberRepo.findByAssetIds(*assetIdList)

    fun findOnlineUsersByAssetId(vararg assetIdList: Long): List<MemberMinDto> {
        val m = memberCacheRepo.findOnlineUsernameMap()
        return this.findByAssetIds(*assetIdList).map {
            if (m.contains(it.username)) {
                it.active = true
            }
            it.minimize()
        }.filter{
            it.id > 1
        }
    }


    /**
     * 사용자 삭제 (추후 필요 시 사용)
     */
    fun deleteById(id: Long): Response {
        val memberMinDto = memberRepo.findById(id) ?: return Response.NotFound()
        if (memberMinDto.username == currentUsername()) {
            throw Exception("cannot terminate your own session")
        }

        // DB에서 사용자 정보 삭제
        val affectedRows = memberRepo.deleteById(id)
        return Response.Updated(affectedRows)
    }


    /**
     * DB에 기록된 사용자 권한을 Cache로 Overwrite (LDAP인증 상황에서 사용)
     */
    fun syncMember(sessionId: String) {
        // Security context 에 있는 인증정보 조회
        val context = SecurityContextHolder.getContext() ?: return
        val auth: Authentication = context.authentication
        val member = auth.principal as LdapUserDetailsImpl
        if (member.dn == null) return
        val (memberDto, inserted) = this.createIfNotExist(member) // DB에 사용자가 없으면 등록

        // 사용자 정보를 cache에 저장
        val memberMinDto = memberDto.minimize()
        memberCacheRepo.mark2FA(sessionId) // 2FA인증 표시
        memberCacheRepo.save(memberMinDto) // Cache에 사용자 저장

        // Audit
        if (inserted > 0) { // New member?
             auditService._audit(member.username, AuditCategory.MemberRegistered.value, this.loginInfo(member.username, memberMinDto.name, memberDto.id!!, "ldap"))
        }
        auditService._audit(member.username, AuditCategory.SignIn.value, this.loginInfo(memberDto.username, memberMinDto.name, memberDto.id!!, "ldap"))

        // SecurityContext 업데이트
        val authentication = NewUsernamePasswordAuthenticationToken(auth, memberDto.roles) // 2FA 인증을 위해, 권한 제한
        SecurityContextHolder.getContext().authentication = authentication
    }


    /**
     * 로그인한지 30일 지난 사용자들 권한 회수
     */
    fun revokeInactiveUsersRole() {
        val now = DateTime.now()
        this.findAll().forEach {
            val logTime: DateTime = RFC3339PatternWithMilli.parseDateTime(it.lastSuccessLogin) // 마지막 로그인 시간
            val diff = Days.daysBetween(logTime, now).days
//            logger.debug("username={}, lastLogin={}, diff={}", it.username, it.lastSuccessLogin, diff)
            var days = appConfig.revokeInactiveUsersRoleAfterDays
            if (days < 30) { // just-in-case 날짜 설정 오류이면 정정(최소 30일)
                days = 30
            }
            if (diff > days && it.roles > 0) {
                val affectedRows = memberRepo.revokeRole(it.id!!)
                if (affectedRows > 0) {
                    logger.info("User roles has been revoked; username={}, lastSuccessLogin={}", it.username, it.lastSuccessLogin)
                    auditService.auditBySystem(AuditCategory.UserRolesRevoked.value, hashMapOf(
                            Pair("username", it.username),
                            Pair("name", it.name),
                            Pair("roles", it.roles),
                            Pair("lastSuccessLogin", it.lastSuccessLogin),
                            Pair("criteriaDays", days)
                    ))
                }
            }
        }
    }
        
        


}
