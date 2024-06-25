package com.praneeth.teaCenterManagement.dto.order;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReqDto {
    private Long farmerId;
    private List<OrderData> orderData = new ArrayList<>();
}
