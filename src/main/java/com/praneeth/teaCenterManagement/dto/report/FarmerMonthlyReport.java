package com.praneeth.teaCenterManagement.dto.report;

import com.praneeth.teaCenterManagement.dto.advance.AdvanceWithoutUserResDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksWithoutUserResDto;
import com.praneeth.teaCenterManagement.dto.order.OrderResDto;
import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerMonthlyReport {
    private Long farmerId;
    private int year;
    private int month;
    private String farmerFirstName;
    private String farmerLastName;
    private String farmerMobileNumber;
    private String farmerNic;
    private Long monthlyTotalTeaKg;
    private Long monthlyNetTotalTeaKg;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyTotalAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyTotalDeduction;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal monthlyNetTotal;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingOrderAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingAdvanceAmount;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal totalPendingBalancePayment;
    @Digits(integer = 9, fraction = 2)
    private BigDecimal thisPaymentBalanceAmount;

    private List<OrderResDto> orderList = new ArrayList<>();
    private List<AdvanceWithoutUserResDto> advanceList = new ArrayList<>();
    private List<FarmerStocksWithoutUserResDto> farmerStocksList = new ArrayList<>();

    public FarmerMonthlyReport(Long farmerId, String farmerFirstName, String farmerLastName, String farmerMobileNumber, String farmerNic, Long monthlyTotalTeaKg, Long monthlyNetTotalTeaKg, BigDecimal monthlyTotalAmount) {
        this.farmerId = farmerId;
        this.farmerFirstName = farmerFirstName;
        this.farmerLastName = farmerLastName;
        this.farmerMobileNumber = farmerMobileNumber;
        this.farmerNic = farmerNic;
        this.monthlyTotalTeaKg = monthlyTotalTeaKg;
        this.monthlyNetTotalTeaKg = monthlyNetTotalTeaKg;
        this.monthlyTotalAmount = monthlyTotalAmount;
    }

    public FarmerMonthlyReport(Long farmerId, String farmerFirstName, String farmerLastName, String farmerMobileNumber, String farmerNic, Long monthlyTotalTeaKg, Long monthlyNetTotalTeaKg, BigDecimal monthlyTotalAmount, BigDecimal totalPendingOrderAmount, BigDecimal totalPendingAdvanceAmount) {
        this.farmerId = farmerId;
        this.farmerFirstName = farmerFirstName;
        this.farmerLastName = farmerLastName;
        this.farmerMobileNumber = farmerMobileNumber;
        this.farmerNic = farmerNic;
        this.monthlyTotalTeaKg = monthlyTotalTeaKg;
        this.monthlyNetTotalTeaKg = monthlyNetTotalTeaKg;
        this.monthlyTotalAmount = monthlyTotalAmount;
        this.totalPendingOrderAmount = totalPendingOrderAmount;
        this.totalPendingAdvanceAmount = totalPendingAdvanceAmount;
    }
}
