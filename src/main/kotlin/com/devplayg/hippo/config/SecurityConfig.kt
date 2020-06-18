package com.devplayg.hippo.config

import com.devplayg.hippo.define.MemberRole
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.filter.CharacterEncodingFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
//        private val appProperties: AppProperties
) : WebSecurityConfigurerAdapter() {
    companion object : KLogging()

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        val filter = CharacterEncodingFilter()
        httpSecurity
                .authorizeRequests() // API for Administrators

                .antMatchers("/audit/**", "/members/**", "/workschedule/**")
                .hasAnyRole(MemberRole.Admin.name, MemberRole.Sheriff.name)

                .antMatchers("/cameras/{\\d+}/policy", "/factoryevent/{\\d+}/toggleEventType")
                .hasAnyRole(MemberRole.Admin.name) // Request create the period report

//                .antMatchers(HttpMethod.POST, "/periodreport")
//                .hasAnyRole(MemberRole.Admin.name) // White APIs

                .antMatchers("/api/**", "/public/**")
                .permitAll() // Others need to be authenticated

                .anyRequest()
                .authenticated()
                .and() // Login

                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/app-login")
                .usernameParameter("app_username")

                .passwordParameter("app_password") // Logged in successfully
//                .successHandler(authenticationSuccessHandler()) // Login failed
//                .failureHandler(authenticationFailureHandler())
                .permitAll()
                .and() // Logout

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()

                .and()
                .addFilterAt(CharacterEncodingFilter(), CsrfFilter::class.java)
                .csrf().disable()
    }

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
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

//    @Throws(java.lang.Exception::class)
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        UserDetailsPasswordService
//        auth.userDetailsService(UserDetailsService { username -> // return SecurityConfig.this.readerRepository.findOne(username);
//            this@SecurityConfig.readerRepository.getOne(username)
//        }).passwordEncoder(this.noOpPasswordEncoder())
//    }

    @Throws(java.lang.Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(inMemoryMemberManager())
    }

//    @Throws(java.lang.Exception::class)
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth.userDetailsService(inMemoryMemberManager())
//    }

}
