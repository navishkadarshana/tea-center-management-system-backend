package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.Stock;
import com.praneeth.teaCenterManagement.enums.common.StockType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT s FROM Stock s  WHERE (?1='ALL' OR CONVERT(s.stockType, CHAR)=?1) AND (?2 IS NULL OR (s.variant LIKE %?2%))")
    Page<Stock> filterStock(String stockType, String keyword, Pageable pageable);

    @Query(value = "SELECT SUM(s.availableSize) FROM Stock s  WHERE s.stockType=?1")
    Long getTotalStock(StockType stockType);
}
