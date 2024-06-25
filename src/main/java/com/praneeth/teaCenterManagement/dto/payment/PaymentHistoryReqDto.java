package com.praneeth.teaCenterManagement.dto.payment;

import com.praneeth.teaCenterManagement.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryReqDto {

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

    private Long userId;
}
