package com.example.demo.controller;

import com.example.demo.form.LoginForm;
import com.example.demo.form.SignupForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    String index(){
        return "index";
    }

    @GetMapping("/user")
    String user_login(Model model) {
        model.addAttribute("login_form", new LoginForm());
        return "login";
    }

    @PostMapping("/user")
    String user_login_post(@ModelAttribute LoginForm login_form,
                           Model model){
        String email = login_form.getEmail();
        String userType = login_form.getUserType();
        if (Objects.equals(userType, "user")){
            User specific_user = userRepository.findByEmail(email);
            if (Objects.equals(specific_user.getPassword(), login_form.getPassword())){
                model.addAttribute("login_form", login_form);
                return "user_dashboard";
            }
        }else {
            model.addAttribute("login_form", login_form);
            return "dude";
        }
        return "dude";
    }

    @GetMapping("/signup")
    String signupForm(Model model) {
        model.addAttribute("signup_form", new SignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute SignupForm signup_form,
                               Model model) {
        String userType = signup_form.getUserType();
        if (Objects.equals(userType, "user")) {
            User user = new User(
                    signup_form.getUsername(),
                    signup_form.getEmail(),
                    signup_form.getPassword()
            );
            userRepository.save(user);
            return "redirect:/user";
        }else if (Objects.equals(userType, "admin")){
            model.addAttribute("signup_form", signup_form);
            return "dude";  // TODO: Check this out.
        }
        return "Dude, You failed!"; // TODO: Check this out.
    }
}
