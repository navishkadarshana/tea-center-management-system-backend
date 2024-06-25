package com.praneeth.teaCenterManagement.service;

import com.praneeth.teaCenterManagement.dto.order.FarmerOrderResDto;
import com.praneeth.teaCenterManagement.dto.order.OrderReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface FarmerOrderService {

    void addFarmerOrder(OrderReqDto farmerOrder);

    Page<FarmerOrderResDto> filterOrders(Date fromDate, Date toDate, String keyword, Pageable pageable);

    void deleteFarmerOrder(Long id);

}
