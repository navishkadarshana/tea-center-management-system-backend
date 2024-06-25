package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.constants.EmailHtmlConstant;
import com.praneeth.teaCenterManagement.dto.SystemVariableReqDto;
import com.praneeth.teaCenterManagement.dto.SystemVariableResDto;
import com.praneeth.teaCenterManagement.dto.user.*;

import com.praneeth.teaCenterManagement.entity.SystemVariable;
import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.AccountVerifyStatus;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.enums.common.UserRole;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.*;
import com.praneeth.teaCenterManagement.service.PublicUserService;
import com.praneeth.teaCenterManagement.utilities.EmailSender;
import com.praneeth.teaCenterManagement.utilities.EmailVerificationTokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.praneeth.teaCenterManagement.constants.AppConstants.Email.*;
import static com.praneeth.teaCenterManagement.constants.AppConstants.ErrorConstants.EMAIL_RESEND_ERROR;
import static com.praneeth.teaCenterManagement.constants.AppConstants.ErrorConstants.TOKEN_HAS_EXPIRED;
import static com.praneeth.teaCenterManagement.constants.AppConstants.NotFoundConstants.*;
import static com.praneeth.teaCenterManagement.exception.constants.ErrorCodeConstants.*;
import static java.math.RoundingMode.HALF_UP;


@Log4j2
@RequiredArgsConstructor
@Service
public class PublicUserServiceImpl implements PublicUserService {


    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserRepository  userRepository;
    private final EmailSender emailSender;
    private final EmailVerificationTokenGenerator emailVerificationTokenGenerator;
    private final SystemVariableRepository systemVariableRepository;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;




    @Override
    @Transactional
    public void createNewFarmer(PublicUserReqDto dto) {
        log.info("Execute method userSignUp {}", dto.toString());

        if (userRepository.findFirstByMobile(dto.getMobile()).isPresent()){
            throw new CustomServiceException(DUPLICATE_ENTRY,"Mobile Number already exists!");
        }

        if (userRepository.findFirstByNic(dto.getNic()).isPresent()){
            throw new CustomServiceException(DUPLICATE_ENTRY,"NIC already exists!");
        }


        try {
            User user = modelMapper.map(dto, User.class);
            user.setFiledLoginAttemptCount(0);
            user.setStatus(ActiveStatus.ACTIVE);
            user.setAccount_verify_status(AccountVerifyStatus.NOT_VERIFY);
            user.setUserRole(UserRole.USER);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userRepository.save(user);

        }catch (Exception ex){
            log.error("Method userSignUp => {}", ex.getMessage());
            throw ex;
        }
    }



    @Override
    public PublicUserResDto updateFarmerProfile(PublicUserReqDto dto, Long userId) {
        log.info("Execute method userDetailUpdate {}", dto.toString());
        try{
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomServiceException(RESOURCE_NOT_FOUND, NO_USER_FOUND));


            if (userRepository.getUserByMobileNotInID(dto.getMobile(), user.getId()).isPresent()){
                throw new CustomServiceException(DUPLICATE_ENTRY,"Mobile already exists!");
            }

            modelMapper.map(dto, user);
            User saveUser = userRepository.save(user);
            return modelMapper.map(saveUser, PublicUserResDto.class);

        }catch (Exception ex){
                log.error("Method userUpdate Details => {}", ex.getMessage());
                throw ex;
            }
    }

    @Override
    public Page<PublicUserResDto> getAllActiveFarmer(String keyword,  Pageable pageable) {
        log.info("Execute method getAllActiveFarmer");
        try {
            if (keyword==null || keyword.isEmpty()){
                return userRepository.getAllUserByStatus(pageable)
                        .map(user -> {
                            PublicUserResDto map = modelMapper.map(user, PublicUserResDto.class);
                            return map;
                        });
            }else {
                return userRepository.getAllUserByStatusAndEmail(keyword, pageable)
                        .map(user -> {
                            PublicUserResDto map = modelMapper.map(user, PublicUserResDto.class);
                            return map;
                        });
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw ex;
        }


    }

    @Override
    public void resendVerifyEmail(String email) {
        User user = userRepository.findFirstByUserName(email).orElseThrow(() -> new CustomServiceException(RESOURCE_NOT_FOUND,NO_USER_FOUND));
        if (user.getAccount_verify_status() == AccountVerifyStatus.VERIFY)
            throw new CustomServiceException("Account Already verified!");
        Date modifiedDate = user.getEmail_verify_link_timestamp();
        Date currentDate = new Date();
        double diff = currentDate.getTime() - modifiedDate.getTime();
        double diffMinutes = diff / (60 * 1000) % 60;

        if (diffMinutes < 1)
            throw new CustomServiceException(SYSTEM_ERROR, EMAIL_RESEND_ERROR);
        this.sendVerificationEmail(user);
    }


    @Override
    public void sendForgotPasswordVerifyLink(String email) {
        User user = userRepository.findFirstByUserName(email).orElseThrow(() -> new CustomServiceException(RESOURCE_NOT_FOUND,NO_USER_FOUND));
        if (user.getAccount_verify_status() == AccountVerifyStatus.NOT_VERIFY)
            throw new CustomServiceException("Your account is not yet verified. Please verify your account first!");
        if (user.getStatus() == ActiveStatus.BLOCK)
            throw new CustomServiceException("This account has been deactivated. please contact your organization admin");

        String token = emailVerificationTokenGenerator.generate(user.getId(), user.getUserName());
        try {
            emailSender.sendSimpleEmail(user.getUserName(), FORGOT_PASSWORD,
                    EmailHtmlConstant.sendForgotPasswordEmailBody(
                            FORGOT_PASSWORD_URL.replace("{frontend_base_url}", frontendBaseUrl).replace("{token}", token), (user)));

            user.setCurrent_verify_token(token);
            user.setEmail_verify_link_timestamp(new Date());
            userRepository.save(user);
        } catch (MessagingException e) {
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Override
    public void updateForgetPassword(String token, String password) {
        log.info("Start function updateForgetPassword");
        try {
            Jws<Claims> claimsJws = emailVerificationTokenGenerator.verify(token);
            if (claimsJws == null) throw new CustomServiceException("Your verification link has expired");

            long userId = Long.parseLong(claimsJws.getBody().getSubject());
            String email = (String) claimsJws.getBody().get(EmailVerificationTokenGenerator.EMAIL_CLAIM);
            User user = userRepository.findUserByIdAndUserName(userId, email)
                    .orElseThrow(() -> new CustomServiceException(NO_USER_FOUND));

            if (!Objects.equals(user.getCurrent_verify_token(), token)) {
                throw new CustomServiceException(TOKEN_HAS_EXPIRED);
            }

            if (user.getAccount_verify_status() == AccountVerifyStatus.NOT_VERIFY)
                throw new CustomServiceException("Your account is not yet verified. Please verify your account first!");
            if (user.getStatus() == ActiveStatus.BLOCK)
                throw new CustomServiceException("This account has been deactivated. please contact your organization admin");

            user.setPassword(passwordEncoder.encode(password));
            user.setFiledLoginAttemptCount(0);
            user.setLastLogOutTimestamp(new Date());
            user.setCurrent_verify_token(null);
            user.setUpdated(new Date());
            userRepository.save(user);
        } catch (Exception ex) {
            log.error("updateForgetPassword : {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean checkPasswordResetToken(String token) {
        log.info("Start function updateForgetPassword");
        try {
            Jws<Claims> claimsJws = emailVerificationTokenGenerator.verify(token);
            if (claimsJws == null) throw new CustomServiceException(VERIFY_TOKEN_EXPIRED, TOKEN_HAS_EXPIRED);

            long userId = Long.parseLong(claimsJws.getBody().getSubject());
            String email = (String) claimsJws.getBody().get(EmailVerificationTokenGenerator.EMAIL_CLAIM);
            User user = userRepository.findUserByIdAndUserName(userId, email)
                    .orElseThrow(() -> new CustomServiceException(INVALID_VERIFY_TOKEN, "Your token appears to be invalid. Please try again later!"));

            if (!Objects.equals(user.getCurrent_verify_token(), token)){
                throw new CustomServiceException(VERIFY_TOKEN_EXPIRED,TOKEN_HAS_EXPIRED);
            }
            return true;
        } catch (Exception ex) {
            log.error("checkPasswordResetToken : {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void verifyAccount(String token) {
        log.info("Start function verifyAccount");
        try {
            Jws<Claims> claimsJws = emailVerificationTokenGenerator.verify(token);
            if (claimsJws == null) throw new CustomServiceException("Your verification link has expired");

            long userId = Long.parseLong(claimsJws.getBody().getSubject());
            String email = (String) claimsJws.getBody().get(EmailVerificationTokenGenerator.EMAIL_CLAIM);
            User user = userRepository.findUserByIdAndUserName(userId, email)
                    .orElseThrow(() -> new CustomServiceException(RESOURCE_NOT_FOUND,NO_USER_FOUND));

            if (!Objects.equals(user.getCurrent_verify_token(), token)) {
                throw new CustomServiceException(TOKEN_HAS_EXPIRED);
            }

            if (user.getAccount_verify_status() == AccountVerifyStatus.VERIFY)
                throw new CustomServiceException("Account Already verified!");

            user.setAccount_verify_status(AccountVerifyStatus.VERIFY);
            userRepository.save(user);

        } catch (Exception ex) {
            log.error("verifyAccount : {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public SystemVariableResDto getSystemVariable() {
        log.info("Start function getSystemVariable");
        try {
            SystemVariable systemVariable = systemVariableRepository.findFirstById(1L).orElseThrow(() -> new CustomServiceException("System Variable Not Found!"));
            return modelMapper.map(systemVariable, SystemVariableResDto.class);
        } catch (Exception ex) {
            log.error("Error getSystemVariable : {}", ex.getMessage());
            throw ex;
        }
    }


    @Override
    public List<PublicUserResDto> filterAllUserForAdmin(String keyword, ActiveStatus status) {

        String updateKeyword = (keyword == null || keyword.isEmpty()) ? null : keyword.trim();
        String updatedStatus = status.equals(ActiveStatus.ALL) ? null : status.name();

        return userRepository.filterUserForAdmin(updateKeyword, updatedStatus)
                .stream()
                .map(user -> modelMapper.map(user, PublicUserResDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateSystem(SystemVariableReqDto dto) {
        log.info("Start function updateSystem");
        try {
            BigDecimal todayTeaPrice = dto.getTeaFactoryPrice().add(dto.getTeaCenterCharge()).setScale(2, HALF_UP);
            SystemVariable systemVariable = systemVariableRepository.findFirstById(1L).orElseThrow(() -> new CustomServiceException("System Variable Not Found!"));
            modelMapper.map(dto, systemVariable);
            systemVariable.setTodayTeaPrice(todayTeaPrice);
            systemVariable.setUpdated(new Date());
            systemVariableRepository.save(systemVariable);
        } catch (Exception ex) {
            log.error("Error updateSystem : {}", ex.getMessage());
            throw ex;
        }
    }


    public void sendVerificationEmail(User user) {
        try {
            String token = emailVerificationTokenGenerator.generate(user.getId(), user.getUserName());
            emailSender.sendSimpleEmail(user.getUserName(), VERIFY_EMAIL,
                    EmailHtmlConstant.sendUserToVerificationLink(
                            EMAIL_CONFORM_URL.replace("{frontend_base_url}", frontendBaseUrl).replace("{token}", token), user));
            user.setCurrent_verify_token(token);
            user.setEmail_verify_link_timestamp(new Date());
            userRepository.save(user);
        } catch (MessagingException e) {
            throw new CustomServiceException(e.getMessage());
        }
    }

}
