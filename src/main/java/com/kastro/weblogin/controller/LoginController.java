package com.kastro.weblogin.controller;

import com.kastro.weblogin.entity.Role;
import com.kastro.weblogin.entity.User;
import com.kastro.weblogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/")
    String index(){
        return "index";
    }

    @GetMapping("/login")
    String login(){
        return "login";
    }

    @GetMapping("/register")
    String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String registerFinish(@Valid @ModelAttribute("user") User user,BindingResult result,Model model){
        User user1 = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ADMIN"));
        user1.setUsername(user.getUsername());
        user1.setPassword(encoder.encode(user.getPassword()));
        user1.setRoles(roles);

        if (result.hasErrors()){
            model.addAttribute("errores", result.getFieldErrors());
            return "register";
        }else{
            this.userRepository.save(user1);
        }

        return "redirect:/login";
    }

    @GetMapping("/panel")
    String panel(Principal principal, Model model){
        model.addAttribute("usuario", principal.getName());
        return "panel";
    }
}
