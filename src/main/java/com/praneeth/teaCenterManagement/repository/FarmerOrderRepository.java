package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.Advance;
import com.praneeth.teaCenterManagement.entity.FarmerOrder;
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
public interface FarmerOrderRepository extends JpaRepository<FarmerOrder, Long> {

    @Query(value = "SELECT fo FROM FarmerOrder fo INNER JOIN User u ON fo.user.id=u.id WHERE DATE(fo.created) BETWEEN DATE(?1) AND DATE(?2) " +
            "AND (?3 IS NULL OR (u.firstName LIKE %?3%) OR (u.lastName LIKE %?3%) OR (u.mobile LIKE %?3%)) GROUP BY fo.id ORDER BY fo.id ASC")
    Page<FarmerOrder> getOrderByDateRangAndKeyword(Date formDate, Date toDate, String keyword, Pageable pageable);

    @Query(value = "SELECT fo FROM FarmerOrder fo WHERE fo.orderPaidStatus='NOT_PAID' AND fo.user.id=?1 GROUP BY fo.id ORDER BY fo.id ASC")
    List<FarmerOrder> getPendingOrder(Long userId);

    @Query(value = "SELECT SUM(fo.totalAmount) FROM FarmerOrder fo WHERE fo.orderPaidStatus='NOT_PAID' AND fo.user.id=?1 GROUP BY fo.id")
    BigDecimal getPendingOrderAmount(Long userId);

    @Modifying
    @Query("UPDATE FarmerOrder fo SET fo.orderPaidStatus ='ACTIVE'  WHERE fo.orderPaidStatus='NOT_PAID' AND fo.user.id=?1")
    void updateOrderStatus(Long userId);

}
