package com.bogu.bogubackend.config.app

import org.springframework.stereotype.Component

@Component
class ApiAuthenticationProperties {
    var username: String = "test"
    var password: String = "test"
}