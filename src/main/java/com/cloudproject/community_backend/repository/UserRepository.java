package com.cloudproject.community_backend.repository;

import com.cloudproject.community_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // username으로 회원 찾기 (로그인에 활용)
    User findByUsername(String username);
}
