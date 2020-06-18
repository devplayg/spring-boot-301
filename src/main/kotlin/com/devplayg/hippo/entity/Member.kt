package com.devplayg.hippo.entity

import com.devplayg.hippo.define.MemberRole
import com.google.gson.Gson
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.data.annotation.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


// Table
object Members : IntIdTable("mbr_member", "member_id") {
    val memberId = long("member_Id")
    val username = varchar("username", 32)
    val name = varchar("name", 64)
    val password = varchar("password", 72)
    val roles = integer("roles")
    val timezone = varchar("timezone", 64)

    override val primaryKey = PrimaryKey(id, name = "PK_mbr_member_memberId ")
}

//// Entity
//class Member(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<Member>(Members)
//
//    var memberId by Members.memberId
//    var username by Members.username
//    var name by Members.name
//    var password by Members.password
//    var roles by Members.roles
//    var timezone = Members.timezone
//
//    fun toDto() = MemberDto(
//            memberId = this.id.value.toLong(),
////            username = this.username,
//            name = this.name,
////            password = this.password,
//
//            roles = arrayListOf(MemberRole.Admin),
//            timezone = this.timezone.toString()
//    )
//
//    fun toJson() = this.toDto().toJson()
//}
//



// DTO
//@RedisHash("member")
data class MemberDto(
        @Id
        var memberId: Long,
        var username: String,
        var name: String,
        var password: String,
        var roles: Int,
        var timezone: String
)
fun MemberDto.toJson() = Gson().toJson(this)

//fun MemberDto.getAuthorities(): User {
//    return User(
//            this.username, this.password,
//            this.roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
//             this.roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
//    )
//}

fun toMemberDto(it: ResultRow) = MemberDto(
        memberId = it[Members.memberId].toLong(),
        username = it[Members.username],
        name = it[Members.name],
        password = it[Members.password],
        roles = it[Members.roles],
        timezone = it[Members.timezone].toString()
)

//fun MemberDto(memberId: Long, username: String, name: String, password: String, timezone: String): MemberDto {
//
//}


//data class AuditDto(
//        val id: Long,
//        val ip: Long,
//        val category: Int,
//        val message: String?,
//        val created: String
//)
//
//fun mapToAuditDto(it: ResultRow) = AuditDto(
//        id = it[Audits.id].value,
//        ip = it[Audits.ip],
//        category = it[Audits.category],
//        message = it[Audits.message],
//        created = it[Audits.created].toString()
//)
