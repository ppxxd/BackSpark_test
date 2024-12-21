package com.ppxxd.backspark_test;

import com.ppxxd.backspark_test.socks.controller.SocksController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BackSparkTestApplicationTests {
    @Autowired
    private SocksController socksController;

    @Test
    void contextLoads() {
        assertThat(socksController).isNotNull();
    }

}
