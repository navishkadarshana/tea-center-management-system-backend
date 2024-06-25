package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import com.praneeth.teaCenterManagement.dto.order.FarmerOrderResDto;
import com.praneeth.teaCenterManagement.dto.order.OrderData;
import com.praneeth.teaCenterManagement.dto.order.OrderReqDto;
import com.praneeth.teaCenterManagement.entity.FarmerOrder;
import com.praneeth.teaCenterManagement.entity.FarmerOrderData;
import com.praneeth.teaCenterManagement.entity.Stock;
import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.*;
import com.praneeth.teaCenterManagement.service.FarmerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

import static java.math.RoundingMode.HALF_UP;

@Log4j2
@RequiredArgsConstructor
@Service
public class FarmerOrderServiceImpl implements FarmerOrderService {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;
    private final AdvanceRepository advanceRepository;
    private final FarmerOrderRepository farmerOrderRepository;
    private final FarmerOrderDataRepository farmerOrderDataRepository;


    @Override
    @Transactional
    public void addFarmerOrder(OrderReqDto farmerOrder) {
        log.info("start addFarmerOrder req : {}", farmerOrder);
        try {
            User user = userRepository.findById(farmerOrder.getFarmerId()).orElseThrow(() -> new CustomServiceException("User nor found"));

            FarmerOrder order = FarmerOrder.builder()
                    .orderPaidStatus(ActiveStatus.NOT_PAID)
                    .updated(new Date())
                    .created(new Date())
                    .user(user)
                    .build();

            FarmerOrder saveOrder = farmerOrderRepository.save(order);

            BigDecimal totalPrice = BigDecimal.ZERO;

            LinkedList<FarmerOrderData> farmerOrderData = new LinkedList<>();

            for (OrderData orderData : farmerOrder.getOrderData()){

                Stock stock = stockRepository.findById(orderData.getStockId()).orElseThrow(() -> new CustomServiceException("Stock nor found"));
                BigDecimal total = stock.getOneKgPrice().multiply(BigDecimal.valueOf(orderData.getKg())).setScale(2, HALF_UP);
                FarmerOrderData singleOrderData = FarmerOrderData
                        .builder()
                        .farmerOrder(saveOrder)
                        .stockPrice(total)
                        .oneKgPrice(stock.getOneKgPrice())
                        .reqSize(orderData.getKg())
                        .stock(stock)
                        .build();
                farmerOrderData.add(singleOrderData);

                totalPrice = totalPrice.add(total);

                stock.setAvailableSize(stock.getAvailableSize()-orderData.getKg());
                stockRepository.save(stock);

            }

            farmerOrderDataRepository.saveAll(farmerOrderData);
            saveOrder.setTotalAmount(totalPrice);
            farmerOrderRepository.save(saveOrder);
        }catch (Exception exception){
            log.error("Exception occurred while addStock :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Page<FarmerOrderResDto> filterOrders(Date fromDate, Date toDate, String keyword, Pageable pageable) {
        log.info("start filterOrders");
        try {
            keyword = keyword.isEmpty() ? null : keyword.trim();

            return farmerOrderRepository.getOrderByDateRangAndKeyword(fromDate, toDate, keyword, pageable)
                    .map(farmerOrder -> modelMapper.map(farmerOrder, FarmerOrderResDto.class));

        }catch (Exception exception){
            log.error("Exception occurred while getAllFarmerStocks :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional
    public void deleteFarmerOrder(Long id) {
        log.info("start deleteFarmerOrder");
        try {
            FarmerOrder order = farmerOrderRepository.findById(id).orElseThrow(() -> new CustomServiceException("Order not found"));
            farmerOrderDataRepository.deleteAll(order.getFarmerOrders());
            farmerOrderRepository.delete(order);
        }catch (Exception exception){
            log.error("Exception occurred while deleteFarmerOrder :{} ", exception.getMessage());
            throw exception;
        }
    }
}
