package com.praneeth.teaCenterManagement.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResDto {
    private Long farmerCount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalWithdrawal;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal previousMonthWithdrawal;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal todayTeaPrice;
    private Long teaKg;
    private Long fertilizerKg;
    private Long thisMonthTeaKg;
}
