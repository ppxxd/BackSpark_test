package com.ppxxd.backspark_test.socks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSocksDto {
    private String color;
    private Integer cottonPart;
    private Integer quantity;
}
