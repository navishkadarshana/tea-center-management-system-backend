package com.praneeth.teaCenterManagement.dto.farmerstocks;



import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerStocksResDto {
    private Long id;
    private int numberOfKg;
    private int bagWeight;
    private int netWeight;
    private int year;
    private int month;
    private BigDecimal totalPrice;
    private BigDecimal todayTeaPrice;
    private Date created;
    private Date updated;
    private PublicUserResDto user;
}
