package com.devplayg.hippo.entity

import com.google.gson.Gson
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime


/**
 * Table: mbr_member
 * Description: 사용자 관리
 */
object Members : LongIdTable("mbr_member", "member_id"), MapperTable {
    val username = varchar("username", 32).uniqueIndex()
    val name = varchar("name", 64)
    val email = varchar("email", 128)
    val password = varchar("password", 72)
    val roles = integer("roles")
    val timezone = varchar("timezone", 64)
    val failedLoginCount = integer("failed_login_count").default(0)
    val loginCount = integer("login_count").default(0)
    val assetId = long("asset_id").default(0)
    val created = datetime("created").default(DateTime.now())
    val lastSuccessLogin = datetime("last_success_login")
    val lastFailedLogin = datetime("last_failed_login")

    override val primaryKey = PrimaryKey(id, name = "PK_mbrMember_memberId")

    override fun mapper() = hashMapOf(
            "assetId" to "asset_id"
    )
}

data class MemberDto(
        var id: Long?,
        val assetId: Long = 0,
        var username: String,
        var name: String,
        var email: String,
        var roles: Int,
        var timezone: String,
        var password: String?,
        var failedLoginCount: Int?,
        var accessibleIpListText: String = "0.0.0.0/0",
        var accessibleIpList: MutableList<String>?,
        var active: Boolean = false,
        var sessionCount: Int = 0,
        val created: String? = "",
        val lastSuccessLogin: String? = "",
        val lastFailedLogin: String? = ""

)

fun MemberDto.json(): String = Gson().toJson(this)
fun MemberDto.minimize() = MemberMinDto(
        id = id!!,
        assetId = assetId,
        username = username,
        name = name,
        email = email,
        roles = roles,
        timezone = timezone,
        active = active
)

data class MemberMinDto(
        val id: Long,
        val assetId: Long,
        val username: String,
        val name: String,
        val email: String,
        val roles: Int,
        val timezone: String,
        val active: Boolean
)

fun MemberMinDto.json() = Gson().toJson(this)


fun toMemberDto(it: ResultRow, pwVisible: Boolean = false): MemberDto {
    return MemberDto(
            id = it[Members.id].value,
            assetId = it[Members.assetId],
            username = it[Members.username],
            name = it[Members.name],
            email = it[Members.email],
            roles = it[Members.roles],
            timezone = it[Members.timezone].toString(),
            password = if (pwVisible) it[Members.password] else "",
            failedLoginCount = it[Members.failedLoginCount],
            accessibleIpListText = "",
            accessibleIpList = mutableListOf(),
            active = false,
            created = it[Members.created].toString(),
            lastSuccessLogin = it[Members.lastSuccessLogin].toString(),
            lastFailedLogin = it[Members.lastFailedLogin].toString()
    )
}

fun toMemberMinDto(it: ResultRow): MemberMinDto {
    return MemberMinDto(
            id = it[Members.id].value,
            assetId = it[Members.assetId],
            username = it[Members.username],
            name = it[Members.name],
            email = it[Members.email],
            roles = it[Members.roles],
            timezone = it[Members.timezone],
            active = false
    )
}

/*
 * Table: mbr_allowed_ip
 * Description: 사용자 접속허용 IP
 */
object MemberAllowedIpList : IntIdTable("mbr_allowed_ip", "network_id") {
    val memberId = long("member_id")
    val ipCidr = varchar("ip_cidr", 19)
    val created = datetime("created").default(DateTime.now())

    override val primaryKey = PrimaryKey(id)
}
