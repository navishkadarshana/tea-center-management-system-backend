package com.praneeth.teaCenterManagement.service;

import com.amazonaws.services.codebuild.model.Report;
import com.praneeth.teaCenterManagement.dto.order.FarmerOrderResDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryReqDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryResSto;
import com.praneeth.teaCenterManagement.dto.report.FarmerMonthlyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ReportService {

    Page<FarmerMonthlyReport> filterMonthlyReport(Date date, String keyword, Pageable pageable);

    void saveReport(PaymentHistoryReqDto reqDto);

    Page<PaymentHistoryResSto> filterPaymentHistory(Integer year, Integer month, String keyword, Pageable pageable);

}
