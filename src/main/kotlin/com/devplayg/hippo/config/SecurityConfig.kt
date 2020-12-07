package com.devplayg.hippo.config

import com.devplayg.hippo.define.MemberRole
import com.devplayg.hippo.framework.CustomAuthenticationFailureHandler
import com.devplayg.hippo.framework.CustomAuthenticationSuccessHandler
import com.devplayg.hippo.service.MemberService
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.filter.CharacterEncodingFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val memberService: MemberService,
        private val appConfig: AppConfig
) : WebSecurityConfigurerAdapter() {
    companion object : KLogging()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests() // API for Administrators
                .antMatchers(*appConfig.pathPatternsNotToBeIntercepted.toTypedArray())
                    .permitAll() // Others need to be authenticated

                .antMatchers("/audits/**", "/members/**")
                    .hasAnyRole(MemberRole.Admin.name, MemberRole.Sheriff.name)

                .anyRequest()
                    .authenticated()
                    .and()

                // Login
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/app-login")
                    .usernameParameter("app_username")
                    .passwordParameter("app_password")
                    .successHandler(CustomAuthenticationSuccessHandler(appConfig.homeUri)) // failure
                    .failureHandler(CustomAuthenticationFailureHandler(memberService)) // success
                    .permitAll()
                    .and()
                .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .permitAll()
                    .and()
                .addFilterAt(CharacterEncodingFilter(), CsrfFilter::class.java)

//                .antMatchers("/cameras/{\\d+}/policy", "/factoryevent/{\\d+}/toggleEventType")
//                .hasAnyRole(MemberRole.Admin.name) // Request create the period report

//                .antMatchers(HttpMethod.POST, "/periodreport")
//                .hasAnyRole(MemberRole.Admin.name) // White APIs
 var maxSessionCount = appConfig.maxUserSessions
        if (maxSessionCount < 1) {
            maxSessionCount = 1
        }
        http.sessionManagement().maximumSessions(maxSessionCount).sessionRegistry(sessionRegistry())
    }

    fun allowUrlEncodedSlashHttpFirewall(): HttpFirewall {
        val firewall = DefaultHttpFirewall()
        firewall.setAllowUrlEncodedSlash(true)
        return firewall
    }
 /*       
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Throws(java.lang.Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder())
    }
   */
 override fun configure(auth: AuthenticationManagerBuilder) {
        
        if (appConfig.ldap != null) {
            auth
                    .ldapAuthentication()
                    .groupSearchBase(appConfig.ldap.groupSearchBase)
                    .userSearchFilter(appConfig.ldap.userSearchFilter)
                    .contextSource()
                    .url("ldap://" + appConfig.ldap.url + ":" + appConfig.ldap.port + "/" + appConfig.ldap.userDn)
                    .managerDn(appConfig.ldap.managerDn)
                    .managerPassword(appConfig.ldap.managerPassword)
            return
        }

       
        auth.userDetailsService(memberService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
    }
        @Bean
    fun sessionRegistry() = SessionRegistryImpl()

    @Bean
    fun httpSessionEventPublisher() = ServletListenerRegistrationBean(HttpSessionEventPublisher())
}
