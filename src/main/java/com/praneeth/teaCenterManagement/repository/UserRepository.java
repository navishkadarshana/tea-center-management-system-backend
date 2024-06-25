package com.praneeth.teaCenterManagement.repository;

import com.praneeth.teaCenterManagement.entity.User;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.enums.common.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT au FROM User au WHERE au.userName=?1 AND au.userRole='USER'")
    Optional<User> findActiveUserByUserNameOrEmail(String userName);

    @Query(value = "SELECT au FROM User au WHERE au.userName=?1 AND au.userRole=?2")
    Optional<User> findActiveAdminUserByUserNameOrEmail(String userName, UserRole userRole);

    @Query(value = "select * from user u WHERE " +
            "IF(?1 is not null , (u.first_name LIKE %?1%) OR (u.user_unique_id LIKE %?1%) OR (u.mobile LIKE %?1%), true) " +
            "AND IF(?2 is not null , u.status=?2, true)", nativeQuery = true)
    List<User> filterUserForAdmin(String keyword, String status);

    Optional<User> findFirstByUserName(String email);

    Optional<User> findFirstByMobile(String mobile);

    Optional<User> findFirstByNic(String nic);


    Optional<User> findUserByIdAndUserName(Long id, String email);

    @Query(value = "SELECT u FROM User u WHERE u.userRole='USER'")
    Page<User> getAllUserByStatus(Pageable pageable);

    @Query(value = "SELECT u FROM User u WHERE (u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.mobile LIKE %?1% AND u.userRole='USER')")
    Page<User> getAllUserByStatusAndEmail(String keyword, Pageable pageable);

    @Query(value = "SELECT au FROM User au WHERE au.mobile=?1 AND au.id<>?2 ")
    Optional<User> getUserByMobileNotInID(String userName, Long userId);

    @Query(value = "SELECT COUNT(DISTINCT u.id) FROM User u WHERE u.userRole='USER'")
    Long getUserCount();
}
