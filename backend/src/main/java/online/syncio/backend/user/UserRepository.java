package online.syncio.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User>findByEmail(String email);

    Optional<User>findByUsername(String username);

    public User findByResetPasswordToken(String token);

    List<User> findTop20ByUsernameContainingOrEmailContaining(String username, String email);

    @Modifying
    @Query("UPDATE User u SET u.status = 'ACTIVE' WHERE u.id = :id")
    void enableUser(UUID id);

    @Query("SELECT u FROM User u JOIN u.stories s WHERE s.createdDate > :createdDate")
    List<User> findAllUsersWithAtLeastOneStoryAfterCreatedDate(LocalDateTime createdDate);

    @Query("SELECT DATE(u.createdDate) as date, COUNT(u) as count " +
            "FROM User u " +
            "WHERE u.createdDate >= :startDate " +
            "GROUP BY DATE(u.createdDate)")
    List<Object[]> countNewUsersSince(@Param("startDate") LocalDateTime startDate);
}
