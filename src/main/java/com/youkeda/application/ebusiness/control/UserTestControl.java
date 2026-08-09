package com.youkeda.application.ebusiness.control;


import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.model.User;
import com.youkeda.application.ebusiness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserTestControl {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping(path = "/reg")
    public String register(Model model) {
        return "register";
    }

    @GetMapping(path = "/product.html")
    public String productHtml(Model model) {
        return "product-list";
    }

    @PostMapping(path = "/reg/api")
    @ResponseBody
    public Result<User> reg(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(path = "/login/api")
    @ResponseBody
    public Result<User> login(@RequestBody Map<String, String> body) {
        String userName = body == null ? null : body.get("userName");
        String password = body == null ? null : body.get("password");
        if (password == null && body != null) {
            password = body.get("pwd");
        }
        return userService.login(userName, password);
    }
}




