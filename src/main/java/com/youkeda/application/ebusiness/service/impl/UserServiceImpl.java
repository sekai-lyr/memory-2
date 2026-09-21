package com.youkeda.application.ebusiness.service.impl;

import com.youkeda.application.ebusiness.dao.UserDAO;
import com.youkeda.application.ebusiness.dataobject.UserDO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.model.User;
import com.youkeda.application.ebusiness.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    /**
     * 注册方法：把用户信息存入数据库
     */
    @Override
    public Result<User> register(User user) {
        if (user == null) {
            return fail("400", "用户信息不能为空");
        }

        user.setUserName(trim(user.getUserName()));
        user.setPassword(trim(user.getPassword()));
        user.setMobile(trim(user.getMobile()));
        user.setEmail(trim(user.getEmail()));
        user.setName(trim(user.getName()));
        user.setGender(trim(user.getGender()));

        // 1. 非空校验（核心校验，防止空数据入库）
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return fail("400", "用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return fail("400", "密码不能为空");
        }
        if (user.getMobile() == null || user.getMobile().trim().isEmpty()) {
            return fail("400", "电话号码不能为空");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return fail("400", "姓名不能为空");
        }
        if (user.getGender() == null || user.getGender().trim().isEmpty()) {
            return fail("400", "性别不能为空");
        }

        // 2. 检查用户名是否已存在（避免重复注册）
        UserDO existUser = userDAO.findByUserName(user.getUserName());
        if (existUser != null) {
            return fail("400", "用户名已存在，请换一个用户名");
        }

        // 3. 调用 DAO 插入数据库
        UserDO userDO=new UserDO(user);
        int insertRow = userDAO.add(userDO);
        if (insertRow > 0) {
            userDO.setPassword(null);
            Result<User> result = new Result<>();
            result.setCode("200");
            result.setMessage("注册成功");
            result.setSuccess(true);
            result.setData(userDO.convertToModel());
            return result;
        } else {
            return fail("400", "数据库异常");
        }

    }

    /**
     * 登录方法：根据用户名密码查询数据库，校验登录
     */
    @Override
    public Result<User> login(String usrName, String pwd) {
        usrName = trim(usrName);
        pwd = trim(pwd);

        // 1. 非空校验
        if (usrName == null || usrName.trim().isEmpty()) {
            return fail("400", "用户名不能为空");
        }
        if (pwd == null || pwd.trim().isEmpty()) {
            return fail("400", "密码不能为空");
        }

        // 2. 根据用户名查询数据库
        UserDO dbUser = userDAO.findByUserName(usrName);
        if (dbUser == null) {
            // 用户名不存在
            return fail("400", "用户名不存在");
        }

        // 3. 校验密码是否正确
        if (!dbUser.getPassword().equals(pwd)) {
            return fail("400", "密码不正确");
        }

        // 4. 校验通过，登录成功
        Result<User> result = new Result<User>();
        result.setCode("200");
        result.setMessage("登录成功");
        result.setSuccess(true);
        dbUser.setPassword(null);
        result.setData(dbUser.convertToModel());
        return result;
    }

    private Result<User> fail(String code, String message) {
        Result<User> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
