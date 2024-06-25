package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.dto.SystemVariableResDto;
import com.praneeth.teaCenterManagement.dto.advance.AdvanceWithoutUserResDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksWithoutUserResDto;
import com.praneeth.teaCenterManagement.dto.order.OrderResDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryReqDto;
import com.praneeth.teaCenterManagement.dto.payment.PaymentHistoryResSto;
import com.praneeth.teaCenterManagement.dto.report.FarmerMonthlyReport;
import com.praneeth.teaCenterManagement.entity.*;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.*;
import com.praneeth.teaCenterManagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.praneeth.teaCenterManagement.constants.AppConstants.NotFoundConstants.NO_USER_FOUND;
import static com.praneeth.teaCenterManagement.exception.constants.ErrorCodeConstants.RESOURCE_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final FarmerStocksRepository farmerStocksRepository;
    private final AdvanceRepository advanceRepository;
    private final ModelMapper modelMapper;
    private final FarmerOrderRepository farmerOrderRepository;
    private final BalancePaymentRepository balancePaymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<FarmerMonthlyReport> filterMonthlyReport(Date date,  String keyword, Pageable pageable) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // Get the year and month
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            return farmerStocksRepository.getFarmerStocksByDateRangAndKeywordForReport(year, month, keyword, pageable)
                    .map(farmerStocks -> {
                        farmerStocks.setYear(year);
                        farmerStocks.setMonth(month);

                        BigDecimal pendingAdvanceTotal=BigDecimal.ZERO;
                        BigDecimal pendingOrderAmount=BigDecimal.ZERO;
                        BigDecimal pendingBalanceAmount=BigDecimal.ZERO;


                        BigDecimal pendingAdvance = advanceRepository.getPendingAdvanceTotal(farmerStocks.getFarmerId());
                        if (pendingAdvance!=null){
                            pendingAdvanceTotal=pendingAdvanceTotal.add(pendingAdvance);
                        }

                        BigDecimal pendingOrder = farmerOrderRepository.getPendingOrderAmount(farmerStocks.getFarmerId());
                        if (pendingOrder!=null){
                            pendingOrderAmount=pendingOrderAmount.add(pendingOrder);
                        }

                        BigDecimal balanceAmount = balancePaymentRepository.getPendingBalanceAmount(farmerStocks.getFarmerId());
                        if (balanceAmount!=null){
                            pendingBalanceAmount=pendingBalanceAmount.add(balanceAmount);
                        }

                        farmerStocks.setTotalPendingAdvanceAmount(pendingAdvanceTotal);
                        farmerStocks.setTotalPendingOrderAmount(pendingOrderAmount);
                        farmerStocks.setTotalPendingBalancePayment(pendingBalanceAmount);
                        farmerStocks.setMonthlyTotalDeduction(farmerStocks.getTotalPendingAdvanceAmount().add(farmerStocks.getTotalPendingOrderAmount()).add(farmerStocks.getTotalPendingBalancePayment()));
                        farmerStocks.setMonthlyNetTotal(farmerStocks.getMonthlyTotalAmount().subtract(farmerStocks.getMonthlyTotalDeduction()));

                        int comparisonResult = farmerStocks.getMonthlyTotalAmount().compareTo(farmerStocks.getMonthlyTotalDeduction());

                        BigDecimal thisMonthBalance = BigDecimal.ZERO;
                        if (comparisonResult < 0) {
                            thisMonthBalance = farmerStocks.getMonthlyTotalDeduction().subtract(farmerStocks.getMonthlyTotalAmount());
                            farmerStocks.setThisPaymentBalanceAmount(thisMonthBalance);
                        }


                        //set farmer stock list
                        List<FarmerStocksWithoutUserResDto> farmerStocksList = farmerStocksRepository.getFarmerStocksByYearMonthAndIdForReport(year, month, farmerStocks.getFarmerId())
                                .stream().map(fs -> modelMapper.map(fs, FarmerStocksWithoutUserResDto.class))
                                .collect(Collectors.toList());
                        farmerStocks.setFarmerStocksList(farmerStocksList);

                        //set advance list
                        List<AdvanceWithoutUserResDto> advanceList = advanceRepository.getPendingAdvance(farmerStocks.getFarmerId())
                                .stream().map(fs -> modelMapper.map(fs, AdvanceWithoutUserResDto.class))
                                .collect(Collectors.toList());
                        farmerStocks.setAdvanceList(advanceList);

                        //set order list
                        List<OrderResDto> orderList = farmerOrderRepository.getPendingOrder(farmerStocks.getFarmerId())
                                .stream().map(fs -> modelMapper.map(fs, OrderResDto.class))
                                .collect(Collectors.toList());
                        farmerStocks.setOrderList(orderList);

                        return farmerStocks;
                    });
        }catch (Exception ex){
            log.error("Method userSignUp => {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public void saveReport(PaymentHistoryReqDto reqDto) {
        try {
            User user = userRepository.findById(reqDto.getUserId()).orElseThrow(() -> new CustomServiceException(RESOURCE_NOT_FOUND, NO_USER_FOUND));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based

            //if (month== reqDto.getMonth()) throw new CustomServiceException("You can pay this month only after the end of this month.");

            boolean present = paymentHistoryRepository.findFirstByYearAndMonthAndUser(year, month, user).isPresent();
            if (present) throw new CustomServiceException("This payment already proceed");
            PaymentHistory paymentHistory = modelMapper.map(reqDto, PaymentHistory.class);
            paymentHistory.setCreated(new Date());
            paymentHistory.setUpdated(new Date());
            paymentHistory.setYear(reqDto.getYear());
            paymentHistory.setMonth(reqDto.getMonth());
            paymentHistory.setUser(user);

            paymentHistoryRepository.save(paymentHistory);

            advanceRepository.updateAdvanceStatus(user.getId());
            farmerOrderRepository.updateOrderStatus(user.getId());
            balancePaymentRepository.updateBalanceStatus(user.getId());
            farmerStocksRepository.updateStockStatus(reqDto.getYear(), reqDto.getMonth(), user.getId());

            if (reqDto.getThisPaymentBalanceAmount()!=null || reqDto.getThisPaymentBalanceAmount()!=BigDecimal.ZERO){
                BalancePayment build = BalancePayment
                        .builder()
                        .amount(reqDto.getThisPaymentBalanceAmount())
                        .month(month)
                        .year(year)
                        .user(user)
                        .paidStatus(ActiveStatus.PENDING)
                        .created(new Date())
                        .updated(new Date())
                        .build();
                balancePaymentRepository.save(build);
            }

        }catch (Exception ex){
            log.error("Method saveReport => {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Page<PaymentHistoryResSto> filterPaymentHistory(Integer year, Integer month, String keyword, Pageable pageable) {
        log.info("Start function filterOrders");
        year = year==0 ? null : year;
        month = month==0 ? null : month;
        keyword = keyword.isEmpty() ? null : keyword.trim();

        try {
            return paymentHistoryRepository.filterPaymentHistory(year, month, keyword, pageable)
                    .map(paymentHistory-> modelMapper.map(paymentHistory, PaymentHistoryResSto.class));
        } catch (Exception ex) {
            log.error("Error filterOrders : {}", ex.getMessage());
            throw ex;
        }
    }
}
