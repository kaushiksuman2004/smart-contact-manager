package com.smart.scm2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.scm2.Entities.User;
import com.smart.scm2.Helper.Message;
import com.smart.scm2.dao.UserRepository;


@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/do-register", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {

        try {
            
            if (result.hasErrors()) {
                System.out.println("ERROR : "+result.toString());
                model.addAttribute("user", user);
                return "signup";
            }
            
            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions to proceed !!");
                throw new Exception("You have not agreed the terms and conditions to proceed !!" );
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println("\n \n Agreement : " +agreement);
            System.out.println(user+" \n \n");
    
            this.userRepository.save(user);

            model.addAttribute("user", new User());

            session.setAttribute("message1", new Message("Successfully Registered !!", "alert-success"));
            return "signup";

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong !! "+ e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    // handler for custom login
    @GetMapping("/signin")
    public String customLogin(Model model)
    {
        model.addAttribute("title", "Login page");
        return "login";
    }

}
