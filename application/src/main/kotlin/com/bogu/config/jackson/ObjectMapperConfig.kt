package com.bogu.config.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ObjectMapperConfig {
    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .registerModule(ParameterNamesModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 모르는 프로퍼티는 무시
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true) // 모르는 enum 값은 null 로 설정

}