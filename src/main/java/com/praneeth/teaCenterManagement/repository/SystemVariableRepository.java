package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.SystemVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemVariableRepository extends JpaRepository<SystemVariable, Long> {

    Optional<SystemVariable> findFirstById(Long id);
}
