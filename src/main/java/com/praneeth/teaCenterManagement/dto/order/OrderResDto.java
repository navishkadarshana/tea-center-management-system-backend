package com.praneeth.teaCenterManagement.dto.order;

import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResDto {

    private Long id;
    private BigDecimal totalAmount;
    private ActiveStatus orderPaidStatus;
    private Date created;
    private Date updated;
    private List<FarmerOrderDataResDto> farmerOrders = new ArrayList<>();
}
