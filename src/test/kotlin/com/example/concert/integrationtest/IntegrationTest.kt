package com.example.concert.integrationtest

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MySQLContainer


@ActiveProfiles("test")
@SpringBootTest
@Sql("classpath:/db/init_table.sql")
@Sql("classpath:/db/dml.sql")
class IntegrationTest {

    companion object {
        @JvmStatic
        var container: MySQLContainer<*> = MySQLContainer("mysql:8.0.33")

        @JvmStatic
        @BeforeAll
        fun beforeAll() {

        }
    }
}