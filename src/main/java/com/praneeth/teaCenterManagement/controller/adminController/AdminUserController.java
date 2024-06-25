package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.StatisticsResDto;
import com.praneeth.teaCenterManagement.dto.SystemVariableReqDto;
import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.user.PublicUserReqDto;
import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import com.praneeth.teaCenterManagement.service.PublicUserService;
import com.praneeth.teaCenterManagement.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminUserController {

    private final PublicUserService publicUserService;
    private final StockService stockService;

    @PostMapping(value = "/farmer/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewFarmer(@RequestBody PublicUserReqDto reqDto) {
        log.info("start createNewFarmer {}", reqDto);
        publicUserService.createNewFarmer(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "Farmer successfully created!"));
    }


    @GetMapping(value = "/farmer/user-list")
    public ResponseEntity<?> getFarmerList(@RequestParam String keyword, Pageable pageable){
        Page<PublicUserResDto> res = publicUserService.getAllActiveFarmer(keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }

    @PutMapping(value = "/farmer/{farmer_id}/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmer(@PathVariable Long farmer_id,@RequestBody PublicUserReqDto reqDto) {
        publicUserService.updateFarmerProfile(reqDto, farmer_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Farmer successfully updated!"));
    }

    @PutMapping(value = "/system-variable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSystem(@RequestBody SystemVariableReqDto reqDto) {
        publicUserService.updateSystem(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "System variable successfully updated!"));
    }

    @GetMapping(value = "/statistics")
    public ResponseEntity<?> getStockStatistics(){
        StatisticsResDto stockStatistics = stockService.getStockStatistics();
        return ResponseEntity.ok(new CommonResponse<>(true,  stockStatistics));
    }


}
