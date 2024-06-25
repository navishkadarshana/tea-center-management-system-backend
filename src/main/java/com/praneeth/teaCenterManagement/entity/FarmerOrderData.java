package com.praneeth.teaCenterManagement.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FarmerOrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reqSize;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal stockPrice;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal oneKgPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private FarmerOrder farmerOrder;
}
