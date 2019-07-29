package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    //add on for week 7
    @Autowired
    MessageRepository messageRepository;

    @GetMapping(value= "/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

//    @PostMapping(value = "/register", method=RequestMethod.POST)
    @PostMapping(value="/register")
    public String processRegistrationPage(@Valid
        @ModelAttribute("user") User user, BindingResult result,
                                          Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }
        else
        {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "secure";
    }

    /* comment out for week 7
    @RequestMapping("/")
    public String index(){
        return "index";
    } */

    //add on below for week 7
    @RequestMapping("/")
    public String listCourses(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        if(userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "list";
    }

    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result) {
        if (result.hasErrors()){
            return "messageform";
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessages(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id,  Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessages(@PathVariable("id") long id,  Model model) {
        messageRepository.deleteById(id);
        return "redirect:/";
    }
}
