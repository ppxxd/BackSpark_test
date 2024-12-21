package com.ppxxd.backspark_test.socks.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "socks")
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "cotton_part", nullable = false)
    private Integer cottonPart;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
