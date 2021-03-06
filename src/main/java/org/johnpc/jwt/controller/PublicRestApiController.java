package org.johnpc.jwt.controller;

import org.johnpc.jwt.model.User;
import org.johnpc.jwt.repo.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/public")
@CrossOrigin
public class PublicRestApiController {
    private UserRepository userRepository;

    public PublicRestApiController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("test1")
    public String test1(){
        return "API Test 1";
    }

    @GetMapping("management/reports")
    public String test2(){
        return "API Test 2";
    }

    @GetMapping("admin/users")
    public List<User> users(){
        return this.userRepository.findAll();
    }
}
