package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.FarmerOrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FarmerOrderDataRepository extends JpaRepository<FarmerOrderData, Long> {

}
