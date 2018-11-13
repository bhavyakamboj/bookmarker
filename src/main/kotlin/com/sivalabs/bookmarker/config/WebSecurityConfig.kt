package com.sivalabs.bookmarker.config

import com.sivalabs.bookmarker.security.CustomUserDetailsService
import com.sivalabs.bookmarker.security.TokenHelper
import com.sivalabs.bookmarker.security.auth.RestAuthenticationEntryPoint
import com.sivalabs.bookmarker.security.auth.TokenAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUserDetailsService: CustomUserDetailsService

    @Autowired
    private lateinit var restAuthenticationEntryPoint: RestAuthenticationEntryPoint

    @Autowired
    private lateinit var tokenHelper: TokenHelper

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            //.antMatchers(HttpMethod.POST,"/users").hasAnyRole("USER", "ADMIN")
            //.anyRequest().authenticated()
            .and()
            .addFilterBefore(TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService), BasicAuthenticationFilter::class.java)

        http.csrf().disable()
    }

    override fun configure(web: WebSecurity) {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring()
            .antMatchers(HttpMethod.POST, "/api/auth/login")
    }
}
