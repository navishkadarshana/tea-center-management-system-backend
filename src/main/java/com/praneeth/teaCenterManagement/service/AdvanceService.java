package com.praneeth.teaCenterManagement.service;

import com.praneeth.teaCenterManagement.dto.advance.AdvanceReqDto;
import com.praneeth.teaCenterManagement.dto.advance.AdvanceResDto;
import com.praneeth.teaCenterManagement.entity.Advance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface AdvanceService {
    void addAdvance(AdvanceReqDto dto);
    void updateAdvance(AdvanceReqDto dto, Long id);
    void deleteAdvance(Long id);
    Page<AdvanceResDto> getAdvance(Date fromDate, Date toDate, String keyword, Pageable pageable);
}
