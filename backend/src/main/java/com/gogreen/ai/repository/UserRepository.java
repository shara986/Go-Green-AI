package com.gogreen.ai.repository;

import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByDeletedFalse();

    long countByDeletedFalseAndRolesName(UserRole role);

    long countByDeletedFalseAndRolesNameAndApprovalStatus(UserRole role, UserApprovalStatus approvalStatus);

    long countByDeletedFalseAndApprovalStatus(UserApprovalStatus approvalStatus);

    Page<User> findByRolesName(UserRole role, Pageable pageable);

    Page<User> findByRolesNameAndApprovalStatus(UserRole role, UserApprovalStatus approvalStatus, Pageable pageable);

    Page<User> findByDeletedFalse(Pageable pageable);

    @Query("select u from User u where u.deleted = false and " +
            "( :search is null or lower(u.name) like lower(concat('%', :search, '%')) " +
            "   or lower(u.username) like lower(concat('%', :search, '%')) " +
            "   or lower(u.email) like lower(concat('%', :search, '%')) ) " +
            "and ( :role is null or :role in (select r.name from u.roles r) ) " +
            "and ( :approvalStatus is null or u.approvalStatus = :approvalStatus ) " +
            "and ( :enabled is null or u.enabled = :enabled )")
    Page<User> searchUsers(@Param("search") String search,
                           @Param("role") UserRole role,
                           @Param("approvalStatus") UserApprovalStatus approvalStatus,
                           @Param("enabled") Boolean enabled,
                           Pageable pageable);

    @Query("select function('date_format', u.createdAt, '%Y-%m') as month, count(u) " +
            "from User u where u.deleted = false " +
            "group by function('date_format', u.createdAt, '%Y-%m') order by month")
    List<Object[]> countUsersByMonth();
}
