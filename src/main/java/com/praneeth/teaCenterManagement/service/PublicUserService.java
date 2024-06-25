package com.praneeth.teaCenterManagement.service;

import com.praneeth.teaCenterManagement.dto.SystemVariableReqDto;
import com.praneeth.teaCenterManagement.dto.SystemVariableResDto;
import com.praneeth.teaCenterManagement.dto.user.*;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PublicUserService {

    void createNewFarmer(PublicUserReqDto dto);

    PublicUserResDto updateFarmerProfile(PublicUserReqDto dto, Long userId);

    Page<PublicUserResDto> getAllActiveFarmer(String keyword,  Pageable pageable);

    void resendVerifyEmail(String email);

    void sendForgotPasswordVerifyLink(String email);

    void updateForgetPassword(String token, String password);

    boolean checkPasswordResetToken(String token);

    void verifyAccount(String token);

    SystemVariableResDto getSystemVariable();

    List<PublicUserResDto> filterAllUserForAdmin(String keyword, ActiveStatus status);

    void updateSystem(SystemVariableReqDto dto);

}
