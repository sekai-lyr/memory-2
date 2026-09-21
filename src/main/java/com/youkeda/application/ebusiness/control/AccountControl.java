package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.config.LoginContext;
import com.youkeda.application.ebusiness.dao.UserDAO;
import com.youkeda.application.ebusiness.dataobject.UserDO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountControl {

    @Autowired
    private LoginContext loginContext;

    @Autowired
    private UserDAO userDAO;

    /**
     * 按 id 查询用户公开信息（卖家展示用）
     */
    @GetMapping("/api/user/byId")
    public Result<User> byId(@RequestParam Long id) {
        Result<User> result = new Result<>();
        UserDO userDO = userDAO.findById(id);
        if (userDO == null) {
            result.setCode("404");
            result.setMessage("用户不存在");
            result.setSuccess(false);
            return result;
        }
        User user = new User();
        user.setId(userDO.getId());
        user.setUserName(userDO.getUserName());
        user.setName(userDO.getName());
        user.setGender(userDO.getGender());
        result.setCode("200");
        result.setMessage("ok");
        result.setSuccess(true);
        result.setData(user);
        return result;
    }

    /**
     * 当前登录用户
     */
    @GetMapping("/api/user/current")
    public Result<User> current() {
        Result<User> result = new Result<>();
        UserDO user = loginContext.currentUser();
        if (user == null) {
            result.setCode("401");
            result.setMessage("未登录");
            result.setSuccess(false);
            return result;
        }
        User model = user.convertToModel();
        model.setPassword(null);
        result.setCode("200");
        result.setMessage("ok");
        result.setSuccess(true);
        result.setData(model);
        return result;
    }

    /**
     * 退出登录
     */
    @PostMapping("/api/user/logout")
    public Result<Void> logout(HttpSession session) {
        Result<Void> result = new Result<>();
        if (session != null) {
            session.invalidate();
        }
        result.setCode("200");
        result.setMessage("已退出登录");
        result.setSuccess(true);
        return result;
    }
}
