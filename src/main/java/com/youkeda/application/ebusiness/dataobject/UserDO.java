package com.youkeda.application.ebusiness.dataobject;

import com.youkeda.application.ebusiness.model.BaseDate;
import com.youkeda.application.ebusiness.model.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class UserDO extends BaseDate {

    private String userName;

    private String password;

    private String mobile;

    private String email;

    private String name;

    private String gender;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserDO() {} // 👈 必须加！

    public UserDO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User convertToModel() {
       User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}
