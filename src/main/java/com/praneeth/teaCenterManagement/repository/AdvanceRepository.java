package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.Advance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface AdvanceRepository extends JpaRepository<Advance, Long> {

    @Query(value = "SELECT a FROM Advance a INNER JOIN User u ON a.user.id=u.id WHERE DATE(a.created) BETWEEN DATE(?1) AND DATE(?2) " +
            "AND (?3 IS NULL OR (u.firstName LIKE %?3%) OR (u.lastName LIKE %?3%) OR (u.mobile LIKE %?3%)) GROUP BY a.id ORDER BY a.id ASC")
    Page<Advance> getAdvanceByDateRangAndKeyword(Date formDate, Date toDate, String keyword, Pageable pageable);

    @Query(value = "SELECT a FROM Advance a WHERE a.status='PENDING' AND a.user.id=?1 GROUP BY a.id ORDER BY a.id ASC")
    List<Advance> getPendingAdvance(Long userId);

    @Query(value = "SELECT SUM(a.amount) FROM Advance a WHERE a.status='PENDING' AND a.user.id=?1 GROUP BY a.id ORDER BY a.id ASC")
    BigDecimal getPendingAdvanceTotal(Long userId);

    @Modifying
    @Query("UPDATE Advance a SET a.status ='ACTIVE'  WHERE a.status='PENDING' AND a.user.id=?1")
    void updateAdvanceStatus(Long userId);
}
