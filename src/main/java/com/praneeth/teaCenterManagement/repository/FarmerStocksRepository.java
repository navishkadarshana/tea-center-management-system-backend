package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.dto.report.FarmerMonthlyReport;
import com.praneeth.teaCenterManagement.entity.FarmerStocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FarmerStocksRepository extends JpaRepository<FarmerStocks, Long> {

    @Query(value = "SELECT fs FROM FarmerStocks fs INNER JOIN User u ON fs.user.id=u.id WHERE DATE(fs.created) BETWEEN DATE(?1) AND DATE(?2) " +
            "AND (?3 IS NULL OR (u.firstName LIKE %?3%) OR (u.lastName LIKE %?3%) OR (u.mobile LIKE %?3%)) GROUP BY fs.id ORDER BY fs.id ASC")
    Page<FarmerStocks> getFarmerStocksByDateRangAndKeyword(Date formDate, Date toDate, String keyword, Pageable pageable);

    @Query(value = "SELECT new com.praneeth.teaCenterManagement.dto.report.FarmerMonthlyReport(u.id, u.firstName, u.lastName, u.mobile, u.nic, SUM(fs.numberOfKg), SUM(fs.netWeight), SUM (fs.totalPrice)) FROM FarmerStocks fs " +
            "INNER JOIN User u ON fs.user.id=u.id " +
            "WHERE fs.year=?1 AND fs.month=?2 " +
            "AND (?3 IS NULL OR (u.firstName LIKE %?3%) OR (u.lastName LIKE %?3%) OR (u.mobile LIKE %?3%)) AND fs.paidStatus='PENDING' GROUP BY fs.user.id ORDER BY fs.user.id ASC")
    Page<FarmerMonthlyReport> getFarmerStocksByDateRangAndKeywordForReport(int year, int month, String keyword, Pageable pageable);

    @Query(value = "SELECT fs FROM FarmerStocks fs WHERE fs.year=?1 AND fs.month=?2 AND fs.user.id=?3 AND fs.paidStatus='PENDING' GROUP BY fs.id ORDER BY fs.id ASC")
    List<FarmerStocks> getFarmerStocksByYearMonthAndIdForReport(int year, int month, Long userId);

    @Modifying
    @Query("UPDATE FarmerStocks fs SET fs.paidStatus ='ACTIVE'  WHERE fs.paidStatus='PENDING' AND fs.year=?1 AND fs.month=?2 AND fs.user.id=?3")
    void updateStockStatus(int year, int month, Long userId);

    @Query(value = "SELECT SUM(fs.netWeight) FROM FarmerStocks fs WHERE fs.month=?1")
    Long getMonthTeaKg(int month);
}
