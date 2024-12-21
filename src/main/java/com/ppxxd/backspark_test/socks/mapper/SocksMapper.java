package com.ppxxd.backspark_test.socks.mapper;

import com.ppxxd.backspark_test.socks.dto.NewSocksDto;
import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.model.Socks;

public class SocksMapper {

    public static Socks fromDto(SocksDto socksDto) {
        if (socksDto == null) {
            return null;
        }

        return Socks.builder()
                .id(socksDto.getId())
                .color(socksDto.getColor())
                .cottonPart(socksDto.getCottonPart())
                .quantity(socksDto.getQuantity())
                .build();
    }

    public static Socks fromNewDto(NewSocksDto socksDto) {
        if (socksDto == null) {
            return null;
        }

        return Socks.builder()
                .color(socksDto.getColor())
                .cottonPart(socksDto.getCottonPart())
                .quantity(socksDto.getQuantity())
                .build();
    }

    public static SocksDto toDto(Socks socks) {
        if (socks == null) {
            return null;
        }

        return SocksDto.builder()
                .id(socks.getId())
                .color(socks.getColor())
                .cottonPart(socks.getCottonPart())
                .quantity(socks.getQuantity())
                .build();
    }
}
