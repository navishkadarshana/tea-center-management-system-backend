package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import com.praneeth.teaCenterManagement.dto.order.FarmerOrderResDto;
import com.praneeth.teaCenterManagement.dto.order.OrderReqDto;
import com.praneeth.teaCenterManagement.dto.stock.StockReqDto;
import com.praneeth.teaCenterManagement.dto.stock.StockResDto;
import com.praneeth.teaCenterManagement.enums.common.StockType;
import com.praneeth.teaCenterManagement.service.FarmerOrderService;
import com.praneeth.teaCenterManagement.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/admin/order")
@RequiredArgsConstructor
@Log4j2
public class AdminOrderController {

    private final StockService stockService;
    private final FarmerOrderService farmerOrderService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFarmerOrder(@RequestBody OrderReqDto reqDto) {
        log.info("start addFarmerOrder  res : {}", reqDto);
        farmerOrderService.addFarmerOrder(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "Order successfully saved!"));
    }

    @PutMapping(value = "/{stock_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAdvance(@PathVariable Long stock_id,
                                          @RequestBody StockReqDto reqDto) {
        stockService.updateStock(reqDto, stock_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Stock successfully updated!"));
    }


    @DeleteMapping(value = "/{order_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFarmerOrder(@PathVariable Long order_id) {
        farmerOrderService.deleteFarmerOrder(order_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Stock successfully deleted!"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterOrders(@RequestParam Date toDate, @RequestParam Date fromDate, @RequestParam String keyword, Pageable pageable){
        Page<FarmerOrderResDto> res = farmerOrderService.filterOrders(fromDate, toDate, keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }


}
