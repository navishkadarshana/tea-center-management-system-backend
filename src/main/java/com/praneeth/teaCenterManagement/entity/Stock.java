package com.praneeth.teaCenterManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.praneeth.teaCenterManagement.enums.common.Status;
import com.praneeth.teaCenterManagement.enums.common.StockType;
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
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StockType stockType;

    private String variant;

    private Long size;

    private Long availableSize;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal oneKgPrice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FarmerOrderData> farmerOrderData = new ArrayList<>();
}
