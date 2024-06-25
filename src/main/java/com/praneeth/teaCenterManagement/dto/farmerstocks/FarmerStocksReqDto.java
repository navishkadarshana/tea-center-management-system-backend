package com.praneeth.teaCenterManagement.dto.farmerstocks;


import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerStocksReqDto {

    private int numberOfKg;
    private int bagWeight;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal todayTeaPrice;
    private Date date;
    private Long userId;
}
