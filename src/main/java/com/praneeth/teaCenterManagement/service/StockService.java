package com.praneeth.teaCenterManagement.service;

import com.praneeth.teaCenterManagement.dto.StatisticsResDto;
import com.praneeth.teaCenterManagement.dto.stock.StockReqDto;
import com.praneeth.teaCenterManagement.dto.stock.StockResDto;
import com.praneeth.teaCenterManagement.enums.common.StockType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public interface StockService {
    void addStock(StockReqDto dto);
    void updateStock(StockReqDto dto, Long id);
    void deleteStock(Long id);
    Page<StockResDto> getStock(String keyword, StockType stockType, Pageable pageable);
    StatisticsResDto getStockStatistics();
}
