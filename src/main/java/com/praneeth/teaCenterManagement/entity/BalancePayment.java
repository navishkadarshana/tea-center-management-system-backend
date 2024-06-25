package com.praneeth.teaCenterManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
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
public class BalancePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;

    private int year;
    private int month;

    @Enumerated(value = EnumType.STRING)
    private ActiveStatus paidStatus;

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
