package com.example.loginregister.controller;

import com.example.loginregister.service.TodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final TodoService todoService;

    public HomeController(TodoService todoService) {
        this.todoService = todoService;
    }

    /* ------------------------------------------------------------------ */
    /*  HOME — protected, shows the logged-in user's todos                */
    /* ------------------------------------------------------------------ */

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("todos", todoService.getTodosForUser(userId));
        return "home";
    }

    /* ------------------------------------------------------------------ */
    /*  ADD a task                                                         */
    /* ------------------------------------------------------------------ */

    @PostMapping("/todos/add")
    public String addTodo(@RequestParam(name = "title", required = false) String title,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        if (title == null || title.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Task title cannot be empty");
            return "redirect:/home";
        }
        if (title.length() > 200) {
            redirectAttributes.addFlashAttribute("error", "Task title is too long (max 200 characters)");
            return "redirect:/home";
        }

        todoService.addTodo(title, userId);
        return "redirect:/home";
    }

    /* ------------------------------------------------------------------ */
    /*  TOGGLE completed / not completed                                   */
    /* ------------------------------------------------------------------ */

    @PostMapping("/todos/{id}/toggle")
    public String toggleTodo(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        todoService.toggleTodo(id, userId);
        return "redirect:/home";
    }

    /* ------------------------------------------------------------------ */
    /*  DELETE a task                                                      */
    /* ------------------------------------------------------------------ */

    @PostMapping("/todos/{id}/delete")
    public String deleteTodo(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        todoService.deleteTodo(id, userId);
        return "redirect:/home";
    }
}