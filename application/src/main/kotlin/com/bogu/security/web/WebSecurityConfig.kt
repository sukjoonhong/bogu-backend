package com.bogu.security.web

import com.bogu.config.app.ApiAuthenticationProperties
import com.bogu.security.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val apiAuthenticationProperties: ApiAuthenticationProperties,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer = WebSecurityCustomizer {
        it.ignoring().requestMatchers("/actuator/**")
        it.ignoring().requestMatchers("/")
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it.requestMatchers(
                "/auth/public-key",
                "/auth/login",
                "/auth/refresh",
                "/ws/chat/**"
            ).permitAll()
            it.anyRequest().authenticated()
        }
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(apiAuthenticationProperties.username)
            .password("{noop}${apiAuthenticationProperties.password}")
            .roles("ADMIN")
    }
}