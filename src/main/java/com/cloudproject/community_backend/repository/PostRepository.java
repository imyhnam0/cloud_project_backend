package com.cloudproject.community_backend.repository;

import com.cloudproject.community_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
