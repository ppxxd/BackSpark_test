package com.ppxxd.backspark_test.socks.repository;

import com.ppxxd.backspark_test.socks.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {

    Socks findByColorAndCottonPart(String color, int cottonPart);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE s.color = :color " +
            "AND (:minCottonPart IS NULL OR s.cottonPart >= :minCottonPart) " +
            "AND (:maxCottonPart IS NULL OR s.cottonPart <= :maxCottonPart) " +
            "AND ((:operation = 'MORE_THAN' AND s.quantity > :quantity) " +
            "OR (:operation = 'LESS_THAN' AND s.quantity < :quantity) " +
            "OR (:operation = 'EQUAL' AND s.quantity = :quantity) " +
            "OR (:operation IS NULL))")
    Integer findSocksQuantityByParams(
            @Param("color") String color,
            @Param("operation") String operation,
            @Param("minCottonPart") Integer minCottonPart,
            @Param("maxCottonPart") Integer maxCottonPart,
            @Param("quantity") Integer quantity);

    @Query("SELECT s FROM Socks s WHERE s.color = :color " +
            "AND (:minCottonPart IS NULL OR s.cottonPart >= :minCottonPart) " +
            "AND (:maxCottonPart IS NULL OR s.cottonPart <= :maxCottonPart) " +
            "AND ((:operation = 'MORE_THAN' AND s.quantity > :quantity) " +
            "OR (:operation = 'LESS_THAN' AND s.quantity < :quantity) " +
            "OR (:operation = 'EQUAL' AND s.quantity = :quantity) " +
            "OR (:operation IS NULL))")
    List<Socks> findSocksByParams(
            @Param("color") String color,
            @Param("operation") String operation,
            @Param("minCottonPart") Integer minCottonPart,
            @Param("maxCottonPart") Integer maxCottonPart,
            @Param("quantity") Integer quantity);

}
