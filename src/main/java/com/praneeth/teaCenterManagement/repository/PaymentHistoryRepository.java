package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.BalancePayment;
import com.praneeth.teaCenterManagement.entity.FarmerOrder;
import com.praneeth.teaCenterManagement.entity.PaymentHistory;
import com.praneeth.teaCenterManagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;


@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    @Query(value = "SELECT SUM(p.monthlyNetTotal) FROM PaymentHistory p WHERE p.month=?1")
    BigDecimal getMonthWithdrawal(int month);

    Optional<PaymentHistory> findFirstByYearAndMonthAndUser(int year, int month, User userId);

    @Query(value = "SELECT SUM(p.monthlyNetTotal) FROM PaymentHistory p")
    BigDecimal getTotalWithdrawal();

    @Query(value = "SELECT ph FROM PaymentHistory ph INNER JOIN User u ON ph.user.id=u.id WHERE  (?1 IS NULL OR ph.year=?1) AND (?2 IS NULL OR ph.month=?2)" +
            "AND (?3 IS NULL OR (u.firstName LIKE %?3%) OR (u.lastName LIKE %?3%) OR (u.mobile LIKE %?3%)) GROUP BY ph.id ORDER BY ph.id ASC")
    Page<PaymentHistory> filterPaymentHistory(Integer year, Integer month, String keyword, Pageable pageable);
}
