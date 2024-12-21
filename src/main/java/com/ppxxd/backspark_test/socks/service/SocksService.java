package com.ppxxd.backspark_test.socks.service;

import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.dto.UpdateSocksDto;
import com.ppxxd.backspark_test.socks.enums.FilterOperations;
import com.ppxxd.backspark_test.socks.enums.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocksService {
    SocksDto registerIncome(String color, int cottonPart, int quantity);

    SocksDto registerOutcome(String color, int cottonPart, int quantity);

    Integer getSocksQuantity(String color, FilterOperations operation, int minCottonPart,
                             int maxCottonPart, int quantity);

    List<SocksDto> getSocks(String color, FilterOperations operation, int minCottonPart,
                            int maxCottonPart, int quantity, Sort sort);

    SocksDto updateSock(Long id, UpdateSocksDto updatedSocks);

    List<SocksDto> processBatch(MultipartFile file);
}
