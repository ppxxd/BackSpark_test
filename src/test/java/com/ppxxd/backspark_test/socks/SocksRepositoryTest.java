package com.ppxxd.backspark_test.socks;

import com.ppxxd.backspark_test.socks.enums.FilterOperations;
import com.ppxxd.backspark_test.socks.model.Socks;
import com.ppxxd.backspark_test.socks.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SocksRepositoryTest {
    @Autowired
    private SocksRepository socksRepository;
    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        Socks redSocks = new Socks(null, "Red", 80, 10);
        Socks blueSocks = new Socks(null, "Blue", 70, 20);
        entityManager.persist(redSocks);
        entityManager.persist(blueSocks);
    }

    @Test
    void testFindByColorAndCottonPart_ShouldReturnMatchingSocks() {
        Socks result = socksRepository.findByColorAndCottonPart("Red", 80);

        assertThat(result).isNotNull();
        assertThat(result.getColor()).isEqualTo("Red");
        assertThat(result.getCottonPart()).isEqualTo(80);
        assertThat(result.getQuantity()).isEqualTo(10);
    }

    @Test
    void testFindSocksQuantityByParams_WithMoreThan_ShouldReturnMatchingQuantity() {
        Integer quantity = socksRepository.findSocksQuantityByParams("Red",
                FilterOperations.MORE_THAN.name(), 70, 90, 5);

        assertThat(quantity).isNotNull();
        assertThat(quantity).isEqualTo(10);
    }

    @Test
    void testFindSocksByParams_WithMoreThan_ShouldReturnMatchingSocks() {
        List<Socks> result = socksRepository.findSocksByParams("Red", FilterOperations.MORE_THAN.name(), 70, 90, 5);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getColor()).isEqualTo("Red");
        assertThat(result.get(0).getCottonPart()).isEqualTo(80);
    }
}
