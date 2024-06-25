package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksReqDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import com.praneeth.teaCenterManagement.entity.FarmerStocks;
import com.praneeth.teaCenterManagement.entity.SystemVariable;
import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.FarmerStocksRepository;
import com.praneeth.teaCenterManagement.repository.SystemVariableRepository;
import com.praneeth.teaCenterManagement.repository.UserRepository;
import com.praneeth.teaCenterManagement.service.FarmerStocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static java.math.RoundingMode.HALF_UP;

@Log4j2
@RequiredArgsConstructor
@Service
public class FarmerStocksServiceImpl implements FarmerStocksService {

    private final UserRepository userRepository;
    private final FarmerStocksRepository farmerStocksRepository;
    private final ModelMapper modelMapper;
    private final SystemVariableRepository systemVariableRepository;

    @Override
    public void addFarmerStocks(FarmerStocksReqDto farmerStocksReqDto) {
        log.info("start addFarmerStocks req : {}", farmerStocksReqDto);
        try {
            User user = userRepository.findById(farmerStocksReqDto.getUserId()).orElseThrow(() -> new CustomServiceException("User nor found"));
            SystemVariable systemVariable = systemVariableRepository.findFirstById(1L).orElseThrow(() -> new CustomServiceException("System Variable Not Found!"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(farmerStocksReqDto.getDate());
            // Get the year and month
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based

            BigDecimal netTotal = systemVariable.getTodayTeaPrice().multiply(BigDecimal.valueOf(farmerStocksReqDto.getNumberOfKg() - farmerStocksReqDto.getBagWeight()));

            FarmerStocks farmerStocks = FarmerStocks.builder()
                    .created(farmerStocksReqDto.getDate())
                    .bagWeight(farmerStocksReqDto.getBagWeight())
                    .numberOfKg(farmerStocksReqDto.getNumberOfKg())
                    .year(year)
                    .paidStatus(ActiveStatus.PENDING)
                    .month(month)
                    .netWeight(farmerStocksReqDto.getNumberOfKg()-farmerStocksReqDto.getBagWeight())
                    .totalPrice(netTotal.setScale(2, HALF_UP))
                    .todayTeaPrice(systemVariable.getTodayTeaPrice())
                    .updated(new Date())
                    .user(user).build();

            farmerStocksRepository.save(farmerStocks);
        }catch (Exception exception){
            log.error("Exception occurred while addFarmerStocks :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void updateFarmerStocks(FarmerStocksReqDto farmerStocksReqDto, Long farmerStocksId) {
        log.info("start updateFarmerStocks req : {}", farmerStocksReqDto);
        try {
            FarmerStocks farmerStocks = farmerStocksRepository.findById(farmerStocksId).orElseThrow(() -> new CustomServiceException("FarmerStocks not found"));
            User user = userRepository.findById(farmerStocksReqDto.getUserId()).orElseThrow(() -> new CustomServiceException("User nor found"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(farmerStocksReqDto.getDate());
            // Get the year and month
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based

            BigDecimal netTotal = farmerStocksReqDto.getTodayTeaPrice().multiply(BigDecimal.valueOf(farmerStocksReqDto.getNumberOfKg() - farmerStocksReqDto.getBagWeight()));


            farmerStocks.setBagWeight(farmerStocksReqDto.getBagWeight());
            farmerStocks.setNumberOfKg(farmerStocksReqDto.getNumberOfKg());
            farmerStocks.setYear(year);
            farmerStocks.setMonth(month);
            farmerStocks.setNetWeight(farmerStocksReqDto.getNumberOfKg()-farmerStocksReqDto.getBagWeight());
            farmerStocks.setCreated(farmerStocksReqDto.getDate());
            farmerStocks.setUpdated(new Date());
            farmerStocks.setUser(user);
            farmerStocks.setTotalPrice(netTotal.setScale(2, HALF_UP));

            farmerStocksRepository.save(farmerStocks);
        }catch (Exception exception){
            log.error("Exception occurred while updateFarmerStocks :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void deleteFarmerStocks(Long farmerStocksId) {
        log.info("start deleteFarmerStocks req : {}", farmerStocksId);
        try {
            FarmerStocks farmerStocks = farmerStocksRepository.findById(farmerStocksId).orElseThrow(() -> new CustomServiceException("FarmerStocks not found"));
            farmerStocksRepository.delete(farmerStocks);
        }catch (Exception exception){
            log.error("Exception occurred while deleteFarmerStocks :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Page<FarmerStocksResDto> getAllFarmerStocks(Date fromDate, Date toDate, String keyword, Pageable pageable) {
        log.info("start getAllFarmerStocks");
        try {
            keyword = keyword.isEmpty() ? null : keyword.trim();

            return farmerStocksRepository.getFarmerStocksByDateRangAndKeyword(fromDate, toDate, keyword, pageable)
                    .map(farmerStocks -> modelMapper.map(farmerStocks, FarmerStocksResDto.class));

        }catch (Exception exception){
            log.error("Exception occurred while getAllFarmerStocks :{} ", exception.getMessage());
            throw exception;
        }
    }
}
