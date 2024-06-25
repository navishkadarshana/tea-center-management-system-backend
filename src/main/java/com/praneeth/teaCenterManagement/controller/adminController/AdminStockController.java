package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.advance.AdvanceReqDto;
import com.praneeth.teaCenterManagement.dto.advance.AdvanceResDto;
import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.stock.StockReqDto;
import com.praneeth.teaCenterManagement.dto.stock.StockResDto;
import com.praneeth.teaCenterManagement.enums.common.StockType;
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
@RequestMapping(value = "/admin/stock")
@RequiredArgsConstructor
@Log4j2
public class AdminStockController {

    private final StockService stockService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addStock(@RequestBody StockReqDto reqDto) {
        log.info("start addStock {}", reqDto);
        stockService.addStock(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "Stock successfully saved!"));
    }

    @PutMapping(value = "/{stock_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAdvance(@PathVariable Long stock_id,
                                          @RequestBody StockReqDto reqDto) {
        stockService.updateStock(reqDto, stock_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Stock successfully updated!"));
    }


    @DeleteMapping(value = "/{stock_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAdvance(@PathVariable Long stock_id) {
        stockService.deleteStock(stock_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Stock successfully deleted!"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmerStocks(@RequestParam StockType stockType, @RequestParam String keyword, Pageable pageable){
        Page<StockResDto> res = stockService.getStock(keyword, stockType, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }


}
