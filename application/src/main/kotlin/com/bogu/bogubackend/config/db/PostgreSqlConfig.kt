package com.bogu.bogubackend.config.db


import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import jakarta.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "postgreSqlEntityManagerFactory",
    transactionManagerRef = "postgreSqlTransactionManager",
    basePackages = ["com.bogu.bogubackend.repo.postgresql"]
)
class PostgreSqlConfig {
    @Bean
    @ConfigurationProperties(prefix = "postgresql.datasource")
    fun postgreSqlDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    fun postgreSqlEntityManagerFactory(
        builder: EntityManagerFactoryBuilder, @Qualifier("postgreSqlDataSource") postgreSqlDataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(postgreSqlDataSource)
            .packages(
                "com.bogu.bogubackend.domain.entity.postgresql"
            )
            .properties(
                mutableMapOf(
                    Pair("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"),
                    Pair(
                        "hibernate.physical_naming_strategy",
                        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
                    )
                )
            )
            .persistenceUnit("postgresql")
            .build()
    }

    @Bean
    fun postgreSqlTransactionManager(
        @Qualifier("postgreSqlEntityManagerFactory") postgreSqlEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(postgreSqlEntityManagerFactory)
    }
}
