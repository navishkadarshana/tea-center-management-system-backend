package com.praneeth.teaCenterManagement.controller.UserController;

import com.praneeth.teaCenterManagement.config.throttling_config.Throttling;
import com.praneeth.teaCenterManagement.dto.common.CommonResponse;
import com.praneeth.teaCenterManagement.dto.user.PublicUserReqDto;
import com.praneeth.teaCenterManagement.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/public-user")
@RequiredArgsConstructor
@Log4j2
public class PublicUserController {

    private final PublicUserService userService;



    @Throttling(timeFrameInSeconds = 60, calls = 10)
    @PostMapping(value = "/account/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        log.info("verifyAccount token => reqBody: {}",token);
        userService.verifyAccount(token);
        return ResponseEntity.ok(new CommonResponse<>(true, "Your account has been activated successfully!"));
    }

    @Throttling(timeFrameInSeconds = 60, calls = 2)
    @PostMapping(value = "/account/forgot-password")
    public ResponseEntity<?> forgotPasswordEmailSend(@RequestParam("email") String email) {
        log.info("send forgot password verify email req => email : {}", email);
        userService.sendForgotPasswordVerifyLink(email);
        return ResponseEntity.ok(new CommonResponse<>(true, "Email sent!"));
    }


}
