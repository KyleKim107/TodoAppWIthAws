package com.todoAppWithAws.book.springboot.domain.posts;

import com.todoAppWithAws.book.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
