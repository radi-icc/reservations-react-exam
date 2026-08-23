package com.pid.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Runs only when a prepared MySQL schema is intentionally supplied by CI or a developer. */
@SpringBootTest
@ActiveProfiles("mysql")
@EnabledIfEnvironmentVariable(named = "RUN_MYSQL_TESTS", matches = "true")
class MysqlConfigurationIntegrationTest {
    @Test
    void mysqlSchemaAndApplicationContextAreCompatible() {
    }
}
