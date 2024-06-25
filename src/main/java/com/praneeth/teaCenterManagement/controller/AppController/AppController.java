package com.praneeth.teaCenterManagement.controller.AppController;

import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping(value = "/application")
@RequiredArgsConstructor
@Log4j2
public class AppController {
    @Value("${appVersion}")
    private String appVersion;
    private final PublicUserService publicUserService;



    @GetMapping(value = "/version")
    public ResponseEntity<?> getAppVersion() {
        return ResponseEntity.ok(new CommonResponse<>(true, appVersion));
    }

    @GetMapping(value = "/system-variable")
    public ResponseEntity<?> getSystemVariable() {
        return ResponseEntity.ok(new CommonResponse<>(true, publicUserService.getSystemVariable()));
    }

}
