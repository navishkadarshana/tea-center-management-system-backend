package com.praneeth.teaCenterManagement.controller.adminController;



import com.praneeth.teaCenterManagement.dto.advance.AdvanceReqDto;
import com.praneeth.teaCenterManagement.dto.advance.AdvanceResDto;
import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.service.AdvanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/admin/advance")
@RequiredArgsConstructor
@Log4j2
public class AdminAdvanceController {

    private final AdvanceService advanceService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAdvance(@RequestBody AdvanceReqDto reqDto) {
        log.info("start addAdvance {}", reqDto);
        advanceService.addAdvance(reqDto);
        return ResponseEntity.ok(new CommonResponse<>(true, "Advance payment successfully saved!"));
    }

    @PutMapping(value = "/{advance_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAdvance(@PathVariable Long advance_id,
                                          @RequestBody AdvanceReqDto reqDto) {
        advanceService.updateAdvance(reqDto, advance_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Advance successfully updated!"));
    }


    @DeleteMapping(value = "/{advance_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAdvance(@PathVariable Long advance_id) {
        advanceService.deleteAdvance(advance_id);
        return ResponseEntity.ok(new CommonResponse<>(true, "Advance payment successfully deleted!"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFarmerStocks(@RequestParam Date toDate, @RequestParam Date fromDate, @RequestParam String keyword, Pageable pageable){
        Page<AdvanceResDto> res = advanceService.getAdvance(fromDate, toDate, keyword, pageable);
        return ResponseEntity.ok(new CommonResponse<>(true,  res));
    }


}
