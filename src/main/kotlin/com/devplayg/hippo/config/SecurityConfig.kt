package com.devplayg.hippo.config

import mu.KLogging
import org.springframework.context.annotation.Configuration


@Configuration
class SecurityConfig {
    companion object : KLogging()

//    @Bean
//    fun inMemoryMemberManager(): InMemoryMemberManager? {
//        val members: MutableList<*> = memberRepository.findAll()
//        val member = Member()
//        member.setId(InMemoryMemberManager.adminId)
//        member.setUsername(InMemoryMemberManager.adminUsername)
//        member.setEnabled(false)
//        member.setName(appConfig.getAdminInfo().getName())
//        member.setRoles(RoleType.Role.ADMIN.getValue())
//        member.setTimezone(TimeZone.getDefault().toZoneId().id)
//        member.setEmail(appConfig.getAdminInfo().getEmail())
//        member.setPassword("")
//        members.add(member)
//        return InMemoryMemberManager()
//    }


//    @Bean
//    fun inMemoryMemberManager() = MemberManager()

//    fun init() {
//        logger.info { "#########################################" }
//    }
}
