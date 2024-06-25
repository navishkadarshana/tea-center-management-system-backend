package com.praneeth.teaCenterManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int year;
    private int month;

    private Long monthlyTotalTeaKg;
    private Long monthlyNetTotalTeaKg;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyTotalAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyTotalDeduction;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyNetTotal;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingOrderAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingAdvanceAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingBalancePayment;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal thisPaymentBalanceAmount;

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
