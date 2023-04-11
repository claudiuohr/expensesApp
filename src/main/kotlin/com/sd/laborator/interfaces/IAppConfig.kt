package com.sd.laborator.interfaces

import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource

interface IAppConfig {
    @Bean
    fun dataSource(): DriverManagerDataSource

    @Bean
    fun jdbcTemplate(dataSource: DriverManagerDataSource): JdbcTemplate
}