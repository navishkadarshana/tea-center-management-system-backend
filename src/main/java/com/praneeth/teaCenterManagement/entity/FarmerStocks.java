package com.praneeth.teaCenterManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.enums.common.StockType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FarmerStocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfKg;
    private int bagWeight;
    private int netWeight;

    private int year;
    private int month;

    @Enumerated(value = EnumType.STRING)
    private ActiveStatus paidStatus;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPrice;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal todayTeaPrice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private User user;
}
