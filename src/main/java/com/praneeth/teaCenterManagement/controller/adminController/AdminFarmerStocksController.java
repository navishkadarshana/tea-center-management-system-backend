package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksReqDto;
import com.praneeth.teaCenterManagement.dto.farmerstocks.FarmerStocksResDto;
import com.praneeth.teaCenterManagement.dto.user.PublicUserReqDto;
import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import com.praneeth.teaCenterManagement.service.FarmerStocksService;
import com.praneeth.teaCenterManagement.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/admin/farmer-stocks")
@RequiredArgsConstructor
@Log4j2
public class AdminFarmerStocksController {

    private final FarmerStocksService farmerStocksService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFarmerStocks(@RequestBody FarmerStocksReqDto reqDto) {
        log.info("start addFarmerStocks {}", reqDto);
        farmerStocksService.addFarmerStocks(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "Farmer Tea Stocks successfully add to the stocks!"));
    }

    @PutMapping(value = "/{farmer_stocks_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmerStocks(@PathVariable Long farmer_stocks_id,
                                          @RequestBody FarmerStocksReqDto reqDto) {
        farmerStocksService.updateFarmerStocks(reqDto, farmer_stocks_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Farmer Tea Stocks successfully updated!"));
    }


    @DeleteMapping(value = "/{farmer_stocks_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmer(@PathVariable Long farmer_stocks_id) {
        farmerStocksService.deleteFarmerStocks(farmer_stocks_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Farmer Tea Stocks successfully deleted!"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmerStocks(@RequestParam Date toDate, @RequestParam Date fromDate, @RequestParam String keyword, Pageable pageable){
        Page<FarmerStocksResDto> res = farmerStocksService.getAllFarmerStocks(fromDate, toDate, keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }


}
