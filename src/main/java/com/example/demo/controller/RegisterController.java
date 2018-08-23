package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reg")
public class RegisterController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add")
    public String add(@ModelAttribute User user) {
        userRepository.save(user);
        System.out.println(user);
        return "redirect:/regForm";
    }

    @GetMapping("/search/{id}")
    public String delete(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/regForm";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }


}
