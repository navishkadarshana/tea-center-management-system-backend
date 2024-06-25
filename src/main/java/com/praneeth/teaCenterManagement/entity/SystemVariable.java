package com.praneeth.teaCenterManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SystemVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal todayTeaPrice;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal teaFactoryPrice;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal teaCenterCharge;
    private int bagWeight;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
