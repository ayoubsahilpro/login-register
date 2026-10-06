package com.example.loginregister.service;

import com.example.loginregister.model.Todo;
import com.example.loginregister.model.User;
import com.example.loginregister.repository.TodoRepository;
import com.example.loginregister.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<Todo> getTodosForUser(Long userId) {
        return todoRepository.findByUserIdOrderByIdDesc(userId);
    }

    public void addTodo(String title, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        Todo todo = new Todo();
        todo.setTitle(title.trim());
        todo.setUser(user);
        todoRepository.save(todo);
    }

    /** Returns true if the todo existed and belonged to this user. */
    public boolean toggleTodo(Long todoId, Long userId) {
        Optional<Todo> found = todoRepository.findByIdAndUserId(todoId, userId);
        if (found.isEmpty()) {
            return false;
        }
        Todo todo = found.get();
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
        return true;
    }

    /** Returns true if the todo existed and belonged to this user. */
    public boolean deleteTodo(Long todoId, Long userId) {
        Optional<Todo> found = todoRepository.findByIdAndUserId(todoId, userId);
        if (found.isEmpty()) {
            return false;
        }
        todoRepository.delete(found.get());
        return true;
    }
}