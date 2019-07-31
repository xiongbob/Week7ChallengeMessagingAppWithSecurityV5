package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

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
        model.addAttribute("user", userService.getUser());
        model.addAttribute("messages", messageRepository.findAll());
        if(userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "list";
    }

    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("user", userService.getUser());
        model.addAttribute("message", new Message());
        //return "messageform";
        return "HowToDoMessage";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result) {
        if (result.hasErrors()){
            //return "messageform";
            return "HowToDoMessage";
        }

        message.setUser(userService.getUser());
        messageRepository.save(message);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String processMessage(@ModelAttribute Message message,
                                 @RequestParam("file")MultipartFile file){
        if (file.isEmpty()) {
//            messageRepository.save(message);
            return "redirect:/add";
        }
        /*
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            message.setImage(uploadResult.get("url").toString());
            messageRepository.save(message);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }*/
        return "redirect:/";
    }



    @RequestMapping("/detail/{id}")
    public String showMessages(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser());
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id,  Model model) {
        model.addAttribute("user", userService.getUser());
        model.addAttribute("message", messageRepository.findById(id).get());
        //return "messageform";
        return "HowToDoMessage";
    }

    @RequestMapping("/delete/{id}")
    public String delMessages(@PathVariable("id") long id,  Model model) {
        messageRepository.deleteById(id);
        return "redirect:/";
    }
}
