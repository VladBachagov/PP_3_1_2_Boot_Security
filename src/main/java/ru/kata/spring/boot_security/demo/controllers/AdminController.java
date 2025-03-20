package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("newUser") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin";
        }

        try {
            userService.save(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameError", e.getMessage());
            model.addAttribute("users", userService.findAll());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin";
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "edit-user";
        }

        try {
            userService.update(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameError", e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "edit-user";
        }

        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "user";
    }
}