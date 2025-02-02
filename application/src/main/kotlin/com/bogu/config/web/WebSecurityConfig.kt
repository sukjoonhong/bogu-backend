package com.bogu.config.web

import com.bogu.config.app.ApiAuthenticationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val apiAuthenticationProperties: ApiAuthenticationProperties,
) {
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer = WebSecurityCustomizer {
        it.ignoring().requestMatchers("/actuator/**")
        it.ignoring().requestMatchers("/")
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .authorizeHttpRequests { it.anyRequest().permitAll() }
//        .httpBasic(Customizer.withDefaults())
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