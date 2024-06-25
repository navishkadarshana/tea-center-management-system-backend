package com.praneeth.teaCenterManagement.dto.order;

import com.praneeth.teaCenterManagement.dto.stock.StockResDto;
import com.praneeth.teaCenterManagement.entity.FarmerOrder;
import com.praneeth.teaCenterManagement.entity.Stock;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerOrderDataResDto {

    private Long id;
    private Long reqSize;
    private BigDecimal stockPrice;
    private BigDecimal oneKgPrice;
    private StockResDto stock;
}
