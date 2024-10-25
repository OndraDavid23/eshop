package com.eshop.my_eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eshop.my_eshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
