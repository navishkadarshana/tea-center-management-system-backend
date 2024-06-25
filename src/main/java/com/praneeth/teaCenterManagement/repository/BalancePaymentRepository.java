package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.BalancePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface BalancePaymentRepository extends JpaRepository<BalancePayment, Long> {
    @Query(value = "SELECT SUM(b.amount) FROM BalancePayment b WHERE b.paidStatus='PENDING' AND b.user.id=?1 GROUP BY b.id")
    BigDecimal getPendingBalanceAmount(Long userId);

    @Modifying
    @Query("UPDATE BalancePayment b SET b.paidStatus ='ACTIVE'  WHERE b.paidStatus='PENDING' AND b.user.id=?1")
    void updateBalanceStatus(Long userId);
}
