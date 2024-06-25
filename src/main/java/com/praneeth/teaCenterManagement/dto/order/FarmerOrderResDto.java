package com.praneeth.teaCenterManagement.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerOrderResDto {

    private Long id;
    private BigDecimal totalAmount;
    private ActiveStatus orderPaidStatus;
    private Date created;
    private Date updated;
    private PublicUserResDto user;
    private List<FarmerOrderDataResDto> farmerOrders = new ArrayList<>();
}
