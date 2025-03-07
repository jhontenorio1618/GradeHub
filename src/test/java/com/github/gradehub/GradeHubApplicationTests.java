package com.github.gradehub;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GradeHubApplicationTests {

    @Test
    void contextLoads() {
    }

    @BeforeAll
    static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("TEST_DB_PASSWORD", dotenv.get("TEST_DB_PASSWORD"));
        System.setProperty("PROD_DB_PASSWORD", dotenv.get("PROD_DB_PASSWORD"));
    }

}
