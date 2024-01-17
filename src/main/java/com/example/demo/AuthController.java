package com.example.demo;

import jakarta.jws.soap.SOAPBinding;
import jakarta.validation.Valid;
import com.example.demo.UserDto;
import com.example.demo.User;
import com.example.demo.Role;
import com.example.demo.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public AuthController(UserService userService, RoleRepository roleRepository, UserRepository userRepository) {

        this.roleRepository = roleRepository;

        this.userRepository = userRepository;

        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Уже существует аккаунт с указаным Email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model, Principal principal){
        List<UserDto> users = userService.findAllUsers();
        for (UserDto user:users
             ) {
            user.getRoles().stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
        }
        if (principal != null && principal.getName() != null) {
            User user = userService.findUserByEmail(principal.getName());
            model.addAttribute("username", user.getName());
        }
        model.addAttribute("users", users);
        model.addAttribute("roles", roleRepository.findAll());
        return "users";
    }

    @PostMapping("/users/{id}/role")
    public String changeUserRole(@PathVariable("id") Long id, @RequestParam("role") String role)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));;
        Role newRole = roleRepository.findByName(role);
        user.getRoles().clear();
        user.getRoles().add(newRole);
        userRepository.save(user);
        return "redirect:/users";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }
}
