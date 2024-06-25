package com.praneeth.teaCenterManagement.dto.stock;

import com.praneeth.teaCenterManagement.enums.common.StockType;
import lombok.*;


import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResDto {
    private Long id;

    private StockType stockType;
    private String variant;
    private Long size;
    private Long availableSize;
    private BigDecimal oneKgPrice;
    private Date created;
    private Date updated;
}
