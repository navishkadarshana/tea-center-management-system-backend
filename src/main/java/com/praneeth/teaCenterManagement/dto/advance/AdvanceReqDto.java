package com.praneeth.teaCenterManagement.dto.advance;


import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceReqDto {
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
    private Date created;
    private Long userId;
}
