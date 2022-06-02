package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "update user u set u.balance=u.balance +:balance where u.id= :id",nativeQuery = true)
    void updateBalance(long id, Double balance);

    Boolean existsByEmail(String email);

}
