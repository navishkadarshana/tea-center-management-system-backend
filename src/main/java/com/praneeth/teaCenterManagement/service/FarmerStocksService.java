package com.praneeth.teaCenterManagement.service;

import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksReqDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface FarmerStocksService {
    void addFarmerStocks(FarmerStocksReqDto farmerStocksReqDto);
    void updateFarmerStocks(FarmerStocksReqDto farmerStocksReqDto, Long farmerStocksId);
    void deleteFarmerStocks(Long farmerStocksId);
    Page<FarmerStocksResDto> getAllFarmerStocks(Date fromDate, Date toDate, String keyword, Pageable pageable);
}
