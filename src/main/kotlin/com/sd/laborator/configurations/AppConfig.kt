package com.sd.laborator.configurations

import com.sd.laborator.interfaces.IAppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource


@Configuration
open class AppConfig : IAppConfig {
    @Bean
    override fun dataSource():  DriverManagerDataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver")
        dataSource.url = "jdbc:mariadb://localhost:3306/cheltuieli?user=root&password=123456789"
        dataSource.username = ""
        dataSource.password = ""
        return dataSource
    }
    @Bean
    override fun jdbcTemplate(dataSource: DriverManagerDataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}
