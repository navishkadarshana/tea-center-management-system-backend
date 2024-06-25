package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.advance.AdvanceResDto;
import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.order.FarmerOrderResDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryReqDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryResSto;
import com.praneeth.teaCenterManagement.dto.report.FarmerMonthlyReport;

import com.praneeth.teaCenterManagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/admin/report")
@RequiredArgsConstructor
@Log4j2
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterMonthlyReport(@RequestParam Date date, @RequestParam String keyword, Pageable pageable){
        Page<FarmerMonthlyReport> res = reportService.filterMonthlyReport(date, keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveReport(@RequestBody PaymentHistoryReqDto reqDto){
        reportService.saveReport(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true,  "Payment successfully released!"));
    }

    @GetMapping(value = "/payment-history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterPaymentHistory(@RequestParam Integer year, @RequestParam Integer month, @RequestParam String keyword, Pageable pageable){
        Page<PaymentHistoryResSto> res = reportService.filterPaymentHistory(year, month, keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }


}
