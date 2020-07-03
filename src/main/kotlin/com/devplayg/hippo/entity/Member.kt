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
    val email = varchar("email", 128)
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
//data class MemberDto(
//        @Id
//        var memberId: Long,
//        var username: String,
//        var name: String,
//        var email: String,
//        var password: String,
//        var roles: Int,
//        var timezone: String
//)
//fun MemberDto.toJson() = Gson().toJson(this)

//fun MemberDto.getAuthorities(): User {
//    return User(
//            this.username, this.password,
//            this.roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
//             this.roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
//    )
//}

//fun toMemberDto(it: ResultRow) = MemberDto(
//        memberId = it[Members.memberId].toLong(),
//        username = it[Members.username],
//        name = it[Members.name],
//        email = it[Members.email],
//        password = it[Members.password],
//        roles = it[Members.roles],
//        timezone = it[Members.timezone].toString()
//)

//data class MemberDto(
//        @Id
//        var memberId: Long,
//        var username: String,
//        var name: String,
//        var email: String,
//        var password: String,
//        var roles: Int,
//        var timezone: String
//)
//fun toMemberDtoSecured(it: ResultRow) = MemberDto(
//        memberId = it[Members.memberId].toLong(),
//        username = it[Members.username],
//        name = it[Members.name],
//        email = it[Members.email],
//        roles = it[Members.roles],
//        timezone = it[Members.timezone].toString()
//)


abstract class Member {
    abstract var memberId: Long
    abstract var username: String
    abstract var name: String
    abstract var email: String
    abstract var roles: Int
    abstract var timezone: String
}

data class MemberDtoSecured (
        @Id
        override var memberId: Long,
        override var username: String,
        override var name: String,
        override var email: String,
        override var roles: Int,
        override var timezone: String,
        var password: String
) : Member()
fun MemberDtoSecured.toJson() = Gson().toJson(this)
fun toMemberSecuredDto(it: ResultRow) = MemberDtoSecured(
        memberId = it[Members.memberId].toLong(),
        username = it[Members.username],
        name = it[Members.name],
        email = it[Members.email],
        password = it[Members.password],
        roles = it[Members.roles],
        timezone = it[Members.timezone].toString()
)

data class MemberDto (
        @Id
        override var memberId: Long,
        override var username: String,
        override var name: String,
        override var email: String,
        override var roles: Int,
        override var timezone: String
) : Member()

fun toMemberDto(it: ResultRow) = MemberDto(
        memberId = it[Members.memberId].toLong(),
        username = it[Members.username],
        name = it[Members.name],
        email = it[Members.email],
        roles = it[Members.roles],
        timezone = it[Members.timezone].toString()
)
