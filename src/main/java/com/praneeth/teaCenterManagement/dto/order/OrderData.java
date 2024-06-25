package com.praneeth.teaCenterManagement.dto.order;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {
    private Long stockId;
    private Long kg;
}
