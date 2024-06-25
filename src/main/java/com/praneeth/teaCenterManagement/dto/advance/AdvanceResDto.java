package com.praneeth.teaCenterManagement.dto.advance;

import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import com.praneeth.teaCenterManagement.enums.common.Status;
import lombok.*;


import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceResDto {

    private Long id;
    private BigDecimal amount;
    private Status status;
    private Date created;
    private Date updated;
    private PublicUserResDto user;
}
