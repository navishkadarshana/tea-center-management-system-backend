package com.praneeth.teaCenterManagement.dto.stock;

import com.praneeth.teaCenterManagement.enums.common.StockType;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockReqDto {

    private StockType stockType;
    private String variant;
    private Long size;
    private Long availableSize;
    private BigDecimal oneKgPrice;
}
