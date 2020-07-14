package com.devplayg.hippo.entity

import com.google.gson.Gson
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.data.annotation.Id


// Table
object Members : LongIdTable("mbr_member", "member_id") {
    //    val memberId = integer("member_Id")
    val username = varchar("username", 32)
    val name = varchar("name", 64)
    val email = varchar("email", 128)
    val password = varchar("password", 72)
    val roles = integer("roles")
    val timezone = varchar("timezone", 64)
    val failedLoginCount = integer("failed_login_count").default(0)

    override val primaryKey = PrimaryKey(id)
}

// Entity
class Member(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Member>(Members)

    //    var memberId by Members.memberId
    var username by Members.username
    var name by Members.name
    var password by Members.password
    var email by Members.email
    var roles by Members.roles
    var timezone = Members.timezone
    var failedLoginCount = Members.failedLoginCount

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
}

data class MemberDto(
        @Id
        var id: Long,
        var username: String,
        var name: String,
        var email: String,
        var roles: Int,
        var timezone: String,
        var password: String,
        val failedLoginCount: Int
)

fun MemberDto.toJson() = Gson().toJson(this)

fun toMemberDto(it: ResultRow) = MemberDto(
        id = it[Members.id].value,
        username = it[Members.username],
        name = it[Members.name],
        email = it[Members.email],
        roles = it[Members.roles],
        timezone = it[Members.timezone].toString(),
        password = it[Members.password],
        failedLoginCount = it[Members.failedLoginCount]
)

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
//
//data class MemberPublicDto(
//        @Id
//        var memberId: Long,
//        var username: String,
//        var name: String,
//        var email: String,
//        var roles: Int,
//        var timezone: String,
//        var failedLoginCount: Int
//)
//fun toMemberPublicDtoSecured(it: ResultRow) = MemberPublicDto(
//        memberId = it[Members.memberId].toLong(),
//        username = it[Members.username],
//        name = it[Members.name],
//        email = it[Members.email],
//        roles = it[Members.roles],
//        timezone = it[Members.timezone].toString(),
//        failedLoginCount = it[Members.failedLoginCount]
//)

// -----------------------------------------------------------
//abstract class Member {
//    abstract var memberId: Long
//    abstract var username: String
//    abstract var name: String
//    abstract var email: String
//    abstract var roles: Int
//    abstract var timezone: String
//}
//
//data class MemberDto (
//        @Id
//        override var memberId: Long,
//        override var username: String,
//        override var name: String,
//        override var email: String,
//        override var roles: Int,
//        override var timezone: String,
//        var password: String
//) : Member()
//fun MemberDto.toJson() = Gson().toJson(this)
//fun toMemberDto(it: ResultRow) = MemberDto(
//        memberId = it[Members.memberId].toLong(),
//        username = it[Members.username],
//        name = it[Members.name],
//        email = it[Members.email],
//        password = it[Members.password],
//        roles = it[Members.roles],
//        timezone = it[Members.timezone].toString()
//)
// -----------------------------------------------------------
//data class MemberDto (
//        @Id
//        override var memberId: Long,
//        override var username: String,
//        override var name: String,
//        override var email: String,
//        override var roles: Int,
//        override var timezone: String
//) : Member()
//
//fun toMemberDto(it: ResultRow) = MemberDto(
//        memberId = it[Members.memberId].toLong(),
//        username = it[Members.username],
//        name = it[Members.name],
//        email = it[Members.email],
//        roles = it[Members.roles],
//        timezone = it[Members.timezone].toString()
//)
