package com.bogu.config.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "api.basic.authentication")
class ApiAuthenticationProperties {
    lateinit var username: String
    lateinit var password: String
}