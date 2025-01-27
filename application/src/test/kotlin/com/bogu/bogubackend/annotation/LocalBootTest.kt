package com.bogu.bogubackend.annotation

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local", "local-db")
annotation class LocalBootTest
