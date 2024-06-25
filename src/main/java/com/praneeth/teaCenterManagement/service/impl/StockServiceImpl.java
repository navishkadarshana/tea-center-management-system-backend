package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.dto.StatisticsResDto;
import com.praneeth.teaCenterManagement.dto.stock.StockReqDto;
import com.praneeth.teaCenterManagement.dto.stock.StockResDto;
import com.praneeth.teaCenterManagement.entity.PaymentHistory;
import com.praneeth.teaCenterManagement.entity.Stock;
import com.praneeth.teaCenterManagement.entity.SystemVariable;
import com.praneeth.teaCenterManagement.enums.common.StockType;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.*;
import com.praneeth.teaCenterManagement.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Log4j2
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private final ModelMapper modelMapper;
    private final StockRepository stockRepository;
    private final FarmerStocksRepository farmerStocksRepository;
    private final UserRepository userRepository;
    private final SystemVariableRepository systemVariableRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Override
    public void addStock(StockReqDto dto) {
        log.info("start addStock req : {}", dto);
        try {
            Stock stock = modelMapper.map(dto, Stock.class);
            stock.setCreated(new Date());
            stock.setUpdated(new Date());
            stock.setAvailableSize(stock.getSize());
            stockRepository.save(stock);
        }catch (Exception exception){
            log.error("Exception occurred while addStock :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void updateStock(StockReqDto dto, Long id) {
        log.info("start updateStock req : {}", dto);
        try {
            Stock stock = stockRepository.findById(id).orElseThrow(() -> new CustomServiceException("Stock not found"));
            modelMapper.map(dto, stock);
            stock.setUpdated(new Date());
            stockRepository.save(stock);
        }catch (Exception exception){
            log.error("Exception occurred while updateStock :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void deleteStock(Long id) {
        log.info("start deleteStock id : {}", id);
        try {
            Stock stock = stockRepository.findById(id).orElseThrow(() -> new CustomServiceException("Stock not found"));
            stockRepository.delete(stock);
        }catch (Exception exception){
            log.error("Exception occurred while deleteStock :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Page<StockResDto> getStock(String keyword, StockType stockType, Pageable pageable) {
        log.info("start getStock");
        try {
            keyword = keyword.isEmpty() ? null : keyword.trim();

            return stockRepository.filterStock(stockType.name(), keyword, pageable)
                    .map(advance -> modelMapper.map(advance, StockResDto.class));

        }catch (Exception exception){
            log.error("Exception occurred while getStock :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public StatisticsResDto getStockStatistics() {
        log.info("start getStockStatistics");
        try {

            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonth = currentDate.minusMonths(1);
            int previousMonthValue = previousMonth.getMonthValue();
            SystemVariable systemVariable = systemVariableRepository.findFirstById(1L).orElseThrow(() -> new CustomServiceException("System Variable Not Found!"));

            return StatisticsResDto.builder()
                    .teaKg(stockRepository.getTotalStock(StockType.TEA))
                    .fertilizerKg(stockRepository.getTotalStock(StockType.FERTILIZER))
                    .farmerCount(userRepository.getUserCount())
                    .previousMonthWithdrawal(paymentHistoryRepository.getMonthWithdrawal(previousMonthValue))
                    .totalWithdrawal(paymentHistoryRepository.getTotalWithdrawal())
                    .todayTeaPrice(systemVariable.getTodayTeaPrice())
                    .thisMonthTeaKg(farmerStocksRepository.getMonthTeaKg(currentDate.getMonthValue()))
                    .build();

        }catch (Exception exception){
            log.error("Exception occurred while getStockStatistics :{} ", exception.getMessage());
            throw exception;
        }
    }
}
