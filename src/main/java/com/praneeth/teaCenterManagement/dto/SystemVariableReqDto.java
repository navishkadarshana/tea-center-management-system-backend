package com.praneeth.teaCenterManagement.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemVariableReqDto {
    private BigDecimal todayTeaPrice;
    private BigDecimal teaFactoryPrice;
    private BigDecimal teaCenterCharge;
    private int bagWeight;
}
