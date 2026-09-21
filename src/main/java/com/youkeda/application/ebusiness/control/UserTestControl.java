package com.youkeda.application.ebusiness.control;


import com.youkeda.application.ebusiness.config.LoginContext;
import com.youkeda.application.ebusiness.dataobject.UserDO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.model.User;
import com.youkeda.application.ebusiness.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    public Result<User> reg(@RequestBody User user, HttpSession session) {
        Result<User> result = userService.register(user);
        if (result.isSuccess() && result.getData() != null) {
            loginToSession(result.getData(), session);
        }
        return result;
    }

    @PostMapping(path = "/login/api")
    @ResponseBody
    public Result<User> login(@RequestBody Map<String, String> body, HttpSession session) {
        String userName = body == null ? null : body.get("userName");
        String password = body == null ? null : body.get("password");
        if (password == null && body != null) {
            password = body.get("pwd");
        }
        Result<User> result = userService.login(userName, password);
        if (result.isSuccess() && result.getData() != null) {
            loginToSession(result.getData(), session);
        }
        return result;
    }

    private void loginToSession(User user, HttpSession session) {
        UserDO userDO = new UserDO(user);
        session.setAttribute(LoginContext.SESSION_USER, userDO);
    }
}
