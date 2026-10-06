package com.example.loginregister.repository;

import com.example.loginregister.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    /** All tasks belonging to one user, newest first. */
    List<Todo> findByUserIdOrderByIdDesc(Long userId);

    /**
     * Look up a task ONLY if it belongs to this user.
     * This is what stops user A from deleting user B's tasks by guessing IDs.
     */
    Optional<Todo> findByIdAndUserId(Long id, Long userId);
}