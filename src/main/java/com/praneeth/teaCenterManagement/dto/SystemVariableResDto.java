package com.praneeth.teaCenterManagement.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemVariableResDto {
    private Long id;
    private BigDecimal todayTeaPrice;
    private BigDecimal teaFactoryPrice;
    private BigDecimal teaCenterCharge;
    private int bagWeight;
    private Date updated;
}
