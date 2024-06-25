package com.praneeth.teaCenterManagement.service.impl;

import com.praneeth.teaCenterManagement.dto.advance.AdvanceReqDto;
import com.praneeth.teaCenterManagement.dto.advance.AdvanceResDto;
import com.praneeth.teaCenterManagement.entity.Advance;
import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.Status;
import com.praneeth.teaCenterManagement.exception.dto.CustomServiceException;
import com.praneeth.teaCenterManagement.repository.AdvanceRepository;
import com.praneeth.teaCenterManagement.repository.UserRepository;
import com.praneeth.teaCenterManagement.service.AdvanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Date;


@Log4j2
@RequiredArgsConstructor
@Service
public class AdvanceServiceImpl implements AdvanceService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AdvanceRepository advanceRepository;


    @Override
    public void addAdvance(AdvanceReqDto dto) {
        log.info("start addAdvance req : {}", dto);
        try {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new CustomServiceException("User nor found"));
            Advance advance = modelMapper.map(dto, Advance.class);
            advance.setUser(user);
            advance.setStatus(Status.PENDING);
            advance.setUpdated(new Date());
            advanceRepository.save(advance);
        }catch (Exception exception){
            log.error("Exception occurred while addAdvance :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void updateAdvance(AdvanceReqDto dto, Long id) {
        log.info("start updateAdvance req : {}", dto);
        try {
            Advance advance = advanceRepository.findById(id).orElseThrow(() -> new CustomServiceException("Advance nor found"));
            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new CustomServiceException("User nor found"));
            modelMapper.map(dto, advance);
            advance.setUser(user);
            advance.setUpdated(new Date());
            advanceRepository.save(advance);
        }catch (Exception exception){
            log.error("Exception occurred while updateAdvance :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void deleteAdvance(Long id) {
        log.info("start deleteAdvance id : {}", id);
        try {
            Advance advance = advanceRepository.findById(id).orElseThrow(() -> new CustomServiceException("Advance nor found"));
            advanceRepository.delete(advance);
        }catch (Exception exception){
            log.error("Exception occurred while deleteAdvance :{} ", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Page<AdvanceResDto> getAdvance(Date fromDate, Date toDate, String keyword, Pageable pageable) {
        log.info("start getAdvance");
        try {
            keyword = keyword.isEmpty() ? null : keyword.trim();

            return advanceRepository.getAdvanceByDateRangAndKeyword(fromDate, toDate, keyword, pageable)
                    .map(advance -> modelMapper.map(advance, AdvanceResDto.class));

        }catch (Exception exception){
            log.error("Exception occurred while getAdvance :{} ", exception.getMessage());
            throw exception;
        }
    }
}
