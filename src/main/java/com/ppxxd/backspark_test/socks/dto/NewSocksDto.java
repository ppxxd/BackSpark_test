package com.ppxxd.backspark_test.socks.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NewSocksDto {
    private String color;
    private Integer cottonPart;
    private Integer quantity;
}
