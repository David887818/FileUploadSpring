package com.example.demo.controller;

import com.example.demo.model.Advertisment;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repository.AdsRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdsRepository adsRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public String main( ModelMap modelMap) {
        List<Advertisment> advertisments = adsRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("category",categories);
        modelMap.addAttribute("ads",advertisments);
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {

        return "redirect:/";
    }


    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        ModelMap modelMap
    ) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user != null) {
            List<Advertisment> advertisments = adsRepository.findAll();
            List<Category> categories = categoryRepository.findAll();
            modelMap.addAttribute("category",categories);
            modelMap.addAttribute("ads",advertisments);
            return "adminPage";
        } else {
            modelMap.addAttribute("error", "Invalid Email or Password");
            return "index";
        }

    }

    @RequestMapping("/search")
    public String delete(  @RequestParam("category_id") int id,ModelMap modelMap) {
        Optional<Advertisment> byId = adsRepository.findById(id);
        List<Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("category",categories);
        modelMap.addAttribute("ads",byId);
        return "index";
    }

    @RequestMapping("/addCategory")
    public String add(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "adminPage";
    }


    @Value("${AdsSpring.pic.url}")
    private String adPicDir;

    @PostMapping("/addAds")
    public String addAdvertise(@ModelAttribute Advertisment advertisement,
                               @RequestParam("category_id") int id,
                               @RequestParam("image")MultipartFile multipartFile){
        File dir = new File(adPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        advertisement.setPicUrl(picName);
        advertisement.setCategory(categoryRepository.getOne(id));
        adsRepository.save(advertisement);
        return "adminPage";
    }

    @GetMapping(value = "/adImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(adPicDir + picUrl);
        return IOUtils.toByteArray(in);
    }

}


